package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public abstract class Field
{
  String fieldName = new String("?");
  BaseNode ownerNode = null;
  double lastUpdate = -1.0D;
  Vector connections = null;
  public ConstField constField;
  public int fieldType = 3;
  static final int FIELD = 0;
  static final int EVENT_IN = 1;
  static final int EVENT_OUT = 2;
  static final int EXPOSED_FIELD = 3;
  static final boolean printRoutes = false;

  void init(BaseNode node, Hashtable fieldSpec, int fieldType, String fieldName)
  {
    this.ownerNode = node;
    this.fieldType = fieldType;
    this.fieldName = fieldName;
    if (fieldSpec != null)
      fieldSpec.put(fieldName, this);
  }

  boolean isEventOut()
  {
    return (this.fieldType & 0x2) != 0;
  }

  boolean isEventIn()
  {
    return (this.fieldType & 0x1) != 0;
  }

  synchronized void route()
  {
    double eventTime;
    if (this.ownerNode != null)
    {
      if (this.ownerNode.browser != null) {
        eventTime = this.ownerNode.browser.beginRoute();
      }
      else
      {
        eventTime = 0.0D;
      }

      if ((isEventIn()) && (this.ownerNode.implReady))
      {
        this.ownerNode.notifyMethod(this.fieldName, eventTime);
      }
    }
    else {
      eventTime = Time.getNow();
    }

    if (this.lastUpdate != eventTime) {
      this.lastUpdate = eventTime;
      if (this.connections != null) {
        Enumeration e = this.connections.elements();
        while (e.hasMoreElements()) {
          Field routeField = (Field)e.nextElement();

          if (routeField.lastUpdate != eventTime)
          {
            routeField.update(this);
          }

        }

      }

    }

    if ((this.ownerNode != null) && (this.ownerNode.browser != null))
      this.ownerNode.browser.endRoute();
  }

  void connectToField(Field field)
  {
    if (this.connections == null) {
      this.connections = new Vector();
    }
    if (!this.connections.contains(field))
      this.connections.addElement(field);
  }

  void deleteConnection(Field field)
  {
    this.connections.removeElement(field);
  }

  abstract void update(Field paramField);

  abstract ConstField constify();

  public abstract Object clone();

  public abstract vrml.Field wrap();

  public String toStringId()
  {
    return getClass().getName() + "@" + Integer.toHexString(hashCode());
  }

  public void markWriteable()
  {
    this.ownerNode.notifyMethod("route_" + this.fieldName, 0.0D);
  }

  public static String baseName(String fieldName)
  {
    String newName = fieldName;
    if (fieldName.startsWith("set_")) {
      newName = fieldName.substring(4);
    }

    if (fieldName.endsWith("_changed")) {
      newName = fieldName.substring(0, fieldName.indexOf("_changed"));
    }

    return newName;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Field
 * JD-Core Version:    0.6.0
 */