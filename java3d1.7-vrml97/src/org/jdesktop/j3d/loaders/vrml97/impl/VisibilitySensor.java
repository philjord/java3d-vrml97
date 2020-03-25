package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;

public class VisibilitySensor extends Node
{
  SFVec3f center;
  SFBool enabled;
  SFVec3f size;
  SFBool isActive;
  SFTime enterTime;
  SFTime exitTime;

  public VisibilitySensor(Loader loader)
  {
    super(loader);
    this.enabled = new SFBool(true);
    this.isActive = new SFBool(true);
    this.enterTime = new SFTime(0.0D);
    this.exitTime = new SFTime(0.0D);
    loader.addVisibilitySensor(this);
    initFields();
  }

  VisibilitySensor(Loader loader, SFBool enabled, SFVec3f center, SFVec3f size, SFTime enterTime, SFTime exitTime, SFBool isActive)
  {
    super(loader);
    this.enabled = enabled;
    this.enterTime = enterTime;
    this.exitTime = exitTime;
    this.isActive = isActive;
    loader.addVisibilitySensor(this);
    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (!eventInName.equals("enabled"))
    {
      System.err.println("VisibilitySensor: unknown eventInName " + eventInName);
    }
  }

  public Object clone()
  {
    VisibilitySensor v = new VisibilitySensor(this.loader, (SFBool)this.enabled.clone(), (SFVec3f)this.center.clone(), (SFVec3f)this.size.clone(), (SFTime)this.enterTime.clone(), (SFTime)this.exitTime.clone(), (SFBool)this.isActive.clone());

    return v;
  }

  public String getType()
  {
    return "VisibilitySensor";
  }

  void initFields()
  {
    this.enabled.init(this, this.FieldSpec, 3, "enabled");
    this.center.init(this, this.FieldSpec, 3, "center");
    this.size.init(this, this.FieldSpec, 3, "size");
    this.isActive.init(this, this.FieldSpec, 2, "isActive");
    this.enterTime.init(this, this.FieldSpec, 2, "enterTime");
    this.exitTime.init(this, this.FieldSpec, 2, "exitTime");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.VisibilitySensor
 * JD-Core Version:    0.6.0
 */