package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Enumeration;
import java.util.Iterator;

import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.TransparencyInterpolator;
import org.jogamp.java3d.WakeupCondition;
import org.jogamp.java3d.WakeupCriterion;

public class PickSphereTimer extends TransparencyInterpolator
{
  WakeupCondition critter;
  RGroup handle;

  public PickSphereTimer(Browser browser, RGroup handle, TransparencyAttributes ta)
  {
    super(new Alpha(1, 3, 0L, 0L, 5000L, 0L, 0L, 500L, 0L, 0L), ta);

    this.handle = handle;
    setSchedulingBoundingLeaf(browser.loader.infiniteBoundingLeaf);
    setMinimumTransparency(0.5F);
    setMaximumTransparency(0.9F);
  }

  public void processStimulus(Iterator<WakeupCriterion> critters)
  {
    if (getAlpha().finished()) {
      this.handle.detach();
      try {
        while (true) {
          this.handle.removeChild(0);
        }
      }
      catch (ArrayIndexOutOfBoundsException ex)
      {
      }
    }
    super.processStimulus(critters);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.PickSphereTimer
 * JD-Core Version:    0.6.0
 */