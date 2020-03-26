/*
 * $RCSfile: PlaneSensor.java,v $
 *
 *      @(#)PlaneSensor.java 1.15 98/11/05 20:34:52
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
 * $Date: 2005/02/03 23:06:59 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.vecmath.Point3d;

/**  Description of the Class */
public class PlaneSensor extends DragSensor {
    // exposedField
    SFVec3f offset;
    SFVec2f maxPosition;
    SFVec2f minPosition;

    // eventOut
    SFVec3f translation;

    /**
     *Constructor for the PlaneSensor object
     *
     *@param  loader Description of the Parameter
     */
    public PlaneSensor(Loader loader) {
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
        return new PlaneSensor(loader);
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
     *  Gets the type attribute of the PlaneSensor object
     *
     *@return  The type value
     */
    public String getType() {
        return "PlaneSensor";
    }

    /**  Description of the Method */
    void initCylinderSensorFields() {
        offset.init(this, FieldSpec, Field.EXPOSED_FIELD, "offset");
        maxPosition.init(this, FieldSpec, Field.EXPOSED_FIELD, "maxPosition");
        minPosition.init(this, FieldSpec, Field.EXPOSED_FIELD, "minPosition");
        translation.init(this, FieldSpec, Field.EVENT_OUT, "translation");
    }

    /**  Description of the Method */
    void offset() {
        ;
    }

    /**
     *  Description of the Method
     *
     *@param  t Description of the Parameter
     */
    void simTick(double t) {
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
        System.out.println("PlaneSensor NYI");
    }

}

