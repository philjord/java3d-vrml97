package org.jdesktop.j3d.loaders.vrml97.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.TexCoordGeneration;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector4f;

public class Appearance extends Node
{
  org.jogamp.java3d.Appearance impl;
  SFNode material;
  SFNode texture;
  SFNode textureTransform;
  TransparencyAttributes implTransp = null;
  private TransparencyListener listener = new TransparencyListener();

  boolean haveTexture = false;
  int numUses = 0;
  TexCoordGeneration texGen;
  TextureAttributes ta = new TextureAttributes();

  Transform3D tr = new Transform3D();
  Transform3D T = new Transform3D();
  Transform3D C = new Transform3D();
  Transform3D R = new Transform3D();
  Transform3D S = new Transform3D();
  Vector3d v1 = new Vector3d();
  Vector3d v2 = new Vector3d();
  Vector3d v3 = new Vector3d();
  Vector3d v4 = new Vector3d();
  AxisAngle4f al = new AxisAngle4f();

  public Appearance(Loader loader)
  {
    super(loader);
    this.material = new SFNode(null);
    this.texture = new SFNode(null);
    this.textureTransform = new SFNode(null);
    initFields();
  }

  Appearance(Loader loader, SFNode material, SFNode texture, SFNode textureTransform)
  {
    super(loader);
    this.material = material;
    this.texture = texture;
    this.textureTransform = textureTransform;
    initFields();
  }

  private void updateMaterial()
  {
    this.listener.setMaterial((Material)this.material.node);
    updateTransparency();
    if (this.material.node != null)
    {
      this.impl.setMaterial(((Material)this.material.node).impl);
    }
  }

  private void updateTexture()
  {
    this.haveTexture = false;
    if (this.texture.node != null) {
      Texture tex = ((TextureSrc)this.texture.node).getImplTexture();
      if (tex != null) {
        this.impl.setTexture(tex);
        tex.setEnable(true);
        this.haveTexture = true;
      }
    }
    this.listener.setTexture((TextureSrc)this.texture.node);
    updateTransparency();
  }

  public void updateTransparency() {
    if (this.loader.debug) {
      System.out.print(toStringId() + ": Updating transparency ");
    }
    boolean transparent = false;
    float value = 0.0F;
    if ((this.material != null) && (this.material.node != null)) {
      value = ((Material)this.material.node).getTransparency();
      transparent = value > 0.0F;
      if (this.loader.debug) {
        System.out.print(" Material=" + value);
      }
    }
    if ((this.texture != null) && (this.texture.node != null) && ((this.texture.node instanceof ImageTexture))) {
      transparent = (transparent) || (((ImageTexture)this.texture.node).getTransparency());
      if (this.loader.debug) {
        System.out.print(" Texture=" + ((ImageTexture)this.texture.node).getTransparency());
      }
    }
    if (this.loader.debug)
      System.out.println(" Result=" + transparent);
    int mode;
    if (transparent)
      mode = 1;
    else {
      mode = 4;
    }
    if (this.implTransp == null) {
      this.implTransp = new TransparencyAttributes(mode, value);
      this.implTransp.setCapability(1);
      this.implTransp.setCapability(3);
    } else {
      this.implTransp.setTransparency(value);
      this.implTransp.setTransparencyMode(mode);
    }
    this.impl.setTransparencyAttributes(this.implTransp);
  }

  void updateTextureTransform()
  {
    if (this.textureTransform.node != null)
    {
      ((TextureTransform)this.textureTransform.node).owner = this;

      TextureTransform tf = (TextureTransform)this.textureTransform.node;

      double tx = tf.translation.vec2f[0];
      double ty = tf.translation.vec2f[0];
      this.v1.set(tx, ty, 0.0D);
      this.T.set(this.v1);

      double cx = tf.center.vec2f[0];
      double cy = tf.center.vec2f[1];
      this.v2.set(cx, cy, 0.0D);
      this.C.set(this.v2);

      double r = tf.rotation.value;
      this.al.set(0.0F, 0.0F, 1.0F, (float)r);
      this.R.setRotation(this.al);

      double sx = tf.scale.vec2f[0];
      double sy = tf.scale.vec2f[1];
      this.v3.set(sx, sy, 1.0D);
      this.S.setScale(this.v3);

      this.tr.setIdentity();
      this.tr.mul(this.T);
      this.tr.mul(this.C);
      this.tr.mul(this.R);
      this.tr.mul(this.S);
      this.v2.negate();
      this.C.set(this.v2);
      this.tr.mul(this.C);

      this.ta.setTextureTransform(this.tr);
      this.impl.setTextureAttributes(this.ta);
    }
    this.ta.setTextureMode(6);
    this.ta.setCombineAlphaMode(1);
    this.ta.setCombineRgbMode(0);
    this.ta.setCombineAlphaSource(0, 0);
    this.ta.setCombineAlphaSource(1, 1);
    this.ta.setCombineRgbSource(0, 1);
    this.impl.setTextureAttributes(this.ta);
  }

