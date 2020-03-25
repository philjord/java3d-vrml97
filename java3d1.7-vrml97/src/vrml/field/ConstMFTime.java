package vrml.field;

import vrml.ConstMField;

public class ConstMFTime extends ConstMField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstMFTime impl;

  public ConstMFTime(org.jdesktop.j3d.loaders.vrml97.impl.ConstMFTime init)
  {
    this.impl = init;
  }

  public void getValue(double[] values)
  {
    this.impl.getValue(values);
  }

  public double get1Value(int index)
  {
    return this.impl.get1Value(index);
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public Object clone()
  {
    return new ConstMFTime((org.jdesktop.j3d.loaders.vrml97.impl.ConstMFTime)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstMFTime
 * JD-Core Version:    0.6.0
 */