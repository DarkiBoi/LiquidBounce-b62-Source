// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.ScriptObject;

final class ReverseScriptArrayIterator extends ScriptArrayIterator
{
    public ReverseScriptArrayIterator(final ScriptObject array, final boolean includeUndefined) {
        super(array, includeUndefined);
        this.index = array.getArray().length() - 1L;
    }
    
    @Override
    public boolean isReverse() {
        return true;
    }
    
    @Override
    protected boolean indexInArray() {
        return this.index >= 0L;
    }
    
    @Override
    protected long bumpIndex() {
        return this.index--;
    }
}
