// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParserTokenManager.java

package org.jdesktop.j3d.loaders.vrml97.impl;

import java.io.IOException;

// Referenced classes of package org.jdesktop.j3d.loaders.vrml97.impl:
//            TokenMgrError, ParserConstants, ASCII_UCodeESC_CharStream, Token

public class ParserTokenManager
    implements ParserConstants
{

    private final int jjStopStringLiteralDfa_0(int pos, long active0)
    {
        switch(pos)
        {
        case 0: // '\0'
            if((active0 & 0x3ffffbfffc000L) != 0L)
            {
                jjmatchedKind = 50;
                return 19;
            }
            return (active0 & 0x40000000L) == 0L ? -1 : 7;

        case 1: // '\001'
            if((active0 & 0x28000000L) != 0L)
                return 19;
            if((active0 & 0x3ffff97ffc000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 1;
                return 19;
            } else
            {
                return -1;
            }

        case 2: // '\002'
            if((active0 & 0x3ffff97f3c000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 2;
                return 19;
            }
            return (active0 & 0xc0000L) == 0L ? -1 : 19;

        case 3: // '\003'
            if((active0 & 0x3ffff87f30000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 3;
                return 19;
            }
            return (active0 & 0x1000c000L) == 0L ? -1 : 19;

        case 4: // '\004'
            if((active0 & 0x3ffff83600000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 4;
                return 19;
            }
            return (active0 & 0x4930000L) == 0L ? -1 : 19;

        case 5: // '\005'
            if((active0 & 0x36fb703600000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 5;
                return 19;
            }
            return (active0 & 0x904880000000L) == 0L ? -1 : 19;

        case 6: // '\006'
            if((active0 & 0x603003400000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 6;
                return 19;
            }
            return (active0 & 0x30f8700200000L) == 0L ? -1 : 19;

        case 7: // '\007'
            if((active0 & 0x201003000000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 7;
                return 19;
            }
            return (active0 & 0x402000400000L) == 0L ? -1 : 19;

        case 8: // '\b'
            if((active0 & 0x201003000000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 8;
                return 19;
            } else
            {
                return -1;
            }

        case 9: // '\t'
            if((active0 & 0x3000000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 9;
                return 19;
            }
            return (active0 & 0x201000000000L) == 0L ? -1 : 19;

        case 10: // '\n'
            if((active0 & 0x1000000L) != 0L)
            {
                jjmatchedKind = 50;
                jjmatchedPos = 10;
                return 19;
            }
            return (active0 & 0x2000000L) == 0L ? -1 : 19;
        }
        return -1;
    }

    private final int jjStartNfa_0(int pos, long active0)
    {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private final int jjStopAtPos(int pos, int kind)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private final int jjStartNfaWithStates_0(int pos, int kind, int state)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_0()
    {
        switch(curChar)
        {
        case 46: // '.'
            return jjStartNfaWithStates_0(0, 30, 7);

        case 68: // 'D'
            return jjMoveStringLiteralDfa1_0(0x40000L);

        case 69: // 'E'
            return jjMoveStringLiteralDfa1_0(0x2000000L);

        case 70: // 'F'
            return jjMoveStringLiteralDfa1_0(0x10000L);

        case 73: // 'I'
            return jjMoveStringLiteralDfa1_0(0x20000000L);

        case 77: // 'M'
            return jjMoveStringLiteralDfa1_0(0x3fe0000000000L);

        case 78: // 'N'
            return jjMoveStringLiteralDfa1_0(0x10000000L);

        case 80: // 'P'
            return jjMoveStringLiteralDfa1_0(0x100000L);

        case 82: // 'R'
            return jjMoveStringLiteralDfa1_0(0x4000000L);

        case 83: // 'S'
            return jjMoveStringLiteralDfa1_0(0x1ff80000000L);

        case 84: // 'T'
            return jjMoveStringLiteralDfa1_0(0x8004000L);

        case 85: // 'U'
            return jjMoveStringLiteralDfa1_0(0x80000L);

        case 91: // '['
            return jjStopAtPos(0, 12);

        case 93: // ']'
            return jjStopAtPos(0, 13);

        case 101: // 'e'
            return jjMoveStringLiteralDfa1_0(0x1600000L);

        case 102: // 'f'
            return jjMoveStringLiteralDfa1_0(0x820000L);

        case 116: // 't'
            return jjMoveStringLiteralDfa1_0(32768L);

        case 123: // '{'
            return jjStopAtPos(0, 10);

        case 125: // '}'
            return jjStopAtPos(0, 11);

        case 47: // '/'
        case 48: // '0'
        case 49: // '1'
        case 50: // '2'
        case 51: // '3'
        case 52: // '4'
        case 53: // '5'
        case 54: // '6'
        case 55: // '7'
        case 56: // '8'
        case 57: // '9'
        case 58: // ':'
        case 59: // ';'
        case 60: // '<'
        case 61: // '='
        case 62: // '>'
        case 63: // '?'
        case 64: // '@'
        case 65: // 'A'
        case 66: // 'B'
        case 67: // 'C'
        case 71: // 'G'
        case 72: // 'H'
        case 74: // 'J'
        case 75: // 'K'
        case 76: // 'L'
        case 79: // 'O'
        case 81: // 'Q'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 89: // 'Y'
        case 90: // 'Z'
        case 92: // '\\'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 97: // 'a'
        case 98: // 'b'
        case 99: // 'c'
        case 100: // 'd'
        case 103: // 'g'
        case 104: // 'h'
        case 105: // 'i'
        case 106: // 'j'
        case 107: // 'k'
        case 108: // 'l'
        case 109: // 'm'
        case 110: // 'n'
        case 111: // 'o'
        case 112: // 'p'
        case 113: // 'q'
        case 114: // 'r'
        case 115: // 's'
        case 117: // 'u'
        case 118: // 'v'
        case 119: // 'w'
        case 120: // 'x'
        case 121: // 'y'
        case 122: // 'z'
        case 124: // '|'
        default:
            return jjMoveNfa_0(0, 0);
        }
    }

    private final int jjMoveStringLiteralDfa1_0(long active0)
    {
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
        switch(curChar)
        {
        case 65: // 'A'
            return jjMoveStringLiteralDfa2_0(active0, 0x10000L);

        case 69: // 'E'
            return jjMoveStringLiteralDfa2_0(active0, 0x40000L);

        case 70: // 'F'
            return jjMoveStringLiteralDfa2_0(active0, 0x3ffff80000000L);

        case 79: // 'O'
            if((active0 & 0x8000000L) != 0L)
                return jjStartNfaWithStates_0(1, 27, 19);
            else
                return jjMoveStringLiteralDfa2_0(active0, 0x4000000L);

        case 82: // 'R'
            return jjMoveStringLiteralDfa2_0(active0, 0x104000L);

        case 83: // 'S'
            if((active0 & 0x20000000L) != 0L)
                return jjStartNfaWithStates_0(1, 29, 19);
            else
                return jjMoveStringLiteralDfa2_0(active0, 0x80000L);

        case 85: // 'U'
            return jjMoveStringLiteralDfa2_0(active0, 0x10000000L);

        case 88: // 'X'
            return jjMoveStringLiteralDfa2_0(active0, 0x2000000L);

        case 97: // 'a'
            return jjMoveStringLiteralDfa2_0(active0, 0x20000L);

        case 105: // 'i'
            return jjMoveStringLiteralDfa2_0(active0, 0x800000L);

        case 114: // 'r'
            return jjMoveStringLiteralDfa2_0(active0, 32768L);

        case 118: // 'v'
            return jjMoveStringLiteralDfa2_0(active0, 0x600000L);

        case 120: // 'x'
            return jjMoveStringLiteralDfa2_0(active0, 0x1000000L);
        }
        return jjStartNfa_0(0, active0);
    }

    private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(0, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(1, active0);
            return 2;
        }
        switch(curChar)
        {
        case 68: // 'D'
        case 71: // 'G'
        case 72: // 'H'
        case 74: // 'J'
        case 75: // 'K'
        case 77: // 'M'
        case 80: // 'P'
        case 81: // 'Q'
        case 87: // 'W'
        case 88: // 'X'
        case 89: // 'Y'
        case 90: // 'Z'
        case 91: // '['
        case 92: // '\\'
        case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 97: // 'a'
        case 98: // 'b'
        case 99: // 'c'
        case 100: // 'd'
        case 102: // 'f'
        case 103: // 'g'
        case 104: // 'h'
        case 105: // 'i'
        case 106: // 'j'
        case 107: // 'k'
        case 109: // 'm'
        case 110: // 'n'
        case 111: // 'o'
        case 113: // 'q'
        case 114: // 'r'
        case 115: // 's'
        case 116: // 't'
        default:
            break;

        case 66: // 'B'
            return jjMoveStringLiteralDfa3_0(active0, 0x80000000L);

        case 67: // 'C'
            return jjMoveStringLiteralDfa3_0(active0, 0x20100000000L);

        case 69: // 'E'
            if((active0 & 0x80000L) != 0L)
                return jjStartNfaWithStates_0(2, 19, 19);
            break;

        case 70: // 'F'
            if((active0 & 0x40000L) != 0L)
                return jjStartNfaWithStates_0(2, 18, 19);
            else
                return jjMoveStringLiteralDfa3_0(active0, 0x40200000000L);

        case 73: // 'I'
            return jjMoveStringLiteralDfa3_0(active0, 0x80400000000L);

        case 76: // 'L'
            return jjMoveStringLiteralDfa3_0(active0, 0x10010000L);

        case 78: // 'N'
            return jjMoveStringLiteralDfa3_0(active0, 0x100800000000L);

        case 79: // 'O'
            return jjMoveStringLiteralDfa3_0(active0, 0x100000L);

        case 82: // 'R'
            return jjMoveStringLiteralDfa3_0(active0, 0x201000000000L);

        case 83: // 'S'
            return jjMoveStringLiteralDfa3_0(active0, 0x402000000000L);

        case 84: // 'T'
            return jjMoveStringLiteralDfa3_0(active0, 0x804002000000L);

        case 85: // 'U'
            return jjMoveStringLiteralDfa3_0(active0, 0x4004000L);

        case 86: // 'V'
            return jjMoveStringLiteralDfa3_0(active0, 0x3018000000000L);

        case 101: // 'e'
            return jjMoveStringLiteralDfa3_0(active0, 0xe00000L);

        case 108: // 'l'
            return jjMoveStringLiteralDfa3_0(active0, 0x20000L);

        case 112: // 'p'
            return jjMoveStringLiteralDfa3_0(active0, 0x1000000L);

        case 117: // 'u'
            return jjMoveStringLiteralDfa3_0(active0, 32768L);
        }
        return jjStartNfa_0(1, active0);
    }

    private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(1, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(2, active0);
            return 3;
        }
        switch(curChar)
        {
        default:
            break;

        case 69: // 'E'
            if((active0 & 16384L) != 0L)
                return jjStartNfaWithStates_0(3, 14, 19);
            else
                return jjMoveStringLiteralDfa4_0(active0, 0x2000000L);

        case 76: // 'L'
            if((active0 & 0x10000000L) != 0L)
                return jjStartNfaWithStates_0(3, 28, 19);
            break;

        case 83: // 'S'
            return jjMoveStringLiteralDfa4_0(active0, 0x10000L);

        case 84: // 'T'
            return jjMoveStringLiteralDfa4_0(active0, 0x4100000L);

        case 101: // 'e'
            if((active0 & 32768L) != 0L)
                return jjStartNfaWithStates_0(3, 15, 19);
            else
                return jjMoveStringLiteralDfa4_0(active0, 0x3018000000000L);

        case 105: // 'i'
            return jjMoveStringLiteralDfa4_0(active0, 0x804000000000L);

        case 108: // 'l'
            return jjMoveStringLiteralDfa4_0(active0, 0x40200800000L);

        case 110: // 'n'
            return jjMoveStringLiteralDfa4_0(active0, 0x80400600000L);

        case 111: // 'o'
            return jjMoveStringLiteralDfa4_0(active0, 0x321981000000L);

        case 115: // 's'
            return jjMoveStringLiteralDfa4_0(active0, 0x20000L);

        case 116: // 't'
            return jjMoveStringLiteralDfa4_0(active0, 0x402000000000L);
        }
        return jjStartNfa_0(2, active0);
    }

    private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(2, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(3, active0);
            return 4;
        }
        switch(curChar)
        {
        case 70: // 'F'
        case 71: // 'G'
        case 72: // 'H'
        case 73: // 'I'
        case 74: // 'J'
        case 75: // 'K'
        case 76: // 'L'
        case 77: // 'M'
        case 78: // 'N'
        case 80: // 'P'
        case 81: // 'Q'
        case 83: // 'S'
        case 84: // 'T'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 89: // 'Y'
        case 90: // 'Z'
        case 91: // '['
        case 92: // '\\'
        case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 97: // 'a'
        case 98: // 'b'
        case 102: // 'f'
        case 103: // 'g'
        case 104: // 'h'
        case 105: // 'i'
        case 106: // 'j'
        case 107: // 'k'
        case 110: // 'n'
        case 112: // 'p'
        case 113: // 'q'
        default:
            break;

        case 69: // 'E'
            if((active0 & 0x10000L) != 0L)
                return jjStartNfaWithStates_0(4, 16, 19);
            if((active0 & 0x4000000L) != 0L)
                return jjStartNfaWithStates_0(4, 26, 19);
            break;

        case 79: // 'O'
            if((active0 & 0x100000L) != 0L)
                return jjStartNfaWithStates_0(4, 20, 19);
            break;

        case 82: // 'R'
            return jjMoveStringLiteralDfa5_0(active0, 0x2000000L);

        case 99: // 'c'
            return jjMoveStringLiteralDfa5_0(active0, 0x3018000000000L);

        case 100: // 'd'
            if((active0 & 0x800000L) != 0L)
                return jjStartNfaWithStates_0(4, 23, 19);
            else
                return jjMoveStringLiteralDfa5_0(active0, 0x100800000000L);

        case 101: // 'e'
            if((active0 & 0x20000L) != 0L)
                return jjStartNfaWithStates_0(4, 17, 19);
            break;

        case 108: // 'l'
            return jjMoveStringLiteralDfa5_0(active0, 0x20100000000L);

        case 109: // 'm'
            return jjMoveStringLiteralDfa5_0(active0, 0x804000000000L);

        case 111: // 'o'
            return jjMoveStringLiteralDfa5_0(active0, 0x40280000000L);

        case 114: // 'r'
            return jjMoveStringLiteralDfa5_0(active0, 0x402000000000L);

        case 115: // 's'
            return jjMoveStringLiteralDfa5_0(active0, 0x1000000L);

        case 116: // 't'
            return jjMoveStringLiteralDfa5_0(active0, 0x281400600000L);
        }
        return jjStartNfa_0(3, active0);
    }

    private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(3, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(4, active0);
            return 5;
        }
        switch(curChar)
        {
        case 50: // '2'
            return jjMoveStringLiteralDfa6_0(active0, 0x1008000000000L);

        case 51: // '3'
            return jjMoveStringLiteralDfa6_0(active0, 0x2090400000000L);

        case 73: // 'I'
            return jjMoveStringLiteralDfa6_0(active0, 0x200000L);

        case 78: // 'N'
            return jjMoveStringLiteralDfa6_0(active0, 0x2000000L);

        case 79: // 'O'
            return jjMoveStringLiteralDfa6_0(active0, 0x400000L);

        case 97: // 'a'
            return jjMoveStringLiteralDfa6_0(active0, 0x241200000000L);

        case 101: // 'e'
            if((active0 & 0x800000000L) != 0L)
                return jjStartNfaWithStates_0(5, 35, 19);
            if((active0 & 0x4000000000L) != 0L)
                return jjStartNfaWithStates_0(5, 38, 19);
            if((active0 & 0x100000000000L) != 0L)
                return jjStartNfaWithStates_0(5, 44, 19);
            if((active0 & 0x800000000000L) != 0L)
                return jjStartNfaWithStates_0(5, 47, 19);
            else
                return jjMoveStringLiteralDfa6_0(active0, 0x1000000L);

        case 105: // 'i'
            return jjMoveStringLiteralDfa6_0(active0, 0x402000000000L);

        case 108: // 'l'
            if((active0 & 0x80000000L) != 0L)
                return jjStartNfaWithStates_0(5, 31, 19);
            break;

        case 111: // 'o'
            return jjMoveStringLiteralDfa6_0(active0, 0x20100000000L);
        }
        return jjStartNfa_0(4, active0);
    }

    private final int jjMoveStringLiteralDfa6_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(4, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(5, active0);
            return 6;
        }
        switch(curChar)
        {
        default:
            break;

        case 50: // '2'
            if((active0 & 0x400000000L) != 0L)
                return jjStartNfaWithStates_0(6, 34, 19);
            if((active0 & 0x80000000000L) != 0L)
                return jjStartNfaWithStates_0(6, 43, 19);
            break;

        case 80: // 'P'
            return jjMoveStringLiteralDfa7_0(active0, 0x2000000L);

        case 100: // 'd'
            return jjMoveStringLiteralDfa7_0(active0, 0x1000000L);

        case 102: // 'f'
            if((active0 & 0x8000000000L) != 0L)
                return jjStartNfaWithStates_0(6, 39, 19);
            if((active0 & 0x10000000000L) != 0L)
                return jjStartNfaWithStates_0(6, 40, 19);
            if((active0 & 0x1000000000000L) != 0L)
                return jjStartNfaWithStates_0(6, 48, 19);
            if((active0 & 0x2000000000000L) != 0L)
                return jjStartNfaWithStates_0(6, 49, 19);
            break;

        case 110: // 'n'
            if((active0 & 0x200000L) != 0L)
                return jjStartNfaWithStates_0(6, 21, 19);
            else
                return jjMoveStringLiteralDfa7_0(active0, 0x402000000000L);

        case 114: // 'r'
            if((active0 & 0x100000000L) != 0L)
                return jjStartNfaWithStates_0(6, 32, 19);
            if((active0 & 0x20000000000L) != 0L)
                return jjStartNfaWithStates_0(6, 41, 19);
            break;

        case 116: // 't'
            if((active0 & 0x200000000L) != 0L)
                return jjStartNfaWithStates_0(6, 33, 19);
            if((active0 & 0x40000000000L) != 0L)
                return jjStartNfaWithStates_0(6, 42, 19);
            else
                return jjMoveStringLiteralDfa7_0(active0, 0x201000000000L);

        case 117: // 'u'
            return jjMoveStringLiteralDfa7_0(active0, 0x400000L);
        }
        return jjStartNfa_0(5, active0);
    }

    private final int jjMoveStringLiteralDfa7_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(5, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(6, active0);
            return 7;
        }
        switch(curChar)
        {
        default:
            break;

        case 70: // 'F'
            return jjMoveStringLiteralDfa8_0(active0, 0x1000000L);

        case 82: // 'R'
            return jjMoveStringLiteralDfa8_0(active0, 0x2000000L);

        case 103: // 'g'
            if((active0 & 0x2000000000L) != 0L)
                return jjStartNfaWithStates_0(7, 37, 19);
            if((active0 & 0x400000000000L) != 0L)
                return jjStartNfaWithStates_0(7, 46, 19);
            break;

        case 105: // 'i'
            return jjMoveStringLiteralDfa8_0(active0, 0x201000000000L);

        case 116: // 't'
            if((active0 & 0x400000L) != 0L)
                return jjStartNfaWithStates_0(7, 22, 19);
            break;
        }
        return jjStartNfa_0(6, active0);
    }

    private final int jjMoveStringLiteralDfa8_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(6, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(7, active0);
            return 8;
        }
        switch(curChar)
        {
        case 79: // 'O'
            return jjMoveStringLiteralDfa9_0(active0, 0x2000000L);

        case 105: // 'i'
            return jjMoveStringLiteralDfa9_0(active0, 0x1000000L);

        case 111: // 'o'
            return jjMoveStringLiteralDfa9_0(active0, 0x201000000000L);
        }
        return jjStartNfa_0(7, active0);
    }

    private final int jjMoveStringLiteralDfa9_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(7, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(8, active0);
            return 9;
        }
        switch(curChar)
        {
        case 84: // 'T'
            return jjMoveStringLiteralDfa10_0(active0, 0x2000000L);

        case 101: // 'e'
            return jjMoveStringLiteralDfa10_0(active0, 0x1000000L);

        case 110: // 'n'
            if((active0 & 0x1000000000L) != 0L)
                return jjStartNfaWithStates_0(9, 36, 19);
            if((active0 & 0x200000000000L) != 0L)
                return jjStartNfaWithStates_0(9, 45, 19);
            break;
        }
        return jjStartNfa_0(8, active0);
    }

    private final int jjMoveStringLiteralDfa10_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(8, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(9, active0);
            return 10;
        }
        switch(curChar)
        {
        case 79: // 'O'
            if((active0 & 0x2000000L) != 0L)
                return jjStartNfaWithStates_0(10, 25, 19);
            break;

        case 108: // 'l'
            return jjMoveStringLiteralDfa11_0(active0, 0x1000000L);
        }
        return jjStartNfa_0(9, active0);
    }

    private final int jjMoveStringLiteralDfa11_0(long old0, long active0)
    {
        if((active0 &= old0) == 0L)
            return jjStartNfa_0(9, old0);
        try
        {
            curChar = input_stream.readChar();
        }
        catch(IOException e)
        {
            jjStopStringLiteralDfa_0(10, active0);
            return 11;
        }
        switch(curChar)
        {
        case 100: // 'd'
            if((active0 & 0x1000000L) != 0L)
                return jjStartNfaWithStates_0(11, 24, 19);
            break;
        }
        return jjStartNfa_0(10, active0);
    }

    private final void jjCheckNAdd(int state)
    {
        if(jjrounds[state] != jjround)
        {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private final void jjAddStates(int start, int end)
    {
        do
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        while(start++ != end);
    }

    private final void jjCheckNAddTwoStates(int state1, int state2)
    {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private final void jjCheckNAddStates(int start, int end)
    {
        do
            jjCheckNAdd(jjnextStates[start]);
        while(start++ != end);
    }

    private final void jjCheckNAddStates(int start)
    {
        jjCheckNAdd(jjnextStates[start]);
        jjCheckNAdd(jjnextStates[start + 1]);
    }

    private final int jjMoveNfa_0(int startState, int curPos)
    {
        int startsAt = 0;
        jjnewStateCnt = 20;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        do
        {
            if(++jjround == 0x7fffffff)
                ReInitRounds();
            if(curChar < '@')
            {
                long l = 1L << curChar;
                do
                    switch(jjstateSet[--i])
                    {
                    case 0: // '\0'
                        if((0xfc00873200000000L & l) != 0L)
                        {
                            if(kind > 50)
                                kind = 50;
                            jjCheckNAdd(19);
                        } else
                        if((0x3ff000000000000L & l) != 0L)
                        {
                            if(kind > 8)
                                kind = 8;
                            jjCheckNAdd(8);
                        } else
                        if(curChar == '"')
                            jjCheckNAddStates(0, 2);
                        else
                        if(curChar == '.')
                            jjCheckNAdd(7);
                        else
                        if(curChar == '-')
                            jjCheckNAddTwoStates(6, 7);
                        else
                        if(curChar == '#')
                            jjCheckNAddStates(3, 5);
                        break;

                    case 1: // '\001'
                        if((-9217L & l) != 0L)
                            jjCheckNAddStates(3, 5);
                        break;

                    case 2: // '\002'
                        if((9216L & l) != 0L && kind > 7)
                            kind = 7;
                        break;

                    case 3: // '\003'
                        if(curChar == '\n' && kind > 7)
                            kind = 7;
                        break;

                    case 4: // '\004'
                        if(curChar == '\r')
                            jjstateSet[jjnewStateCnt++] = 3;
                        break;

                    case 5: // '\005'
                        if(curChar == '-')
                            jjCheckNAddTwoStates(6, 7);
                        break;

                    case 6: // '\006'
                        if(curChar == '.')
                            jjCheckNAdd(7);
                        break;

                    case 7: // '\007'
                        if((0x3ff000000000000L & l) != 0L)
                        {
                            if(kind > 8)
                                kind = 8;
                            jjCheckNAdd(8);
                        }
                        break;

                    case 8: // '\b'
                        if((0x3ff680000000000L & l) != 0L)
                        {
                            if(kind > 8)
                                kind = 8;
                            jjCheckNAdd(8);
                        }
                        break;

                    case 9: // '\t'
                        if(curChar == '"')
                            jjCheckNAddStates(0, 2);
                        break;

                    case 10: // '\n'
                        if((0xfffffffbffffffffL & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 12: // '\f'
                        if((0x8400000000L & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 13: // '\r'
                        if(curChar == '"' && kind > 9)
                            kind = 9;
                        break;

                    case 14: // '\016'
                        if((0xff000000000000L & l) != 0L)
                            jjCheckNAddStates(6, 9);
                        break;

                    case 15: // '\017'
                        if((0xff000000000000L & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 16: // '\020'
                        if((0xf000000000000L & l) != 0L)
                            jjstateSet[jjnewStateCnt++] = 17;
                        break;

                    case 17: // '\021'
                        if((0xff000000000000L & l) != 0L)
                            jjCheckNAdd(15);
                        break;

                    case 18: // '\022'
                        if((0xfc00873200000000L & l) != 0L)
                        {
                            if(kind > 50)
                                kind = 50;
                            jjCheckNAdd(19);
                        }
                        break;

                    case 19: // '\023'
                        if((0xffffaf3200000000L & l) != 0L)
                        {
                            if(kind > 50)
                                kind = 50;
                            jjCheckNAdd(19);
                        }
                        break;
                    }
                while(i != startsAt);
            } else
            if(curChar < '\200')
            {
                long l = 1L << (curChar & 0x3f);
                do
                    switch(jjstateSet[--i])
                    {
                    case 0: // '\0'
                    case 19: // '\023'
                        if((0x57ffffffc7ffffffL & l) != 0L)
                        {
                            if(kind > 50)
                                kind = 50;
                            jjCheckNAdd(19);
                        }
                        break;

                    case 1: // '\001'
                        jjAddStates(3, 5);
                        break;

                    case 8: // '\b'
                        if((0x100007e0000007eL & l) != 0L)
                        {
                            if(kind > 8)
                                kind = 8;
                            jjstateSet[jjnewStateCnt++] = 8;
                        }
                        break;

                    case 10: // '\n'
                        if((0xffffffffefffffffL & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 11: // '\013'
                        if(curChar == '\\')
                            jjAddStates(10, 12);
                        break;

                    case 12: // '\f'
                        if((0x14404410000000L & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;
                    }
                while(i != startsAt);
            } else
            {
                int hiByte = curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 0x3f);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 0x3f);
                do
                    switch(jjstateSet[--i])
                    {
                    case 1: // '\001'
                        if(jjCanMove_0(hiByte, i1, i2, l1, l2))
                            jjAddStates(3, 5);
                        break;

                    case 10: // '\n'
                        if(jjCanMove_0(hiByte, i1, i2, l1, l2))
                            jjAddStates(0, 2);
                        break;
                    }
                while(i != startsAt);
            }
            if(kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            curPos++;
            if((i = jjnewStateCnt) == (startsAt = 20 - (jjnewStateCnt = startsAt)))
                return curPos;
            try
            {
                curChar = input_stream.readChar();
            }
            catch(IOException e)
            {
                return curPos;
            }
        } while(true);
    }

    private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
    {
        switch(hiByte)
        {
        case 0: // '\0'
            return (jjbitVec2[i2] & l2) != 0L;
        }
        return (jjbitVec0[i1] & l1) != 0L;
    }

    public ParserTokenManager(ASCII_UCodeESC_CharStream stream)
    {
        jjrounds = new int[20];
        jjstateSet = new int[40];
        curLexState = 0;
        defaultLexState = 0;
        input_stream = stream;
    }

    public ParserTokenManager(ASCII_UCodeESC_CharStream stream, int lexState)
    {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(ASCII_UCodeESC_CharStream stream)
    {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private final void ReInitRounds()
    {
        jjround = 0x80000001;
        for(int i = 20; i-- > 0;)
            jjrounds[i] = 0x80000000;

    }

    public void ReInit(ASCII_UCodeESC_CharStream stream, int lexState)
    {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState)
    {
        if(lexState >= 1 || lexState < 0)
        {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        } else
        {
            curLexState = lexState;
            return;
        }
    }

    private final Token jjFillToken()
    {
        Token t = Token.newToken(jjmatchedKind);
        t.kind = jjmatchedKind;
        String im = jjstrLiteralImages[jjmatchedKind];
        t.image = im != null ? im : input_stream.GetImage();
        t.beginLine = input_stream.getBeginLine();
        t.beginColumn = input_stream.getBeginColumn();
        t.endLine = input_stream.getEndLine();
        t.endColumn = input_stream.getEndColumn();
        return t;
    }

    public final Token getNextToken()
    {
        Token specialToken = null;
        int curPos = 0;
        do
        {
            try
            {
                curChar = input_stream.BeginToken();
            }
            catch(IOException e)
            {
                jjmatchedKind = 0;
                Token matchedToken = jjFillToken();
                matchedToken.specialToken = specialToken;
                return matchedToken;
            }
            try
            {
                input_stream.backup(0);
                for(; curChar <= ',' && (0x100100003600L & 1L << curChar) != 0L; curChar = input_stream.BeginToken());
            }
            catch(IOException e1)
            {
                continue;
            }
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_0();
            if(jjmatchedKind == 0x7fffffff)
                break;
            if(jjmatchedPos + 1 < curPos)
                input_stream.backup(curPos - jjmatchedPos - 1);
            if((jjtoToken[jjmatchedKind >> 6] & 1L << (jjmatchedKind & 0x3f)) != 0L)
            {
                Token matchedToken = jjFillToken();
                matchedToken.specialToken = specialToken;
                return matchedToken;
            }
            if((jjtoSpecial[jjmatchedKind >> 6] & 1L << (jjmatchedKind & 0x3f)) != 0L)
            {
                Token matchedToken = jjFillToken();
                if(specialToken == null)
                {
                    specialToken = matchedToken;
                } else
                {
                    matchedToken.specialToken = specialToken;
                    specialToken = specialToken.next = matchedToken;
                }
            }
        } while(true);
        int error_line = input_stream.getEndLine();
        int error_column = input_stream.getEndColumn();
        String error_after = null;
        boolean EOFSeen = false;
        try
        {
            input_stream.readChar();
            input_stream.backup(1);
        }
        catch(IOException e1)
        {
            EOFSeen = true;
            error_after = curPos > 1 ? input_stream.GetImage() : "";
            if(curChar == '\n' || curChar == '\r')
            {
                error_line++;
                error_column = 0;
            } else
            {
                error_column++;
            }
        }
        if(!EOFSeen)
        {
            input_stream.backup(1);
            error_after = curPos > 1 ? input_stream.GetImage() : "";
        }
        throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, 0);
    }

    static final long jjbitVec0[] = {
        -2L, -1L, -1L, -1L
    };
    static final long jjbitVec2[] = {
        0L, 0L, -1L, -1L
    };
    static final int jjnextStates[] = {
        10, 11, 13, 1, 2, 4, 10, 11, 15, 13, 
        12, 14, 16
    };
    public static final String jjstrLiteralImages[] = {
        "", null, null, null, null, null, null, null, null, null, 
        "{", "}", "[", "]", "TRUE", "true", "FALSE", "false", "DEF", "USE", 
        "PROTO", "eventIn", "eventOut", "field", "exposedField", "EXTERNPROTO", "ROUTE", "TO", "NULL", "IS", 
        ".", "SFBool", "SFColor", "SFFloat", "SFInt32", "SFNode", "SFRotation", "SFString", "SFTime", "SFVec2f", 
        "SFVec3f", "MFColor", "MFFloat", "MFInt32", "MFNode", "MFRotation", "MFString", "MFTime", "MFVec2f", "MFVec3f", 
        null, null, null
    };
    public static final String lexStateNames[] = {
        "DEFAULT"
    };
    static final long jjtoToken[] = {
        0x7ffffffffff01L
    };
    static final long jjtoSkip[] = {
        254L
    };
    static final long jjtoSpecial[] = {
        128L
    };
    private ASCII_UCodeESC_CharStream input_stream;
    private final int jjrounds[];
    private final int jjstateSet[];
    protected char curChar;
    int curLexState;
    int defaultLexState;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;

}
