/*
 * $RCSfile: MFRotation.java,v $
 *
 *      @(#)MFRotation.java 1.9 98/11/05 20:40:33
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
public class MFRotation extends vrml.MField {
    org.jdesktop.j3d.loaders.vrml97.impl.MFRotation impl;

    /**
     *Constructor for the MFRotation object
     *
     *@param  init Description of the Parameter
     */
    public MFRotation(org.jdesktop.j3d.loaders.vrml97.impl.MFRotation init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new MFRotation(
                (org.jdesktop.j3d.loaders.vrml97.impl.MFRotation) impl.clone());
    }

    /**
     *Constructor for the MFRotation object
     *
     *@param  values Description of the Parameter
     */
    public MFRotation(float[][] values) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFRotation(values);
        implField = impl;
    }

    /**
     *  Gets the value attribute of the MFRotation object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float[][] values) {
        impl.getValue(values);
    }

    /**
     *  Gets the value attribute of the MFRotation object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float[] values) {
        impl.getValue(values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  values Description of the Parameter
     */
    public void get1Value(int index, float[] values) {
        impl.get1Value(index, values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void get1Value(int index, SFRotation vec) {
        impl.get1Value(index, vec.impl);
    }

    /**
     *  Sets the value attribute of the MFRotation object
     *
     *@param  values The new value value
     */
    public void setValue(float[][] values) {
        impl.setValue(values);
    }

    /**
     *  Sets the value attribute of the MFRotation object
     *
     *@param  values The new value value
     */
    public void setValue(float[] values) {
        impl.setValue(values);
    }

    /**
     *  Sets the value attribute of the MFRotation object
     *
     *@param  size The new value value
     *@param  values The new value value
     */
    public void setValue(int size, float[] values) {
        impl.setValue(size, values);
    }

    /**
     *  Sets the value attribute of the MFRotation object
     *
     *@param  values The new value value
     */
    public void setValue(ConstMFRotation values) {
        impl.setValue(values.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void set1Value(int index, ConstSFRotation constvec) {
        impl.set1Value(index, constvec.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void set1Value(int index, SFRotation vec) {
        impl.set1Value(index, vec.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  z Description of the Parameter
     *@param  angle Description of the Parameter
     */
    public void set1Value(int index, float x, float y, float z, float angle) {
        impl.set1Value(index, x, y, z, angle);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void insertValue(int index, ConstSFRotation constvec) {
        impl.set1Value(index, constvec.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void insertValue(int index, SFRotation vec) {
        impl.set1Value(index, vec.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  z Description of the Parameter
     *@param  angle Description of the Parameter
     */
    public void insertValue(int index, float x, float y, float z, float angle) {
        impl.insertValue(index, x, y, z, angle);
    }

    /**
     *  Gets the size attribute of the MFRotation object
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

