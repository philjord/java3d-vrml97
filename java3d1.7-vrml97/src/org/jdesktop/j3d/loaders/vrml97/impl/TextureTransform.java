/*
 * $RCSfile: TextureTransform.java,v $
 *
 *      @(#)TextureTransform.java 1.16 98/11/05 20:35:26
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

/**  Description of the Class */
public class TextureTransform extends Node {

    // exposedField
    SFVec2f center;
    SFFloat rotation;
    SFVec2f scale;
    SFVec2f translation;

    Appearance owner;

    /**
     *Constructor for the TextureTransform object
     *
     *@param  loader Description of the Parameter
     */
    public TextureTransform(Loader loader) {
        super(loader);
        center = new SFVec2f(0.0f, 0.0f);
        rotation = new SFFloat(0.0f);
        scale = new SFVec2f(1.0f, 1.0f);
        translation = new SFVec2f(0.0f, 0.0f);
        initFields();
    }


    /**
     *Constructor for the TextureTransform object
     *
     *@param  loader Description of the Parameter
     *@param  center Description of the Parameter
     *@param  rotation Description of the Parameter
     *@param  scale Description of the Parameter
     *@param  translation Description of the Parameter
     */
    public TextureTransform(Loader loader, SFVec2f center, SFFloat rotation,
            SFVec2f scale, SFVec2f translation) {
        super(loader);
        this.center = center;
        this.rotation = rotation;
        this.scale = scale;
        this.translation = translation;
        initFields();
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        owner.updateTextureTransform();
    }

    /**
     *  Gets the type attribute of the TextureTransform object
     *
     *@return  The type value
     */
    public String getType() {
        return "TextureTransform";
    }


    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        TextureTransform c = new TextureTransform(
                loader,
                (SFVec2f) center.clone(),
                (SFFloat) rotation.clone(),
                (SFVec2f) scale.clone(),
                (SFVec2f) translation.clone());

        return c;
    }

    /**  Description of the Method */
    void initFields() {
        center.init(this, FieldSpec, Field.EXPOSED_FIELD, "center");
        rotation.init(this, FieldSpec, Field.EXPOSED_FIELD, "rotation");
        scale.init(this, FieldSpec, Field.EXPOSED_FIELD, "scale");
        translation.init(this, FieldSpec, Field.EXPOSED_FIELD, "translation");
    }

}

