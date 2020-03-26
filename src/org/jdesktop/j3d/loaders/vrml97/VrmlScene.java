/*
 * $RCSfile: VrmlScene.java,v $
 *
 *      @(#)VrmlScene.java 1.21 99/03/11 11:12:44
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
 *
 * $Revision: 1.2 $
 * $Date: 2005/02/03 23:06:51 $
 * $State: Exp $
 */
/* @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
// Static scene description for time zero loader

package org.jdesktop.j3d.loaders.vrml97;
import java.util.Enumeration;
import java.util.Hashtable;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.TransformGroup;
import org.jdesktop.j3d.loaders.vrml97.node.*;
import vrml.*;
import vrml.node.*;

/**  Description of the Class */
public class VrmlScene implements org.jogamp.java3d.loaders.Scene {
    org.jdesktop.j3d.loaders.vrml97.impl.Scene base;
    String description;
    int numTris;
    BaseNode[] objects;
    Viewpoint[] viewpoints;
    Node[] navInfos;
    Background[] backgrounds;
    Fog[] fogs;
    Light[] lights;
    Hashtable defTable;
    BranchGroup scene = null;

    /**
     *Constructor for the VrmlScene object
     *
     *@param  base Description of the Parameter
     */
    VrmlScene(org.jdesktop.j3d.loaders.vrml97.impl.Scene base) {
        this.base = base;
        description = base.description;
        numTris = base.numTris;

        objects = new BaseNode[base.objects.size()];
        Enumeration elems = base.objects.elements();
        for (int i = 0; i < objects.length; i++) {
            org.jdesktop.j3d.loaders.vrml97.impl.BaseNode node =
                    (org.jdesktop.j3d.loaders.vrml97.impl.BaseNode) elems.nextElement();
            objects[i] = node.wrap();
        }

        viewpoints = new Viewpoint[base.viewpoints.size()];
        elems = base.viewpoints.elements();
        for (int i = 0; i < viewpoints.length; i++) {
            viewpoints[i] = (org.jdesktop.j3d.loaders.vrml97.node.Viewpoint)
                    ((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)
                    elems.nextElement()).wrap();
        }

        navInfos = new Node[base.navInfos.size()];
        elems = base.navInfos.elements();
        for (int i = 0; i < navInfos.length; i++) {
            navInfos[i] = (vrml.node.Node)
                    ((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)
                    elems.nextElement()).wrap();
        }

        backgrounds = new Background[base.backgrounds.size()];
        elems = base.backgrounds.elements();
        for (int i = 0; i < backgrounds.length; i++) {
            backgrounds[i] = (org.jdesktop.j3d.loaders.vrml97.node.Background)
                    ((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)
                    elems.nextElement()).wrap();
        }

        fogs = new Fog[base.fogs.size()];
        elems = base.fogs.elements();
        for (int i = 0; i < fogs.length; i++) {
            fogs[i] = (org.jdesktop.j3d.loaders.vrml97.node.Fog)
                    ((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)
                    elems.nextElement()).wrap();
        }

        lights = new Light[base.lights.size()];
        elems = base.lights.elements();
        for (int i = 0; i < lights.length; i++) {
            lights[i] = (org.jdesktop.j3d.loaders.vrml97.node.Light)
                    ((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)
                    elems.nextElement()).wrap();
        }

        defTable = new Hashtable();
        for (elems = base.defTable.keys(); elems.hasMoreElements(); ) {
            Object key = elems.nextElement();
            Object value = ((org.jdesktop.j3d.loaders.vrml97.impl.BaseNode)
                    base.defTable.get(key)).wrap();
            defTable.put(key, value);
        }
    }

    /**
     *  Gets the sceneGroup attribute of the VrmlScene object
     *
     *@return  The sceneGroup value
     */
    public BranchGroup getSceneGroup() {
        if (scene == null) {
            scene = new BranchGroup();
            for (int i = 0; i < objects.length; i++) {
                org.jogamp.java3d.Node j3dNode;
                if ((j3dNode = objects[i].getImplNode()) != null) {
                    ;
                    scene.addChild(j3dNode);
                }
            }
        }
        return scene;
    }

    /**
     * The TransformGroups returned will be parented withing the SceneGroup.
     * The ViewPlatform will be the child of the TransformGroup
     *
     *@return  The viewGroups value
     */
    public TransformGroup[] getViewGroups() {
        TransformGroup[] views = new TransformGroup[viewpoints.length];
        for (int i = 0; i < viewpoints.length; i++) {
            views[i] = (TransformGroup) viewpoints[i].getImplNode();
        }
        return views;
    }

    /**
     *  Gets the horizontalFOVs attribute of the VrmlScene object
     *
     *@return  The horizontalFOVs value
     */
    public float[] getHorizontalFOVs() {
        float[] fovs = new float[viewpoints.length];
        for (int i = 0; i < viewpoints.length; i++) {
            fovs[i] = viewpoints[i].getFOV();
        }
        return fovs;
    }

