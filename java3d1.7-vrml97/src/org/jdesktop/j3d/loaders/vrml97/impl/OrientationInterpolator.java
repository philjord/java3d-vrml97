package org.jdesktop.j3d.loaders.vrml97.impl;

public class OrientationInterpolator extends Interpolator
{
  SFFloat fraction;
  MFRotation keyValue;
  SFRotation value;
  float[] v1;
  float[] v2;
  int i;
  int j;

  public OrientationInterpolator(Loader loader)
  {
    super(loader);
    this.fraction = new SFFloat(0.0F);
    this.key = new MFFloat();
    this.keyValue = new MFRotation();
    this.value = new SFRotation(0.0F, 0.0F, 1.0F, 0.0F);
    initFields();
  }

  OrientationInterpolator(Loader loader, MFFloat key, MFRotation keyValue)
  {
    super(loader);

    this.fraction = new SFFloat(0.0F);
    this.key = key;
    this.keyValue = keyValue;
    this.value = new SFRotation(0.0F, 0.0F, 1.0F, 0.0F);
    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("fraction"))
    {
      if (this.key.mfloat.length > 0) {
        setIndexFract(this.fraction.value);

        this.v1 = this.keyValue.rots[this.iL].rot;
        this.v2 = this.keyValue.rots[(this.iL + 1)].rot;
        this.value.rot[0] = (this.v1[0] * this.af + this.v2[0] * this.f);
        this.value.rot[1] = (this.v1[1] * this.af + this.v2[1] * this.f);
        this.value.rot[2] = (this.v1[2] * this.af + this.v2[2] * this.f);
        this.value.rot[3] = (this.v1[3] * this.af + this.v2[3] * this.f);
      }
      this.value.route();
    }
  }

  public Object clone()
  {
    return new OrientationInterpolator(this.loader, (MFFloat)this.key.clone(), (MFRotation)this.keyValue.clone());
  }

  public String getType()
  {
    return "OrientationInterpolator";
  }

  void initFields()
  {
    this.fraction.init(this, this.FieldSpec, 1, "fraction");
    this.key.init(this, this.FieldSpec, 3, "key");
    this.keyValue.init(this, this.FieldSpec, 3, "keyValue");
    this.value.init(this, this.FieldSpec, 2, "value");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.OrientationInterpolator
 * JD-Core Version:    0.6.0
 */