/*
 * $RCSfile: MFRotation.java,v $
 *
 *      @(#)MFRotation.java 1.12 98/11/05 20:34:41
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
public class MFRotation extends MField {

    SFRotation[] rots;

    /**Constructor for the MFRotation object */
    public MFRotation() {
        rots = new SFRotation[0];
    }

    /**
     *Constructor for the MFRotation object
     *
     *@param  size Description of the Parameter
     *@param  values Description of the Parameter
     */
    public MFRotation(int size, float[] values) {
        setValue(size, values);
    }

    /**
     *Constructor for the MFRotation object
     *
     *@param  values Description of the Parameter
     */
    public MFRotation(float[] values) {
        setValue(values);
    }

    /**
     *Constructor for the MFRotation object
     *
     *@param  values Description of the Parameter
     */
    public MFRotation(float[][] values) {
        setValue(values);
    }


    /**
     *  Gets the value attribute of the MFRotation object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float[][] values) {
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(rots[i].rot, 0, values[i], 0, 4);
        }
    }

    /**
     *  Gets the value attribute of the MFRotation object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(float[] values) {
        for (int i = 0; i < values.length; i += 4) {
            System.arraycopy(rots[i / 4].rot, 0, values, i, 4);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  values Description of the Parameter
     */
    public void get1Value(int index, float[] values) {
        System.arraycopy(rots[index].rot, 0, values, 0, 4);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void get1Value(int index, SFRotation vec) {
        vec.setValue(rots[index].rot);
    }

    /**
     *  Sets the value attribute of the MFRotation object
     *
     *@param  values The new value value
     */
    public void setValue(float[][] values) {
        rots = new SFRotation[values.length];
        for (int i = 0; i < values.length; i++) {
            rots[i] = new SFRotation(values[i]);
        }
        route();
    }

    /**
     *  Sets the value attribute of the MFRotation object
     *
     *@param  values The new value value
     */
    public void setValue(float[] values) {
        rots = new SFRotation[values.length / 4];
        for (int i = 0; i < values.length; i += 4) {
            rots[i / 4] = new SFRotation(values[i],
                    values[i + 1],
                    values[i + 2],
                    values[i + 3]);
        }
        route();
    }

    /**
     *  Sets the value attribute of the MFRotation object
     *
     *@param  size The new value value
     *@param  values The new value value
     */
    public void setValue(int size, float[] values) {
        // TODO: should use size?
        setValue(values);
    }

    /**
     *  Sets the value attribute of the MFRotation object
     *
     *@param  values The new value value
     */
    public void setValue(MFRotation values) {
        rots = new SFRotation[values.rots.length];
        for (int i = 0; i < values.rots.length; i++) {
            rots[i] = new SFRotation(values.rots[i].rot);
        }
        route();
    }

    /**
     *  Sets the value attribute of the MFRotation object
     *
     *@param  values The new value value
     */
    public void setValue(ConstMFRotation values) {
        setValue((MFRotation) values.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void set1Value(int index, ConstSFRotation constvec) {
        set1Value(index, (SFRotation) constvec.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void set1Value(int index, SFRotation vec) {
        set1Value(index, vec.rot[0], vec.rot[1], vec.rot[2], vec.rot[3]);
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
        rots[index].rot[0] = x;
        rots[index].rot[1] = y;
        rots[index].rot[2] = z;
        rots[index].rot[3] = angle;
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void insertValue(int index, ConstSFRotation constvec) {
        insertValue(index, (SFRotation) constvec.ownerField);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void insertValue(int index, SFRotation vec) {
        insertValue(index, vec.rot[0], vec.rot[1], vec.rot[2], vec.rot[3]);
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
        Vector veclist = new Vector(rots.length + 1);
        float[] sfr = new float[4];
        sfr[0] = x;
        sfr[1] = y;
        sfr[2] = z;
        sfr[3] = angle;
        for (int i = 0; i < rots.length; i++) {
            veclist.addElement(rots[i]);
        }
        veclist.insertElementAt(new SFRotation(sfr), index);
        rots = new SFRotation[veclist.size()];
        veclist.copyInto(rots);
        route();
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((MFRotation) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        SFRotation[] tmp = new SFRotation[rots.length];
        MFRotation ref = new MFRotation();
        ref.rots = tmp;
        System.arraycopy(rots, 0, ref.rots, 0, rots.length);
        return ref;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized ConstField constify() {
        if (constField == null) {
            constField = new ConstMFRotation(this);
        }
        return constField;
    }

    /**
     *  Gets the size attribute of the MFRotation object
     *
     *@return  The size value
     */
    public int getSize() {
        return rots.length;
    }

    /**  Description of the Method */
    public void clear() {
        rots = new SFRotation[1];
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
        return new vrml.field.MFRotation(this);
    }
}

