/*
 * $RCSfile: VrmlCharStream.java,v $
 *
 *      @(#)VrmlCharStream.java 1.5 98/11/05 20:35:31
 *
 * Copyright (c) 1996-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 *
 * $Revision: 1.3 $
 * $Date: 2006/03/30 08:19:28 $
 * $State: Exp $
 */
/*
 * Generated by JavaCC, renamed from ASCII_CharStream to VrmlCharStream to
 * avoid name conflict.  Had to put into SCCS since JavaCC doesn't geneate
 * the file when using a user token manager
 */
// Search for _Node to see start of nodes

package org.jdesktop.j3d.loaders.vrml97.impl;

/**
 * An implementation of interface CharStream, where the stream is assumed to
 * contain only ASCII characters (without unicode processing).
 */

public final class VrmlCharStream {
    /**  Description of the Field */
    public final static boolean staticFlag = false;
    int bufsize;
    int available;
    int tokenBegin;
    /**  Description of the Field */
    public int bufpos = -1;
    private int bufline[];
    private int bufcolumn[];

    private int column = 0;
    private int line = 1;

    private boolean prevCharIsCR = false;
    private boolean prevCharIsLF = false;

    private java.io.Reader inputStream;

    private char[] buffer;
    private int maxNextCharInd = 0;
    private int inBuf = 0;

    /**
     *  Description of the Method
     *
     *@param  wrapAround Description of the Parameter
     */
    private final void ExpandBuff(boolean wrapAround) {
        char[] newbuffer = new char[bufsize + 2048];
        int newbufline[] = new int[bufsize + 2048];
        int newbufcolumn[] = new int[bufsize + 2048];

        try {
            if (wrapAround) {
                System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
                System.arraycopy(buffer, 0, newbuffer,
                        bufsize - tokenBegin, bufpos);
                buffer = newbuffer;

                System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
                System.arraycopy(bufline, 0, newbufline, bufsize - tokenBegin, bufpos);
                bufline = newbufline;

                System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
                System.arraycopy(bufcolumn, 0, newbufcolumn, bufsize - tokenBegin, bufpos);
                bufcolumn = newbufcolumn;

                maxNextCharInd = (bufpos += (bufsize - tokenBegin));
            }
            else {
                System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
                buffer = newbuffer;

                System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
                bufline = newbufline;

                System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
                bufcolumn = newbufcolumn;

                maxNextCharInd = (bufpos -= tokenBegin);
            }
        }
        catch (Throwable t) {
            throw new Error(t);
        }

        bufsize += 2048;
        available = bufsize;
        tokenBegin = 0;
    }

