/*
 * $RCSfile: SFFloat.java,v $
 *
 *      @(#)SFFloat.java 1.13 98/11/05 20:35:00
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

// SF as opposed to a SJFloat?

// Single Java float seeks eventIn, for
// dinner, dancing, and el camino real big numbers.
// no clones or children.

/**  Description of the Class */
public class SFFloat extends Field {

    float value;
    float initValue;

    /**
     *Constructor for the SFFloat object
     *
     *@param  f Description of the Parameter
     */
    public SFFloat(float f) {
        value = f;
        initValue = f;
    }

    /**  Description of the Method */
    void reset() {
        value = initValue;
    }

    /**
     *  Gets the value attribute of the SFFloat object
     *
     *@return  The value value
     */
    public float getValue() {
        return value;
    }

    /**
     *  Sets the value attribute of the SFFloat object
     *
     *@param  f The new value value
     */
    public void setValue(float f) {
        value = f;
        route();
    }

    /**
     *  Sets the value attribute of the SFFloat object
     *
     *@param  f The new value value
     */
    public void setValue(ConstSFFloat f) {
        value = f.getValue();
        route();
    }

    /**
     *  Sets the value attribute of the SFFloat object
     *
     *@param  f The new value value
     */
    public void setValue(SFFloat f) {
        value = f.value;
        route();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFFloat(value);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((SFFloat) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstSFFloat(this);
        }
        return constField;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.SFFloat(this);
    }

}

