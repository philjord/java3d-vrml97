package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.Node;

public abstract class BaseNode
{
  String defName;
  Browser browser;
  Loader loader;
  boolean implReady;
  Node implNode;
  BaseNode parent;

  public BaseNode(Browser browser)
  {
    this.browser = browser;
  }

  BaseNode(Loader loader)
  {
    this.loader = loader;
    this.browser = loader.browser;
  }

  public abstract String getType();

  public abstract Object clone();

  public abstract void notifyMethod(String paramString, double paramDouble);

  public abstract vrml.BaseNode wrap();

  public abstract Field getField(String paramString);

  public Browser getBrowser()
  {
    return this.browser;
  }

  void initImpl()
  {
    if (this.loader.debug) {
      System.out.println("BaseNode.initImpl() called on node " + toStringId());
    }

    this.implNode = null;
    this.implReady = true;
  }

  void updateParent(Node parentImpl)
  {
  }

  public Node getImplNode()
  {
    return this.implNode;
  }

  void define(String defName)
  {
    this.defName = defName;
  }

  void registerUse(Scene scene)
  {
  }

  public int getNumTris()
  {
    return 0;
  }

  public final String toString()
  {
    String retval = "";
    if (this.defName != null) {
      retval = retval + "DEF " + this.defName + " ";
    }
    retval = retval + toStringBody();
    return retval;
  }

  public String toStringId()
  {
    return getClass().getName() + "@" + Integer.toHexString(hashCode());
  }

  String toStringBody()
  {
    return toStringId();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.BaseNode
 * JD-Core Version:    0.6.0
 */