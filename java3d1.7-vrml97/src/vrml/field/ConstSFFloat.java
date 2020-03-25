package vrml.field;

import vrml.ConstField;

public class ConstSFFloat extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFFloat impl;

  public ConstSFFloat(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFFloat init)
  {
    this.impl = init;
  }

  public float getValue()
  {
    return this.impl.getValue();
  }

  public Object clone()
  {
    return new ConstSFFloat((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFFloat)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFFloat
 * JD-Core Version:    0.6.0
 */