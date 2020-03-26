/*
 * $RCSfile: Fog.java,v $
 *
 *      @(#)Fog.java 1.3 98/11/05 20:36:29
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
 *
 * $Revision: 1.2 $
 * $Date: 2005/02/03 23:07:09 $
 * $State: Exp $
 */
/* @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.node;

/**  Description of the Class */
public class Fog extends vrml.node.Node {

    org.jdesktop.j3d.loaders.vrml97.impl.Fog impl;

    /**
     * This is an internal constructor.  All nodes are created through the
     * the Browser or Loader
     *
     *@param  init Description of the Parameter
     */
    public Fog(org.jdesktop.j3d.loaders.vrml97.impl.Fog init) {
        super(init);
        impl = init;
    }

    /**
     *  Gets the fogImpl attribute of the Fog object
     *
     *@return  The fogImpl value
     */
    public org.jogamp.java3d.Fog getFogImpl() {
        org.jogamp.java3d.BranchGroup bg = impl.getFogImpl();
        return (org.jogamp.java3d.Fog) bg.getChild(0);
    }
}

