package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.SceneGraphPath;
import org.jogamp.vecmath.Point3d;

public class PlaneSensor extends DragSensor
{
  SFVec3f offset;
  SFVec2f maxPosition;
  SFVec2f minPosition;
  SFVec3f translation;

  public PlaneSensor(Loader loader)
  {
    super(loader);
    initCylinderSensorFields();
  }

  void initFields()
  {
    super.initFields();
    initCylinderSensorFields();
  }

  public Object clone()
  {
    return new PlaneSensor(this.loader);
  }

  public void notifyMethod(String eventInName, double time)
  {
  }

  public String getType()
  {
    return "PlaneSensor";
  }

  void initCylinderSensorFields()
  {
    this.offset.init(this, this.FieldSpec, 3, "offset");
    this.maxPosition.init(this, this.FieldSpec, 3, "maxPosition");
    this.minPosition.init(this, this.FieldSpec, 3, "minPosition");
    this.translation.init(this, this.FieldSpec, 2, "translation");
  }

  void offset()
  {
  }

  void simTick(double t)
  {
  }

  void update(Point3d p1, Point3d p2, Node node, SceneGraphPath path)
  {
    System.out.println("PlaneSensor NYI");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.PlaneSensor
 * JD-Core Version:    0.6.0
 */