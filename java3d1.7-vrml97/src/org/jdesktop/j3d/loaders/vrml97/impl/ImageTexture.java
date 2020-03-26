/*
 * $RCSfile: ImageTexture.java,v $
 *
 *      @(#)ImageTexture.java 1.25 99/03/24 16:26:11
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
 * $Revision: 1.6 $
 * $Date: 2006/04/19 21:30:46 $
 * $State: Exp $
 */
/*
 * @author Rick Goldberg
 * @author Doug Gehringer
 * @author Nikolai V. Chr.
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.utils.image.TextureLoader;
import java.awt.*;
import java.awt.image.*;
import java.net.MalformedURLException;
import java.net.URL;
import org.jogamp.java3d.ImageComponent;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;
import java.beans.*;

/**  Description of the Class */
public class ImageTexture extends Node implements TextureSrc {

    // exposedField
    MFString url;
    SFBool repeatS;
    SFBool repeatT;

    Texture impl;
    Canvas observer = new Canvas();// dummy component
	
	boolean transparency = false;
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	final public static String TRANSPARENCY = "transparency";

    /**
     *Constructor for the ImageTexture object
     *
     *@param  loader Description of the Parameter
     */
    public ImageTexture(Loader loader) {
        super(loader);
        url = new MFString();
        repeatS = new SFBool(true);
        repeatT = new SFBool(true);
        initFields();
    }

    /**
     *Constructor for the ImageTexture object
     *
     *@param  loader Description of the Parameter
     *@param  url Description of the Parameter
     *@param  repeatS Description of the Parameter
     *@param  repeatT Description of the Parameter
     */
    ImageTexture(Loader loader, MFString url, SFBool repeatS,
            SFBool repeatT) {
        super(loader);
        this.url = url;
        this.repeatS = repeatS;
        this.repeatT = repeatT;
        initFields();
    }

    /**  Description of the Method */
    void initImpl() {
        impl = null;
        doChangeUrl();
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new ImageTexture(loader, (MFString) url.clone(),
                (SFBool) repeatS.clone(), (SFBool) repeatT.clone());
    }

    /**  Sets the repeatS attribute of the ImageTexture object */
    private void setRepeatS() {
        if (repeatS.value == true) {
            impl.setBoundaryModeS(Texture.WRAP);
        }
        else {
            impl.setBoundaryModeS(Texture.CLAMP);
        }
    }

    /**  Sets the repeatT attribute of the ImageTexture object */
    private void setRepeatT() {
        if (repeatT.value == true) {
            impl.setBoundaryModeT(Texture.WRAP);
        }
        else {
            impl.setBoundaryModeT(Texture.CLAMP);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("url")) {
            doChangeUrl();
        }
        else if (eventInName.equals("repeatS")) {
            if (impl != null) {
                setRepeatS();
            }
        }
        else if (eventInName.equals("repeatT")) {
            if (impl != null) {
                setRepeatT();
            }
        }
    }

    /**  Description of the Method */
    void doChangeUrl() {
        if (url.strings == null) {
            return;
        }
        else if (url.strings.length > 0) {// this could be an absolute url
            // though
            for (int i = 0; i < url.strings.length; i++) {
                URL urlObj = null;
                try {
                    urlObj = loader.stringToURL(url.strings[i]);
                }
                catch (MalformedURLException e) {
                    continue;
                }
                if (urlObj == null) {
                    continue;
                }
				String suffix =	url.strings[i].substring(url.strings[i].lastIndexOf('.') + 1).toLowerCase();
				String format = "RGBA";
				if (suffix.equals("jpg") || suffix.equals("jpeg") || suffix.equals("jp2") || suffix.equals("j2c")) {
					format = "RGB";
				}
                TextureLoader tl = new TextureLoader(urlObj, format, TextureLoader.BY_REFERENCE, observer);
                impl = tl.getTexture();
                if (impl == null) {
                    continue;
                }
                impl.setMinFilter(Texture.BASE_LEVEL_LINEAR);
                impl.setMagFilter(Texture.BASE_LEVEL_LINEAR);
                impl.setEnable(true);
            }
        }
		updateTransparency();
        if (impl != null) {
            setRepeatS();
            setRepeatT();
        }
    }

	boolean isTransparent (Texture texture) {
		if(texture.getImage(0) instanceof ImageComponent2D) {
			ImageComponent2D comp = (ImageComponent2D)texture.getImage(0);
			try {
				 ColorModel cm = comp.getImage().getColorModel();
				 SampleModel sm = comp.getImage().getSampleModel();
				 DataBuffer db = comp.getImage().getRaster().getDataBuffer();
				 if(cm.hasAlpha() && cm.getTransparency() != cm.OPAQUE) {
					 for(int w = 0;w < comp.getWidth();w++){
						 for(int h = 0;h < comp.getHeight();h++){
							 if(cm.getAlpha(sm.getDataElements(w,h,null,db)) != 255) {
								comp.getImage().flush();
								return true; 
							 }
						 }
					 }
				 }
			} catch (IllegalStateException e) {
			}
			comp.getImage().flush();
		}
		return false;
	}
	
	public boolean getTransparency() {
		return transparency;
	}
	
	void updateTransparency() {
		boolean newValue = false;
		if(impl != null && impl instanceof Texture2D) {
			newValue = isTransparent(impl);
		}		 
		if(newValue != transparency) {
			transparency = newValue;
			propertyChangeSupport.firePropertyChange(TRANSPARENCY, null, Boolean.valueOf(newValue));
		}
	}
	
	public void addPropertyChangeListener(String n, PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(n, l);
	}
	
	public void removePropertyChangeListener(String n, PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(n, l);
	}
	
    /**
     *  Gets the implTexture attribute of the ImageTexture object
     *
     *@return  The implTexture value
     */
    public Texture getImplTexture() {
        return impl;
    }

    /**
     *  Gets the type attribute of the ImageTexture object
     *
     *@return  The type value
     */
    public String getType() {
        return "ImageTexture";
    }

    /**  Description of the Method */
    void initFields() {
        url.init(this, FieldSpec, Field.EXPOSED_FIELD, "url");
        repeatS.init(this, FieldSpec, Field.EXPOSED_FIELD, "repeatS");
        repeatT.init(this, FieldSpec, Field.EXPOSED_FIELD, "repeatT");

    }
}

