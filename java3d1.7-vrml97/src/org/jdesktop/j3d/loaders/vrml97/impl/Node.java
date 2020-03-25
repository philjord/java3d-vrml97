package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Hashtable;
import vrml.InvalidEventInException;
import vrml.InvalidEventOutException;
import vrml.InvalidExposedFieldException;
import vrml.InvalidFieldException;

public abstract class Node extends BaseNode
  implements Cloneable, Notifier
{
  public Hashtable FieldSpec = new Hashtable(4);

  public Node(Browser browser)
  {
    super(browser);
  }

  public Node(Loader loader)
  {
    super(loader);
  }

  public Field getExposedField(String fieldName)
    throws InvalidExposedFieldException
  {
    Field f;
    if ((f = (Field)this.FieldSpec.get(fieldName)) == null) {
      throw new InvalidExposedFieldException("No field named " + fieldName);
    }
    return f;
  }

  public ConstField getEventOut(String eventOutName)
    throws InvalidEventOutException
  {
    String fieldName = Field.baseName(eventOutName);
    Field f = (Field)this.FieldSpec.get(fieldName);
    if (f == null) {
      throw new InvalidEventOutException("No field named " + fieldName);
    }
    if ((f.fieldType & 0x2) != 2) {
      throw new InvalidEventOutException("Field is not an EVENT_OUT");
    }
    if (!(f instanceof ConstField)) {
      f = f.constify();
    }
    return (ConstField)f;
  }

  public Field getEventIn(String eventInName)
    throws InvalidEventInException
  {
    String fieldName = Field.baseName(eventInName);
    Field f = (Field)this.FieldSpec.get(fieldName);
    if (f == null) {
      throw new InvalidEventInException("No field named " + eventInName);
    }
    if ((f.fieldType & 0x1) != 1) {
      throw new InvalidEventInException("Field is not an EVENT_IN");
    }
    if ((f instanceof ConstField)) {
      throw new InvalidEventInException("Field is an ConstField");
    }
    return f;
  }

  public Field getField(String fieldName)
    throws InvalidFieldException
  {
    fieldName = Field.baseName(fieldName);
    Field f;
    if ((f = (Field)this.FieldSpec.get(fieldName)) == null) {
      throw new InvalidFieldException("No field named " + fieldName);
    }
    return f;
  }

  public abstract void notifyMethod(String paramString, double paramDouble);

  abstract void initFields();

  public vrml.BaseNode wrap()
  {
    return new vrml.node.Node(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Node
 * JD-Core Version:    0.6.0
 */