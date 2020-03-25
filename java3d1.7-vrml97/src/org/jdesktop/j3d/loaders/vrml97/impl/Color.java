package org.jdesktop.j3d.loaders.vrml97.impl;

public class Color extends Node
{
  MFColor color;
  Node owner;

  public Color(Loader loader)
  {
    super(loader);
    this.color = new MFColor();
    initFields();
  }

  public Color(Loader loader, MFColor color)
  {
    super(loader);
    this.color = color;
    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("color")) {
      this.owner.notifyMethod("color", time);
    }
    if (eventInName.equals("route_color"))
      this.owner.notifyMethod("route_color", time);
  }

  public Object clone()
  {
    return new Color(this.loader, (MFColor)this.color.clone());
  }

  public String getType()
  {
    return "Color";
  }

  void initFields()
  {
    this.color.init(this, this.FieldSpec, 3, "color");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Color
 * JD-Core Version:    0.6.0
 */