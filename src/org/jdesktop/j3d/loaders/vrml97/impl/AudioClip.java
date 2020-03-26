/*
 * $RCSfile: AudioClip.java,v $
 *
 *      @(#)AudioClip.java 1.33 98/11/19 15:44:48
 *
 * Copyright (c) 1996-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 *
 * $Revision: 1.2 $
 * $Date: 2005/02/03 23:06:51 $
 * $State: Exp $
 */
/*
 *
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.net.URL;
import org.jogamp.java3d.MediaContainer;

/**  Description of the Class */
public class AudioClip extends Node {

    // exposedField
    SFString description;
    SFBool loop;
    SFFloat pitch;
    SFTime startTime;
    SFTime stopTime;
    MFString url;

    // eventOut
    SFTime duration;
    SFBool isActive;

    // if the clip's active, startTime, stopTime, cycleInterval
    // events are ignored. these will store the previous values since
    // our event mechanism automatically updates the value.
    // but, there are really no cycleInterval assume that means
    // duration.
    double activeCycleInterval;
    double activeStartTime;
    double activeStopTime;

    MediaContainer impl;
    Sound sound = null;

    /**
     *Constructor for the AudioClip object
     *
     *@param  loader Description of the Parameter
     */
    public AudioClip(Loader loader) {
        super(loader);
        description = new SFString();
        loop = new SFBool(false);
        pitch = new SFFloat(1.0f);
        startTime = new SFTime(0.0);
        stopTime = new SFTime(0.0);
        url = new MFString();
        duration = new SFTime();
        isActive = new SFBool(false);
        loader.addAudioClip(this);
        initFields();
    }

    /**
     *Constructor for the AudioClip object
     *
     *@param  loader Description of the Parameter
     *@param  description Description of the Parameter
     *@param  loop Description of the Parameter
     *@param  pitch Description of the Parameter
     *@param  startTime Description of the Parameter
     *@param  stopTime Description of the Parameter
     *@param  url Description of the Parameter
     */
    AudioClip(Loader loader, SFString description, SFBool loop,
            SFFloat pitch, SFTime startTime, SFTime stopTime, MFString url) {
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

    /**  Description of the Method */
    void initImpl() {
        impl = new MediaContainer();
        impl.setCapability(MediaContainer.ALLOW_URL_READ);
        impl.setCapability(MediaContainer.ALLOW_URL_WRITE);
        impl.setCapability(MediaContainer.ALLOW_CACHE_READ);
        impl.setCapability(MediaContainer.ALLOW_CACHE_WRITE);
        impl.setCacheEnable(true);
        doChangeUrl();
        implReady = true;
        if (browser.debug) {
            System.out.println("AudioClip:initImpl()");
        }
    }

    /**
     *  Sets the sound attribute of the AudioClip object
     *
     *@param  owner The new sound value
     */
    void setSound(Sound owner) {
        sound = owner;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new AudioClip(loader, (SFString) description.clone(),
                (SFBool) loop.clone(), (SFFloat) pitch.clone(),
                (SFTime) startTime.clone(), (SFTime) stopTime.clone(),
                (MFString) url.clone());
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("url")) {
            doChangeUrl();
        }
        if (eventInName.equals("startTime")) {
            if (isActive.value == true) {
                startTime.time = activeStartTime;
            }
            else {
                activeCycleInterval = duration.time;
                activeStartTime = startTime.time;
                activeStopTime = stopTime.time;
            }
        }
        else if (eventInName.equals("stopTime")) {
            if (isActive.value == true) {
                // ignore by resetting value back
                // spec says "Any set_stopTime events where
                // stopTime <= startTime, to an active time-dependent
                // node are also ignored." the interperetation of
                // that is the stopTime < active startTime
                if (stopTime.time < activeStartTime) {
                    stopTime.time = activeStopTime;
                }
                // this should be done only in simTick?
                if ((activeStartTime < stopTime.time) &&
                        (stopTime.time <= time)) {
                    setSoundEnable(false);
                }
            }
        }
        else if (eventInName.equals("duration")) {
            // this could be a problem if JavaSoundMixer is late to report
            // the real duration which we need to calculate lots of things.
            if (isActive.value == true) {
                duration.time = activeCycleInterval;
            }
            // because of the latency in getting duration,
            // need to set this.
            activeCycleInterval = duration.time;
        }
        else if (eventInName.equals("isActive")) {
            if (!isActive.value) {
                activeStartTime = time;
                activeStopTime = stopTime.time;
                activeCycleInterval = duration.time;
            }
        }
        if (!eventInName.startsWith("route_")) {
            simTick(time);
        }
    }

