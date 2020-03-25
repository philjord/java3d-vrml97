package vrml.field;

import vrml.MField;

public class MFFloat extends MField
{
  org.jdesktop.j3d.loaders.vrml97.impl.MFFloat impl;

  public MFFloat(org.jdesktop.j3d.loaders.vrml97.impl.MFFloat init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new MFFloat((org.jdesktop.j3d.loaders.vrml97.impl.MFFloat)this.impl.clone());
  }

  public void getValue(float[] values)
  {
    this.impl.setValue(values);
  }

  public void setValue(int size, float[] value)
  {
    this.impl.setValue(size, value);
  }

  public void setValue(ConstMFFloat value)
  {
    this.impl.setValue(value.impl);
  }

  public float get1Value(int index)
  {
    return this.impl.get1Value(index);
  }

  public void set1Value(int index, float f)
  {
    this.impl.set1Value(index, f);
  }

  public void set1Value(int index, ConstSFFloat f)
  {
    this.impl.set1Value(index, f.impl);
  }

  public void set1Value(int index, SFFloat f)
  {
    this.impl.set1Value(index, f.impl);
  }

  public void addValue(float f)
  {
    this.impl.addValue(f);
  }

  public void addValue(ConstSFFloat f)
  {
    this.impl.addValue(f.impl);
  }

  public void addValue(SFFloat f)
  {
    this.impl.addValue(f.impl);
  }

  public void insertValue(int index, float f)
  {
    this.impl.insertValue(index, f);
  }

  public void insertValue(int index, ConstSFFloat f)
  {
    this.impl.insertValue(index, f.impl);
  }

  public void insertValue(int index, SFFloat f)
  {
    this.impl.insertValue(index, f.impl);
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
 * Qualified Name:     vrml.field.MFFloat
 * JD-Core Version:    0.6.0
 */