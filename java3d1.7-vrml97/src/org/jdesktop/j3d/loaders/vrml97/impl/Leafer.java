package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Iterator;

import org.jogamp.java3d.Background;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.BoundingLeaf;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.CapabilityNotSetException;
import org.jogamp.java3d.Clip;
import org.jogamp.java3d.Fog;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Light;
import org.jogamp.java3d.Locale;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.SceneGraphObject;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Soundscape;
import org.jogamp.java3d.ViewPlatform;
import org.jogamp.vecmath.Color3f;

public class Leafer
{
  static final int LIGHTS = 0;
  static final int BOUNDINGLEAF = 1;
  static final int VIEWPLAT = 2;
  static final int SHAPE = 3;
  static final int UNLINKABLE = 9;

  public static void clean(Locale l, int leafType)
  {
    try
    {
      Iterator<BranchGroup> e = l.getAllBranchGraphs();
      while (e.hasNext()) {
        Object o = e.next();
        if ((o instanceof Locale)) {
          clean((Locale)o, leafType);
        }
        else if ((o instanceof SceneGraphObject)) {
          clean((SceneGraphObject)o, leafType);
        }
        else
          System.out.println(o + " unknown and in tree");
      }
    }
    catch (CapabilityNotSetException e)
    {
    }
  }

  public static void clean(SceneGraphObject sgo, int leafType)
  {
    if ((sgo instanceof Group)) {
      try {
        Iterator<Node> e = ((Group)sgo).getAllChildren();
        while (e.hasNext()) {
          clean((SceneGraphObject)(SceneGraphObject)e.next(), leafType);
        }
      }
      catch (CapabilityNotSetException e)
      {
      }
    }
    else
      switch (leafType) {
      case 0:
        if (!(sgo instanceof Light)) break;
        ((Light)sgo).setEnable(false);

        break;
      }
  }

  public static boolean has(Locale l, int leafType)
  {
    try
    {
      Iterator<BranchGroup> e = l.getAllBranchGraphs();
      while (e.hasNext()) {
        Object o = e.next();
        if ((o instanceof Locale)) {
          return has((Locale)o, leafType);
        }
        if ((o instanceof SceneGraphObject)) {
          return has((SceneGraphObject)o, leafType);
        }

        System.out.println(o + " unknown and in tree");
      }
    }
    catch (CapabilityNotSetException e)
    {
    }

    return false;
  }

  public static boolean has(SceneGraphObject sgo, int leafType)
  {
    if ((sgo instanceof Group)) {
      try {
        Iterator<Node> e = ((Group)sgo).getAllChildren();
        boolean hasIt = false;
        while (e.hasNext()) {
          hasIt |= has((SceneGraphObject)(SceneGraphObject)e.next(), leafType);
        }
        return hasIt;
      }
      catch (CapabilityNotSetException e)
      {
        ((Group)sgo).setCapability(12);
        return has(sgo, leafType);
      }
    }

    switch (leafType) {
    case 0:
      if (!(sgo instanceof Light)) break;
      Color3f c = new Color3f();
      ((Light)sgo).getColor(c);
      System.out.println(c);
      System.out.println(sgo + " found");
      return true;
    case 1:
      if (!(sgo instanceof BoundingLeaf)) break;
      return true;
    case 2:
      if (!(sgo instanceof ViewPlatform)) break;
      return true;
    case 3:
      if (!(sgo instanceof Shape3D)) break;
      return true;
    case 9:
      return sgo instanceof Background | sgo instanceof Behavior | sgo instanceof Clip | sgo instanceof Fog | sgo instanceof Soundscape | sgo instanceof ViewPlatform;
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    }

    return false;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Leafer
 * JD-Core Version:    0.6.0
 */