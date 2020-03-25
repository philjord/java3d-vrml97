package org.jdesktop.j3d.loaders.vrml97.impl;

public class Coordinate extends Node
  implements Reusable
{
  MFVec3f point;
  Node owner;

  public Coordinate(Loader loader)
  {
    super(loader);
    this.point = new MFVec3f();
    initFields();
  }

  public Coordinate(Loader loader, float[] points)
  {
    super(loader);
    this.point = new MFVec3f(points);
    initFields();
  }

  public Coordinate(Loader loader, MFVec3f points)
  {
    super(loader);
    this.point = points;
    initFields();
  }

  void initFields()
  {
    this.point.init(this, this.FieldSpec, 3, "point");
  }

  public void reset()
  {
    this.point.reset();
    this.implReady = false;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("point")) {
      if (this.owner != null) {
        this.owner.notifyMethod("coord", time);
      }
    }
    else if ((eventInName.equals("route_point")) && 
      (this.owner != null))
      this.owner.notifyMethod("route_coord", time);
  }

  public Object clone()
  {
    return new Coordinate(this.loader, (MFVec3f)this.point.clone());
  }

  public String getType()
  {
    return "Coordinate";
  }

  public String toStringBody()
  {
    return "Coordinate {\npoint " + this.point + "}";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Coordinate
 * JD-Core Version:    0.6.0
 */