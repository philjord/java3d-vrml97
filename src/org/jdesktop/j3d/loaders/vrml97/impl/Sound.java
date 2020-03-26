/*
 * $RCSfile: Sound.java,v $
 *
 *      @(#)Sound.java 1.28 98/11/05 20:35:21
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
 * $Date: 2005/02/03 23:07:02 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;

/**  Description of the Class */
public class Sound extends NonSharedNode {
    // exposedFields

    SFVec3f direction;
    SFFloat intensity;
    SFVec3f location;
    SFFloat maxBack;
    SFFloat maxFront;
    SFFloat minBack;
    SFFloat minFront;
    SFFloat priority;
    SFNode source;

    // field
    SFBool spatialize;

    BranchGroup impl;
    org.jogamp.java3d.PointSound soundImpl;
    //org.jogamp.java3d.BackgroundSound soundImpl;
    boolean wait = false;
    Point3f pos3f = new Point3f();
    Point3d pos3d = new Point3d();
    BoundingSphere bounds = new BoundingSphere();
    float[] distance = new float[2];
    float[] attenuation = new float[2];
    org.jogamp.java3d.MediaContainer media;
    AudioClip clip;

    /**
     *Constructor for the Sound object
     *
     *@param  loader Description of the Parameter
     */
    public Sound(Loader loader) {
        super(loader);
        direction = new SFVec3f(0.0f, 0.0f, 1.0f);
        intensity = new SFFloat(1.0f);
        location = new SFVec3f(0.0f, 0.0f, 0.0f);
        maxBack = new SFFloat(10.0f);
        maxFront = new SFFloat(10.0f);
        minBack = new SFFloat(1.0f);
        minFront = new SFFloat(1.0f);
        priority = new SFFloat(0.0f);
        source = new SFNode();
        spatialize = new SFBool(true);

        initFields();
    }

