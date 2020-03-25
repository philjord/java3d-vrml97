package org.jdesktop.j3d.loaders.vrml97.impl;

public class IntBuf
{
  int size;
  int[] array;

  IntBuf()
  {
    this.array = new int[1024];
    reset();
  }

  void reset()
  {
    this.size = 0;
  }

  void add(int newInt)
  {
    if (this.size == this.array.length) {
      int[] newArray = new int[this.array.length + 1024];
      System.arraycopy(this.array, 0, newArray, 0, this.array.length);
      this.array = newArray;
    }
    this.array[(this.size++)] = newInt;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.IntBuf
 * JD-Core Version:    0.6.0
 */