package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.utils.geometry.Sphere;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.PrintStream;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Bounds;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Texture2D;
import vrml.BaseNode;
import vrml.InvalidVRMLSyntaxException;

public class Background extends BindableNode
{
  MFFloat groundAngle;
  MFColor groundColor;
  MFString backUrl;
  MFString bottomUrl;
  MFString frontUrl;
  MFString leftUrl;
  MFString rightUrl;
  MFString topUrl;
  MFFloat skyAngle;
  MFColor skyColor;
  double[] thetas = { 0.0D, 0.252647726284564D, 0.3582612185126168D, 0.4399759547909189D, 0.509443848811893D, 0.5711684985655375D, 0.6274557729231908D, 0.6796738189082439D, 0.7287134507434055D, 0.7751933733103613D, 0.8195643276682608D, 0.8621670552325126D, 0.9032668821590636D, 0.943075509136984D, 0.9817653565786227D, 1.019479357663014D, 1.05633785123371D, 1.092443562145639D, 1.127885282721258D, 1.162740649493618D, 1.197078275851983D, 1.230959417340775D, 1.264439292237978D, 1.297568144254259D, 1.330392110025637D, 1.36295393744155D, 1.395293589219174D, 1.427448757889531D, 1.459455312453933D, 1.491347692709759D, 1.523159264170493D, 1.554922644304941D, 1.586670009284852D, 1.6184333894193D, 1.650244960880034D, 1.682137341135861D, 1.714143895700262D, 1.746299064370619D, 1.778638716148243D, 1.811200543564156D, 1.844024509335534D, 1.877153361351816D, 1.910633236249018D, 1.944514377737811D, 1.978852004096175D, 2.013707370868536D, 2.049149091444154D, 2.085254802356083D, 2.122113295926779D, 2.15982729701117D, 2.198517144452809D, 2.23832577143073D, 2.27942559835728D, 2.322028325921532D, 2.366399280279432D, 2.412879202846388D, 2.461918834681549D, 2.514136880666603D, 2.570424155024256D, 2.6321488047779D, 2.701616698798874D, 2.783331435077177D, 2.888944927305229D, 3.141592653589793D };
  private static final boolean GROUND = true;
  private static final boolean SKY = false;
  BranchGroup backgroundImpl;
  org.jogamp.java3d.Background background;
  Bounds bound;

  public Background(Loader loader)
  {
    super(loader, loader.getBackgroundStack());
    this.groundAngle = new MFFloat();
    this.groundColor = new MFColor();
    this.backUrl = new MFString();
    this.bottomUrl = new MFString();
    this.frontUrl = new MFString();
    this.leftUrl = new MFString();
    this.rightUrl = new MFString();
    this.topUrl = new MFString();
    this.skyAngle = new MFFloat();
    float[] color = new float[3];
    color[0] = 0.3F;
    color[1] = 0.3F;
    color[2] = 0.3F;
    this.skyColor = new MFColor(color);
    loader.addBackground(this);
    initFields();
  }

  public Background(Loader loader, SFBool bind, SFTime bindTime, SFBool isBound, MFFloat groundAngle, MFColor groundColor, MFString backUrl, MFString bottomUrl, MFString frontUrl, MFString leftUrl, MFString rightUrl, MFString topUrl, MFFloat skyAngle, MFColor skyColor)
  {
    super(loader, loader.getBackgroundStack(), bind, bindTime, isBound);
    this.groundAngle = groundAngle;
    this.groundColor = groundColor;
    this.backUrl = backUrl;
    this.bottomUrl = bottomUrl;
    this.frontUrl = frontUrl;
    this.leftUrl = leftUrl;
    this.rightUrl = rightUrl;
    this.topUrl = topUrl;
    this.skyAngle = skyAngle;
    this.skyColor = skyColor;
    loader.addBackground(this);
    initFields();
  }

  void initImpl()
  {
    BranchGroup bkgGeom = new BranchGroup();

    if (this.skyAngle.mfloat.length > 0) {
      int[] bkg = createBkgGrad(this.skyColor, this.skyAngle, false);
      if (this.groundAngle.mfloat.length > 0) {
        int[] grndBkg = createBkgGrad(this.groundColor, this.groundAngle, true);
        for (int i = 0; i < 32; i++) {
          bkg[(i + 31)] = grndBkg[(31 - i)];
        }
      }
      Texture2D img = getImageBkg(bkg);
      this.background = new org.jogamp.java3d.Background();

      Appearance app = new Appearance();
      app.setTexture(img);
      PolygonAttributes pa = new PolygonAttributes();
      pa.setCullFace(0);
      pa.setBackFaceNormalFlip(true);
      app.setPolygonAttributes(pa);
      bkgGeom = new BranchGroup();
      Group sphere = new org.jogamp.java3d.utils.geometry.Sphere(1.0F, 2, 20, app);

      bkgGeom.addChild(sphere);
      this.background.setGeometry(bkgGeom);
    }
    else {
      this.background = new org.jogamp.java3d.Background(this.skyColor.vals[0], this.skyColor.vals[1], this.skyColor.vals[2]);
    }

    this.background.setApplicationBounds(this.loader.infiniteBounds);
    this.backgroundImpl = new RGroup();
    this.backgroundImpl.addChild(this.background);

    this.implReady = true;
  }

