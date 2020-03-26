/*
 * $RCSfile: RoutePrinter.java,v $
 *
 *      @(#)RoutePrinter.java 1.6 98/11/05 20:34:59
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
import java.util.Vector;

/**  Description of the Class */
class RoutePrinter {
    Vector fieldsVisited = new Vector();
    Vector fieldsToVisit = new Vector();
    Vector nodesVisited = new Vector();
    Vector nodesToVisit = new Vector();
    String indent;

    /**
     *  Description of the Method
     *
     *@param  in Description of the Parameter
     *@param  length Description of the Parameter
     *@return  Description of the Return Value
     */
    String fixedLengthString(String in, int length) {
        while (in.length() < length) {
            in = in + " ";
        }
        return in;
    }

    /**
     *  Description of the Method
     *
     *@param  curField Description of the Parameter
     */
    private void printRoutesField(Field curField) {
        if (fieldsVisited.contains(curField)) {
            return;
        }
        fieldsVisited.addElement(curField);
        fieldsToVisit.removeElement(curField);
        if ((curField.connections != null) &&
                (curField.connections.size() != 0)) {
            System.out.println("Field " + curField.fieldName + ": " + curField);
            System.out.println(" owned by " + curField.ownerNode);
            System.out.println(" connects to " +
                    curField.connections.size() + " field(s):");
            for (Enumeration e = curField.connections.elements();
                    e.hasMoreElements(); ) {
                Field newField = (Field) e.nextElement();
                System.out.println("    " +
                        fixedLengthString(newField.fieldName, 20) + " " +
                        newField + "\n        owned by " +
                        newField.ownerNode.defName + " " + newField.ownerNode);
                if (!fieldsVisited.contains(newField) &&
                        !fieldsToVisit.contains(newField)) {
                    fieldsToVisit.addElement(newField);
                }
            }
        }
        else {
            //System.out.println("Field " + curField.fieldName + ": " +
            //    curField);
            //System.out.println(" owned by " + curField.ownerNode);
            //System.out.println(" has no connections");
        }
        if (curField.ownerNode != null) {
            printRoutesNode(curField.ownerNode);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  curNode Description of the Parameter
     */
    private void printRoutesNode(BaseNode curNode) {
        if (nodesVisited.contains(curNode)) {
            return;
        }
        nodesVisited.addElement(curNode);
        nodesToVisit.removeElement(curNode);
        System.out.println("Node " + curNode + " named " + curNode.defName +
                "\n  has fields: ");
        Hashtable fieldSpec = null;
        if (curNode instanceof Node) {
            fieldSpec = ((Node) curNode).FieldSpec;
        }
        else if (curNode instanceof Script) {
            fieldSpec = ((Node) curNode).FieldSpec;
        }
        Vector printedFields = new Vector();
        for (Enumeration e = fieldSpec.elements();
                e.hasMoreElements(); ) {
            Field newField = (Field) e.nextElement();
            if (newField instanceof ConstField) {
                newField = ((ConstField) newField).ownerField;
            }
            if (!printedFields.contains(newField)) {
                printedFields.addElement(newField);
                System.out.println("    " +
                        fixedLengthString(newField.fieldName, 20) + " " +
                        newField);
                if (!fieldsVisited.contains(newField) &&
                        !fieldsToVisit.contains(newField)) {
                    fieldsToVisit.addElement(newField);
                }
            }
        }
    }

    /**  Description of the Method */
    private void reset() {
        fieldsVisited.removeAllElements();
        nodesVisited.removeAllElements();
        nodesToVisit.removeAllElements();
        fieldsToVisit.removeAllElements();
    }

    /**
     *  Description of the Method
     *
     *@param  startField Description of the Parameter
     */
    void printRoutes(Field startField) {
        fieldsToVisit.addElement(startField);
        doIt();
    }

    /**
     *  Description of the Method
     *
     *@param  startNode Description of the Parameter
     */
    void printRoutes(BaseNode startNode) {
        nodesToVisit.addElement(startNode);
        doIt();
    }

    /**  Description of the Method */
    private void doIt() {
        while ((fieldsToVisit.size() > 0) || (nodesToVisit.size() > 0)) {
            for (Enumeration e = fieldsToVisit.elements();
                    e.hasMoreElements(); ) {
                Field newField = (Field) e.nextElement();
                printRoutesField(newField);
            }
            for (Enumeration e = nodesToVisit.elements();
                    e.hasMoreElements(); ) {
                BaseNode newNode = (BaseNode) e.nextElement();
                printRoutesNode(newNode);
            }
        }
    }

}

