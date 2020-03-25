package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

class RoutePrinter
{
  Vector fieldsVisited = new Vector();
  Vector fieldsToVisit = new Vector();
  Vector nodesVisited = new Vector();
  Vector nodesToVisit = new Vector();
  String indent;

  String fixedLengthString(String in, int length)
  {
    while (in.length() < length) {
      in = in + " ";
    }
    return in;
  }

  private void printRoutesField(Field curField)
  {
    if (this.fieldsVisited.contains(curField)) {
      return;
    }
    this.fieldsVisited.addElement(curField);
    this.fieldsToVisit.removeElement(curField);
    if ((curField.connections != null) && (curField.connections.size() != 0))
    {
      System.out.println("Field " + curField.fieldName + ": " + curField);
      System.out.println(" owned by " + curField.ownerNode);
      System.out.println(" connects to " + curField.connections.size() + " field(s):");

      Enumeration e = curField.connections.elements();
      while (e.hasMoreElements()) {
        Field newField = (Field)e.nextElement();
        System.out.println("    " + fixedLengthString(newField.fieldName, 20) + " " + newField + "\n        owned by " + newField.ownerNode.defName + " " + newField.ownerNode);

        if ((!this.fieldsVisited.contains(newField)) && (!this.fieldsToVisit.contains(newField)))
        {
          this.fieldsToVisit.addElement(newField);
        }

      }

    }

    if (curField.ownerNode != null)
      printRoutesNode(curField.ownerNode);
  }

  private void printRoutesNode(BaseNode curNode)
  {
    if (this.nodesVisited.contains(curNode)) {
      return;
    }
    this.nodesVisited.addElement(curNode);
    this.nodesToVisit.removeElement(curNode);
    System.out.println("Node " + curNode + " named " + curNode.defName + "\n  has fields: ");

    Hashtable fieldSpec = null;
    if ((curNode instanceof Node)) {
      fieldSpec = ((Node)curNode).FieldSpec;
    }
    else if ((curNode instanceof Script)) {
      fieldSpec = ((Node)curNode).FieldSpec;
    }
    Vector printedFields = new Vector();
    Enumeration e = fieldSpec.elements();
    while (e.hasMoreElements()) {
      Field newField = (Field)e.nextElement();
      if ((newField instanceof ConstField)) {
        newField = ((ConstField)newField).ownerField;
      }
      if (!printedFields.contains(newField)) {
        printedFields.addElement(newField);
        System.out.println("    " + fixedLengthString(newField.fieldName, 20) + " " + newField);

        if ((!this.fieldsVisited.contains(newField)) && (!this.fieldsToVisit.contains(newField)))
        {
          this.fieldsToVisit.addElement(newField);
        }
      }
    }
  }

  private void reset()
  {
    this.fieldsVisited.removeAllElements();
    this.nodesVisited.removeAllElements();
    this.nodesToVisit.removeAllElements();
    this.fieldsToVisit.removeAllElements();
  }

  void printRoutes(Field startField)
  {
    this.fieldsToVisit.addElement(startField);
    doIt();
  }

  void printRoutes(BaseNode startNode)
  {
    this.nodesToVisit.addElement(startNode);
    doIt();
  }

  private void doIt()
  {
    while ((this.fieldsToVisit.size() > 0) || (this.nodesToVisit.size() > 0)) {
      Enumeration e = this.fieldsToVisit.elements();
      while (e.hasMoreElements()) {
        Field newField = (Field)e.nextElement();
        printRoutesField(newField);
      }
      e = this.nodesToVisit.elements();
      while (e.hasMoreElements()) {
        BaseNode newNode = (BaseNode)e.nextElement();
        printRoutesNode(newNode);
      }
    }
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.RoutePrinter
 * JD-Core Version:    0.6.0
 */