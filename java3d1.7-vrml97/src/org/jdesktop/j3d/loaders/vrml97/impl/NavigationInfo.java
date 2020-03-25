package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Stack;

public class NavigationInfo extends BindableNode
{
  MFFloat avatarSize;
  SFBool headlight;
  SFFloat speed;
  MFString type;
  SFFloat visibilityLimit;

  public NavigationInfo(Loader loader)
  {
    super(loader, loader.getNavigationInfoStack());
    String[] navTypes = new String[1];
    navTypes[0] = new String("EXAMINE");
    float[] av = new float[3];
    av[0] = 0.25F;
    av[1] = 1.6F;
    av[2] = 0.75F;
    this.avatarSize = new MFFloat(av);
    this.headlight = new SFBool(true);
    this.speed = new SFFloat(1.0F);
    this.type = new MFString(navTypes);
    this.visibilityLimit = new SFFloat(0.0F);
    loader.addNavigationInfo(this);
    initFields();
  }

  public NavigationInfo(Loader loader, SFBool bind, SFTime bindTime, SFBool isBound, MFFloat avatarSize, SFBool healdlight, SFFloat speed, MFString type, SFFloat visibilityLimit)
  {
    super(loader, loader.getNavigationInfoStack(), bind, bindTime, isBound);
    this.bind = bind;
    this.bindTime = bindTime;
    this.avatarSize = avatarSize;
    this.headlight = this.headlight;
    this.speed = speed;
    this.type = type;
    this.visibilityLimit = visibilityLimit;
    loader.addNavigationInfo(this);
    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("bind")) {
      super.notifyMethod(eventInName, time);
    }
    else if (!this.browser.navigationInfoStack.empty()) {
      NavigationInfo top = (NavigationInfo)this.browser.navigationInfoStack.peek();

      if (top == this) {
        System.out.println("navigation updateView");
        this.browser.updateView();
      }
    }
  }

  public String getType()
  {
    return "NavigationInfo";
  }

  public Object clone()
  {
    return new NavigationInfo(this.loader, (SFBool)this.bind.clone(), (SFTime)this.bindTime.clone(), (SFBool)this.isBound.clone(), (MFFloat)this.avatarSize.clone(), (SFBool)this.headlight.clone(), (SFFloat)this.speed.clone(), (MFString)this.type.clone(), (SFFloat)this.visibilityLimit.clone());
  }

  void initFields()
  {
    initBindableFields();
    this.speed.init(this, this.FieldSpec, 3, "speed");
    this.headlight.init(this, this.FieldSpec, 3, "headlight");
    this.avatarSize.init(this, this.FieldSpec, 3, "avatarSize");
    this.type.init(this, this.FieldSpec, 3, "type");
    this.visibilityLimit.init(this, this.FieldSpec, 3, "visibilityLimit");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.NavigationInfo
 * JD-Core Version:    0.6.0
 */