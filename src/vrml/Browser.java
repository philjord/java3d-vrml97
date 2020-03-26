/*
 * $RCSfile: Browser.java,v $
 *
 *      @(#)Browser.java 1.14 98/08/05 14:31:21
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
 * $Date: 2005/02/03 23:07:10 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package vrml;
import org.jogamp.java3d.Canvas3D;

import vrml.node.Node;

/**  Description of the Class */
public class Browser {

    org.jdesktop.j3d.loaders.vrml97.impl.Browser impl;

    /**
     *Constructor for the Browser object
     *
     *@param  b Description of the Parameter
     */
    public Browser(org.jdesktop.j3d.loaders.vrml97.impl.Browser b) {
        impl = b;
    }

    /**Constructor for the Browser object */
    public Browser() {
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.Browser();
    }

    /**
     *Constructor for the Browser object
     *
     *@param  canvas Description of the Parameter
     */
    public Browser(Canvas3D canvas) {
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.Browser(canvas);
    }

    /**
     *  Gets the description attribute of the Browser object
     *
     *@return  The description value
     */
    public String getDescription() {
        return impl.getDescription();
    }

    /**
     *  Gets the name attribute of the Browser object
     *
     *@return  The name value
     */
    public String getName() {
        return impl.getName();
    }

    /**
     *  Gets the version attribute of the Browser object
     *
     *@return  The version value
     */
    public String getVersion() {
        return impl.getVersion();
    }

    /**
     *  Gets the currentSpeed attribute of the Browser object
     *
     *@return  The currentSpeed value
     */
    public float getCurrentSpeed() {
        return impl.getCurrentSpeed();
    }

    /**
     *  Gets the currentFrameRate attribute of the Browser object
     *
     *@return  The currentFrameRate value
     */
    public float getCurrentFrameRate() {
        return impl.getCurrentFrameRate();
    }

    /**
     *  Gets the worldURL attribute of the Browser object
     *
     *@return  The worldURL value
     */
    public String getWorldURL() {
        return impl.getWorldURL();
    }

    /**
     *  Gets the uRL attribute of the Browser object
     *
     *@return  The uRL value
     */
    public java.net.URL getURL() {
        return impl.getURL();
    }

    /**
     *  Description of the Method
     *
     *@param  nodes Description of the Parameter
     */
    public void replaceWorld(BaseNode[] nodes) {
        org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes =
                new org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            implNodes[i] = nodes[i].getImpl();
        }
        impl.replaceWorld(implNodes);
    }

    /**
     *  Description of the Method
     *
     *@param  vrmlSyntax Description of the Parameter
     *@return  Description of the Return Value
     *@exception  InvalidVRMLSyntaxException Description of the Exception
     */
    public BaseNode[] createVrmlFromString(String vrmlSyntax)
             throws InvalidVRMLSyntaxException {
        org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes =
                impl.createVrmlFromString(vrmlSyntax);
        BaseNode[] nodes = new BaseNode[implNodes.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = implNodes[i].wrap();
        }
        return nodes;
    }

    /**
     *  Description of the Method
     *
     *@param  url Description of the Parameter
     *@param  node Description of the Parameter
     *@param  event Description of the Parameter
     *@exception  InvalidVRMLSyntaxException Description of the Exception
     */
    public void createVrmlFromURL(String[] url, BaseNode node, String event)
             throws InvalidVRMLSyntaxException {
        impl.createVrmlFromURL(url, node.getImpl(), event);
    }

    /**
     *  Adds a feature to the Route attribute of the Browser object
     *
     *@param  fromNode The feature to be added to the Route attribute
     *@param  fromEventOut The feature to be added to the Route attribute
     *@param  toNode The feature to be added to the Route attribute
     *@param  toEventIn The feature to be added to the Route attribute
     */
    public void addRoute(BaseNode fromNode, String fromEventOut,
            BaseNode toNode, String toEventIn) {
        impl.addRoute(fromNode.getImpl(), fromEventOut, toNode.getImpl(),
                toEventIn);
    }

    /**
     *  Description of the Method
     *
     *@param  fromNode Description of the Parameter
     *@param  fromEventOut Description of the Parameter
     *@param  toNode Description of the Parameter
     *@param  toEventIn Description of the Parameter
     */
    public void deleteRoute(BaseNode fromNode, String fromEventOut,
            BaseNode toNode, String toEventIn) {
        impl.deleteRoute(fromNode.getImpl(), fromEventOut, toNode.getImpl(),
                toEventIn);
    }

    /**
     *  Description of the Method
     *
     *@param  url Description of the Parameter
     *@param  parameter Description of the Parameter
     *@exception  InvalidVRMLSyntaxException Description of the Exception
     *@exception  java.net.MalformedURLException Description of the Exception
     *@exception  java.io.IOException Description of the Exception
     */
    public void loadURL(String[] url, String[] parameter)
             throws InvalidVRMLSyntaxException, java.net.MalformedURLException,
            java.io.IOException {
        impl.loadURL(url, parameter);
    }

    /**
     *  Description of the Method
     *
     *@param  sourceVrml Description of the Parameter
     */
    public void loadStringAsVrml(String sourceVrml) {
        impl.loadStringAsVrml(sourceVrml);
    }

    /**
     *  Sets the description attribute of the Browser object
     *
     *@param  description The new description value
     */
    public void setDescription(String description) {
        impl.setDescription(description);
    }

    /**
     *  Sets the viewpoint attribute of the Browser object
     *
     *@param  vi The new viewpoint value
     */
    public void setViewpoint(int vi) {
        impl.setViewpoint(vi);
    }

    /**  Description of the Method */
    public void resetViewpoint() {
        impl.resetViewpoint();
    }

    /**
     *  Gets the viewpointDescriptions attribute of the Browser object
     *
     *@return  The viewpointDescriptions value
     */
    public String[] getViewpointDescriptions() {
        return impl.getViewpointDescriptions();
    }

    /**  Description of the Method */
    public void outputTiming() {
        impl.outputTiming();
    }

    /**  Description of the Method */
    public void shutDown() {
        impl.shutDown();
    }

    /**  Description of the Method */
    public void startRender() {
        impl.startRender();
    }

    /**  Description of the Method */
    public void stopRender() {
        impl.stopRender();
    }

    /**
     *  Gets the canvas3D attribute of the Browser object
     *
     *@return  The canvas3D value
     */
    public Canvas3D getCanvas3D() {
        return impl.getCanvas3D();
    }

    /**
     *  Sets the aWTContainer attribute of the Browser object
     *
     *@param  container The new aWTContainer value
     */
    public void setAWTContainer(java.awt.Container container) {
        impl.setAWTContainer(container);
    }

    // annoying "feature" of default creaseAngle == 0.0
    /**
     *  Sets the autoSmooth attribute of the Browser object
     *
     *@param  s The new autoSmooth value
     */
    public void setAutoSmooth(boolean s) {
        impl.setAutoSmooth(s);
    }

}

