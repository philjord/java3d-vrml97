/*
 * $RCSfile: SFColor.java,v $
 *
 *      @(#)SFColor.java 1.10 98/11/05 20:40:36
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
 * $Date: 2005/02/03 23:07:15 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package vrml.field;

/**  Description of the Class */
public class SFColor extends vrml.Field {
    org.jdesktop.j3d.loaders.vrml97.impl.SFColor impl;

    /**
     *Constructor for the SFColor object
     *
     *@param  init Description of the Parameter
     */
    public SFColor(org.jdesktop.j3d.loaders.vrml97.impl.SFColor init) {
        super(init);
        impl = init;
    }

    /**
     *Constructor for the SFColor object
     *
     *@param  red Description of the Parameter
     *@param  green Description of the Parameter
     *@param  blue Description of the Parameter
     */
    public SFColor(float red, float green, float blue) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFColor(red, green, blue);
        implField = impl;
    }

    /**
     *Constructor for the SFColor object
     *
     *@param  initColor Description of the Parameter
     */
    public SFColor(float[] initColor) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFColor(initColor);
        implField = impl;
    }

    /**
     *  Gets the value attribute of the SFColor object
     *
     *@param  colr Description of the Parameter
     */
    public void getValue(float colr[]) {
        impl.getValue(colr);
    }

    /**
     *  Gets the value attribute of the SFColor object
     *
     *@return  The value value
     */
    public float[] getValue() {
        return impl.getValue();
    }

    /**
     *  Sets the red attribute of the SFColor object
     *
     *@param  red The new red value
     */
    public void setRed(float red) {
        impl.setRed(red);
    }

    /**
     *  Sets the green attribute of the SFColor object
     *
     *@param  green The new green value
     */
    public void setGreen(float green) {
        impl.setGreen(green);
    }

    /**
     *  Sets the blue attribute of the SFColor object
     *
     *@param  blue The new blue value
     */
    public void setBlue(float blue) {
        impl.setBlue(blue);
    }

    /**
     *  Gets the red attribute of the SFColor object
     *
     *@return  The red value
     */
    public float getRed() {
        return impl.getRed();
    }

    /**
     *  Gets the green attribute of the SFColor object
     *
     *@return  The green value
     */
    public float getGreen() {
        return impl.getGreen();
    }

    /**
     *  Gets the blue attribute of the SFColor object
     *
     *@return  The blue value
     */
    public float getBlue() {
        return impl.getBlue();
    }

    /**
     *  Sets the value attribute of the SFColor object
     *
     *@param  colrs The new value value
     */
    public void setValue(float[] colrs) {
        impl.setValue(colrs);
    }

    /**
     *  Sets the value attribute of the SFColor object
     *
     *@param  red The new value value
     *@param  green The new value value
     *@param  blue The new value value
     */
    public void setValue(float red, float green, float blue) {
        impl.setValue(red, green, blue);
    }

    /**
     *  Sets the value attribute of the SFColor object
     *
     *@param  constsfcolr The new value value
     */
    public void setValue(ConstSFColor constsfcolr) {
        impl.setValue(constsfcolr.impl);
    }

    /**
     *  Sets the value attribute of the SFColor object
     *
     *@param  sfcolr The new value value
     */
    public void setValue(SFColor sfcolr) {
        impl.setValue(sfcolr.impl);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new SFColor((org.jdesktop.j3d.loaders.vrml97.impl.SFColor) impl.clone());
    }
}

