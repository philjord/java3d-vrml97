package org.jdesktop.j3d.loaders.vrml97.impl;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.AudioDevice3D;
import org.jogamp.java3d.AuralAttributes;
import org.jogamp.java3d.BoundingLeaf;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.Locale;
import org.jogamp.java3d.MediaContainer;
import org.jogamp.java3d.PhysicalBody;
import org.jogamp.java3d.PhysicalEnvironment;
import org.jogamp.java3d.PickRay;
import org.jogamp.java3d.SceneGraphPath;
import org.jogamp.java3d.Soundscape;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.View;
import org.jogamp.java3d.ViewPlatform;
import org.jogamp.java3d.VirtualUniverse;
import org.jogamp.java3d.audioengines.javasound.JavaSoundMixer;
import org.jogamp.vecmath.Color3f;

import vrml.InvalidEventInException;
import vrml.InvalidEventOutException;
import vrml.InvalidVRMLSyntaxException;

public class Browser
{
  String name = "Java3D VRML'97 Browser";
  String version = "unknown";
  String description;
  float speed;
  float frameRate;
  Canvas3D canvas;
  VirtualUniverse universe;
  Locale locale;
  PhysicalBody body;
  PhysicalEnvironment environment;
  View view;
  AudioDevice3D audioDevice;
  BranchGroup browserRoot;
  org.jogamp.java3d.DirectionalLight browserDirLight;
  AmbientLight browserAmbLight;
  Evagation evagation;
  SimTicker simTicker;
  FrameCounter frameCount;
  BranchGroup browserSoundAtts;
  BranchGroup browserLightGroup;
  RGroup browserBackgroundSlot;
  RGroup browserFogSlot;
  Stack viewpointStack = null;
  Stack navigationInfoStack = null;
  Stack fogStack = null;
  Stack backgroundStack = null;
  Viewpoint defViewpoint;
  NavigationInfo defNavInfo;
  SphereSensor sceneExaminer;
  Background defBackground;
  Fog defFog;
  WorldInfo defWorldInfo;
  Loader loader;
  BranchGroup curScene;
  SceneTransform curSceneT;
  BoundingSphere sceneBounds;
  Vector viewpointList;
  Vector sharedGroups;
  Vector timeSensors;
  Vector visibilitySensors;
  Vector touchSensors;
  Vector audioClips;
  Viewpoint initViewpoint;
  NavigationInfo initNavInfo;
  Background initBackground;
  Fog initFog;
  WorldInfo initWorldInfo;
  NavigationInfo curNavInfo;
  Background curBackground;
  Fog curFog;
  Viewpoint curViewpoint;
  WorldInfo curWorldInfo;
  TransformGroup curViewGroup;
  TransformGroup curImplGroup;
  BranchGroup curImplGroupBranch;
  ViewPlatform curViewPlatform;
  TransformBuf pendingTransforms = new TransformBuf();

  boolean simTickEnable = false;
  boolean resetOnNextFrame = false;

  boolean stopped = false;
  boolean soundinited = false;
  double eventTime;
  PickRay pickRay = new PickRay();
  SceneGraphPath[] stuffPicked;
  Transform3D identity = new Transform3D();
  RoutePrinter routePrinter = new RoutePrinter();
  TreePrinter treePrinter = new TreePrinter();
  static boolean printRoutes = false;
  int routeDepth = 0;

  int numTris = 0;
  NumFormat numFormat = new NumFormat();
  int numFrames = 0;
  int numSimTicks = 0;
  double renderTime = 0.0D;
  double routeTime = 0.0D;
  double frameStartTime = 0.0D;
  double netStartTime = 0.0D;
  double start;
  static long memUsage;
  static long memLowLimit = 800000L;
  static boolean debug;
  static boolean debug2;
  boolean timing;
  boolean pickEcho;
  double attachTime;
  boolean checkDelay;
  double relTimeBase = 0.0D;
  Vector debugVec = new Vector();
  Container container;
  BoundingLeaf defBoundingLeaf;
  static Browser instance;
  int vi = 0;

