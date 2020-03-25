package org.jdesktop.j3d.loaders.vrml97.node;

import org.jogamp.java3d.AmbientLight;
import vrml.node.Node;

public class Light extends Node
{
  org.jdesktop.j3d.loaders.vrml97.impl.Light impl;

  public Light(org.jdesktop.j3d.loaders.vrml97.impl.Light init)
  {
    super(init);
    this.impl = init;
  }

  public AmbientLight getAmbientLight()
  {
    return this.impl.getAmbientLight();
  }

  public org.jogamp.java3d.Light getLight()
  {
    return this.impl.getLight();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.node.Light
 * JD-Core Version:    0.6.0
 */