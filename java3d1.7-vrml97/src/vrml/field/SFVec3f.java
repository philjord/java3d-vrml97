package vrml.field;

import vrml.Field;

public class SFVec3f extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f impl;

  public SFVec3f(org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFVec3f((org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f)this.impl.clone());
  }

  public SFVec3f(float[] values)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f(values);
    this.implField = this.impl;
  }

  public SFVec3f(float x, float y, float z)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f(x, y, z);
    this.implField = this.impl;
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

  public void setValue(float[] v)
  {
    this.impl.setValue(v);
  }

  public void setValue(float x, float y, float z)
  {
    this.impl.setValue(x, y, z);
  }

  public void setValue(ConstSFVec3f v)
  {
    this.impl.setValue(v.impl);
  }

  public void setValue(SFVec3f v)
  {
    this.impl.setValue(v.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFVec3f
 * JD-Core Version:    0.6.0
 */