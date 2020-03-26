/*
 * $RCSfile: DirectionalLight.java,v $
 *
 *      @(#)DirectionalLight.java 1.19 98/11/05 20:34:23
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
 *@Author:  Rick Goldberg
 *@Author:  Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.BoundingSphere;

import org.jogamp.java3d.SharedGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

/**  Description of the Class */
public class DirectionalLight extends Light {

    // exposedField
    SFFloat ambientIntensity;
    SFColor color;
    SFVec3f direction;
    SFFloat intensity;
    SFBool on;

    Color3f lightColor;
    Vector3f lightDir;
    org.jogamp.java3d.DirectionalLight dirLight;

    /**
     *Constructor for the DirectionalLight object
     *
     *@param  loader Description of the Parameter
     */
    public DirectionalLight(Loader loader) {
        super(loader);
        ambientIntensity = new SFFloat(0.0f);
        color = new SFColor(1.0f, 1.0f, 1.0f);
        direction = new SFVec3f(0.0f, 0.0f, -1.0f);
        intensity = new SFFloat(1.0f);
        on = new SFBool(true);

        initFields();
    }

    /**
     *Constructor for the DirectionalLight object
     *
     *@param  loader Description of the Parameter
     *@param  ambientIntensity Description of the Parameter
     *@param  color Description of the Parameter
     *@param  direction Description of the Parameter
     *@param  intensity Description of the Parameter
     *@param  on Description of the Parameter
     */
    DirectionalLight(Loader loader, SFFloat ambientIntensity, SFColor color,
            SFVec3f direction, SFFloat intensity, SFBool on) {
        super(loader);
        this.ambientIntensity = ambientIntensity;
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
        this.on = on;

        initFields();
    }

    /**  Description of the Method */
    void initImpl() {
        lightColor = new Color3f();
        lightDir = new Vector3f();
        sharedGroup = new SharedGroup();
        lightColor.x = color.color[0] * ambientIntensity.value;
        lightColor.y = color.color[1] * ambientIntensity.value;
        lightColor.z = color.color[2] * ambientIntensity.value;
        ambLight = new AmbientLight(on.value, lightColor);
        sharedGroup.addChild(ambLight);
        lightColor.x = color.color[0] * intensity.value;
        lightColor.y = color.color[1] * intensity.value;
        lightColor.z = color.color[2] * intensity.value;
        lightDir.x = direction.value[0];
        lightDir.y = direction.value[1];
        lightDir.z = direction.value[2];
        light = dirLight = new org.jogamp.java3d.DirectionalLight(on.value,
                lightColor, lightDir);
        ambLight.setInfluencingBounds(loader.infiniteBounds);
        dirLight.setInfluencingBounds(loader.infiniteBounds);
        sharedGroup.addChild(dirLight);
        implReady = true;
    }

    /**
     *  Sets the scope attribute of the DirectionalLight object
     *
     *@param  scopeGroup The new scope value
     */
    void setScope(org.jogamp.java3d.Group scopeGroup) {
        ambLight.addScope(scopeGroup);
        dirLight.addScope(scopeGroup);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new DirectionalLight(loader, (SFFloat) ambientIntensity.clone(),
                (SFColor) color.clone(), (SFVec3f) direction.clone(),
                (SFFloat) intensity.clone(), (SFBool) on.clone());
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("ambientIntensity")) {
            lightColor.x = color.color[0] * ambientIntensity.value;
            lightColor.y = color.color[1] * ambientIntensity.value;
            lightColor.z = color.color[2] * ambientIntensity.value;
            ambLight.setColor(lightColor);
        }
        else if (eventInName.equals("color") ||
                eventInName.equals("intensity")) {
            lightColor.x = color.color[0] * ambientIntensity.value;
            lightColor.y = color.color[1] * ambientIntensity.value;
            lightColor.z = color.color[2] * ambientIntensity.value;
            ambLight.setColor(lightColor);
            lightColor.x = color.color[0] * intensity.value;
            lightColor.y = color.color[1] * intensity.value;
            lightColor.z = color.color[2] * intensity.value;
            dirLight.setColor(lightColor);
        }
        else if (eventInName.equals("direction")) {
            lightDir.x = direction.value[0];
            lightDir.y = direction.value[1];
            lightDir.z = direction.value[2];
            dirLight.setDirection(lightDir);
        }
        else if (eventInName.equals("on")) {
            ambLight.setEnable(on.value);
            dirLight.setEnable(on.value);
        }
        else if (eventInName.equals("route_on")) {
            ambLight.setCapability(org.jogamp.java3d.Light.ALLOW_STATE_WRITE);
            dirLight.setCapability(org.jogamp.java3d.Light.ALLOW_STATE_WRITE);
        }
        else if (eventInName.equals("route_direction")) {
            dirLight.setCapability(
                    org.jogamp.java3d.DirectionalLight.ALLOW_DIRECTION_WRITE);
        }
        else if (eventInName.equals("route_color") ||
                eventInName.equals("route_intensity")) {
            ambLight.setCapability(org.jogamp.java3d.Light.ALLOW_COLOR_WRITE);
            dirLight.setCapability(org.jogamp.java3d.Light.ALLOW_COLOR_WRITE);
        }
        else if (eventInName.equals("route_ambientIntensity")) {
            ambLight.setCapability(org.jogamp.java3d.Light.ALLOW_COLOR_WRITE);
        }
    }

    /**
     *  Gets the type attribute of the DirectionalLight object
     *
     *@return  The type value
     */
    public String getType() {
        return "DirectionalLight";
    }


    /**  Description of the Method */
    void initFields() {
        ambientIntensity.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "ambientIntensity");
        color.init(this, FieldSpec, Field.EXPOSED_FIELD, "color");
        direction.init(this, FieldSpec, Field.EXPOSED_FIELD, "direction");
        intensity.init(this, FieldSpec, Field.EXPOSED_FIELD, "intensity");
        on.init(this, FieldSpec, Field.EXPOSED_FIELD, "on");
    }
}

