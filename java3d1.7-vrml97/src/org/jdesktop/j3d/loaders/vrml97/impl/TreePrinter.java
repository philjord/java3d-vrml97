package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.CapabilityNotSetException;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Link;
import org.jogamp.java3d.Locale;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.SceneGraphObject;
import org.jogamp.java3d.SharedGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;

public class TreePrinter
{
  PrintStream printStream;
  String j3dPkg = new String("org.jogamp.java3d.");
  String v97Pkg = new String("org.jdesktop.j3d.loaders.vrml97.impl.");

  public void print(PrintStream s, Locale l)
  {
    this.printStream = s;
    HashSet sharedGroups = new HashSet();
    printTree(l, 0, sharedGroups);
    Iterator iterator = sharedGroups.iterator();
    while (iterator.hasNext()) {
      SharedGroup sg = (SharedGroup)iterator.next();
      print(s, sg);
    }
  }

  public void print(Locale l)
  {
    print(System.out, l);
  }

  private void printTree(Locale l, int graphDepth, Set sharedGroups)
  {
    printNode(l, 0, sharedGroups);
    try {
      Iterator<BranchGroup> e = l.getAllBranchGraphs();
      while (e.hasNext()) {
        Object o = e.next();
        if ((o instanceof Locale)) {
          printTree((Locale)o, graphDepth + 1, sharedGroups);
        }
        else if ((o instanceof SceneGraphObject)) {
          printTree((SceneGraphObject)o, graphDepth + 1, sharedGroups);
        }
        else
          this.printStream.println(o + " unknown and in tree");
      }
    }
    catch (CapabilityNotSetException e)
    {
      this.printStream.println("No capability to read children");
    }
  }

  public void print(PrintStream s, SceneGraphObject sgo)
  {
    this.printStream = s;
    HashSet sharedGroups = new HashSet();
    printTree(sgo, 0, sharedGroups);
    Iterator iterator = sharedGroups.iterator();
    while (iterator.hasNext()) {
      SharedGroup sg = (SharedGroup)iterator.next();
      print(s, sg);
    }
  }

  public void print(SceneGraphObject sgo)
  {
    print(System.out, sgo);
  }

  private void printTree(SceneGraphObject sgo, int graphDepth, Set sharedGroups)
  {
    printNode(sgo, graphDepth, sharedGroups);
    if ((sgo instanceof Group))
      try {
        Iterator<Node> e = ((Group)sgo).getAllChildren();
        while (e.hasNext())
          printTree((SceneGraphObject)(SceneGraphObject)e.next(), graphDepth + 1, sharedGroups);
      }
      catch (CapabilityNotSetException e)
      {
      }
  }

  private String nodeString(Object o)
  {
    String objString = o.toString();
    if (objString.startsWith(this.j3dPkg)) {
      objString = objString.substring(this.j3dPkg.length());
    }
    if (objString.startsWith(this.v97Pkg)) {
      objString = objString.substring(this.v97Pkg.length());
    }
    return objString;
  }

  private void printNode(Object o, int indent, Set sharedGroups)
  {
    for (int i = 0; i < indent; i++) {
      this.printStream.print(">");
    }
    this.printStream.print(nodeString(o) + ": ");
    if ((o instanceof SceneGraphObject)) {
      SceneGraphObject sgo = (SceneGraphObject)o;
      int capBits = 0;

      for (int i = 0; i < 64; i++) {
        if (sgo.getCapability(i)) {
          capBits |= 1 << i;
        }
      }
      this.printStream.print("capBits:Ox" + Integer.toHexString(capBits));
      if ((o instanceof Group)) {
        Group g = (Group)o;
        int numChildren = 0;
        try {
          numChildren = g.numChildren();
        }
        catch (CapabilityNotSetException e)
        {
          g.setCapability(12);
          numChildren = g.numChildren();
        }

        this.printStream.print(" children:" + numChildren);
        if ((o instanceof TransformGroup)) {
          Transform3D transform = new Transform3D();
          Transform3D identity = new Transform3D();
          TransformGroup t = (TransformGroup)o;
          t.getTransform(transform);

          if (transform.equals(identity)) {
            this.printStream.print(" xform:IDENTITY ");
          }
          else {
            this.printStream.print(" xform:NON-IDENTITY ");
          }
        }
      }
      else if ((o instanceof Link)) {
        Link l = (Link)o;
        SharedGroup sg = l.getSharedGroup();
        this.printStream.print(" sg:" + nodeString(sg));
        sharedGroups.add(sg);
      }
      else {
        this.printStream.print(": leaf");
      }
    }
    this.printStream.println();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.TreePrinter
 * JD-Core Version:    0.6.0
 */