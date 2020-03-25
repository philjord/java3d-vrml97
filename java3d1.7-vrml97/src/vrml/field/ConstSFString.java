package vrml.field;

import vrml.ConstField;

public class ConstSFString extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFString impl;

  public ConstSFString(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFString init)
  {
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new ConstSFString((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFString)this.impl.clone());
  }

  public String getValue()
  {
    return this.impl.getValue();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFString
 * JD-Core Version:    0.6.0
 */