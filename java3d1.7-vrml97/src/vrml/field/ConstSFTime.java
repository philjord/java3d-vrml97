package vrml.field;

import vrml.ConstField;

public class ConstSFTime extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFTime impl;

  public ConstSFTime(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFTime init)
  {
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new ConstSFTime((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFTime)this.impl.clone());
  }

  public double getValue()
  {
    return this.impl.getValue();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFTime
 * JD-Core Version:    0.6.0
 */