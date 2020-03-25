package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Vector;

public class Group extends GroupBase
{
  org.jogamp.java3d.Group impl;

  public Group(Loader loader)
  {
    super(loader);
  }

  Group(Loader loader, MFNode children, SFVec3f bboxCenter, SFVec3f bboxSize)
  {
    super(loader, children, bboxCenter, bboxSize);
  }

  void initImpl()
  {
    this.impl = new org.jogamp.java3d.Group();
    this.implGroup = this.impl;
    this.implNode = this.impl;
    this.impl.setUserData(new Vector());
    super.replaceChildren();
    this.implReady = true;
  }

  void initImpl(org.jogamp.java3d.Group g)
  {
    this.impl = g;
    this.implGroup = this.impl;
    this.implNode = this.impl;
    this.impl.setUserData(new Vector());
    super.replaceChildren();
    this.implReady = true;
  }

  public void notifyMethod(String eventInName, double time)
  {
    super.notifyMethod(eventInName, time);
  }

  public Object clone()
  {
    Object o = new Group(this.loader, (MFNode)this.children.clone(), (SFVec3f)this.bboxCenter.clone(), (SFVec3f)this.bboxSize.clone());

    this.loader.cleanUp();
    return o;
  }

  public String getType()
  {
    return "Group";
  }

  void initFields()
  {
    initGroupBaseFields();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Group
 * JD-Core Version:    0.6.0
 */