package vrml.field;

import vrml.Field;

public class SFInt32 extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFInt32 impl;

  public SFInt32(org.jdesktop.j3d.loaders.vrml97.impl.SFInt32 init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFInt32((org.jdesktop.j3d.loaders.vrml97.impl.SFInt32)this.impl.clone());
  }

  SFInt32(int value)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFInt32(value);
    this.implField = this.impl;
  }

  public int getValue()
  {
    return this.impl.getValue();
  }

  public void setValue(int i)
  {
    this.impl.setValue(i);
  }

  public void setValue(SFInt32 i)
  {
    this.impl.setValue(i.impl);
  }

  public void setValue(ConstSFInt32 i)
  {
    this.impl.setValue(i.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFInt32
 * JD-Core Version:    0.6.0
 */