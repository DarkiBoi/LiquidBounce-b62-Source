// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni.exception;

public class ValueException extends SyntaxException
{
    private static final long serialVersionUID = -196013852479929134L;
    
    public ValueException(final String message) {
        super(message);
    }
    
    public ValueException(final String message, final String str) {
        super(message.replaceAll("%n", str));
    }
}
