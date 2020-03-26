/*
 * $RCSfile: ContentNegotiator.java,v $
 *
 *      @(#)ContentNegotiator.java 1.11 98/11/05 20:35:57
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
import java.io.*;

import java.net.*;

/**  Description of the Class */
class ContentNegotiator extends Thread implements Runnable {
    org.jdesktop.j3d.loaders.vrml97.impl.Sound sound;
    URL url;
    boolean locked;
    byte[] buffer;
    Object content;
    final static int SOUND_LOADER = 1;
    final static int URL_BYTE_LOADER = 2;
    int negotiation = 0;

    /**
     *Constructor for the ContentNegotiator object
     *
     *@param  url Description of the Parameter
     */
    ContentNegotiator(URL url) {
        // to do get negotiation from content-type
        this.url = url;
        this.locked = true;
        negotiation = URL_BYTE_LOADER;
        start();
    }

    // this is no longer in use
    /**
     *Constructor for the ContentNegotiator object
     *
     *@param  sound Description of the Parameter
     */
    ContentNegotiator(Sound sound) {
        this.sound = sound;
        this.locked = true;
        negotiation = SOUND_LOADER;
        start();
    }

    /**  Main processing method for the ContentNegotiator object */
    public void run() {
        startLoading();
    }

    /**
     *  Gets the content attribute of the ContentNegotiator object
     *
     *@return  The content value
     */
    synchronized Object getContent() {
        if (locked) {
            try {
                wait();
            }
            catch (InterruptedException ie) {
                ;
            }
        }

        return content;
    }

    /**  Description of the Method */
    synchronized void startLoading() {
        if (negotiation == URL_BYTE_LOADER) {
            try {
                DataInputStream d;
                int contentLength = -1;
                URLConnection urlc = url.openConnection();
                urlc.connect();
                urlc.getContentType();
                d = new DataInputStream(urlc.getInputStream());
                contentLength = urlc.getContentLength();
                buffer = new byte[contentLength];
                content = buffer;
                if (d != null) {
                    d.readFully(buffer, 0, contentLength);
                }
                yield();
            }
            catch (IOException ie) {
                ie.printStackTrace();
            }
            locked = false;
            notify();
        }
        else if (negotiation == SOUND_LOADER) {
            if (Browser.getBrowser().debug) {
                System.out.println("Sound negotiation begin");
            }
            // This could be a problem, since the j3d.Sound needs to be live
            //while ( !((org.jogamp.java3d.Sound)(sound.soundImpl)).isReady() ) {
            //yield();
            //try { wait(100); } catch (InterruptedException ie) {;}
            //}
            content = sound;
            locked = false;
            if (Browser.getBrowser().debug) {
                System.out.println("Sound negotiation end");
            }
            notify();
        }

    }
}

