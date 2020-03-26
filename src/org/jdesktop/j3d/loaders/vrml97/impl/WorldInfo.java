/*
 * $RCSfile: WorldInfo.java,v $
 *
 *      @(#)WorldInfo.java 1.19 98/11/05 20:35:33
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
 * $Date: 2005/02/03 23:07:05 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class WorldInfo extends Node {

    //field
    MFString info;
    SFString title;

    /**
     *Constructor for the WorldInfo object
     *
     *@param  loader Description of the Parameter
     */
    public WorldInfo(Loader loader) {
        super(loader);
        info = new MFString();
        String[] infoStrings = new String[1];
        infoStrings[0] = "";
        info.setValue(infoStrings);
        title = new SFString();
        initFields();
        loader.setWorldInfo(this);
    }

    /**
     *Constructor for the WorldInfo object
     *
     *@param  loader Description of the Parameter
     *@param  info Description of the Parameter
     *@param  title Description of the Parameter
     */
    public WorldInfo(Loader loader, MFString info, SFString title) {
        super(loader);
        this.info = info;
        this.title = title;
        initFields();
        loader.setWorldInfo(this);
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("title")) {
            loader.setDescription(title.getValue());
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new WorldInfo(loader, (MFString) info.clone(),
                (SFString) title.clone());
    }

    /**
     *  Gets the type attribute of the WorldInfo object
     *
     *@return  The type value
     */
    public String getType() {
        return "WorldInfo";
    }

    /**  Description of the Method */
    void initFields() {
        info.init(this, FieldSpec, Field.FIELD, "info");
        title.init(this, FieldSpec, Field.FIELD, "title");
    }

}

