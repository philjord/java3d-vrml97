package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Hashtable;
import java.util.Vector;
import org.jogamp.java3d.SharedGroup;

public class Scene
  implements Namespace
{
  public Vector objects = new Vector();

  public Vector viewpoints = new Vector();

  public Vector navInfos = new Vector();
  public WorldInfo worldInfo;
  public Vector backgrounds = new Vector();

  public Vector fogs = new Vector();

  public Vector lights = new Vector();

  public Vector sharedGroups = new Vector();

  public Vector timeSensors = new Vector();

  public Vector visibilitySensors = new Vector();

  public Vector touchSensors = new Vector();

  public Vector audioClips = new Vector();

  public Hashtable defTable = new Hashtable();

  public Proto firstProto = null;

  public Hashtable protos = new Hashtable();

  public String description = null;
  public int numTris;

  void addObject(BaseNode object)
  {
    if (object != null) {
      this.objects.addElement(object);
      this.numTris += object.getNumTris();
    }
  }

  void addViewpoint(Viewpoint viewpoint)
  {
    this.viewpoints.addElement(viewpoint);
  }

  void addNavigationInfo(NavigationInfo navInfo)
  {
    this.navInfos.addElement(navInfo);
  }

  void addBackground(Background background)
  {
    this.backgrounds.addElement(background);
  }

  void addFog(Fog fog)
  {
    this.fogs.addElement(fog);
  }

  void addLight(Light light)
  {
    this.lights.addElement(light);
  }

  void addSharedGroup(SharedGroup sg)
  {
    this.sharedGroups.addElement(sg);
  }

  void addTimeSensor(TimeSensor ts)
  {
    this.timeSensors.addElement(ts);
  }

  void addVisibilitySensor(VisibilitySensor vs)
  {
    this.visibilitySensors.addElement(vs);
  }

  void addTouchSensor(TouchSensor ts)
  {
    this.touchSensors.addElement(ts);
  }

  void addAudioClip(AudioClip ac)
  {
    this.audioClips.addElement(ac);
  }

  void setWorldInfo(WorldInfo wi)
  {
    this.worldInfo = wi;
  }

  void setDescription(String desc)
  {
    this.description = desc;
  }

  public void define(String defName, BaseNode node)
  {
    this.defTable.put(defName, node);
  }

  public BaseNode use(String defName)
  {
    return (BaseNode)this.defTable.get(defName);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Scene
 * JD-Core Version:    0.6.0
 */