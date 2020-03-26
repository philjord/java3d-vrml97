/*
 * $RCSfile: SFImage.java,v $
 *
 *      @(#)SFImage.java 1.13 98/11/05 20:35:01
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
 * $Date: 2005/02/03 23:07:01 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Observable;
import java.util.Observer;

/**  Description of the Class */
public class SFImage extends Field {
    byte[] pixels;
    int width;
    int height;
    int depth;

    /**
     *Constructor for the SFImage object
     *
     *@param  w Description of the Parameter
     *@param  h Description of the Parameter
     *@param  d Description of the Parameter
     *@param  p Description of the Parameter
     */
    public SFImage(int w, int h, int d, byte[] p) {
        if (p != null) {
            pixels = new byte[p.length];
            System.arraycopy(p, 0, pixels, 0, p.length);
        }
        else {
            pixels = null;
        }

        width = w;
        height = h;
        depth = d;

    }

    /**
     *  Gets the width attribute of the SFImage object
     *
     *@return  The width value
     */
    public int getWidth() {
        return width;
    }

    /**
     *  Gets the height attribute of the SFImage object
     *
     *@return  The height value
     */
    public int getHeight() {
        return height;
    }

    /**
     *  Gets the components attribute of the SFImage object
     *
     *@return  The components value
     */
    public int getComponents() {
        return depth;
    }

    /**
     *  Gets the pixels attribute of the SFImage object
     *
     *@param  p Description of the Parameter
     */
    public void getPixels(byte[] p) {
        System.arraycopy(pixels, 0, p, 0, pixels.length);
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
        width = w;
        height = h;
        depth = d;
        if ((w * h * d) > 0) {
            pixels = new byte[p.length];// could check h*w*d = p.length
            try {
                System.arraycopy(p, 0, pixels, 0, p.length);
            }
            catch (Exception e) {
                System.err.println("SFImage.setValue(): exception " + e);
            }
        }
        else {
            pixels = null;
        }

        route();// be very careful since the new new new new semantic
        // is route to copy
    }

    /**
     *  Sets the value attribute of the SFImage object
     *
     *@param  i The new value value
     */
    public void setValue(ConstSFImage i) {
        setValue((SFImage) i.ownerField);
    }

    /**
     *  Sets the value attribute of the SFImage object
     *
     *@param  i The new value value
     */
    public void setValue(SFImage i) {
        setValue(i.width, i.height, i.depth, i.pixels);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFImage(width, height, depth, pixels);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((SFImage) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstSFImage(this);
        }
        return constField;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.SFImage(this);
    }

}

