package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFString extends ConstField
{
  ConstSFString(SFString owner)
  {
    super(owner);
  }

  ConstSFString(String string)
  {
    super(new SFString(string));
  }

  public String getValue()
  {
    return ((SFString)this.ownerField).getValue();
  }

  public synchronized Object clone()
  {
    return new ConstSFString((SFString)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFString(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFString
 * JD-Core Version:    0.6.0
 */