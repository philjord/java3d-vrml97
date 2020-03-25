package org.jdesktop.j3d.loaders.vrml97.impl;

public class SFRotation extends Field
{
  float[] rot = new float[4];

  public SFRotation()
  {
    this.rot[0] = 0.0F;
    this.rot[1] = 0.0F;
    this.rot[2] = 1.0F;
    this.rot[3] = 0.0F;
  }

  public SFRotation(float x, float y, float z, float axisAngle)
  {
    this.rot[0] = x;
    this.rot[1] = y;
    this.rot[2] = z;
    this.rot[3] = axisAngle;
  }

  public SFRotation(float[] axisAngle)
  {
    this.rot[0] = axisAngle[0];
    this.rot[1] = axisAngle[1];
    this.rot[2] = axisAngle[2];
    this.rot[3] = axisAngle[3];
  }

  public void getValue(float[] rotation)
  {
    System.arraycopy(this.rot, 0, rotation, 0, 4);
  }

  public float[] getValue()
  {
    return this.rot;
  }

  public void setValue(float[] r)
  {
    System.arraycopy(r, 0, this.rot, 0, 4);
    route();
  }

  public void setValue(float xAxis, float yAxis, float zAxis, float angle)
  {
    this.rot[0] = xAxis;
    this.rot[1] = yAxis;
    this.rot[2] = zAxis;
    this.rot[3] = angle;

    route();
  }

  public void setValue(ConstSFRotation rotation)
  {
    setValue((SFRotation)rotation.ownerField);
  }

  public void setValue(SFRotation rotation)
  {
    setValue(rotation.rot);
  }

  public Object clone()
  {
    return new SFRotation(this.rot);
  }

  public void update(Field field)
  {
    setValue((SFRotation)field);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFRotation(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFRotation(this);
  }

  void route()
  {
    double normalizer = Math.sqrt(this.rot[0] * this.rot[0] + this.rot[1] * this.rot[1] + this.rot[2] * this.rot[2]);
    if (normalizer < 0.001000000047497451D) {
      this.rot[0] = 0.0F;
      this.rot[1] = 1.0F;
      this.rot[2] = 0.0F;
    }
    else
    {
      int tmp83_82 = 0;
      float[] tmp83_79 = this.rot; tmp83_79[tmp83_82] = (float)(tmp83_79[tmp83_82] / normalizer);
      int tmp95_94 = 1;
      float[] tmp95_91 = this.rot; tmp95_91[tmp95_94] = (float)(tmp95_91[tmp95_94] / normalizer);
      int tmp107_106 = 2;
      float[] tmp107_103 = this.rot; tmp107_103[tmp107_106] = (float)(tmp107_103[tmp107_106] / normalizer);
    }
    super.route();
  }

  public String toString()
  {
    return this.rot[0] + " " + this.rot[1] + " " + this.rot[2] + " " + this.rot[3] + "\n";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFRotation
 * JD-Core Version:    0.6.0
 */