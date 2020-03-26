/*
 * $RCSfile: Anchor.java,v $
 *
 *      @(#)Anchor.java 1.22 99/03/15 10:26:58
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
 * $Revision: 1.3 $
 * $Date: 2006/03/29 16:28:11 $
 * $State: Exp $
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Vector;

/**
 *  Description of the Class
 *
 *@author  Rick Goldberg
 *@author  Doug Gehringer
 */
public class Anchor extends GroupBase implements VrmlSensor {

    // exposedField
    SFString description;
    MFString parameter;
    MFString url;

    // Additional fields are in GroupBase
    org.jogamp.java3d.Group impl;

    /**
     *Constructor for the Anchor object
     *
     *@param  loader Description of the Parameter
     */
    public Anchor(Loader loader) {
        super(loader);
        description = new SFString();
        parameter = new MFString();
        url = new MFString();
        initFields();
    }

    /**
     *Constructor for the Anchor object
     *
     *@param  loader Description of the Parameter
     *@param  children Description of the Parameter
     *@param  bboxCenter Description of the Parameter
     *@param  bboxSize Description of the Parameter
     *@param  description Description of the Parameter
     *@param  parameter Description of the Parameter
     *@param  url Description of the Parameter
     */
    public Anchor(Loader loader, MFNode children, SFVec3f bboxCenter,
            SFVec3f bboxSize, SFString description, MFString parameter,
            MFString url) {
        super(loader, children, bboxCenter, bboxSize);
        this.description = description;
        this.parameter = parameter;
        this.url = url;
        initFields();
    }

    /**  Description of the Method */
    public void initImpl() {
        if (loader.debug) {
            System.out.println("Anchor.initImpl() called");
        }
        impl = new org.jogamp.java3d.Group();
        implGroup = impl;
        implNode = impl;
        Vector v = new Vector();
        v.addElement(this);
        impl.setUserData(v);// need to back track pick
        impl.setCapability(org.jogamp.java3d.Node.ENABLE_PICK_REPORTING);
        impl.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
        impl.setCapability(org.jogamp.java3d.Node.ALLOW_LOCAL_TO_VWORLD_READ);
        // must not compile Anchors to get proper picking!
        impl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_READ);
        impl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_EXTEND);
        impl.setCapability(org.jogamp.java3d.Group.ALLOW_CHILDREN_WRITE);
        super.replaceChildren();
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if ((eventInName.equals("description")) ||
                (eventInName.equals("parameter")) ||
                (eventInName.equals("url"))) {
            // NO-OP
        }
        else if ((eventInName.equals("route_description")) ||
                (eventInName.equals("route_parameter")) ||
                (eventInName.equals("route_url"))) {
            // NO-OP
        }
        else {
            super.notifyMethod(eventInName, time);
        }
    }


    /**  Description of the Method */
    void initFields() {
        initGroupBaseFields();
        description.init(this, FieldSpec, Field.EXPOSED_FIELD, "description");
        parameter.init(this, FieldSpec, Field.EXPOSED_FIELD, "parameter");
        url.init(this, FieldSpec, Field.EXPOSED_FIELD, "url");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        Anchor a = new Anchor(loader,
                (MFNode) children.clone(),
                (SFVec3f) bboxCenter.clone(),
                (SFVec3f) bboxSize.clone(),
                (SFString) description.clone(),
                (MFString) parameter.clone(),
                (MFString) url.clone());
        return a;
    }

}

