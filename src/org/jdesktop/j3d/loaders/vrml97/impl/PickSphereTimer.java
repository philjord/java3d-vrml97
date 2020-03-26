/*
 * $RCSfile: PickSphereTimer.java,v $
 *
 *      @(#)PickSphereTimer.java 1.5 98/11/05 20:34:51
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
 * $Date: 2005/02/03 23:06:59 $
 * $State: Exp $
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Iterator;

import org.jogamp.java3d.*;

/**  Description of the Class */
public class PickSphereTimer extends TransparencyInterpolator {
    WakeupCondition critter;
    RGroup handle;

    /**
     *Constructor for the PickSphereTimer object
     *
     *@param  browser Description of the Parameter
     *@param  handle Description of the Parameter
     *@param  ta Description of the Parameter
     */
    public PickSphereTimer(Browser browser, RGroup handle,
            TransparencyAttributes ta) {

        super(new Alpha(1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0,
                5000, 0, 0,
                500, 0, 0), ta);

        this.handle = handle;
        setSchedulingBoundingLeaf(browser.loader.infiniteBoundingLeaf);
        setMinimumTransparency(.5f);
        setMaximumTransparency(.9f);
    }


    /**
     *  Description of the Method
     *
     *@param  critters Description of the Parameter
     */
    public void processStimulus(Iterator<WakeupCriterion> critters) {
        if (getAlpha().finished()) {
            handle.detach();
            try {
                while (true) {
                    handle.removeChild(0);
                }
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                ;
            }
        }
        super.processStimulus(critters);
    }

}

