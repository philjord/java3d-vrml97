/*
 * $RCSfile: SpotLight.java,v $
 *
 *      @(#)SpotLight.java 1.17 98/11/05 20:35:23
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
 * $Date: 2005/02/03 23:07:02 $
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
import org.jogamp.java3d.SharedGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

/**  Description of the Class */
public class SpotLight extends Light {

    // exposedField

    SFFloat ambientIntensity;
    SFVec3f attenuation;
    SFFloat beamWidth;
    SFColor color;
    SFFloat cutOffAngle;
    SFVec3f direction;
    SFFloat intensity;
    SFVec3f location;
    SFBool on;
    SFFloat radius;

    Color3f lightColor;
    Point3f lightPos;
    Vector3f lightDir;
    Point3f lightAtt;
    BoundingSphere bounds;
    org.jogamp.java3d.SpotLight spotLight;


    /**
     *Constructor for the SpotLight object
     *
     *@param  loader Description of the Parameter
     */
    public SpotLight(Loader loader) {
        super(loader);
        ambientIntensity = new SFFloat(0.0f);
        attenuation = new SFVec3f(1.0f, 0.0f, 0.0f);
        beamWidth = new SFFloat(1.570796f);
        color = new SFColor(1.0f, 1.0f, 1.0f);
        cutOffAngle = new SFFloat(.785398f);
        direction = new SFVec3f(0.0f, 0.0f, -1.0f);
        intensity = new SFFloat(1.0f);
        location = new SFVec3f(0.0f, 0.0f, 0.0f);
        on = new SFBool(true);
        radius = new SFFloat(100.0f);

        initFields();
    }

    /**
     *Constructor for the SpotLight object
     *
     *@param  loader Description of the Parameter
     *@param  ambientIntensity Description of the Parameter
     *@param  attenuation Description of the Parameter
     *@param  color Description of the Parameter
     *@param  cutOffAngle Description of the Parameter
     *@param  direction Description of the Parameter
     *@param  intensity Description of the Parameter
     *@param  on Description of the Parameter
     *@param  radius Description of the Parameter
     */
    SpotLight(Loader loader, SFFloat ambientIntensity, SFVec3f attenuation,
            SFColor color, SFFloat cutOffAngle,
            SFVec3f direction, SFFloat intensity,
            SFBool on, SFFloat radius) {
        super(loader);
        this.ambientIntensity = ambientIntensity;
        this.attenuation = attenuation;
        this.color = color;
        this.cutOffAngle = cutOffAngle;
        this.direction = direction;
        this.intensity = intensity;
        this.on = on;

        initFields();
    }


