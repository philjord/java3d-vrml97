package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;

public class MFNode extends MField
{
  BaseNode[] nodes;

  public MFNode()
  {
    this.nodes = new BaseNode[0];
  }

  public MFNode(BaseNode[] values)
  {
    this.nodes = new BaseNode[values.length];
    System.arraycopy(values, 0, this.nodes, 0, values.length);
  }

  public void getValue(BaseNode[] values)
  {
    System.arraycopy(this.nodes, 0, values, 0, this.nodes.length);
  }

  public BaseNode[] getValue()
  {
    return this.nodes;
  }

  public void setValue(BaseNode[] values)
  {
    this.nodes = new BaseNode[values.length];
    System.arraycopy(values, 0, this.nodes, 0, values.length);
    route();
  }

  public void setValue(int size, BaseNode[] value)
  {
    setValue(value);
  }

  public void setValue(MFNode node)
  {
    setValue((BaseNode[])node.nodes);
  }

  public void setValue(ConstMFNode constNode)
  {
    setValue((MFNode)constNode.ownerField);
  }

  public BaseNode get1Value(int index)
  {
    return this.nodes[index];
  }

  public void set1Value(int index, BaseNode f)
  {
    this.nodes[index] = f;
    route();
  }

  public void set1Value(int index, ConstSFNode csfn)
  {
    set1Value(index, (SFNode)csfn.ownerField);
  }

  public void set1Value(int index, SFNode sfnod)
  {
    set1Value(index, sfnod.node);
  }

  public void addValue(BaseNode f)
  {
    BaseNode[] temp = new BaseNode[this.nodes.length + 1];
    for (int i = 0; i < temp.length - 1; i++) {
      temp[i] = this.nodes[i];
    }
    temp[(temp.length - 1)] = f;
    this.nodes = temp;

    route();
  }

  public void addValue(ConstSFNode constsfnod)
  {
    addValue((SFNode)constsfnod.ownerField);
  }

  public void addValue(SFNode sfnod)
  {
    addValue(sfnod.node);
  }

  public void insertValue(int index, BaseNode f)
  {
    BaseNode[] temp = new BaseNode[this.nodes.length + 1];
    for (int i = 0; i < index; i++) {
      temp[i] = this.nodes[i];
    }
    temp[index] = f;
    for (int i = index + 1; i < temp.length + 1; i++) {
      temp[i] = this.nodes[(i - 1)];
    }
    this.nodes = temp;
    route();
  }

  public void insertValue(int index, ConstSFNode constsfnod)
  {
    insertValue(index, (SFNode)constsfnod.ownerField);
  }

  public void insertValue(int index, SFNode constsfnod)
  {
    insertValue(index, constsfnod.node);
  }

  public synchronized void update(Field field)
  {
    setValue((MFNode)field);
  }

  public synchronized Object clone()
  {
    MFNode ref = new MFNode(this.nodes);

    for (int i = 0; i < ref.nodes.length; i++) {
      if (this.nodes[i] != null) {
        BaseNode cloneNode = (BaseNode)this.nodes[i].clone();
        ref.nodes[i] = cloneNode;
        if (this.nodes[i].loader.debug) {
          System.out.println("MFNode.clone(): child " + i + " = " + cloneNode.toStringId() + " = " + cloneNode);
        }

        this.nodes[i].loader.registerClone(this.nodes[i], cloneNode);
        this.nodes[i].loader.cleanUp();
      }
    }
    return ref;
  }

  public synchronized ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstMFNode(this);
    }
    return this.constField;
  }

  public int getSize()
  {
    return this.nodes.length;
  }

  public void clear()
  {
    this.nodes = null;
  }

  public void delete(int n)
  {
  }

  public vrml.Field wrap()
  {
    return new vrml.field.MFNode(this);
  }

  public String toString()
  {
    String retval = "[\n";
    for (int i = 0; i < this.nodes.length; i++) {
      retval = retval + this.nodes[i] + "\n";
    }
    retval = retval + "]\n";
    return retval;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.MFNode
 * JD-Core Version:    0.6.0
 */