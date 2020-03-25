package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.BoundingBox;

public abstract class Geometry extends Node
{
  Geometry(Loader loader)
  {
    super(loader);
  }

  abstract org.jogamp.java3d.Geometry getImplGeom();

  abstract boolean haveTexture();

  abstract BoundingBox getBoundingBox();
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Geometry
 * JD-Core Version:    0.6.0
 */