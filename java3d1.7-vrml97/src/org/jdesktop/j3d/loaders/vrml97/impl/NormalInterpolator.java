package org.jdesktop.j3d.loaders.vrml97.impl;

public class NormalInterpolator extends Interpolator
{
  SFFloat fraction;
  MFVec3f keyValue;
  MFVec3f value;

  public NormalInterpolator(Loader loader)
  {
    super(loader);
    this.fraction = new SFFloat(0.0F);

    this.key = new MFFloat();
    this.keyValue = new MFVec3f();

    initFields();
  }

  public NormalInterpolator(Loader loader, SFFloat fraction, MFFloat key, MFVec3f keyValue)
  {
    super(loader);
    this.fraction = new SFFloat(0.0F);

    this.key = key;
    this.keyValue = keyValue;

    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("fraction")) {
      if (this.key.mfloat.length > 0) {
        setIndexFract(this.fraction.value);
        int valsPerKey = this.keyValue.size / this.key.mfloat.length;
        if (this.value.size != valsPerKey * 3) {
          this.value.checkSize(valsPerKey * 3, false);
          this.value.size = (valsPerKey * 3);
        }

        for (int j = 0; j < valsPerKey; j++) {
          int val1 = this.iL * valsPerKey * 3;
          int val2 = (this.iL + 1) * valsPerKey * 3;
          float v1x = this.keyValue.value[((val1 + j) * 3 + 0)];
          float v1y = this.keyValue.value[((val1 + j) * 3 + 1)];
          float v1z = this.keyValue.value[((val1 + j) * 3 + 2)];
          float v2x = this.keyValue.value[((val2 + j) * 3 + 0)];
          float v2y = this.keyValue.value[((val2 + j) * 3 + 1)];
          float v2z = this.keyValue.value[((val2 + j) * 3 + 2)];
          this.value.value[(j * 3 + 0)] = (v1x * this.af + v2x * this.f);
          this.value.value[(j * 3 + 1)] = (v1y * this.af + v2y * this.f);
          this.value.value[(j * 3 + 2)] = (v1z * this.af + v2z * this.f);
        }
      }
      this.value.route();
    }
  }

  public String getType()
  {
    return "NormalInterpolator";
  }

  public Object clone()
  {
    return new NormalInterpolator(this.loader, (SFFloat)this.fraction.clone(), this.key, this.keyValue);
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
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.NormalInterpolator
 * JD-Core Version:    0.6.0
 */