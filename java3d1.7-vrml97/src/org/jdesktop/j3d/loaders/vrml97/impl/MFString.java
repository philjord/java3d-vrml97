/*
 * $RCSfile: MFString.java,v $
 *
 *      @(#)MFString.java 1.18 99/03/24 15:33:57
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
public class MFString extends MField {

    String[] strings;

    /**Constructor for the MFString object */
    public MFString() {
        strings = new String[0];
    }

    /**
     *Constructor for the MFString object
     *
     *@param  s Description of the Parameter
     */
    public MFString(String[] s) {
        strings = new String[s.length];
        for (int i = 0; i < s.length; i++) {
            strings[i] = new String(s[i]);
        }
    }

    /**
     *Constructor for the MFString object
     *
     *@param  size Description of the Parameter
     *@param  s Description of the Parameter
     */
    public MFString(int size, String[] s) {
        strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = new String(s[i]);
        }
    }

    /**
     *  Sets the value attribute of the MFString object
     *
     *@param  s The new value value
     */
    public void setValue(String[] s) {
        strings = new String[s.length];
        for (int i = 0; i < s.length; i++) {
            strings[i] = new String(s[i]);
        }
    }

    /**
     *  Sets the value attribute of the MFString object
     *
     *@param  field The new value value
     */
    public void setValue(MFString field) {
        setValue(field.strings);
    }

    /**
     *  Sets the value attribute of the MFString object
     *
     *@param  field The new value value
     */
    public void setValue(ConstMFString field) {
        setValue((MFString) field.ownerField);
    }

    /**
     *  Gets the value attribute of the MFString object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(String[] values) {
        try {
            for (int i = 0; i < strings.length; i++) {
                values[i] = strings[i];
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(" string array lengths must match better ");
        }
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@return  Description of the Return Value
     */
    public String get1Value(int index) {
        String retval = "";
        try {
            retval = strings[index];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return retval;
    }

    /**
     *  Description of the Method
     *
     *@param  field Description of the Parameter
     */
    public void update(Field field) {
        setValue((MFString) field);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public ConstField constify() {
        if (constField == null) {
            constField = new ConstMFString(this);
        }
        return constField;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        MFString ref = new MFString(strings);
        return ref;
    }

    /**
     *  Gets the size attribute of the MFString object
     *
     *@return  The size value
     */
    public int getSize() {
        return strings.length;
    }

    /**  Description of the Method */
    public void clear() {
        strings = new String[1];
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
        return new vrml.field.MFString(this);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public String toString() {
        String s = new String();
        for (int i = 0; i < strings.length; i++) {
            s += strings[i];
            s += '\n';
        }
        return s;
    }
}

