package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.Link;
import org.jogamp.java3d.SharedGroup;
import vrml.BaseNode;

public abstract class Light extends Node
{
  SharedGroup sharedGroup;
  AmbientLight ambLight;
  org.jogamp.java3d.Light light;

  Light(Loader loader)
  {
    super(loader);
    loader.addLight(this);
  }

  public AmbientLight getAmbientLight()
  {
    return this.ambLight;
  }

  public org.jogamp.java3d.Light getLight()
  {
    return this.light;
  }

  public org.jogamp.java3d.Node getImplNode()
  {
    return new Link(this.sharedGroup);
  }

  public BaseNode wrap()
  {
    return new org.jdesktop.j3d.loaders.vrml97.node.Light(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Light
 * JD-Core Version:    0.6.0
 */