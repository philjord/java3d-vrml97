package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;

public abstract class Interpolator extends Node
{
  MFFloat key;
  private int lastIndex = 0;
  int iL;
  float f;
  float af;

  Interpolator(Loader loader)
  {
    super(loader);
  }

  void setIndexFract(float value)
  {
    int j;
    if (this.key.mfloat[this.lastIndex] < value) {
      j = this.lastIndex;
    }
    else {
      j = 0;
    }
    while ((j < this.key.mfloat.length) && (this.key.mfloat[j] < value)) j++;

    this.iL = (j - 1);

    if (this.iL >= this.key.mfloat.length - 1) {
      this.iL = (this.key.mfloat.length - 2);
      this.f = 1.0F;
    }
    else if (this.iL < 0) {
      this.iL = 0;
      this.f = 0.0F;
    }
    else {
      try {
        this.f = (value - this.key.mfloat[this.iL]);
        this.f /= (this.key.mfloat[(this.iL + 1)] - this.key.mfloat[this.iL]);
      }
      catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Interpolator madness!");
        this.iL = 0;
        this.f = (value - this.key.mfloat[this.iL]);
        this.f /= (this.key.mfloat[(this.iL + 1)] - this.key.mfloat[this.iL]);
      }
    }
    this.lastIndex = this.iL;
    this.af = (1.0F - this.f);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Interpolator
 * JD-Core Version:    0.6.0
 */