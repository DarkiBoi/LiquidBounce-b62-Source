// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.api.scripting.JSObject;

final class ReverseJSObjectIterator extends JSObjectIterator
{
    ReverseJSObjectIterator(final JSObject obj, final boolean includeUndefined) {
        super(obj, includeUndefined);
        this.index = JSType.toUint32(obj.hasMember("length") ? obj.getMember("length") : Integer.valueOf(0)) - 1L;
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
