package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class VrmlCharStream
{
  public static final boolean staticFlag = false;
  int bufsize;
  int available;
  int tokenBegin;
  public int bufpos = -1;
  private int[] bufline;
  private int[] bufcolumn;
  private int column = 0;
  private int line = 1;

  private boolean prevCharIsCR = false;
  private boolean prevCharIsLF = false;
  private Reader inputStream;
  private char[] buffer;
  private int maxNextCharInd = 0;
  private int inBuf = 0;

  private final void ExpandBuff(boolean wrapAround)
  {
    char[] newbuffer = new char[this.bufsize + 2048];
    int[] newbufline = new int[this.bufsize + 2048];
    int[] newbufcolumn = new int[this.bufsize + 2048];
    try
    {
      if (wrapAround) {
        System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
        System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);

        this.buffer = newbuffer;

        System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
        System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
        this.bufline = newbufline;

        System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
        System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
        this.bufcolumn = newbufcolumn;

        this.maxNextCharInd = (this.bufpos += this.bufsize - this.tokenBegin);
      }
      else {
        System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
        this.buffer = newbuffer;

        System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
        this.bufline = newbufline;

        System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
        this.bufcolumn = newbufcolumn;

        this.maxNextCharInd = (this.bufpos -= this.tokenBegin);
      }
    }
    catch (Throwable t) {
      throw new Error(t);
    }

    this.bufsize += 2048;
    this.available = this.bufsize;
    this.tokenBegin = 0;
  }

  private final void FillBuff()
    throws IOException
  {
    if (this.maxNextCharInd == this.available)
      if (this.available == this.bufsize) {
        if (this.tokenBegin > 2048) {
          this.bufpos = (this.maxNextCharInd = 0);
          this.available = this.tokenBegin;
        }
        else if (this.tokenBegin < 0) {
          this.bufpos = (this.maxNextCharInd = 0);
        }
        else {
          ExpandBuff(false);
        }
      }
      else if (this.available > this.tokenBegin) {
        this.available = this.bufsize;
      }
      else if (this.tokenBegin - this.available < 2048) {
        ExpandBuff(true);
      }
      else
        this.available = this.tokenBegin;
    try
    {
      int i;
      if ((i = this.inputStream.read(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd)) == -1)
      {
        this.inputStream.close();
        throw new IOException();
      }

      this.maxNextCharInd += i;

      return;
    }
    catch (IOException e) {
      this.bufpos -= 1;
      backup(0);
      if (this.tokenBegin == -1)
        this.tokenBegin = this.bufpos;
      throw e;
    }
    
  }

  public final char BeginToken()
    throws IOException
  {
    this.tokenBegin = -1;
    char c = readChar();
    this.tokenBegin = this.bufpos;

    return c;
  }

  private final void UpdateLineColumn(char c)
  {
    this.column += 1;

    if (this.prevCharIsLF) {
      this.prevCharIsLF = false;
      this.line += (this.column = 1);
    }
    else if (this.prevCharIsCR) {
      this.prevCharIsCR = false;
      if (c == '\n') {
        this.prevCharIsLF = true;
      }
      else {
        this.line += (this.column = 1);
      }
    }

    switch (c) {
    case '\r':
      this.prevCharIsCR = true;
      break;
    case '\n':
      this.prevCharIsLF = true;
      break;
    case '\t':
      this.column -= 1;
      this.column += 8 - (this.column & 0x7);
      break;
    case '\013':
    case '\f':
    }

    this.bufline[this.bufpos] = this.line;
    this.bufcolumn[this.bufpos] = this.column;
  }

  public final char readChar()
    throws IOException
  {
    if (this.inBuf > 0) {
      this.inBuf -= 1;
      return (char)(0xFF & this.buffer[(++this.bufpos)]);
    }

    if (++this.bufpos >= this.maxNextCharInd) {
      FillBuff();
    }

    char c = (char)(0xFF & this.buffer[this.bufpos]);

    UpdateLineColumn(c);
    return c;
  }

  /** @deprecated */
  public final int getColumn()
  {
    return this.bufcolumn[this.bufpos];
  }

  /** @deprecated */
  public final int getLine()
  {
    return this.bufline[this.bufpos];
  }

  public final int getEndColumn()
  {
    return this.bufcolumn[this.bufpos];
  }

  public final int getEndLine()
  {
    return this.bufline[this.bufpos];
  }

  public final int getBeginColumn()
  {
    return this.bufcolumn[this.tokenBegin];
  }

  public final int getBeginLine()
  {
    return this.bufline[this.tokenBegin];
  }

  public final void backup(int amount)
  {
    this.inBuf += amount;
    if ((this.bufpos -= amount) < 0)
      this.bufpos += this.bufsize;
  }

  public VrmlCharStream(Reader dstream, int startline, int startcolumn, int buffersize)
  {
    this.inputStream = dstream;
    this.line = startline;
    this.column = (startcolumn - 1);

    this.available = (this.bufsize = buffersize);
    this.buffer = new char[buffersize];
    this.bufline = new int[buffersize];
    this.bufcolumn = new int[buffersize];
  }

  public VrmlCharStream(Reader dstream, int startline, int startcolumn)
  {
    this(dstream, startline, startcolumn, 4096);
  }

  public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize)
  {
    this.inputStream = dstream;
    this.line = startline;
    this.column = (startcolumn - 1);

    if ((this.buffer == null) || (buffersize != this.buffer.length)) {
      this.available = (this.bufsize = buffersize);
      this.buffer = new char[buffersize];
      this.bufline = new int[buffersize];
      this.bufcolumn = new int[buffersize];
    }
    this.prevCharIsLF = (this.prevCharIsCR = false);
    this.tokenBegin = (this.inBuf = this.maxNextCharInd = 0);
    this.bufpos = -1;
  }

  public void ReInit(Reader dstream, int startline, int startcolumn)
  {
    ReInit(dstream, startline, startcolumn, 4096);
  }

  public VrmlCharStream(InputStream dstream, int startline, int startcolumn, int buffersize)
  {
    this(new InputStreamReader(dstream), startline, startcolumn, 4096);
  }

  public VrmlCharStream(InputStream dstream, int startline, int startcolumn)
  {
    this(dstream, startline, startcolumn, 4096);
  }

  public void ReInit(InputStream dstream, int startline, int startcolumn, int buffersize)
  {
    ReInit(new InputStreamReader(dstream), startline, startcolumn, 4096);
  }

  public void ReInit(InputStream dstream, int startline, int startcolumn)
  {
    ReInit(dstream, startline, startcolumn, 4096);
  }

  public final String GetImage()
  {
    if (this.bufpos >= this.tokenBegin) {
      return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
    }

    return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
  }

  public final char[] GetSuffix(int len)
  {
    char[] ret = new char[len];

    if (this.bufpos + 1 >= len) {
      System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
    }
    else {
      System.arraycopy(this.buffer, this.bufsize - (len - this.bufpos - 1), ret, 0, len - this.bufpos - 1);

      System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
    }

    return ret;
  }

  public void Done()
  {
    this.buffer = null;
    this.bufline = null;
    this.bufcolumn = null;
  }

  public void adjustBeginLineColumn(int newLine, int newCol)
  {
    int start = this.tokenBegin;
    int len;
    if (this.bufpos >= this.tokenBegin) {
      len = this.bufpos - this.tokenBegin + this.inBuf + 1;
    }
    else {
      len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
    }

    int i = 0;

    int j = 0;

    int k = 0;
    int nextColDiff = 0;
    int columnDiff = 0;

    while (i < len) { start++; if (this.bufline[(j = start % this.bufsize)] != this.bufline[(k = start % this.bufsize)]) break;
      this.bufline[j] = newLine;
      nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
      this.bufcolumn[j] = (newCol + columnDiff);
      columnDiff = nextColDiff;
      i++;
    }

    if (i < len) {
      this.bufline[j] = (newLine++);
      this.bufcolumn[j] = (newCol + columnDiff);

      while (i++ < len) {
        start++; if (this.bufline[(j = start % this.bufsize)] != this.bufline[(start % this.bufsize)]) {
          this.bufline[j] = (newLine++);
          continue;
        }
        this.bufline[j] = newLine;
      }

    }

    this.line = this.bufline[j];
    this.column = this.bufcolumn[j];
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.VrmlCharStream
 * JD-Core Version:    0.6.0
 */