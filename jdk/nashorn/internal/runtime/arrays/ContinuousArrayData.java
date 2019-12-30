// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.codegen.types.Type;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.logging.Logger;

@Logger(name = "arrays")
public abstract class ContinuousArrayData extends ArrayData
{
    protected static final MethodHandle FAST_ACCESS_GUARD;
    
    protected ContinuousArrayData(final long length) {
        super(length);
    }
    
    public final boolean hasRoomFor(final int index) {
        return this.has(index) || (index == this.length() && this.ensure(index) == this);
    }
    
    public boolean isEmpty() {
        return this.length() == 0L;
    }
    
    public abstract MethodHandle getElementGetter(final Class<?> p0, final int p1);
    
    public abstract MethodHandle getElementSetter(final Class<?> p0);
    
    protected final int throwHas(final int index) {
        if (!this.has(index)) {
            throw new ClassCastException();
        }
        return index;
    }
    
    @Override
    public abstract ContinuousArrayData copy();
    
    public abstract Class<?> getElementType();
    
    @Override
    public Type getOptimisticType() {
        return Type.typeFor(this.getElementType());
    }
    
    public abstract Class<?> getBoxedElementType();
    
    public ContinuousArrayData widest(final ContinuousArrayData otherData) {
        final Class<?> elementType = this.getElementType();
        return (Type.widest(elementType, otherData.getElementType()) == elementType) ? this : otherData;
    }
    
    protected final MethodHandle getContinuousElementGetter(final MethodHandle get, final Class<?> returnType, final int programPoint) {
        return this.getContinuousElementGetter(this.getClass(), get, returnType, programPoint);
    }
    
    protected final MethodHandle getContinuousElementSetter(final MethodHandle set, final Class<?> returnType) {
        return this.getContinuousElementSetter(this.getClass(), set, returnType);
    }
    
    protected MethodHandle getContinuousElementGetter(final Class<? extends ContinuousArrayData> clazz, final MethodHandle getHas, final Class<?> returnType, final int programPoint) {
        final boolean isOptimistic = UnwarrantedOptimismException.isValid(programPoint);
        final int fti = JSType.getAccessorTypeIndex(getHas.type().returnType());
        final int ti = JSType.getAccessorTypeIndex(returnType);
        MethodHandle mh = getHas;
        if (isOptimistic && ti < fti) {
            mh = Lookup.MH.insertArguments(ArrayData.THROW_UNWARRANTED.methodHandle(), 1, programPoint);
        }
        mh = Lookup.MH.asType(mh, mh.type().changeReturnType(returnType).changeParameterType(0, (Class<?>)clazz));
        if (!isOptimistic) {
            return Lookup.filterReturnType(mh, returnType);
        }
        return mh;
    }
    
    protected MethodHandle getContinuousElementSetter(final Class<? extends ContinuousArrayData> clazz, final MethodHandle setHas, final Class<?> elementType) {
        return Lookup.MH.asType(setHas, setHas.type().changeParameterType(2, elementType).changeParameterType(0, (Class<?>)clazz));
    }
    
    private static final boolean guard(final Class<? extends ContinuousArrayData> clazz, final ScriptObject sobj) {
        return sobj != null && sobj.getArray().getClass() == clazz;
    }
    
    @Override
    public GuardedInvocation findFastGetIndexMethod(final Class<? extends ArrayData> clazz, final CallSiteDescriptor desc, final LinkRequest request) {
        final MethodType callType = desc.getMethodType();
        final Class<?> indexType = callType.parameterType(1);
        final Class<?> returnType = callType.returnType();
        if (ContinuousArrayData.class.isAssignableFrom(clazz) && indexType == Integer.TYPE) {
            final Object[] args = request.getArguments();
            final int index = (int)args[args.length - 1];
            if (this.has(index)) {
                final MethodHandle getArray = ScriptObject.GET_ARRAY.methodHandle();
                final int programPoint = NashornCallSiteDescriptor.isOptimistic(desc) ? NashornCallSiteDescriptor.getProgramPoint(desc) : -1;
                MethodHandle getElement = this.getElementGetter(returnType, programPoint);
                if (getElement != null) {
                    getElement = Lookup.MH.filterArguments(getElement, 0, Lookup.MH.asType(getArray, getArray.type().changeReturnType((Class<?>)clazz)));
                    final MethodHandle guard = Lookup.MH.insertArguments(ContinuousArrayData.FAST_ACCESS_GUARD, 0, clazz);
                    return new GuardedInvocation(getElement, guard, (SwitchPoint)null, ClassCastException.class);
                }
            }
        }
        return null;
    }
    
    @Override
    public GuardedInvocation findFastSetIndexMethod(final Class<? extends ArrayData> clazz, final CallSiteDescriptor desc, final LinkRequest request) {
        final MethodType callType = desc.getMethodType();
        final Class<?> indexType = callType.parameterType(1);
        final Class<?> elementType = callType.parameterType(2);
        if (ContinuousArrayData.class.isAssignableFrom(clazz) && indexType == Integer.TYPE) {
            final Object[] args = request.getArguments();
            final int index = (int)args[args.length - 2];
            if (this.hasRoomFor(index)) {
                MethodHandle setElement = this.getElementSetter(elementType);
                if (setElement != null) {
                    MethodHandle getArray = ScriptObject.GET_ARRAY.methodHandle();
                    getArray = Lookup.MH.asType(getArray, getArray.type().changeReturnType((Class<?>)this.getClass()));
                    setElement = Lookup.MH.filterArguments(setElement, 0, getArray);
                    final MethodHandle guard = Lookup.MH.insertArguments(ContinuousArrayData.FAST_ACCESS_GUARD, 0, clazz);
                    return new GuardedInvocation(setElement, guard, (SwitchPoint)null, ClassCastException.class);
                }
            }
        }
        return null;
    }
    
    public double fastPush(final int arg) {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }
    
    public double fastPush(final long arg) {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }
    
    public double fastPush(final double arg) {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }
    
    public double fastPush(final Object arg) {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }
    
    public int fastPopInt() {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }
    
    public double fastPopDouble() {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }
    
    public Object fastPopObject() {
        throw new ClassCastException(String.valueOf(this.getClass()));
    }
    
    public ContinuousArrayData fastConcat(final ContinuousArrayData otherData) {
        throw new ClassCastException(String.valueOf(this.getClass()) + " != " + String.valueOf(otherData.getClass()));
    }
    
    static {
        FAST_ACCESS_GUARD = Lookup.MH.dropArguments(CompilerConstants.staticCall(MethodHandles.lookup(), ContinuousArrayData.class, "guard", Boolean.TYPE, Class.class, ScriptObject.class).methodHandle(), 2, Integer.TYPE);
    }
}
