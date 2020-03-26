/*
 * $RCSfile: PointSet.java,v $
 *
 *      @(#)PointSet.java 1.16 98/11/05 20:34:53
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
 *
 * $Revision: 1.2 $
 * $Date: 2005/02/03 23:06:59 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.GeometryArray;

import org.jogamp.java3d.PointArray;
import org.jogamp.vecmath.Point3d;

/**  Description of the Class */
public class PointSet extends Geometry {

    PointArray impl;
    BoundingBox bounds;

    SFNode color;
    SFNode coord;

    int vertexFormat = 0;
    int vertexCount = 0;
    boolean haveColors = false;
    Coordinate coordNode = null;
    Color colorNode = null;

    /**
     *Constructor for the PointSet object
     *
     *@param  loader Description of the Parameter
     */
    public PointSet(Loader loader) {
        super(loader);
        color = new SFNode(null);
        coord = new SFNode(null);
        initFields();
    }

    /**
     *Constructor for the PointSet object
     *
     *@param  loader Description of the Parameter
     *@param  coord Description of the Parameter
     *@param  color Description of the Parameter
     */
    PointSet(Loader loader, SFNode coord, SFNode color) {
        super(loader);
        this.coord = coord;
        this.color = color;
        initFields();
    }

    /**  Description of the Method */
    public void initImpl() {
        if ((coord.node != null) && (coord.node instanceof Coordinate)) {
            coordNode = (Coordinate) coord.node;
            if (coordNode.point.size >= 3) {
                vertexFormat = org.jogamp.java3d.GeometryArray.COORDINATES;
                vertexCount = coordNode.point.size / 3;

                if ((color.node != null) && (color.node instanceof Color)) {
                    colorNode = (Color) color.node;
                    if ((colorNode.color.vals.length / 3) == vertexCount) {
                        haveColors = true;
                        vertexFormat |= org.jogamp.java3d.GeometryArray.COLOR_3;
                    }
                }

                impl = new PointArray(vertexCount, vertexFormat);
                impl.setCoordinates(0, coordNode.point.value, 0, vertexCount);

                if (haveColors) {
                    impl.setColors(0, colorNode.color.vals);
                }

                // else make sure j3d uses emmissive color from material
                // or track the material in the this shape

                bounds = coordNode.point.getBoundingBox();
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
        if (eventInName.equals("color") || eventInName.equals("coord")) {
            initImpl();
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new PointSet(loader, (SFNode) coord.clone(),
                (SFNode) color.clone());
    }

    /**
     *  Gets the type attribute of the PointSet object
     *
     *@return  The type value
     */
    public String getType() {
        return "PointSet";
    }

    /**  Description of the Method */
    void initFields() {
        coord.init(this, FieldSpec, Field.EXPOSED_FIELD, "coord");
        color.init(this, FieldSpec, Field.EXPOSED_FIELD, "color");
    }

    /**
     *  Gets the implGeom attribute of the PointSet object
     *
     *@return  The implGeom value
     */
    public org.jogamp.java3d.Geometry getImplGeom() {
        return (org.jogamp.java3d.Geometry) impl;
    }

    /**
     *  Gets the boundingBox attribute of the PointSet object
     *
     *@return  The boundingBox value
     */
    public org.jogamp.java3d.BoundingBox getBoundingBox() {
        return bounds;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public boolean haveTexture() {
        return false;
    }

    /**
     *  Gets the numTris attribute of the PointSet object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        return 0;
    }

}

