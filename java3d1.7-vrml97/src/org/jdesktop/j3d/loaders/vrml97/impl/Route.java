package org.jdesktop.j3d.loaders.vrml97.impl;

public class Route
{
  BaseNode fromNode;
  String fromField;
  BaseNode toNode;
  String toField;

  Route(BaseNode fromNode, String fromField, BaseNode toNode, String toField)
  {
    this.fromNode = fromNode;
    this.fromField = fromField;
    this.toNode = toNode;
    this.toField = toField;
  }

  public String toString()
  {
    return "ROUTE " + this.fromNode.defName + "." + this.fromField + " TO " + this.toNode.defName + "." + this.toField;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Route
 * JD-Core Version:    0.6.0
 */