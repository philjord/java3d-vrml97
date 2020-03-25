package vrml.field;

import vrml.Field;

public class SFImage extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFImage impl;

  public SFImage(org.jdesktop.j3d.loaders.vrml97.impl.SFImage init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFImage((org.jdesktop.j3d.loaders.vrml97.impl.SFImage)this.impl.clone());
  }

  public SFImage(int w, int h, int d, byte[] p)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFImage(w, h, d, p);
    this.implField = this.impl;
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

  public void getPixels(byte[] p)
  {
    this.impl.getPixels(p);
  }

  public void setValue(int w, int h, int d, byte[] p)
  {
    this.impl.setValue(w, h, d, p);
  }

  public void setValue(ConstSFImage i)
  {
    this.impl.setValue(i.impl);
  }

  public void setValue(SFImage i)
  {
    this.impl.setValue(i.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFImage
 * JD-Core Version:    0.6.0
 */