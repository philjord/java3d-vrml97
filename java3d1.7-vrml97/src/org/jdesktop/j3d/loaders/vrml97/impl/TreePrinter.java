/*
 * $RCSfile: TreePrinter.java,v $
 *
 *      @(#)TreePrinter.java 1.15 98/11/05 20:35:29
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
 * $Date: 2005/02/03 23:07:03 $
 * $State: Exp $
 */
/*
 * @Author: Doug Gehringer
 * @Author: Rick Goldberg
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jogamp.java3d.*;
import org.jogamp.java3d.Node;

/**  Description of the Class */
public class TreePrinter {
    PrintStream printStream;
    String j3dPkg = new String("org.jogamp.java3d.");
    String v97Pkg = new String("org.jdesktop.j3d.loaders.vrml97.impl.");

    /**
     *  Description of the Method
     *
     *@param  s Description of the Parameter
     *@param  l Description of the Parameter
     */
    public void print(PrintStream s, Locale l) {
        printStream = s;
        HashSet sharedGroups = new HashSet();
        printTree(l, 0, sharedGroups);
        Iterator iterator = sharedGroups.iterator();
        while (iterator.hasNext()) {
            SharedGroup sg = (SharedGroup) iterator.next();
            print(s, sg);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  l Description of the Parameter
     */
    public void print(Locale l) {
        print(System.out, l);
    }

    /**
     *  Description of the Method
     *
     *@param  l Description of the Parameter
     *@param  graphDepth Description of the Parameter
     *@param  sharedGroups Description of the Parameter
     */
    private void printTree(Locale l, int graphDepth, Set sharedGroups) {
        printNode(l, 0, sharedGroups);
        try {
            Iterator<BranchGroup> e = l.getAllBranchGraphs();
            while (e.hasNext()) {
                Object o = e.next();
                if (o instanceof Locale) {
                    printTree((Locale) o, graphDepth + 1, sharedGroups);
                }
                else if (o instanceof SceneGraphObject) {
                    printTree((SceneGraphObject) o, graphDepth + 1, sharedGroups);
                }
                else {
                    printStream.println(o + " unknown and in tree");
                }
            }
        }
        catch (CapabilityNotSetException e) {
            printStream.println("No capability to read children");
        }
    }

    /**
     *  Description of the Method
     *
     *@param  s Description of the Parameter
     *@param  sgo Description of the Parameter
     */
    public void print(PrintStream s, SceneGraphObject sgo) {
        printStream = s;
        HashSet sharedGroups = new HashSet();
        printTree(sgo, 0, sharedGroups);
        Iterator iterator = sharedGroups.iterator();
        while (iterator.hasNext()) {
            SharedGroup sg = (SharedGroup) iterator.next();
            print(s, sg);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  sgo Description of the Parameter
     */
    public void print(SceneGraphObject sgo) {
        print(System.out, sgo);
    }

    /**
     *  Description of the Method
     *
     *@param  sgo Description of the Parameter
     *@param  graphDepth Description of the Parameter
     *@param  sharedGroups Description of the Parameter
     */
    private void printTree(SceneGraphObject sgo,
            int graphDepth, Set sharedGroups) {

        printNode(sgo, graphDepth, sharedGroups);
        if (sgo instanceof org.jogamp.java3d.Group) {
            try {
                Iterator<Node> e = ((org.jogamp.java3d.Group) sgo).getAllChildren();
                while (e.hasNext()) {
                    printTree((SceneGraphObject) (e.next()), graphDepth + 1,
                            sharedGroups);
                }
            }
            catch (CapabilityNotSetException e) {
                // Can't read handled below
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@param  o Description of the Parameter
     *@return  Description of the Return Value
     */
    private String nodeString(Object o) {
        String objString = o.toString();
        if (objString.startsWith(j3dPkg)) {
            objString = objString.substring(j3dPkg.length());
        }
        if (objString.startsWith(v97Pkg)) {
            objString = objString.substring(v97Pkg.length());
        }
        return objString;
    }

    /**
     *  Description of the Method
     *
     *@param  o Description of the Parameter
     *@param  indent Description of the Parameter
     *@param  sharedGroups Description of the Parameter
     */
    private void printNode(Object o, int indent, Set sharedGroups) {
        for (int i = 0; i < indent; i++) {
            printStream.print(">");
        }
        printStream.print(nodeString(o) + ": ");
        if (o instanceof SceneGraphObject) {
            SceneGraphObject sgo = (SceneGraphObject) o;
            int capBits = 0;
            // TODO: how to make sure we always check all the valid bits?
            for (int i = 0; i < 64; i++) {
                if (sgo.getCapability(i)) {
                    capBits |= 1 << i;
                }
            }
            printStream.print("capBits:Ox" + Integer.toHexString(capBits));
            if (o instanceof org.jogamp.java3d.Group) {
                org.jogamp.java3d.Group g = (org.jogamp.java3d.Group) o;
                int numChildren = 0;
                try {
                    numChildren = g.numChildren();
                }
                catch (CapabilityNotSetException e) {
                    //anyone who is using treePrinter, is debugging, so it is
                    //alright to blindly allow read. you should first detach
                    //browser.curScene, print the tree, then add it back to
                    //browser.locale when finished.
                    g.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_READ);
                    numChildren = g.numChildren();
                    //System.out.println("Can't read children on group");
                    //return;
                }
                printStream.print(" children:" + numChildren);
                if (o instanceof TransformGroup) {
                    Transform3D transform = new Transform3D();
                    Transform3D identity = new Transform3D();
                    TransformGroup t = (TransformGroup) o;
                    t.getTransform(transform);
                    // TODO: use getBestType() when implemented
                    if (transform.equals(identity)) {
                        printStream.print(" xform:IDENTITY ");
                    }
                    else {
                        printStream.print(" xform:NON-IDENTITY ");
                    }
                }
            }
            else if (o instanceof Link) {
                Link l = (Link) o;
                SharedGroup sg = l.getSharedGroup();
                printStream.print(" sg:" + nodeString(sg));
                sharedGroups.add(sg);
            }
            else {
                printStream.print(": leaf");
            }
        }
        printStream.println();
    }
}

