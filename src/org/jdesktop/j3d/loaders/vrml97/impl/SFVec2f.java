/*
 * $RCSfile: SFVec2f.java,v $
 *
 *      @(#)SFVec2f.java 1.11 98/11/05 20:35:44
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
public class SFVec2f extends Field {

    float[] vec2f = new float[2];

    /**
     *Constructor for the SFVec2f object
     *
     *@param  values Description of the Parameter
     */
    public SFVec2f(float[] values) {
        setValue(values);
    }

    /**Constructor for the SFVec2f object */
    public SFVec2f() { }

    /**
     *Constructor for the SFVec2f object
     *
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     */
    public SFVec2f(float x, float y) {
        vec2f[0] = x;
        vec2f[1] = y;
    }

    /**
     *  Gets the value attribute of the SFVec2f object
     *
     *@param  vec Description of the Parameter
     */
    public void getValue(float[] vec) {
        System.arraycopy(vec2f, 0, vec, 0, 2);
    }

    /**
     *  Gets the value attribute of the SFVec2f object
     *
     *@return  The value value
     */
    public float[] getValue() {
        return vec2f;
    }

    /**
     *  Gets the x attribute of the SFVec2f object
     *
     *@return  The x value
     */
    public float getX() {
        return vec2f[0];
    }

    /**
     *  Gets the y attribute of the SFVec2f object
     *
     *@return  The y value
     */
    public float getY() {
        return vec2f[1];
    }

    /**
     *  Sets the value attribute of the SFVec2f object
     *
     *@param  v The new value value
     */
    public void setValue(float[] v) {
        try {
            System.arraycopy(v, 0, vec2f, 0, 2);
            route();
        }
        catch (Exception e) {
            System.err.println("You need to instance enough float to get by ");
        }

    }

    /**
     *  Sets the value attribute of the SFVec2f object
     *
     *@param  x The new value value
     *@param  y The new value value
     */
    public void setValue(float x, float y) {
        vec2f[0] = x;
        vec2f[1] = y;
        route();
    }

    /**
     *  Sets the value attribute of the SFVec2f object
     *
     *@param  v The new value value
     */
    public void setValue(ConstSFVec2f v) {
        setValue((SFVec2f) v.ownerField);
    }

    /**
     *  Sets the value attribute of the SFVec2f object
     *
     *@param  v The new value value
     */
    public void setValue(SFVec2f v) {
        setValue(v.getValue());
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFVec2f(vec2f);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((SFVec2f) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstSFVec2f(this);
        }
        return constField;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.SFVec2f(this);
    }

}

