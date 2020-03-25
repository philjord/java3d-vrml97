package vrml.field;

import vrml.ConstMField;

public class ConstMFRotation extends ConstMField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstMFRotation impl;

  public ConstMFRotation(org.jdesktop.j3d.loaders.vrml97.impl.ConstMFRotation init)
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

  public void get1Value(int index, SFRotation vec)
  {
    this.impl.get1Value(index, vec.impl);
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public Object clone()
  {
    return new ConstMFRotation((org.jdesktop.j3d.loaders.vrml97.impl.ConstMFRotation)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstMFRotation
 * JD-Core Version:    0.6.0
 */