    /**
     *  Description of the Method
     *
     *@exception  java.io.IOException Description of the Exception
     */
    private final void FillBuff() throws java.io.IOException {
        if (maxNextCharInd == available) {
            if (available == bufsize) {
                if (tokenBegin > 2048) {
                    bufpos = maxNextCharInd = 0;
                    available = tokenBegin;
                }
                else if (tokenBegin < 0) {
                    bufpos = maxNextCharInd = 0;
                }
                else {
                    ExpandBuff(false);
                }
            }
            else if (available > tokenBegin) {
                available = bufsize;
            }
            else if ((tokenBegin - available) < 2048) {
                ExpandBuff(true);
            }
            else {
                available = tokenBegin;
            }
        }

        int i;
        try {
            if ((i = inputStream.read(buffer, maxNextCharInd,
                    available - maxNextCharInd)) == -1) {
                inputStream.close();
                throw new java.io.IOException();
            }
            else {
                maxNextCharInd += i;
            }
            return;
        }
        catch (java.io.IOException e) {
            --bufpos;
            backup(0);
            if (tokenBegin == -1) {
                tokenBegin = bufpos;
            }
            throw e;
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     *@exception  java.io.IOException Description of the Exception
     */
    public final char BeginToken() throws java.io.IOException {
        tokenBegin = -1;
        char c = readChar();
        tokenBegin = bufpos;

        return c;
    }

    /**
     *  Description of the Method
     *
     *@param  c Description of the Parameter
     */
    private final void UpdateLineColumn(char c) {
        column++;

        if (prevCharIsLF) {
            prevCharIsLF = false;
            line += (column = 1);
        }
        else if (prevCharIsCR) {
            prevCharIsCR = false;
            if (c == '\n') {
                prevCharIsLF = true;
            }
            else {
                line += (column = 1);
            }
        }

        switch (c) {
            case '\r':
                prevCharIsCR = true;
                break;
            case '\n':
                prevCharIsLF = true;
                break;
            case '\t':
                column--;
                column += (8 - (column & 07));
                break;
            default:
                break;
        }

        bufline[bufpos] = line;
        bufcolumn[bufpos] = column;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     *@exception  java.io.IOException Description of the Exception
     */
    public final char readChar() throws java.io.IOException {
        if (inBuf > 0) {
            --inBuf;
            return (char) ((char) 0xff & buffer[(bufpos == bufsize - 1) ? (bufpos = 0) : ++bufpos]);
        }

        if (++bufpos >= maxNextCharInd) {
            FillBuff();
        }

        char c = (char) ((char) 0xff & buffer[bufpos]);

        UpdateLineColumn(c);
        return (c);
    }

    /**
     *@return  The column value
     *@deprecated
     *@see  #getEndColumn
     */

    public final int getColumn() {
        return bufcolumn[bufpos];
    }

    /**
     *@return  The line value
     *@deprecated
     *@see  #getEndLine
     */

    public final int getLine() {
        return bufline[bufpos];
    }

    /**
     *  Gets the endColumn attribute of the VrmlCharStream object
     *
     *@return  The endColumn value
     */
    public final int getEndColumn() {
        return bufcolumn[bufpos];
    }

    /**
     *  Gets the endLine attribute of the VrmlCharStream object
     *
     *@return  The endLine value
     */
    public final int getEndLine() {
        return bufline[bufpos];
    }

    /**
     *  Gets the beginColumn attribute of the VrmlCharStream object
     *
     *@return  The beginColumn value
     */
    public final int getBeginColumn() {
        return bufcolumn[tokenBegin];
    }

    /**
     *  Gets the beginLine attribute of the VrmlCharStream object
     *
     *@return  The beginLine value
     */
    public final int getBeginLine() {
        return bufline[tokenBegin];
    }

    /**
     *  Description of the Method
     *
     *@param  amount Description of the Parameter
     */
    public final void backup(int amount) {

        inBuf += amount;
        if ((bufpos -= amount) < 0) {
            bufpos += bufsize;
        }
    }

    /**
     *Constructor for the VrmlCharStream object
     *
     *@param  dstream Description of the Parameter
     *@param  startline Description of the Parameter
     *@param  startcolumn Description of the Parameter
     *@param  buffersize Description of the Parameter
     */
    public VrmlCharStream(java.io.Reader dstream, int startline,
            int startcolumn, int buffersize) {
        inputStream = dstream;
        line = startline;
        column = startcolumn - 1;

        available = bufsize = buffersize;
        buffer = new char[buffersize];
        bufline = new int[buffersize];
        bufcolumn = new int[buffersize];
    }

    /**
     *Constructor for the VrmlCharStream object
     *
     *@param  dstream Description of the Parameter
     *@param  startline Description of the Parameter
     *@param  startcolumn Description of the Parameter
     */
    public VrmlCharStream(java.io.Reader dstream, int startline,
            int startcolumn) {
        this(dstream, startline, startcolumn, 4096);
    }

    /**
     *  Description of the Method
     *
     *@param  dstream Description of the Parameter
     *@param  startline Description of the Parameter
     *@param  startcolumn Description of the Parameter
     *@param  buffersize Description of the Parameter
     */
    public void ReInit(java.io.Reader dstream, int startline,
            int startcolumn, int buffersize) {
        inputStream = dstream;
        line = startline;
        column = startcolumn - 1;

        if (buffer == null || buffersize != buffer.length) {
            available = bufsize = buffersize;
            buffer = new char[buffersize];
            bufline = new int[buffersize];
            bufcolumn = new int[buffersize];
        }
        prevCharIsLF = prevCharIsCR = false;
        tokenBegin = inBuf = maxNextCharInd = 0;
        bufpos = -1;
    }

    /**
     *  Description of the Method
     *
     *@param  dstream Description of the Parameter
     *@param  startline Description of the Parameter
     *@param  startcolumn Description of the Parameter
     */
    public void ReInit(java.io.Reader dstream, int startline,
            int startcolumn) {
        ReInit(dstream, startline, startcolumn, 4096);
    }

    /**
     *Constructor for the VrmlCharStream object
     *
     *@param  dstream Description of the Parameter
     *@param  startline Description of the Parameter
     *@param  startcolumn Description of the Parameter
     *@param  buffersize Description of the Parameter
     */
    public VrmlCharStream(java.io.InputStream dstream, int startline,
            int startcolumn, int buffersize) {
        this(new java.io.InputStreamReader(dstream), startline, startcolumn, 4096);
    }

    /**
     *Constructor for the VrmlCharStream object
     *
     *@param  dstream Description of the Parameter
     *@param  startline Description of the Parameter
     *@param  startcolumn Description of the Parameter
     */
    public VrmlCharStream(java.io.InputStream dstream, int startline,
            int startcolumn) {
        this(dstream, startline, startcolumn, 4096);
    }

    /**
     *  Description of the Method
     *
     *@param  dstream Description of the Parameter
     *@param  startline Description of the Parameter
     *@param  startcolumn Description of the Parameter
     *@param  buffersize Description of the Parameter
     */
    public void ReInit(java.io.InputStream dstream, int startline,
            int startcolumn, int buffersize) {
        ReInit(new java.io.InputStreamReader(dstream), startline, startcolumn, 4096);
    }

    /**
     *  Description of the Method
     *
     *@param  dstream Description of the Parameter
     *@param  startline Description of the Parameter
     *@param  startcolumn Description of the Parameter
     */
    public void ReInit(java.io.InputStream dstream, int startline,
            int startcolumn) {
        ReInit(dstream, startline, startcolumn, 4096);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public final String GetImage() {
        if (bufpos >= tokenBegin) {
            return new String(buffer, tokenBegin, bufpos - tokenBegin + 1);
        }
        else {
            return new String(buffer, tokenBegin, bufsize - tokenBegin) +
                    new String(buffer, 0, bufpos + 1);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  len Description of the Parameter
     *@return  Description of the Return Value
     */
    public final char[] GetSuffix(int len) {
        char[] ret = new char[len];

        if ((bufpos + 1) >= len) {
            System.arraycopy(buffer, bufpos - len + 1, ret, 0, len);
        }
        else {
            System.arraycopy(buffer, bufsize - (len - bufpos - 1), ret, 0,
                    len - bufpos - 1);
            System.arraycopy(buffer, 0, ret, len - bufpos - 1, bufpos + 1);
        }

        return ret;
    }

    /**  Description of the Method */
    public void Done() {
        buffer = null;
        bufline = null;
        bufcolumn = null;
    }

    /**
     * Method to adjust line and column numbers for the start of a token.<BR>
     *
     *@param  newLine Description of the Parameter
     *@param  newCol Description of the Parameter
     */
    public void adjustBeginLineColumn(int newLine, int newCol) {
        int start = tokenBegin;
        int len;

        if (bufpos >= tokenBegin) {
            len = bufpos - tokenBegin + inBuf + 1;
        }
        else {
            len = bufsize - tokenBegin + bufpos + 1 + inBuf;
        }

        int i = 0;

        int j = 0;

        int k = 0;
        int nextColDiff = 0;
        int columnDiff = 0;

        while (i < len &&
                bufline[j = start % bufsize] == bufline[k = ++start % bufsize]) {
            bufline[j] = newLine;
            nextColDiff = columnDiff + bufcolumn[k] - bufcolumn[j];
            bufcolumn[j] = newCol + columnDiff;
            columnDiff = nextColDiff;
            i++;
        }

        if (i < len) {
            bufline[j] = newLine++;
            bufcolumn[j] = newCol + columnDiff;

            while (i++ < len) {
                if (bufline[j = start % bufsize] != bufline[++start % bufsize]) {
                    bufline[j] = newLine++;
                }
                else {
                    bufline[j] = newLine;
                }
            }
        }

        line = bufline[j];
        column = bufcolumn[j];
    }

}

