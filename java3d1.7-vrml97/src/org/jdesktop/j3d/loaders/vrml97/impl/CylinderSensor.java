package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.SceneGraphPath;
import org.jogamp.vecmath.Point3d;

public class CylinderSensor extends DragSensor
{
  SFFloat diskAngle;
  SFFloat maxAngle;
  SFFloat minAngle;
  SFFloat offset;
  SFRotation rotation;

  public CylinderSensor(Loader loader)
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
    return new CylinderSensor(this.loader);
  }

  public void notifyMethod(String eventInName, double time)
  {
  }

  public String getType()
  {
    return "CylinderSensor";
  }

  void initCylinderSensorFields()
  {
    this.diskAngle.init(this, this.FieldSpec, 3, "diskAngle");
    this.maxAngle.init(this, this.FieldSpec, 3, "maxAngle");
    this.minAngle.init(this, this.FieldSpec, 3, "minAngle");
    this.offset.init(this, this.FieldSpec, 3, "offset");
  }

  void offset()
  {
  }

  void simTick(double now)
  {
  }

  void update(Point3d p1, Point3d p2, Node node, SceneGraphPath path)
  {
    System.out.println("CylinderSensor NYI");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.CylinderSensor
 * JD-Core Version:    0.6.0
 */