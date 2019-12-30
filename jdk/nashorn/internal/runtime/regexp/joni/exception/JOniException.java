// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni.exception;

public class JOniException extends RuntimeException
{
    private static final long serialVersionUID = -6027192180014164667L;
    
    public JOniException(final String message) {
        super(message, null, false, false);
    }
}
