package org.jdesktop.j3d.loaders.vrml97.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.jogamp.java3d.Group;
import org.jogamp.java3d.Link;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.RestrictedAccessException;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.SharedGroup;

public class TreeCleaner
{
  private static final int CLEAN_UNUSED = 1;
  private static final int CLEAN_NONE = 2;
  private static final int ALREADY_CLEANED = -1;
  static final boolean debug = false;

  public static void cleanSubgraph(Node implNode)
    throws RestrictedAccessException
  {
    Hashtable sharedGroups = new Hashtable();

    checkAndClean(implNode, 1, sharedGroups);

    int numGroupsCleaned = 0;
    Integer alreadyCleaned = new Integer(-1);

    while (numGroupsCleaned < sharedGroups.size())
    {
      Enumeration e = sharedGroups.keys();
      while (e.hasMoreElements()) {
        SharedGroup sg = (SharedGroup)e.nextElement();
        int sgFlag = ((Integer)sharedGroups.get(sg)).intValue();
        if (sgFlag != -1) {
          checkAndClean(sg, sgFlag, sharedGroups);
          sharedGroups.put(sg, alreadyCleaned);
          numGroupsCleaned++;
        }
      }
    }
  }

  static void checkAndClean(Node node, int pickingFlag, Hashtable sharedGroups)
    throws RestrictedAccessException
  {
    if (node != null) {
      if (node.isLive()) {
        throw new RestrictedAccessException("Can't clean a live scene graph");
      }

      clean(node, pickingFlag, sharedGroups);
    }
  }

  static void clean(Node node, int pickingFlag, Hashtable sharedGroups)
  {
    if ((node instanceof Group))
    {
      if ((pickingFlag == 1) && (node.getCapability(1)))
      {
        pickingFlag = 2;
      }
      Iterator<Node> e = ((Group)node).getAllChildren();
      while (e.hasNext()) {
        clean((Node)(Node)e.next(), pickingFlag, sharedGroups);
      }

    }
    else if ((node instanceof Link)) {
      Link link = (Link)node;
      SharedGroup sg = link.getSharedGroup();
      Integer value = (Integer)sharedGroups.get(sg);

      if ((value == null) || (pickingFlag > value.intValue())) {
        value = new Integer(pickingFlag);
        sharedGroups.put(sg, new Integer(pickingFlag));
      }
    }
    else if ((node instanceof Shape3D)) {
      if (pickingFlag != 2) {
        node.setPickable(false);
      }
      node.setCollidable(false);
    }
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.TreeCleaner
 * JD-Core Version:    0.6.0
 */