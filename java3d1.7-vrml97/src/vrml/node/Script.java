package vrml.node;

import vrml.BaseNode;
import vrml.Browser;
import vrml.Event;
import vrml.InvalidEventInException;
import vrml.InvalidEventOutException;
import vrml.InvalidFieldException;

public class Script extends BaseNode
{
  Browser browser;
  org.jdesktop.j3d.loaders.vrml97.impl.Script impl;

  public Script()
  {
    super(null);
    this.browser = null;
    this.impl = null;
  }

  public Script(org.jdesktop.j3d.loaders.vrml97.impl.Script init)
  {
    super(init);
    this.impl = init;

    this.browser = new Browser(this.impl.getBrowser());
  }

  public Object clone()
  {
    return new Script((org.jdesktop.j3d.loaders.vrml97.impl.Script)this.impl.clone());
  }

  public String getType()
  {
    return "Script";
  }

  public Browser getBrowser()
  {
    return this.browser;
  }

  public synchronized void registerOwner(org.jdesktop.j3d.loaders.vrml97.impl.Script s)
  {
    this.impl = s;
    this.browser = new Browser(this.impl.getBrowser());
  }

  protected final synchronized vrml.Field getField(String fieldName)
    throws InvalidFieldException
  {
    return this.impl.getField(fieldName).wrap();
  }

  protected final synchronized vrml.Field getEventOut(String eventOutName)
    throws InvalidEventOutException
  {
    return this.impl.getEventOut(eventOutName).wrap();
  }

  protected final synchronized vrml.Field getEventIn(String eventInName)
    throws InvalidEventInException
  {
    return this.impl.getEventIn(eventInName).wrap();
  }

  public void initialize()
  {
  }

  public void processEvents(int count, Event[] events)
  {
  }

  public void processEvent(Event event)
  {
  }

  public void eventsProcessed()
  {
  }

  public void shutdown()
  {
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.node.Script
 * JD-Core Version:    0.6.0
 */