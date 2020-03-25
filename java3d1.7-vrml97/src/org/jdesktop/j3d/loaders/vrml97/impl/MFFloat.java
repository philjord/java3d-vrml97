package org.jdesktop.j3d.loaders.vrml97.impl;

public class MFFloat extends MField
{
  float[] mfloat;

  public MFFloat()
  {
    this.mfloat = new float[0];
  }

  public MFFloat(float[] values)
  {
    this.mfloat = new float[values.length];
    System.arraycopy(values, 0, this.mfloat, 0, values.length);
  }

  public void getValue(float[] values)
  {
    System.arraycopy(this.mfloat, 0, values, 0, values.length);
  }

  public void setValue(float[] values)
  {
    this.mfloat = new float[values.length];
    System.arraycopy(values, 0, this.mfloat, 0, values.length);
    route();
  }

  public void setValue(int size, float[] value)
  {
    this.mfloat = new float[size];
    System.arraycopy(value, 0, this.mfloat, 0, size);
    route();
  }

  public void setValue(MFFloat value)
  {
    setValue(value.mfloat);
  }

  public void setValue(ConstMFFloat value)
  {
    setValue((MFFloat)value.ownerField);
  }

  public float get1Value(int index)
  {
    float f = 0.0F;
    f = this.mfloat[index];
    return f;
  }

  public void set1Value(int index, float f)
  {
    this.mfloat[index] = f;
    route();
  }

  public void set1Value(int index, ConstSFFloat f)
  {
    set1Value(index, (SFFloat)f.ownerField);
  }

  public void set1Value(int index, SFFloat f)
  {
    set1Value(index, f.value);
  }

  public void addValue(float f)
  {
    float[] temp = new float[this.mfloat.length + 1];
    System.arraycopy(this.mfloat, 0, temp, 0, this.mfloat.length);
    temp[this.mfloat.length] = f;
    this.mfloat = temp;

    route();
  }

  public void addValue(ConstSFFloat f)
  {
    addValue((SFFloat)f.ownerField);
  }

  public void addValue(SFFloat f)
  {
    addValue(f.value);
  }

  public void insertValue(int index, float f)
  {
    float[] temp = new float[this.mfloat.length + 1];
    System.arraycopy(this.mfloat, 0, temp, 0, index);
    temp[index] = f;
    System.arraycopy(this.mfloat, index, temp, index + 1, this.mfloat.length - index);
    this.mfloat = temp;
    route();
  }

  public void insertValue(int index, ConstSFFloat f)
  {
    insertValue(index, (SFFloat)f.ownerField);
  }

  public void insertValue(int index, SFFloat f)
  {
    insertValue(index, f.value);
  }

  public synchronized void update(Field field)
  {
    setValue((MFFloat)field);
  }

  public synchronized Object clone()
  {
    return new MFFloat(this.mfloat);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstMFFloat(this);
    }
    return this.constField;
  }

  public int getSize()
  {
    return this.mfloat.length;
  }

  public void clear()
  {
    this.mfloat = new float[1];
  }

  public void delete(int i)
  {
  }

  public vrml.Field wrap()
  {
    return new vrml.field.MFFloat(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.MFFloat
 * JD-Core Version:    0.6.0
 */