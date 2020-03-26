/*
 * $RCSfile: MFVec2f.java,v $
 *
 *      @(#)MFVec2f.java 1.12 98/11/05 20:34:42
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
import java.util.Vector;

/**  Description of the Class */
public class MFVec2f extends MField {

    float[] vals;

    /**Constructor for the MFVec2f object */
    public MFVec2f() {
        float[] dummy = new float[1];
        setValue(dummy);
    }

    /**
     *Constructor for the MFVec2f object
     *
     *@param  values Description of the Parameter
     */
    public MFVec2f(float[][] values) {
        setValue(values);
    }

    // What is the size doing here?
    /**
     *Constructor for the MFVec2f object
     *
     *@param  size Description of the Parameter
     *@param  values Description of the Parameter
     */
    public MFVec2f(int size, float[] values) {
        setValue(values);
    }

    /**
     *Constructor for the MFVec2f object
     *
     *@param  values Description of the Parameter
     */
    public MFVec2f(float[] values) {
        setValue(values);
    }


    /**
     *  Gets the value attribute of the MFVec2f object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float[][] values) {
        int numVecs = vals.length / 2;
        for (int i = 0; i < numVecs; i++) {
            System.arraycopy(vals, i * 2, values[i], 0, 2);
        }
    }

    /**
     *  Gets the value attribute of the MFVec2f object
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
        System.arraycopy(vals, index * 2, values, 0, 2);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void get1Value(int index, SFVec2f vec) {
        vec.setValue(vals[index], vals[index + 1]);
    }

    /**
     *  Sets the value attribute of the MFVec2f object
     *
     *@param  values The new value value
     */
    public void setValue(float[][] values) {
        vals = new float[values.length * 2];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(values, 0, vals, i * 2, i * 2 + 2);
        }
        route();

    }

    /**
     *  Sets the value attribute of the MFVec2f object
     *
     *@param  values The new value value
     */
    public void setValue(float[] values) {
        vals = new float[values.length];
        System.arraycopy(values, 0, vals, 0, values.length);
        route();
    }

    /**
     *  Sets the value attribute of the MFVec2f object
     *
     *@param  size The new value value
     *@param  values The new value value
     */
    public void setValue(int size, float[] values) {
        setValue(values);
    }

    /**
     *  Sets the value attribute of the MFVec2f object
     *
     *@param  field The new value value
     */
    public void setValue(MFVec2f field) {
        setValue(field.vals);
    }

    /**
     *  Sets the value attribute of the MFVec2f object
     *
     *@param  field The new value value
     */
    public void setValue(ConstMFVec2f field) {
        setValue((MFVec2f) field.ownerField);
    }


    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void set1Value(int index, ConstSFVec2f constvec) {
        set1Value(index, (SFVec2f) constvec.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void set1Value(int index, SFVec2f vec) {
        set1Value(index, vec.vec2f[0], vec.vec2f[1]);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     */
    public void set1Value(int index, float x, float y) {
        vals[index * 2 + 0] = x;
        vals[index * 2 + 1] = y;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void insertValue(int index, ConstSFVec2f constvec) {
        insertValue(index, (SFVec2f) constvec.ownerField);
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void insertValue(int index, SFVec2f vec) {
        insertValue(index, vec.vec2f[0], vec.vec2f[1]);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     */
    public void insertValue(int index, float x, float y) {
        int i;
        float[] temp = new float[vals.length + 2];
        for (i = 0; i < index * 2; i++) {
            temp[i] = vals[i];
        }
        temp[i++] = x;
        temp[i++] = y;
        for (; i < temp.length; i++) {
            temp[i] = vals[i - 2];
        }
        vals = temp;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((MFVec2f) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        MFVec2f ref = new MFVec2f();
        ref.vals = new float[vals.length];
        System.arraycopy(vals, 0, ref.vals, 0, vals.length);
        return ref;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public ConstField constify() {
        if (constField == null) {
            constField = new ConstMFVec2f(this);
        }
        return constField;
    }

    /**
     *  Gets the size attribute of the MFVec2f object
     *
     *@return  The size value
     */
    public int getSize() {
        return vals.length / 2;
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
        float[] temp = new float[vals.length - 2];
        for (i = 0; i < index * 2; i++) {
            temp[i] = vals[i];
        }
        for (; i < temp.length; i++) {
            temp[i] = vals[i + 2];
        }
        vals = temp;
        route();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.Field wrap() {
        return new vrml.field.MFVec2f(this);
    }
}

