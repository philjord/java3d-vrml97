/*
 * $RCSfile: Material.java,v $
 *
 *      @(#)Material.java 1.29 98/11/05 20:34:44
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
 * $Revision: 1.5 $
 * $Date: 2006/04/18 19:33:51 $
 * $State: Exp $
 */
/*
 * @author Rick Goldberg
 * @author Doug Gehringer
 * @author Nikolai V. Chr.
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

import java.beans.*;
import org.jogamp.vecmath.Color3f;

/**  Description of the Class */
public class Material extends Node {

    org.jogamp.java3d.Material impl;
    

    // exposedFields
    SFFloat ambientIntensity;
    SFColor diffuseColor;
    SFColor emissiveColor;
    SFFloat shininess;
    SFColor specularColor;
    SFFloat transparency;

	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	final public static String TRANSPARENCY = "transparency";
	
    Color3f ambColor = new Color3f();
    Color3f emmColor = new Color3f();
    Color3f diffColor = new Color3f();
    Color3f specColor = new Color3f();

    /**
     *Constructor for the Material object
     *
     *@param  loader Description of the Parameter
     */
    public Material(Loader loader) {
        super(loader);

        ambientIntensity = new SFFloat(0.2f);
        diffuseColor = new SFColor(0.8f, 0.8f, 0.8f);
        emissiveColor = new SFColor(0.0f, 0.0f, 0.0f);
        shininess = new SFFloat(0.2f);
        specularColor = new SFColor(0.0f, 0.0f, 0.0f);
        transparency = new SFFloat(0.0f);

        initFields();
    }

    /**
     *Constructor for the Material object
     *
     *@param  loader Description of the Parameter
     *@param  ambientIntensity Description of the Parameter
     *@param  diffuseColor Description of the Parameter
     *@param  emissiveColor Description of the Parameter
     *@param  shininess Description of the Parameter
     *@param  specularColor Description of the Parameter
     *@param  transparency Description of the Parameter
     */
    Material(Loader loader, SFFloat ambientIntensity, SFColor diffuseColor,
            SFColor emissiveColor, SFFloat shininess, SFColor specularColor,
            SFFloat transparency) {
        super(loader);

        this.ambientIntensity = ambientIntensity;
        this.diffuseColor = diffuseColor;
        this.emissiveColor = emissiveColor;
        this.shininess = shininess;
        this.specularColor = specularColor;
        this.transparency = transparency;

        initFields();
    }


    /**  Description of the Method */
    void initImpl() {
        float ambIntensity = ambientIntensity.getValue();
        ambColor.x = ambIntensity * diffuseColor.color[0];
        ambColor.y = ambIntensity * diffuseColor.color[1];
        ambColor.z = ambIntensity * diffuseColor.color[2];
        emmColor.x = emissiveColor.color[0];
        emmColor.y = emissiveColor.color[1];
        emmColor.z = emissiveColor.color[2];
        diffColor.x = diffuseColor.color[0];
        diffColor.y = diffuseColor.color[1];
        diffColor.z = diffuseColor.color[2];
        specColor.x = specularColor.color[0];
        specColor.y = specularColor.color[1];
        specColor.z = specularColor.color[2];
        float val = (shininess.value * 127.0f) + 1.0f;
        if (val > 127.0f) {
            val = 127.0f;
        }// should be 128.0f
        else if (val < 1.0f) {
            val = 1.0f;
        }
        impl = new org.jogamp.java3d.Material(ambColor, emmColor, diffColor,
                specColor, val);
		propertyChangeSupport.firePropertyChange(TRANSPARENCY, null, new Float(transparency.value));
        impl.setLightingEnable(true);
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {

        Material a = new Material(loader, (SFFloat) ambientIntensity.clone(),
                (SFColor) diffuseColor.clone(),
                (SFColor) emissiveColor.clone(),
                (SFFloat) shininess.clone(),
                (SFColor) specularColor.clone(),
                (SFFloat) transparency.clone());
        return a;
    }

    /**
     *  Gets the type attribute of the Material object
     *
     *@return  The type value
     */
    public String getType() {
        return "Material";
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        //System.out.println("Material.notifyMethod(): " + eventInName);
        if (eventInName.equals("ambientIntensity")) {
            float ambIntensity = ambientIntensity.value;
            ambColor.x = ambIntensity * diffuseColor.color[0];
            ambColor.y = ambIntensity * diffuseColor.color[1];
            ambColor.z = ambIntensity * diffuseColor.color[2];
            impl.setAmbientColor(ambColor);
        }
        else if (eventInName.equals("diffuseColor")) {
            float ambIntensity = ambientIntensity.value;
            ambColor.x = ambIntensity * diffuseColor.color[0];
            ambColor.y = ambIntensity * diffuseColor.color[1];
            ambColor.z = ambIntensity * diffuseColor.color[2];
            impl.setAmbientColor(ambColor);
            diffColor.x = diffuseColor.color[0];
            diffColor.y = diffuseColor.color[1];
            diffColor.z = diffuseColor.color[2];
            impl.setDiffuseColor(diffColor);
        }
        else if (eventInName.equals("emissiveColor")) {
            emmColor.x = emissiveColor.color[0];
            emmColor.y = emissiveColor.color[1];
            emmColor.z = emissiveColor.color[2];
            impl.setEmissiveColor(emmColor);
        }
        else if (eventInName.equals("shininess")) {
            float val = (shininess.value * 127.0f) + 1.0f;
            if (val > 128.0f) {
                val = 127.0f;
            }
            else if (val < 1.0f) {
                val = 1.0f;
            }
            impl.setShininess(val);
        }
        else if (eventInName.equals("specularColor")) {
            specColor.x = specularColor.color[0];
            specColor.y = specularColor.color[1];
            specColor.z = specularColor.color[2];
            impl.setSpecularColor(specColor);
        }
        else if (eventInName.equals("transparency")) {
            propertyChangeSupport.firePropertyChange(TRANSPARENCY, null, new Float(transparency.value));
        }
        else if (eventInName.equals("route_ambientIntensity") ||
                eventInName.equals("route_diffuseColor") ||
                eventInName.equals("route_emissiveColor") ||
                eventInName.equals("route_shininess") ||
                eventInName.equals("route_specularColor")) {
            impl.setCapability(org.jogamp.java3d.Material.ALLOW_COMPONENT_WRITE);
        }
        else if (eventInName.equals("route_transparency")) {
			//TODO: implement again
            //implTransp.setCapability(
            //        org.jogamp.java3d.TransparencyAttributes.ALLOW_MODE_WRITE);
        }
        else {
            System.err.println("Material: unknown eventInName " + eventInName);
        }
    }

	public float getTransparency() {
		return transparency.getValue();
	}
	
	public void addPropertyChangeListener(String n, PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(n, l);
	}
	
	public void removePropertyChangeListener(String n, PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(n, l);
	}
	
    /**  Description of the Method */
    void initFields() {
        ambientIntensity.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "ambientIntensity");
        diffuseColor.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "diffuseColor");
        emissiveColor.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "emissiveColor");
        shininess.init(this, FieldSpec, Field.EXPOSED_FIELD, "shininess");
        specularColor.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "specularColor");
        transparency.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "transparency");
    }

}

