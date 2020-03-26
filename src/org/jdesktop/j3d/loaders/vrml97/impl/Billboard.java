/*
 * $RCSfile: Billboard.java,v $
 *
 *      @(#)Billboard.java 1.21 99/03/15 10:27:43
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
 * $Date: 2005/02/03 23:06:52 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.TransformGroup;

/**  Description of the Class */
public class Billboard extends GroupBase {

    // exposedField
    SFVec3f axisOfRotation;
    // Note: this field does *not* change as the Billboard rotates, rather,
    // this is the axis about which it rotates.

    Group impl;
    TransformGroup implTrans;
    org.jogamp.java3d.Billboard implBillboard;

    /**
     *Constructor for the Billboard object
     *
     *@param  loader Description of the Parameter
     */
    public Billboard(Loader loader) {
        super(loader);
        axisOfRotation = new SFVec3f(0.0f, 1.0f, 0.0f);
        initBillboardFields();
    }

    /**
     *Constructor for the Billboard object
     *
     *@param  loader Description of the Parameter
     *@param  children Description of the Parameter
     *@param  bboxCenter Description of the Parameter
     *@param  bboxSize Description of the Parameter
     *@param  aor Description of the Parameter
     */
    Billboard(Loader loader, MFNode children, SFVec3f bboxCenter,
            SFVec3f bboxSize, SFVec3f aor) {
        super(loader, children, bboxCenter, bboxSize);
        axisOfRotation = aor;
        initBillboardFields();
    }

    /**  Description of the Method */
    public void initImpl() {
        impl = new Group();
        implTrans = new TransformGroup();
        implGroup = implTrans;
        implNode = impl;
        implGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        implGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        implBillboard = new org.jogamp.java3d.Billboard(implTrans);
        setAxis();
        impl.addChild(implBillboard);
        impl.addChild(implTrans);
        implBillboard.setTarget(implTrans);
        implBillboard.setSchedulingBoundingLeaf(loader.infiniteBoundingLeaf);
        super.replaceChildren();// add the children to the group
        implReady = true;
    }

    /**  Sets the axis attribute of the Billboard object */
    private void setAxis() {
        float[] axis = axisOfRotation.value;
        if ((axis[0] == 0.0) && (axis[1] == 0.0) &&
                (axis[2] == 0.0)) {
            implBillboard.setAlignmentMode(
                    org.jogamp.java3d.Billboard.ROTATE_ABOUT_POINT);
            implBillboard.setRotationPoint(0.0f, 0.0f, 0.0f);
        }
        else {
            implBillboard.setAlignmentMode(
                    org.jogamp.java3d.Billboard.ROTATE_ABOUT_AXIS);
            implBillboard.setAlignmentAxis(axis[0], axis[1], axis[2]);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("axisOfRotation")) {
            setAxis();
        }
        else if (eventInName.equals("route_axisOfRotation")) {
            ;// No-op
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
        return new Billboard(loader,
                (MFNode) children.clone(),
                (SFVec3f) bboxCenter.clone(),
                (SFVec3f) bboxSize.clone(),
                (SFVec3f) axisOfRotation.clone());
    }


    /**
     *  Gets the type attribute of the Billboard object
     *
     *@return  The type value
     */
    public String getType() {
        return "Billboard";
    }

    /**  Description of the Method */
    void initFields() {
        super.initFields();
        initBillboardFields();
    }

    /**  Description of the Method */
    void initBillboardFields() {
        axisOfRotation.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "axisOfRotation");
    }

}

