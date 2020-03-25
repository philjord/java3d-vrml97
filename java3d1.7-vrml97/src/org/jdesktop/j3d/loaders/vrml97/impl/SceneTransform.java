package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Point3d;

public class SceneTransform extends Transform
{
  SceneTransform(Loader loader)
  {
    super(loader);
  }

  void initImpl()
  {
    super.initImpl();
    this.impl.setCapability(17);
    this.impl.setCapability(18);
    this.impl.setCapability(3);
    this.impl.setCapability(4);

    this.impl.setCapability(1);
    this.impl.setCapability(11);

    this.impl.setPickable(true);
  }

  void setSceneBounds(BoundingSphere b)
  {
    Point3d p = new Point3d();
    b.getCenter(p);
    this.center.setValue((float)p.x, (float)p.y, (float)p.z);
    if (b.getRadius() > 10.0D) {
      float inv = 1.0F / (float)b.getRadius();

      this.scale.setValue(inv, inv, inv);
    }
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.SceneTransform
 * JD-Core Version:    0.6.0
 */