// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

public class NameCodec
{
    private static final char ESCAPE_CHAR = '\\';
    private static final char EMPTY_ESCAPE = '=';
    private static final String EMPTY_NAME;
    private static final char EMPTY_CHAR = '\ufeff';
    private static final int MIN_ENCODING = 36;
    private static final int MAX_ENCODING = 93;
    private static final char[] ENCODING;
    private static final int MIN_DECODING = 33;
    private static final int MAX_DECODING = 125;
    private static final char[] DECODING;
    
    private NameCodec() {
    }
    
    public static String encode(final String name) {
        final int l = name.length();
        if (l == 0) {
            return NameCodec.EMPTY_NAME;
        }
        StringBuilder b = null;
        int lastEscape = -1;
        for (int i = 0; i < l; ++i) {
            final int encodeIndex = name.charAt(i) - '$';
            if (encodeIndex >= 0 && encodeIndex < NameCodec.ENCODING.length) {
                final char e = NameCodec.ENCODING[encodeIndex];
                if (e != '\0') {
                    if (b == null) {
                        b = new StringBuilder(name.length() + 3);
                        if (name.charAt(0) != '\\' && i > 0) {
                            b.append(NameCodec.EMPTY_NAME);
                        }
                        b.append(name, 0, i);
                    }
                    else {
                        b.append(name, lastEscape + 1, i);
                    }
                    b.append('\\').append(e);
                    lastEscape = i;
                }
            }
        }
        if (b == null) {
            return name.toString();
        }
        assert lastEscape != -1;
        b.append(name, lastEscape + 1, l);
        return b.toString();
    }
    
    public static String decode(final String name) {
        if (name.charAt(0) != '\\') {
            return name;
        }
        final int l = name.length();
        if (l == 2 && name.charAt(1) == '\ufeff') {
            return "";
        }
        final StringBuilder b = new StringBuilder(name.length());
        int lastEscape = -2;
        int lastBackslash = -1;
        while (true) {
            final int nextBackslash = name.indexOf(92, lastBackslash + 1);
            if (nextBackslash == -1 || nextBackslash == l - 1) {
                break;
            }
            final int decodeIndex = name.charAt(nextBackslash + 1) - '!';
            if (decodeIndex >= 0 && decodeIndex < NameCodec.DECODING.length) {
                final char d = NameCodec.DECODING[decodeIndex];
                if (d == '\ufeff') {
                    if (nextBackslash == 0) {
                        lastEscape = 0;
                    }
                }
                else if (d != '\0') {
                    b.append(name, lastEscape + 2, nextBackslash).append(d);
                    lastEscape = nextBackslash;
                }
            }
            lastBackslash = nextBackslash;
        }
        b.append(name, lastEscape + 2, l);
        return b.toString();
    }
    
    private static void addEncoding(final char from, final char to) {
        NameCodec.ENCODING[from - '$'] = to;
        NameCodec.DECODING[to - '!'] = from;
    }
    
    static {
        EMPTY_NAME = new String(new char[] { '\\', '=' });
        ENCODING = new char[58];
        DECODING = new char[93];
        addEncoding('/', '|');
        addEncoding('.', ',');
        addEncoding(';', '?');
        addEncoding('$', '%');
        addEncoding('<', '^');
        addEncoding('>', '_');
        addEncoding('[', '{');
        addEncoding(']', '}');
        addEncoding(':', '!');
        addEncoding('\\', '-');
        NameCodec.DECODING[28] = '\ufeff';
    }
}