    /**
     *Constructor for the Sound object
     *
     *@param  loader Description of the Parameter
     *@param  direction Description of the Parameter
     *@param  intensity Description of the Parameter
     *@param  location Description of the Parameter
     *@param  maxBack Description of the Parameter
     *@param  maxFront Description of the Parameter
     *@param  minBack Description of the Parameter
     *@param  minFront Description of the Parameter
     *@param  priority Description of the Parameter
     *@param  source Description of the Parameter
     *@param  spatialize Description of the Parameter
     */
    public Sound(Loader loader, SFVec3f direction, SFFloat intensity,
            SFVec3f location, SFFloat maxBack, SFFloat maxFront, SFFloat minBack,
            SFFloat minFront, SFFloat priority, SFNode source, SFBool spatialize) {

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

    /**  Description of the Method */
    public void initImpl() {
        pos3f.x = location.value[0];
        pos3f.y = location.value[1];
        pos3f.z = location.value[2];
        // todo: double check the double check
        pos3d.x = (double) pos3f.x;
        pos3d.y = (double) pos3f.y;
        pos3d.z = (double) pos3f.z;
        soundImpl = new org.jogamp.java3d.PointSound();
        soundImpl.setPosition(pos3f);
        //soundImpl = new org.jogamp.java3d.BackgroundSound();
        bounds.setCenter(pos3d);
        bounds.setRadius((double) maxFront.value);
        soundImpl.setReleaseEnable(false);
        soundImpl.setContinuousEnable(false);
        soundImpl.setSchedulingBounds(bounds);
        soundImpl.setInitialGain(intensity.value);
        //soundImpl.setLoop(org.jogamp.java3d.Sound.INFINITE_LOOPS);
        soundImpl.setLoop(0);
        distance[0] = minFront.value;
        distance[1] = maxFront.value * 100.0f;
        attenuation[0] = 1.0f;
        attenuation[1] = 0.0f;
        soundImpl.setDistanceGain(distance, attenuation);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_ENABLE_WRITE);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_INITIAL_GAIN_WRITE);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_SOUND_DATA_WRITE);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_DURATION_READ);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_IS_PLAYING_READ);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_SCHEDULING_BOUNDS_WRITE);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_CONT_PLAY_WRITE);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_LOOP_WRITE);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_RELEASE_WRITE);
        soundImpl.setCapability(org.jogamp.java3d.Sound.ALLOW_IS_READY_READ);

        //setClip();

        impl = new RGroup();
        impl.addChild(soundImpl);
        implNode = impl;
        implReady = true;
    }

    // pull this out to make available at updateParent() time for the clip.
    /**  Sets the clip attribute of the Sound object */
    void setClip() {
        if ((source.node != null) && (source.node instanceof AudioClip)) {
            if (browser.debug) {
                System.out.println(this + " setClip() available");
            }
            clip = (AudioClip) (source.node);
            if (media != clip.impl) {
                media = clip.impl;
                clip.setSound(this);
            }

            if (browser.debug) {
                System.out.println(browser.environment.getAudioDevice());
            }
            soundImpl.setSoundData(media);
            waitSoundReadiness();
            long duration = soundImpl.getDuration();
            if (browser.debug) {
                System.out.println(this + " duration = " + duration);
            }
            clip.setDuration(duration / 1000.0);
        }
        else {
            if (browser.debug) {
                System.out.println(this + "setClip() null media");
            }
            soundImpl.setSoundData(null);
        }
    }

    /**
     *  Sets the enable attribute of the Sound object
     *
     *@param  enable The new enable value
     */
    void setEnable(boolean enable) {
        soundImpl.setEnable(enable);
    }

    /**
     *  Sets the loop attribute of the Sound object
     *
     *@param  l The new loop value
     */
    void setLoop(int l) {
        soundImpl.setLoop(l);
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {

    }

    /**  Description of the Method */
    void doneParse() {// done with _ALL_ parsing
        ;
    }


    /**  Description of the Method */
    void initFields() {
        direction.init(this, FieldSpec, Field.EXPOSED_FIELD, "direction");
        intensity.init(this, FieldSpec, Field.EXPOSED_FIELD, "intensity");
        location.init(this, FieldSpec, Field.EXPOSED_FIELD, "location");
        maxBack.init(this, FieldSpec, Field.EXPOSED_FIELD, "maxBack");
        minBack.init(this, FieldSpec, Field.EXPOSED_FIELD, "minBack");
        minFront.init(this, FieldSpec, Field.EXPOSED_FIELD, "minFront");
        maxFront.init(this, FieldSpec, Field.EXPOSED_FIELD, "maxFront");
        priority.init(this, FieldSpec, Field.EXPOSED_FIELD, "priority");
        source.init(this, FieldSpec, Field.EXPOSED_FIELD, "source");
        spatialize.init(this, FieldSpec, Field.FIELD, "spatialize");
    }


    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Sound(loader,
                (SFVec3f) direction.clone(), (SFFloat) intensity.clone(),
                (SFVec3f) location.clone(), (SFFloat) maxBack.clone(),
                (SFFloat) maxFront.clone(), (SFFloat) minBack.clone(),
                (SFFloat) minFront.clone(), (SFFloat) priority.clone(),
                (SFNode) source.clone(), (SFBool) spatialize.clone());
    }

    /**
     *  Gets the type attribute of the Sound object
     *
     *@return  The type value
     */
    public String getType() {
        return "Sound";
    }


    /**  Description of the Method */
    void waitSoundReadiness() {
        ContentNegotiator cn = new ContentNegotiator(this);
        // getContent blocks until sound is ready
        // never happens. Sound is loaded by soundscheduler, sometime
        // when it is live which could not happen here. If someday,
        // Sound has a byte array in method, then keep this.
        Sound returned = (Sound) cn.getContent();
    }

    /**
     *  Description of the Method
     *
     *@param  p Description of the Parameter
     */
    void updateParent(org.jogamp.java3d.Node p) {
        if (impl.getParent() != null) {
            ((org.jogamp.java3d.Group) p).addChild(impl);
        }
        setClip();
    }

}

