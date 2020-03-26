/*
 *      @(#)ProtoUseNode.java 1.11 98/11/05 20:34:56
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

/**  Description of the Class */
public class ProtoUseNode extends BaseNode {
    BaseNode orgNode;
    Proto proto;

    /**
     *Constructor for the ProtoUseNode object
     *
     *@param  loader Description of the Parameter
     *@param  proto Description of the Parameter
     *@param  orgNode Description of the Parameter
     */
    ProtoUseNode(Loader loader, Proto proto, BaseNode orgNode) {
        super(loader);
        this.proto = proto;
        this.orgNode = orgNode;
        //System.out.println("Created ProtoUseNode " + this +
        //   " with orgNode= " + orgNode);
    }

    /**
     *  Gets the type attribute of the ProtoUseNode object
     *
     *@return  The type value
     */
    public String getType() {
        return "internal: ProtoUseNode";
    }

    /**
     *  Description of the Method
     *
     *@param  s Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String s, double time) { }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.BaseNode wrap() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        BaseNode newNode;
        newNode = (BaseNode) proto.newInstance.nodeMapping.get(orgNode);
        if (loader.debug) {
            System.out.println("ProtoUseNode.clone(): orgNode=" + orgNode +
                    " new=" + newNode);
        }
        if (newNode == null) {
            System.err.println("ProtoUseNode: new node not found!");
        }
        return newNode;
    }
    // should not be called on ProtoUseNode
    /**
     *  Gets the field attribute of the ProtoUseNode object
     *
     *@param  fieldName Description of the Parameter
     *@return  The field value
     */
    public Field getField(String fieldName) {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toStringBody() {
        return "USE " + orgNode.defName;
    }
}

