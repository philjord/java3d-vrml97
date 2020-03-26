/*
 * $RCSfile: SFImage.java,v $
 *
 *      @(#)SFImage.java 1.10 98/11/05 20:40:37
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
public class SFImage extends vrml.Field {
    org.jdesktop.j3d.loaders.vrml97.impl.SFImage impl;

    /**
     *Constructor for the SFImage object
     *
     *@param  init Description of the Parameter
     */
    public SFImage(org.jdesktop.j3d.loaders.vrml97.impl.SFImage init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFImage(
                (org.jdesktop.j3d.loaders.vrml97.impl.SFImage) impl.clone());
    }

    /**
     *Constructor for the SFImage object
     *
     *@param  w Description of the Parameter
     *@param  h Description of the Parameter
     *@param  d Description of the Parameter
     *@param  p Description of the Parameter
     */
    public SFImage(int w, int h, int d, byte[] p) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFImage(w, h, d, p);
        implField = impl;
    }

    /**
     *  Gets the width attribute of the SFImage object
     *
     *@return  The width value
     */
    public int getWidth() {
        return impl.getWidth();
    }

    /**
     *  Gets the height attribute of the SFImage object
     *
     *@return  The height value
     */
    public int getHeight() {
        return impl.getHeight();
    }

    /**
     *  Gets the components attribute of the SFImage object
     *
     *@return  The components value
     */
    public int getComponents() {
        return impl.getComponents();
    }

    /**
     *  Gets the pixels attribute of the SFImage object
     *
     *@param  p Description of the Parameter
     */
    public void getPixels(byte[] p) {
        impl.getPixels(p);
    }

    /**
     *  Sets the value attribute of the SFImage object
     *
     *@param  w The new value value
     *@param  h The new value value
     *@param  d The new value value
     *@param  p The new value value
     */
    public void setValue(int w, int h, int d, byte[] p) {
        impl.setValue(w, h, d, p);
    }

    /**
     *  Sets the value attribute of the SFImage object
     *
     *@param  i The new value value
     */
    public void setValue(ConstSFImage i) {
        impl.setValue(i.impl);
    }

    /**
     *  Sets the value attribute of the SFImage object
     *
     *@param  i The new value value
     */
    public void setValue(SFImage i) {
        impl.setValue(i.impl);
    }

}
