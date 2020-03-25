package vrml.field;

import vrml.ConstField;

public class ConstSFNode extends ConstField
{
  org.jdesktop.j3d.loaders.vrml97.impl.ConstSFNode impl;

  public ConstSFNode(org.jdesktop.j3d.loaders.vrml97.impl.ConstSFNode init)
  {
    this.impl = init;
  }

  public vrml.BaseNode getValue()
  {
    return this.impl.getValue().wrap();
  }

  public synchronized Object clone()
  {
    return new ConstSFNode((org.jdesktop.j3d.loaders.vrml97.impl.ConstSFNode)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.ConstSFNode
 * JD-Core Version:    0.6.0
 */