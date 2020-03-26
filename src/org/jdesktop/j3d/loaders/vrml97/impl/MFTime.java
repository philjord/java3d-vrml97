/*
 * $RCSfile: MFTime.java,v $
 *
 *      @(#)MFTime.java 1.13 98/11/05 20:35:57
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
 * $Date: 2005/02/03 23:06:58 $
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
public class MFTime extends MField {

    double[] time = new double[1];

    /**Constructor for the MFTime object */
    public MFTime() {
        ;
    }

    /**
     *Constructor for the MFTime object
     *
     *@param  time Description of the Parameter
     */
    public MFTime(double[] time) {
        this.time = new double[time.length];
        System.arraycopy(time, 0, this.time, 0, time.length);
    }

    /**
     *  Gets the value attribute of the MFTime object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(double[] values) {
        System.arraycopy(this.time, 0, values, 0, this.time.length);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@return  Description of the Return Value
     */
    public double get1Value(int index) {
        double ret = 0.0;// compiler still complains about uninitialized
        ret = time[index];
        return ret;
    }

    /**
     *  Sets the value attribute of the MFTime object
     *
     *@param  values The new value value
     */
    public void setValue(double[] values) {
        time = new double[values.length];
        System.arraycopy(values, 0, time, 0, values.length);
        route();
    }

    /**
     *  Sets the value attribute of the MFTime object
     *
     *@param  size The new value value
     *@param  values The new value value
     */
    public void setValue(int size, double[] values) {
        setValue(values);
    }

    /**
     *  Sets the value attribute of the MFTime object
     *
     *@param  field The new value value
     */
    public void setValue(MFTime field) {
        setValue(field.time);
    }

    /**
     *  Sets the value attribute of the MFTime object
     *
     *@param  field The new value value
     */
    public void setValue(ConstMFTime field) {
        setValue((MFTime) field.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  d Description of the Parameter
     */
    public void set1Value(int index, double d) {
        time[index] = d;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  t Description of the Parameter
     */
    public void set1Value(int index, ConstSFTime t) {
        set1Value(index, (SFTime) t.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  t Description of the Parameter
     */
    public void set1Value(int index, SFTime t) {
        set1Value(index, t.time);
    }


    /**
     *  Adds a feature to the Value attribute of the MFTime object
     *
     *@param  d The feature to be added to the Value attribute
     */
    public void addValue(double d) {
        double[] temp = new double[time.length + 1];
        System.arraycopy(time, 0, temp, 0, time.length);
        temp[time.length] = d;
        time = temp;

        route();
    }

    /**
     *  Adds a feature to the Value attribute of the MFTime object
     *
     *@param  t The feature to be added to the Value attribute
     */
    public void addValue(ConstSFTime t) {
        addValue((SFTime) t.ownerField);
    }

    /**
     *  Adds a feature to the Value attribute of the MFTime object
     *
     *@param  t The feature to be added to the Value attribute
     */
    public void addValue(SFTime t) {
        addValue(t.time);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  d Description of the Parameter
     */
    public void insertValue(int index, double d) {
        double[] temp = new double[time.length + 1];
        System.arraycopy(time, 0, temp, 0, index);
        temp[index] = d;
        System.arraycopy(time, index, temp, index + 1, time.length - index);
        time = temp;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  t Description of the Parameter
     */
    public void insertValue(int index, SFTime t) {
        insertValue(index, t.time);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  t Description of the Parameter
     */
    public void insertValue(int index, ConstSFTime t) {
        insertValue(index, (SFTime) t.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new MFTime(time);
    }


    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((MFTime) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public ConstField constify() {
        if (constField == null) {
            constField = new ConstMFTime(this);
        }
        return constField;
    }

    /**
     *  Gets the size attribute of the MFTime object
     *
     *@return  The size value
     */
    public int getSize() {
        return time.length;
    }

    /**  Description of the Method */
    public void clear() {
        time = new double[1];
    }

    /**
     *  Description of the Method
     *
     *@param  i Description of the Parameter
     */
    public void delete(int i) {
        ;
    }// TBD

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.MFTime(this);
    }
}

