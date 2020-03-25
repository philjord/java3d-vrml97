package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Group;
import org.jogamp.java3d.MediaContainer;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PhysicalEnvironment;
import org.jogamp.java3d.PointSound;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;

public class Sound extends NonSharedNode
{
  SFVec3f direction;
  SFFloat intensity;
  SFVec3f location;
  SFFloat maxBack;
  SFFloat maxFront;
  SFFloat minBack;
  SFFloat minFront;
  SFFloat priority;
  SFNode source;
  SFBool spatialize;
  BranchGroup impl;
  PointSound soundImpl;
  boolean wait = false;
  Point3f pos3f = new Point3f();
  Point3d pos3d = new Point3d();
  BoundingSphere bounds = new BoundingSphere();
  float[] distance = new float[2];
  float[] attenuation = new float[2];
  MediaContainer media;
  AudioClip clip;

  public Sound(Loader loader)
  {
    super(loader);
    this.direction = new SFVec3f(0.0F, 0.0F, 1.0F);
    this.intensity = new SFFloat(1.0F);
    this.location = new SFVec3f(0.0F, 0.0F, 0.0F);
    this.maxBack = new SFFloat(10.0F);
    this.maxFront = new SFFloat(10.0F);
    this.minBack = new SFFloat(1.0F);
    this.minFront = new SFFloat(1.0F);
    this.priority = new SFFloat(0.0F);
    this.source = new SFNode();
    this.spatialize = new SFBool(true);

    initFields();
  }

  public Sound(Loader loader, SFVec3f direction, SFFloat intensity, SFVec3f location, SFFloat maxBack, SFFloat maxFront, SFFloat minBack, SFFloat minFront, SFFloat priority, SFNode source, SFBool spatialize)
  {
    super(loader);
    this.direction = direction;
    this.intensity = intensity;
    this.location = location;
    this.maxBack = maxBack;
    this.maxFront = maxFront;
    this.minBack = minBack;
    this.minFront = minFront;
    this.priority = priority;
    this.source = source;

    initFields();
  }

  public void initImpl()
  {
    this.pos3f.x = this.location.value[0];
    this.pos3f.y = this.location.value[1];
    this.pos3f.z = this.location.value[2];

    this.pos3d.x = this.pos3f.x;
    this.pos3d.y = this.pos3f.y;
    this.pos3d.z = this.pos3f.z;
    this.soundImpl = new PointSound();
    this.soundImpl.setPosition(this.pos3f);

    this.bounds.setCenter(this.pos3d);
    this.bounds.setRadius(this.maxFront.value);
    this.soundImpl.setReleaseEnable(false);
    this.soundImpl.setContinuousEnable(false);
    this.soundImpl.setSchedulingBounds(this.bounds);
    this.soundImpl.setInitialGain(this.intensity.value);

    this.soundImpl.setLoop(0);
    this.distance[0] = this.minFront.value;
    this.distance[1] = (this.maxFront.value * 100.0F);
    this.attenuation[0] = 1.0F;
    this.attenuation[1] = 0.0F;
    this.soundImpl.setDistanceGain(this.distance, this.attenuation);
    this.soundImpl.setCapability(23);
    this.soundImpl.setCapability(15);
    this.soundImpl.setCapability(13);
    this.soundImpl.setCapability(28);
    this.soundImpl.setCapability(30);
    this.soundImpl.setCapability(25);
    this.soundImpl.setCapability(21);
    this.soundImpl.setCapability(17);
    this.soundImpl.setCapability(19);
    this.soundImpl.setCapability(29);

    this.impl = new RGroup();
    this.impl.addChild(this.soundImpl);
    this.implNode = this.impl;
    this.implReady = true;
  }

  void setClip()
  {
    if ((this.source.node != null) && ((this.source.node instanceof AudioClip))) {
      if (Browser.debug) {
        System.out.println(this + " setClip() available");
      }
      this.clip = ((AudioClip)(AudioClip)this.source.node);
      if (this.media != this.clip.impl) {
        this.media = this.clip.impl;
        this.clip.setSound(this);
      }

      if (Browser.debug) {
        System.out.println(this.browser.environment.getAudioDevice());
      }
      this.soundImpl.setSoundData(this.media);
      waitSoundReadiness();
      long duration = this.soundImpl.getDuration();
      if (Browser.debug) {
        System.out.println(this + " duration = " + duration);
      }
      this.clip.setDuration(duration / 1000.0D);
    }
    else {
      if (Browser.debug) {
        System.out.println(this + "setClip() null media");
      }
      this.soundImpl.setSoundData(null);
    }
  }

  void setEnable(boolean enable)
  {
    this.soundImpl.setEnable(enable);
  }

  void setLoop(int l)
  {
    this.soundImpl.setLoop(l);
  }

  public void notifyMethod(String eventInName, double time)
  {
  }

  void doneParse()
  {
  }

  void initFields()
  {
    this.direction.init(this, this.FieldSpec, 3, "direction");
    this.intensity.init(this, this.FieldSpec, 3, "intensity");
    this.location.init(this, this.FieldSpec, 3, "location");
    this.maxBack.init(this, this.FieldSpec, 3, "maxBack");
    this.minBack.init(this, this.FieldSpec, 3, "minBack");
    this.minFront.init(this, this.FieldSpec, 3, "minFront");
    this.maxFront.init(this, this.FieldSpec, 3, "maxFront");
    this.priority.init(this, this.FieldSpec, 3, "priority");
    this.source.init(this, this.FieldSpec, 3, "source");
    this.spatialize.init(this, this.FieldSpec, 0, "spatialize");
  }

  public Object clone()
  {
    return new Sound(this.loader, (SFVec3f)this.direction.clone(), (SFFloat)this.intensity.clone(), (SFVec3f)this.location.clone(), (SFFloat)this.maxBack.clone(), (SFFloat)this.maxFront.clone(), (SFFloat)this.minBack.clone(), (SFFloat)this.minFront.clone(), (SFFloat)this.priority.clone(), (SFNode)this.source.clone(), (SFBool)this.spatialize.clone());
  }

  public String getType()
  {
    return "Sound";
  }

  void waitSoundReadiness()
  {
    ContentNegotiator cn = new ContentNegotiator(this);

    Sound returned = (Sound)cn.getContent();
  }

  void updateParent(Node p)
  {
    if (this.impl.getParent() != null) {
      ((Group)p).addChild(this.impl);
    }
    setClip();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Sound
 * JD-Core Version:    0.6.0
 */