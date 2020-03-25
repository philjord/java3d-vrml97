package org.jdesktop.j3d.loaders.vrml97.node;

import org.jogamp.java3d.BranchGroup;
import vrml.node.Node;

public class Background extends Node
{
  org.jdesktop.j3d.loaders.vrml97.impl.Background impl;

  public Background(org.jdesktop.j3d.loaders.vrml97.impl.Background init)
  {
    super(init);
    this.impl = init;
  }

  public org.jogamp.java3d.Background getBackgroundImpl()
  {
    BranchGroup bg = this.impl.getBackgroundImpl();
    return (org.jogamp.java3d.Background)bg.getChild(0);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.node.Background
 * JD-Core Version:    0.6.0
 */