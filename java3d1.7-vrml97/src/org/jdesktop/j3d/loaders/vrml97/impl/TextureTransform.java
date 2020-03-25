package org.jdesktop.j3d.loaders.vrml97.impl;

public class TextureTransform extends Node
{
  SFVec2f center;
  SFFloat rotation;
  SFVec2f scale;
  SFVec2f translation;
  Appearance owner;

  public TextureTransform(Loader loader)
  {
    super(loader);
    this.center = new SFVec2f(0.0F, 0.0F);
    this.rotation = new SFFloat(0.0F);
    this.scale = new SFVec2f(1.0F, 1.0F);
    this.translation = new SFVec2f(0.0F, 0.0F);
    initFields();
  }

  public TextureTransform(Loader loader, SFVec2f center, SFFloat rotation, SFVec2f scale, SFVec2f translation)
  {
    super(loader);
    this.center = center;
    this.rotation = rotation;
    this.scale = scale;
    this.translation = translation;
    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    this.owner.updateTextureTransform();
  }

  public String getType()
  {
    return "TextureTransform";
  }

  public Object clone()
  {
    TextureTransform c = new TextureTransform(this.loader, (SFVec2f)this.center.clone(), (SFFloat)this.rotation.clone(), (SFVec2f)this.scale.clone(), (SFVec2f)this.translation.clone());

    return c;
  }

  void initFields()
  {
    this.center.init(this, this.FieldSpec, 3, "center");
    this.rotation.init(this, this.FieldSpec, 3, "rotation");
    this.scale.init(this, this.FieldSpec, 3, "scale");
    this.translation.init(this, this.FieldSpec, 3, "translation");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.TextureTransform
 * JD-Core Version:    0.6.0
 */