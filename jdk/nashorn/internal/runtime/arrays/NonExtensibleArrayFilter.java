// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.objects.Global;

final class NonExtensibleArrayFilter extends ArrayFilter
{
    NonExtensibleArrayFilter(final ArrayData underlying) {
        super(underlying);
    }
    
    @Override
    public ArrayData copy() {
        return new NonExtensibleArrayFilter(this.underlying.copy());
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        return new NonExtensibleArrayFilter(this.underlying.slice(from, to));
    }
    
    private ArrayData extensionCheck(final boolean strict, final int index) {
        if (!strict) {
            return this;
        }
        throw ECMAErrors.typeError(Global.instance(), "object.non.extensible", String.valueOf(index), ScriptRuntime.safeToString(this));
    }
    
    @Override
    public ArrayData set(final int index, final Object value, final boolean strict) {
        if (this.has(index)) {
            return this.underlying.set(index, value, strict);
        }
        return this.extensionCheck(strict, index);
    }
    
    @Override
    public ArrayData set(final int index, final int value, final boolean strict) {
        if (this.has(index)) {
            return this.underlying.set(index, value, strict);
        }
        return this.extensionCheck(strict, index);
    }
    
    @Override
    public ArrayData set(final int index, final double value, final boolean strict) {
        if (this.has(index)) {
            return this.underlying.set(index, value, strict);
        }
        return this.extensionCheck(strict, index);
    }
}
