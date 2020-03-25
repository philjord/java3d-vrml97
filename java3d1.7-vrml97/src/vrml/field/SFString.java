package vrml.field;

import vrml.Field;

public class SFString extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFString impl;

  public SFString(org.jdesktop.j3d.loaders.vrml97.impl.SFString init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFString((org.jdesktop.j3d.loaders.vrml97.impl.SFString)this.impl.clone());
  }

  public SFString(String string)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFString(string);
    this.implField = this.impl;
  }

  public String getValue()
  {
    return this.impl.getValue();
  }

  public void setValue(String s)
  {
    this.impl.setValue(s);
  }

  public void setValue(ConstSFString s)
  {
    this.impl.setValue(s.impl);
  }

  public void setValue(SFString s)
  {
    this.impl.setValue(s.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFString
 * JD-Core Version:    0.6.0
 */