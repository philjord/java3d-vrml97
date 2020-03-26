/*
 * $RCSfile: Box.java,v $
 *
 *      @(#)Box.java 1.25 98/11/05 20:34:04
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
 * $Revision: 1.3 $
 * $Date: 2005/02/03 23:06:52 $
 * $State: Exp $
 */
/*
 *
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.BoundingBox;

import org.jogamp.vecmath.Point3d;

/**  Description of the Class */
public class Box extends Geometry {

    // field
    SFVec3f size;

    org.jogamp.java3d.QuadArray impl;
    BoundingBox bounds;

    /**
     *Constructor for the Box object
     *
     *@param  loader Description of the Parameter
     */
    public Box(Loader loader) {
        super(loader);
        size = new SFVec3f(2.0f, 2.0f, 2.0f);
        initFields();
    }

    /**
     *Constructor for the Box object
     *
     *@param  loader Description of the Parameter
     *@param  size Description of the Parameter
     */
    Box(Loader loader, SFVec3f size) {
        super(loader);
        this.size = size;
        initFields();
    }

    /**  Description of the Method */
    private void updatePoints() {
        float[] coords = new float[72];

        float sx = size.value[0] / 2.0f;
        float sy = size.value[1] / 2.0f;
        float sz = size.value[2] / 2.0f;

        Point3d min = new Point3d(-sx, -sy, -sz);
        Point3d max = new Point3d(sx, sy, sz);
        bounds = new BoundingBox(min, max);

        // front face
        coords[0] = sx;
        coords[1] = -sy;
        coords[2] = sz;
        coords[3] = sx;
        coords[4] = sy;
        coords[5] = sz;
        coords[6] = -sx;
        coords[7] = sy;
        coords[8] = sz;
        coords[9] = -sx;
        coords[10] = -sy;
        coords[11] = sz;
        // back face
        coords[12] = -sx;
        coords[13] = -sy;
        coords[14] = -sz;
        coords[15] = -sx;
        coords[16] = sy;
        coords[17] = -sz;
        coords[18] = sx;
        coords[19] = sy;
        coords[20] = -sz;
        coords[21] = sx;
        coords[22] = -sy;
        coords[23] = -sz;
        // right face
        coords[24] = sx;
        coords[25] = -sy;
        coords[26] = -sz;
        coords[27] = sx;
        coords[28] = sy;
        coords[29] = -sz;
        coords[30] = sx;
        coords[31] = sy;
        coords[32] = sz;
        coords[33] = sx;
        coords[34] = -sy;
        coords[35] = sz;
        // left face
        coords[36] = -sx;
        coords[37] = -sy;
        coords[38] = sz;
        coords[39] = -sx;
        coords[40] = sy;
        coords[41] = sz;
        coords[42] = -sx;
        coords[43] = sy;
        coords[44] = -sz;
        coords[45] = -sx;
        coords[46] = -sy;
        coords[47] = -sz;
        // top face
        coords[48] = sx;
        coords[49] = sy;
        coords[50] = sz;
        coords[51] = sx;
        coords[52] = sy;
        coords[53] = -sz;
        coords[54] = -sx;
        coords[55] = sy;
        coords[56] = -sz;
        coords[57] = -sx;
        coords[58] = sy;
        coords[59] = sz;
        // bottom face
        coords[60] = -sx;
        coords[61] = -sy;
        coords[62] = sz;
        coords[63] = -sx;
        coords[64] = -sy;
        coords[65] = -sz;
        coords[66] = sx;
        coords[67] = -sy;
        coords[68] = -sz;
        coords[69] = sx;
        coords[70] = -sy;
        coords[71] = sz;

        impl.setCoordinates(0, coords);
    }

