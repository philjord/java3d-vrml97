package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFVec2f extends ConstField
{
  ConstSFVec2f(SFVec2f owner)
  {
    super(owner);
  }

  ConstSFVec2f(float[] values)
  {
    super(new SFVec3f(values));
  }

  public void getValue(float[] vec)
  {
    ((SFVec2f)this.ownerField).getValue(vec);
  }

  public float[] getValue()
  {
    return ((SFVec2f)this.ownerField).getValue();
  }

  public float getX()
  {
    return ((SFVec2f)this.ownerField).getX();
  }

  public float getY()
  {
    return ((SFVec2f)this.ownerField).getY();
  }

  public synchronized Object clone()
  {
    return new ConstSFVec2f((SFVec2f)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFVec2f(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFVec2f
 * JD-Core Version:    0.6.0
 */