package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Vector;
import org.jogamp.java3d.BadTransformException;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Vector3d;

public class Transform extends GroupBase
{
  TransformGroup impl;
  SFVec3f center;
  SFRotation rotation;
  SFVec3f scale;
  SFRotation scaleOrientation;
  SFVec3f translation;
  private Transform3D trans1;
  private Transform3D trans2;
  private Transform3D trans3;
  private Transform3D T;
  private Transform3D C;
  private Transform3D R;
  private Transform3D SR;
  private Transform3D S;
  private Transform3D P;
  private boolean pending = false;
  private Vector3d tempVec;
  private AxisAngle4d tempAxis;

  public Transform(Loader loader)
  {
    super(loader);
    this.center = new SFVec3f(0.0F, 0.0F, 0.0F);
    this.rotation = new SFRotation();
    this.scale = new SFVec3f(1.0F, 1.0F, 1.0F);
    this.scaleOrientation = new SFRotation();
    this.translation = new SFVec3f(0.0F, 0.0F, 0.0F);

    initTransformFields();
  }

  Transform(Loader loader, MFNode children, SFVec3f bboxCenter, SFVec3f bboxSize, SFVec3f center, SFRotation rotation, SFVec3f scale, SFRotation scaleOrientation, SFVec3f translation)
  {
    super(loader, children, bboxCenter, bboxSize);

    this.center = center;
    this.rotation = rotation;
    this.scale = scale;
    this.scaleOrientation = scaleOrientation;
    this.translation = translation;

    initTransformFields();
  }

  private boolean floatEq(float val1, float val2)
  {
    float diff = val1 - val2;
    if (diff < 0.0F) {
      diff *= -1.0F;
    }

    return diff < 0.001D;
  }

  void updateTransform()
  {
    if ((this.browser == null) || (this.browser.pendingTransforms.batchReady)) {
      this.tempVec.x = (-this.center.value[0]);
      this.tempVec.y = (-this.center.value[1]);
      this.tempVec.z = (-this.center.value[2]);
      this.trans2.setIdentity();
      this.trans2.setTranslation(this.tempVec);

      float scaleVal = 1.0F;
      if ((floatEq(this.scale.value[0], this.scale.value[1])) && (floatEq(this.scale.value[0], this.scale.value[2])))
      {
        scaleVal = this.scale.value[0];
        this.trans1.set(scaleVal);
      }
      else
      {
        this.tempAxis.x = this.scaleOrientation.rot[0];
        this.tempAxis.y = this.scaleOrientation.rot[1];
        this.tempAxis.z = this.scaleOrientation.rot[2];
        this.tempAxis.angle = (-this.scaleOrientation.rot[3]);
        double tempAxisNormalizer = Math.sqrt(this.tempAxis.x * this.tempAxis.x + this.tempAxis.y * this.tempAxis.y + this.tempAxis.z * this.tempAxis.z);

        this.tempAxis.x /= tempAxisNormalizer;
        this.tempAxis.y /= tempAxisNormalizer;
        this.tempAxis.z /= tempAxisNormalizer;

        this.trans1.set(this.tempAxis);
        this.trans3.mul(this.trans1, this.trans2);
        this.trans1.setNonUniformScale(this.scale.value[0], this.scale.value[1], this.scale.value[2]);

        this.trans2.mul(this.trans1, this.trans3);
        this.tempAxis.x = this.scaleOrientation.rot[0];
        this.tempAxis.y = this.scaleOrientation.rot[1];
        this.tempAxis.z = this.scaleOrientation.rot[2];
        this.tempAxis.angle = this.scaleOrientation.rot[3];
        this.trans1.set(this.tempAxis);
      }
      this.trans3.mul(this.trans1, this.trans2);

      float magSq = this.rotation.rot[0] * this.rotation.rot[0] + this.rotation.rot[1] * this.rotation.rot[1] + this.rotation.rot[2] * this.rotation.rot[2];

      if (magSq < 0.0001D)
      {
        this.tempAxis.x = 0.0D;
        this.tempAxis.y = 0.0D;

        this.tempAxis.z = 0.0D;
      }
      else if ((magSq > 1.01D) || (magSq < 0.99D)) {
        float mag = (float)Math.sqrt(magSq);
        this.tempAxis.x = (this.rotation.rot[0] / mag);
        this.tempAxis.y = (this.rotation.rot[1] / mag);
        this.tempAxis.z = (this.rotation.rot[2] / mag);
      }
      else {
        this.tempAxis.x = this.rotation.rot[0];
        this.tempAxis.y = this.rotation.rot[1];
        this.tempAxis.z = this.rotation.rot[2];
      }

      this.tempAxis.angle = this.rotation.rot[3];
      this.trans1.set(this.tempAxis);

      this.trans2.mul(this.trans1, this.trans3);

      this.tempVec.x = this.center.value[0];
      this.tempVec.y = this.center.value[1];
      this.tempVec.z = this.center.value[2];
      this.trans1.setIdentity();
      this.trans1.setTranslation(this.tempVec);

      this.trans3.mul(this.trans1, this.trans2);

      this.tempVec.x = this.translation.value[0];
      this.tempVec.y = this.translation.value[1];
      this.tempVec.z = this.translation.value[2];
      this.trans1.setIdentity();
      this.trans1.setTranslation(this.tempVec);

      this.trans2.mul(this.trans1, this.trans3);
      try
      {
        this.impl.setTransform(this.trans2);
      }
      catch (BadTransformException bte) {
        if (Browser.debug)
          bte.printStackTrace();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      this.pending = false;
    }
    else if (!this.pending) {
      this.pending = true;
      this.browser.pendingTransforms.add(this);
    }
  }

  void initImpl()
  {
    this.impl = new TransformGroup();
    this.implGroup = this.impl;
    this.implNode = this.impl;
    this.impl.setUserData(new Vector());
    this.trans1 = new Transform3D();
    this.trans2 = new Transform3D();
    this.trans3 = new Transform3D();
    this.T = new Transform3D();
    this.C = new Transform3D();
    this.R = new Transform3D();
    this.SR = new Transform3D();
    this.S = new Transform3D();
    this.P = new Transform3D();
    this.tempVec = new Vector3d();
    this.tempAxis = new AxisAngle4d();

    if ((this.rotation.rot[0] == this.rotation.rot[1]) && (this.rotation.rot[1] == this.rotation.rot[2]) && (this.rotation.rot[2] == 0.0F))
    {
      this.rotation.rot[1] = 1.0F;
    }

    if ((this.scale.value[0] == this.scale.value[1]) && (this.scale.value[0] == this.scale.value[2]) && (this.scale.value[0] == 0.0F))
    {
      this.scale.setValue(1.0F, 1.0F, 1.0F);
    }

    updateTransform();
    super.replaceChildren();
    this.implReady = true;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("rotation")) {
      updateTransform();
    }
    else if ((eventInName.equals("scale")) || (eventInName.equals("scaleOrientation")) || (eventInName.equals("center")) || (eventInName.equals("translation")))
    {
      updateTransform();
    }
    else if ((eventInName.equals("route_rotation")) || (eventInName.equals("route_translation")) || (eventInName.equals("route_scale")) || (eventInName.equals("route_center")) || (eventInName.equals("route_scaleOrientation")))
    {
      this.impl.setCapability(18);
      this.impl.setCapability(17);
      this.impl.setCapability(1);
    }
    else {
      super.notifyMethod(eventInName, time);
    }
  }

