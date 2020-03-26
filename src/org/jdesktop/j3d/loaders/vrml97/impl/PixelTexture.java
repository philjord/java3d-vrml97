/*
 * $RCSfile: PixelTexture.java,v $
 *
 *      @(#)PixelTexture.java 1.16 98/11/05 20:34:52
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
 * $Date: 2005/02/03 23:06:59 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class PixelTexture extends Node {

    SFImage image;// exposedField
    SFBool repeatS;// field
    SFBool repeatT;// field

    /**
     *Constructor for the PixelTexture object
     *
     *@param  loader Description of the Parameter
     */
    public PixelTexture(Loader loader) {

        super(loader);
        image = new SFImage(0, 0, 0, new byte[1]);
        repeatS = new SFBool(true);
        repeatT = new SFBool(true);

        initFields();
    }

    /**
     *Constructor for the PixelTexture object
     *
     *@param  loader Description of the Parameter
     *@param  initImage Description of the Parameter
     *@param  initRepeatS Description of the Parameter
     *@param  initRepeatT Description of the Parameter
     */
    public PixelTexture(Loader loader, SFImage initImage, SFBool initRepeatS,
            SFBool initRepeatT) {
        super(loader);
        image = initImage;
        repeatS = initRepeatS;
        repeatT = initRepeatT;

        initFields();
    }

    /**  Description of the Method */
    void initFields() {
        image.init(this, FieldSpec, Field.EXPOSED_FIELD, "image");
        repeatS.init(this, FieldSpec, Field.FIELD, "repeatS");
        repeatT.init(this, FieldSpec, Field.FIELD, "repeatT");
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

    /**
     *  Gets the type attribute of the PixelTexture object
     *
     *@return  The type value
     */
    public String getType() {
        return "PixelTexture";
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        PixelTexture pt = new PixelTexture(loader,
                (SFImage) image.clone(),
                (SFBool) repeatS.clone(),
                (SFBool) repeatT.clone());
        return pt;
    }

}

