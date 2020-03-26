/*
 * $RCSfile: Coordinate.java,v $
 *
 *      @(#)Coordinate.java 1.19 98/09/01 15:05:55
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
 * $Date: 2005/02/03 23:06:54 $
 * $State: Exp $
 */
/*
 *@Author:  Rick Goldberg
 *@Author:  Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class Coordinate extends Node implements Reusable {
    // exposedField
    MFVec3f point;
    Node owner;

    /**
     *Constructor for the Coordinate object
     *
     *@param  loader Description of the Parameter
     */
    public Coordinate(Loader loader) {
        super(loader);
        point = new MFVec3f();
        initFields();
    }

    /**
     *Constructor for the Coordinate object
     *
     *@param  loader Description of the Parameter
     *@param  points Description of the Parameter
     */
    public Coordinate(Loader loader, float[] points) {
        super(loader);
        this.point = new MFVec3f(points);
        initFields();
    }

    /**
     *Constructor for the Coordinate object
     *
     *@param  loader Description of the Parameter
     *@param  points Description of the Parameter
     */
    public Coordinate(Loader loader, MFVec3f points) {
        super(loader);
        this.point = points;
        initFields();
    }

    /**  Description of the Method */
    void initFields() {
        point.init(this, FieldSpec, Field.EXPOSED_FIELD, "point");

    }

    /**  Description of the Method */
    public void reset() {
        point.reset();
        implReady = false;
    }


    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("point")) {
            if (owner != null) {
                owner.notifyMethod("coord", time);
            }
        }
        else if (eventInName.equals("route_point")) {
            if (owner != null) {
                owner.notifyMethod("route_coord", time);
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Coordinate(loader, (MFVec3f) point.clone());
    }

    /**
     *  Gets the type attribute of the Coordinate object
     *
     *@return  The type value
     */
    public String getType() {
        return "Coordinate";
    }


    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toStringBody() {
        return "Coordinate {\npoint " + point + "}";
    }
}

