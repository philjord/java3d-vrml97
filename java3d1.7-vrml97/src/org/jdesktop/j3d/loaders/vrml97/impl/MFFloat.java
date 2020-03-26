/*
 * $RCSfile: MFFloat.java,v $
 *
 *      @(#)MFFloat.java 1.13 98/11/05 20:34:39
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
public class MFFloat extends MField {

    float[] mfloat;

    /**Constructor for the MFFloat object */
    public MFFloat() {
        mfloat = new float[0];
    }

    /**
     *Constructor for the MFFloat object
     *
     *@param  values Description of the Parameter
     */
    public MFFloat(float values[]) {
        mfloat = new float[values.length];
        System.arraycopy(values, 0, mfloat, 0, values.length);

    }

    /**
     *  Gets the value attribute of the MFFloat object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float values[]) {
        System.arraycopy(mfloat, 0, values, 0, values.length);
    }


    /**
     *  Sets the value attribute of the MFFloat object
     *
     *@param  values The new value value
     */
    public void setValue(float[] values) {
        mfloat = new float[values.length];
        System.arraycopy(values, 0, mfloat, 0, values.length);
        route();
    }

    /**
     *  Sets the value attribute of the MFFloat object
     *
     *@param  size The new value value
     *@param  value The new value value
     */
    public void setValue(int size, float[] value) {
        mfloat = new float[size];
        System.arraycopy(value, 0, mfloat, 0, size);
        route();
    }

    /**
     *  Sets the value attribute of the MFFloat object
     *
     *@param  value The new value value
     */
    public void setValue(MFFloat value) {
        setValue(value.mfloat);
    }

    /**
     *  Sets the value attribute of the MFFloat object
     *
     *@param  value The new value value
     */
    public void setValue(ConstMFFloat value) {
        setValue((MFFloat) value.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@return  Description of the Return Value
     */
    public float get1Value(int index) {
        float f = 0.0f;
        f = mfloat[index];
        return f;
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, float f) {
        mfloat[index] = f;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, ConstSFFloat f) {
        set1Value(index, (SFFloat) f.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, SFFloat f) {
        set1Value(index, f.value);
    }

    /**
     *  Adds a feature to the Value attribute of the MFFloat object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(float f) {
        float[] temp = new float[mfloat.length + 1];
        System.arraycopy(mfloat, 0, temp, 0, mfloat.length);
        temp[mfloat.length] = f;
        mfloat = temp;

        route();
    }

    /**
     *  Adds a feature to the Value attribute of the MFFloat object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(ConstSFFloat f) {
        addValue((SFFloat) f.ownerField);
    }

    /**
     *  Adds a feature to the Value attribute of the MFFloat object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(SFFloat f) {
        addValue(f.value);
    }

    // the following needs double check
    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, float f) {

        float[] temp = new float[mfloat.length + 1];
        System.arraycopy(mfloat, 0, temp, 0, index);
        temp[index] = f;
        System.arraycopy(mfloat, index, temp, index + 1, mfloat.length - index);
        mfloat = temp;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, ConstSFFloat f) {
        insertValue(index, (SFFloat) f.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, SFFloat f) {
        insertValue(index, f.value);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public synchronized void update(Field field) {
        setValue((MFFloat) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new MFFloat(mfloat);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstMFFloat(this);
        }
        return constField;
    }

    /**
     *  Gets the size attribute of the MFFloat object
     *
     *@return  The size value
     */
    public int getSize() {
        return mfloat.length;
    }

    /**  Description of the Method */
    public void clear() {
        mfloat = new float[1];
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
        return new vrml.field.MFFloat(this);
    }
}

