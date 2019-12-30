// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;

public final class ApplyCaseFoldArg
{
    final ScanEnvironment env;
    final CClassNode cc;
    ConsAltNode altRoot;
    ConsAltNode tail;
    
    public ApplyCaseFoldArg(final ScanEnvironment env, final CClassNode cc) {
        this.env = env;
        this.cc = cc;
    }
}
