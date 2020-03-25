package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.PrintStream;

public class MFString extends MField
{
  String[] strings;

  public MFString()
  {
    this.strings = new String[0];
  }

  public MFString(String[] s)
  {
    this.strings = new String[s.length];
    for (int i = 0; i < s.length; i++)
      this.strings[i] = new String(s[i]);
  }

  public MFString(int size, String[] s)
  {
    this.strings = new String[size];
    for (int i = 0; i < size; i++)
      this.strings[i] = new String(s[i]);
  }

  public void setValue(String[] s)
  {
    this.strings = new String[s.length];
    for (int i = 0; i < s.length; i++)
      this.strings[i] = new String(s[i]);
  }

  public void setValue(MFString field)
  {
    setValue(field.strings);
  }

  public void setValue(ConstMFString field)
  {
    setValue((MFString)field.ownerField);
  }

  public void getValue(String[] values)
  {
    try
    {
      for (int i = 0; i < this.strings.length; i++)
        values[i] = this.strings[i];
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      System.out.println(" string array lengths must match better ");
    }
  }

  public String get1Value(int index)
  {
    String retval = "";
    try {
      retval = this.strings[index];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      e.printStackTrace();
    }
    return retval;
  }

  public void update(Field field)
  {
    setValue((MFString)field);
  }

  public ConstField constify()
  {
    if (this.constField == null) {
      this.constField = new ConstMFString(this);
    }
    return this.constField;
  }

  public Object clone()
  {
    MFString ref = new MFString(this.strings);
    return ref;
  }

  public int getSize()
  {
    return this.strings.length;
  }

  public void clear()
  {
    this.strings = new String[1];
  }

  public void delete(int i)
  {
  }

  public vrml.Field wrap()
  {
    return new vrml.field.MFString(this);
  }

  public String toString()
  {
    String s = new String();
    for (int i = 0; i < this.strings.length; i++) {
      s = s + this.strings[i];
      s = s + '\n';
    }
    return s;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.MFString
 * JD-Core Version:    0.6.0
 */