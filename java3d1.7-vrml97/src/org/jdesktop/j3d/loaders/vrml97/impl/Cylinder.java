package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.Geometry;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Node;
import org.jogamp.vecmath.Point3d;

public class Cylinder extends GroupGeom
{
  SFFloat radius;
  SFFloat height;
  SFBool side;
  SFBool bottom;
  SFBool top;
  BoundingBox bounds;

  public Cylinder(Loader loader)
  {
    super(loader);
    this.radius = new SFFloat(1.0F);
    this.height = new SFFloat(2.0F);
    this.side = new SFBool(true);
    this.bottom = new SFBool(true);
    this.top = new SFBool(true);
    initFields();
  }

  Cylinder(Loader loader, SFFloat radius, SFFloat height, SFBool side, SFBool bottom, SFBool top)
  {
    super(loader);
    this.radius = radius;
    this.height = height;
    this.side = side;
    this.bottom = bottom;
    this.top = top;
    initFields();
  }

  void initFields()
  {
    this.radius.init(this, this.FieldSpec, 0, "radius");
    this.height.init(this, this.FieldSpec, 0, "height");
    this.side.init(this, this.FieldSpec, 0, "side");
    this.bottom.init(this, this.FieldSpec, 0, "bottom");
    this.top.init(this, this.FieldSpec, 0, "top");
  }

  public void notifyMethod(String eventInName, double time)
  {
  }

  public Object clone()
  {
    return new Cylinder(this.loader, (SFFloat)this.radius.clone(), (SFFloat)this.height.clone(), (SFBool)this.side.clone(), (SFBool)this.bottom.clone(), (SFBool)this.top.clone());
  }

  public void initImpl()
  {
  }

  public Group initGroupImpl(Appearance ap)
  {
    this.implGroup = new org.jogamp.java3d.utils.geometry.Cylinder(this.radius.value, this.height.value, 3, 20, 2, ap);

    this.implGroup.clearCapability(1);

    if (this.loader.debug) {
      System.out.println("Cylinder group before edit: ");
      this.loader.treePrinter.print(this.implGroup);
    }
    if (!this.bottom.value) {
      if (this.loader.debug) {
        System.out.println("no bottom");
      }
      this.implGroup.removeChild(2);
    }
    if (!this.top.value) {
      if (this.loader.debug) {
        System.out.println("no top");
      }
      this.implGroup.removeChild(1);
    }
    if (!this.side.value) {
      if (this.loader.debug) {
        System.out.println("no side");
      }
      this.implGroup.removeChild(0);
    }
    if (this.loader.debug) {
      System.out.println("Cylinder group after edit: ");
      this.loader.treePrinter.print(this.implGroup);
    }

    this.bounds = new BoundingBox(new Point3d(-this.radius.value, -this.height.value / 2.0D, -this.radius.value), new Point3d(this.radius.value, this.height.value / 2.0D, this.radius.value));

    return this.implGroup;
  }

  public BoundingBox getBoundingBox()
  {
    return this.bounds;
  }

  public Geometry getImplGeom()
  {
    throw new NullPointerException();
  }

  public boolean haveTexture()
  {
    return true;
  }

  public String getType()
  {
    return "Cylinder";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Cylinder
 * JD-Core Version:    0.6.0
 */