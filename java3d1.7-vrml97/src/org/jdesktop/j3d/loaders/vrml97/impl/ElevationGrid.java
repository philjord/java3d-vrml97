package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;
import java.io.PrintStream;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point2f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;
import vrml.InvalidVRMLSyntaxException;

public class ElevationGrid extends Geometry
  implements Ownable
{
  SFNode color;
  SFNode normal;
  SFNode texCoord;
  MFFloat height;
  SFBool ccw;
  SFBool colorPerVertex;
  SFBool normalPerVertex;
  SFFloat creaseAngle;
  SFBool solid;
  SFInt32 xDimension;
  SFFloat xSpacing;
  SFInt32 zDimension;
  SFFloat zSpacing;
  GeometryArray impl;
  GeometryInfo gi;
  Point3f[] coordArray;
  Color3f[] colorArray;
  Point2f[] texCoordArray;
  Vector3f[] normalArray;
  Shape owner;
  boolean nullable = true;
  boolean haveTexture = false;

  public ElevationGrid(Loader loader)
  {
    super(loader);
    this.height = new MFFloat();
    this.color = new SFNode(null);
    this.normal = new SFNode(null);
    this.texCoord = new SFNode(null);
    this.ccw = new SFBool(true);
    this.colorPerVertex = new SFBool(true);
    this.creaseAngle = new SFFloat(0.0F);
    this.normalPerVertex = new SFBool(true);
    this.solid = new SFBool(true);
    this.xDimension = new SFInt32(0);
    this.xSpacing = new SFFloat(1.0F);
    this.zDimension = new SFInt32(0);
    this.zSpacing = new SFFloat(1.0F);

    initFields();
  }

  ElevationGrid(Loader loader, MFFloat height, SFNode color, SFNode normal, SFNode texCoord, SFBool ccw, SFBool colorPerVertex, SFFloat creaseAngle, SFBool normalPerVertex, SFBool solid, SFInt32 xDimension, SFFloat xSpacing, SFInt32 zDimension, SFFloat zSpacing)
  {
    super(loader);
    this.height = height;
    this.color = color;
    this.normal = normal;
    this.texCoord = texCoord;
    this.ccw = ccw;
    this.colorPerVertex = colorPerVertex;
    this.creaseAngle = creaseAngle;
    this.normalPerVertex = normalPerVertex;
    this.solid = solid;
    this.xDimension = xDimension;
    this.xSpacing = xSpacing;
    this.zDimension = zDimension;
    this.zSpacing = zSpacing;

    initFields();
  }

  void initImpl()
  {
    initSetup();
    this.gi = new GeometryInfo(2);

    if (this.coordArray != null) {
      this.gi.setCoordinates(this.coordArray);

      if (this.loader.debug) {
        for (int i = 0; i < this.coordArray.length; i++) {
          System.out.println(this.coordArray[i]);
        }
      }
    }
    else
    {
      throw new InvalidVRMLSyntaxException("No coordinates supplied for ElevationGrid");
    }

    if (this.colorArray != null) {
      this.gi.setColors(this.colorArray);

      ((Color)(Color)this.color.node).owner = this;
    }

    if (this.texCoordArray != null) {
      this.gi.setTextureCoordinates(this.texCoordArray);
      this.haveTexture = true;

      ((TextureCoordinate)(TextureCoordinate)this.texCoord.node).owner = this;
    }

    if (this.normalArray != null) {
      this.gi.setNormals(this.normalArray);
      ((Normal)(Normal)this.normal.node).owner = this;
    }
    else
    {
      float ca = this.creaseAngle.getValue();
      if (ca < 0.0F) {
        ca = 0.0F;
      }
      if (ca > 3.141593F) {
        ca -= 3.141593F;
      }
      NormalGenerator ng = new NormalGenerator(ca);
      ng.generateNormals(this.gi);
    }

    this.impl = this.gi.getGeometryArray();

    if (this.nullable) {
      this.height = null;
      this.color = null;
      this.normal = null;
      this.texCoord = null;
    }

    this.loader.cleanUp();
    this.implReady = true;
  }

  void initSetup()
  {
    int xD = this.xDimension.getValue();
    int zD = this.zDimension.getValue();
    int quadNum = 0;
    int quadCount = (xD - 1) * (zD - 1);
    float[] heights = this.height.mfloat;

    if (this.loader.debug) {
      System.out.println("X dimension" + xD);
      System.out.println("Z dimension" + zD);
      System.out.println("quad count" + quadCount);
      System.out.println("height" + this.height);
      for (int h = 0; h < heights.length; h++) {
        System.out.print(heights[h] + ",");
      }
    }
    if ((xD < 1) || (zD < 1)) {
      throw new InvalidVRMLSyntaxException("ElevationGrid dimensionless");
    }
    if (this.height.mfloat.length != xD * zD) {
      throw new InvalidVRMLSyntaxException("ElevationGrid dimension mismatch");
    }

    this.coordArray = new Point3f[quadCount * 4];

    this.colorArray = new Color3f[quadCount * 4];

    this.normalArray = new Vector3f[quadCount * 4];

    this.texCoordArray = new Point2f[quadCount * 4];

    for (int k = 0; k < zD - 1; k++)
      for (int j = 0; j < xD - 1; j++)
      {
        int i = j + k * xD;
        Point3f p0 = new Point3f();
        Point3f p1 = new Point3f();
        Point3f p2 = new Point3f();
        Point3f p3 = new Point3f();
        float zSp = this.zSpacing.value;
        float xSp = this.xSpacing.value;

        p0.x = (j * xSp);
        p0.y = heights[i];
        p0.z = (k * zSp);

        p1.x = (j * xSp);
        p1.y = heights[(i + xD)];
        p1.z = ((k + 1) * zSp);

        p2.x = ((j + 1) * xSp);
        p2.y = heights[(i + xD + 1)];
        p2.z = ((k + 1) * zSp);

        p3.x = ((j + 1) * xSp);
        p3.y = heights[(i + 1)];
        p3.z = (k * zSp);

        if (this.ccw.value) {
          this.coordArray[(quadNum * 4 + 0)] = p0;
          this.coordArray[(quadNum * 4 + 1)] = p1;
          this.coordArray[(quadNum * 4 + 2)] = p2;
          this.coordArray[(quadNum * 4 + 3)] = p3;
        }
        else {
          this.coordArray[(quadNum * 4 + 0)] = p0;
          this.coordArray[(quadNum * 4 + 1)] = p3;
          this.coordArray[(quadNum * 4 + 2)] = p2;
          this.coordArray[(quadNum * 4 + 3)] = p1;
        }

        if (this.loader.debug) {
          System.out.println("quadNum" + quadNum);
          System.out.println("v0 " + this.coordArray[(quadNum * 4 + 0)]);
          System.out.println("v1 " + this.coordArray[(quadNum * 4 + 1)]);
          System.out.println("v2 " + this.coordArray[(quadNum * 4 + 2)]);
          System.out.println("v3 " + this.coordArray[(quadNum * 4 + 3)]);
        }

        if (this.color.node != null) {
          Color3f c0 = new Color3f();
          Color3f c1 = new Color3f();
          Color3f c2 = new Color3f();
          Color3f c3 = new Color3f();
          MFColor mfclr = ((Color)(Color)this.color.node).color;
          float[] clrs = (float[])mfclr.vals;
          if (this.colorPerVertex.value)
          {
            int ind0 = i * 3;
            int ind1 = (i + xD) * 3;
            int ind2 = (i + xD + 1) * 3;
            int ind3 = (i + 1) * 3;

            c0.x = clrs[ind0];
            c0.y = clrs[(ind0 + 1)];
            c0.z = clrs[(ind0 + 2)];

            c1.x = clrs[ind1];
            c1.y = clrs[(ind1 + 1)];
            c1.z = clrs[(ind1 + 2)];

            c2.x = clrs[ind2];
            c2.y = clrs[(ind2 + 1)];
            c2.z = clrs[(ind2 + 2)];

            c3.x = clrs[ind3];
            c3.y = clrs[(ind3 + 1)];
            c3.z = clrs[(ind3 + 2)];

            if (this.ccw.value) {
              this.colorArray[(quadNum * 4 + 0)] = c0;
              this.colorArray[(quadNum * 4 + 1)] = c1;
              this.colorArray[(quadNum * 4 + 2)] = c2;
              this.colorArray[(quadNum * 4 + 3)] = c3;
            }
            else {
              this.colorArray[(quadNum * 4 + 0)] = c0;
              this.colorArray[(quadNum * 4 + 1)] = c3;
              this.colorArray[(quadNum * 4 + 2)] = c2;
              this.colorArray[(quadNum * 4 + 3)] = c1;
            }

          }
          else
          {
            c0.x = clrs[(quadNum * 3 + 0)];
            c0.y = clrs[(quadNum * 3 + 1)];
            c0.z = clrs[(quadNum * 3 + 2)];

            this.colorArray[(quadNum * 4 + 0)] = c0;
            this.colorArray[(quadNum * 4 + 1)] = c0;
            this.colorArray[(quadNum * 4 + 2)] = c0;
            this.colorArray[(quadNum * 4 + 3)] = c0;
          }
        }
        else {
          this.colorArray = null;
        }

        if (this.texCoord.node != null) {
          Point2f v0 = new Point2f();
          Point2f v1 = new Point2f();
          Point2f v2 = new Point2f();
          Point2f v3 = new Point2f();
          float[] tx = (float[])((TextureCoordinate)(TextureCoordinate)this.texCoord.node).point.vals;

          int ind0 = i * 2;
          int ind1 = (i + xD) * 2;
          int ind2 = (i + xD + 1) * 2;
          int ind3 = (i + 1) * 2;
          v0.x = tx[ind0];
          v0.y = tx[(ind0 + 1)];

          v1.x = tx[ind1];
          v1.y = tx[(ind1 + 1)];

          v2.x = tx[ind2];
          v2.y = tx[(ind2 + 1)];

          v3.x = tx[ind3];
          v3.y = tx[(ind3 + 1)];

          if (this.ccw.value) {
            this.texCoordArray[(quadNum * 4 + 0)] = v0;
            this.texCoordArray[(quadNum * 4 + 1)] = v1;
            this.texCoordArray[(quadNum * 4 + 2)] = v2;
            this.texCoordArray[(quadNum * 4 + 3)] = v3;
          }
          else {
            this.texCoordArray[(quadNum * 4 + 0)] = v0;
            this.texCoordArray[(quadNum * 4 + 1)] = v3;
            this.texCoordArray[(quadNum * 4 + 2)] = v2;
            this.texCoordArray[(quadNum * 4 + 3)] = v1;
          }
        }
        else
        {
          this.texCoordArray = null;
        }

        if (this.normal.node != null)
        {
          Vector3f v0 = new Vector3f();
          Vector3f v1 = new Vector3f();
          Vector3f v2 = new Vector3f();
          Vector3f v3 = new Vector3f();

          float[] no = (float[])((Normal)(Normal)this.normal.node).vector.value;

          if (this.normalPerVertex.value)
          {
            int ind0 = i * 3;
            int ind1 = (i + xD) * 3;
            int ind2 = (i + xD + 1) * 3;
            int ind3 = (i + 1) * 3;

            v0.x = no[ind0];
            v0.y = no[(ind0 + 1)];
            v0.z = no[(ind0 + 2)];

            v1.x = no[ind1];
            v1.y = no[(ind1 + 1)];
            v1.z = no[(ind1 + 2)];

            v2.x = no[ind2];
            v2.y = no[(ind2 + 1)];
            v2.z = no[(ind2 + 2)];

            v3.x = no[ind3];
            v3.y = no[(ind3 + 1)];
            v3.z = no[(ind3 + 2)];

            if (this.ccw.value) {
              this.normalArray[(quadNum * 4 + 0)] = v0;
              this.normalArray[(quadNum * 4 + 1)] = v1;
              this.normalArray[(quadNum * 4 + 2)] = v2;
              this.normalArray[(quadNum * 4 + 3)] = v3;
            }
            else {
              this.normalArray[(quadNum * 4 + 0)] = v0;
              this.normalArray[(quadNum * 4 + 1)] = v3;
              this.normalArray[(quadNum * 4 + 2)] = v2;
              this.normalArray[(quadNum * 4 + 3)] = v1;
            }

          }
          else
          {
            v0.x = no[(quadNum * 3 + 0)];
            v0.y = no[(quadNum * 3 + 1)];
            v0.z = no[(quadNum * 3 + 2)];

            this.normalArray[(quadNum * 4 + 0)] = v0;
            this.normalArray[(quadNum * 4 + 1)] = v0;
            this.normalArray[(quadNum * 4 + 2)] = v0;
            this.normalArray[(quadNum * 4 + 3)] = v0;
          }
        }
        else {
          this.normalArray = null;
        }

        quadNum++;
      }
  }

  public void notifyMethod(String eventInName, double time)
  {
    if ((eventInName.equals("route_color")) || (eventInName.equals("route_normal")) || (eventInName.equals("route_texCoord")) || (eventInName.equals("route_height")))
    {
      this.impl.setCapability(1);
      this.impl.setCapability(0);
      this.impl.setCapability(5);
      this.impl.setCapability(4);
      this.impl.setCapability(7);
      this.impl.setCapability(6);
      this.impl.setCapability(3);
      this.impl.setCapability(2);

      ((Shape3D)(Shape3D)this.owner.implNode).setCapability(13);
      this.nullable = false;
    }
    else if ((eventInName.equals("height")) || (eventInName.equals("color")) || (eventInName.equals("normal")) || (eventInName.equals("texCoord")))
    {
      initImpl();
      ((Shape3D)(Shape3D)this.owner.implNode).setGeometry(this.impl);
    }
  }

  void initFields()
  {
    this.height.init(this, this.FieldSpec, 3, "height");
    this.color.init(this, this.FieldSpec, 3, "color");
    this.normal.init(this, this.FieldSpec, 3, "normal");
    this.texCoord.init(this, this.FieldSpec, 3, "texCoord");
    this.ccw.init(this, this.FieldSpec, 0, "ccw");
    this.colorPerVertex.init(this, this.FieldSpec, 0, "colorPerVertex");
    this.creaseAngle.init(this, this.FieldSpec, 0, "creaseAngle");
    this.normalPerVertex.init(this, this.FieldSpec, 0, "normalPerVertex");
    this.solid.init(this, this.FieldSpec, 0, "solid");
    this.xDimension.init(this, this.FieldSpec, 0, "xDimension");
    this.xSpacing.init(this, this.FieldSpec, 0, "xSpacing");
    this.zDimension.init(this, this.FieldSpec, 0, "zDimension");
    this.zSpacing.init(this, this.FieldSpec, 0, "zSpacing");
  }

  public Object clone()
  {
    return new ElevationGrid(this.loader, (MFFloat)(MFFloat)this.height.clone(), (SFNode)(SFNode)this.color.clone(), (SFNode)(SFNode)this.normal.clone(), (SFNode)(SFNode)this.texCoord.clone(), (SFBool)(SFBool)this.ccw.clone(), (SFBool)(SFBool)this.colorPerVertex.clone(), (SFFloat)(SFFloat)this.creaseAngle.clone(), (SFBool)(SFBool)this.normalPerVertex.clone(), (SFBool)(SFBool)this.solid.clone(), (SFInt32)(SFInt32)this.xDimension.clone(), (SFFloat)(SFFloat)this.xSpacing.clone(), (SFInt32)(SFInt32)this.zDimension.clone(), (SFFloat)(SFFloat)this.zSpacing.clone());
  }

  public String getType()
  {
    return "ElevationGrid";
  }

  public boolean haveTexture()
  {
    return this.haveTexture;
  }

  public org.jogamp.java3d.Geometry getImplGeom()
  {
    return this.impl;
  }

  BoundingBox getBoundingBox()
  {
    Point3d epsilon = new Point3d(1.0E-006D, 1.0E-006D, 1.0E-006D);
    Point3d lower = new Point3d(this.coordArray[0]);
    Point3d upper = new Point3d(this.coordArray[0]);
    lower.sub(epsilon);
    upper.add(epsilon);

    BoundingBox b = new BoundingBox(lower, upper);

    for (int c = 1; c < this.coordArray.length; c++) {
      b.combine(new Point3d(this.coordArray[c]));
    }
    if (this.loader.debug) {
      System.out.println(b);
    }
    return b;
  }

  public boolean getSolid()
  {
    return this.solid.value;
  }

  public void setOwner(Shape s)
  {
    this.owner = s;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ElevationGrid
 * JD-Core Version:    0.6.0
 */