/*
 * $RCSfile: Script.java,v $
 *
 *      @(#)Script.java 1.26 99/03/24 15:34:13
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
 * $Date: 2006/03/30 08:19:28 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 * @author  Nikolai V. Chr.
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.Hashtable;
import java.util.StringTokenizer;

/**  Description of the Class */
public class Script extends BaseNode implements Notifier {
    /**  Description of the Field */
    public Hashtable FieldSpec = new Hashtable(24);
    vrml.node.Script specScript;
    org.jdesktop.j3d.loaders.vrml97.impl.Event ievent;
    vrml.Event event;
    URLClassLoader scl;
    String scriptName = "";

    // Exposed Field
    MFString url;// currently unused

    // field
    SFBool directOutput;
    SFBool mustEvaluate;

    // the byteCodes are resolved by the Parser's class loader to
    // an instance of vrml.node.Script

    String annexC = "Annex C1 of ISO/IEC 14772 clearly states \"Note that support for the ECMAScript is not required by ISO/IEC 14772\". and \"Browsers are not required to support any specific scripting language. However, browsers shall adhere to the protocol defined in the corresponding annex of ISO/IEC 14772 for any scripting language which is supported. \" and in our case, that is Annex B1 not Annex C1. Please use Java byte code Script nodes, thank you.";

    /**
     *Constructor for the Script object
     *
     *@param  loader Description of the Parameter
     */
    public Script(Loader loader) {
        super(loader);
        ievent = new org.jdesktop.j3d.loaders.vrml97.impl.Event("default",
                0.0, (ConstField) null);
        event = new vrml.Event(ievent);

        url = new MFString();
        directOutput = new SFBool(false);
        mustEvaluate = new SFBool(false);
        initFields();
    }

    /**
     *  Sets the byteCodes attribute of the Script object
     *
     *@param  byteCodes The new byteCodes value
     */
    void setByteCodes(vrml.node.Script byteCodes) {
        specScript = byteCodes;
    }

    /**
     *  Gets the eventIn attribute of the Script object
     *
     *@param  eventInName Description of the Parameter
     *@return  The eventIn value
     *@exception  vrml.InvalidEventInException Description of the Exception
     */
    public final Field getEventIn(String eventInName)
             throws vrml.InvalidEventInException {

        Field f = (Field) FieldSpec.get(eventInName);
        if ((f == null) || !f.isEventIn()) {
            throw new vrml.InvalidEventInException();
        }
        return f;
    }

    /**
     *  Gets the eventOut attribute of the Script object
     *
     *@param  eventOutName Description of the Parameter
     *@return  The eventOut value
     *@exception  vrml.InvalidEventOutException Description of the Exception
     */
    public Field getEventOut(String eventOutName)
             throws vrml.InvalidEventOutException {

        Field f = (Field) FieldSpec.get(eventOutName);
        if ((f == null) || !f.isEventOut()) {
            throw new vrml.InvalidEventOutException(eventOutName);
        }
        return f;
    }


    /**
     *  Gets the field attribute of the Script object
     *
     *@param  fieldName Description of the Parameter
     *@return  The field value
     */
    public final Field getField(String fieldName) {
        fieldName = Field.baseName(fieldName);
        Field f = (Field) FieldSpec.get(fieldName);
        if (f == null) {
            throw new vrml.InvalidFieldException();
        }
        return f;
    }

