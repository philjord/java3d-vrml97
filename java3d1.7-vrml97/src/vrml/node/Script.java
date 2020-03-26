/*
 * $RCSfile: Script.java,v $
 *
 *      @(#)Script.java 1.18 98/11/05 20:41:14
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
 * $Date: 2005/02/03 23:07:16 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package vrml.node;
import vrml.Event;
import vrml.Field;
import vrml.InvalidEventInException;
import vrml.InvalidEventOutException;
import vrml.InvalidExposedFieldException;
import vrml.InvalidFieldException;

/** This is the base class for VRML scripts. */
public class Script extends vrml.BaseNode {

    vrml.Browser browser;
    org.jdesktop.j3d.loaders.vrml97.impl.Script impl;

    /**Constructor for the Script object */
    public Script() {
        super(null);
        browser = null;
        impl = null;
    }

    /**
     * This is the internal constructor, called only when the Script is
     * being initialized by the Loader.
     *
     *@param  init Description of the Parameter
     */
    public Script(org.jdesktop.j3d.loaders.vrml97.impl.Script init) {
        super(init);
        impl = init;
        //
        browser = new vrml.Browser(impl.getBrowser());
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new Script((org.jdesktop.j3d.loaders.vrml97.impl.Script) impl.clone());
    }


    /**
     *  Gets the type attribute of the Script object
     *
     *@return  The type value
     */
    public String getType() {
        return "Script";
    }

    /**
     *  Gets the browser attribute of the Script object
     *
     *@return  The browser value
     */
    public vrml.Browser getBrowser() {
        return browser;
    }

    /**
     * This is an internal method, used to link the Script to its
     * implementaton
     *
     *@param  s Description of the Parameter
     */
    public synchronized void registerOwner(org.jdesktop.j3d.loaders.vrml97.impl.Script s) {
        impl = s;
        browser = new vrml.Browser(impl.getBrowser());
    }

    /**
     * Returns the Field specified in the VRML which invokes the script
     *
     *@param  fieldName Description of the Parameter
     *@return  The field value
     *@exception  InvalidFieldException Description of the Exception
     */
    protected final synchronized Field getField(String fieldName)
             throws InvalidFieldException {
        return impl.getField(fieldName).wrap();
    }

    /**
     * Returns the EventOut specified in the VRML which invokes the script
     *
     *@param  eventOutName Description of the Parameter
     *@return  The eventOut value
     *@exception  InvalidEventOutException Description of the Exception
     */
    protected final synchronized Field getEventOut(String eventOutName)
             throws InvalidEventOutException {
        return impl.getEventOut(eventOutName).wrap();
    }

    /**
     * Returns the EventIn specified in the VRML which invokes the script
     *
     *@param  eventInName Description of the Parameter
     *@return  The eventIn value
     *@exception  InvalidEventInException Description of the Exception
     */
    protected final synchronized Field getEventIn(String eventInName)
             throws InvalidEventInException {
        return impl.getEventIn(eventInName).wrap();
    }

    /** Called before any event is generated */
    public void initialize() {
        ;
    }// doesn't have to be abOutstract

    /**
     * Called when there are Events to be processed
     *
     *@param  count Description of the Parameter
     *@param  events Description of the Parameter
     */
    public void processEvents(int count, Event[] events) {
        ;
    }

    /**
     * Called when there is an Event to be processed
     *
     *@param  event Description of the Parameter
     */
    public void processEvent(Event event) {
        ;
    }

    /** Called after every invocation of processEvents() */
    public void eventsProcessed() {
        ;
    }

    /** Called when the Script node is deleted */
    public void shutdown() { }
}

