/*
 * $RCSfile: Text.java,v $
 *
 *      @(#)Text.java 1.16 98/11/05 20:35:24
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
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

import org.jogamp.java3d.utils.geometry.Text2D;
import java.awt.Font;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;

import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;

/**  Description of the Class */
public class Text extends Geometry {

    // exposedField
    MFString string;
    SFNode fontStyle;
    MFFloat length;
    SFFloat maxExtent;

    static String vrmlSerif = "SERIF";
    static String vrmlSans = "SANS";
    static String vrmlFixed = "TYPEWRITER";

    static String j3dSerif = "Serif";
    static String j3dSans = "Sans";
    static String j3dFixed = "Courier";

    static String vrmlPlain = "PLAIN";
    static String vrmlBold = "BOLD";
    static String vrmlItalic = "ITALIC";
    static String vrmlBoldItalic = "BOLDITALIC";

    final static int RASTER_SIZE = 24;

    TransformGroup impl = null;

    /**
     *Constructor for the Text object
     *
     *@param  loader Description of the Parameter
     */
    public Text(Loader loader) {
        super(loader);
        string = new MFString();
        fontStyle = new SFNode();
        length = new MFFloat();
        maxExtent = new SFFloat(0.0f);
        initFields();
    }

    /**
     *Constructor for the Text object
     *
     *@param  loader Description of the Parameter
     *@param  string Description of the Parameter
     *@param  fontStyle Description of the Parameter
     *@param  length Description of the Parameter
     *@param  maxExtent Description of the Parameter
     */
    public Text(Loader loader, MFString string,
            SFNode fontStyle, MFFloat length,
            SFFloat maxExtent) {

        super(loader);
        this.string = string;
        this.fontStyle = fontStyle;
        this.length = length;
        this.maxExtent = maxExtent;
        initFields();
    }


    /**
     *  Description of the Method
     *
     *@param  app Description of the Parameter
     *@return  Description of the Return Value
     */
    org.jogamp.java3d.Node createText2D(org.jogamp.java3d.Appearance app) {
        if (loader.debug) {
            System.out.println("Text.createText2D() called");
        }
        if ((string.strings != null) && (string.strings.length > 0)) {
            if (loader.debug) {
                System.out.println("creating Text2D object");
            }

            FontStyle fs = null;
            if ((fontStyle.node != null) &&
                    (fontStyle.node instanceof FontStyle)) {
                fs = (FontStyle) fontStyle.node;
            }
            else {
                fs = new FontStyle(loader);
            }

            // Derive color from Appearance
            Color3f fontColor = new Color3f(1.0f, 1.0f, 1.0f);
            if (app != null) {
                org.jogamp.java3d.Material mat = app.getMaterial();
                if (mat != null) {
                    mat.getDiffuseColor(fontColor);
                }
            }

            String fontName = j3dSerif;
            if (fs.family.strings.length > 0) {
                boolean found = false;
                for (int i = 0;
                        ((i < fs.family.strings.length) && !found);
                        i++) {
                    if (vrmlSerif.equals(fs.family.strings[i])) {
                        fontName = j3dSerif;
                        found = true;
                    }
                    else if (vrmlSans.equals(fs.family.strings[i])) {
                        fontName = j3dSans;
                        found = true;
                    }
                    else if (vrmlFixed.equals(fs.family.strings[i])) {
                        fontName = j3dFixed;
                        found = true;
                    }
                }
            }
            int fontStyleId = Font.PLAIN;
            if (fs.style.string != null) {
                if (vrmlPlain.equals(fs.style.string)) {
                    fontStyleId = Font.PLAIN;
                }
                else if (vrmlBold.equals(fs.style.string)) {
                    fontStyleId = Font.BOLD;
                }
                else if (vrmlItalic.equals(fs.style.string)) {
                    fontStyleId = Font.ITALIC;
                }
                else if (vrmlBoldItalic.equals(fs.style.string)) {
                    fontStyleId = Font.BOLD + Font.ITALIC;
                }
            }

            impl = new TransformGroup();

            // TODO: handle multiple strings by making multiple Text3D's
            // in transform groups to offset them correctly
            Text2D text = new Text2D(string.strings[0], fontColor,
                    fontName, RASTER_SIZE, fontStyleId);

            impl.addChild(text);

            Transform3D textXform = new Transform3D();

            float fontSize = RASTER_SIZE * text.getRectangleScaleFactor();
            float scale = fs.size.value / fontSize;

            textXform.setScale(scale);
            impl.setTransform(textXform);

            implNode = impl;
        }
        return impl;
    }

    /**
     *  Gets the implGeom attribute of the Text object
     *
     *@return  The implGeom value
     */
    public org.jogamp.java3d.Geometry getImplGeom() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public boolean haveTexture() {
        return false;
    }

    /**
     *  Gets the numTris attribute of the Text object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        return 0;
    }

    /**
     *  Gets the boundingBox attribute of the Text object
     *
     *@return  The boundingBox value
     */
    public org.jogamp.java3d.BoundingBox getBoundingBox() {
        return null;
    }


    /**
     *  Gets the type attribute of the Text object
     *
     *@return  The type value
     */
    public String getType() {
        return "Text";
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Text(loader, (MFString) string.clone(),
                (SFNode) fontStyle.clone(), (MFFloat) length.clone(),
                (SFFloat) maxExtent.clone());
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        System.out.println("Text: unimplemented event " + eventInName);
    }

    /**  Description of the Method */
    void initFields() {
        string.init(this, FieldSpec, Field.EXPOSED_FIELD, "string");
        fontStyle.init(this, FieldSpec, Field.EXPOSED_FIELD, "fontStyle");
        length.init(this, FieldSpec, Field.EXPOSED_FIELD, "length");
        maxExtent.init(this, FieldSpec, Field.EXPOSED_FIELD, "maxExtent");
    }
}

