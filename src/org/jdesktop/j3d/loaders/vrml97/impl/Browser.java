/*
 * $RCSfile: Browser.java,v $
 *
 *      @(#)Browser.java 1.168 99/03/24 15:31:16
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
 * $Revision: 1.3 $
 * $Date: 2006/03/30 08:19:29 $
 * $State: Exp $
 */
/*
 *@Author:  Rick Goldberg
 *@Author:  Doug Gehringer
 *@author   Nikolai V. Chr.
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.audioengines.javasound.*;
import java.applet.*;

import java.awt.AWTEvent;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.lang.System;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

/**
 * Browser J3D structure:
 * <p>
 * The browser has two kinds of J3D structures: permanent structures which
 * are changed the current scene and reset for each URL, and a scene
 * branchgraph which holds the J3D tree parsed from the current VRML url.
 *
 * <p>
 * The permanent BranchGroup is called browserRoot.  It holds:
 * <UL>
 * <LI> the browser's behaviors (which control the VRML runtime) </LI>
 * <LI> a FogSlot and a BackgroundSlot which groups which hold the currently
 *      active VRML Fog or Background </LI>
 * <LI> The default ViewPlatform, associated with the default Viewpoint </LI>
 * </UL>
 * <p>
 * Views:
 * <p>
 * There is one View, PhysicalBody and PhysicalEnvironment which are used
 * by the currently active Viewpoint and NavigationInfo.
 * <p>
 * Each file Viewpoint defines a new ViewPlaform, which get associated with
 * the view when the viewpoint is bound.
 * <p>
 * There are default Viewpoint, NavigationInfo, Fog and Background VRML nodes
 * which are used if the file does not define them
 * <p>
 * The initial VP, NI, Fog and BG are the value first seen from the file.  This
 * value will be bound in loadFinalize().
 * <p>
 * The VP, NI, Fog and BG stacks are used after the file is loaded.  When each
 * node is bound, if it is already on the stack, it removed itself from the
 * stack and then it pushes itself onto the stack.  The top entry on the stack
 * is the currently active value.
 * <p>
 * The browser also has an ambient light and a directional light (the
 * headlight).  These are added to the TransformGroup which holds the
 * ViewPlatform associated with the current Viewpoint.
 */

public class Browser {

    // VRML data
    String name = "Java3D VRML'97 Browser";
    String version = "unknown";
    String description;
    float speed;
    float frameRate;

    // J3D stuff owned by the browser
    Canvas3D canvas;
    VirtualUniverse universe;
    Locale locale;
    PhysicalBody body;
    PhysicalEnvironment environment;
    View view;
    AudioDevice3D audioDevice;

    // the browser's permanent state
    BranchGroup browserRoot;
    org.jogamp.java3d.DirectionalLight browserDirLight;
    org.jogamp.java3d.AmbientLight browserAmbLight;
    Evagation evagation;// browser behaviors
    SimTicker simTicker;
    FrameCounter frameCount;

    BranchGroup browserSoundAtts;
    BranchGroup browserLightGroup;
    RGroup browserBackgroundSlot;
    RGroup browserFogSlot;

    // Bindable Node stacks
    Stack viewpointStack = null;
    Stack navigationInfoStack = null;
    Stack fogStack = null;
    Stack backgroundStack = null;

    // default VRML objects, used if file does not define these
    Viewpoint defViewpoint;
    NavigationInfo defNavInfo;
    SphereSensor sceneExaminer;
    Background defBackground;
    Fog defFog;
    WorldInfo defWorldInfo;

    // The loader which parses input files
    Loader loader;

    // Stuff loaded from the current URL
    BranchGroup curScene;// top level J3D nodes from the file
    SceneTransform curSceneT;
    BoundingSphere sceneBounds;
    Vector viewpointList;
    // list of SharedGroups to compile
    Vector sharedGroups;
    // lists of active sensors that the browser must pass events to
    Vector timeSensors;
    Vector visibilitySensors;
    Vector touchSensors;
    Vector audioClips;
    // the initial values specified by the current file.  Null if not specified
    // by file
    Viewpoint initViewpoint;
    NavigationInfo initNavInfo;
    Background initBackground;
    Fog initFog;
    WorldInfo initWorldInfo;

    // the currently active values VRML
    NavigationInfo curNavInfo;
    Background curBackground;
    Fog curFog;
    Viewpoint curViewpoint;
    WorldInfo curWorldInfo;

    // these are all pieces of the current viewpoint
    TransformGroup curViewGroup;
    TransformGroup curImplGroup;// see Viewpoint
    BranchGroup curImplGroupBranch;
    ViewPlatform curViewPlatform;

    // used for batching tranform changes
    TransformBuf pendingTransforms = new TransformBuf();

    // state info for startup
    boolean simTickEnable = false;
    boolean resetOnNextFrame = false;

    // render state
    boolean stopped = false;
    boolean soundinited = false;

    // the time of the current event
    double eventTime;

    // utility stuff
    PickRay pickRay = new PickRay();
    SceneGraphPath[] stuffPicked;
    Transform3D identity = new Transform3D();
    RoutePrinter routePrinter = new RoutePrinter();
    TreePrinter treePrinter = new TreePrinter();
    static boolean printRoutes = false;
    int routeDepth = 0;

    // peformance stats
    int numTris = 0;
    NumFormat numFormat = new NumFormat();
    int numFrames = 0;
    int numSimTicks = 0;
    double renderTime = 0;
    double routeTime = 0;
    double frameStartTime = 0;
    double netStartTime = 0;
    double start;
    static long memUsage;
    static long memLowLimit = 800000;
    // debug mode
    static boolean debug;
    static boolean debug2;//fine grained
    boolean timing;
    boolean pickEcho;
    double attachTime;
    boolean checkDelay;
    double relTimeBase = 0.0;
    Vector debugVec = new Vector();

