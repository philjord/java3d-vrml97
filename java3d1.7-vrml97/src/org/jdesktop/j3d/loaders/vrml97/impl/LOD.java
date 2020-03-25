package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Vector;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.DistanceLOD;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Switch;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;

public class LOD extends NonSharedNode
{
  MFNode level;
  SFVec3f center;
  MFFloat range;
  Group impl;
  Switch implSwitch;
  DistanceLOD implLOD;

  public LOD(Loader loader)
  {
    super(loader);
    this.level = new MFNode();
    this.center = new SFVec3f();
    this.range = new MFFloat();
    initFields();
  }

  LOD(Loader loader, MFNode level, SFVec3f center, MFFloat range)
  {
    super(loader);
    this.level = level;
    this.center = center;
    this.range = range;
    initFields();
  }

  void initImpl()
  {
    this.impl = new Group();

    this.implSwitch = new Switch();
    this.implSwitch.setUserData(new Vector());

    if (this.range.mfloat.length >= 1)
    {
      this.implSwitch.setWhichChild(this.range.mfloat.length - 1);
      double furthest = this.range.mfloat[(this.range.mfloat.length - 1)];
      this.implSwitch.setCapability(17);
      this.implSwitch.setCapability(18);
      this.implSwitch.setUserData(new Vector());
      Point3f lodCenter = new Point3f(this.center.value[0], this.center.value[1], this.center.value[2]);

      this.implLOD = new DistanceLOD(this.range.mfloat, lodCenter);
      this.implLOD.addSwitch(this.implSwitch);

      Point3d boundCenter = new Point3d(lodCenter);
      this.implLOD.setSchedulingBounds(new BoundingSphere(boundCenter, furthest * 1.1D));

      this.impl.addChild(this.implLOD);
    }
    else
    {
      this.implSwitch.setWhichChild(0);
    }
    this.impl.addChild(this.implSwitch);
    replaceLevels();
    this.implNode = this.impl;
    this.implReady = true;
  }

  void replaceLevels()
  {
    int numChildren;
    if ((numChildren = this.implSwitch.numChildren()) != 0) {
      for (int i = 0; i < numChildren; i++) {
        this.implSwitch.removeChild(0);
      }
    }
    for (int i = 0; i < this.level.nodes.length; i++) {
      BaseNode child = this.level.nodes[i];

      child.updateParent(this.implSwitch);

      Node implNode = child.getImplNode();
      if (this.loader.debug) {
        System.out.println(toStringId() + ":  index = " + i + " child node type is " + child.getType() + " " + child.toStringId() + " gets implNoded to " + implNode);
      }

      if (implNode != null) {
        if (implNode.getParent() == null) {
          this.implSwitch.addChild(implNode);
        }
        else {
          this.implSwitch.addChild(implNode.cloneNode(true));
        }
        if ((child instanceof DirectionalLight)) {
          DirectionalLight dirLight = (DirectionalLight)child;
          dirLight.setScope(this.implSwitch);
        }
      }
    }
  }

  public int getNumTris()
  {
    int numTris = 0;
    if (this.level.nodes.length > 0) {
      BaseNode child = this.level.nodes[0];
      if (child != null) {
        numTris += child.getNumTris();
      }
    }
    return numTris;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("level"))
      replaceLevels();
  }

  public Object clone()
  {
    return new LOD(this.loader, (MFNode)this.level.clone(), (SFVec3f)this.center.clone(), (MFFloat)this.range.clone());
  }

  public String getType()
  {
    return "LOD";
  }

  void initFields()
  {
    this.level.init(this, this.FieldSpec, 3, "level");
    this.center.init(this, this.FieldSpec, 0, "center");
    this.range.init(this, this.FieldSpec, 0, "range");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.LOD
 * JD-Core Version:    0.6.0
 */