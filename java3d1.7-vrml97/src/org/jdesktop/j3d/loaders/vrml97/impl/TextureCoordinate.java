/*
 * $RCSfile: TextureCoordinate.java,v $
 *
 *      @(#)TextureCoordinate.java 1.18 99/02/11 18:04:15
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
 * $Date: 2005/02/03 23:07:03 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class TextureCoordinate extends Node {
    // exposedField
    /**  Description of the Field */
    public MFVec2f point;

    Node owner;

    /**
     *Constructor for the TextureCoordinate object
     *
     *@param  loader Description of the Parameter
     */
    public TextureCoordinate(Loader loader) {
        super(loader);
        point = new MFVec2f();
        initFields();

    }

    /**
     *Constructor for the TextureCoordinate object
     *
     *@param  loader Description of the Parameter
     *@param  points Description of the Parameter
     */
    TextureCoordinate(Loader loader, float[][] points) {
        super(loader);
        this.point = new MFVec2f(points);
        initFields();
    }

    /**
     *Constructor for the TextureCoordinate object
     *
     *@param  loader Description of the Parameter
     *@param  points Description of the Parameter
     */
    TextureCoordinate(Loader loader, MFVec2f points) {
        super(loader);
        this.point = points;
        initFields();
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("point")) {
            owner.notifyMethod("texCoord", time);
        }
        if (eventInName.equals("route_point")) {
            owner.notifyMethod("route_texCoord", time);
        }
    }

    /**  Description of the Method */
    void initFields() {
        point.init(this, FieldSpec, Field.EXPOSED_FIELD, "point");
    }


    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new TextureCoordinate(loader, (MFVec2f) point.clone());
    }

    /**
     *  Gets the type attribute of the TextureCoordinate object
     *
     *@return  The type value
     */
    public String getType() {
        return "TextureCoordinate";
    }

}

