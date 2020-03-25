package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFColor extends ConstField
{
  ConstSFColor(SFColor owner)
  {
    super(owner);
  }

  public ConstSFColor(float red, float green, float blue)
  {
    super(new SFColor(red, green, blue));
  }

  public ConstSFColor(float[] initColor)
  {
    super(new SFColor(initColor));
  }

  public void getValue(float[] colr)
  {
    ((SFColor)this.ownerField).getValue(colr);
  }

  public float[] getValue()
  {
    return ((SFColor)this.ownerField).getValue();
  }

  public float getRed()
  {
    return ((SFColor)this.ownerField).getRed();
  }

  public float getGreen()
  {
    return ((SFColor)this.ownerField).getGreen();
  }

  public float getBlue()
  {
    return ((SFColor)this.ownerField).getBlue();
  }

  public synchronized Object clone()
  {
    return new ConstSFColor((SFColor)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFColor(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFColor
 * JD-Core Version:    0.6.0
 */