/*
 * $RCSfile: VisibilitySensor.java,v $
 *
 *      @(#)VisibilitySensor.java 1.12 98/11/05 20:35:30
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
 * $Date: 2005/02/03 23:07:04 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class VisibilitySensor extends Node {

    // exposedField
    SFVec3f center;
    SFBool enabled;
    SFVec3f size;

    // Event outs
    SFBool isActive;
    SFTime enterTime;
    SFTime exitTime;

    /**
     *Constructor for the VisibilitySensor object
     *
     *@param  loader Description of the Parameter
     */
    public VisibilitySensor(Loader loader) {
        super(loader);
        enabled = new SFBool(true);
        isActive = new SFBool(true);
        enterTime = new SFTime(0.0);
        exitTime = new SFTime(0.0);
        loader.addVisibilitySensor(this);
        initFields();
    }

    /**
     *Constructor for the VisibilitySensor object
     *
     *@param  loader Description of the Parameter
     *@param  enabled Description of the Parameter
     *@param  center Description of the Parameter
     *@param  size Description of the Parameter
     *@param  enterTime Description of the Parameter
     *@param  exitTime Description of the Parameter
     *@param  isActive Description of the Parameter
     */
    VisibilitySensor(Loader loader, SFBool enabled, SFVec3f center, SFVec3f size,
            SFTime enterTime, SFTime exitTime, SFBool isActive) {
        super(loader);
        this.enabled = enabled;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.isActive = isActive;
        loader.addVisibilitySensor(this);
        initFields();
    }

    //public void initImpl() { ; } // tbd

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("enabled")) {
        }
        else {
            System.err.println("VisibilitySensor: unknown eventInName " +
                    eventInName);
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        VisibilitySensor v = new VisibilitySensor(loader, (SFBool) enabled.clone(),
                (SFVec3f) center.clone(), (SFVec3f) size.clone(), (SFTime) enterTime.clone(),
                (SFTime) exitTime.clone(), (SFBool) isActive.clone());
        return v;
    }

    /**
     *  Gets the type attribute of the VisibilitySensor object
     *
     *@return  The type value
     */
    public String getType() {
        return "VisibilitySensor";
    }

    /**  Description of the Method */
    void initFields() {
        enabled.init(this, FieldSpec, Field.EXPOSED_FIELD, "enabled");
        center.init(this, FieldSpec, Field.EXPOSED_FIELD, "center");
        size.init(this, FieldSpec, Field.EXPOSED_FIELD, "size");
        isActive.init(this, FieldSpec, Field.EVENT_OUT, "isActive");
        enterTime.init(this, FieldSpec, Field.EVENT_OUT, "enterTime");
        exitTime.init(this, FieldSpec, Field.EVENT_OUT, "exitTime");
    }

}

