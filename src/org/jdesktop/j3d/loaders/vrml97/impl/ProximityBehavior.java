/*
 * $RCSfile: ProximityBehavior.java,v $
 *
 *      @(#)ProximityBehavior.java 1.7 98/11/05 20:34:56
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
 * $Date: 2005/02/03 23:07:00 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Iterator;

import org.jogamp.java3d.*;

/**  Description of the Class */
public class ProximityBehavior extends Behavior {

    ProximitySensor owner;
    WakeupOnViewPlatformEntry woven;
    WakeupOnViewPlatformExit wovex;
    WakeupCriterion[] conditions;
    WakeupOr wor;
    boolean active = false;

    /**
     *Constructor for the ProximityBehavior object
     *
     *@param  ps Description of the Parameter
     */
    public ProximityBehavior(ProximitySensor ps) {
        owner = ps;
    }

    /**  Description of the Method */
    public void initialize() {
        setSchedulingBounds(owner.bbox);
        wovex = new WakeupOnViewPlatformExit(owner.bbox);
        woven = new WakeupOnViewPlatformEntry(owner.bbox);
        conditions = new WakeupCriterion[2];
        conditions[0] = wovex;
        conditions[1] = woven;
        wor = new WakeupOr(conditions);
        wakeupOn(wor);
    }

    // TODO: the Browser should maintain a list of objects interested
    // in the current position and orientation of the view platform.
    // when woven happens, we should add to that to generate
    // the proper eventOuts from the ProximitySensor;
    // Additionally to correctly implement, the eventOuts must be in the
    // Bounds own coordinate system.

    /**
     *  Description of the Method
     *
     *@param  ofElements Description of the Parameter
     */
    public void processStimulus(Iterator<WakeupCriterion> ofElements) {
        WakeupCriterion wakeup;
        double t = Time.getNow();
        while (ofElements.hasNext()) {
            wakeup = (WakeupCriterion) ofElements.next();
            if (wakeup instanceof WakeupOnViewPlatformExit) {
                owner.exitTime.setValue(t);
                active = false;
                owner.isActive.setValue(active);
                wakeupOn(new WakeupOnViewPlatformEntry(owner.bbox));
                //System.out.println("DEBUG: ProximtyBehavior:exit");
            }
            else if (wakeup instanceof WakeupOnViewPlatformEntry) {
                owner.enterTime.setValue(t);
                active = true;
                owner.isActive.setValue(active);
                wakeupOn(new WakeupOnViewPlatformExit(owner.bbox));
                //System.out.println("DEBUG: ProximtyBehavior:entry");
            }
        }
    }
}

