package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.utils.image.TextureLoader;
import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.net.URL;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;

public class ImageTexture extends Node
  implements TextureSrc
{
  MFString url;
  SFBool repeatS;
  SFBool repeatT;
  Texture impl;
  Canvas observer = new Canvas();

  boolean transparency = false;

  PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
  public static final String TRANSPARENCY = "transparency";

  public ImageTexture(Loader loader)
  {
    super(loader);

    this.url = new MFString();

    this.repeatS = new SFBool(true);

    this.repeatT = new SFBool(true);

    initFields();
  }

  ImageTexture(Loader loader, MFString url, SFBool repeatS, SFBool repeatT)
  {
    super(loader);

    this.url = url;

    this.repeatS = repeatS;

    this.repeatT = repeatT;

    initFields();
  }

  void initImpl()
  {
    this.impl = null;

    doChangeUrl();

    this.implReady = true;
  }

  public Object clone()
  {
    return new ImageTexture(this.loader, (MFString)this.url.clone(), (SFBool)this.repeatS.clone(), (SFBool)this.repeatT.clone());
  }

  private void setRepeatS()
  {
    if (this.repeatS.value == true)
    {
      this.impl.setBoundaryModeS(3);
    }
    else
    {
      this.impl.setBoundaryModeS(2);
    }
  }

  private void setRepeatT()
  {
    if (this.repeatT.value == true)
    {
      this.impl.setBoundaryModeT(3);
    }
    else
    {
      this.impl.setBoundaryModeT(2);
    }
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("url"))
    {
      doChangeUrl();
    }
    else if (eventInName.equals("repeatS"))
    {
      if (this.impl != null)
      {
        setRepeatS();
      }

    }
    else if (eventInName.equals("repeatT"))
    {
      if (this.impl != null)
      {
        setRepeatT();
      }
    }
  }

  void doChangeUrl()
  {
    if (this.url.strings == null)
    {
      return;
    }

    if (this.url.strings.length > 0)
    {
      for (int i = 0; i < this.url.strings.length; i++)
      {
        URL urlObj = null;
        try
        {
          urlObj = this.loader.stringToURL(this.url.strings[i]);
        }
        catch (MalformedURLException e)
        {
          continue;
        }

        if (urlObj == null)
        {
          continue;
        }

        String suffix = this.url.strings[i].substring(this.url.strings[i].lastIndexOf('.') + 1).toLowerCase();
        String format = "RGBA";
        if ((suffix.equals("jpg")) || (suffix.equals("jpeg")) || (suffix.equals("jp2")) || (suffix.equals("j2c"))) {
          format = "RGB";
        }
        TextureLoader tl = new TextureLoader(urlObj, format, 2, this.observer);
        this.impl = tl.getTexture();

        if (this.impl == null)
        {
          continue;
        }

        this.impl.setMinFilter(3);

        this.impl.setMagFilter(3);

        this.impl.setEnable(true);
      }

    }

    updateTransparency();

    if (this.impl != null)
    {
      setRepeatS();

      setRepeatT();
    }
  }

  boolean isTransparent(Texture texture)
  {
    if ((texture.getImage(0) instanceof ImageComponent2D))
    {
      ImageComponent2D comp = (ImageComponent2D)texture.getImage(0);
      try
      {
        ColorModel cm = comp.getImage().getColorModel();

        SampleModel sm = comp.getImage().getSampleModel();

        DataBuffer db = comp.getImage().getRaster().getDataBuffer();

        if ((cm.hasAlpha()) && (cm.getTransparency() != 1))
        {
          for (int w = 0; w < comp.getWidth(); w++)
          {
            for (int h = 0; h < comp.getHeight(); h++)
            {
              if (cm.getAlpha(sm.getDataElements(w, h, null, db)) == 255)
                continue;
              comp.getImage().flush();

              return true;
            }

          }

        }

      }
      catch (IllegalStateException e)
      {
      }

      comp.getImage().flush();
    }

    return false;
  }

  public boolean getTransparency()
  {
    return this.transparency;
  }

  void updateTransparency()
  {
    boolean newValue = false;

    if ((this.impl != null) && ((this.impl instanceof Texture2D)))
    {
      newValue = isTransparent(this.impl);
    }

    if (newValue != this.transparency)
    {
      this.transparency = newValue;

      this.propertyChangeSupport.firePropertyChange("transparency", null, Boolean.valueOf(newValue));
    }
  }

  public void addPropertyChangeListener(String n, PropertyChangeListener l)
  {
    this.propertyChangeSupport.addPropertyChangeListener(n, l);
  }

  public void removePropertyChangeListener(String n, PropertyChangeListener l)
  {
    this.propertyChangeSupport.removePropertyChangeListener(n, l);
  }

  public Texture getImplTexture()
  {
    return this.impl;
  }

  public String getType()
  {
    return "ImageTexture";
  }

  void initFields()
  {
    this.url.init(this, this.FieldSpec, 3, "url");

    this.repeatS.init(this, this.FieldSpec, 3, "repeatS");

    this.repeatT.init(this, this.FieldSpec, 3, "repeatT");
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ImageTexture
 * JD-Core Version:    0.6.0
 */