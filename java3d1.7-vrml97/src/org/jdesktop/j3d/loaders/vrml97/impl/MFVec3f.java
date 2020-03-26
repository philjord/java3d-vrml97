/*
 * $RCSfile: MFVec3f.java,v $
 *
 *      @(#)MFVec3f.java 1.18 99/02/03 19:05:34
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

import java.lang.Double;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.vecmath.Point3d;

/**  Description of the Class */
public class MFVec3f extends MField {

    int size;// number of float elements actually used
    float[] value;// .length is alloc'd size
    // initial value, to allow reset()
    int initSize;
    float[] initValue;

    /**  Sets the init attribute of the MFVec3f object */
    private void setInit() {
        initSize = size;
        if (size > 0) {
            initValue = new float[size];
            System.arraycopy(value, 0, initValue, 0, size);
        }
        else {
            initValue = null;
        }
    }

    /**Constructor for the MFVec3f object */
    public MFVec3f() {
        size = 0;
        setInit();
    }

    /**
     *Constructor for the MFVec3f object
     *
     *@param  setVal Description of the Parameter
     */
    public MFVec3f(float[][] setVal) {
        setValue(setVal);
        setInit();
    }

    /**
     *Constructor for the MFVec3f object
     *
     *@param  size Description of the Parameter
     *@param  setVal Description of the Parameter
     */
    public MFVec3f(int size, float[] setVal) {
        size *= 3;
        setValue(size, setVal);
        setInit();
    }

    /**
     *Constructor for the MFVec3f object
     *
     *@param  setVal Description of the Parameter
     */
    public MFVec3f(float[] setVal) {
        setValue(setVal);
        setInit();
    }

    /**  Description of the Method */
    public void reset() {
        size = initSize;
        if (initSize > 0) {
            setValue(initValue);
        }
    }

    /**
     *  Gets the value attribute of the MFVec3f object
     *
     *@param  getVal Description of the Parameter
     */
    public void getValue(float[][] getVal) {
        int numVecs = size / 3;
        for (int i = 0; i < numVecs; i++) {
            System.arraycopy(value, i * 3, getVal[i], 0, 3);
        }
    }

    /**
     *  Gets the value attribute of the MFVec3f object
     *
     *@param  getVal Description of the Parameter
     */
    public void getValue(float[] getVal) {
        System.arraycopy(value, 0, getVal, 0, size);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  getVal Description of the Parameter
     */
    public void get1Value(int index, float[] getVal) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        System.arraycopy(value, index * 3, getVal, 0, 3);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  getVal Description of the Parameter
     */
    public void get1Value(int index, SFVec3f getVal) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        getVal.setValue(value[index], value[index + 1], value[index + 2]);
    }

    /**
     *  Description of the Method
     *
     *@param  needed Description of the Parameter
     *@param  preserveValue Description of the Parameter
     */
    void checkSize(int needed, boolean preserveValue) {
        if ((value == null) && (needed > 0)) {
            value = new float[needed];
        }
        else if (needed > value.length) {
            int newSize = value.length;
            if (newSize == 0) {
                newSize = needed;
            }
            while (needed > newSize) {
                newSize <<= 1;
            }
            float[] prevValue = value;
            value = new float[newSize];
            if (preserveValue) {
                System.arraycopy(prevValue, 0, value, 0, size);
            }
        }
    }

    /**
     *  Sets the value attribute of the MFVec3f object
     *
     *@param  setVal The new value value
     */
    public void setValue(float[][] setVal) {
        checkSize(setVal.length * 3, false);
        size = setVal.length * 3;
        for (int i = 0; i < setVal.length; i++) {
            System.arraycopy(setVal, 0, value, i * 3, i * 3 + 3);
        }
        route();

    }

    /**
     *  Sets the value attribute of the MFVec3f object
     *
     *@param  setVal The new value value
     */
    public void setValue(float[] setVal) {
        setValue(setVal.length, setVal);
    }

