/*
 * $RCSfile: CylinderSensor.java,v $
 *
 *      @(#)CylinderSensor.java 1.17 98/11/05 20:34:23
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
 *@Author:  Rick Goldberg
 *@Author:  Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.vecmath.Point3d;

/**  Description of the Class */
public class CylinderSensor extends DragSensor {

    // exposedField
    SFFloat diskAngle;
    SFFloat maxAngle;
    SFFloat minAngle;
    SFFloat offset;

    // eventOut
    SFRotation rotation;

    /**
     *Constructor for the CylinderSensor object
     *
     *@param  loader Description of the Parameter
     */
    public CylinderSensor(Loader loader) {
        super(loader);
        initCylinderSensorFields();
    }

    /**  Description of the Method */
    void initFields() {
        super.initFields();
        initCylinderSensorFields();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new CylinderSensor(loader);
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
    }

    /**
     *  Gets the type attribute of the CylinderSensor object
     *
     *@return  The type value
     */
    public String getType() {
        return "CylinderSensor";
    }


    /**  Description of the Method */
    void initCylinderSensorFields() {
        diskAngle.init(this, FieldSpec, Field.EXPOSED_FIELD, "diskAngle");
        maxAngle.init(this, FieldSpec, Field.EXPOSED_FIELD, "maxAngle");
        minAngle.init(this, FieldSpec, Field.EXPOSED_FIELD, "minAngle");
        offset.init(this, FieldSpec, Field.EXPOSED_FIELD, "offset");
    }

    /**  Description of the Method */
    void offset() {
        ;
    }

    /**
     *  Description of the Method
     *
     *@param  now Description of the Parameter
     */
    void simTick(double now) {
        ;
    }

    /**
     *  Description of the Method
     *
     *@param  p1 Description of the Parameter
     *@param  p2 Description of the Parameter
     *@param  node Description of the Parameter
     *@param  path Description of the Parameter
     */
    void update(Point3d p1, Point3d p2, org.jogamp.java3d.Node node,
            org.jogamp.java3d.SceneGraphPath path) {
        System.out.println("CylinderSensor NYI");
    }

}

