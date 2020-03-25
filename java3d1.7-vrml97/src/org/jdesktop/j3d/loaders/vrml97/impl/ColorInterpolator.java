package org.jdesktop.j3d.loaders.vrml97.impl;

public class ColorInterpolator extends Interpolator
{
  SFFloat fraction;
  MFColor keyValue;
  SFColor value;

  public ColorInterpolator(Loader loader)
  {
    super(loader);
    this.fraction = new SFFloat(0.0F);
    this.key = new MFFloat();
    this.keyValue = new MFColor();
    this.value = new SFColor(1.0F, 1.0F, 1.0F);
    initFields();
  }

  ColorInterpolator(Loader loader, MFFloat key, MFColor keyValue)
  {
    super(loader);

    this.fraction = new SFFloat(0.0F);
    this.key = key;
    this.keyValue = keyValue;
    this.value = new SFColor(1.0F, 1.0F, 1.0F);

    initFields();
  }

  public Object clone()
  {
    return new ColorInterpolator(this.loader, (MFFloat)this.key.clone(), (MFColor)this.keyValue.clone());
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("fraction")) {
      if (this.key.mfloat.length > 0) {
        setIndexFract(this.fraction.value);
        float v1r = this.keyValue.vals[(this.iL * 3 + 0)];
        float v1g = this.keyValue.vals[(this.iL * 3 + 1)];
        float v1b = this.keyValue.vals[(this.iL * 3 + 2)];
        float v2r = this.keyValue.vals[((this.iL + 1) * 3 + 0)];
        float v2g = this.keyValue.vals[((this.iL + 1) * 3 + 1)];
        float v2b = this.keyValue.vals[((this.iL + 1) * 3 + 2)];
        this.value.color[0] = (v1r * this.af + v2r * this.f);
        this.value.color[1] = (v1g * this.af + v2g * this.f);
        this.value.color[2] = (v1b * this.af + v2b * this.f);
      }
      this.value.route();
    }
  }

  public String getType()
  {
    return "ColorInterpolator";
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
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ColorInterpolator
 * JD-Core Version:    0.6.0
 */