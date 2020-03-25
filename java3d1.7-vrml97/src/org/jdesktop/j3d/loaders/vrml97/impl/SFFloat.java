package org.jdesktop.j3d.loaders.vrml97.impl;

public class SFFloat extends Field
{
  float value;
  float initValue;

  public SFFloat(float f)
  {
    this.value = f;
    this.initValue = f;
  }

  void reset()
  {
    this.value = this.initValue;
  }

  public float getValue()
  {
    return this.value;
  }

  public void setValue(float f)
  {
    this.value = f;
    route();
  }

  public void setValue(ConstSFFloat f)
  {
    this.value = f.getValue();
    route();
  }

  public void setValue(SFFloat f)
  {
    this.value = f.value;
    route();
  }

  public synchronized Object clone()
  {
    return new SFFloat(this.value);
  }

  public void update(Field field)
  {
    setValue((SFFloat)field);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFFloat(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFFloat(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFFloat
 * JD-Core Version:    0.6.0
 */