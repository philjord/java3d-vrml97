/*
 * $RCSfile: NormalInterpolator.java,v $
 *
 *      @(#)NormalInterpolator.java 1.16 98/11/05 20:34:48
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
 * $Date: 2005/02/03 23:06:59 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
// TODO: interp the normals on the unit sphere

package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class NormalInterpolator extends Interpolator {

    // eventIn
    SFFloat fraction;
    // exposedField
    // MFFloat key; // From Interpolator
    MFVec3f keyValue;
    // eventOut
    MFVec3f value;

    /**
     *Constructor for the NormalInterpolator object
     *
     *@param  loader Description of the Parameter
     */
    public NormalInterpolator(Loader loader) {
        super(loader);
        fraction = new SFFloat(0.0f);

        //there should be empty constructors for all fields.
        key = new MFFloat();
        keyValue = new MFVec3f();

        initFields();

    }

    /**
     *Constructor for the NormalInterpolator object
     *
     *@param  loader Description of the Parameter
     *@param  fraction Description of the Parameter
     *@param  key Description of the Parameter
     *@param  keyValue Description of the Parameter
     */
    public NormalInterpolator(Loader loader, SFFloat fraction,
            MFFloat key, MFVec3f keyValue) {

        super(loader);
        this.fraction = new SFFloat(0.0f);

        //there should be empty constructors for all fields.
        this.key = key;
        this.keyValue = keyValue;

        initFields();
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
                int valsPerKey = keyValue.size / key.mfloat.length;
                if (value.size != (valsPerKey * 3)) {
                    value.checkSize(valsPerKey * 3, false);
                    value.size = valsPerKey * 3;
                }

                for (int j = 0; j < valsPerKey; j++) {
                    int val1 = iL * valsPerKey * 3;
                    int val2 = (iL + 1) * valsPerKey * 3;
                    float v1x = keyValue.value[(val1 + j) * 3 + 0];
                    float v1y = keyValue.value[(val1 + j) * 3 + 1];
                    float v1z = keyValue.value[(val1 + j) * 3 + 2];
                    float v2x = keyValue.value[(val2 + j) * 3 + 0];
                    float v2y = keyValue.value[(val2 + j) * 3 + 1];
                    float v2z = keyValue.value[(val2 + j) * 3 + 2];
                    value.value[j * 3 + 0] = (v1x * af) + (v2x * f);
                    value.value[j * 3 + 1] = (v1y * af) + (v2y * f);
                    value.value[j * 3 + 2] = (v1z * af) + (v2z * f);
                }
            }
            value.route();
        }

    }

    /**
     *  Gets the type attribute of the NormalInterpolator object
     *
     *@return  The type value
     */
    public String getType() {
        return "NormalInterpolator";
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new NormalInterpolator(loader, (SFFloat) fraction.clone(),
                (MFFloat) key, (MFVec3f) keyValue);
    }

    /**  Description of the Method */
    void initFields() {
        fraction.init(this, FieldSpec, Field.EVENT_IN, "fraction");
        key.init(this, FieldSpec, Field.EXPOSED_FIELD, "key");
        keyValue.init(this, FieldSpec, Field.EXPOSED_FIELD, "keyValue");
        value.init(this, FieldSpec, Field.EVENT_OUT, "value");
    }

}

