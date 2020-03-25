package org.jdesktop.j3d.loaders.vrml97.impl;

public abstract class ConstMField extends ConstField
{
  ConstMField(Field owner)
  {
    super(owner);
  }

  public int getSize()
  {
    return ((MField)this.ownerField).getSize();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMField
 * JD-Core Version:    0.6.0
 */