    // Browser awt info
    java.awt.Container container;

    // unclassified stuff
    BoundingLeaf defBoundingLeaf;
    static Browser instance;
    int vi = 0;


    /**
     *Constructor for the Browser object
     *
     *@param  vc3d Description of the Parameter
     */
    public Browser(Canvas3D vc3d) {
        canvas = vc3d;
        browserInit();
    }

    /**Constructor for the Browser object */
    public Browser() {
        canvas = new Canvas3D(null);
        browserInit();
    }

    /**  Description of the Method */
    void browserInit() {
        loader = new Loader(this);
        timing = loader.timing;
        printRoutes = loader.printRoutes;
        debug = loader.debug;
        initBrowserObjs();
        loadDefaults();
    }

    /**  Description of the Method */
    void initBrowserObjs() {
        instance = this;
        // set up the VRML objects
        loader = new Loader(this);

        fogStack = new Stack();
        viewpointStack = new Stack();
        backgroundStack = new Stack();
        navigationInfoStack = new Stack();

        defViewpoint = new Viewpoint(loader);
        defNavInfo = new NavigationInfo(loader);
        defBackground = new Background(loader);
        defWorldInfo = new WorldInfo(loader);
        // no need for default fog - remove after testing
        //defFog = new Fog(loader);

        // init the def objects
        defViewpoint.initImpl();
        defNavInfo.initImpl();
        defBackground.initImpl();
        //defFog.initImpl();
        defWorldInfo.initImpl();

        // initialize the J3D objects
        universe = new VirtualUniverse();
        locale = new Locale(universe);

        body = new PhysicalBody();
        environment = new PhysicalEnvironment();
        view = new View();
        view.addCanvas3D(canvas);
        view.setPhysicalBody(body);
        view.setPhysicalEnvironment(environment);

        // This branchgroup holds the browser's J3D state.  The browser
        // behaviors, fog and background are attached here
        browserRoot = new RGroup();
        curSceneT = new SceneTransform(loader);
        curSceneT.initImpl();

        evagation = new Evagation(this);
        evagation.setSchedulingBoundingLeaf(loader.infiniteBoundingLeaf);
        browserRoot.addChild(evagation);

        simTicker = new SimTicker(this);
        simTicker.setSchedulingBoundingLeaf(loader.infiniteBoundingLeaf);
        browserRoot.addChild(simTicker);

        browserBackgroundSlot = new RGroup();
        browserBackgroundSlot.addChild(defBackground.getBackgroundImpl());
        browserRoot.addChild(browserBackgroundSlot);
        browserFogSlot = new RGroup();
        //browserFogSlot.addChild(defFog.getFogImpl());
        browserRoot.addChild(browserFogSlot);

        // the default Viewpoint is located here
        browserRoot.addChild(defViewpoint.getImplNode());
        locale.addBranchGraph(browserRoot);

        // The browserLightGroup is added to the Viewpoint's group after the
        // view platform (so that it moves with the VP)
        browserAmbLight = new org.jogamp.java3d.AmbientLight(true,
                new Color3f(0.2f, 0.2f, 0.2f));
        browserAmbLight.setCapability(org.jogamp.java3d.Light.ALLOW_STATE_WRITE);
        browserDirLight = new org.jogamp.java3d.DirectionalLight();
        browserDirLight.setColor(new Color3f(0.8f, 0.8f, 0.8f));
        browserDirLight.setCapability(org.jogamp.java3d.Light.ALLOW_STATE_WRITE);
        browserDirLight.setInfluencingBounds(loader.infiniteBounds);

        browserLightGroup = new RGroup();
        browserLightGroup.addChild(browserDirLight);
        browserLightGroup.addChild(browserAmbLight);

        AuralAttributes aa = new AuralAttributes();
        aa.setFrequencyScaleFactor(.1f);
        Soundscape sc = new Soundscape(loader.infiniteBoundingLeaf.getRegion(), aa);
        browserSoundAtts = new RGroup();
        browserSoundAtts.addChild(sc);
        browserRoot.addChild(browserSoundAtts);
        audioDevice = new JavaSoundMixer(environment);
        audioDevice.initialize();
        environment.setAudioDevice(audioDevice);

    }

