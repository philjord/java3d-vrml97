package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFFloat extends ConstField
{
  ConstSFFloat(SFFloat owner)
  {
    super(owner);
  }

  public ConstSFFloat(float f)
  {
    super(new SFFloat(f));
  }

  public float getValue()
  {
    return ((SFFloat)this.ownerField).getValue();
  }

  public synchronized Object clone()
  {
    return new ConstSFFloat((SFFloat)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFFloat(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFFloat
 * JD-Core Version:    0.6.0
 */