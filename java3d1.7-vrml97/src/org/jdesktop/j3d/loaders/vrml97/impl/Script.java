package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Hashtable;
import vrml.InvalidEventInException;
import vrml.InvalidEventOutException;
import vrml.InvalidFieldException;
import vrml.InvalidVRMLSyntaxException;

public class Script extends BaseNode
  implements Notifier
{
  public Hashtable FieldSpec = new Hashtable(24);
  vrml.node.Script specScript;
  Event ievent;
  vrml.Event event;
  URLClassLoader scl;
  String scriptName = "";
  MFString url;
  SFBool directOutput;
  SFBool mustEvaluate;
  String annexC = "Annex C1 of ISO/IEC 14772 clearly states \"Note that support for the ECMAScript is not required by ISO/IEC 14772\". and \"Browsers are not required to support any specific scripting language. However, browsers shall adhere to the protocol defined in the corresponding annex of ISO/IEC 14772 for any scripting language which is supported. \" and in our case, that is Annex B1 not Annex C1. Please use Java byte code Script nodes, thank you.";

  public Script(Loader loader)
  {
    super(loader);
    this.ievent = new Event("default", 0.0D, (ConstField)null);

    this.event = new vrml.Event(this.ievent);

    this.url = new MFString();
    this.directOutput = new SFBool(false);
    this.mustEvaluate = new SFBool(false);
    initFields();
  }

  void setByteCodes(vrml.node.Script byteCodes)
  {
    this.specScript = byteCodes;
  }

  public final Field getEventIn(String eventInName)
    throws InvalidEventInException
  {
    Field f = (Field)this.FieldSpec.get(eventInName);
    if ((f == null) || (!f.isEventIn())) {
      throw new InvalidEventInException();
    }
    return f;
  }

  public Field getEventOut(String eventOutName)
    throws InvalidEventOutException
  {
    Field f = (Field)this.FieldSpec.get(eventOutName);
    if ((f == null) || (!f.isEventOut())) {
      throw new InvalidEventOutException(eventOutName);
    }
    return f;
  }

  public final Field getField(String fieldName)
  {
    fieldName = Field.baseName(fieldName);
    Field f = (Field)this.FieldSpec.get(fieldName);
    if (f == null) {
      throw new InvalidFieldException();
    }
    return f;
  }

  void initImpl()
  {
    this.scriptName = this.url.strings[0];

    if (this.scriptName.endsWith(".class")) {
      this.scriptName = this.scriptName.substring(0, this.scriptName.length() - 6);
    }

    if (this.loader.debug) {
      System.out.println("Script.initImpl() called in " + this);
    }
    if (this.loader.loaderMode == 0) {
      return;
    }
    if ((this.scriptName.startsWith("vrmlscript:")) || (this.scriptName.startsWith("javascript:")) || (this.scriptName.startsWith("ecma:")) || (this.scriptName.startsWith("ECMA:")) || (this.scriptName.startsWith("excema")))
    {
      throw new InvalidVRMLSyntaxException(this.annexC);
    }

    this.scl = this.loader.scl;
    try {
      setByteCodes((vrml.node.Script)this.scl.loadClass(this.scriptName).newInstance());
    }
    catch (ClassNotFoundException cnfe) {
      InvalidVRMLSyntaxException i = new InvalidVRMLSyntaxException("Unable to load class " + this.scriptName);
      i.initCause(cnfe);
      throw i;
    }
    catch (InstantiationException ie) {
      InvalidVRMLSyntaxException i = new InvalidVRMLSyntaxException("Unable to intance script node from data");
      i.initCause(ie);
      throw i;
    }
    catch (IllegalAccessException iae) {
      InvalidVRMLSyntaxException i = new InvalidVRMLSyntaxException("Illegal access to script node data");
      i.initCause(iae);
      throw i;
    }

    this.specScript.registerOwner(this);
    this.specScript.initialize();

    this.implReady = true;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (this.loader.debug) {
      System.out.println("Script.notifyMethod(" + eventInName + ")");
    }
    if (!eventInName.equals("url"))
    {
      if (!eventInName.startsWith("route_"))
      {
        if (this.loader.debug) {
          System.out.println("Passing event to script");
        }
        this.ievent.name = eventInName;

        this.ievent.value = getField(eventInName).constify();
        this.ievent.timeStamp = time;
        this.specScript.processEvent(this.event);
        this.specScript.eventsProcessed();
      }
    }
  }

  public String getType()
  {
    return "Script";
  }

  public Object clone()
  {
    Script s = new Script(this.loader);
    Enumeration keys = this.FieldSpec.keys();
    Enumeration elements = this.FieldSpec.elements();

    while (keys.hasMoreElements()) {
      String fname = (String)keys.nextElement();
      Field f = (Field)elements.nextElement();
      Field c = (Field)f.clone();
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

    s.url = ((MFString)s.FieldSpec.get("url"));
    s.mustEvaluate = ((SFBool)s.FieldSpec.get("mustEvaluate"));
    s.directOutput = ((SFBool)s.FieldSpec.get("direcOutput"));

    s.initImpl();
    try
    {
      s.setByteCodes((vrml.node.Script)this.scl.loadClass(this.scriptName).newInstance());
    }
    catch (Exception e)
    {
      System.out.println("Oops in " + this);
      e.printStackTrace();
    }
    return s;
  }

  public void setEventIn(String eventInName, Field f)
  {
    if (this.loader.debug) {
      System.out.println("Adding eventIn " + f.toStringId() + "\n    with name " + eventInName + "\n    to script node " + toStringId());
    }

    f.init(this, this.FieldSpec, 1, Field.baseName(eventInName));
  }

  public void setEventOut(String eventOutName, Field f)
  {
    if (this.loader.debug) {
      System.out.println("Adding eventOut " + f.toStringId() + "\n    with name " + eventOutName + "\n    to script node " + toStringId());
    }

    f.init(this, this.FieldSpec, 2, Field.baseName(eventOutName));
  }

  public void setField(String fieldName, Field f)
  {
    if (this.loader.debug) {
      System.out.println("Adding field " + f.toStringId() + "\n    with name " + fieldName + "\n    to script node " + toStringId());
    }

    f.init(this, this.FieldSpec, 0, Field.baseName(fieldName));
  }

  public Browser getBrowser()
  {
    return this.browser;
  }

  void initFields()
  {
    this.url.init(this, this.FieldSpec, 3, "url");
    this.directOutput.init(this, this.FieldSpec, 0, "directOutput");
    this.mustEvaluate.init(this, this.FieldSpec, 0, "mustEvaluate");
  }

  public vrml.BaseNode wrap()
  {
    return new vrml.node.Script(this);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Script
 * JD-Core Version:    0.6.0
 */