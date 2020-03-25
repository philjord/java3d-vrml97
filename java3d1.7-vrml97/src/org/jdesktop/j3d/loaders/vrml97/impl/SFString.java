package org.jdesktop.j3d.loaders.vrml97.impl;

public class SFString extends Field
{
  String string;

  public SFString()
  {
    setValue("");
  }

  public SFString(String string)
  {
    setValue(string);
  }

  public String getValue()
  {
    return this.string;
  }

  public void setValue(String s)
  {
    this.string = new String(s);
    route();
  }

  public void setValue(ConstSFString s)
  {
    setValue((SFString)s.ownerField);
  }

  public void setValue(SFString s)
  {
    setValue(s.string);
  }

  public synchronized Object clone()
  {
    return new SFString(this.string);
  }

  public void update(Field field)
  {
    setValue((SFString)field);
  }

  public ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFString(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFString(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFString
 * JD-Core Version:    0.6.0
 */