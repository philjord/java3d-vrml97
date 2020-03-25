package org.jdesktop.j3d.loaders.vrml97.impl;

public class Token
{
  public int kind;
  public int beginLine;
  public int beginColumn;
  public int endLine;
  public int endColumn;
  public String image;
  public Token next;
  public Token specialToken;

  public final String toString()
  {
    return this.image;
  }

  public static final Token newToken(int ofKind)
  {
    switch (ofKind) {
    }
    return new Token();
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Token
 * JD-Core Version:    0.6.0
 */