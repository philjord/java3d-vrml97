package org.jdesktop.j3d.loaders.vrml97.impl;

import java.text.NumberFormat;

public class NumFormat
{
  NumberFormat instance;
  int digits;

  public NumFormat()
  {
    this.instance = NumberFormat.getInstance();
    this.digits = 1;
    this.instance.setMaximumFractionDigits(this.digits);
  }

  String format(double number, int numDigits)
  {
    if (this.digits != numDigits) {
      this.instance.setMaximumFractionDigits(numDigits);
      this.digits = numDigits;
    }
    return this.instance.format(number);
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.NumFormat
 * JD-Core Version:    0.6.0
 */