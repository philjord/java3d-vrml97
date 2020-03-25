package org.jdesktop.j3d.loaders.vrml97.impl;

public class Time
{
  static double systemInit = 0.0D;
  static long lastTimeMillis = 0L;

  public static void setSystemInitTime()
  {
    long curTimeMillis = System.currentTimeMillis();

    systemInit = curTimeMillis / 1000.0D;

    lastTimeMillis = curTimeMillis;
  }

  public static double getNow()
  {
    long curTimeMillis = System.currentTimeMillis();

    if (curTimeMillis == lastTimeMillis) {
      curTimeMillis += 1L;
    }
    lastTimeMillis = curTimeMillis;

    return curTimeMillis / 1000.0D - systemInit;
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.Time
 * JD-Core Version:    0.6.0
 */