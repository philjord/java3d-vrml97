package org.jdesktop.j3d.loaders.vrml97.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import org.jogamp.vecmath.Color3f;

public class Material extends Node
{
  org.jogamp.java3d.Material impl;
  SFFloat ambientIntensity;
  SFColor diffuseColor;
  SFColor emissiveColor;
  SFFloat shininess;
  SFColor specularColor;
  SFFloat transparency;
  PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
  public static final String TRANSPARENCY = "transparency";
  Color3f ambColor = new Color3f();
  Color3f emmColor = new Color3f();
  Color3f diffColor = new Color3f();
  Color3f specColor = new Color3f();

  public Material(Loader loader)
  {
    super(loader);

    this.ambientIntensity = new SFFloat(0.2F);
    this.diffuseColor = new SFColor(0.8F, 0.8F, 0.8F);
    this.emissiveColor = new SFColor(0.0F, 0.0F, 0.0F);
    this.shininess = new SFFloat(0.2F);
    this.specularColor = new SFColor(0.0F, 0.0F, 0.0F);
    this.transparency = new SFFloat(0.0F);

    initFields();
  }

  Material(Loader loader, SFFloat ambientIntensity, SFColor diffuseColor, SFColor emissiveColor, SFFloat shininess, SFColor specularColor, SFFloat transparency)
  {
    super(loader);

    this.ambientIntensity = ambientIntensity;
    this.diffuseColor = diffuseColor;
    this.emissiveColor = emissiveColor;
    this.shininess = shininess;
    this.specularColor = specularColor;
    this.transparency = transparency;

    initFields();
  }

  void initImpl()
  {
    float ambIntensity = this.ambientIntensity.getValue();
    this.ambColor.x = (ambIntensity * this.diffuseColor.color[0]);
    this.ambColor.y = (ambIntensity * this.diffuseColor.color[1]);
    this.ambColor.z = (ambIntensity * this.diffuseColor.color[2]);
    this.emmColor.x = this.emissiveColor.color[0];
    this.emmColor.y = this.emissiveColor.color[1];
    this.emmColor.z = this.emissiveColor.color[2];
    this.diffColor.x = this.diffuseColor.color[0];
    this.diffColor.y = this.diffuseColor.color[1];
    this.diffColor.z = this.diffuseColor.color[2];
    this.specColor.x = this.specularColor.color[0];
    this.specColor.y = this.specularColor.color[1];
    this.specColor.z = this.specularColor.color[2];
    float val = this.shininess.value * 127.0F + 1.0F;
    if (val > 127.0F) {
      val = 127.0F;
    }
    else if (val < 1.0F) {
      val = 1.0F;
    }
    this.impl = new org.jogamp.java3d.Material(this.ambColor, this.emmColor, this.diffColor, this.specColor, val);

    this.propertyChangeSupport.firePropertyChange("transparency", null, new Float(this.transparency.value));
    this.impl.setLightingEnable(true);
    this.implReady = true;
  }

  public Object clone()
  {
    Material a = new Material(this.loader, (SFFloat)this.ambientIntensity.clone(), (SFColor)this.diffuseColor.clone(), (SFColor)this.emissiveColor.clone(), (SFFloat)this.shininess.clone(), (SFColor)this.specularColor.clone(), (SFFloat)this.transparency.clone());

    return a;
  }

  public String getType()
  {
    return "Material";
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("ambientIntensity")) {
      float ambIntensity = this.ambientIntensity.value;
      this.ambColor.x = (ambIntensity * this.diffuseColor.color[0]);
      this.ambColor.y = (ambIntensity * this.diffuseColor.color[1]);
      this.ambColor.z = (ambIntensity * this.diffuseColor.color[2]);
      this.impl.setAmbientColor(this.ambColor);
    }
    else if (eventInName.equals("diffuseColor")) {
      float ambIntensity = this.ambientIntensity.value;
      this.ambColor.x = (ambIntensity * this.diffuseColor.color[0]);
      this.ambColor.y = (ambIntensity * this.diffuseColor.color[1]);
      this.ambColor.z = (ambIntensity * this.diffuseColor.color[2]);
      this.impl.setAmbientColor(this.ambColor);
      this.diffColor.x = this.diffuseColor.color[0];
      this.diffColor.y = this.diffuseColor.color[1];
      this.diffColor.z = this.diffuseColor.color[2];
      this.impl.setDiffuseColor(this.diffColor);
    }
    else if (eventInName.equals("emissiveColor")) {
      this.emmColor.x = this.emissiveColor.color[0];
      this.emmColor.y = this.emissiveColor.color[1];
      this.emmColor.z = this.emissiveColor.color[2];
      this.impl.setEmissiveColor(this.emmColor);
    }
    else if (eventInName.equals("shininess")) {
      float val = this.shininess.value * 127.0F + 1.0F;
      if (val > 128.0F) {
        val = 127.0F;
      }
      else if (val < 1.0F) {
        val = 1.0F;
      }
      this.impl.setShininess(val);
    }
    else if (eventInName.equals("specularColor")) {
      this.specColor.x = this.specularColor.color[0];
      this.specColor.y = this.specularColor.color[1];
      this.specColor.z = this.specularColor.color[2];
      this.impl.setSpecularColor(this.specColor);
    }
    else if (eventInName.equals("transparency")) {
      this.propertyChangeSupport.firePropertyChange("transparency", null, new Float(this.transparency.value));
    }
    else if ((eventInName.equals("route_ambientIntensity")) || (eventInName.equals("route_diffuseColor")) || (eventInName.equals("route_emissiveColor")) || (eventInName.equals("route_shininess")) || (eventInName.equals("route_specularColor")))
    {
      this.impl.setCapability(1);
    }
    else if (!eventInName.equals("route_transparency"))
    {
      System.err.println("Material: unknown eventInName " + eventInName);
    }
  }

  public float getTransparency() {
    return this.transparency.getValue();
  }

  public void addPropertyChangeListener(String n, PropertyChangeListener l) {
    this.propertyChangeSupport.addPropertyChangeListener(n, l);
  }

  public void removePropertyChangeListener(String n, PropertyChangeListener l) {
    this.propertyChangeSupport.removePropertyChangeListener(n, l);
  }

  void initFields()
  {
    this.ambientIntensity.init(this, this.FieldSpec, 3, "ambientIntensity");

    this.diffuseColor.init(this, this.FieldSpec, 3, "diffuseColor");

    this.emissiveColor.init(this, this.FieldSpec, 3, "emissiveColor");

    this.shininess.init(this, this.FieldSpec, 3, "shininess");
    this.specularColor.init(this, this.FieldSpec, 3, "specularColor");

    this.transparency.init(this, this.FieldSpec, 3, "transparency");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Material
 * JD-Core Version:    0.6.0
 */