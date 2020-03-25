package vrml.field;

import vrml.ConstField;

public class ConstSFImage extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFImage impl;

  public ConstSFImage(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFImage init)
  {
    this.impl = init;
  }

  public int getWidth()
  {
    return this.impl.getWidth();
  }

  public int getHeight()
  {
    return this.impl.getHeight();
  }

  public int getComponents()
  {
    return this.impl.getComponents();
  }

  public void getPixels(byte[] pixels)
  {
    this.impl.getPixels(pixels);
  }

  public synchronized Object clone()
  {
    return new ConstSFImage((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFImage)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFImage
 * JD-Core Version:    0.6.0
 */