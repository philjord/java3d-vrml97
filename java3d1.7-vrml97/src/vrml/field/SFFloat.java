/*
 * $RCSfile: SFFloat.java,v $
 *
 *      @(#)SFFloat.java 1.9 98/11/05 20:40:37
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
 * $Date: 2005/02/03 23:07:15 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package vrml.field;

/**  Description of the Class */
public class SFFloat extends vrml.Field {
    org.jdesktop.j3d.loaders.vrml97.impl.SFFloat impl;

    /**
     *Constructor for the SFFloat object
     *
     *@param  init Description of the Parameter
     */
    public SFFloat(org.jdesktop.j3d.loaders.vrml97.impl.SFFloat init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFFloat(
                (org.jdesktop.j3d.loaders.vrml97.impl.SFFloat) impl.clone());
    }

    /**
     *Constructor for the SFFloat object
     *
     *@param  f Description of the Parameter
     */
    public SFFloat(float f) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFFloat(f);
        implField = impl;
    }

    /**
     *  Gets the value attribute of the SFFloat object
     *
     *@return  The value value
     */
    public float getValue() {
        return impl.getValue();
    }

    /**
     *  Sets the value attribute of the SFFloat object
     *
     *@param  f The new value value
     */
    public void setValue(float f) {
        impl.setValue(f);
    }

    /**
     *  Sets the value attribute of the SFFloat object
     *
     *@param  f The new value value
     */
    public void setValue(ConstSFFloat f) {
        impl.setValue(f.impl);
    }

    /**
     *  Sets the value attribute of the SFFloat object
     *
     *@param  f The new value value
     */
    public void setValue(SFFloat f) {
        impl.setValue(f.impl);
    }

}

