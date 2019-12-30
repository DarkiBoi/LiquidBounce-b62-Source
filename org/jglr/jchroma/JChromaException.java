// 
// Decompiled by Procyon v0.5.36
// 

package org.jglr.jchroma;

public class JChromaException extends RuntimeException
{
    private int err;
    
    public JChromaException(final String s, final int err) {
        super("Error in " + s + ": " + errorCodeToString(err) + " (" + err + ")");
        this.err = err;
    }
    
    public static String errorCodeToString(final int err) {
        switch (err) {
            case 0: {
                return "No error";
            }
            case 87: {
                return "Invalid parameter";
            }
            case 50: {
                return "Not supported";
            }
            default: {
                return "Unknown(" + err + ")";
            }
        }
    }
    
    public int getErrorCode() {
        return this.err;
    }
}
