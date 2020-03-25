package vrml;

public abstract class MField extends Field
{
  public MField(org.jdesktop.j3d.loaders.vrml97.impl.Field init)
  {
    super(init);
  }

  public abstract int getSize();

  public abstract void clear();

  public abstract void delete(int paramInt);
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.MField
 * JD-Core Version:    0.6.0
 */