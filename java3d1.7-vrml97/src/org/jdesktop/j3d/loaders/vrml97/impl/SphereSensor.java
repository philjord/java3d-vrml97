package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Bounds;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.CapabilityNotSetException;
import org.jogamp.java3d.IllegalSharingException;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.SceneGraphPath;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Quat4d;
import org.jogamp.vecmath.Vector3d;

public class SphereSensor extends DragSensor
{
  SFRotation offset;
  SFBool autoSpin;
  SFInt32 autoSpinFrameWait;
  SFFloat spinKick;
  SFRotation rotation;
  Transform3D l2vwTf = new Transform3D();
  Transform3D im2vwTf = new Transform3D();
  Transform3D TTf = new Transform3D();

  Quat4d T = new Quat4d();
  Quat4d T_;
  Quat4d rot = new Quat4d();
  Transform3D im2lTf = new Transform3D();
  Transform3D l2imTf = new Transform3D();
  Transform3D vw2lTf = new Transform3D();

  Point3d p1 = new Point3d();
  Point3d p2 = new Point3d();

  Transform3D rotTf = new Transform3D();
  AxisAngle4d sfrOffset = new AxisAngle4d();
  AxisAngle4d sfrR = new AxisAngle4d();
  AxisAngle4d rotAxAngle = new AxisAngle4d();
  Vector3d trackpt = new Vector3d();
  Vector3d v = new Vector3d();
  Vector3d vlast = new Vector3d();
  Vector3d t = new Vector3d();
  Vector3d u = new Vector3d();
  double a;
  Bounds bounds;
  int count = 0;
  boolean autoSpinning = false;
  static double EPSILON = 1.0E-008D;
  static double DELTA = 1.E-005D;

  Node node = null;
  SceneGraphPath path = null;

  public SphereSensor(Loader loader)
  {
    super(loader);
    this.offset = new SFRotation(0.0F, 1.0F, 0.0F, 0.0F);
    this.rotation = new SFRotation(0.0F, 1.0F, 0.0F, 0.0F);
    this.sfrOffset.set(this.offset.rot[0], this.offset.rot[1], this.offset.rot[2], this.offset.rot[3]);
    this.sfrR.set(this.rotation.rot[0], this.rotation.rot[1], this.rotation.rot[2], this.rotation.rot[3]);
    this.autoSpin = new SFBool(false);
    this.autoSpinFrameWait = new SFInt32(3);
    this.spinKick = new SFFloat(10.0F);
    this.vlast.x = 0.0D;
    this.vlast.y = 1.0D;
    this.vlast.z = 0.0D;
    initSphereSensorFields();
  }

  void update(Point3d p1, Point3d p2, Node nodeIn, SceneGraphPath pathIn)
  {
    this.p1 = p1;
    this.p2 = p2;

    this.count = this.autoSpinFrameWait.value;

    if ((nodeIn != null) && (pathIn != null)) {
      this.node = nodeIn;
      this.path = pathIn;

      if (Browser.debug)
      {
        System.out.println(pathIn);

        System.out.println(pathIn.getLocale());
        for (int xx = 0; xx < pathIn.nodeCount(); xx++) {
          System.out.println(pathIn.getNode(xx));
        }
        System.out.println(pathIn.getObject());
        System.out.println("picked node" + this.node);
      }
      aquireTransform(nodeIn);

      this.isActive.setValue(true);

      this.bounds = this.node.getBounds();
    }
    else if (this.autoSpinning)
    {
      this.browser.evagation.forceUpDown();
      aquireTransform(this.node);
      doUpdate();
      offset();

      this.browser.evagation.curDragSensor = this;
      this.autoSpinning = false;
    }

    this.browser.canvas.getImagePlateToVworld(this.im2vwTf);
    try
    {
      this.node.getLocalToVworld(this.l2vwTf);
    }
    catch (IllegalSharingException e) {
      this.node.getLocalToVworld(this.path, this.l2vwTf);
    }
    catch (NullPointerException npe) {
      npe.printStackTrace();
    }
    catch (CapabilityNotSetException cnse)
    {
    }

    this.vw2lTf.invert(this.l2vwTf);
    this.im2lTf.mul(this.vw2lTf, this.im2vwTf);
    this.im2lTf.transform(p1);
    this.im2lTf.transform(p2);
    this.t.set(p1);
    this.u.set(p2);
    try {
      norm(this.t);
      norm(this.u);
      this.v.cross(this.t, this.u);
      this.a = (angle(this.t, this.u) * coorelate(this.im2lTf));
      if (Math.abs(this.a) > EPSILON) {
        norm(this.v);
        this.vlast.set(this.v);
      }
      else {
        this.v.set(this.vlast);
      }
    }
    catch (ArithmeticException ae) {
      System.out.println("dinky delta");
    }

    this.rotAxAngle.set(this.v.x, this.v.y, this.v.z, this.a);
    this.rot.set(this.rotAxAngle);
    this.T_.mul(this.rot, this.T);

    doUpdate();
  }

  void simTick(double t)
  {
    if ((this.autoSpin.value) && (this.count-- <= 0)) {
      this.autoSpinning = true;
      this.rotAxAngle.set(this.v.x, this.v.y, this.v.z, this.rotAxAngle.angle + this.a);
      this.rot.set(this.rotAxAngle);
      this.T_.mul(this.rot, this.T);
      doUpdate();
    }
    else {
      this.autoSpinning = false;
    }
  }

  void aquireTransform(Node nodeIn)
  {
    if (this.T_ == null) {
      if ((nodeIn instanceof TransformGroup)) {
        ((TransformGroup)nodeIn).getTransform(this.TTf);
      }
      else {
        this.TTf.setIdentity();
      }
      this.T_ = new Quat4d();
      this.TTf.get(this.T);
      this.T_.set(this.T);
    }
    else
    {
      this.T_.get(this.T);
    }
  }

  void doUpdate()
  {
    if (this.enabled.value) {
      this.sfrR.set(this.T_);
      this.rotation.setValue((float)this.sfrR.x, (float)this.sfrR.y, (float)this.sfrR.z, (float)this.sfrR.angle);

      this.trackpt.set(this.p2);
      try {
        norm(this.trackpt);
      }
      catch (ArithmeticException ae)
      {
      }
      this.trackpt.scale(((BoundingSphere)this.bounds).getRadius());

      this.trackPoint.setValue((float)this.trackpt.x, (float)this.trackpt.y, (float)this.trackpt.z);
    }
  }

  public void offset()
  {
    this.isActive.setValue(false);

    AxisAngle4d sfrO = new AxisAngle4d();
    sfrO.set(this.T_);

    this.offset.setValue((float)sfrO.x, (float)sfrO.y, (float)sfrO.z, (float)sfrO.angle);
    this.count = 0;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if ((eventInName.equals("autoSpin")) && 
      (this.autoSpinning))
      this.autoSpinning = false;
  }

  public String getType()
  {
    return "SphereSensor";
  }

  public Object clone()
  {
    return new SphereSensor(this.loader);
  }

  void initSphereSensorFields()
  {
    this.offset.init(this, this.FieldSpec, 3, "offset");
    this.rotation.init(this, this.FieldSpec, 2, "rotation");
    this.autoSpin.init(this, this.FieldSpec, 0, "autoSpin");
    this.autoSpinFrameWait.init(this, this.FieldSpec, 0, "autoSpinFrameWait");
    this.spinKick.init(this, this.FieldSpec, 0, "spinKick");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SphereSensor
 * JD-Core Version:    0.6.0
 */