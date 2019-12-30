// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp;

import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.BitVector;

public abstract class RegExp
{
    private final String source;
    private boolean global;
    private boolean ignoreCase;
    private boolean multiline;
    protected BitVector groupsInNegativeLookahead;
    
    protected RegExp(final String source, final String flags) {
        this.source = ((source.length() == 0) ? "(?:)" : source);
        for (int i = 0; i < flags.length(); ++i) {
            final char ch = flags.charAt(i);
            switch (ch) {
                case 'g': {
                    if (this.global) {
                        throwParserException("repeated.flag", "g");
                    }
                    this.global = true;
                    break;
                }
                case 'i': {
                    if (this.ignoreCase) {
                        throwParserException("repeated.flag", "i");
                    }
                    this.ignoreCase = true;
                    break;
                }
                case 'm': {
                    if (this.multiline) {
                        throwParserException("repeated.flag", "m");
                    }
                    this.multiline = true;
                    break;
                }
                default: {
                    throwParserException("unsupported.flag", Character.toString(ch));
                    break;
                }
            }
        }
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void setGlobal(final boolean global) {
        this.global = global;
    }
    
    public boolean isGlobal() {
        return this.global;
    }
    
    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }
    
    public boolean isMultiline() {
        return this.multiline;
    }
    
    public BitVector getGroupsInNegativeLookahead() {
        return this.groupsInNegativeLookahead;
    }
    
    public abstract RegExpMatcher match(final String p0);
    
    protected static void throwParserException(final String key, final String str) throws ParserException {
        throw new ParserException(ECMAErrors.getMessage("parser.error.regex." + key, str));
    }
}
