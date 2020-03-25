package org.jdesktop.j3d.loaders.vrml97.impl;

public class MFTime extends MField
{
  double[] time = new double[1];

  public MFTime()
  {
  }

  public MFTime(double[] time)
  {
    this.time = new double[time.length];
    System.arraycopy(time, 0, this.time, 0, time.length);
  }

  public void getValue(double[] values)
  {
    System.arraycopy(this.time, 0, values, 0, this.time.length);
  }

  public double get1Value(int index)
  {
    double ret = 0.0D;
    ret = this.time[index];
    return ret;
  }

  public void setValue(double[] values)
  {
    this.time = new double[values.length];
    System.arraycopy(values, 0, this.time, 0, values.length);
    route();
  }

  public void setValue(int size, double[] values)
  {
    setValue(values);
  }

  public void setValue(MFTime field)
  {
    setValue(field.time);
  }

  public void setValue(ConstMFTime field)
  {
    setValue((MFTime)field.ownerField);
  }

  public void set1Value(int index, double d)
  {
    this.time[index] = d;
    route();
  }

  public void set1Value(int index, ConstSFTime t)
  {
    set1Value(index, (SFTime)t.ownerField);
  }

  public void set1Value(int index, SFTime t)
  {
    set1Value(index, t.time);
  }

  public void addValue(double d)
  {
    double[] temp = new double[this.time.length + 1];
    System.arraycopy(this.time, 0, temp, 0, this.time.length);
    temp[this.time.length] = d;
    this.time = temp;

    route();
  }

  public void addValue(ConstSFTime t)
  {
    addValue((SFTime)t.ownerField);
  }

  public void addValue(SFTime t)
  {
    addValue(t.time);
  }

  public void insertValue(int index, double d)
  {
    double[] temp = new double[this.time.length + 1];
    System.arraycopy(this.time, 0, temp, 0, index);
    temp[index] = d;
    System.arraycopy(this.time, index, temp, index + 1, this.time.length - index);
    this.time = temp;
    route();
  }

  public void insertValue(int index, SFTime t)
  {
    insertValue(index, t.time);
  }

  public void insertValue(int index, ConstSFTime t)
  {
    insertValue(index, (SFTime)t.ownerField);
  }

  public synchronized Object clone()
  {
    return new MFTime(this.time);
  }

  public void update(Field field)
  {
    setValue((MFTime)field);
  }

  public ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstMFTime(this);
    }
    return this.constField;
  }

  public int getSize()
  {
    return this.time.length;
  }

  public void clear()
  {
    this.time = new double[1];
  }

  public void delete(int i)
  {
  }

  public vrml.Field wrap()
  {
    return new vrml.field.MFTime(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.MFTime
 * JD-Core Version:    0.6.0
 */