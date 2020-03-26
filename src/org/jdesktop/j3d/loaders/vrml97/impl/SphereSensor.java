/*
 * $RCSfile: SphereSensor.java,v $
 *
 *      @(#)SphereSensor.java 1.27 99/03/01 14:30:43
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
 * $Date: 2005/02/03 23:07:02 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Bounds;

import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.*;

/**  Description of the Class */
public class SphereSensor extends DragSensor {

    // exposedField
    SFRotation offset;
    // experimental
    SFBool autoSpin;
    SFInt32 autoSpinFrameWait;
    SFFloat spinKick;

    // eventOut
    SFRotation rotation;

    // api math objs  ( gets from j3d )
    Transform3D l2vwTf = new Transform3D();// local to vworld
    Transform3D im2vwTf = new Transform3D();// image plate to vworld
    Transform3D TTf = new Transform3D();// the xform in question
    //  obtained orig
    //  from getLocalToVworld()
    //  called on the node

    // vworld math
    Quat4d T = new Quat4d();
    Quat4d T_;// don't set until first used, set
    // null if ever unrouted.
    Quat4d rot = new Quat4d();
    Transform3D im2lTf = new Transform3D();
    Transform3D l2imTf = new Transform3D();
    Transform3D vw2lTf = new Transform3D();

    Point3d p1 = new Point3d();
    Point3d p2 = new Point3d();

    // track ball math
    Transform3D rotTf = new Transform3D();
    AxisAngle4d sfrOffset = new AxisAngle4d();
    AxisAngle4d sfrR = new AxisAngle4d();
    AxisAngle4d rotAxAngle = new AxisAngle4d();
    Vector3d trackpt = new Vector3d();
    Vector3d v = new Vector3d();
    Vector3d vlast = new Vector3d();
    Vector3d t = new Vector3d();
    Vector3d u = new Vector3d();
    double a;
    Bounds bounds;
    int count = 0;
    boolean autoSpinning = false;
    static double EPSILON = .00000001;
    static double DELTA = .00001;

    // node references
    org.jogamp.java3d.Node node = null;
    org.jogamp.java3d.SceneGraphPath path = null;// could be a shared node


    /**
     *Constructor for the SphereSensor object
     *
     *@param  loader Description of the Parameter
     */
    public SphereSensor(Loader loader) {
        super(loader);
        offset = new SFRotation(0.0f, 1.0f, 0.0f, 0.0f);
        rotation = new SFRotation(0.0f, 1.0f, 0.0f, 0.0f);
        sfrOffset.set(offset.rot[0], offset.rot[1], offset.rot[2], offset.rot[3]);
        sfrR.set(rotation.rot[0], rotation.rot[1], rotation.rot[2], rotation.rot[3]);
        autoSpin = new SFBool(false);
        autoSpinFrameWait = new SFInt32(3);
        spinKick = new SFFloat(10.0f);
        vlast.x = 0.0;
        vlast.y = 1.0;
        vlast.z = 0.0;
        initSphereSensorFields();
    }

    // P1 and P2 are in image plate coordinates and represent the beginning
    // of the first drag segment and end of the current drag segment, where
    // a drag segment is defined to be the multiple connected line segments
    // that make up a complete AWT (or whathaveu)
    // mousePressed->mouseDragged->mouseReleased action.
    // nodeIn is supposed to be a TransformGroup(), and it really might not
    // make sense if the receiver of the route is not in the same space.
    // However, there may be occaisions that the node is not the TrasformGroup,
    // in which case it is safe to assume that the offset is I.

