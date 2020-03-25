package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstMFString extends ConstMField
{
  ConstMFString(MFString owner)
  {
    super(owner);
  }

  ConstMFString(String[] s)
  {
    super(new MFString(s));
  }

  public void getValue(String[] values)
  {
    ((MFString)this.ownerField).getValue(values);
  }

  public String get1Value(int index)
  {
    return ((MFString)this.ownerField).get1Value(index);
  }

  public Object clone()
  {
    return new ConstMFString((MFString)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstMFString(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMFString
 * JD-Core Version:    0.6.0
 */