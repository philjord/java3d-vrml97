package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Enumeration;
import java.util.Iterator;

import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.WakeupCondition;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnElapsedFrames;

public class FrameCounter extends Behavior
{
  Browser br;
  WakeupCondition critter;
  int count;
  String name;
  RGroup rHandle;

  public FrameCounter(Browser b, int count, String name)
  {
    this.br = b;
    this.count = count;
    this.name = name;
    this.rHandle = new RGroup();
    this.rHandle.addChild(this);
  }

  public void initialize()
  {
    this.critter = new WakeupOnElapsedFrames(this.count);

    wakeupOn(this.critter);
  }

  public void processStimulus(Iterator<WakeupCriterion> critters)
  {
    while (critters.hasNext())
      if ((critters.next() instanceof WakeupOnElapsedFrames))
        this.br.frameCountCallback(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.FrameCounter
 * JD-Core Version:    0.6.0
 */