package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import org.jogamp.java3d.BoundingLeaf;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Bounds;
import org.jogamp.java3d.SharedGroup;
import org.jogamp.vecmath.Point3d;
import vrml.InvalidEventInException;
import vrml.InvalidEventOutException;
import vrml.InvalidVRMLSyntaxException;

public class Loader
{
  GZIPInputStream gis;
  Browser browser = null;
  URL worldURL;
  URL worldURLBase;
  String worldURLBaseName;
  int loaderMode;
  Parser parser = null;
  Scene scene = null;

  Stack defTableStack = new Stack();

  Proto curProto = null;
  ProtoInstance protoInstance = null;

  IntBuf intBuf = new IntBuf();
  FloatBuf floatBuf = new FloatBuf();
  DoubleBuf doubleBuf = new DoubleBuf();
  Bounds infiniteBounds;
  BoundingLeaf infiniteBoundingLeaf;
  Bounds zeroBounds;
  boolean autoSmooth = false;
  URLClassLoader scl;
  boolean debug = false;
  boolean traceLex = false;
  boolean nowarn = false;
  boolean warnall = false;
  boolean parserMarks = false;
  boolean printRoutes = false;
  boolean timing = false;

  TreePrinter treePrinter = new TreePrinter();
  double nodeInitImpl;
  double ifsParse;
  double ifsInit;
  double MFInt32Parse;
  double MFInt32Copy;
  double MFVec3fParse;
  double MFVec3fCopy;
  int numGroups;
  int numIfs;
  NumFormat numFormat = new NumFormat();

  int warnLevel = 1;
  HashSet warnSet = new HashSet();
  public static final int LOAD_STATIC = 0;
  public static final int LOAD_OPTIMIZED = 1;
  public static final int LOAD_CONFORMANT = 2;
  static final int WARN_NEVER = 0;
  static final int WARN_ONCE = 1;
  static final int WARN_ALL = 2;

