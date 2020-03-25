package org.jdesktop.j3d.loaders.vrml97.impl;

public class WorldInfo extends Node
{
  MFString info;
  SFString title;

  public WorldInfo(Loader loader)
  {
    super(loader);
    this.info = new MFString();
    String[] infoStrings = new String[1];
    infoStrings[0] = "";
    this.info.setValue(infoStrings);
    this.title = new SFString();
    initFields();
    loader.setWorldInfo(this);
  }

  public WorldInfo(Loader loader, MFString info, SFString title)
  {
    super(loader);
    this.info = info;
    this.title = title;
    initFields();
    loader.setWorldInfo(this);
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("title"))
      this.loader.setDescription(this.title.getValue());
  }

  public Object clone()
  {
    return new WorldInfo(this.loader, (MFString)this.info.clone(), (SFString)this.title.clone());
  }

  public String getType()
  {
    return "WorldInfo";
  }

  void initFields()
  {
    this.info.init(this, this.FieldSpec, 0, "info");
    this.title.init(this, this.FieldSpec, 0, "title");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.WorldInfo
 * JD-Core Version:    0.6.0
 */