    /**  Description of the Method */
    private void initNormals() {
        float[] normals = new float[72];

        // front face
        normals[0] = 0.0f;
        normals[1] = 0.0f;
        normals[2] = 1.0f;
        normals[3] = 0.0f;
        normals[4] = 0.0f;
        normals[5] = 1.0f;
        normals[6] = 0.0f;
        normals[7] = 0.0f;
        normals[8] = 1.0f;
        normals[9] = 0.0f;
        normals[10] = 0.0f;
        normals[11] = 1.0f;

        // back face
        normals[12] = 0.0f;
        normals[13] = 0.0f;
        normals[14] = -1.0f;
        normals[15] = 0.0f;
        normals[16] = 0.0f;
        normals[17] = -1.0f;
        normals[18] = 0.0f;
        normals[19] = 0.0f;
        normals[20] = -1.0f;
        normals[21] = 0.0f;
        normals[22] = 0.0f;
        normals[23] = -1.0f;

        // right face
        normals[24] = 1.0f;
        normals[25] = 0.0f;
        normals[26] = 0.0f;
        normals[27] = 1.0f;
        normals[28] = 0.0f;
        normals[29] = 0.0f;
        normals[30] = 1.0f;
        normals[31] = 0.0f;
        normals[32] = 0.0f;
        normals[33] = 1.0f;
        normals[34] = 0.0f;
        normals[35] = 0.0f;

        // left face
        normals[36] = -1.0f;
        normals[37] = 0.0f;
        normals[38] = 0.0f;
        normals[39] = -1.0f;
        normals[40] = 0.0f;
        normals[41] = 0.0f;
        normals[42] = -1.0f;
        normals[43] = 0.0f;
        normals[44] = 0.0f;
        normals[45] = -1.0f;
        normals[46] = 0.0f;
        normals[47] = 0.0f;

        // top face
        normals[48] = 0.0f;
        normals[49] = 1.0f;
        normals[50] = 0.0f;
        normals[51] = 0.0f;
        normals[52] = 1.0f;
        normals[53] = 0.0f;
        normals[54] = 0.0f;
        normals[55] = 1.0f;
        normals[56] = 0.0f;
        normals[57] = 0.0f;
        normals[58] = 1.0f;
        normals[59] = 0.0f;

        // bottom face
        normals[60] = 0.0f;
        normals[61] = -1.0f;
        normals[62] = 0.0f;
        normals[63] = 0.0f;
        normals[64] = -1.0f;
        normals[65] = 0.0f;
        normals[66] = 0.0f;
        normals[67] = -1.0f;
        normals[68] = 0.0f;
        normals[69] = 0.0f;
        normals[70] = -1.0f;
        normals[71] = 0.0f;

        impl.setNormals(0, normals);
    }

    /**  Description of the Method */
    private void initTextureCoordinates() {
        float[] coords = new float[48];
        // front face
        int i = 0;
        coords[i++] = 0.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        // back face
        coords[i++] = 0.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        // right face
        coords[i++] = 0.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        // left face
        coords[i++] = 0.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        // top face
        coords[i++] = 0.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        // bottom face
        coords[i++] = 0.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;
        coords[i++] = 1.0f;
        coords[i++] = 0.0f;
        coords[i++] = 1.0f;

        impl.setTextureCoordinates(0, 0, coords);
    }
    
    /**  Description of the Method */
    void initImpl() {
        impl = new org.jogamp.java3d.QuadArray(24,
                org.jogamp.java3d.QuadArray.COORDINATES | 
                org.jogamp.java3d.QuadArray.TEXTURE_COORDINATE_2 |
                org.jogamp.java3d.QuadArray.NORMALS);
        updatePoints();
        initNormals();
        initTextureCoordinates();
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("size")) {
            System.out.println("Updating the points size = " +
                    size.value[0] + " " + size.value[1] + " " + size.value[2]);
            updatePoints();
        }
        else {
            System.err.println("Box.notifyMethod: unexpected eventInName: " +
                    eventInName);
        }
    }

    /**
     *  Gets the implGeom attribute of the Box object
     *
     *@return  The implGeom value
     */
    public org.jogamp.java3d.Geometry getImplGeom() {
        if (impl == null) {
            throw new NullPointerException();
        }
        return impl;
    }

    /**
     *  Gets the boundingBox attribute of the Box object
     *
     *@return  The boundingBox value
     */
    public BoundingBox getBoundingBox() {
        return bounds;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public boolean haveTexture() {
        //return false;
        return true;
    }

    /**
     *  Gets the numTris attribute of the Box object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        return 12;// 6 faces * 2 tris
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Box(loader, (SFVec3f) size.clone());
    }

    /**
     *  Gets the type attribute of the Box object
     *
     *@return  The type value
     */
    public String getType() {
        return "Box";
    }

    /**  Description of the Method */
    void initFields() {
        size.init(this, FieldSpec, Field.EXPOSED_FIELD, "size");
    }

}

