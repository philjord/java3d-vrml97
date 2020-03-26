/*
 * $RCSfile: Loader.java,v $
 *
 *      @(#)Loader.java 1.46 99/03/24 15:35:16
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
 * $Revision: 1.4 $
 * $Date: 2006/04/08 14:45:24 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 * @author  Scott Hong
 * @author  Nikolai V. Chr.
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.awt.AWTEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.System;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

/**  Description of the Class */
public class Loader {

    GZIPInputStream gis;

    // the browser which owns the loader (null if using J3D Loader interface)
    Browser browser = null;

    // The top level URL and it's base, which is used for relative URLs
    URL worldURL;
    URL worldURLBase;
    String worldURLBaseName;

    int loaderMode;

    // State which depends on the current file (pushed/popped in load() for
    // inlines, extern protos, etc)
    Parser parser = null;
    Scene scene = null;
    // DEF/USE table stack, current namespace is on top, loader is always there
    Stack defTableStack = new Stack();

    // temp state for defining or instancing a proto
    Proto curProto = null;
    ProtoInstance protoInstance = null;

    // temp areas for parser
    IntBuf intBuf = new IntBuf();
    FloatBuf floatBuf = new FloatBuf();
    DoubleBuf doubleBuf = new DoubleBuf();

    // utility constants
    Bounds infiniteBounds;
    BoundingLeaf infiniteBoundingLeaf;
    Bounds zeroBounds;
    boolean autoSmooth = false;

    // Java bytecode loader
    URLClassLoader scl;

    // debug stuff
    boolean debug = false;
    boolean traceLex = false;
    boolean nowarn = false;
    boolean warnall = false;
    boolean parserMarks = false;
    boolean printRoutes = false;
    boolean timing = false;

    TreePrinter treePrinter = new TreePrinter();

    // timing stuff
    double nodeInitImpl;
    double ifsParse;
    double ifsInit;
    double MFInt32Parse, MFInt32Copy;
    double MFVec3fParse, MFVec3fCopy;
    int numGroups;
    int numIfs;
    NumFormat numFormat = new NumFormat();

    int warnLevel = WARN_ONCE;
    HashSet warnSet = new HashSet();

    /**  Description of the Field */
    public final static int LOAD_STATIC = 0;
    /**  Description of the Field */
    public final static int LOAD_OPTIMIZED = 1;
    /**  Description of the Field */
    public final static int LOAD_CONFORMANT = 2;

    final static int WARN_NEVER = 0;
    final static int WARN_ONCE = 1;
    final static int WARN_ALL = 2;


