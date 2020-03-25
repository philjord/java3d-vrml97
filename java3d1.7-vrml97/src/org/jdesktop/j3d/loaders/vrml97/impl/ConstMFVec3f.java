package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstMFVec3f extends ConstMField
{
  ConstMFVec3f(MFVec3f owner)
  {
    super(owner);
  }

  public ConstMFVec3f(float[][] values)
  {
    super(new MFVec3f(values));
  }

  public ConstMFVec3f(int size, float[] values)
  {
    super(new MFVec3f(size, values));
  }

  public void getValue(float[][] values)
  {
    ((MFVec3f)this.ownerField).getValue(values);
  }

  public void getValue(float[] values)
  {
    ((MFVec3f)this.ownerField).getValue(values);
  }

  public void get1Value(int index, float[] values)
  {
    ((MFVec3f)this.ownerField).get1Value(index, values);
  }

  public void get1Value(int index, SFVec3f vec)
  {
    ((MFVec3f)this.ownerField).get1Value(index, vec);
  }

  public synchronized Object clone()
  {
    return new ConstMFVec3f((MFVec3f)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstMFVec3f(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMFVec3f
 * JD-Core Version:    0.6.0
 */