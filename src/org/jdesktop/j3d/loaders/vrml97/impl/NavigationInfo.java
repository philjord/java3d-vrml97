/*
 * $RCSfile: NavigationInfo.java,v $
 *
 *      @(#)NavigationInfo.java 1.23 98/11/05 20:34:46
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
 * $Date: 2005/02/03 23:06:58 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class NavigationInfo extends BindableNode {

    // From BindableNode:
    //SFBool bind;
    //SFBool isBound;
    //SFTime bindTime;

    // exposedField
    MFFloat avatarSize;
    SFBool headlight;
    SFFloat speed;
    MFString type;
    SFFloat visibilityLimit;

    /**
     *Constructor for the NavigationInfo object
     *
     *@param  loader Description of the Parameter
     */
    public NavigationInfo(Loader loader) {
        super(loader, loader.getNavigationInfoStack());
        String[] navTypes = new String[1];
        navTypes[0] = new String("EXAMINE");
        float[] av = new float[3];
        av[0] = .25f;
        av[1] = 1.6f;
        av[2] = .75f;
        avatarSize = new MFFloat(av);
        headlight = new SFBool(true);
        speed = new SFFloat(1.0f);// 0.0f????
        type = new MFString(navTypes);// really MFString?
        visibilityLimit = new SFFloat(0.0f);
        loader.addNavigationInfo(this);
        initFields();
    }

    /**
     *Constructor for the NavigationInfo object
     *
     *@param  loader Description of the Parameter
     *@param  bind Description of the Parameter
     *@param  bindTime Description of the Parameter
     *@param  isBound Description of the Parameter
     *@param  avatarSize Description of the Parameter
     *@param  healdlight Description of the Parameter
     *@param  speed Description of the Parameter
     *@param  type Description of the Parameter
     *@param  visibilityLimit Description of the Parameter
     */
    public NavigationInfo(Loader loader, SFBool bind, SFTime bindTime,
            SFBool isBound, MFFloat avatarSize, SFBool healdlight,
            SFFloat speed, MFString type, SFFloat visibilityLimit) {
        super(loader, loader.getNavigationInfoStack(), bind, bindTime, isBound);
        this.bind = bind;
        this.bindTime = bindTime;
        this.avatarSize = avatarSize;
        this.headlight = headlight;
        this.speed = speed;
        this.type = type;
        this.visibilityLimit = visibilityLimit;
        loader.addNavigationInfo(this);
        initFields();
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("bind")) {
            super.notifyMethod(eventInName, time);
        }
        else {
            if (!browser.navigationInfoStack.empty()) {
                NavigationInfo top =
                        (NavigationInfo) browser.navigationInfoStack.peek();
                if (top == this) {
                    System.out.println("navigation updateView");
                    browser.updateView();
                }
            }
        }
    }

    /**
     *  Gets the type attribute of the NavigationInfo object
     *
     *@return  The type value
     */
    public String getType() {
        return "NavigationInfo";
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new NavigationInfo(loader,
                (SFBool) bind.clone(),
                (SFTime) bindTime.clone(),
                (SFBool) isBound.clone(),
                (MFFloat) avatarSize.clone(),
                (SFBool) headlight.clone(),
                (SFFloat) speed.clone(),
                (MFString) type.clone(),
                (SFFloat) visibilityLimit.clone());
    }

    /**  Description of the Method */
    void initFields() {
        initBindableFields();
        speed.init(this, FieldSpec, Field.EXPOSED_FIELD, "speed");
        headlight.init(this, FieldSpec, Field.EXPOSED_FIELD, "headlight");
        avatarSize.init(this, FieldSpec, Field.EXPOSED_FIELD, "avatarSize");
        type.init(this, FieldSpec, Field.EXPOSED_FIELD, "type");
        visibilityLimit.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "visibilityLimit");

    }

}

