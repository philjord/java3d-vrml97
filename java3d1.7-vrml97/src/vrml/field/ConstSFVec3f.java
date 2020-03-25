package vrml.field;

import vrml.ConstField;

public class ConstSFVec3f extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFVec3f impl;

  public ConstSFVec3f(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFVec3f init)
  {
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new ConstSFVec3f((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFVec3f)this.impl.clone());
  }

  public void getValue(float[] vec)
  {
    this.impl.getValue(vec);
  }

  public float[] getValue()
  {
    return this.impl.getValue();
  }

  public float getX()
  {
    return this.impl.getX();
  }

  public float getY()
  {
    return this.impl.getY();
  }

  public float getZ()
  {
    return this.impl.getZ();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFVec3f
 * JD-Core Version:    0.6.0
 */