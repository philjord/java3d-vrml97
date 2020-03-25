package vrml.field;

import vrml.MField;

public class MFTime extends MField
{
  org.jdesktop.j3d.loaders.vrml97.impl.MFTime impl;

  public MFTime(org.jdesktop.j3d.loaders.vrml97.impl.MFTime init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new MFTime((org.jdesktop.j3d.loaders.vrml97.impl.MFTime)this.impl.clone());
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

  public MFTime(double[] time)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFTime(time);
    this.implField = this.impl;
  }

  public void getValue(double[] values)
  {
    this.impl.getValue(values);
  }

  public double get1Value(int index)
  {
    return this.impl.get1Value(index);
  }

  public void setValue(double[] values)
  {
    this.impl.setValue(values);
  }

  public void setValue(int size, double[] values)
  {
    this.impl.setValue(size, values);
  }

  public void set1Value(int index, double d)
  {
    this.impl.set1Value(index, d);
  }

  public void set1Value(int index, ConstSFTime t)
  {
    this.impl.set1Value(index, t.impl);
  }

  public void set1Value(int index, SFTime t)
  {
    this.impl.set1Value(index, t.impl);
  }

  public void addValue(double d)
  {
    this.impl.addValue(d);
  }

  public void addValue(ConstSFTime t)
  {
    this.impl.addValue(t.impl);
  }

  public void addValue(SFTime t)
  {
    this.impl.addValue(t.impl);
  }

  public void insertValue(int index, double d)
  {
    this.impl.insertValue(index, d);
  }

  public void insertValue(int index, SFTime t)
  {
    this.impl.insertValue(index, t.impl);
  }

  public void insertValue(int index, ConstSFTime t)
  {
    this.impl.insertValue(index, t.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.MFTime
 * JD-Core Version:    0.6.0
 */