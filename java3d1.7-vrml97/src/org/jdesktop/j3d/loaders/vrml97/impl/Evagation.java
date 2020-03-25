package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.utils.geometry.Sphere;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Bounds;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.IllegalSharingException;
import org.jogamp.java3d.Locale;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PickRay;
import org.jogamp.java3d.SceneGraphPath;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnAWTEvent;
import org.jogamp.java3d.WakeupOnCollisionEntry;
import org.jogamp.java3d.WakeupOnCollisionExit;
import org.jogamp.java3d.WakeupOr;
import org.jogamp.vecmath.Matrix3d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jdesktop.j3d.loaders.vrml97.Player;
import vrml.InvalidVRMLSyntaxException;

public class Evagation extends Behavior
{
  TransformGroup viewTrans = new TransformGroup();
  Browser browser;
  PickRay pickRay;
  WakeupOnCollisionEntry collisionEnter;
  WakeupOnCollisionExit collisionExit;
  WakeupCriterion[] criterion = new WakeupCriterion[7];
  Matrix3d rot = new Matrix3d();
  Matrix3d lastFreeRot = new Matrix3d();
  Matrix3d tmpDirection = new Matrix3d();
  Matrix3d zComp = new Matrix3d();
  Vector3d newDirection = new Vector3d();
  Transform3D positionTrans = new Transform3D();
  Matrix3d rollRot = new Matrix3d();
  Matrix3d pitchRot = new Matrix3d();
  Matrix3d yawRot = new Matrix3d();
  double rollAngle = 0.0D;
  double pitchAngle = 0.0D;
  double yawAngle = 0.0D;
  double velocity = 0.0D;
  double rollAngleDelta = 0.0D;
  double pitchAngleDelta = 0.0D;
  double yawAngleDelta = 0.0D;
  double directionX;
  double directionY;
  double directionZ;
  double acceleration;
  static final double SPEED = 0.005D;
  double speed = 0.005D;
  double angularSpeed = 1.190476190476191E-005D;
  Transform3D currentView;
  Vector3d currentPosn;
  Vector3d lastFreePosn = new Vector3d();
  Point mousePosn;
  Point3d eyePosn = new Point3d();
  double eyeZoffset = 0.0D;
  double startX = 0.0D;
  double startY = 0.0D;
  double startZ = 0.0D;
  double lastT = 0.0D;
  double deltaT = 0.0D;
  static double MAXANG = 6.283185307179586D;
  int last_x;
  int last_y;
  int first_x;
  int first_y;
  int drag_dx;
  int drag_dy;
  int eid;
  boolean initialized;
  boolean firstTime = true;
  boolean collided = false;
  String navMode;
  SphereSensor sceneExaminer = null;
  DragSensor curDragSensor;
  boolean dragIsSensor = false;

  Transform3D tfm = new Transform3D();
  Transform3D tfmO = new Transform3D();
  Appearance pickAppear;
  TransparencyAttributes pickTransat;
  RGroup pickSphereHandle;
  TransformGroup pickSphereLocator = new TransformGroup();

  Point3d p1 = new Point3d();
  Point3d p2 = new Point3d();

  public Evagation(Browser initBrowser)
  {
    this.browser = initBrowser;
    this.initialized = false;
    this.collided = false;
    this.currentView = new Transform3D();
    this.currentView.setIdentity();
    this.pickRay = new PickRay();
  }

  public void initialize()
  {
    initialize(new BoundingSphere(new Point3d(), 1000.0D), new Vector3d(this.startX, this.startY, this.startZ));
  }

  public void initialize(BoundingSphere bounds, Vector3d currentPosn)
  {
    Transform3D initTransform = new Transform3D();
    setSchedulingBounds(bounds);
    this.currentPosn = currentPosn;

    this.currentView.setTranslation(currentPosn);

    this.rollAngle = 0.0D;
    this.pitchAngle = 0.0D;
    this.rollAngleDelta = 0.0D;
    this.pitchAngleDelta = 0.0D;
    this.yawAngleDelta = 0.0D;
    this.yawAngleDelta = 0.0D;
    this.velocity = 0.0D;

    this.initialized = true;
    resetWakeups();
  }

