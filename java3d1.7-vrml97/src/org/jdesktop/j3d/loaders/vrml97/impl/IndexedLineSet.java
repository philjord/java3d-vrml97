package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.LineStripArray;

public class IndexedLineSet extends Geometry
{
  LineStripArray impl;
  BoundingBox bounds;
  SFNode color;
  SFNode coord;
  MFInt32 colorIndex;
  SFBool colorPerVertex;
  MFInt32 coordIndex;
  int vertexFormat = 0;
  int numIndices = 0;
  int numLines = 0;
  boolean haveColor = false;
  int[] lineSizes;
  int[] implCoordIndex = null;
  int[] implColorIndex = null;
  Coordinate coordNode = null;
  Color colorNode = null;

  public IndexedLineSet(Loader loader)
  {
    super(loader);
    this.colorPerVertex = new SFBool(true);
    this.color = new SFNode();
    this.coord = new SFNode();
    this.colorIndex = new MFInt32();
    this.coordIndex = new MFInt32();
    initFields();
  }

  public IndexedLineSet(Loader loader, SFNode coord, MFInt32 coordIndex, SFNode color, MFInt32 colorIndex, SFBool colorPerVertex)
  {
    super(loader);
    this.coord = coord;
    this.coordIndex = coordIndex;
    this.color = color;
    this.colorIndex = colorIndex;
    this.colorPerVertex = colorPerVertex;
    initFields();
  }

  public void initImpl()
  {
    if ((this.coord != null) && ((this.coord.node instanceof Coordinate)) && (this.coordIndex.size != 0))
    {
      this.coordNode = ((Coordinate)this.coord.node);
      this.vertexFormat = 1;
      this.numLines = this.coordIndex.primCount();
      this.numIndices = this.coordIndex.indexCount();
      this.lineSizes = new int[this.numLines];
      this.implCoordIndex = new int[this.numIndices];
      this.coordIndex.fillImplArrays(this.lineSizes, this.implCoordIndex);

      if ((this.color != null) && ((this.color.node instanceof Color))) {
        this.colorNode = ((Color)this.color.node);
        this.vertexFormat |= 4;
        this.haveColor = true;
        if (!this.colorPerVertex.value) {
          if (this.colorIndex.size == 0) {
            this.implColorIndex = new int[this.numIndices];
            int curIndex = 0;
            for (int j = 0; j < this.numLines; j++) {
              for (int i = 0; i < this.lineSizes[j]; i++) {
                this.implColorIndex[(curIndex++)] = j;
              }
            }
          }
          else
          {
            if (this.colorIndex.size != this.numLines) {
              System.out.println("ILS: colorIndex.size = " + this.colorIndex.size + " != numLines = " + this.numLines);
            }

            this.implColorIndex = new int[this.numIndices];
            int curIndex = 0;
            for (int j = 0; j < this.numLines; j++) {
              for (int i = 0; i < this.lineSizes[j]; i++) {
                this.implColorIndex[(curIndex++)] = this.colorIndex.value[j];
              }
            }

          }

        }
        else if (this.colorIndex.size == 0) {
          this.implColorIndex = this.implCoordIndex;
        }
        else
        {
          this.implColorIndex = new int[this.numIndices];
          if (this.coordIndex.fillImplArraysTest(this.lineSizes, this.implColorIndex));
        }

      }

      float[] implCoords = new float[3 * this.numIndices];
      for (int i = 0; i < this.numIndices; i++) {
        int implBase = i * 3;
        int indexBase = this.implCoordIndex[i] * 3;
        implCoords[implBase] = this.coordNode.point.value[indexBase];
        implCoords[(implBase + 1)] = this.coordNode.point.value[(indexBase + 1)];
        implCoords[(implBase + 2)] = this.coordNode.point.value[(indexBase + 2)];
      }

      this.impl = new LineStripArray(this.numIndices, this.vertexFormat, this.lineSizes);
      this.impl.setCoordinates(0, implCoords);

      if (this.haveColor) {
        float[] implColors = new float[3 * this.numIndices];
        for (int i = 0; i < this.numIndices; i++) {
          int implBase = i * 3;
          int indexBase = this.implColorIndex[i] * 3;
          implColors[implBase] = this.colorNode.color.vals[indexBase];
          implColors[(implBase + 1)] = this.colorNode.color.vals[(indexBase + 1)];
          implColors[(implBase + 2)] = this.colorNode.color.vals[(indexBase + 2)];
        }
        this.impl.setColors(0, implColors);
      }

      this.bounds = this.coordNode.point.getBoundingBox();
    }
  }

  public void notifyMethod(String eventInName, double time)
  {
    if ((eventInName.equals("colorIndex")) || (eventInName.equals("coordIndex")))
      initImpl();
  }

  public Object clone()
  {
    return new IndexedLineSet(this.loader, (SFNode)this.coord.clone(), (MFInt32)this.coordIndex.clone(), (SFNode)this.color.clone(), (MFInt32)this.colorIndex.clone(), (SFBool)this.colorPerVertex.clone());
  }

  public String getType()
  {
    return "IndexedLineSet";
  }

  void initFields()
  {
    this.coord.init(this, this.FieldSpec, 3, "coord");
    this.coordIndex.init(this, this.FieldSpec, 1, "coordIndex");
    this.color.init(this, this.FieldSpec, 3, "color");
    this.colorIndex.init(this, this.FieldSpec, 1, "colorIndex");
    this.colorPerVertex.init(this, this.FieldSpec, 0, "colorPerVertex");
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
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.IndexedLineSet
 * JD-Core Version:    0.6.0
 */