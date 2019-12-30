// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

public final class URIUtils
{
    private static final String URI_UNESCAPED_NONALPHANUMERIC = "-_.!~*'()";
    private static final String URI_RESERVED = ";/?:@&=+$,#";
    
    private URIUtils() {
    }
    
    static String encodeURI(final Object self, final String string) {
        return encode(self, string, false);
    }
    
    static String encodeURIComponent(final Object self, final String string) {
        return encode(self, string, true);
    }
    
    static String decodeURI(final Object self, final String string) {
        return decode(self, string, false);
    }
    
    static String decodeURIComponent(final Object self, final String string) {
        return decode(self, string, true);
    }
    
    private static String encode(final Object self, final String string, final boolean component) {
        if (string.isEmpty()) {
            return string;
        }
        final int len = string.length();
        final StringBuilder sb = new StringBuilder();
        for (int k = 0; k < len; ++k) {
            final char C = string.charAt(k);
            if (isUnescaped(C, component)) {
                sb.append(C);
            }
            else {
                if (C >= '\udc00' && C <= '\udfff') {
                    return error(string, k);
                }
                int V;
                if (C < '\ud800' || C > '\udbff') {
                    V = C;
                }
                else {
                    if (++k == len) {
                        return error(string, k);
                    }
                    final char kChar = string.charAt(k);
                    if (kChar < '\udc00' || kChar > '\udfff') {
                        return error(string, k);
                    }
                    V = (C - '\ud800') * 1024 + (kChar - '\udc00') + 65536;
                }
                try {
                    sb.append(toHexEscape(V));
                }
                catch (Exception e) {
                    throw ECMAErrors.uriError(e, "bad.uri", string, Integer.toString(k));
                }
            }
        }
        return sb.toString();
    }
    
    private static String decode(final Object self, final String string, final boolean component) {
        if (string.isEmpty()) {
            return string;
        }
        final int len = string.length();
        final StringBuilder sb = new StringBuilder();
        for (int k = 0; k < len; ++k) {
            final char ch = string.charAt(k);
            if (ch != '%') {
                sb.append(ch);
            }
            else {
                final int start = k;
                if (k + 2 >= len) {
                    return error(string, k);
                }
                int B = toHexByte(string.charAt(k + 1), string.charAt(k + 2));
                if (B < 0) {
                    return error(string, k + 1);
                }
                k += 2;
                if ((B & 0x80) == 0x0) {
                    final char C = (char)B;
                    if (!component && ";/?:@&=+$,#".indexOf(C) >= 0) {
                        for (int j = start; j <= k; ++j) {
                            sb.append(string.charAt(j));
                        }
                    }
                    else {
                        sb.append(C);
                    }
                }
                else {
                    if ((B & 0xC0) == 0x80) {
                        return error(string, k);
                    }
                    int n;
                    int V;
                    int minV;
                    if ((B & 0x20) == 0x0) {
                        n = 2;
                        V = (B & 0x1F);
                        minV = 128;
                    }
                    else if ((B & 0x10) == 0x0) {
                        n = 3;
                        V = (B & 0xF);
                        minV = 2048;
                    }
                    else if ((B & 0x8) == 0x0) {
                        n = 4;
                        V = (B & 0x7);
                        minV = 65536;
                    }
                    else if ((B & 0x4) == 0x0) {
                        n = 5;
                        V = (B & 0x3);
                        minV = 2097152;
                    }
                    else {
                        if ((B & 0x2) != 0x0) {
                            return error(string, k);
                        }
                        n = 6;
                        V = (B & 0x1);
                        minV = 67108864;
                    }
                    if (k + 3 * (n - 1) >= len) {
                        return error(string, k);
                    }
                    for (int i = 1; i < n; ++i) {
                        ++k;
                        if (string.charAt(k) != '%') {
                            return error(string, k);
                        }
                        B = toHexByte(string.charAt(k + 1), string.charAt(k + 2));
                        if (B < 0 || (B & 0xC0) != 0x80) {
                            return error(string, k + 1);
                        }
                        V = (V << 6 | (B & 0x3F));
                        k += 2;
                    }
                    if (V < minV || (V >= 55296 && V <= 57343)) {
                        V = Integer.MAX_VALUE;
                    }
                    if (V < 65536) {
                        final char C = (char)V;
                        if (!component && ";/?:@&=+$,#".indexOf(C) >= 0) {
                            for (int i = start; i != k; ++i) {
                                sb.append(string.charAt(i));
                            }
                        }
                        else {
                            sb.append(C);
                        }
                    }
                    else {
                        if (V > 1114111) {
                            return error(string, k);
                        }
                        final int L = (V - 65536 & 0x3FF) + 56320;
                        final int H = (V - 65536 >> 10 & 0x3FF) + 55296;
                        sb.append((char)H);
                        sb.append((char)L);
                    }
                }
            }
        }
        return sb.toString();
    }
    
    private static int hexDigit(final char ch) {
        final char chu = Character.toUpperCase(ch);
        if (chu >= '0' && chu <= '9') {
            return chu - '0';
        }
        if (chu >= 'A' && chu <= 'F') {
            return chu - 'A' + 10;
        }
        return -1;
    }
    
    private static int toHexByte(final char ch1, final char ch2) {
        final int i1 = hexDigit(ch1);
        final int i2 = hexDigit(ch2);
        if (i1 >= 0 && i2 >= 0) {
            return i1 << 4 | i2;
        }
        return -1;
    }
    
    private static String toHexEscape(final int u0) {
        int u = u0;
        final byte[] b = new byte[6];
        int len;
        if (u <= 127) {
            b[0] = (byte)u;
            len = 1;
        }
        else {
            len = 2;
            for (int mask = u >>> 11; mask != 0; mask >>>= 5) {
                ++len;
            }
            for (int i = len - 1; i > 0; --i) {
                b[i] = (byte)(0x80 | (u & 0x3F));
                u >>>= 6;
            }
            b[0] = (byte)(~((1 << 8 - len) - 1) | u);
        }
        final StringBuilder sb = new StringBuilder();
        for (int j = 0; j < len; ++j) {
            sb.append('%');
            if ((b[j] & 0xFF) < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(b[j] & 0xFF).toUpperCase());
        }
        return sb.toString();
    }
    
    private static String error(final String string, final int index) {
        throw ECMAErrors.uriError("bad.uri", string, Integer.toString(index));
    }
    
    private static boolean isUnescaped(final char ch, final boolean component) {
        return ('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z') || ('0' <= ch && ch <= '9') || "-_.!~*'()".indexOf(ch) >= 0 || (!component && ";/?:@&=+$,#".indexOf(ch) >= 0);
    }
}
