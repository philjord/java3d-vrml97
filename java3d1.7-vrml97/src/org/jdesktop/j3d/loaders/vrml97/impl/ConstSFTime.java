package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFTime extends ConstField
{
  ConstSFTime(SFTime owner)
  {
    super(owner);
  }

  ConstSFTime(double time)
  {
    super(new SFTime(time));
  }

  public double getValue()
  {
    return ((SFTime)this.ownerField).getValue();
  }

  public synchronized Object clone()
  {
    return new ConstSFTime((SFTime)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFTime(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFTime
 * JD-Core Version:    0.6.0
 */