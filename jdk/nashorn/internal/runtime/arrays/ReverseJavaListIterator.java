// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import java.util.List;

final class ReverseJavaListIterator extends JavaListIterator
{
    public ReverseJavaListIterator(final List<?> list, final boolean includeUndefined) {
        super(list, includeUndefined);
        this.index = list.size() - 1;
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
