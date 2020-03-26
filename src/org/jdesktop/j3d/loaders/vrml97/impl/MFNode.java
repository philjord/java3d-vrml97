/*
 * $RCSfile: MFNode.java,v $
 *
 *      @(#)MFNode.java 1.18 98/11/05 20:34:40
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
 * $Revision: 1.2 $
 * $Date: 2005/02/03 23:06:57 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Observable;
import java.util.Observer;

/**  Description of the Class */
public class MFNode extends MField {

    BaseNode[] nodes;

    /**Constructor for the MFNode object */
    public MFNode() {
        nodes = new BaseNode[0];
    }

    /**
     *Constructor for the MFNode object
     *
     *@param  values Description of the Parameter
     */
    public MFNode(BaseNode[] values) {
        nodes = new BaseNode[values.length];
        System.arraycopy(values, 0, nodes, 0, values.length);
    }

    /**
     *  Gets the value attribute of the MFNode object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(BaseNode values[]) {
        System.arraycopy(nodes, 0, values, 0, nodes.length);
    }

    /**
     *  Gets the value attribute of the MFNode object
     *
     *@return  The value value
     */
    public BaseNode[] getValue() {
        return nodes;
    }


    /**
     *  Sets the value attribute of the MFNode object
     *
     *@param  values The new value value
     */
    public void setValue(BaseNode[] values) {
        nodes = new BaseNode[values.length];
        System.arraycopy(values, 0, nodes, 0, values.length);
        route();
    }

    /**
     *  Sets the value attribute of the MFNode object
     *
     *@param  size The new value value
     *@param  value The new value value
     */
    public void setValue(int size, BaseNode[] value) {
        // does this really mean take the first size of value?
        setValue(value);
    }

    /**
     *  Sets the value attribute of the MFNode object
     *
     *@param  node The new value value
     */
    public void setValue(MFNode node) {
        setValue((BaseNode[]) node.nodes);
    }

    /**
     *  Sets the value attribute of the MFNode object
     *
     *@param  constNode The new value value
     */
    public void setValue(ConstMFNode constNode) {
        setValue((MFNode) constNode.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@return  Description of the Return Value
     */
    public BaseNode get1Value(int index) {
        return (nodes[index]);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, BaseNode f) {
        nodes[index] = f;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  csfn Description of the Parameter
     */
    public void set1Value(int index, ConstSFNode csfn) {
        set1Value(index, (SFNode) csfn.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  sfnod Description of the Parameter
     */
    public void set1Value(int index, SFNode sfnod) {
        set1Value(index, sfnod.node);
    }

    /**
     *  Adds a feature to the Value attribute of the MFNode object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(BaseNode f) {
        BaseNode[] temp = new BaseNode[nodes.length + 1];
        for (int i = 0; i < temp.length - 1; i++) {
            temp[i] = nodes[i];
        }
        temp[temp.length - 1] = f;
        nodes = temp;

        route();
    }

    /**
     *  Adds a feature to the Value attribute of the MFNode object
     *
     *@param  constsfnod The feature to be added to the Value attribute
     */
    public void addValue(ConstSFNode constsfnod) {
        addValue((SFNode) constsfnod.ownerField);
    }

    /**
     *  Adds a feature to the Value attribute of the MFNode object
     *
     *@param  sfnod The feature to be added to the Value attribute
     */
    public void addValue(SFNode sfnod) {
        addValue(sfnod.node);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, BaseNode f) {
        BaseNode[] temp = new BaseNode[nodes.length + 1];
        for (int i = 0; i < index; i++) {
            temp[i] = nodes[i];
        }
        temp[index] = f;
        for (int i = index + 1; i < temp.length + 1; i++) {
            temp[i] = nodes[i - 1];
        }
        nodes = temp;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constsfnod Description of the Parameter
     */
    public void insertValue(int index, ConstSFNode constsfnod) {
        insertValue(index, (SFNode) constsfnod.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constsfnod Description of the Parameter
     */
    public void insertValue(int index, SFNode constsfnod) {
        insertValue(index, constsfnod.node);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public synchronized void update(Field field) {
        setValue((MFNode) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        MFNode ref = new MFNode(nodes);
        // deep copy
        for (int i = 0; i < ref.nodes.length; i++) {
            if (nodes[i] != null) {
                BaseNode cloneNode = (BaseNode) nodes[i].clone();
                ref.nodes[i] = cloneNode;
                if (nodes[i].loader.debug) {
                    System.out.println("MFNode.clone(): child " + i +
                            " = " + cloneNode.toStringId() + " = " + cloneNode);
                }
                nodes[i].loader.registerClone(nodes[i], cloneNode);
                nodes[i].loader.cleanUp();
            }
        }
        return ref;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstMFNode(this);
        }
        return constField;
    }

    /**
     *  Gets the size attribute of the MFNode object
     *
     *@return  The size value
     */
    public int getSize() {
        return nodes.length;
    }

    /**  Description of the Method */
    public void clear() {
        nodes = null;
    }
    // todo
    /**
     *  Description of the Method
     *
     *@param  n Description of the Parameter
     */
    public void delete(int n) {
        ;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.MFNode(this);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toString() {
        String retval = "[\n";
        for (int i = 0; i < nodes.length; i++) {
            retval += nodes[i] + "\n";
        }
        retval += "]\n";
        return retval;
    }
}

