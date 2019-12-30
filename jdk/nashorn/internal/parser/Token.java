// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.parser;

import jdk.nashorn.internal.runtime.Source;

public class Token
{
    private Token() {
    }
    
    public static long toDesc(final TokenType type, final int position, final int length) {
        return (long)position << 32 | (long)length << 8 | (long)type.ordinal();
    }
    
    public static int descPosition(final long token) {
        return (int)(token >>> 32);
    }
    
    public static long withDelimiter(final long token) {
        final TokenType tokenType = descType(token);
        switch (tokenType) {
            case STRING:
            case ESCSTRING:
            case EXECSTRING: {
                final int start = descPosition(token) - 1;
                final int len = descLength(token) + 2;
                return toDesc(tokenType, start, len);
            }
            default: {
                return token;
            }
        }
    }
    
    public static int descLength(final long token) {
        return (int)token >>> 8;
    }
    
    public static TokenType descType(final long token) {
        return TokenType.getValues()[(int)token & 0xFF];
    }
    
    public static long recast(final long token, final TokenType newType) {
        return (token & 0xFFFFFFFFFFFFFF00L) | (long)newType.ordinal();
    }
    
    public static String toString(final Source source, final long token, final boolean verbose) {
        final TokenType type = descType(token);
        String result;
        if (source != null && type.getKind() == TokenKind.LITERAL) {
            result = source.getString(token);
        }
        else {
            result = type.getNameOrType();
        }
        if (verbose) {
            final int position = descPosition(token);
            final int length = descLength(token);
            result = result + " (" + position + ", " + length + ")";
        }
        return result;
    }
    
    public static String toString(final Source source, final long token) {
        return toString(source, token, false);
    }
    
    public static String toString(final long token) {
        return toString(null, token, false);
    }
    
    public static int hashCode(final long token) {
        return (int)(token ^ token >>> 32);
    }
}
