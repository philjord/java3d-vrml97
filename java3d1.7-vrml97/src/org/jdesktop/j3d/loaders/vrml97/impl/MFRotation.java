package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Vector;

public class MFRotation extends MField
{
  SFRotation[] rots;

  public MFRotation()
  {
    this.rots = new SFRotation[0];
  }

  public MFRotation(int size, float[] values)
  {
    setValue(size, values);
  }

  public MFRotation(float[] values)
  {
    setValue(values);
  }

  public MFRotation(float[][] values)
  {
    setValue(values);
  }

  public void getValue(float[][] values)
  {
    for (int i = 0; i < values.length; i++)
      System.arraycopy(this.rots[i].rot, 0, values[i], 0, 4);
  }

  public void getValue(float[] values)
  {
    for (int i = 0; i < values.length; i += 4)
      System.arraycopy(this.rots[(i / 4)].rot, 0, values, i, 4);
  }

  public void get1Value(int index, float[] values)
  {
    System.arraycopy(this.rots[index].rot, 0, values, 0, 4);
  }

  public void get1Value(int index, SFRotation vec)
  {
    vec.setValue(this.rots[index].rot);
  }

  public void setValue(float[][] values)
  {
    this.rots = new SFRotation[values.length];
    for (int i = 0; i < values.length; i++) {
      this.rots[i] = new SFRotation(values[i]);
    }
    route();
  }

  public void setValue(float[] values)
  {
    this.rots = new SFRotation[values.length / 4];
    for (int i = 0; i < values.length; i += 4) {
      this.rots[(i / 4)] = new SFRotation(values[i], values[(i + 1)], values[(i + 2)], values[(i + 3)]);
    }

    route();
  }

  public void setValue(int size, float[] values)
  {
    setValue(values);
  }

  public void setValue(MFRotation values)
  {
    this.rots = new SFRotation[values.rots.length];
    for (int i = 0; i < values.rots.length; i++) {
      this.rots[i] = new SFRotation(values.rots[i].rot);
    }
    route();
  }

  public void setValue(ConstMFRotation values)
  {
    setValue((MFRotation)values.ownerField);
  }

  public void set1Value(int index, ConstSFRotation constvec)
  {
    set1Value(index, (SFRotation)constvec.ownerField);
  }

  public void set1Value(int index, SFRotation vec)
  {
    set1Value(index, vec.rot[0], vec.rot[1], vec.rot[2], vec.rot[3]);
  }

  public void set1Value(int index, float x, float y, float z, float angle)
  {
    this.rots[index].rot[0] = x;
    this.rots[index].rot[1] = y;
    this.rots[index].rot[2] = z;
    this.rots[index].rot[3] = angle;
    route();
  }

  public void insertValue(int index, ConstSFRotation constvec)
  {
    insertValue(index, (SFRotation)constvec.ownerField);
  }

  public void insertValue(int index, SFRotation vec)
  {
    insertValue(index, vec.rot[0], vec.rot[1], vec.rot[2], vec.rot[3]);
  }

  public void insertValue(int index, float x, float y, float z, float angle)
  {
    Vector veclist = new Vector(this.rots.length + 1);
    float[] sfr = new float[4];
    sfr[0] = x;
    sfr[1] = y;
    sfr[2] = z;
    sfr[3] = angle;
    for (int i = 0; i < this.rots.length; i++) {
      veclist.addElement(this.rots[i]);
    }
    veclist.insertElementAt(new SFRotation(sfr), index);
    this.rots = new SFRotation[veclist.size()];
    veclist.copyInto(this.rots);
    route();
  }

  public void update(Field field)
  {
    setValue((MFRotation)field);
  }

  public synchronized Object clone()
  {
    SFRotation[] tmp = new SFRotation[this.rots.length];
    MFRotation ref = new MFRotation();
    ref.rots = tmp;
    System.arraycopy(this.rots, 0, ref.rots, 0, this.rots.length);
    return ref;
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstMFRotation(this);
    }
    return this.constField;
  }

  public int getSize()
  {
    return this.rots.length;
  }

  public void clear()
  {
    this.rots = new SFRotation[1];
  }

  public void delete(int i)
  {
  }

  public vrml.Field wrap()
  {
    return new vrml.field.MFRotation(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.MFRotation
 * JD-Core Version:    0.6.0
 */