// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ECMAErrors;

class SealedArrayFilter extends ArrayFilter
{
    SealedArrayFilter(final ArrayData underlying) {
        super(underlying);
    }
    
    @Override
    public ArrayData copy() {
        return new SealedArrayFilter(this.underlying.copy());
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        return this.getUnderlying().slice(from, to);
    }
    
    @Override
    public boolean canDelete(final int index, final boolean strict) {
        return this.canDelete(ArrayIndex.toLongIndex(index), strict);
    }
    
    @Override
    public boolean canDelete(final long longIndex, final boolean strict) {
        if (strict) {
            throw ECMAErrors.typeError("cant.delete.property", Long.toString(longIndex), "sealed array");
        }
        return false;
    }
    
    @Override
    public PropertyDescriptor getDescriptor(final Global global, final int index) {
        return global.newDataDescriptor(this.getObject(index), false, true, true);
    }
}
