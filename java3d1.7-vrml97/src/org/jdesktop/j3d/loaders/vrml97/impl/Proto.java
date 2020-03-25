package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Proto
  implements Namespace
{
  String name;
  Loader loader;
  Vector nodes = new Vector();

  public Hashtable FieldSpec = new Hashtable(4);
  Hashtable defTable = new Hashtable();
  Vector routes = new Vector();
  ProtoInstance newInstance;

  Proto(Loader loader, String initName)
  {
    this.loader = loader;
    this.name = initName;
    if (loader.debug)
      System.out.println("PROTO " + initName);
  }

  String getName()
  {
    return this.name;
  }

  public void define(String defName, BaseNode node)
  {
    this.defTable.put(defName, node);
  }

  public BaseNode use(String defName)
  {
    BaseNode orgNode = (BaseNode)this.defTable.get(defName);
    return new ProtoUseNode(this.loader, this, orgNode);
  }

  public void addRoute(BaseNode fromNode, String fromEventOut, BaseNode toNode, String toEventIn)
  {
    if ((fromNode instanceof ProtoUseNode)) {
      fromNode = ((ProtoUseNode)fromNode).orgNode;
    }

    if ((toNode instanceof ProtoUseNode)) {
      toNode = ((ProtoUseNode)toNode).orgNode;
    }

    this.routes.addElement(new Route(fromNode, fromEventOut, toNode, toEventIn));
  }

  synchronized boolean setupIsMap(Field nodeField, String protoFieldName)
  {
    protoFieldName = Field.baseName(protoFieldName);
    Field protoField = (Field)this.FieldSpec.get(protoFieldName);
    if (protoField == null) {
      System.err.println("Proto.setupIsMap(): Can't find proto field \"" + protoFieldName + "\"");

      return false;
    }
    nodeField.update(protoField);

    if (protoField.isEventIn()) {
      if (nodeField.isEventIn()) {
        this.routes.addElement(new Route(null, protoFieldName, nodeField.ownerNode, nodeField.fieldName));
      }
      else
      {
        return false;
      }
    }
    if (protoField.isEventOut()) {
      if (nodeField.isEventOut()) {
        this.routes.addElement(new Route(nodeField.ownerNode, nodeField.fieldName, null, protoFieldName));
      }
      else
      {
        return false;
      }

    }

    if ((!protoField.isEventIn()) && (!protoField.isEventOut())) {
      this.routes.addElement(new Route(null, protoFieldName, nodeField.ownerNode, nodeField.fieldName));
    }

    return true;
  }

  public boolean setupEventIsMap(Node node, String nodeEventName, String protoFieldName)
  {
    protoFieldName = Field.baseName(protoFieldName);
    Field protoField = (Field)this.FieldSpec.get(protoFieldName);
    if (protoField == null) {
      System.err.println("Proto.setupEventIsMap: Can't find protoField " + protoFieldName);

      return false;
    }
    nodeEventName = Field.baseName(nodeEventName);
    Field nodeField = (Field)node.FieldSpec.get(nodeEventName);
    if (nodeField == null) {
      System.err.println("Proto.setupEventIsMap: Can't find nodeEventName " + nodeEventName);

      return false;
    }

    if (protoField.isEventIn()) {
      if (nodeField.isEventIn()) {
        this.routes.addElement(new Route(null, protoFieldName, nodeField.ownerNode, nodeEventName));
      }
      else
      {
        return false;
      }
    }
    if (protoField.isEventOut()) {
      if (nodeField.isEventOut()) {
        this.routes.addElement(new Route(nodeField.ownerNode, nodeEventName, null, protoFieldName));
      }
      else
      {
        return false;
      }
    }
    return true;
  }

  public void addObject(BaseNode node)
  {
    this.nodes.addElement(node);
  }

  public ProtoInstance instance()
  {
    this.newInstance = new ProtoInstance(this.loader, this.name, this);
    if (this.loader.debug) {
      System.out.println("Proto.instance() begins newInstance is " + this.newInstance.toStringId());
    }

    this.loader.beginProtoInstance(this.newInstance);

    this.newInstance.initFields();

    Group protoInstanceHandleabra = new Group(this.loader);
    protoInstanceHandleabra.initImpl();

    BaseNode[] children = new BaseNode[this.nodes.size()];
    for (int i = 0; i < this.nodes.size(); i++) {
      BaseNode baseNode = (BaseNode)this.nodes.elementAt(i);

      if (((baseNode instanceof Script)) && (!baseNode.implReady)) {
        baseNode.initImpl();
      }

      if ((baseNode instanceof VrmlSensor)) {
        this.newInstance.containsSensor = true;
        if (this.newInstance.sensors == null) {
          this.newInstance.sensors = new Vector();
        }
        this.newInstance.sensors.addElement(baseNode);
      }

      BaseNode clone = (BaseNode)baseNode.clone();
      this.loader.cleanUp();

      children[i] = clone;
      this.loader.registerClone(baseNode, clone);
    }

    protoInstanceHandleabra.addChildren.setValue(children);
    this.newInstance.instanceNode = protoInstanceHandleabra;
    if (this.newInstance.containsSensor) {
      protoInstanceHandleabra.implGroup.setPickable(true);
    }

    this.loader.cleanUp();
    this.newInstance.applyRoutes();
    if (this.loader.debug) {
      System.out.println("Proto.instance() returns instance with VRML tree:\n" + this.newInstance.instanceNode);
    }

    this.loader.endProtoInstance();
    return this.newInstance;
  }

  void registerClone(BaseNode org, BaseNode clone)
  {
    this.newInstance.nodeMapping.put(org, clone);
  }

  public void setEventIn(String eventInName, Field f)
  {
    String fieldName = Field.baseName(eventInName);
    f.init(null, this.FieldSpec, 1, fieldName);
  }

  public void setEventOut(String eventOutName, Field f)
  {
    String fieldName = Field.baseName(eventOutName);
    f.init(null, this.FieldSpec, 2, fieldName);
  }

  public void setField(String fieldName, Field f)
  {
    if (this.loader.debug) {
      System.out.println("Proto.setField(): adding field named " + fieldName);
    }

    fieldName = Field.baseName(fieldName);
    f.init(null, this.FieldSpec, 0, fieldName);
  }

  public void setExposedField(String fieldName, Field f)
  {
    fieldName = Field.baseName(fieldName);
    if (this.loader.debug) {
      System.out.println("Proto.setExposedField(): adding field named \"" + fieldName + "\"");
    }

    f.init(null, this.FieldSpec, 3, fieldName);
  }

  public String toString()
  {
    String retval = "Proto " + this.name + " [\n";
    for (Enumeration e = this.FieldSpec.keys(); e.hasMoreElements(); ) {
      String fieldName = (String)e.nextElement();
      Field field = (Field)this.FieldSpec.get(fieldName);
      retval = retval + "field ";
      String fieldType = field.getClass().getName() + " ";
      int start = fieldType.lastIndexOf(".") + 1;
      int end = fieldType.length();
      fieldType = fieldType.substring(start, end);
      retval = retval + fieldType + " " + fieldName + " " + field;
    }
    retval = retval + "]\n{\n";
    for (int i = 0; i < this.nodes.size(); i++) {
      BaseNode baseNode = (BaseNode)this.nodes.elementAt(i);
      retval = retval + baseNode + "\n";
    }
    retval = retval + "}\n";
    return retval;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Proto
 * JD-Core Version:    0.6.0
 */