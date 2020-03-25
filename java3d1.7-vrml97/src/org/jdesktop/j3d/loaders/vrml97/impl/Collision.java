package org.jdesktop.j3d.loaders.vrml97.impl;

public class Collision extends Group
{
  SFBool collide;
  SFNode proxy;
  SFTime collideTime;
  org.jogamp.java3d.Group impl;

  public Collision(Loader loader)
  {
    super(loader);

    this.collide = new SFBool(true);

    this.proxy = new SFNode(null);

    this.collideTime = new SFTime(0.0D);

    initCollisionFields();
  }

  Collision(Loader loader, MFNode children, SFVec3f bboxCenter, SFVec3f bboxSize, SFBool collide, SFNode proxy)
  {
    super(loader, children, bboxCenter, bboxSize);
    this.collide = collide;
    this.proxy = proxy;

    this.collideTime = new SFTime(0.0D);

    initCollisionFields();
  }

  void initImpl()
  {
    this.impl = new org.jogamp.java3d.Group();
    this.impl.setCapability(12);
    this.impl.setCapability(13);
    this.impl.setCapability(14);
    this.impl.setCapability(11);
    this.implNode = (this.implGroup = this.impl);
    super.replaceChildren();
    this.implReady = true;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName == "collide")
    {
      if (this.collide.getValue() != true);
    }
    else
    {
      super.notifyMethod(eventInName, time);
    }
  }

  public String getType()
  {
    return "Collision";
  }

  void initCollisionFields()
  {
    this.collide.init(this, this.FieldSpec, 3, "collide");
    this.proxy.init(this, this.FieldSpec, 0, "proxy");
    this.collideTime.init(this, this.FieldSpec, 2, "collideTime");
  }

  public Object clone()
  {
    return new Collision(this.loader, (MFNode)this.children.clone(), (SFVec3f)this.bboxCenter.clone(), (SFVec3f)this.bboxSize.clone(), (SFBool)this.collide.clone(), (SFNode)this.proxy.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Collision
 * JD-Core Version:    0.6.0
 */