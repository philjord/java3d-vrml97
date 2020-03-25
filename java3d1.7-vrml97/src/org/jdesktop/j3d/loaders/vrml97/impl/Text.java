package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.utils.geometry.Text2D;
import java.io.PrintStream;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;

public class Text extends Geometry
{
  MFString string;
  SFNode fontStyle;
  MFFloat length;
  SFFloat maxExtent;
  static String vrmlSerif = "SERIF";
  static String vrmlSans = "SANS";
  static String vrmlFixed = "TYPEWRITER";

  static String j3dSerif = "Serif";
  static String j3dSans = "Sans";
  static String j3dFixed = "Courier";

  static String vrmlPlain = "PLAIN";
  static String vrmlBold = "BOLD";
  static String vrmlItalic = "ITALIC";
  static String vrmlBoldItalic = "BOLDITALIC";
  static final int RASTER_SIZE = 24;
  TransformGroup impl = null;

  public Text(Loader loader)
  {
    super(loader);
    this.string = new MFString();
    this.fontStyle = new SFNode();
    this.length = new MFFloat();
    this.maxExtent = new SFFloat(0.0F);
    initFields();
  }

  public Text(Loader loader, MFString string, SFNode fontStyle, MFFloat length, SFFloat maxExtent)
  {
    super(loader);
    this.string = string;
    this.fontStyle = fontStyle;
    this.length = length;
    this.maxExtent = maxExtent;
    initFields();
  }

  Node createText2D(Appearance app)
  {
    if (this.loader.debug) {
      System.out.println("Text.createText2D() called");
    }
    if ((this.string.strings != null) && (this.string.strings.length > 0)) {
      if (this.loader.debug) {
        System.out.println("creating Text2D object");
      }

      FontStyle fs = null;
      if ((this.fontStyle.node != null) && ((this.fontStyle.node instanceof FontStyle)))
      {
        fs = (FontStyle)this.fontStyle.node;
      }
      else {
        fs = new FontStyle(this.loader);
      }

      Color3f fontColor = new Color3f(1.0F, 1.0F, 1.0F);
      if (app != null) {
        Material mat = app.getMaterial();
        if (mat != null) {
          mat.getDiffuseColor(fontColor);
        }
      }

      String fontName = j3dSerif;
      if (fs.family.strings.length > 0) {
        boolean found = false;
        int i = 0;
        while ((i < fs.family.strings.length) && (!found))
        {
          if (vrmlSerif.equals(fs.family.strings[i])) {
            fontName = j3dSerif;
            found = true;
          }
          else if (vrmlSans.equals(fs.family.strings[i])) {
            fontName = j3dSans;
            found = true;
          }
          else if (vrmlFixed.equals(fs.family.strings[i])) {
            fontName = j3dFixed;
            found = true;
          }
          i++;
        }

      }

      int fontStyleId = 0;
      if (fs.style.string != null) {
        if (vrmlPlain.equals(fs.style.string)) {
          fontStyleId = 0;
        }
        else if (vrmlBold.equals(fs.style.string)) {
          fontStyleId = 1;
        }
        else if (vrmlItalic.equals(fs.style.string)) {
          fontStyleId = 2;
        }
        else if (vrmlBoldItalic.equals(fs.style.string)) {
          fontStyleId = 3;
        }
      }

      this.impl = new TransformGroup();

      Text2D text = new Text2D(this.string.strings[0], fontColor, fontName, 24, fontStyleId);

      this.impl.addChild(text);

      Transform3D textXform = new Transform3D();

      float fontSize = 24.0F * text.getRectangleScaleFactor();
      float scale = fs.size.value / fontSize;

      textXform.setScale(scale);
      this.impl.setTransform(textXform);

      this.implNode = this.impl;
    }
    return this.impl;
  }

  public org.jogamp.java3d.Geometry getImplGeom()
  {
    return null;
  }

  public boolean haveTexture()
  {
    return false;
  }

  public int getNumTris()
  {
    return 0;
  }

  public BoundingBox getBoundingBox()
  {
    return null;
  }

  public String getType()
  {
    return "Text";
  }

  public Object clone()
  {
    return new Text(this.loader, (MFString)this.string.clone(), (SFNode)this.fontStyle.clone(), (MFFloat)this.length.clone(), (SFFloat)this.maxExtent.clone());
  }

  public void notifyMethod(String eventInName, double time)
  {
    System.out.println("Text: unimplemented event " + eventInName);
  }

  void initFields()
  {
    this.string.init(this, this.FieldSpec, 3, "string");
    this.fontStyle.init(this, this.FieldSpec, 3, "fontStyle");
    this.length.init(this, this.FieldSpec, 3, "length");
    this.maxExtent.init(this, this.FieldSpec, 3, "maxExtent");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Text
 * JD-Core Version:    0.6.0
 */