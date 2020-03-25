package org.jdesktop.j3d.loaders.vrml97.node;

import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.ViewPlatform;
import vrml.node.Node;

public class Viewpoint extends Node
{
  org.jdesktop.j3d.loaders.vrml97.impl.Viewpoint impl;

  public Viewpoint(org.jdesktop.j3d.loaders.vrml97.impl.Viewpoint init)
  {
    super(init);
    this.impl = init;
  }

  public ViewPlatform getViewPlatform()
  {
    return this.impl.getViewPlatform();
  }

  public TransformGroup getTransformGroup()
  {
    return this.impl.getTransformGroup();
  }

  public float getFOV()
  {
    return this.impl.getFOV();
  }

  public String getDescription()
  {
    return this.impl.getDescription();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.node.Viewpoint
 * JD-Core Version:    0.6.0
 */