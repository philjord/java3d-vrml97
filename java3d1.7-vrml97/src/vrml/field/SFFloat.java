package vrml.field;

import vrml.Field;

public class SFFloat extends Field
{
  org.jdesktop.j3d.loaders.vrml97.impl.SFFloat impl;

  public SFFloat(org.jdesktop.j3d.loaders.vrml97.impl.SFFloat init)
  {
    super(init);
    this.impl = init;
  }

  public synchronized Object clone()
  {
    return new SFFloat((org.jdesktop.j3d.loaders.vrml97.impl.SFFloat)this.impl.clone());
  }

  public SFFloat(float f)
  {
    super(null);
    this.impl = new org.jdesktop.j3d.loaders.vrml97.impl.SFFloat(f);
    this.implField = this.impl;
  }

  public float getValue()
  {
    return this.impl.getValue();
  }

  public void setValue(float f)
  {
    this.impl.setValue(f);
  }

  public void setValue(ConstSFFloat f)
  {
    this.impl.setValue(f.impl);
  }

  public void setValue(SFFloat f)
  {
    this.impl.setValue(f.impl);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.field.SFFloat
 * JD-Core Version:    0.6.0
 */