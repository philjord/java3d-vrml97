package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class UCode_UCodeESC_CharStream
{
  public static final boolean staticFlag = false;
  public int bufpos = -1;
  int bufsize;
  int available;
  int tokenBegin;
  private int[] bufline;
  private int[] bufcolumn;
  private int column = 0;
  private int line = 1;
  private Reader inputStream;
  private boolean prevCharIsCR = false;
  private boolean prevCharIsLF = false;
  private char[] nextCharBuf;
  private char[] buffer;
  private int maxNextCharInd = 0;
  private int nextCharInd = -1;

  private int inBuf = 0;

  static final int hexval(char c)
    throws IOException
  {
    switch (c)
    {
    case '0':
      return 0;
    case '1':
      return 1;
    case '2':
      return 2;
    case '3':
      return 3;
    case '4':
      return 4;
    case '5':
      return 5;
    case '6':
      return 6;
    case '7':
      return 7;
    case '8':
      return 8;
    case '9':
      return 9;
    case 'A':
    case 'a':
      return 10;
    case 'B':
    case 'b':
      return 11;
    case 'C':
    case 'c':
      return 12;
    case 'D':
    case 'd':
      return 13;
    case 'E':
    case 'e':
      return 14;
    case 'F':
    case 'f':
      return 15;
    case ':':
    case ';':
    case '<':
    case '=':
    case '>':
    case '?':
    case '@':
    case 'G':
    case 'H':
    case 'I':
    case 'J':
    case 'K':
    case 'L':
    case 'M':
    case 'N':
    case 'O':
    case 'P':
    case 'Q':
    case 'R':
    case 'S':
    case 'T':
    case 'U':
    case 'V':
    case 'W':
    case 'X':
    case 'Y':
    case 'Z':
    case '[':
    case '\\':
    case ']':
    case '^':
    case '_':
    case '`': } throw new IOException();
  }

  private final void ExpandBuff(boolean wrapAround)
  {
    char[] newbuffer = new char[this.bufsize + 2048];
    int[] newbufline = new int[this.bufsize + 2048];
    int[] newbufcolumn = new int[this.bufsize + 2048];
    try
    {
      if (wrapAround)
      {
        System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
        System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);

        this.buffer = newbuffer;

        System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
        System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
        this.bufline = newbufline;

        System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
        System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
        this.bufcolumn = newbufcolumn;

        this.bufpos += this.bufsize - this.tokenBegin;
      }
      else
      {
        System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
        this.buffer = newbuffer;

        System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
        this.bufline = newbufline;

        System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
        this.bufcolumn = newbufcolumn;

        this.bufpos -= this.tokenBegin;
      }
    }
    catch (Throwable t)
    {
      throw new Error(t.getMessage());
    }

    this.available = (this.bufsize += 2048);
    this.tokenBegin = 0;
  }

  private final void FillBuff()
    throws IOException
  {
    if (this.maxNextCharInd == 4096)
      this.maxNextCharInd = (this.nextCharInd = 0);
    try
    {
      int i;
      if ((i = this.inputStream.read(this.nextCharBuf, this.maxNextCharInd, 4096 - this.maxNextCharInd)) == -1)
      {
        this.inputStream.close();
        throw new IOException();
      }

      this.maxNextCharInd += i;
      return;
    }
    catch (IOException e) {
      if (this.bufpos != 0)
      {
        this.bufpos -= 1;
        backup(0);
      }
      else
      {
        this.bufline[this.bufpos] = this.line;
        this.bufcolumn[this.bufpos] = this.column;
      }
      throw e;
    }
    
  }

  private final char ReadChar()
    throws IOException
  {
    if (++this.nextCharInd >= this.maxNextCharInd) {
      FillBuff();
    }
    char c = (char)(this.nextCharBuf[this.nextCharInd] << '\b');

    if (++this.nextCharInd >= this.maxNextCharInd) {
      FillBuff();
    }
    return (char)(c | 0xFF & this.nextCharBuf[this.nextCharInd]);
  }

  public final char BeginToken() throws IOException
  {
    if (this.inBuf > 0)
    {
      this.inBuf -= 1;
      return this.buffer[(this.tokenBegin = ++this.bufpos)];
    }

    this.tokenBegin = 0;
    this.bufpos = -1;

    return readChar();
  }

  private final void AdjustBuffSize()
  {
    if (this.available == this.bufsize)
    {
      if (this.tokenBegin > 2048)
      {
        this.bufpos = 0;
        this.available = this.tokenBegin;
      }
      else {
        ExpandBuff(false);
      }
    } else if (this.available > this.tokenBegin)
      this.available = this.bufsize;
    else if (this.tokenBegin - this.available < 2048)
      ExpandBuff(true);
    else
      this.available = this.tokenBegin;
  }

  private final void UpdateLineColumn(char c)
  {
    this.column += 1;

    if (this.prevCharIsLF)
    {
      this.prevCharIsLF = false;
      this.line += (this.column = 1);
    }
    else if (this.prevCharIsCR)
    {
      this.prevCharIsCR = false;
      if (c == '\n')
      {
        this.prevCharIsLF = true;
      }
      else {
        this.line += (this.column = 1);
      }
    }
    switch (c)
    {
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
    if (this.inBuf > 0)
    {
      this.inBuf -= 1;
      return this.buffer[(++this.bufpos)];
    }

    if (++this.bufpos == this.available)
      AdjustBuffSize();
    char c;
    if ((this.buffer[this.bufpos] = c = ReadChar()) == '\\')
    {
      UpdateLineColumn(c);

      int backSlashCnt = 1;
      while (true)
      {
        if (++this.bufpos == this.available) {
          AdjustBuffSize();
        }
        try
        {
          if ((this.buffer[this.bufpos] = c = ReadChar()) != '\\')
          {
            UpdateLineColumn(c);

            if ((c == 'u') && ((backSlashCnt & 0x1) == 1))
            {
              if (--this.bufpos < 0) {
                this.bufpos = (this.bufsize - 1);
              }
              break;
            }

            backup(backSlashCnt);
            return '\\';
          }
        }
        catch (IOException e)
        {
          if (backSlashCnt > 1) {
            backup(backSlashCnt);
          }
          return '\\';
        }

        UpdateLineColumn(c);
        backSlashCnt++;
      }

      try
      {
        while ((c = ReadChar()) == 'u')
          this.column += 1;
        char tmp299_298 = (char)(hexval(c) << 12 | hexval(ReadChar()) << 8 | hexval(ReadChar()) << 4 | hexval(ReadChar())); c = tmp299_298; this.buffer[this.bufpos] = tmp299_298;

        this.column += 4;
      }
      catch (IOException e)
      {
        throw new Error("Invalid escape character at line " + this.line + " column " + this.column + ".");
      }

      if (backSlashCnt == 1) {
        return c;
      }

      backup(backSlashCnt - 1);
      return '\\';
    }

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

  public final int getEndColumn() {
    return this.bufcolumn[this.bufpos];
  }

  public final int getEndLine() {
    return this.bufline[this.bufpos];
  }

  public final int getBeginColumn() {
    return this.bufcolumn[this.tokenBegin];
  }

  public final int getBeginLine() {
    return this.bufline[this.tokenBegin];
  }

  public final void backup(int amount)
  {
    this.inBuf += amount;
    if ((this.bufpos -= amount) < 0)
      this.bufpos += this.bufsize;
  }

  public UCode_UCodeESC_CharStream(Reader dstream, int startline, int startcolumn, int buffersize)
  {
    this.inputStream = dstream;
    this.line = startline;
    this.column = (startcolumn - 1);

    this.available = (this.bufsize = buffersize);
    this.buffer = new char[buffersize];
    this.bufline = new int[buffersize];
    this.bufcolumn = new int[buffersize];
    this.nextCharBuf = new char[4096];
  }

  public UCode_UCodeESC_CharStream(Reader dstream, int startline, int startcolumn)
  {
    this(dstream, startline, startcolumn, 4096);
  }

  public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize)
  {
    this.inputStream = dstream;
    this.line = startline;
    this.column = (startcolumn - 1);

    if ((this.buffer == null) || (buffersize != this.buffer.length))
    {
      this.available = (this.bufsize = buffersize);
      this.buffer = new char[buffersize];
      this.bufline = new int[buffersize];
      this.bufcolumn = new int[buffersize];
      this.nextCharBuf = new char[4096];
    }
  }

  public void ReInit(Reader dstream, int startline, int startcolumn)
  {
    ReInit(dstream, startline, startcolumn, 4096);
  }

  public UCode_UCodeESC_CharStream(InputStream dstream, int startline, int startcolumn, int buffersize)
  {
    this(new InputStreamReader(dstream), startline, startcolumn, 4096);
  }

  public UCode_UCodeESC_CharStream(InputStream dstream, int startline, int startcolumn)
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
    this.nextCharBuf = null;
    this.buffer = null;
    this.bufline = null;
    this.bufcolumn = null;
  }

  public void adjustBeginLineColumn(int newLine, int newCol)
  {
    int start = this.tokenBegin;
    int len;
    if (this.bufpos >= this.tokenBegin)
    {
      len = this.bufpos - this.tokenBegin + this.inBuf + 1;
    }
    else
    {
      len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
    }

    int i = 0; int j = 0; int k = 0;
    int nextColDiff = 0; int columnDiff = 0;

    while (i < len) { start++; if (this.bufline[(j = start % this.bufsize)] != this.bufline[(k = start % this.bufsize)])
        break;
      this.bufline[j] = newLine;
      nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
      this.bufcolumn[j] = (newCol + columnDiff);
      columnDiff = nextColDiff;
      i++;
    }

    if (i < len)
    {
      this.bufline[j] = (newLine++);
      this.bufcolumn[j] = (newCol + columnDiff);

      while (i++ < len)
      {
        start++; if (this.bufline[(j = start % this.bufsize)] != this.bufline[(start % this.bufsize)]) {
          this.bufline[j] = (newLine++); continue;
        }
        this.bufline[j] = newLine;
      }
    }

    this.line = this.bufline[j];
    this.column = this.bufcolumn[j];
  }
}

/* Location:           C:\temp\j3d-vrml97.jar
 * Qualified Name:     org.jdesktop.j3d.loaders.vrml97.impl.UCode_UCodeESC_CharStream
 * JD-Core Version:    0.6.0
 */