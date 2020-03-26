/*
 * $RCSfile: ConstMFRotation.java,v $
 *
 *      @(#)ConstMFRotation.java 1.12 98/11/05 20:34:12
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
 * $Date: 2005/02/03 23:06:53 $
 * $State: Exp $
 */
/*
 *@Author:  Rick Goldberg
 *@Author:  Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class ConstMFRotation extends ConstMField {

    /**
     *Constructor for the ConstMFRotation object
     *
     *@param  owner Description of the Parameter
     */
    ConstMFRotation(MFRotation owner) {
        super(owner);
    }

    /**
     *Constructor for the ConstMFRotation object
     *
     *@param  values Description of the Parameter
     */
    public ConstMFRotation(float[][] values) {
        super(new MFRotation(values));
    }

    /**
     *Constructor for the ConstMFRotation object
     *
     *@param  values Description of the Parameter
     */
    public ConstMFRotation(float[] values) {
        super(new MFRotation(values));
    }

    /**
     *Constructor for the ConstMFRotation object
     *
     *@param  size Description of the Parameter
     *@param  values Description of the Parameter
     */
    public ConstMFRotation(int size, float[] values) {
        super(new MFRotation(size, values));
    }

    /**
     *  Gets the value attribute of the ConstMFRotation object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float[][] values) {
        ((MFRotation) ownerField).getValue(values);
    }

    /**
     *  Gets the value attribute of the ConstMFRotation object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float[] values) {
        ((MFRotation) ownerField).getValue(values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  values Description of the Parameter
     */
    public void get1Value(int index, float[] values) {
        ((MFRotation) ownerField).get1Value(index, values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void get1Value(int index, SFRotation vec) {
        ((MFRotation) ownerField).get1Value(index, vec);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new ConstMFRotation((MFRotation) ownerField);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.ConstMFRotation(this);
    }

}

