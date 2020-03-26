/*
 * $RCSfile: SFColor.java,v $
 *
 *      @(#)SFColor.java 1.15 98/11/05 20:35:00
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
import java.util.Observable;
import java.util.Observer;

/**  Description of the Class */
public class SFColor extends Field {

    float[] color;

    /**
     *Constructor for the SFColor object
     *
     *@param  red Description of the Parameter
     *@param  green Description of the Parameter
     *@param  blue Description of the Parameter
     */
    public SFColor(float red, float green, float blue) {
        color = new float[3];
        color[0] = red;
        color[1] = green;
        color[2] = blue;
    }

    /**
     *Constructor for the SFColor object
     *
     *@param  initColor Description of the Parameter
     */
    public SFColor(float[] initColor) {
        color = new float[3];
        setValue(initColor);
    }

    /**
     *  Gets the value attribute of the SFColor object
     *
     *@param  colr Description of the Parameter
     */
    public void getValue(float colr[]) {
        System.arraycopy(color, 0, colr, 0, 3);

    }

    /**
     *  Gets the value attribute of the SFColor object
     *
     *@return  The value value
     */
    public float[] getValue() {
        return color;
    }

    /**
     *  Sets the red attribute of the SFColor object
     *
     *@param  red The new red value
     */
    public void setRed(float red) {
        color[0] = red;
        route();
    }

    /**
     *  Sets the green attribute of the SFColor object
     *
     *@param  green The new green value
     */
    public void setGreen(float green) {
        color[1] = green;
        route();
    }

    /**
     *  Sets the blue attribute of the SFColor object
     *
     *@param  blue The new blue value
     */
    public void setBlue(float blue) {
        color[2] = blue;
        route();
    }

    /**
     *  Gets the red attribute of the SFColor object
     *
     *@return  The red value
     */
    public float getRed() {
        return color[0];
    }

    /**
     *  Gets the green attribute of the SFColor object
     *
     *@return  The green value
     */
    public float getGreen() {
        return color[1];
    }

    /**
     *  Gets the blue attribute of the SFColor object
     *
     *@return  The blue value
     */
    public float getBlue() {
        return color[2];
    }

    /**
     *  Sets the value attribute of the SFColor object
     *
     *@param  colrs The new value value
     */
    public void setValue(float[] colrs) {
        System.arraycopy(colrs, 0, color, 0, 3);
        route();
    }

    /**
     *  Sets the value attribute of the SFColor object
     *
     *@param  red The new value value
     *@param  green The new value value
     *@param  blue The new value value
     */
    public void setValue(float red, float green, float blue) {
        color[0] = red;
        color[1] = green;
        color[2] = blue;

        route();
    }

    /**
     *  Sets the value attribute of the SFColor object
     *
     *@param  constsfcolr The new value value
     */
    public void setValue(ConstSFColor constsfcolr) {
        setValue((SFColor) constsfcolr.ownerField);

    }

    /**
     *  Sets the value attribute of the SFColor object
     *
     *@param  sfcolr The new value value
     */
    public void setValue(SFColor sfcolr) {
        setValue(sfcolr.color);

    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFColor(color);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((SFColor) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstSFColor(this);
        }
        return constField;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.SFColor(this);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toString() {
        return color[0] + " " + color[1] + " " + color[2] + "\n";
    }

}

