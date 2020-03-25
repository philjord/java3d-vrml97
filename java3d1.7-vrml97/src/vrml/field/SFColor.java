package vrml.field;

import vrml.Field;

public class SFColor extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFColor impl;

  public SFColor(org.jdesktop.j3d.loaders.vrml97.impl.SFColor init)
  {
    super(init);
    this.impl = init;
  }

  public SFColor(float red, float green, float blue)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFColor(red, green, blue);
    this.implField = this.impl;
  }

  public SFColor(float[] initColor)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFColor(initColor);
    this.implField = this.impl;
  }

  public void getValue(float[] colr)
  {
    this.impl.getValue(colr);
  }

  public float[] getValue()
  {
    return this.impl.getValue();
  }

  public void setRed(float red)
  {
    this.impl.setRed(red);
  }

  public void setGreen(float green)
  {
    this.impl.setGreen(green);
  }

  public void setBlue(float blue)
  {
    this.impl.setBlue(blue);
  }

  public float getRed()
  {
    return this.impl.getRed();
  }

  public float getGreen()
  {
    return this.impl.getGreen();
  }

  public float getBlue()
  {
    return this.impl.getBlue();
  }

  public void setValue(float[] colrs)
  {
    this.impl.setValue(colrs);
  }

  public void setValue(float red, float green, float blue)
  {
    this.impl.setValue(red, green, blue);
  }

  public void setValue(ConstSFColor constsfcolr)
  {
    this.impl.setValue(constsfcolr.impl);
  }

  public void setValue(SFColor sfcolr)
  {
    this.impl.setValue(sfcolr.impl);
  }

  public Object clone()
  {
    return new SFColor((org.jdesktop.j3d.loaders.vrml97.impl.SFColor)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFColor
 * JD-Core Version:    0.6.0
 */