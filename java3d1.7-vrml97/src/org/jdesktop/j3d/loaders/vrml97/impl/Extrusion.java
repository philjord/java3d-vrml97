/*
 * $RCSfile: Extrusion.java,v $
 *
 *      @(#)Extrusion.java 1.23 99/03/24 15:56:40
 *
 * Copyright (c) 1996-1999 Sun Microsystems, Inc. All Rights Reserved.
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

import org.jogamp.java3d.utils.geometry.GeometryInfo;
import org.jogamp.java3d.utils.geometry.NormalGenerator;
import org.jogamp.java3d.utils.geometry.Stripifier;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.IndexedQuadArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.*;

/**  Description of the Class */
class Extrusion extends Geometry implements Ownable {

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
    // will contain the per spine
    // transform composed with orientation
    Matrix3f[] rotations;
    Transform3D[] transforms;

    // data -> gi
    Point3f[] coords;
    int[] coordIndex;
    int[] stripCounts;

    boolean collinear = false;
    boolean closed = false;// spines

    float[] a2 = new float[2];
    float[] a3 = new float[3];
    float[] a4 = new float[4];

    int numTris = 0;

    boolean hardDebug = false;


    /**
     *Constructor for the Extrusion object
     *
     *@param  loader Description of the Parameter
     */
    public Extrusion(Loader loader) {
        super(loader);
        beginCap = new SFBool(true);
        endCap = new SFBool(true);
        ccw = new SFBool(true);
        convex = new SFBool(true);
        solid = new SFBool(true);
        if (loader.autoSmooth) {
            creaseAngle = new SFFloat(.9f);
        }
        else {
            creaseAngle = new SFFloat(0);
        }
        crossSection = new MFVec2f(5, new float[10]);
        crossSection.set1Value(0, 1.0f, 1.0f);
        crossSection.set1Value(1, 1.0f, -1.0f);
        crossSection.set1Value(2, -1.0f, -1.0f);
        crossSection.set1Value(3, -1.0f, 1.0f);
        crossSection.set1Value(4, 1.0f, 1.0f);
        spine = new MFVec3f(2, new float[6]);
        spine.set1Value(0, 0.0f, 0.0f, 0.0f);
        spine.set1Value(1, 0.0f, 1.0f, 0.0f);
        orientation = new MFRotation(1, new float[4]);
        orientation.set1Value(0, 0.0f, 0.0f, 1.0f, 0.0f);
        scale = new MFVec2f(new float[2]);
        scale.set1Value(0, 1.0f, 1.0f);
        initFields();
    }

