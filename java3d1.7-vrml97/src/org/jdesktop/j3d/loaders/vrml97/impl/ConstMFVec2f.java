package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstMFVec2f extends ConstMField
{
  ConstMFVec2f(MFVec2f owner)
  {
    super(owner);
  }

  public ConstMFVec2f(float[][] values)
  {
    super(new MFVec2f(values));
  }

  public ConstMFVec2f(int size, float[] values)
  {
    super(new MFVec2f(size, values));
  }

  public void getValue(float[][] values)
  {
    ((MFVec2f)this.ownerField).getValue(values);
  }

  public void getValue(float[] values)
  {
    ((MFVec2f)this.ownerField).getValue(values);
  }

  public void get1Value(int index, float[] values)
  {
    ((MFVec2f)this.ownerField).get1Value(index, values);
  }

  public void get1Value(int index, SFVec2f vec)
  {
    ((MFVec2f)this.ownerField).get1Value(index, vec);
  }

  public synchronized Object clone()
  {
    return new ConstMFVec2f((MFVec2f)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstMFVec2f(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMFVec2f
 * JD-Core Version:    0.6.0
 */