/*
 * $RCSfile: ColorInterpolator.java,v $
 *
 *      @(#)ColorInterpolator.java 1.20 98/11/05 20:34:08
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
 * $Date: 2005/02/03 23:06:52 $
 * $State: Exp $
 */
/*
 *@Author:  Rick Goldberg
 *@Author:  Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class ColorInterpolator extends Interpolator {
    // eventIn
    SFFloat fraction;

    // exposedField
    // MFFloat key; // From Interpolator
    MFColor keyValue;

    // eventOut

    // spec goes its value_changed, but we need to have a value first
    SFColor value;

    /**
     *Constructor for the ColorInterpolator object
     *
     *@param  loader Description of the Parameter
     */
    public ColorInterpolator(Loader loader) {
        super(loader);
        fraction = new SFFloat(0.0f);
        key = new MFFloat();
        keyValue = new MFColor();
        value = new SFColor(1.0f, 1.0f, 1.0f);
        initFields();
    }


    /**
     *Constructor for the ColorInterpolator object
     *
     *@param  loader Description of the Parameter
     *@param  key Description of the Parameter
     *@param  keyValue Description of the Parameter
     */
    ColorInterpolator(Loader loader, MFFloat key, MFColor keyValue) {
        super(loader);
        // initialize the interpolator to the right
        // value. otherwise do it manually;
        this.fraction = new SFFloat(0.0f);
        this.key = key;
        this.keyValue = keyValue;
        this.value = new SFColor(1.0f, 1.0f, 1.0f);// may need to cycle
        // through one interp
        initFields();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new ColorInterpolator(loader, (MFFloat) key.clone(),
                (MFColor) keyValue.clone());
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("fraction")) {
            if (key.mfloat.length > 0) {
                setIndexFract(fraction.value);
                float v1r = keyValue.vals[iL * 3 + 0];
                float v1g = keyValue.vals[iL * 3 + 1];
                float v1b = keyValue.vals[iL * 3 + 2];
                float v2r = keyValue.vals[(iL + 1) * 3 + 0];
                float v2g = keyValue.vals[(iL + 1) * 3 + 1];
                float v2b = keyValue.vals[(iL + 1) * 3 + 2];
                value.color[0] = (v1r * af) + (v2r * f);
                value.color[1] = (v1g * af) + (v2g * f);
                value.color[2] = (v1b * af) + (v2b * f);
            }
            value.route();
        }
    }

    /**
     *  Gets the type attribute of the ColorInterpolator object
     *
     *@return  The type value
     */
    public String getType() {
        return "ColorInterpolator";
    }

    /**  Description of the Method */
    void initFields() {
        fraction.init(this, FieldSpec, Field.EVENT_IN, "fraction");
        key.init(this, FieldSpec, Field.EXPOSED_FIELD, "key");
        keyValue.init(this, FieldSpec, Field.EXPOSED_FIELD, "keyValue");
        value.init(this, FieldSpec, Field.EVENT_OUT, "value");
    }

}