    /**
     *Constructor for the Extrusion object
     *
     *@param  loader Description of the Parameter
     *@param  beginCap Description of the Parameter
     *@param  ccw Description of the Parameter
     *@param  convex Description of the Parameter
     *@param  creaseAngle Description of the Parameter
     *@param  crossSection Description of the Parameter
     *@param  endCap Description of the Parameter
     *@param  orientation Description of the Parameter
     *@param  scale Description of the Parameter
     *@param  solid Description of the Parameter
     *@param  spine Description of the Parameter
     */
    Extrusion(Loader loader, SFBool beginCap, SFBool ccw,
            SFBool convex, SFFloat creaseAngle, MFVec2f crossSection,
            SFBool endCap, MFRotation orientation, MFVec2f scale,
            SFBool solid, MFVec3f spine) {

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

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public boolean haveTexture() {
        return false;
    }

    /**
     *  Gets the numTris attribute of the Extrusion object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        return numTris;
    }

    /**
     *  Gets the implGeom attribute of the Extrusion object
     *
     *@return  The implGeom value
     */
    org.jogamp.java3d.Geometry getImplGeom() {
        return impl;
    }

    /**
     *  Gets the boundingBox attribute of the Extrusion object
     *
     *@return  The boundingBox value
     */
    BoundingBox getBoundingBox() {

        // create a tiny bounding box about a coordinate
        // to seed the placement
        Point3d epsilon = new Point3d(.000001, .000001, .000001);
        Point3d lower = new Point3d(coords[0]);
        Point3d upper = new Point3d(coords[0]);
        lower.sub(epsilon);
        upper.add(epsilon);

        bounds = new org.jogamp.java3d.BoundingBox(lower, upper);

        for (int c = 1; c < coords.length; c++) {
            bounds.combine(new Point3d(coords[c]));
        }
        if (loader.debug) {
            System.out.println(bounds);
        }
        return bounds;
    }

    /**  Description of the Method */
    void initImpl() {

        // endcaps may need special handling
        gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);

        // convert vrml data to intermediate form
        initSetup();

        // calculate per spine SCP transforms
        // results in transforms[] being filled withs SCP info
        // complete with scale, translation and orientation from
        // fields
        calculateSCP();

        // transform the crossSections to coordinates
        createExtrusion();

        // indexify, including endcaps if needed
        // leaves coordIndex with index and stripCounts with counts
        // per facet.
        createIndices();
        if (hardDebug) {
            System.out.println("coords");
            for (int i = 0; i < coords.length; i++) {
                System.out.println(coords[i]);
            }
            System.out.println("coordIndex");
            for (int i = 0; i < coordIndex.length; i++) {
                System.out.println(coordIndex[i]);
            }
            System.out.println("stripCounts");
            for (int i = 0; i < stripCounts.length; i++) {
                System.out.println(stripCounts[i]);
            }
        }
        gi.setCoordinates(coords);
        gi.setCoordinateIndices(coordIndex);
        gi.setStripCounts(stripCounts);

        //Stripifier st = new Stripifier();
        //st.stripify(gi);

        float ca = creaseAngle.getValue();
        if (ca < 0.0f) {
            ca = 0.0f;
        }
        if (ca > (float) Math.PI) {
            ca -= (float) Math.PI;
        }

        NormalGenerator ng = new NormalGenerator(ca);
        ng.generateNormals(gi);

        impl = gi.getGeometryArray();

        implReady = true;

    }

    /**  Description of the Method */
    void initSetup() {
        // load the crossSectionPts data
        crossSectionPts = new Point3f[crossSection.getSize()];

        if (hardDebug) {
            System.out.println(crossSection.getSize());
        }
        for (int i = 0; i < crossSectionPts.length; i++) {
            crossSection.get1Value(i, a2);
            crossSectionPts[i] = new Point3f(a2[0], 0.0f, a2[1]);
        }

        // load the scales
        // scales size may not match spine size, if so
        // use previously set scale
        scales = new Vector3f[spine.getSize()];
        for (int i = 0; i < scales.length; i++) {
            if (i < scale.getSize()) {
                scale.get1Value(i, a2);
            }
            scales[i] = new Vector3f(a2[0], 1.0f, a2[1]);
        }

        // load the spines
        spines = new Point3f[spine.getSize()];
        for (int i = 0; i < spines.length; i++) {
            spine.get1Value(i, a3);
            spines[i] = new Point3f(a3);
        }

        // load the per spine orientation modifiers
        orientations = new AxisAngle4f[spine.getSize()];
        for (int i = 0; i < orientations.length; i++) {
            if (i < orientation.getSize()) {
                orientation.get1Value(i, a4);
            }
            orientations[i] = new AxisAngle4f(a4);
        }

        rotations = new Matrix3f[spines.length];

        // if the tail meets the head
        if (spines[0].equals(spines[spines.length - 1])) {
            closed = true;
        }

        // if entirely collinear
        Vector3d v2 = new Vector3d();
        Vector3d v1 = new Vector3d();
        Vector3d v0 = new Vector3d();
        double d = 0.0;
        for (int i = 1; i < spines.length - 1; i++) {
            v2.set(spines[i + 1]);
            v1.set(spines[i]);
            v0.set(spines[i - 1]);
            v2.sub(v1);
            v1.sub(v0);
            v0.cross(v2, v1);
            d += v0.dot(v0);
        }
        collinear = (d == 0.0);
        if (hardDebug && collinear) {
            System.out.println("spine is straight");
        }

    }

