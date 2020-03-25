package org.jdesktop.j3d.loaders.vrml97.impl;

public class DoubleBuf
{
  int size;
  double[] array;

  DoubleBuf()
  {
    this.array = new double[1024];
    reset();
  }

  void reset()
  {
    this.size = 0;
  }

  void add(double newDouble)
  {
    if (this.size == this.array.length) {
      double[] newArray = new double[this.array.length + 1024];
      System.arraycopy(this.array, 0, newArray, 0, this.array.length);
      this.array = newArray;
    }
    this.array[(this.size++)] = newDouble;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.DoubleBuf
 * JD-Core Version:    0.6.0
 */