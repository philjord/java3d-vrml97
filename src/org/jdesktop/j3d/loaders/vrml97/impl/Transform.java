/*
 * $RCSfile: Transform.java,v $
 *
 *      @(#)Transform.java 1.57 99/03/15 10:26:40
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
 * $Date: 2005/02/03 23:07:03 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Vector;
import org.jogamp.java3d.BoundingSphere;

import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

/**  Description of the Class */
public class Transform extends GroupBase {

    TransformGroup impl;

    SFVec3f center;
    SFRotation rotation;
    SFVec3f scale;
    SFRotation scaleOrientation;
    SFVec3f translation;

    private Transform3D trans1;
    private Transform3D trans2;
    private Transform3D trans3;
    private Transform3D T;
    private Transform3D C;
    private Transform3D R;
    private Transform3D SR;
    private Transform3D S;
    private Transform3D P;
    private boolean pending = false;
    private Vector3d tempVec;
    private AxisAngle4d tempAxis;

    /**
     *Constructor for the Transform object
     *
     *@param  loader Description of the Parameter
     */
    public Transform(Loader loader) {
        super(loader);
        center = new SFVec3f(0.0f, 0.0f, 0.0f);
        rotation = new SFRotation();
        scale = new SFVec3f(1.0f, 1.0f, 1.0f);
        scaleOrientation = new SFRotation();
        translation = new SFVec3f(0.0f, 0.0f, 0.0f);

        initTransformFields();
    }

    /**
     *Constructor for the Transform object
     *
     *@param  loader Description of the Parameter
     *@param  children Description of the Parameter
     *@param  bboxCenter Description of the Parameter
     *@param  bboxSize Description of the Parameter
     *@param  center Description of the Parameter
     *@param  rotation Description of the Parameter
     *@param  scale Description of the Parameter
     *@param  scaleOrientation Description of the Parameter
     *@param  translation Description of the Parameter
     */
    Transform(Loader loader, MFNode children, SFVec3f bboxCenter,
            SFVec3f bboxSize, SFVec3f center, SFRotation rotation, SFVec3f scale,
            SFRotation scaleOrientation, SFVec3f translation) {

        super(loader, children, bboxCenter, bboxSize);

        this.center = center;
        this.rotation = rotation;
        this.scale = scale;
        this.scaleOrientation = scaleOrientation;
        this.translation = translation;

        initTransformFields();
    }

    /**
     *  Description of the Method
     *
     *@param  val1 Description of the Parameter
     *@param  val2 Description of the Parameter
     *@return  Description of the Return Value
     */
    private boolean floatEq(float val1, float val2) {
        float diff = val1 - val2;
        if (diff < 0) {
            diff *= -1;
        }
        if (diff < 0.001) {
            return true;
        }
        else {
            return false;
        }
    }

