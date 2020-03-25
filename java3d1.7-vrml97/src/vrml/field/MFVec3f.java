package vrml.field;

import vrml.MField;

public class MFVec3f extends MField
{
  org.jdesktop.j3d.loaders.vrml97.impl.MFVec3f impl;

  public MFVec3f(org.jdesktop.j3d.loaders.vrml97.impl.MFVec3f init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new MFVec3f((org.jdesktop.j3d.loaders.vrml97.impl.MFVec3f)this.impl.clone());
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

  public MFVec3f(float[][] values)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFVec3f(values);
    this.implField = this.impl;
  }

  public MFVec3f(int size, float[] values)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFVec3f(size, values);
    this.implField = this.impl;
  }

  public MFVec3f(float[] values)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFVec3f(values);
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

  public void get1Value(int index, SFVec3f vec)
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

  public void setValue(ConstMFVec3f values)
  {
    this.impl.setValue(values.impl);
  }

  public void set1Value(int index, ConstSFVec3f constvec)
  {
    this.impl.set1Value(index, constvec.impl);
  }

  public void set1Value(int index, SFVec3f vec)
  {
    this.impl.set1Value(index, vec.impl);
  }

  public void set1Value(int index, float x, float y, float z)
  {
    this.impl.set1Value(index, x, y, z);
  }

  public void insertValue(int index, ConstSFVec3f constvec)
  {
    this.impl.insertValue(index, constvec.impl);
  }

  public void insertValue(int index, SFVec3f vec)
  {
    this.impl.insertValue(index, vec.impl);
  }

  public void insertValue(int index, float x, float y, float z)
  {
    this.impl.insertValue(index, x, y, z);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.MFVec3f
 * JD-Core Version:    0.6.0
 */