    /**
     * The Light nodes returned will be parented within the SceneGroup
     *
     *@return  The lightNodes value
     */
    public org.jogamp.java3d.Light[] getLightNodes() {
        org.jogamp.java3d.Light[] j3dLights =
                new org.jogamp.java3d.Light[lights.length * 2];
        for (int i = 0; i < lights.length; i++) {
            j3dLights[i * 2] = lights[i].getAmbientLight();
            j3dLights[i * 2 + 1] = lights[i].getLight();
        }
        return j3dLights;
    }

    /**
     *  Gets the namedObjects attribute of the VrmlScene object
     *
     *@return  The namedObjects value
     */
    public Hashtable getNamedObjects() {
        Hashtable j3dDefTable = new Hashtable();
        for (Enumeration elems = defTable.keys();
                elems.hasMoreElements(); ) {
            Object key = elems.nextElement();
            BaseNode node = (BaseNode) defTable.get(key);
            org.jogamp.java3d.Node value = node.getImplNode();
            if (value != null) {
                j3dDefTable.put(key, value);
            }
        }
        return j3dDefTable;
    }

    /**
     * The Background nodes returned will be parented within the SceneGroup
     *
     *@return  The backgroundNodes value
     */
    public org.jogamp.java3d.Background[] getBackgroundNodes() {
        org.jogamp.java3d.Background[] j3dBackgrounds =
                new org.jogamp.java3d.Background[backgrounds.length];
        for (int i = 0; i < backgrounds.length; i++) {
            j3dBackgrounds[i] = backgrounds[i].getBackgroundImpl();
        }
        return j3dBackgrounds;
    }

    /**
     * The Fog nodes returned will be parented within the SceneGroup
     *
     *@return  The fogNodes value
     */
    public org.jogamp.java3d.Fog[] getFogNodes() {
        org.jogamp.java3d.Fog[] j3dFogs = new org.jogamp.java3d.Fog[fogs.length];
        for (int i = 0; i < fogs.length; i++) {
            j3dFogs[i] = fogs[i].getFogImpl();
        }
        return j3dFogs;
    }

    /**
     * The VRML loader does not support loading behaviors, this method
     * returns null.
     *
     *@return  The behaviorNodes value
     */
    public org.jogamp.java3d.Behavior[] getBehaviorNodes() {
        return null;
    }


    /**
     * The VRML loader does not support loading sounds, this method
     * returns null.
     *
     *@return  The soundNodes value
     */
    public org.jogamp.java3d.Sound[] getSoundNodes() {
        return null;
    }

    /**
     * Returns the description (if any) from the first WorldInfo node
     * read.  If there is no description specified, null will be returned
     *
     *@return  The description value
     */
    public String getDescription() {
        return description;
    }


    // the VRML specific methods start here

    /**
     * Scans the subgraph, clearing the pickable and collidable flags on
     * the Shape3Ds in the subgraph to allow compilation.  The pickable
     * flag will be set to false if the Shape3D does not have an ancestor
     * which sets the ALLOW_PICK_REPORTING bit.  The collidable flag will
     * always be set to false.
     *
     *@param  root Description of the Parameter
     */
    public void cleanForCompile(org.jogamp.java3d.Node root) {
        org.jdesktop.j3d.loaders.vrml97.impl.TreeCleaner.cleanSubgraph(root);
    }

    /**
     * Returns the base level VRML nodes
     *
     *@return  The objects value
     */
    public BaseNode[] getObjects() {
        BaseNode nodes[] = new BaseNode[objects.length];
        for (int i = 0; i < objects.length; i++) {
            nodes[i] = objects[i];
        }
        return nodes;
    }

    /**
     * Returns the Viewpoint nodes in the scene
     *
     *@return  The viewpoints value
     */
    public Viewpoint[] getViewpoints() {
        Viewpoint[] vps = new Viewpoint[viewpoints.length];
        for (int i = 0; i < viewpoints.length; i++) {
            vps[i] = viewpoints[i];
        }
        return vps;
    }

    /**
     * Returns the a Hashtable which associated DEF names with Nodes
     *
     *@return  The defineTable value
     */
    public Hashtable getDefineTable() {
        Hashtable userDefTable = new Hashtable();
        for (Enumeration elems = defTable.keys();
                elems.hasMoreElements(); ) {
            Object key = elems.nextElement();
            Object value = defTable.get(key);
            userDefTable.put(key, value);
        }
        return userDefTable;
    }

    /**
     * Returns the approximate number of triangles in the Scene.  For Switch
     * and LOD nodes, only the triangles on the first child of the node are
     * counted.
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        return numTris;
    }
}

