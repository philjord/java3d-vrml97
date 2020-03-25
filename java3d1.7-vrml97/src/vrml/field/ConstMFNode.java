package vrml.field;

import vrml.ConstMField;

public class ConstMFNode extends ConstMField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstMFNode impl;

  public ConstMFNode(org.jdesktop.j3d.loaders.vrml97.impl.ConstMFNode init)
  {
    this.impl = init;
  }

  public void getValue(vrml.BaseNode[] values)
  {
    org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implValues = new org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[values.length];

    this.impl.getValue(implValues);
    for (int i = 0; i < values.length; i++)
      values[i] = implValues[i].wrap();
  }

  public vrml.BaseNode get1Value(int index)
  {
    return this.impl.get1Value(index).wrap();
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public Object clone()
  {
    return new ConstMFNode((org.jdesktop.j3d.loaders.vrml97.impl.ConstMFNode)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstMFNode
 * JD-Core Version:    0.6.0
 */