    /**  Description of the Method */
    void calculateSCP() {
        // find an orthonormal basis and construct rotation matrix
        // for each spine. handle special cases in second pass
        Vector3f[] x;
        // find an orthonormal basis and construct rotation matrix
        // for each spine. handle special cases in second pass
        Vector3f[] y;
        // find an orthonormal basis and construct rotation matrix
        // for each spine. handle special cases in second pass
        Vector3f[] z;
        Vector3f u;
        Vector3f v;
        Vector3f zero = new Vector3f(0.0f, 0.0f, 0.0f);
        int last = spines.length - 1;

        x = new Vector3f[spines.length];
        y = new Vector3f[spines.length];
        z = new Vector3f[spines.length];

        if (collinear) {
            if (closed) {
                throw new vrml.InvalidVRMLSyntaxException(
                        "invalid Extrusion data; looks like a solid of revolution"
                        );
            }

            for (int i = 0; i < spines.length; i++) {
                // redo, this should take the direction of y
                x[i] = new Vector3f(1.0f, 0.0f, 0.0f);
                y[i] = new Vector3f(0.0f, 1.0f, 0.0f);
                z[i] = new Vector3f(0.0f, 0.0f, 1.0f);
            }
        }
        else {
            // find y[i] for all but first and last
            // most times the exception cases are bad data and hopefully
            // wont happen. It is free to try catch you later, so hopes
            // 99% cases will be one if faster by not checking the if
            for (int i = 1; i < last; i++) {
                y[i] = new Vector3f();
                y[i].sub(spines[i + 1], spines[i - 1]);
                try {
                    norm(y[i]);
                }
                catch (ArithmeticException ae) {
                    if (hardDebug) {
                        System.out.println(ae + " " + y[i]);
                    }
                    // spines[i+1] equals spines[i-1]
                    try {
                        y[i].sub(spines[i + 1], spines[i]);
                        norm(y[i]);
                    }
                    catch (ArithmeticException ae1) {
                        if (hardDebug) {
                            System.out.println(ae1 + " " + y[i]);
                        }
                        // spines[i+1] equaled spines[i]
                        try {
                            y[i].sub(spines[i], spines[i - 1]);
                            norm(y[i]);
                        }
                        catch (ArithmeticException ae2) {
                            if (hardDebug) {
                                System.out.println(ae2 + " " + y[i]);
                            }
                            // spines[i] equaled spines[i-1]
                            // real bad case, do something
                            int w = i + 2;
                            while ((w < last + 1) && (spines[i - 1].equals(spines[w]))) {
                                w++;
                            }
                            if (w < last + 1) {
                                y[i].sub(spines[w], spines[i - 1]);
                                if (hardDebug) {
                                    System.out.println("did something " + y[i]);
                                }
                                norm(y[i]);// should never divide by zero here
                            }
                            else {// worst worst case
                                if (hardDebug) {
                                    System.out.println("worst worst y " + y[i]);
                                }
                                y[i] = new Vector3f(0.0f, 1.0f, 0.0f);
                            }
                        }
                    }
                }
            }
            // y for ends
            if (closed) {
                // closed and not collinear -> not all one point
                y[0] = new Vector3f();
                y[0].sub(spines[1], spines[last - 1]);
                try {
                    norm(y[0]);
                }
                catch (ArithmeticException ae) {
                    // bad case that the spine[n-2] == spine[1]
                    int w = last - 2;
                    while ((w > 1) && (spines[1].equals(spines[w]))) {
                        w--;
                    }
                    if (w > 1) {
                        y[0].sub(spines[1], spines[w]);
                        norm(y[0]);// should never divide by zero here
                    }
                    else {
                        // how did this happen?
                        y[0].set(0.0f, 0.0f, 1.0f);
                    }
                }
                y[last] = new Vector3f(y[0]);
            }
            else {
                y[0] = new Vector3f();
                y[last] = new Vector3f();
                y[0].sub(spines[1], spines[0]);
                try {
                    norm(y[0]);
                }
                catch (ArithmeticException ae) {
                    int w = 2;
                    while ((w < last) && (spines[0].equals(spines[w]))) {
                        w++;
                    }
                    if (w < last) {
                        y[0].sub(spines[w], spines[0]);
                        norm(y[0]);// should not divide by zero here
                    }
                    else {
                        y[0].set(0.0f, 0.0f, 1.0f);
                    }
                }
                y[last] = new Vector3f();
                y[last].sub(spines[last], spines[last - 1]);
                try {
                    norm(y[last]);
                }
                catch (ArithmeticException ae) {
                    int w = last - 2;
                    while ((w > -1) && (spines[last].equals(spines[w]))) {
                        w--;
                    }
                    if (w > -1) {
                        y[last].sub(spines[last], spines[w]);
                        norm(y[last]);
                    }
                    else {
                        y[last].set(0.0f, 0.0f, 1.0f);
                    }
                }
            }
            // now z axis for each spine
            // first all except first and last

            boolean recheck = false;
            for (int i = 1; i < last; i++) {
                u = new Vector3f();
                v = new Vector3f();
                z[i] = new Vector3f();
                u.sub(spines[i - 1], spines[i]);
                v.sub(spines[i + 1], spines[i]);
                // spec seems backwards on u and v
                // shouldn't it be z[i].cross(u,v)???
                //z[i].cross(v,u);
                z[i].cross(u, v);
                try {
                    norm(z[i]);
                }
                catch (ArithmeticException ae) {
                    recheck = true;
                }
            }
            if (closed) {
                z[0] = z[last] = new Vector3f();
                u = new Vector3f();
                v = new Vector3f();
                u.sub(spines[last - 1], spines[0]);
                v.sub(spines[1], spines[0]);
                try {
                    z[0].cross(u, v);
                }
                catch (ArithmeticException ae) {
                    recheck = true;
                }
            }
            else {// not closed
                z[0] = new Vector3f(z[1]);
                z[last] = new Vector3f(z[last - 1]);
            }
            if (recheck) {// found adjacent collinear spines
                // first z has no length ?
                if (hardDebug) {
                    System.out.println("rechecking, found adjacent collinear spines");
                }
                if (z[0].dot(z[0]) == 0.0f) {
                    for (int i = 1; i < spines.length; i++) {
                        if (z[i].dot(z[i]) > 0.0f) {
                            z[0] = new Vector3f(z[i]);
                        }
                    }
                    // test again could be most degenerate of cases
                    if (z[0].dot(z[0]) == 0.0f) {
                        z[0] = new Vector3f(0.0f, 0.0f, 1.0f);
                    }
                }

                // check rest of z's
                for (int i = 1; i < last + 1; i++) {
                    if (z[i].dot(z[i]) == 0.0f) {
                        z[i] = new Vector3f(z[i - 1]);
                    }
                }
            }
            // finally, do a neighbor comparison
            // and evaluate the x's
            for (int i = 0; i < spines.length; i++) {
                if (i > 0) {
                    if (z[i].dot(z[i - 1]) < 0.0f) {
                        z[i].negate();
                    }
                }

                // at this point, y and z should be nice
                x[i] = new Vector3f();
                x[i].cross(z[i], y[i]);
                try {
                    norm(x[i]);
                }
                catch (ArithmeticException ae) {
                    // this should not happen
                    ae.printStackTrace();
                }
                if (hardDebug) {
                    System.out.println("x[" + i + "] " + x[i]);
                }
            }

        }

        // should now have orthonormal vectors for each
        // spine. create the rotation matrix with scale for
        // each spine. spec is unclear whether a twist imparted
        // at one of the spines is inherited by its "children"
        // so assume not.

        // also, the order looks like SxTxRscpxRo , ie ,
        // the spec doc looks suspect, double check

        Matrix3f m = new Matrix3f();
        transforms = new Transform3D[spines.length];

        for (int i = 0; i < spines.length; i++) {
            rotations[i] = new Matrix3f();
            if (hardDebug) {
                System.out.println("orthos " +
                        i + " " + x[i] + " " + y[i] + " " + z[i] + " " + orientations[i]);
            }
            rotations[i].setRow(0, x[i]);
            rotations[i].setRow(1, y[i]);
            rotations[i].setRow(2, z[i]);
            m.set(orientations[i]);
            rotations[i].mul(m);
            transforms[i] = new Transform3D();
            transforms[i].setScale(new Vector3d(scales[i]));
            transforms[i].setTranslation(new Vector3d(spines[i]));
            transforms[i].setRotation(rotations[i]);
        }
    }


