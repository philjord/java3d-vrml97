/*
 * $RCSfile: Group.java,v $
 *
 *      @(#)Group.java 1.31 98/11/05 20:34:30
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
import java.util.Vector;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.vecmath.Point3d;

/**  Description of the Class */
public class Group extends GroupBase {

    // all fields come from GroupBase

    org.jogamp.java3d.Group impl;

    /**
     *Constructor for the Group object
     *
     *@param  loader Description of the Parameter
     */
    public Group(Loader loader) {
        super(loader);
    }

    /**
     *Constructor for the Group object
     *
     *@param  loader Description of the Parameter
     *@param  children Description of the Parameter
     *@param  bboxCenter Description of the Parameter
     *@param  bboxSize Description of the Parameter
     */
    Group(Loader loader, MFNode children, SFVec3f bboxCenter,
            SFVec3f bboxSize) {
        super(loader, children, bboxCenter, bboxSize);
    }

    /**  Description of the Method */
    void initImpl() {
        impl = new org.jogamp.java3d.Group();
        implGroup = impl;
        implNode = impl;
        impl.setUserData(new Vector());
        super.replaceChildren();// init the implGroup for clone()
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@param  g Description of the Parameter
     */
    void initImpl(org.jogamp.java3d.Group g) {
        impl = g;
        implGroup = impl;
        implNode = impl;
        impl.setUserData(new Vector());
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
        super.notifyMethod(eventInName, time);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        Object o = new Group(loader,
                (MFNode) children.clone(),
                (SFVec3f) bboxCenter.clone(),
                (SFVec3f) bboxSize.clone());
        loader.cleanUp();
        return o;
    }

    /**
     *  Gets the type attribute of the Group object
     *
     *@return  The type value
     */
    public String getType() {
        return "Group";
    }

    /**  Description of the Method */
    void initFields() {
        initGroupBaseFields();
    }

}

