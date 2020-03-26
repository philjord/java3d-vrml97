/*
 * $RCSfile: MFNode.java,v $
 *
 *      @(#)MFNode.java 1.9 98/11/05 20:40:32
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
 * $Date: 2005/02/03 23:07:14 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package vrml.field;

import vrml.BaseNode;
import vrml.Field;

/**  Description of the Class */
public class MFNode extends vrml.MField {
    org.jdesktop.j3d.loaders.vrml97.impl.MFNode impl;

    /**
     *Constructor for the MFNode object
     *
     *@param  init Description of the Parameter
     */
    public MFNode(org.jdesktop.j3d.loaders.vrml97.impl.MFNode init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new MFNode(
                (org.jdesktop.j3d.loaders.vrml97.impl.MFNode) impl.clone());
    }

    /**
     *Constructor for the MFNode object
     *
     *@param  nodes Description of the Parameter
     */
    MFNode(BaseNode nodes[]) {
        super(null);
        org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes =
                new org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            implNodes[i] = Field.getImpl(nodes[i]);
        }
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFNode(implNodes);
        implField = impl;
    }

    /**
     *  Gets the value attribute of the MFNode object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(BaseNode values[]) {
        org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes = impl.getValue();
        for (int i = 0; i < implNodes.length; i++) {
            values[i] = implNodes[i].wrap();
        }
    }

    /**
     *  Gets the value attribute of the MFNode object
     *
     *@return  The value value
     */
    public BaseNode[] getValue() {
        org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes = impl.getValue();
        BaseNode[] nodes = new BaseNode[implNodes.length];
        for (int i = 0; i < implNodes.length; i++) {
            nodes[i] = implNodes[i].wrap();
        }
        return nodes;
    }

    /**
     *  Sets the value attribute of the MFNode object
     *
     *@param  values The new value value
     */
    public void setValue(BaseNode[] values) {
        org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes =
                new org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[values.length];
        for (int i = 0; i < values.length; i++) {
            implNodes[i] = Field.getImpl(values[i]);
        }
        impl.setValue(implNodes);
    }

    /**
     *  Sets the value attribute of the MFNode object
     *
     *@param  size The new value value
     *@param  values The new value value
     */
    public void setValue(int size, BaseNode[] values) {
        org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[] implNodes =
                new org.jdesktop.j3d.loaders.vrml97.impl.BaseNode[size];
        for (int i = 0; i < size; i++) {
            implNodes[i] = Field.getImpl(values[i]);
        }
        impl.setValue(size, implNodes);
    }

    /**
     *  Sets the value attribute of the MFNode object
     *
     *@param  n The new value value
     */
    public void setValue(ConstMFNode n) {
        impl.setValue(n.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@return  Description of the Return Value
     */
    public BaseNode get1Value(int index) {
        return impl.get1Value(index).wrap();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  n Description of the Parameter
     */
    public void set1Value(int index, BaseNode n) {
        impl.set1Value(index, vrml.Field.getImpl(n));
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  n Description of the Parameter
     */
    public void set1Value(int index, ConstSFNode n) {
        impl.set1Value(index, n.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  n Description of the Parameter
     */
    public void set1Value(int index, SFNode n) {
        impl.set1Value(index, n.impl);
    }

    /**
     *  Adds a feature to the Value attribute of the MFNode object
     *
     *@param  n The feature to be added to the Value attribute
     */
    public void addValue(BaseNode n) {
        impl.addValue(vrml.Field.getImpl(n));
    }

    /**
     *  Adds a feature to the Value attribute of the MFNode object
     *
     *@param  n The feature to be added to the Value attribute
     */
    public void addValue(ConstSFNode n) {
        impl.addValue(n.impl);
    }

    /**
     *  Adds a feature to the Value attribute of the MFNode object
     *
     *@param  n The feature to be added to the Value attribute
     */
    public void addValue(SFNode n) {
        impl.addValue(n.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  n Description of the Parameter
     */
    public void insertValue(int index, BaseNode n) {
        impl.insertValue(index, vrml.Field.getImpl(n));
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  n Description of the Parameter
     */
    public void insertValue(int index, ConstSFNode n) {
        impl.insertValue(index, n.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  n Description of the Parameter
     */
    public void insertValue(int index, SFNode n) {
        impl.insertValue(index, n.impl);
    }

    /**
     *  Gets the size attribute of the MFNode object
     *
     *@return  The size value
     */
    public int getSize() {
        return impl.getSize();
    }

    /**  Description of the Method */
    public void clear() {
        impl.clear();
    }

    /**
     *  Description of the Method
     *
     *@param  n Description of the Parameter
     */
    public void delete(int n) {
        impl.delete(n);
    }
}

