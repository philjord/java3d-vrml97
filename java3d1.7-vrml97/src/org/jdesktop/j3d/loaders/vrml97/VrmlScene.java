package org.jdesktop.j3d.loaders.vrml97;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Sound;
import org.jogamp.java3d.TransformGroup;
import org.jdesktop.j3d.loaders.vrml97.impl.TreeCleaner;
import org.jdesktop.j3d.loaders.vrml97.node.Viewpoint;

public class VrmlScene
  implements org.jogamp.java3d.loaders.Scene
{
  org.jdesktop.j3d.loaders.vrml97.impl.Scene base;
  String description;
  int numTris;
  vrml.BaseNode[] objects;
  Viewpoint[] viewpoints;
  vrml.node.Node[] navInfos;
  org.jdesktop.j3d.loaders.vrml97.node.Background[] backgrounds;
  org.jdesktop.j3d.loaders.vrml97.node.Fog[] fogs;
  org.jdesktop.j3d.loaders.vrml97.node.Light[] lights;
  Hashtable defTable;
  BranchGroup scene = null;

  VrmlScene(org.jdesktop.j3d.loaders.vrml97.impl.Scene base)
  {
    this.base = base;
    this.description = base.description;
    this.numTris = base.numTris;

    this.objects = new vrml.BaseNode[base.objects.size()];
    Enumeration elems = base.objects.elements();
    for (int i = 0; i < this.objects.length; i++) {
      org.jdesktop.j3d.loaders.vrml97.impl.BaseNode node = (org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)elems.nextElement();

      this.objects[i] = node.wrap();
    }

    this.viewpoints = new Viewpoint[base.viewpoints.size()];
    elems = base.viewpoints.elements();
    for (int i = 0; i < this.viewpoints.length; i++) {
      this.viewpoints[i] = ((Viewpoint)((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)elems.nextElement()).wrap());
    }

    this.navInfos = new vrml.node.Node[base.navInfos.size()];
    elems = base.navInfos.elements();
    for (int i = 0; i < this.navInfos.length; i++) {
      this.navInfos[i] = ((vrml.node.Node)((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)elems.nextElement()).wrap());
    }

    this.backgrounds = new org.jdesktop.j3d.loaders.vrml97.node.Background[base.backgrounds.size()];
    elems = base.backgrounds.elements();
    for (int i = 0; i < this.backgrounds.length; i++) {
      this.backgrounds[i] = ((org.jdesktop.j3d.loaders.vrml97.node.Background)((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)elems.nextElement()).wrap());
    }

    this.fogs = new org.jdesktop.j3d.loaders.vrml97.node.Fog[base.fogs.size()];
    elems = base.fogs.elements();
    for (int i = 0; i < this.fogs.length; i++) {
      this.fogs[i] = ((org.jdesktop.j3d.loaders.vrml97.node.Fog)((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)elems.nextElement()).wrap());
    }

    this.lights = new org.jdesktop.j3d.loaders.vrml97.node.Light[base.lights.size()];
    elems = base.lights.elements();
    for (int i = 0; i < this.lights.length; i++) {
      this.lights[i] = ((org.jdesktop.j3d.loaders.vrml97.node.Light)((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)elems.nextElement()).wrap());
    }

    this.defTable = new Hashtable();
    for (elems = base.defTable.keys(); elems.hasMoreElements(); ) {
      Object key = elems.nextElement();
      Object value = ((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)base.defTable.get(key)).wrap();

      this.defTable.put(key, value);
    }
  }

  public BranchGroup getSceneGroup()
  {
    if (this.scene == null) {
      this.scene = new BranchGroup();
      for (int i = 0; i < this.objects.length; i++)
      {
        org.jogamp.java3d.Node j3dNode;
        if ((j3dNode = this.objects[i].getImplNode()) == null)
          continue;
        this.scene.addChild(j3dNode);
      }
    }

    return this.scene;
  }

  public TransformGroup[] getViewGroups()
  {
    TransformGroup[] views = new TransformGroup[this.viewpoints.length];
    for (int i = 0; i < this.viewpoints.length; i++) {
      views[i] = ((TransformGroup)this.viewpoints[i].getImplNode());
    }
    return views;
  }

  public float[] getHorizontalFOVs()
  {
    float[] fovs = new float[this.viewpoints.length];
    for (int i = 0; i < this.viewpoints.length; i++) {
      fovs[i] = this.viewpoints[i].getFOV();
    }
    return fovs;
  }

  public org.jogamp.java3d.Light[] getLightNodes()
  {
    org.jogamp.java3d.Light[] j3dLights = new org.jogamp.java3d.Light[this.lights.length * 2];

    for (int i = 0; i < this.lights.length; i++) {
      j3dLights[(i * 2)] = this.lights[i].getAmbientLight();
      j3dLights[(i * 2 + 1)] = this.lights[i].getLight();
    }
    return j3dLights;
  }

  public Hashtable getNamedObjects()
  {
    Hashtable j3dDefTable = new Hashtable();
    Enumeration elems = this.defTable.keys();
    while (elems.hasMoreElements()) {
      Object key = elems.nextElement();
      vrml.BaseNode node = (vrml.BaseNode)this.defTable.get(key);
      org.jogamp.java3d.Node value = node.getImplNode();
      if (value != null) {
        j3dDefTable.put(key, value);
      }
    }
    return j3dDefTable;
  }

  public org.jogamp.java3d.Background[] getBackgroundNodes()
  {
    org.jogamp.java3d.Background[] j3dBackgrounds = new org.jogamp.java3d.Background[this.backgrounds.length];

    for (int i = 0; i < this.backgrounds.length; i++) {
      j3dBackgrounds[i] = this.backgrounds[i].getBackgroundImpl();
    }
    return j3dBackgrounds;
  }

  public org.jogamp.java3d.Fog[] getFogNodes()
  {
    org.jogamp.java3d.Fog[] j3dFogs = new org.jogamp.java3d.Fog[this.fogs.length];
    for (int i = 0; i < this.fogs.length; i++) {
      j3dFogs[i] = this.fogs[i].getFogImpl();
    }
    return j3dFogs;
  }

  public Behavior[] getBehaviorNodes()
  {
    return null;
  }

  public Sound[] getSoundNodes()
  {
    return null;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void cleanForCompile(org.jogamp.java3d.Node root)
  {
    TreeCleaner.cleanSubgraph(root);
  }

  public vrml.BaseNode[] getObjects()
  {
    vrml.BaseNode[] nodes = new vrml.BaseNode[this.objects.length];
    for (int i = 0; i < this.objects.length; i++) {
      nodes[i] = this.objects[i];
    }
    return nodes;
  }

  public Viewpoint[] getViewpoints()
  {
    Viewpoint[] vps = new Viewpoint[this.viewpoints.length];
    for (int i = 0; i < this.viewpoints.length; i++) {
      vps[i] = this.viewpoints[i];
    }
    return vps;
  }

  public Hashtable getDefineTable()
  {
    Hashtable userDefTable = new Hashtable();
    Enumeration elems = this.defTable.keys();
    while (elems.hasMoreElements()) {
      Object key = elems.nextElement();
      Object value = this.defTable.get(key);
      userDefTable.put(key, value);
    }
    return userDefTable;
  }

  public int getNumTris()
  {
    return this.numTris;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.VrmlScene
 * JD-Core Version:    0.6.0
 */