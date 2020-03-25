package vrml.field;

import vrml.ConstMField;

public class ConstMFFloat extends ConstMField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstMFFloat impl;

  public ConstMFFloat(org.jdesktop.j3d.loaders.vrml97.impl.ConstMFFloat init)
  {
    this.impl = init;
  }

  public void getValue(float[] values)
  {
    this.impl.getValue(values);
  }

  public float get1Value(int index)
  {
    return this.impl.get1Value(index);
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public Object clone()
  {
    return new ConstMFFloat((org.jdesktop.j3d.loaders.vrml97.impl.ConstMFFloat)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstMFFloat
 * JD-Core Version:    0.6.0
 */