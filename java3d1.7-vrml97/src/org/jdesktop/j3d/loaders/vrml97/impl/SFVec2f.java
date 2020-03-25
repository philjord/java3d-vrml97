package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;

public class SFVec2f extends Field
{
  float[] vec2f = new float[2];

  public SFVec2f(float[] values)
  {
    setValue(values);
  }

  public SFVec2f()
  {
  }

  public SFVec2f(float x, float y)
  {
    this.vec2f[0] = x;
    this.vec2f[1] = y;
  }

  public void getValue(float[] vec)
  {
    System.arraycopy(this.vec2f, 0, vec, 0, 2);
  }

  public float[] getValue()
  {
    return this.vec2f;
  }

  public float getX()
  {
    return this.vec2f[0];
  }

  public float getY()
  {
    return this.vec2f[1];
  }

  public void setValue(float[] v)
  {
    try
    {
      System.arraycopy(v, 0, this.vec2f, 0, 2);
      route();
    }
    catch (Exception e) {
      System.err.println("You need to instance enough float to get by ");
    }
  }

  public void setValue(float x, float y)
  {
    this.vec2f[0] = x;
    this.vec2f[1] = y;
    route();
  }

  public void setValue(ConstSFVec2f v)
  {
    setValue((SFVec2f)v.ownerField);
  }

  public void setValue(SFVec2f v)
  {
    setValue(v.getValue());
  }

  public synchronized Object clone()
  {
    return new SFVec2f(this.vec2f);
  }

  public void update(Field field)
  {
    setValue((SFVec2f)field);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFVec2f(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFVec2f(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFVec2f
 * JD-Core Version:    0.6.0
 */