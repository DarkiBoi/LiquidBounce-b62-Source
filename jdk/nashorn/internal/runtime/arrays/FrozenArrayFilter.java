// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.objects.Global;

final class FrozenArrayFilter extends SealedArrayFilter
{
    FrozenArrayFilter(final ArrayData underlying) {
        super(underlying);
    }
    
    @Override
    public ArrayData copy() {
        return this;
    }
    
    @Override
    public PropertyDescriptor getDescriptor(final Global global, final int index) {
        return global.newDataDescriptor(this.getObject(index), false, true, false);
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        if (strict) {
            throw ECMAErrors.typeError("cant.set.property", Integer.toString(index), "frozen array");
        }
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        if (strict) {
            throw ECMAErrors.typeError("cant.set.property", Integer.toString(index), "frozen array");
        }
        return this;
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        if (strict) {
            throw ECMAErrors.typeError("cant.set.property", Integer.toString(index), "frozen array");
        }
        return this;
    }
    
    @Override
    public ArrayData push(final boolean strict, final Object... items) {
        return this;
    }
    
    @Override
    public Object pop() {
        final int len = (int)this.underlying.length();
        return (len == 0) ? ScriptRuntime.UNDEFINED : this.underlying.getObject(len - 1);
    }
}
