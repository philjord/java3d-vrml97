package org.jdesktop.j3d.loaders.vrml97.impl;

public class SFVec3f extends Field
{
  float[] value = new float[3];
  float[] initValue = new float[3];

  private void setInit()
  {
    this.initValue[0] = this.value[0];
    this.initValue[1] = this.value[1];
    this.initValue[2] = this.value[2];
  }

  public SFVec3f(float[] setVal)
  {
    setValue(setVal);
    setInit();
  }

  public SFVec3f(float x, float y, float z)
  {
    this.value[0] = x;
    this.value[1] = y;
    this.value[2] = z;
    setInit();
  }

  public SFVec3f()
  {
    this.value[0] = 0.0F;
    this.value[1] = 0.0F;
    this.value[2] = 0.0F;
    setInit();
  }

  public void reset()
  {
    this.value[0] = this.initValue[0];
    this.value[1] = this.initValue[1];
    this.value[2] = this.initValue[2];
  }

  public void getValue(float[] vec)
  {
    vec[0] = this.value[0];
    vec[1] = this.value[1];
    vec[2] = this.value[2];
  }

  public float getX()
  {
    return this.value[0];
  }

  public float getY()
  {
    return this.value[1];
  }

  public float getZ()
  {
    return this.value[2];
  }

  public float[] getValue()
  {
    return this.value;
  }

  public void setValue(float[] v)
  {
    this.value[0] = v[0];
    this.value[1] = v[1];
    this.value[2] = v[2];
    route();
  }

  public void setValue(float x, float y, float z)
  {
    this.value[0] = x;
    this.value[1] = y;
    this.value[2] = z;

    route();
  }

  public void setValue(ConstSFVec3f v)
  {
    setValue(((SFVec3f)v.ownerField).value);
  }

  public void setValue(SFVec3f v)
  {
    setValue(v.value);
  }

  public synchronized Object clone()
  {
    return new SFVec3f(this.value);
  }

  synchronized void update(Field field)
  {
    setValue(((SFVec3f)field).value);
  }

  synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFVec3f(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFVec3f(this);
  }

  public String toString()
  {
    return this.value[0] + " " + this.value[1] + " " + this.value[2] + "\n";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f
 * JD-Core Version:    0.6.0
 */