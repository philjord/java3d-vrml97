/*
 * $RCSfile: SFNode.java,v $
 *
 *      @(#)SFNode.java 1.11 98/11/05 20:40:39
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
 * $Date: 2005/02/03 23:07:15 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 */
package vrml.field;
import vrml.BaseNode;

/**  Description of the Class */
public class SFNode extends vrml.Field {
    org.jdesktop.j3d.loaders.vrml97.impl.SFNode impl;

    /**
     *Constructor for the SFNode object
     *
     *@param  init Description of the Parameter
     */
    public SFNode(org.jdesktop.j3d.loaders.vrml97.impl.SFNode init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new SFNode(
                (org.jdesktop.j3d.loaders.vrml97.impl.SFNode) impl.clone());
    }

    /**
     *Constructor for the SFNode object
     *
     *@param  node Description of the Parameter
     */
    public SFNode(BaseNode node) {
        super(null);
        impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFNode(
                vrml.Field.getImpl(node));
        implField = impl;
    }

    /**
     *  Gets the value attribute of the SFNode object
     *
     *@return  The value value
     */
    public BaseNode getValue() {
        return impl.getValue().wrap();
    }

    /**
     *  Sets the value attribute of the SFNode object
     *
     *@param  node The new value value
     */
    public void setValue(BaseNode node) {
        impl.setValue(vrml.Field.getImpl(node));
    }

    /**
     *  Sets the value attribute of the SFNode object
     *
     *@param  sfnode The new value value
     */
    public void setValue(SFNode sfnode) {
        impl.setValue(sfnode.impl);
    }

    /**
     *  Sets the value attribute of the SFNode object
     *
     *@param  csfnode The new value value
     */
    public void setValue(ConstSFNode csfnode) {
        impl.setValue(csfnode.impl);
    }
}