  public Browser(Canvas3D vc3d)
  {
    this.canvas = vc3d;
    browserInit();
  }

  public Browser()
  {
    this.canvas = new Canvas3D(null);
    browserInit();
  }

  void browserInit()
  {
    this.loader = new Loader(this);
    this.timing = this.loader.timing;
    printRoutes = this.loader.printRoutes;
    debug = this.loader.debug;
    initBrowserObjs();
    loadDefaults();
  }

  void initBrowserObjs()
  {
    instance = this;

    this.loader = new Loader(this);

    this.fogStack = new Stack();
    this.viewpointStack = new Stack();
    this.backgroundStack = new Stack();
    this.navigationInfoStack = new Stack();

    this.defViewpoint = new Viewpoint(this.loader);
    this.defNavInfo = new NavigationInfo(this.loader);
    this.defBackground = new Background(this.loader);
    this.defWorldInfo = new WorldInfo(this.loader);

    this.defViewpoint.initImpl();
    this.defNavInfo.initImpl();
    this.defBackground.initImpl();

    this.defWorldInfo.initImpl();

    this.universe = new VirtualUniverse();
    this.locale = new Locale(this.universe);

    this.body = new PhysicalBody();
    this.environment = new PhysicalEnvironment();
    this.view = new View();
    this.view.addCanvas3D(this.canvas);
    this.view.setPhysicalBody(this.body);
    this.view.setPhysicalEnvironment(this.environment);

    this.browserRoot = new RGroup();
    this.curSceneT = new SceneTransform(this.loader);
    this.curSceneT.initImpl();

    this.evagation = new Evagation(this);
    this.evagation.setSchedulingBoundingLeaf(this.loader.infiniteBoundingLeaf);
    this.browserRoot.addChild(this.evagation);

    this.simTicker = new SimTicker(this);
    this.simTicker.setSchedulingBoundingLeaf(this.loader.infiniteBoundingLeaf);
    this.browserRoot.addChild(this.simTicker);

    this.browserBackgroundSlot = new RGroup();
    this.browserBackgroundSlot.addChild(this.defBackground.getBackgroundImpl());
    this.browserRoot.addChild(this.browserBackgroundSlot);
    this.browserFogSlot = new RGroup();

    this.browserRoot.addChild(this.browserFogSlot);

    this.browserRoot.addChild(this.defViewpoint.getImplNode());
    this.locale.addBranchGraph(this.browserRoot);

    this.browserAmbLight = new AmbientLight(true, new Color3f(0.2F, 0.2F, 0.2F));

    this.browserAmbLight.setCapability(13);
    this.browserDirLight = new org.jogamp.java3d.DirectionalLight();
    this.browserDirLight.setColor(new Color3f(0.8F, 0.8F, 0.8F));
    this.browserDirLight.setCapability(13);
    this.browserDirLight.setInfluencingBounds(this.loader.infiniteBounds);

    this.browserLightGroup = new RGroup();
    this.browserLightGroup.addChild(this.browserDirLight);
    this.browserLightGroup.addChild(this.browserAmbLight);

    AuralAttributes aa = new AuralAttributes();
    aa.setFrequencyScaleFactor(0.1F);
    Soundscape sc = new Soundscape(this.loader.infiniteBoundingLeaf.getRegion(), aa);
    this.browserSoundAtts = new RGroup();
    this.browserSoundAtts.addChild(sc);
    this.browserRoot.addChild(this.browserSoundAtts);
    this.audioDevice = new JavaSoundMixer(this.environment);
    this.audioDevice.initialize();
    this.environment.setAudioDevice(this.audioDevice);
  }

