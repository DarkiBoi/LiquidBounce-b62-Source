// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;

final class ReverseJavaArrayIterator extends JavaArrayIterator
{
    public ReverseJavaArrayIterator(final Object array, final boolean includeUndefined) {
        super(array, includeUndefined);
        this.index = Array.getLength(array) - 1;
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
