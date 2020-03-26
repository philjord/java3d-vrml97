/*
 * $RCSfile: SFVec3f.java,v $
 *
 *      @(#)SFVec3f.java 1.19 99/02/09 17:26:42
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
public class SFVec3f extends Field {

    float[] value = new float[3];
    float[] initValue = new float[3];

    /**  Sets the init attribute of the SFVec3f object */
    private void setInit() {
        initValue[0] = value[0];
        initValue[1] = value[1];
        initValue[2] = value[2];
    }

    /**
     *Constructor for the SFVec3f object
     *
     *@param  setVal Description of the Parameter
     */
    public SFVec3f(float[] setVal) {
        setValue(setVal);
        setInit();
    }

    /**
     *Constructor for the SFVec3f object
     *
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  z Description of the Parameter
     */
    public SFVec3f(float x, float y, float z) {
        value[0] = x;
        value[1] = y;
        value[2] = z;
        setInit();
    }

    /**Constructor for the SFVec3f object */
    public SFVec3f() {
        value[0] = 0.0f;
        value[1] = 0.0f;
        value[2] = 0.0f;
        setInit();
    }

    /**  Description of the Method */
    public void reset() {
        value[0] = initValue[0];
        value[1] = initValue[1];
        value[2] = initValue[2];
    }

    /**
     *  Gets the value attribute of the SFVec3f object
     *
     *@param  vec Description of the Parameter
     */
    public void getValue(float[] vec) {
        vec[0] = value[0];
        vec[1] = value[1];
        vec[2] = value[2];
    }

    /**
     *  Gets the x attribute of the SFVec3f object
     *
     *@return  The x value
     */
    public float getX() {
        return value[0];
    }

    /**
     *  Gets the y attribute of the SFVec3f object
     *
     *@return  The y value
     */
    public float getY() {
        return value[1];
    }

    /**
     *  Gets the z attribute of the SFVec3f object
     *
     *@return  The z value
     */
    public float getZ() {
        return value[2];
    }

    /**
     *  Gets the value attribute of the SFVec3f object
     *
     *@return  The value value
     */
    public float[] getValue() {
        // the ConstSFVec3f does the clone. not here.
        return value;
    }

    /**
     *  Sets the value attribute of the SFVec3f object
     *
     *@param  v The new value value
     */
    public void setValue(float[] v) {
        value[0] = v[0];
        value[1] = v[1];
        value[2] = v[2];
        route();
    }

    /**
     *  Sets the value attribute of the SFVec3f object
     *
     *@param  x The new value value
     *@param  y The new value value
     *@param  z The new value value
     */
    public void setValue(float x, float y, float z) {
        value[0] = x;
        value[1] = y;
        value[2] = z;

        route();
    }

    /**
     *  Sets the value attribute of the SFVec3f object
     *
     *@param  v The new value value
     */
    public void setValue(ConstSFVec3f v) {
        setValue(((SFVec3f) v.ownerField).value);
    }

    /**
     *  Sets the value attribute of the SFVec3f object
     *
     *@param  v The new value value
     */
    public void setValue(SFVec3f v) {
        setValue(v.value);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFVec3f(value);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    synchronized void update(Field field) {
        setValue(((SFVec3f) field).value);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstSFVec3f(this);
        }
        return constField;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.SFVec3f(this);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toString() {
        return value[0] + " " + value[1] + " " + value[2] + "\n";
    }
}

