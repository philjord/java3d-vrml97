/*
 * $RCSfile: BindableNode.java,v $
 *
 *      @(#)BindableNode.java 1.18 98/11/05 20:34:04
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
 * $Date: 2005/02/03 23:06:52 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.util.Stack;

/**  Description of the Class */
public abstract class BindableNode extends Node {

    // eventIn
    SFBool bind;
    SFTime bindTime;

    // eventOut
    SFBool isBound;

    Stack stack;

    /**
     *Constructor for the BindableNode object
     *
     *@param  loader Description of the Parameter
     *@param  initStack Description of the Parameter
     */
    BindableNode(Loader loader, Stack initStack) {
        super(loader);
        stack = initStack;
        bind = new SFBool(false);
        bindTime = new SFTime();
        isBound = new SFBool(false);
    }

    /**
     *Constructor for the BindableNode object
     *
     *@param  loader Description of the Parameter
     *@param  initStack Description of the Parameter
     *@param  bind Description of the Parameter
     *@param  bindTime Description of the Parameter
     *@param  isBound Description of the Parameter
     */
    BindableNode(Loader loader, Stack initStack, SFBool bind,
            SFTime bindTime, SFBool isBound) {
        super(loader);
        stack = initStack;
        this.bind = bind;
        this.bindTime = bindTime;
        this.isBound = isBound;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        BindableNode top;
        if (eventInName.equals("bind")) {
            if (bind.getValue() == true) {
                if (stack != null) {
                    if (stack.empty()) {
                        top = null;
                    }
                    else {
                        top = (BindableNode) stack.peek();
                    }
                    if (top != this) {
                        if (stack.contains(this)) {
                            stack.removeElement(this);
                        }
                        stack.push(this);// no matter what
                        //System.out.println("node is bound");
                        browser.bindableChanged(stack);
                        bindTime.setValue(time);
                    }
                }
                else {
                    if (loader.debug) {
                        System.out.println("Bind called on bindable with " +
                                "null stack");
                    }
                }
                isBound.setValue(true);
            }
            else {// bind == false
                if (stack != null) {
                    if ((BindableNode) stack.peek() == this) {// i'm on top
                        stack.pop();// remove this from stack
                        // TODO: timestamp of both setValues should be same
                        top = (BindableNode) stack.peek();
                        if (top != null) {
                            top.bind.setValue(true);
                            top.isBound.setValue(true);
                        }
                        browser.bindableChanged(stack);
                    }
                    else {
                        if (stack.contains(this)) {
                            stack.removeElement(this);
                        }
                    }
                }
                isBound.setValue(false);
            }
        }
    }

    /**  Description of the Method */
    void initBindableFields() {
        bind.init(this, FieldSpec, Field.EVENT_IN, "bind");
        bindTime.init(this, FieldSpec, Field.EVENT_OUT, "bindTime");
        isBound.init(this, FieldSpec, Field.EVENT_OUT, "isBound");
    }

    /**
     *  Gets the implNode attribute of the BindableNode object
     *
     *@return  The implNode value
     */
    public org.jogamp.java3d.Node getImplNode() {
        return implNode;
    }
}


