/*
 * $RCSfile: Interpolator.java,v $
 *
 *      @(#)Interpolator.java 1.11 98/11/05 20:34:36
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
 * $Revision: 1.2 $
 * $Date: 2005/02/03 23:06:57 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public abstract class Interpolator extends Node {

    // exposedField
    MFFloat key;// defined here but initialized by subclass

    private int lastIndex = 0;

    int iL;
    float f, af;

    /**
     *Constructor for the Interpolator object
     *
     *@param  loader Description of the Parameter
     */
    Interpolator(Loader loader) {
        super(loader);
    }

    /**
     *  Sets the indexFract attribute of the Interpolator object
     *
     *@param  value The new indexFract value
     */
    void setIndexFract(float value) {
        int j;
        if (key.mfloat[lastIndex] < value) {
            j = lastIndex;
        }
        else {
            j = 0;
        }
        for (; (j < key.mfloat.length) && (key.mfloat[j] < value); j++) {
            ;
        }

        iL = j - 1;

        if (iL >= (key.mfloat.length - 1)) {
            iL = key.mfloat.length - 2;
            f = 1.0f;
        }
        else if (iL < 0) {
            iL = 0;
            f = 0.0f;
        }
        else {
            try {
                f = value - key.mfloat[iL];
                f /= key.mfloat[iL + 1] - key.mfloat[iL];
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Interpolator madness!");
                iL = 0;
                f = value - key.mfloat[iL];
                f /= key.mfloat[iL + 1] - key.mfloat[iL];
            }
        }
        lastIndex = iL;
        af = 1.0f - f;
    }
}

