package vrml.field;

import vrml.ConstMField;

public class ConstMFInt32 extends ConstMField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstMFInt32 impl;

  public ConstMFInt32(org.jdesktop.j3d.loaders.vrml97.impl.ConstMFInt32 init)
  {
    this.impl = init;
  }

  public void getValue(int[] values)
  {
    this.impl.getValue(values);
  }

  public int get1Value(int index)
  {
    return this.impl.get1Value(index);
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public Object clone()
  {
    return new ConstMFInt32((org.jdesktop.j3d.loaders.vrml97.impl.ConstMFInt32)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstMFInt32
 * JD-Core Version:    0.6.0
 */