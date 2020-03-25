package org.jdesktop.j3d.loaders.vrml97.impl;

public class Event
  implements Cloneable
{
  public String name;
  public double timeStamp;
  public ConstField value;

  public Event(String name, double time, ConstField value)
  {
    this.name = new String(name);
    this.value = value;

    this.timeStamp = time;
  }

  public String getName()
  {
    return this.name;
  }

  public double getTimeStamp()
  {
    return this.timeStamp;
  }

  public ConstField getValue()
  {
    return this.value;
  }

  public Object clone()
  {
    Object myClone = new Event(this.name, this.timeStamp, this.value);
    return myClone;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Event
 * JD-Core Version:    0.6.0
 */