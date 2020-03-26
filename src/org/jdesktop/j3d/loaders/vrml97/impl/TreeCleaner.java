/*
 * $RCSfile: TreeCleaner.java,v $
 *
 *      @(#)TreeCleaner.java 1.2 99/03/11 11:12:00
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
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.*;

import org.jogamp.java3d.*;
import org.jogamp.java3d.Node;
import org.jogamp.vecmath.Color3f;

/**  Description of the Class */
public class TreeCleaner {

    private final static int CLEAN_UNUSED = 1;
    private final static int CLEAN_NONE = 2;

    private final static int ALREADY_CLEANED = -1;

    final static boolean debug = false;

    /**
     * Clears pickable and collidable flags in Shape3Ds in the subgraph
     * under the input node.  The pickable flag is set to false if there
     * are no groups above the Shape3D with ENABLE_PICK_REPORTING set. The
     * collidable flag on Shape3ds is always set to false.
     *
     *@param  implNode Description of the Parameter
     *@exception  RestrictedAccessException Description of the Exception
     */
    public static void cleanSubgraph(org.jogamp.java3d.Node implNode)
             throws RestrictedAccessException {
        Hashtable sharedGroups = new Hashtable();

        checkAndClean(implNode, CLEAN_UNUSED, sharedGroups);

        // now go through the shared groups we found, since traversing
        // the sg's can find new sg's, we need to iterate
        int numGroupsCleaned = 0;
        Integer alreadyCleaned = new Integer(ALREADY_CLEANED);

        while (numGroupsCleaned < sharedGroups.size()) {
            if (debug) {
                System.out.println("Cleaning shared groups: " +
                        numGroupsCleaned + " done out of " +
                        sharedGroups.size());
            }
            Enumeration e = sharedGroups.keys();
            while (e.hasMoreElements()) {
                SharedGroup sg = (SharedGroup) e.nextElement();
                int sgFlag = ((Integer) sharedGroups.get(sg)).intValue();
                if (sgFlag != ALREADY_CLEANED) {
                    checkAndClean(sg, sgFlag, sharedGroups);
                    sharedGroups.put(sg, alreadyCleaned);
                    numGroupsCleaned++;
                }
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@param  node Description of the Parameter
     *@param  pickingFlag Description of the Parameter
     *@param  sharedGroups Description of the Parameter
     *@exception  RestrictedAccessException Description of the Exception
     */
    static void checkAndClean(org.jogamp.java3d.Node node, int pickingFlag,
            Hashtable sharedGroups) throws RestrictedAccessException {
        if (node != null) {
            if (node.isLive()) {
                throw new RestrictedAccessException(
                        "Can't clean a live scene graph");
            }
            else {
                clean(node, pickingFlag, sharedGroups);
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@param  node Description of the Parameter
     *@param  pickingFlag Description of the Parameter
     *@param  sharedGroups Description of the Parameter
     */
    static void clean(org.jogamp.java3d.Node node, int pickingFlag,
            Hashtable sharedGroups) {

        if (node instanceof org.jogamp.java3d.Group) {
            // if current flag is unused and this group is pickable, keep it's
            // children pickable
            if ((pickingFlag == CLEAN_UNUSED) &&
                    (node.getCapability(
                    org.jogamp.java3d.Node.ENABLE_PICK_REPORTING))) {
                pickingFlag = CLEAN_NONE;
            }
            Iterator<Node> e = ((org.jogamp.java3d.Group) node).getAllChildren();
            while (e.hasNext()) {
                clean((org.jogamp.java3d.Node) (e.next()), pickingFlag,
                        sharedGroups);
            }
        }
        else if (node instanceof Link) {
            Link link = (Link) node;
            SharedGroup sg = link.getSharedGroup();
            Integer value = (Integer) sharedGroups.get(sg);
            // Set value if none set before or this value is more restrictive
            if ((value == null) || (pickingFlag > value.intValue())) {
                value = new Integer(pickingFlag);
                sharedGroups.put(sg, new Integer(pickingFlag));
            }
        }
        else if (node instanceof Shape3D) {
            if (pickingFlag != CLEAN_NONE) {
                node.setPickable(false);
            }
            node.setCollidable(false);
        }
    }
}

