// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.parser;

public final class TokenLookup
{
    private static final TokenType[] table;
    private static final int tableBase = 32;
    private static final int tableLimit = 126;
    private static final int tableLength = 95;
    
    private TokenLookup() {
    }
    
    public static TokenType lookupKeyword(final char[] content, final int position, final int length) {
        assert TokenLookup.table != null : "Token lookup table is not initialized";
        final char first = content[position];
        if ('a' <= first && first <= 'z') {
            final int index = first - ' ';
            for (TokenType tokenType = TokenLookup.table[index]; tokenType != null; tokenType = tokenType.getNext()) {
                final int tokenLength = tokenType.getLength();
                if (tokenLength == length) {
                    String name;
                    int i;
                    for (name = tokenType.getName(), i = 0; i < length && content[position + i] == name.charAt(i); ++i) {}
                    if (i == length) {
                        return tokenType;
                    }
                }
                else if (tokenLength < length) {
                    break;
                }
            }
        }
        return TokenType.IDENT;
    }
    
    public static TokenType lookupOperator(final char ch0, final char ch1, final char ch2, final char ch3) {
        assert TokenLookup.table != null : "Token lookup table is not initialized";
        if (' ' < ch0 && ch0 <= '~' && ('a' > ch0 || ch0 > 'z')) {
            final int index = ch0 - ' ';
            for (TokenType tokenType = TokenLookup.table[index]; tokenType != null; tokenType = tokenType.getNext()) {
                final String name = tokenType.getName();
                switch (name.length()) {
                    case 1: {
                        return tokenType;
                    }
                    case 2: {
                        if (name.charAt(1) == ch1) {
                            return tokenType;
                        }
                        break;
                    }
                    case 3: {
                        if (name.charAt(1) == ch1 && name.charAt(2) == ch2) {
                            return tokenType;
                        }
                        break;
                    }
                    case 4: {
                        if (name.charAt(1) == ch1 && name.charAt(2) == ch2 && name.charAt(3) == ch3) {
                            return tokenType;
                        }
                        break;
                    }
                }
            }
        }
        return null;
    }
    
    static {
        table = new TokenType[95];
        for (final TokenType tokenType : TokenType.getValues()) {
            final String name = tokenType.getName();
            if (name != null) {
                if (tokenType.getKind() != TokenKind.SPECIAL) {
                    final char first = name.charAt(0);
                    final int index = first - ' ';
                    assert index < 95 : "Token name does not fit lookup table";
                    final int length = tokenType.getLength();
                    TokenType prev = null;
                    TokenType next;
                    for (next = TokenLookup.table[index]; next != null && next.getLength() > length; next = next.getNext()) {
                        prev = next;
                    }
                    tokenType.setNext(next);
                    if (prev == null) {
                        TokenLookup.table[index] = tokenType;
                    }
                    else {
                        prev.setNext(tokenType);
                    }
                }
            }
        }
    }
}
