package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstMFRotation extends ConstMField
{
  ConstMFRotation(MFRotation owner)
  {
    super(owner);
  }

  public ConstMFRotation(float[][] values)
  {
    super(new MFRotation(values));
  }

  public ConstMFRotation(float[] values)
  {
    super(new MFRotation(values));
  }

  public ConstMFRotation(int size, float[] values)
  {
    super(new MFRotation(size, values));
  }

  public void getValue(float[][] values)
  {
    ((MFRotation)this.ownerField).getValue(values);
  }

  public void getValue(float[] values)
  {
    ((MFRotation)this.ownerField).getValue(values);
  }

  public void get1Value(int index, float[] values)
  {
    ((MFRotation)this.ownerField).get1Value(index, values);
  }

  public void get1Value(int index, SFRotation vec)
  {
    ((MFRotation)this.ownerField).get1Value(index, vec);
  }

  public Object clone()
  {
    return new ConstMFRotation((MFRotation)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstMFRotation(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMFRotation
 * JD-Core Version:    0.6.0
 */