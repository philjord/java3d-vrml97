/*
 * $RCSfile: ConstSFVec2f.java,v $
 *
 *      @(#)ConstSFVec2f.java 1.10 98/11/05 20:34:20
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
public class ConstSFVec2f extends ConstField {

    /**
     *Constructor for the ConstSFVec2f object
     *
     *@param  owner Description of the Parameter
     */
    ConstSFVec2f(SFVec2f owner) {
        super(owner);
    }

    /**
     *Constructor for the ConstSFVec2f object
     *
     *@param  values Description of the Parameter
     */
    ConstSFVec2f(float[] values) {
        super(new SFVec3f(values));
    }

    /**
     *  Gets the value attribute of the ConstSFVec2f object
     *
     *@param  vec Description of the Parameter
     */
    public void getValue(float[] vec) {
        ((SFVec2f) ownerField).getValue(vec);
    }

    /**
     *  Gets the value attribute of the ConstSFVec2f object
     *
     *@return  The value value
     */
    public float[] getValue() {
        return ((SFVec2f) ownerField).getValue();
    }

    /**
     *  Gets the x attribute of the ConstSFVec2f object
     *
     *@return  The x value
     */
    public float getX() {
        return ((SFVec2f) ownerField).getX();
    }

    /**
     *  Gets the y attribute of the ConstSFVec2f object
     *
     *@return  The y value
     */
    public float getY() {
        return ((SFVec2f) ownerField).getY();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new ConstSFVec2f((SFVec2f) ownerField);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.ConstSFVec2f(this);
    }

}

