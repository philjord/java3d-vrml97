/*
 * $RCSfile: BaseNode.java,v $
 *
 *      @(#)BaseNode.java 1.21 98/11/05 20:34:02
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
 * $Date: 2005/02/03 23:06:52 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public abstract class BaseNode {
    String defName;
    Browser browser;
    Loader loader;
    boolean implReady;
    org.jogamp.java3d.Node implNode;
    BaseNode parent;

    /**
     *Constructor for the BaseNode object
     *
     *@param  browser Description of the Parameter
     */
    public BaseNode(Browser browser) {
        this.browser = browser;
    }

    /**
     *Constructor for the BaseNode object
     *
     *@param  loader Description of the Parameter
     */
    BaseNode(Loader loader) {
        this.loader = loader;
        this.browser = loader.browser;
        //System.out.println("Node " + this + " created");
    }

    /**
     *  Gets the type attribute of the BaseNode object
     *
     *@return  The type value
     */
    public abstract String getType();

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public abstract Object clone();

    /**
     *  Description of the Method
     *
     *@param  s Description of the Parameter
     *@param  time Description of the Parameter
     */
    public abstract void notifyMethod(String s, double time);

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public abstract vrml.BaseNode wrap();

    /**
     *  Gets the field attribute of the BaseNode object
     *
     *@param  fieldName Description of the Parameter
     *@return  The field value
     */
    public abstract Field getField(String fieldName);

    /**
     *  Gets the browser attribute of the BaseNode object
     *
     *@return  The browser value
     */
    public Browser getBrowser() {
        return browser;
    }

    /**  Description of the Method */
    void initImpl() {
        if (loader.debug) {
            System.out.println("BaseNode.initImpl() called on node " +
                    toStringId());
        }
        implNode = null;
        implReady = true;
    }
    // called by parent after node is added to parent
    /**
     *  Description of the Method
     *
     *@param  parentImpl Description of the Parameter
     */
    void updateParent(org.jogamp.java3d.Node parentImpl) { }

    /**
     *  Gets the implNode attribute of the BaseNode object
     *
     *@return  The implNode value
     */
    public org.jogamp.java3d.Node getImplNode() {
        return implNode;
    }

    /**
     *  Description of the Method
     *
     *@param  defName Description of the Parameter
     */
    void define(String defName) {
        this.defName = defName;
        //System.out.println("Node " + this + " def'd to name " + defName);
    }

    /**
     *  Description of the Method
     *
     *@param  scene Description of the Parameter
     */
    void registerUse(Scene scene) { }

    // subclasses which have triangles should define
    /**
     *  Gets the numTris attribute of the BaseNode object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        return 0;
    }

    // subclasses should implement toStringBody() not toString()
    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public final String toString() {
        String retval = "";
        if (defName != null) {
            retval += "DEF " + defName + " ";
        }
        retval += toStringBody();
        return retval;
    }

    // use instead of toString() to get object id instead of VRML source
    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toStringId() {
        return this.getClass().getName() + "@" +
                Integer.toHexString(this.hashCode());
    }

    // this should be overridden by a method which outputs the VRML for the
    // node
    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    String toStringBody() {
        return toStringId();
    }
}

