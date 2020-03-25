package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.SharedGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

public class DirectionalLight extends Light
{
  SFFloat ambientIntensity;
  SFColor color;
  SFVec3f direction;
  SFFloat intensity;
  SFBool on;
  Color3f lightColor;
  Vector3f lightDir;
  org.jogamp.java3d.DirectionalLight dirLight;

  public DirectionalLight(Loader loader)
  {
    super(loader);
    this.ambientIntensity = new SFFloat(0.0F);
    this.color = new SFColor(1.0F, 1.0F, 1.0F);
    this.direction = new SFVec3f(0.0F, 0.0F, -1.0F);
    this.intensity = new SFFloat(1.0F);
    this.on = new SFBool(true);

    initFields();
  }

  DirectionalLight(Loader loader, SFFloat ambientIntensity, SFColor color, SFVec3f direction, SFFloat intensity, SFBool on)
  {
    super(loader);
    this.ambientIntensity = ambientIntensity;
    this.color = color;
    this.direction = direction;
    this.intensity = intensity;
    this.on = on;

    initFields();
  }

  void initImpl()
  {
    this.lightColor = new Color3f();
    this.lightDir = new Vector3f();
    this.sharedGroup = new SharedGroup();
    this.lightColor.x = (this.color.color[0] * this.ambientIntensity.value);
    this.lightColor.y = (this.color.color[1] * this.ambientIntensity.value);
    this.lightColor.z = (this.color.color[2] * this.ambientIntensity.value);
    this.ambLight = new AmbientLight(this.on.value, this.lightColor);
    this.sharedGroup.addChild(this.ambLight);
    this.lightColor.x = (this.color.color[0] * this.intensity.value);
    this.lightColor.y = (this.color.color[1] * this.intensity.value);
    this.lightColor.z = (this.color.color[2] * this.intensity.value);
    this.lightDir.x = this.direction.value[0];
    this.lightDir.y = this.direction.value[1];
    this.lightDir.z = this.direction.value[2];
    this.light = (this.dirLight = new org.jogamp.java3d.DirectionalLight(this.on.value, this.lightColor, this.lightDir));

    this.ambLight.setInfluencingBounds(this.loader.infiniteBounds);
    this.dirLight.setInfluencingBounds(this.loader.infiniteBounds);
    this.sharedGroup.addChild(this.dirLight);
    this.implReady = true;
  }

  void setScope(Group scopeGroup)
  {
    this.ambLight.addScope(scopeGroup);
    this.dirLight.addScope(scopeGroup);
  }

  public Object clone()
  {
    return new DirectionalLight(this.loader, (SFFloat)this.ambientIntensity.clone(), (SFColor)this.color.clone(), (SFVec3f)this.direction.clone(), (SFFloat)this.intensity.clone(), (SFBool)this.on.clone());
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("ambientIntensity")) {
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
      this.dirLight.setColor(this.lightColor);
    }
    else if (eventInName.equals("direction")) {
      this.lightDir.x = this.direction.value[0];
      this.lightDir.y = this.direction.value[1];
      this.lightDir.z = this.direction.value[2];
      this.dirLight.setDirection(this.lightDir);
    }
    else if (eventInName.equals("on")) {
      this.ambLight.setEnable(this.on.value);
      this.dirLight.setEnable(this.on.value);
    }
    else if (eventInName.equals("route_on")) {
      this.ambLight.setCapability(13);
      this.dirLight.setCapability(13);
    }
    else if (eventInName.equals("route_direction")) {
      this.dirLight.setCapability(19);
    }
    else if ((eventInName.equals("route_color")) || (eventInName.equals("route_intensity")))
    {
      this.ambLight.setCapability(15);
      this.dirLight.setCapability(15);
    }
    else if (eventInName.equals("route_ambientIntensity")) {
      this.ambLight.setCapability(15);
    }
  }

  public String getType()
  {
    return "DirectionalLight";
  }

  void initFields()
  {
    this.ambientIntensity.init(this, this.FieldSpec, 3, "ambientIntensity");

    this.color.init(this, this.FieldSpec, 3, "color");
    this.direction.init(this, this.FieldSpec, 3, "direction");
    this.intensity.init(this, this.FieldSpec, 3, "intensity");
    this.on.init(this, this.FieldSpec, 3, "on");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.DirectionalLight
 * JD-Core Version:    0.6.0
 */