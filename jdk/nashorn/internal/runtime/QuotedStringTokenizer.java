// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Stack;
import java.util.StringTokenizer;
import java.util.LinkedList;

public final class QuotedStringTokenizer
{
    private final LinkedList<String> tokens;
    private final char[] quotes;
    
    public QuotedStringTokenizer(final String str) {
        this(str, " ");
    }
    
    public QuotedStringTokenizer(final String str, final String delim) {
        this(str, delim, new char[] { '\"', '\'' });
    }
    
    private QuotedStringTokenizer(final String str, final String delim, final char[] quotes) {
        this.quotes = quotes;
        boolean delimIsWhitespace = true;
        for (int i = 0; i < delim.length(); ++i) {
            if (!Character.isWhitespace(delim.charAt(i))) {
                delimIsWhitespace = false;
                break;
            }
        }
        final StringTokenizer st = new StringTokenizer(str, delim);
        this.tokens = new LinkedList<String>();
        while (st.hasMoreTokens()) {
            String token;
            for (token = st.nextToken(); this.unmatchedQuotesIn(token); token = token + (delimIsWhitespace ? " " : delim) + st.nextToken()) {
                if (!st.hasMoreTokens()) {
                    throw new IndexOutOfBoundsException(token);
                }
            }
            this.tokens.add(this.stripQuotes(token));
        }
    }
    
    public int countTokens() {
        return this.tokens.size();
    }
    
    public boolean hasMoreTokens() {
        return this.countTokens() > 0;
    }
    
    public String nextToken() {
        return this.tokens.removeFirst();
    }
    
    private String stripQuotes(final String value0) {
        String value = value0.trim();
        for (final char q : this.quotes) {
            if (value.length() >= 2 && value.startsWith("" + q) && value.endsWith("" + q)) {
                value = value.substring(1, value.length() - 1);
                value = value.replace("\\" + q, "" + q);
            }
        }
        return value;
    }
    
    private boolean unmatchedQuotesIn(final String str) {
        final Stack<Character> quoteStack = new Stack<Character>();
        for (int i = 0; i < str.length(); ++i) {
            final char c = str.charAt(i);
            for (final char q : this.quotes) {
                if (c == q) {
                    if (quoteStack.isEmpty()) {
                        quoteStack.push(c);
                    }
                    else {
                        final char top = quoteStack.pop();
                        if (top != c) {
                            quoteStack.push(top);
                            quoteStack.push(c);
                        }
                    }
                }
            }
        }
        return !quoteStack.isEmpty();
    }
}
