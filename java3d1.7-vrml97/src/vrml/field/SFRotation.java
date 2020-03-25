package vrml.field;

import vrml.Field;

public class SFRotation extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFRotation impl;

  public SFRotation(org.jdesktop.j3d.loaders.vrml97.impl.SFRotation init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFRotation((org.jdesktop.j3d.loaders.vrml97.impl.SFRotation)this.impl.clone());
  }

  public SFRotation(float x, float y, float z, float axisAngle)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFRotation(x, y, z, axisAngle);

    this.implField = this.impl;
  }

  public SFRotation(float[] axisAngle)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFRotation(axisAngle);
    this.implField = this.impl;
  }

  public void getValue(float[] rotation)
  {
    this.impl.getValue(rotation);
  }

  public float[] getValue()
  {
    return this.impl.getValue();
  }

  public void setValue(float[] r)
  {
    this.impl.setValue(r);
  }

  public void setValue(float xAxis, float yAxis, float zAxis, float angle)
  {
    this.impl.setValue(xAxis, yAxis, zAxis, angle);
  }

  public void setValue(ConstSFRotation rotation)
  {
    this.impl.setValue(rotation.impl);
  }

  public void setValue(SFRotation rotation)
  {
    this.impl.setValue(rotation.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFRotation
 * JD-Core Version:    0.6.0
 */