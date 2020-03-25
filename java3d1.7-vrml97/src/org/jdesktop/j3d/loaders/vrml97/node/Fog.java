package org.jdesktop.j3d.loaders.vrml97.node;

import org.jogamp.java3d.BranchGroup;
import vrml.node.Node;

public class Fog extends Node
{
  org.jdesktop.j3d.loaders.vrml97.impl.Fog impl;

  public Fog(org.jdesktop.j3d.loaders.vrml97.impl.Fog init)
  {
    super(init);
    this.impl = init;
  }

  public org.jogamp.java3d.Fog getFogImpl()
  {
    BranchGroup bg = this.impl.getFogImpl();
    return (org.jogamp.java3d.Fog)bg.getChild(0);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.node.Fog
 * JD-Core Version:    0.6.0
 */