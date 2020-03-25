package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstSFNode extends ConstField
{
  ConstSFNode(SFNode owner)
  {
    super(owner);
  }

  public ConstSFNode(BaseNode node)
  {
    super(new SFNode(node));
  }

  public BaseNode getValue()
  {
    return ((SFNode)this.ownerField).getValue();
  }

  public Object clone()
  {
    return new ConstSFNode((SFNode)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstSFNode(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstSFNode
 * JD-Core Version:    0.6.0
 */