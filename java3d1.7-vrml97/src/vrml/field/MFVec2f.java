package vrml.field;

import vrml.MField;

public class MFVec2f extends MField
{
  org.jdesktop.j3d.loaders.vrml97.impl.MFVec2f impl;

  public MFVec2f(org.jdesktop.j3d.loaders.vrml97.impl.MFVec2f init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new MFVec2f((org.jdesktop.j3d.loaders.vrml97.impl.MFVec2f)this.impl.clone());
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

  public MFVec2f(float[][] values)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFVec2f(values);
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

  public void get1Value(int index, SFVec2f vec)
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

  public void setValue(ConstMFVec2f values)
  {
    this.impl.setValue(values.impl);
  }

  public void set1Value(int index, ConstSFVec2f constvec)
  {
    this.impl.set1Value(index, constvec.impl);
  }

  public void set1Value(int index, SFVec2f vec)
  {
    this.impl.set1Value(index, vec.impl);
  }

  public void set1Value(int index, float x, float y)
  {
    this.impl.set1Value(index, x, y);
  }

  public void insertValue(int index, ConstSFVec2f constvec)
  {
    this.impl.insertValue(index, constvec.impl);
  }

  public void insertValue(int index, SFVec2f vec)
  {
    this.impl.insertValue(index, vec.impl);
  }

  public void insertValue(int index, float x, float y)
  {
    this.impl.insertValue(index, x, y);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.MFVec2f
 * JD-Core Version:    0.6.0
 */