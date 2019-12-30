// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.api.scripting.NashornException;

public final class ParserException extends NashornException
{
    private final Source source;
    private final long token;
    private final JSErrorType errorType;
    
    public ParserException(final String msg) {
        this(JSErrorType.SYNTAX_ERROR, msg, null, -1, -1, -1L);
    }
    
    public ParserException(final JSErrorType errorType, final String msg, final Source source, final int line, final int column, final long token) {
        super(msg, (source != null) ? source.getName() : null, line, column);
        this.source = source;
        this.token = token;
        this.errorType = errorType;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    public long getToken() {
        return this.token;
    }
    
    public int getPosition() {
        return Token.descPosition(this.token);
    }
    
    public JSErrorType getErrorType() {
        return this.errorType;
    }
    
    public void throwAsEcmaException() {
        throw ECMAErrors.asEcmaException(this);
    }
    
    public void throwAsEcmaException(final Global global) {
        throw ECMAErrors.asEcmaException(global, this);
    }
}
