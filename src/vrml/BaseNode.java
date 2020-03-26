/*
 * $RCSfile: BaseNode.java,v $
 *
 *      @(#)BaseNode.java 1.10 98/08/05 14:31:20
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
 * $Date: 2005/02/03 23:07:10 $
 * $State: Exp $
 */
/* @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package vrml;

/**  Description of the Class */
public abstract class BaseNode {
    /**  Description of the Field */
    protected org.jdesktop.j3d.loaders.vrml97.impl.BaseNode implBaseNode;

    /**
     *Constructor for the BaseNode object
     *
     *@param  init Description of the Parameter
     */
    protected BaseNode(org.jdesktop.j3d.loaders.vrml97.impl.BaseNode init) {
        implBaseNode = init;
    }

    /**
     *  Gets the type attribute of the BaseNode object
     *
     *@return  The type value
     */
    public abstract String getType();

    /**
     *  Gets the browser attribute of the BaseNode object
     *
     *@return  The browser value
     */
    public abstract Browser getBrowser();

    /**
     *  Description of the Method
     *
     *@param  browser Description of the Parameter
     *@return  Description of the Return Value
     */
    protected static org.jdesktop.j3d.loaders.vrml97.impl.Browser browserImpl(
            Browser browser) {
        return browser.impl;
    }

    /**
     *  Gets the impl attribute of the BaseNode object
     *
     *@return  The impl value
     */
    protected org.jdesktop.j3d.loaders.vrml97.impl.BaseNode getImpl() {
        return implBaseNode;
    }

    /**
     *  Gets the implNode attribute of the BaseNode object
     *
     *@return  The implNode value
     */
    public org.jogamp.java3d.Node getImplNode() {
        return implBaseNode.getImplNode();
    }

}

