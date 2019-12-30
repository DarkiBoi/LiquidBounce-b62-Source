// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.ScriptObject;

class ScriptArrayIterator extends ArrayLikeIterator<Object>
{
    protected final ScriptObject array;
    protected final long length;
    
    protected ScriptArrayIterator(final ScriptObject array, final boolean includeUndefined) {
        super(includeUndefined);
        this.array = array;
        this.length = array.getArray().length();
    }
    
    protected boolean indexInArray() {
        return this.index < this.length;
    }
    
    @Override
    public Object next() {
        return this.array.get((double)this.bumpIndex());
    }
    
    @Override
    public long getLength() {
        return this.length;
    }
    
    @Override
    public boolean hasNext() {
        if (!this.includeUndefined) {
            while (this.indexInArray()) {
                if (this.array.has((double)this.index)) {
                    break;
                }
                this.bumpIndex();
            }
        }
        return this.indexInArray();
    }
    
    @Override
    public void remove() {
        this.array.delete((double)this.index, false);
    }
}
