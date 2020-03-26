/*
 * $RCSfile: Viewpoint.java,v $
 *
 *      @(#)Viewpoint.java 1.54 99/03/11 09:48:15
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
import org.jogamp.java3d.BoundingLeaf;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Transform3D;

import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.ViewPlatform;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Quat4d;
import org.jogamp.vecmath.Vector3d;

/**  Description of the Class */
public class Viewpoint extends BindableNode {

    // If (loader.browser == null), the J3D structure will be:
    //
    // impl == implOrient: a TransformGroup with xform set by VP fields
    // -->ViewPlatform


    // If (loader.browser == null), the J3D structure will be:
    //
    // impl is the detachable handle to this node.
    // ->implWorld: a TransformGroup with browser conrolled xform
    // ->->implOrient:  a TransformGroup with xform set by VP fields
    // ->->->implBrowser: a TransformGroup with browser controlled xform
    // ->->->->ViewPlatform
    // ->->->->Other RGroups used by browser

    BranchGroup impl;
    TransformGroup implOrient;
    TransformGroup implWorld;

    // From BindableNode;
    //SFBool bind;
    //SFBool isBound;
    //SFTime bindTime;

    // exposedField
    SFFloat fieldOfView;
    SFBool jump;
    SFRotation orientation;
    SFVec3f position;
    SFString description;

    SFRotation examine;// means just update the rotation without translation

    // impl objects
    Transform3D implTrans, implBrowserTrans;//?implBrowserTrans?
    TransformGroup implBrowser;
    Transform3D examineViewpoint = new Transform3D();
    Transform3D examineRotation = new Transform3D();
    ViewPlatform implViewPlatform;
    BoundingLeaf implBoundingLeaf;
    AxisAngle4d axis = new AxisAngle4d();
    Vector3d trans = new Vector3d();

    /**
     *Constructor for the Viewpoint object
     *
     *@param  loader Description of the Parameter
     */
    public Viewpoint(Loader loader) {
        super(loader, loader.getViewpointStack());
        fieldOfView = new SFFloat(.5398f);
        jump = new SFBool(true);
        orientation = new SFRotation(0.0f, 0.0f, 1.0f, 0.0f);
        examine = new SFRotation(0.0f, 0.0f, 1.0f, 0.0f);
        position = new SFVec3f(0.0f, 0.0f, 10.0f);
        description = new SFString("");
        loader.addViewpoint(this);
        initFields();
    }

