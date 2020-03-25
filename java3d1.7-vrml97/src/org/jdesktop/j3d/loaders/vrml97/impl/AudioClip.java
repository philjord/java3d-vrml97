package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.jogamp.java3d.MediaContainer;

public class AudioClip extends Node
{
  SFString description;
  SFBool loop;
  SFFloat pitch;
  SFTime startTime;
  SFTime stopTime;
  MFString url;
  SFTime duration;
  SFBool isActive;
  double activeCycleInterval;
  double activeStartTime;
  double activeStopTime;
  MediaContainer impl;
  Sound sound = null;

  public AudioClip(Loader loader)
  {
    super(loader);
    this.description = new SFString();
    this.loop = new SFBool(false);
    this.pitch = new SFFloat(1.0F);
    this.startTime = new SFTime(0.0D);
    this.stopTime = new SFTime(0.0D);
    this.url = new MFString();
    this.duration = new SFTime();
    this.isActive = new SFBool(false);
    loader.addAudioClip(this);
    initFields();
  }

  AudioClip(Loader loader, SFString description, SFBool loop, SFFloat pitch, SFTime startTime, SFTime stopTime, MFString url)
  {
    super(loader);
    this.description = description;
    this.loop = loop;
    this.pitch = pitch;
    this.startTime = startTime;
    this.stopTime = stopTime;
    this.url = url;
    this.duration = new SFTime();
    this.isActive = new SFBool(false);
    loader.addAudioClip(this);
    initFields();
  }

  void initImpl()
  {
    this.impl = new MediaContainer();
    this.impl.setCapability(2);
    this.impl.setCapability(3);
    this.impl.setCapability(0);
    this.impl.setCapability(1);
    this.impl.setCacheEnable(true);
    doChangeUrl();
    this.implReady = true;
    if (Browser.debug)
      System.out.println("AudioClip:initImpl()");
  }

  void setSound(Sound owner)
  {
    this.sound = owner;
  }

  public Object clone()
  {
    return new AudioClip(this.loader, (SFString)this.description.clone(), (SFBool)this.loop.clone(), (SFFloat)this.pitch.clone(), (SFTime)this.startTime.clone(), (SFTime)this.stopTime.clone(), (MFString)this.url.clone());
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("url")) {
      doChangeUrl();
    }
    if (eventInName.equals("startTime")) {
      if (this.isActive.value == true) {
        this.startTime.time = this.activeStartTime;
      }
      else {
        this.activeCycleInterval = this.duration.time;
        this.activeStartTime = this.startTime.time;
        this.activeStopTime = this.stopTime.time;
      }
    }
    else if (eventInName.equals("stopTime")) {
      if (this.isActive.value == true)
      {
        if (this.stopTime.time < this.activeStartTime) {
          this.stopTime.time = this.activeStopTime;
        }

        if ((this.activeStartTime < this.stopTime.time) && (this.stopTime.time <= time))
        {
          setSoundEnable(false);
        }
      }
    }
    else if (eventInName.equals("duration"))
    {
      if (this.isActive.value == true) {
        this.duration.time = this.activeCycleInterval;
      }

      this.activeCycleInterval = this.duration.time;
    }
    else if ((eventInName.equals("isActive")) && 
      (!this.isActive.value)) {
      this.activeStartTime = time;
      this.activeStopTime = this.stopTime.time;
      this.activeCycleInterval = this.duration.time;
    }

    if (!eventInName.startsWith("route_"))
      simTick(time);
  }

  public void setDuration(double inDuration)
  {
    this.duration.setValue(inDuration);
    this.duration.time = inDuration;
  }

  void simTick(double now)
  {
    if (this.loop.value) {
      this.sound.setLoop(-1);
    }
    else {
      this.sound.setLoop(0);
    }

    if ((now >= this.stopTime.time) && (this.stopTime.time > this.startTime.time) && (!this.loop.value))
    {
      if (this.isActive.value) {
        setSoundEnable(false);
      }
    }
    else if ((now >= this.startTime.time + this.duration.time) && (!this.loop.value))
    {
      if (this.isActive.value) {
        setSoundEnable(false);
      }
    }
    else if ((this.loop.value) && (this.stopTime.time > this.startTime.time) && (now >= this.stopTime.time))
    {
      if (this.isActive.value) {
        setSoundEnable(false);
      }
    }
    else if ((this.loop.value) && (this.stopTime.time <= this.startTime.time) && (now >= this.startTime.time))
    {
      if (!this.isActive.value) {
        setSoundEnable(true);
      }
    }
    else if ((now > this.startTime.time) && (!this.isActive.value))
      setSoundEnable(true);
  }

  void setSoundEnable(boolean b)
  {
    this.isActive.setValue(b);

    if (this.sound != null)
      this.sound.setEnable(b);
  }

  void doChangeUrl()
  {
    if (this.url.strings == null) {
      System.out.println("url is null!");
    }
    if (this.url.strings.length > 0) {
      if (Browser.debug)
        System.out.println(this.loader.worldURLBaseName + this.url.strings[0]);
      try
      {
        URL u = new URL(this.loader.worldURLBaseName + this.url.strings[0]);

        this.impl.setURL(u);
      }
      catch (MalformedURLException murle) {
        murle.printStackTrace();
      }
    }
  }

  public String getType()
  {
    return "AudioClip";
  }

  void initFields()
  {
    this.description.init(this, this.FieldSpec, 3, "description");
    this.loop.init(this, this.FieldSpec, 3, "loop");
    this.pitch.init(this, this.FieldSpec, 3, "pitch");
    this.startTime.init(this, this.FieldSpec, 3, "startTime");
    this.stopTime.init(this, this.FieldSpec, 3, "stopTime");
    this.url.init(this, this.FieldSpec, 3, "url");
    this.duration.init(this, this.FieldSpec, 2, "duration");
    this.isActive.init(this, this.FieldSpec, 2, "isActive");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.AudioClip
 * JD-Core Version:    0.6.0
 */