  void loadDefaults()
  {
    this.curViewpoint = this.defViewpoint;
    this.curNavInfo = this.defNavInfo;
    this.curBackground = this.defBackground;

    this.initViewpoint = null;
    this.initNavInfo = null;
    this.initBackground = null;
    this.initFog = null;
    try
    {
      this.browserFogSlot.detach();
      while (this.browserFogSlot.numChildren() > 0)
        this.browserFogSlot.removeChild(0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    this.browserFogSlot = new RGroup();
    this.browserRoot.addChild(this.browserFogSlot);

    this.browserBackgroundSlot.removeChild(0);
    this.browserBackgroundSlot.addChild(this.defBackground.getBackgroundImpl());
    updateView();
  }

  public synchronized void loadURL(String[] urlString, String[] parameter)
    throws InvalidVRMLSyntaxException, IOException, MalformedURLException
  {
    URL worldURL = null;
    URL cb = null;

    this.simTickEnable = false;
    this.simTicker.setEnable(false);

    System.gc();

    urlString[0] = urlString[0].replace('\\', '/');
    try
    {
      worldURL = new URL(urlString[0]);
    }
    catch (MalformedURLException murle) {
      if (murle.getMessage().indexOf("no protocol") >= 0) {
        try {
          if ((this.container instanceof Applet)) {
            cb = ((Applet)(Applet)this.container).getCodeBase();
          }
          worldURL = new URL(cb, urlString[0]);
        }
        catch (MalformedURLException murle2) {
          murle2.printStackTrace();
        }
      }
    }

    this.loader.setWorldURL(null, worldURL);
    ContentNegotiator cn = new ContentNegotiator(worldURL);
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream((byte[])(byte[])cn.getContent());

      doParse(bais);
    }
    catch (NullPointerException npe)
    {
      IOException i = new IOException();
      i.initCause(npe);
      throw i;
    }
    loadFinalize();
  }

  public void loadStringAsVrml(String sourceVrml)
  {
    this.simTickEnable = false;
    this.simTicker.setEnable(false);

    ByteArrayInputStream bais = new ByteArrayInputStream(sourceVrml.getBytes());

    doParse(bais);
    loadFinalize();
  }

  void doParse(InputStream is)
  {
    clear();

    if (this.loader.debug) {
      System.out.println("locale before scene is added:");
      this.browserRoot.detach();
      this.treePrinter.print(this.browserRoot);
      this.locale.addBranchGraph(this.browserRoot);
      System.out.println();
    }

    this.curScene = new RGroup();
    this.curSceneT = new SceneTransform(this.loader);
    this.curSceneT.initImpl();

    this.sceneExaminer = new SphereSensor(this.loader);
    this.sceneExaminer.autoSpinFrameWait.setValue(1);

    MFNode curSceneNodes = (MFNode)this.curSceneT.getField("addChildren");
    curSceneNodes.addValue(this.sceneExaminer);

    this.curScene.addChild(this.curSceneT.impl);

    Scene scene = null;
    try {
      scene = this.loader.load(is);
    }
    catch (Exception e) {
      containerMessage(this.container, e.toString());
      clear();
    }

    for (Enumeration e = scene.objects.elements(); e.hasMoreElements(); ) {
      BaseNode node = (BaseNode)e.nextElement();
      if (node != null) {
        if (debug) {
          System.out.println("Adding to browser " + node.toStringId());
        }

        node.updateParent(this.curSceneT.impl);
        org.jogamp.java3d.Node implNode = node.getImplNode();
        if ((node instanceof GroupBase)) {
          this.debugVec.addElement(node);
        }

        if (((node instanceof Viewpoint)) && (implNode != null)) {
          this.curScene.addChild(implNode);
        }
        else if (implNode != null) {
          implNode.setCapability(3);
          implNode.setCapability(11);
          if (debug) {
            System.out.println("curScene: " + this.curScene);
          }
          if (debug) {
            System.out.println("Adding to scene " + implNode);
          }
          this.curSceneT.impl.addChild(implNode);
          if ((node instanceof DirectionalLight))
          {
            org.jogamp.java3d.DirectionalLight dirLight = ((DirectionalLight)node).dirLight;

            dirLight.addScope(this.curSceneT.impl);
          }
        }
      }

    }

    this.curSceneT.impl.setCapability(6);
    this.curSceneT.impl.setCapability(1);
    this.curSceneT.impl.setPickable(true);

    this.viewpointList = scene.viewpoints;
    if (scene.viewpoints.size() > 0) {
      this.initViewpoint = ((Viewpoint)scene.viewpoints.elementAt(0));
    }
    if (scene.navInfos.size() > 0) {
      this.initNavInfo = ((NavigationInfo)scene.navInfos.elementAt(0));
    }
    if (scene.backgrounds.size() > 0) {
      this.initBackground = ((Background)scene.backgrounds.elementAt(0));
    }
    if (scene.fogs.size() > 0) {
      this.initFog = ((Fog)scene.fogs.elementAt(0));
    }

    this.timeSensors = scene.timeSensors;
    this.visibilitySensors = scene.visibilitySensors;
    this.touchSensors = scene.touchSensors;
    this.sharedGroups = scene.sharedGroups;
    this.audioClips = scene.audioClips;
    this.numTris = scene.numTris;

    if (scene.worldInfo != null) {
      this.curWorldInfo = scene.worldInfo;
    }
    else {
      this.curWorldInfo = this.defWorldInfo;
    }
    this.name = this.curWorldInfo.title.getValue();

    this.description = this.curWorldInfo.info.get1Value(0);

    if (debug) {
      System.out.println("Parsed scene makes J3D scene graph:");
      this.treePrinter.print(this.curScene);
    }

    if (debug) {
      System.out.println("Locale already has " + this.locale.numBranchGraphs());

      this.browserRoot.detach();
      this.treePrinter.print(this.browserRoot);
      this.locale.addBranchGraph(this.browserRoot);
    }
    this.locale.addBranchGraph(this.curScene);

    cleanUp();
    if ((debug) || (this.timing)) {
      System.out.println("Scene contains " + this.numTris + " triangles");
    }
    if (debug)
      System.out.println("Load completed");
  }

  protected void clear()
  {
    if (debug) {
      System.out.println("Browser:clear()");
    }

    if (this.curScene != null) {
      this.curScene.detach();
    }

    this.curScene = null;

    loadDefaults();

    this.numTris = 0;
    this.loader.clear();
    disableSounds();

    this.viewpointList = null;
    this.timeSensors = null;
    this.visibilitySensors = null;
    this.touchSensors = null;
    this.audioClips = null;

    this.viewpointStack.removeAllElements();
    this.navigationInfoStack.removeAllElements();
    this.fogStack.removeAllElements();
    this.backgroundStack.removeAllElements();

    this.evagation.resetViewpoint();
    this.debugVec = new Vector();
    cleanUp();

    this.audioDevice = new JavaSoundMixer(this.environment);
    this.audioDevice.initialize();
    this.environment.setAudioDevice(this.audioDevice);
  }

  void loadFinalize()
  {
    for (int i = 0; i < this.timeSensors.size(); i++) {
      ((TimeSensor)(TimeSensor)this.timeSensors.elementAt(i)).doneParse();
    }

    if (printRoutes) {
      for (int i = 0; i < this.touchSensors.size(); i++)
      {
        this.routePrinter.printRoutes((TouchSensor)this.touchSensors.elementAt(i));
      }

    }

    if (this.initFog != null) {
      SFBool set_bind = (SFBool)this.initFog.getEventIn("bind");
      set_bind.setValue(true);
    }
    if (this.initBackground != null) {
      SFBool set_bind = (SFBool)this.initBackground.getEventIn("bind");
      set_bind.setValue(true);
    }
    if (this.initNavInfo != null) {
      SFBool set_bind = (SFBool)this.initNavInfo.getEventIn("bind");
      set_bind.setValue(true);
    }

    if (this.initViewpoint != null) {
      SFBool set_bind = (SFBool)this.initViewpoint.getEventIn("bind");
      set_bind.setValue(true);
    }

    this.simTickEnable = true;
    this.simTicker.setEnable(true);

    this.sceneBounds = ((BoundingSphere)(BoundingSphere)this.curSceneT.impl.getBounds());
    if (this.sceneBounds.getRadius() == 0.0D) {
      this.sceneBounds.setRadius(1.0D);
      this.curScene.setBounds(this.sceneBounds);
    }

    this.curSceneT.setSceneBounds(this.sceneBounds);

    this.sceneExaminer.spinKick.setValue((float)this.sceneBounds.getRadius() / 2.0F);

    if (this.viewpointList.size() == 0) {
      this.curViewpoint.frameObject(this.sceneBounds);
    }

    querySounds();

    frameCountAdd();

    initTiming();
    resetViewpoint();
  }

  synchronized void querySounds()
  {
    for (int i = 0; i < this.audioClips.size(); i++) {
      AudioClip cl = (AudioClip)(AudioClip)this.audioClips.elementAt(i);
      org.jogamp.java3d.Sound s = cl.sound.soundImpl;
      MediaContainer mc = cl.impl;

      s.setSoundData(mc);
      cl.setDuration(s.getDuration() / 1000.0D);
    }

    int numChannels = this.audioDevice.getTotalChannels();
    if (debug)
      System.out.println("audioDevice has " + numChannels + "channels");
  }

  void frameCountAdd()
  {
    if (this.browserRoot != null) {
      Iterator<org.jogamp.java3d.Node> e = this.browserRoot.getAllChildren();
      int ind = 0;
      try {
        while ((e.hasNext()) && 
          (e.next() != this.frameCount.rHandle)) {
          ind++;
        }

        if (this.browserRoot.getChild(ind) == this.frameCount.rHandle) {
          this.browserRoot.removeChild(ind);
        }
      }
      catch (NullPointerException npe)
      {
      }

      this.frameCount = new FrameCounter(this, 4, "soundSync");

      this.frameCount.setSchedulingBoundingLeaf(this.loader.infiniteBoundingLeaf);
      this.browserRoot.addChild(this.frameCount.rHandle);
    }
  }

  void frameCountCallback(FrameCounter b)
  {
    if (b.name.equals("soundSync"))
      querySounds();
  }

  protected void addViewpoint(Viewpoint viewpoint)
  {
    if (this.initViewpoint == null)
    {
      this.initViewpoint = viewpoint;
    }
    this.viewpointList.addElement(viewpoint);
  }

  void updateView()
  {
    if (debug) {
      System.out.println("updateView");
    }

    this.evagation.resetViewpoint();

    this.curImplGroupBranch = this.curViewpoint.impl;
    this.curImplGroup = this.curViewpoint.implOrient;
    this.curViewGroup = this.curViewpoint.implBrowser;
    this.curViewPlatform = this.curViewpoint.implViewPlatform;

    this.curViewGroup.setTransform(this.identity);

    this.curViewPlatform.setActivationRadius(2112.0F);

    this.view.setFieldOfView(this.curViewpoint.fieldOfView.value);
    double frontClip;
    if (this.curNavInfo.avatarSize.mfloat.length > 0) {
      frontClip = this.curNavInfo.avatarSize.mfloat[0] / 2.0D;
    }
    else
    {
      frontClip = 0.125D;
    }

    this.view.setFrontClipDistance(frontClip);
    double backClip;
    if (this.curNavInfo.visibilityLimit.value > 0.0D) {
      backClip = this.curNavInfo.visibilityLimit.value;
    }
    else {
      backClip = frontClip * 2999.0D;
    }

    this.view.setBackClipDistance(backClip);

    this.browserDirLight.setEnable(this.curNavInfo.headlight.value);
    this.browserAmbLight.setEnable(this.curNavInfo.headlight.value);

    this.browserLightGroup.detach();
    this.curViewGroup.addChild(this.browserLightGroup);

    this.view.attachViewPlatform(this.curViewPlatform);
    this.view.setPhysicalBody(this.body);
    this.view.setPhysicalEnvironment(this.environment);

    if (this.timing) {
      this.start = Time.getNow();
    }

    this.evagation.setViewGroup(this.curViewGroup);
    try
    {
      float s = 1.6F / this.curNavInfo.avatarSize.mfloat[1];
      this.curSceneT.scale.setValue(s, s, s);
    }
    catch (NullPointerException npe)
    {
    }
    catch (ArrayIndexOutOfBoundsException aioobe)
    {
    }

    cleanUp();
  }

  void updateBackground()
  {
    this.browserBackgroundSlot.removeChild(0);
    this.browserBackgroundSlot.addChild(this.curBackground.getBackgroundImpl());
  }

  void updateFog()
  {
    try
    {
      this.browserFogSlot.detach();
      while (this.browserFogSlot.numChildren() > 0)
        this.browserFogSlot.removeChild(0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    this.browserFogSlot = new RGroup();
    this.browserRoot.addChild(this.browserFogSlot);
    this.browserFogSlot.addChild(this.curFog.getFogImpl());
  }

  void viewChanged(Viewpoint changedView)
  {
    if (changedView == this.curViewpoint)
      this.view.setFieldOfView(this.curViewpoint.fieldOfView.value);
  }

  public void setViewpoint(int vi)
  {
    if (this.viewpointList.size() > 0)
    {
      Viewpoint viewpoint = (Viewpoint)this.viewpointList.elementAt(vi);
      SFBool set_bind = (SFBool)viewpoint.getEventIn("bind");
      set_bind.setValue(true);
    }
  }

  public void resetViewpoint()
  {
    updateView();
  }

  public String[] getViewpointDescriptions()
  {
    String[] vd = new String[this.viewpointList.size()];
    for (int i = 0; i < this.viewpointList.size(); i++) {
      Viewpoint cur = (Viewpoint)this.viewpointList.elementAt(i);
      if ((cur.description.string == null) || (cur.description.string.equals("")))
      {
        vd[i] = ("Viewpoint " + i);
      }
      else {
        vd[i] = cur.description.string;
      }
    }
    return vd;
  }

  void bindableChanged(Stack changedStack)
  {
    if (debug) {
      System.out.println(this + "bindableChanged()" + changedStack);
    }
    if (changedStack == this.viewpointStack) {
      Viewpoint newViewpoint = (Viewpoint)this.viewpointStack.peek();
      if ((newViewpoint != null) && (newViewpoint != this.curViewpoint)) {
        if (debug) {
          System.out.println(newViewpoint + " bound");
        }
        this.curViewpoint = newViewpoint;
        updateView();
      }
    }
    else if (changedStack == this.navigationInfoStack) {
      NavigationInfo newNavInfo = (NavigationInfo)this.navigationInfoStack.peek();

      if ((newNavInfo != this.curNavInfo) && (newNavInfo != null)) {
        this.curNavInfo = newNavInfo;
        updateView();
      }

    }
    else if (changedStack == this.backgroundStack) {
      Background newBackground = (Background)this.backgroundStack.peek();
      if ((newBackground != this.curBackground) && (newBackground != null)) {
        this.curBackground = newBackground;
        updateBackground();
      }
    }
    else if (changedStack == this.fogStack) {
      Fog newFog = (Fog)this.fogStack.peek();
      if ((newFog != this.curFog) && (newFog != null)) {
        this.curFog = newFog;
        updateFog();
      }
    }
  }

  void preRender()
  {
    if (this.timing) {
      this.frameStartTime = Time.getNow();
      if (this.checkDelay) {
        double delay = this.frameStartTime - this.attachTime;
        System.out.println("Attach to render delay = " + this.numFormat.format(delay, 2) + " seconds");
      }
    }
    try
    {
      simTick();
    }
    catch (Exception e0) {
      e0.printStackTrace();
    }
  }

  void postRender()
  {
    if (this.timing) {
      double now = Time.getNow();
      double elapsed = now - this.frameStartTime;
      this.renderTime += elapsed;
      if (elapsed < 0.0D) {
        System.out.println("Negative elaspsed time for frame: " + this.numFormat.format(elapsed, 2) + " seconds");

        this.renderTime = -1.0D;
      }
      if (this.checkDelay) {
        System.out.println("Time to render first frame = " + this.numFormat.format(elapsed, 2) + " seconds");

        this.checkDelay = false;
      }
    }
    this.numFrames += 1;
    if ((this.timing) && (this.numFrames % 10 == 0)) {
      outputTiming();
    }

    if (this.resetOnNextFrame) {
      resetTiming();
      this.resetOnNextFrame = false;
    }
  }

  void initTiming()
  {
    Time.setSystemInitTime();
    resetTiming();
  }

  void resetTiming()
  {
    double now = Time.getNow();
    this.netStartTime = now;
    this.renderTime = 0.0D;
    this.routeTime = 0.0D;
    this.numFrames = 0;
    this.numSimTicks = 0;
  }

  double relativeTime(double timeVal)
  {
    if (this.relTimeBase == 0.0D) {
      this.relTimeBase = timeVal;
    }
    return timeVal - this.relTimeBase;
  }

  double beginRoute()
  {
    if (this.routeDepth++ == 0) {
      this.eventTime = Time.getNow();
    }

    return this.eventTime;
  }

  void beginRoute(double now)
  {
    if (this.routeDepth++ == 0)
      this.eventTime = now;
  }

  double eventTime()
  {
    return this.eventTime;
  }

  void endRoute()
  {
    this.routeDepth -= 1;
  }

  void simTick()
  {
    double now = Time.getNow();

    this.numSimTicks += 1;

    this.pendingTransforms.startBatchLoading();

    beginRoute(now);

    if (this.timeSensors != null) {
      Enumeration e = this.timeSensors.elements();
      while (e.hasMoreElements()) {
        TimeSensor ts = (TimeSensor)e.nextElement();
        if (ts.enabled.value == true) {
          ts.simTick(Time.getNow());
        }
      }
    }

    if (this.audioClips != null) {
      Enumeration e = this.audioClips.elements();
      while (e.hasMoreElements()) {
        AudioClip clip = (AudioClip)e.nextElement();
        if (clip.sound != null) {
          clip.simTick(now);
        }
      }
    }

    this.evagation.simTick(now);

    this.pendingTransforms.stopBatchLoading();

    for (int i = 0; i < this.pendingTransforms.size; i++) {
      this.pendingTransforms.array[i].updateTransform();

      this.pendingTransforms.array[i] = null;
    }

    this.pendingTransforms.size = 0;

    endRoute();

    this.routeTime += Time.getNow() - now;
  }

  public void outputTiming()
  {
    if ((this.numFrames > 0) && (this.timing)) {
      double now = Time.getNow();
      double elapsed = now - this.netStartTime;
      double netTrisPerSec = this.numTris * this.numFrames / (1000.0D * elapsed);

      double trisPerSec = this.numTris * this.numFrames / (1000.0D * this.renderTime);

      System.out.print(this.numFormat.format(elapsed, 1) + " seconds " + this.numFrames + " frames " + "overall: " + this.numFormat.format(netTrisPerSec, 1) + "K tris/sec");

      if (this.renderTime > 0.0D) {
        System.out.println(" render: " + this.numFormat.format(trisPerSec, 1) + "K tris/sec");
      }
      else
      {
        System.out.println(" render: ???");
      }
      if (this.routeTime > 0.0D) {
        System.out.println(this.numFormat.format(this.routeTime, 2) + " seconds updating nodes (" + this.numFormat.format(this.routeTime * 100.0D / elapsed, 1) + "%) " + this.numSimTicks + " ticks " + this.numFormat.format(this.routeTime * 1000.0D / this.numSimTicks, 1) + "ms/tick");
      }

    }

    this.resetOnNextFrame = true;
  }

  void disableSounds()
  {
    if (this.audioClips != null) {
      Enumeration e = this.audioClips.elements();
      while (e.hasMoreElements()) {
        AudioClip clip = (AudioClip)e.nextElement();
        if (clip.sound != null)
          clip.sound.setEnable(false);
      }
    }
  }

  void enableSounds()
  {
    if (this.audioClips != null) {
      Enumeration e = this.audioClips.elements();
      while (e.hasMoreElements()) {
        AudioClip clip = (AudioClip)e.nextElement();
        if (clip.sound != null)
          clip.sound.setEnable(true);
      }
    }
  }

  public void shutDown()
  {
    outputTiming();
    disableSounds();
    System.exit(0);
  }

  public Canvas3D getCanvas3D()
  {
    return this.canvas;
  }

  public void createVrmlFromURL(String[] url, BaseNode node, String event)
    throws InvalidVRMLSyntaxException
  {
  }

  public String getName()
  {
    return this.name;
  }

  public String getVersion()
  {
    return this.version;
  }

  public float getCurrentSpeed()
  {
    return this.speed;
  }

  public float getCurrentFrameRate()
  {
    return this.frameRate;
  }

  public String getWorldURL()
  {
    if (this.loader.worldURL != null) {
      return this.loader.worldURL.toString();
    }

    return new String("NULL worldURL");
  }

  public void replaceWorld(BaseNode[] nodes)
  {
  }

  public BaseNode[] createVrmlFromString(String vrmlSyntax)
    throws InvalidVRMLSyntaxException
  {
    return null;
  }

  protected void setDefTable(Hashtable t)
  {
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void addRoute(BaseNode fromNode, String fromEventOut, BaseNode toNode, String toEventIn)
  {
    fromEventOut = Field.baseName(fromEventOut);
    toEventIn = Field.baseName(toEventIn);
    this.loader.connect(fromNode, fromEventOut, toNode, toEventIn, true);
  }

  public void deleteRoute(BaseNode fromNode, String fromEventOut, BaseNode toNode, String toEventIn)
  {
    fromEventOut = Field.baseName(fromEventOut);
    toEventIn = Field.baseName(toEventIn);
    Field fromField;
    if ((fromNode instanceof Script)) {
      fromField = ((Script)fromNode).getField(fromEventOut);
    }
    else {
      fromField = ((Node)fromNode).getField(fromEventOut);
    }
    if (!fromField.isEventOut())
      throw new InvalidEventOutException();
    Field toField;
    if ((toNode instanceof Script)) {
      toField = ((Script)toNode).getField(toEventIn);
    }
    else {
      toField = ((Node)toNode).getField(toEventIn);
    }
    if (!toField.isEventIn()) {
      throw new InvalidEventInException();
    }

    fromField.deleteConnection(toField);
  }

  public void processEvent(AWTEvent evt)
  {
    this.evagation.processEvent(evt);
  }

  public void startRender()
  {
    if (this.stopped) {
      this.canvas.startRenderer();
      enableSounds();
    }
    this.stopped = false;
  }

  public void stopRender()
  {
    if (!this.stopped) {
      this.canvas.stopRenderer();
      disableSounds();
    }
    this.stopped = true;
  }

  public void cleanUp()
  {
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
      memLowLimit -= 1000000L;
      if (memLowLimit < 500000L)
        memLowLimit = 500000L;
    }
  }

  public URL getURL()
  {
    return this.loader.worldURL;
  }

  byte[] getBytes(String URLstring)
  {
    byte[] buf = new byte[1];
    try {
      URL fu = new URL(URLstring);
      ContentNegotiator cn = new ContentNegotiator(fu);
      buf = (byte[])(byte[])cn.getContent();
    }
    catch (Exception e) {
      System.out.println(e);
    }

    return buf;
  }

  byte[] getRelBytes(String relURL)
  {
    String fullURL = this.loader.worldURLBaseName + relURL;
    byte[] buf = getBytes(fullURL);
    return buf;
  }

  int count(Enumeration e)
  {
    int c = 0;
    while (e.hasMoreElements()) {
      c++;
      e.nextElement();
    }
    return c;
  }

  public void removeCanvas3D(Canvas3D can)
  {
    this.view.removeCanvas3D(can);
  }

  public void addCanvas3D(Canvas3D can)
  {
    this.view.addCanvas3D(can);
  }

  public void setAWTContainer(Container container)
  {
    this.container = container;
  }

  void containerMessage(Container c, String mesg)
  {
    if ((c instanceof Applet)) {
      ((Applet)(Applet)c).showStatus(mesg);
    }
    else if ((c instanceof Frame))
      ((Frame)(Frame)c).setTitle(mesg);
  }

  static Browser getBrowser()
  {
    return instance;
  }

  public void setAutoSmooth(boolean s)
  {
    this.loader.autoSmooth = s;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Browser
 * JD-Core Version:    0.6.0
 */