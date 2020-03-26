/*
 * $RCSfile: Cylinder.java,v $
 *
 *      @(#)Cylinder.java 1.23 98/11/05 20:34:22
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
 * $Date: 2006/03/28 14:58:09 $
 * $State: Exp $
 */
/*
 *@Author:  Rick Goldberg
 *@Author:  Doug Gehringer
 *@Author:  Nikolai V. Chr.
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.vecmath.Point3d;

/**  Description of the Class */
public class Cylinder extends GroupGeom {

    // field
    SFFloat radius;
    SFFloat height;
    SFBool side;
    SFBool bottom;
    SFBool top;

    BoundingBox bounds;

    /**
     *Constructor for the Cylinder object
     *
     *@param  loader Description of the Parameter
     */
    public Cylinder(Loader loader) {
        super(loader);
        radius = new SFFloat(1.0f);
        height = new SFFloat(2.0f);
        side = new SFBool(true);
        bottom = new SFBool(true);
        top = new SFBool(true);
        initFields();
    }

    /**
     *Constructor for the Cylinder object
     *
     *@param  loader Description of the Parameter
     *@param  radius Description of the Parameter
     *@param  height Description of the Parameter
     *@param  side Description of the Parameter
     *@param  bottom Description of the Parameter
     *@param  top Description of the Parameter
     */
    Cylinder(Loader loader, SFFloat radius, SFFloat height,
            SFBool side, SFBool bottom, SFBool top) {
        super(loader);
        this.radius = radius;
        this.height = height;
        this.side = side;
        this.bottom = bottom;
        this.top = top;
        initFields();
    }

    /**  Description of the Method */
    void initFields() {
        radius.init(this, FieldSpec, Field.FIELD, "radius");
        height.init(this, FieldSpec, Field.FIELD, "height");
        side.init(this, FieldSpec, Field.FIELD, "side");
        bottom.init(this, FieldSpec, Field.FIELD, "bottom");
        top.init(this, FieldSpec, Field.FIELD, "top");
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        ;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Cylinder(loader, (SFFloat) radius.clone(),
                (SFFloat) height.clone(),
                (SFBool) side.clone(),
                (SFBool) bottom.clone(),
                (SFBool) top.clone());
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

        //    System.out.println("r and h"+radius.value+ " " + height.value);
        implGroup = new org.jogamp.java3d.utils.geometry.Cylinder(radius.value,
                height.value,
                org.jogamp.java3d.utils.geometry.Cylinder.GENERATE_NORMALS |
                org.jogamp.java3d.utils.geometry.Cylinder.GENERATE_TEXTURE_COORDS,
                20, 2,
                ap);
        ((org.jogamp.java3d.Node) implGroup).clearCapability(org.jogamp.java3d.Node.ENABLE_PICK_REPORTING);

        // The util constructs a group with the side, bottom and top as
        // children.  Handle removing the elements from the end of the
        // group to the beginning
        if (loader.debug) {
            System.out.println("Cylinder group before edit: ");
            loader.treePrinter.print(implGroup);
        }
        if (!bottom.value) {
            if (loader.debug) {
                System.out.println("no bottom");
            }
            ((org.jogamp.java3d.Group) implGroup).removeChild(org.jogamp.java3d.utils.geometry.Cylinder.BOTTOM);
        }
        if (!top.value) {
            if (loader.debug) {
                System.out.println("no top");
            }
            ((org.jogamp.java3d.Group) implGroup).removeChild(org.jogamp.java3d.utils.geometry.Cylinder.TOP);
        }
        if (!side.value) {
            if (loader.debug) {
                System.out.println("no side");
            }
            ((org.jogamp.java3d.Group) implGroup).removeChild(org.jogamp.java3d.utils.geometry.Cylinder.BODY);
        }
        if (loader.debug) {
            System.out.println("Cylinder group after edit: ");
            loader.treePrinter.print(implGroup);
        }

        bounds = new BoundingBox(new Point3d(-radius.value,
                -height.value / 2.0,
                -radius.value),
                new Point3d(radius.value,
                height.value / 2.0,
                radius.value));

        return implGroup;
    }

    /**
     *  Gets the boundingBox attribute of the Cylinder object
     *
     *@return  The boundingBox value
     */
    public BoundingBox getBoundingBox() {
        return bounds;
    }

    /**
     *  Gets the implGeom attribute of the Cylinder object
     *
     *@return  The implGeom value
     */
    public org.jogamp.java3d.Geometry getImplGeom() {
        // should not be called
        throw new NullPointerException();
    }

    // this should perhaps be renamed haveTextureCoords()
    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public boolean haveTexture() {
        return true;
    }

    /**
     *  Gets the type attribute of the Cylinder object
     *
     *@return  The type value
     */
    public String getType() {
        return "Cylinder";
    }

}

