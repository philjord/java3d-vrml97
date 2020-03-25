package org.jdesktop.j3d.loaders.vrml97.impl;

public class SFBool extends Field
{
  boolean value;
  boolean initValue;

  public SFBool(boolean value)
  {
    this.value = value;
    this.initValue = value;
  }

  void reset()
  {
    this.value = this.initValue;
  }

  public boolean getValue()
  {
    return this.value;
  }

  public void setValue(boolean b)
  {
    this.value = b;
    route();
  }

  public void setValue(SFBool b)
  {
    setValue(b.value);
  }

  public void setValue(ConstSFBool b)
  {
    setValue((SFBool)b.ownerField);
  }

  public synchronized Object clone()
  {
    return new SFBool(this.value);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFBool(this);
    }
    return this.constField;
  }

  public void update(Field field)
  {
    setValue(((SFBool)field).getValue());
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFBool(this);
  }

  public String toString()
  {
    return this.value + "\n";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFBool
 * JD-Core Version:    0.6.0
 */