    /**
     *Constructor for the Loader object
     *
     * @param  initBrowser Description of the Parameter
     * @param  loadMode Description of the Parameter
     */
    public Loader(Browser initBrowser, int loadMode) {
        browser = initBrowser;
        loaderMode = loadMode;
        try {
            debug = ((Boolean) java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction() {
                    public Object run() {
                        Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.debug"));
                        return b;
                    }
                }
                    )).booleanValue();
            timing = ((Boolean) java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction() {
                    public Object run() {
                        Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.timing"));
                        return b;
                    }
                }
                    )).booleanValue();
            printRoutes = ((Boolean) java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction() {
                    public Object run() {
                        Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.printRoutes"));
                        return b;
                    }
                }
                    )).booleanValue();
            parserMarks = ((Boolean) java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction() {
                    public Object run() {
                        Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.parserMarks"));
                        return b;
                    }
                }
                    )).booleanValue();
            warnall = ((Boolean) java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction() {
                    public Object run() {
                        Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.warnall"));
                        return b;
                    }
                }
                    )).booleanValue();
            nowarn = ((Boolean) java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction() {
                    public Object run() {
                        Boolean b = new Boolean(Boolean.getBoolean("org.jdesktop.j3d.loaders.vrml97.nowarn"));
                        return b;
                    }
                }
                    )).booleanValue();

        }
        catch (java.security.AccessControlException ace) {
            ;
        }// silently, Applet

        if (nowarn) {
            warnLevel = WARN_NEVER;
        }
        if (warnall) {
            warnLevel = WARN_ALL;
        }
        defTableStack = new Stack();
        infiniteBounds = new BoundingSphere(new Point3d(), Double.MAX_VALUE);
        infiniteBoundingLeaf = new BoundingLeaf(infiniteBounds);
        infiniteBoundingLeaf.setCapability(BoundingLeaf.ALLOW_REGION_READ);
        zeroBounds = new BoundingSphere(new Point3d(), -1.0);
    }

    /**
     *Constructor for the Loader object
     *
     * @param  browser Description of the Parameter
     */
    public Loader(Browser browser) {
        this(browser, LOAD_OPTIMIZED);
    }

    /**
     *  Sets the worldURL attribute of the Loader object
     *
     * @param  baseURL The new worldURL value
     * @param  worldURL The new worldURL value
     * @exception  MalformedURLException Either no legal protocol could be found in one of the urls or one could not be parsed.
     */
    public void setWorldURL(URL baseURL, URL worldURL) throws MalformedURLException {
        this.worldURL = worldURL;
        if (baseURL == null) {
            if (worldURL == null) {// input is from an input stream
                worldURLBaseName = new String("." + "/");
            }
            else {
                // figure out baseURL from the worldURL
                java.util.StringTokenizer stok =
                        new java.util.StringTokenizer(worldURL.toString(),
                        "/");
                int tocount = stok.countTokens() - 1;
                StringBuffer sb = new StringBuffer(80);
                for (int ji = 0; ji < tocount; ji++) {
                    String a = stok.nextToken();
                    //Issue 4 : https://j3d-vrml97.dev.java.net/issues/show_bug.cgi?id=4
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
                worldURLBaseName = sb.toString();
            }
        }
        else {
            worldURLBaseName = baseURL.toString();
        }
        if (debug) {
            System.out.println("World URL base is \"" + worldURLBaseName + "\"");
        }
        try {
            worldURLBase = new URL(worldURLBaseName);
            // Give Applet style security for where classes
            // can come from. A Script must originate relative
            // to the parent URL.
            scl = (java.net.URLClassLoader) java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction() {
                    public Object run() {
                        return new URLClassLoader(new URL[]{worldURLBase});
                    }
                }
                    );

        }
        catch (java.net.MalformedURLException murle) {
            worldURLBase = (URL) null;
            throw murle;
        }
    }

    /**
     *  Description of the Method
     *
     * @param  url Description of the Parameter
     * @return  Description of the Return Value
     * @exception  IOException If the file could not be opened.
     */
    InputStream openURL(URL url) throws IOException {
        InputStream is = null;
        if (url.getProtocol().equals("file")) {
            if (debug) {
                System.out.println("Using direct input stream on file url");
            }
            URLConnection urlc = url.openConnection();
            urlc.connect();
            is = new DataInputStream(urlc.getInputStream());
        }
        else {
            double start = 0;
            if (timing) {
                start = Time.getNow();
            }
            ContentNegotiator cn = new ContentNegotiator(url);
            byte[] buf = (byte[]) cn.getContent();
            is = new ByteArrayInputStream(buf);
            if (timing) {
                double elapsed = Time.getNow() - start;
                System.out.println("Loader: open and buffer URL in: " +
                        numFormat.format(elapsed, 2) + " seconds");
            }
        }
        return is;
    }

    /**
     *  Description of the Method
     *
     * @param  worldURL Description of the Parameter
     * @return  Description of the Return Value
     * @exception  IOException If the file could not be opened.
     * @exception  vrml.InvalidVRMLSyntaxException If there was a problem unzipping or parsing.
     */
    public Scene load(URL worldURL)
             throws IOException, vrml.InvalidVRMLSyntaxException {
        return load(openURL(worldURL));
    }

    /**
     *  Description of the Method
     *
     * @param  is Description of the Parameter
     * @return  Description of the Return Value
     * @exception  vrml.InvalidVRMLSyntaxException If there was a problem unzipping.
     */
    Reader checkInput(InputStream is) throws vrml.InvalidVRMLSyntaxException {
        if (is.markSupported() == false) {
            is = new BufferedInputStream(is);
        }

        // check for a gzipped input stream
        byte[] buf = new byte[20];
        try {
            is.mark(buf.length);
            is.read(buf, 0, buf.length);
            is.reset();
            //if (buf[0]==0x1f && buf[1]==0x8b && buf[2]==0x08 && buf[3]==0x08)
            // don't ask why the header changes.
            if ((buf[0] == 37 && buf[1] == 213 && buf[2] == 8 && buf[3] == 8) ||
                    (buf[0] == 31 && buf[1] == -117 && buf[2] == 8 && buf[3] == 8)) {
                double start = 0.0;
                if (timing) {
                    start = Time.getNow();
                }
                gis = new GZIPInputStream(is);
                ByteBuf bb = new ByteBuf();
                int r;
                while ((r = gis.read()) != -1) {
                    bb.add((byte) r);
                }
                bb.trim();
                is = new ByteArrayInputStream(bb.array);
                if (debug) {
                    System.out.println(new String(bb.array));
                }
                if (timing) {
                    double elapsed = Time.getNow() - start;
                    System.out.println("Loader: upzip URL in: " +
                            numFormat.format(elapsed, 2) + " seconds");
                }
                is.mark(buf.length);
                is.read(buf, 0, buf.length);
                is.reset();
            }
        }
        catch (IOException e) {
            vrml.InvalidVRMLSyntaxException i = new vrml.InvalidVRMLSyntaxException("Problem unzipping");
            i.initCause(e);
            throw i;
        }
        return new InputStreamReader(is);
    }

    /**
     *  Description of the Method
     *
     * @param  reader Description of the Parameter
     * @return  Description of the Return Value
     */
    Parser newParser(Reader reader) {
        // only one of these blocks of code should be in use:

        // For using generated Token manager:
        return new Parser(this, reader);
        // for custom token manager (USER_TOKEN_MANAGER=true in Parser.jj):
        // VrmlCharStream charStream = new VrmlCharStream(reader, 0, 0);
        // VrmlTokenManager tokenManager =
        //			new VrmlTokenManager(charStream, this);
        // return new Parser(this, tokenManager);
    }

    /**
     *  Description of the Method
     *
     * @param  is Description of the Parameter
     * @return  Description of the Return Value
     * @exception  vrml.InvalidVRMLSyntaxException If there was a problem unzipping or parsing.
     */
    public Scene load(InputStream is) throws vrml.InvalidVRMLSyntaxException {
        Reader reader = checkInput(is);
        return load(reader);
    }

    /**
     *  Description of the Method
     *
     * @param  reader Description of the Parameter
     * @return  Description of the Return Value
     * @exception  vrml.InvalidVRMLSyntaxException If there was a problem parsing.
     */
    public Scene load(Reader reader) throws vrml.InvalidVRMLSyntaxException {
        double start = 0.0;

        // Check for VRML header
        // if (reader.markSupported() == false) {
        //    reader = new BufferedReader(reader);
        //}
        //char[] buf = new char[20];
        //reader.mark(buf.length);
        //reader.read(buf, 0, buf.length);
        //reader.reset();
        //String header = new String(buf);
        //bug: doesn't always recognize as correct header even if it is
        //if (!header.startsWith("#VRML V2.0 utf8",header.indexOf('#'))) {
        //    throw new vrml.InvalidVRMLSyntaxException();
        //}

        if (timing) {
            start = Time.getNow();
            nodeInitImpl = 0.0;
            ifsParse = 0.0;
            ifsInit = 0.0;
            MFInt32Parse = 0.0;
            MFInt32Copy = 0.0;
            MFVec3fParse = 0.0;
            MFVec3fCopy = 0.0;
            numGroups = 0;
            numIfs = 0;
        }

        // save the old values (if any) to restore after the load
        Parser oldParser = parser;
        Scene oldScene = scene;

        parser = newParser(reader);
        scene = new Scene();
        // general Scene bug:
        // A WorldInfo node can only be instanced by someone who
        // has a Loader. The Scene object has no knowledge of
        // its Loader owner, but has to handle the WorldInfo,
        // and in the case one is not provided in the file,
        // can't create the default WorldInfo.
        // this is also a general impl.Node bug, that you need
        // the Loader to instance one is too restrictive.
        scene.setWorldInfo(new WorldInfo(this));

        defTableStack.push(scene);

        try {
            parser.Scene();
        }
        catch (ParseException e) {
            vrml.InvalidVRMLSyntaxException i = new vrml.InvalidVRMLSyntaxException("Last token was \"" + parser.token.image +
                    "\" at line " + parser.token.beginLine);
            i.initCause(e);
            throw i;
        }
        defTableStack.pop();

        if (timing) {
            double elapsed = Time.getNow() - start;
            System.out.println("Parse in " + numFormat.format(elapsed, 2) +
                    " seconds");
            System.out.println("Node init impl: " +
                    numFormat.format(nodeInitImpl, 2) +
                    " IFS parse: " + numFormat.format(ifsParse, 2) +
                    " IFS init: " + numFormat.format(ifsInit, 2));
            System.out.println("MFInt32 parse: " +
                    numFormat.format(MFInt32Parse, 2) + " copy: " +
                    numFormat.format(MFInt32Copy, 2) + " MFVec3f parse: " +
                    numFormat.format(MFVec3fParse, 2) + " copy: " +
                    numFormat.format(MFVec3fCopy, 2));
            System.out.println("Scene contains " + numGroups + " groups " +
                    numIfs + " IFSs");
        }

        Scene retval = scene;

        // restore the prevous load's values
        parser = oldParser;
        scene = oldScene;

        return retval;
    }

    /**
     *  Description of the Method
     *
     * @param  urlString Description of the Parameter
     * @return  Description of the Return Value
     * @exception  java.net.MalformedURLException Either no legal protocol could be found in a specification string or the string could not be parsed.
     */
    public URL stringToURL(String urlString)
             throws java.net.MalformedURLException {
        URL loadUrl;
        try {
            loadUrl = new URL(urlString);
        }
        catch (java.net.MalformedURLException e) {
            // might be a relative URL
            loadUrl = new URL(worldURLBaseName + urlString);
            // will throw MalformedURLException if still bad
        }
        return loadUrl;
    }

    // load the proto specified by the urlString, name it protoName, ensure
    // that it has fields that match protoFields
    // TODO: cache old loads to avoid reloading multiple EXTPROTOS with
    // the same URL but different "#name" specifications
    /**
     *  Description of the Method
     *
     * @param  urlString Description of the Parameter
     * @param  protoName Description of the Parameter
     * @param  protoFields Description of the Parameter
     * @exception  IOException If the proto could not be opened.
     * @exception  MalformedURLException Either no legal protocol could be found in urlString or it could not be parsed.
     * @exception  ParseException If there was a problem parsing.
     */
    public void loadProto(String urlString, String protoName,
            Vector protoFields)
             throws IOException, MalformedURLException, ParseException {
        // check for a external name
        String extName = null;
        int hashIndex = urlString.lastIndexOf("#");
        if (hashIndex != -1) {
            extName = urlString.substring(hashIndex + 1);
            urlString = urlString.substring(0, hashIndex);
        }
        // TODO: cut short the parse once we find the right proto
        URL url = stringToURL(urlString);
        Scene protoScene;
        try {
            protoScene = load(url);
        }
        catch (Exception e) {
            if (e instanceof java.io.IOException) {
                throw (IOException) e;
            }
            else if (e instanceof java.net.MalformedURLException) {
                throw (MalformedURLException) e;
            }
            else if (e instanceof ParseException) {
                throw (ParseException) e;
            }
            else {
                System.out.println("Internal error parsing EXTERNPROTO:");
                e.printStackTrace(System.err);
                return;
            }
        }
        Proto proto;
        if (extName != null) {
            if ((proto = (Proto) protoScene.protos.get(extName)) == null) {
                throw new ParseException("Parsing EXTERNPROTO, no PROTO " +
                        " named \"" + extName + "\" found in URL \"" +
                        urlString + "\"");
            }
        }
        else {
            if ((proto = protoScene.firstProto) == null) {
                throw new ParseException("Parsing EXTERNPROTO, no PROTO " +
                        "found in URL \"" + urlString + "\"");
            }
        }

        proto.name = protoName;
        // TODO: check if protoFields match fields in proto
        scene.protos.put(protoName, proto);
    }

    // TODO: these should get passed to the application so they can be
    // displayed in a GUI
    /**
     *  Description of the Method
     *
     * @param  id Description of the Parameter
     * @param  warning Description of the Parameter
     */
    void warning(String id, String warning) {
        if (warnLevel == WARN_NEVER) {
            return;
        }
        if (warnLevel == WARN_ONCE) {
            if (warnSet.contains(id)) {
                return;
            }
            else {
                warnSet.add(id);
            }
        }
        // this should be passed to a listener
        System.err.println("VRML Loader warning: " + id + " reports: \n\"" +
                warning + "\" at or before line " + parser.currentLine());
        return;
    }

    /**  Description of the Method */
    void clear() {
        curProto = null;
    }

    /**
     *  Description of the Method
     *
     * @return  Description of the Return Value
     */
    Proto curProto() {
        return curProto;
    }

    // these are the "switching" versions which use the current namespace
    /**
     *  Description of the Method
     *
     * @param  defName Description of the Parameter
     * @param  node Description of the Parameter
     */
    void namespaceDefine(String defName, BaseNode node) {
        if (node == null) {
            System.err.println("Define with name " + defName + " is null");
        }
        else {
            node.define(defName);
        }
        Namespace namespace = (Namespace) defTableStack.peek();
        namespace.define(defName, node);
    }

    /**
     *  Description of the Method
     *
     * @param  defName Description of the Parameter
     * @return  Description of the Return Value
     */
    BaseNode namespaceUse(String defName) {
        Namespace namespace = (Namespace) defTableStack.peek();
        BaseNode retval = namespace.use(defName);
        return retval;
    }

    /**
     *  Description of the Method
     *
     * @param  proto Description of the Parameter
     */
    synchronized void beginProtoDefine(Proto proto) {
        curProto = proto;
        if (debug) {
            System.out.println("Starting to define proto: " + proto.getName());
        }
        if (scene.firstProto == null) {
            if (debug) {
                System.out.println("Proto is first in scene");
            }
            scene.firstProto = proto;
        }
        scene.protos.put(proto.getName(), proto);
        defTableStack.push(proto);
    }

    /**  Description of the Method */
    synchronized void endProtoDefine() {
        if (debug) {
            System.out.println("Proto " + curProto.getName() +
                    " is stored as\n" + curProto);
        }
        curProto = null;
        defTableStack.pop();
    }

    /**
     *  Description of the Method
     *
     * @param  name Description of the Parameter
     * @return  Description of the Return Value
     */
    synchronized Proto lookupProto(String name) {
        Proto proto = (Proto) scene.protos.get(name);
        if (debug) {
            System.out.println("Proto " + proto.getName() +
                    " is retrived as:\n" + proto);
        }
        return proto;
    }

    /**
     *  Description of the Method
     *
     * @param  newInstance Description of the Parameter
     */
    synchronized void beginProtoInstance(ProtoInstance newInstance) {
        protoInstance = newInstance;
        defTableStack.push(newInstance);
    }

    /**
     *  Description of the Method
     *
     * @param  org Description of the Parameter
     * @param  clone Description of the Parameter
     */
    synchronized void registerClone(BaseNode org, BaseNode clone) {

        if (debug) {
            System.out.println("Registering clone " + clone.toStringId() +
                    " = " + clone);
        }
        // make sure the clone is defined in the new namespace if needed
        if (org.defName != null) {
            if (debug) {
                System.out.println("Register clone DEF " + org.defName);
            }
            namespaceDefine(org.defName, clone);
        }

        // Make sure the clone has been initialized (done after the name
        // is registered so that anonymous nodes can be optimized
        clone.initImpl();

        // tell the protoInstance about the new clone
        if (protoInstance != null) {
            protoInstance.proto.registerClone(org, clone);
        }
    }

    /**  Description of the Method */
    synchronized void endProtoInstance() {
        protoInstance = null;
        defTableStack.pop();
    }

    // internal version, only test types if flag is set.  Protos make
    // illegal EventIn->EventIn and EventOut->EventOut routes for IS maps
    /**
     *  Description of the Method
     *
     * @param  fromNode Description of the Parameter
     * @param  fromEventOut Description of the Parameter
     * @param  toNode Description of the Parameter
     * @param  toEventIn Description of the Parameter
     * @param  testTypes Description of the Parameter
     */
    synchronized void connect(BaseNode fromNode, String fromEventOut,
            BaseNode toNode, String toEventIn, boolean testTypes) {
        Field fromField;
        Field toField;

        if (curProto != null) {
            curProto.addRoute(fromNode, fromEventOut, toNode, toEventIn);
            return;
        }

        if (fromNode instanceof Script) {
            fromField = ((Script) fromNode).getField(fromEventOut);
        }
        else {
            fromField = ((Node) fromNode).getField(fromEventOut);
        }
        if (fromField instanceof ConstField) {
            fromField = ((ConstField) fromField).ownerField;
        }
        if (testTypes && !fromField.isEventOut()) {
            throw new vrml.InvalidEventOutException();
        }

        if (toNode instanceof Script) {
            toField = ((Script) toNode).getField(toEventIn);
        }
        else {
            toField = ((Node) toNode).getField(toEventIn);
        }
        if (fromField instanceof ConstField) {
            throw new vrml.InvalidEventOutException();
        }
        if (testTypes && !toField.isEventIn()) {
            throw new vrml.InvalidEventInException();
        }

        fromField.connectToField(toField);

        if (debug) {
            System.out.println("Added ROUTE ");
        }
        if (debug) {
            System.out.println("   " + fromNode.toStringId() + "." +
                    fromField.toStringId());
        }
        if (debug) {
            System.out.println("TO " + toNode.toStringId() + "." +
                    toField.toStringId());
        }

        // tell the toNode to make the capability writeable
        // Note: will only work if Node has called eventify--
        // should be done in the Field constructors...
        if (!(toNode instanceof SceneTransform)) {
            toField.markWriteable();
        }

    }


    /**
     *  Description of the Method
     *
     * @param  scriptClassName Description of the Parameter
     * @return  Description of the Return Value
     * @exception  IllegalAccessException No access to the definition of the specified class.
     * @exception  InstantiationException There was a problem making an instance of the class.
     * @exception  ClassNotFoundException The class was not found.
     */
    public vrml.node.Script newScriptInstanceFromClassName(String scriptClassName)
             throws IllegalAccessException,
            InstantiationException, ClassNotFoundException {
        return (vrml.node.Script)
                scl.loadClass(scriptClassName).newInstance();
    }

    /**
     *  Gets the bytes attribute of the Loader object
     *
     * @param  URLstring Description of the Parameter
     * @return  The bytes value
     * @exception  IOException If the URLstring could not be opened.
     * @exception  MalformedURLException Either no legal protocol could be found in URLstring or it could not be parsed.
     */
    byte[] getBytes(String URLstring) throws MalformedURLException, IOException {
        URL fu;
        byte[] buf = null;
        if (debug) {
            System.out.println("loader.getBytes(" + URLstring + ") called");
        }
        // try {
        fu = new URL(URLstring);
        if (fu.getProtocol().equals("file")) {
            if (debug) {
                System.out.println(
                        "Using direct input stream on file url");
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
            buf = (byte[]) cn.getContent();
        }
        return buf;
    }


    /**
     *  Gets the relBytes attribute of the Loader object
     *
     * @param  relURL Description of the Parameter
     * @return  The relBytes value
     * @exception  IOException If the relURL could not be opened.
     * @exception  MalformedURLException Either no legal protocol could be found in relURL or it could not be parsed.
     */
    byte[] getRelBytes(String relURL) throws MalformedURLException, IOException {
        String fullURL = worldURLBaseName + relURL;
        byte[] buf = getBytes(fullURL);
        return buf;
    }


    /**
     *  Gets the viewpointStack attribute of the Loader object
     *
     * @return  The viewpointStack value
     */
    Stack getViewpointStack() {
        if (browser != null) {
            return browser.viewpointStack;
        }
        else {
            return null;
        }
    }

    /**
     *  Gets the navigationInfoStack attribute of the Loader object
     *
     * @return  The navigationInfoStack value
     */
    Stack getNavigationInfoStack() {
        if (browser != null) {
            return browser.navigationInfoStack;
        }
        else {
            return null;
        }
    }

    /**
     *  Gets the fogStack attribute of the Loader object
     *
     * @return  The fogStack value
     */
    Stack getFogStack() {
        if (browser != null) {
            return browser.fogStack;
        }
        else {
            return null;
        }
    }

    /**
     *  Gets the backgroundStack attribute of the Loader object
     *
     * @return  The backgroundStack value
     */
    Stack getBackgroundStack() {
        if (browser != null) {
            return browser.backgroundStack;
        }
        else {
            return null;
        }
    }


    /**  Description of the Method */
    void cleanUp() {
        if (browser != null) {
            browser.cleanUp();
        }
    }

    // Parser callbacks:
    /**
     *  Sets the description attribute of the Loader object
     *
     * @param  description The new description value
     */
    void setDescription(String description) {
        scene.setDescription(description);
    }

    /**
     *  Adds a feature to the Route attribute of the Loader object
     *
     * @param  fromNode The feature to be added to the Route attribute
     * @param  fromEventOut The feature to be added to the Route attribute
     * @param  toNode The feature to be added to the Route attribute
     * @param  toEventIn The feature to be added to the Route attribute
     */
    void addRoute(BaseNode fromNode, String fromEventOut,
            BaseNode toNode, String toEventIn) {
        if (loaderMode > LOAD_STATIC) {
            fromEventOut = Field.baseName(fromEventOut);
            toEventIn = Field.baseName(toEventIn);
            connect(fromNode, fromEventOut, toNode, toEventIn, true);
        }
    }

    /**
     *  Adds a feature to the Object attribute of the Loader object
     *
     * @param  node The feature to be added to the Object attribute
     */
    void addObject(BaseNode node) {
        if (curProto != null) {
            if (debug) {
                System.out.println("Loader.addObject(): adding node " +
                        node.toStringId() + " to curProto instead of scene");
            }
            curProto.addObject(node);
        }
        else {
            scene.addObject(node);
        }
    }

    // The following add the node to the scene, unless we are defining a
    // proto.  For protos,  the definition does not get added to the scene.
    // When the proto is instanced, the instance does get added to the scene.
    // Note the scene interface is shared with other file loaders, otherwise
    // this would be more direct.
    /**
     *  Adds a feature to the Viewpoint attribute of the Loader object
     *
     * @param  viewpoint The feature to be added to the Viewpoint attribute
     */
    void addViewpoint(Viewpoint viewpoint) {
        if ((curProto == null) && (scene != null)) {
            scene.addViewpoint(viewpoint);
        }
    }

    /**
     *  Adds a feature to the NavigationInfo attribute of the Loader object
     *
     * @param  navInfo The feature to be added to the NavigationInfo attribute
     */
    void addNavigationInfo(NavigationInfo navInfo) {
        if ((curProto == null) && (scene != null)) {
            scene.addNavigationInfo(navInfo);
        }
    }

    /**
     *  Adds a feature to the Background attribute of the Loader object
     *
     * @param  background The feature to be added to the Background attribute
     */
    void addBackground(Background background) {
        if ((curProto == null) && (scene != null)) {
            scene.addBackground(background);
        }
    }

    /**
     *  Adds a feature to the Fog attribute of the Loader object
     *
     * @param  fog The feature to be added to the Fog attribute
     */
    void addFog(Fog fog) {
        if ((curProto == null) && (scene != null)) {
            scene.addFog(fog);
        }
    }

    /**
     *  Adds a feature to the Light attribute of the Loader object
     *
     * @param  light The feature to be added to the Light attribute
     */
    void addLight(Light light) {
        if ((curProto == null) && (scene != null)) {
            scene.addLight(light);
        }
    }

    /**
     *  Adds a feature to the SharedGroup attribute of the Loader object
     *
     * @param  newGroup The feature to be added to the SharedGroup attribute
     */
    void addSharedGroup(SharedGroup newGroup) {
        if ((curProto == null) && (scene != null)) {
            scene.addSharedGroup(newGroup);
        }
    }

    /**
     *  Adds a feature to the TimeSensor attribute of the Loader object
     *
     * @param  ts The feature to be added to the TimeSensor attribute
     */
    void addTimeSensor(TimeSensor ts) {
        if ((curProto == null) && (scene != null)) {
            scene.addTimeSensor(ts);
        }
    }

    /**
     *  Adds a feature to the VisibilitySensor attribute of the Loader object
     *
     * @param  vs The feature to be added to the VisibilitySensor attribute
     */
    void addVisibilitySensor(VisibilitySensor vs) {
        if ((curProto == null) && (scene != null)) {
            scene.addVisibilitySensor(vs);
        }
    }

    /**
     *  Adds a feature to the TouchSensor attribute of the Loader object
     *
     * @param  ts The feature to be added to the TouchSensor attribute
     */
    void addTouchSensor(TouchSensor ts) {
        if ((curProto == null) && (scene != null)) {
            scene.addTouchSensor(ts);
        }
    }

    /**
     *  Adds a feature to the AudioClip attribute of the Loader object
     *
     * @param  clip The feature to be added to the AudioClip attribute
     */
    void addAudioClip(AudioClip clip) {
        if ((curProto == null) && (scene != null)) {
            scene.addAudioClip(clip);
        }
    }

    /**
     *  Sets the worldInfo attribute of the Loader object
     *
     * @param  worldInfo The new worldInfo value
     */
    void setWorldInfo(WorldInfo worldInfo) {
        if ((curProto == null) && (scene != null)) {
            scene.setWorldInfo(worldInfo);
        }
    }
}

