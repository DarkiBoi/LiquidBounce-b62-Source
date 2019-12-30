// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;

final class ApplyCaseFold
{
    static final ApplyCaseFold INSTANCE;
    
    public static void apply(final int from, final int to, final Object o) {
        final ApplyCaseFoldArg arg = (ApplyCaseFoldArg)o;
        final ScanEnvironment env = arg.env;
        final CClassNode cc = arg.cc;
        final BitSet bs = cc.bs;
        final boolean inCC = cc.isCodeInCC(from);
        if ((inCC && !cc.isNot()) || (!inCC && cc.isNot())) {
            if (to >= 256) {
                cc.addCodeRange(env, to, to);
            }
            else {
                bs.set(to);
            }
        }
    }
    
    static {
        INSTANCE = new ApplyCaseFold();
    }
}
