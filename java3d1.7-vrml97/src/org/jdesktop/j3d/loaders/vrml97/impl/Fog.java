/*
 * $RCSfile: Fog.java,v $
 *
 *      @(#)Fog.java 1.19 98/11/05 20:34:28
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
 * $Date: 2005/02/03 23:06:55 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.ExponentialFog;
import org.jogamp.java3d.LinearFog;

/**  Description of the Class */
public class Fog extends BindableNode {

    // In BindableNode
    // eventIn
    //SFBool set_bind;
    // eventOut
    //SFBool isBound;

    // exposedField
    SFColor color;
    SFString fogType;
    SFFloat visibilityRange;

    // The fog does not have an impl which gets added to the tree directly
    // so implNode=null

    // When the fog is bound, it's fogImpl is attached to the browserRoot
    org.jogamp.java3d.BranchGroup fogImpl;
    org.jogamp.java3d.Fog fog;

    /**
     *Constructor for the Fog object
     *
     *@param  loader Description of the Parameter
     */
    public Fog(Loader loader) {
        super(loader, loader.getFogStack());

        // exposedFields
        color = new SFColor(1.0f, 1.0f, 1.0f);
        fogType = new SFString("LINEAR");
        visibilityRange = new SFFloat(0.0f);

        loader.addFog(this);
        initFields();

    }

    /**
     *Constructor for the Fog object
     *
     *@param  loader Description of the Parameter
     *@param  bind Description of the Parameter
     *@param  bindTime Description of the Parameter
     *@param  isBound Description of the Parameter
     *@param  color Description of the Parameter
     *@param  fogType Description of the Parameter
     *@param  visibilityRange Description of the Parameter
     */
    Fog(Loader loader, SFBool bind, SFTime bindTime, SFBool isBound,
            SFColor color, SFString fogType, SFFloat visibilityRange) {
        super(loader, loader.getFogStack(), bind, bindTime, isBound);

        this.color = color;
        this.fogType = fogType;
        this.visibilityRange = visibilityRange;

        loader.addFog(this);
        initFields();
    }

    /**  Description of the Method */
    public void initImpl() {
        //TODO: add event handlers for fogType eventIns.
        //TOTO: move setCapabilities() event handler to be set only if ROUTE
        if (fogType.getValue().equals("LINEAR")) {
            LinearFog linearFog = new LinearFog(color.color[0],
                    color.color[1], color.color[2]);
            linearFog.setCapability(LinearFog.ALLOW_DISTANCE_WRITE);
            linearFog.setBackDistance(visibilityRange.getValue());
            linearFog.setFrontDistance(visibilityRange.getValue() / 10.0);
            fog = linearFog;
        }
        else {
            ExponentialFog expFog = new ExponentialFog(color.color[0],
                    color.color[1], color.color[2]);
            expFog.setCapability(ExponentialFog.ALLOW_DENSITY_WRITE);
            expFog.setDensity(visibilityRange.getValue());
            fog = expFog;
        }

        // TODO: do only if ROUTE
        fog.setCapability(org.jogamp.java3d.Fog.ALLOW_COLOR_READ);
        fog.setCapability(org.jogamp.java3d.Fog.ALLOW_COLOR_WRITE);

        fogImpl = new RGroup();
        if (visibilityRange.getValue() == 0.0) {
            fog.setInfluencingBounds(loader.zeroBounds);
        }
        else {
            fog.setInfluencingBounds(loader.infiniteBounds);
        }
        fogImpl.addChild(fog);
        implReady = true;
    }

    /**
     *  Gets the fogImpl attribute of the Fog object
     *
     *@return  The fogImpl value
     */
    public org.jogamp.java3d.BranchGroup getFogImpl() {
        return fogImpl;
    }


    /**  Description of the Method */
    public void initFields() {
        initBindableFields();
        color.init(this, FieldSpec, Field.EXPOSED_FIELD, "color");
        fogType.init(this, FieldSpec, Field.EXPOSED_FIELD, "fogType");
        visibilityRange.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "visibilityRange");
    }


    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Fog(loader, (SFBool) bind.clone(), (SFTime) bindTime,
                (SFBool) isBound, (SFColor) color.clone(),
                (SFString) fogType.clone(), (SFFloat) visibilityRange.clone());
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.BaseNode wrap() {
        return new org.jdesktop.j3d.loaders.vrml97.node.Fog(this);
    }

    /**
     *  Gets the type attribute of the Fog object
     *
     *@return  The type value
     */
    public String getType() {
        return "Fog";
    }

}

