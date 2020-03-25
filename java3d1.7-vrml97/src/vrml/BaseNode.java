package vrml;

import org.jogamp.java3d.Node;

public abstract class BaseNode
{
  protected org.jdesktop.j3d.loaders.vrml97.impl.BaseNode implBaseNode;

  protected BaseNode(org.jdesktop.j3d.loaders.vrml97.impl.BaseNode init)
  {
    this.implBaseNode = init;
  }

  public abstract String getType();

  public abstract Browser getBrowser();

  protected static org.jdesktop.j3d.loaders.vrml97.impl.Browser browserImpl(Browser browser)
  {
    return browser.impl;
  }

  protected org.jdesktop.j3d.loaders.vrml97.impl.BaseNode getImpl()
  {
    return this.implBaseNode;
  }

  public Node getImplNode()
  {
    return this.implBaseNode.getImplNode();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.BaseNode
 * JD-Core Version:    0.6.0
 */