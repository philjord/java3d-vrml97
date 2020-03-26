/*
 * $RCSfile: Scene.java,v $
 *
 *      @(#)Scene.java 1.14 98/11/05 20:35:46
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

import java.util.Hashtable;
import java.util.Vector;
import org.jogamp.java3d.SharedGroup;

/**
 * This is an internal class used by the Loader.
 * <p>
 * Collects elements loaded by the Loader into a lists
 */
public class Scene implements Namespace {
    /**  Description of the Field */
    public Vector objects = new Vector();
    /**  Description of the Field */
    public Vector viewpoints = new Vector();
    /**  Description of the Field */
    public Vector navInfos = new Vector();
    /**  Description of the Field */
    public WorldInfo worldInfo;
    /**  Description of the Field */
    public Vector backgrounds = new Vector();
    /**  Description of the Field */
    public Vector fogs = new Vector();
    /**  Description of the Field */
    public Vector lights = new Vector();
    /**  Description of the Field */
    public Vector sharedGroups = new Vector();
    /**  Description of the Field */
    public Vector timeSensors = new Vector();
    /**  Description of the Field */
    public Vector visibilitySensors = new Vector();
    /**  Description of the Field */
    public Vector touchSensors = new Vector();
    /**  Description of the Field */
    public Vector audioClips = new Vector();
    /**  Description of the Field */
    public Hashtable defTable = new Hashtable();
    /**  Description of the Field */
    public Proto firstProto = null;
    /**  Description of the Field */
    public Hashtable protos = new Hashtable();
    /**  Description of the Field */
    public String description = null;
    /**  Description of the Field */
    public int numTris;

    /**
     *  Adds a feature to the Object attribute of the Scene object
     *
     *@param  object The feature to be added to the Object attribute
     */
    void addObject(org.jdesktop.j3d.loaders.vrml97.impl.BaseNode object) {
        if (object != null) {
            objects.addElement(object);
            numTris += object.getNumTris();
        }
    }

    /**
     *  Adds a feature to the Viewpoint attribute of the Scene object
     *
     *@param  viewpoint The feature to be added to the Viewpoint attribute
     */
    void addViewpoint(Viewpoint viewpoint) {
        viewpoints.addElement(viewpoint);
    }

    /**
     *  Adds a feature to the NavigationInfo attribute of the Scene object
     *
     *@param  navInfo The feature to be added to the NavigationInfo attribute
     */
    void addNavigationInfo(NavigationInfo navInfo) {
        navInfos.addElement(navInfo);
    }

    /**
     *  Adds a feature to the Background attribute of the Scene object
     *
     *@param  background The feature to be added to the Background attribute
     */
    void addBackground(Background background) {
        backgrounds.addElement(background);
    }

    /**
     *  Adds a feature to the Fog attribute of the Scene object
     *
     *@param  fog The feature to be added to the Fog attribute
     */
    void addFog(Fog fog) {
        fogs.addElement(fog);
    }

    /**
     *  Adds a feature to the Light attribute of the Scene object
     *
     *@param  light The feature to be added to the Light attribute
     */
    void addLight(Light light) {
        lights.addElement(light);
    }

    /**
     *  Adds a feature to the SharedGroup attribute of the Scene object
     *
     *@param  sg The feature to be added to the SharedGroup attribute
     */
    void addSharedGroup(SharedGroup sg) {
        sharedGroups.addElement(sg);
    }

    /**
     *  Adds a feature to the TimeSensor attribute of the Scene object
     *
     *@param  ts The feature to be added to the TimeSensor attribute
     */
    void addTimeSensor(TimeSensor ts) {
        timeSensors.addElement(ts);
    }

    /**
     *  Adds a feature to the VisibilitySensor attribute of the Scene object
     *
     *@param  vs The feature to be added to the VisibilitySensor attribute
     */
    void addVisibilitySensor(VisibilitySensor vs) {
        visibilitySensors.addElement(vs);
    }

    /**
     *  Adds a feature to the TouchSensor attribute of the Scene object
     *
     *@param  ts The feature to be added to the TouchSensor attribute
     */
    void addTouchSensor(TouchSensor ts) {
        touchSensors.addElement(ts);
    }

    /**
     *  Adds a feature to the AudioClip attribute of the Scene object
     *
     *@param  ac The feature to be added to the AudioClip attribute
     */
    void addAudioClip(AudioClip ac) {
        audioClips.addElement(ac);
    }

    // for now, worldInfo is a single field, ie not a
    // bindable node or stack/vector.
    /**
     *  Sets the worldInfo attribute of the Scene object
     *
     *@param  wi The new worldInfo value
     */
    void setWorldInfo(WorldInfo wi) {
        // cant set the description, since at this point,
        // the parser hasn't read the fields yet. we only
        // know were done parsing within the browser.doParse();
        worldInfo = wi;
    }

    /**
     *  Sets the description attribute of the Scene object
     *
     *@param  desc The new description value
     */
    void setDescription(String desc) {
        description = desc;
    }
    // these are the def/use for the scene's namespace
    /**
     *  Description of the Method
     *
     *@param  defName Description of the Parameter
     *@param  node Description of the Parameter
     */
    public void define(String defName, BaseNode node) {
        defTable.put(defName, node);
    }

    /**
     *  Description of the Method
     *
     *@param  defName Description of the Parameter
     *@return  Description of the Return Value
     */
    public BaseNode use(String defName) {
        return (BaseNode) defTable.get(defName);
    }
}

