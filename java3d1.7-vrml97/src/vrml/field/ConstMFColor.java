package vrml.field;

import vrml.ConstMField;

public class ConstMFColor extends ConstMField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstMFColor impl;

  public ConstMFColor(org.jdesktop.j3d.loaders.vrml97.impl.ConstMFColor init)
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

  public void get1Value(int index, SFColor vec)
  {
    this.impl.get1Value(index, vec.impl);
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public Object clone()
  {
    return new ConstMFColor((org.jdesktop.j3d.loaders.vrml97.impl.ConstMFColor)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstMFColor
 * JD-Core Version:    0.6.0
 */