package test;

import org.jogamp.java3d.loaders.Loader;
import org.jogamp.java3d.loaders.Scene;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Node;
import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;

/**  Description of the Class */
public class TestVrmlLoader {
    /**
     *  Description of the Method
     *
     * @param  argv Description of the Parameter
     */
    public static void main(String argv[]) {
        try {
            String filename = argv[0];
            VrmlLoader loader = new VrmlLoader();
            ///*
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
            File file = new File(filename);
            URL url = pathToURL(filename);
            loader.setBaseUrl(url);
            Scene scene = loader.load(in);
            //*/
            //Scene scene = loader.load(filename);
            BranchGroup branch = scene.getSceneGroup();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     *  Description of the Method
     *
     * @param  path Description of the Parameter
     * @return  Description of the Return Value
     * @exception  MalformedURLException Description of the Exception
     */
    public static URL pathToURL(String path) throws MalformedURLException {
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

