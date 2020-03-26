/*
 * $RCSfile: Sphere.java,v $
 *
 *      @(#)Sphere.java 1.24 98/11/05 20:35:22
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
import org.jogamp.java3d.BoundingBox;
import org.jogamp.vecmath.Point3d;

// only a groupgeom because of the util orientation
/**  Description of the Class */
public class Sphere extends GroupGeom {

    // field
    SFFloat radius;

    BoundingBox bounds;

    /**
     *Constructor for the Sphere object
     *
     *@param  loader Description of the Parameter
     */
    public Sphere(Loader loader) {
        super(loader);
        radius = new SFFloat(1.0f);
        initFields();
    }


    /**
     *Constructor for the Sphere object
     *
     *@param  loader Description of the Parameter
     *@param  radius Description of the Parameter
     */
    Sphere(Loader loader, SFFloat radius) {
        super(loader);
        this.radius = radius;
        initFields();
    }


    /**  Description of the Method */
    public void initImpl() {
        ;
    }

    /**
     *  Description of the Method
     *
     *@param  ap Description of the Parameter
     *@return  Description of the Return Value
     */
    public org.jogamp.java3d.Group initGroupImpl(org.jogamp.java3d.Appearance ap) {
        if (ap == null) {
            ap = new org.jogamp.java3d.Appearance();
        }

        implGroup = new org.jogamp.java3d.utils.geometry.Sphere(radius.value,
                org.jogamp.java3d.utils.geometry.Sphere.GENERATE_NORMALS |
                org.jogamp.java3d.utils.geometry.Sphere.GEOMETRY_NOT_SHARED |
                org.jogamp.java3d.utils.geometry.Sphere.GENERATE_TEXTURE_COORDS, 20,
                ap);
        ((org.jogamp.java3d.utils.geometry.Sphere) implGroup).getShape().clearCapability(org.jogamp.java3d.Node.ENABLE_PICK_REPORTING);
        implGroup.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);

        //((org.jogamp.java3d.utils.geometry.Sphere)implGroup).getShape().setPickable(false);
        bounds = new BoundingBox(new Point3d(-radius.value,
                -radius.value,
                -radius.value),
                new Point3d(radius.value,
                radius.value,
                radius.value));
        return implGroup;
    }


    /**
     *  Gets the type attribute of the Sphere object
     *
     *@return  The type value
     */
    public String getType() {
        return "Sphere";
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (loader.debug) {
            System.out.println(
                    "Sphere.notifyMethod: unexpected eventInName" + eventInName);
        }
    }

    /**  Description of the Method */
    void initFields() {
        radius.init(this, FieldSpec, Field.FIELD, "radius");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Sphere(loader, (SFFloat) radius.clone());
    }

    /**
     *  Gets the implGeom attribute of the Sphere object
     *
     *@return  The implGeom value
     */
    public org.jogamp.java3d.Geometry getImplGeom() {
        // should not be called
        throw new NullPointerException();
    }

    /**
     *  Description of the Method
     *
     *@param  impl Description of the Parameter
     */
    void updateParent(org.jogamp.java3d.Node impl) {
        super.updateParent(impl);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public boolean haveTexture() {
        return false;
    }

    /**
     *  Gets the boundingBox attribute of the Sphere object
     *
     *@return  The boundingBox value
     */
    public BoundingBox getBoundingBox() {
        return bounds;
    }

}

