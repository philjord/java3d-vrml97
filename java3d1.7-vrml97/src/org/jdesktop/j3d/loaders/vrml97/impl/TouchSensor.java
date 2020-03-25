package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Vector;

public class TouchSensor extends Node
  implements VrmlSensor
{
  SFBool enabled;
  SFVec3f hitNormal;
  SFVec3f hitPoint;
  SFVec2f hitTexCoord;
  SFBool isActive;
  SFBool isOver;
  SFTime touchTime;
  org.jogamp.java3d.Node parentImpl;

  public TouchSensor(Loader loader)
  {
    super(loader);
    this.enabled = new SFBool(true);
    this.hitNormal = new SFVec3f();
    this.hitPoint = new SFVec3f();
    this.hitTexCoord = new SFVec2f();
    this.isActive = new SFBool(true);
    this.isOver = new SFBool(false);
    this.touchTime = new SFTime(0.0D);
    loader.addTouchSensor(this);
    initFields();
  }

  TouchSensor(Loader loader, SFBool enabled)
  {
    super(loader);
    this.enabled = enabled;
    this.hitNormal = new SFVec3f();
    this.hitPoint = new SFVec3f();
    this.hitTexCoord = new SFVec2f();
    this.isActive = new SFBool(true);
    this.isOver = new SFBool(false);
    this.touchTime = new SFTime(0.0D);
    loader.addTouchSensor(this);
    initFields();
  }

  public void initImpl()
  {
    this.loader.addTouchSensor(this);
  }

  void updateParent(org.jogamp.java3d.Node parentImpl)
  {
    if (this.loader.debug) {
      System.out.println("TouchSensor.updateParent()");
    }
    if (this.loader.loaderMode > 0) {
      if (this.loader.debug) {
        System.out.println("Touch Sensor enabling PICK_REPORTING on  parent " + parentImpl);
      }

      Vector v = (Vector)(Vector)parentImpl.getUserData();
      if (v == null) {
        v = new Vector();
        parentImpl.setUserData(v);
        if (this.loader.debug) {
          System.out.println("Touch Sensor parent: " + parentImpl + " had no user data, added vector:" + v);
        }
      }

      v.addElement(this);
      parentImpl.setCapability(1);

      parentImpl.setCapability(3);
      parentImpl.setCapability(11);

      parentImpl.setPickable(true);
    }
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (!eventInName.equals("enabled"))
    {
      if (!eventInName.equals("route_enabled"))
      {
        System.err.println("TouchSensor: unknown eventInName " + eventInName);
      }
    }
  }

  public Object clone()
  {
    TouchSensor t = new TouchSensor(this.loader, (SFBool)this.enabled.clone());
    return t;
  }

  public String getType()
  {
    return "TouchSensor";
  }

  void initFields()
  {
    this.enabled.init(this, this.FieldSpec, 3, "enabled");
    this.hitNormal.init(this, this.FieldSpec, 2, "hitNormal");
    this.hitPoint.init(this, this.FieldSpec, 2, "hitPoint");
    this.hitTexCoord.init(this, this.FieldSpec, 2, "hitTexCoord");
    this.isActive.init(this, this.FieldSpec, 2, "isActive");
    this.isOver.init(this, this.FieldSpec, 2, "isOver");
    this.touchTime.init(this, this.FieldSpec, 2, "touchTime");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.TouchSensor
 * JD-Core Version:    0.6.0
 */