  public void setViewGroup(TransformGroup newViewTrans)
  {
    this.viewTrans = newViewTrans;

    Bounds b = new BoundingSphere();
    if (this.browser.curNavInfo == null) {
      this.browser.curNavInfo = new NavigationInfo(this.browser.loader);
    }
    else {
      double sizex = this.browser.curNavInfo.avatarSize.mfloat[0] / 2.0F;
      double sizey;
      if (this.browser.curNavInfo.avatarSize.mfloat.length > 1) {
        sizey = this.browser.curNavInfo.avatarSize.mfloat[1] / 2.0F;
      }
      else
        sizey = this.browser.curNavInfo.avatarSize.mfloat[0] / 2.0F;
      double sizez;
      if (this.browser.curNavInfo.avatarSize.mfloat.length > 2) {
        sizez = this.browser.curNavInfo.avatarSize.mfloat[2] / 2.0F;
      }
      else {
        sizez = this.browser.curNavInfo.avatarSize.mfloat[0] / 2.0F;
      }
      b = new BoundingBox(new Point3d(-sizex, -sizey, -sizez), new Point3d(sizex, sizey, sizez));

      this.navMode = this.browser.curNavInfo.type.strings[0];
      if ((!this.navMode.equals("EXAMINE")) && (!this.navMode.equals("WALK"))) {
        this.navMode = "WALK";
      }
    }
    this.speed = (this.browser.curNavInfo.speed.value * 0.005D);
    this.viewTrans.setCollisionBounds(b);
    this.viewTrans.setBounds(b);
    this.viewTrans.getTransform(this.currentView);
    Vector3d vc = new Vector3d();
    this.currentView.get(vc);
    this.startX = vc.x;
    this.startY = vc.y;
    this.startZ = vc.z;

    if ((this.sceneExaminer != null) && (this.navMode.equals("WALK"))) {
      try {
        this.browser.deleteRoute(this.sceneExaminer, "rotation", this.browser.curSceneT, "rotation");

        this.browser.curSceneT.impl.setUserData(null);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      this.browser.curViewpoint.updateViewTrans();
    }
    if (this.navMode.equals("EXAMINE"))
    {
      if (this.browser.sceneExaminer == null) {
        this.browser.sceneExaminer = new SphereSensor(this.browser.loader);
        try
        {
          this.browser.sceneExaminer.updateParent(this.browser.curSceneT.impl);
        }
        catch (NullPointerException npe)
        {
        }
      }
      this.sceneExaminer = this.browser.sceneExaminer;
      try {
        this.browser.addRoute(this.sceneExaminer, "rotation", this.browser.curSceneT, "rotation");
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  void resetWakeups()
  {
    SceneGraphPath viewPath = new SceneGraphPath(this.browser.locale, this.viewTrans);

    this.criterion = new WakeupCriterion[7];
    this.criterion[0] = new WakeupOnAWTEvent(503);
    this.criterion[1] = new WakeupOnAWTEvent(501);
    this.criterion[2] = new WakeupOnAWTEvent(506);
    this.criterion[3] = new WakeupOnAWTEvent(500);
    this.criterion[4] = new WakeupOnAWTEvent(502);
    this.criterion[5] = new WakeupOnAWTEvent(402);
    this.criterion[6] = new WakeupOnAWTEvent(401);

    wakeupOn(new WakeupOr(this.criterion));

    this.firstTime = false;
  }

  @Override
  public void processStimulus(Iterator<WakeupCriterion> critter)
  {
    while (critter.hasNext()) {
      WakeupCriterion wakeup = (WakeupCriterion)critter.next();

      AWTEvent[] awts = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
      for (int i = 0; i < awts.length; i++) {
        processEvent(awts[i]);
      }
      resetWakeups();
    }
  }

  void processEvent(AWTEvent evt)
  {
    if (!this.initialized) {
      System.out.println("Event before behavior is initialized");
      return;
    }
    if ((evt instanceof KeyEvent)) {
      processKeyEvent((KeyEvent)evt);
    }
    else if ((evt instanceof MouseEvent)) {
      processMouseEvent((MouseEvent)evt);
    }
    else if ((evt instanceof FocusEvent))
      processFocusEvent((FocusEvent)evt);
  }

  public void processMouseEvent(MouseEvent evt)
  {
    this.eid = evt.getID();
    boolean mmb = (evt.getModifiers() & 0x8) > 0;
    switch (this.eid) {
    case 506:
      this.mousePosn = evt.getPoint();

      this.drag_dx = (this.mousePosn.x - this.first_x);
      this.drag_dy = (this.mousePosn.y - this.first_y);
      this.last_x = this.mousePosn.x;
      this.last_y = this.mousePosn.y;

      if (this.drag_dx * this.drag_dx + this.drag_dy * this.drag_dy > 100) {
        mouseDragged(evt.getWhen(), this.drag_dx, this.drag_dy, this.first_x, this.first_y, mmb);
      }

      if (this.dragIsSensor) {
        this.velocity = 0.0D;
      }
      else {
        this.pitchAngleDelta = (-this.drag_dx * this.angularSpeed);
        if (evt.isShiftDown()) {
          this.yawAngleDelta = (-this.drag_dy * this.angularSpeed);
        }
        else {
          this.velocity = (this.drag_dy * this.speed);
        }
      }

      break;
    case 500:
      this.mousePosn = evt.getPoint();
      this.first_x = (this.last_x = this.mousePosn.x);
      this.first_y = (this.last_y = this.mousePosn.y);
      mouseClicked(evt.getWhen(), this.last_x, this.last_y, mmb);
      break;
    case 501:
      this.mousePosn = evt.getPoint();
      this.first_x = this.mousePosn.x;
      this.first_y = this.mousePosn.y;
      break;
    case 502:
      clearDragState();
      this.curDragSensor = null;
      this.dragIsSensor = false;
      break;
    case 503:
    case 504:
    case 505:
    }update();
  }

  private void processKeyEvent(KeyEvent evt)
  {
    double ROT_ANGLE = 0.03D;
    double VELOCITY = 5.0D;
    int key = evt.getKeyCode();
    if (evt.getID() == 402) {
      this.velocity = 0.0D;
      this.rollAngleDelta = 0.0D;
      this.pitchAngleDelta = 0.0D;
      this.yawAngleDelta = 0.0D;
    }

    if (evt.getID() == 401) {
      if (key == 38) {
        this.velocity = (-VELOCITY);
      }
      else if (key == 40) {
        this.velocity = VELOCITY;
      }
      else if (key == 37) {
        this.pitchAngleDelta += ROT_ANGLE;
      }
      else if (key == 39) {
        this.pitchAngleDelta -= ROT_ANGLE;
      }
      else if (key == 83) {
        resetViewpoint();
      }
      else if (key == 67) {
        this.pitchAngle = 0.0D;
        this.pitchAngleDelta = 0.0D;
        this.rollAngle = 0.0D;
        this.rollAngleDelta = 0.0D;
        this.velocity = 0.0D;
      }
      else if (key == 68) {
        this.browser.treePrinter.print(this.browser.curScene);
      }
      else if (key == 47) {
        this.browser.outputTiming();
      }
      else if (key == 27) {
        this.browser.outputTiming();
        this.browser.shutDown();
      }
      else if (key == 33) {
        this.yawAngleDelta = 0.01D;
      }
      else if (key == 34) {
        this.yawAngleDelta = -0.01D;
      }
    }
    calcTransform();
    this.viewTrans.setTransform(this.currentView);
  }

  private void processFocusEvent(FocusEvent evt)
  {
    if (evt.getID() != 1004)
    {
      if (evt.getID() != 1005);
    }
  }

  void forceUpDown()
  {
    this.curDragSensor = null;
    clearDragState();
    this.first_x = (this.last_x = this.mousePosn.x);
    this.first_y = (this.last_y = this.mousePosn.y);
    mouseDragged(Time.getNow(), this.drag_dx, this.drag_dy, this.first_x, this.first_y, false);
  }

  void clearDragState()
  {
    this.drag_dx = (this.drag_dy = 0);
    this.first_x = (this.first_y = -1);
    this.rollAngleDelta = (this.pitchAngleDelta = this.yawAngleDelta = 0.0D);
    this.velocity = 0.0D;
    if (this.curDragSensor != null)
      this.curDragSensor.offset();
  }

  private void calcTransform()
  {
    this.positionTrans.set(this.velocity * this.deltaT);
    this.zComp.setIdentity();
    this.zComp.setRow(2, 0.0D, 0.0D, 1.0D);

    this.rot.setIdentity();
    this.rot.mul(this.rollRot);
    this.rot.mul(this.pitchRot);
    this.rot.mul(this.yawRot);

    this.tmpDirection.mul(this.rot, this.zComp);
    this.newDirection.x = (this.directionX = this.tmpDirection.m02);
    this.newDirection.y = (this.directionY = this.tmpDirection.m12);
    this.newDirection.z = (this.directionZ = this.tmpDirection.m22);

    this.positionTrans.transform(this.newDirection);
    this.currentPosn.add(this.newDirection);

    this.rollAngle = Math.IEEEremainder(this.rollAngle + this.rollAngleDelta, 6.283185307179586D);
    this.pitchAngle = Math.IEEEremainder(this.pitchAngle + this.pitchAngleDelta, 6.283185307179586D);

    this.yawAngle = Math.IEEEremainder(this.yawAngle + this.yawAngleDelta, 6.283185307179586D);

    this.rollRot.rotZ(this.rollAngle);
    this.pitchRot.rotY(this.pitchAngle);
    this.yawRot.rotX(this.yawAngle);

    this.currentView.setRotationScale(this.rot);
    this.currentView.setTranslation(this.currentPosn);
  }

  public void simTick(double now)
  {
    if (!this.collided) {
      this.deltaT = (now - this.lastT);
      this.lastFreeRot.set(this.rot);
      this.lastFreePosn.set(this.currentPosn);
    }
    else {
      if (Browser.debug) {
        System.out.println("collided");
      }
      this.deltaT = 0.0D;
    }

    this.lastT = now;
    update();
  }

  void update()
  {
    if (this.initialized) {
      calcTransform();

      if ((this.currentView.getType() & 0x40) != 0)
        this.viewTrans.setTransform(this.currentView);
    }
  }

  public void resetViewpoint()
  {
    this.rollAngle = 0.0D;
    this.pitchAngle = 0.0D;
    this.yawAngle = 0.0D;
    this.rollAngleDelta = 0.0D;
    this.pitchAngleDelta = 0.0D;
    this.yawAngleDelta = 0.0D;
    this.velocity = 0.0D;
    this.currentPosn = new Vector3d(this.startX, this.startY, this.startZ);
  }

  private PickRay getPickRay(int xpos, int ypos)
  {
    Transform3D motion = new Transform3D();
    Point3d mousePosn = new Point3d();
    Vector3d mouseVec = new Vector3d();

    this.browser.canvas.getCenterEyeInImagePlate(this.eyePosn);
    this.browser.canvas.getPixelLocationInImagePlate(xpos, ypos, mousePosn);
    this.browser.canvas.getImagePlateToVworld(motion);

    this.eyeZoffset = this.eyePosn.z;

    motion.transform(this.eyePosn);
    motion.transform(mousePosn);
    mouseVec.sub(mousePosn, this.eyePosn);
    mouseVec.normalize();

    this.pickRay.set(this.eyePosn, mouseVec);

    return this.pickRay;
  }

  private void pickEcho(Node step, SceneGraphPath path)
  {
    try
    {
      Bounds b = step.getBounds();
      if ((b instanceof BoundingSphere)) {
        Point3d p = new Point3d();
        ((BoundingSphere)b).getCenter(p);
        try {
          step.getLocalToVworld(this.tfm);
        }
        catch (IllegalSharingException e) {
          try {
            step.getLocalToVworld(path, this.tfm);
          }
          catch (Exception f)
          {
          }
        }

        this.tfmO.set(new Vector3d(p));
        this.tfm.mul(this.tfmO);
        Sphere sp = new Sphere((float)((BoundingSphere)b).getRadius());

        attachPickSphere(sp);
      }
      else if (Browser.debug) {
        System.out.println("node bounds are not BoundingSphere");
      }

    }
    catch (Exception ed)
    {
      System.out.println("Exception trying to echo pick");
      ed.printStackTrace();
    }
  }

  void mouseClicked(double when, int x, int y, boolean mmb)
  {
    if (Browser.debug) {
      System.out.println("Evagation.mouseClicked() called");
    }

    double touchTime = Time.getNow();
    this.browser.beginRoute(touchTime);

    SceneGraphPath[] stuffPicked = this.browser.curScene.pickAllSorted(getPickRay(x, y));

    if (stuffPicked != null) {
      if (Browser.debug) {
        System.out.println("stuffPicked.length=" + stuffPicked.length);
      }
      for (int i = 0; i < stuffPicked.length; i++) {
        if (Browser.debug) {
          System.out.println("stuffPicked[" + i + "] is " + stuffPicked[i]);
        }

        VrmlSensor vs = (VrmlSensor)null;
        Vector sv = (Vector)null;
        Node step = null;
        if (Browser.debug) {
          System.out.println("stuffPicked[" + i + "].nodeCount() = " + stuffPicked[i].nodeCount());
        }

        for (int j = 0; j < stuffPicked[i].nodeCount(); j++) {
          step = stuffPicked[i].getNode(j);
          if ((Browser.debug) || (this.browser.pickEcho)) {
            pickEcho(step, stuffPicked[i]);
          }
          Object o = step.getUserData();
          if ((Browser.debug) && (j == 0)) {
            System.out.println(i + " " + j + "Picked node is " + step + " user data is " + o);

            System.out.println(stuffPicked[i].getObject());
          }
          if (((o instanceof Vector)) && (!((Vector)o).isEmpty()))
          {
            sv = (Vector)o;
          }
          else if ((o instanceof SphereSensor)) {
            sv = new Vector();
            sv.addElement(o);
            step.setUserData(sv);
          }
        }

        if (sv != null) {
          Enumeration e = sv.elements();
          while (e.hasMoreElements()) {
            vs = (VrmlSensor)(VrmlSensor)e.nextElement();
            if ((vs instanceof TouchSensor)) {
              TouchSensor ts = (TouchSensor)vs;
              if (ts.enabled.value == true) {
                if (Browser.debug) {
                  System.out.println("Calling setValue on touchTime for sensor " + ts.toStringId());
                }

                ts.touchTime.setValue(touchTime);
              }
              continue;
            }if ((((vs instanceof DragSensor)) && (!mmb)) || 
              (!(vs instanceof Anchor))) continue;
            String toGoto = ((Anchor)vs).url.strings[0];
            try {
              this.browser.canvas.setCursor(new Cursor(3));

              this.browser.loadURL(((Anchor)vs).url.strings, (String[])null);
            }
            catch (IOException ioe)
            {
              System.err.println("IO exception reading URL");
            }
            catch (InvalidVRMLSyntaxException ivse) {
              System.err.println("VRML parse error");
            }
            String status = this.browser.getName() + " : " + this.browser.getDescription();

            if ((this.browser.container instanceof Applet)) {
              if (toGoto.endsWith(".wrl")) {
                ((Applet)(Applet)this.browser.container).showStatus(status);
              }
              else {
                try
                {
                  ((Applet)(Applet)this.browser.container).getAppletContext().showDocument(new URL(toGoto));
                }
                catch (MalformedURLException me)
                {
                  ((Applet)(Applet)this.browser.container).showStatus(me.toString());
                }
              }

            }
            else if ((this.browser.container instanceof Frame)) {
              ((Frame)(Frame)this.browser.container).setTitle(status);
              ((Player)(Player)this.browser.container).setGotoString(toGoto);
            }

            this.browser.canvas.setCursor(new Cursor(12));
          }
        }

      }

    }

    this.browser.endRoute();
  }

  void mouseDragged(double when, int dx, int dy, int x, int y, boolean mmb)
  {
    this.browser.beginRoute(Time.getNow());
    if ((this.curDragSensor != null) && (!mmb))
    {
      this.browser.canvas.getPixelLocationInImagePlate(x, y, this.p1);
      this.browser.canvas.getPixelLocationInImagePlate(x + dx, y + dy, this.p2);
      this.p1.z = (this.p2.z = this.eyeZoffset);
      this.curDragSensor.update(this.p1, this.p2, null, null);
    }
    else {
      SceneGraphPath[] stuffDragged = this.browser.curScene.pickAllSorted(getPickRay(x, y));

      if (stuffDragged != null) {
        for (int i = 0; i < stuffDragged.length; i++) {
          VrmlSensor vs = (VrmlSensor)null;
          Vector sv = (Vector)null;
          Node step = null;
          for (int j = 0; j < stuffDragged[i].nodeCount(); j++) {
            try {
              step = stuffDragged[i].getNode(j);
              Object o = step.getUserData();
              if ((Browser.debug) && (j == 0)) {
                System.out.println(i + "." + j + ": Picked node is " + step + " user data is " + o);
              }

              if (((o instanceof Vector)) && (!((Vector)o).isEmpty()))
              {
                sv = (Vector)o;

                if (this.navMode.equals("EXAMINE")) {
                  j = stuffDragged[i].nodeCount();
                }
              }
            }
            catch (Exception ed)
            {
              ed.printStackTrace();
            }
          }
          if (sv != null) {
            Enumeration e = sv.elements();
            while (e.hasMoreElements()) {
              vs = (VrmlSensor)(VrmlSensor)e.nextElement();
              if (((vs instanceof DragSensor)) && (!mmb)) {
                this.browser.canvas.getPixelLocationInImagePlate(x, y, this.p1);
                this.browser.canvas.getPixelLocationInImagePlate(x + dx, y + dy, this.p2);
                this.p1.z = (this.p2.z = this.eyeZoffset);

                ((DragSensor)vs).update(this.p1, this.p2, step, stuffDragged[i]);
                this.dragIsSensor = true;
                this.curDragSensor = ((DragSensor)vs);
                continue;
              }
              this.curDragSensor = null;
            }
          }
        }
      }
    }

    this.browser.endRoute();
  }

  void attachPickSphere(Sphere sp)
  {
    if (!this.pickSphereLocator.isLive()) {
      try {
        this.pickSphereLocator.removeChild(0);
      }
      catch (ArrayIndexOutOfBoundsException aioobe)
      {
      }

      this.pickSphereHandle = new RGroup();
      this.pickSphereLocator = new TransformGroup();
      this.pickSphereHandle.addChild(this.pickSphereLocator);
      this.pickTransat = new TransparencyAttributes(0, 0.5F);

      this.pickTransat.setCapability(3);
      this.pickTransat.setCapability(2);
      this.pickAppear = new Appearance();
      this.pickAppear.setTransparencyAttributes(this.pickTransat);

      sp.setAppearance(this.pickAppear);
      this.pickSphereLocator.addChild(sp);
      this.pickSphereLocator.addChild(new PickSphereTimer(this.browser, this.pickSphereHandle, this.pickTransat));

      this.pickSphereLocator.setTransform(this.tfm);
      this.browser.locale.addBranchGraph(this.pickSphereHandle);
    }
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Evagation
 * JD-Core Version:    0.6.0
 */