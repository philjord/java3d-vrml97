/*
 * $RCSfile: ProximitySensor.java,v $
 *
 *      @(#)ProximitySensor.java 1.16 98/11/05 20:34:57
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
 * $Date: 2005/02/03 23:07:00 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.WakeupAnd;
import org.jogamp.java3d.WakeupOnViewPlatformEntry;
import org.jogamp.java3d.WakeupOnViewPlatformExit;
import org.jogamp.java3d.WakeupOr;
import org.jogamp.vecmath.Point3d;

/**  Description of the Class */
public class ProximitySensor extends Node {

    // exposedFields
    SFVec3f center;
    SFVec3f size;
    SFBool enabled;

    // eventOuts
    SFBool isActive;
    SFVec3f position;
    SFRotation orientation;
    SFTime enterTime;
    SFTime exitTime;

    //
    org.jogamp.java3d.BoundingBox bbox;
    ProximityBehavior impl;

    /**
     *Constructor for the ProximitySensor object
     *
     *@param  loader Description of the Parameter
     */
    public ProximitySensor(Loader loader) {
        super(loader);
        center = new SFVec3f(0.0f, 0.0f, 0.0f);
        size = new SFVec3f(0.0f, 0.0f, 0.0f);
        enabled = new SFBool(true);
        isActive = new SFBool(false);
        position = new SFVec3f();
        orientation = new SFRotation();
        enterTime = new SFTime();
        exitTime = new SFTime();
        initFields();
    }

    /**
     *Constructor for the ProximitySensor object
     *
     *@param  loader Description of the Parameter
     *@param  center Description of the Parameter
     *@param  size Description of the Parameter
     *@param  enabled Description of the Parameter
     */
    ProximitySensor(Loader loader, SFVec3f center, SFVec3f size,
            SFBool enabled) {
        super(loader);
        this.center = center;
        this.size = size;
        this.enabled = enabled;
        isActive = new SFBool(false);
        position = new SFVec3f();
        orientation = new SFRotation();
        enterTime = new SFTime();
        exitTime = new SFTime();
        initFields();
    }


    /**  Description of the Method */
    void initImpl() {

        bbox = new BoundingBox(new Point3d(center.value[0] - size.value[0] / 2,
                center.value[1] - size.value[1] / 2,
                center.value[2] - size.value[2] / 2),
                new Point3d(center.value[0] + size.value[0] / 2,
                center.value[1] + size.value[1] / 2,
                center.value[2] + size.value[2] / 2));

        impl = new ProximityBehavior(this);
    }

    /**  Description of the Method */
    void initFields() {
        enabled.init(this, FieldSpec, Field.EXPOSED_FIELD, "enabled");
        center.init(this, FieldSpec, Field.EXPOSED_FIELD, "center");
        size.init(this, FieldSpec, Field.EXPOSED_FIELD, "size");
        isActive.init(this, FieldSpec, Field.EVENT_OUT, "isActive");
        position.init(this, FieldSpec, Field.EVENT_OUT, "position");
        orientation.init(this, FieldSpec, Field.EVENT_OUT, "orientation");
        enterTime.init(this, FieldSpec, Field.EVENT_OUT, "enterTime");
        exitTime.init(this, FieldSpec, Field.EVENT_OUT, "exitTime");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new ProximitySensor(loader, (SFVec3f) center.clone(),
                (SFVec3f) size.clone(), (SFBool) enabled.clone());
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("enabled")) {
            if (enabled.getValue() == true) {
                impl.initialize();
            }//else {
            //impl.conditions[0] = impl.wovex;
            //impl.conditions[1] = impl.woven;
            //// should never happen
            //impl.wakeupOn(new WakeupAnd(impl.conditions));
            //}
        }
    }

    /**
     *  Gets the type attribute of the ProximitySensor object
     *
     *@return  The type value
     */
    public String getType() {
        return "ProximitySensor";
    }

    /**
     *  Gets the implNode attribute of the ProximitySensor object
     *
     *@return  The implNode value
     */
    public org.jogamp.java3d.Node getImplNode() {
        return impl;
    }

}

