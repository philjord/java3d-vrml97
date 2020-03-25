package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstMFTime extends ConstMField
{
  ConstMFTime(MFTime owner)
  {
    super(owner);
  }

  public ConstMFTime(double[] time)
  {
    super(new MFTime(time));
  }

  public void getValue(double[] values)
  {
    ((MFTime)this.ownerField).getValue(values);
  }

  public double get1Value(int index)
  {
    return ((MFTime)this.ownerField).get1Value(index);
  }

  public Object clone()
  {
    return new ConstMFTime((MFTime)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstMFTime(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMFTime
 * JD-Core Version:    0.6.0
 */