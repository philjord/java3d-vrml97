package org.jdesktop.j3d.loaders.vrml97.impl;

public class Normal extends Node
  implements Reusable
{
  MFVec3f vector;
  Node owner;

  public Normal(Loader loader)
  {
    super(loader);
    this.vector = new MFVec3f();
    initFields();
  }

  public Normal(Loader loader, float[] normals)
  {
    super(loader);
    this.vector = new MFVec3f(normals);
    initFields();
  }

  Normal(Loader loader, MFVec3f vectors)
  {
    super(loader);
    this.vector = vectors;
    initFields();
  }

  public void reset()
  {
    this.vector.reset();
    this.implReady = false;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("vector")) {
      this.owner.notifyMethod("normal", time);
    }
    if (eventInName.equals("route_vector"))
      this.owner.notifyMethod("route_normal", time);
  }

  public String getType()
  {
    return "Normal";
  }

  public Object clone()
  {
    return new Normal(this.loader, (MFVec3f)this.vector.clone());
  }

  void initFields()
  {
    this.vector.init(this, this.FieldSpec, 3, "vector");
  }

  public String toStringBody()
  {
    return "Normal {\nvector " + this.vector + "}";
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Normal
 * JD-Core Version:    0.6.0
 */