  public Loader(Browser initBrowser, int loadMode)
  {
    this.browser = initBrowser;
    this.loaderMode = loadMode;
    try {
      this.debug = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Object run() {
          Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.debug"));
          return b;
        }
      })).booleanValue();

      this.timing = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Object run() {
          Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.timing"));
          return b;
        }
      })).booleanValue();

      this.printRoutes = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Object run() {
          Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.printRoutes"));
          return b;
        }
      })).booleanValue();

      this.parserMarks = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Object run() {
          Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.parserMarks"));
          return b;
        }
      })).booleanValue();

      this.warnall = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Object run() {
          Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.warnall"));
          return b;
        }
      })).booleanValue();

      this.nowarn = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Object run() {
          Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.nowarn"));
          return b;
        }
      })).booleanValue();
    }
    catch (AccessControlException ace)
    {
    }

    if (this.nowarn) {
      this.warnLevel = 0;
    }
    if (this.warnall) {
      this.warnLevel = 2;
    }
    this.defTableStack = new Stack();
    this.infiniteBounds = new BoundingSphere(new Point3d(), 1.7976931348623157E+308D);
    this.infiniteBoundingLeaf = new BoundingLeaf(this.infiniteBounds);
    this.infiniteBoundingLeaf.setCapability(12);
    this.zeroBounds = new BoundingSphere(new Point3d(), -1.0D);
  }

  public Loader(Browser browser)
  {
    this(browser, 1);
  }

  public void setWorldURL(URL baseURL, URL worldURL)
    throws MalformedURLException
  {
    this.worldURL = worldURL;
    if (baseURL == null) {
      if (worldURL == null) {
        this.worldURLBaseName = new String("./");
      }
      else
      {
        StringTokenizer stok = new StringTokenizer(worldURL.toString(), "/");

        int tocount = stok.countTokens() - 1;
        StringBuffer sb = new StringBuffer(80);
        for (int ji = 0; ji < tocount; ji++) {
          String a = stok.nextToken();

          if ((ji == 0) && (!a.equals("file:")) && (!a.equals("jar:file:"))) {
            sb.append(a);
            sb.append("/");
            sb.append("/");
          }
          else {
            sb.append(a);
            sb.append("/");
          }
        }
        this.worldURLBaseName = sb.toString();
      }
    }
    else {
      this.worldURLBaseName = baseURL.toString();
    }
    if (this.debug)
      System.out.println("World URL base is \"" + this.worldURLBaseName + "\"");
    try
    {
      this.worldURLBase = new URL(this.worldURLBaseName);

      this.scl = ((URLClassLoader)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Object run() {
          return new URLClassLoader(new URL[] { Loader.this.worldURLBase });
        }
      }));
    }
    catch (MalformedURLException murle)
    {
      this.worldURLBase = ((URL)null);
      throw murle;
    }
  }

  InputStream openURL(URL url)
    throws IOException
  {
    InputStream is = null;
    if (url.getProtocol().equals("file")) {
      if (this.debug) {
        System.out.println("Using direct input stream on file url");
      }
      URLConnection urlc = url.openConnection();
      urlc.connect();
      is = new DataInputStream(urlc.getInputStream());
    }
    else {
      double start = 0.0D;
      if (this.timing) {
        start = Time.getNow();
      }
      ContentNegotiator cn = new ContentNegotiator(url);
      byte[] buf = (byte[])(byte[])cn.getContent();
      is = new ByteArrayInputStream(buf);
      if (this.timing) {
        double elapsed = Time.getNow() - start;
        System.out.println("Loader: open and buffer URL in: " + this.numFormat.format(elapsed, 2) + " seconds");
      }
    }

    return is;
  }

  public Scene load(URL worldURL)
    throws IOException, InvalidVRMLSyntaxException
  {
    return load(openURL(worldURL));
  }

  Reader checkInput(InputStream is)
    throws InvalidVRMLSyntaxException
  {
    if (!is.markSupported()) {
      is = new BufferedInputStream(is);
    }

    byte[] buf = new byte[20];
    try {
      is.mark(buf.length);
      is.read(buf, 0, buf.length);
      is.reset();

      if (((buf[0] == 37) && (buf[1] == 213) && (buf[2] == 8) && (buf[3] == 8)) || ((buf[0] == 31) && (buf[1] == -117) && (buf[2] == 8) && (buf[3] == 8)))
      {
        double start = 0.0D;
        if (this.timing) {
          start = Time.getNow();
        }
        this.gis = new GZIPInputStream(is);
        ByteBuf bb = new ByteBuf();
        int r;
        while ((r = this.gis.read()) != -1) {
          bb.add((byte)r);
        }
        bb.trim();
        is = new ByteArrayInputStream(bb.array);
        if (this.debug) {
          System.out.println(new String(bb.array));
        }
        if (this.timing) {
          double elapsed = Time.getNow() - start;
          System.out.println("Loader: upzip URL in: " + this.numFormat.format(elapsed, 2) + " seconds");
        }

        is.mark(buf.length);
        is.read(buf, 0, buf.length);
        is.reset();
      }
    }
    catch (IOException e) {
      InvalidVRMLSyntaxException i = new InvalidVRMLSyntaxException("Problem unzipping");
      i.initCause(e);
      throw i;
    }
    return new InputStreamReader(is);
  }

  Parser newParser(Reader reader)
  {
    return new Parser(this, reader);
  }

  public Scene load(InputStream is)
    throws InvalidVRMLSyntaxException
  {
    Reader reader = checkInput(is);
    return load(reader);
  }

  public Scene load(Reader reader)
    throws InvalidVRMLSyntaxException
  {
    double start = 0.0D;

    if (this.timing) {
      start = Time.getNow();
      this.nodeInitImpl = 0.0D;
      this.ifsParse = 0.0D;
      this.ifsInit = 0.0D;
      this.MFInt32Parse = 0.0D;
      this.MFInt32Copy = 0.0D;
      this.MFVec3fParse = 0.0D;
      this.MFVec3fCopy = 0.0D;
      this.numGroups = 0;
      this.numIfs = 0;
    }

    Parser oldParser = this.parser;
    Scene oldScene = this.scene;

    this.parser = newParser(reader);
    this.scene = new Scene();

    this.scene.setWorldInfo(new WorldInfo(this));

    this.defTableStack.push(this.scene);
    try
    {
      this.parser.Scene();
    }
    catch (ParseException e) {
      InvalidVRMLSyntaxException i = new InvalidVRMLSyntaxException("Last token was \"" + this.parser.token.image + "\" at line " + this.parser.token.beginLine);

      i.initCause(e);
      throw i;
    }
    this.defTableStack.pop();

    if (this.timing) {
      double elapsed = Time.getNow() - start;
      System.out.println("Parse in " + this.numFormat.format(elapsed, 2) + " seconds");

      System.out.println("Node init impl: " + this.numFormat.format(this.nodeInitImpl, 2) + " IFS parse: " + this.numFormat.format(this.ifsParse, 2) + " IFS init: " + this.numFormat.format(this.ifsInit, 2));

      System.out.println("MFInt32 parse: " + this.numFormat.format(this.MFInt32Parse, 2) + " copy: " + this.numFormat.format(this.MFInt32Copy, 2) + " MFVec3f parse: " + this.numFormat.format(this.MFVec3fParse, 2) + " copy: " + this.numFormat.format(this.MFVec3fCopy, 2));

      System.out.println("Scene contains " + this.numGroups + " groups " + this.numIfs + " IFSs");
    }

    Scene retval = this.scene;

    this.parser = oldParser;
    this.scene = oldScene;

    return retval;
  }

  public URL stringToURL(String urlString)
    throws MalformedURLException
  {
    URL loadUrl;
    try
    {
      loadUrl = new URL(urlString);
    }
    catch (MalformedURLException e)
    {
      loadUrl = new URL(this.worldURLBaseName + urlString);
    }

    return loadUrl;
  }

  public void loadProto(String urlString, String protoName, Vector protoFields)
    throws IOException, MalformedURLException, ParseException
  {
    String extName = null;
    int hashIndex = urlString.lastIndexOf("#");
    if (hashIndex != -1) {
      extName = urlString.substring(hashIndex + 1);
      urlString = urlString.substring(0, hashIndex);
    }
URL url = stringToURL(urlString);
    Scene protoScene;
    try { protoScene = load(url);
    } catch (Exception e)
    {
      if ((e instanceof IOException)) {
        throw ((IOException)e);
      }
      if ((e instanceof MalformedURLException)) {
        throw ((MalformedURLException)e);
      }
      if ((e instanceof ParseException)) {
        throw ((ParseException)e);
      }

      System.out.println("Internal error parsing EXTERNPROTO:");
      e.printStackTrace(System.err);
      return;
    }
    Proto proto;
    if (extName != null)
    {
      if ((proto = (Proto)protoScene.protos.get(extName)) == null) {
        throw new ParseException("Parsing EXTERNPROTO, no PROTO  named \"" + extName + "\" found in URL \"" + urlString + "\"");
      }

    }
    else if ((proto = protoScene.firstProto) == null) {
      throw new ParseException("Parsing EXTERNPROTO, no PROTO found in URL \"" + urlString + "\"");
    }

    proto.name = protoName;

    this.scene.protos.put(protoName, proto);
  }

  void warning(String id, String warning)
  {
    if (this.warnLevel == 0) {
      return;
    }
    if (this.warnLevel == 1) {
      if (this.warnSet.contains(id)) {
        return;
      }

      this.warnSet.add(id);
    }

    System.err.println("VRML Loader warning: " + id + " reports: \n\"" + warning + "\" at or before line " + this.parser.currentLine());
  }

  void clear()
  {
    this.curProto = null;
  }

  Proto curProto()
  {
    return this.curProto;
  }

  void namespaceDefine(String defName, BaseNode node)
  {
    if (node == null) {
      System.err.println("Define with name " + defName + " is null");
    }
    else {
      node.define(defName);
    }
    Namespace namespace = (Namespace)this.defTableStack.peek();
    namespace.define(defName, node);
  }

  BaseNode namespaceUse(String defName)
  {
    Namespace namespace = (Namespace)this.defTableStack.peek();
    BaseNode retval = namespace.use(defName);
    return retval;
  }

  synchronized void beginProtoDefine(Proto proto)
  {
    this.curProto = proto;
    if (this.debug) {
      System.out.println("Starting to define proto: " + proto.getName());
    }
    if (this.scene.firstProto == null) {
      if (this.debug) {
        System.out.println("Proto is first in scene");
      }
      this.scene.firstProto = proto;
    }
    this.scene.protos.put(proto.getName(), proto);
    this.defTableStack.push(proto);
  }

  synchronized void endProtoDefine()
  {
    if (this.debug) {
      System.out.println("Proto " + this.curProto.getName() + " is stored as\n" + this.curProto);
    }

    this.curProto = null;
    this.defTableStack.pop();
  }

  synchronized Proto lookupProto(String name)
  {
    Proto proto = (Proto)this.scene.protos.get(name);
    if (this.debug) {
      System.out.println("Proto " + proto.getName() + " is retrived as:\n" + proto);
    }

    return proto;
  }

  synchronized void beginProtoInstance(ProtoInstance newInstance)
  {
    this.protoInstance = newInstance;
    this.defTableStack.push(newInstance);
  }

  synchronized void registerClone(BaseNode org, BaseNode clone)
  {
    if (this.debug) {
      System.out.println("Registering clone " + clone.toStringId() + " = " + clone);
    }

    if (org.defName != null) {
      if (this.debug) {
        System.out.println("Register clone DEF " + org.defName);
      }
      namespaceDefine(org.defName, clone);
    }

    clone.initImpl();

    if (this.protoInstance != null)
      this.protoInstance.proto.registerClone(org, clone);
  }

  synchronized void endProtoInstance()
  {
    this.protoInstance = null;
    this.defTableStack.pop();
  }

  synchronized void connect(BaseNode fromNode, String fromEventOut, BaseNode toNode, String toEventIn, boolean testTypes)
  {
    if (this.curProto != null) {
      this.curProto.addRoute(fromNode, fromEventOut, toNode, toEventIn);
      return;
    }
    Field fromField;
    if ((fromNode instanceof Script)) {
      fromField = ((Script)fromNode).getField(fromEventOut);
    }
    else {
      fromField = ((Node)fromNode).getField(fromEventOut);
    }
    if ((fromField instanceof ConstField)) {
      fromField = ((ConstField)fromField).ownerField;
    }
    if ((testTypes) && (!fromField.isEventOut()))
      throw new InvalidEventOutException();
    Field toField;
    if ((toNode instanceof Script)) {
      toField = ((Script)toNode).getField(toEventIn);
    }
    else {
      toField = ((Node)toNode).getField(toEventIn);
    }
    if ((fromField instanceof ConstField)) {
      throw new InvalidEventOutException();
    }
    if ((testTypes) && (!toField.isEventIn())) {
      throw new InvalidEventInException();
    }

    fromField.connectToField(toField);

    if (this.debug) {
      System.out.println("Added ROUTE ");
    }
    if (this.debug) {
      System.out.println("   " + fromNode.toStringId() + "." + fromField.toStringId());
    }

    if (this.debug) {
      System.out.println("TO " + toNode.toStringId() + "." + toField.toStringId());
    }

    if (!(toNode instanceof SceneTransform))
      toField.markWriteable();
  }

  public vrml.node.Script newScriptInstanceFromClassName(String scriptClassName)
    throws IllegalAccessException, InstantiationException, ClassNotFoundException
  {
    return (vrml.node.Script)this.scl.loadClass(scriptClassName).newInstance();
  }

  byte[] getBytes(String URLstring)
    throws MalformedURLException, IOException
  {
    byte[] buf = null;
    if (this.debug) {
      System.out.println("loader.getBytes(" + URLstring + ") called");
    }

    URL fu = new URL(URLstring);
    if (fu.getProtocol().equals("file")) {
      if (this.debug) {
        System.out.println("Using direct input stream on file url");
      }

      URLConnection urlc = fu.openConnection();
      urlc.connect();
      urlc.getContentType();
      DataInputStream is = new DataInputStream(urlc.getInputStream());
      int length = urlc.getContentLength();
      buf = new byte[length];
      is.readFully(buf, 0, length);
    }
    else {
      ContentNegotiator cn = new ContentNegotiator(fu);
      buf = (byte[])(byte[])cn.getContent();
    }
    return buf;
  }

  byte[] getRelBytes(String relURL)
    throws MalformedURLException, IOException
  {
    String fullURL = this.worldURLBaseName + relURL;
    byte[] buf = getBytes(fullURL);
    return buf;
  }

  Stack getViewpointStack()
  {
    if (this.browser != null) {
      return this.browser.viewpointStack;
    }

    return null;
  }

  Stack getNavigationInfoStack()
  {
    if (this.browser != null) {
      return this.browser.navigationInfoStack;
    }

    return null;
  }

  Stack getFogStack()
  {
    if (this.browser != null) {
      return this.browser.fogStack;
    }

    return null;
  }

  Stack getBackgroundStack()
  {
    if (this.browser != null) {
      return this.browser.backgroundStack;
    }

    return null;
  }

  void cleanUp()
  {
    if (this.browser != null)
      this.browser.cleanUp();
  }

  void setDescription(String description)
  {
    this.scene.setDescription(description);
  }

  void addRoute(BaseNode fromNode, String fromEventOut, BaseNode toNode, String toEventIn)
  {
    if (this.loaderMode > 0) {
      fromEventOut = Field.baseName(fromEventOut);
      toEventIn = Field.baseName(toEventIn);
      connect(fromNode, fromEventOut, toNode, toEventIn, true);
    }
  }

  void addObject(BaseNode node)
  {
    if (this.curProto != null) {
      if (this.debug) {
        System.out.println("Loader.addObject(): adding node " + node.toStringId() + " to curProto instead of scene");
      }

      this.curProto.addObject(node);
    }
    else {
      this.scene.addObject(node);
    }
  }

  void addViewpoint(Viewpoint viewpoint)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addViewpoint(viewpoint);
  }

  void addNavigationInfo(NavigationInfo navInfo)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addNavigationInfo(navInfo);
  }

  void addBackground(Background background)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addBackground(background);
  }

  void addFog(Fog fog)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addFog(fog);
  }

  void addLight(Light light)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addLight(light);
  }

  void addSharedGroup(SharedGroup newGroup)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addSharedGroup(newGroup);
  }

  void addTimeSensor(TimeSensor ts)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addTimeSensor(ts);
  }

  void addVisibilitySensor(VisibilitySensor vs)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addVisibilitySensor(vs);
  }

  void addTouchSensor(TouchSensor ts)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addTouchSensor(ts);
  }

  void addAudioClip(AudioClip clip)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.addAudioClip(clip);
  }

  void setWorldInfo(WorldInfo worldInfo)
  {
    if ((this.curProto == null) && (this.scene != null))
      this.scene.setWorldInfo(worldInfo);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Loader
 * JD-Core Version:    0.6.0
 */