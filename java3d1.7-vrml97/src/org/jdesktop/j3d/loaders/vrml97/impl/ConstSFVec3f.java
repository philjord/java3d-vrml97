package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFVec3f extends ConstField
{
  ConstSFVec3f(SFVec3f owner)
  {
    super(owner);
  }

  public ConstSFVec3f(float[] values)
  {
    super(new SFVec3f(values));
  }

  public void getValue(float[] vec)
  {
    ((SFVec3f)this.ownerField).getValue(vec);
  }

  public float[] getValue()
  {
    return ((SFVec3f)this.ownerField).getValue();
  }

  public float getX()
  {
    return ((SFVec3f)this.ownerField).getX();
  }

  public float getY()
  {
    return ((SFVec3f)this.ownerField).getY();
  }

  public float getZ()
  {
    return ((SFVec3f)this.ownerField).getZ();
  }

  public Object clone()
  {
    return new ConstSFVec3f((SFVec3f)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFVec3f(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFVec3f
 * JD-Core Version:    0.6.0
 */