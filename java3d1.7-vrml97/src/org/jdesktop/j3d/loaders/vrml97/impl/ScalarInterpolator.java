package org.jdesktop.j3d.loaders.vrml97.impl;

public class ScalarInterpolator extends Interpolator
{
  SFFloat fraction;
  MFFloat keyValue;
  SFFloat value;
  float v1;
  float v2;

  public ScalarInterpolator(Loader loader)
  {
    super(loader);
    this.fraction = new SFFloat(0.0F);
    this.key = new MFFloat();
    this.keyValue = new MFFloat();
    this.value = new SFFloat(0.0F);
    initFields();
  }

  ScalarInterpolator(Loader loader, MFFloat key, MFFloat keyValue)
  {
    super(loader);

    this.fraction = new SFFloat(0.0F);
    this.key = key;
    this.keyValue = keyValue;
    this.value = new SFFloat(0.0F);

    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("fraction")) {
      if (this.key.mfloat.length > 0) {
        setIndexFract(this.fraction.value);
        this.v1 = this.keyValue.mfloat[this.iL];
        this.v2 = this.keyValue.mfloat[(this.iL + 1)];
        this.value.value = (this.v1 * this.af + this.v2 * this.f);
      }
      this.value.route();
    }
  }

  public Object clone()
  {
    return new ScalarInterpolator(this.loader, (MFFloat)this.key.clone(), (MFFloat)this.keyValue.clone());
  }

  public String getType()
  {
    return "ScalarInterpolator";
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
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ScalarInterpolator
 * JD-Core Version:    0.6.0
 */