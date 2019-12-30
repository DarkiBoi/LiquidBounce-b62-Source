// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodHandle;
import java.nio.Buffer;

public abstract class TypedArrayData<T extends Buffer> extends ContinuousArrayData
{
    protected final T nb;
    
    protected TypedArrayData(final T nb, final int elementLength) {
        super(elementLength);
        this.nb = nb;
    }
    
    public final int getElementLength() {
        return (int)this.length();
    }
    
    public boolean isUnsigned() {
        return false;
    }
    
    public boolean isClamped() {
        return false;
    }
    
    @Override
    public boolean canDelete(final int index, final boolean strict) {
        return false;
    }
    
    @Override
    public boolean canDelete(final long longIndex, final boolean strict) {
        return false;
    }
    
    @Override
    public TypedArrayData<T> copy() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] asObjectArray() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ArrayData shiftLeft(final int by) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ArrayData shiftRight(final int by) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ArrayData ensure(final long safeIndex) {
        return this;
    }
    
    @Override
    public ArrayData shrink(final long newLength) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean has(final int index) {
        return 0 <= index && index < this.length();
    }
    
    @Override
    public ArrayData delete(final int index) {
        return this;
    }
    
    @Override
    public ArrayData delete(final long fromIndex, final long toIndex) {
        return this;
    }
    
    @Override
    public TypedArrayData<T> convert(final Class<?> type) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object pop() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ArrayData slice(final long from, final long to) {
        throw new UnsupportedOperationException();
    }
    
    protected abstract MethodHandle getGetElem();
    
    protected abstract MethodHandle getSetElem();
    
    @Override
    public MethodHandle getElementGetter(final Class<?> returnType, final int programPoint) {
        final MethodHandle getter = this.getContinuousElementGetter(this.getClass(), this.getGetElem(), returnType, programPoint);
        if (getter != null) {
            return Lookup.filterReturnType(getter, returnType);
        }
        return getter;
    }
    
    @Override
    public MethodHandle getElementSetter(final Class<?> elementType) {
        return this.getContinuousElementSetter(this.getClass(), Lookup.filterArgumentType(this.getSetElem(), 2, elementType), elementType);
    }
    
    @Override
    protected MethodHandle getContinuousElementSetter(final Class<? extends ContinuousArrayData> clazz, final MethodHandle setHas, final Class<?> elementType) {
        final MethodHandle mh = Lookup.filterArgumentType(setHas, 2, elementType);
        return Lookup.MH.asType(mh, mh.type().changeParameterType(0, (Class<?>)clazz));
    }
    
    @Override
    public GuardedInvocation findFastGetIndexMethod(final Class<? extends ArrayData> clazz, final CallSiteDescriptor desc, final LinkRequest request) {
        final GuardedInvocation inv = super.findFastGetIndexMethod(clazz, desc, request);
        if (inv != null) {
            return inv;
        }
        return null;
    }
    
    @Override
    public GuardedInvocation findFastSetIndexMethod(final Class<? extends ArrayData> clazz, final CallSiteDescriptor desc, final LinkRequest request) {
        final GuardedInvocation inv = super.findFastSetIndexMethod(clazz, desc, request);
        if (inv != null) {
            return inv;
        }
        return null;
    }
}
