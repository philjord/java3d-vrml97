package org.jdesktop.j3d.loaders.vrml97.impl;

import vrml.Field;

public class ConstMFNode extends ConstMField
{
  ConstMFNode(MFNode owner)
  {
    super(owner);
  }

  public ConstMFNode(BaseNode[] values)
  {
    super(new MFNode(values));
  }

  public void getValue(BaseNode[] values)
  {
    ((MFNode)this.ownerField).getValue(values);
  }

  public BaseNode[] getValue()
  {
    return ((MFNode)this.ownerField).getValue();
  }

  public BaseNode get1Value(int index)
  {
    return ((MFNode)this.ownerField).get1Value(index);
  }

  public Object clone()
  {
    return new ConstMFNode((MFNode)this.ownerField);
  }

  public Field wrap()
  {
    return new vrml.field.ConstMFNode(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ConstMFNode
 * JD-Core Version:    0.6.0
 */