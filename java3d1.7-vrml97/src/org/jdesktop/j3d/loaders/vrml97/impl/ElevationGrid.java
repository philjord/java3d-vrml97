/*
 * $RCSfile: ElevationGrid.java,v $
 *
 *      @(#)ElevationGrid.java 1.17 99/03/05 17:13:20
 *
 * Copyright (c) 1996-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
  *
 * $Revision: 1.2 $
 * $Date: 2005/02/03 23:06:55 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.utils.geometry.*;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;
import vrml.InvalidVRMLSyntaxException;

/**  Description of the Class */
public class ElevationGrid extends Geometry implements Ownable {

    //eventIn redundant
    //MFFloat height;

    //exposedField
    SFNode color;
    SFNode normal;
    SFNode texCoord;
    MFFloat height;

    //field
    SFBool ccw;
    SFBool colorPerVertex;
    SFBool normalPerVertex;
    SFFloat creaseAngle;
    SFBool solid;
    SFInt32 xDimension;
    SFFloat xSpacing;
    SFInt32 zDimension;
    SFFloat zSpacing;

    // j3d info
    GeometryArray impl;
    GeometryInfo gi;
    Point3f[] coordArray;
    Color3f[] colorArray;
    Point2f[] texCoordArray;
    Vector3f[] normalArray;

    // vrml
    Shape owner;

    // other
    boolean nullable = true;
    boolean haveTexture = false;


    /**
     *Constructor for the ElevationGrid object
     *
     *@param  loader Description of the Parameter
     */
    public ElevationGrid(Loader loader) {
        super(loader);
        height = new MFFloat();
        color = new SFNode(null);
        normal = new SFNode(null);
        texCoord = new SFNode(null);
        ccw = new SFBool(true);
        colorPerVertex = new SFBool(true);
        creaseAngle = new SFFloat(0.0f);
        normalPerVertex = new SFBool(true);
        solid = new SFBool(true);
        xDimension = new SFInt32(0);
        xSpacing = new SFFloat(1.0f);
        zDimension = new SFInt32(0);
        zSpacing = new SFFloat(1.0f);

        initFields();
    }

    /**
     *Constructor for the ElevationGrid object
     *
     *@param  loader Description of the Parameter
     *@param  height Description of the Parameter
     *@param  color Description of the Parameter
     *@param  normal Description of the Parameter
     *@param  texCoord Description of the Parameter
     *@param  ccw Description of the Parameter
     *@param  colorPerVertex Description of the Parameter
     *@param  creaseAngle Description of the Parameter
     *@param  normalPerVertex Description of the Parameter
     *@param  solid Description of the Parameter
     *@param  xDimension Description of the Parameter
     *@param  xSpacing Description of the Parameter
     *@param  zDimension Description of the Parameter
     *@param  zSpacing Description of the Parameter
     */
    ElevationGrid(Loader loader,
            MFFloat height, SFNode color, SFNode normal, SFNode texCoord,
            SFBool ccw, SFBool colorPerVertex, SFFloat creaseAngle,
            SFBool normalPerVertex, SFBool solid, SFInt32 xDimension,
            SFFloat xSpacing, SFInt32 zDimension, SFFloat zSpacing) {
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

    /**  Description of the Method */
    void initImpl() {
        // do the vrml data to j3d conversion ( initSetup() )
        // or leave them null.

        initSetup();
        gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);

        if (coordArray != null) {
            gi.setCoordinates(coordArray);

            if (loader.debug) {
                for (int i = 0; i < coordArray.length; i++) {
                    System.out.println(coordArray[i]);
                }
            }

        }
        else {
            throw new vrml.InvalidVRMLSyntaxException(
                    "No coordinates supplied for ElevationGrid");
        }

        if (colorArray != null) {
            gi.setColors(colorArray);
            // enable event mapping
            ((Color) (color.node)).owner = this;
        }

        if (texCoordArray != null) {
            gi.setTextureCoordinates(texCoordArray);
            haveTexture = true;
            // enable event mapping
            ((TextureCoordinate) (texCoord.node)).owner = this;
        }

        if (normalArray != null) {
            gi.setNormals(normalArray);
            ((Normal) (normal.node)).owner = this;
        }
        else {
            // this one has to be dealt with here since
            // coordinates are now known to gi
            float ca = creaseAngle.getValue();
            if (ca < 0.0f) {
                ca = 0.0f;
            }
            if (ca > (float) Math.PI) {
                ca -= (float) Math.PI;
            }
            NormalGenerator ng = new NormalGenerator(ca);
            ng.generateNormals(gi);
        }

        impl = gi.getGeometryArray();

        // if no routes were specified at parsetime
        // allow these to get GC'd
        if (nullable) {
            height = null;
            color = null;
            normal = null;
            texCoord = null;
        }

        // cleanUp null references
        loader.cleanUp();
        implReady = true;

    }

