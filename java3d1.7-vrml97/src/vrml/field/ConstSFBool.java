package vrml.field;

import vrml.ConstField;

public class ConstSFBool extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFBool impl;

  public ConstSFBool(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFBool init)
  {
    this.impl = init;
  }

  public boolean getValue()
  {
    return this.impl.getValue();
  }

  public Object clone()
  {
    return new ConstSFBool((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFBool)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFBool
 * JD-Core Version:    0.6.0
 */