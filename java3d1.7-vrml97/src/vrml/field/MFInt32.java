package vrml.field;

import vrml.MField;

public class MFInt32 extends MField
{
  org.jdesktop.j3d.loaders.vrml97.impl.MFInt32 impl;

  public MFInt32(org.jdesktop.j3d.loaders.vrml97.impl.MFInt32 init)
  {
    super(init);
    this.impl = init;
  }

  public Object clone()
  {
    return new MFInt32((org.jdesktop.j3d.loaders.vrml97.impl.MFInt32)this.impl.clone());
  }

  public void getValue(int[] values)
  {
    this.impl.getValue(values);
  }

  public void setValue(int[] values)
  {
    this.impl.setValue(values);
  }

  public void setValue(int size, int[] values)
  {
    this.impl.setValue(size, values);
  }

  public void setValue(ConstMFInt32 f)
  {
    this.impl.setValue(f.impl);
  }

  public int get1Value(int index)
  {
    return this.impl.get1Value(index);
  }

  public void set1Value(int index, int f)
  {
    this.impl.set1Value(index, f);
  }

  public void set1Value(int index, ConstSFInt32 f)
  {
    this.impl.set1Value(index, f.impl);
  }

  public void set1Value(int index, SFInt32 f)
  {
    this.impl.set1Value(index, f.impl);
  }

  public void addValue(int f)
  {
  }

  public void addValue(ConstSFInt32 f)
  {
    this.impl.addValue(f.impl);
  }

  public void addValue(SFInt32 f)
  {
    this.impl.addValue(f.impl);
  }

  public void insertValue(int index, int f)
  {
  }

  public void insertValue(int index, ConstSFInt32 f)
  {
    this.impl.insertValue(index, f.impl);
  }

  public void insertValue(int index, SFInt32 f)
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
 * Qualified Name:     vrml.field.MFInt32
 * JD-Core Version:    0.6.0
 */