package org.jdesktop.j3d.loaders.vrml97.impl;

public class PixelTexture extends Node
{
  SFImage image;
  SFBool repeatS;
  SFBool repeatT;

  public PixelTexture(Loader loader)
  {
    super(loader);
    this.image = new SFImage(0, 0, 0, new byte[1]);
    this.repeatS = new SFBool(true);
    this.repeatT = new SFBool(true);

    initFields();
  }

  public PixelTexture(Loader loader, SFImage initImage, SFBool initRepeatS, SFBool initRepeatT)
  {
    super(loader);
    this.image = initImage;
    this.repeatS = initRepeatS;
    this.repeatT = initRepeatT;

    initFields();
  }

  void initFields()
  {
    this.image.init(this, this.FieldSpec, 3, "image");
    this.repeatS.init(this, this.FieldSpec, 0, "repeatS");
    this.repeatT.init(this, this.FieldSpec, 0, "repeatT");
  }

  public void notifyMethod(String eventInName, double time)
  {
  }

  public String getType()
  {
    return "PixelTexture";
  }

  public Object clone()
  {
    PixelTexture pt = new PixelTexture(this.loader, (SFImage)this.image.clone(), (SFBool)this.repeatS.clone(), (SFBool)this.repeatT.clone());

    return pt;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.PixelTexture
 * JD-Core Version:    0.6.0
 */