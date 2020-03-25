package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFRotation extends ConstField
{
  ConstSFRotation(SFRotation owner)
  {
    super(owner);
  }

  public ConstSFRotation(float[] axisAngle)
  {
    super(new SFRotation(axisAngle));
  }

  public void getValue(float[] axisAngle)
  {
    ((SFRotation)this.ownerField).getValue(axisAngle);
  }

  public float[] getValue()
  {
    return ((SFRotation)this.ownerField).getValue();
  }

  public Object clone()
  {
    return new ConstSFRotation((SFRotation)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFRotation(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFRotation
 * JD-Core Version:    0.6.0
 */