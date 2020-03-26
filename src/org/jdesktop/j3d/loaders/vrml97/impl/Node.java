/*
 * $RCSfile: Node.java,v $
 *
 *      @(#)Node.java 1.30 98/11/05 20:34:46
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
 * $Date: 2005/02/03 23:06:58 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Hashtable;
import vrml.InvalidEventInException;
import vrml.InvalidEventOutException;
import vrml.InvalidExposedFieldException;
import vrml.InvalidFieldException;

/**  Description of the Class */
public abstract class Node extends BaseNode implements Cloneable, Notifier {

    /**  Description of the Field */
    public Hashtable FieldSpec = new Hashtable(4);

    /**
     *Constructor for the ImplNode object
     *
     *@param  browser Description of the Parameter
     */
    public Node(Browser browser) {
        super(browser);
    }

    /**
     *Constructor for the Node object
     *
     *@param  loader Description of the Parameter
     */

    /**
     *Constructor for the Node object
     *
     *@param  loader Description of the Parameter
     */
    public Node(Loader loader) {
        super(loader);
    }

    // Exposed fields may be either input ( eventIn )
    // output ( eventOut ) or both
    /**
     *  Gets the exposedField attribute of the Node object
     *
     *@param  fieldName Description of the Parameter
     *@return  The exposedField value
     *@exception  InvalidExposedFieldException Description of the Exception
     */
    public Field getExposedField(String fieldName)
             throws InvalidExposedFieldException {
        Field f;
        if ((f = (Field) FieldSpec.get(fieldName)) == null) {
            throw new InvalidExposedFieldException("No field named " + fieldName);
        }
        return f;
    }

    /**
     *  Gets the eventOut attribute of the Node object
     *
     *@param  eventOutName Description of the Parameter
     *@return  The eventOut value
     *@exception  InvalidEventOutException Description of the Exception
     */
    public ConstField getEventOut(String eventOutName)
             throws InvalidEventOutException {
        Object cf;
        Field f;

        String fieldName = Field.baseName(eventOutName);
        f = (Field) FieldSpec.get(fieldName);
        if (f == null) {
            throw new InvalidEventOutException("No field named " + fieldName);
        }
        else if ((f.fieldType & Field.EVENT_OUT) != Field.EVENT_OUT) {
            throw new InvalidEventOutException("Field is not an EVENT_OUT");
        }
        if (!(f instanceof ConstField)) {
            f = ((Field) f).constify();
        }
        return (ConstField) f;
    }


    // the default action is to connect a route by accessing the fields
    // through these calls, and a setValue on the input side automatically
    // causes a getValue on the output.


    /**
     *  Gets the eventIn attribute of the Node object
     *
     *@param  eventInName Description of the Parameter
     *@return  The eventIn value
     *@exception  InvalidEventInException Description of the Exception
     */
    public Field getEventIn(String eventInName) throws InvalidEventInException {
        String fieldName = Field.baseName(eventInName);
        Field f = (Field) FieldSpec.get(fieldName);
        if (f == null) {
            throw new InvalidEventInException("No field named " + eventInName);
        }
        else if ((f.fieldType & Field.EVENT_IN) != Field.EVENT_IN) {
            throw new InvalidEventInException("Field is not an EVENT_IN");
        }
        if (f instanceof ConstField) {
            throw new InvalidEventInException("Field is an ConstField");
        }
        return f;
    }

    /**
     *  Gets the field attribute of the Node object
     *
     *@param  fieldName Description of the Parameter
     *@return  The field value
     *@exception  InvalidFieldException Description of the Exception
     */
    public Field getField(String fieldName) throws InvalidFieldException {
        Field f;
        fieldName = Field.baseName(fieldName);
        //if(loader.debug) {
        //    System.out.println(toStringId()+" getField "+fieldName);
        //}
        if ((f = (Field) FieldSpec.get(fieldName)) == null) {
            throw new InvalidFieldException("No field named " + fieldName);
        }
        return f;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public abstract void notifyMethod(String eventInName, double time);

    /**  Description of the Method */
    abstract void initFields();

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public vrml.BaseNode wrap() {
        return new vrml.node.Node(this);
    }

}

