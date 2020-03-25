package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Enumeration;
import java.util.Iterator;

import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.WakeupCondition;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnElapsedFrames;

public class SimTicker extends Behavior
{
  Browser br;
  WakeupCondition critter;

  public SimTicker(Browser b)
  {
    this.br = b;
  }

  public void initialize()
  {
    this.critter = new WakeupOnElapsedFrames(0);
    this.br.preRender();
    this.br.postRender();

    wakeupOn(this.critter);
  }

  public void processStimulus(Iterator<WakeupCriterion> critters)
  {
    this.br.postRender();
    this.br.preRender();
    this.critter = new WakeupOnElapsedFrames(0);
    wakeupOn(this.critter);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SimTicker
 * JD-Core Version:    0.6.0
 */