    // this is only used by the browser to sync duration
    // that comes from the mixer late.
    /**
     *  Sets the duration attribute of the AudioClip object
     *
     *@param  inDuration The new duration value
     */
    public void setDuration(double inDuration) {
        duration.setValue(inDuration);
        duration.time = inDuration;
    }

    /**
     *  Description of the Method
     *
     *@param  now Description of the Parameter
     */
    void simTick(double now) {
        //if(browser.debug)System.out.println("now"+now+" start"+startTime.time+" stop"+stopTime.time+" isActive"+isActive.value+" duration"+duration.time+" loop"+ loop.value);

        if (loop.value) {
            sound.setLoop(org.jogamp.java3d.Sound.INFINITE_LOOPS);
        }
        else {
            sound.setLoop(0);
        }

        if ((now >= stopTime.time) && (stopTime.time > startTime.time) &&
                (!loop.value)) {
            if (isActive.value) {
                setSoundEnable(false);
            }
        }
        else if ((now >= (startTime.time + duration.time)) &&
                (!loop.value)) {
            if (isActive.value) {
                setSoundEnable(false);
            }
        }
        else if ((loop.value) && (stopTime.time > startTime.time) &&
                (now >= stopTime.time)) {
            if (isActive.value) {
                setSoundEnable(false);
            }
        }
        else if ((loop.value) && (stopTime.time <= startTime.time) &&
                (now >= startTime.time)) {
            if (!isActive.value) {
                setSoundEnable(true);
            }
        }
        else if ((now > startTime.time) && (!isActive.value)) {
            setSoundEnable(true);
        }

    }

    /**
     *  Sets the soundEnable attribute of the AudioClip object
     *
     *@param  b The new soundEnable value
     */
    void setSoundEnable(boolean b) {
        isActive.setValue(b);
        // it is possible that the vrml file specified a clip
        // without the sound associated.
        if (sound != null) {
            sound.setEnable(b);
        }
    }

    /**  Description of the Method */
    void doChangeUrl() {
        if (url.strings == null) {
            System.out.println("url is null!");
        }
        if (url.strings.length > 0) {
            if (browser.debug) {
                System.out.println(loader.worldURLBaseName + url.strings[0]);
            }
            try {
                URL u = new URL(loader.worldURLBaseName + url.strings[0]);
                //impl.setURL(loader.worldURLBaseName+url.strings[0]);
                impl.setURL(u);
            }
            catch (java.net.MalformedURLException murle) {
                murle.printStackTrace();
            }
        }
    }

    /**
     *  Gets the type attribute of the AudioClip object
     *
     *@return  The type value
     */
    public String getType() {
        return "AudioClip";
    }

    /**  Description of the Method */
    void initFields() {
        description.init(this, FieldSpec, Field.EXPOSED_FIELD, "description");
        loop.init(this, FieldSpec, Field.EXPOSED_FIELD, "loop");
        pitch.init(this, FieldSpec, Field.EXPOSED_FIELD, "pitch");
        startTime.init(this, FieldSpec, Field.EXPOSED_FIELD, "startTime");
        stopTime.init(this, FieldSpec, Field.EXPOSED_FIELD, "stopTime");
        url.init(this, FieldSpec, Field.EXPOSED_FIELD, "url");
        duration.init(this, FieldSpec, Field.EVENT_OUT, "duration");
        isActive.init(this, FieldSpec, Field.EVENT_OUT, "isActive");
    }

}

