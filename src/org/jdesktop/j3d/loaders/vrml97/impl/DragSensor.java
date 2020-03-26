/*
 * $RCSfile: DragSensor.java,v $
 *
 *      @(#)DragSensor.java 1.15 99/03/10 18:02:47
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

import java.util.Vector;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.*;

// abstract base class for all drag sensors
/**  Description of the Class */
public abstract class DragSensor extends Node implements VrmlSensor {

    // exposedField
    SFBool enabled;
    SFBool autoOffset;

    // Event outs
    SFBool isActive;
    SFVec3f trackPoint;

    // Scene graph reference
    org.jogamp.java3d.Node parent;

    static double EPSILON = .00000001;
    static double DELTA = .00001;


    /**
     *Constructor for the DragSensor object
     *
     *@param  loader Description of the Parameter
     */
    public DragSensor(Loader loader) {
        super(loader);
        enabled = new SFBool(true);
        autoOffset = new SFBool(true);
        isActive = new SFBool(true);
        trackPoint = new SFVec3f(0.0f, 0.0f, 0.0f);
        initFields();
    }

    /**
     *Constructor for the DragSensor object
     *
     *@param  loader Description of the Parameter
     *@param  enabled Description of the Parameter
     */
    DragSensor(Loader loader, SFBool enabled) {
        super(loader);
        this.enabled = enabled;
        isActive = new SFBool(true);
        initFields();
    }

    /**
     *  Description of the Method
     *
     *@param  parentImpl Description of the Parameter
     */
    void updateParent(org.jogamp.java3d.Node parentImpl) {
        Vector v = (Vector) (parentImpl.getUserData());
        if (v == null) {
            v = new Vector();
            parentImpl.setUserData(v);
            if (loader.debug) {
                System.out.println("Drag Sensor parent: "
                         + parentImpl + " had no user data, added vector:" + v);
            }
        }
        v.addElement(this);
        // in case GroupBase did not hit this parent
        parentImpl.setCapability(org.jogamp.java3d.Node.ENABLE_PICK_REPORTING);
        parentImpl.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
        parentImpl.setCapability(org.jogamp.java3d.Node.ALLOW_LOCAL_TO_VWORLD_READ);
        // bug: j3d: compiles away parentImpl losing pick info
        parentImpl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_READ);
        parentImpl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_WRITE);
        parentImpl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_EXTEND);
        if (parentImpl instanceof org.jogamp.java3d.TransformGroup) {
            parentImpl.setCapability(org.jogamp.java3d.TransformGroup.ALLOW_TRANSFORM_READ);
        }
        parentImpl.setPickable(true);
        parent = parentImpl;
    }

    /**  Description of the Method */
    void initFields() {
        enabled.init(this, FieldSpec, Field.EXPOSED_FIELD, "enabled");
        autoOffset.init(this, FieldSpec, Field.EXPOSED_FIELD, "autoOffset");
        isActive.init(this, FieldSpec, Field.EVENT_OUT, "isActive");
        trackPoint.init(this, FieldSpec, Field.EVENT_OUT, "trackPoint");
    }

    /**  Description of the Method */
    abstract void offset();

    /**
     *  Description of the Method
     *
     *@param  t Description of the Parameter
     */
    abstract void simTick(double t);

    // Given the transformed pixel , the next pixel (if any), the node under the
    // pick group, and the unique path incase of shared link , do something.
    /**
     *  Description of the Method
     *
     *@param  p1 Description of the Parameter
     *@param  p2 Description of the Parameter
     *@param  node Description of the Parameter
     *@param  unique Description of the Parameter
     */
    abstract void update(Point3d p1, Point3d p2, org.jogamp.java3d.Node node,
            org.jogamp.java3d.SceneGraphPath unique);

    // The following are adaptations of vecmath related methods, which have
    // improved safety nets.

    /**
     *  Description of the Method
     *
     *@param  n Description of the Parameter
     *@exception  ArithmeticException Description of the Exception
     */
    static void norm(Vector3d n) throws ArithmeticException {
        double norml = (float) Math.sqrt(n.x * n.x + n.y * n.y + n.z * n.z);
        if (norml == 0.0) {
            throw new ArithmeticException();
        }

        n.x /= norml;
        n.y /= norml;
        n.z /= norml;
    }

    /**
     *  Description of the Method
     *
     *@param  t Description of the Parameter
     *@param  u Description of the Parameter
     *@return  Description of the Return Value
     */
    static double angle(Vector3d t, Vector3d u) {
        double l1 = length(t);
        double l2 = length(u);
        double a;
        if (l1 == 0.0 || l2 == 0.0) {
            return 0.0;
        }
        // acos is returning a NAN
        else {
            a = Math.acos(dot(t, u) / (l1 * l2));
        }
        if (a < 0.0 || a > 0.0) {
            return a;
        }
        else {
            return EPSILON;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  v Description of the Parameter
     *@return  Description of the Return Value
     */
    static double length(Vector3d v) {
        double l = Math.sqrt(dot(v, v));
        return l;
    }

    /**
     *  Description of the Method
     *
     *@param  v Description of the Parameter
     *@param  u Description of the Parameter
     *@return  Description of the Return Value
     */
    static double dot(Vector3d v, Vector3d u) {
        return (v.x * u.x + v.y * u.y + v.z * u.z);
    }


    // this function will corect for the Alice in Wonderland
    // effect.  No, not the white mice talking backwards...
    /**
     *  Description of the Method
     *
     *@param  tr Description of the Parameter
     *@return  Description of the Return Value
     */
    static double coorelate(Transform3D tr) {
        double c = 0.0;
        double COORELATION_FACTOR = 10.0;
        if (c == 0.0) {
            Point3d p1 = new Point3d(0.0, 0.0, 0.0);
            Point3d p2 = new Point3d(0.0 + DELTA, 0.0, 0.0);

            tr.transform(p1);
            tr.transform(p2);

            Vector3d v1 = new Vector3d(p1);
            Vector3d v2 = new Vector3d(p2);

            norm(v1);
            norm(v2);

            // increase or decrease COORELATION_FACTOR
            // to taste
            c = COORELATION_FACTOR * DELTA / angle(v1, v2);
        }

        return c;
    }

}

