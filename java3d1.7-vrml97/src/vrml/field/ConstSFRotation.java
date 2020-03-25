package vrml.field;

import vrml.ConstField;

public class ConstSFRotation extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFRotation impl;

  public ConstSFRotation(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFRotation init)
  {
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new ConstSFRotation((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFRotation)this.impl.clone());
  }

  public void getValue(float[] axisAngle)
  {
    this.impl.getValue(axisAngle);
  }

  public float[] getValue()
  {
    return this.impl.getValue();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFRotation
 * JD-Core Version:    0.6.0
 */