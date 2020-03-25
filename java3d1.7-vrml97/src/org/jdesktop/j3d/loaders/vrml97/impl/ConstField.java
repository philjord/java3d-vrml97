package org.jdesktop.j3d.loaders.vrml97.impl;

public abstract class ConstField extends Field
{
  protected Field ownerField;

  ConstField(Field ownerField)
  {
    this.ownerField = ownerField;
  }

  void route()
  {
  }

  ConstField constify()
  {
    return this;
  }

  void update(Field field)
  {
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstField
 * JD-Core Version:    0.6.0
 */