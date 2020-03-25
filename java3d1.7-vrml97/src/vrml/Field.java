package vrml;

public abstract class Field
  implements Cloneable
{
  protected org.jdesktop.j3d.loaders.vrml97.impl.Field implField;

  public Field(org.jdesktop.j3d.loaders.vrml97.impl.Field init)
  {
    this.implField = init;
  }

  public void markWriteable()
  {
    this.implField.markWriteable();
  }

  protected static org.jdesktop.j3d.loaders.vrml97.impl.BaseNode getImpl(BaseNode node)
  {
    return node.getImpl();
  }

  public abstract Object clone();
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.Field
 * JD-Core Version:    0.6.0
 */