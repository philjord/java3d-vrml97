/*
 * $RCSfile: Appearance.java,v $
 *
 *      @(#)Appearance.java 1.31 99/03/09 17:02:07
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
 * $Revision: 1.5 $
 * $Date: 2006/04/03 12:43:39 $
 * $State: Exp $
 */
/*
 *
 * @author Rick Goldberg
 * @author Doug Gehringer
 * @author Nikolai V. Chr.
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import org.jogamp.java3d.BoundingBox;
import org.jogamp.java3d.TexCoordGeneration;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransparencyAttributes;

import java.beans.*;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector4f;

/**  Description of the Class */
public class Appearance extends Node {
	
	org.jogamp.java3d.Appearance impl;
	
	// exposedField
	SFNode material;// all default NULL
	SFNode texture;
	SFNode textureTransform;
	
	TransparencyAttributes implTransp = null;
	private TransparencyListener listener = new TransparencyListener();
	
	class TransparencyListener implements PropertyChangeListener{
		Material material = null;
		ImageTexture texture = null;
				
		public void setMaterial(Material m) {
			if(m != material) {
				if(material != null) {
					material.removePropertyChangeListener(Material.TRANSPARENCY, this);
					material = null;
				}
				material = m;
				if(m != null) {
					m.addPropertyChangeListener(Material.TRANSPARENCY, this);
				}
			}
		}
				
		public void setTexture(TextureSrc t) {
			if(t != texture) {
				if(texture != null) {
					texture.removePropertyChangeListener(ImageTexture.TRANSPARENCY, this);
					texture = null;
				}
				if(t instanceof ImageTexture) {
					texture = (ImageTexture)t;
					if(t != null) {
						texture.addPropertyChangeListener(ImageTexture.TRANSPARENCY, this);
					}
				}
			}
		}
		
		public void propertyChange(PropertyChangeEvent e) {
			updateTransparency();
		}
	};
	
	boolean haveTexture = false;
	int numUses = 0;
	TexCoordGeneration texGen;
	org.jogamp.java3d.TextureAttributes ta =
			new org.jogamp.java3d.TextureAttributes();
	Transform3D tr = new Transform3D();
	Transform3D T = new Transform3D();
	Transform3D C = new Transform3D();
	Transform3D R = new Transform3D();
	Transform3D S = new Transform3D();
	Vector3d v1 = new Vector3d();
	Vector3d v2 = new Vector3d();
	Vector3d v3 = new Vector3d();
	Vector3d v4 = new Vector3d();
	org.jogamp.vecmath.AxisAngle4f al =
			new org.jogamp.vecmath.AxisAngle4f();
	
	/**
	 *Constructor for the Appearance object
	 *
	 *@param  loader Description of the Parameter
	 */
	public Appearance(Loader loader) {
		super(loader);
		material = new SFNode(null);
		texture = new SFNode(null);
		textureTransform = new SFNode(null);
		initFields();
	}
	
	/**
	 *Constructor for the Appearance object
	 *
	 *@param  loader Description of the Parameter
	 *@param  material Description of the Parameter
	 *@param  texture Description of the Parameter
	 *@param  textureTransform Description of the Parameter
	 */
	Appearance(Loader loader, SFNode material, SFNode texture,
			SFNode textureTransform) {
		super(loader);
		this.material = material;
		this.texture = texture;
		this.textureTransform = textureTransform;
		initFields();
	}
	
	/**  Description of the Method */
	private void updateMaterial() {
		listener.setMaterial((Material) material.node);
		updateTransparency();
		if (material.node == null) {
			//impl.setLightingEnable(false);
		} else {
			impl.setMaterial(((Material) material.node).impl);
			//impl.setLightingEnable(true);
		}
	}
	
	/**  Description of the Method */
	private void updateTexture() {
		haveTexture = false;
		if (texture.node != null) {
			Texture tex = ((TextureSrc) texture.node).getImplTexture();
			if (tex != null) {
				impl.setTexture(tex);
				tex.setEnable(true);
				haveTexture = true;
			}
		}
		listener.setTexture((TextureSrc) texture.node);
		updateTransparency();
	}

	public void updateTransparency() {
		if (loader.debug) {
            System.out.print(toStringId()+": Updating transparency ");
        }
		boolean transparent = false;
		float value = 0.0f;
		if(material != null && material.node != null) {
			value = ((Material)material.node).getTransparency();
			transparent =  value > 0.0f;
			if (loader.debug) {
                System.out.print(" Material="+value);
            }
		}
		if(texture != null && texture.node != null && texture.node instanceof ImageTexture) {
			transparent = transparent || ((ImageTexture)texture.node).getTransparency();
			if (loader.debug) {
                System.out.print(" Texture="+((ImageTexture)texture.node).getTransparency());
            }
		}
		if (loader.debug) {
            System.out.println(" Result="+transparent);
        }
		int mode;
		if (transparent) {
			mode = TransparencyAttributes.NICEST;
		} else {
			mode = TransparencyAttributes.NONE;
		}
		if(implTransp == null) {
			implTransp = new TransparencyAttributes(mode, value);
			implTransp.setCapability(org.jogamp.java3d.TransparencyAttributes.ALLOW_MODE_WRITE);
			implTransp.setCapability(org.jogamp.java3d.TransparencyAttributes.ALLOW_VALUE_WRITE);
		} else {
			implTransp.setTransparency(value);
			implTransp.setTransparencyMode(mode);
		}
		impl.setTransparencyAttributes(implTransp);
	}	
	
