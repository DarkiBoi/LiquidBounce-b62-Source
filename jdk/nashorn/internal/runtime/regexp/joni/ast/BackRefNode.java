// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni.ast;

import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;

public final class BackRefNode extends StateNode
{
    public final int backRef;
    
    public BackRefNode(final int backRef, final ScanEnvironment env) {
        this.backRef = backRef;
        if (backRef <= env.numMem && env.memNodes[backRef] == null) {
            this.setRecursion();
        }
    }
    
    @Override
    public int getType() {
        return 4;
    }
    
    @Override
    public String getName() {
        return "Back Ref";
    }
    
    @Override
    public String toString(final int level) {
        final StringBuilder value = new StringBuilder(super.toString(level));
        value.append("\n  back: ").append(this.backRef);
        return value.toString();
    }
}
