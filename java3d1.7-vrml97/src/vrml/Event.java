package vrml;

public class Event
  implements Cloneable
{
  org.jdesktop.j3d.loaders.vrml97.impl.Event impl;

  public Event(org.jdesktop.j3d.loaders.vrml97.impl.Event init)
  {
    this.impl = init;
  }

  public String getName()
  {
    return this.impl.getName();
  }

  public double getTimeStamp()
  {
    return this.impl.getTimeStamp();
  }

  public ConstField getValue()
  {
    return (ConstField)(ConstField)this.impl.getValue().wrap();
  }

  public Object clone()
  {
    return new Event((org.jdesktop.j3d.loaders.vrml97.impl.Event)this.impl.clone());
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.Event
 * JD-Core Version:    0.6.0
 */