package org.jdesktop.j3d.loaders.vrml97.impl;

public class MFVec2f extends MField
{
  float[] vals;

  public MFVec2f()
  {
    float[] dummy = new float[1];
    setValue(dummy);
  }

  public MFVec2f(float[][] values)
  {
    setValue(values);
  }

  public MFVec2f(int size, float[] values)
  {
    setValue(values);
  }

  public MFVec2f(float[] values)
  {
    setValue(values);
  }

  public void getValue(float[][] values)
  {
    int numVecs = this.vals.length / 2;
    for (int i = 0; i < numVecs; i++)
      System.arraycopy(this.vals, i * 2, values[i], 0, 2);
  }

  public void getValue(float[] values)
  {
    System.arraycopy(this.vals, 0, values, 0, this.vals.length);
  }

  public void get1Value(int index, float[] values)
  {
    System.arraycopy(this.vals, index * 2, values, 0, 2);
  }

  public void get1Value(int index, SFVec2f vec)
  {
    vec.setValue(this.vals[index], this.vals[(index + 1)]);
  }

  public void setValue(float[][] values)
  {
    this.vals = new float[values.length * 2];
    for (int i = 0; i < values.length; i++) {
      System.arraycopy(values, 0, this.vals, i * 2, i * 2 + 2);
    }
    route();
  }

  public void setValue(float[] values)
  {
    this.vals = new float[values.length];
    System.arraycopy(values, 0, this.vals, 0, values.length);
    route();
  }

  public void setValue(int size, float[] values)
  {
    setValue(values);
  }

  public void setValue(MFVec2f field)
  {
    setValue(field.vals);
  }

  public void setValue(ConstMFVec2f field)
  {
    setValue((MFVec2f)field.ownerField);
  }

  public void set1Value(int index, ConstSFVec2f constvec)
  {
    set1Value(index, (SFVec2f)constvec.ownerField);
  }

  public void set1Value(int index, SFVec2f vec)
  {
    set1Value(index, vec.vec2f[0], vec.vec2f[1]);
  }

  public void set1Value(int index, float x, float y)
  {
    this.vals[(index * 2 + 0)] = x;
    this.vals[(index * 2 + 1)] = y;
    route();
  }

  public void insertValue(int index, ConstSFVec2f constvec)
  {
    insertValue(index, (SFVec2f)constvec.ownerField);
    route();
  }

  public void insertValue(int index, SFVec2f vec)
  {
    insertValue(index, vec.vec2f[0], vec.vec2f[1]);
  }

  public void insertValue(int index, float x, float y)
  {
    float[] temp = new float[this.vals.length + 2];
    int i = 0;
    for (; i < index * 2; i++) {
      temp[i] = this.vals[i];
    }
    temp[(i++)] = x;
    temp[(i++)] = y;
    for (; i < temp.length; i++) {
      temp[i] = this.vals[(i - 2)];
    }
    this.vals = temp;
    route();
  }

  public void update(Field field)
  {
    setValue((MFVec2f)field);
  }

  public Object clone()
  {
    MFVec2f ref = new MFVec2f();
    ref.vals = new float[this.vals.length];
    System.arraycopy(this.vals, 0, ref.vals, 0, this.vals.length);
    return ref;
  }

  public ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstMFVec2f(this);
    }
    return this.constField;
  }

  public int getSize()
  {
    return this.vals.length / 2;
  }

  public void clear()
  {
    this.vals = new float[1];
    route();
  }

  public void delete(int index)
  {
    float[] temp = new float[this.vals.length - 2];
    int i = 0;
    for (; i < index * 2; i++) {
      temp[i] = this.vals[i];
    }
    for (; i < temp.length; i++) {
      temp[i] = this.vals[(i + 2)];
    }
    this.vals = temp;
    route();
  }

  public vrml.Field wrap()
  {
    return new vrml.field.MFVec2f(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.MFVec2f
 * JD-Core Version:    0.6.0
 */