package org.jdesktop.j3d.loaders.vrml97.impl;

public class FloatBuf
{
  int size;
  float[] array;

  FloatBuf()
  {
    this.array = new float[1024];
    reset();
  }

  void reset()
  {
    this.size = 0;
  }

  void add(float newFloat)
  {
    if (this.size == this.array.length) {
      float[] newArray = new float[this.array.length + 1024];
      System.arraycopy(this.array, 0, newArray, 0, this.array.length);
      this.array = newArray;
    }
    this.array[(this.size++)] = newFloat;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.FloatBuf
 * JD-Core Version:    0.6.0
 */