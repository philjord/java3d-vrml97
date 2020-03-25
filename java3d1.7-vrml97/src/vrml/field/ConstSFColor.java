package vrml.field;

import vrml.ConstField;

public class ConstSFColor extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFColor impl;

  public ConstSFColor(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFColor init)
  {
    this.impl = init;
  }

  public void getValue(float[] colr)
  {
    this.impl.getValue(colr);
  }

  public float[] getValue()
  {
    return this.impl.getValue();
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

  public synchronized Object clone()
  {
    return new ConstSFColor((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFColor)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFColor
 * JD-Core Version:    0.6.0
 */