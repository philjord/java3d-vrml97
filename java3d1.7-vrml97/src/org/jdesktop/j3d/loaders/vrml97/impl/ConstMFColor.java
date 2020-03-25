package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstMFColor extends ConstMField
{
  ConstMFColor(MFColor owner)
  {
    super(owner);
  }

  public ConstMFColor(float[][] values)
  {
    super(new MFColor(values));
  }

  public ConstMFColor(int size, float[] values)
  {
    super(new MFColor(size, values));
  }

  public void getValue(float[][] values)
  {
    ((MFColor)this.ownerField).getValue(values);
  }

  public void getValue(float[] values)
  {
    ((MFColor)this.ownerField).getValue(values);
  }

  public void get1Value(int index, float[] values)
  {
    ((MFColor)this.ownerField).get1Value(index, values);
  }

  public void get1Value(int index, SFColor vec)
  {
    ((MFColor)this.ownerField).get1Value(index, vec);
  }

  public Object clone()
  {
    return new ConstMFColor((MFColor)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstMFColor(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMFColor
 * JD-Core Version:    0.6.0
 */