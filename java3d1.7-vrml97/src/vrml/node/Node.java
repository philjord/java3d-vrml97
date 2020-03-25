package vrml.node;

import vrml.Browser;
import vrml.InvalidEventInException;
import vrml.InvalidEventOutException;
import vrml.InvalidExposedFieldException;

public class Node extends vrml.BaseNode
{
  protected org.jdesktop.j3d.loaders.vrml97.impl.Node implNode;

  public Node(org.jdesktop.j3d.loaders.vrml97.impl.Node init)
  {
    super(init);
    this.implNode = init;
  }

  public final vrml.Field getExposedField(String fieldName)
    throws InvalidExposedFieldException
  {
    org.jdesktop.j3d.loaders.vrml97.impl.Field implField = this.implNode.getExposedField(fieldName);

    return implField.wrap();
  }

  public final vrml.Field getEventIn(String eventInName)
    throws InvalidEventInException
  {
    org.jdesktop.j3d.loaders.vrml97.impl.Field implField = this.implNode.getEventIn(eventInName);

    return implField.wrap();
  }

  public vrml.ConstField getEventOut(String eventOutName)
    throws InvalidEventOutException
  {
    org.jdesktop.j3d.loaders.vrml97.impl.ConstField implField = this.implNode.getEventOut(eventOutName);

    return (vrml.ConstField)implField.wrap();
  }

  public String getType()
  {
    return this.implNode.getType();
  }

  public Object clone()
  {
    return ((org.jdesktop.j3d.loaders.vrml97.impl.Node)this.implNode.clone()).wrap();
  }

  public Browser getBrowser()
  {
    return new Browser(this.implNode.getBrowser());
  }

  protected org.jdesktop.j3d.loaders.vrml97.impl.BaseNode getImpl()
  {
    return this.implNode;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     vrml.node.Node
 * JD-Core Version:    0.6.0
 */