/*
 * $RCSfile: SFVec3f.java,v $
 *
 *      @(#)SFVec3f.java 1.10 98/11/05 20:40:42
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
public class SFVec3f extends vrml.Field {
    org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f impl;

    /**
     *Constructor for the SFVec3f object
     *
     *@param  init Description of the Parameter
     */
    public SFVec3f(org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFVec3f(
                (org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f) impl.clone());
    }

    /**
     *Constructor for the SFVec3f object
     *
     *@param  values Description of the Parameter
     */
    public SFVec3f(float[] values) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f(values);
        implField = impl;
    }

    /**
     *Constructor for the SFVec3f object
     *
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  z Description of the Parameter
     */
    public SFVec3f(float x, float y, float z) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFVec3f(x, y, z);
        implField = impl;
    }

    /**
     *  Gets the value attribute of the SFVec3f object
     *
     *@param  vec Description of the Parameter
     */
    public void getValue(float[] vec) {
        impl.getValue(vec);
    }

    /**
     *  Gets the value attribute of the SFVec3f object
     *
     *@return  The value value
     */
    public float[] getValue() {
        return impl.getValue();
    }

    /**
     *  Gets the x attribute of the SFVec3f object
     *
     *@return  The x value
     */
    public float getX() {
        return impl.getX();
    }

    /**
     *  Gets the y attribute of the SFVec3f object
     *
     *@return  The y value
     */
    public float getY() {
        return impl.getY();
    }

    /**
     *  Gets the z attribute of the SFVec3f object
     *
     *@return  The z value
     */
    public float getZ() {
        return impl.getZ();
    }

    /**
     *  Sets the value attribute of the SFVec3f object
     *
     *@param  v The new value value
     */
    public void setValue(float[] v) {
        impl.setValue(v);
    }

    /**
     *  Sets the value attribute of the SFVec3f object
     *
     *@param  x The new value value
     *@param  y The new value value
     *@param  z The new value value
     */
    public void setValue(float x, float y, float z) {
        impl.setValue(x, y, z);
    }

    /**
     *  Sets the value attribute of the SFVec3f object
     *
     *@param  v The new value value
     */
    public void setValue(ConstSFVec3f v) {
        impl.setValue(v.impl);
    }

    /**
     *  Sets the value attribute of the SFVec3f object
     *
     *@param  v The new value value
     */
    public void setValue(SFVec3f v) {
        impl.setValue(v.impl);
    }

}

