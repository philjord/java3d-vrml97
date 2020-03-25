// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Parser.java

package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Vector;

// Referenced classes of package org.jdesktop.j3d.loaders.vrml97.impl:
//            ParseException, ProtoInstance, Proto, IndexedFaceSet, 
//            GroupBase, Node, SFBool, SFColor, 
//            SFFloat, SFInt32, SFNode, SFRotation, 
//            SFString, SFTime, SFVec2f, SFVec3f, 
//            MFColor, MFFloat, MFInt32, MFNode, 
//            MFRotation, MFString, MFTime, MFVec2f, 
//            MFVec3f, Script, BaseNode, ASCII_UCodeESC_CharStream, 
//            ParserTokenManager, Token, ParserConstants, Loader, 
//            Field, Time, Browser, RoutePrinter, 
//            IntBuf, FloatBuf, DoubleBuf

public class Parser
    implements ParserConstants
{

    Parser(Loader loader, Reader reader)
    {
        this(reader);
        this.loader = loader;
        initTables();
    }

    int currentLine()
    {
        Token curToken = getToken(0);
        return curToken.endLine;
    }

    void initTables()
    {
        loaderClass = new Class[1];
        loaderClass[0] = loader.getClass();
        loaderParam = new Object[1];
        loaderParam[0] = loader;
        vrmlNodes = new Hashtable();
        addVrmlNode("Anchor");
        addVrmlNode("Appearance");
        addVrmlNode("AudioClip");
        addVrmlNode("Background");
        addVrmlNode("Billboard");
        addVrmlNode("Box");
        addVrmlNode("Collision");
        addVrmlNode("Color");
        addVrmlNode("ColorInterpolator");
        addVrmlNode("Cone");
        addVrmlNode("Coordinate");
        addVrmlNode("CoordinateInterpolator");
        addVrmlNode("Cylinder");
        addVrmlNode("CylinderSensor");
        addVrmlNode("DirectionalLight");
        addVrmlNode("ElevationGrid");
        addVrmlNode("Extrusion");
        addVrmlNode("Fog");
        addVrmlNode("FontStyle");
        addVrmlNode("Group");
        addVrmlNode("ImageTexture");
        addVrmlNode("IndexedFaceSet");
        addVrmlNode("IndexedLineSet");
        addVrmlNode("Inline");
        addVrmlNode("LOD");
        addVrmlNode("Material");
        addVrmlNode("NavigationInfo");
        addVrmlNode("Normal");
        addVrmlNode("NormalInterpolator");
        addVrmlNode("OrientationInterpolator");
        addVrmlNode("PlaneSensor");
        addVrmlNode("PointLight");
        addVrmlNode("PointSet");
        addVrmlNode("PositionInterpolator");
        addVrmlNode("ProximitySensor");
        addVrmlNode("ScalarInterpolator");
        addVrmlNode("Shape");
        addVrmlNode("Sound");
        addVrmlNode("Sphere");
        addVrmlNode("SphereSensor");
        addVrmlNode("SpotLight");
        addVrmlNode("Switch");
        addVrmlNode("Text");
        addVrmlNode("TextureCoordinate");
        addVrmlNode("TextureTransform");
        addVrmlNode("TimeSensor");
        addVrmlNode("TouchSensor");
        addVrmlNode("Transform");
        addVrmlNode("Viewpoint");
        addVrmlNode("WorldInfo");
        fieldTypeCodes = new Hashtable();
        addField("SFBool", 31);
        addField("SFColor", 32);
        addField("SFFloat", 33);
        addField("SFInt32", 34);
        addField("SFNode", 35);
        addField("SFRotation", 36);
        addField("SFString", 37);
        addField("SFTime", 38);
        addField("SFVec2f", 39);
        addField("SFVec3f", 40);
        addField("MFColor", 41);
        addField("MFFloat", 42);
        addField("MFInt32", 43);
        addField("MFNode", 44);
        addField("MFRotation", 45);
        addField("MFString", 46);
        addField("MFTime", 47);
        addField("MFVec2f", 48);
        addField("MFVec3f", 49);
    }

    void addVrmlNode(String nodeTypeId)
    {
        String className = "org.jdesktop.j3d.loaders.vrml97.impl." + nodeTypeId;
        try
        {
            Class nodeClass = Class.forName(className);
            try
            {
                Constructor nodeConstructor = nodeClass.getConstructor(loaderClass);
                vrmlNodes.put(nodeTypeId, nodeConstructor);
            }
            catch(NoSuchMethodException e)
            {
                System.out.println("vrml97 Loader: Initialization error: Can't find constructor for VRML node: " + className);
            }
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("vrml97 Loader: Initialization error: Can't find class for VRML node: " + className);
        }
    }

    void addField(String fieldTypeId, int fieldCode)
    {
        String className = "org.jdesktop.j3d.loaders.vrml97.impl." + fieldTypeId;
        try
        {
            Class fieldClass = Class.forName(className);
            fieldTypeCodes.put(fieldClass, new Integer(fieldCode));
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("vrml97 Loader: Initialization error: Can't find class for VRML node: " + className);
        }
    }

    Constructor vrmlNodeConstructor(String nodeTypeId)
    {
        return (Constructor)vrmlNodes.get(nodeTypeId);
    }

    public final void Scene()
        throws ParseException
    {
label0:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[0] = jj_gen;
                break label0;

            case 18: // '\022'
            case 19: // '\023'
            case 20: // '\024'
            case 25: // '\031'
            case 26: // '\032'
            case 50: // '2'
                Statement();
                break;
            }
        while(true);
        jj_consume_token(0);
    }

    public final void Statement()
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 18: // '\022'
        case 19: // '\023'
        case 50: // '2'
            BaseNode node = NodeStatement();
            loader.addObject(node);
            break;

        case 26: // '\032'
            RouteStatement();
            break;

        case 20: // '\024'
        case 25: // '\031'
            ProtoStatement();
            break;

        default:
            jj_la1[1] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final BaseNode NodeStatement()
        throws ParseException
    {
        loader.cleanUp();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 50: // '2'
        {
            BaseNode node = Node();
            if(node instanceof ProtoInstance)
                node = ((ProtoInstance)node).instanceNode();
            return node;
        }

        case 18: // '\022'
        {
            jj_consume_token(18);
            String defName = NodeNameId();
            BaseNode node = Node();
            loader.namespaceDefine(defName, node);
            if(node instanceof ProtoInstance)
                node = ((ProtoInstance)node).instanceNode();
            return node;
        }

        case 19: // '\023'
        {
            jj_consume_token(19);
            String defName = NodeNameId();
            BaseNode node = loader.namespaceUse(defName);
            if(node instanceof ProtoInstance)
                node = ((ProtoInstance)node).instanceNode();
            node.registerUse(loader.scene);
            return node;
        }
        }
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }

    public final void ProtoStatement()
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 20: // '\024'
            ProtoDefinition();
            break;

        case 25: // '\031'
            ExternProtoDefinition();
            break;

        default:
            jj_la1[3] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void ProtoDefinition()
        throws ParseException
    {
        jj_consume_token(20);
        Proto proto = ProtoFromName();
        ProtoInterface(proto);
        jj_consume_token(10);
        ProtoBody(proto);
        jj_consume_token(11);
        loader.endProtoDefine();
    }

    public final Proto ProtoFromName()
        throws ParseException
    {
        String protoName = NodeNameId();
        Proto proto = new Proto(loader, protoName);
        loader.beginProtoDefine(proto);
        return proto;
    }

    public final void ProtoInterface(Proto proto)
        throws ParseException
    {
        jj_consume_token(12);
label0:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[4] = jj_gen;
                break label0;

            case 21: // '\025'
            case 22: // '\026'
            case 23: // '\027'
            case 24: // '\030'
                ProtoInterface_fields(proto);
                break;
            }
        while(true);
        jj_consume_token(13);
    }

    public final void ProtoInterface_fields(Proto proto)
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 21: // '\025'
            jj_consume_token(21);
            ProtoEventIn(proto);
            break;

        case 23: // '\027'
            jj_consume_token(23);
            ProtoField(proto);
            break;

        case 22: // '\026'
            jj_consume_token(22);
            ProtoEventOut(proto);
            break;

        case 24: // '\030'
            jj_consume_token(24);
            ProtoExposedField(proto);
            break;

        default:
            jj_la1[5] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void ProtoEventIn(Proto proto)
        throws ParseException
    {
        Field field = genEvent();
        String name = EventInId();
        proto.setEventIn(name, field);
    }

    public final void ProtoField(Proto proto)
        throws ParseException
    {
        Field f = genField();
        proto.setField(f.fieldName, f);
    }

    public final void ProtoEventOut(Proto proto)
        throws ParseException
    {
        Field field = genEvent();
        String name = EventOutId();
        proto.setEventOut(name, field);
    }

    public final void ProtoExposedField(Proto proto)
        throws ParseException
    {
        Field f = genField();
        proto.setExposedField(f.fieldName, f);
    }

    public final void ProtoBody(Proto proto)
        throws ParseException
    {
        BaseNode node = null;
label0:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[6] = jj_gen;
                break label0;

            case 18: // '\022'
            case 19: // '\023'
            case 20: // '\024'
            case 25: // '\031'
            case 26: // '\032'
            case 50: // '2'
                Statement();
                break;
            }
        while(true);
    }

    public final void ExternProtoDefinition()
        throws ParseException
    {
        Vector protoFields = new Vector();
        Vector urlList = new Vector();
        jj_consume_token(25);
        String protoName = NodeTypeId();
        jj_consume_token(12);
label0:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[7] = jj_gen;
                break label0;

            case 21: // '\025'
            case 22: // '\026'
            case 23: // '\027'
            case 24: // '\030'
                ExternProtoInterface(protoFields);
                break;
            }
        while(true);
        jj_consume_token(13);
