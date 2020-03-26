/*
 * $RCSfile: IndexedFaceSet.java,v $
 *
 *      @(#)IndexedFaceSet.java 1.66 99/03/05 17:13:57
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
 * $Revision: 1.4 $
 * $Date: 2006/04/05 09:55:09 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.utils.geometry.*;
import java.util.*;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.IndexedGeometryArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.*;

/**  Description of the Class */
public class IndexedFaceSet extends Geometry implements Reusable,
        Ownable {

    GeometryArray impl;

    // eventIn
    MFInt32 colorIndex;
    MFInt32 coordIndex;
    MFInt32 normalIndex;
    MFInt32 texCoordIndex;

    SFNode color;
    SFNode coord;
    SFNode normal;
    SFNode texCoord;

    // field
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

    // Reusable buffers, Length is allocated size
    Point3f[] coordArray = null;
    int coordArrayLength = 0;
    Vector3f[] normalArray = null;
    int normalArrayLength = 0;
    int[] tempFace;
    int tempFaceLength = 0;

    GeometryInfo gi;

    BoundingBox bounds;
    boolean allTriangles;

    // impl types
    /**  Description of the Field */
    protected final static int GENERAL = 100;
    /**  Description of the Field */
    protected final static int TRIS = 101;
    /**  Description of the Field */
    protected final static int QUAD = 102;

    int implType;
    org.jogamp.java3d.IndexedTriangleFanArray implIndexed;
    org.jogamp.java3d.TriangleArray implTris;
    Shape owner;

    /**
     *Constructor for the IndexedFaceSet object
     *
     *@param  loader Description of the Parameter
     */
    public IndexedFaceSet(Loader loader) {
        super(loader);
        colorIndex = new MFInt32();
        coordIndex = new MFInt32();
        normalIndex = new MFInt32();
        texCoordIndex = new MFInt32();

        coord = new SFNode(null);
        normal = new SFNode(null);
        color = new SFNode(null);
        texCoord = new SFNode(null);

        ccw = new SFBool(true);
        colorPerVertex = new SFBool(true);
        convex = new SFBool(true);
        if (loader.autoSmooth) {
            creaseAngle = new SFFloat(3.14f);
        }
        else {
            creaseAngle = new SFFloat(0.0f);
        }
        normalPerVertex = new SFBool(true);
        solid = new SFBool(true);

        initFields();
    }

    /**
     *Constructor for the IndexedFaceSet object
     *
     *@param  loader Description of the Parameter
     *@param  colorIndex Description of the Parameter
     *@param  coordIndex Description of the Parameter
     *@param  normalIndex Description of the Parameter
     *@param  texCoordIndex Description of the Parameter
     *@param  coord Description of the Parameter
     *@param  normal Description of the Parameter
     *@param  color Description of the Parameter
     *@param  texCoord Description of the Parameter
     *@param  ccw Description of the Parameter
     *@param  colorPerVertex Description of the Parameter
     *@param  convex Description of the Parameter
     *@param  creaseAngle Description of the Parameter
     *@param  normalPerVertex Description of the Parameter
     *@param  solid Description of the Parameter
     */
    IndexedFaceSet(Loader loader,
            MFInt32 colorIndex, MFInt32 coordIndex,
            MFInt32 normalIndex, MFInt32 texCoordIndex, SFNode coord,
            SFNode normal, SFNode color, SFNode texCoord, SFBool ccw,
            SFBool colorPerVertex, SFBool convex, SFFloat creaseAngle,
            SFBool normalPerVertex, SFBool solid) {
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

    /**  Description of the Method */
    public void reset() {
    }

    /*
	haveNormals = false;
	haveColors = false;
	haveTexture = false;
	colorIndex.reset();
	coordIndex.reset();
	normalIndex.reset();
	texCoordIndex.reset();
	coord.reset();
	normal.reset();
	color.reset();
	texCoord.reset();
	ccw.reset();
	colorPerVertex.reset();
	convex.reset();
	creaseAngle.reset();
	normalPerVertex.reset();
	solid.reset();
	numTris = 0;
	impl = null;
	implReady = false;
    }
*/
    // counts number of faces and picks impl type
    /**  Description of the Method */
    private void initSetup() {
        Coordinate coordNode = (Coordinate) coord.node;
        int coordValsSize = coordNode.point.size;
        float[] coordVals = coordNode.point.value;
        int coordListSize = coordIndex.size;
        int[] coordList = coordIndex.value;
        numFaces = 0;
        numIndices = 0;
        int curSize = 0;

        constSize = 0;
        isConstSize = false;

        // initialize the bounding box
        bounds = coordNode.point.getBoundingBox();

        // some modelers seem to overrun their coords
        for (int i = 0; i < coordListSize; i++) {
            if (coordList[i] > coordValsSize - 1) {
                // index overran actual coords, wrap around
                coordList[i] %= coordValsSize;
            }
        }

        if (normal.node != null) {
            Normal normalNode = (Normal) normal.node;
            float[] norms = normalNode.vector.value;

            if (normalIndex.size > 0) {
                for (int i = 0; i < normalIndex.size; i++) {
                    if (normalIndex.value[i] > norms.length - 1) {
                        normalIndex.value[i] %= norms.length;
                    }
                }
            }
        }
        // TBD same for color and texture

        // count how many faces and indices
        boolean lastCoord = false;
        for (int i = 0; i < coordListSize; i++) {
            if (coordList[i] == -1) {
                numTris += curSize - 2;
                if (numFaces == 0) {
                    isConstSize = true;
                    constSize = curSize;
                    ;
                }
                else {
                    if (curSize != constSize) {
                        isConstSize = false;
                    }
                }
                numFaces++;
                curSize = 0;
                lastCoord = false;
            }
            else {
                numIndices++;
                curSize++;
                lastCoord = true;
            }
        }
        if (lastCoord) {
            // coord list ended with a coord, finish off the last face
            numTris += curSize - 2;
            if (numFaces == 0) {
                isConstSize = true;
                constSize = curSize;
                ;
            }
            else {
                if (curSize != constSize) {
                    isConstSize = false;
                }
            }
            numFaces++;
        }
        implType = GENERAL;
        if (isConstSize == true) {
            if (constSize == 4) {
                implType = QUAD;
                if (loader.debug) {
                    System.out.println("Const size IFS with " + numFaces +
                            " quads");
                }
            }
            else if (constSize == 3) {
                implType = TRIS;
                if (loader.debug) {
                    System.out.println("Const size IFS with " + numFaces +
                            " tris");
                }
            }
            else {
                if (loader.debug) {
                    System.out.println("Const size IFS with " + numFaces +
                            " faces of size " + constSize);
                }
            }
        }
        else {
            if (loader.debug) {
                System.out.println("Variable size IFS, numIndicies = " +
                        numIndices);
                System.out.println("curSize = " + curSize);
                System.out.println("numFaces = " + numFaces);
                System.out.println("(Index)coordList.length= " +
                        coordList.length);
                System.out.println("(Points)coordVals.length= " +
                        coordVals.length);
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@param  normalValSize Description of the Parameter
     *@param  normalVals Description of the Parameter
     */
    void copyNormals(int normalValSize, float[] normalVals) {
        int i;
        int curVal;
        // initialize the normal Vector3f array
        int numNormals = normalValSize / 3;
        if (normalArrayLength < numNormals) {
            Vector3f[] newNormalArray = new Vector3f[numNormals];
            if (normalArray != null) {
                System.arraycopy(normalArray, 0, newNormalArray, 0,
                        normalArrayLength);
            }
            for (i = normalArrayLength; i < numNormals; i++) {
                newNormalArray[i] = new Vector3f();
            }
            normalArray = newNormalArray;
            normalArrayLength = numNormals;
        }
        for (i = 0, curVal = 0; i < numNormals; i++) {
            normalArray[i].x = normalVals[curVal++];
            normalArray[i].y = normalVals[curVal++];
            normalArray[i].z = normalVals[curVal++];
        }
    }

    /**
     *  Description of the Method
     *
     *@param  coordListSize Description of the Parameter
     *@param  coordList Description of the Parameter
     */
    private void setupIndexNormals(int coordListSize, int[] coordList) {
        haveNormals = false;

        int normalListSize = normalIndex.size;
        int[] normalList = normalIndex.value;

        if (normalPerVertex.value == true) {
            if ((normalList == null) || (normalListSize == 0)) {
                normalListSize = coordListSize;
                normalList = coordList;
            }

            if (normalList == coordList) {
                implNormalIndex = implCoordIndex;
            }
            else {
                implNormalIndex = new int[numIndices];
                if (!normalIndex.fillImplArraysTest(facetSizes,
                        implNormalIndex)) {
                    loader.warning(warnId, "facet sizes on normalIndex "
                             + " don't match coordIndex");
                }
            }
            // the field's normals are the vertex normals
            Normal normalNode = (Normal) normal.node;
            if (normalNode != null) {
                int normalValSize = normalNode.vector.size;
                float[] normalVals = normalNode.vector.value;
                if (normalVals == null) {
                    loader.warning(warnId, "normalVals is null, ignoring " +
                            "normals");
                    return;
                }
                copyNormals(normalValSize, normalVals);

                // assume that normVals is correctly sized for the indicies
                haveNormals = true;
            }
            else {// No normals included - must generate them
                haveNormals = false;
            }
        }
        else {
            // normal per facet
            if ((normalListSize > 0) && (normalListSize != numFaces)) {
                loader.warning(warnId, "normalIndex length != number of faces");
            }

            // set up the normal indicies
            implNormalIndex = new int[numIndices];
            int curIndex = 0;
            for (int curFace = 0; curFace < numFaces; curFace++) {
                for (int j = 0; j < facetSizes[curFace]; j++) {
                    if (curFace < normalListSize) {
                        implNormalIndex[curIndex++] = normalList[curFace];
                    }
                    else {
                        // this is the std defn for normalList == null
                        implNormalIndex[curIndex++] = curFace;
                    }
                }
            }

            // the field's normals are the facet normals
            Normal normalNode = (Normal) normal.node;
            if (normalNode != null) {
                int normalValSize = normalNode.vector.size;
                float[] normalVals = normalNode.vector.value;
                if (normalListSize == 0) {
                    //if (normalValSize != (numFaces * 3)) {
                    //loader.warning(warnId, "normals length != (number of " +
                    //" faces * 3)");
                    //return;
                    //}
                }
                copyNormals(normalValSize, normalVals);
                haveNormals = true;
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@param  coordListSize Description of the Parameter
     *@param  coordList Description of the Parameter
     */
    private void setupIndexTextures(int coordListSize, int[] coordList) {

        haveTexture = false;

        int texListSize = texCoordIndex.size;
        int[] texList = texCoordIndex.value;
        if ((texList == null) || (texListSize == 0)) {
            texListSize = coordListSize;
            texList = coordList;
        }

        if (texList == coordList) {
            implTexIndex = implCoordIndex;
        }
        else {
            implTexIndex = new int[numIndices];
            if (texCoordIndex.fillImplArraysTest(facetSizes,
                    implTexIndex) == false) {
                loader.warning(warnId, "texCoordIndex does not match " +
                        "coordIndex");
            }
        }

        // the field's texCoords are the vertex texCoords
        TextureCoordinate texNode = (TextureCoordinate) texCoord.node;
        if (texNode != null) {
            indexTexVals = texNode.point.vals;
            if (indexTexVals == null) {
                loader.warning(warnId, "texture value is null");
                return;
            }
            // assume that texVals is correctly sized for the indicies
            haveTexture = true;
        }
    }

    /**
     *  Description of the Method
     *
     *@param  coordListSize Description of the Parameter
     *@param  coordList Description of the Parameter
     */
    private void setupIndexColors(int coordListSize, int[] coordList) {

        haveColors = false;

        int colorListSize = colorIndex.size;
        int[] colorList = colorIndex.value;

        if (colorPerVertex.value == true) {
            if ((colorList == null) || (colorListSize == 0)) {
                colorListSize = coordListSize;
                colorList = coordList;
            }

            if (colorList == coordList) {
                implColorIndex = implCoordIndex;
            }
            else {
                implColorIndex = new int[numIndices];
                if (!colorIndex.fillImplArraysTest(facetSizes,
                        implColorIndex)) {
                    loader.warning(warnId, "colorIndex does not match " +
                            "coordIndex");
                }
            }
            // the field's colors are the vertex colors
            Color colorNode = (Color) color.node;
            if (colorNode != null) {
                indexColorVals = colorNode.color.vals;
                if (indexColorVals == null) {
                    loader.warning(warnId, "color is null");
                    return;
                }
                // assume that normVals is correctly sized for the indicies
                haveColors = true;
            }
        }
        else {
            // color per facet
            if ((colorListSize > 0) && (colorListSize != numFaces)) {
                loader.warning(warnId, "colorIndex size != num faces");
            }

            // set up the color indicies
            implColorIndex = new int[numIndices];
            int curIndex = 0;
            for (int curFace = 0; curFace < numFaces; curFace++) {
                for (int j = 0; j < facetSizes[curFace]; j++) {
                    if (curFace < colorListSize) {
                        implColorIndex[curIndex++] = colorList[curFace];
                    }
                    else {
                        // this is the std defn for colorList == null
                        implColorIndex[curIndex++] = curFace;
                    }
                }
            }

            // the field's colors are the facet colors
            Color colorNode = (Color) color.node;
            if (colorNode != null) {
                indexColorVals = colorNode.color.vals;
                if (colorListSize == 0) {
                    // only do test if no color index values
                    if (indexColorVals.length != (numFaces * 3)) {
                        loader.warning(warnId, "color size != (3 * num faces)");
                        return;
                    }
                }
                haveColors = true;
            }
        }
    }


    /**  Description of the Method */
    private void buildIndexLists() {
        Coordinate coordNode = (Coordinate) coord.node;
        int coordValSize = coordNode.point.size;
        float[] coordVals = coordNode.point.value;
        int coordListSize = coordIndex.size;
        int[] coordList = coordIndex.value;
        int i;
        int curVal;

        // get the arrays from the fields into the arrays GeomInfo wants

        // initialize the coord Point3f array
        int numCoords = coordValSize / 3;
        if (coordArrayLength < numCoords) {
            Point3f[] newCoordArray = new Point3f[coordValSize / 3];
            if (coordArray != null) {
                System.arraycopy(coordArray, 0, newCoordArray, 0,
                        coordArrayLength);
            }
            for (i = coordArrayLength; i < numCoords; i++) {
                newCoordArray[i] = new Point3f();
            }
            coordArray = newCoordArray;
            coordArrayLength = numCoords;
        }
        for (i = 0, curVal = 0; i < numCoords; i++) {
            coordArray[i].x = coordVals[curVal++];
            coordArray[i].y = coordVals[curVal++];
            coordArray[i].z = coordVals[curVal++];
        }

        // allocate the arrays
        // can't resuse these arrays (yet) because .length is used to
        // determine the number of active elements
        facetSizes = new int[numFaces];
        implCoordIndex = new int[numIndices];

        // fill in the arrays
        coordIndex.fillImplArrays(facetSizes, implCoordIndex);

        if (!ccw.getValue()) {
            //System.out.println("CW winding - reverse facet ordering");
            int curIndex = 0;
            for (i = 0; i < numFaces; ++i) {
                int curFaceSize = facetSizes[i];
                if (tempFaceLength < curFaceSize) {
                    tempFace = new int[curFaceSize];
                }
                tempFaceLength = curFaceSize;
                int faceBeginIndex = curIndex;
                for (int j = 0; j < curFaceSize; ++j) {
                    tempFace[j] = implCoordIndex[curIndex];
                    curIndex++;
                }
                for (int j = 0; j < curFaceSize; ++j) {
                    implCoordIndex[faceBeginIndex + j] =
                            tempFace[tempFaceLength - j - 1];
                }
            }
        }
        // try to set up the data we need for normals
        setupIndexNormals(coordListSize, coordList);

        // try to set up the data we need for colors
        setupIndexColors(coordListSize, coordList);

        // try to set up the data we need for colors
        setupIndexTextures(coordListSize, coordList);
    }


    /**  Description of the Method */
    public void initImpl() {
        Coordinate coordNode = (Coordinate) coord.node;
        TextureCoordinate texCoordNode = (TextureCoordinate) texCoord.node;
        Normal normalNode = (Normal) normal.node;
        Color colorNode = (Color) color.node;
        if ((coordNode == null) || (coordIndex.size <= 0)) {
            if (loader.debug) {
                System.out.println("IFS coordIndex.size =" + coordIndex.size);
            }
            //loader.warning(warnId, "no coordinates");
        }
        else {
            // attach this to the coordinate
            // should also be done for any of the Normal, TexCoord...
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

            // use getGeometryArray(), it will use less memory (using indexed
            // will copy the coordArrays to J3D, which may be a waste if the
            // coordArray has been reused so that it is larger than it has
            // to be)

            // ?? any set method is a copy whether it is indexed or not. -rg

            try {
                buildImpl();
                impl = gi.getGeometryArray();
            }
            catch (java.lang.ArrayIndexOutOfBoundsException aioobe) {
                // some cases fool the gi hashtable?
                // workaround: do it agian
                buildImpl();
                impl = gi.getIndexedGeometryArray();
            }
        }

        loader.cleanUp();
        implReady = true;
    }

    /**  Description of the Method */
    void buildImpl() {

        gi = null;
        initSetup();
        if (implType == TRIS) {
            gi = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
        }
        else if (implType == QUAD) {
            gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        }
        else {
            // TODO? handle non-convex with POLYGON
            gi = new GeometryInfo(GeometryInfo.TRIANGLE_FAN_ARRAY);
        }

        buildIndexLists();
		
        gi.setCoordinates(coordArray);
        gi.setCoordinateIndices(implCoordIndex);
        if (implType == GENERAL) {
            gi.setStripCounts(facetSizes);
        }
        if (haveColors) {
            gi.setColors3(indexColorVals);
            gi.setColorIndices(implColorIndex);
        }
        if (haveTexture) {
			gi.setTextureCoordinateParams(1,2);
            gi.setTextureCoordinates(0, indexTexVals);
			if(!ccw.getValue()) {
				int[] implTexIndexCW = new int[implTexIndex.length];
				for(int i = 0;i < implTexIndex.length;i++) {
					implTexIndexCW[i] = implTexIndex[implTexIndex.length-(i+1)];
				}
				implTexIndex = implTexIndexCW;
			}
            gi.setTextureCoordinateIndices(0, implTexIndex);
        }
        if (haveNormals) {
            gi.setNormals(normalArray);
            gi.setNormalIndices(implNormalIndex);
			validateIndexes();
        }
        else {
            // note setting crease angle to max when really it is
            // 0 is not called for in the spec, but it looks much better.
            // this should perhaps be a browser settable menu option.
            float ca = creaseAngle.getValue();
            if (ca < 0.0f) {
                ca = 0.0f;
            }
            if (ca > (float) Math.PI) {
                ca -= (float) Math.PI;
            }
			validateIndexes();
            NormalGenerator ng = new NormalGenerator(ca);
            ng.generateNormals(gi);
        }

        ///if (implType != TRIS) { // stripifier doesn't beat TRIS (yet)
        ///Stripifier st = new Stripifier();
        ///st.stripify(gi);
        ///}

    }
	
	void validateIndexes() throws vrml.InvalidVRMLSyntaxException {
		try {
			gi.recomputeIndices();
		} catch (IllegalArgumentException e) {
			vrml.InvalidVRMLSyntaxException i = new vrml.InvalidVRMLSyntaxException((defName==null)?"in IndexedFaceSet":"in DEF " + defName);
			i.initCause(e);
			throw i;
		} catch (ArrayIndexOutOfBoundsException e) {
			vrml.InvalidVRMLSyntaxException i = new vrml.InvalidVRMLSyntaxException((defName==null)?"in IndexedFaceSet":"in DEF " + defName);
			i.initCause(e);
			throw i;
		}
	}

    /**
     *  Gets the implGeom attribute of the IndexedFaceSet object
     *
     *@return  The implGeom value
     */
    public org.jogamp.java3d.Geometry getImplGeom() {
        return (org.jogamp.java3d.Geometry) impl;
    }

    /**
     *  Gets the boundingBox attribute of the IndexedFaceSet object
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
        return haveTexture;
    }

    /**
     *  Gets the numTris attribute of the IndexedFaceSet object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        if (loader.debug) {
            System.out.println("IFS num tris: " + numTris);
        }
        return numTris;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        // when we handle these events we'll have to check the counts against
        // the impl object. If the impl sizes or format change, then we will
        // have to make a new impl and update any group objects which have
        // references to impl.
        if ((eventInName.equals("coord")) ||
                (eventInName.equals("color")) ||
                (eventInName.equals("normal")) ||
                (eventInName.equals("coordIndex")) ||
                (eventInName.equals("colorIndex")) ||
                (eventInName.equals("normalIndex")) ||
                (eventInName.equals("texCoord")) ||
                (eventInName.equals("texCoordIndex"))) {
            if (loader.debug) {
                System.out.println("updating IFS impl from route!");
            }
            initImpl();
            ((Shape3D) (owner.implNode)).setGeometry(impl);
        }
        else if ((eventInName.equals("route_coord")) ||
                (eventInName.equals("route_coordIndex")) ||
                (eventInName.equals("route_coord_point")) ||
                (eventInName.equals("route_color")) ||
                (eventInName.equals("route_colorIndex")) ||
                (eventInName.equals("route_normal")) ||
                (eventInName.equals("route_normalIndex")) ||
                (eventInName.equals("route_texCoord")) ||
                (eventInName.equals("route_texCoordIndex"))) {
            impl.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
            impl.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
            impl.setCapability(GeometryArray.ALLOW_NORMAL_WRITE);
            impl.setCapability(GeometryArray.ALLOW_NORMAL_READ);
            impl.setCapability(GeometryArray.ALLOW_TEXCOORD_WRITE);
            impl.setCapability(GeometryArray.ALLOW_TEXCOORD_READ);
            impl.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
            impl.setCapability(GeometryArray.ALLOW_COLOR_READ);

            ((Shape3D) (owner.implNode)).setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

        }
        else {
            System.err.println("IndexFaceSet.notifyMethod(): unknown " +
                    "eventInName: " + eventInName);
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        if (loader.debug) {
            System.out.println("IFS.clone() called");
        }
        Object o = new IndexedFaceSet(loader, (MFInt32) colorIndex.clone(),
                (MFInt32) coordIndex.clone(), (MFInt32) normalIndex.clone(),
                (MFInt32) texCoordIndex.clone(), (SFNode) coord.clone(),
                (SFNode) normal.clone(), (SFNode) color.clone(),
                (SFNode) texCoord.clone(), (SFBool) ccw.clone(),
                (SFBool) colorPerVertex.clone(), (SFBool) convex.clone(),
                (SFFloat) creaseAngle.clone(), (SFBool) normalPerVertex.clone(),
                (SFBool) solid.clone());
        loader.cleanUp();
        return o;
    }

    /**
     *  Gets the type attribute of the IndexedFaceSet object
     *
     *@return  The type value
     */
    public String getType() {
        return "IndexedFaceSet";
    }

    /**  Description of the Method */
    void initFields() {
        colorIndex.init(this, FieldSpec, Field.EVENT_IN, "colorIndex");
        coordIndex.init(this, FieldSpec, Field.EVENT_IN, "coordIndex");
        normalIndex.init(this, FieldSpec, Field.EVENT_IN, "normalIndex");
        texCoordIndex.init(this, FieldSpec, Field.EVENT_IN, "texCoordIndex");

        color.init(this, FieldSpec, Field.EXPOSED_FIELD, "color");
        coord.init(this, FieldSpec, Field.EXPOSED_FIELD, "coord");
        normal.init(this, FieldSpec, Field.EXPOSED_FIELD, "normal");
        texCoord.init(this, FieldSpec, Field.EXPOSED_FIELD, "texCoord");

        ccw.init(this, FieldSpec, Field.FIELD, "ccw");
        colorPerVertex.init(this, FieldSpec, Field.FIELD, "colorPerVertex");
        convex.init(this, FieldSpec, Field.FIELD, "convex");
        creaseAngle.init(this, FieldSpec, Field.FIELD, "creaseAngle");
        normalPerVertex.init(this, FieldSpec, Field.FIELD, "normalPerVertex");
        solid.init(this, FieldSpec, Field.FIELD, "solid");
    }


    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toStringBody() {
        String retval = "IndexedFaceSet {\n";
        if (color.node != null) {
            retval += "color " + color;
        }
        if (coord.node != null) {
            retval += "coord " + coord;
        }
        if (normal.node != null) {
            retval += "normal " + normal;
        }
        if (texCoord.node != null) {
            retval += "    texCoord " + texCoord;
        }
        if (ccw.value != true) {
            retval += "ccw FALSE\n";
        }
        if (colorIndex.size != 0) {
            retval += "colorIndex " + colorIndex;
        }
        if (colorPerVertex.value != true) {
            retval += "colorPerVertex FALSE\n";
        }
        if (convex.value != true) {
            retval += "convex FALSE\n";
        }
        if (coordIndex.size != 0) {
            retval += "coordIndex " + coordIndex;
        }
        if (creaseAngle.value != 0.0) {
            retval += "creaseAngle " + creaseAngle.value + "\n";
        }
        if (normalIndex.size != 0) {
            retval += "normalIndex " + normalIndex;
        }
        if (normalPerVertex.value != true) {
            retval += "normalPerVertex FALSE\n";
        }
        if (solid.value != true) {
            retval += "solid FALSE\n";
        }
        if (texCoordIndex.size != 0) {
            retval += "texCoordIndex " + texCoordIndex;
        }
        retval += "}";
        return retval;
    }

    // fulfill the Ownable interface
    /**
     *  Gets the solid attribute of the IndexedFaceSet object
     *
     *@return  The solid value
     */
    public boolean getSolid() {
        return solid.value;
    }

    /**
     *  Sets the owner attribute of the IndexedFaceSet object
     *
     *@param  s The new owner value
     */
    public void setOwner(Shape s) {
        this.owner = s;
    }

}

