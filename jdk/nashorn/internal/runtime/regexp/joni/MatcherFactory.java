// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

public abstract class MatcherFactory
{
    static final MatcherFactory DEFAULT;
    
    public abstract Matcher create(final Regex p0, final char[] p1, final int p2, final int p3);
    
    static {
        DEFAULT = new MatcherFactory() {
            @Override
            public Matcher create(final Regex regex, final char[] chars, final int p, final int end) {
                return new ByteCodeMachine(regex, chars, p, end);
            }
        };
    }
}
