package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;
import java.io.PrintStream;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Matrix3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;
import vrml.InvalidVRMLSyntaxException;

class Extrusion extends Geometry
  implements Ownable
{
  SFBool beginCap;
  SFBool ccw;
  SFBool convex;
  SFFloat creaseAngle;
  MFVec2f crossSection;
  SFBool endCap;
  MFRotation orientation;
  MFVec2f scale;
  SFBool solid;
  MFVec3f spine;
  GeometryArray impl;
  BoundingBox bounds;
  GeometryInfo gi;
  Shape owner;
  Point3f[] spines;
  Vector3f[] scales;
  AxisAngle4f[] orientations;
  Transform3D[] spineTransforms;
  Point3f[] crossSectionPts;
  Matrix3f[] rotations;
  Transform3D[] transforms;
  Point3f[] coords;
  int[] coordIndex;
  int[] stripCounts;
  boolean collinear = false;
  boolean closed = false;

  float[] a2 = new float[2];
  float[] a3 = new float[3];
  float[] a4 = new float[4];

  int numTris = 0;

  boolean hardDebug = false;

  public Extrusion(Loader loader)
  {
    super(loader);
    this.beginCap = new SFBool(true);
    this.endCap = new SFBool(true);
    this.ccw = new SFBool(true);
    this.convex = new SFBool(true);
    this.solid = new SFBool(true);
    if (loader.autoSmooth) {
      this.creaseAngle = new SFFloat(0.9F);
    }
    else {
      this.creaseAngle = new SFFloat(0.0F);
    }
    this.crossSection = new MFVec2f(5, new float[10]);
    this.crossSection.set1Value(0, 1.0F, 1.0F);
    this.crossSection.set1Value(1, 1.0F, -1.0F);
    this.crossSection.set1Value(2, -1.0F, -1.0F);
    this.crossSection.set1Value(3, -1.0F, 1.0F);
    this.crossSection.set1Value(4, 1.0F, 1.0F);
    this.spine = new MFVec3f(2, new float[6]);
    this.spine.set1Value(0, 0.0F, 0.0F, 0.0F);
    this.spine.set1Value(1, 0.0F, 1.0F, 0.0F);
    this.orientation = new MFRotation(1, new float[4]);
    this.orientation.set1Value(0, 0.0F, 0.0F, 1.0F, 0.0F);
    this.scale = new MFVec2f(new float[2]);
    this.scale.set1Value(0, 1.0F, 1.0F);
    initFields();
  }

  Extrusion(Loader loader, SFBool beginCap, SFBool ccw, SFBool convex, SFFloat creaseAngle, MFVec2f crossSection, SFBool endCap, MFRotation orientation, MFVec2f scale, SFBool solid, MFVec3f spine)
  {
    super(loader);
    this.beginCap = beginCap;
    this.ccw = ccw;
    this.convex = convex;
    this.creaseAngle = creaseAngle;
    this.crossSection = crossSection;
    this.endCap = endCap;
    this.orientation = orientation;
    this.scale = scale;
    this.solid = solid;
    this.spine = spine;
    initFields();
  }

  public boolean haveTexture()
  {
    return false;
  }

  public int getNumTris()
  {
    return this.numTris;
  }

  org.jogamp.java3d.Geometry getImplGeom()
  {
    return this.impl;
  }

  BoundingBox getBoundingBox()
  {
    Point3d epsilon = new Point3d(1.0E-006D, 1.0E-006D, 1.0E-006D);
    Point3d lower = new Point3d(this.coords[0]);
    Point3d upper = new Point3d(this.coords[0]);
    lower.sub(epsilon);
    upper.add(epsilon);

    this.bounds = new BoundingBox(lower, upper);

    for (int c = 1; c < this.coords.length; c++) {
      this.bounds.combine(new Point3d(this.coords[c]));
    }
    if (this.loader.debug) {
      System.out.println(this.bounds);
    }
    return this.bounds;
  }

  void initImpl()
  {
    this.gi = new GeometryInfo(5);

    initSetup();

    calculateSCP();

    createExtrusion();

    createIndices();
    if (this.hardDebug) {
      System.out.println("coords");
      for (int i = 0; i < this.coords.length; i++) {
        System.out.println(this.coords[i]);
      }
      System.out.println("coordIndex");
      for (int i = 0; i < this.coordIndex.length; i++) {
        System.out.println(this.coordIndex[i]);
      }
      System.out.println("stripCounts");
      for (int i = 0; i < this.stripCounts.length; i++) {
        System.out.println(this.stripCounts[i]);
      }
    }
    this.gi.setCoordinates(this.coords);
    this.gi.setCoordinateIndices(this.coordIndex);
    this.gi.setStripCounts(this.stripCounts);

    float ca = this.creaseAngle.getValue();
    if (ca < 0.0F) {
      ca = 0.0F;
    }
    if (ca > 3.141593F) {
      ca -= 3.141593F;
    }

    NormalGenerator ng = new NormalGenerator(ca);
    ng.generateNormals(this.gi);

    this.impl = this.gi.getGeometryArray();

    this.implReady = true;
  }

  void initSetup()
  {
    this.crossSectionPts = new Point3f[this.crossSection.getSize()];

    if (this.hardDebug) {
      System.out.println(this.crossSection.getSize());
    }
    for (int i = 0; i < this.crossSectionPts.length; i++) {
      this.crossSection.get1Value(i, this.a2);
      this.crossSectionPts[i] = new Point3f(this.a2[0], 0.0F, this.a2[1]);
    }

    this.scales = new Vector3f[this.spine.getSize()];
    for (int i = 0; i < this.scales.length; i++) {
      if (i < this.scale.getSize()) {
        this.scale.get1Value(i, this.a2);
      }
      this.scales[i] = new Vector3f(this.a2[0], 1.0F, this.a2[1]);
    }

    this.spines = new Point3f[this.spine.getSize()];
    for (int i = 0; i < this.spines.length; i++) {
      this.spine.get1Value(i, this.a3);
      this.spines[i] = new Point3f(this.a3);
    }

    this.orientations = new AxisAngle4f[this.spine.getSize()];
    for (int i = 0; i < this.orientations.length; i++) {
      if (i < this.orientation.getSize()) {
        this.orientation.get1Value(i, this.a4);
      }
      this.orientations[i] = new AxisAngle4f(this.a4);
    }

    this.rotations = new Matrix3f[this.spines.length];

    if (this.spines[0].equals(this.spines[(this.spines.length - 1)])) {
      this.closed = true;
    }

    Vector3d v2 = new Vector3d();
    Vector3d v1 = new Vector3d();
    Vector3d v0 = new Vector3d();
    double d = 0.0D;
    for (int i = 1; i < this.spines.length - 1; i++) {
      v2.set(this.spines[(i + 1)]);
      v1.set(this.spines[i]);
      v0.set(this.spines[(i - 1)]);
      v2.sub(v1);
      v1.sub(v0);
      v0.cross(v2, v1);
      d += v0.dot(v0);
    }
    this.collinear = (d == 0.0D);
    if ((this.hardDebug) && (this.collinear))
      System.out.println("spine is straight");
  }

  void calculateSCP()
  {
    Vector3f zero = new Vector3f(0.0F, 0.0F, 0.0F);
    int last = this.spines.length - 1;

    Vector3f[] x = new Vector3f[this.spines.length];
    Vector3f[] y = new Vector3f[this.spines.length];
    Vector3f[] z = new Vector3f[this.spines.length];

    if (this.collinear) {
      if (this.closed) {
        throw new InvalidVRMLSyntaxException("invalid Extrusion data; looks like a solid of revolution");
      }

      for (int i = 0; i < this.spines.length; i++)
      {
        x[i] = new Vector3f(1.0F, 0.0F, 0.0F);
        y[i] = new Vector3f(0.0F, 1.0F, 0.0F);
        z[i] = new Vector3f(0.0F, 0.0F, 1.0F);
      }

    }
    else
    {
      for (int i = 1; i < last; i++) {
        y[i] = new Vector3f();
        y[i].sub(this.spines[(i + 1)], this.spines[(i - 1)]);
        try {
          norm(y[i]);
        }
        catch (ArithmeticException ae) {
          if (this.hardDebug) {
            System.out.println(ae + " " + y[i]);
          }
          try
          {
            y[i].sub(this.spines[(i + 1)], this.spines[i]);
            norm(y[i]);
          }
          catch (ArithmeticException ae1) {
            if (this.hardDebug) {
              System.out.println(ae1 + " " + y[i]);
            }
            try
            {
              y[i].sub(this.spines[i], this.spines[(i - 1)]);
              norm(y[i]);
            }
            catch (ArithmeticException ae2) {
              if (this.hardDebug) {
                System.out.println(ae2 + " " + y[i]);
              }

              int w = i + 2;
              while ((w < last + 1) && (this.spines[(i - 1)].equals(this.spines[w]))) {
                w++;
              }
              if (w < last + 1) {
                y[i].sub(this.spines[w], this.spines[(i - 1)]);
                if (this.hardDebug) {
                  System.out.println("did something " + y[i]);
                }
                norm(y[i]);
              }
              else {
                if (this.hardDebug) {
                  System.out.println("worst worst y " + y[i]);
                }
                y[i] = new Vector3f(0.0F, 1.0F, 0.0F);
              }
            }
          }
        }
      }

      if (this.closed)
      {
        y[0] = new Vector3f();
        y[0].sub(this.spines[1], this.spines[(last - 1)]);
        try {
          norm(y[0]);
        }
        catch (ArithmeticException ae)
        {
          int w = last - 2;
          while ((w > 1) && (this.spines[1].equals(this.spines[w]))) {
            w--;
          }
          if (w > 1) {
            y[0].sub(this.spines[1], this.spines[w]);
            norm(y[0]);
          }
          else
          {
            y[0].set(0.0F, 0.0F, 1.0F);
          }
        }
        y[last] = new Vector3f(y[0]);
      }
      else {
        y[0] = new Vector3f();
        y[last] = new Vector3f();
        y[0].sub(this.spines[1], this.spines[0]);
        try {
          norm(y[0]);
        }
        catch (ArithmeticException ae) {
          int w = 2;
          while ((w < last) && (this.spines[0].equals(this.spines[w]))) {
            w++;
          }
          if (w < last) {
            y[0].sub(this.spines[w], this.spines[0]);
            norm(y[0]);
          }
          else {
            y[0].set(0.0F, 0.0F, 1.0F);
          }
        }
        y[last] = new Vector3f();
        y[last].sub(this.spines[last], this.spines[(last - 1)]);
        try {
          norm(y[last]);
        }
        catch (ArithmeticException ae) {
          int w = last - 2;
          while ((w > -1) && (this.spines[last].equals(this.spines[w]))) {
            w--;
          }
          if (w > -1) {
            y[last].sub(this.spines[last], this.spines[w]);
            norm(y[last]);
          }
          else {
            y[last].set(0.0F, 0.0F, 1.0F);
          }

        }

      }

      boolean recheck = false;
      for (int i = 1; i < last; i++) {
        Vector3f u = new Vector3f();
        Vector3f v = new Vector3f();
        z[i] = new Vector3f();
        u.sub(this.spines[(i - 1)], this.spines[i]);
        v.sub(this.spines[(i + 1)], this.spines[i]);

        z[i].cross(u, v);
        try {
          norm(z[i]);
        }
        catch (ArithmeticException ae) {
          recheck = true;
        }
      }
      if (this.closed)
      {
    	  Vector3f tmp1111_1108 = new Vector3f(); z[last] = tmp1111_1108; z[0] = tmp1111_1108;
        Vector3f u = new Vector3f();
        Vector3f v = new Vector3f();
        u.sub(this.spines[(last - 1)], this.spines[0]);
        v.sub(this.spines[1], this.spines[0]);
        try {
          z[0].cross(u, v);
        }
        catch (ArithmeticException ae) {
          recheck = true;
        }
      }
      else {
        z[0] = new Vector3f(z[1]);
        z[last] = new Vector3f(z[(last - 1)]);
      }
      if (recheck)
      {
        if (this.hardDebug) {
          System.out.println("rechecking, found adjacent collinear spines");
        }
        if (z[0].dot(z[0]) == 0.0F) {
          for (int i = 1; i < this.spines.length; i++) {
            if (z[i].dot(z[i]) > 0.0F) {
              z[0] = new Vector3f(z[i]);
            }
          }

          if (z[0].dot(z[0]) == 0.0F) {
            z[0] = new Vector3f(0.0F, 0.0F, 1.0F);
          }

        }

        for (int i = 1; i < last + 1; i++) {
          if (z[i].dot(z[i]) == 0.0F) {
            z[i] = new Vector3f(z[(i - 1)]);
          }
        }

      }

      for (int i = 0; i < this.spines.length; i++) {
        if ((i > 0) && 
          (z[i].dot(z[(i - 1)]) < 0.0F)) {
          z[i].negate();
        }

        x[i] = new Vector3f();
        x[i].cross(z[i], y[i]);
        try {
          norm(x[i]);
        }
        catch (ArithmeticException ae)
        {
          ae.printStackTrace();
        }
        if (this.hardDebug) {
          System.out.println("x[" + i + "] " + x[i]);
        }

      }

    }

    Matrix3f m = new Matrix3f();
    this.transforms = new Transform3D[this.spines.length];

    for (int i = 0; i < this.spines.length; i++) {
      this.rotations[i] = new Matrix3f();
      if (this.hardDebug) {
        System.out.println("orthos " + i + " " + x[i] + " " + y[i] + " " + z[i] + " " + this.orientations[i]);
      }

      this.rotations[i].setRow(0, x[i]);
      this.rotations[i].setRow(1, y[i]);
      this.rotations[i].setRow(2, z[i]);
      m.set(this.orientations[i]);
      this.rotations[i].mul(m);
      this.transforms[i] = new Transform3D();
      this.transforms[i].setScale(new Vector3d(this.scales[i]));
      this.transforms[i].setTranslation(new Vector3d(this.spines[i]));
      this.transforms[i].setRotation(this.rotations[i]);
    }
  }

  void createExtrusion()
  {
    this.coords = new Point3f[this.spines.length * this.crossSectionPts.length];

    for (int i = 0; i < this.spines.length; i++)
      for (int j = 0; j < this.crossSectionPts.length; j++) {
        int ind = i * this.crossSectionPts.length + j;
        this.coords[ind] = new Point3f(this.crossSectionPts[j]);
        this.transforms[i].transform(this.coords[ind]);
      }
  }

  void createIndices()
  {
    int m = 0;
    int k = this.crossSectionPts.length;
    int l = this.coords.length;
    int s = 0;
    int n = 0;
    if (this.endCap.value) {
      m += k - 1;
      s++;
    }
    if (this.beginCap.value) {
      m += k - 1;
      s++;
    }
    m += (this.spines.length - 1) * (4 * (k - 1));
    this.coordIndex = new int[m];
    if (this.hardDebug) {
      System.out.println("coordIndexSize" + m);
    }
    this.stripCounts = new int[s + (this.spines.length - 1) * (k - 1)];
    s = 0;
    if (this.hardDebug) {
      System.out.println("stripCounts.length" + this.stripCounts.length);
    }
    if (this.hardDebug) {
      System.out.println("spines.length" + this.spines.length);
    }

    for (int i = 0; i < this.spines.length - 1; i++) {
      if (this.hardDebug) {
        System.out.println(" i " + i);
      }
      for (int j = 0; j < k - 1; j++) {
        if (this.hardDebug) {
          System.out.println(" j " + j);
        }

        if (this.ccw.value) {
          if (this.hardDebug) {
            System.out.println("i " + i + " j " + j + " k " + k);
          }
          this.coordIndex[(n++)] = (i * k + j);
          if (this.hardDebug) {
            System.out.println(n - 1 + " " + (i * k + j));
          }
          this.coordIndex[(n++)] = (i * k + j + 1);
          if (this.hardDebug) {
            System.out.println(n - 1 + " " + (i * k + (j + 1)));
          }
          this.coordIndex[(n++)] = ((i + 1) * k + j + 1);
          if (this.hardDebug) {
            System.out.println(n - 1 + " " + ((i + 1) * k + (j + 1)));
          }
          this.coordIndex[(n++)] = ((i + 1) * k + j);
          if (this.hardDebug)
            System.out.println(n - 1 + " " + ((i + 1) * k + j));
        }
        else
        {
          this.coordIndex[(n++)] = (i * k + j);
          this.coordIndex[(n++)] = ((i + 1) * k + j);
          this.coordIndex[(n++)] = ((i + 1) * k + j + 1);
          this.coordIndex[(n++)] = (i * k + j + 1);
        }
        this.stripCounts[(s++)] = 4;
        this.numTris += 2;
      }

    }

    if ((this.beginCap.value) && (this.endCap.value)) {
      int indB = m - 2 * (k - 1);
      int indE = m - (k - 1);
      if (!this.ccw.value) {
        for (int i = 0; i < k - 1; i++) {
          this.coordIndex[(indB++)] = i;
        }

        for (int i = l - 1; i > l - k; i--) {
          this.coordIndex[(indE++)] = i;
        }
      }
      else
      {
        for (int i = k - 1; i > 0; i--) {
          this.coordIndex[(indB++)] = i;
        }
        for (int i = 0; i < k - 1; i++) {
          this.coordIndex[(indE++)] = (l - (k - 1) + i);
        }
      }
      this.stripCounts[(s++)] = (k - 1);
      this.stripCounts[(s++)] = (k - 1);
      this.numTris += k - 1;
    }
    else if (this.beginCap.value) {
      int ind = m - (k - 1);
      if (!this.ccw.value) {
        for (int i = 0; i < k - 1; i++) {
          this.coordIndex[(ind++)] = i;
        }
      }
      else {
        for (int i = k - 1; i > 0; i--) {
          this.coordIndex[(ind++)] = i;
        }
      }
      this.stripCounts[(s++)] = (k - 1);
      this.numTris += k - 1;
    }
    else if (this.endCap.value) {
      int ind = m - (k - 1);
      if (this.ccw.value) {
        for (int i = l - (k - 1); i < l; i++) {
          this.coordIndex[(ind++)] = i;
        }
      }
      else {
        for (int i = l - 1; i > l - k; i--) {
          this.coordIndex[(ind++)] = i;
        }
      }
      this.stripCounts[(s++)] = (k - 1);
      this.numTris += k - 1;
    }
  }

  void norm(Vector3f n)
  {
    float norml = (float)Math.sqrt(n.x * n.x + n.y * n.y + n.z * n.z);
    if (norml == 0.0F) {
      throw new ArithmeticException();
    }

    n.x /= norml;
    n.y /= norml;
    n.z /= norml;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (!eventInName.startsWith("route_")) {
      initImpl();
      ((Shape3D)(Shape3D)this.owner.implNode).setGeometry(this.impl);
    }
    else {
      ((Shape3D)(Shape3D)this.owner.implNode).setCapability(13);
    }
  }

  void initFields()
  {
    this.beginCap.init(this, this.FieldSpec, 0, "beginCap");
    this.ccw.init(this, this.FieldSpec, 0, "ccw");
    this.convex.init(this, this.FieldSpec, 0, "convex");
    this.creaseAngle.init(this, this.FieldSpec, 0, "creaseAngle");
    this.crossSection.init(this, this.FieldSpec, 1, "crossSection");
    this.endCap.init(this, this.FieldSpec, 0, "endCap");
    this.orientation.init(this, this.FieldSpec, 1, "orientation");
    this.scale.init(this, this.FieldSpec, 1, "scale");
    this.solid.init(this, this.FieldSpec, 0, "solid");
    this.spine.init(this, this.FieldSpec, 1, "spine");
  }

  public Object clone()
  {
    return new Extrusion(this.loader, (SFBool)this.beginCap.clone(), (SFBool)this.ccw.clone(), (SFBool)this.convex.clone(), (SFFloat)this.creaseAngle.clone(), (MFVec2f)this.crossSection.clone(), (SFBool)this.endCap.clone(), (MFRotation)this.orientation.clone(), (MFVec2f)this.scale.clone(), (SFBool)this.solid.clone(), (MFVec3f)this.spine.clone());
  }

  public String getType()
  {
    return "Extrusion";
  }

  public boolean getSolid()
  {
    return this.solid.value;
  }

  public void setOwner(Shape owner)
  {
    this.owner = owner;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Extrusion
 * JD-Core Version:    0.6.0
 */