package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.SharedGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3f;

public class PointLight extends Light
{
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

  public PointLight(Loader loader)
  {
    super(loader);

    this.ambientIntensity = new SFFloat(0.0F);
    this.attenuation = new SFVec3f(1.0F, 0.0F, 0.0F);
    this.color = new SFColor(1.0F, 1.0F, 1.0F);
    this.intensity = new SFFloat(1.0F);
    this.location = new SFVec3f(0.0F, 0.0F, 0.0F);
    this.on = new SFBool(true);
    this.radius = new SFFloat(100.0F);

    initFields();
  }

  PointLight(Loader loader, SFFloat ambientIntensity, SFVec3f attenuation, SFColor color, SFFloat intensity, SFVec3f location, SFBool on, SFFloat radius)
  {
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

  void initImpl()
  {
    this.lightColor = new Color3f();
    this.lightPos = new Point3f();
    this.lightAtt = new Point3f();
    this.bounds = new BoundingSphere();
    this.bounds.setRadius(this.radius.value);
    this.sharedGroup = new SharedGroup();
    this.lightColor.x = (this.color.color[0] * this.ambientIntensity.value);
    this.lightColor.y = (this.color.color[1] * this.ambientIntensity.value);
    this.lightColor.z = (this.color.color[2] * this.ambientIntensity.value);
    this.ambLight = new AmbientLight(this.on.value, this.lightColor);
    this.ambLight.setInfluencingBounds(this.bounds);
    this.sharedGroup.addChild(this.ambLight);
    this.lightColor.x = (this.color.color[0] * this.intensity.value);
    this.lightColor.y = (this.color.color[1] * this.intensity.value);
    this.lightColor.z = (this.color.color[2] * this.intensity.value);
    this.lightPos.x = this.location.value[0];
    this.lightPos.y = this.location.value[1];
    this.lightPos.z = this.location.value[2];
    this.lightAtt.x = this.attenuation.value[0];
    this.lightAtt.y = this.attenuation.value[1];
    this.lightAtt.z = this.attenuation.value[2];
    this.light = (this.pntLight = new org.jogamp.java3d.PointLight(this.on.value, this.lightColor, this.lightPos, this.lightAtt));

    this.pntLight.setInfluencingBounds(this.bounds);
    this.sharedGroup.addChild(this.pntLight);
    this.implReady = true;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("AmbientIntensity")) {
      this.lightColor.x = (this.color.color[0] * this.ambientIntensity.value);
      this.lightColor.y = (this.color.color[1] * this.ambientIntensity.value);
      this.lightColor.z = (this.color.color[2] * this.ambientIntensity.value);
      this.ambLight.setColor(this.lightColor);
    }
    else if ((eventInName.equals("color")) || (eventInName.equals("intensity")))
    {
      this.lightColor.x = (this.color.color[0] * this.ambientIntensity.value);
      this.lightColor.y = (this.color.color[1] * this.ambientIntensity.value);
      this.lightColor.z = (this.color.color[2] * this.ambientIntensity.value);
      this.ambLight.setColor(this.lightColor);
      this.lightColor.x = (this.color.color[0] * this.intensity.value);
      this.lightColor.y = (this.color.color[1] * this.intensity.value);
      this.lightColor.z = (this.color.color[2] * this.intensity.value);
      this.pntLight.setColor(this.lightColor);
    }
    else if (eventInName.equals("location")) {
      this.lightPos.x = this.location.value[0];
      this.lightPos.y = this.location.value[1];
      this.lightPos.z = this.location.value[2];
      this.pntLight.setPosition(this.lightPos);
    }
    else if (eventInName.equals("attenuation")) {
      this.lightAtt.x = this.attenuation.value[0];
      this.lightAtt.y = this.attenuation.value[1];
      this.lightAtt.z = this.attenuation.value[2];
      this.pntLight.setAttenuation(this.lightAtt);
    }
    else if (eventInName.equals("on")) {
      this.ambLight.setEnable(this.on.value);
      this.pntLight.setEnable(this.on.value);
    }
    else if (eventInName.equals("radius")) {
      this.bounds.setRadius(this.radius.value);
      this.ambLight.setInfluencingBounds(this.bounds);
      this.pntLight.setInfluencingBounds(this.bounds);
    }
    else if (eventInName.equals("route_on")) {
      this.ambLight.setCapability(13);
      this.pntLight.setCapability(13);
    }
    else if (eventInName.equals("route_location")) {
      this.pntLight.setCapability(19);
    }
    else if (eventInName.equals("route_attenuation")) {
      this.pntLight.setCapability(21);
    }
    else if ((eventInName.equals("route_color")) || (eventInName.equals("route_intensity")))
    {
      this.ambLight.setCapability(15);
      this.pntLight.setCapability(15);
    }
    else if (eventInName.equals("route_ambientIntensity")) {
      this.ambLight.setCapability(15);
    }
    else if (eventInName.equals("route_radius")) {
      this.ambLight.setCapability(17);

      this.pntLight.setCapability(17);
    }
  }

  public Object clone()
  {
    PointLight l = new PointLight(this.loader, (SFFloat)this.ambientIntensity.clone(), (SFVec3f)this.attenuation.clone(), (SFColor)this.color.clone(), (SFFloat)this.intensity.clone(), (SFVec3f)this.location.clone(), (SFBool)this.on.clone(), (SFFloat)this.radius.clone());

    return l;
  }

  public String getType()
  {
    return "PointLight";
  }

  void initFields()
  {
    this.ambientIntensity.init(this, this.FieldSpec, 3, "ambientIntensity");

    this.attenuation.init(this, this.FieldSpec, 3, "attenuation");
    this.color.init(this, this.FieldSpec, 3, "color");
    this.intensity.init(this, this.FieldSpec, 3, "intensity");
    this.location.init(this, this.FieldSpec, 3, "location");
    this.on.init(this, this.FieldSpec, 3, "on");
    this.radius.init(this, this.FieldSpec, 3, "radius");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.PointLight
 * JD-Core Version:    0.6.0
 */