  public void setTexGen(BoundingBox box)
  {
    Point3d min = new Point3d();
    Point3d max = new Point3d();

    box.getLower(min);
    box.getUpper(max);

    float xRange = (float)(max.x - min.x);
    float yRange = (float)(max.y - min.y);
    float zRange = (float)(max.z - min.z);

    Vector4f xEq = new Vector4f(1.0F / xRange, 0.0F, 0.0F, (float)(-min.x) / xRange);

    Vector4f yEq = new Vector4f(0.0F, 1.0F / yRange, 0.0F, (float)(-min.y) / xRange);

    Vector4f zEq = new Vector4f(0.0F, 0.0F, 1.0F / zRange, (float)(-min.z) / xRange);
    Vector4f tEq;
    Vector4f sEq;
    if (xRange >= yRange)
    {
      if (xRange >= zRange) {
        sEq = xEq;
        if (yRange >= zRange)
          tEq = yEq;
        else
          tEq = zEq;
      }
      else {
        sEq = zEq;
        tEq = xEq;
      }
    }
    else
    {
      if (yRange >= zRange) {
        sEq = yEq;
        if (xRange >= zRange)
          tEq = xEq;
        else
          tEq = zEq;
      }
      else {
        sEq = zEq;
        tEq = yEq;
      }
    }
    this.texGen = new TexCoordGeneration(0, 0, sEq, tEq);

    this.impl.setTexCoordGeneration(this.texGen);
    this.texGen.setEnable(true);
  }

  public void initImpl()
  {
    this.impl = new org.jogamp.java3d.Appearance();
    updateMaterial();
    updateTexture();
    updateTextureTransform();
    this.implReady = true;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("material"))
      updateMaterial();
    else if (eventInName.equals("texture"))
      updateTexture();
    else if (eventInName.equals("textureTransform")) {
      if (this.textureTransform.node != null) {
        System.err.println("Appearance: textureTransform not implemented");
      }
    }
    else if (eventInName.equals("route_material"))
      this.impl.setCapability(1);
    else if (eventInName.equals("route_texture"))
      this.impl.setCapability(3);
    else if (eventInName.equals("route_textureTransform")) {
      this.impl.setCapability(7);
    }
    else
      System.err.println("Appearance: unknown eventInName " + eventInName);
  }

  public Object clone()
  {
    Appearance a = new Appearance(this.loader, (SFNode)this.material.clone(), (SFNode)this.texture.clone(), (SFNode)this.textureTransform.clone());

    return a;
  }

  public String getType()
  {
    return "Appearance";
  }

  void initFields()
  {
    this.material.init(this, this.FieldSpec, 3, "material");
    this.texture.init(this, this.FieldSpec, 3, "texture");
    this.textureTransform.init(this, this.FieldSpec, 3, "textureTransform");
  }

  class TransparencyListener
    implements PropertyChangeListener
  {
    Material material = null;
    ImageTexture texture = null;

    TransparencyListener() {  }

    public void setMaterial(Material m) { if (m != this.material) {
        if (this.material != null) {
          this.material.removePropertyChangeListener("transparency", this);
          this.material = null;
        }
        this.material = m;
        if (m != null)
          m.addPropertyChangeListener("transparency", this);
      }
    }

    public void setTexture(TextureSrc t)
    {
      if (t != this.texture) {
        if (this.texture != null) {
          this.texture.removePropertyChangeListener("transparency", this);
          this.texture = null;
        }
        if ((t instanceof ImageTexture)) {
          this.texture = ((ImageTexture)t);
          if (t != null)
            this.texture.addPropertyChangeListener("transparency", this);
        }
      }
    }

    public void propertyChange(PropertyChangeEvent e)
    {
      Appearance.this.updateTransparency();
    }
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Appearance
 * JD-Core Version:    0.6.0
 */