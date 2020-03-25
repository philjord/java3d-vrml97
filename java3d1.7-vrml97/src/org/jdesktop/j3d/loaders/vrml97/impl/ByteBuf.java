package org.jdesktop.j3d.loaders.vrml97.impl;

public class ByteBuf
{
  int size;
  byte[] array;

  ByteBuf()
  {
    this.array = new byte[1024];
    reset();
  }

  void reset()
  {
    this.size = 0;
  }

  void add(byte newByte)
  {
    if (this.size == this.array.length) {
      byte[] newArray = new byte[this.array.length + 1024];
      System.arraycopy(this.array, 0, newArray, 0, this.array.length);
      this.array = newArray;
    }
    this.array[(this.size++)] = newByte;
  }

  void trim()
  {
    byte[] newArray = new byte[this.size];
    System.arraycopy(this.array, 0, newArray, 0, this.size);
    this.array = newArray;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ByteBuf
 * JD-Core Version:    0.6.0
 */