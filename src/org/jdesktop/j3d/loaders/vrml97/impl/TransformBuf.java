/*
 * $RCSfile: TransformBuf.java,v $
 *
 *      @(#)TransformBuf.java 1.6 98/11/05 20:35:29
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
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class TransformBuf {

    int size;
    Transform[] array;
    boolean batchReady;

    /**Constructor for the TransformBuf object */
    TransformBuf() {
        array = new Transform[128];
        batchReady = true;
        size = 0;
    }

    /**
     *  Description of the Method
     *
     *@param  newTrans Description of the Parameter
     */
    void add(Transform newTrans) {
        if (size == array.length) {
            Transform[] newArray = new Transform[array.length + 128];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size++] = newTrans;
    }

    /**  Description of the Method */
    void startBatchLoading() {
        size = 0;
        batchReady = false;
    }

    /**  Description of the Method */
    void stopBatchLoading() {
        batchReady = true;
    }
}

