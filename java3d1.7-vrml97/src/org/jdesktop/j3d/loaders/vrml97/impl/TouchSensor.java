/*
 * $RCSfile: TouchSensor.java,v $
 *
 *      @(#)TouchSensor.java 1.28 99/03/10 18:02:52
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
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Vector;

/**  Description of the Class */
public class TouchSensor extends Node implements VrmlSensor {

    // exposedField
    SFBool enabled;

    // Event outs
    SFVec3f hitNormal;
    SFVec3f hitPoint;
    SFVec2f hitTexCoord;
    SFBool isActive;
    SFBool isOver;
    SFTime touchTime;

    // Scene graph reference
    org.jogamp.java3d.Node parentImpl;

    /**
     *Constructor for the TouchSensor object
     *
     *@param  loader Description of the Parameter
     */
    public TouchSensor(Loader loader) {
        super(loader);
        enabled = new SFBool(true);
        hitNormal = new SFVec3f();
        hitPoint = new SFVec3f();
        hitTexCoord = new SFVec2f();
        isActive = new SFBool(true);
        isOver = new SFBool(false);
        touchTime = new SFTime(0.0);
        loader.addTouchSensor(this);
        initFields();
    }

    /**
     *Constructor for the TouchSensor object
     *
     *@param  loader Description of the Parameter
     *@param  enabled Description of the Parameter
     */
    TouchSensor(Loader loader, SFBool enabled) {
        super(loader);
        this.enabled = enabled;
        hitNormal = new SFVec3f();
        hitPoint = new SFVec3f();
        hitTexCoord = new SFVec2f();
        isActive = new SFBool(true);
        isOver = new SFBool(false);
        touchTime = new SFTime(0.0);
        loader.addTouchSensor(this);
        initFields();
    }

    /**  Description of the Method */
    public void initImpl() {
        loader.addTouchSensor(this);
    }

    /**
     *  Description of the Method
     *
     *@param  parentImpl Description of the Parameter
     */
    void updateParent(org.jogamp.java3d.Node parentImpl) {
        if (loader.debug) {
            System.out.println("TouchSensor.updateParent()");
        }
        if (loader.loaderMode > Loader.LOAD_STATIC) {
            if (loader.debug) {
                System.out.println("Touch Sensor enabling PICK_REPORTING on "
                         + " parent " + parentImpl);
            }
            Vector v = (Vector) (parentImpl.getUserData());
            if (v == null) {
                v = new Vector();
                parentImpl.setUserData(v);
                if (loader.debug) {
                    System.out.println("Touch Sensor parent: " + parentImpl +
                            " had no user data, added vector:" + v);
                }
            }
            v.addElement(this);
            parentImpl.setCapability(
                    org.jogamp.java3d.Node.ENABLE_PICK_REPORTING);
            parentImpl.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
            parentImpl.setCapability(
                    org.jogamp.java3d.Node.ALLOW_LOCAL_TO_VWORLD_READ);
            parentImpl.setPickable(true);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("enabled")) {
        }
        else if (eventInName.equals("route_enabled")) {
            // No-op
        }
        else {
            System.err.println("TouchSensor: unknown eventInName " +
                    eventInName);
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        TouchSensor t = new TouchSensor(loader, (SFBool) enabled.clone());
        return t;
    }

    /**
     *  Gets the type attribute of the TouchSensor object
     *
     *@return  The type value
     */
    public String getType() {
        return "TouchSensor";
    }

    /**  Description of the Method */
    void initFields() {
        enabled.init(this, FieldSpec, Field.EXPOSED_FIELD, "enabled");
        hitNormal.init(this, FieldSpec, Field.EVENT_OUT, "hitNormal");
        hitPoint.init(this, FieldSpec, Field.EVENT_OUT, "hitPoint");
        hitTexCoord.init(this, FieldSpec, Field.EVENT_OUT, "hitTexCoord");
        isActive.init(this, FieldSpec, Field.EVENT_OUT, "isActive");
        isOver.init(this, FieldSpec, Field.EVENT_OUT, "isOver");
        touchTime.init(this, FieldSpec, Field.EVENT_OUT, "touchTime");
    }

}

