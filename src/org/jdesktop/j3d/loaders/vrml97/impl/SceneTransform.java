/*
 * $RCSfile: SceneTransform.java,v $
 *
 *      @(#)Transform.java 1.48 98/10/21 16:57:19
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
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

/**  Description of the Class */
public class SceneTransform extends Transform {
    /**
     *Constructor for the SceneTransform object
     *
     *@param  loader Description of the Parameter
     */
    SceneTransform(Loader loader) {
        super(loader);
    }

    /**  Description of the Method */
    void initImpl() {
        super.initImpl();
        impl.setCapability(org.jogamp.java3d.TransformGroup.ALLOW_TRANSFORM_READ);
        impl.setCapability(org.jogamp.java3d.TransformGroup.ALLOW_TRANSFORM_WRITE);
        impl.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
        impl.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_WRITE);
        //impl.setCapability(org.jogamp.java3d.Node.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
        //impl.setCapability(org.jogamp.java3d.Node.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
        impl.setCapability(org.jogamp.java3d.Node.ENABLE_PICK_REPORTING);
        impl.setCapability(org.jogamp.java3d.Node.ALLOW_LOCAL_TO_VWORLD_READ);

        impl.setPickable(true);
    }

    /**
     *  Sets the sceneBounds attribute of the SceneTransform object
     *
     *@param  b The new sceneBounds value
     */
    void setSceneBounds(BoundingSphere b) {
        Point3d p = new Point3d();
        ((BoundingSphere) b).getCenter(p);
        center.setValue((float) p.x, (float) p.y, (float) p.z);
        if (b.getRadius() > 10.0) {
            float inv = 1.0f / (float) (b.getRadius());
            // this did not seem to work ???
            // while setting in the file did.
            scale.setValue(inv, inv, inv);
        }
    }

}

