package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;

public class TimeSensor extends Node
{
  SFTime cycleInterval;
  SFBool enabled;
  SFBool loop;
  SFTime startTime;
  SFTime stopTime;
  SFTime cycleTime;
  SFFloat fraction;
  SFBool isActive;
  SFTime time;
  double last = 0.0D;
  boolean simActive = false;
  double activeCycleInterval;
  double activeStartTime;
  double activeStopTime;
  float lastF = 0.0F;

  public TimeSensor(Loader loader)
  {
    super(loader);

    this.cycleInterval = new SFTime(1.0D);
    this.enabled = new SFBool(true);
    this.loop = new SFBool(false);
    this.startTime = new SFTime(0.0D);
    this.stopTime = new SFTime(0.0D);
    this.cycleTime = new SFTime(0.0D);
    this.fraction = new SFFloat(0.0F);
    this.isActive = new SFBool(false);
    this.time = new SFTime(0.0D);

    loader.addTimeSensor(this);
    initFields();
  }

  public TimeSensor(Loader loader, SFTime cycleInterval, SFBool enabled, SFBool loop, SFTime startTime, SFTime stopTime)
  {
    super(loader);
    this.cycleInterval = cycleInterval;
    this.enabled = enabled;
    this.loop = loop;
    this.startTime = startTime;
    this.stopTime = stopTime;
    this.cycleTime = new SFTime(0.0D);
    this.fraction = new SFFloat(0.0F);
    this.isActive = new SFBool(false);
    this.time = new SFTime(0.0D);

    loader.addTimeSensor(this);
    initFields();
  }

  void doneParse()
  {
    if ((this.enabled.value == true) && 
      (this.loop.value == true) && (this.stopTime.time <= this.startTime.time)) {
      this.isActive.setValue(true);
    }

    this.simActive = true;
    this.implReady = true;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("enabled"))
    {
      simTick(time);
    }
    else if (eventInName.equals("startTime"))
    {
      if (this.isActive.value == true)
      {
        this.startTime.time = this.activeStartTime;
      }
      else {
        this.isActive.setValue(true);
        this.activeStartTime = this.startTime.time;
        this.activeStopTime = this.stopTime.time;
      }
    }
    else if (eventInName.equals("stopTime")) {
      if (this.isActive.value == true)
      {
        if (this.activeStopTime < this.activeStartTime) {
          this.stopTime.time = this.activeStopTime;
        }
        if ((this.activeStartTime < this.stopTime.time) && (this.stopTime.time <= time))
        {
          this.isActive.setValue(false);
        }
      }
    }
    else if (eventInName.equals("cycleInterval")) {
      if (this.isActive.value == true) {
        this.cycleInterval.time = this.activeCycleInterval;
      }
    }
    else if ((!eventInName.equals("loop")) && (!eventInName.equals("route_enabled")) && (!eventInName.equals("route_startTime")) && (!eventInName.equals("route_stopTime")) && (!eventInName.equals("route_cycleInterval")) && (!eventInName.equals("route_loop")))
    {
      if (eventInName.equals("isActive")) {
        if (this.isActive.value) {
          this.activeStartTime = this.startTime.time;
          this.activeStopTime = this.stopTime.time;
          this.activeCycleInterval = this.cycleInterval.time;
        }
      }
      else
        System.err.println("TimeSensor: unknown eventIn " + eventInName);
    }
  }

  void updateFraction(double updateTime)
  {
    float fractionValue = 0.0F;

    this.activeStopTime = this.stopTime.time;
    this.activeCycleInterval = this.cycleInterval.time;
    this.activeStartTime = this.startTime.time;

    double delta = updateTime - this.startTime.time;

    double f = Math.IEEEremainder(delta, this.cycleInterval.time);

    if (f < 0.0D) {
      f += this.cycleInterval.time;
    }
    if (f <= 0.0D) {
      if (updateTime > this.startTime.time) {
        fractionValue = 1.0F;
      }
      else {
        fractionValue = 0.0F;
      }
    }
    else {
      fractionValue = (float)(f / this.cycleInterval.time);
    }
    if (fractionValue < this.lastF) {
      this.cycleTime.setValue(updateTime);
    }
    this.lastF = fractionValue;
    this.fraction.setValue(fractionValue);
  }

  void simTick(double now)
  {
    this.time.setValue(now);

    if (this.enabled.value == true)
    {
      if ((now >= this.stopTime.time) && (this.stopTime.time > this.startTime.time) && (!this.loop.value))
      {
        if (this.isActive.value) {
          updateFraction(this.stopTime.time);
          this.isActive.setValue(false);
        }
      }
      else if ((now >= this.startTime.time + this.cycleInterval.time) && (!this.loop.value))
      {
        if (this.isActive.value) {
          updateFraction(this.startTime.time + this.cycleInterval.time);
          this.isActive.setValue(false);
        }
      }
      else if ((this.loop.value) && (this.stopTime.time > this.startTime.time) && (now >= this.stopTime.time))
      {
        if (this.isActive.value) {
          this.isActive.setValue(false);
          updateFraction(this.stopTime.time);
        }
      }
      else if ((this.loop.value) && (this.stopTime.time <= this.startTime.time) && (now >= this.startTime.time))
      {
        if (!this.isActive.value) {
          this.isActive.setValue(true);
        }
        updateFraction(now);
      }
      else if ((now > this.startTime.time) && (!this.isActive.value)) {
        this.isActive.setValue(true);
        updateFraction(now);
      }
      else if (this.isActive.value) {
        updateFraction(now);
      }
    }
    else {
      this.activeStopTime = -1.0D;
      this.activeStartTime = -1.0D;
      this.activeCycleInterval = -1.0D;
    }
  }

  void initFields()
  {
    this.cycleInterval.init(this, this.FieldSpec, 3, "cycleInterval");

    this.enabled.init(this, this.FieldSpec, 3, "enabled");
    this.loop.init(this, this.FieldSpec, 3, "loop");
    this.startTime.init(this, this.FieldSpec, 3, "startTime");
    this.stopTime.init(this, this.FieldSpec, 3, "stopTime");
    this.cycleTime.init(this, this.FieldSpec, 2, "cycleTime");
    this.fraction.init(this, this.FieldSpec, 2, "fraction");
    this.isActive.init(this, this.FieldSpec, 2, "isActive");
    this.time.init(this, this.FieldSpec, 2, "time");
  }

  public Object clone()
  {
    return new TimeSensor(this.loader, (SFTime)this.cycleInterval.clone(), (SFBool)this.enabled.clone(), (SFBool)this.loop.clone(), (SFTime)this.startTime.clone(), (SFTime)this.stopTime.clone());
  }

  public String getType()
  {
    return "TimeSensor";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.TimeSensor
 * JD-Core Version:    0.6.0
 */