package org.jdesktop.j3d.loaders.vrml97.impl;

public abstract interface ParserConstants
{
  public static final int EOF = 0;
  public static final int COMMENT = 7;
  public static final int NUMBER_LITERAL = 8;
  public static final int STRING_LITERAL = 9;
  public static final int LBRACE = 10;
  public static final int RBRACE = 11;
  public static final int LBRACKET = 12;
  public static final int RBRACKET = 13;
  public static final int TRUE_UC_LITERAL = 14;
  public static final int TRUE_LC_LITERAL = 15;
  public static final int FALSE_UC_LITERAL = 16;
  public static final int FALSE_LC_LITERAL = 17;
  public static final int DEF = 18;
  public static final int USE = 19;
  public static final int PROTO = 20;
  public static final int EVENTIN = 21;
  public static final int EVENTOUT = 22;
  public static final int FIELD = 23;
  public static final int EXPOSEDFIELD = 24;
  public static final int EXTERNPROTO = 25;
  public static final int ROUTE = 26;
  public static final int TO = 27;
  public static final int NULL = 28;
  public static final int IS = 29;
  public static final int DOT = 30;
  public static final int SFBOOL = 31;
  public static final int SFCOLOR = 32;
  public static final int SFFLOAT = 33;
  public static final int SFINT32 = 34;
  public static final int SFNODE = 35;
  public static final int SFROTATION = 36;
  public static final int SFSTRING = 37;
  public static final int SFTIME = 38;
  public static final int SFVEC2F = 39;
  public static final int SFVEC3F = 40;
  public static final int MFCOLOR = 41;
  public static final int MFFLOAT = 42;
  public static final int MFINT32 = 43;
  public static final int MFNODE = 44;
  public static final int MFROTATION = 45;
  public static final int MFSTRING = 46;
  public static final int MFTIME = 47;
  public static final int MFVEC2F = 48;
  public static final int MFVEC3F = 49;
  public static final int ID = 50;
  public static final int ID_FIRST = 51;
  public static final int ID_REST = 52;
  public static final int DEFAULT = 0;
  public static final String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"\\f\"", "\",\"", "<COMMENT>", "<NUMBER_LITERAL>", "<STRING_LITERAL>", "\"{\"", "\"}\"", "\"[\"", "\"]\"", "\"TRUE\"", "\"true\"", "\"FALSE\"", "\"false\"", "\"DEF\"", "\"USE\"", "\"PROTO\"", "\"eventIn\"", "\"eventOut\"", "\"field\"", "\"exposedField\"", "\"EXTERNPROTO\"", "\"ROUTE\"", "\"TO\"", "\"NULL\"", "\"IS\"", "\".\"", "\"SFBool\"", "\"SFColor\"", "\"SFFloat\"", "\"SFInt32\"", "\"SFNode\"", "\"SFRotation\"", "\"SFString\"", "\"SFTime\"", "\"SFVec2f\"", "\"SFVec3f\"", "\"MFColor\"", "\"MFFloat\"", "\"MFInt32\"", "\"MFNode\"", "\"MFRotation\"", "\"MFString\"", "\"MFTime\"", "\"MFVec2f\"", "\"MFVec3f\"", "<ID>", "<ID_FIRST>", "<ID_REST>" };
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.ParserConstants
 * JD-Core Version:    0.6.0
 */