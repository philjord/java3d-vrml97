/*
 * $RCSfile: Leafer.java,v $
 *
 *      @(#)Leafer.java 1.12 98/11/05 20:34:37
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
 * $Date: 2005/02/03 23:06:57 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Enumeration;
import java.util.Iterator;

import org.jogamp.java3d.*;
import org.jogamp.java3d.Node;
import org.jogamp.vecmath.Color3f;

/**  Description of the Class */
public class Leafer {

    final static int LIGHTS = 0;
    final static int BOUNDINGLEAF = 1;
    final static int VIEWPLAT = 2;
    final static int SHAPE = 3;
    final static int UNLINKABLE = 9;

    /**
     *  Description of the Method
     *
     *@param  l Description of the Parameter
     *@param  leafType Description of the Parameter
     */
    public static void clean(Locale l, int leafType) {

        try {
            Iterator<BranchGroup> e = l.getAllBranchGraphs();
            while (e.hasNext()) {
                Object o = e.next();
                if (o instanceof Locale) {
                    clean((Locale) o, leafType);
                }
                else if (o instanceof SceneGraphObject) {
                    clean((SceneGraphObject) o, leafType);
                }
                else {
                    System.out.println(o + " unknown and in tree");
                }
            }
        }
        catch (CapabilityNotSetException e) {
            //System.out.println("No capability to read children");
        }
    }

    /**
     *  Description of the Method
     *
     *@param  sgo Description of the Parameter
     *@param  leafType Description of the Parameter
     */
    public static void clean(org.jogamp.java3d.SceneGraphObject sgo,
            int leafType) {

        if (sgo instanceof org.jogamp.java3d.Group) {
            try {
                Iterator<Node> e = ((org.jogamp.java3d.Group) sgo).getAllChildren();
                while (e.hasNext()) {
                    clean((SceneGraphObject) (e.next()), leafType);
                }
            }
            catch (CapabilityNotSetException e) {
                //System.out.println(sgo+ "can't read children!");
            }
        }
        else {
            switch (leafType) {
                case LIGHTS:
                    if (sgo instanceof org.jogamp.java3d.Light) {
                        ((org.jogamp.java3d.Light) sgo).setEnable(false);
                        //System.out.println(sgo+"light disabled");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@param  l Description of the Parameter
     *@param  leafType Description of the Parameter
     *@return  Description of the Return Value
     */
    public static boolean has(Locale l, int leafType) {

        try {
            Iterator<BranchGroup> e = l.getAllBranchGraphs();
            while (e.hasNext()) {
                Object o = e.next();
                if (o instanceof Locale) {
                    return has((Locale) o, leafType);
                }
                else if (o instanceof SceneGraphObject) {
                    return has((SceneGraphObject) o, leafType);
                }
                else {
                    System.out.println(o + " unknown and in tree");
                }
            }
        }
        catch (CapabilityNotSetException e) {
            //System.out.println("No capability to read children");
        }
        return false;
    }

    /**
     *  Description of the Method
     *
     *@param  sgo Description of the Parameter
     *@param  leafType Description of the Parameter
     *@return  Description of the Return Value
     */
    public static boolean has(org.jogamp.java3d.SceneGraphObject sgo,
            int leafType) {

        if (sgo instanceof org.jogamp.java3d.Group) {
            try {
                Iterator<Node> e = ((org.jogamp.java3d.Group) sgo).getAllChildren();
                boolean hasIt = false;
                while (e.hasNext()) {
                    hasIt |= has((SceneGraphObject) (e.next()), leafType);
                }
                return hasIt;
            }
            catch (CapabilityNotSetException e) {
                //System.out.println(sgo+" Can't read children (cap not set)");
                ((org.jogamp.java3d.Group) sgo).setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_READ);
                return has(sgo, leafType);
            }
        }
        else {
            switch (leafType) {
                case LIGHTS:
                    if (sgo instanceof org.jogamp.java3d.Light) {
                        Color3f c = new Color3f();
                        ((org.jogamp.java3d.Light) sgo).getColor(c);
                        System.out.println(c);
                        System.out.println(sgo + " found");
                        return true;
                    }
                    break;
                case BOUNDINGLEAF:
                    if (sgo instanceof org.jogamp.java3d.BoundingLeaf) {
                        return true;
                    }
                    break;
                case VIEWPLAT:
                    if (sgo instanceof org.jogamp.java3d.ViewPlatform) {
                        return true;
                    }
                    break;
                case SHAPE:
                    if (sgo instanceof org.jogamp.java3d.Shape3D) {
                        return true;
                    }
                    break;
                case UNLINKABLE:
                    return ((sgo instanceof org.jogamp.java3d.Background) |
                            (sgo instanceof org.jogamp.java3d.Behavior) |
                            (sgo instanceof org.jogamp.java3d.Clip) |
                            (sgo instanceof org.jogamp.java3d.Fog) |
                            (sgo instanceof org.jogamp.java3d.Soundscape) |
                            (sgo instanceof org.jogamp.java3d.ViewPlatform));
                default:
                    break;
            }
            return false;
        }
    }

}

