package vrml.field;

import vrml.Field;

public class SFNode extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFNode impl;

  public SFNode(org.jdesktop.j3d.loaders.vrml97.impl.SFNode init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFNode((org.jdesktop.j3d.loaders.vrml97.impl.SFNode)this.impl.clone());
  }

  public SFNode(vrml.BaseNode node)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFNode(Field.getImpl(node));

    this.implField = this.impl;
  }

  public vrml.BaseNode getValue()
  {
    return this.impl.getValue().wrap();
  }

  public void setValue(vrml.BaseNode node)
  {
    this.impl.setValue(Field.getImpl(node));
  }

  public void setValue(SFNode sfnode)
  {
    this.impl.setValue(sfnode.impl);
  }

  public void setValue(ConstSFNode csfnode)
  {
    this.impl.setValue(csfnode.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFNode
 * JD-Core Version:    0.6.0
 */