    /**  Description of the Method */
    void initImpl() {
        lightColor = new Color3f();
        lightPos = new Point3f();
        lightDir = new Vector3f();
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
        lightDir.x = direction.value[0];
        lightDir.y = direction.value[1];
        lightDir.z = direction.value[2];
        lightAtt.x = attenuation.value[0];
        lightAtt.y = attenuation.value[1];
        lightAtt.z = attenuation.value[2];

        // VRML and Java3D disconnect on the spread angle.  VRML uses two
        // angles: beamWidth and curOffAngle:
        //   if (angle > cutOffAngle) {
        //      multiplier = 0.0;
        //   } else if (angle <= beamWidth) {
        //      multiplier = 1.0;
        //   } else {
        //      multiplier = (angle - cutOffAngle) / (beamWidth - cutOffAngle);
        //   }
        //
        // Java3D has only one angle with a exponential concentration:
        //   if (angle > spreadAngle) {
        //      multiplier = 0.0;
        //   } else {
        //      multiplier = cos(angle) ** concentration;
        //   }
        // TODO: look for an optimal mapping for now just use 0.0 for
        // concentration (no attenuation)
        //
        light = spotLight = new org.jogamp.java3d.SpotLight(on.value, lightColor,
                lightPos, lightAtt, lightDir, cutOffAngle.value, 0.0f);
        spotLight.setInfluencingBounds(bounds);
        sharedGroup.addChild(spotLight);
        implReady = true;
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
            spotLight.setColor(lightColor);
        }
        else if (eventInName.equals("location")) {
            lightPos.x = location.value[0];
            lightPos.y = location.value[1];
            lightPos.z = location.value[2];
            spotLight.setPosition(lightPos);
        }
        else if (eventInName.equals("attenuation")) {
            lightAtt.x = attenuation.value[0];
            lightAtt.y = attenuation.value[1];
            lightAtt.z = attenuation.value[2];
            spotLight.setAttenuation(lightAtt);
        }
        else if (eventInName.equals("direction")) {
            lightDir.x = direction.value[0];
            lightDir.y = direction.value[1];
            lightDir.z = direction.value[2];
            spotLight.setDirection(lightDir);
        }
        else if (eventInName.equals("cutOffAngle")) {
            spotLight.setSpreadAngle(cutOffAngle.value);
        }
        else if (eventInName.equals("beamWidth")) {
            // no mapping yet
        }
        else if (eventInName.equals("on")) {
            ambLight.setEnable(on.value);
            spotLight.setEnable(on.value);
        }
        else if (eventInName.equals("radius")) {
            bounds.setRadius((double) radius.value);
            ambLight.setInfluencingBounds(bounds);
            spotLight.setInfluencingBounds(bounds);
        }
        else if (eventInName.equals("route_on")) {
            ambLight.setCapability(org.jogamp.java3d.Light.ALLOW_STATE_WRITE);
            spotLight.setCapability(org.jogamp.java3d.Light.ALLOW_STATE_WRITE);
        }
        else if (eventInName.equals("route_location")) {
            spotLight.setCapability(
                    org.jogamp.java3d.SpotLight.ALLOW_POSITION_WRITE);
        }
        else if (eventInName.equals("route_direction")) {
            spotLight.setCapability(
                    org.jogamp.java3d.SpotLight.ALLOW_DIRECTION_WRITE);
        }
        else if (eventInName.equals("route_cutOffAngle")) {
            spotLight.setCapability(
                    org.jogamp.java3d.SpotLight.ALLOW_SPREAD_ANGLE_WRITE);
        }
        else if (eventInName.equals("route_beamWidth")) {
            spotLight.setCapability(
                    org.jogamp.java3d.SpotLight.ALLOW_CONCENTRATION_WRITE);
        }
        else if (eventInName.equals("route_attenuation")) {
            spotLight.setCapability(
                    org.jogamp.java3d.SpotLight.ALLOW_ATTENUATION_WRITE);
        }
        else if (eventInName.equals("route_color") ||
                eventInName.equals("route_intensity")) {
            ambLight.setCapability(org.jogamp.java3d.Light.ALLOW_COLOR_WRITE);
            spotLight.setCapability(org.jogamp.java3d.Light.ALLOW_COLOR_WRITE);
        }
        else if (eventInName.equals("route_ambientIntensity")) {
            ambLight.setCapability(org.jogamp.java3d.Light.ALLOW_COLOR_WRITE);
        }
        else if (eventInName.equals("route_radius")) {
            ambLight.setCapability(
                    org.jogamp.java3d.Light.ALLOW_INFLUENCING_BOUNDS_WRITE);
            spotLight.setCapability(
                    org.jogamp.java3d.Light.ALLOW_INFLUENCING_BOUNDS_WRITE);
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new SpotLight(loader, (SFFloat) ambientIntensity.clone(),
                (SFVec3f) attenuation.clone(), (SFColor) color.clone(),
                (SFFloat) cutOffAngle.clone(), (SFVec3f) direction.clone(),
                (SFFloat) intensity.clone(), (SFBool) on.clone(),
                (SFFloat) radius.clone());
    }

    /**
     *  Gets the type attribute of the SpotLight object
     *
     *@return  The type value
     */
    public String getType() {
        return "SpotLight";
    }


    /**  Description of the Method */
    void initFields() {
        ambientIntensity.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "ambientIntensity");
        attenuation.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "attenuation");
        beamWidth.init(this, FieldSpec, Field.EXPOSED_FIELD, "beamWidth");
        color.init(this, FieldSpec, Field.EXPOSED_FIELD, "color");
        cutOffAngle.init(this, FieldSpec, Field.EXPOSED_FIELD, "cutOffAngle");
        direction.init(this, FieldSpec, Field.EXPOSED_FIELD, "direction");
        intensity.init(this, FieldSpec, Field.EXPOSED_FIELD, "intensity");
        location.init(this, FieldSpec, Field.EXPOSED_FIELD, "location");
        on.init(this, FieldSpec, Field.EXPOSED_FIELD, "on");
        radius.init(this, FieldSpec, Field.EXPOSED_FIELD, "radius");
    }
}

