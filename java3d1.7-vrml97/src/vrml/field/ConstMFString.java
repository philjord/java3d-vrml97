package vrml.field;

import vrml.ConstMField;

public class ConstMFString extends ConstMField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstMFString impl;

  public ConstMFString(org.jdesktop.j3d.loaders.vrml97.impl.ConstMFString init)
  {
    this.impl = init;
  }

  public void getValue(String[] values)
  {
    this.impl.getValue(values);
  }

  public String get1Value(int index)
  {
    return get1Value(index);
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public Object clone()
  {
    return new ConstMFString((org.jdesktop.j3d.loaders.vrml97.impl.ConstMFString)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstMFString
 * JD-Core Version:    0.6.0
 */