    /**  Description of the Method */
    void updateTransform() {
//System.out.println(this);
        // T x C x R x SR x S x -SR x -C
        if ((browser == null) || browser.pendingTransforms.batchReady) {
            tempVec.x = -center.value[0];
            tempVec.y = -center.value[1];
            tempVec.z = -center.value[2];
            trans2.setIdentity();
            trans2.setTranslation(tempVec);
//System.out.println("-C "+trans2);
            float scaleVal = 1.0f;
            if (floatEq(scale.value[0], scale.value[1]) &&
                    floatEq(scale.value[0], scale.value[2])) {
                scaleVal = scale.value[0];
                trans1.set(scaleVal);
//System.out.println("S"+trans1);
            }
            else {
                // non-uniform scale
//System.out.println("Non Uniform Scale");
                tempAxis.x = scaleOrientation.rot[0];
                tempAxis.y = scaleOrientation.rot[1];
                tempAxis.z = scaleOrientation.rot[2];
                tempAxis.angle = -scaleOrientation.rot[3];
                double tempAxisNormalizer = Math.sqrt(
                        tempAxis.x * tempAxis.x +
                        tempAxis.y * tempAxis.y +
                        tempAxis.z * tempAxis.z);
                tempAxis.x /= tempAxisNormalizer;
                tempAxis.y /= tempAxisNormalizer;
                tempAxis.z /= tempAxisNormalizer;

                trans1.set(tempAxis);
                trans3.mul(trans1, trans2);
                trans1.setNonUniformScale(scale.value[0], scale.value[1],
                        scale.value[2]);
                trans2.mul(trans1, trans3);
                tempAxis.x = scaleOrientation.rot[0];
                tempAxis.y = scaleOrientation.rot[1];
                tempAxis.z = scaleOrientation.rot[2];
                tempAxis.angle = scaleOrientation.rot[3];
                trans1.set(tempAxis);
            }
            trans3.mul(trans1, trans2);
//System.out.println("Sx-C"+trans3);
            float magSq = (rotation.rot[0] * rotation.rot[0] +
                    rotation.rot[1] * rotation.rot[1] +
                    rotation.rot[2] * rotation.rot[2]);
            if (magSq < 0.0001) {
                // all zeros, use the default
                // ?? does this still happen
                tempAxis.x = 0.0;
                tempAxis.y = 0.0;
                //tempAxis.y = 1.0;
                tempAxis.z = 0.0;
            }
            else {
                if ((magSq > 1.01) || (magSq < 0.99)) {
                    float mag = (float) Math.sqrt((double) magSq);
                    tempAxis.x = rotation.rot[0] / mag;
                    tempAxis.y = rotation.rot[1] / mag;
                    tempAxis.z = rotation.rot[2] / mag;
                }
                else {
                    tempAxis.x = rotation.rot[0];
                    tempAxis.y = rotation.rot[1];
                    tempAxis.z = rotation.rot[2];
                }
            }
            tempAxis.angle = rotation.rot[3];
            trans1.set(tempAxis);
//System.out.println("R"+trans1);
            trans2.mul(trans1, trans3);
//System.out.println("RxSx-C"+trans2);
            tempVec.x = center.value[0];
            tempVec.y = center.value[1];
            tempVec.z = center.value[2];
            trans1.setIdentity();
            trans1.setTranslation(tempVec);
//System.out.println("C"+trans1);
            //trans1.set(1.0, tempVec);
            trans3.mul(trans1, trans2);
//System.out.println("CxRxSx-C"+trans3);
            tempVec.x = translation.value[0];
            tempVec.y = translation.value[1];
            tempVec.z = translation.value[2];
            trans1.setIdentity();
            trans1.setTranslation(tempVec);
            //trans1.set(1.0, tempVec);
            trans2.mul(trans1, trans3);
//System.out.println("TxCxRxSx-C"+trans2);

            //trans2.normalize();

            //if(browser.debug)System.out.println(trans2+" "+trans2.getType());

            try {
                impl.setTransform(trans2);
            }
            catch (org.jogamp.java3d.BadTransformException bte) {
                if (browser.debug) {
                    bte.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            pending = false;
        }
        else {
            if (pending == false) {
                pending = true;
                browser.pendingTransforms.add(this);
            }
        }
    }

    /**  Description of the Method */
    void initImpl() {
        impl = new TransformGroup();
        implGroup = (org.jogamp.java3d.Group) impl;
        implNode = impl;
        impl.setUserData(new Vector());
        trans1 = new Transform3D();
        trans2 = new Transform3D();
        trans3 = new Transform3D();
        T = new Transform3D();
        C = new Transform3D();
        R = new Transform3D();
        SR = new Transform3D();
        S = new Transform3D();
        P = new Transform3D();
        tempVec = new Vector3d();
        tempAxis = new AxisAngle4d();

        // make sure the rotation axis magnitude is non zero
        if ((rotation.rot[0] == rotation.rot[1]) &&
                (rotation.rot[1] == rotation.rot[2]) &&
                (rotation.rot[2] == 0.0f)) {
            rotation.rot[1] = 1.0f;
        }

        // and the scale?
        if ((scale.value[0] == scale.value[1]) &&
                (scale.value[0] == scale.value[2]) &&
                (scale.value[0] == 0.0f)) {
            scale.setValue(1.0f, 1.0f, 1.0f);
        }

        updateTransform();
        super.replaceChildren();// init the implGroup for clone()
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("rotation")) {
            updateTransform();
        }
        else if (eventInName.equals("scale") ||
                eventInName.equals("scaleOrientation") ||
                eventInName.equals("center") ||
                eventInName.equals("translation")) {
            updateTransform();
        }
        else if (eventInName.equals("route_rotation") ||
                eventInName.equals("route_translation") ||
                eventInName.equals("route_scale") ||
                eventInName.equals("route_center") ||
                eventInName.equals("route_scaleOrientation")) {
            impl.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            impl.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            impl.setCapability(org.jogamp.java3d.Group.ENABLE_PICK_REPORTING);
        }
        else {
            super.notifyMethod(eventInName, time);
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        if (loader.debug) {
            System.out.println("Transform.clone() called");
        }
        return new Transform(loader,
                (MFNode) children.clone(),
                (SFVec3f) bboxCenter.clone(),
                (SFVec3f) bboxSize.clone(),
                (SFVec3f) center.clone(),
                (SFRotation) rotation.clone(),
                (SFVec3f) scale.clone(),
                (SFRotation) scaleOrientation.clone(),
                (SFVec3f) translation.clone());
    }

    /**
     *  Gets the type attribute of the Transform object
     *
     *@return  The type value
     */
    public String getType() {
        return "Transform";
    }

    /**  Description of the Method */
    void initFields() {
        super.initFields();
        initTransformFields();
    }

    /**  Description of the Method */
    void initTransformFields() {
        center.init(this, FieldSpec, Field.EXPOSED_FIELD, "center");
        rotation.init(this, FieldSpec, Field.EXPOSED_FIELD, "rotation");
        scale.init(this, FieldSpec, Field.EXPOSED_FIELD, "scale");
        scaleOrientation.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "scaleOrientation");
        translation.init(this, FieldSpec, Field.EXPOSED_FIELD, "translation");
    }


    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toStringBodyS() {
        String retval = "Transform {\n";
        if ((center.value[0] != 0.0) ||
                (center.value[1] != 0.0) ||
                (center.value[2] != 0.0)) {
            retval += "center " + center;
        }
        if ((rotation.rot[0] != 0.0) ||
                (rotation.rot[1] != 0.0) ||
                (rotation.rot[2] != 1.0) ||
                (rotation.rot[3] != 0.0)) {
            retval += "rotation " + rotation;
        }
        if ((scale.value[0] != 1.0) ||
                (scale.value[1] != 1.0) ||
                (scale.value[2] != 1.0)) {
            retval += "scale " + scale;
        }
        if ((scaleOrientation.rot[0] != 0.0) ||
                (scaleOrientation.rot[1] != 0.0) ||
                (scaleOrientation.rot[2] != 1.0) ||
                (scaleOrientation.rot[3] != 0.0)) {
            retval += "scaleOrientation " + scaleOrientation;
        }
        if ((translation.value[0] != 0.0) ||
                (translation.value[1] != 0.0) ||
                (translation.value[2] != 0.0)) {
            retval += "translation " + translation;
        }
        retval += super.toStringBody();
        retval += "}";
        return retval;
    }

}

