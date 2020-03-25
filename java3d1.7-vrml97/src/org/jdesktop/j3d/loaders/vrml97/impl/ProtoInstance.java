package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ProtoInstance extends Node
  implements Namespace
{
  String name;
  Proto proto;
  Hashtable defTable = new Hashtable();
  BaseNode instanceNode;
  Vector routes;
  Hashtable fieldMapping = new Hashtable();
  Hashtable nodeMapping = new Hashtable();

  boolean containsSensor = false;
  Vector sensors;

  ProtoInstance(Loader loader, String name, Proto proto)
  {
    super(loader);
    this.name = name;
    this.proto = proto;
    this.routes = proto.routes;
  }

  Field addField(Field field)
  {
    Field newField;
    if (!this.fieldMapping.containsKey(field)) {
      newField = (Field)field.clone();
      this.fieldMapping.put(field, newField);
      newField.init(this, this.FieldSpec, field.fieldType, field.fieldName);
    }
    else {
      newField = (Field)this.fieldMapping.get(field);
    }
    return newField;
  }

  void initFields()
  {
    for (Enumeration e = this.proto.FieldSpec.keys(); e.hasMoreElements(); ) {
      String fieldName = (String)e.nextElement();
      Field field = (Field)this.proto.FieldSpec.get(fieldName);

      if ((field instanceof ConstField)) {
        Field newField = addField(((ConstField)field).ownerField);
        this.FieldSpec.put(fieldName, newField.constify());
      }
      else {
        Field newField = addField(field);
        this.FieldSpec.put(fieldName, newField);
      }
    }
  }

  public void define(String defName, BaseNode node)
  {
    this.defTable.put(defName, node);
  }

  public BaseNode use(String defName)
  {
    BaseNode retval = (BaseNode)this.defTable.get(defName);

    retval.registerUse(this.loader.scene);
    return retval;
  }

  BaseNode updateNode(BaseNode node)
  {
    if (node == null) {
      return this;
    }
    BaseNode retval = (BaseNode)this.nodeMapping.get(node);

    if (retval == null) {
      System.err.println("ProtoInstance: Unable to update node reference: " + node.toStringId());
    }

    return retval;
  }

  void applyRoutes()
  {
    for (Enumeration e = this.routes.elements(); e.hasMoreElements(); ) {
      Route route = (Route)e.nextElement();

      BaseNode fromNode = updateNode(route.fromNode);
      BaseNode toNode = updateNode(route.toNode);

      if ((!route.fromField.equals("?")) && (!route.toField.equals("?")))
      {
        this.loader.connect(fromNode, route.fromField, toNode, route.toField, false);
      }
    }
  }

  BaseNode instanceNode()
  {
    return this.instanceNode;
  }

  public org.jogamp.java3d.Node getImplNode()
  {
    return this.instanceNode.getImplNode();
  }

  public void notifyMethod(String eventInName, double time)
  {
  }

  public String getType()
  {
    return this.name;
  }

  public Object clone()
  {
    System.err.println("Warning: clone called on a ProtoInstance!");
    return null;
  }

  public String toStringBody()
  {
    return "PROTO " + this.name + " {\n" + "}\n";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ProtoInstance
 * JD-Core Version:    0.6.0
 */