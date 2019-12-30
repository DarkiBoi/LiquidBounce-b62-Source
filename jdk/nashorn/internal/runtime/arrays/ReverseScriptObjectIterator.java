// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;

final class ReverseScriptObjectIterator extends ScriptObjectIterator
{
    ReverseScriptObjectIterator(final ScriptObject obj, final boolean includeUndefined) {
        super(obj, includeUndefined);
        this.index = JSType.toUint32(obj.getLength()) - 1L;
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
