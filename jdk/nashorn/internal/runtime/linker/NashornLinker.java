// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodType;
import jdk.nashorn.internal.runtime.ListAdapter;
import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.api.scripting.ScriptUtils;
import java.lang.reflect.Modifier;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.JSObject;
import javax.script.Bindings;
import java.util.Map;
import jdk.nashorn.internal.runtime.JSType;
import java.util.Collection;
import java.util.Queue;
import java.util.Deque;
import java.util.List;
import jdk.nashorn.internal.objects.NativeArray;
import java.security.AccessController;
import jdk.internal.dynalink.support.LinkerServicesImpl;
import java.security.PrivilegedAction;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.internal.dynalink.linker.GuardedTypeConversion;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.ConversionComparator;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

final class NashornLinker implements TypeBasedGuardingDynamicLinker, GuardingTypeConverterFactory, ConversionComparator
{
    private static final ClassValue<MethodHandle> ARRAY_CONVERTERS;
    private static final MethodHandle IS_SCRIPT_OBJECT;
    private static final MethodHandle IS_SCRIPT_FUNCTION;
    private static final MethodHandle IS_NATIVE_ARRAY;
    private static final MethodHandle IS_NASHORN_OR_UNDEFINED_TYPE;
    private static final MethodHandle CREATE_MIRROR;
    private static final MethodHandle TO_COLLECTION;
    private static final MethodHandle TO_DEQUE;
    private static final MethodHandle TO_LIST;
    private static final MethodHandle TO_QUEUE;
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return canLinkTypeStatic(type);
    }
    
    static boolean canLinkTypeStatic(final Class<?> type) {
        return ScriptObject.class.isAssignableFrom(type) || Undefined.class == type;
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest request, final LinkerServices linkerServices) throws Exception {
        final LinkRequest requestWithoutContext = request.withoutRuntimeContext();
        final Object self = requestWithoutContext.getReceiver();
        final CallSiteDescriptor desc = requestWithoutContext.getCallSiteDescriptor();
        if (desc.getNameTokenCount() < 2 || !"dyn".equals(desc.getNameToken(0))) {
            return null;
        }
        return Bootstrap.asTypeSafeReturn(getGuardedInvocation(self, request, desc), linkerServices, desc);
    }
    
    private static GuardedInvocation getGuardedInvocation(final Object self, final LinkRequest request, final CallSiteDescriptor desc) {
        GuardedInvocation inv;
        if (self instanceof ScriptObject) {
            inv = ((ScriptObject)self).lookup(desc, request);
        }
        else {
            if (!(self instanceof Undefined)) {
                throw new AssertionError((Object)self.getClass().getName());
            }
            inv = Undefined.lookup(desc);
        }
        return inv;
    }
    
    @Override
    public GuardedTypeConversion convertToType(final Class<?> sourceType, final Class<?> targetType) throws Exception {
        GuardedInvocation gi = convertToTypeNoCast(sourceType, targetType);
        if (gi != null) {
            return new GuardedTypeConversion(gi.asType(Lookup.MH.type(targetType, sourceType)), true);
        }
        gi = getSamTypeConverter(sourceType, targetType);
        if (gi != null) {
            return new GuardedTypeConversion(gi.asType(Lookup.MH.type(targetType, sourceType)), false);
        }
        return null;
    }
    
    private static GuardedInvocation convertToTypeNoCast(final Class<?> sourceType, final Class<?> targetType) throws Exception {
        final MethodHandle mh = JavaArgumentConverters.getConverter(targetType);
        if (mh != null) {
            return new GuardedInvocation(mh, canLinkTypeStatic(sourceType) ? null : NashornLinker.IS_NASHORN_OR_UNDEFINED_TYPE);
        }
        final GuardedInvocation arrayConverter = getArrayConverter(sourceType, targetType);
        if (arrayConverter != null) {
            return arrayConverter;
        }
        return getMirrorConverter(sourceType, targetType);
    }
    
    private static GuardedInvocation getSamTypeConverter(final Class<?> sourceType, final Class<?> targetType) throws Exception {
        final boolean isSourceTypeGeneric = sourceType.isAssignableFrom(ScriptFunction.class);
        if ((!isSourceTypeGeneric && !ScriptFunction.class.isAssignableFrom(sourceType)) || !isAutoConvertibleFromFunction(targetType)) {
            return null;
        }
        final MethodHandle ctor = JavaAdapterFactory.getConstructor(ScriptFunction.class, targetType, getCurrentLookup());
        assert ctor != null;
        return new GuardedInvocation(ctor, isSourceTypeGeneric ? NashornLinker.IS_SCRIPT_FUNCTION : null);
    }
    
    private static MethodHandles.Lookup getCurrentLookup() {
        final LinkRequest currentRequest = AccessController.doPrivileged((PrivilegedAction<LinkRequest>)new PrivilegedAction<LinkRequest>() {
            @Override
            public LinkRequest run() {
                return LinkerServicesImpl.getCurrentLinkRequest();
            }
        });
        return (currentRequest == null) ? MethodHandles.publicLookup() : currentRequest.getCallSiteDescriptor().getLookup();
    }
    
    private static GuardedInvocation getArrayConverter(final Class<?> sourceType, final Class<?> targetType) {
        final boolean isSourceTypeNativeArray = sourceType == NativeArray.class;
        final boolean isSourceTypeGeneric = !isSourceTypeNativeArray && sourceType.isAssignableFrom(NativeArray.class);
        if (isSourceTypeNativeArray || isSourceTypeGeneric) {
            final MethodHandle guard = isSourceTypeGeneric ? NashornLinker.IS_NATIVE_ARRAY : null;
            if (targetType.isArray()) {
                return new GuardedInvocation(NashornLinker.ARRAY_CONVERTERS.get(targetType), guard);
            }
            if (targetType == List.class) {
                return new GuardedInvocation(NashornLinker.TO_LIST, guard);
            }
            if (targetType == Deque.class) {
                return new GuardedInvocation(NashornLinker.TO_DEQUE, guard);
            }
            if (targetType == Queue.class) {
                return new GuardedInvocation(NashornLinker.TO_QUEUE, guard);
            }
            if (targetType == Collection.class) {
                return new GuardedInvocation(NashornLinker.TO_COLLECTION, guard);
            }
        }
        return null;
    }
    
    private static MethodHandle createArrayConverter(final Class<?> type) {
        assert type.isArray();
        final MethodHandle converter = Lookup.MH.insertArguments(JSType.TO_JAVA_ARRAY.methodHandle(), 1, type.getComponentType());
        return Lookup.MH.asType(converter, converter.type().changeReturnType(type));
    }
    
    private static GuardedInvocation getMirrorConverter(final Class<?> sourceType, final Class<?> targetType) {
        if (targetType == Map.class || targetType == Bindings.class || targetType == JSObject.class || targetType == ScriptObjectMirror.class) {
            if (ScriptObject.class.isAssignableFrom(sourceType)) {
                return new GuardedInvocation(NashornLinker.CREATE_MIRROR);
            }
            if (sourceType.isAssignableFrom(ScriptObject.class) || sourceType.isInterface()) {
                return new GuardedInvocation(NashornLinker.CREATE_MIRROR, NashornLinker.IS_SCRIPT_OBJECT);
            }
        }
        return null;
    }
    
    private static boolean isAutoConvertibleFromFunction(final Class<?> clazz) {
        return isAbstractClass(clazz) && !ScriptObject.class.isAssignableFrom(clazz) && JavaAdapterFactory.isAutoConvertibleFromFunction(clazz);
    }
    
    static boolean isAbstractClass(final Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers()) && !clazz.isArray();
    }
    
    @Override
    public Comparison compareConversion(final Class<?> sourceType, final Class<?> targetType1, final Class<?> targetType2) {
        if (sourceType == NativeArray.class) {
            if (isArrayPreferredTarget(targetType1)) {
                if (!isArrayPreferredTarget(targetType2)) {
                    return Comparison.TYPE_1_BETTER;
                }
            }
            else if (isArrayPreferredTarget(targetType2)) {
                return Comparison.TYPE_2_BETTER;
            }
            if (targetType1.isArray()) {
                if (!targetType2.isArray()) {
                    return Comparison.TYPE_1_BETTER;
                }
            }
            else if (targetType2.isArray()) {
                return Comparison.TYPE_2_BETTER;
            }
        }
        if (ScriptObject.class.isAssignableFrom(sourceType)) {
            if (targetType1.isInterface()) {
                if (!targetType2.isInterface()) {
                    return Comparison.TYPE_1_BETTER;
                }
            }
            else if (targetType2.isInterface()) {
                return Comparison.TYPE_2_BETTER;
            }
        }
        return Comparison.INDETERMINATE;
    }
    
    private static boolean isArrayPreferredTarget(final Class<?> clazz) {
        return clazz == List.class || clazz == Collection.class || clazz == Queue.class || clazz == Deque.class;
    }
    
    private static MethodHandle asReturning(final MethodHandle mh, final Class<?> nrtype) {
        return mh.asType(mh.type().changeReturnType(nrtype));
    }
    
    private static boolean isNashornTypeOrUndefined(final Object obj) {
        return obj instanceof ScriptObject || obj instanceof Undefined;
    }
    
    private static Object createMirror(final Object obj) {
        return (obj instanceof ScriptObject) ? ScriptUtils.wrap(obj) : obj;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NashornLinker.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        ARRAY_CONVERTERS = new ClassValue<MethodHandle>() {
            @Override
            protected MethodHandle computeValue(final Class<?> type) {
                return createArrayConverter(type);
            }
        };
        IS_SCRIPT_OBJECT = Guards.isInstance(ScriptObject.class, Lookup.MH.type(Boolean.TYPE, Object.class));
        IS_SCRIPT_FUNCTION = Guards.isInstance(ScriptFunction.class, Lookup.MH.type(Boolean.TYPE, Object.class));
        IS_NATIVE_ARRAY = Guards.isOfClass(NativeArray.class, Lookup.MH.type(Boolean.TYPE, Object.class));
        IS_NASHORN_OR_UNDEFINED_TYPE = findOwnMH("isNashornTypeOrUndefined", Boolean.TYPE, Object.class);
        CREATE_MIRROR = findOwnMH("createMirror", Object.class, Object.class);
        final MethodHandle listAdapterCreate = new jdk.internal.dynalink.support.Lookup(MethodHandles.lookup()).findStatic(ListAdapter.class, "create", MethodType.methodType(ListAdapter.class, Object.class));
        TO_COLLECTION = asReturning(listAdapterCreate, Collection.class);
        TO_DEQUE = asReturning(listAdapterCreate, Deque.class);
        TO_LIST = asReturning(listAdapterCreate, List.class);
        TO_QUEUE = asReturning(listAdapterCreate, Queue.class);
    }
}
