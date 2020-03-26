/*
 * $RCSfile: SFRotation.java,v $
 *
 *      @(#)SFRotation.java 1.9 98/11/05 20:40:39
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
public class SFRotation extends vrml.Field {
    org.jdesktop.j3d.loaders.vrml97.impl.SFRotation impl;

    /**
     *Constructor for the SFRotation object
     *
     *@param  init Description of the Parameter
     */
    public SFRotation(org.jdesktop.j3d.loaders.vrml97.impl.SFRotation init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFRotation(
                (org.jdesktop.j3d.loaders.vrml97.impl.SFRotation) impl.clone());
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
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFRotation(x, y, z,
                axisAngle);
        implField = impl;
    }

    /**
     *Constructor for the SFRotation object
     *
     *@param  axisAngle Description of the Parameter
     */
    public SFRotation(float[] axisAngle) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFRotation(axisAngle);
        implField = impl;
    }

    /**
     *  Gets the value attribute of the SFRotation object
     *
     *@param  rotation Description of the Parameter
     */
    public void getValue(float[] rotation) {
        impl.getValue(rotation);
    }

    /**
     *  Gets the value attribute of the SFRotation object
     *
     *@return  The value value
     */
    public float[] getValue() {
        return impl.getValue();
    }

    /**
     *  Sets the value attribute of the SFRotation object
     *
     *@param  r The new value value
     */
    public void setValue(float[] r) {
        impl.setValue(r);
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
        impl.setValue(xAxis, yAxis, zAxis, angle);
    }

    /**
     *  Sets the value attribute of the SFRotation object
     *
     *@param  rotation The new value value
     */
    public void setValue(ConstSFRotation rotation) {
        impl.setValue(rotation.impl);
    }

    /**
     *  Sets the value attribute of the SFRotation object
     *
     *@param  rotation The new value value
     */
    public void setValue(SFRotation rotation) {
        impl.setValue(rotation.impl);
    }
}

