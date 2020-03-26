/*
 * $RCSfile: LOD.java,v $
 *
 *      @(#)LOD.java 1.12 98/11/05 20:34:36
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
 * $Revision: 1.2 $
 * $Date: 2005/02/03 23:06:57 $
 * $State: Exp $
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Vector;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

/**  Description of the Class */
public class LOD extends NonSharedNode {
    // exposedField
    MFNode level;

    // field
    SFVec3f center;
    MFFloat range;

    org.jogamp.java3d.Group impl;
    org.jogamp.java3d.Switch implSwitch;
    DistanceLOD implLOD;

    /**
     *Constructor for the LOD object
     *
     *@param  loader Description of the Parameter
     */
    public LOD(Loader loader) {
        super(loader);
        level = new MFNode();
        center = new SFVec3f();
        range = new MFFloat();
        initFields();
    }

    /**
     *Constructor for the LOD object
     *
     *@param  loader Description of the Parameter
     *@param  level Description of the Parameter
     *@param  center Description of the Parameter
     *@param  range Description of the Parameter
     */
    LOD(Loader loader, MFNode level, SFVec3f center, MFFloat range) {
        super(loader);
        this.level = level;
        this.center = center;
        this.range = range;
        initFields();
    }

    /**  Description of the Method */
    void initImpl() {
        impl = new org.jogamp.java3d.Group();
        // init the switch to the furthest level
        implSwitch = new org.jogamp.java3d.Switch();
        implSwitch.setUserData(new Vector());
        double furthest;
        if (range.mfloat.length >= 1) {
            // set to the furthest
            implSwitch.setWhichChild(range.mfloat.length - 1);
            furthest = (double) range.mfloat[range.mfloat.length - 1];
            implSwitch.setCapability(org.jogamp.java3d.Switch.ALLOW_SWITCH_READ);
            implSwitch.setCapability(org.jogamp.java3d.Switch.ALLOW_SWITCH_WRITE);
            implSwitch.setUserData(new Vector());
            Point3f lodCenter = new Point3f(center.value[0], center.value[1],
                    center.value[2]);
            implLOD = new DistanceLOD(range.mfloat, lodCenter);
            implLOD.addSwitch(implSwitch);
            // set the scheduling bounds to be just outside the furthest
            // distance
            Point3d boundCenter = new Point3d(lodCenter);
            implLOD.setSchedulingBounds(new BoundingSphere(boundCenter,
                    furthest * 1.1));
            impl.addChild(implLOD);
        }
        else {
            // wrl has selected "auto", can't really do, so just set
            // switch to most detailed and don't bother with LOD behavior
            implSwitch.setWhichChild(0);
        }
        impl.addChild(implSwitch);
        replaceLevels();
        implNode = impl;
        implReady = true;
    }

    /**  Description of the Method */
    void replaceLevels() {
        int numChildren;
        if ((numChildren = implSwitch.numChildren()) != 0) {
            for (int i = 0; i < numChildren; i++) {
                implSwitch.removeChild(0);
            }
        }
        for (int i = 0; i < level.nodes.length; i++) {
            BaseNode child = level.nodes[i];

            // let the new child know this is their parent
            child.updateParent(implSwitch);

            // add the child's impl to the j3d group
            org.jogamp.java3d.Node implNode = child.getImplNode();
            if (loader.debug) {
                System.out.println(this.toStringId() + ":  index = " + i +
                        " child node type is " + child.getType() + " " +
                        child.toStringId() +
                        " gets implNoded to " + implNode);
            }
            if (implNode != null) {
                if (implNode.getParent() == null) {
                    implSwitch.addChild(implNode);
                }
                else {
                    implSwitch.addChild(implNode.cloneNode(true));
                }
                if (child instanceof DirectionalLight) {
                    DirectionalLight dirLight = (DirectionalLight) child;
                    dirLight.setScope(implSwitch);
                }
            }
        }
    }

    // return the number of tris in level 0
    /**
     *  Gets the numTris attribute of the LOD object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        int numTris = 0;
        if (level.nodes.length > 0) {
            BaseNode child = level.nodes[0];
            if (child != null) {
                numTris += child.getNumTris();
            }
        }
        return numTris;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        // spec says range needs to be 1 element less than the number
        // of elements in level, but range is not an updatable field.
        // if level nodes change, need to either clip the levels to
        // range+1 or add values to range
        if (eventInName.equals("level")) {
            replaceLevels();
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new LOD(loader,
                (MFNode) level.clone(),
                (SFVec3f) center.clone(),
                (MFFloat) range.clone());
    }

    /**
     *  Gets the type attribute of the LOD object
     *
     *@return  The type value
     */
    public String getType() {
        return "LOD";
    }

    /**  Description of the Method */
    void initFields() {
        level.init(this, FieldSpec, Field.EXPOSED_FIELD, "level");
        center.init(this, FieldSpec, Field.FIELD, "center");
        range.init(this, FieldSpec, Field.FIELD, "range");
    }

}

