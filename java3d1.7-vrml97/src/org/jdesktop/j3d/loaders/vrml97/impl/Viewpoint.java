package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.BoundingLeaf;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.ViewPlatform;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import vrml.BaseNode;

public class Viewpoint extends BindableNode
{
  BranchGroup impl;
  TransformGroup implOrient;
  TransformGroup implWorld;
  SFFloat fieldOfView;
  SFBool jump;
  SFRotation orientation;
  SFVec3f position;
  SFString description;
  SFRotation examine;
  Transform3D implTrans;
  Transform3D implBrowserTrans;
  TransformGroup implBrowser;
  Transform3D examineViewpoint = new Transform3D();
  Transform3D examineRotation = new Transform3D();
  ViewPlatform implViewPlatform;
  BoundingLeaf implBoundingLeaf;
  AxisAngle4d axis = new AxisAngle4d();
  Vector3d trans = new Vector3d();

  public Viewpoint(Loader loader)
  {
    super(loader, loader.getViewpointStack());
    this.fieldOfView = new SFFloat(0.5398F);
    this.jump = new SFBool(true);
    this.orientation = new SFRotation(0.0F, 0.0F, 1.0F, 0.0F);
    this.examine = new SFRotation(0.0F, 0.0F, 1.0F, 0.0F);
    this.position = new SFVec3f(0.0F, 0.0F, 10.0F);
    this.description = new SFString("");
    loader.addViewpoint(this);
    initFields();
  }

  public Viewpoint(Loader loader, SFBool bind, SFTime bindTime, SFBool isBound, SFFloat fieldOfView, SFBool jump, SFRotation orientation, SFVec3f position, SFString description)
  {
    super(loader, loader.getViewpointStack(), bind, bindTime, isBound);
    this.fieldOfView = fieldOfView;
    this.jump = jump;
    this.orientation = orientation;
    this.position = position;
    this.description = description;
    this.examine = new SFRotation(0.0F, 0.0F, 1.0F, 0.0F);
    loader.addViewpoint(this);
    initFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if ((eventInName.equals("position")) || (eventInName.equals("orientation")))
    {
      updateViewTrans();
    }
    else if (eventInName.equals("examine")) {
      updateViewTransExamine();
    }
    else if ((eventInName.equals("fieldOfView")) || (eventInName.equals("description")) || (eventInName.equals("jump")))
    {
      updateBrowser();
    }
    else if (eventInName.equals("bind"))
    {
      super.notifyMethod("bind", time);
    }
    else if ((!eventInName.equals("route_position")) && (!eventInName.equals("route_orientation")) && (!eventInName.equals("route_fieldOfView")) && (!eventInName.equals("route_bind")) && (!eventInName.equals("route_description")))
    {
      if (eventInName.equals("route_examine")) {
        updateViewTrans();
      }
      else
        System.err.println("Viewpoint: unexpected notify " + eventInName);
    }
  }

  void updateViewTrans()
  {
    this.axis.x = this.orientation.rot[0];
    this.axis.y = this.orientation.rot[1];
    this.axis.z = this.orientation.rot[2];
    double normalizer = Math.sqrt(this.axis.x * this.axis.x + this.axis.y * this.axis.y + this.axis.z * this.axis.z);

    if (normalizer < 0.001D) {
      this.axis.x = 0.0D;
      this.axis.y = 1.0D;
      this.axis.z = 0.0D;
    }
    else {
      this.axis.x /= normalizer;
      this.axis.y /= normalizer;
      this.axis.z /= normalizer;
    }
    this.axis.angle = Math.IEEEremainder(this.orientation.rot[3], 6.283185307179586D);
    this.implTrans.setIdentity();
    this.implTrans.set(this.axis);
    this.trans.x = this.position.value[0];
    this.trans.y = this.position.value[1];
    this.trans.z = this.position.value[2];
    this.implTrans.setTranslation(this.trans);
    if ((this.implTrans.getType() & 0x40) != 0)
      this.implOrient.setTransform(this.implTrans);
  }

  void updateViewTransExamine()
  {
    this.axis.x = (-this.examine.rot[0]);
    this.axis.y = (-this.examine.rot[1]);
    this.axis.z = (-this.examine.rot[2]);
    double normalizer = Math.sqrt(this.axis.x * this.axis.x + this.axis.y * this.axis.y + this.axis.z * this.axis.z);

    if (normalizer < 0.001D) {
      this.axis.x = 0.0D;
      this.axis.y = 1.0D;
      this.axis.z = 0.0D;
    }
    else {
      this.axis.x /= normalizer;
      this.axis.y /= normalizer;
      this.axis.z /= normalizer;
    }
    this.axis.angle = Math.IEEEremainder(this.examine.rot[3], 6.283185307179586D);
    this.examineRotation.set(this.axis);
    this.examineViewpoint.mul(this.examineRotation, this.implTrans);
    if ((this.examineViewpoint.getType() & 0x40) != 0)
      this.implWorld.setTransform(this.examineViewpoint);
  }

  void updateBrowser()
  {
    this.browser.viewChanged(this);
  }

