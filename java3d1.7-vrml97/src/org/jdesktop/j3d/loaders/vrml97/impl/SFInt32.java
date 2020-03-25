package org.jdesktop.j3d.loaders.vrml97.impl;

public class SFInt32 extends Field
{
  int value;

  public SFInt32(int value)
  {
    this.value = value;
  }

  public int getValue()
  {
    return this.value;
  }

  public void setValue(int i)
  {
    this.value = i;
    route();
  }

  public void setValue(SFInt32 i)
  {
    this.value = i.getValue();
    route();
  }

  public void setValue(ConstSFInt32 i)
  {
    setValue((SFInt32)i.ownerField);
  }

  public synchronized Object clone()
  {
    return new SFInt32(this.value);
  }

  public void update(Field field)
  {
    setValue((SFInt32)field);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFInt32(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFInt32(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFInt32
 * JD-Core Version:    0.6.0
 */