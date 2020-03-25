package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Vector;
import org.jogamp.java3d.Node;

public class Switch extends NonSharedNode
{
  MFNode choice;
  SFInt32 whichChoice;
  org.jogamp.java3d.Switch impl;

  public Switch(Loader loader)
  {
    super(loader);
    this.choice = new MFNode();
    this.whichChoice = new SFInt32(-1);
    initFields();
  }

  Switch(Loader loader, MFNode choice, SFInt32 whichChoice)
  {
    super(loader);
    this.choice = choice;
    this.whichChoice = whichChoice;
    initFields();
  }

  void initImpl()
  {
    if (this.loader.debug) {
      System.out.println("Switch.initImpl() called");
    }
    this.impl = new org.jogamp.java3d.Switch();
    this.impl.setCapability(17);
    this.impl.setCapability(18);
    this.impl.setCapability(12);
    this.impl.setCapability(13);
    this.impl.setCapability(14);
    this.impl.setUserData(new Vector());
    replaceChoices();
    setWhichChild();
    if (this.loader.debug) {
      System.out.println("Switch " + toStringId() + " impl is " + this.impl);
    }
    this.implNode = this.impl;
    this.implReady = true;
  }

  void setWhichChild()
  {
    if (Browser.debug) {
      System.out.println("Switch: setting " + this.whichChoice.value);
    }
    if ((this.whichChoice.value < 0) || (this.whichChoice.value > this.impl.numChildren())) {
      this.impl.setWhichChild(-1);
    }
    else
      this.impl.setWhichChild(this.whichChoice.value);
  }

  void replaceChoices()
  {
    int numChildren;
    if ((numChildren = this.impl.numChildren()) != 0) {
      for (int i = 0; i < numChildren; i++) {
        this.impl.removeChild(0);
      }
    }
    for (int i = 0; i < this.choice.nodes.length; i++) {
      BaseNode child = this.choice.nodes[i];

      child.updateParent(this.impl);

      Node implNode = child.getImplNode();
      if (this.loader.debug) {
        System.out.println(toStringId() + ":  index = " + i + " child node type is " + child.getType() + " " + child.toStringId() + " gets implNoded to " + implNode);
      }

      if (implNode != null) {
        if (implNode.getParent() == null) {
          this.impl.addChild(implNode);
        }
        else {
          this.impl.addChild(implNode.cloneNode(true));
        }
        if ((child instanceof DirectionalLight)) {
          DirectionalLight dirLight = (DirectionalLight)child;
          dirLight.setScope(this.impl);
        }
      }
    }
  }

  public int getNumTris()
  {
    int numTris = 0;
    if ((this.choice != null) && (this.choice.nodes != null) && (this.choice.nodes.length > 0))
    {
      numTris += this.choice.nodes[0].getNumTris();
    }
    return numTris;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("choice")) {
      replaceChoices();
    }
    else if (eventInName.equals("whichChoice"))
      setWhichChild();
  }

  public Object clone()
  {
    return new Switch(this.loader, (MFNode)this.choice.clone(), (SFInt32)this.whichChoice.clone());
  }

  public String getType()
  {
    return "Switch";
  }

  void initFields()
  {
    this.choice.init(this, this.FieldSpec, 3, "choice");
    this.whichChoice.init(this, this.FieldSpec, 3, "whichChoice");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Switch
 * JD-Core Version:    0.6.0
 */