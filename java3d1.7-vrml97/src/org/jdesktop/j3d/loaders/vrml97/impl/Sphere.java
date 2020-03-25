package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.Geometry;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Node;
import org.jogamp.vecmath.Point3d;

public class Sphere extends GroupGeom
{
  SFFloat radius;
  BoundingBox bounds;

  public Sphere(Loader loader)
  {
    super(loader);
    this.radius = new SFFloat(1.0F);
    initFields();
  }

  Sphere(Loader loader, SFFloat radius)
  {
    super(loader);
    this.radius = radius;
    initFields();
  }

  public void initImpl()
  {
  }

  public Group initGroupImpl(Appearance ap)
  {
    if (ap == null) {
      ap = new Appearance();
    }

    this.implGroup = new org.jogamp.java3d.utils.geometry.Sphere(this.radius.value, 19, 20, ap);

    ((org.jogamp.java3d.utils.geometry.Sphere)this.implGroup).getShape().clearCapability(1);
    this.implGroup.setCapability(3);

    this.bounds = new BoundingBox(new Point3d(-this.radius.value, -this.radius.value, -this.radius.value), new Point3d(this.radius.value, this.radius.value, this.radius.value));

    return this.implGroup;
  }

  public String getType()
  {
    return "Sphere";
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (this.loader.debug)
      System.out.println("Sphere.notifyMethod: unexpected eventInName" + eventInName);
  }

  void initFields()
  {
    this.radius.init(this, this.FieldSpec, 0, "radius");
  }

  public Object clone()
  {
    return new Sphere(this.loader, (SFFloat)this.radius.clone());
  }

  public Geometry getImplGeom()
  {
    throw new NullPointerException();
  }

  void updateParent(Node impl)
  {
    super.updateParent(impl);
  }

  public boolean haveTexture()
  {
    return false;
  }

  public BoundingBox getBoundingBox()
  {
    return this.bounds;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Sphere
 * JD-Core Version:    0.6.0
 */