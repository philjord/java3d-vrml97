package org.jdesktop.j3d.loaders.vrml97.impl;

public class SFColor extends Field
{
  float[] color;

  public SFColor(float red, float green, float blue)
  {
    this.color = new float[3];
    this.color[0] = red;
    this.color[1] = green;
    this.color[2] = blue;
  }

  public SFColor(float[] initColor)
  {
    this.color = new float[3];
    setValue(initColor);
  }

  public void getValue(float[] colr)
  {
    System.arraycopy(this.color, 0, colr, 0, 3);
  }

  public float[] getValue()
  {
    return this.color;
  }

  public void setRed(float red)
  {
    this.color[0] = red;
    route();
  }

  public void setGreen(float green)
  {
    this.color[1] = green;
    route();
  }

  public void setBlue(float blue)
  {
    this.color[2] = blue;
    route();
  }

  public float getRed()
  {
    return this.color[0];
  }

  public float getGreen()
  {
    return this.color[1];
  }

  public float getBlue()
  {
    return this.color[2];
  }

  public void setValue(float[] colrs)
  {
    System.arraycopy(colrs, 0, this.color, 0, 3);
    route();
  }

  public void setValue(float red, float green, float blue)
  {
    this.color[0] = red;
    this.color[1] = green;
    this.color[2] = blue;

    route();
  }

  public void setValue(ConstSFColor constsfcolr)
  {
    setValue((SFColor)constsfcolr.ownerField);
  }

  public void setValue(SFColor sfcolr)
  {
    setValue(sfcolr.color);
  }

  public synchronized Object clone()
  {
    return new SFColor(this.color);
  }

  public void update(Field field)
  {
    setValue((SFColor)field);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFColor(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFColor(this);
  }

  public String toString()
  {
    return this.color[0] + " " + this.color[1] + " " + this.color[2] + "\n";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFColor
 * JD-Core Version:    0.6.0
 */