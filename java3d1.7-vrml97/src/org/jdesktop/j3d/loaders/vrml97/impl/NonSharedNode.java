package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Link;
import org.jogamp.java3d.SharedGroup;

public abstract class NonSharedNode extends Node
{
  SharedGroup sharedGroup = null;
  boolean linkTested = false;
  boolean linkable = true;

  NonSharedNode(Loader loader)
  {
    super(loader);
  }

  public void registerUse(Scene scene)
  {
    if (this.loader.debug) {
      System.out.println("Use of non-sharable tree " + this.defName + " = " + toStringId() + " impl = " + this.implNode);
    }

    if (!this.linkTested) {
      if (Leafer.has(this.implNode, 9)) {
        this.linkable = false;
      }
      if (this.loader.debug) {
        System.out.println("Tested, linkable = " + this.linkable);
      }
      this.linkTested = true;
    }
    if ((this.linkable) && 
      (this.sharedGroup == null))
    {
      this.sharedGroup = new SharedGroup();
      scene.addSharedGroup(this.sharedGroup);
      org.jogamp.java3d.Node parent;
      if ((parent = this.implNode.getParent()) != null) {
        Group parentGroup = (Group)parent;

        boolean found = false;
        for (int i = 0; i < parentGroup.numChildren(); i++) {
          org.jogamp.java3d.Node child = parentGroup.getChild(i);

          if (child == this.implNode) {
            found = true;
            Link link = new Link(this.sharedGroup);
            parentGroup.setChild(link, i);
            if (this.loader.debug) {
              System.out.println("Updated reference to " + this.implNode + " in parent " + parentGroup + " to link " + link);
            }
          }

        }

        if ((this.loader.debug) && (!found)) {
          System.out.println("Could not find " + this.implNode + " in parent " + parentGroup);
        }

      }

      this.sharedGroup.addChild(this.implNode);
      if (this.loader.debug)
        System.out.println("nonShared tree: " + toStringId() + " is now in SharedGroup " + this.sharedGroup);
    }
  }

  public org.jogamp.java3d.Node getImplNode()
  {
    if ((this.linkTested) && (!this.linkable)) {
      if (this.implNode.getParent() == null) {
        return this.implNode;
      }

      if (this.loader.debug) {
        System.out.println("cloning a non linkable subtree:" + this.implNode);
      }

      org.jogamp.java3d.Node clone = this.implNode.cloneTree(false, true);
      if (this.implNode.getUserData() != null) {
        clone.setUserData(this.implNode.getUserData());
      }
      if (this.loader.debug) {
        System.out.println("cloning is: " + clone);
      }
      return clone;
    }

    if (this.sharedGroup != null) {
      return new Link(this.sharedGroup);
    }

    return this.implNode;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.NonSharedNode
 * JD-Core Version:    0.6.0
 */