  void initImpl()
  {
    if (this.loader.browser != null)
    {
      this.implWorld = new TransformGroup();
      this.implWorld.setCapability(12);
      this.implWorld.setCapability(13);
      this.implWorld.setCapability(14);
      this.implWorld.setCapability(18);
      this.implWorld.setCapability(17);
      this.implWorld.setCapability(9);

      this.implWorld.setCapability(10);

      this.implWorld.setCapability(3);
      this.implWorld.setCapability(4);
      this.implWorld.setCapability(16);

      this.implTrans = new Transform3D();
      this.implOrient = new TransformGroup();
      this.implOrient.setCapability(12);
      this.implOrient.setCapability(13);
      this.implOrient.setCapability(14);
      this.implOrient.setCapability(18);
      this.implOrient.setCapability(17);
      this.implOrient.setCapability(9);

      this.implOrient.setCapability(10);

      this.implOrient.setCapability(10);
      this.implOrient.setCapability(3);
      this.implOrient.setCapability(4);
      this.implOrient.setCapability(16);
      updateViewTrans();

      this.implBrowser = new TransformGroup();
      this.implBrowserTrans = new Transform3D();
      this.implBrowser.setTransform(this.implBrowserTrans);
      this.implBrowser.setCapability(17);
      this.implBrowser.setCapability(18);
      this.implBrowser.setCapability(12);
      this.implBrowser.setCapability(13);
      this.implBrowser.setCapability(14);
      this.implBrowser.setCapability(9);

      this.implBrowser.setCapability(10);

      this.implBrowser.setCapability(3);
      this.implBrowser.setCapability(4);
      this.implBrowser.setCapability(16);

      BranchGroup t = new RGroup();
      t.addChild(this.implBrowser);

      this.implOrient.addChild(t);
      this.implOrient.addChild(new RGroup());
      this.implOrient.addChild(new RGroup());
      this.implOrient.addChild(new RGroup());
      ((RGroup)this.implOrient.getChild(3)).addChild(this.implBoundingLeaf = new BoundingLeaf(new BoundingSphere(new Point3d(0.0D, 0.0D, 0.0D), 100.0D)));

      this.implViewPlatform = new ViewPlatform();
      BranchGroup u = new RGroup();
      u.addChild(this.implViewPlatform);
      this.implViewPlatform.setActivationRadius(3.4028235E+38F);
      this.implViewPlatform.setCapability(11);

      this.implBrowser.addChild(u);
      this.implBrowser.addChild(new RGroup());
      this.implBrowser.addChild(new RGroup());

      this.impl = new RGroup();
      this.impl.addChild(this.implWorld);
      this.implWorld.addChild(this.implOrient);

      if (this.loader.debug) {
        System.out.println("Viewpoint impl = " + this.impl);
      }

      this.implNode = this.impl;
    }
    else
    {
      this.implOrient = new TransformGroup();
      this.implOrient.setCapability(12);
      this.implOrient.setCapability(13);
      this.implOrient.setCapability(14);
      this.implOrient.setCapability(18);
      this.implOrient.setCapability(17);
      this.implOrient.setCapability(9);

      this.implOrient.setCapability(10);

      this.implOrient.setCapability(3);
      this.implOrient.setCapability(4);
      this.implOrient.setCapability(16);

      this.implTrans = new Transform3D();
      updateViewTrans();

      this.implViewPlatform = new ViewPlatform();
      this.implViewPlatform.setActivationRadius(3.4028235E+38F);
      this.implOrient.addChild(this.implViewPlatform);
      this.implNode = this.implOrient;
    }

    this.implReady = true;
  }

  void frameObject(BoundingSphere bounds)
  {
    Point3d p = new Point3d();
    bounds.getCenter(p);

    this.position.value[0] = (float)p.x;
    this.position.value[1] = ((float)p.y / 1.05F);
    this.position.value[2] = ((float)p.z + 3.14F * (float)bounds.getRadius());
    updateViewTrans();
  }

  public ViewPlatform getViewPlatform()
  {
    return this.implViewPlatform;
  }

  public TransformGroup getTransformGroup()
  {
    return this.implOrient;
  }

  public float getFOV()
  {
    return this.fieldOfView.value;
  }

  public String getDescription()
  {
    return this.description.getValue();
  }

  public String getType()
  {
    return "Viewpoint";
  }

  public Object clone()
  {
    return new Viewpoint(this.loader, (SFBool)this.bind.clone(), (SFTime)this.bindTime.clone(), (SFBool)this.isBound.clone(), (SFFloat)this.fieldOfView.clone(), (SFBool)this.jump.clone(), (SFRotation)this.orientation.clone(), (SFVec3f)this.position.clone(), (SFString)this.description.clone());
  }

  void initFields()
  {
    initBindableFields();
    this.fieldOfView.init(this, this.FieldSpec, 3, "fieldOfView");
    this.jump.init(this, this.FieldSpec, 3, "jump");
    this.orientation.init(this, this.FieldSpec, 3, "orientation");
    this.position.init(this, this.FieldSpec, 3, "position");
    this.description.init(this, this.FieldSpec, 0, "description");
    this.examine.init(this, this.FieldSpec, 3, "examine");
  }

  public BaseNode wrap()
  {
    return new org.jdesktop.j3d.loaders.vrml97.node.Viewpoint(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Viewpoint
 * JD-Core Version:    0.6.0
 */