    /**  Description of the Method */
    void initImpl() {
        URL u;
        String urlName;
        StringTokenizer tok;
        int countToks;
        scriptName = url.strings[0];
        // strip off ".class"
        if (scriptName.endsWith(".class")) {
            scriptName = scriptName.substring(0, scriptName.length() - 6);
        }

        if (loader.debug) {
            System.out.println("Script.initImpl() called in " + this);
        }
        if (loader.loaderMode == Loader.LOAD_STATIC) {
            return;
        }
        if (scriptName.startsWith("vrmlscript:") ||
                scriptName.startsWith("javascript:") ||
                scriptName.startsWith("ecma:") ||
                scriptName.startsWith("ECMA:") ||
                scriptName.startsWith("excema")) {
            throw new vrml.InvalidVRMLSyntaxException(annexC);
        }
        else {
            scl = loader.scl;
            try {
                setByteCodes((vrml.node.Script) scl.loadClass(scriptName).newInstance());
            }
            catch (java.lang.ClassNotFoundException cnfe) {
				vrml.InvalidVRMLSyntaxException i = new vrml.InvalidVRMLSyntaxException("Unable to load class " + scriptName);
				i.initCause(cnfe);
				throw i;
            }
            catch (java.lang.InstantiationException ie) {
                vrml.InvalidVRMLSyntaxException i = new vrml.InvalidVRMLSyntaxException("Unable to intance script node from data");
				i.initCause(ie);
				throw i;
            }
            catch (java.lang.IllegalAccessException iae) {
                vrml.InvalidVRMLSyntaxException i = new vrml.InvalidVRMLSyntaxException("Illegal access to script node data");
				i.initCause(iae);
				throw i;
            }

            // these used to be in initialize()
            specScript.registerOwner(this);
            specScript.initialize();
        }
        implReady = true;
    }


    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (loader.debug) {
            System.out.println("Script.notifyMethod(" + eventInName + ")");
        }
        if (eventInName.equals("url")) {
            ;
        }
        else if (eventInName.startsWith("route_")) {
            // Noop
        }
        else {
            if (loader.debug) {
                System.out.println("Passing event to script");
            }
            ievent.name = eventInName;
            // the user sees an event with a const field in value
            ievent.value = (getField(eventInName)).constify();
            ievent.timeStamp = time;
            specScript.processEvent(event);
            specScript.eventsProcessed();
        }
    }

    // there is a processEvents(int count, Event[] events)
    // method but we don't think at this time that scripts
    // will use it.

    /**
     *  Gets the type attribute of the Script object
     *
     *@return  The type value
     */
    public String getType() {
        return "Script";
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        Script s;
        String name;
        s = new Script(loader);
        java.util.Enumeration keys = FieldSpec.keys();
        java.util.Enumeration elements = FieldSpec.elements();

        while (keys.hasMoreElements()) {
            String fname = (String) keys.nextElement();
            Field f = (Field) elements.nextElement();
            Field c = (Field) f.clone();
            if (f.isEventIn()) {
                s.setEventIn(fname, c);
            }
            else if (f.isEventOut()) {
                s.setEventOut(fname, c);
            }
            else {
                s.setField(fname, c);
            }
        }

        // bug: setEventIn does not throw the isEventIn bit
        s.url = (MFString) s.FieldSpec.get("url");
        s.mustEvaluate = (SFBool) s.FieldSpec.get("mustEvaluate");
        s.directOutput = (SFBool) s.FieldSpec.get("direcOutput");

        s.initImpl();

        //name = (String)((Object)specScript.toString());
        //name = url.get1Value(0);
        try {
            //s.setByteCodes(loader.newInstance(
            //name.substring(0,name.indexOf('@'))));
            s.setByteCodes(
                    (vrml.node.Script) scl.loadClass(scriptName).newInstance()
                    );
            //} catch (IllegalAccessException iae) {
            //iae.printStackTrace();
            //} catch (InstantiationException ie) {
            //ie.printStackTrace();
            //} catch (ClassNotFoundException cnfe) {
            //cnfe.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Oops in " + this);
            e.printStackTrace();
        }
        return (Object) s;
    }

    /**
     *  Sets the eventIn attribute of the Script object
     *
     *@param  eventInName The new eventIn value
     *@param  f The new eventIn value
     */
    public void setEventIn(String eventInName, Field f) {
        if (loader.debug) {
            System.out.println("Adding eventIn " + f.toStringId() +
                    "\n    with name " + eventInName +
                    "\n    to script node " + this.toStringId());
        }
        f.init(this, FieldSpec, Field.EVENT_IN, f.baseName(eventInName));
    }

    /**
     *  Sets the eventOut attribute of the Script object
     *
     *@param  eventOutName The new eventOut value
     *@param  f The new eventOut value
     */
    public void setEventOut(String eventOutName, Field f) {
        if (loader.debug) {
            System.out.println("Adding eventOut " + f.toStringId() +
                    "\n    with name " + eventOutName +
                    "\n    to script node " + this.toStringId());
        }
        f.init(this, FieldSpec, Field.EVENT_OUT, f.baseName(eventOutName));
    }

    /**
     *  Sets the field attribute of the Script object
     *
     *@param  fieldName The new field value
     *@param  f The new field value
     */
    public void setField(String fieldName, Field f) {
        if (loader.debug) {
            System.out.println("Adding field " + f.toStringId() +
                    "\n    with name " + fieldName +
                    "\n    to script node " + this.toStringId());
        }
        f.init(this, FieldSpec, Field.FIELD, f.baseName(fieldName));
    }

    /**
     *  Gets the browser attribute of the Script object
     *
     *@return  The browser value
     */
    public org.jdesktop.j3d.loaders.vrml97.impl.Browser getBrowser() {
        return browser;
    }

    /**  Description of the Method */
    void initFields() {
        url.init(this, FieldSpec, Field.EXPOSED_FIELD, "url");
        directOutput.init(this, FieldSpec, Field.FIELD, "directOutput");
        mustEvaluate.init(this, FieldSpec, Field.FIELD, "mustEvaluate");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.BaseNode wrap() {
        return new vrml.node.Script(this);
    }

}

