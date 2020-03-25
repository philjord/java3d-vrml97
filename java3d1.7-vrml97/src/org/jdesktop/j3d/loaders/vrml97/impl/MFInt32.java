package org.jdesktop.j3d.loaders.vrml97.impl;

public class MFInt32 extends MField
{
  int size;
  int[] value;
  int initSize;
  int[] initValue;

  private void setInit()
  {
    this.initSize = this.size;
    if (this.size > 0) {
      this.initValue = new int[this.size];
      System.arraycopy(this.value, 0, this.initValue, 0, this.size);
    }
    else {
      this.initValue = null;
    }
  }

  public MFInt32()
  {
    this.size = 0;
  }

  public MFInt32(int[] setVal)
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

  public void getValue(int[] getVal)
  {
    System.arraycopy(this.value, 0, getVal, 0, this.size);
  }

  public void setValue(int[] setVal)
  {
    setValue(setVal.length, setVal);
  }

  public void setValue(int setSize, int[] setVal)
  {
    checkSize(setSize, false);
    this.size = setSize;
    if (this.size > 0) {
      System.arraycopy(setVal, 0, this.value, 0, this.size);
    }
    route();
  }

  public void setValue(MFInt32 value)
  {
    setValue(value.value);
  }

  public void setValue(ConstMFInt32 value)
  {
    setValue((MFInt32)value.ownerField);
  }

  public int get1Value(int index)
  {
    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return this.value[index];
  }

  void checkSize(int needed, boolean preserveValue)
  {
    if ((this.value == null) && (needed > 0)) {
      this.value = new int[needed];
    }
    else if (needed > this.value.length) {
      int newSize = this.value.length;
      if (newSize == 0) {
        newSize = needed;
      }
      while (needed > newSize) {
        newSize <<= 1;
      }
      int[] prevValue = this.value;
      this.value = new int[newSize];
      if (preserveValue)
        System.arraycopy(prevValue, 0, this.value, 0, this.size);
    }
  }

  int primCount()
  {
    int count = 0;
    for (int i = 0; i < this.size; i++) {
      if (this.value[i] == -1) {
        count++;
      }
    }

    if (this.value[(this.size - 1)] != -1) {
      count++;
    }
    return count;
  }

  int indexCount()
  {
    int count = 0;
    for (int i = 0; i < this.size; i++) {
      if (this.value[i] != -1) {
        count++;
      }
    }
    return count;
  }

  void fillImplArrays(int[] implSize, int[] implIndex)
  {
    int curPrim = 0;
    int curSize = 0;
    int curIndex = 0;

    boolean lastValue = false;
    for (int i = 0; i < this.size; i++) {
      if (this.value[i] == -1) {
        implSize[(curPrim++)] = curSize;
        curSize = 0;
        lastValue = false;
      }
      else {
        implIndex[(curIndex++)] = this.value[i];
        curSize++;
        lastValue = true;
      }
    }
    if (lastValue)
    {
      implSize[(curPrim++)] = curSize;
    }
  }

  boolean fillImplArraysTest(int[] implSize, int[] implIndex)
  {
    int curPrim = 0;
    int curSize = 0;
    int inIndex = 0;
    int outIndex = 0;
    boolean dataOK = true;

    while (outIndex < implIndex.length)
    {
      int useValue;
      if (inIndex >= this.size) {
        useValue = this.value[(this.size - 1)];
        dataOK = false;
      }
      else {
        useValue = this.value[inIndex];
      }
      if (useValue == -1) {
        if (implSize[curPrim] != curSize)
        {
          dataOK = false;
        }
        curPrim++;
        if (curPrim >= implSize.length) {
          dataOK = false;
          curPrim--;
        }
        curSize = 0;
      }
      else {
        implIndex[(outIndex++)] = useValue;
        if (curSize++ > implSize[curPrim]) {
          dataOK = false;
        }
      }
      inIndex++;
    }
    return dataOK;
  }

  public void set1Value(int index, int f)
  {
    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    this.value[index] = f;
    route();
  }

  public void set1Value(int index, ConstSFInt32 f)
  {
    set1Value(index, (SFInt32)f.ownerField);
  }

  public void set1Value(int index, SFInt32 f)
  {
    set1Value(index, f.value);
  }

  public void addValue(int f)
  {
    checkSize(this.size + 1, true);
    this.value[(this.size++)] = f;
    route();
  }

  public void addValue(ConstSFInt32 f)
  {
    addValue((SFInt32)f.ownerField);
  }

  public void addValue(SFInt32 f)
  {
    addValue(f.value);
  }

  public void insertValue(int index, int f)
  {
    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    checkSize(this.size + 1, true);

    System.arraycopy(this.value, index, this.value, index + 1, this.size - index);
    this.value[index] = f;
    route();
  }

  public void insertValue(int index, ConstSFInt32 f)
  {
    insertValue(index, (SFInt32)f.ownerField);
  }

  public void insertValue(int index, SFInt32 f)
  {
    insertValue(index, f.value);
  }

  public void update(Field field)
  {
    setValue((MFInt32)field);
  }

  public Object clone()
  {
    MFInt32 ref = new MFInt32();
    ref.size = this.size;
    if (this.size == 0) {
      ref.value = null;
    }
    else {
      ref.value = new int[this.size];
      System.arraycopy(this.value, 0, ref.value, 0, this.size);
    }
    return ref;
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstMFInt32(this);
    }
    return this.constField;
  }

  public int getSize()
  {
    return this.size;
  }

  public void clear()
  {
    this.size = 0;
  }

  public void delete(int index)
  {
    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    System.arraycopy(this.value, index + 1, this.value, index, this.size - index);
    this.size -= 1;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.MFInt32(this);
  }

  public String toString()
  {
    String retval = "[\n   ";
    for (int i = 0; i < this.size; i++) {
      retval = retval + this.value[i] + " ";

      if (this.value[i] == -1) {
        retval = retval + "\n   ";
      }
    }
    retval = retval + "\n]\n";
    return retval;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.MFInt32
 * JD-Core Version:    0.6.0
 */