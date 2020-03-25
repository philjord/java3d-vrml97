package vrml.field;

import vrml.ConstField;

public class ConstSFInt32 extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFInt32 impl;

  public ConstSFInt32(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFInt32 init)
  {
    this.impl = init;
  }

  public int getValue()
  {
    return this.impl.getValue();
  }

  public Object clone()
  {
    return new ConstSFInt32((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFInt32)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFInt32
 * JD-Core Version:    0.6.0
 */