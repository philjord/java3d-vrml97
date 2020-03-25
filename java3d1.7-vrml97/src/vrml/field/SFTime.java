package vrml.field;

import vrml.Field;

public class SFTime extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFTime impl;

  public SFTime(org.jdesktop.j3d.loaders.vrml97.impl.SFTime init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFTime((org.jdesktop.j3d.loaders.vrml97.impl.SFTime)this.impl.clone());
  }

  public SFTime(double time)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFTime(time);
    this.implField = this.impl;
  }

  public double getValue()
  {
    return this.impl.getValue();
  }

  public void setValue(double time)
  {
    this.impl.setValue(time);
  }

  public void setValue(SFTime time)
  {
    this.impl.setValue(time.impl);
  }

  public void setValue(ConstSFTime time)
  {
    this.impl.setValue(time.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFTime
 * JD-Core Version:    0.6.0
 */