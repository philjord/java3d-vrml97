package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;

public class MFColor extends MField
{
  float[] vals;

  public MFColor()
  {
    float[] dummy = new float[0];
    setValue(dummy);
  }

  public MFColor(float[][] values)
  {
    setValue(values);
  }

  public MFColor(int size, float[] values)
  {
    setValue(values);
  }

  public MFColor(float[] values)
  {
    setValue(values);
  }

  public void getValue(float[][] values)
  {
    int numVecs = this.vals.length / 3;
    for (int i = 0; i < numVecs; i++)
      System.arraycopy(this.vals, i * 3, values[i], 0, 3);
  }

  public void getValue(float[] values)
  {
    System.arraycopy(this.vals, 0, values, 0, this.vals.length);
  }

  public void get1Value(int index, float[] values)
  {
    System.arraycopy(this.vals, index * 3, values, 0, 3);
  }

  public void get1Value(int index, SFColor vec)
  {
    vec.setValue(this.vals[index], this.vals[(index + 1)], this.vals[(index + 2)]);
  }

  public void setValue(float[][] values)
  {
    this.vals = new float[values.length * 3];
    for (int i = 0; i < values.length; i++) {
      System.arraycopy(values, 0, this.vals, i * 3, i * 3 + 3);
    }
    route();
  }

  public void setValue(float[] values)
  {
    this.vals = new float[values.length];
    System.arraycopy(values, 0, this.vals, 0, values.length);
    route();
  }

  public void setValue(MFColor color)
  {
    setValue(color.vals);
  }

  public void setValue(int size, float[] values)
  {
    setValue(values);
  }

  public void setValue(ConstMFColor cf)
  {
    setValue((MFColor)cf.ownerField);
  }

  public void set1Value(int index, ConstSFColor constvec)
  {
    set1Value(index, (SFColor)constvec.ownerField);
  }

  public void set1Value(int index, SFColor vec)
  {
    set1Value(index, vec.color[0], vec.color[1], vec.color[2]);
  }

  public void set1Value(int index, float x, float y, float z)
  {
    try
    {
      this.vals[(index * 3 + 0)] = x;
      this.vals[(index * 3 + 1)] = y;
      this.vals[(index * 3 + 2)] = z;
    }
    catch (IndexOutOfBoundsException e)
    {
      System.err.println("MFColor.set1Value(index,float,float,float): exception " + e);
    }

    route();
  }

  public void insertValue(int index, ConstSFColor constvec)
  {
    insertValue(index, (SFColor)constvec.ownerField);
  }

  public void insertValue(int index, SFColor vec)
  {
    insertValue(index, vec.color[0], vec.color[1], vec.color[2]);
  }

  public void insertValue(int index, float x, float y, float z)
  {
    try
    {
      float[] temp = new float[this.vals.length + 3];
      int i = 0;
      for (; i < index * 3; i++) {
        temp[i] = this.vals[i];
      }
      temp[(i++)] = x;
      temp[(i++)] = y;
      temp[(i++)] = z;
      for (; i < temp.length; i++) {
        temp[i] = this.vals[(i - 3)];
      }
      this.vals = temp;
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("MFColor.insertValue(index,float,float,float): exception " + e);
    }

    route();
  }

  public void update(Field field)
  {
    setValue((MFColor)field);
  }

  public synchronized Object clone()
  {
    MFColor ref = new MFColor();
    ref.vals = new float[this.vals.length];
    try {
      System.arraycopy(this.vals, 0, ref.vals, 0, this.vals.length);
    }
    catch (Exception e) {
      System.err.println("MFColor.clone(): exception " + e);
    }

    return ref;
  }

  public int getSize()
  {
    return this.vals.length / 3;
  }

  public void clear()
  {
    this.vals = new float[1];
    route();
  }

  public void delete(int index)
  {
    float[] temp = new float[this.vals.length - 3];
    int i = 0;
    for (; i < index * 3; i++) {
      temp[i] = this.vals[i];
    }
    for (; i < temp.length; i++) {
      temp[i] = this.vals[(i + 3)];
    }
    this.vals = temp;
    route();
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstMFColor(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.MFColor(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.MFColor
 * JD-Core Version:    0.6.0
 */