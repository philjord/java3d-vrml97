/*
 * $RCSfile: Proto.java,v $
 *
 *      @(#)Proto.java 1.15 99/03/24 15:33:26
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
 * $Date: 2005/02/03 23:07:00 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observer;
import java.util.Vector;

/**  Description of the Class */
public class Proto implements Namespace {

    String name;
    Loader loader;
    Vector nodes = new Vector();
    /**  Description of the Field */
    public Hashtable FieldSpec = new Hashtable(4);
    Hashtable defTable = new Hashtable();
    Vector routes = new Vector();
    ProtoInstance newInstance;

    /**
     *Constructor for the Proto object
     *
     *@param  loader Description of the Parameter
     *@param  initName Description of the Parameter
     */
    Proto(Loader loader, String initName) {
        this.loader = loader;
        name = initName;
        if (loader.debug) {
            System.out.println("PROTO " + initName);
        }
    }

    /**
     *  Gets the name attribute of the Proto object
     *
     *@return  The name value
     */
    String getName() {
        return name;
    }

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
        BaseNode orgNode = (BaseNode) defTable.get(defName);
        return new ProtoUseNode(loader, this, orgNode);
    }

    /**
     *  Adds a feature to the Route attribute of the Proto object
     *
     *@param  fromNode The feature to be added to the Route attribute
     *@param  fromEventOut The feature to be added to the Route attribute
     *@param  toNode The feature to be added to the Route attribute
     *@param  toEventIn The feature to be added to the Route attribute
     */
    public void addRoute(BaseNode fromNode, String fromEventOut,
            BaseNode toNode, String toEventIn) {
        if (fromNode instanceof ProtoUseNode) {
            fromNode = ((ProtoUseNode) fromNode).orgNode;
        }
        // TODO: test field name and type
        if (toNode instanceof ProtoUseNode) {
            toNode = ((ProtoUseNode) toNode).orgNode;
        }
        // TODO: test field name and type
        routes.addElement(new Route(fromNode, fromEventOut, toNode, toEventIn));
    }

    /**
     *  Description of the Method
     *
     *@param  nodeField Description of the Parameter
     *@param  protoFieldName Description of the Parameter
     *@return  Description of the Return Value
     */
    synchronized boolean setupIsMap(Field nodeField, String protoFieldName) {
        protoFieldName = Field.baseName(protoFieldName);
        Field protoField = (Field) FieldSpec.get(protoFieldName);
        if (protoField == null) {
            System.err.println("Proto.setupIsMap(): Can't find proto field \"" +
                    protoFieldName + "\"");
            return false;
        }
        nodeField.update(protoField);// set the initial value

        // Add routes for each way the proto field implies (timestamps will
        // avoid loops)
        if (protoField.isEventIn()) {
            if (nodeField.isEventIn()) {
                routes.addElement(
                        new Route(null, protoFieldName,
                        nodeField.ownerNode, nodeField.fieldName));
            }
            else {
                return false;
            }
        }
        if (protoField.isEventOut()) {
            if (nodeField.isEventOut()) {
                routes.addElement(
                        new Route(nodeField.ownerNode, nodeField.fieldName,
                        null, protoFieldName));
            }
            else {
                return false;
            }
        }
        // If the proto field is a plain field, build the EventIn route
        // to map the initial value from the parse.  There won't
        // be any further updates since you can't route to the field
        if (!protoField.isEventIn() && !protoField.isEventOut()) {
            routes.addElement(new Route(null, protoFieldName,
                    nodeField.ownerNode, nodeField.fieldName));
        }
        return true;
    }

    /**
     *  Description of the Method
     *
     *@param  node Description of the Parameter
     *@param  nodeEventName Description of the Parameter
     *@param  protoFieldName Description of the Parameter
     *@return  Description of the Return Value
     */
    public boolean setupEventIsMap(Node node, String nodeEventName,
            String protoFieldName) {
        protoFieldName = Field.baseName(protoFieldName);
        Field protoField = (Field) FieldSpec.get(protoFieldName);
        if (protoField == null) {
            System.err.println("Proto.setupEventIsMap: Can't find protoField " +
                    protoFieldName);
            return false;
        }
        nodeEventName = Field.baseName(nodeEventName);
        Field nodeField = (Field) node.FieldSpec.get(nodeEventName);
        if (nodeField == null) {
            System.err.println("Proto.setupEventIsMap: Can't find " +
                    "nodeEventName " +
                    nodeEventName);
            return false;
        }
        // Add routes for each way the proto field implies (timestamps will
        // avoid loops) (( but cause proto bugs if too fast ))
        if (protoField.isEventIn()) {
            if (nodeField.isEventIn()) {
                routes.addElement(
                        new Route(null, protoFieldName,
                        nodeField.ownerNode, nodeEventName));
            }
            else {
                return false;
            }
        }
        if (protoField.isEventOut()) {
            if (nodeField.isEventOut()) {
                routes.addElement(
                        new Route(nodeField.ownerNode, nodeEventName,
                        null, protoFieldName));
            }
            else {
                return false;
            }
        }
        return true;
    }

    /**
     *  Adds a feature to the Object attribute of the Proto object
     *
     *@param  node The feature to be added to the Object attribute
     */
    public void addObject(BaseNode node) {
        nodes.addElement(node);
    }


    // generate a copy of the proto, resolving linkages to the new instance
    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public ProtoInstance instance() {

        // create a new Proto and register it with the loader
        newInstance = new ProtoInstance(loader, name, this);
        if (loader.debug) {
            System.out.println("Proto.instance() begins newInstance is " +
                    newInstance.toStringId());
        }
        loader.beginProtoInstance(newInstance);

        // set up the new fields
        newInstance.initFields();

        // clone the trees attached to the Proto.  Def'd nodes will
        // register with the the new instance (see also SFNode.clone() and
        // MFNode.clone()).  Use'd nodes will lookup in the new instance (see
        // ProtoUseNode.clone()

        Group protoInstanceHandleabra = new Group(loader);
        protoInstanceHandleabra.initImpl();

        BaseNode[] children = new BaseNode[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            BaseNode baseNode = (BaseNode) nodes.elementAt(i);
            //System.out.println(baseNode.getType());
            if ((baseNode instanceof Script) && (!baseNode.implReady)) {
                baseNode.initImpl();
            }

            // since the sensor will be in an artificial group,
            // the group above may want the pickability information
            if (baseNode instanceof VrmlSensor) {
                newInstance.containsSensor = true;
                if (newInstance.sensors == null) {
                    newInstance.sensors = new java.util.Vector();
                }
                newInstance.sensors.addElement(baseNode);
            }

            BaseNode clone = (BaseNode) baseNode.clone();
            loader.cleanUp();

            //
            //if (i == 0) {
            //newInstance.instanceNode = clone;
            //}

            children[i] = clone;
            loader.registerClone(baseNode, clone);
        }

        // TBD
        // bug, now the protoInstanceHandleabra if it contains
        // a VrmlSensor must enable pick reporting on the parent
        // of the protoInstance, which can be discovered(?)
        // by Proto when it is asked for instance.

        protoInstanceHandleabra.addChildren.setValue(children);
        newInstance.instanceNode = protoInstanceHandleabra;
        if (newInstance.containsSensor) {
            protoInstanceHandleabra.implGroup.setPickable(true);
        }

        loader.cleanUp();
        newInstance.applyRoutes();
        if (loader.debug) {
            System.out.println("Proto.instance() returns instance with " +
                    "VRML tree:\n" +
                    newInstance.instanceNode);
        }
        loader.endProtoInstance();
        return newInstance;
    }

    /**
     *  Description of the Method
     *
     *@param  org Description of the Parameter
     *@param  clone Description of the Parameter
     */
    void registerClone(BaseNode org, BaseNode clone) {
        //System.out.println("registerClone org = " + org.toStringId() +
        //		" clone = " + clone.toStringId());
        newInstance.nodeMapping.put(org, clone);
    }

    /**
     *  Sets the eventIn attribute of the Proto object
     *
     *@param  eventInName The new eventIn value
     *@param  f The new eventIn value
     */
    public void setEventIn(String eventInName, Field f) {
        String fieldName = Field.baseName(eventInName);
        f.init(null, FieldSpec, Field.EVENT_IN, fieldName);
    }

    /**
     *  Sets the eventOut attribute of the Proto object
     *
     *@param  eventOutName The new eventOut value
     *@param  f The new eventOut value
     */
    public void setEventOut(String eventOutName, Field f) {
        String fieldName = Field.baseName(eventOutName);
        f.init(null, FieldSpec, Field.EVENT_OUT, fieldName);
    }

    /**
     *  Sets the field attribute of the Proto object
     *
     *@param  fieldName The new field value
     *@param  f The new field value
     */
    public void setField(String fieldName, Field f) {
        if (loader.debug) {
            System.out.println("Proto.setField(): adding field named " +
                    fieldName);
        }
        fieldName = Field.baseName(fieldName);
        f.init(null, FieldSpec, Field.FIELD, fieldName);
    }

    /**
     *  Sets the exposedField attribute of the Proto object
     *
     *@param  fieldName The new exposedField value
     *@param  f The new exposedField value
     */
    public void setExposedField(String fieldName, Field f) {
        fieldName = Field.baseName(fieldName);
        if (loader.debug) {
            System.out.println(
                    "Proto.setExposedField(): adding field named \"" + fieldName +
                    "\"");
        }
        f.init(null, FieldSpec, Field.EXPOSED_FIELD, fieldName);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toString() {
        String retval = "Proto " + name + " [\n";
        for (Enumeration e = FieldSpec.keys(); e.hasMoreElements(); ) {
            String fieldName = (String) e.nextElement();
            Field field = (Field) FieldSpec.get(fieldName);
            retval += "field ";
            String fieldType = field.getClass().getName() + " ";
            int start = fieldType.lastIndexOf(".") + 1;
            int end = fieldType.length();
            fieldType = fieldType.substring(start, end);
            retval += fieldType + " " + fieldName + " " + field;
        }
        retval += "]\n{\n";
        for (int i = 0; i < nodes.size(); i++) {
            BaseNode baseNode = (BaseNode) nodes.elementAt(i);
            retval += baseNode + "\n";
        }
        retval += "}\n";
        return retval;
    }
}

