package vrml.field;

import vrml.MField;

public class MFRotation extends MField
{
  org.jdesktop.j3d.loaders.vrml97.impl.MFRotation impl;

  public MFRotation(org.jdesktop.j3d.loaders.vrml97.impl.MFRotation init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new MFRotation((org.jdesktop.j3d.loaders.vrml97.impl.MFRotation)this.impl.clone());
  }

  public MFRotation(float[][] values)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFRotation(values);
    this.implField = this.impl;
  }

  public void getValue(float[][] values)
  {
    this.impl.getValue(values);
  }

  public void getValue(float[] values)
  {
    this.impl.getValue(values);
  }

  public void get1Value(int index, float[] values)
  {
    this.impl.get1Value(index, values);
  }

  public void get1Value(int index, SFRotation vec)
  {
    this.impl.get1Value(index, vec.impl);
  }

  public void setValue(float[][] values)
  {
    this.impl.setValue(values);
  }

  public void setValue(float[] values)
  {
    this.impl.setValue(values);
  }

  public void setValue(int size, float[] values)
  {
    this.impl.setValue(size, values);
  }

  public void setValue(ConstMFRotation values)
  {
    this.impl.setValue(values.impl);
  }

  public void set1Value(int index, ConstSFRotation constvec)
  {
    this.impl.set1Value(index, constvec.impl);
  }

  public void set1Value(int index, SFRotation vec)
  {
    this.impl.set1Value(index, vec.impl);
  }

  public void set1Value(int index, float x, float y, float z, float angle)
  {
    this.impl.set1Value(index, x, y, z, angle);
  }

  public void insertValue(int index, ConstSFRotation constvec)
  {
    this.impl.set1Value(index, constvec.impl);
  }

  public void insertValue(int index, SFRotation vec)
  {
    this.impl.set1Value(index, vec.impl);
  }

  public void insertValue(int index, float x, float y, float z, float angle)
  {
    this.impl.insertValue(index, x, y, z, angle);
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
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.MFRotation
 * JD-Core Version:    0.6.0
 */