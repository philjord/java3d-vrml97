/*
 * $RCSfile: MFInt32.java,v $
 *
 *      @(#)MFInt32.java 1.9 98/11/05 20:40:31
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

/**  Description of the Class */
public class MFInt32 extends vrml.MField {
    org.jdesktop.j3d.loaders.vrml97.impl.MFInt32 impl;

    /**
     *Constructor for the MFInt32 object
     *
     *@param  init Description of the Parameter
     */
    public MFInt32(org.jdesktop.j3d.loaders.vrml97.impl.MFInt32 init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new MFInt32(
                (org.jdesktop.j3d.loaders.vrml97.impl.MFInt32) impl.clone());
    }

    /**
     *  Gets the value attribute of the MFInt32 object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(int[] values) {
        impl.getValue(values);
    }

    /**
     *  Sets the value attribute of the MFInt32 object
     *
     *@param  values The new value value
     */
    public void setValue(int[] values) {
        impl.setValue(values);
    }

    /**
     *  Sets the value attribute of the MFInt32 object
     *
     *@param  size The new value value
     *@param  values The new value value
     */
    public void setValue(int size, int[] values) {
        impl.setValue(size, values);
    }

    /**
     *  Sets the value attribute of the MFInt32 object
     *
     *@param  f The new value value
     */
    public void setValue(ConstMFInt32 f) {
        impl.setValue(f.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@return  Description of the Return Value
     */
    public int get1Value(int index) {
        return impl.get1Value(index);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, int f) {
        impl.set1Value(index, f);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, ConstSFInt32 f) {
        impl.set1Value(index, f.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, SFInt32 f) {
        impl.set1Value(index, f.impl);
    }

    /**
     *  Adds a feature to the Value attribute of the MFInt32 object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(int f) {
    }

    /**
     *  Adds a feature to the Value attribute of the MFInt32 object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(ConstSFInt32 f) {
        impl.addValue(f.impl);
    }

    /**
     *  Adds a feature to the Value attribute of the MFInt32 object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(SFInt32 f) {
        impl.addValue(f.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, int f) {
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, ConstSFInt32 f) {
        impl.insertValue(index, f.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, SFInt32 f) {
        impl.insertValue(index, f.impl);
    }

    /**
     *  Gets the size attribute of the MFInt32 object
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
     *@param  i Description of the Parameter
     */
    public void delete(int i) {
        impl.delete(i);
    }

}

