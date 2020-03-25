package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFBool extends ConstField
{
  ConstSFBool(SFBool owner)
  {
    super(owner);
  }

  public ConstSFBool(boolean bool)
  {
    super(new SFBool(bool));
  }

  public boolean getValue()
  {
    return ((SFBool)this.ownerField).getValue();
  }

  public synchronized Object clone()
  {
    return new ConstSFBool((SFBool)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFBool(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFBool
 * JD-Core Version:    0.6.0
 */