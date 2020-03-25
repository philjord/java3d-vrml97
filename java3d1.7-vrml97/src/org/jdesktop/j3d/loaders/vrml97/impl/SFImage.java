package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;

public class SFImage extends Field
{
  byte[] pixels;
  int width;
  int height;
  int depth;

  public SFImage(int w, int h, int d, byte[] p)
  {
    if (p != null) {
      this.pixels = new byte[p.length];
      System.arraycopy(p, 0, this.pixels, 0, p.length);
    }
    else {
      this.pixels = null;
    }

    this.width = w;
    this.height = h;
    this.depth = d;
  }

  public int getWidth()
  {
    return this.width;
  }

  public int getHeight()
  {
    return this.height;
  }

  public int getComponents()
  {
    return this.depth;
  }

  public void getPixels(byte[] p)
  {
    System.arraycopy(this.pixels, 0, p, 0, this.pixels.length);
  }

  public void setValue(int w, int h, int d, byte[] p)
  {
    this.width = w;
    this.height = h;
    this.depth = d;
    if (w * h * d > 0) {
      this.pixels = new byte[p.length];
      try {
        System.arraycopy(p, 0, this.pixels, 0, p.length);
      }
      catch (Exception e) {
        System.err.println("SFImage.setValue(): exception " + e);
      }
    }
    else {
      this.pixels = null;
    }

    route();
  }

  public void setValue(ConstSFImage i)
  {
    setValue((SFImage)i.ownerField);
  }

  public void setValue(SFImage i)
  {
    setValue(i.width, i.height, i.depth, i.pixels);
  }

  public synchronized Object clone()
  {
    return new SFImage(this.width, this.height, this.depth, this.pixels);
  }

  public void update(Field field)
  {
    setValue((SFImage)field);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFImage(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFImage(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFImage
 * JD-Core Version:    0.6.0
 */