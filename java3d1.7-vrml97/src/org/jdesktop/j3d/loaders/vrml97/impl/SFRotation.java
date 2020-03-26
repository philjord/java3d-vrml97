/*
 * $RCSfile: SFRotation.java,v $
 *
 *      @(#)SFRotation.java 1.18 98/11/05 20:35:43
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
 * $Date: 2005/02/03 23:07:01 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Observable;
import java.util.Observer;

/**  Description of the Class */
public class SFRotation extends Field {
    float[] rot = new float[4];

    /**Constructor for the SFRotation object */
    public SFRotation() {
        rot[0] = 0.0f;
        rot[1] = 0.0f;
        rot[2] = 1.0f;
        rot[3] = 0.0f;
    }

    /**
     *Constructor for the SFRotation object
     *
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  z Description of the Parameter
     *@param  axisAngle Description of the Parameter
     */
    public SFRotation(float x, float y, float z, float axisAngle) {
        rot[0] = x;
        rot[1] = y;
        rot[2] = z;
        rot[3] = axisAngle;
    }

    /**
     *Constructor for the SFRotation object
     *
     *@param  axisAngle Description of the Parameter
     */
    public SFRotation(float[] axisAngle) {
        rot[0] = axisAngle[0];
        rot[1] = axisAngle[1];
        rot[2] = axisAngle[2];
        rot[3] = axisAngle[3];
    }

    /**
     *  Gets the value attribute of the SFRotation object
     *
     *@param  rotation Description of the Parameter
     */
    public void getValue(float[] rotation) {
        System.arraycopy(rot, 0, rotation, 0, 4);
    }

    /**
     *  Gets the value attribute of the SFRotation object
     *
     *@return  The value value
     */
    public float[] getValue() {
        return rot;
    }

    /**
     *  Sets the value attribute of the SFRotation object
     *
     *@param  r The new value value
     */
    public void setValue(float[] r) {
        System.arraycopy(r, 0, rot, 0, 4);
        route();
    }


    /**
     *  Sets the value attribute of the SFRotation object
     *
     *@param  xAxis The new value value
     *@param  yAxis The new value value
     *@param  zAxis The new value value
     *@param  angle The new value value
     */
    public void setValue(float xAxis, float yAxis, float zAxis, float angle) {
        rot[0] = xAxis;
        rot[1] = yAxis;
        rot[2] = zAxis;
        rot[3] = angle;

        route();
    }

    /**
     *  Sets the value attribute of the SFRotation object
     *
     *@param  rotation The new value value
     */
    public void setValue(ConstSFRotation rotation) {
        setValue((SFRotation) rotation.ownerField);
    }

    /**
     *  Sets the value attribute of the SFRotation object
     *
     *@param  rotation The new value value
     */
    public void setValue(SFRotation rotation) {
        setValue(rotation.rot);

    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        //Object o = (Object) new SFRotation();
        return new SFRotation(rot);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((SFRotation) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstSFRotation(this);
        }
        return constField;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.SFRotation(this);
    }

    /**  Description of the Method */
    void route() {
        double normalizer = Math.sqrt(rot[0] * rot[0] + rot[1] * rot[1] + rot[2] * rot[2]);
        if (normalizer < .001f) {
            rot[0] = 0.0f;
            rot[1] = 1.0f;
            rot[2] = 0.0f;
        }
        else {
            rot[0] /= normalizer;
            rot[1] /= normalizer;
            rot[2] /= normalizer;
        }
        super.route();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toString() {
        return rot[0] + " " + rot[1] + " " + rot[2] + " " + rot[3] + "\n";
    }

}

