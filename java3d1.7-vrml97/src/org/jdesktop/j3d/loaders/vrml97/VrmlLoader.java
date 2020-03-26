/*
 * $RCSfile: VrmlLoader.java,v $
 *
 *      @(#)VrmlLoader.java 1.15 99/03/08 11:02:02
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
 * $Revision: 1.4 $
 * $Date: 2006/03/30 08:19:29 $
 * $State: Exp $
 */
package org.jdesktop.j3d.loaders.vrml97;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.lang.String;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is an implementation of the Java3D utility loader interface.
 *
 * A VrmlScene can be loaded via URL, filesystem pathname or a
 * Reader.
 *
 * The "base" URL or pathname is the pathname used for relative paths in the
 * WRL.   If the base is not specified it will be derived from the main URL or
 * pathname.
 *
 * @see  VrmlScene
 * @see  org.jogamp.java3d.loaders.Loader
 * @see  org.jogamp.java3d.loaders.Scene
 * @author  Rick Goldberg
 * @author  Doug Gehringer
 * @author  Nikolai V. Chr.
 */
public class VrmlLoader extends org.jogamp.java3d.loaders.LoaderBase {

    org.jdesktop.j3d.loaders.vrml97.impl.Loader loader;

    /**
     * Constructor for the VrmlLoader object
     *
     * Will be setup to load static scene.
     *
     * @param  flags These are ignored for the time being.
     */
    public VrmlLoader(int flags) {
        super(flags);
        loader = new org.jdesktop.j3d.loaders.vrml97.impl.Loader(null,
                org.jdesktop.j3d.loaders.vrml97.impl.Loader.LOAD_STATIC);
    }

    /**
     * Constructor for the VrmlLoader object
     *
     * Will be setup to load static scene.
     */
    public VrmlLoader() {
        this(0);
    }

    /**
     *  Description of the Method
     *
     * @param  reader Description of the Parameter
     * @return  Description of the Return Value
     * @exception  FileNotFoundException If the file could not be found or opened.
     * @exception  IncorrectFormatException If a file of an incorrect type was passed.
     * @exception  ParsingErrorException If a problem was encountered parsing the file.
     */
    public org.jogamp.java3d.loaders.Scene load(Reader reader)
             throws FileNotFoundException, IncorrectFormatException,
            ParsingErrorException {
        org.jdesktop.j3d.loaders.vrml97.impl.Scene scene;
        try {
            loader.setWorldURL(baseUrl, null);
        }
        catch (MalformedURLException e) {
            // TODO: make a better handler
            FileNotFoundException f = new FileNotFoundException("BaseUrl: " + baseUrl);
            f.initCause(e);
            throw f;
        }
        try {
            scene = loader.load(reader);
        }
        catch (vrml.InvalidVRMLSyntaxException e) {
            e.printStackTrace();
            ParsingErrorException p = new ParsingErrorException();
            p.initCause(e);
            throw p;
        }
        return new VrmlScene(scene);
    }

    /**
     *  Description of the Method
     *
     * @param  pathname Description of the Parameter
     * @return  Description of the Return Value
     * @exception  FileNotFoundException If the file could not be found or opened.
     * @exception  IncorrectFormatException If a file of an incorrect type was passed.
     * @exception  ParsingErrorException If a problem was encountered parsing the file.
     */
    public org.jogamp.java3d.loaders.Scene load(String pathname)
             throws FileNotFoundException, IncorrectFormatException,
            ParsingErrorException {
        URL url = null;
        try {
            url = pathToURL(pathname);
        }
        catch (MalformedURLException e) {
            FileNotFoundException f = new FileNotFoundException("Loading: " + pathname);
            f.initCause(e);
            throw f;
        }
        try {
            loader.setWorldURL(pathToURL(basePath), url);
        }
        catch (MalformedURLException e) {
            FileNotFoundException f = new FileNotFoundException("BasePath: " + basePath);
            f.initCause(e);
            throw f;
        }
        return doLoad(url);
    }

    /**
     *  Description of the Method
     *
     * @param  url Description of the Parameter
     * @return  Description of the Return Value
     * @exception  FileNotFoundException If the file could not be found or opened.
     * @exception  IncorrectFormatException If a file of an incorrect type was passed.
     * @exception  ParsingErrorException If a problem was encountered parsing the file.
     */
    public org.jogamp.java3d.loaders.Scene load(URL url)
             throws FileNotFoundException, IncorrectFormatException,
            ParsingErrorException {
        try {
            loader.setWorldURL(baseUrl, url);
        }
        catch (MalformedURLException e) {
            FileNotFoundException f = new FileNotFoundException("BaseUrl: " + baseUrl);
            f.initCause(e);
            throw f;
        }
        return doLoad(url);
    }

    /**
     *  Description of the Method
     *
     * @param  url Description of the Parameter
     * @return  The loaded scene
     * @exception  ParsingErrorException If a problem was encountered parsing the file.
     * @exception  FileNotFoundException If the file could not be found or opened.
     */
    private org.jogamp.java3d.loaders.Scene doLoad(URL url)
             throws ParsingErrorException, FileNotFoundException {
        org.jdesktop.j3d.loaders.vrml97.impl.Scene scene;
        try {
            scene = loader.load(url);
        }
        catch (IOException e) {
            FileNotFoundException f = new FileNotFoundException(url.toString());
            f.initCause(e);
            throw f;
        }
        catch (vrml.InvalidVRMLSyntaxException e) {
            ParsingErrorException p = new ParsingErrorException(url.toString());
            p.initCause(e);
            throw p;
        }
        return new VrmlScene(scene);
    }

    // this method should only be used by methods that expect to only operate
    // on filesystem resident files, and those methods should not be used.
    /**
     *  Description of the Method
     *
     * @param  path Description of the Parameter
     * @return  Description of the Return Value
     * @exception  MalformedURLException If an URL could not be made from the path.
     */
    private URL pathToURL(String path) throws MalformedURLException {
        URL retval = null;
        if (path == null) {
            return null;
        }

        // hack: for win32 we check drive specifier
        //       for solaris we check startsWith /

        // this really should all take a clean look at URL(context,spec)

        if (!path.startsWith(java.io.File.separator) && (path.indexOf(':') != 1)) {
            path = System.getProperties().getProperty("user.dir") + '/' + path;
        }

        // switch from file separator to URL separator
        path = path.replace(java.io.File.separatorChar, '/');

        retval = new URL("file:" + path);
        return retval;
    }

}