  public Object clone()
  {
    if (this.loader.debug) {
      System.out.println("Transform.clone() called");
    }
    return new Transform(this.loader, (MFNode)this.children.clone(), (SFVec3f)this.bboxCenter.clone(), (SFVec3f)this.bboxSize.clone(), (SFVec3f)this.center.clone(), (SFRotation)this.rotation.clone(), (SFVec3f)this.scale.clone(), (SFRotation)this.scaleOrientation.clone(), (SFVec3f)this.translation.clone());
  }

  public String getType()
  {
    return "Transform";
  }

  void initFields()
  {
    super.initFields();
    initTransformFields();
  }

  void initTransformFields()
  {
    this.center.init(this, this.FieldSpec, 3, "center");
    this.rotation.init(this, this.FieldSpec, 3, "rotation");
    this.scale.init(this, this.FieldSpec, 3, "scale");
    this.scaleOrientation.init(this, this.FieldSpec, 3, "scaleOrientation");

    this.translation.init(this, this.FieldSpec, 3, "translation");
  }

  public String toStringBodyS()
  {
    String retval = "Transform {\n";
    if ((this.center.value[0] != 0.0D) || (this.center.value[1] != 0.0D) || (this.center.value[2] != 0.0D))
    {
      retval = retval + "center " + this.center;
    }
    if ((this.rotation.rot[0] != 0.0D) || (this.rotation.rot[1] != 0.0D) || (this.rotation.rot[2] != 1.0D) || (this.rotation.rot[3] != 0.0D))
    {
      retval = retval + "rotation " + this.rotation;
    }
    if ((this.scale.value[0] != 1.0D) || (this.scale.value[1] != 1.0D) || (this.scale.value[2] != 1.0D))
    {
      retval = retval + "scale " + this.scale;
    }
    if ((this.scaleOrientation.rot[0] != 0.0D) || (this.scaleOrientation.rot[1] != 0.0D) || (this.scaleOrientation.rot[2] != 1.0D) || (this.scaleOrientation.rot[3] != 0.0D))
    {
      retval = retval + "scaleOrientation " + this.scaleOrientation;
    }
    if ((this.translation.value[0] != 0.0D) || (this.translation.value[1] != 0.0D) || (this.translation.value[2] != 0.0D))
    {
      retval = retval + "translation " + this.translation;
    }
    retval = retval + super.toStringBody();
    retval = retval + "}";
    return retval;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Transform
 * JD-Core Version:    0.6.0
 */