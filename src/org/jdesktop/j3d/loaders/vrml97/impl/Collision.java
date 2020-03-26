/*
 * $RCSfile: Collision.java,v $
 *
 *      @(#)Collision.java 1.18 98/11/05 20:34:07
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
 *@Author:  Rick Goldberg
 *@Author:  Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class Collision extends Group {
    // eventIn MFNode addChildren from Group
    // eventIn MFNode removeChildren from Group
    // exposedField MFNode children [] from Group
    // field SFVec3f bboxCenter from Group
    // field SFVec3f bboxSize from Group

    // exposedField
    SFBool collide;

    // field
    SFNode proxy;// the proxy geometry for collision detection, not drawn

    // eventOut
    SFTime collideTime;

    org.jogamp.java3d.Group impl;

    /**
     *Constructor for the Collision object
     *
     *@param  loader Description of the Parameter
     */
    public Collision(Loader loader) {
        super(loader);

        // exposedField
        collide = new SFBool(true);

        // need to turn detection on or off ( which means add or subtract
        // this proxy from the avatars collide list
        proxy = new SFNode(null);

        // eventOut
        collideTime = new SFTime(0.0);

        initCollisionFields();
    }


    /**
     *Constructor for the Collision object
     *
     *@param  loader Description of the Parameter
     *@param  children Description of the Parameter
     *@param  bboxCenter Description of the Parameter
     *@param  bboxSize Description of the Parameter
     *@param  collide Description of the Parameter
     *@param  proxy Description of the Parameter
     */
    Collision(Loader loader, MFNode children, SFVec3f bboxCenter,
            SFVec3f bboxSize, SFBool collide, SFNode proxy) {
        super(loader, children, bboxCenter, bboxSize);
        this.collide = collide;
        this.proxy = proxy;

        collideTime = new SFTime(0.0);

        initCollisionFields();
    }

    /**  Description of the Method */
    void initImpl() {
        impl = new org.jogamp.java3d.Group();
        impl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_READ);
        impl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_WRITE);
        impl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_EXTEND);
        impl.setCapability(org.jogamp.java3d.Node.ALLOW_LOCAL_TO_VWORLD_READ);
        implNode = implGroup = impl;
        super.replaceChildren();// init the implGroup for clone()
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName == "collide") {
            //SFBool collideOn = (SFBool)FieldSpec.get(eventInName);
            // if collideOn check avatars collide list if not there add to it
            // otherwise remove it.
            if (collide.getValue() == true) {
                ;
            }
            else {
                ;
            }
        }
        else {
            super.notifyMethod(eventInName, time);
        }
    }

    /**
     *  Gets the type attribute of the Collision object
     *
     *@return  The type value
     */
    public String getType() {
        return "Collision";
    }

    /**  Description of the Method */
    void initCollisionFields() {
        collide.init(this, FieldSpec, Field.EXPOSED_FIELD, "collide");
        proxy.init(this, FieldSpec, Field.FIELD, "proxy");
        collideTime.init(this, FieldSpec, Field.EVENT_OUT, "collideTime");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Collision(loader, (MFNode) children.clone(),
                (SFVec3f) bboxCenter.clone(), (SFVec3f) bboxSize.clone(),
                (SFBool) collide.clone(), (SFNode) proxy.clone());
    }

}

