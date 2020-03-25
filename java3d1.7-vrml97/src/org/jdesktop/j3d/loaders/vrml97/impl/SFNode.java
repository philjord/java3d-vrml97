package org.jdesktop.j3d.loaders.vrml97.impl;

public class SFNode extends Field
{
  BaseNode node;
  BaseNode initNode;

  public SFNode()
  {
    this.node = null;
    this.initNode = null;
  }

  public SFNode(BaseNode node)
  {
    setValue(node);
    this.initNode = node;
  }

  void reset()
  {
    this.node = this.initNode;
  }

  public BaseNode getValue()
  {
    return this.node;
  }

  public void setValue(BaseNode node)
  {
    this.node = node;
    route();
  }

  public void setValue(SFNode sfnode)
  {
    setValue(sfnode.node);
  }

  public void setValue(ConstSFNode csfnode)
  {
    setValue((SFNode)csfnode.ownerField);
  }

  public synchronized Object clone()
  {
    SFNode clone = new SFNode(this.node);
    if (this.node != null) {
      BaseNode cloneNode = (BaseNode)this.node.clone();
      clone.node = cloneNode;
      this.node.loader.registerClone(this.node, cloneNode);
      this.node.loader.cleanUp();
    }
    return clone;
  }

  public void update(Field field)
  {
    setValue((SFNode)field);
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstSFNode(this);
    }
    return this.constField;
  }

  public vrml.Field wrap()
  {
    return new vrml.field.SFNode(this);
  }

  public String toString()
  {
    return this.node + "\n";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SFNode
 * JD-Core Version:    0.6.0
 */