    // convert vrml data to j3d data

    /**  Description of the Method */
    void initSetup() {

        int xD = xDimension.getValue();
        int zD = zDimension.getValue();
        int quadNum = 0;
        int quadCount = (xD - 1) * (zD - 1);
        float[] heights = height.mfloat;

        if (loader.debug) {
            System.out.println("X dimension" + xD);
            System.out.println("Z dimension" + zD);
            System.out.println("quad count" + quadCount);
            System.out.println("height" + height);
            for (int h = 0; h < heights.length; h++) {
                System.out.print(heights[h] + ",");
            }
        }
        if ((xD < 1) || (zD < 1)) {
            throw new vrml.InvalidVRMLSyntaxException("ElevationGrid dimensionless");
        }
        if (height.mfloat.length != xD * zD) {
            throw new vrml.InvalidVRMLSyntaxException("ElevationGrid dimension mismatch");
        }

        // TBD, check the size of the heights. If it is really large
        // may want to indexify the data.

        // coordArray will be passed to gi in terms of independent quads
        coordArray = new Point3f[quadCount * 4];
        // only used if color field exists else nullout
        colorArray = new Color3f[quadCount * 4];
        // only used if normal field exists else nullout
        normalArray = new Vector3f[quadCount * 4];
        // only used if texCoord field exists else nullout
        texCoordArray = new Point2f[quadCount * 4];

        // reader beware: i and j are NOT the same i and j in the vrml spec
        // rather k is j and j is i
        for (int k = 0; k < zD - 1; k++) {
            for (int j = 0; j < xD - 1; j++) {
                // this steps through the dimensions, one
                // quad at a time
                int i = j + (k * xD);
                Point3f p0 = new Point3f();
                Point3f p1 = new Point3f();
                Point3f p2 = new Point3f();
                Point3f p3 = new Point3f();
                float zSp = zSpacing.value;
                float xSp = xSpacing.value;

                p0.x = (float) j * xSp;
                p0.y = heights[i];
                p0.z = ((float) k) * zSp;

                p1.x = (float) j * xSp;
                p1.y = heights[i + xD];
                p1.z = ((float) (k + 1)) * zSp;

                p2.x = ((float) (j + 1)) * xSp;
                p2.y = heights[i + xD + 1];
                p2.z = ((float) (k + 1)) * zSp;

                p3.x = ((float) (j + 1)) * xSp;
                p3.y = heights[i + 1];
                p3.z = ((float) k) * zSp;

                // fill in the coordArray

                if (ccw.value) {
                    coordArray[(quadNum * 4) + 0] = p0;
                    coordArray[(quadNum * 4) + 1] = p1;
                    coordArray[(quadNum * 4) + 2] = p2;
                    coordArray[(quadNum * 4) + 3] = p3;
                }
                else {
                    coordArray[(quadNum * 4) + 0] = p0;
                    coordArray[(quadNum * 4) + 1] = p3;
                    coordArray[(quadNum * 4) + 2] = p2;
                    coordArray[(quadNum * 4) + 3] = p1;
                }

                if (loader.debug) {
                    System.out.println("quadNum" + quadNum);
                    System.out.println("v0 " + coordArray[quadNum * 4 + 0]);
                    System.out.println("v1 " + coordArray[quadNum * 4 + 1]);
                    System.out.println("v2 " + coordArray[quadNum * 4 + 2]);
                    System.out.println("v3 " + coordArray[quadNum * 4 + 3]);

                }
                // do the
                // colorArray, texCoordArray, but not normalArray

                if (color.node != null) {
                    Color3f c0 = new Color3f();
                    Color3f c1 = new Color3f();
                    Color3f c2 = new Color3f();
                    Color3f c3 = new Color3f();
                    MFColor mfclr = (MFColor) (((Color) (color.node)).color);
                    float[] clrs = (float[]) (mfclr.vals);
                    if (colorPerVertex.value) {
                        // colors supplied are for each vtx
                        int ind0 = i * 3;
                        int ind1 = (i + xD) * 3;
                        int ind2 = (i + xD + 1) * 3;
                        int ind3 = (i + 1) * 3;

                        // shouldn't Color3f be .r, .g, .b?
                        c0.x = clrs[ind0];
                        c0.y = clrs[ind0 + 1];
                        c0.z = clrs[ind0 + 2];

                        c1.x = clrs[ind1];
                        c1.y = clrs[ind1 + 1];
                        c1.z = clrs[ind1 + 2];

                        c2.x = clrs[ind2];
                        c2.y = clrs[ind2 + 1];
                        c2.z = clrs[ind2 + 2];

                        c3.x = clrs[ind3];
                        c3.y = clrs[ind3 + 1];
                        c3.z = clrs[ind3 + 2];

                        if (ccw.value) {
                            colorArray[(quadNum * 4) + 0] = c0;
                            colorArray[(quadNum * 4) + 1] = c1;
                            colorArray[(quadNum * 4) + 2] = c2;
                            colorArray[(quadNum * 4) + 3] = c3;
                        }
                        else {
                            colorArray[(quadNum * 4) + 0] = c0;
                            colorArray[(quadNum * 4) + 1] = c3;
                            colorArray[(quadNum * 4) + 2] = c2;
                            colorArray[(quadNum * 4) + 3] = c1;
                        }

                    }
                    else {
                        // there are only xD-1 * zD-1 colors
                        c0.x = clrs[(quadNum * 3) + 0];
                        c0.y = clrs[(quadNum * 3) + 1];
                        c0.z = clrs[(quadNum * 3) + 2];

                        colorArray[(quadNum * 4) + 0] = c0;
                        colorArray[(quadNum * 4) + 1] = c0;
                        colorArray[(quadNum * 4) + 2] = c0;
                        colorArray[(quadNum * 4) + 3] = c0;
                    }
                }
                else {
                    colorArray = null;
                }

                if (texCoord.node != null) {
                    Point2f v0 = new Point2f();
                    Point2f v1 = new Point2f();
                    Point2f v2 = new Point2f();
                    Point2f v3 = new Point2f();
                    float[] tx =
                            (float[])
                            ((MFVec2f)
                            (((TextureCoordinate) (texCoord.node)).point)).vals;
                    // texCoords are supplied per vertex
                    int ind0 = i * 2;
                    int ind1 = (i + xD) * 2;
                    int ind2 = (i + xD + 1) * 2;
                    int ind3 = (i + 1) * 2;
                    v0.x = tx[ind0];
                    v0.y = tx[ind0 + 1];

                    v1.x = tx[ind1];
                    v1.y = tx[ind1 + 1];

                    v2.x = tx[ind2];
                    v2.y = tx[ind2 + 1];

                    v3.x = tx[ind3];
                    v3.y = tx[ind3 + 1];

                    if (ccw.value) {
                        texCoordArray[(quadNum * 4) + 0] = v0;
                        texCoordArray[(quadNum * 4) + 1] = v1;
                        texCoordArray[(quadNum * 4) + 2] = v2;
                        texCoordArray[(quadNum * 4) + 3] = v3;
                    }
                    else {
                        texCoordArray[(quadNum * 4) + 0] = v0;
                        texCoordArray[(quadNum * 4) + 1] = v3;
                        texCoordArray[(quadNum * 4) + 2] = v2;
                        texCoordArray[(quadNum * 4) + 3] = v1;
                    }

                }
                else {
                    texCoordArray = null;
                }

                if (normal.node != null) {

                    Vector3f v0 = new Vector3f();
                    Vector3f v1 = new Vector3f();
                    Vector3f v2 = new Vector3f();
                    Vector3f v3 = new Vector3f();

                    float[] no =
                            (float[])
                            ((MFVec3f)
                            (((Normal) (normal.node)).vector)).value;

                    // handle just like color
                    if (normalPerVertex.value) {

                        int ind0 = i * 3;
                        int ind1 = (i + xD) * 3;
                        int ind2 = (i + xD + 1) * 3;
                        int ind3 = (i + 1) * 3;

                        v0.x = no[ind0];
                        v0.y = no[ind0 + 1];
                        v0.z = no[ind0 + 2];

                        v1.x = no[ind1];
                        v1.y = no[ind1 + 1];
                        v1.z = no[ind1 + 2];

                        v2.x = no[ind2];
                        v2.y = no[ind2 + 1];
                        v2.z = no[ind2 + 2];

                        v3.x = no[ind3];
                        v3.y = no[ind3 + 1];
                        v3.z = no[ind3 + 2];

                        if (ccw.value) {
                            normalArray[(quadNum * 4) + 0] = v0;
                            normalArray[(quadNum * 4) + 1] = v1;
                            normalArray[(quadNum * 4) + 2] = v2;
                            normalArray[(quadNum * 4) + 3] = v3;
                        }
                        else {
                            normalArray[(quadNum * 4) + 0] = v0;
                            normalArray[(quadNum * 4) + 1] = v3;
                            normalArray[(quadNum * 4) + 2] = v2;
                            normalArray[(quadNum * 4) + 3] = v1;
                        }

                    }
                    else {
                        // there are only xD-1 * zD-1 nomals, ie one per quad
                        v0.x = no[(quadNum * 3) + 0];
                        v0.y = no[(quadNum * 3) + 1];
                        v0.z = no[(quadNum * 3) + 2];

                        normalArray[(quadNum * 4) + 0] = v0;
                        normalArray[(quadNum * 4) + 1] = v0;
                        normalArray[(quadNum * 4) + 2] = v0;
                        normalArray[(quadNum * 4) + 3] = v0;
                    }
                }
                else {
                    normalArray = null;
                }

                // next quadNum

                quadNum++;

            }
        }
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {

        if ((eventInName.equals("route_color")) ||
                (eventInName.equals("route_normal")) ||
                (eventInName.equals("route_texCoord")) ||
                (eventInName.equals("route_height"))) {
            impl.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
            impl.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
            impl.setCapability(GeometryArray.ALLOW_NORMAL_WRITE);
            impl.setCapability(GeometryArray.ALLOW_NORMAL_READ);
            impl.setCapability(GeometryArray.ALLOW_TEXCOORD_WRITE);
            impl.setCapability(GeometryArray.ALLOW_TEXCOORD_READ);
            impl.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
            impl.setCapability(GeometryArray.ALLOW_COLOR_READ);

            ((Shape3D) (owner.implNode)).setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
            nullable = false;
        }
        else if (eventInName.equals("height") ||
                eventInName.equals("color") ||
                eventInName.equals("normal") ||
                eventInName.equals("texCoord")) {
            initImpl();
            ((Shape3D) (owner.implNode)).setGeometry(impl);
        }

    }

    /**  Description of the Method */
    void initFields() {
        height.init(this, FieldSpec, Field.EXPOSED_FIELD, "height");
        color.init(this, FieldSpec, Field.EXPOSED_FIELD, "color");
        normal.init(this, FieldSpec, Field.EXPOSED_FIELD, "normal");
        texCoord.init(this, FieldSpec, Field.EXPOSED_FIELD, "texCoord");
        ccw.init(this, FieldSpec, Field.FIELD, "ccw");
        colorPerVertex.init(this, FieldSpec, Field.FIELD, "colorPerVertex");
        creaseAngle.init(this, FieldSpec, Field.FIELD, "creaseAngle");
        normalPerVertex.init(this, FieldSpec, Field.FIELD, "normalPerVertex");
        solid.init(this, FieldSpec, Field.FIELD, "solid");
        xDimension.init(this, FieldSpec, Field.FIELD, "xDimension");
        xSpacing.init(this, FieldSpec, Field.FIELD, "xSpacing");
        zDimension.init(this, FieldSpec, Field.FIELD, "zDimension");
        zSpacing.init(this, FieldSpec, Field.FIELD, "zSpacing");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new ElevationGrid(loader,
                (MFFloat) (height.clone()),
                (SFNode) (color.clone()),
                (SFNode) (normal.clone()),
                (SFNode) (texCoord.clone()),
                (SFBool) (ccw.clone()),
                (SFBool) (colorPerVertex.clone()),
                (SFFloat) (creaseAngle.clone()),
                (SFBool) (normalPerVertex.clone()),
                (SFBool) (solid.clone()),
                (SFInt32) (xDimension.clone()),
                (SFFloat) (xSpacing.clone()),
                (SFInt32) (zDimension.clone()),
                (SFFloat) (zSpacing.clone()));
    }

    /**
     *  Gets the type attribute of the ElevationGrid object
     *
     *@return  The type value
     */
    public String getType() {
        return "ElevationGrid";
    }


    // must extend Geometry and implement abstracts

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public boolean haveTexture() {
        return haveTexture;
    }

    /**
     *  Gets the implGeom attribute of the ElevationGrid object
     *
     *@return  The implGeom value
     */
    public org.jogamp.java3d.Geometry getImplGeom() {
        return impl;
    }

    /**
     *  Gets the boundingBox attribute of the ElevationGrid object
     *
     *@return  The boundingBox value
     */
    org.jogamp.java3d.BoundingBox getBoundingBox() {
        org.jogamp.java3d.BoundingBox b;

        // create a tiny bounding box about a coordinate
        // to seed the placement
        Point3d epsilon = new Point3d(.000001, .000001, .000001);
        Point3d lower = new Point3d(coordArray[0]);
        Point3d upper = new Point3d(coordArray[0]);
        lower.sub(epsilon);
        upper.add(epsilon);

        b = new org.jogamp.java3d.BoundingBox(lower, upper);

        for (int c = 1; c < coordArray.length; c++) {
            b.combine(new Point3d(coordArray[c]));
        }
        if (loader.debug) {
            System.out.println(b);
        }
        return b;
    }

    // fulfill the Ownable interface
    /**
     *  Gets the solid attribute of the ElevationGrid object
     *
     *@return  The solid value
     */
    public boolean getSolid() {
        return solid.value;
    }

    /**
     *  Sets the owner attribute of the ElevationGrid object
     *
     *@param  s The new owner value
     */
    public void setOwner(Shape s) {
        this.owner = s;
    }

}

