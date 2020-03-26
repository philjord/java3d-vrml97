/*
 * $RCSfile: MFColor.java,v $
 *
 *      @(#)MFColor.java 1.12 98/11/05 20:34:38
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
import java.util.Vector;

/**  Description of the Class */
public class MFColor extends MField {

    float[] vals;

    /**Constructor for the MFColor object */
    public MFColor() {
        float[] dummy = new float[0];
        setValue(dummy);
    }

    /**
     *Constructor for the MFColor object
     *
     *@param  values Description of the Parameter
     */
    public MFColor(float[][] values) {
        setValue(values);
    }

    // What is the size doing here?
    /**
     *Constructor for the MFColor object
     *
     *@param  size Description of the Parameter
     *@param  values Description of the Parameter
     */
    public MFColor(int size, float[] values) {
        setValue(values);
    }

    /**
     *Constructor for the MFColor object
     *
     *@param  values Description of the Parameter
     */
    public MFColor(float[] values) {
        setValue(values);
    }


    /**
     *  Gets the value attribute of the MFColor object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float[][] values) {
        int numVecs = vals.length / 3;
        for (int i = 0; i < numVecs; i++) {
            System.arraycopy(vals, i * 3, values[i], 0, 3);
        }
    }

    /**
     *  Gets the value attribute of the MFColor object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float[] values) {
        System.arraycopy(vals, 0, values, 0, vals.length);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  values Description of the Parameter
     */
    public void get1Value(int index, float[] values) {
        System.arraycopy(vals, index * 3, values, 0, 3);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void get1Value(int index, SFColor vec) {
        vec.setValue(vals[index], vals[index + 1], vals[index + 2]);
    }

    /**
     *  Sets the value attribute of the MFColor object
     *
     *@param  values The new value value
     */
    public void setValue(float[][] values) {
        vals = new float[values.length * 3];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(values, 0, vals, i * 3, i * 3 + 3);
        }
        route();

    }

    /**
     *  Sets the value attribute of the MFColor object
     *
     *@param  values The new value value
     */
    public void setValue(float[] values) {
        vals = new float[values.length];
        System.arraycopy(values, 0, vals, 0, values.length);
        route();
    }

    /**
     *  Sets the value attribute of the MFColor object
     *
     *@param  color The new value value
     */
    public void setValue(MFColor color) {
        setValue(color.vals);
    }

    /**
     *  Sets the value attribute of the MFColor object
     *
     *@param  size The new value value
     *@param  values The new value value
     */
    public void setValue(int size, float[] values) {
        setValue(values);
    }

    /**
     *  Sets the value attribute of the MFColor object
     *
     *@param  cf The new value value
     */
    public void setValue(ConstMFColor cf) {
        setValue((MFColor) cf.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void set1Value(int index, ConstSFColor constvec) {
        set1Value(index, (SFColor) constvec.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void set1Value(int index, SFColor vec) {
        set1Value(index, vec.color[0], vec.color[1], vec.color[2]);
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
        try {
            vals[index * 3 + 0] = x;
            vals[index * 3 + 1] = y;
            vals[index * 3 + 2] = z;

        }
        catch (IndexOutOfBoundsException e) {
            System.err.println("MFColor.set1Value(index,float,float,float): " +
                    "exception " + e);
        }
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void insertValue(int index, ConstSFColor constvec) {
        insertValue(index, (SFColor) constvec.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void insertValue(int index, SFColor vec) {
        insertValue(index, vec.color[0], vec.color[1], vec.color[2]);
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
        try {
            int i;
            float[] temp = new float[vals.length + 3];
            for (i = 0; i < index * 3; i++) {
                temp[i] = vals[i];
            }
            temp[i++] = x;
            temp[i++] = y;
            temp[i++] = z;
            for (; i < temp.length; i++) {
                temp[i] = vals[i - 3];
            }
            vals = temp;
        }
        catch (IndexOutOfBoundsException e) {
            System.err.println("MFColor.insertValue(index,float,float,float): " +
                    "exception " + e);
        }
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((MFColor) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        MFColor ref = new MFColor();
        ref.vals = new float[vals.length];
        try {
            System.arraycopy(vals, 0, ref.vals, 0, vals.length);
        }
        catch (Exception e) {
            System.err.println("MFColor.clone(): exception " + e);
        }

        return ref;
    }

    /**
     *  Gets the size attribute of the MFColor object
     *
     *@return  The size value
     */
    public int getSize() {
        return vals.length / 3;
    }

    /**  Description of the Method */
    public void clear() {
        vals = new float[1];
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     */
    public void delete(int index) {
        int i;
        float[] temp = new float[vals.length - 3];
        for (i = 0; i < index * 3; i++) {
            temp[i] = vals[i];
        }
        for (; i < temp.length; i++) {
            temp[i] = vals[i + 3];
        }
        vals = temp;
        route();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstMFColor(this);
        }
        return constField;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.MFColor(this);
    }
}

