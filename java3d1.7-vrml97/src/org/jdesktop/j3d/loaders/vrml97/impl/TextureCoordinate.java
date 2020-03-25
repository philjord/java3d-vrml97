package org.jdesktop.j3d.loaders.vrml97.impl;

public class TextureCoordinate extends Node
{
  public MFVec2f point;
  Node owner;

  public TextureCoordinate(Loader loader)
  {
    super(loader);
    this.point = new MFVec2f();
    initFields();
  }

  TextureCoordinate(Loader loader, float[][] points)
  {
    super(loader);
    this.point = new MFVec2f(points);
    initFields();
  }

  TextureCoordinate(Loader loader, MFVec2f points)
  {
    super(loader);
    this.point = points;
    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("point")) {
      this.owner.notifyMethod("texCoord", time);
    }
    if (eventInName.equals("route_point"))
      this.owner.notifyMethod("route_texCoord", time);
  }

  void initFields()
  {
    this.point.init(this, this.FieldSpec, 3, "point");
  }

  public Object clone()
  {
    return new TextureCoordinate(this.loader, (MFVec2f)this.point.clone());
  }

  public String getType()
  {
    return "TextureCoordinate";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.TextureCoordinate
 * JD-Core Version:    0.6.0
 */