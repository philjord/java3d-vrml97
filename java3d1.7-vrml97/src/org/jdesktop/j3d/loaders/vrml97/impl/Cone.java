package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.Geometry;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Node;
import org.jogamp.vecmath.Point3d;

public class Cone extends GroupGeom
{
  SFFloat bottomRadius;
  SFFloat height;
  SFBool side;
  SFBool bottom;
  BoundingBox bounds;

  public Cone(Loader loader)
  {
    super(loader);
    this.bottomRadius = new SFFloat(1.0F);
    this.height = new SFFloat(2.0F);
    this.side = new SFBool(true);
    this.bottom = new SFBool(true);
    initFields();
  }

  Cone(Loader loader, SFFloat bottomRadius, SFFloat height, SFBool side, SFBool bottom)
  {
    super(loader);
    this.bottomRadius = bottomRadius;
    this.height = height;
    this.side = side;
    this.bottom = bottom;
    initFields();
  }

  void initFields()
  {
    this.bottomRadius.init(this, this.FieldSpec, 0, "bottomRadius");
    this.height.init(this, this.FieldSpec, 0, "height");
    this.side.init(this, this.FieldSpec, 0, "side");
    this.bottom.init(this, this.FieldSpec, 0, "bottom");
  }

  public void notifyMethod(String eventInName, double time)
  {
  }

  public Object clone()
  {
    return new Cone(this.loader, (SFFloat)this.bottomRadius.clone(), (SFFloat)this.height.clone(), (SFBool)this.side.clone(), (SFBool)this.bottom.clone());
  }

  public void initImpl()
  {
  }

  public Group initGroupImpl(Appearance ap)
  {
    this.implGroup = new org.jogamp.java3d.utils.geometry.Cone(this.bottomRadius.value, this.height.value, 19, 30, 4, ap);

    this.implGroup.clearCapability(1);

    if (this.loader.debug) {
      System.out.println("Cone group before edit: ");
      this.loader.treePrinter.print(this.implGroup);
    }
    if (!this.bottom.value) {
      if (this.loader.debug) {
        System.out.println("no bottom");
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
      System.out.println("Cone group after edit: ");
      this.loader.treePrinter.print(this.implGroup);
    }

    this.bounds = new BoundingBox(new Point3d(-this.bottomRadius.value, -this.height.value / 2.0D, -this.bottomRadius.value), new Point3d(this.bottomRadius.value, this.height.value / 2.0D, this.bottomRadius.value));

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
    return "Cone";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Cone
 * JD-Core Version:    0.6.0
 */