/*
 * $RCSfile: ConstSFColor.java,v $
 *
 *      @(#)ConstSFColor.java 1.12 98/11/05 20:34:15
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
 * $Date: 2005/02/03 23:06:54 $
 * $State: Exp $
 */
/*
 *@Author:  Rick Goldberg
 *@Author:  Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class ConstSFColor extends ConstField {
    /**
     *Constructor for the ConstSFColor object
     *
     *@param  owner Description of the Parameter
     */
    ConstSFColor(SFColor owner) {
        super(owner);
    }

    /**
     *Constructor for the ConstSFColor object
     *
     *@param  red Description of the Parameter
     *@param  green Description of the Parameter
     *@param  blue Description of the Parameter
     */
    public ConstSFColor(float red, float green, float blue) {
        super(new SFColor(red, green, blue));
    }

    /**
     *Constructor for the ConstSFColor object
     *
     *@param  initColor Description of the Parameter
     */
    public ConstSFColor(float[] initColor) {
        super(new SFColor(initColor));
    }

    /**
     *  Gets the value attribute of the ConstSFColor object
     *
     *@param  colr Description of the Parameter
     */
    public void getValue(float colr[]) {
        ((SFColor) ownerField).getValue(colr);
    }

    /**
     *  Gets the value attribute of the ConstSFColor object
     *
     *@return  The value value
     */
    public float[] getValue() {
        return ((SFColor) ownerField).getValue();
    }

    /**
     *  Gets the red attribute of the ConstSFColor object
     *
     *@return  The red value
     */
    public float getRed() {
        return ((SFColor) ownerField).getRed();
    }

    /**
     *  Gets the green attribute of the ConstSFColor object
     *
     *@return  The green value
     */
    public float getGreen() {
        return ((SFColor) ownerField).getGreen();
    }

    /**
     *  Gets the blue attribute of the ConstSFColor object
     *
     *@return  The blue value
     */
    public float getBlue() {
        return ((SFColor) ownerField).getBlue();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new ConstSFColor((SFColor) ownerField);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.ConstSFColor(this);
    }

}

