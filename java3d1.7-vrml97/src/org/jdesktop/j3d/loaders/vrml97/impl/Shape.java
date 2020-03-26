/*
 * $RCSfile: Shape.java,v $
 *
 *      @(#)Shape.java 1.45 99/03/11 14:58:12
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
import org.jogamp.java3d.Shape3D;

/**  Description of the Class */
public class Shape extends NonSharedNode {

    //exposedField
    SFNode appearance;
    SFNode geometry;

    int numTris = 0;
    boolean ifsChangeable = false;

    /**
     *Constructor for the Shape object
     *
     *@param  loader Description of the Parameter
     */
    public Shape(Loader loader) {
        super(loader);
        appearance = new SFNode(null);
        geometry = new SFNode(null);

        initFields();
    }

    /**
     *Constructor for the Shape object
     *
     *@param  loader Description of the Parameter
     *@param  appearancenode Description of the Parameter
     *@param  geonode Description of the Parameter
     */
    public Shape(Loader loader, SFNode appearancenode, SFNode geonode) {
        super(loader);
        appearance = appearancenode;
        geometry = geonode;

        initFields();
    }

    /**  Description of the Method */
    void initFields() {
        appearance.init(this, FieldSpec, Field.EXPOSED_FIELD, "appearance");
        geometry.init(this, FieldSpec, Field.EXPOSED_FIELD, "geometry");
    }

    /**  Description of the Method */
    public void initImpl() {
        Appearance appNode = (Appearance) appearance.node;
        Geometry geomNode = (Geometry) geometry.node;
        implNode = null;

        if (appNode == null) {
            appNode = new Appearance(loader);
            appNode.initImpl();
        }
        if (geomNode instanceof Text) {
            Text textNode = (Text) geomNode;
            implNode = textNode.createText2D(appNode.impl);
            implReady = true;
        }
        else if (geomNode instanceof GroupGeom) {
            implNode = ((GroupGeom) geomNode).initGroupImpl(appNode.impl);
            implReady = true;
        }
        else {
            if (geomNode != null) {
                org.jogamp.java3d.Geometry geom = geomNode.getImplGeom();
                if (geom != null) {
                    if (appearance.node == null) {
                        implNode = new Shape3D(geom);
                    }
                    else {
                        implNode = new Shape3D(geom, appNode.impl);
                    }
                }
            }
            if ((appNode != null) && (geomNode != null)) {
                appNode.numUses++;
                if (appNode.haveTexture && !geomNode.haveTexture()) {
                    // need to to a texGen
                    appNode.setTexGen(geomNode.getBoundingBox());
                }

                if (geomNode instanceof Ownable) {
                    // set the ifs owner to this
                    ((Ownable) geomNode).setOwner(this);
                    if (((Ownable) geomNode).getSolid() == false) {
                        org.jogamp.java3d.PolygonAttributes pa =
                                appNode.impl.getPolygonAttributes();
                        if (pa == null) {
                            pa = new org.jogamp.java3d.PolygonAttributes();
                            // set a default pa then
                            appNode.impl.setPolygonAttributes(pa);
                        }
                        appNode.impl.getPolygonAttributes().setCullFace(
                                org.jogamp.java3d.PolygonAttributes.CULL_NONE);
                        appNode.impl.getPolygonAttributes().
                                setBackFaceNormalFlip(true);
                    }
                }
                if ((geomNode instanceof IndexedLineSet) ||
                        (geomNode instanceof PointSet)) {
                    org.jogamp.java3d.Material material =
                            appNode.impl.getMaterial();
                    if (material == null) {
                        material = new org.jogamp.java3d.Material();
                        appNode.impl.setMaterial(material);
                    }
                    material.setLightingEnable(false);
                }
            }

            if (defName == null) {
                // If not a DEF'd node, null out those references to free up
                // the memory
                if (loader.debug) {
                    System.out.println("Shape.initImpl(): nulling refrences");
                }
                geometry.node = null;
                appearance.node = null;
            }
            implReady = true;
        }
        if (implNode != null) {
            //implNode.setCapability(org.jogamp.java3d.Node.ALLOW_BOUNDS_READ);
            //implNode.setCapability(
            //	    org.jogamp.java3d.Node.ALLOW_LOCAL_TO_VWORLD_READ);
            numTris = geomNode.getNumTris();
            if (ifsChangeable) {
                ((Shape3D) implNode).setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
                ((Shape3D) implNode).setCapability(Shape3D.ALLOW_GEOMETRY_READ);
            }
        }
    }

    /**
     *  Gets the numTris attribute of the Shape object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        //if (loader.debug) {
        //    System.out.println("Shape num tris: " + numTris);
        //}
        return numTris;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        if (loader.debug) {
            System.out.println("Shape.clone() called");
        }
        Shape retval = new Shape(loader, (SFNode) appearance.clone(),
                (SFNode) geometry.clone());
        if (loader.debug) {
            System.out.println("Shape.clone() returns " + retval.toStringId() +
                    " = " + retval);
        }
        return retval;
    }

    /**
     *  Gets the type attribute of the Shape object
     *
     *@return  The type value
     */
    public String getType() {
        return "Shape";
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("geometry")) {
            if (implReady && (implNode == null)) {
                // TODO: node needs to get attached to parent.
                initImpl();
            }
            else {
                if ((implNode instanceof Shape3D) &&
                        !(geometry.node instanceof GroupGeom)) {
                    try {
                        //Shape3D s = (Shape3D)implNode;
                        //((Shape3D)implNode).setGeometry(
                        //((Geometry)geometry.node).getImplGeom());
                        initImpl();
                    }
                    catch (NullPointerException npe) {
                        System.out.println(npe);
                    }
                }
                else {
                    System.err.println(
                            "Shape: Unimplemented case replacing geometry");
                }
            }
        }
        else if (eventInName.equals("appearance")) {
            if (implNode != null) {
                Appearance app = (Appearance) appearance.node;
                if (implNode instanceof Shape3D) {
                    ((Shape3D) implNode).setAppearance(app.impl);
                }
                else {
                    System.err.println(
                            "Shape: Unimplemented case replacing appearance");
                }
            }
        }
        else if (eventInName.equals("route_ifs_changeable")) {
            ifsChangeable = true;
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toStringBody() {
        String retval = "Shape {\n";
        if (appearance.node != null) {
            retval += "appearance " + appearance;
        }
        if (geometry.node != null) {
            retval += "geometry " + geometry;
        }
        retval += "}";
        return retval;
    }

}

