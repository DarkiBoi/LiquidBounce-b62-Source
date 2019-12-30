// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

public abstract class IteratorAction<T>
{
    protected final Object self;
    protected Object thisArg;
    protected final Object callbackfn;
    protected T result;
    protected long index;
    private final ArrayLikeIterator<Object> iter;
    
    public IteratorAction(final Object self, final Object callbackfn, final Object thisArg, final T initialResult) {
        this(self, callbackfn, thisArg, initialResult, ArrayLikeIterator.arrayLikeIterator(self));
    }
    
    public IteratorAction(final Object self, final Object callbackfn, final Object thisArg, final T initialResult, final ArrayLikeIterator<Object> iter) {
        this.self = self;
        this.callbackfn = callbackfn;
        this.result = initialResult;
        this.iter = iter;
        this.thisArg = thisArg;
    }
    
    protected void applyLoopBegin(final ArrayLikeIterator<Object> iterator) {
    }
    
    public final T apply() {
        final boolean strict = Bootstrap.isStrictCallable(this.callbackfn);
        this.thisArg = ((this.thisArg == ScriptRuntime.UNDEFINED && !strict) ? Context.getGlobal() : this.thisArg);
        this.applyLoopBegin(this.iter);
        final boolean reverse = this.iter.isReverse();
        while (this.iter.hasNext()) {
            final Object val = this.iter.next();
            this.index = this.iter.nextIndex() + (reverse ? 1 : -1);
            try {
                if (!this.forEach(val, (double)this.index)) {
                    return this.result;
                }
                continue;
            }
            catch (RuntimeException | Error ex) {
                final Throwable t2;
                final Throwable e = t2;
                throw e;
            }
            catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        return this.result;
    }
    
    protected abstract boolean forEach(final Object p0, final double p1) throws Throwable;
}
