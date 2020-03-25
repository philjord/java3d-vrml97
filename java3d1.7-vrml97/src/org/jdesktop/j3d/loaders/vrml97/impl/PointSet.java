package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.PointArray;

public class PointSet extends Geometry
{
  PointArray impl;
  BoundingBox bounds;
  SFNode color;
  SFNode coord;
  int vertexFormat = 0;
  int vertexCount = 0;
  boolean haveColors = false;
  Coordinate coordNode = null;
  Color colorNode = null;

  public PointSet(Loader loader)
  {
    super(loader);
    this.color = new SFNode(null);
    this.coord = new SFNode(null);
    initFields();
  }

  PointSet(Loader loader, SFNode coord, SFNode color)
  {
    super(loader);
    this.coord = coord;
    this.color = color;
    initFields();
  }

  public void initImpl()
  {
    if ((this.coord.node != null) && ((this.coord.node instanceof Coordinate))) {
      this.coordNode = ((Coordinate)this.coord.node);
      if (this.coordNode.point.size >= 3) {
        this.vertexFormat = 1;
        this.vertexCount = (this.coordNode.point.size / 3);

        if ((this.color.node != null) && ((this.color.node instanceof Color))) {
          this.colorNode = ((Color)this.color.node);
          if (this.colorNode.color.vals.length / 3 == this.vertexCount) {
            this.haveColors = true;
            this.vertexFormat |= 4;
          }
        }

        this.impl = new PointArray(this.vertexCount, this.vertexFormat);
        this.impl.setCoordinates(0, this.coordNode.point.value, 0, this.vertexCount);

        if (this.haveColors) {
          this.impl.setColors(0, this.colorNode.color.vals);
        }

        this.bounds = this.coordNode.point.getBoundingBox();
      }
    }
  }

  public void notifyMethod(String eventInName, double time)
  {
    if ((eventInName.equals("color")) || (eventInName.equals("coord")))
      initImpl();
  }

  public Object clone()
  {
    return new PointSet(this.loader, (SFNode)this.coord.clone(), (SFNode)this.color.clone());
  }

  public String getType()
  {
    return "PointSet";
  }

  void initFields()
  {
    this.coord.init(this, this.FieldSpec, 3, "coord");
    this.color.init(this, this.FieldSpec, 3, "color");
  }

  public org.jogamp.java3d.Geometry getImplGeom()
  {
    return this.impl;
  }

  public BoundingBox getBoundingBox()
  {
    return this.bounds;
  }

  public boolean haveTexture()
  {
    return false;
  }

  public int getNumTris()
  {
    return 0;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.PointSet
 * JD-Core Version:    0.6.0
 */