label1:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[8] = jj_gen;
                break label1;

            case 9: // '\t'
                ExternProtoURL(urlList);
                break;
            }
        while(true);
        boolean found = false;
        int size = urlList.size();
        Exception lastException = null;
        for(int i = 0; !found && i < size;)
            try
            {
                String urlString = (String)urlList.elementAt(i);
                loader.loadProto(urlString, protoName, protoFields);
                found = true;
                continue;
            }
            catch(Exception e)
            {
                if(e instanceof ParseException)
                    throw (ParseException)e;
                lastException = e;
                i++;
            }

        if(!found && lastException != null)
        {
            if(loader.debug)
            {
                System.err.println("Exception reading EXTERNPROTO:");
                lastException.printStackTrace(System.err);
            }
            throw new ParseException("Exception " + lastException + " loading EXTERNPROTO");
        } else
        {
            return;
        }
    }

    public final void ExternProtoInterface(Vector protoFields)
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 21: // '\025'
        {
            jj_consume_token(21);
            Field f = genEvent();
            String name = EventInId();
            f.fieldName = name;
            protoFields.add(f);
            break;
        }

        case 23: // '\027'
        {
            jj_consume_token(23);
            Field f = genEvent();
            String name = FieldId();
            f.fieldName = name;
            protoFields.add(f);
            break;
        }

        case 22: // '\026'
        {
            jj_consume_token(22);
            Field f = genEvent();
            String name = EventOutId();
            f.fieldName = name;
            protoFields.add(f);
            break;
        }

        case 24: // '\030'
        {
            jj_consume_token(24);
            Field f = genEvent();
            String name = FieldId();
            f.fieldName = name;
            protoFields.add(f);
            break;
        }

        default:
        {
            jj_la1[9] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        }
    }

    public final void ExternProtoURL(Vector urlList)
        throws ParseException
    {
        String url = String();
        urlList.add(url);
    }

    public final void RouteStatement()
        throws ParseException
    {
        jj_consume_token(26);
        String fromName = NodeNameId();
        jj_consume_token(30);
        String fromField = EventOutId();
        jj_consume_token(27);
        String toName = NodeNameId();
        jj_consume_token(30);
        String toField = EventInId();
        BaseNode fromNode = loader.namespaceUse(fromName);
        BaseNode toNode = loader.namespaceUse(toName);
        if(fromNode == null || toNode == null)
        {
            throw new ParseException("To or From node on ROUTE not DEF'd");
        } else
        {
            loader.addRoute(fromNode, fromField, toNode, toField);
            return;
        }
    }

    public final BaseNode Node()
        throws ParseException
    {
        String nodeTypeId = NodeTypeId();
        BaseNode node;
        if(nodeTypeId.equals("Script"))
            node = Script();
        else
            node = NodeBody(nodeTypeId);
        if(loader.curProto == null)
            if(loader.timing)
            {
                double start = Time.getNow();
                node.initImpl();
                double elapsed = Time.getNow() - start;
                loader.nodeInitImpl += elapsed;
                if(node instanceof IndexedFaceSet)
                {
                    loader.ifsInit += elapsed;
                    loader.numIfs++;
                }
                if(node instanceof GroupBase)
                    loader.numGroups++;
            } else
            {
                node.initImpl();
            }
        if(loader.parserMarks)
        {
            Token curToken = getToken(0);
            if(curToken.beginLine > lastLine + 1000)
            {
                lastLine = curToken.beginLine;
                lastLine = lastLine - lastLine % 1000;
                System.out.println("Parser is at about line " + lastLine);
            }
        }
        return node;
    }

    public final Node NodeBody(String nodeTypeId)
        throws ParseException
    {
        Node node = null;
        Constructor nodeConstructor;
        Proto proto;
        if((nodeConstructor = vrmlNodeConstructor(nodeTypeId)) != null)
            try
            {
                node = (Node)nodeConstructor.newInstance(loaderParam);
            }
            catch(Exception e)
            {
                System.out.println("vrml97 Loader: error in constructor for " + nodeTypeId + ": " + e);
                if(e instanceof InvocationTargetException)
                {
                    System.out.println(((InvocationTargetException)e).getTargetException());
                    e.printStackTrace();
                }
            }
        else
        if((proto = loader.lookupProto(nodeTypeId)) != null)
        {
            ProtoInstance instance = proto.instance();
            if(loader.debug && loader.browser != null)
                loader.browser.routePrinter.printRoutes(instance);
            node = instance;
        } else
        {
            throw new ParseException("Unknown node type: " + nodeTypeId);
        }
        jj_consume_token(10);
label0:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[10] = jj_gen;
                break label0;

            case 20: // '\024'
            case 25: // '\031'
            case 26: // '\032'
            case 50: // '2'
                NodeBodyElement(node);
                break;
            }
        while(true);
        jj_consume_token(11);
        return node;
    }

    public final void NodeBodyElement(BaseNode node)
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 50: // '2'
            FieldStatement(node);
            break;

        case 26: // '\032'
            RouteStatement();
            break;

        case 20: // '\024'
        case 25: // '\031'
            ProtoStatement();
            break;

        default:
            jj_la1[11] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final Field FieldFromId(BaseNode node)
        throws ParseException
    {
        String fieldName = FieldId();
        Field field = node.getField(fieldName);
        return field;
    }

    public final void FieldStatement(BaseNode node)
        throws ParseException
    {
        Field field = FieldFromId(node);
        FieldInit(field);
    }

    public final void FieldInit(Field field)
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 29: // '\035'
            IsField(field);
            break;

        default:
            jj_la1[12] = jj_gen;
            FieldValue(field);
            break;
        }
    }

    public final void IsField(Field toField)
        throws ParseException
    {
        jj_consume_token(29);
        String fieldName = FieldId();
        Proto proto;
        if((proto = loader.curProto()) != null)
        {
            if(!proto.setupIsMap(toField, fieldName))
                throw new ParseException("Error setting up IS map in PROTO");
            else
                return;
        } else
        {
            throw new ParseException("IS statement outside PROTO");
        }
    }

    public final void FieldValue(Field field)
        throws ParseException
    {
        Integer fieldTypeCode = (Integer)fieldTypeCodes.get(field.getClass());
        if(fieldTypeCode == null)
            throw new ParseException("Parser Error: unexpected field type");
        switch(fieldTypeCode.intValue())
        {
        case 31: // '\037'
            _SFBool((SFBool)field);
            break;

        case 32: // ' '
            _SFColor((SFColor)field);
            break;

        case 33: // '!'
            _SFFloat((SFFloat)field);
            break;

        case 34: // '"'
            _SFInt32((SFInt32)field);
            break;

        case 35: // '#'
            _SFNode((SFNode)field);
            break;

        case 36: // '$'
            _SFRotation((SFRotation)field);
            break;

        case 37: // '%'
            _SFString((SFString)field);
            break;

        case 38: // '&'
            _SFTime((SFTime)field);
            break;

        case 39: // '\''
            _SFVec2f((SFVec2f)field);
            break;

        case 40: // '('
            _SFVec3f((SFVec3f)field);
            break;

        case 41: // ')'
            _MFColor((MFColor)field);
            break;

        case 42: // '*'
            _MFFloat((MFFloat)field);
            break;

        case 43: // '+'
            _MFInt32((MFInt32)field);
            break;

        case 44: // ','
            _MFNode((MFNode)field);
            break;

        case 45: // '-'
            _MFRotation((MFRotation)field);
            break;

        case 46: // '.'
            _MFString((MFString)field);
            break;

        case 47: // '/'
            _MFTime((MFTime)field);
            break;

        case 48: // '0'
            _MFVec2f((MFVec2f)field);
            break;

        case 49: // '1'
            _MFVec3f((MFVec3f)field);
            break;

        default:
            throw new ParseException("Internal error: Field type " + field.getClass() + " not found");
        }
        field.route();
    }

    public final Script Script()
        throws ParseException
    {
        Script script = new Script(loader);
        jj_consume_token(10);
label0:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[13] = jj_gen;
                break label0;

            case 20: // '\024'
            case 21: // '\025'
            case 22: // '\026'
            case 23: // '\027'
            case 25: // '\031'
            case 26: // '\032'
            case 50: // '2'
                ScriptBodyElement(script);
                break;
            }
        while(true);
        jj_consume_token(11);
        return script;
    }

    public final void ScriptBodyElement(Script script)
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 21: // '\025'
            jj_consume_token(21);
            ScriptEventIn(script);
            break;

        case 22: // '\026'
            jj_consume_token(22);
            ScriptEventOut(script);
            break;

        case 23: // '\027'
            jj_consume_token(23);
            ScriptField(script);
            break;

        case 20: // '\024'
        case 25: // '\031'
        case 26: // '\032'
        case 50: // '2'
            NodeBodyElement(script);
            break;

        default:
            jj_la1[14] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void ScriptEventIn(Script s)
        throws ParseException
    {
        Field f = genEvent();
        String name = EventInId();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 29: // '\035'
            IsField(f);
            break;

        default:
            jj_la1[15] = jj_gen;
            break;
        }
        s.setEventIn(name, f);
    }

    public final void ScriptField(Script s)
        throws ParseException
    {
        Field f = genField();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 29: // '\035'
            IsField(f);
            break;

        default:
            jj_la1[16] = jj_gen;
            break;
        }
        s.setField(f.fieldName, f);
    }

    public final void ScriptEventOut(Script s)
        throws ParseException
    {
        Field f = genEvent();
        String name = EventOutId();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 29: // '\035'
            IsField(f);
            break;

        default:
            jj_la1[17] = jj_gen;
            break;
        }
        s.setEventOut(name, f);
    }

    public final Field genEvent()
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 31: // '\037'
        {
            jj_consume_token(31);
            Field field = new SFBool(false);
            return field;
        }

        case 32: // ' '
        {
            jj_consume_token(32);
            Field field = new SFColor(0.0F, 1.0F, 0.0F);
            return field;
        }

        case 33: // '!'
        {
            jj_consume_token(33);
            Field field = new SFFloat(0.0F);
            return field;
        }

        case 34: // '"'
        {
            jj_consume_token(34);
            Field field = new SFInt32(0);
            return field;
        }

        case 35: // '#'
        {
            jj_consume_token(35);
            Field field = new SFNode();
            return field;
        }

        case 36: // '$'
        {
            jj_consume_token(36);
            Field field = new SFRotation();
            return field;
        }

        case 37: // '%'
        {
            jj_consume_token(37);
            Field field = new SFString();
            return field;
        }

        case 38: // '&'
        {
            jj_consume_token(38);
            Field field = new SFTime();
            return field;
        }

        case 39: // '\''
        {
            jj_consume_token(39);
            Field field = new SFVec2f();
            return field;
        }

        case 40: // '('
        {
            jj_consume_token(40);
            Field field = new SFVec3f();
            return field;
        }

        case 41: // ')'
        {
            jj_consume_token(41);
            Field field = new MFColor();
            return field;
        }

        case 42: // '*'
        {
            jj_consume_token(42);
            Field field = new MFFloat();
            return field;
        }

        case 43: // '+'
        {
            jj_consume_token(43);
            Field field = new MFInt32();
            return field;
        }

        case 44: // ','
        {
            jj_consume_token(44);
            Field field = new MFNode();
            return field;
        }

        case 45: // '-'
        {
            jj_consume_token(45);
            Field field = new MFRotation();
            return field;
        }

        case 46: // '.'
        {
            jj_consume_token(46);
            Field field = new MFString();
            return field;
        }

        case 47: // '/'
        {
            jj_consume_token(47);
            Field field = new MFTime();
            return field;
        }

        case 48: // '0'
        {
            jj_consume_token(48);
            Field field = new MFVec2f();
            return field;
        }

        case 49: // '1'
        {
            jj_consume_token(49);
            Field field = new MFVec3f();
            return field;
        }
        }
        jj_la1[18] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }

    public final Field genField()
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 31: // '\037'
        {
            jj_consume_token(31);
            String name = FieldId();
            Field field = new SFBool(false);
            field.fieldName = name;
            _SFBool((SFBool)field);
            return field;
        }

        case 32: // ' '
        {
            jj_consume_token(32);
            String name = FieldId();
            Field field = new SFColor(0.0F, 1.0F, 0.0F);
            field.fieldName = name;
            _SFColor((SFColor)field);
            return field;
        }

        case 33: // '!'
        {
            jj_consume_token(33);
            String name = FieldId();
            Field field = new SFFloat(0.0F);
            field.fieldName = name;
            _SFFloat((SFFloat)field);
            return field;
        }

        case 34: // '"'
        {
            jj_consume_token(34);
            String name = FieldId();
            Field field = new SFInt32(0);
            field.fieldName = name;
            _SFInt32((SFInt32)field);
            return field;
        }

        case 35: // '#'
        {
            jj_consume_token(35);
            String name = FieldId();
            Field field = new SFNode();
            field.fieldName = name;
            _SFNode((SFNode)field);
            return field;
        }

        case 36: // '$'
        {
            jj_consume_token(36);
            String name = FieldId();
            Field field = new SFRotation();
            field.fieldName = name;
            _SFRotation((SFRotation)field);
            return field;
        }

        case 37: // '%'
        {
            jj_consume_token(37);
            String name = FieldId();
            Field field = new SFString();
            field.fieldName = name;
            _SFString((SFString)field);
            return field;
        }

        case 38: // '&'
        {
            jj_consume_token(38);
            String name = FieldId();
            Field field = new SFTime();
            field.fieldName = name;
            _SFTime((SFTime)field);
            return field;
        }

        case 39: // '\''
        {
            jj_consume_token(39);
            String name = FieldId();
            Field field = new SFVec2f();
            field.fieldName = name;
            _SFVec2f((SFVec2f)field);
            return field;
        }

        case 40: // '('
        {
            jj_consume_token(40);
            String name = FieldId();
            Field field = new SFVec3f();
            field.fieldName = name;
            _SFVec3f((SFVec3f)field);
            return field;
        }

        case 41: // ')'
        {
            jj_consume_token(41);
            String name = FieldId();
            Field field = new MFColor();
            field.fieldName = name;
            _MFColor((MFColor)field);
            return field;
        }

        case 42: // '*'
        {
            jj_consume_token(42);
            String name = FieldId();
            Field field = new MFFloat();
            field.fieldName = name;
            _MFFloat((MFFloat)field);
            return field;
        }

        case 43: // '+'
        {
            jj_consume_token(43);
            String name = FieldId();
            Field field = new MFInt32();
            field.fieldName = name;
            _MFInt32((MFInt32)field);
            return field;
        }

        case 44: // ','
        {
            jj_consume_token(44);
            String name = FieldId();
            Field field = new MFNode();
            field.fieldName = name;
            _MFNode((MFNode)field);
            return field;
        }

        case 45: // '-'
        {
            jj_consume_token(45);
            String name = FieldId();
            Field field = new MFRotation();
            field.fieldName = name;
            _MFRotation((MFRotation)field);
            return field;
        }

        case 46: // '.'
        {
            jj_consume_token(46);
            String name = FieldId();
            Field field = new MFString();
            field.fieldName = name;
            _MFString((MFString)field);
            return field;
        }

        case 47: // '/'
        {
            jj_consume_token(47);
            String name = FieldId();
            Field field = new MFTime();
            field.fieldName = name;
            _MFTime((MFTime)field);
            return field;
        }

        case 48: // '0'
        {
            jj_consume_token(48);
            String name = FieldId();
            Field field = new MFVec2f();
            field.fieldName = name;
            _MFVec2f((MFVec2f)field);
            return field;
        }

        case 49: // '1'
        {
            jj_consume_token(49);
            String name = FieldId();
            Field field = new MFVec3f();
            field.fieldName = name;
            _MFVec3f((MFVec3f)field);
            return field;
        }
        }
        jj_la1[19] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }

    public final String NodeNameId()
        throws ParseException
    {
        String id = Id();
        return id;
    }

    public final String NodeTypeId()
        throws ParseException
    {
        String id = Id();
        return id;
    }

    public final String FieldId()
        throws ParseException
    {
        String id = Id();
        return id;
    }

    public final String EventOutId()
        throws ParseException
    {
        String id = Id();
        return id;
    }

    public final String EventInId()
        throws ParseException
    {
        String id = Id();
        return id;
    }

    public final String Id()
        throws ParseException
    {
        jj_consume_token(50);
        return new String(token.image);
    }

    public final String String()
        throws ParseException
    {
        jj_consume_token(9);
        return token.image.substring(1, token.image.length() - 1);
    }

    public final void MultString(Vector stringVec)
        throws ParseException
    {
        jj_consume_token(9);
        stringVec.addElement(new String(token.image.substring(1, token.image.length() - 1)));
    }

    public final int IntegerVal()
        throws ParseException
    {
        jj_consume_token(8);
        try
        {
            return Integer.parseInt(token.image);
        }
        catch(NumberFormatException ee)
        {
            throw new ParseException("Can't parse integer");
        }
    }

    public final float FloatVal()
        throws ParseException
    {
        jj_consume_token(8);
        try
        {
            return Float.parseFloat(token.image);
        }
        catch(NumberFormatException ee)
        {
            throw new ParseException("Can't parse float");
        }
    }

    public final double DoubleVal()
        throws ParseException
    {
        jj_consume_token(8);
        try
        {
            return Double.parseDouble(token.image);
        }
        catch(NumberFormatException ee)
        {
            throw new ParseException("Can't parse double");
        }
    }

    public final void MultIntegerVal()
        throws ParseException
    {
        int val = IntegerVal();
        loader.intBuf.add(val);
    }

    public final void MultFloatVal()
        throws ParseException
    {
        float val = FloatVal();
        loader.floatBuf.add(val);
    }

    public final void MultDoubleVal()
        throws ParseException
    {
        double val = DoubleVal();
        loader.doubleBuf.add(val);
    }

    public final void _MultNode(Vector nodeVec)
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 18: // '\022'
        case 19: // '\023'
        case 50: // '2'
            BaseNode val = NodeStatement();
            nodeVec.addElement(val);
            break;

        case 28: // '\034'
            jj_consume_token(28);
            nodeVec.addElement(null);
            break;

        default:
            jj_la1[20] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void _SFString(SFString field)
        throws ParseException
    {
        field.string = String();
    }

    public final void _MFString(MFString field)
        throws ParseException
    {
        Vector stringVec = new Vector();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 9: // '\t'
        {
            String val = String();
            String strings[] = new String[1];
            strings[0] = val;
            field.strings = strings;
            break;
        }

        case 12: // '\f'
        {
            jj_consume_token(12);
label0:
            do
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                default:
                    jj_la1[21] = jj_gen;
                    break label0;

                case 9: // '\t'
                    MultString(stringVec);
                    break;
                }
            while(true);
            jj_consume_token(13);
            String strings[] = new String[stringVec.size()];
            stringVec.copyInto(strings);
            field.strings = strings;
            break;
        }

        default:
        {
            jj_la1[22] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        }
    }

    public final void _SFBool(SFBool field)
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 14: // '\016'
            jj_consume_token(14);
            field.value = true;
            break;

        case 15: // '\017'
            jj_consume_token(15);
            field.value = true;
            break;

        case 16: // '\020'
            jj_consume_token(16);
            field.value = false;
            break;

        case 17: // '\021'
            jj_consume_token(17);
            field.value = false;
            break;

        default:
            jj_la1[23] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void _SFInt32(SFInt32 field)
        throws ParseException
    {
        field.value = IntegerVal();
    }

    public final void _SFFloat(SFFloat field)
        throws ParseException
    {
        field.value = FloatVal();
    }

    public final void _SFTime(SFTime field)
        throws ParseException
    {
        field.time = FloatVal();
    }

    public final void _SFRotation(SFRotation field)
        throws ParseException
    {
        float vec[] = new float[4];
        vec[0] = FloatVal();
        vec[1] = FloatVal();
        vec[2] = FloatVal();
        vec[3] = FloatVal();
        field.rot = vec;
    }

    public final void _SFVec2f(SFVec2f field)
        throws ParseException
    {
        float vec[] = new float[2];
        vec[0] = FloatVal();
        vec[1] = FloatVal();
        field.vec2f = vec;
    }

    public final void _SFVec3f(SFVec3f field)
        throws ParseException
    {
        float vec[] = new float[3];
        vec[0] = FloatVal();
        vec[1] = FloatVal();
        vec[2] = FloatVal();
        field.value = vec;
    }

    public final void _SFColor(SFColor field)
        throws ParseException
    {
        float color[] = new float[3];
        color[0] = FloatVal();
        color[1] = FloatVal();
        color[2] = FloatVal();
        field.color = color;
    }

    public final void _MFFloat(MFFloat field)
        throws ParseException
    {
        double start = 0.0D;
        loader.floatBuf.reset();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 8: // '\b'
        {
            float val = FloatVal();
            float values[] = new float[1];
            values[0] = val;
            field.mfloat = values;
            break;
        }

        case 12: // '\f'
        {
            jj_consume_token(12);
label0:
            do
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                default:
                    jj_la1[24] = jj_gen;
                    break label0;

                case 8: // '\b'
                    MultFloatVal();
                    break;
                }
            while(true);
            jj_consume_token(13);
            float values[] = new float[loader.floatBuf.size];
            System.arraycopy(loader.floatBuf.array, 0, values, 0, loader.floatBuf.size);
            field.mfloat = values;
            break;
        }

        default:
        {
            jj_la1[25] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        }
    }

    public final void _MFTime(MFTime field)
        throws ParseException
    {
        loader.doubleBuf.reset();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 8: // '\b'
        {
            double val = DoubleVal();
            double values[] = new double[1];
            values[0] = val;
            field.time = values;
            break;
        }

        case 12: // '\f'
        {
            jj_consume_token(12);
label0:
            do
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                default:
                    jj_la1[26] = jj_gen;
                    break label0;

                case 8: // '\b'
                    MultDoubleVal();
                    break;
                }
            while(true);
            jj_consume_token(13);
            double values[] = new double[loader.doubleBuf.size];
            System.arraycopy(loader.doubleBuf.array, 0, values, 0, loader.doubleBuf.size);
            field.time = values;
            break;
        }

        default:
        {
            jj_la1[27] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        }
    }

    public final void _MFVec2f(MFVec2f field)
        throws ParseException
    {
        double start = 0.0D;
        loader.floatBuf.reset();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 8: // '\b'
        {
            float x = FloatVal();
            float y = FloatVal();
            float values[] = new float[2];
            values[0] = x;
            values[1] = y;
            field.vals = values;
            break;
        }

        case 12: // '\f'
        {
            jj_consume_token(12);
label0:
            do
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                default:
                    jj_la1[28] = jj_gen;
                    break label0;

                case 8: // '\b'
                    MultFloatVal();
                    break;
                }
            while(true);
            jj_consume_token(13);
            int numVecs = loader.floatBuf.size / 2;
            float values[] = new float[numVecs * 2];
            System.arraycopy(loader.floatBuf.array, 0, values, 0, numVecs * 2);
            field.vals = values;
            break;
        }

        default:
        {
            jj_la1[29] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        }
    }

    public final void _MFVec3f(MFVec3f field)
        throws ParseException
    {
        double start = 0.0D;
        if(loader.timing)
            start = Time.getNow();
        loader.floatBuf.reset();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 8: // '\b'
            float x = FloatVal();
            float y = FloatVal();
            float z = FloatVal();
            field.checkSize(3, false);
            field.size = 3;
            field.value[0] = x;
            field.value[1] = y;
            field.value[2] = z;
            break;

        case 12: // '\f'
            jj_consume_token(12);
label0:
            do
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                default:
                    jj_la1[30] = jj_gen;
                    break label0;

                case 8: // '\b'
                    MultFloatVal();
                    break;
                }
            while(true);
            jj_consume_token(13);
            if(loader.timing)
            {
                double now = Time.getNow();
                loader.MFVec3fParse += now - start;
                start = now;
            }
            int numVecs = loader.floatBuf.size / 3;
            field.checkSize(numVecs * 3, false);
            System.arraycopy(loader.floatBuf.array, 0, field.value, 0, numVecs * 3);
            field.size = numVecs * 3;
            if(loader.timing)
                loader.MFVec3fCopy += Time.getNow() - start;
            break;

        default:
            jj_la1[31] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void _MFRotation(MFRotation field)
        throws ParseException
    {
        double start = 0.0D;
        loader.floatBuf.reset();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 8: // '\b'
        {
            float x = FloatVal();
            float y = FloatVal();
            float z = FloatVal();
            float angle = FloatVal();
            SFRotation values[] = new SFRotation[1];
            values[0] = new SFRotation(x, y, z, angle);
            field.rots = values;
            break;
        }

        case 12: // '\f'
        {
            jj_consume_token(12);
label0:
            do
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                default:
                    jj_la1[32] = jj_gen;
                    break label0;

                case 8: // '\b'
                    MultFloatVal();
                    break;
                }
            while(true);
            jj_consume_token(13);
            int numRots = loader.floatBuf.size / 4;
            SFRotation values[] = new SFRotation[numRots];
            for(int i = 0; i < numRots; i++)
            {
                float x = loader.floatBuf.array[i * 4 + 0];
                float y = loader.floatBuf.array[i * 4 + 1];
                float z = loader.floatBuf.array[i * 4 + 2];
                float angle = loader.floatBuf.array[i * 4 + 3];
                values[i] = new SFRotation(x, y, z, angle);
            }

            field.rots = values;
            break;
        }

        default:
        {
            jj_la1[33] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        }
    }

    public final void _MFColor(MFColor field)
        throws ParseException
    {
        double start = 0.0D;
        loader.floatBuf.reset();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 8: // '\b'
        {
            float r = FloatVal();
            float g = FloatVal();
            float b = FloatVal();
            float values[] = new float[3];
            values[0] = r;
            values[1] = g;
            values[2] = b;
            field.vals = values;
            break;
        }

        case 12: // '\f'
        {
            jj_consume_token(12);
label0:
            do
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                default:
                    jj_la1[34] = jj_gen;
                    break label0;

                case 8: // '\b'
                    MultFloatVal();
                    break;
                }
            while(true);
            jj_consume_token(13);
            int numVecs = loader.floatBuf.size / 3;
            float values[] = new float[numVecs * 3];
            System.arraycopy(loader.floatBuf.array, 0, values, 0, numVecs * 3);
            field.vals = values;
            break;
        }

        default:
        {
            jj_la1[35] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        }
    }

    public final void _MFInt32(MFInt32 field)
        throws ParseException
    {
        double start = 0.0D;
        if(loader.timing)
            start = Time.getNow();
        loader.intBuf.reset();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 8: // '\b'
            int val = IntegerVal();
            field.checkSize(1, false);
            field.value[0] = val;
            field.size = 1;
            break;

        case 12: // '\f'
            jj_consume_token(12);
label0:
            do
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                default:
                    jj_la1[36] = jj_gen;
                    break label0;

                case 8: // '\b'
                    MultIntegerVal();
                    break;
                }
            while(true);
            jj_consume_token(13);
            if(loader.intBuf.size <= 0)
                break;
            if(loader.timing)
            {
                double now = Time.getNow();
                loader.MFInt32Parse += now - start;
                start = now;
            }
            field.checkSize(loader.intBuf.size, false);
            field.size = loader.intBuf.size;
            System.arraycopy(loader.intBuf.array, 0, field.value, 0, loader.intBuf.size);
            loader.intBuf.reset();
            if(loader.timing)
                loader.MFInt32Copy += Time.getNow() - start;
            break;

        default:
            jj_la1[37] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void _SFNode(SFNode field)
        throws ParseException
    {
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 18: // '\022'
        case 19: // '\023'
        case 50: // '2'
            field.node = NodeStatement();
            break;

        case 28: // '\034'
            jj_consume_token(28);
            field.node = null;
            break;

        default:
            jj_la1[38] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void _MFNode(MFNode field)
        throws ParseException
    {
        Vector nodeVec = new Vector();
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 28: // '\034'
        {
            jj_consume_token(28);
            field.nodes = new BaseNode[0];
            break;
        }

        case 18: // '\022'
        case 19: // '\023'
        case 50: // '2'
        {
            BaseNode single = NodeStatement();
            BaseNode values[] = new BaseNode[1];
            values[0] = single;
            field.nodes = values;
            break;
        }

        case 12: // '\f'
        {
            jj_consume_token(12);
label0:
            do
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                default:
                    jj_la1[39] = jj_gen;
                    break label0;

                case 18: // '\022'
                case 19: // '\023'
                case 28: // '\034'
                case 50: // '2'
                    _MultNode(nodeVec);
                    break;
                }
            while(true);
            jj_consume_token(13);
            BaseNode values[] = new BaseNode[nodeVec.size()];
            for(int i = 0; i < nodeVec.size(); i++)
                values[i] = (BaseNode)nodeVec.elementAt(i);

            field.nodes = values;
            break;
        }

        default:
        {
            jj_la1[40] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        }
    }

    public Parser(InputStream stream)
    {
        lastLine = 0;
        vrmlNodes = null;
        fieldTypeCodes = null;
        jj_la1 = new int[41];
        jj_expentries = new Vector();
        jj_kind = -1;
        jj_input_stream = new ASCII_UCodeESC_CharStream(stream, 1, 1);
        token_source = new ParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 41; i++)
            jj_la1[i] = -1;

    }

    public void ReInit(InputStream stream)
    {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 41; i++)
            jj_la1[i] = -1;

    }

    public Parser(Reader stream)
    {
        lastLine = 0;
        vrmlNodes = null;
        fieldTypeCodes = null;
        jj_la1 = new int[41];
        jj_expentries = new Vector();
        jj_kind = -1;
        jj_input_stream = new ASCII_UCodeESC_CharStream(stream, 1, 1);
        token_source = new ParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 41; i++)
            jj_la1[i] = -1;

    }

    public void ReInit(Reader stream)
    {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 41; i++)
            jj_la1[i] = -1;

    }

    public Parser(ParserTokenManager tm)
    {
        lastLine = 0;
        vrmlNodes = null;
        fieldTypeCodes = null;
        jj_la1 = new int[41];
        jj_expentries = new Vector();
        jj_kind = -1;
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 41; i++)
            jj_la1[i] = -1;

    }

    public void ReInit(ParserTokenManager tm)
    {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 41; i++)
            jj_la1[i] = -1;

    }

    private final Token jj_consume_token(int kind)
        throws ParseException
    {
        Token oldToken;
        if((oldToken = token).next != null)
            token = token.next;
        else
            token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if(token.kind == kind)
        {
            jj_gen++;
            return token;
        } else
        {
            token = oldToken;
            jj_kind = kind;
            throw generateParseException();
        }
    }

    public final Token getNextToken()
    {
        if(token.next != null)
            token = token.next;
        else
            token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    public final Token getToken(int index)
    {
        Token t = token;
        for(int i = 0; i < index; i++)
            if(t.next != null)
                t = t.next;
            else
                t = t.next = token_source.getNextToken();

        return t;
    }

    private final int jj_ntk()
    {
        if((jj_nt = token.next) == null)
            return jj_ntk = (token.next = token_source.getNextToken()).kind;
        else
            return jj_ntk = jj_nt.kind;
    }

    public final ParseException generateParseException()
    {
        jj_expentries.removeAllElements();
        boolean la1tokens[] = new boolean[53];
        for(int i = 0; i < 53; i++)
            la1tokens[i] = false;

        if(jj_kind >= 0)
        {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for(int i = 0; i < 41; i++)
        {
            if(jj_la1[i] != jj_gen)
                continue;
            for(int j = 0; j < 32; j++)
            {
                if((jj_la1_0[i] & 1 << j) != 0)
                    la1tokens[j] = true;
                if((jj_la1_1[i] & 1 << j) != 0)
                    la1tokens[32 + j] = true;
            }

        }

        for(int i = 0; i < 53; i++)
            if(la1tokens[i])
            {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.addElement(jj_expentry);
            }

        int exptokseq[][] = new int[jj_expentries.size()][];
        for(int i = 0; i < jj_expentries.size(); i++)
            exptokseq[i] = (int[])(int[])jj_expentries.elementAt(i);

        return new ParseException(token, exptokseq, tokenImage);
    }

    public final void enable_tracing()
    {
    }

    public final void disable_tracing()
    {
    }

    Loader loader;
    int lastLine;
    Object loaderParam[];
    Class loaderClass[];
    Hashtable vrmlNodes;
    Hashtable fieldTypeCodes;
    public ParserTokenManager token_source;
    ASCII_UCodeESC_CharStream jj_input_stream;
    public Token token;
    public Token jj_nt;
    private int jj_ntk;
    private int jj_gen;
    private final int jj_la1[];
    private final int jj_la1_0[] = {
        0x61c0000, 0x61c0000, 0xc0000, 0x2100000, 0x1e00000, 0x1e00000, 0x61c0000, 0x1e00000, 512, 0x1e00000, 
        0x6100000, 0x6100000, 0x20000000, 0x6f00000, 0x6f00000, 0x20000000, 0x20000000, 0x20000000, 0x80000000, 0x80000000, 
        0x100c0000, 512, 4608, 0x3c000, 256, 4352, 256, 4352, 256, 4352, 
        256, 4352, 256, 4352, 256, 4352, 256, 4352, 0x100c0000, 0x100c0000, 
        0x100c1000
    };
    private final int jj_la1_1[] = {
        0x40000, 0x40000, 0x40000, 0, 0, 0, 0x40000, 0, 0, 0, 
        0x40000, 0x40000, 0, 0x40000, 0x40000, 0, 0, 0, 0x3ffff, 0x3ffff, 
        0x40000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0x40000, 0x40000, 
        0x40000
    };
    private Vector jj_expentries;
    private int jj_expentry[];
    private int jj_kind;
}
