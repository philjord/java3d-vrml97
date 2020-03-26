/*
 * $RCSfile: NonSharedNode.java,v $
 *
 *      @(#)NonSharedNode.java 1.18 99/03/16 14:29:41
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
 * $Date: 2005/02/03 23:06:58 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.Link;

import org.jogamp.java3d.SharedGroup;

/**  Description of the Class */
public abstract class NonSharedNode extends Node {
    SharedGroup sharedGroup = null;
    boolean linkTested = false;
    boolean linkable = true;

    /**
     *Constructor for the NonSharedNode object
     *
     *@param  loader Description of the Parameter
     */
    NonSharedNode(Loader loader) {
        super(loader);
    }

    /**
     *  Description of the Method
     *
     *@param  scene Description of the Parameter
     */
    public void registerUse(Scene scene) {
        if (loader.debug) {
            System.out.println("Use of non-sharable tree " +
                    defName + " = " + this.toStringId() + " impl = " +
                    implNode);
        }
        // see if the tree is linkable
        if (!linkTested) {
            if (Leafer.has(implNode, Leafer.UNLINKABLE)) {
                linkable = false;
            }
            if (loader.debug) {
                System.out.println("Tested, linkable = " + linkable);
            }
            linkTested = true;
        }
        if (linkable) {
            if (sharedGroup == null) {
                // need to create a shared group and update the reference
                sharedGroup = new SharedGroup();
                scene.addSharedGroup(sharedGroup);

                org.jogamp.java3d.Node parent;
                if ((parent = implNode.getParent()) != null) {
                    org.jogamp.java3d.Group parentGroup =
                            (org.jogamp.java3d.Group) parent;
                    boolean found = false;
                    for (int i = 0; i < parentGroup.numChildren(); i++) {
                        org.jogamp.java3d.Node child =
                                (org.jogamp.java3d.Node) parentGroup.getChild(i);
                        if (child == implNode) {
                            found = true;
                            Link link = new Link(sharedGroup);
                            parentGroup.setChild(link, i);
                            if (loader.debug) {
                                System.out.println("Updated reference to " +
                                        implNode + " in parent " +
                                        parentGroup + " to link " + link);
                            }
                        }
                    }
                    if (loader.debug && (found == false)) {
                        System.out.println("Could not find " +
                                implNode + " in parent " + parentGroup);
                    }
                }

                // now the child should no longer be attached to a parent
                // (we replaced it with a link), so we can do this without
                // causing a multiple parent exception
                sharedGroup.addChild(implNode);
                if (loader.debug) {
                    System.out.println("nonShared tree: " +
                            this.toStringId() + " is now in SharedGroup " +
                            sharedGroup);
                }
            }
        }
        else {
            // Node unlinkable.  First use will use implNode, after will
            // use clones
        }
    }

    /**
     *  Gets the implNode attribute of the NonSharedNode object
     *
     *@return  The implNode value
     */
    public org.jogamp.java3d.Node getImplNode() {
        if (linkTested && !linkable) {
            if (implNode.getParent() == null) {
                return implNode;
            }
            else {
                // need to return a clone
                if (loader.debug) {
                    System.out.println("cloning a non linkable subtree:" +
                            implNode);
                    //loader.treePrinter.print(implNode);
                }
                org.jogamp.java3d.Node clone = implNode.cloneTree(false, true);
                if (implNode.getUserData() != null) {
                    clone.setUserData(implNode.getUserData());
                }
                if (loader.debug) {
                    System.out.println("cloning is: " + clone);
                }
                return clone;
            }
        }
        else {
            if (sharedGroup != null) {
                return (org.jogamp.java3d.Node) new Link(sharedGroup);
            }
            else {
                return implNode;
            }
        }
    }
}

