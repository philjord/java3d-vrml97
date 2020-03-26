/*
 * $RCSfile: GroupBase.java,v $
 *
 *      @(#)GroupBase.java 1.30 99/03/24 15:32:53
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
 * $Date: 2005/02/03 23:06:56 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.*;

/**  Description of the Class */
public abstract class GroupBase extends NonSharedNode {

    // eventIn
    MFNode addChildren;// to comply with the spec, these are really
    MFNode removeChildren;// supposed to only be write only.

    // exposedField
    MFNode children;

    // field
    SFVec3f bboxCenter;
    SFVec3f bboxSize;

    org.jogamp.java3d.Group implGroup;

    /**
     *Constructor for the GroupBase object
     *
     *@param  loader Description of the Parameter
     */
    public GroupBase(Loader loader) {
        super(loader);
        children = new MFNode();
        bboxCenter = new SFVec3f();
        bboxSize = new SFVec3f(-1.0f, -1.0f, -1.0f);
        addChildren = new MFNode();
        removeChildren = new MFNode();
        initGroupBaseFields();
    }

    /**
     *Constructor for the GroupBase object
     *
     *@param  loader Description of the Parameter
     *@param  children Description of the Parameter
     *@param  bboxCenter Description of the Parameter
     *@param  bboxSize Description of the Parameter
     */
    GroupBase(Loader loader, MFNode children, SFVec3f bboxCenter,
            SFVec3f bboxSize) {
        super(loader);
        this.children = children;
        this.bboxCenter = bboxCenter;
        this.bboxSize = bboxSize;
        addChildren = new MFNode();
        removeChildren = new MFNode();
        initGroupBaseFields();
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("addChildren")) {
            MFNode newKids = (MFNode) FieldSpec.get(eventInName);
            doAddChildren(newKids);
        }
        else if (eventInName.equals("removeChildren")) {
            MFNode badKids = (MFNode) FieldSpec.get(eventInName);
            doRemoveChildren(badKids);
        }
        else if (eventInName.equals("children")) {
            replaceChildren();
        }
        else if (eventInName.equals("route_children")) {
            implGroup.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_READ);
            implGroup.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_WRITE);
            implGroup.setCapability(
                    org.jogamp.java3d.Group.ALLOW_CHILDREN_EXTEND);
        }
        else {
            System.out.println("GroupBase: unknown eventInName: " +
                    eventInName);
        }

    }

    // the children field has been updated. Remove any nodes on the implGroup
    // and add the current list to the implGroup
    /**  Description of the Method */
    void replaceChildren() {
        int numChildren;
        if ((numChildren = implGroup.numChildren()) != 0) {
            for (int i = 0; i < numChildren; i++) {
                implGroup.removeChild(0);// should shift the rest down...
            }
        }
        for (int i = 0; i < children.nodes.length; i++) {
            BaseNode child = children.nodes[i];

            child.updateParent(implGroup);
            // let the new child know this is their vrml parent
            child.parent = this;
            boolean doit = false;
            if (child instanceof VrmlSensor) {
                doit = true;
            }
            if (child instanceof ProtoInstance) {
                if (((ProtoInstance) child).containsSensor) {
                    Enumeration e = (((ProtoInstance) child).sensors).elements();
                    while (e.hasMoreElements()) {
                        BaseNode vs = (BaseNode) (e.nextElement());
                        vs.updateParent(implGroup);
                    }
                    doit = true;
                }
            }
            if (doit) {
                implGroup.setCapability(org.jogamp.java3d.Group.ENABLE_PICK_REPORTING);
                implGroup.setCapability(org.jogamp.java3d.Group.ALLOW_BOUNDS_READ);
                implGroup.setCapability(org.jogamp.java3d.Group.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
                implGroup.setCapability(org.jogamp.java3d.Group.ALLOW_LOCAL_TO_VWORLD_READ);
                implGroup.setPickable(true);
            }

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
                    implGroup.addChild(implNode);
                }
                else {
                    implGroup.addChild(implNode.cloneNode(true));
                }
                if (child instanceof DirectionalLight) {
                    DirectionalLight dirLight = (DirectionalLight) child;
                    dirLight.setScope(implGroup);
                }
            }
        }
    }

    /**
     *  Gets the numTris attribute of the GroupBase object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        int numTris = 0;
        for (int i = 0; i < children.nodes.length; i++) {
            BaseNode child = children.nodes[i];
            numTris += child.getNumTris();
        }
        return numTris;
    }

    /**
     *  Description of the Method
     *
     *@param  newChildren Description of the Parameter
     */
    void doAddChildren(MFNode newChildren) {
        BaseNode[] newCh = newChildren.getValue();
        BaseNode[] oldCh = children.getValue();

        Vector v = new Vector(newCh.length + oldCh.length);
        for (int i = 0; i < oldCh.length; i++) {
            v.addElement(oldCh[i]);
        }

        for (int i = 0; i < newCh.length; i++) {
            if (loader.debug) {
                System.out.println(this + " doAddChildren " + newCh[i]);
            }
            v.addElement(newCh[i]);
        }

        BaseNode[] newFamily = new BaseNode[newCh.length + oldCh.length];
        v.copyInto(newFamily);

        children = new MFNode(newFamily);
        FieldSpec.remove("children");
        children.init(this, FieldSpec, Field.EXPOSED_FIELD, "children");

        replaceChildren();
    }

    /**
     *  Description of the Method
     *
     *@param  removeChildren Description of the Parameter
     */
    void doRemoveChildren(MFNode removeChildren) {
        BaseNode[] oldCh = children.getValue();// where do new children
        // get thier values from? MTV
        BaseNode[] removeCh = removeChildren.getValue();

        Vector v = new Vector(children.getValue().length);

        for (int i = 0; i < oldCh.length; i++) {
            v.addElement(oldCh[i]);
        }

        for (int i = 0; i < removeCh.length; i++) {
            // unfortunately these children don't
            // go to the prinicpals office!
            // ever see soilent green?
            if (loader.debug) {
                System.out.println("doRemoveChildren " + removeCh[i]);
            }
            v.removeElement(removeCh[i]);
        }

        BaseNode[] newFamily =
                new BaseNode[children.getSize() - removeCh.length];
        v.copyInto(newFamily);

        children = new MFNode(newFamily);
        FieldSpec.remove("children");
        FieldSpec.put("children", children);
        replaceChildren();
    }

    /**
     *  Gets the type attribute of the GroupBase object
     *
     *@return  The type value
     */
    public String getType() {
        return "Group";
    }

    // this may be overridden by subclasses
    /**  Description of the Method */
    void initFields() {
    }

    /**  Description of the Method */
    void initGroupBaseFields() {
        addChildren.init(this, FieldSpec, Field.EVENT_IN, "addChildren");
        removeChildren.init(this, FieldSpec, Field.EVENT_IN, "removeChildren");
        children.init(this, FieldSpec, Field.EXPOSED_FIELD, "children");
        bboxCenter.init(this, FieldSpec, Field.FIELD, "bboxCenter");
        bboxSize.init(this, FieldSpec, Field.FIELD, "bboxSize");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toStringBodyS() {
        String retval = "children " + children;
        if ((bboxCenter.value[0] != 0.0) ||
                (bboxCenter.value[1] != 0.0) ||
                (bboxCenter.value[2] != 0.0)) {
            retval += "bboxCenter " + bboxCenter;
        }
        if ((bboxSize.value[0] != -1.0) ||
                (bboxSize.value[1] != -1.0) ||
                (bboxSize.value[2] != -1.0)) {
            retval += "bboxSize " + bboxSize;
        }
        return retval;
    }
}

