package vrml.field;

import vrml.Field;

public class SFBool extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFBool impl;

  public SFBool(org.jdesktop.j3d.loaders.vrml97.impl.SFBool init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFBool((org.jdesktop.j3d.loaders.vrml97.impl.SFBool)this.impl.clone());
  }

  public SFBool(boolean value)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFBool(value);
    this.implField = this.impl;
  }

  public boolean getValue()
  {
    return this.impl.getValue();
  }

  public void setValue(boolean b)
  {
    this.impl.setValue(b);
  }

  public void setValue(SFBool b)
  {
    this.impl.setValue(b.impl);
  }

  public void setValue(ConstSFBool b)
  {
    this.impl.setValue(b.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFBool
 * JD-Core Version:    0.6.0
 */