    // create a list of unique coords ( of Point3f )
    // by applying the transforms to the crossSectionPts
    /**  Description of the Method */
    void createExtrusion() {

        coords = new Point3f[spines.length * crossSectionPts.length];

        for (int i = 0; i < spines.length; i++) {
            for (int j = 0; j < crossSectionPts.length; j++) {
                int ind = i * (crossSectionPts.length) + j;
                coords[ind] = new Point3f(crossSectionPts[j]);
                transforms[i].transform(coords[ind]);
            }
        }
    }

    // wind the coords with indexed connectivity and create
    // stripCounts see page 47 of small bluebook
    /**  Description of the Method */
    void createIndices() {
        int m = 0;// coordIndex length
        int k = crossSectionPts.length;
        int l = coords.length;
        int s = 0;
        int n = 0;// coordIndex count
        if (endCap.value) {
            m += k - 1;
            s++;
        }
        if (beginCap.value) {
            m += k - 1;
            s++;
        }
        m += (spines.length - 1) * (4 * (k - 1));
        coordIndex = new int[m];
        if (hardDebug) {
            System.out.println("coordIndexSize" + m);
        }
        stripCounts = new int[s + (spines.length - 1) * (k - 1)];
        s = 0;
        if (hardDebug) {
            System.out.println("stripCounts.length" + stripCounts.length);
        }
        if (hardDebug) {
            System.out.println("spines.length" + spines.length);
        }
        // start with extrusion body from bottom
        for (int i = 0; i < spines.length - 1; i++) {
            if (hardDebug) {
                System.out.println(" i " + i);
            }
            for (int j = 0; j < k - 1; j++) {
                if (hardDebug) {
                    System.out.println(" j " + j);
                }
                // create a quad
                if (ccw.value) {
                    if (hardDebug) {
                        System.out.println("i " + i + " j " + j + " k " + k);
                    }
                    coordIndex[n++] = (i * k) + j;
                    if (hardDebug) {
                        System.out.println((n - 1) + " " + ((i * k) + (j)));
                    }
                    coordIndex[n++] = (i * k) + j + 1;
                    if (hardDebug) {
                        System.out.println((n - 1) + " " + ((i * k) + (j + 1)));
                    }
                    coordIndex[n++] = ((i + 1) * k) + j + 1;
                    if (hardDebug) {
                        System.out.println((n - 1) + " " + (((i + 1) * k) + (j + 1)));
                    }
                    coordIndex[n++] = ((i + 1) * k) + j;
                    if (hardDebug) {
                        System.out.println((n - 1) + " " + (((i + 1) * k) + (j)));
                    }
                }
                else {
                    coordIndex[n++] = (i * k) + j;
                    coordIndex[n++] = ((i + 1) * k) + j;
                    coordIndex[n++] = ((i + 1) * k) + j + 1;
                    coordIndex[n++] = (i * k) + j + 1;
                }
                stripCounts[s++] = 4;
                numTris += 2;
            }
        }
        // add top and bottom
        // note: when switching cw from ccw notice that
        // the index is off by one, this is ok since there
        // is one extra point in the cross-section, each
        // cap has 2 ways to be drawn
        // also note top and bottom caps are reverse oriented to
        // each other
        if (beginCap.value && endCap.value) {
            int indB = m - (2 * (k - 1));
            int indE = m - (k - 1);
            if (!ccw.value) {
                for (int i = 0; i < k - 1; i++) {
                    coordIndex[indB++] = i;
                }

                for (int i = l - 1; i > l - k; i--) {
                    coordIndex[indE++] = i;
                }

            }
            else {
                for (int i = k - 1; i > 0; i--) {
                    coordIndex[indB++] = i;
                }
                for (int i = 0; i < k - 1; i++) {
                    coordIndex[indE++] = l - (k - 1) + i;
                }
            }
            stripCounts[s++] = k - 1;
            stripCounts[s++] = k - 1;
            numTris += k - 1;// best guess what gi did?
        }
        else if (beginCap.value) {
            int ind = m - (k - 1);
            if (!ccw.value) {
                for (int i = 0; i < k - 1; i++) {
                    coordIndex[ind++] = i;
                }
            }
            else {// this is ok since extra x-sectpt give off by one
                for (int i = k - 1; i > 0; i--) {
                    coordIndex[ind++] = i;
                }
            }
            stripCounts[s++] = k - 1;
            numTris += k - 1;
        }
        else if (endCap.value) {
            int ind = m - (k - 1);
            if (ccw.value) {
                for (int i = l - (k - 1); i < l; i++) {
                    coordIndex[ind++] = i;
                }
            }
            else {
                for (int i = l - 1; i > l - k; i--) {
                    coordIndex[ind++] = i;
                }
            }
            stripCounts[s++] = k - 1;
            numTris += k - 1;
        }
    }