    /**
     *  Description of the Method
     *
     *@param  p1 Description of the Parameter
     *@param  p2 Description of the Parameter
     *@param  nodeIn Description of the Parameter
     *@param  pathIn Description of the Parameter
     */
    void update(Point3d p1, Point3d p2, org.jogamp.java3d.Node nodeIn,
            org.jogamp.java3d.SceneGraphPath pathIn) {

        double ang;

        double m;
        this.p1 = p1;
        this.p2 = p2;

        count = autoSpinFrameWait.value;// count default 3 frames before autoSpin

        // A null path and node means this was expected to have been already set.
        // not null does not mean that this was the first time to use this sensor,
        // but the first time during this continuous mouse drag

        if ((nodeIn != null) && (pathIn != null)) {
            this.node = nodeIn;
            this.path = pathIn;

            if (browser.debug) {
                // send out the whole path, including the
                // Locale and "Object"
                System.out.println(pathIn);
                int xx;
                System.out.println(pathIn.getLocale());
                for (xx = 0; xx < pathIn.nodeCount(); xx++) {
                    System.out.println(pathIn.getNode(xx));
                }
                System.out.println(pathIn.getObject());
                System.out.println("picked node" + node);
            }
            aquireTransform(nodeIn);

            // send the vrml event that this sensor is active
            isActive.setValue(true);

            bounds = node.getBounds();

        }
        else {

            // already in a drag, check if currently autoSpinning, if so
            // set the offset as if a mouse up happend

            if (autoSpinning) {

                // unlock the sensor
                browser.evagation.forceUpDown();
                aquireTransform(this.node);
                doUpdate();
                offset();

                // place this back
                browser.evagation.curDragSensor = this;
                autoSpinning = false;

            }
        }
        browser.canvas.getImagePlateToVworld(im2vwTf);

        try {
            node.getLocalToVworld(l2vwTf);
        }
        catch (org.jogamp.java3d.IllegalSharingException e) {
            node.getLocalToVworld(path, l2vwTf);
        }
        catch (java.lang.NullPointerException npe) {
            npe.printStackTrace();
            // todo:why did sphere generate this, but it still works?
        }
        catch (org.jogamp.java3d.CapabilityNotSetException cnse) {
            /*cnse.printStackTrace()*/
            ;
        }

        // this is the local to vworld of the group, need the children
        //
        vw2lTf.invert(l2vwTf);
        im2lTf.mul(vw2lTf, im2vwTf);
        im2lTf.transform(p1);
        im2lTf.transform(p2);
        t.set(p1);
        u.set(p2);
        try {
            norm(t);
            norm(u);
            v.cross(t, u);
            a = angle(t, u) * coorelate(im2lTf);
            if (Math.abs(a) > EPSILON) {
                norm(v);
                vlast.set(v);
            }
            else {
                v.set(vlast);
            }
        }
        catch (ArithmeticException ae) {
            System.out.println("dinky delta");
        }

        rotAxAngle.set(v.x, v.y, v.z, a);
        rot.set(rotAxAngle);
        T_.mul(rot, T);
        //it'd be nice to have javac or the makefile use cpp
        //if (browser.debug) {
        //System.out.println("------------");
        //System.out.println("P1 "+t);
        //System.out.println("P2 "+u);
        //System.out.println("P1xP2 "+v);
        //System.out.println("x "+v.x+" y "+v.y+" z "+v.z+" a "+a);
        //System.out.println("rot "+rot);
        //System.out.println("T "+T);
        //System.out.println("rot mul T "+T_);
        //System.out.println("------------");
        //}
        doUpdate();
    }

    // this is an experimental feature, to enable, uncomment the
    // simTick call to curDragSensors in Evagation.
    // its relatively light weight since only once dragSensor can
    // be active at anyone time
    /**
     *  Description of the Method
     *
     *@param  t Description of the Parameter
     */
    void simTick(double t) {
        if (autoSpin.value && (count-- <= 0)) {
            autoSpinning = true;
            rotAxAngle.set(v.x, v.y, v.z, rotAxAngle.angle + a);
            rot.set(rotAxAngle);
            T_.mul(rot, T);
            doUpdate();
        }
        else {
            autoSpinning = false;
        }
    }

    /**
     *  Description of the Method
     *
     *@param  nodeIn Description of the Parameter
     */
    void aquireTransform(org.jogamp.java3d.Node nodeIn) {
        if (T_ == null) {
            if (nodeIn instanceof org.jogamp.java3d.TransformGroup) {
                ((org.jogamp.java3d.TransformGroup) nodeIn).getTransform(TTf);
            }
            else {
                TTf.setIdentity();
            }
            T_ = new Quat4d();
            TTf.get(T);
            T_.set(T);
        }
        else {
            // update has been called once for this drag
            T_.get(T);
        }
    }

    /**  Description of the Method */
    void doUpdate() {

        if (enabled.value) {
            sfrR.set(T_);
            rotation.setValue((float) sfrR.x, (float) sfrR.y,
                    (float) sfrR.z, (float) sfrR.angle);

            // trackPoint is the origninal P2 in local coordinates
            // componentwise divided by the distance to the origin, multiplied by distance
            // of initial intersection to the origin; this can be obtained by rigorous
            // geometry intersection, or just take the boundingSphere radius );

            trackpt.set(p2);
            try {
                norm(trackpt);
            }
            catch (ArithmeticException ae) {
                ;
            }
            trackpt.scale(((BoundingSphere) bounds).getRadius());

            // inherited from DragSensor
            trackPoint.setValue((float) trackpt.x, (float) trackpt.y, (float) trackpt.z);
        }
    }


    // call this at the end of a mouse drag sequence (mouse up event)
    // to save the current transformation.
    /**  Description of the Method */
    public void offset() {
        // complete the vrml events,
        isActive.setValue(false);

        AxisAngle4d sfrO = new AxisAngle4d();
        sfrO.set(T_);

        offset.setValue((float) sfrO.x, (float) sfrO.y, (float) sfrO.z, (float) sfrO.angle);
        count = 0;

    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("autoSpin")) {
            if (autoSpinning) {
                autoSpinning = false;
            }
        }
    }

    /**
     *  Gets the type attribute of the SphereSensor object
     *
     *@return  The type value
     */
    public String getType() {
        return "SphereSensor";
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new SphereSensor(loader);
    }

    /**  Description of the Method */
    void initSphereSensorFields() {
        offset.init(this, FieldSpec, Field.EXPOSED_FIELD, "offset");
        rotation.init(this, FieldSpec, Field.EVENT_OUT, "rotation");
        autoSpin.init(this, FieldSpec, Field.FIELD, "autoSpin");
        autoSpinFrameWait.init(this, FieldSpec, Field.FIELD, "autoSpinFrameWait");
        spinKick.init(this, FieldSpec, Field.FIELD, "spinKick");

    }

}

