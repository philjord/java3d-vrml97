package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.utils.geometry.Primitive;

public abstract class GroupGeom extends Geometry
{
  Primitive implGroup;

  public GroupGeom(Loader loader)
  {
    super(loader);
  }

  public abstract Group initGroupImpl(Appearance paramAppearance);

  public int getNumTris()
  {
    return this.implGroup.getNumTriangles();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.GroupGeom
 * JD-Core Version:    0.6.0
 */