package org.jdesktop.j3d.loaders.vrml97;

import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.LoaderBase;
import org.jogamp.java3d.loaders.ParsingErrorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import org.jdesktop.j3d.loaders.vrml97.impl.Loader;
import vrml.InvalidVRMLSyntaxException;

public class VrmlLoader extends LoaderBase
{
  Loader loader;

  public VrmlLoader(int flags)
  {
    super(flags);
    this.loader = new Loader(null, 0);
  }

  public VrmlLoader()
  {
    this(0);
  }

  public org.jogamp.java3d.loaders.Scene load(Reader reader)
    throws FileNotFoundException, IncorrectFormatException, ParsingErrorException
  {
    try
    {
      this.loader.setWorldURL(this.baseUrl, null);
    }
    catch (MalformedURLException e)
    {
      FileNotFoundException f = new FileNotFoundException("BaseUrl: " + this.baseUrl);
      f.initCause(e);
      throw f;
    }org.jdesktop.j3d.loaders.vrml97.impl.Scene scene;
    try { scene = this.loader.load(reader);
    } catch (InvalidVRMLSyntaxException e)
    {
      e.printStackTrace();
      ParsingErrorException p = new ParsingErrorException();
      p.initCause(e);
      throw p;
    }
    return new VrmlScene(scene);
  }

  public org.jogamp.java3d.loaders.Scene load(String pathname)
    throws FileNotFoundException, IncorrectFormatException, ParsingErrorException
  {
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
      this.loader.setWorldURL(pathToURL(this.basePath), url);
    }
    catch (MalformedURLException e) {
      FileNotFoundException f = new FileNotFoundException("BasePath: " + this.basePath);
      f.initCause(e);
      throw f;
    }
    return doLoad(url);
  }

  public org.jogamp.java3d.loaders.Scene load(URL url)
    throws FileNotFoundException, IncorrectFormatException, ParsingErrorException
  {
    try
    {
      this.loader.setWorldURL(this.baseUrl, url);
    }
    catch (MalformedURLException e) {
      FileNotFoundException f = new FileNotFoundException("BaseUrl: " + this.baseUrl);
      f.initCause(e);
      throw f;
    }
    return doLoad(url);
  }

  private org.jogamp.java3d.loaders.Scene doLoad(URL url)
    throws ParsingErrorException, FileNotFoundException
  {
    org.jdesktop.j3d.loaders.vrml97.impl.Scene scene;
    try
    {
      scene = this.loader.load(url);
    }
    catch (IOException e) {
      FileNotFoundException f = new FileNotFoundException(url.toString());
      f.initCause(e);
      throw f;
    }
    catch (InvalidVRMLSyntaxException e) {
      ParsingErrorException p = new ParsingErrorException(url.toString());
      p.initCause(e);
      throw p;
    }
    return new VrmlScene(scene);
  }

  private URL pathToURL(String path)
    throws MalformedURLException
  {
    URL retval = null;
    if (path == null) {
      return null;
    }

    if ((!path.startsWith(File.separator)) && (path.indexOf(':') != 1)) {
      path = System.getProperties().getProperty("user.dir") + '/' + path;
    }

    path = path.replace(File.separatorChar, '/');

    retval = new URL("file:" + path);
    return retval;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.VrmlLoader
 * JD-Core Version:    0.6.0
 */