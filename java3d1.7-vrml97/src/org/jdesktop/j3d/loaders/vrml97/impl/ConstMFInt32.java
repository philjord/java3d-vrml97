package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstMFInt32 extends ConstMField
{
  ConstMFInt32(MFInt32 owner)
  {
    super(owner);
  }

  ConstMFInt32(int[] values)
  {
    super(new MFInt32(values));
  }

  public void getValue(int[] values)
  {
    ((MFInt32)this.ownerField).getValue(values);
  }

  public int get1Value(int index)
  {
    return ((MFInt32)this.ownerField).get1Value(index);
  }

  public Object clone()
  {
    return new ConstMFInt32((MFInt32)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstMFInt32(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMFInt32
 * JD-Core Version:    0.6.0
 */