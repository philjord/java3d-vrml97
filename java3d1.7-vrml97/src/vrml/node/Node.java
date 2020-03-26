/*
 * $RCSfile: Node.java,v $
 *
 *      @(#)Node.java 1.19 98/11/05 20:41:20
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
 * $Date: 2005/02/03 23:07:16 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package vrml.node;

import vrml.*;

/**
 * This is the basic interface for all VRML nodes.  All VRML fields are
 * accessed through the field methods.  New nodes are created through the
 * Browser using the load and create methods.
 * <p>
 * Subclasses of Node exist only when the subclass give additional
 * functionality, such as exposing additional parts of the Java3D implemenation
 * for the node.
 *
 *@see  vrml.Browser
 */
public class Node extends vrml.BaseNode {
    /**  Description of the Field */
    protected org.jdesktop.j3d.loaders.vrml97.impl.Node implNode;

    /**
     * This is an internal constructor. Nodes are only created by the
     * Browser or Loader.
     *
     *@param  init Description of the Parameter
     */
    public Node(org.jdesktop.j3d.loaders.vrml97.impl.Node init) {
        super(init);
        implNode = init;
    }

    /**
     * Returns an ExposedField field
     *
     *@param  fieldName Description of the Parameter
     *@return  The exposedField value
     *@exception  InvalidExposedFieldException Description of the Exception
     */
    public final Field getExposedField(String fieldName)
             throws InvalidExposedFieldException {
        org.jdesktop.j3d.loaders.vrml97.impl.Field implField =
                implNode.getExposedField(fieldName);
        return implField.wrap();
    }

    /**
     * Returns an EventIn field
     *
     *@param  eventInName Description of the Parameter
     *@return  The eventIn value
     *@exception  InvalidEventInException Description of the Exception
     */
    public final Field getEventIn(String eventInName)
             throws InvalidEventInException {
        org.jdesktop.j3d.loaders.vrml97.impl.Field implField =
                implNode.getEventIn(eventInName);
        return implField.wrap();
    }


    /**
     * Returns an EventOut field
     *
     *@param  eventOutName Description of the Parameter
     *@return  The eventOut value
     *@exception  InvalidEventOutException Description of the Exception
     */
    public ConstField getEventOut(String eventOutName)
             throws InvalidEventOutException {
        org.jdesktop.j3d.loaders.vrml97.impl.ConstField implField =
                implNode.getEventOut(eventOutName);
        return (ConstField) implField.wrap();
    }

    /**
     * Returns the VRML class type for the node.
     *
     *@return  The type value
     */
    public String getType() {
        return implNode.getType();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return ((org.jdesktop.j3d.loaders.vrml97.impl.Node) implNode.clone()).wrap();
    }

    /**
     * Returns the Browser which created this node.
     *
     *@return  The browser value
     */
    public vrml.Browser getBrowser() {
        return new Browser(implNode.getBrowser());
    }

    /**
     *  Gets the impl attribute of the Node object
     *
     *@return  The impl value
     */
    protected org.jdesktop.j3d.loaders.vrml97.impl.BaseNode getImpl() {
        return implNode;
    }

}

