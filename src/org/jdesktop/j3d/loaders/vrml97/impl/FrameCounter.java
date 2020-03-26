/*
 * $RCSfile: FrameCounter.java,v $
 *
 *      @(#)FrameCounter.java 1.4 98/11/05 20:34:29
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
package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Iterator;

import org.jogamp.java3d.*;

/**  Description of the Class */
public class FrameCounter extends Behavior {
    Browser br;
    WakeupCondition critter;
    int count;
    String name;
    RGroup rHandle;

    /**
     *Constructor for the FrameCounter object
     *
     *@param  b Description of the Parameter
     *@param  count Description of the Parameter
     *@param  name Description of the Parameter
     */
    public FrameCounter(Browser b, int count, String name) {
        this.br = b;
        this.count = count;
        this.name = name;
        rHandle = new RGroup();
        rHandle.addChild(this);
    }

    /**  Description of the Method */
    public void initialize() {
        critter = new WakeupOnElapsedFrames(count);

        wakeupOn((critter));
    }

    /**
     *  Description of the Method
     *
     *@param  critters Description of the Parameter
     */
    public void processStimulus(Iterator<WakeupCriterion> critters) {
        while (critters.hasNext()) {
            if (critters.next() instanceof WakeupOnElapsedFrames) {
                br.frameCountCallback(this);
            }
        }
    }

}

