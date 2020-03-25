package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.BoundingBox;
import org.jogamp.vecmath.Point3d;

public class MFVec3f extends MField
{
  int size;
  float[] value;
  int initSize;
  float[] initValue;

  private void setInit()
  {
    this.initSize = this.size;
    if (this.size > 0) {
      this.initValue = new float[this.size];
      System.arraycopy(this.value, 0, this.initValue, 0, this.size);
    }
    else {
      this.initValue = null;
    }
  }

  public MFVec3f()
  {
    this.size = 0;
    setInit();
  }

  public MFVec3f(float[][] setVal)
  {
    setValue(setVal);
    setInit();
  }

  public MFVec3f(int size, float[] setVal)
  {
    size *= 3;
    setValue(size, setVal);
    setInit();
  }

  public MFVec3f(float[] setVal)
  {
    setValue(setVal);
    setInit();
  }

  public void reset()
  {
    this.size = this.initSize;
    if (this.initSize > 0)
      setValue(this.initValue);
  }

  public void getValue(float[][] getVal)
  {
    int numVecs = this.size / 3;
    for (int i = 0; i < numVecs; i++)
      System.arraycopy(this.value, i * 3, getVal[i], 0, 3);
  }

  public void getValue(float[] getVal)
  {
    System.arraycopy(this.value, 0, getVal, 0, this.size);
  }

  public void get1Value(int index, float[] getVal)
  {
    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    System.arraycopy(this.value, index * 3, getVal, 0, 3);
  }

  public void get1Value(int index, SFVec3f getVal)
  {
    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    getVal.setValue(this.value[index], this.value[(index + 1)], this.value[(index + 2)]);
  }

  void checkSize(int needed, boolean preserveValue)
  {
    if ((this.value == null) && (needed > 0)) {
      this.value = new float[needed];
    }
    else if (needed > this.value.length) {
      int newSize = this.value.length;
      if (newSize == 0) {
        newSize = needed;
      }
      while (needed > newSize) {
        newSize <<= 1;
      }
      float[] prevValue = this.value;
      this.value = new float[newSize];
      if (preserveValue)
        System.arraycopy(prevValue, 0, this.value, 0, this.size);
    }
  }

  public void setValue(float[][] setVal)
  {
    checkSize(setVal.length * 3, false);
    this.size = (setVal.length * 3);
    for (int i = 0; i < setVal.length; i++) {
      System.arraycopy(setVal, 0, this.value, i * 3, i * 3 + 3);
    }
    route();
  }

  public void setValue(float[] setVal)
  {
    setValue(setVal.length, setVal);
  }

  public void setValue(int setSize, float[] setVal)
  {
    checkSize(setSize, false);
    this.size = setSize;
    if (this.size > 0) {
      System.arraycopy(setVal, 0, this.value, 0, this.size);
    }
    route();
  }

  public void setValue(MFVec3f field)
  {
    setValue(field.value);
  }

  public void setValue(ConstMFVec3f field)
  {
    setValue(((MFVec3f)field.ownerField).value);
  }

  public void set1Value(int index, ConstSFVec3f constvec)
  {
    set1Value(index, (SFVec3f)(SFVec3f)constvec.ownerField);
  }

  public void set1Value(int index, SFVec3f vec)
  {
    set1Value(index, vec.value[0], vec.value[1], vec.value[2]);
  }

  public void set1Value(int index, float x, float y, float z)
  {
    index *= 3;
    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    this.value[(index + 0)] = x;
    this.value[(index + 1)] = y;
    this.value[(index + 2)] = z;
    route();
  }

  public void insertValue(int index, ConstSFVec3f constvec)
  {
    insertValue(index, (SFVec3f)constvec.ownerField);
  }

  public void insertValue(int index, SFVec3f vec)
  {
    insertValue(index, vec.value[0], vec.value[1], vec.value[2]);
  }

  public void insertValue(int index, float x, float y, float z)
  {
    index *= 3;
    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    checkSize(this.size + 3, true);

    System.arraycopy(this.value, index, this.value, index + 3, this.size - index);

    this.value[index] = x;
    this.value[(index + 1)] = y;
    this.value[(index + 2)] = y;
    this.size += 3;
    route();
  }

  public void update(Field field)
  {
    setValue((MFVec3f)field);
  }

  public Object clone()
  {
    MFVec3f ref = new MFVec3f();
    ref.size = this.size;
    if (this.size == 0) {
      ref.value = null;
    }
    else {
      ref.value = new float[this.size];
      System.arraycopy(this.value, 0, ref.value, 0, this.size);
    }
    return ref;
  }

  public ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstMFVec3f(this);
    }
    return this.constField;
  }

  public int getSize()
  {
    return this.size / 3;
  }

  public void clear()
  {
    this.size = 0;
    route();
  }

  public void delete(int index)
  {
    index *= 3;
    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    System.arraycopy(this.value, index + 3, this.value, index, this.size - index);
    this.size -= 3;
    route();
  }

  public vrml.Field wrap()
  {
    return new vrml.field.MFVec3f(this);
  }

  public String toString()
  {
    String retval = "[";
    for (int i = 0; i < this.size; i += 3) {
      for (int j = 0; j < 3; j++) {
        retval = retval + this.value[(i + j)];
        retval = retval + " ";
      }
      retval = retval + "\n  ";
    }
    retval = retval + "\n]\n";
    return retval;
  }

  BoundingBox getBoundingBox()
  {
    Point3d min = new Point3d(1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.7976931348623157E+308D);

    Point3d max = new Point3d(4.9E-324D, 4.9E-324D, 4.9E-324D);

    for (int i = 0; i < this.size; i += 3) {
      if (this.value[i] > max.x) {
        max.x = this.value[i];
      }
      if (this.value[i] < min.x) {
        min.x = this.value[i];
      }
      if (this.value[(i + 1)] > max.y) {
        max.y = this.value[(i + 1)];
      }
      if (this.value[(i + 1)] < min.y) {
        min.y = this.value[(i + 1)];
      }
      if (this.value[(i + 2)] > max.z) {
        max.z = this.value[(i + 2)];
      }
      if (this.value[(i + 2)] < min.z) {
        min.z = this.value[(i + 2)];
      }
    }
    return new BoundingBox(min, max);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.MFVec3f
 * JD-Core Version:    0.6.0
 */