    /**
     *  Sets the value attribute of the MFVec3f object
     *
     *@param  setSize The new value value
     *@param  setVal The new value value
     */
    public void setValue(int setSize, float[] setVal) {
        checkSize(setSize, false);
        size = setSize;
        if (size > 0) {
            System.arraycopy(setVal, 0, value, 0, size);
        }
        route();
    }

    /**
     *  Sets the value attribute of the MFVec3f object
     *
     *@param  field The new value value
     */
    public void setValue(MFVec3f field) {
        setValue(field.value);
    }

    /**
     *  Sets the value attribute of the MFVec3f object
     *
     *@param  field The new value value
     */
    public void setValue(ConstMFVec3f field) {
        setValue(((MFVec3f) field.ownerField).value);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void set1Value(int index, ConstSFVec3f constvec) {
        set1Value(index, (SFVec3f) (constvec.ownerField));
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void set1Value(int index, SFVec3f vec) {
        set1Value(index, vec.value[0], vec.value[1], vec.value[2]);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  z Description of the Parameter
     */
    public void set1Value(int index, float x, float y, float z) {
        index *= 3;
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        value[index + 0] = x;
        value[index + 1] = y;
        value[index + 2] = z;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void insertValue(int index, ConstSFVec3f constvec) {
        insertValue(index, (SFVec3f) constvec.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void insertValue(int index, SFVec3f vec) {
        insertValue(index, vec.value[0], vec.value[1], vec.value[2]);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  z Description of the Parameter
     */
    public void insertValue(int index, float x, float y, float z) {
        int i;
        index *= 3;
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        checkSize(size + 3, true);

        // move the values after the inserted value up by 3,
        System.arraycopy(value, index, value, index + 3, size - index);

        // write the value
        value[index] = x;
        value[index + 1] = y;
        value[index + 2] = y;
        size += 3;
        route();
    }


    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((MFVec3f) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {// the copy is only as large as it needs to be
        MFVec3f ref = new MFVec3f();
        ref.size = size;
        if (size == 0) {
            ref.value = null;
        }
        else {
            ref.value = new float[size];
            System.arraycopy(value, 0, ref.value, 0, size);
        }
        return ref;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public ConstField constify() {
        if (constField == null) {
            constField = new ConstMFVec3f(this);
        }
        return constField;
    }

    /**
     *  Gets the size attribute of the MFVec3f object
     *
     *@return  The size value
     */
    public int getSize() {
        return size / 3;
    }

    /**  Description of the Method */
    public void clear() {
        size = 0;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     */
    public void delete(int index) {
        int i;
        index *= 3;
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        System.arraycopy(value, index + 3, value, index, size - index);
        size -= 3;
        route();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.MFVec3f(this);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toString() {
        String retval = "[";
        for (int i = 0; i < size; i += 3) {
            for (int j = 0; j < 3; j++) {
                retval += value[i + j];
                retval += " ";
            }
            retval += "\n  ";
        }
        retval += "\n]\n";
        return retval;
    }

    /**
     *  Gets the boundingBox attribute of the MFVec3f object
     *
     *@return  The boundingBox value
     */
    BoundingBox getBoundingBox() {
        Point3d min = new Point3d(Double.MAX_VALUE, Double.MAX_VALUE,
                Double.MAX_VALUE);
        Point3d max = new Point3d(Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE);
        for (int i = 0; i < size; i += 3) {
            if (value[i] > max.x) {
                max.x = value[i];
            }
            if (value[i] < min.x) {
                min.x = value[i];
            }
            if (value[i + 1] > max.y) {
                max.y = value[i + 1];
            }
            if (value[i + 1] < min.y) {
                min.y = value[i + 1];
            }
            if (value[i + 2] > max.z) {
                max.z = value[i + 2];
            }
            if (value[i + 2] < min.z) {
                min.z = value[i + 2];
            }
        }
        return new BoundingBox(min, max);
    }
}

