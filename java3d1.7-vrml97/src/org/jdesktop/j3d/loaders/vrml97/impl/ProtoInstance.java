/*
 * $RCSfile: ProtoInstance.java,v $
 *
 *      @(#)ProtoInstance.java 1.15 99/03/24 15:33:33
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
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**  Description of the Class */
public class ProtoInstance extends Node implements Namespace {

    String name;
    Proto proto;
    Hashtable defTable = new Hashtable();
    BaseNode instanceNode;
    Vector routes;
    Hashtable fieldMapping = new Hashtable();
    Hashtable nodeMapping = new Hashtable();
    // need to get the updateParent() correct
    boolean containsSensor = false;
    Vector sensors;

    /**
     *Constructor for the ProtoInstance object
     *
     *@param  loader Description of the Parameter
     *@param  name Description of the Parameter
     *@param  proto Description of the Parameter
     */
    ProtoInstance(Loader loader, String name, Proto proto) {
        super(loader);
        this.name = name;
        this.proto = proto;
        this.routes = proto.routes;
    }

    /**
     *  Adds a feature to the Field attribute of the ProtoInstance object
     *
     *@param  field The feature to be added to the Field attribute
     *@return  Description of the Return Value
     */
    Field addField(Field field) {
        Field newField;
        if (!fieldMapping.containsKey(field)) {
            newField = (Field) field.clone();
            fieldMapping.put(field, newField);
            newField.init(this, FieldSpec, field.fieldType, field.fieldName);
        }
        else {
            newField = (Field) fieldMapping.get(field);
        }
        return newField;
    }

    /**  Description of the Method */
    void initFields() {
        // clone the Proto's fields and event mappings
        // handle the fields first
        for (Enumeration e = proto.FieldSpec.keys(); e.hasMoreElements(); ) {
            String fieldName = (String) e.nextElement();
            Field field = (Field) proto.FieldSpec.get(fieldName);
            //System.err.println("ProtoInstance.initFields(): got field " +
            //  "named " + fieldName + " with value " + field);
            Field newField;
            if ((field instanceof ConstField)) {
                newField = addField(((ConstField) field).ownerField);
                FieldSpec.put(fieldName, newField.constify());
            }
            else {
                newField = addField(field);
                FieldSpec.put(fieldName, newField);
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@param  defName Description of the Parameter
     *@param  node Description of the Parameter
     */
    public void define(String defName, BaseNode node) {
        //System.out.println("ProtoInstance.define() name: " + defName +
        //	"value: " + node);
        defTable.put(defName, node);
    }

    /**
     *  Description of the Method
     *
     *@param  defName Description of the Parameter
     *@return  Description of the Return Value
     */
    public BaseNode use(String defName) {
        BaseNode retval = (BaseNode) defTable.get(defName);
        //System.out.println("ProtoInstance.use(" + defName + ") " +
        //	" returned " + " node " + retval.toStringId());
        retval.registerUse(loader.scene);
        return retval;
    }

    /**
     *  Description of the Method
     *
     *@param  node Description of the Parameter
     *@return  Description of the Return Value
     */
    BaseNode updateNode(BaseNode node) {
        BaseNode retval;
        if (node == null) {
            return this;
        }
        retval = (BaseNode) nodeMapping.get(node);
        //System.out.println("some strange method called updateNode "+node);
        //System.out.println("mapped to "+retval);
        if (retval == null) {
            System.err.println("ProtoInstance: Unable to update node " +
                    "reference: " + node.toStringId());
        }
        return retval;
    }

    /**  Description of the Method */
    void applyRoutes() {
        // apply the routes, updating the nodes
        //System.out.println("ProtoInstance: applying routes:");
        for (Enumeration e = routes.elements(); e.hasMoreElements(); ) {
            Route route = (Route) e.nextElement();
            BaseNode fromNode;
            BaseNode toNode;
            fromNode = updateNode(route.fromNode);
            toNode = updateNode(route.toNode);
            //System.out.println("Add route: " + fromNode + " . " +
            //route.fromField + "\n   to " + toNode + " . " + route.toField);

            // bug: after fixing 4221802 the multinoded protodef somehow
            // a "?" field was trying to get addRouted
            if (!route.fromField.equals("?") &&
                    !route.toField.equals("?")) {
                loader.connect(fromNode, route.fromField, toNode,
                        route.toField, false);
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    BaseNode instanceNode() {
        return instanceNode;
    }

    /**
     *  Gets the implNode attribute of the ProtoInstance object
     *
     *@return  The implNode value
     */
    public org.jogamp.java3d.Node getImplNode() {
        return instanceNode.getImplNode();
    }

    // dummy methods to satisfy BaseNode interface
    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
    }

    /**
     *  Gets the type attribute of the ProtoInstance object
     *
     *@return  The type value
     */
    public String getType() {
        return name;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        System.err.println("Warning: clone called on a ProtoInstance!");
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toStringBody() {
        return "PROTO " + name + " {\n" +  /* TODO: fields here */"}\n";
    }
}

