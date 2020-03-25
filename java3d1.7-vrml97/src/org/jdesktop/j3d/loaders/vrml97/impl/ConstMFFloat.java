package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstMFFloat extends ConstMField
{
  ConstMFFloat(MFFloat owner)
  {
    super(owner);
  }

  public ConstMFFloat(float[] values)
  {
    super(new MFFloat(values));
  }

  public void getValue(float[] values)
  {
    ((MFFloat)this.ownerField).getValue(values);
  }

  public float get1Value(int index)
  {
    return ((MFFloat)this.ownerField).get1Value(index);
  }

  public Object clone()
  {
    return new ConstMFFloat((MFFloat)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstMFFloat(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMFFloat
 * JD-Core Version:    0.6.0
 */