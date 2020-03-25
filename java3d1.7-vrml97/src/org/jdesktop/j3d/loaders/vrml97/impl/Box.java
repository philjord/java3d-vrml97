package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.QuadArray;
import org.jogamp.vecmath.Point3d;

public class Box extends Geometry
{
  SFVec3f size;
  QuadArray impl;
  BoundingBox bounds;

  public Box(Loader loader)
  {
    super(loader);
    this.size = new SFVec3f(2.0F, 2.0F, 2.0F);
    initFields();
  }

  Box(Loader loader, SFVec3f size)
  {
    super(loader);
    this.size = size;
    initFields();
  }

  private void updatePoints()
  {
    float[] coords = new float[72];

    float sx = this.size.value[0] / 2.0F;
    float sy = this.size.value[1] / 2.0F;
    float sz = this.size.value[2] / 2.0F;

    Point3d min = new Point3d(-sx, -sy, -sz);
    Point3d max = new Point3d(sx, sy, sz);
    this.bounds = new BoundingBox(min, max);

    coords[0] = sx;
    coords[1] = (-sy);
    coords[2] = sz;
    coords[3] = sx;
    coords[4] = sy;
    coords[5] = sz;
    coords[6] = (-sx);
    coords[7] = sy;
    coords[8] = sz;
    coords[9] = (-sx);
    coords[10] = (-sy);
    coords[11] = sz;

    coords[12] = (-sx);
    coords[13] = (-sy);
    coords[14] = (-sz);
    coords[15] = (-sx);
    coords[16] = sy;
    coords[17] = (-sz);
    coords[18] = sx;
    coords[19] = sy;
    coords[20] = (-sz);
    coords[21] = sx;
    coords[22] = (-sy);
    coords[23] = (-sz);

    coords[24] = sx;
    coords[25] = (-sy);
    coords[26] = (-sz);
    coords[27] = sx;
    coords[28] = sy;
    coords[29] = (-sz);
    coords[30] = sx;
    coords[31] = sy;
    coords[32] = sz;
    coords[33] = sx;
    coords[34] = (-sy);
    coords[35] = sz;

    coords[36] = (-sx);
    coords[37] = (-sy);
    coords[38] = sz;
    coords[39] = (-sx);
    coords[40] = sy;
    coords[41] = sz;
    coords[42] = (-sx);
    coords[43] = sy;
    coords[44] = (-sz);
    coords[45] = (-sx);
    coords[46] = (-sy);
    coords[47] = (-sz);

    coords[48] = sx;
    coords[49] = sy;
    coords[50] = sz;
    coords[51] = sx;
    coords[52] = sy;
    coords[53] = (-sz);
    coords[54] = (-sx);
    coords[55] = sy;
    coords[56] = (-sz);
    coords[57] = (-sx);
    coords[58] = sy;
    coords[59] = sz;

    coords[60] = (-sx);
    coords[61] = (-sy);
    coords[62] = sz;
    coords[63] = (-sx);
    coords[64] = (-sy);
    coords[65] = (-sz);
    coords[66] = sx;
    coords[67] = (-sy);
    coords[68] = (-sz);
    coords[69] = sx;
    coords[70] = (-sy);
    coords[71] = sz;

    this.impl.setCoordinates(0, coords);
  }

  private void initNormals()
  {
    float[] normals = new float[72];

    normals[0] = 0.0F;
    normals[1] = 0.0F;
    normals[2] = 1.0F;
    normals[3] = 0.0F;
    normals[4] = 0.0F;
    normals[5] = 1.0F;
    normals[6] = 0.0F;
    normals[7] = 0.0F;
    normals[8] = 1.0F;
    normals[9] = 0.0F;
    normals[10] = 0.0F;
    normals[11] = 1.0F;

    normals[12] = 0.0F;
    normals[13] = 0.0F;
    normals[14] = -1.0F;
    normals[15] = 0.0F;
    normals[16] = 0.0F;
    normals[17] = -1.0F;
    normals[18] = 0.0F;
    normals[19] = 0.0F;
    normals[20] = -1.0F;
    normals[21] = 0.0F;
    normals[22] = 0.0F;
    normals[23] = -1.0F;

    normals[24] = 1.0F;
    normals[25] = 0.0F;
    normals[26] = 0.0F;
    normals[27] = 1.0F;
    normals[28] = 0.0F;
    normals[29] = 0.0F;
    normals[30] = 1.0F;
    normals[31] = 0.0F;
    normals[32] = 0.0F;
    normals[33] = 1.0F;
    normals[34] = 0.0F;
    normals[35] = 0.0F;

    normals[36] = -1.0F;
    normals[37] = 0.0F;
    normals[38] = 0.0F;
    normals[39] = -1.0F;
    normals[40] = 0.0F;
    normals[41] = 0.0F;
    normals[42] = -1.0F;
    normals[43] = 0.0F;
    normals[44] = 0.0F;
    normals[45] = -1.0F;
    normals[46] = 0.0F;
    normals[47] = 0.0F;

    normals[48] = 0.0F;
    normals[49] = 1.0F;
    normals[50] = 0.0F;
    normals[51] = 0.0F;
    normals[52] = 1.0F;
    normals[53] = 0.0F;
    normals[54] = 0.0F;
    normals[55] = 1.0F;
    normals[56] = 0.0F;
    normals[57] = 0.0F;
    normals[58] = 1.0F;
    normals[59] = 0.0F;

    normals[60] = 0.0F;
    normals[61] = -1.0F;
    normals[62] = 0.0F;
    normals[63] = 0.0F;
    normals[64] = -1.0F;
    normals[65] = 0.0F;
    normals[66] = 0.0F;
    normals[67] = -1.0F;
    normals[68] = 0.0F;
    normals[69] = 0.0F;
    normals[70] = -1.0F;
    normals[71] = 0.0F;

    this.impl.setNormals(0, normals);
  }

  private void initTextureCoordinates()
  {
    float[] coords = new float[48];

    int i = 0;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;

    coords[(i++)] = 0.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;

    coords[(i++)] = 0.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;

    coords[(i++)] = 0.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;

    coords[(i++)] = 0.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;

    coords[(i++)] = 0.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 1.0F;
    coords[(i++)] = 0.0F;
    coords[(i++)] = 1.0F;

    this.impl.setTextureCoordinates(0, 0, coords);
  }

  void initImpl()
  {
    this.impl = new QuadArray(24, 35);

    updatePoints();
    initNormals();
    initTextureCoordinates();
    this.implReady = true;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("size")) {
      System.out.println("Updating the points size = " + this.size.value[0] + " " + this.size.value[1] + " " + this.size.value[2]);

      updatePoints();
    }
    else {
      System.err.println("Box.notifyMethod: unexpected eventInName: " + eventInName);
    }
  }

  public org.jogamp.java3d.Geometry getImplGeom()
  {
    if (this.impl == null) {
      throw new NullPointerException();
    }
    return this.impl;
  }

  public BoundingBox getBoundingBox()
  {
    return this.bounds;
  }

  public boolean haveTexture()
  {
    return true;
  }

  public int getNumTris()
  {
    return 12;
  }

  public Object clone()
  {
    return new Box(this.loader, (SFVec3f)this.size.clone());
  }

  public String getType()
  {
    return "Box";
  }

  void initFields()
  {
    this.size.init(this, this.FieldSpec, 3, "size");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Box
 * JD-Core Version:    0.6.0
 */