  int[] createBkgGrad(MFColor colors, MFFloat angles, boolean dome)
    throws InvalidVRMLSyntaxException
  {
    float[] clrs0 = new float[3];
    float[] clrs1 = new float[3];
    int c0dex = 0;
    int c1dex = 1;
    int maxdex = angles.mfloat.length;
    int mindex = 0;
    int span = dome ? 32 : 64;
    int[] gradients = new int[span];

    if (maxdex != colors.vals.length / 3 - 1) {
      throw new InvalidVRMLSyntaxException("Background: there shall be one less angle than colors");
    }

    colors.get1Value(0, clrs0);
    gradients[0] = ((int)(clrs0[0] * 255.0F) << 24 | (int)(clrs0[1] * 255.0F) << 16 | (int)(clrs0[2] * 255.0F) << 8 | 0xFF);

    for (int i = 1; i < span; i++) {
      if (c1dex <= maxdex)
      {
        if (c1dex != maxdex)
        {
          if (this.thetas[i] >= angles.get1Value(c1dex)) {
            c0dex = c1dex;
            c1dex++;
          }
        }
        colors.get1Value(c0dex, clrs0);
        colors.get1Value(c1dex, clrs1);
        float i0 = (1.0F - (float)Math.cos(this.thetas[i])) * 0.5F;
        float i1 = 1.0F - i0;
        int r = (int)((clrs0[0] * i0 + clrs1[0] * i1) * 255.0F);
        int g = (int)((clrs0[1] * i0 + clrs1[1] * i1) * 255.0F);
        int b = (int)((clrs0[2] * i0 + clrs1[2] * i1) * 255.0F);

        gradients[i] = (r << 24 | g << 16 | b << 8 | 0xFF);
      }
      else {
        gradients[i] = gradients[(i - 1)];
      }

    }

    return gradients;
  }

  Texture2D getImageBkg(int[] gradients)
  {
    ColorModel cm = new DirectColorModel(32, -16777216, 16711680, 65280, 255);
    WritableRaster raster = cm.createCompatibleWritableRaster(1, 64);
    int[] data = ((DataBufferInt)raster.getDataBuffer()).getData();
    System.arraycopy(gradients, 0, data, 0, gradients.length);
    BufferedImage bufIm = new BufferedImage(cm, raster, false, null);
    ImageComponent2D component2D = new ImageComponent2D(2, bufIm);

    Texture2D tex = new Texture2D(1, 5, 1, 64);

    tex.setMinFilter(3);
    tex.setMagFilter(3);
    tex.setImage(0, component2D);
    tex.setEnable(true);
    return tex;
  }

  public BranchGroup getBackgroundImpl()
  {
    return this.backgroundImpl;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("bind")) {
      super.notifyMethod("bind", time);
    }
    else if (eventInName.equals("skyColor")) {
      if (this.skyColor.vals.length == 3) {
        this.background.setColor(this.skyColor.vals[0], this.skyColor.vals[1], this.skyColor.vals[2]);
      }
      else
      {
        System.err.println("Background: unexpected number of colors");
      }
    }
    else if (eventInName.equals("route_skyColor")) {
      this.background.setCapability(17);
    }
    else if (!eventInName.equals("route_bind"))
    {
      System.err.println("Background: unexpected notify " + eventInName);
    }
  }

  public String getType()
  {
    return "Background";
  }

  public Object clone()
  {
    return new Background(this.loader, (SFBool)this.bind.clone(), (SFTime)this.bindTime.clone(), (SFBool)this.isBound.clone(), (MFFloat)this.groundAngle.clone(), (MFColor)this.groundColor.clone(), (MFString)this.backUrl.clone(), (MFString)this.bottomUrl.clone(), (MFString)this.frontUrl.clone(), (MFString)this.leftUrl.clone(), (MFString)this.rightUrl.clone(), (MFString)this.topUrl.clone(), (MFFloat)this.skyAngle.clone(), (MFColor)this.skyColor.clone());
  }

  void initFields()
  {
    initBindableFields();
    this.groundAngle.init(this, this.FieldSpec, 3, "groundAngle");
    this.groundColor.init(this, this.FieldSpec, 3, "groundColor");
    this.backUrl.init(this, this.FieldSpec, 3, "backUrl");
    this.bottomUrl.init(this, this.FieldSpec, 3, "bottomUrl");
    this.frontUrl.init(this, this.FieldSpec, 3, "frontUrl");
    this.leftUrl.init(this, this.FieldSpec, 3, "leftUrl");
    this.rightUrl.init(this, this.FieldSpec, 3, "rightUrl");
    this.topUrl.init(this, this.FieldSpec, 3, "topUrl");
    this.skyAngle.init(this, this.FieldSpec, 3, "skyAngle");
    this.skyColor.init(this, this.FieldSpec, 3, "skyColor");
  }

  public BaseNode wrap()
  {
    return new org.jdesktop.j3d.loaders.vrml97.node.Background(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Background
 * JD-Core Version:    0.6.0
 */