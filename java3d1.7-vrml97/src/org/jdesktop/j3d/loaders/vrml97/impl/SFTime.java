package org.jdesktop.j3d.loaders.vrml97.impl;

public class SFTime extends Field
{
  double time;

  public SFTime()
  {
    this.time = -1.0D;
  }

  public SFTime(double time)
  {
    this.time = time;
  }

  public double getValue()
  {
    return this.time;
  }

  public void setValue(double time)
  {
    this.time = time;
    route();
  }

  public void setValue(SFTime time)
  {
    setValue(time.time);
  }

  public void setValue(ConstSFTime time)
  {
    setValue((SFTime)time.ownerField);
  }

  public synchronized Object clone()
  {
    return new SFTime(this.time);
  }

  public void update(Field field)
  {
    setValue((SFTime)field);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFTime(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFTime(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFTime
 * JD-Core Version:    0.6.0
 */