package vrml.field;

import vrml.Field;

public class SFVec2f extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFVec2f impl;

  public SFVec2f(org.jdesktop.j3d.loaders.vrml97.impl.SFVec2f init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFVec2f((org.jdesktop.j3d.loaders.vrml97.impl.SFVec2f)this.impl.clone());
  }

  public SFVec2f(float[] values)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFVec2f(values);
    this.implField = this.impl;
  }

  public SFVec2f(float x, float y)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFVec2f(x, y);
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

  public void setValue(float[] v)
  {
    this.impl.setValue(v);
  }

  public void setValue(float x, float y)
  {
    this.impl.setValue(x, y);
  }

  public void setValue(ConstSFVec2f v)
  {
    this.impl.setValue(v.impl);
  }

  public void setValue(SFVec2f v)
  {
    this.impl.setValue(v.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFVec2f
 * JD-Core Version:    0.6.0
 */