package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Hashtable;

public class ProtoUseNode extends BaseNode
{
  BaseNode orgNode;
  Proto proto;

  ProtoUseNode(Loader loader, Proto proto, BaseNode orgNode)
  {
    super(loader);
    this.proto = proto;
    this.orgNode = orgNode;
  }

  public String getType()
  {
    return "internal: ProtoUseNode";
  }

  public void notifyMethod(String s, double time)
  {
  }

  public vrml.BaseNode wrap()
  {
    return null;
  }

  public Object clone()
  {
    BaseNode newNode = (BaseNode)this.proto.newInstance.nodeMapping.get(this.orgNode);
    if (this.loader.debug) {
      System.out.println("ProtoUseNode.clone(): orgNode=" + this.orgNode + " new=" + newNode);
    }

    if (newNode == null) {
      System.err.println("ProtoUseNode: new node not found!");
    }
    return newNode;
  }

  public Field getField(String fieldName)
  {
    return null;
  }

  public String toStringBody()
  {
    return "USE " + this.orgNode.defName;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ProtoUseNode
 * JD-Core Version:    0.6.0
 */