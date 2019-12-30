// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

public interface WarnCallback
{
    public static final WarnCallback DEFAULT = new WarnCallback() {
        @Override
        public void warn(final String message) {
            System.err.println(message);
        }
    };
    
    void warn(final String p0);
}
