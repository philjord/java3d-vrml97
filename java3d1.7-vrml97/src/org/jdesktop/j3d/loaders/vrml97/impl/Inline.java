package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import vrml.InvalidVRMLSyntaxException;

public class Inline extends Group
{
  MFString url;
  SFVec3f bboxCenter;
  SFVec3f bboxSize;

  public Inline(Loader loader)
  {
    super(loader);
    this.url = new MFString();
    this.bboxCenter = new SFVec3f(0.0F, 0.0F, 0.0F);
    this.bboxSize = new SFVec3f(-1.0F, -1.0F, -1.0F);
    initInlineFields();
  }

  Inline(Loader loader, MFString url, SFVec3f bboxCenter, SFVec3f bboxSize)
  {
    super(loader);
    this.url = url;
    this.bboxCenter = bboxCenter;
    this.bboxSize = bboxSize;
    initInlineFields();
  }

  void initImpl()
  {
    this.impl = new org.jogamp.java3d.Group();
    this.implNode = (this.implGroup = this.impl);
    this.impl.setUserData(new Vector());
    loadURL();
    this.implReady = true;
  }
  void loadURL() {
    Scene scene = null;
    URL loadURL;
    try {
      loadURL = this.loader.stringToURL(this.url.strings[0]);
    }
    catch (MalformedURLException ue) {
      InvalidVRMLSyntaxException i = new InvalidVRMLSyntaxException("Bad URL readling Inline: " + this.url.strings[0]);

      i.initCause(ue);
      throw i;
    }
    try {
      scene = this.loader.load(loadURL);
    }
    catch (IOException e) {
      InvalidVRMLSyntaxException i = new InvalidVRMLSyntaxException("IOException reading Inline:" + loadURL);

      i.initCause(e);
      throw i;
    }

    BaseNode[] nodes = new BaseNode[scene.objects.size()];
    int i = 0;
    for (Enumeration e = scene.objects.elements(); e.hasMoreElements(); i++) {
      nodes[i] = ((BaseNode)e.nextElement());
    }
    this.children.nodes = nodes;
    replaceChildren();

    this.loader.scene.viewpoints.addAll(scene.viewpoints);
    this.loader.scene.navInfos.addAll(scene.navInfos);
    this.loader.scene.backgrounds.addAll(scene.backgrounds);
    this.loader.scene.fogs.addAll(scene.fogs);
    this.loader.scene.lights.addAll(scene.lights);
    this.loader.scene.sharedGroups.addAll(scene.sharedGroups);
    this.loader.scene.timeSensors.addAll(scene.timeSensors);
    this.loader.scene.visibilitySensors.addAll(scene.visibilitySensors);
    this.loader.scene.touchSensors.addAll(scene.touchSensors);
    this.loader.scene.audioClips.addAll(scene.audioClips);
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("url"))
      loadURL();
  }

  public Object clone()
  {
    return new Inline(this.loader, (MFString)this.url.clone(), (SFVec3f)this.bboxSize.clone(), (SFVec3f)this.bboxCenter.clone());
  }

  public String getType()
  {
    return "Inline";
  }

  void initFields()
  {
    super.initFields();
    initInlineFields();
  }

  void initInlineFields()
  {
    this.url.init(this, this.FieldSpec, 3, "url");
    this.bboxCenter.init(this, this.FieldSpec, 0, "bboxCenter");
    this.bboxSize.init(this, this.FieldSpec, 0, "bboxSize");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Inline
 * JD-Core Version:    0.6.0
 */