    /**  Description of the Method */
    void loadDefaults() {
        curViewpoint = defViewpoint;
        curNavInfo = defNavInfo;
        curBackground = defBackground;
        //curFog =  defFog;

        initViewpoint = null;
        initNavInfo = null;
        initBackground = null;
        initFog = null;

        try {
            browserFogSlot.detach();
            while (browserFogSlot.numChildren() > 0) {
                browserFogSlot.removeChild(0);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        browserFogSlot = new RGroup();
        browserRoot.addChild(browserFogSlot);
        //browserFogSlot.addChild(defFog.getFogImpl());
        browserBackgroundSlot.removeChild(0);
        browserBackgroundSlot.addChild(defBackground.getBackgroundImpl());
        updateView();

    }

    /**
     *  Description of the Method
     *
     *@param  urlString Description of the Parameter
     *@param  parameter Description of the Parameter
     *@exception  vrml.InvalidVRMLSyntaxException Description of the Exception
     *@exception  java.io.IOException Description of the Exception
     *@exception  java.net.MalformedURLException Description of the Exception
     */
    public synchronized void loadURL(String[] urlString, String[] parameter)
             throws vrml.InvalidVRMLSyntaxException,
            java.io.IOException, java.net.MalformedURLException {

        URL worldURL = null;
        URL cb = null;

        simTickEnable = false;
        simTicker.setEnable(false);

        System.gc();

        urlString[0] = urlString[0].replace((char) 0x5c, (char) 0x2f);

        try {
            worldURL = new URL(urlString[0]);
        }
        catch (java.net.MalformedURLException murle) {
            if (murle.getMessage().indexOf("no protocol") >= 0) {
                try {
                    if (this.container instanceof Applet) {
                        cb = ((Applet) (this.container)).getCodeBase();
                    }
                    worldURL = new URL(cb, urlString[0]);
                }
                catch (java.net.MalformedURLException murle2) {
                    murle2.printStackTrace();
                }
            }
        }

        loader.setWorldURL(null, worldURL);
        ContentNegotiator cn = new ContentNegotiator(worldURL);
        try {
            ByteArrayInputStream bais =
                    new ByteArrayInputStream((byte[]) cn.getContent());

            doParse(bais);
            // cn already threw fnf
        }
        catch (NullPointerException npe) {
            java.io.IOException i = new java.io.IOException();
			i.initCause(npe);
			throw i;
        }
        loadFinalize();
    }

    /**
     *  Description of the Method
     *
     *@param  sourceVrml Description of the Parameter
     */
    public void loadStringAsVrml(String sourceVrml) {
        simTickEnable = false;
        simTicker.setEnable(false);

        //loader.setWorldURL(null, null);

        ByteArrayInputStream bais = new
                ByteArrayInputStream(sourceVrml.getBytes());

        doParse(bais);
        loadFinalize();

    }

    /**
     *  Description of the Method
     *
     *@param  is Description of the Parameter
     */
    void doParse(InputStream is) {

        clear();

        if (loader.debug) {
            System.out.println("locale before scene is added:");
            browserRoot.detach();
            treePrinter.print(browserRoot);
            locale.addBranchGraph(browserRoot);
            System.out.println();
        }

        curScene = new RGroup();
        curSceneT = new SceneTransform(loader);
        curSceneT.initImpl();

        sceneExaminer = new SphereSensor(loader);
        sceneExaminer.autoSpinFrameWait.setValue(1);
        // do the safe thing and use the vrml event
        MFNode curSceneNodes = (MFNode) curSceneT.getField("addChildren");
        curSceneNodes.addValue(sceneExaminer);

        curScene.addChild(curSceneT.impl);

        Scene scene = null;
        try {
            scene = loader.load(is);
        }
        catch (Exception e) {
            containerMessage(this.container, e.toString());
            clear();
        }

        // extract the stuff we need from the scene

        // first the top level nodes
        for (Enumeration e = scene.objects.elements(); e.hasMoreElements(); ) {
            BaseNode node = (BaseNode) e.nextElement();
            if (node != null) {
                if (debug) {
                    System.out.println("Adding to browser " +
                            node.toStringId());
                }

                node.updateParent(curSceneT.impl);
                org.jogamp.java3d.Node implNode = node.getImplNode();
                if (node instanceof GroupBase) {
                    debugVec.addElement(node);
                }

                // bug, assume for now that Viewpoints are not nested within
                // any transforms, later we'll need to transform the viewpoint
                // by the inverse of what the parent would have been, since,
                // that factors out of the tree when we pull the Viewpoint up
                // to the top curScene.

                if (node instanceof Viewpoint && implNode != null) {
                    curScene.addChild(implNode);
                }
                else if (implNode != null) {
                    implNode.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
                    implNode.setCapability(org.jogamp.java3d.Node.ALLOW_LOCAL_TO_VWORLD_READ);
                    if (debug) {
                        System.out.println("curScene: " + curScene);
                    }
                    if (debug) {
                        System.out.println("Adding to scene " + implNode);
                    }
                    curSceneT.impl.addChild(implNode);
                    if (node instanceof
                            org.jdesktop.j3d.loaders.vrml97.impl.DirectionalLight) {
                        org.jogamp.java3d.DirectionalLight dirLight =
                                ((org.jdesktop.j3d.loaders.vrml97.impl.DirectionalLight)
                                node).dirLight;
                        dirLight.addScope(curSceneT.impl);
                    }
                }
            }
        }

        // over zealous cleaning
        curSceneT.impl.setCapability(org.jogamp.java3d.Node.ALLOW_PICKABLE_WRITE);
        curSceneT.impl.setCapability(org.jogamp.java3d.Node.ENABLE_PICK_REPORTING);
        curSceneT.impl.setPickable(true);
        // could call
        // TreeCleaner.cleanSubgraph(curSceneT);
        // but that would be a no-op since ENABLE_PICK_REPORTING is set.
        // (it *would* clear the collidable flags, but compile() would still
        // not have any effect)

        // then the bindable nodes
        viewpointList = scene.viewpoints;
        if (scene.viewpoints.size() > 0) {
            initViewpoint = (Viewpoint) scene.viewpoints.elementAt(0);
        }
        if (scene.navInfos.size() > 0) {
            initNavInfo = (NavigationInfo) scene.navInfos.elementAt(0);
        }
        if (scene.backgrounds.size() > 0) {
            initBackground = (Background) scene.backgrounds.elementAt(0);
        }
        if (scene.fogs.size() > 0) {
            initFog = (Fog) scene.fogs.elementAt(0);
        }
        // and the other stuff we need from the scene
        timeSensors = scene.timeSensors;
        visibilitySensors = scene.visibilitySensors;
        touchSensors = scene.touchSensors;
        sharedGroups = scene.sharedGroups;
        audioClips = scene.audioClips;
        numTris = scene.numTris;

        // this used to be handled in the parser, and could
        // be cleaned up some.
        if (scene.worldInfo != null) {
            curWorldInfo = scene.worldInfo;
        }
        else {
            curWorldInfo = defWorldInfo;
        }
        name = curWorldInfo.title.getValue();
        // how much info fits on the titlebar? TBD, implement the
        // info popup in the player, but for now this gives us a titlebar.
        description = curWorldInfo.info.get1Value(0);

        if (debug) {
            System.out.println("Parsed scene makes J3D scene graph:");
            treePrinter.print(curScene);
        }

        /* Don't bother to compile() since the pickable flag is still on
	 * for all the Shape3D's
	 *if(!debug) {
	 *     curScene.compile();
	 *     Enumeration e = sharedGroups.elements();
	 *     while (e.hasMoreElements()) {
	 *	   SharedGroup sg = (SharedGroup) e.nextElement();
         *		sg.compile();
 	 *     }
 	 *}
	 */
        //attach the scene to the objRoot
        if (debug) {
            System.out.println("Locale already has " + locale.numBranchGraphs());

            browserRoot.detach();
            treePrinter.print(browserRoot);
            locale.addBranchGraph(browserRoot);
        }
        locale.addBranchGraph(curScene);

        cleanUp();
        if (debug || timing) {
            System.out.println("Scene contains " + numTris + " triangles");
        }
        if (debug) {
            System.out.println("Load completed");
        }
    }

    /**  Description of the Method */
    protected void clear() {
        int n;

        if (debug) {
            System.out.println("Browser:clear()");
        }

        if (curScene != null) {
            curScene.detach();
        }

        curScene = null;

        // load up the defaults.  This will move the browserLightGroup to
        // the defViewpoint
        loadDefaults();

        numTris = 0;
        loader.clear();
        disableSounds();

        viewpointList = null;
        timeSensors = null;
        visibilitySensors = null;
        touchSensors = null;
        audioClips = null;

        viewpointStack.removeAllElements();
        navigationInfoStack.removeAllElements();
        fogStack.removeAllElements();
        backgroundStack.removeAllElements();

        evagation.resetViewpoint();
        debugVec = new Vector();
        cleanUp();

        audioDevice = new JavaSoundMixer(environment);
        audioDevice.initialize();
        environment.setAudioDevice(audioDevice);

    }

    /**  Description of the Method */
    void loadFinalize() {
        SFBool set_bind;

        // finalize the TimeSensors
        for (int i = 0; i < timeSensors.size(); i++) {
            ((TimeSensor) (timeSensors.elementAt(i))).doneParse();
        }

        if (printRoutes) {
            for (int i = 0; i < touchSensors.size(); i++) {
                // print the routes attached to touch sensors
                routePrinter.printRoutes(
                        (TouchSensor) touchSensors.elementAt(i));
            }
        }

        // bind the initial bindables, these will make cur = init
        if (initFog != null) {
            set_bind = (SFBool) initFog.getEventIn("bind");
            set_bind.setValue(true);
        }
        if (initBackground != null) {
            set_bind = (SFBool) initBackground.getEventIn("bind");
            set_bind.setValue(true);
        }
        if (initNavInfo != null) {
            set_bind = (SFBool) initNavInfo.getEventIn("bind");
            set_bind.setValue(true);
        }
        // do this last, since it depends on NavInfo
        if (initViewpoint != null) {
            set_bind = (SFBool) initViewpoint.getEventIn("bind");
            set_bind.setValue(true);
        }
        // binding the VP will reset the View

        // all set
        simTickEnable = true;
        simTicker.setEnable(true);

        sceneBounds = (BoundingSphere) (((TransformGroup) curSceneT.impl).getBounds());
        if (sceneBounds.getRadius() == 0.0) {
            sceneBounds.setRadius(1.0);
            curScene.setBounds(sceneBounds);
        }

        curSceneT.setSceneBounds(sceneBounds);

        // arbitrary formula here:
        // set the spinKick (ammount to amplify
        // mouse angle delta) depending on relative
        // size of object.

        sceneExaminer.spinKick.setValue(((float) (sceneBounds.getRadius())) / 2.0f);

        if (viewpointList.size() == 0) {
            curViewpoint.frameObject(sceneBounds);
        }

        // do the sounds after all else
        querySounds();

        // bug: see javasound duration bug
        frameCountAdd();

        initTiming();
        resetViewpoint();

    }

    /**  Description of the Method */
    synchronized void querySounds() {
        // sounds should be live, scene should be
        // ready; can now initialize the clips with the known
        // duration needed to calculate event responses.
        for (int i = 0; i < audioClips.size(); i++) {
            AudioClip cl = (AudioClip) (audioClips.elementAt(i));
            org.jogamp.java3d.Sound s = cl.sound.soundImpl;
            org.jogamp.java3d.MediaContainer mc = cl.impl;
            // need to prepare the sound now if we want to know
            // how long it is in duration time.
            s.setSoundData(mc);
            cl.setDuration(s.getDuration() / 1000.0);
        }

        int numChannels = audioDevice.getTotalChannels();
        if (debug) {
            System.out.println("audioDevice has " + numChannels + "channels");
        }
    }

    /**  Description of the Method */
    void frameCountAdd() {

        // JavaSoundMixer will not report the size of the sound
        // until after the scene is live and into a couple of frames.
        // this is a temporary workaround which should dissappear
        // remove the old frame count
        if (browserRoot != null) {
            Iterator<org.jogamp.java3d.Node> e = browserRoot.getAllChildren();
            int ind = 0;
            try {
                while (e.hasNext()) {
                    if (e.next() != frameCount.rHandle) {
                        ind++;
                    }
                    else {
                        break;
                    }
                }
                // double check it was found
                if (browserRoot.getChild(ind) == frameCount.rHandle) {
                    browserRoot.removeChild(ind);
                }
            }
            catch (NullPointerException npe) {
                ;// first time through, frameCount hasn't been init'd
            }

            frameCount = new FrameCounter(this, 4, "soundSync");
            //frameCount = new FrameCounter(this,1,"soundSync");
            frameCount.setSchedulingBoundingLeaf(loader.infiniteBoundingLeaf);
            browserRoot.addChild(frameCount.rHandle);
        }

    }

    /**
     *  Description of the Method
     *
     *@param  b Description of the Parameter
     */
    void frameCountCallback(FrameCounter b) {
        if (b.name.equals("soundSync")) {
            querySounds();
        }
    }


    // This does two things: keeps track of the initial viewpoint, which
    // is bound in loadFinalize() and makes a list of all the viewpoints.
    /**
     *  Adds a feature to the Viewpoint attribute of the Browser object
     *
     *@param  viewpoint The feature to be added to the Viewpoint attribute
     */
    protected void addViewpoint(Viewpoint viewpoint) {
        if (initViewpoint == null) {
            // this is the first one we have seen
            initViewpoint = viewpoint;
        }
        viewpointList.addElement(viewpoint);
    }


    /**  Description of the Method */
    void updateView() {
        if (debug) {
            System.out.println("updateView");
        }

        // clear the behavior so as to remove "jump"
        evagation.resetViewpoint();

        curImplGroupBranch = (BranchGroup) curViewpoint.impl;
        curImplGroup = (TransformGroup) curViewpoint.implOrient;
        curViewGroup = (TransformGroup) curViewpoint.implBrowser;
        curViewPlatform = (ViewPlatform) curViewpoint.implViewPlatform;

        // re-init the browserGroup transform
        // this is the one that "moves"
        curViewGroup.setTransform(identity);

        curViewPlatform.setActivationRadius(2112.0f);

        view.setFieldOfView(curViewpoint.fieldOfView.value);
        double frontClip;
        if (curNavInfo.avatarSize.mfloat.length > 0) {
            frontClip = curNavInfo.avatarSize.mfloat[0] / 2.0d;
        }
        else {
            // default value
            frontClip = 0.25 / 2.0d;
        }

        view.setFrontClipDistance(frontClip);
        double backClip;
        if (curNavInfo.visibilityLimit.value > 0.0) {
            backClip = curNavInfo.visibilityLimit.value;
        }
        else {
            backClip = frontClip * 2999.0;// no greater than 3000 or loss of
            // zbuff resolution
        }
        view.setBackClipDistance(backClip);

        browserDirLight.setEnable(curNavInfo.headlight.value);
        browserAmbLight.setEnable(curNavInfo.headlight.value);

        browserLightGroup.detach();
        curViewGroup.addChild(browserLightGroup);

        view.attachViewPlatform(curViewPlatform);
        view.setPhysicalBody(body);
        view.setPhysicalEnvironment(environment);

        if (timing) {
            start = Time.getNow();
        }

        evagation.setViewGroup(curViewGroup);

        // cant scale above the viewPlatform, so scale inversly above the scene
        // 1.6 meters is avatar's default height

        try {
            float s = 1.6f / curNavInfo.avatarSize.mfloat[1];
            curSceneT.scale.setValue(s, s, s);
        }
        catch (NullPointerException npe) {
            ;//expected 1st time

        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            ;
        }

        cleanUp();
    }

    /**  Description of the Method */
    void updateBackground() {
        browserBackgroundSlot.removeChild(0);
        browserBackgroundSlot.addChild(curBackground.getBackgroundImpl());
    }


    // the reason why this is special cased so, is that there is not
    // "default fog" which kind of inverts the logic
    /**  Description of the Method */
    void updateFog() {
        try {
            browserFogSlot.detach();
            while (browserFogSlot.numChildren() > 0) {
                browserFogSlot.removeChild(0);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        browserFogSlot = new RGroup();
        browserRoot.addChild(browserFogSlot);
        browserFogSlot.addChild(curFog.getFogImpl());
    }

    // rename fovChanged ?
    /**
     *  Description of the Method
     *
     *@param  changedView Description of the Parameter
     */
    void viewChanged(Viewpoint changedView) {
        if (changedView == curViewpoint) {
            view.setFieldOfView(curViewpoint.fieldOfView.value);
            // TODO: handle description
        }
    }

    /**
     *  Sets the viewpoint attribute of the Browser object
     *
     *@param  vi The new viewpoint value
     */
    public void setViewpoint(int vi) {
        if (viewpointList.size() > 0) {
            // this causes bindableChanged with this view stack
            Viewpoint viewpoint = (Viewpoint) viewpointList.elementAt(vi);
            SFBool set_bind = (SFBool) viewpoint.getEventIn("bind");
            set_bind.setValue(true);
        }
    }

    /**  Description of the Method */
    public void resetViewpoint() {
        updateView();// this will clean out any changes
    }

    /**
     *  Gets the viewpointDescriptions attribute of the Browser object
     *
     *@return  The viewpointDescriptions value
     */
    public String[] getViewpointDescriptions() {
        String[] vd = new String[viewpointList.size()];
        for (int i = 0; i < viewpointList.size(); i++) {
            Viewpoint cur = (Viewpoint) viewpointList.elementAt(i);
            if ((cur.description.string == null) ||
                    cur.description.string.equals("")) {
                vd[i] = "Viewpoint " + i;
            }
            else {
                vd[i] = cur.description.string;
            }
        }
        return vd;
    }

    /**
     *  Description of the Method
     *
     *@param  changedStack Description of the Parameter
     */
    void bindableChanged(Stack changedStack) {
        if (debug) {
            System.out.println(this + "bindableChanged()" + changedStack);
        }
        if (changedStack == viewpointStack) {
            Viewpoint newViewpoint = (Viewpoint) viewpointStack.peek();
            if (newViewpoint != null && newViewpoint != curViewpoint) {
                if (debug) {
                    System.out.println(newViewpoint + " bound");
                }
                curViewpoint = newViewpoint;
                updateView();
            }
        }
        else if (changedStack == navigationInfoStack) {
            NavigationInfo newNavInfo =
                    (NavigationInfo) navigationInfoStack.peek();
            if (newNavInfo != curNavInfo && newNavInfo != null) {
                curNavInfo = newNavInfo;
                updateView();// view uses NavInfo for some info
                // TODO: add updateNavInfo to change browser mode (walk,
                // examine) when implemented

            }
        }
        else if (changedStack == backgroundStack) {
            Background newBackground = (Background) backgroundStack.peek();
            if (newBackground != curBackground && newBackground != null) {
                curBackground = newBackground;
                updateBackground();
            }
        }
        else if (changedStack == fogStack) {
            Fog newFog = (Fog) fogStack.peek();
            if (newFog != curFog && newFog != null) {
                curFog = newFog;
                updateFog();
            }
        }
    }

    /**  Description of the Method */
    void preRender() {
        if (timing) {
            frameStartTime = Time.getNow();
            if (checkDelay) {
                double delay = frameStartTime - attachTime;
                System.out.println("Attach to render delay = " +
                        numFormat.format(delay, 2) + " seconds");
            }
        }
        try {
            simTick();
        }
        catch (Exception e0) {
            e0.printStackTrace();
        }
    }

    /**  Description of the Method */
    void postRender() {
        if (timing) {
            double now = Time.getNow();
            double elapsed = now - frameStartTime;
            renderTime += elapsed;
            if (elapsed < 0.0) {
                System.out.println("Negative elaspsed time for frame: " +
                        numFormat.format(elapsed, 2) + " seconds");
                renderTime = -1;
            }
            if (checkDelay) {
                System.out.println("Time to render first frame = " +
                        numFormat.format(elapsed, 2) + " seconds");
                checkDelay = false;
            }
        }
        numFrames++;
        if (timing && ((numFrames % 10) == 0)) {
            outputTiming();
        }
        //try {
        //simTick();
        //} catch (Exception e0 ) { e0.printStackTrace(); }
        // Reset here to that if previous output was between pre and post
        // render we don't include output time in timing
        if (resetOnNextFrame) {
            resetTiming();
            resetOnNextFrame = false;
        }
    }

    /**  Description of the Method */
    void initTiming() {
        Time.setSystemInitTime();
        resetTiming();
    }

    /**  Description of the Method */
    void resetTiming() {
        double now = Time.getNow();
        netStartTime = now;
        renderTime = 0.0;
        routeTime = 0.0;
        numFrames = 0;
        numSimTicks = 0;
    }

    /**
     *  Description of the Method
     *
     *@param  timeVal Description of the Parameter
     *@return  Description of the Return Value
     */
    double relativeTime(double timeVal) {
        if (relTimeBase == 0.0) {
            relTimeBase = timeVal;
        }
        return timeVal - relTimeBase;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    double beginRoute() {
        if (routeDepth++ == 0) {
            eventTime = Time.getNow();
            //if(debug2)System.out.println("beginRoute() base eventTime = " +
            //relativeTime(eventTime));
        }
        return eventTime;
    }

    /**
     *  Description of the Method
     *
     *@param  now Description of the Parameter
     */
    void beginRoute(double now) {
        if (routeDepth++ == 0) {
            eventTime = now;
            //if(debug2)System.out.println("beginRoute(now) base eventTime = " +
            //relativeTime(eventTime));
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    double eventTime() {
        //if(debug2)System.out.println("EventTime = " + relativeTime(eventTime));
        return eventTime;
    }

    /**  Description of the Method */
    void endRoute() {
        routeDepth--;
    }

    /**  Description of the Method */
    void simTick() {

        if (true) {
            double now = Time.getNow();

            numSimTicks++;

            // any other things that would be better to batch like this?
            // mpeg frames to Textures.
            pendingTransforms.startBatchLoading();

            beginRoute(now);// set the event time for all events

            if (timeSensors != null) {
                for (Enumeration e = timeSensors.elements();
                        e.hasMoreElements(); ) {
                    TimeSensor ts = (TimeSensor) e.nextElement();
                    if (ts.enabled.value == true) {
                        ts.simTick(Time.getNow());
                    }
                }
            }

            if (audioClips != null) {
                for (Enumeration e = audioClips.elements();
                        e.hasMoreElements(); ) {
                    AudioClip clip = (AudioClip) e.nextElement();
                    if (clip.sound != null) {
                        clip.simTick(now);
                    }
                }
            }

            evagation.simTick(now);

            // update the pending transforms
            // if any of the previous behaviors had resulted in setValue()
            // to any Transforms during this simTick()

            pendingTransforms.stopBatchLoading();

            for (int i = 0; i < pendingTransforms.size; i++) {
                pendingTransforms.array[i].updateTransform();
                //// just over write and keeping size as a TransforBufMark
                pendingTransforms.array[i] = null;// to allow GC
            }

            pendingTransforms.size = 0;

            endRoute();

            routeTime += (Time.getNow() - now);

            // hard to track during runtime stuff can be added to the generic
            // debug
            if (false) {
                for (Enumeration e = debugVec.elements(); e.hasMoreElements(); ) {
                    Object o = e.nextElement();
                    Transform3D t = new Transform3D();
                    try {
                        ((Node) o).implNode.getLocalToVworld(t);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(t);
                    System.out.println("+++++++++++++++++++++++++");
                }
            }
        }
    }

    /**  Description of the Method */
    public void outputTiming() {
        if ((numFrames > 0) && timing) {
            double now = Time.getNow();
            double elapsed = now - netStartTime;
            double netTrisPerSec =
                    numTris * numFrames / (1000 * elapsed);
            double trisPerSec =
                    numTris * numFrames / (1000 * renderTime);

            System.out.print(numFormat.format(elapsed, 1) +
                    " seconds " + numFrames + " frames " +
                    "overall: " +
                    numFormat.format(netTrisPerSec, 1) + "K tris/sec");
            if (renderTime > 0.0) {
                System.out.println(" render: " +
                        numFormat.format(trisPerSec, 1) + "K tris/sec");
            }
            else {
                System.out.println(" render: ???");
            }
            if (routeTime > 0.0) {
                System.out.println(numFormat.format(routeTime, 2) +
                        " seconds updating nodes (" +
                        numFormat.format(routeTime * 100.0 / elapsed, 1) +
                        "%) " + numSimTicks + " ticks " +
                        numFormat.format(routeTime * 1000.0 / numSimTicks, 1) +
                        "ms/tick");
            }
        }
        resetOnNextFrame = true;
    }

    /**  Description of the Method */
    void disableSounds() {
        if (audioClips != null) {
            for (Enumeration e = audioClips.elements();
                    e.hasMoreElements(); ) {
                AudioClip clip = (AudioClip) e.nextElement();
                if (clip.sound != null) {
                    clip.sound.setEnable(false);
                }
            }
        }
    }

    /**  Description of the Method */
    void enableSounds() {
        if (audioClips != null) {
            for (Enumeration e = audioClips.elements();
                    e.hasMoreElements(); ) {
                AudioClip clip = (AudioClip) e.nextElement();
                if (clip.sound != null) {
                    clip.sound.setEnable(true);
                }
            }
        }
    }

    /**  Description of the Method */
    public void shutDown() {
        outputTiming();
        disableSounds();
        System.exit(0);
    }

    /**
     *  Gets the canvas3D attribute of the Browser object
     *
     *@return  The canvas3D value
     */
    public Canvas3D getCanvas3D() {
        return canvas;
    }

    /**
     *  Description of the Method
     *
     *@param  url Description of the Parameter
     *@param  node Description of the Parameter
     *@param  event Description of the Parameter
     *@exception  vrml.InvalidVRMLSyntaxException Description of the Exception
     */
    public void createVrmlFromURL(String[] url, BaseNode node, String event)
             throws vrml.InvalidVRMLSyntaxException {
        ;
    }

    /**
     *  Gets the name attribute of the Browser object
     *
     *@return  The name value
     */
    public String getName() {
        return name;
    }

    /**
     *  Gets the version attribute of the Browser object
     *
     *@return  The version value
     */
    public String getVersion() {
        return version;
    }

    /**
     *  Gets the currentSpeed attribute of the Browser object
     *
     *@return  The currentSpeed value
     */
    public float getCurrentSpeed() {
        return speed;
    }

    /**
     *  Gets the currentFrameRate attribute of the Browser object
     *
     *@return  The currentFrameRate value
     */
    public float getCurrentFrameRate() {
        return frameRate;
    }

    /**
     *  Gets the worldURL attribute of the Browser object
     *
     *@return  The worldURL value
     */
    public String getWorldURL() {
        if (loader.worldURL != null) {
            return loader.worldURL.toString();
        }
        else {
            return new String("NULL worldURL");
        }
    }

    /**
     *  Description of the Method
     *
     *@param  nodes Description of the Parameter
     */
    public void replaceWorld(BaseNode[] nodes) {
        // TODO: implement
    }

    /**
     *  Description of the Method
     *
     *@param  vrmlSyntax Description of the Parameter
     *@return  Description of the Return Value
     *@exception  vrml.InvalidVRMLSyntaxException Description of the Exception
     */
    public BaseNode[] createVrmlFromString(String vrmlSyntax)
             throws vrml.InvalidVRMLSyntaxException {
        // todo: VRML2parser(String);
        //Node node[] = (new VRML2Parser(vrmlSyntax)).returnScene();
        return null;
    }

    /**
     *  Sets the defTable attribute of the Browser object
     *
     *@param  t The new defTable value
     */
    protected void setDefTable(Hashtable t) {
        // currently not used, but could be saved for use by EAI
        // TODO: memory optimization: set children=null on DEF'd group nodes
        // without routes that can change the children and on DEF'd Index
        // Face Sets which have child nodes that can't change, etc.
    }

    /**
     *  Sets the description attribute of the Browser object
     *
     *@param  description The new description value
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *  Gets the description attribute of the Browser object
     *
     *@return  The description value
     */
    public String getDescription() {
        return description;
    }

    /**
     *  Adds a feature to the Route attribute of the Browser object
     *
     *@param  fromNode The feature to be added to the Route attribute
     *@param  fromEventOut The feature to be added to the Route attribute
     *@param  toNode The feature to be added to the Route attribute
     *@param  toEventIn The feature to be added to the Route attribute
     */
    public void addRoute(BaseNode fromNode, String fromEventOut,
            BaseNode toNode, String toEventIn) {
        fromEventOut = Field.baseName(fromEventOut);
        toEventIn = Field.baseName(toEventIn);
        loader.connect(fromNode, fromEventOut, toNode, toEventIn, true);
    }

    /**
     *  Description of the Method
     *
     *@param  fromNode Description of the Parameter
     *@param  fromEventOut Description of the Parameter
     *@param  toNode Description of the Parameter
     *@param  toEventIn Description of the Parameter
     */
    public void deleteRoute(BaseNode fromNode, String fromEventOut,
            BaseNode toNode, String toEventIn) {

        fromEventOut = Field.baseName(fromEventOut);
        toEventIn = Field.baseName(toEventIn);

        Field fromField;
        Field toField;
        if (fromNode instanceof Script) {
            fromField = ((Script) fromNode).getField(fromEventOut);
        }
        else {
            fromField = ((Node) fromNode).getField(fromEventOut);
        }
        if (!fromField.isEventOut()) {
            throw new vrml.InvalidEventOutException();
        }

        if (toNode instanceof Script) {
            toField = ((Script) toNode).getField(toEventIn);
        }
        else {
            toField = ((Node) toNode).getField(toEventIn);
        }
        if (!toField.isEventIn()) {
            throw new vrml.InvalidEventInException();
        }

        fromField.deleteConnection(toField);
        // TODO: someday have a "unroute_" method?

    }

    // we may have defined several evagations that the
    // canvas didn't know about. ie, walk, fly
    /**
     *  Description of the Method
     *
     *@param  evt Description of the Parameter
     */
    public void processEvent(AWTEvent evt) {
        evagation.processEvent(evt);
    }

    /**  Description of the Method */
    public void startRender() {
        // only call this if a stop render has been called
        if (stopped) {
            canvas.startRenderer();
            enableSounds();
        }
        stopped = false;

    }

    /**  Description of the Method */
    public void stopRender() {
        if (!stopped) {
            canvas.stopRenderer();
            disableSounds();
        }
        stopped = true;
    }

    /**  Description of the Method */
    public void cleanUp() {
        long mem = Runtime.getRuntime().freeMemory();
        long tot = Runtime.getRuntime().totalMemory();
        if (mem < memLowLimit) {
            if (debug) {
                System.out.println("Memory usage: " + mem + " of " + tot + " left");
            }
            if (debug) {
                System.out.println("Taking out trash...");
            }
            System.gc();
            memUsage = Runtime.getRuntime().freeMemory() - mem;
            if (debug) {
                System.out.println("Reclaimed " + memUsage + " bytes.");
            }
            memLowLimit -= 1000000;
            if (memLowLimit < 500000) {
                memLowLimit = 500000;
            }
        }
    }

    /**
     *  Gets the uRL attribute of the Browser object
     *
     *@return  The uRL value
     */
    public URL getURL() {
        return loader.worldURL;
    }

    /**
     *  Gets the bytes attribute of the Browser object
     *
     *@param  URLstring Description of the Parameter
     *@return  The bytes value
     */
    byte[] getBytes(String URLstring) {

        ContentNegotiator cn;
        URL fu;
        byte[] buf = new byte[1];
        try {
            fu = new URL(URLstring);
            cn = new ContentNegotiator(fu);
            buf = (byte[]) cn.getContent();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        //if(debug)System.out.println(new String(buf));
        return buf;
    }


    /**
     *  Gets the relBytes attribute of the Browser object
     *
     *@param  relURL Description of the Parameter
     *@return  The relBytes value
     */
    byte[] getRelBytes(String relURL) {
        String fullURL = loader.worldURLBaseName + relURL;
        byte[] buf = getBytes(fullURL);
        return buf;
    }

    /**
     *  Description of the Method
     *
     *@param  e Description of the Parameter
     *@return  Description of the Return Value
     */
    int count(Enumeration e) {
        int c = 0;
        while (e.hasMoreElements()) {
            c++;
            e.nextElement();
        }
        return c;
    }


    /**
     *  Description of the Method
     *
     *@param  can Description of the Parameter
     */
    public void removeCanvas3D(Canvas3D can) {
        view.removeCanvas3D(can);
    }

    /**
     *  Adds a feature to the Canvas3D attribute of the Browser object
     *
     *@param  can The feature to be added to the Canvas3D attribute
     */
    public void addCanvas3D(Canvas3D can) {
        view.addCanvas3D(can);
    }

    /**
     *  Sets the aWTContainer attribute of the Browser object
     *
     *@param  container The new aWTContainer value
     */
    public void setAWTContainer(java.awt.Container container) {
        this.container = container;
    }

    /**
     *  Description of the Method
     *
     *@param  c Description of the Parameter
     *@param  mesg Description of the Parameter
     */
    void containerMessage(java.awt.Container c, String mesg) {
        if (c instanceof Applet) {
            ((Applet) (c)).showStatus(mesg);
        }
        else if (c instanceof Frame) {
            ((Frame) (c)).setTitle(mesg);
        }
    }

    /**
     *  Gets the browser attribute of the Browser class
     *
     *@return  The browser value
     */
    static Browser getBrowser() {
        return instance;
    }

    /**
     *  Sets the autoSmooth attribute of the Browser object
     *
     *@param  s The new autoSmooth value
     */
    public void setAutoSmooth(boolean s) {
        loader.autoSmooth = s;
    }

}

