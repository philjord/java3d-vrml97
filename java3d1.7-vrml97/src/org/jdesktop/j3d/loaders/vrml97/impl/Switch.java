/*
 * $RCSfile: Switch.java,v $
 *
 *      @(#)Switch.java 1.21 98/11/05 20:35:24
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
 * $Date: 2005/02/03 23:07:02 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Vector;

/**  Description of the Class */
public class Switch extends NonSharedNode {
    // exposedField
    MFNode choice;

    // field
    SFInt32 whichChoice;

    org.jogamp.java3d.Switch impl;

    /**
     *Constructor for the Switch object
     *
     *@param  loader Description of the Parameter
     */
    public Switch(Loader loader) {
        super(loader);
        choice = new MFNode();
        whichChoice = new SFInt32(-1);
        initFields();
    }

    /**
     *Constructor for the Switch object
     *
     *@param  loader Description of the Parameter
     *@param  choice Description of the Parameter
     *@param  whichChoice Description of the Parameter
     */
    Switch(Loader loader, MFNode choice, SFInt32 whichChoice) {
        super(loader);
        this.choice = choice;
        this.whichChoice = whichChoice;
        initFields();
    }

    /**  Description of the Method */
    void initImpl() {
        if (loader.debug) {
            System.out.println("Switch.initImpl() called");
        }
        impl = new org.jogamp.java3d.Switch();
        impl.setCapability(org.jogamp.java3d.Switch.ALLOW_SWITCH_READ);
        impl.setCapability(org.jogamp.java3d.Switch.ALLOW_SWITCH_WRITE);
        impl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_READ);
        impl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_WRITE);
        impl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_EXTEND);
        impl.setUserData(new Vector());
        replaceChoices();
        setWhichChild();
        if (loader.debug) {
            System.out.println("Switch " + toStringId() + " impl is " + impl);
        }
        implNode = impl;
        implReady = true;
    }

    /**  Sets the whichChild attribute of the Switch object */
    void setWhichChild() {
        if (browser.debug) {
            System.out.println("Switch: setting " + whichChoice.value);
        }
        if (whichChoice.value < 0 || whichChoice.value > impl.numChildren()) {
            impl.setWhichChild(org.jogamp.java3d.Switch.CHILD_NONE);
        }
        else {
            impl.setWhichChild(whichChoice.value);
        }
    }

    // the choice field has been updated. Remove any nodes on the impl
    // and add the current list to the impl
    /**  Description of the Method */
    void replaceChoices() {
        int numChildren;
        if ((numChildren = impl.numChildren()) != 0) {
            for (int i = 0; i < numChildren; i++) {
                impl.removeChild(0);// should shift the rest down...
            }
        }
        for (int i = 0; i < choice.nodes.length; i++) {
            BaseNode child = choice.nodes[i];

            // let the new child know this is their parent
            child.updateParent(impl);

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
                    impl.addChild(implNode);
                }
                else {
                    impl.addChild(implNode.cloneNode(true));
                }
                if (child instanceof DirectionalLight) {
                    DirectionalLight dirLight = (DirectionalLight) child;
                    dirLight.setScope(impl);
                }
            }
        }
    }

    // return the number of tris in choice 0
    /**
     *  Gets the numTris attribute of the Switch object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        int numTris = 0;
        if ((choice != null) && (choice.nodes != null) &&
                (choice.nodes.length > 0)) {
            numTris += choice.nodes[0].getNumTris();
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
        if (eventInName.equals("choice")) {
            replaceChoices();
        }
        else if (eventInName.equals("whichChoice")) {
            setWhichChild();
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Switch(loader,
                (MFNode) choice.clone(),
                (SFInt32) whichChoice.clone());
    }

    /**
     *  Gets the type attribute of the Switch object
     *
     *@return  The type value
     */
    public String getType() {
        return "Switch";
    }

    /**  Description of the Method */
    void initFields() {
        choice.init(this, FieldSpec, Field.EXPOSED_FIELD, "choice");
        whichChoice.init(this, FieldSpec, Field.EXPOSED_FIELD, "whichChoice");
    }

}

