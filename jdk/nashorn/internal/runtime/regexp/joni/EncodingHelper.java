// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import java.util.Arrays;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;

public final class EncodingHelper
{
    static final int NEW_LINE = 10;
    static final int RETURN = 13;
    static final int LINE_SEPARATOR = 8232;
    static final int PARAGRAPH_SEPARATOR = 8233;
    static final char[] EMPTYCHARS;
    static final int[][] codeRanges;
    
    public static int digitVal(final int code) {
        return code - 48;
    }
    
    public static int odigitVal(final int code) {
        return digitVal(code);
    }
    
    public static boolean isXDigit(final int code) {
        return Character.isDigit(code) || (code >= 97 && code <= 102) || (code >= 65 && code <= 70);
    }
    
    public static int xdigitVal(final int code) {
        if (Character.isDigit(code)) {
            return code - 48;
        }
        if (code >= 97 && code <= 102) {
            return code - 97 + 10;
        }
        return code - 65 + 10;
    }
    
    public static boolean isDigit(final int code) {
        return code >= 48 && code <= 57;
    }
    
    public static boolean isWord(final int code) {
        return (1 << Character.getType(code) & 0x8003FE) != 0x0;
    }
    
    public static boolean isNewLine(final int code) {
        return code == 10 || code == 13 || code == 8232 || code == 8233;
    }
    
    public static boolean isNewLine(final char[] chars, final int p, final int end) {
        return p < end && isNewLine(chars[p]);
    }
    
    public static int prevCharHead(final int p, final int s) {
        return (s <= p) ? -1 : (s - 1);
    }
    
    public static int rightAdjustCharHeadWithPrev(final int s, final IntHolder prev) {
        if (prev != null) {
            prev.value = -1;
        }
        return s;
    }
    
    public static int stepBack(final int p, final int sp, final int np) {
        int s = sp;
        for (int n = np; s != -1 && n-- > 0; --s) {
            if (s <= p) {
                return -1;
            }
        }
        return s;
    }
    
    public static int mbcodeStartPosition() {
        return 128;
    }
    
    public static char[] caseFoldCodesByString(final int flag, final char c) {
        char[] codes = EncodingHelper.EMPTYCHARS;
        final char upper = toUpperCase(c);
        if (upper != toLowerCase(upper)) {
            int count = 0;
            char ch = '\0';
            char c2;
            do {
                final char u = toUpperCase(ch);
                if (u == upper && ch != c) {
                    codes = ((count == 0) ? new char[1] : Arrays.copyOf(codes, count + 1));
                    codes[count++] = ch;
                }
                c2 = ch;
                ++ch;
            } while (c2 < '\uffff');
        }
        return codes;
    }
    
    public static void applyAllCaseFold(final int flag, final ApplyCaseFold fun, final Object arg) {
        for (int c = 0; c < 65535; ++c) {
            if (Character.isLowerCase(c)) {
                final int upper = toUpperCase(c);
                if (upper != c) {
                    ApplyCaseFold.apply(c, upper, arg);
                }
            }
        }
        for (int c = 0; c < 65535; ++c) {
            if (Character.isLowerCase(c)) {
                final int upper = toUpperCase(c);
                if (upper != c) {
                    ApplyCaseFold.apply(upper, c, arg);
                }
            }
        }
    }
    
    public static char toLowerCase(final char c) {
        return (char)toLowerCase((int)c);
    }
    
    public static int toLowerCase(final int c) {
        if (c < 128) {
            return (65 <= c && c <= 90) ? (c + 32) : c;
        }
        final int lower = Character.toLowerCase(c);
        return (lower < 128) ? c : lower;
    }
    
    public static char toUpperCase(final char c) {
        return (char)toUpperCase((int)c);
    }
    
    public static int toUpperCase(final int c) {
        if (c < 128) {
            return (97 <= c && c <= 122) ? (c - 32) : c;
        }
        final int upper = Character.toUpperCase(c);
        return (upper < 128) ? c : upper;
    }
    
    public static int[] ctypeCodeRange(final int ctype, final IntHolder sbOut) {
        sbOut.value = 256;
        int[] range = null;
        if (ctype < EncodingHelper.codeRanges.length) {
            range = EncodingHelper.codeRanges[ctype];
            if (range == null) {
                range = new int[16];
                int rangeCount = 0;
                int lastCode = -2;
                for (int code = 0; code <= 65535; ++code) {
                    if (isCodeCType(code, ctype)) {
                        if (lastCode < code - 1) {
                            if (rangeCount * 2 + 2 >= range.length) {
                                range = Arrays.copyOf(range, range.length * 2);
                            }
                            range[rangeCount * 2 + 1] = code;
                            ++rangeCount;
                        }
                        lastCode = (range[rangeCount * 2] = code);
                    }
                }
                if (rangeCount * 2 + 1 < range.length) {
                    range = Arrays.copyOf(range, rangeCount * 2 + 1);
                }
                range[0] = rangeCount;
                EncodingHelper.codeRanges[ctype] = range;
            }
        }
        return range;
    }
    
    public static boolean isInCodeRange(final int[] p, final int offset, final int code) {
        int low = 0;
        int high;
        final int n = high = p[offset];
        while (low < high) {
            final int x = low + high >> 1;
            if (code > p[(x << 1) + 2 + offset]) {
                low = x + 1;
            }
            else {
                high = x;
            }
        }
        return low < n && code >= p[(low << 1) + 1 + offset];
    }
    
    public static boolean isCodeCType(final int code, final int ctype) {
        switch (ctype) {
            case 0: {
                return isNewLine(code);
            }
            case 1: {
                return (1 << Character.getType(code) & 0x1FE) != 0x0;
            }
            case 2: {
                return code == 9 || Character.getType(code) == 12;
            }
            case 3: {
                final int type = Character.getType(code);
                return (1 << type & 0xD8000) != 0x0 || type == 0;
            }
            case 4: {
                return isDigit(code);
            }
            case 5: {
                switch (code) {
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13: {
                        return false;
                    }
                    default: {
                        final int type = Character.getType(code);
                        return (1 << type & 0x8F000) == 0x0 && type != 0;
                    }
                }
                break;
            }
            case 6: {
                return Character.isLowerCase(code);
            }
            case 7: {
                final int type = Character.getType(code);
                return (1 << type & 0x88000) == 0x0 && type != 0;
            }
            case 8: {
                return (1 << Character.getType(code) & 0x61F00000) != 0x0;
            }
            case 9: {
                switch (code) {
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13: {
                        return true;
                    }
                    default: {
                        return (1 << Character.getType(code) & 0x7000) != 0x0 || code == 65279;
                    }
                }
                break;
            }
            case 10: {
                return Character.isUpperCase(code);
            }
            case 11: {
                return isXDigit(code);
            }
            case 12: {
                return (1 << Character.getType(code) & 0x8003FE) != 0x0;
            }
            case 13: {
                return (1 << Character.getType(code) & 0x3FE) != 0x0;
            }
            case 14: {
                return code < 128;
            }
            default: {
                throw new RuntimeException("illegal character type: " + ctype);
            }
        }
    }
    
    static {
        EMPTYCHARS = new char[0];
        codeRanges = new int[15][];
    }
}
