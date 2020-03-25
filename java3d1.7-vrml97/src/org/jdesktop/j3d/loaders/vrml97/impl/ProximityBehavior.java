package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Enumeration;
import java.util.Iterator;

import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnViewPlatformEntry;
import org.jogamp.java3d.WakeupOnViewPlatformExit;
import org.jogamp.java3d.WakeupOr;

public class ProximityBehavior extends Behavior
{
  ProximitySensor owner;
  WakeupOnViewPlatformEntry woven;
  WakeupOnViewPlatformExit wovex;
  WakeupCriterion[] conditions;
  WakeupOr wor;
  boolean active = false;

  public ProximityBehavior(ProximitySensor ps)
  {
    this.owner = ps;
  }

  public void initialize()
  {
    setSchedulingBounds(this.owner.bbox);
    this.wovex = new WakeupOnViewPlatformExit(this.owner.bbox);
    this.woven = new WakeupOnViewPlatformEntry(this.owner.bbox);
    this.conditions = new WakeupCriterion[2];
    this.conditions[0] = this.wovex;
    this.conditions[1] = this.woven;
    this.wor = new WakeupOr(this.conditions);
    wakeupOn(this.wor);
  }

  public void processStimulus(Iterator<WakeupCriterion> ofElements)
  {
    double t = Time.getNow();
    while (ofElements.hasNext()) {
      WakeupCriterion wakeup = (WakeupCriterion)ofElements.next();
      if ((wakeup instanceof WakeupOnViewPlatformExit)) {
        this.owner.exitTime.setValue(t);
        this.active = false;
        this.owner.isActive.setValue(this.active);
        wakeupOn(new WakeupOnViewPlatformEntry(this.owner.bbox));

        continue;
      }if ((wakeup instanceof WakeupOnViewPlatformEntry)) {
        this.owner.enterTime.setValue(t);
        this.active = true;
        this.owner.isActive.setValue(this.active);
        wakeupOn(new WakeupOnViewPlatformExit(this.owner.bbox));
      }
    }
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ProximityBehavior
 * JD-Core Version:    0.6.0
 */