/*
 *      @(#)PointLight.java 1.17 98/11/05 20:34:53
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
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Link;

import org.jogamp.java3d.SharedGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3f;

/**  Description of the Class */
public class PointLight extends Light {

    SFFloat ambientIntensity;
    SFVec3f attenuation;
    SFColor color;
    SFFloat intensity;
    SFVec3f location;
    SFBool on;
    SFFloat radius;

    Color3f lightColor;
    Point3f lightPos;
    Point3f lightAtt;
    BoundingSphere bounds;
    org.jogamp.java3d.PointLight pntLight;

    /**
     *Constructor for the PointLight object
     *
     *@param  loader Description of the Parameter
     */
    public PointLight(Loader loader) {
        super(loader);

        ambientIntensity = new SFFloat(0.0f);
        attenuation = new SFVec3f(1.0f, 0.0f, 0.0f);
        color = new SFColor(1.0f, 1.0f, 1.0f);
        intensity = new SFFloat(1.0f);
        location = new SFVec3f(0.0f, 0.0f, 0.0f);
        on = new SFBool(true);
        radius = new SFFloat(100f);

        initFields();
    }

    /**
     *Constructor for the PointLight object
     *
     *@param  loader Description of the Parameter
     *@param  ambientIntensity Description of the Parameter
     *@param  attenuation Description of the Parameter
     *@param  color Description of the Parameter
     *@param  intensity Description of the Parameter
     *@param  location Description of the Parameter
     *@param  on Description of the Parameter
     *@param  radius Description of the Parameter
     */
    PointLight(Loader loader, SFFloat ambientIntensity, SFVec3f attenuation,
            SFColor color, SFFloat intensity, SFVec3f location, SFBool on,
            SFFloat radius) {

        super(loader);
        this.ambientIntensity = ambientIntensity;
        this.attenuation = attenuation;
        this.color = color;
        this.intensity = intensity;
        this.location = location;
        this.on = on;
        this.radius = radius;

        initFields();
    }


    /**  Description of the Method */
    void initImpl() {
        lightColor = new Color3f();
        lightPos = new Point3f();
        lightAtt = new Point3f();
        bounds = new BoundingSphere();
        bounds.setRadius((double) radius.value);
        sharedGroup = new SharedGroup();
        lightColor.x = color.color[0] * ambientIntensity.value;
        lightColor.y = color.color[1] * ambientIntensity.value;
        lightColor.z = color.color[2] * ambientIntensity.value;
        ambLight = new AmbientLight(on.value, lightColor);
        ambLight.setInfluencingBounds(bounds);
        sharedGroup.addChild(ambLight);
        lightColor.x = color.color[0] * intensity.value;
        lightColor.y = color.color[1] * intensity.value;
        lightColor.z = color.color[2] * intensity.value;
        lightPos.x = location.value[0];
        lightPos.y = location.value[1];
        lightPos.z = location.value[2];
        lightAtt.x = attenuation.value[0];
        lightAtt.y = attenuation.value[1];
        lightAtt.z = attenuation.value[2];
        light = pntLight = new org.jogamp.java3d.PointLight(on.value, lightColor,
                lightPos, lightAtt);
        pntLight.setInfluencingBounds(bounds);
        sharedGroup.addChild(pntLight);
        implReady = true;
    }


    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("AmbientIntensity")) {
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
            pntLight.setColor(lightColor);
        }
        else if (eventInName.equals("location")) {
            lightPos.x = location.value[0];
            lightPos.y = location.value[1];
            lightPos.z = location.value[2];
            pntLight.setPosition(lightPos);
        }
        else if (eventInName.equals("attenuation")) {
            lightAtt.x = attenuation.value[0];
            lightAtt.y = attenuation.value[1];
            lightAtt.z = attenuation.value[2];
            pntLight.setAttenuation(lightAtt);
        }
        else if (eventInName.equals("on")) {
            ambLight.setEnable(on.value);
            pntLight.setEnable(on.value);
        }
        else if (eventInName.equals("radius")) {
            bounds.setRadius((double) radius.value);
            ambLight.setInfluencingBounds(bounds);
            pntLight.setInfluencingBounds(bounds);
        }
        else if (eventInName.equals("route_on")) {
            ambLight.setCapability(org.jogamp.java3d.Light.ALLOW_STATE_WRITE);
            pntLight.setCapability(org.jogamp.java3d.Light.ALLOW_STATE_WRITE);
        }
        else if (eventInName.equals("route_location")) {
            pntLight.setCapability(
                    org.jogamp.java3d.PointLight.ALLOW_POSITION_WRITE);
        }
        else if (eventInName.equals("route_attenuation")) {
            pntLight.setCapability(
                    org.jogamp.java3d.PointLight.ALLOW_ATTENUATION_WRITE);
        }
        else if (eventInName.equals("route_color") ||
                eventInName.equals("route_intensity")) {
            ambLight.setCapability(org.jogamp.java3d.Light.ALLOW_COLOR_WRITE);
            pntLight.setCapability(org.jogamp.java3d.Light.ALLOW_COLOR_WRITE);
        }
        else if (eventInName.equals("route_ambientIntensity")) {
            ambLight.setCapability(org.jogamp.java3d.Light.ALLOW_COLOR_WRITE);
        }
        else if (eventInName.equals("route_radius")) {
            ambLight.setCapability(
                    org.jogamp.java3d.Light.ALLOW_INFLUENCING_BOUNDS_WRITE);
            pntLight.setCapability(
                    org.jogamp.java3d.Light.ALLOW_INFLUENCING_BOUNDS_WRITE);
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {

        PointLight l = new PointLight(loader,
                (SFFloat) ambientIntensity.clone(),
                (SFVec3f) attenuation.clone(),
                (SFColor) color.clone(),
                (SFFloat) intensity.clone(),
                (SFVec3f) location.clone(),
                (SFBool) on.clone(),
                (SFFloat) radius.clone());
        return l;
    }

    /**
     *  Gets the type attribute of the PointLight object
     *
     *@return  The type value
     */
    public String getType() {
        return "PointLight";
    }

    /**  Description of the Method */
    void initFields() {
        ambientIntensity.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "ambientIntensity");
        attenuation.init(this, FieldSpec, Field.EXPOSED_FIELD, "attenuation");
        color.init(this, FieldSpec, Field.EXPOSED_FIELD, "color");
        intensity.init(this, FieldSpec, Field.EXPOSED_FIELD, "intensity");
        location.init(this, FieldSpec, Field.EXPOSED_FIELD, "location");
        on.init(this, FieldSpec, Field.EXPOSED_FIELD, "on");
        radius.init(this, FieldSpec, Field.EXPOSED_FIELD, "radius");
    }
}

