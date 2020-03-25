package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFInt32 extends ConstField
{
  ConstSFInt32(SFInt32 owner)
  {
    super(owner);
  }

  public ConstSFInt32(int value)
  {
    super(new SFInt32(value));
  }

  public int getValue()
  {
    return ((SFInt32)this.ownerField).getValue();
  }

  public synchronized Object clone()
  {
    return new ConstSFInt32((SFInt32)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFInt32(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFInt32
 * JD-Core Version:    0.6.0
 */