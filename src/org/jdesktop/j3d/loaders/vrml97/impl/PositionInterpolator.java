/*
 * $RCSfile: PositionInterpolator.java,v $
 *
 *      @(#)PositionInterpolator.java 1.18 98/11/05 20:34:54
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
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class PositionInterpolator extends Interpolator {

    // eventIn
    SFFloat fraction;

    // exposedField
    // MFFloat key; // from Interpolator
    MFVec3f keyValue;

    // eventOut

    // spec goes its value_changed, but we need to have a value first
    SFVec3f value;

    float[] v1 = new float[3];
    float[] v2 = new float[3];

    /**
     *Constructor for the PositionInterpolator object
     *
     *@param  loader Description of the Parameter
     */
    public PositionInterpolator(Loader loader) {
        super(loader);
        fraction = new SFFloat(0.0f);
        key = new MFFloat();
        keyValue = new MFVec3f();
        value = new SFVec3f(0.0f, 0.0f, 0.0f);
        initFields();
    }


    /**
     *Constructor for the PositionInterpolator object
     *
     *@param  loader Description of the Parameter
     *@param  key Description of the Parameter
     *@param  keyValue Description of the Parameter
     */
    PositionInterpolator(Loader loader, MFFloat key, MFVec3f keyValue) {
        super(loader);
        // initialize the interpolator to the right
        // value. otherwise do it manually;
        this.fraction = new SFFloat(0.0f);
        this.key = key;
        this.keyValue = keyValue;
        this.value = new SFVec3f(0.0f, 0.0f, 1.0f);// may need to cycle
        // through one interp
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
                // TODO: (Rick) reevaluate performance trade off between
                // MFVec3f{->}float[] vs MFVec3f{->}SFVec3f[], since
                // runtime efficiency is being spent to gain parse/init
                // efficiency which is usually only going to happen once.
                // DDG: As coded, the overhead is minimal.
                // Also, having the array be of SFVec3f objects adds to the
                // memory overhead as well as the accessing overhead.
                // Bottom line:
                // SFVec3f[] is much easier to read and maintain, float[] is
                // leaner and closer to j3d.
                //
                int v0Base = iL * 3;
                int v1Base = (iL + 1) * 3;
                v1[0] = keyValue.value[v0Base];
                v1[1] = keyValue.value[v0Base + 1];
                v1[2] = keyValue.value[v0Base + 2];
                v2[0] = keyValue.value[v1Base];
                v2[1] = keyValue.value[v1Base + 1];
                v2[2] = keyValue.value[v1Base + 2];
                value.value[0] = (v1[0] * af) + (v2[0] * f);
                value.value[1] = (v1[1] * af) + (v2[1] * f);
                value.value[2] = (v1[2] * af) + (v2[2] * f);
            }
            value.route();
        }

    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new PositionInterpolator(loader, (MFFloat) key.clone(),
                (MFVec3f) keyValue.clone());
    }

    /**
     *  Gets the type attribute of the PositionInterpolator object
     *
     *@return  The type value
     */
    public String getType() {
        return "PositionInterpolator";
    }

    /**  Description of the Method */
    void initFields() {
        fraction.init(this, FieldSpec, Field.EVENT_IN, "fraction");
        key.init(this, FieldSpec, Field.EXPOSED_FIELD, "key");
        keyValue.init(this, FieldSpec, Field.EXPOSED_FIELD, "keyValue");
        value.init(this, FieldSpec, Field.EVENT_OUT, "value");
    }

}

