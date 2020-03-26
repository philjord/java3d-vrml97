/*
 * $RCSfile: MFTime.java,v $
 *
 *      @(#)MFTime.java 1.9 98/11/05 20:40:34
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
public class MFTime extends vrml.MField {
    org.jdesktop.j3d.loaders.vrml97.impl.MFTime impl;

    /**
     *Constructor for the MFTime object
     *
     *@param  init Description of the Parameter
     */
    public MFTime(org.jdesktop.j3d.loaders.vrml97.impl.MFTime init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new MFTime((org.jdesktop.j3d.loaders.vrml97.impl.MFTime) impl.clone());
    }

    /**
     *  Gets the size attribute of the MFTime object
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

    /**
     *Constructor for the MFTime object
     *
     *@param  time Description of the Parameter
     */
    public MFTime(double[] time) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.MFTime(time);
        implField = impl;
    }

    /**
     *  Gets the value attribute of the MFTime object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(double[] values) {
        impl.getValue(values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@return  Description of the Return Value
     */
    public double get1Value(int index) {
        return impl.get1Value(index);
    }

    /**
     *  Sets the value attribute of the MFTime object
     *
     *@param  values The new value value
     */
    public void setValue(double[] values) {
        impl.setValue(values);
    }

    /**
     *  Sets the value attribute of the MFTime object
     *
     *@param  size The new value value
     *@param  values The new value value
     */
    public void setValue(int size, double[] values) {
        impl.setValue(size, values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  d Description of the Parameter
     */
    public void set1Value(int index, double d) {
        impl.set1Value(index, d);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  t Description of the Parameter
     */
    public void set1Value(int index, ConstSFTime t) {
        impl.set1Value(index, t.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  t Description of the Parameter
     */
    public void set1Value(int index, SFTime t) {
        impl.set1Value(index, t.impl);
    }


    /**
     *  Adds a feature to the Value attribute of the MFTime object
     *
     *@param  d The feature to be added to the Value attribute
     */
    public void addValue(double d) {
        impl.addValue(d);
    }

    /**
     *  Adds a feature to the Value attribute of the MFTime object
     *
     *@param  t The feature to be added to the Value attribute
     */
    public void addValue(ConstSFTime t) {
        impl.addValue(t.impl);
    }

    /**
     *  Adds a feature to the Value attribute of the MFTime object
     *
     *@param  t The feature to be added to the Value attribute
     */
    public void addValue(SFTime t) {
        impl.addValue(t.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  d Description of the Parameter
     */
    public void insertValue(int index, double d) {
        impl.insertValue(index, d);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  t Description of the Parameter
     */
    public void insertValue(int index, SFTime t) {
        impl.insertValue(index, t.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  t Description of the Parameter
     */
    public void insertValue(int index, ConstSFTime t) {
        impl.insertValue(index, t.impl);
    }

}

