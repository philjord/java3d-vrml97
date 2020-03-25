package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Shape3D;

public class Shape extends NonSharedNode
{
  SFNode appearance;
  SFNode geometry;
  int numTris = 0;
  boolean ifsChangeable = false;

  public Shape(Loader loader)
  {
    super(loader);
    this.appearance = new SFNode(null);
    this.geometry = new SFNode(null);

    initFields();
  }

  public Shape(Loader loader, SFNode appearancenode, SFNode geonode)
  {
    super(loader);
    this.appearance = appearancenode;
    this.geometry = geonode;

    initFields();
  }

  void initFields()
  {
    this.appearance.init(this, this.FieldSpec, 3, "appearance");
    this.geometry.init(this, this.FieldSpec, 3, "geometry");
  }

  public void initImpl()
  {
    Appearance appNode = (Appearance)this.appearance.node;
    Geometry geomNode = (Geometry)this.geometry.node;
    this.implNode = null;

    if (appNode == null) {
      appNode = new Appearance(this.loader);
      appNode.initImpl();
    }
    if ((geomNode instanceof Text)) {
      Text textNode = (Text)geomNode;
      this.implNode = textNode.createText2D(appNode.impl);
      this.implReady = true;
    }
    else if ((geomNode instanceof GroupGeom)) {
      this.implNode = ((GroupGeom)geomNode).initGroupImpl(appNode.impl);
      this.implReady = true;
    }
    else {
      if (geomNode != null) {
        org.jogamp.java3d.Geometry geom = geomNode.getImplGeom();
        if (geom != null) {
          if (this.appearance.node == null) {
            this.implNode = new Shape3D(geom);
          }
          else {
            this.implNode = new Shape3D(geom, appNode.impl);
          }
        }
      }
      if ((appNode != null) && (geomNode != null)) {
        appNode.numUses += 1;
        if ((appNode.haveTexture) && (!geomNode.haveTexture()))
        {
          appNode.setTexGen(geomNode.getBoundingBox());
        }

        if ((geomNode instanceof Ownable))
        {
          ((Ownable)geomNode).setOwner(this);
          if (!((Ownable)geomNode).getSolid()) {
            PolygonAttributes pa = appNode.impl.getPolygonAttributes();

            if (pa == null) {
              pa = new PolygonAttributes();

              appNode.impl.setPolygonAttributes(pa);
            }
            appNode.impl.getPolygonAttributes().setCullFace(0);

            appNode.impl.getPolygonAttributes().setBackFaceNormalFlip(true);
          }
        }

        if (((geomNode instanceof IndexedLineSet)) || ((geomNode instanceof PointSet)))
        {
          Material material = appNode.impl.getMaterial();

          if (material == null) {
            material = new Material();
            appNode.impl.setMaterial(material);
          }
          material.setLightingEnable(false);
        }
      }

      if (this.defName == null)
      {
        if (this.loader.debug) {
          System.out.println("Shape.initImpl(): nulling refrences");
        }
        this.geometry.node = null;
        this.appearance.node = null;
      }
      this.implReady = true;
    }
    if (this.implNode != null)
    {
      this.numTris = geomNode.getNumTris();
      if (this.ifsChangeable) {
        ((Shape3D)this.implNode).setCapability(13);
        ((Shape3D)this.implNode).setCapability(12);
      }
    }
  }

  public int getNumTris()
  {
    return this.numTris;
  }

  public Object clone()
  {
    if (this.loader.debug) {
      System.out.println("Shape.clone() called");
    }
    Shape retval = new Shape(this.loader, (SFNode)this.appearance.clone(), (SFNode)this.geometry.clone());

    if (this.loader.debug) {
      System.out.println("Shape.clone() returns " + retval.toStringId() + " = " + retval);
    }

    return retval;
  }

  public String getType()
  {
    return "Shape";
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("geometry")) {
      if ((this.implReady) && (this.implNode == null))
      {
        initImpl();
      }
      else if (((this.implNode instanceof Shape3D)) && (!(this.geometry.node instanceof GroupGeom)))
      {
        try
        {
          initImpl();
        }
        catch (NullPointerException npe) {
          System.out.println(npe);
        }
      }
      else {
        System.err.println("Shape: Unimplemented case replacing geometry");
      }

    }
    else if (eventInName.equals("appearance")) {
      if (this.implNode != null) {
        Appearance app = (Appearance)this.appearance.node;
        if ((this.implNode instanceof Shape3D)) {
          ((Shape3D)this.implNode).setAppearance(app.impl);
        }
        else {
          System.err.println("Shape: Unimplemented case replacing appearance");
        }
      }

    }
    else if (eventInName.equals("route_ifs_changeable"))
      this.ifsChangeable = true;
  }

  public String toStringBody()
  {
    String retval = "Shape {\n";
    if (this.appearance.node != null) {
      retval = retval + "appearance " + this.appearance;
    }
    if (this.geometry.node != null) {
      retval = retval + "geometry " + this.geometry;
    }
    retval = retval + "}";
    return retval;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Shape
 * JD-Core Version:    0.6.0
 */