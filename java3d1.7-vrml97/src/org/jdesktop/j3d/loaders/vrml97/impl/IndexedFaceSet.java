package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;
import java.io.PrintStream;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.IndexedTriangleFanArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.TriangleArray;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;
import vrml.InvalidVRMLSyntaxException;

public class IndexedFaceSet extends Geometry
  implements Reusable, Ownable
{
  GeometryArray impl;
  MFInt32 colorIndex;
  MFInt32 coordIndex;
  MFInt32 normalIndex;
  MFInt32 texCoordIndex;
  SFNode color;
  SFNode coord;
  SFNode normal;
  SFNode texCoord;
  SFBool ccw;
  SFBool colorPerVertex;
  SFBool convex;
  SFFloat creaseAngle;
  SFBool normalPerVertex;
  SFBool solid;
  int numTris = 0;

  int constSize = 0;
  boolean isConstSize = false;
  int vertexCount;
  int vertexFormat;
  int numFaces;
  int numIndices;
  boolean haveNormals = false;
  boolean haveColors = false;
  boolean haveTexture = false;
  int[] facetSizes;
  int[] implCoordIndex;
  int[] implNormalIndex;
  int[] implColorIndex;
  int[] implTexIndex;
  float[] indexColorVals;
  float[] indexTexVals;
  static String warnId = new String("IndexedFaceSet()");

  Point3f[] coordArray = null;
  int coordArrayLength = 0;
  Vector3f[] normalArray = null;
  int normalArrayLength = 0;
  int[] tempFace;
  int tempFaceLength = 0;
  GeometryInfo gi;
  BoundingBox bounds;
  boolean allTriangles;
  protected static final int GENERAL = 100;
  protected static final int TRIS = 101;
  protected static final int QUAD = 102;
  int implType;
  IndexedTriangleFanArray implIndexed;
  TriangleArray implTris;
  Shape owner;

  public IndexedFaceSet(Loader loader)
  {
    super(loader);
    this.colorIndex = new MFInt32();
    this.coordIndex = new MFInt32();
    this.normalIndex = new MFInt32();
    this.texCoordIndex = new MFInt32();

    this.coord = new SFNode(null);
    this.normal = new SFNode(null);
    this.color = new SFNode(null);
    this.texCoord = new SFNode(null);

    this.ccw = new SFBool(true);
    this.colorPerVertex = new SFBool(true);
    this.convex = new SFBool(true);
    if (loader.autoSmooth) {
      this.creaseAngle = new SFFloat(3.14F);
    }
    else {
      this.creaseAngle = new SFFloat(0.0F);
    }
    this.normalPerVertex = new SFBool(true);
    this.solid = new SFBool(true);

    initFields();
  }

  IndexedFaceSet(Loader loader, MFInt32 colorIndex, MFInt32 coordIndex, MFInt32 normalIndex, MFInt32 texCoordIndex, SFNode coord, SFNode normal, SFNode color, SFNode texCoord, SFBool ccw, SFBool colorPerVertex, SFBool convex, SFFloat creaseAngle, SFBool normalPerVertex, SFBool solid)
  {
    super(loader);
    this.colorIndex = colorIndex;
    this.coordIndex = coordIndex;
    this.normalIndex = normalIndex;
    this.texCoordIndex = texCoordIndex;
    this.coord = coord;
    this.normal = normal;
    this.color = color;
    this.texCoord = texCoord;
    this.ccw = ccw;
    this.colorPerVertex = colorPerVertex;
    this.convex = convex;
    this.creaseAngle = creaseAngle;
    this.normalPerVertex = normalPerVertex;
    this.solid = solid;

    initFields();
  }

  public void reset()
  {
  }

  private void initSetup()
  {
    Coordinate coordNode = (Coordinate)this.coord.node;
    int coordValsSize = coordNode.point.size;
    float[] coordVals = coordNode.point.value;
    int coordListSize = this.coordIndex.size;
    int[] coordList = this.coordIndex.value;
    this.numFaces = 0;
    this.numIndices = 0;
    int curSize = 0;

    this.constSize = 0;
    this.isConstSize = false;

    this.bounds = coordNode.point.getBoundingBox();

    for (int i = 0; i < coordListSize; i++) {
      if (coordList[i] <= coordValsSize - 1)
        continue;
      coordList[i] %= coordValsSize;
    }

    if (this.normal.node != null) {
      Normal normalNode = (Normal)this.normal.node;
      float[] norms = normalNode.vector.value;

      if (this.normalIndex.size > 0) {
        for (int i = 0; i < this.normalIndex.size; i++) {
          if (this.normalIndex.value[i] > norms.length - 1) {
            this.normalIndex.value[i] %= norms.length;
          }
        }

      }

    }

    boolean lastCoord = false;
    for (int i = 0; i < coordListSize; i++) {
      if (coordList[i] == -1) {
        this.numTris += curSize - 2;
        if (this.numFaces == 0) {
          this.isConstSize = true;
          this.constSize = curSize;
        }
        else if (curSize != this.constSize) {
          this.isConstSize = false;
        }

        this.numFaces += 1;
        curSize = 0;
        lastCoord = false;
      }
      else {
        this.numIndices += 1;
        curSize++;
        lastCoord = true;
      }
    }
    if (lastCoord)
    {
      this.numTris += curSize - 2;
      if (this.numFaces == 0) {
        this.isConstSize = true;
        this.constSize = curSize;
      }
      else if (curSize != this.constSize) {
        this.isConstSize = false;
      }

      this.numFaces += 1;
    }
    this.implType = 100;
    if (this.isConstSize == true) {
      if (this.constSize == 4) {
        this.implType = 102;
        if (this.loader.debug) {
          System.out.println("Const size IFS with " + this.numFaces + " quads");
        }

      }
      else if (this.constSize == 3) {
        this.implType = 101;
        if (this.loader.debug) {
          System.out.println("Const size IFS with " + this.numFaces + " tris");
        }

      }
      else if (this.loader.debug) {
        System.out.println("Const size IFS with " + this.numFaces + " faces of size " + this.constSize);
      }

    }
    else if (this.loader.debug) {
      System.out.println("Variable size IFS, numIndicies = " + this.numIndices);

      System.out.println("curSize = " + curSize);
      System.out.println("numFaces = " + this.numFaces);
      System.out.println("(Index)coordList.length= " + coordList.length);

      System.out.println("(Points)coordVals.length= " + coordVals.length);
    }
  }

  void copyNormals(int normalValSize, float[] normalVals)
  {
    int numNormals = normalValSize / 3;
    if (this.normalArrayLength < numNormals) {
      Vector3f[] newNormalArray = new Vector3f[numNormals];
      if (this.normalArray != null) {
        System.arraycopy(this.normalArray, 0, newNormalArray, 0, this.normalArrayLength);
      }

      for (int i = this.normalArrayLength; i < numNormals; i++) {
        newNormalArray[i] = new Vector3f();
      }
      this.normalArray = newNormalArray;
      this.normalArrayLength = numNormals;
    }
    int i = 0; for (int curVal = 0; i < numNormals; i++) {
      this.normalArray[i].x = normalVals[(curVal++)];
      this.normalArray[i].y = normalVals[(curVal++)];
      this.normalArray[i].z = normalVals[(curVal++)];
    }
  }

  private void setupIndexNormals(int coordListSize, int[] coordList)
  {
    this.haveNormals = false;

    int normalListSize = this.normalIndex.size;
    int[] normalList = this.normalIndex.value;

    if (this.normalPerVertex.value == true) {
      if ((normalList == null) || (normalListSize == 0)) {
        normalListSize = coordListSize;
        normalList = coordList;
      }

      if (normalList == coordList) {
        this.implNormalIndex = this.implCoordIndex;
      }
      else {
        this.implNormalIndex = new int[this.numIndices];
        if (!this.normalIndex.fillImplArraysTest(this.facetSizes, this.implNormalIndex))
        {
          this.loader.warning(warnId, "facet sizes on normalIndex  don't match coordIndex");
        }

      }

      Normal normalNode = (Normal)this.normal.node;
      if (normalNode != null) {
        int normalValSize = normalNode.vector.size;
        float[] normalVals = normalNode.vector.value;
        if (normalVals == null) {
          this.loader.warning(warnId, "normalVals is null, ignoring normals");

          return;
        }
        copyNormals(normalValSize, normalVals);

        this.haveNormals = true;
      }
      else {
        this.haveNormals = false;
      }
    }
    else
    {
      if ((normalListSize > 0) && (normalListSize != this.numFaces)) {
        this.loader.warning(warnId, "normalIndex length != number of faces");
      }

      this.implNormalIndex = new int[this.numIndices];
      int curIndex = 0;
      for (int curFace = 0; curFace < this.numFaces; curFace++) {
        for (int j = 0; j < this.facetSizes[curFace]; j++) {
          if (curFace < normalListSize) {
            this.implNormalIndex[(curIndex++)] = normalList[curFace];
          }
          else
          {
            this.implNormalIndex[(curIndex++)] = curFace;
          }
        }

      }

      Normal normalNode = (Normal)this.normal.node;
      if (normalNode != null) {
        int normalValSize = normalNode.vector.size;
        float[] normalVals = normalNode.vector.value;
        if (normalListSize == 0);
        copyNormals(normalValSize, normalVals);
        this.haveNormals = true;
      }
    }
  }

  private void setupIndexTextures(int coordListSize, int[] coordList)
  {
    this.haveTexture = false;

    int texListSize = this.texCoordIndex.size;
    int[] texList = this.texCoordIndex.value;
    if ((texList == null) || (texListSize == 0)) {
      texListSize = coordListSize;
      texList = coordList;
    }

    if (texList == coordList) {
      this.implTexIndex = this.implCoordIndex;
    }
    else {
      this.implTexIndex = new int[this.numIndices];
      if (!this.texCoordIndex.fillImplArraysTest(this.facetSizes, this.implTexIndex))
      {
        this.loader.warning(warnId, "texCoordIndex does not match coordIndex");
      }

    }

    TextureCoordinate texNode = (TextureCoordinate)this.texCoord.node;
    if (texNode != null) {
      this.indexTexVals = texNode.point.vals;
      if (this.indexTexVals == null) {
        this.loader.warning(warnId, "texture value is null");
        return;
      }

      this.haveTexture = true;
    }
  }

  private void setupIndexColors(int coordListSize, int[] coordList)
  {
    this.haveColors = false;

    int colorListSize = this.colorIndex.size;
    int[] colorList = this.colorIndex.value;

    if (this.colorPerVertex.value == true) {
      if ((colorList == null) || (colorListSize == 0)) {
        colorListSize = coordListSize;
        colorList = coordList;
      }

      if (colorList == coordList) {
        this.implColorIndex = this.implCoordIndex;
      }
      else {
        this.implColorIndex = new int[this.numIndices];
        if (!this.colorIndex.fillImplArraysTest(this.facetSizes, this.implColorIndex))
        {
          this.loader.warning(warnId, "colorIndex does not match coordIndex");
        }

      }

      Color colorNode = (Color)this.color.node;
      if (colorNode != null) {
        this.indexColorVals = colorNode.color.vals;
        if (this.indexColorVals == null) {
          this.loader.warning(warnId, "color is null");
          return;
        }

        this.haveColors = true;
      }
    }
    else
    {
      if ((colorListSize > 0) && (colorListSize != this.numFaces)) {
        this.loader.warning(warnId, "colorIndex size != num faces");
      }

      this.implColorIndex = new int[this.numIndices];
      int curIndex = 0;
      for (int curFace = 0; curFace < this.numFaces; curFace++) {
        for (int j = 0; j < this.facetSizes[curFace]; j++) {
          if (curFace < colorListSize) {
            this.implColorIndex[(curIndex++)] = colorList[curFace];
          }
          else
          {
            this.implColorIndex[(curIndex++)] = curFace;
          }
        }

      }

      Color colorNode = (Color)this.color.node;
      if (colorNode != null) {
        this.indexColorVals = colorNode.color.vals;
        if (colorListSize == 0)
        {
          if (this.indexColorVals.length != this.numFaces * 3) {
            this.loader.warning(warnId, "color size != (3 * num faces)");
            return;
          }
        }
        this.haveColors = true;
      }
    }
  }

  private void buildIndexLists()
  {
    Coordinate coordNode = (Coordinate)this.coord.node;
    int coordValSize = coordNode.point.size;
    float[] coordVals = coordNode.point.value;
    int coordListSize = this.coordIndex.size;
    int[] coordList = this.coordIndex.value;

    int numCoords = coordValSize / 3;
    if (this.coordArrayLength < numCoords) {
      Point3f[] newCoordArray = new Point3f[coordValSize / 3];
      if (this.coordArray != null) {
        System.arraycopy(this.coordArray, 0, newCoordArray, 0, this.coordArrayLength);
      }

      for (int i = this.coordArrayLength; i < numCoords; i++) {
        newCoordArray[i] = new Point3f();
      }
      this.coordArray = newCoordArray;
      this.coordArrayLength = numCoords;
    }
    int i = 0; for (int curVal = 0; i < numCoords; i++) {
      this.coordArray[i].x = coordVals[(curVal++)];
      this.coordArray[i].y = coordVals[(curVal++)];
      this.coordArray[i].z = coordVals[(curVal++)];
    }

    this.facetSizes = new int[this.numFaces];
    this.implCoordIndex = new int[this.numIndices];

    this.coordIndex.fillImplArrays(this.facetSizes, this.implCoordIndex);

    if (!this.ccw.getValue())
    {
      int curIndex = 0;
      for (i = 0; i < this.numFaces; i++) {
        int curFaceSize = this.facetSizes[i];
        if (this.tempFaceLength < curFaceSize) {
          this.tempFace = new int[curFaceSize];
        }
        this.tempFaceLength = curFaceSize;
        int faceBeginIndex = curIndex;
        for (int j = 0; j < curFaceSize; j++) {
          this.tempFace[j] = this.implCoordIndex[curIndex];
          curIndex++;
        }
        for (int j = 0; j < curFaceSize; j++) {
          this.implCoordIndex[(faceBeginIndex + j)] = this.tempFace[(this.tempFaceLength - j - 1)];
        }
      }

    }

    setupIndexNormals(coordListSize, coordList);

    setupIndexColors(coordListSize, coordList);

    setupIndexTextures(coordListSize, coordList);
  }

  public void initImpl()
  {
    Coordinate coordNode = (Coordinate)this.coord.node;
    TextureCoordinate texCoordNode = (TextureCoordinate)this.texCoord.node;
    Normal normalNode = (Normal)this.normal.node;
    Color colorNode = (Color)this.color.node;
    if ((coordNode == null) || (this.coordIndex.size <= 0)) {
      if (this.loader.debug) {
        System.out.println("IFS coordIndex.size =" + this.coordIndex.size);
      }

    }
    else
    {
      if (coordNode != null) {
        coordNode.owner = this;
      }
      if (normalNode != null) {
        normalNode.owner = this;
      }
      if (texCoordNode != null) {
        texCoordNode.owner = this;
      }
      if (colorNode != null) {
        colorNode.owner = this;
      }

      try
      {
        buildImpl();
        this.impl = this.gi.getGeometryArray();
      }
      catch (ArrayIndexOutOfBoundsException aioobe)
      {
        buildImpl();
        this.impl = this.gi.getIndexedGeometryArray();
      }
    }

    this.loader.cleanUp();
    this.implReady = true;
  }

  void buildImpl()
  {
    this.gi = null;
    initSetup();
    if (this.implType == 101) {
      this.gi = new GeometryInfo(1);
    }
    else if (this.implType == 102) {
      this.gi = new GeometryInfo(2);
    }
    else
    {
      this.gi = new GeometryInfo(3);
    }

    buildIndexLists();

    this.gi.setCoordinates(this.coordArray);
    this.gi.setCoordinateIndices(this.implCoordIndex);
    if (this.implType == 100) {
      this.gi.setStripCounts(this.facetSizes);
    }
    if (this.haveColors) {
      this.gi.setColors3(this.indexColorVals);
      this.gi.setColorIndices(this.implColorIndex);
    }
    if (this.haveTexture) {
      this.gi.setTextureCoordinateParams(1, 2);
      this.gi.setTextureCoordinates(0, this.indexTexVals);
      if (!this.ccw.getValue()) {
        int[] implTexIndexCW = new int[this.implTexIndex.length];
        for (int i = 0; i < this.implTexIndex.length; i++) {
          implTexIndexCW[i] = this.implTexIndex[(this.implTexIndex.length - (i + 1))];
        }
        this.implTexIndex = implTexIndexCW;
      }
      this.gi.setTextureCoordinateIndices(0, this.implTexIndex);
    }
    if (this.haveNormals) {
      this.gi.setNormals(this.normalArray);
      this.gi.setNormalIndices(this.implNormalIndex);
      validateIndexes();
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
      validateIndexes();
      NormalGenerator ng = new NormalGenerator(ca);
      ng.generateNormals(this.gi);
    }
  }

  void validateIndexes()
    throws InvalidVRMLSyntaxException
  {
    try
    {
      this.gi.recomputeIndices();
    } catch (IllegalArgumentException e) {
      InvalidVRMLSyntaxException i = new InvalidVRMLSyntaxException("in DEF " + this.defName);
      i.initCause(e);
      throw i;
    } catch (ArrayIndexOutOfBoundsException e) {
      InvalidVRMLSyntaxException i = new InvalidVRMLSyntaxException("in DEF " + this.defName);
      i.initCause(e);
      throw i;
    }
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
    return this.haveTexture;
  }

  public int getNumTris()
  {
    if (this.loader.debug) {
      System.out.println("IFS num tris: " + this.numTris);
    }
    return this.numTris;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if ((eventInName.equals("coord")) || (eventInName.equals("color")) || (eventInName.equals("normal")) || (eventInName.equals("coordIndex")) || (eventInName.equals("colorIndex")) || (eventInName.equals("normalIndex")) || (eventInName.equals("texCoord")) || (eventInName.equals("texCoordIndex")))
    {
      if (this.loader.debug) {
        System.out.println("updating IFS impl from route!");
      }
      initImpl();
      ((Shape3D)(Shape3D)this.owner.implNode).setGeometry(this.impl);
    }
    else if ((eventInName.equals("route_coord")) || (eventInName.equals("route_coordIndex")) || (eventInName.equals("route_coord_point")) || (eventInName.equals("route_color")) || (eventInName.equals("route_colorIndex")) || (eventInName.equals("route_normal")) || (eventInName.equals("route_normalIndex")) || (eventInName.equals("route_texCoord")) || (eventInName.equals("route_texCoordIndex")))
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
    }
    else
    {
      System.err.println("IndexFaceSet.notifyMethod(): unknown eventInName: " + eventInName);
    }
  }

  public Object clone()
  {
    if (this.loader.debug) {
      System.out.println("IFS.clone() called");
    }
    Object o = new IndexedFaceSet(this.loader, (MFInt32)this.colorIndex.clone(), (MFInt32)this.coordIndex.clone(), (MFInt32)this.normalIndex.clone(), (MFInt32)this.texCoordIndex.clone(), (SFNode)this.coord.clone(), (SFNode)this.normal.clone(), (SFNode)this.color.clone(), (SFNode)this.texCoord.clone(), (SFBool)this.ccw.clone(), (SFBool)this.colorPerVertex.clone(), (SFBool)this.convex.clone(), (SFFloat)this.creaseAngle.clone(), (SFBool)this.normalPerVertex.clone(), (SFBool)this.solid.clone());

    this.loader.cleanUp();
    return o;
  }

  public String getType()
  {
    return "IndexedFaceSet";
  }

  void initFields()
  {
    this.colorIndex.init(this, this.FieldSpec, 1, "colorIndex");
    this.coordIndex.init(this, this.FieldSpec, 1, "coordIndex");
    this.normalIndex.init(this, this.FieldSpec, 1, "normalIndex");
    this.texCoordIndex.init(this, this.FieldSpec, 1, "texCoordIndex");

    this.color.init(this, this.FieldSpec, 3, "color");
    this.coord.init(this, this.FieldSpec, 3, "coord");
    this.normal.init(this, this.FieldSpec, 3, "normal");
    this.texCoord.init(this, this.FieldSpec, 3, "texCoord");

    this.ccw.init(this, this.FieldSpec, 0, "ccw");
    this.colorPerVertex.init(this, this.FieldSpec, 0, "colorPerVertex");
    this.convex.init(this, this.FieldSpec, 0, "convex");
    this.creaseAngle.init(this, this.FieldSpec, 0, "creaseAngle");
    this.normalPerVertex.init(this, this.FieldSpec, 0, "normalPerVertex");
    this.solid.init(this, this.FieldSpec, 0, "solid");
  }

  public String toStringBody()
  {
    String retval = "IndexedFaceSet {\n";
    if (this.color.node != null) {
      retval = retval + "color " + this.color;
    }
    if (this.coord.node != null) {
      retval = retval + "coord " + this.coord;
    }
    if (this.normal.node != null) {
      retval = retval + "normal " + this.normal;
    }
    if (this.texCoord.node != null) {
      retval = retval + "    texCoord " + this.texCoord;
    }
    if (this.ccw.value != true) {
      retval = retval + "ccw FALSE\n";
    }
    if (this.colorIndex.size != 0) {
      retval = retval + "colorIndex " + this.colorIndex;
    }
    if (this.colorPerVertex.value != true) {
      retval = retval + "colorPerVertex FALSE\n";
    }
    if (this.convex.value != true) {
      retval = retval + "convex FALSE\n";
    }
    if (this.coordIndex.size != 0) {
      retval = retval + "coordIndex " + this.coordIndex;
    }
    if (this.creaseAngle.value != 0.0D) {
      retval = retval + "creaseAngle " + this.creaseAngle.value + "\n";
    }
    if (this.normalIndex.size != 0) {
      retval = retval + "normalIndex " + this.normalIndex;
    }
    if (this.normalPerVertex.value != true) {
      retval = retval + "normalPerVertex FALSE\n";
    }
    if (this.solid.value != true) {
      retval = retval + "solid FALSE\n";
    }
    if (this.texCoordIndex.size != 0) {
      retval = retval + "texCoordIndex " + this.texCoordIndex;
    }
    retval = retval + "}";
    return retval;
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
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.IndexedFaceSet
 * JD-Core Version:    0.6.0
 */