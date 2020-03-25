package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.Node;

public abstract class GroupBase extends NonSharedNode
{
  MFNode addChildren;
  MFNode removeChildren;
  MFNode children;
  SFVec3f bboxCenter;
  SFVec3f bboxSize;
  Group implGroup;

  public GroupBase(Loader loader)
  {
    super(loader);
    this.children = new MFNode();
    this.bboxCenter = new SFVec3f();
    this.bboxSize = new SFVec3f(-1.0F, -1.0F, -1.0F);
    this.addChildren = new MFNode();
    this.removeChildren = new MFNode();
    initGroupBaseFields();
  }

  GroupBase(Loader loader, MFNode children, SFVec3f bboxCenter, SFVec3f bboxSize)
  {
    super(loader);
    this.children = children;
    this.bboxCenter = bboxCenter;
    this.bboxSize = bboxSize;
    this.addChildren = new MFNode();
    this.removeChildren = new MFNode();
    initGroupBaseFields();
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("addChildren")) {
      MFNode newKids = (MFNode)this.FieldSpec.get(eventInName);
      doAddChildren(newKids);
    }
    else if (eventInName.equals("removeChildren")) {
      MFNode badKids = (MFNode)this.FieldSpec.get(eventInName);
      doRemoveChildren(badKids);
    }
    else if (eventInName.equals("children")) {
      replaceChildren();
    }
    else if (eventInName.equals("route_children")) {
      this.implGroup.setCapability(12);
      this.implGroup.setCapability(13);
      this.implGroup.setCapability(14);
    }
    else
    {
      System.out.println("GroupBase: unknown eventInName: " + eventInName);
    }
  }

  void replaceChildren()
  {
    int numChildren;
    if ((numChildren = this.implGroup.numChildren()) != 0) {
      for (int i = 0; i < numChildren; i++) {
        this.implGroup.removeChild(0);
      }
    }
    for (int i = 0; i < this.children.nodes.length; i++) {
      BaseNode child = this.children.nodes[i];

      child.updateParent(this.implGroup);

      child.parent = this;
      boolean doit = false;
      if ((child instanceof VrmlSensor)) {
        doit = true;
      }
      if (((child instanceof ProtoInstance)) && 
        (((ProtoInstance)child).containsSensor)) {
        Enumeration e = ((ProtoInstance)child).sensors.elements();
        while (e.hasMoreElements()) {
          BaseNode vs = (BaseNode)(BaseNode)e.nextElement();
          vs.updateParent(this.implGroup);
        }
        doit = true;
      }

      if (doit) {
        this.implGroup.setCapability(1);
        this.implGroup.setCapability(3);
        this.implGroup.setCapability(9);
        this.implGroup.setCapability(11);
        this.implGroup.setPickable(true);
      }

      Node implNode = child.getImplNode();
      if (this.loader.debug) {
        System.out.println(toStringId() + ":  index = " + i + " child node type is " + child.getType() + " " + child.toStringId() + " gets implNoded to " + implNode);
      }

      if (implNode != null) {
        if (implNode.getParent() == null) {
          this.implGroup.addChild(implNode);
        }
        else {
          this.implGroup.addChild(implNode.cloneNode(true));
        }
        if ((child instanceof DirectionalLight)) {
          DirectionalLight dirLight = (DirectionalLight)child;
          dirLight.setScope(this.implGroup);
        }
      }
    }
  }

  public int getNumTris()
  {
    int numTris = 0;
    for (int i = 0; i < this.children.nodes.length; i++) {
      BaseNode child = this.children.nodes[i];
      numTris += child.getNumTris();
    }
    return numTris;
  }

  void doAddChildren(MFNode newChildren)
  {
    BaseNode[] newCh = newChildren.getValue();
    BaseNode[] oldCh = this.children.getValue();

    Vector v = new Vector(newCh.length + oldCh.length);
    for (int i = 0; i < oldCh.length; i++) {
      v.addElement(oldCh[i]);
    }

    for (int i = 0; i < newCh.length; i++) {
      if (this.loader.debug) {
        System.out.println(this + " doAddChildren " + newCh[i]);
      }
      v.addElement(newCh[i]);
    }

    BaseNode[] newFamily = new BaseNode[newCh.length + oldCh.length];
    v.copyInto(newFamily);

    this.children = new MFNode(newFamily);
    this.FieldSpec.remove("children");
    this.children.init(this, this.FieldSpec, 3, "children");

    replaceChildren();
  }

  void doRemoveChildren(MFNode removeChildren)
  {
    BaseNode[] oldCh = this.children.getValue();

    BaseNode[] removeCh = removeChildren.getValue();

    Vector v = new Vector(this.children.getValue().length);

    for (int i = 0; i < oldCh.length; i++) {
      v.addElement(oldCh[i]);
    }

    for (int i = 0; i < removeCh.length; i++)
    {
      if (this.loader.debug) {
        System.out.println("doRemoveChildren " + removeCh[i]);
      }
      v.removeElement(removeCh[i]);
    }

    BaseNode[] newFamily = new BaseNode[this.children.getSize() - removeCh.length];

    v.copyInto(newFamily);

    this.children = new MFNode(newFamily);
    this.FieldSpec.remove("children");
    this.FieldSpec.put("children", this.children);
    replaceChildren();
  }

  public String getType()
  {
    return "Group";
  }

  void initFields()
  {
  }

  void initGroupBaseFields()
  {
    this.addChildren.init(this, this.FieldSpec, 1, "addChildren");
    this.removeChildren.init(this, this.FieldSpec, 1, "removeChildren");
    this.children.init(this, this.FieldSpec, 3, "children");
    this.bboxCenter.init(this, this.FieldSpec, 0, "bboxCenter");
    this.bboxSize.init(this, this.FieldSpec, 0, "bboxSize");
  }

  public String toStringBodyS()
  {
    String retval = "children " + this.children;
    if ((this.bboxCenter.value[0] != 0.0D) || (this.bboxCenter.value[1] != 0.0D) || (this.bboxCenter.value[2] != 0.0D))
    {
      retval = retval + "bboxCenter " + this.bboxCenter;
    }
    if ((this.bboxSize.value[0] != -1.0D) || (this.bboxSize.value[1] != -1.0D) || (this.bboxSize.value[2] != -1.0D))
    {
      retval = retval + "bboxSize " + this.bboxSize;
    }
    return retval;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.GroupBase
 * JD-Core Version:    0.6.0
 */