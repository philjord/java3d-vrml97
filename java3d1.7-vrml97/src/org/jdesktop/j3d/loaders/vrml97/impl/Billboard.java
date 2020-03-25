package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.Group;
import org.jogamp.java3d.TransformGroup;

public class Billboard extends GroupBase
{
  SFVec3f axisOfRotation;
  Group impl;
  TransformGroup implTrans;
  org.jogamp.java3d.Billboard implBillboard;

  public Billboard(Loader loader)
  {
    super(loader);
    this.axisOfRotation = new SFVec3f(0.0F, 1.0F, 0.0F);
    initBillboardFields();
  }

  Billboard(Loader loader, MFNode children, SFVec3f bboxCenter, SFVec3f bboxSize, SFVec3f aor)
  {
    super(loader, children, bboxCenter, bboxSize);
    this.axisOfRotation = aor;
    initBillboardFields();
  }

  public void initImpl()
  {
    this.impl = new Group();
    this.implTrans = new TransformGroup();
    this.implGroup = this.implTrans;
    this.implNode = this.impl;
    this.implGroup.setCapability(18);
    this.implGroup.setCapability(17);
    this.implBillboard = new org.jogamp.java3d.Billboard(this.implTrans);
    setAxis();
    this.impl.addChild(this.implBillboard);
    this.impl.addChild(this.implTrans);
    this.implBillboard.setTarget(this.implTrans);
    this.implBillboard.setSchedulingBoundingLeaf(this.loader.infiniteBoundingLeaf);
    super.replaceChildren();
    this.implReady = true;
  }

  private void setAxis()
  {
    float[] axis = this.axisOfRotation.value;
    if ((axis[0] == 0.0D) && (axis[1] == 0.0D) && (axis[2] == 0.0D))
    {
      this.implBillboard.setAlignmentMode(1);

      this.implBillboard.setRotationPoint(0.0F, 0.0F, 0.0F);
    }
    else {
      this.implBillboard.setAlignmentMode(0);

      this.implBillboard.setAlignmentAxis(axis[0], axis[1], axis[2]);
    }
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("axisOfRotation")) {
      setAxis();
    }
    else if (!eventInName.equals("route_axisOfRotation"))
    {
      super.notifyMethod(eventInName, time);
    }
  }

  public Object clone()
  {
    return new Billboard(this.loader, (MFNode)this.children.clone(), (SFVec3f)this.bboxCenter.clone(), (SFVec3f)this.bboxSize.clone(), (SFVec3f)this.axisOfRotation.clone());
  }

  public String getType()
  {
    return "Billboard";
  }

  void initFields()
  {
    super.initFields();
    initBillboardFields();
  }

  void initBillboardFields()
  {
    this.axisOfRotation.init(this, this.FieldSpec, 3, "axisOfRotation");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Billboard
 * JD-Core Version:    0.6.0
 */