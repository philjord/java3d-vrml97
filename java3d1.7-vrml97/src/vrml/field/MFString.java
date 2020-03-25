package vrml.field;

import vrml.MField;

public class MFString extends MField
{
  org.jdesktop.j3d.loaders.vrml97.impl.MFString impl;

  public MFString(org.jdesktop.j3d.loaders.vrml97.impl.MFString init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new MFString((org.jdesktop.j3d.loaders.vrml97.impl.MFString)this.impl.clone());
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public void clear()
  {
    this.impl.clear();
  }

  public void delete(int i)
  {
    this.impl.delete(i);
  }

  public MFString(String[] s)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFString(s);
    this.implField = this.impl;
  }

  public void getValue(String[] values)
  {
    this.impl.getValue(values);
  }

  public String get1Value(int index)
  {
    return this.impl.get1Value(index);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.MFString
 * JD-Core Version:    0.6.0
 */