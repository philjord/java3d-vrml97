/*
 * $RCSfile: GroupGeom.java,v $
 *
 *      @(#)GroupGeom.java 1.9 98/11/05 20:34:32
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
/*
 * @(#)GeomGroup.java	1.3 98/2/9
 *
 */
// to implement things that are really java3d groups as geometry
// such as
// Cone
// Cylinder
// Extrusion


package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public abstract class GroupGeom extends Geometry {

    org.jogamp.java3d.utils.geometry.Primitive implGroup;

    /**
     *Constructor for the GroupGeom object
     *
     *@param  loader Description of the Parameter
     */
    public GroupGeom(Loader loader) {
        super(loader);
    }

    /**
     *  Description of the Method
     *
     *@param  ap Description of the Parameter
     *@return  Description of the Return Value
     */
    public abstract org.jogamp.java3d.Group initGroupImpl(
            org.jogamp.java3d.Appearance ap);

    /**
     *  Gets the numTris attribute of the GroupGeom object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        // TODO: fix counts for case where pieces of prim are missing
        return implGroup.getNumTriangles();
    }

}

