/*
 * $RCSfile: FontStyle.java,v $
 *
 *      @(#)FontStyle.java 1.13 98/11/05 20:34:29
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
 * $Date: 2005/02/03 23:06:56 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class FontStyle extends Node {
    //Field
    MFString family;
    SFBool horizontal;
    MFString justify;
    SFString language;
    SFBool leftToRight;
    SFFloat size;
    SFFloat spacing;
    SFString style;
    SFBool topToBottom;

    String[] s = new String[1];

    /**
     *Constructor for the FontStyle object
     *
     *@param  loader Description of the Parameter
     */
    public FontStyle(Loader loader) {
        super(loader);
        s[0] = "SERIF";
        family = new MFString(s);
        horizontal = new SFBool(true);
        s[0] = "BEGIN";
        justify = new MFString(s);
        language = new SFString();
        leftToRight = new SFBool(true);
        size = new SFFloat(1.0f);
        spacing = new SFFloat(1.0f);
        style = new SFString("PLAIN");
        topToBottom = new SFBool(true);
        initFields();
    }

    /**
     *Constructor for the FontStyle object
     *
     *@param  loader Description of the Parameter
     *@param  family Description of the Parameter
     *@param  horizontal Description of the Parameter
     *@param  justify Description of the Parameter
     *@param  language Description of the Parameter
     *@param  leftToRight Description of the Parameter
     *@param  size Description of the Parameter
     *@param  spacing Description of the Parameter
     *@param  style Description of the Parameter
     *@param  topToBottom Description of the Parameter
     */
    public FontStyle(Loader loader, MFString family,
            SFBool horizontal, MFString justify,
            SFString language, SFBool leftToRight,
            SFFloat size, SFFloat spacing,
            SFString style, SFBool topToBottom) {

        super(loader);
        this.family = family;
        this.horizontal = horizontal;
        this.justify = justify;
        this.language = language;
        this.leftToRight = leftToRight;
        this.size = size;
        this.spacing = spacing;
        this.style = style;
        this.topToBottom = topToBottom;
        initFields();
    }

    /**  Description of the Method */
    void initFields() {
        family.init(this, FieldSpec, Field.FIELD, "family");
        horizontal.init(this, FieldSpec, Field.FIELD, "horizontal");
        justify.init(this, FieldSpec, Field.FIELD, "justify");
        language.init(this, FieldSpec, Field.FIELD, "language");
        leftToRight.init(this, FieldSpec, Field.FIELD, "leftToRight");
        size.init(this, FieldSpec, Field.FIELD, "size");
        spacing.init(this, FieldSpec, Field.FIELD, "spacing");
        style.init(this, FieldSpec, Field.FIELD, "style");
        topToBottom.init(this, FieldSpec, Field.FIELD, "horizontal");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new FontStyle(loader, (MFString) family.clone(),
                (SFBool) horizontal.clone(), (MFString) justify.clone(),
                (SFString) language.clone(), (SFBool) leftToRight.clone(),
                (SFFloat) size.clone(), (SFFloat) spacing.clone(),
                (SFString) style.clone(), (SFBool) topToBottom.clone());
    }

    /**
     *  Gets the type attribute of the FontStyle object
     *
     *@return  The type value
     */
    public String getType() {
        return "FontStyle";
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        ;
    }
}

