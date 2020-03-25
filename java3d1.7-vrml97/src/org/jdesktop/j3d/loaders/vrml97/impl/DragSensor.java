package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Vector;
import org.jogamp.java3d.SceneGraphPath;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

public abstract class DragSensor extends Node
  implements VrmlSensor
{
  SFBool enabled;
  SFBool autoOffset;
  SFBool isActive;
  SFVec3f trackPoint;
  org.jogamp.java3d.Node parent;
  static double EPSILON = 1.0E-008D;
  static double DELTA = 1.E-005D;

  public DragSensor(Loader loader)
  {
    super(loader);
    this.enabled = new SFBool(true);
    this.autoOffset = new SFBool(true);
    this.isActive = new SFBool(true);
    this.trackPoint = new SFVec3f(0.0F, 0.0F, 0.0F);
    initFields();
  }

  DragSensor(Loader loader, SFBool enabled)
  {
    super(loader);
    this.enabled = enabled;
    this.isActive = new SFBool(true);
    initFields();
  }

  void updateParent(org.jogamp.java3d.Node parentImpl)
  {
    Vector v = (Vector)(Vector)parentImpl.getUserData();
    if (v == null) {
      v = new Vector();
      parentImpl.setUserData(v);
      if (this.loader.debug) {
        System.out.println("Drag Sensor parent: " + parentImpl + " had no user data, added vector:" + v);
      }
    }

    v.addElement(this);

    parentImpl.setCapability(1);
    parentImpl.setCapability(3);
    parentImpl.setCapability(11);

    parentImpl.setCapability(12);
    parentImpl.setCapability(13);
    parentImpl.setCapability(14);
    if ((parentImpl instanceof TransformGroup)) {
      parentImpl.setCapability(17);
    }
    parentImpl.setPickable(true);
    this.parent = parentImpl;
  }

  void initFields()
  {
    this.enabled.init(this, this.FieldSpec, 3, "enabled");
    this.autoOffset.init(this, this.FieldSpec, 3, "autoOffset");
    this.isActive.init(this, this.FieldSpec, 2, "isActive");
    this.trackPoint.init(this, this.FieldSpec, 2, "trackPoint");
  }

  abstract void offset();

  abstract void simTick(double paramDouble);

  abstract void update(Point3d paramPoint3d1, Point3d paramPoint3d2, org.jogamp.java3d.Node paramNode, SceneGraphPath paramSceneGraphPath);

  static void norm(Vector3d n)
    throws ArithmeticException
  {
    double norml = (float)Math.sqrt(n.x * n.x + n.y * n.y + n.z * n.z);
    if (norml == 0.0D) {
      throw new ArithmeticException();
    }

    n.x /= norml;
    n.y /= norml;
    n.z /= norml;
  }

  static double angle(Vector3d t, Vector3d u)
  {
    double l1 = length(t);
    double l2 = length(u);

    if ((l1 == 0.0D) || (l2 == 0.0D)) {
      return 0.0D;
    }

    double a = Math.acos(dot(t, u) / (l1 * l2));

    if ((a < 0.0D) || (a > 0.0D)) {
      return a;
    }

    return EPSILON;
  }

  static double length(Vector3d v)
  {
    double l = Math.sqrt(dot(v, v));
    return l;
  }

  static double dot(Vector3d v, Vector3d u)
  {
    return v.x * u.x + v.y * u.y + v.z * u.z;
  }

  static double coorelate(Transform3D tr)
  {
    double c = 0.0D;
    double COORELATION_FACTOR = 10.0D;
    if (c == 0.0D) {
      Point3d p1 = new Point3d(0.0D, 0.0D, 0.0D);
      Point3d p2 = new Point3d(0.0D + DELTA, 0.0D, 0.0D);

      tr.transform(p1);
      tr.transform(p2);

      Vector3d v1 = new Vector3d(p1);
      Vector3d v2 = new Vector3d(p2);

      norm(v1);
      norm(v2);

      c = COORELATION_FACTOR * DELTA / angle(v1, v2);
    }

    return c;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.DragSensor
 * JD-Core Version:    0.6.0
 */