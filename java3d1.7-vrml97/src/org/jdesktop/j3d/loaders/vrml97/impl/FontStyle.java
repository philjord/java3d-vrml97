package org.jdesktop.j3d.loaders.vrml97.impl;

public class FontStyle extends Node
{
  MFString family;
  SFBool horizontal;
  MFString justify;
  SFString language;
  SFBool leftToRight;
  SFFloat size;
  SFFloat spacing;
  SFString style;
  SFBool topToBottom;
  String[] s = new String[1];

  public FontStyle(Loader loader)
  {
    super(loader);
    this.s[0] = "SERIF";
    this.family = new MFString(this.s);
    this.horizontal = new SFBool(true);
    this.s[0] = "BEGIN";
    this.justify = new MFString(this.s);
    this.language = new SFString();
    this.leftToRight = new SFBool(true);
    this.size = new SFFloat(1.0F);
    this.spacing = new SFFloat(1.0F);
    this.style = new SFString("PLAIN");
    this.topToBottom = new SFBool(true);
    initFields();
  }

  public FontStyle(Loader loader, MFString family, SFBool horizontal, MFString justify, SFString language, SFBool leftToRight, SFFloat size, SFFloat spacing, SFString style, SFBool topToBottom)
  {
    super(loader);
    this.family = family;
    this.horizontal = horizontal;
    this.justify = justify;
    this.language = language;
    this.leftToRight = leftToRight;
    this.size = size;
    this.spacing = spacing;
    this.style = style;
    this.topToBottom = topToBottom;
    initFields();
  }

  void initFields()
  {
    this.family.init(this, this.FieldSpec, 0, "family");
    this.horizontal.init(this, this.FieldSpec, 0, "horizontal");
    this.justify.init(this, this.FieldSpec, 0, "justify");
    this.language.init(this, this.FieldSpec, 0, "language");
    this.leftToRight.init(this, this.FieldSpec, 0, "leftToRight");
    this.size.init(this, this.FieldSpec, 0, "size");
    this.spacing.init(this, this.FieldSpec, 0, "spacing");
    this.style.init(this, this.FieldSpec, 0, "style");
    this.topToBottom.init(this, this.FieldSpec, 0, "horizontal");
  }

  public Object clone()
  {
    return new FontStyle(this.loader, (MFString)this.family.clone(), (SFBool)this.horizontal.clone(), (MFString)this.justify.clone(), (SFString)this.language.clone(), (SFBool)this.leftToRight.clone(), (SFFloat)this.size.clone(), (SFFloat)this.spacing.clone(), (SFString)this.style.clone(), (SFBool)this.topToBottom.clone());
  }

  public String getType()
  {
    return "FontStyle";
  }

  public void notifyMethod(String eventInName, double time)
  {
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.FontStyle
 * JD-Core Version:    0.6.0
 */