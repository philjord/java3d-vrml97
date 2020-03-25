package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Vector;
import org.jogamp.java3d.Group;

public class Anchor extends GroupBase
  implements VrmlSensor
{
  SFString description;
  MFString parameter;
  MFString url;
  Group impl;

  public Anchor(Loader loader)
  {
    super(loader);
    this.description = new SFString();
    this.parameter = new MFString();
    this.url = new MFString();
    initFields();
  }

  public Anchor(Loader loader, MFNode children, SFVec3f bboxCenter, SFVec3f bboxSize, SFString description, MFString parameter, MFString url)
  {
    super(loader, children, bboxCenter, bboxSize);
    this.description = description;
    this.parameter = parameter;
    this.url = url;
    initFields();
  }

  public void initImpl()
  {
    if (this.loader.debug) {
      System.out.println("Anchor.initImpl() called");
    }
    this.impl = new Group();
    this.implGroup = this.impl;
    this.implNode = this.impl;
    Vector v = new Vector();
    v.addElement(this);
    this.impl.setUserData(v);
    this.impl.setCapability(1);
    this.impl.setCapability(3);
    this.impl.setCapability(11);

    this.impl.setCapability(12);
    this.impl.setCapability(14);
    this.impl.setCapability(13);
    super.replaceChildren();
    this.implReady = true;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if ((!eventInName.equals("description")) && (!eventInName.equals("parameter")) && (!eventInName.equals("url")))
    {
      if ((!eventInName.equals("route_description")) && (!eventInName.equals("route_parameter")) && (!eventInName.equals("route_url")))
      {
        super.notifyMethod(eventInName, time);
      }
    }
  }

  void initFields()
  {
    initGroupBaseFields();
    this.description.init(this, this.FieldSpec, 3, "description");
    this.parameter.init(this, this.FieldSpec, 3, "parameter");
    this.url.init(this, this.FieldSpec, 3, "url");
  }

  public Object clone()
  {
    Anchor a = new Anchor(this.loader, (MFNode)this.children.clone(), (SFVec3f)this.bboxCenter.clone(), (SFVec3f)this.bboxSize.clone(), (SFString)this.description.clone(), (MFString)this.parameter.clone(), (MFString)this.url.clone());

    return a;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Anchor
 * JD-Core Version:    0.6.0
 */