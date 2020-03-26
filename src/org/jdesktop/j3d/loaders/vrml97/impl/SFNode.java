/*
 * $RCSfile: SFNode.java,v $
 *
 *      @(#)SFNode.java 1.19 98/11/05 20:35:02
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
 * $Date: 2005/02/03 23:07:01 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Observable;
import java.util.Observer;

/**  Description of the Class */
public class SFNode extends Field {

    BaseNode node;
    BaseNode initNode;

    /**Constructor for the SFNode object */
    public SFNode() {
        node = null;
        initNode = null;
    }

    /**
     *Constructor for the SFNode object
     *
     *@param  node Description of the Parameter
     */
    public SFNode(BaseNode node) {
        setValue(node);
        initNode = node;
    }

    /**  Description of the Method */
    void reset() {
        node = initNode;
    }

    /**
     *  Gets the value attribute of the SFNode object
     *
     *@return  The value value
     */
    public BaseNode getValue() {
        return node;
    }

    /**
     *  Sets the value attribute of the SFNode object
     *
     *@param  node The new value value
     */
    public void setValue(BaseNode node) {
        this.node = node;
        route();
    }

    /**
     *  Sets the value attribute of the SFNode object
     *
     *@param  sfnode The new value value
     */
    public void setValue(SFNode sfnode) {
        setValue(sfnode.node);
    }

    /**
     *  Sets the value attribute of the SFNode object
     *
     *@param  csfnode The new value value
     */
    public void setValue(ConstSFNode csfnode) {
        setValue((SFNode) csfnode.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        SFNode clone;
        clone = new SFNode(node);
        if (node != null) {
            BaseNode cloneNode = (BaseNode) node.clone();
            clone.node = cloneNode;
            node.loader.registerClone(node, cloneNode);
            node.loader.cleanUp();
        }
        return clone;
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((SFNode) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstSFNode(this);
        }
        return constField;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.SFNode(this);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toString() {
        return node + "\n";
    }

}