    /**
     *Constructor for the Viewpoint object
     *
     *@param  loader Description of the Parameter
     *@param  bind Description of the Parameter
     *@param  bindTime Description of the Parameter
     *@param  isBound Description of the Parameter
     *@param  fieldOfView Description of the Parameter
     *@param  jump Description of the Parameter
     *@param  orientation Description of the Parameter
     *@param  position Description of the Parameter
     *@param  description Description of the Parameter
     */
    public Viewpoint(Loader loader, SFBool bind, SFTime bindTime,
            SFBool isBound, SFFloat fieldOfView, SFBool jump,
            SFRotation orientation, SFVec3f position, SFString description) {
        super(loader, loader.getViewpointStack(), bind, bindTime, isBound);
        this.fieldOfView = fieldOfView;
        this.jump = jump;
        this.orientation = orientation;
        this.position = position;
        this.description = description;
        examine = new SFRotation(0.0f, 0.0f, 1.0f, 0.0f);
        loader.addViewpoint(this);
        initFields();
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("position") ||
                eventInName.equals("orientation")) {
            updateViewTrans();
        }
        else if (eventInName.equals("examine")) {
            updateViewTransExamine();
        }
        else if (eventInName.equals("fieldOfView") ||
                eventInName.equals("description") ||
                eventInName.equals("jump")) {
            updateBrowser();
        }
        else if (eventInName.equals("bind")) {
            // TODO: handle jumping
            super.notifyMethod("bind", time);
        }
        else if (eventInName.equals("route_position") ||
                eventInName.equals("route_orientation") ||
                eventInName.equals("route_fieldOfView") ||
                eventInName.equals("route_bind") ||
                eventInName.equals("route_description")) {
            ;
        }
        else if (eventInName.equals("route_examine")) {
            updateViewTrans();
        }
        else {
            System.err.println("Viewpoint: unexpected notify " + eventInName);
        }
    }

    /**  Description of the Method */
    void updateViewTrans() {
        axis.x = orientation.rot[0];
        axis.y = orientation.rot[1];
        axis.z = orientation.rot[2];
        double normalizer = Math.sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z);

        if (normalizer < .001) {// by about a millimeter
            axis.x = 0.0;
            axis.y = 1.0;
            axis.z = 0.0;
        }
        else {
            axis.x /= normalizer;
            axis.y /= normalizer;
            axis.z /= normalizer;
        }
        axis.angle = Math.IEEEremainder(orientation.rot[3], Math.PI * 2.0);
        implTrans.setIdentity();
        implTrans.set(axis);
        trans.x = position.value[0];
        trans.y = position.value[1];
        trans.z = position.value[2];
        implTrans.setTranslation(trans);
        if ((implTrans.getType() & Transform3D.CONGRUENT) != 0) {
            implOrient.setTransform(implTrans);
        }

    }

    // obsolete see SceneTransform, but could go back
    /**  Description of the Method */
    void updateViewTransExamine() {
        axis.x = -examine.rot[0];
        axis.y = -examine.rot[1];
        axis.z = -examine.rot[2];
        double normalizer = Math.sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z);

        if (normalizer < .001) {// by about a millimeter
            axis.x = 0.0;
            axis.y = 1.0;
            axis.z = 0.0;
        }
        else {
            axis.x /= normalizer;
            axis.y /= normalizer;
            axis.z /= normalizer;
        }
        axis.angle = Math.IEEEremainder(examine.rot[3], Math.PI * 2.0);
        examineRotation.set(axis);
        examineViewpoint.mul(examineRotation, implTrans);
        if ((examineViewpoint.getType() & Transform3D.CONGRUENT) != 0) {
            implWorld.setTransform(examineViewpoint);
        }

    }

    /**  Description of the Method */
    void updateBrowser() {
        browser.viewChanged(this);
    }

    /**  Description of the Method */
    void initImpl() {
        if (loader.browser != null) {
            // A TransformGroup above the Viewpoint's transforms.  This is used
            // by the browser to move the world relative to the viewpoint for
            // "examine" mode
            implWorld = new TransformGroup();
            implWorld.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            implWorld.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
            implWorld.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
            implWorld.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            implWorld.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            implWorld.setCapability(
                    TransformGroup.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
            implWorld.setCapability(
                    TransformGroup.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
            implWorld.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
            implWorld.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_WRITE);
            implWorld.setCapability(
                    org.jogamp.java3d.Group.ALLOW_COLLISION_BOUNDS_WRITE);

            // The Viewpoint itself. This part does not generally
            // move.
            implTrans = new Transform3D();
            implOrient = new TransformGroup();
            implOrient.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            implOrient.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
            implOrient.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
            implOrient.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            implOrient.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            implOrient.setCapability(
                    TransformGroup.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
            implOrient.setCapability(
                    TransformGroup.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
            implOrient.setCapability(TransformGroup.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
            implOrient.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
            implOrient.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_WRITE);
            implOrient.setCapability(org.jogamp.java3d.Group.ALLOW_COLLISION_BOUNDS_WRITE);
            updateViewTrans();

            // The current position of the camera. This is the offset
            // transform and group away from the viewpoint impl and implTrans.
            // The BrowserBehavior will update this due to user interaction.
            implBrowser = new TransformGroup();
            implBrowserTrans = new Transform3D();
            implBrowser.setTransform(implBrowserTrans);
            implBrowser.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            implBrowser.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            implBrowser.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            implBrowser.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
            implBrowser.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
            implBrowser.setCapability(
                    TransformGroup.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
            implBrowser.setCapability(
                    TransformGroup.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
            implBrowser.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
            implBrowser.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_WRITE);
            implBrowser.setCapability(
                    org.jogamp.java3d.Group.ALLOW_COLLISION_BOUNDS_WRITE);

            // children of impl

            BranchGroup t = new RGroup();
            t.addChild(implBrowser);

            // 0 browserGroup
            // 1 fog
            // 2 background
            // 3 boundingLeaf
            implOrient.addChild(t);
            implOrient.addChild(new RGroup());
            implOrient.addChild(new RGroup());
            implOrient.addChild(new RGroup());
            ((RGroup) implOrient.getChild(3)).addChild(
                    implBoundingLeaf = new BoundingLeaf(
                    new BoundingSphere(
                    (new Point3d(0.0, 0.0, 0.0)), 100.0
                    )
                    )
                    );

            // children of implBrowser

            implViewPlatform = new ViewPlatform();
            BranchGroup u = new RGroup();
            u.addChild(implViewPlatform);
            implViewPlatform.setActivationRadius(Float.MAX_VALUE - 1.0f);
            implViewPlatform.setCapability(org.jogamp.java3d.Node.ALLOW_LOCAL_TO_VWORLD_READ);

            // leave room for
            // 0 viewPlatform
            // 1 headLights
            // 2 browserBehavior
            implBrowser.addChild(u);
            implBrowser.addChild(new RGroup());
            implBrowser.addChild(new RGroup());

            impl = new RGroup();
            impl.addChild(implWorld);
            implWorld.addChild(implOrient);

            if (loader.debug) {
                System.out.println("Viewpoint impl = " + impl);
            }

            implNode = impl;
        }
        else {
            // just set stuff up for the loader
            implOrient = new TransformGroup();
            implOrient.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            implOrient.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
            implOrient.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
            implOrient.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            implOrient.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            implOrient.setCapability(
                    TransformGroup.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
            implOrient.setCapability(
                    TransformGroup.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
            implOrient.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
            implOrient.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_WRITE);
            implOrient.setCapability(
                    org.jogamp.java3d.Group.ALLOW_COLLISION_BOUNDS_WRITE);
            implTrans = new Transform3D();
            updateViewTrans();

            implViewPlatform = new ViewPlatform();
            implViewPlatform.setActivationRadius(Float.MAX_VALUE - 1.0f);
            implOrient.addChild(implViewPlatform);
            implNode = implOrient;
        }

        implReady = true;
    }


    /**
     *  Description of the Method
     *
     *@param  bounds Description of the Parameter
     */
    void frameObject(BoundingSphere bounds) {
        Point3d p = new Point3d();
        bounds.getCenter(p);
        //System.out.println("frameObject "+ p + " " + bounds.getRadius());
        position.value[0] = (float) p.x;
        position.value[1] = (float) p.y / 1.05f;
        position.value[2] = (float) p.z + (3.14f * (float) (bounds.getRadius()));
        updateViewTrans();
    }

    /**
     *  Gets the viewPlatform attribute of the Viewpoint object
     *
     *@return  The viewPlatform value
     */
    public org.jogamp.java3d.ViewPlatform getViewPlatform() {
        return implViewPlatform;
    }

    /**
     *  Gets the transformGroup attribute of the Viewpoint object
     *
     *@return  The transformGroup value
     */
    public org.jogamp.java3d.TransformGroup getTransformGroup() {
        return implOrient;
    }

    /**
     *  Gets the fOV attribute of the Viewpoint object
     *
     *@return  The fOV value
     */
    public float getFOV() {
        return fieldOfView.value;
    }

    /**
     *  Gets the description attribute of the Viewpoint object
     *
     *@return  The description value
     */
    public String getDescription() {
        return description.getValue();
    }

    /**
     *  Gets the type attribute of the Viewpoint object
     *
     *@return  The type value
     */
    public String getType() {
        return "Viewpoint";
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Viewpoint(loader,
                (SFBool) bind.clone(),
                (SFTime) bindTime.clone(),
                (SFBool) isBound.clone(),
                (SFFloat) fieldOfView.clone(),
                (SFBool) jump.clone(), (SFRotation) orientation.clone(),
                (SFVec3f) position.clone(),
                (SFString) description.clone());
    }

    /**  Description of the Method */
    void initFields() {
        initBindableFields();
        fieldOfView.init(this, FieldSpec, Field.EXPOSED_FIELD, "fieldOfView");
        jump.init(this, FieldSpec, Field.EXPOSED_FIELD, "jump");
        orientation.init(this, FieldSpec, Field.EXPOSED_FIELD, "orientation");
        position.init(this, FieldSpec, Field.EXPOSED_FIELD, "position");
        description.init(this, FieldSpec, Field.FIELD, "description");
        examine.init(this, FieldSpec, Field.EXPOSED_FIELD, "examine");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.BaseNode wrap() {
        return new org.jdesktop.j3d.loaders.vrml97.node.Viewpoint(this);
    }

}

