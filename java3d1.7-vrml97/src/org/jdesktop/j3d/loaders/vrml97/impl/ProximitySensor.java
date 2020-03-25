package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.BoundingBox;
import org.jogamp.vecmath.Point3d;

public class ProximitySensor extends Node
{
  SFVec3f center;
  SFVec3f size;
  SFBool enabled;
  SFBool isActive;
  SFVec3f position;
  SFRotation orientation;
  SFTime enterTime;
  SFTime exitTime;
  BoundingBox bbox;
  ProximityBehavior impl;

  public ProximitySensor(Loader loader)
  {
    super(loader);
    this.center = new SFVec3f(0.0F, 0.0F, 0.0F);
    this.size = new SFVec3f(0.0F, 0.0F, 0.0F);
    this.enabled = new SFBool(true);
    this.isActive = new SFBool(false);
    this.position = new SFVec3f();
    this.orientation = new SFRotation();
    this.enterTime = new SFTime();
    this.exitTime = new SFTime();
    initFields();
  }

  ProximitySensor(Loader loader, SFVec3f center, SFVec3f size, SFBool enabled)
  {
    super(loader);
    this.center = center;
    this.size = size;
    this.enabled = enabled;
    this.isActive = new SFBool(false);
    this.position = new SFVec3f();
    this.orientation = new SFRotation();
    this.enterTime = new SFTime();
    this.exitTime = new SFTime();
    initFields();
  }

  void initImpl()
  {
    this.bbox = new BoundingBox(new Point3d(this.center.value[0] - this.size.value[0] / 2.0F, this.center.value[1] - this.size.value[1] / 2.0F, this.center.value[2] - this.size.value[2] / 2.0F), new Point3d(this.center.value[0] + this.size.value[0] / 2.0F, this.center.value[1] + this.size.value[1] / 2.0F, this.center.value[2] + this.size.value[2] / 2.0F));

    this.impl = new ProximityBehavior(this);
  }

  void initFields()
  {
    this.enabled.init(this, this.FieldSpec, 3, "enabled");
    this.center.init(this, this.FieldSpec, 3, "center");
    this.size.init(this, this.FieldSpec, 3, "size");
    this.isActive.init(this, this.FieldSpec, 2, "isActive");
    this.position.init(this, this.FieldSpec, 2, "position");
    this.orientation.init(this, this.FieldSpec, 2, "orientation");
    this.enterTime.init(this, this.FieldSpec, 2, "enterTime");
    this.exitTime.init(this, this.FieldSpec, 2, "exitTime");
  }

  public Object clone()
  {
    return new ProximitySensor(this.loader, (SFVec3f)this.center.clone(), (SFVec3f)this.size.clone(), (SFBool)this.enabled.clone());
  }

  public void notifyMethod(String eventInName, double time)
  {
    if ((eventInName.equals("enabled")) && 
      (this.enabled.getValue() == true))
      this.impl.initialize();
  }

  public String getType()
  {
    return "ProximitySensor";
  }

  public org.jogamp.java3d.Node getImplNode()
  {
    return this.impl;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ProximitySensor
 * JD-Core Version:    0.6.0
 */