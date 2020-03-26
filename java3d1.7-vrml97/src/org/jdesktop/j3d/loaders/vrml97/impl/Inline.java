/*
 * $RCSfile: Inline.java,v $
 *
 *      @(#)Inline.java 1.15 98/11/05 20:34:35
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
 * $Revision: 1.3 $
 * $Date: 2006/03/30 08:19:29 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 * @author  Nikolai V. Chr.
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

/**  Description of the Class */
public class Inline extends Group {

    // field
    MFString url;

    // field
    SFVec3f bboxCenter;
    SFVec3f bboxSize;

    /**
     *Constructor for the Inline object
     *
     *@param  loader Description of the Parameter
     */
    public Inline(Loader loader) {
        super(loader);
        url = new MFString();
        bboxCenter = new SFVec3f(0, 0, 0);
        bboxSize = new SFVec3f(-1, -1, -1);
        initInlineFields();
    }

    /**
     *Constructor for the Inline object
     *
     *@param  loader Description of the Parameter
     *@param  url Description of the Parameter
     *@param  bboxCenter Description of the Parameter
     *@param  bboxSize Description of the Parameter
     */
    Inline(Loader loader, MFString url, SFVec3f bboxCenter,
            SFVec3f bboxSize) {
        super(loader);
        this.url = url;
        this.bboxCenter = bboxCenter;
        this.bboxSize = bboxSize;
        initInlineFields();
    }

    /**  Description of the Method */
    void initImpl() {
        impl = new org.jogamp.java3d.Group();
        implNode = implGroup = impl;
        impl.setUserData(new Vector());
        loadURL();
        implReady = true;
    }

    /**  Description of the Method */
    void loadURL() {
        // load the URL
        Scene scene = null;
        URL loadURL;
        try {
            loadURL = loader.stringToURL(url.strings[0]);
        }
        catch (java.net.MalformedURLException ue) {
			vrml.InvalidVRMLSyntaxException i = new vrml.InvalidVRMLSyntaxException(
                    "Bad URL readling Inline: " + url.strings[0]);
			i.initCause(ue);
			throw i;
        }
        try {
            scene = loader.load(loadURL);
        }
        catch (java.io.IOException e) {
            vrml.InvalidVRMLSyntaxException i = new vrml.InvalidVRMLSyntaxException(
                    "IOException reading Inline:" + loadURL);
			i.initCause(e);
			throw i;
        }

        // Add the top level nodes to this group
        BaseNode nodes[] = new BaseNode[scene.objects.size()];
        int i = 0;
        for (Enumeration e = scene.objects.elements(); e.hasMoreElements(); i++) {
            nodes[i] = (BaseNode) e.nextElement();
        }
        children.nodes = nodes;
        replaceChildren();

        // Everything else gets added to the loader's current scene
        loader.scene.viewpoints.addAll(scene.viewpoints);
        loader.scene.navInfos.addAll(scene.navInfos);
        loader.scene.backgrounds.addAll(scene.backgrounds);
        loader.scene.fogs.addAll(scene.fogs);
        loader.scene.lights.addAll(scene.lights);
        loader.scene.sharedGroups.addAll(scene.sharedGroups);
        loader.scene.timeSensors.addAll(scene.timeSensors);
        loader.scene.visibilitySensors.addAll(scene.visibilitySensors);
        loader.scene.touchSensors.addAll(scene.touchSensors);
        loader.scene.audioClips.addAll(scene.audioClips);

    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("url")) {
            loadURL();
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Inline(loader, (MFString) url.clone(),
                (SFVec3f) bboxSize.clone(),
                (SFVec3f) bboxCenter.clone());
    }

    /**
     *  Gets the type attribute of the Inline object
     *
     *@return  The type value
     */
    public String getType() {
        return "Inline";
    }

    /**  Description of the Method */
    void initFields() {
        super.initFields();
        initInlineFields();
    }

    /**  Description of the Method */
    void initInlineFields() {
        url.init(this, FieldSpec, Field.EXPOSED_FIELD, "url");
        bboxCenter.init(this, FieldSpec, Field.FIELD, "bboxCenter");
        bboxSize.init(this, FieldSpec, Field.FIELD, "bboxSize");
    }
}

