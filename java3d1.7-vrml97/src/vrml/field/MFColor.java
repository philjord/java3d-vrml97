package vrml.field;

import vrml.MField;

public class MFColor extends MField
{
  org.jdesktop.j3d.loaders.vrml97.impl.MFColor impl;

  public MFColor(org.jdesktop.j3d.loaders.vrml97.impl.MFColor init)
  {
    super(init);
    this.impl = init;
  }

  public MFColor(float[][] values)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFColor(values);
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

  public void get1Value(int index, SFColor vec)
  {
    this.impl.get1Value(index, vec.impl);
  }

  public void setValue(float[][] values)
  {
    this.impl.setValue(values);
  }

  public void setValue(int size, float[] values)
  {
    this.impl.setValue(size, values);
  }

  public void setValue(float[] values)
  {
    this.impl.setValue(values);
  }

  public void setValue(ConstMFColor values)
  {
    this.impl.setValue(values.impl);
  }

  public void set1Value(int index, ConstSFColor constvec)
  {
    this.impl.set1Value(index, constvec.impl);
  }

  public void set1Value(int index, SFColor vec)
  {
    this.impl.set1Value(index, vec.impl);
  }

  public void set1Value(int index, float r, float g, float b)
  {
    this.impl.set1Value(index, r, g, b);
  }

  public void insertValue(int index, ConstSFColor constvec)
  {
    this.impl.insertValue(index, constvec.impl);
  }

  public void insertValue(int index, SFColor vec)
  {
    this.impl.insertValue(index, vec.impl);
  }

  public void insertValue(int index, float r, float g, float b)
  {
    this.impl.insertValue(index, r, g, b);
  }

  public Object clone()
  {
    return new MFColor((org.jdesktop.j3d.loaders.vrml97.impl.MFColor)this.impl.clone());
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public void delete(int index)
  {
    this.impl.delete(index);
  }

  public void clear()
  {
    this.impl.clear();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.MFColor
 * JD-Core Version:    0.6.0
 */