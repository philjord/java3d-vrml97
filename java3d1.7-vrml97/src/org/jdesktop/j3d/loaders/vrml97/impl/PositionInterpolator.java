package org.jdesktop.j3d.loaders.vrml97.impl;

public class PositionInterpolator extends Interpolator
{
  SFFloat fraction;
  MFVec3f keyValue;
  SFVec3f value;
  float[] v1 = new float[3];
  float[] v2 = new float[3];

  public PositionInterpolator(Loader loader)
  {
    super(loader);
    this.fraction = new SFFloat(0.0F);
    this.key = new MFFloat();
    this.keyValue = new MFVec3f();
    this.value = new SFVec3f(0.0F, 0.0F, 0.0F);
    initFields();
  }

  PositionInterpolator(Loader loader, MFFloat key, MFVec3f keyValue)
  {
    super(loader);

    this.fraction = new SFFloat(0.0F);
    this.key = key;
    this.keyValue = keyValue;
    this.value = new SFVec3f(0.0F, 0.0F, 1.0F);

    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("fraction")) {
      if (this.key.mfloat.length > 0) {
        setIndexFract(this.fraction.value);

        int v0Base = this.iL * 3;
        int v1Base = (this.iL + 1) * 3;
        this.v1[0] = this.keyValue.value[v0Base];
        this.v1[1] = this.keyValue.value[(v0Base + 1)];
        this.v1[2] = this.keyValue.value[(v0Base + 2)];
        this.v2[0] = this.keyValue.value[v1Base];
        this.v2[1] = this.keyValue.value[(v1Base + 1)];
        this.v2[2] = this.keyValue.value[(v1Base + 2)];
        this.value.value[0] = (this.v1[0] * this.af + this.v2[0] * this.f);
        this.value.value[1] = (this.v1[1] * this.af + this.v2[1] * this.f);
        this.value.value[2] = (this.v1[2] * this.af + this.v2[2] * this.f);
      }
      this.value.route();
    }
  }

  public Object clone()
  {
    return new PositionInterpolator(this.loader, (MFFloat)this.key.clone(), (MFVec3f)this.keyValue.clone());
  }

  public String getType()
  {
    return "PositionInterpolator";
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
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.PositionInterpolator
 * JD-Core Version:    0.6.0
 */