package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;
import java.util.Stack;

public abstract class BindableNode extends Node
{
  SFBool bind;
  SFTime bindTime;
  SFBool isBound;
  Stack stack;

  BindableNode(Loader loader, Stack initStack)
  {
    super(loader);
    this.stack = initStack;
    this.bind = new SFBool(false);
    this.bindTime = new SFTime();
    this.isBound = new SFBool(false);
  }

  BindableNode(Loader loader, Stack initStack, SFBool bind, SFTime bindTime, SFBool isBound)
  {
    super(loader);
    this.stack = initStack;
    this.bind = bind;
    this.bindTime = bindTime;
    this.isBound = isBound;
  }

  public void notifyMethod(String eventInName, double time)
  {
    if (eventInName.equals("bind"))
      if (this.bind.getValue() == true) {
        if (this.stack != null)
        {
          BindableNode top;
          if (this.stack.empty()) {
            top = null;
          }
          else {
            top = (BindableNode)this.stack.peek();
          }
          if (top != this) {
            if (this.stack.contains(this)) {
              this.stack.removeElement(this);
            }
            this.stack.push(this);

            this.browser.bindableChanged(this.stack);
            this.bindTime.setValue(time);
          }

        }
        else if (this.loader.debug) {
          System.out.println("Bind called on bindable with null stack");
        }

        this.isBound.setValue(true);
      }
      else {
        if (this.stack != null) {
          if ((BindableNode)this.stack.peek() == this) {
            this.stack.pop();

            BindableNode top = (BindableNode)this.stack.peek();
            if (top != null) {
              top.bind.setValue(true);
              top.isBound.setValue(true);
            }
            this.browser.bindableChanged(this.stack);
          }
          else if (this.stack.contains(this)) {
            this.stack.removeElement(this);
          }
        }

        this.isBound.setValue(false);
      }
  }

  void initBindableFields()
  {
    this.bind.init(this, this.FieldSpec, 1, "bind");
    this.bindTime.init(this, this.FieldSpec, 2, "bindTime");
    this.isBound.init(this, this.FieldSpec, 2, "isBound");
  }

  public org.jogamp.java3d.Node getImplNode()
  {
    return this.implNode;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.BindableNode
 * JD-Core Version:    0.6.0
 */