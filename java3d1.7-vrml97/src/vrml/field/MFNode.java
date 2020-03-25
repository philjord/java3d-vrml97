package vrml.field;

import vrml.Field;
import vrml.MField;

public class MFNode extends MField
{
  org.jdesktop.j3d.loaders.vrml97.impl.MFNode impl;

  public MFNode(org.jdesktop.j3d.loaders.vrml97.impl.MFNode init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new MFNode((org.jdesktop.j3d.loaders.vrml97.impl.MFNode)this.impl.clone());
  }

  MFNode(vrml.BaseNode[] nodes)
  {
    super(null);
    org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes = new org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[nodes.length];

    for (int i = 0; i < nodes.length; i++) {
      implNodes[i] = Field.getImpl(nodes[i]);
    }
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFNode(implNodes);
    this.implField = this.impl;
  }

  public void getValue(vrml.BaseNode[] values)
  {
    org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes = this.impl.getValue();
    for (int i = 0; i < implNodes.length; i++)
      values[i] = implNodes[i].wrap();
  }

  public vrml.BaseNode[] getValue()
  {
    org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes = this.impl.getValue();
    vrml.BaseNode[] nodes = new vrml.BaseNode[implNodes.length];
    for (int i = 0; i < implNodes.length; i++) {
      nodes[i] = implNodes[i].wrap();
    }
    return nodes;
  }

  public void setValue(vrml.BaseNode[] values)
  {
    org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes = new org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[values.length];

    for (int i = 0; i < values.length; i++) {
      implNodes[i] = Field.getImpl(values[i]);
    }
    this.impl.setValue(implNodes);
  }

  public void setValue(int size, vrml.BaseNode[] values)
  {
    org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes = new org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[size];

    for (int i = 0; i < size; i++) {
      implNodes[i] = Field.getImpl(values[i]);
    }
    this.impl.setValue(size, implNodes);
  }

  public void setValue(ConstMFNode n)
  {
    this.impl.setValue(n.impl);
  }

  public vrml.BaseNode get1Value(int index)
  {
    return this.impl.get1Value(index).wrap();
  }

  public void set1Value(int index, vrml.BaseNode n)
  {
    this.impl.set1Value(index, Field.getImpl(n));
  }

  public void set1Value(int index, ConstSFNode n)
  {
    this.impl.set1Value(index, n.impl);
  }

  public void set1Value(int index, SFNode n)
  {
    this.impl.set1Value(index, n.impl);
  }

  public void addValue(vrml.BaseNode n)
  {
    this.impl.addValue(Field.getImpl(n));
  }

  public void addValue(ConstSFNode n)
  {
    this.impl.addValue(n.impl);
  }

  public void addValue(SFNode n)
  {
    this.impl.addValue(n.impl);
  }

  public void insertValue(int index, vrml.BaseNode n)
  {
    this.impl.insertValue(index, Field.getImpl(n));
  }

  public void insertValue(int index, ConstSFNode n)
  {
    this.impl.insertValue(index, n.impl);
  }

  public void insertValue(int index, SFNode n)
  {
    this.impl.insertValue(index, n.impl);
  }

  public int getSize()
  {
    return this.impl.getSize();
  }

  public void clear()
  {
    this.impl.clear();
  }

  public void delete(int n)
  {
    this.impl.delete(n);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.MFNode
 * JD-Core Version:    0.6.0
 */