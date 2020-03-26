/*
 * $RCSfile: Field.java,v $
 *
 *      @(#)Field.java 1.25 99/03/09 16:36:15
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
 * $Date: 2005/02/03 23:06:55 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jdesktop.j3d.loaders.vrml97.impl.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**  Description of the Class */
public abstract class Field {

    String fieldName = new String("?");
    BaseNode ownerNode = null;
    double lastUpdate = -1.0;// to allow one update only in loader case
    Vector connections = null;
    /**  Description of the Field */
    public ConstField constField;
    /**  Description of the Field */
    public int fieldType = EXPOSED_FIELD;// least restrictive for transition

    // Field types
    final static int FIELD = 0;
    final static int EVENT_IN = 1;
    final static int EVENT_OUT = 2;
    final static int EXPOSED_FIELD = 3;

    // debugging
    final static boolean printRoutes = false;

    /**Constructor for the Field object */
    Field() { }

    // should be called for all fields in Node in initFields()
    /**
     *  Description of the Method
     *
     *@param  node Description of the Parameter
     *@param  fieldSpec Description of the Parameter
     *@param  fieldType Description of the Parameter
     *@param  fieldName Description of the Parameter
     */
    void init(BaseNode node, Hashtable fieldSpec, int fieldType,
            String fieldName) {
        this.ownerNode = node;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        if (fieldSpec != null) {
            fieldSpec.put(fieldName, this);
        }
    }

    /**
     *  Gets the eventOut attribute of the Field object
     *
     *@return  The eventOut value
     */
    boolean isEventOut() {
        return ((fieldType & EVENT_OUT) != 0);
    }

    /**
     *  Gets the eventIn attribute of the Field object
     *
     *@return  The eventIn value
     */
    boolean isEventIn() {
        return ((fieldType & EVENT_IN) != 0);
    }

    // to implement ROUTE
    // Node: fields used to be connected using ConstField->Field.  Changed
    // to do this here so that all the timing logic is together.  ConstFields
    // are now just wrappers that limit access to Fields.
    /**  Description of the Method */
    synchronized void route() {
        double eventTime;
        if (ownerNode != null) {
            if (ownerNode.browser != null) {
                eventTime = ownerNode.browser.beginRoute();
            }
            else {
                // if no browser, then time is always 0.0.  lastUpdate is
                // initialized to -1.0 to allow one update but no more.
                eventTime = 0.0;
            }
            if (printRoutes) {
                System.out.println("Owner of field " + this.toStringId() +
                        " is " + ownerNode.toStringId());
                System.out.println("Field type is " + fieldType);
            }
            if (isEventIn() && ownerNode.implReady) {
                if (printRoutes) {
                    System.out.println("Calling notifyMethod() on owner");
                }
                ownerNode.notifyMethod(fieldName, eventTime);
            }
        }
        else {
            eventTime = Time.getNow();
        }
        // Test time to avoid sending two events with same timestamp from
        // a EventOut
        if (lastUpdate != eventTime) {
            lastUpdate = eventTime;
            if (connections != null) {
                for (Enumeration e = connections.elements();
                        e.hasMoreElements(); ) {
                    Field routeField = (Field) e.nextElement();
                    // Test time to avoid sending two event with same
                    // timestamps to an EventIn
                    if (routeField.lastUpdate != eventTime) {
                        if (printRoutes) {
                            System.out.println("Field.route: field " +
                                    fieldName + " " + this + "\n    is updating " +
                                    routeField.fieldName + " " + routeField);
                        }
                        routeField.update(this);
                    }
                    else {
                        if (printRoutes) {
                            System.out.println("Field.route: Not updating " +
                                    "routeField " + routeField.fieldName + " " +
                                    routeField + "\n     lastUpdate (" +
                                    ownerNode.browser.relativeTime(
                                    routeField.lastUpdate) +
                                    ") == eventTime");
                        }
                    }
                }
            }
        }
        else {
            if (printRoutes) {
                System.out.println("After update, field " + fieldName + " " +
                        this + "\n     lastUpdate (" +
                        ownerNode.browser.relativeTime(lastUpdate) +
                        ") == eventTime, not routing");
            }
        }
        if ((ownerNode != null) && (ownerNode.browser != null)) {
            ownerNode.browser.endRoute();
        }
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    void connectToField(Field field) {
        if (connections == null) {
            connections = new Vector();
        }
        if (!connections.contains(field)) {
            connections.addElement(field);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    void deleteConnection(Field field) {
        connections.removeElement(field);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    abstract void update(Field field);

    // should initialize constField
    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    abstract ConstField constify();

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public abstract Object clone();

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public abstract vrml.Field wrap();

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toStringId() {
        return this.getClass().getName() + "@" +
                Integer.toHexString(this.hashCode());
    }

    // This should be done only to nodes that are j3d impls that
    // benefit from compile. Eg, audioclip now has to if check each
    // simtick for route_ prefix but it never uses the prefix.
    // possibly an interface called RoutePrefixable ? If ownernode
    // instance of RoutePrefixable then notifyMethod.
    /**  Description of the Method */
    public void markWriteable() {
        ownerNode.notifyMethod("route_" + fieldName, 0.0d);
    }

    // remove the "set_" or "_changed" from a fieldName
    /**
     *  Description of the Method
     *
     *@param  fieldName Description of the Parameter
     *@return  Description of the Return Value
     */
    public static String baseName(String fieldName) {
        String newName = fieldName;
        if (fieldName.startsWith("set_")) {
            newName = fieldName.substring(4);
            //System.out.println("Field.baseName() Removed set_: old " +
            //	fieldName + " new: " + newName);
        }
        if (fieldName.endsWith("_changed")) {
            newName = fieldName.substring(0, fieldName.indexOf("_changed"));
            //System.out.println("Field.baseName() Removed _changed: old " +
            //	fieldName + " new: " + newName);
        }
        return newName;
    }
}

