package org.jdesktop.j3d.loaders.vrml97.impl;

public abstract class ImplNode extends Node
{
  public ImplNode(Browser browser)
  {
    super(browser);
  }

  public abstract org.jogamp.java3d.Node getImplNode();
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ImplNode
 * JD-Core Version:    0.6.0
 */