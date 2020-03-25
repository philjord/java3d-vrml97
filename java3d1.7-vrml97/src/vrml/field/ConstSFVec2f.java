package vrml.field;

import vrml.ConstField;

public class ConstSFVec2f extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFVec2f impl;

  public ConstSFVec2f(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFVec2f init)
  {
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new ConstSFVec2f((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFVec2f)this.impl.clone());
  }

  public void getValue(float[] vec)
  {
    this.impl.getValue(vec);
  }

  public float[] getValue()
  {
    return this.impl.getValue();
  }

  public float getX()
  {
    return this.impl.getX();
  }

  public float getY()
  {
    return this.impl.getY();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFVec2f
 * JD-Core Version:    0.6.0
 */