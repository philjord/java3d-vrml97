package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFImage extends ConstField
{
  ConstSFImage(SFImage owner)
  {
    super(owner);
  }

  public ConstSFImage(int w, int h, int d, byte[] p)
  {
    super(new SFImage(w, h, d, p));
  }

  public int getWidth()
  {
    return ((SFImage)this.ownerField).getWidth();
  }

  public int getHeight()
  {
    return ((SFImage)this.ownerField).getHeight();
  }

  public int getComponents()
  {
    return ((SFImage)this.ownerField).getComponents();
  }

  public void getPixels(byte[] pixels)
  {
    ((SFImage)this.ownerField).getPixels(pixels);
  }

  public synchronized Object clone()
  {
    return new ConstSFImage((SFImage)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFImage(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFImage
 * JD-Core Version:    0.6.0
 */