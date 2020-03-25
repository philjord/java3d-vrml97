package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.ExponentialFog;
import org.jogamp.java3d.LinearFog;
import vrml.BaseNode;

public class Fog extends BindableNode
{
  SFColor color;
  SFString fogType;
  SFFloat visibilityRange;
  BranchGroup fogImpl;
  org.jogamp.java3d.Fog fog;

  public Fog(Loader loader)
  {
    super(loader, loader.getFogStack());

    this.color = new SFColor(1.0F, 1.0F, 1.0F);
    this.fogType = new SFString("LINEAR");
    this.visibilityRange = new SFFloat(0.0F);

    loader.addFog(this);
    initFields();
  }

  Fog(Loader loader, SFBool bind, SFTime bindTime, SFBool isBound, SFColor color, SFString fogType, SFFloat visibilityRange)
  {
    super(loader, loader.getFogStack(), bind, bindTime, isBound);

    this.color = color;
    this.fogType = fogType;
    this.visibilityRange = visibilityRange;

    loader.addFog(this);
    initFields();
  }

  public void initImpl()
  {
    if (this.fogType.getValue().equals("LINEAR")) {
      LinearFog linearFog = new LinearFog(this.color.color[0], this.color.color[1], this.color.color[2]);

      linearFog.setCapability(17);
      linearFog.setBackDistance(this.visibilityRange.getValue());
      linearFog.setFrontDistance(this.visibilityRange.getValue() / 10.0D);
      this.fog = linearFog;
    }
    else {
      ExponentialFog expFog = new ExponentialFog(this.color.color[0], this.color.color[1], this.color.color[2]);

      expFog.setCapability(17);
      expFog.setDensity(this.visibilityRange.getValue());
      this.fog = expFog;
    }

    this.fog.setCapability(14);
    this.fog.setCapability(15);

    this.fogImpl = new RGroup();
    if (this.visibilityRange.getValue() == 0.0D) {
      this.fog.setInfluencingBounds(this.loader.zeroBounds);
    }
    else {
      this.fog.setInfluencingBounds(this.loader.infiniteBounds);
    }
    this.fogImpl.addChild(this.fog);
    this.implReady = true;
  }

  public BranchGroup getFogImpl()
  {
    return this.fogImpl;
  }

  public void initFields()
  {
    initBindableFields();
    this.color.init(this, this.FieldSpec, 3, "color");
    this.fogType.init(this, this.FieldSpec, 3, "fogType");
    this.visibilityRange.init(this, this.FieldSpec, 3, "visibilityRange");
  }

  public Object clone()
  {
    return new Fog(this.loader, (SFBool)this.bind.clone(), this.bindTime, this.isBound, (SFColor)this.color.clone(), (SFString)this.fogType.clone(), (SFFloat)this.visibilityRange.clone());
  }

  public BaseNode wrap()
  {
    return new org.jdesktop.j3d.loaders.vrml97.node.Fog(this);
  }

  public String getType()
  {
    return "Fog";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Fog
 * JD-Core Version:    0.6.0
 */