	/**  Description of the Method */
	void updateTextureTransform() {
		if (textureTransform.node != null) {
			// need to notify Appearance for this case
			((TextureTransform) textureTransform.node).owner = this;
			double cx;
			double cy;
			double tx;
			double ty;
			double r;
			double sx;
			double sy;
			
			// implemented for readability, several steps are can be
			// optimized away
			
			TextureTransform tf = (TextureTransform) textureTransform.node;
			
			tx = tf.translation.vec2f[0];
			ty = tf.translation.vec2f[0];
			v1.set(tx, ty, 0.0);
			T.set(v1);
			
			cx = tf.center.vec2f[0];
			cy = tf.center.vec2f[1];
			v2.set(cx, cy, 0.0);
			C.set(v2);
			
			r = tf.rotation.value;
			al.set(0.0f, 0.0f, 1.0f, (float) r);
			R.setRotation(al);
			
			sx = tf.scale.vec2f[0];
			sy = tf.scale.vec2f[1];
			v3.set(sx, sy, 1.0);
			S.setScale(v3);
			
			tr.setIdentity();
			tr.mul(T);
			tr.mul(C);
			tr.mul(R);
			tr.mul(S);
			v2.negate();
			C.set(v2);
			tr.mul(C);
			
			ta.setTextureTransform(tr);
			impl.setTextureAttributes(ta);
		}
		ta.setTextureMode(ta.COMBINE);
		ta.setCombineAlphaMode(ta.COMBINE_MODULATE);
		ta.setCombineRgbMode(ta.COMBINE_REPLACE);
		ta.setCombineAlphaSource(0,ta.COMBINE_OBJECT_COLOR);
		ta.setCombineAlphaSource(1,ta.COMBINE_TEXTURE_COLOR);
		ta.setCombineRgbSource(0,ta.COMBINE_TEXTURE_COLOR);
		impl.setTextureAttributes(ta);
	}
	
	/**
	 *  Sets the texGen attribute of the Appearance object
	 *
	 *@param  box The new texGen value
	 */
	public void setTexGen(BoundingBox box) {
		
		Point3d min = new Point3d();
		Point3d max = new Point3d();
		
		box.getLower(min);
		box.getUpper(max);
		
		float xRange = (float) (max.x - min.x);
		float yRange = (float) (max.y - min.y);
		float zRange = (float) (max.z - min.z);
		Vector4f sEq;
		Vector4f tEq;
		Vector4f xEq = new Vector4f(1.0f / xRange, 0.0f, 0.0f,
				(float) -min.x / xRange);
		Vector4f yEq = new Vector4f(0.0f, 1.0f / yRange, 0.0f,
				(float) -min.y / xRange);
		Vector4f zEq = new Vector4f(0.0f, 0.0f, 1.0f / zRange,
				(float) -min.z / xRange);
		
		// handle the S mapping which is to the largest
		if (xRange >= yRange) {
			if (xRange >= zRange) {
				sEq = xEq;
				if (yRange >= zRange) {
					tEq = yEq;
				} else {
					tEq = zEq;
				}
			} else {// z is max
				sEq = zEq;
				tEq = xEq;// x > y
			}
		} else {// y > x
			if (yRange >= zRange) {
				sEq = yEq;
				if (xRange >= zRange) {
					tEq = xEq;
				} else {
					tEq = zEq;
				}
			} else {// z is max
				sEq = zEq;
				tEq = yEq;// y > x
			}
		}
		texGen = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
				TexCoordGeneration.TEXTURE_COORDINATE_2, sEq, tEq);
		impl.setTexCoordGeneration(texGen);
		texGen.setEnable(true);
	}
	
	/**  Description of the Method */
	public void initImpl() {
		impl = new org.jogamp.java3d.Appearance();
		updateMaterial();
		updateTexture();
		updateTextureTransform();
		implReady = true;
	}
	
	/**
	 *  Description of the Method
	 *
	 *@param  eventInName Description of the Parameter
	 *@param  time Description of the Parameter
	 */
	public void notifyMethod(String eventInName, double time) {
		if (eventInName.equals("material")) {
			updateMaterial();
		} else if (eventInName.equals("texture")) {
			updateTexture();
		} else if (eventInName.equals("textureTransform")) {
			if (textureTransform.node != null) {
				System.err.println("Appearance: textureTransform not " +
						"implemented");
			}
		} else if (eventInName.equals("route_material")) {
			impl.setCapability(org.jogamp.java3d.Appearance.ALLOW_MATERIAL_WRITE);
		} else if (eventInName.equals("route_texture")) {
			impl.setCapability(org.jogamp.java3d.Appearance.ALLOW_TEXTURE_WRITE);
		} else if (eventInName.equals("route_textureTransform")) {
			impl.setCapability(
					org.jogamp.java3d.Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
		} else {
			System.err.println("Appearance: unknown eventInName " +
					eventInName);
		}
	}
	
	/**
	 *  Description of the Method
	 *
	 *@return  Description of the Return Value
	 */
	public Object clone() {
		Appearance a = new Appearance(loader, (SFNode) material.clone(),
				(SFNode) texture.clone(),
				(SFNode) textureTransform.clone());
		return a;
	}
	
	/**
	 *  Gets the type attribute of the Appearance object
	 *
	 *@return  The type value
	 */
	public String getType() {
		return "Appearance";
	}
	
	/**  Description of the Method */
	void initFields() {
		material.init(this, FieldSpec, Field.EXPOSED_FIELD, "material");
		texture.init(this, FieldSpec, Field.EXPOSED_FIELD, "texture");
		textureTransform.init(this, FieldSpec, Field.EXPOSED_FIELD,
				"textureTransform");
	}
	
}

