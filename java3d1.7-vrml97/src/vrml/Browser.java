package vrml;

import java.awt.Container;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jogamp.java3d.Canvas3D;

public class Browser
{
  org.jdesktop.j3d.loaders.vrml97.impl.Browser impl;

  public Browser(org.jdesktop.j3d.loaders.vrml97.impl.Browser b)
  {
    this.impl = b;
  }

  public Browser()
  {
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.Browser();
  }

  public Browser(Canvas3D canvas)
  {
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.Browser(canvas);
  }

  public String getDescription()
  {
    return this.impl.getDescription();
  }

  public String getName()
  {
    return this.impl.getName();
  }

  public String getVersion()
  {
    return this.impl.getVersion();
  }

  public float getCurrentSpeed()
  {
    return this.impl.getCurrentSpeed();
  }

  public float getCurrentFrameRate()
  {
    return this.impl.getCurrentFrameRate();
  }

  public String getWorldURL()
  {
    return this.impl.getWorldURL();
  }

  public URL getURL()
  {
    return this.impl.getURL();
  }

  public void replaceWorld(BaseNode[] nodes)
  {
    org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes = new org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[nodes.length];

    for (int i = 0; i < nodes.length; i++) {
      implNodes[i] = nodes[i].getImpl();
    }
    this.impl.replaceWorld(implNodes);
  }

  public BaseNode[] createVrmlFromString(String vrmlSyntax)
    throws InvalidVRMLSyntaxException
  {
    org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes = this.impl.createVrmlFromString(vrmlSyntax);

    BaseNode[] nodes = new BaseNode[implNodes.length];
    for (int i = 0; i < nodes.length; i++) {
      nodes[i] = implNodes[i].wrap();
    }
    return nodes;
  }

  public void createVrmlFromURL(String[] url, BaseNode node, String event)
    throws InvalidVRMLSyntaxException
  {
    this.impl.createVrmlFromURL(url, node.getImpl(), event);
  }

  public void addRoute(BaseNode fromNode, String fromEventOut, BaseNode toNode, String toEventIn)
  {
    this.impl.addRoute(fromNode.getImpl(), fromEventOut, toNode.getImpl(), toEventIn);
  }

  public void deleteRoute(BaseNode fromNode, String fromEventOut, BaseNode toNode, String toEventIn)
  {
    this.impl.deleteRoute(fromNode.getImpl(), fromEventOut, toNode.getImpl(), toEventIn);
  }

  public void loadURL(String[] url, String[] parameter)
    throws InvalidVRMLSyntaxException, MalformedURLException, IOException
  {
    this.impl.loadURL(url, parameter);
  }

  public void loadStringAsVrml(String sourceVrml)
  {
    this.impl.loadStringAsVrml(sourceVrml);
  }

  public void setDescription(String description)
  {
    this.impl.setDescription(description);
  }

  public void setViewpoint(int vi)
  {
    this.impl.setViewpoint(vi);
  }

  public void resetViewpoint()
  {
    this.impl.resetViewpoint();
  }

  public String[] getViewpointDescriptions()
  {
    return this.impl.getViewpointDescriptions();
  }

  public void outputTiming()
  {
    this.impl.outputTiming();
  }

  public void shutDown()
  {
    this.impl.shutDown();
  }

  public void startRender()
  {
    this.impl.startRender();
  }

  public void stopRender()
  {
    this.impl.stopRender();
  }

  public Canvas3D getCanvas3D()
  {
    return this.impl.getCanvas3D();
  }

  public void setAWTContainer(Container container)
  {
    this.impl.setAWTContainer(container);
  }

  public void setAutoSmooth(boolean s)
  {
    this.impl.setAutoSmooth(s);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.Browser
 * JD-Core Version:    0.6.0
 */