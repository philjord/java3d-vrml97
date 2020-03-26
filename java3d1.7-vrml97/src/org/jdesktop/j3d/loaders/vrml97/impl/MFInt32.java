/*
 * $RCSfile: MFInt32.java,v $
 *
 *      @(#)MFInt32.java 1.22 98/11/05 20:34:40
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

/**  Description of the Class */
public class MFInt32 extends MField {

    int size;// number of elements actually used
    int[] value;// .length is alloc'd size
    // initial value, to allow reset()
    int initSize;
    int[] initValue;

    /**  Sets the init attribute of the MFInt32 object */
    private void setInit() {
        initSize = size;
        if (size > 0) {
            initValue = new int[size];
            System.arraycopy(value, 0, initValue, 0, size);
        }
        else {
            initValue = null;
        }
    }

    /**Constructor for the MFInt32 object */
    public MFInt32() {
        size = 0;
    }

    /**
     *Constructor for the MFInt32 object
     *
     *@param  setVal Description of the Parameter
     */
    public MFInt32(int[] setVal) {
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
     *  Gets the value attribute of the MFInt32 object
     *
     *@param  getVal Description of the Parameter
     */
    public void getValue(int[] getVal) {
        System.arraycopy(value, 0, getVal, 0, size);
    }

    /**
     *  Sets the value attribute of the MFInt32 object
     *
     *@param  setVal The new value value
     */
    public void setValue(int[] setVal) {
        setValue(setVal.length, setVal);
    }

    /**
     *  Sets the value attribute of the MFInt32 object
     *
     *@param  setSize The new value value
     *@param  setVal The new value value
     */
    public void setValue(int setSize, int[] setVal) {
        checkSize(setSize, false);
        size = setSize;
        if (size > 0) {
            System.arraycopy(setVal, 0, value, 0, size);
        }
        route();
    }

    /**
     *  Sets the value attribute of the MFInt32 object
     *
     *@param  value The new value value
     */
    public void setValue(MFInt32 value) {
        setValue(value.value);
    }

    /**
     *  Sets the value attribute of the MFInt32 object
     *
     *@param  value The new value value
     */
    public void setValue(ConstMFInt32 value) {
        setValue((MFInt32) value.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@return  Description of the Return Value
     */
    public int get1Value(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (value[index]);
    }

    /**
     *  Description of the Method
     *
     *@param  needed Description of the Parameter
     *@param  preserveValue Description of the Parameter
     */
    void checkSize(int needed, boolean preserveValue) {
        if ((value == null) && (needed > 0)) {
            value = new int[needed];
        }
        else if (needed > value.length) {
            int newSize = value.length;
            if (newSize == 0) {
                newSize = needed;
            }
            while (needed > newSize) {
                newSize <<= 1;
            }
            int[] prevValue = value;
            value = new int[newSize];
            if (preserveValue) {
                System.arraycopy(prevValue, 0, value, 0, size);
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    int primCount() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (value[i] == -1) {
                count++;
            }
        }
        // handle non-terminated final prim
        if (value[size - 1] != -1) {
            count++;
        }
        return count;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    int indexCount() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (value[i] != -1) {
                count++;
            }
        }
        return count;
    }

    /**
     * Fill the implSize array with the size of each prim and
     * the implIndex array with the indices for the prims
     *
     *@param  implSize Description of the Parameter
     *@param  implIndex Description of the Parameter
     */
    void fillImplArrays(int[] implSize, int[] implIndex) {
        int curPrim = 0;
        int curSize = 0;
        int curIndex = 0;
        ;
        boolean lastValue = false;
        for (int i = 0; i < size; i++) {
            if (value[i] == -1) {
                implSize[curPrim++] = curSize;
                curSize = 0;
                lastValue = false;
            }
            else {
                implIndex[curIndex++] = value[i];
                curSize++;
                lastValue = true;
            }
        }
        if (lastValue) {
            // finish off the last face
            implSize[curPrim++] = curSize;
        }
    }

    /**
     * Fill a "subordinate" implIndex array.  Use the implSize array
     * from the coord index parse.  If the current face size does not
     * match the impl face size, try to manage as best we can.  Return
     * true if the face sizes matched, false if the data looked screwy
     *
     *@param  implSize Description of the Parameter
     *@param  implIndex Description of the Parameter
     *@return  Description of the Return Value
     */
    boolean fillImplArraysTest(int[] implSize, int[] implIndex) {
        int curPrim = 0;
        int curSize = 0;
        int inIndex = 0;
        int outIndex = 0;
        boolean dataOK = true;
        int useValue;
        while (outIndex < implIndex.length) {
            if (inIndex >= size) {
                useValue = value[size - 1];
                dataOK = false;
            }
            else {
                useValue = value[inIndex];
            }
            if (useValue == -1) {
                if (implSize[curPrim] != curSize) {
                    ;
                    dataOK = false;
                }
                curPrim++;
                if (curPrim >= implSize.length) {
                    dataOK = false;
                    curPrim--;
                }
                curSize = 0;
            }
            else {
                implIndex[outIndex++] = useValue;
                if (curSize++ > implSize[curPrim]) {
                    dataOK = false;
                }
            }
            inIndex++;
        }
        return dataOK;
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, int f) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        value[index] = f;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, ConstSFInt32 f) {
        set1Value(index, (SFInt32) f.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void set1Value(int index, SFInt32 f) {
        set1Value(index, f.value);
    }

    /**
     *  Adds a feature to the Value attribute of the MFInt32 object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(int f) {
        checkSize(size + 1, true);
        value[size++] = f;
        route();
    }

    /**
     *  Adds a feature to the Value attribute of the MFInt32 object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(ConstSFInt32 f) {
        addValue((SFInt32) f.ownerField);
    }

    /**
     *  Adds a feature to the Value attribute of the MFInt32 object
     *
     *@param  f The feature to be added to the Value attribute
     */
    public void addValue(SFInt32 f) {
        addValue(f.value);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, int f) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        checkSize(size + 1, true);
        // move the later elements up one index
        System.arraycopy(value, index, value, index + 1, size - index);
        value[index] = f;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, ConstSFInt32 f) {
        insertValue(index, (SFInt32) f.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  f Description of the Parameter
     */
    public void insertValue(int index, SFInt32 f) {
        insertValue(index, f.value);
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((MFInt32) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        MFInt32 ref = new MFInt32();
        ref.size = size;
        if (size == 0) {
            ref.value = null;
        }
        else {
            ref.value = new int[size];
            System.arraycopy(value, 0, ref.value, 0, size);
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
            constField = new ConstMFInt32(this);
        }
        return constField;
    }

    /**
     *  Gets the size attribute of the MFInt32 object
     *
     *@return  The size value
     */
    public int getSize() {
        return size;
    }

    /**  Description of the Method */
    public void clear() {
        size = 0;
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     */
    public void delete(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        System.arraycopy(value, index + 1, value, index, size - index);
        size--;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.MFInt32(this);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toString() {
        String retval = "[\n   ";
        for (int i = 0; i < size; i++) {
            retval += value[i] + " ";
            // TODO: if MFInt32 used for non-index, find another
            // line break mode.
            if (value[i] == -1) {
                retval += "\n   ";
            }
        }
        retval += "\n]\n";
        return retval;
    }
}

