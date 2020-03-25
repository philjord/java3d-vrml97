package org.jdesktop.j3d.loaders.vrml97.impl;

public class CoordinateInterpolator extends Interpolator
{
  SFFloat fraction;
  MFVec3f keyValue;
  MFVec3f value;

  public CoordinateInterpolator(Loader loader)
  {
    super(loader);
    this.fraction = new SFFloat(0.0F);

    this.key = new MFFloat();
    this.keyValue = new MFVec3f();
    this.value = new MFVec3f();

    initFields();
  }

  public CoordinateInterpolator(Loader loader, SFFloat fraction, MFFloat key, MFVec3f keyValue)
  {
    super(loader);

    this.fraction = fraction;
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
        if (this.value.size != valsPerKey) {
          this.value.checkSize(valsPerKey, false);
          this.value.size = valsPerKey;
        }
        int indL = this.iL * valsPerKey;
        int indH = (this.iL + 1) * valsPerKey;

        float[] vals = this.keyValue.value;
        for (int j = 0; j < valsPerKey; j += 3)
        {
          int v1xi = indL + j;
          int v1yi = v1xi + 1;
          int v1zi = v1yi + 1;

          int v2xi = indH + j;
          int v2yi = v2xi + 1;
          int v2zi = v2yi + 1;
          float v1x = vals[v1xi];
          float v1y = vals[v1yi];
          float v1z = vals[v1zi];
          float v2x = vals[v2xi];
          float v2y = vals[v2yi];
          float v2z = vals[v2zi];
          this.value.value[(j + 0)] = (v1x * this.af + v2x * this.f);
          this.value.value[(j + 1)] = (v1y * this.af + v2y * this.f);
          this.value.value[(j + 2)] = (v1z * this.af + v2z * this.f);
        }
      }
      this.value.route();
    }
  }

  public String getType()
  {
    return "CoordinateInterpolator";
  }

  public Object clone()
  {
    return new CoordinateInterpolator(this.loader, (SFFloat)this.fraction.clone(), this.key, this.keyValue);
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
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.CoordinateInterpolator
 * JD-Core Version:    0.6.0
 */