    // the vecmath package was not throwing ArithmeticExceptions as
    // expected from the normalize() method.
    /**
     *  Description of the Method
     *
     *@param  n Description of the Parameter
     */
    void norm(Vector3f n) {
        float norml = (float) Math.sqrt(n.x * n.x + n.y * n.y + n.z * n.z);
        if (norml == 0.0f) {
            throw new ArithmeticException();
        }

        n.x /= norml;
        n.y /= norml;
        n.z /= norml;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (!eventInName.startsWith("route_")) {
            initImpl();
            ((Shape3D) (owner.implNode)).setGeometry(impl);
        }
        else {
            ((Shape3D) (owner.implNode)).setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        }
    }

    /**  Description of the Method */
    void initFields() {

        beginCap.init(this, FieldSpec, Field.FIELD, "beginCap");
        ccw.init(this, FieldSpec, Field.FIELD, "ccw");
        convex.init(this, FieldSpec, Field.FIELD, "convex");
        creaseAngle.init(this, FieldSpec, Field.FIELD, "creaseAngle");
        crossSection.init(this, FieldSpec, Field.EVENT_IN, "crossSection");
        endCap.init(this, FieldSpec, Field.FIELD, "endCap");
        orientation.init(this, FieldSpec, Field.EVENT_IN, "orientation");
        scale.init(this, FieldSpec, Field.EVENT_IN, "scale");
        solid.init(this, FieldSpec, Field.FIELD, "solid");
        spine.init(this, FieldSpec, Field.EVENT_IN, "spine");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Extrusion(loader, (SFBool) beginCap.clone(), (SFBool) ccw.clone(),
                (SFBool) convex.clone(), (SFFloat) creaseAngle.clone(), (MFVec2f) crossSection.clone(),
                (SFBool) endCap.clone(), (MFRotation) orientation.clone(), (MFVec2f) scale.clone(),
                (SFBool) solid.clone(), (MFVec3f) spine.clone());
    }

    /**
     *  Gets the type attribute of the Extrusion object
     *
     *@return  The type value
     */
    public String getType() {
        return "Extrusion";
    }

    /**
     *  Gets the solid attribute of the Extrusion object
     *
     *@return  The solid value
     */
    public boolean getSolid() {
        return solid.value;
    }

    /**
     *  Sets the owner attribute of the Extrusion object
     *
     *@param  owner The new owner value
     */
    public void setOwner(Shape owner) {
        this.owner = owner;
    }
}

