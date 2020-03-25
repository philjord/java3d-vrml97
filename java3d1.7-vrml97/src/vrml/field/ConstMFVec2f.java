package vrml.field;

import vrml.ConstMField;

public class ConstMFVec2f extends ConstMField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstMFVec2f impl;

  public ConstMFVec2f(org.jdesktop.j3d.loaders.vrml97.impl.ConstMFVec2f init)
  {
    this.impl = init;
  }

  public void getValue(float[][] values)
  {
    this.impl.getValue(values);
  }

  public void getValue(float[] values)
  {
    this.impl.getValue(values);
  }

  public void get1Value(int index, float[] values)
  {
    this.impl.get1Value(index, values);
  }

  public void get1Value(int index, SFVec2f vec)
  {
    this.impl.get1Value(index, vec.impl);
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public Object clone()
  {
    return new ConstMFVec2f((org.jdesktop.j3d.loaders.vrml97.impl.ConstMFVec2f)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstMFVec2f
 * JD-Core Version:    0.6.0
 */