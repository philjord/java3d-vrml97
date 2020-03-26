/*
 * $RCSfile: IndexedLineSet.java,v $
 *
 *      @(#)IndexedLineSet.java 1.19 98/11/05 20:34:34
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
 * $Date: 2005/02/03 23:06:56 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.LineStripArray;

/**  Description of the Class */
public class IndexedLineSet extends Geometry {

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

    /**
     *Constructor for the IndexedLineSet object
     *
     *@param  loader Description of the Parameter
     */
    public IndexedLineSet(Loader loader) {
        super(loader);
        colorPerVertex = new SFBool(true);
        color = new SFNode();
        coord = new SFNode();
        colorIndex = new MFInt32();
        coordIndex = new MFInt32();
        initFields();
    }

    /**
     *Constructor for the IndexedLineSet object
     *
     *@param  loader Description of the Parameter
     *@param  coord Description of the Parameter
     *@param  coordIndex Description of the Parameter
     *@param  color Description of the Parameter
     *@param  colorIndex Description of the Parameter
     *@param  colorPerVertex Description of the Parameter
     */
    public IndexedLineSet(Loader loader, SFNode coord, MFInt32 coordIndex,
            SFNode color, MFInt32 colorIndex, SFBool colorPerVertex) {
        super(loader);
        this.coord = coord;
        this.coordIndex = coordIndex;
        this.color = color;
        this.colorIndex = colorIndex;
        this.colorPerVertex = colorPerVertex;
        initFields();
    }

    /**  Description of the Method */
    public void initImpl() {
        if ((coord != null) && (coord.node instanceof Coordinate) &&
                (coordIndex.size != 0)) {
            coordNode = (Coordinate) coord.node;
            vertexFormat = GeometryArray.COORDINATES;
            numLines = coordIndex.primCount();
            numIndices = coordIndex.indexCount();
            lineSizes = new int[numLines];
            implCoordIndex = new int[numIndices];
            coordIndex.fillImplArrays(lineSizes, implCoordIndex);

            if ((color != null) && (color.node instanceof Color)) {
                colorNode = (Color) color.node;
                vertexFormat |= org.jogamp.java3d.GeometryArray.COLOR_3;
                haveColor = true;
                if (colorPerVertex.value == false) {
                    if (colorIndex.size == 0) {
                        implColorIndex = new int[numIndices];
                        int curIndex = 0;
                        for (int j = 0; j < numLines; j++) {
                            for (int i = 0; i < lineSizes[j]; i++) {
                                implColorIndex[curIndex++] = j;
                            }
                        }
                    }
                    else {
                        // color per line
                        if (colorIndex.size != numLines) {
                            System.out.println("ILS: colorIndex.size = " +
                                    colorIndex.size + " != numLines = " + numLines);
                        }
                        implColorIndex = new int[numIndices];
                        int curIndex = 0;
                        for (int j = 0; j < numLines; j++) {
                            for (int i = 0; i < lineSizes[j]; i++) {
                                implColorIndex[curIndex++] =
                                        colorIndex.value[j];
                            }
                        }
                    }
                }
                else {
                    if (colorIndex.size == 0) {
                        implColorIndex = implCoordIndex;
                    }
                    else {
                        implColorIndex = new int[numIndices];
                        if (!coordIndex.fillImplArraysTest(lineSizes,
                                implColorIndex)) {
                            // TODO: throw exception
                        }
                        // TODO: test that size of color vals is correct
                    }
                }
            }

            // Don't use indexed data, since SDRC (and probably others) use
            // the same geometry for many different primitives, leading
            // to many different copies of the coords.  Instead, unindexify
            // before creating the impl.

            float[] implCoords = new float[3 * numIndices];
            for (int i = 0; i < numIndices; i++) {
                int implBase = i * 3;
                int indexBase = implCoordIndex[i] * 3;
                implCoords[implBase] = coordNode.point.value[indexBase];
                implCoords[implBase + 1] = coordNode.point.value[indexBase + 1];
                implCoords[implBase + 2] = coordNode.point.value[indexBase + 2];
            }

            impl = new LineStripArray(numIndices, vertexFormat, lineSizes);
            impl.setCoordinates(0, implCoords);

            if (haveColor) {
                float[] implColors = new float[3 * numIndices];
                for (int i = 0; i < numIndices; i++) {
                    int implBase = i * 3;
                    int indexBase = implColorIndex[i] * 3;
                    implColors[implBase] = colorNode.color.vals[indexBase];
                    implColors[implBase + 1] = colorNode.color.vals[indexBase + 1];
                    implColors[implBase + 2] = colorNode.color.vals[indexBase + 2];
                }
                impl.setColors(0, implColors);
            }

            bounds = coordNode.point.getBoundingBox();
        }
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("colorIndex") || eventInName.equals("coordIndex")) {
            initImpl();
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new IndexedLineSet(loader, (SFNode) coord.clone(),
                (MFInt32) coordIndex.clone(), (SFNode) color.clone(),
                (MFInt32) colorIndex.clone(), (SFBool) colorPerVertex.clone());
    }

    /**
     *  Gets the type attribute of the IndexedLineSet object
     *
     *@return  The type value
     */
    public String getType() {
        return "IndexedLineSet";
    }

    /**  Description of the Method */
    void initFields() {
        coord.init(this, FieldSpec, Field.EXPOSED_FIELD, "coord");
        coordIndex.init(this, FieldSpec, Field.EVENT_IN, "coordIndex");
        color.init(this, FieldSpec, Field.EXPOSED_FIELD, "color");
        colorIndex.init(this, FieldSpec, Field.EVENT_IN, "colorIndex");
        colorPerVertex.init(this, FieldSpec, Field.FIELD, "colorPerVertex");
    }

    /**
     *  Gets the implGeom attribute of the IndexedLineSet object
     *
     *@return  The implGeom value
     */
    public org.jogamp.java3d.Geometry getImplGeom() {
        return (org.jogamp.java3d.Geometry) impl;
    }

    /**
     *  Gets the boundingBox attribute of the IndexedLineSet object
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
        return false;//!
    }

    /**
     *  Gets the numTris attribute of the IndexedLineSet object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        return 0;
    }

}

