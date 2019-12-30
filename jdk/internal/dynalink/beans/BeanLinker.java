// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.util.Collection;
import java.lang.reflect.Array;
import jdk.internal.dynalink.support.Lookup;
import jdk.internal.dynalink.support.TypeUtilities;
import jdk.internal.dynalink.linker.GuardedInvocation;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.util.List;
import jdk.internal.dynalink.support.Guards;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

class BeanLinker extends AbstractJavaLinker implements TypeBasedGuardingDynamicLinker
{
    private static MethodHandle GET_LIST_ELEMENT;
    private static MethodHandle GET_MAP_ELEMENT;
    private static MethodHandle LIST_GUARD;
    private static MethodHandle MAP_GUARD;
    private static MethodHandle RANGE_CHECK_ARRAY;
    private static MethodHandle RANGE_CHECK_LIST;
    private static MethodHandle CONTAINS_MAP;
    private static MethodHandle SET_LIST_ELEMENT;
    private static MethodHandle PUT_MAP_ELEMENT;
    private static MethodHandle GET_ARRAY_LENGTH;
    private static MethodHandle GET_COLLECTION_LENGTH;
    private static MethodHandle GET_MAP_LENGTH;
    private static MethodHandle COLLECTION_GUARD;
    
    BeanLinker(final Class<?> clazz) {
        super(clazz, Guards.getClassGuard(clazz), Guards.getInstanceOfGuard(clazz));
        if (clazz.isArray()) {
            this.setPropertyGetter("length", BeanLinker.GET_ARRAY_LENGTH, GuardedInvocationComponent.ValidationType.IS_ARRAY);
        }
        else if (List.class.isAssignableFrom(clazz)) {
            this.setPropertyGetter("length", BeanLinker.GET_COLLECTION_LENGTH, GuardedInvocationComponent.ValidationType.INSTANCE_OF);
        }
    }
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return type == this.clazz;
    }
    
    @Override
    FacetIntrospector createFacetIntrospector() {
        return new BeanIntrospector(this.clazz);
    }
    
    @Override
    protected GuardedInvocationComponent getGuardedInvocationComponent(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices, final List<String> operations) throws Exception {
        final GuardedInvocationComponent superGic = super.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, operations);
        if (superGic != null) {
            return superGic;
        }
        if (operations.isEmpty()) {
            return null;
        }
        final String op = operations.get(0);
        if ("getElem".equals(op)) {
            return this.getElementGetter(callSiteDescriptor, linkerServices, AbstractJavaLinker.pop(operations));
        }
        if ("setElem".equals(op)) {
            return this.getElementSetter(callSiteDescriptor, linkerServices, AbstractJavaLinker.pop(operations));
        }
        if ("getLength".equals(op)) {
            return this.getLengthGetter(callSiteDescriptor);
        }
        return null;
    }
    
    private GuardedInvocationComponent getElementGetter(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices, final List<String> operations) throws Exception {
        final MethodType callSiteType = callSiteDescriptor.getMethodType();
        final Class<?> declaredType = callSiteType.parameterType(0);
        final GuardedInvocationComponent nextComponent = this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, operations);
        GuardedInvocationComponent gic;
        CollectionType collectionType;
        if (declaredType.isArray()) {
            gic = createInternalFilteredGuardedInvocationComponent(MethodHandles.arrayElementGetter(declaredType), linkerServices);
            collectionType = CollectionType.ARRAY;
        }
        else if (List.class.isAssignableFrom(declaredType)) {
            gic = createInternalFilteredGuardedInvocationComponent(BeanLinker.GET_LIST_ELEMENT, linkerServices);
            collectionType = CollectionType.LIST;
        }
        else if (Map.class.isAssignableFrom(declaredType)) {
            gic = createInternalFilteredGuardedInvocationComponent(BeanLinker.GET_MAP_ELEMENT, linkerServices);
            collectionType = CollectionType.MAP;
        }
        else if (this.clazz.isArray()) {
            gic = this.getClassGuardedInvocationComponent(linkerServices.filterInternalObjects(MethodHandles.arrayElementGetter(this.clazz)), callSiteType);
            collectionType = CollectionType.ARRAY;
        }
        else if (List.class.isAssignableFrom(this.clazz)) {
            gic = createInternalFilteredGuardedInvocationComponent(BeanLinker.GET_LIST_ELEMENT, Guards.asType(BeanLinker.LIST_GUARD, callSiteType), List.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF, linkerServices);
            collectionType = CollectionType.LIST;
        }
        else {
            if (!Map.class.isAssignableFrom(this.clazz)) {
                return nextComponent;
            }
            gic = createInternalFilteredGuardedInvocationComponent(BeanLinker.GET_MAP_ELEMENT, Guards.asType(BeanLinker.MAP_GUARD, callSiteType), Map.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF, linkerServices);
            collectionType = CollectionType.MAP;
        }
        final String fixedKey = getFixedKey(callSiteDescriptor);
        Object typedFixedKey;
        if (collectionType != CollectionType.MAP && fixedKey != null) {
            typedFixedKey = convertKeyToInteger(fixedKey, linkerServices);
            if (typedFixedKey == null) {
                return nextComponent;
            }
        }
        else {
            typedFixedKey = fixedKey;
        }
        final GuardedInvocation gi = gic.getGuardedInvocation();
        final Binder binder = new Binder(linkerServices, callSiteType, typedFixedKey);
        final MethodHandle invocation = gi.getInvocation();
        if (nextComponent == null) {
            return gic.replaceInvocation(binder.bind(invocation));
        }
        MethodHandle checkGuard = null;
        switch (collectionType) {
            case LIST: {
                checkGuard = convertArgToInt(BeanLinker.RANGE_CHECK_LIST, linkerServices, callSiteDescriptor);
                break;
            }
            case MAP: {
                checkGuard = linkerServices.filterInternalObjects(BeanLinker.CONTAINS_MAP);
                break;
            }
            case ARRAY: {
                checkGuard = convertArgToInt(BeanLinker.RANGE_CHECK_ARRAY, linkerServices, callSiteDescriptor);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
        final MethodPair matchedInvocations = AbstractJavaLinker.matchReturnTypes(binder.bind(invocation), nextComponent.getGuardedInvocation().getInvocation());
        return nextComponent.compose(matchedInvocations.guardWithTest(binder.bindTest(checkGuard)), gi.getGuard(), gic.getValidatorClass(), gic.getValidationType());
    }
    
    private static GuardedInvocationComponent createInternalFilteredGuardedInvocationComponent(final MethodHandle invocation, final LinkerServices linkerServices) {
        return new GuardedInvocationComponent(linkerServices.filterInternalObjects(invocation));
    }
    
    private static GuardedInvocationComponent createInternalFilteredGuardedInvocationComponent(final MethodHandle invocation, final MethodHandle guard, final Class<?> validatorClass, final GuardedInvocationComponent.ValidationType validationType, final LinkerServices linkerServices) {
        return new GuardedInvocationComponent(linkerServices.filterInternalObjects(invocation), guard, validatorClass, validationType);
    }
    
    private static String getFixedKey(final CallSiteDescriptor callSiteDescriptor) {
        return (callSiteDescriptor.getNameTokenCount() == 2) ? null : callSiteDescriptor.getNameToken(2);
    }
    
    private static Object convertKeyToInteger(final String fixedKey, final LinkerServices linkerServices) throws Exception {
        try {
            if (linkerServices.canConvert(String.class, Number.class)) {
                try {
                    final Object val = linkerServices.getTypeConverter(String.class, Number.class).invoke(fixedKey);
                    if (!(val instanceof Number)) {
                        return null;
                    }
                    final Number n = (Number)val;
                    if (n instanceof Integer) {
                        return n;
                    }
                    final int intIndex = n.intValue();
                    final double doubleValue = n.doubleValue();
                    if (intIndex != doubleValue && !Double.isInfinite(doubleValue)) {
                        return null;
                    }
                    return intIndex;
                }
                catch (Exception | Error ex) {
                    final Throwable t2;
                    final Throwable e = t2;
                    throw e;
                }
                catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
            return Integer.valueOf(fixedKey);
        }
        catch (NumberFormatException e2) {
            return null;
        }
    }
    
    private static MethodHandle convertArgToInt(final MethodHandle mh, final LinkerServices ls, final CallSiteDescriptor desc) {
        final Class<?> sourceType = desc.getMethodType().parameterType(1);
        if (TypeUtilities.isMethodInvocationConvertible(sourceType, Number.class)) {
            return mh;
        }
        if (ls.canConvert(sourceType, Number.class)) {
            final MethodHandle converter = ls.getTypeConverter(sourceType, Number.class);
            return MethodHandles.filterArguments(mh, 1, converter.asType(converter.type().changeReturnType(mh.type().parameterType(1))));
        }
        return mh;
    }
    
    private static MethodHandle findRangeCheck(final Class<?> collectionType) {
        return Lookup.findOwnStatic(MethodHandles.lookup(), "rangeCheck", Boolean.TYPE, collectionType, Object.class);
    }
    
    private static final boolean rangeCheck(final Object array, final Object index) {
        if (!(index instanceof Number)) {
            return false;
        }
        final Number n = (Number)index;
        final int intIndex = n.intValue();
        final double doubleValue = n.doubleValue();
        if (intIndex != doubleValue && !Double.isInfinite(doubleValue)) {
            return false;
        }
        if (0 <= intIndex && intIndex < Array.getLength(array)) {
            return true;
        }
        throw new ArrayIndexOutOfBoundsException("Array index out of range: " + n);
    }
    
    private static final boolean rangeCheck(final List<?> list, final Object index) {
        if (!(index instanceof Number)) {
            return false;
        }
        final Number n = (Number)index;
        final int intIndex = n.intValue();
        final double doubleValue = n.doubleValue();
        if (intIndex != doubleValue && !Double.isInfinite(doubleValue)) {
            return false;
        }
        if (0 <= intIndex && intIndex < list.size()) {
            return true;
        }
        throw new IndexOutOfBoundsException("Index: " + n + ", Size: " + list.size());
    }
    
    private GuardedInvocationComponent getElementSetter(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices, final List<String> operations) throws Exception {
        final MethodType callSiteType = callSiteDescriptor.getMethodType();
        final Class<?> declaredType = callSiteType.parameterType(0);
        GuardedInvocationComponent gic;
        CollectionType collectionType;
        if (declaredType.isArray()) {
            gic = createInternalFilteredGuardedInvocationComponent(MethodHandles.arrayElementSetter(declaredType), linkerServices);
            collectionType = CollectionType.ARRAY;
        }
        else if (List.class.isAssignableFrom(declaredType)) {
            gic = createInternalFilteredGuardedInvocationComponent(BeanLinker.SET_LIST_ELEMENT, linkerServices);
            collectionType = CollectionType.LIST;
        }
        else if (Map.class.isAssignableFrom(declaredType)) {
            gic = createInternalFilteredGuardedInvocationComponent(BeanLinker.PUT_MAP_ELEMENT, linkerServices);
            collectionType = CollectionType.MAP;
        }
        else if (this.clazz.isArray()) {
            gic = this.getClassGuardedInvocationComponent(linkerServices.filterInternalObjects(MethodHandles.arrayElementSetter(this.clazz)), callSiteType);
            collectionType = CollectionType.ARRAY;
        }
        else if (List.class.isAssignableFrom(this.clazz)) {
            gic = createInternalFilteredGuardedInvocationComponent(BeanLinker.SET_LIST_ELEMENT, Guards.asType(BeanLinker.LIST_GUARD, callSiteType), List.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF, linkerServices);
            collectionType = CollectionType.LIST;
        }
        else if (Map.class.isAssignableFrom(this.clazz)) {
            gic = createInternalFilteredGuardedInvocationComponent(BeanLinker.PUT_MAP_ELEMENT, Guards.asType(BeanLinker.MAP_GUARD, callSiteType), Map.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF, linkerServices);
            collectionType = CollectionType.MAP;
        }
        else {
            gic = null;
            collectionType = null;
        }
        final GuardedInvocationComponent nextComponent = (collectionType == CollectionType.MAP) ? null : this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, operations);
        if (gic == null) {
            return nextComponent;
        }
        final String fixedKey = getFixedKey(callSiteDescriptor);
        Object typedFixedKey;
        if (collectionType != CollectionType.MAP && fixedKey != null) {
            typedFixedKey = convertKeyToInteger(fixedKey, linkerServices);
            if (typedFixedKey == null) {
                return nextComponent;
            }
        }
        else {
            typedFixedKey = fixedKey;
        }
        final GuardedInvocation gi = gic.getGuardedInvocation();
        final Binder binder = new Binder(linkerServices, callSiteType, typedFixedKey);
        final MethodHandle invocation = gi.getInvocation();
        if (nextComponent == null) {
            return gic.replaceInvocation(binder.bind(invocation));
        }
        assert collectionType == CollectionType.ARRAY;
        final MethodHandle checkGuard = convertArgToInt((collectionType == CollectionType.LIST) ? BeanLinker.RANGE_CHECK_LIST : BeanLinker.RANGE_CHECK_ARRAY, linkerServices, callSiteDescriptor);
        final MethodPair matchedInvocations = AbstractJavaLinker.matchReturnTypes(binder.bind(invocation), nextComponent.getGuardedInvocation().getInvocation());
        return nextComponent.compose(matchedInvocations.guardWithTest(binder.bindTest(checkGuard)), gi.getGuard(), gic.getValidatorClass(), gic.getValidationType());
    }
    
    private GuardedInvocationComponent getLengthGetter(final CallSiteDescriptor callSiteDescriptor) {
        assertParameterCount(callSiteDescriptor, 1);
        final MethodType callSiteType = callSiteDescriptor.getMethodType();
        final Class<?> declaredType = callSiteType.parameterType(0);
        if (declaredType.isArray()) {
            return new GuardedInvocationComponent(BeanLinker.GET_ARRAY_LENGTH.asType(callSiteType));
        }
        if (Collection.class.isAssignableFrom(declaredType)) {
            return new GuardedInvocationComponent(BeanLinker.GET_COLLECTION_LENGTH.asType(callSiteType));
        }
        if (Map.class.isAssignableFrom(declaredType)) {
            return new GuardedInvocationComponent(BeanLinker.GET_MAP_LENGTH.asType(callSiteType));
        }
        if (this.clazz.isArray()) {
            return new GuardedInvocationComponent(BeanLinker.GET_ARRAY_LENGTH.asType(callSiteType), Guards.isArray(0, callSiteType), GuardedInvocationComponent.ValidationType.IS_ARRAY);
        }
        if (Collection.class.isAssignableFrom(this.clazz)) {
            return new GuardedInvocationComponent(BeanLinker.GET_COLLECTION_LENGTH.asType(callSiteType), Guards.asType(BeanLinker.COLLECTION_GUARD, callSiteType), Collection.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF);
        }
        if (Map.class.isAssignableFrom(this.clazz)) {
            return new GuardedInvocationComponent(BeanLinker.GET_MAP_LENGTH.asType(callSiteType), Guards.asType(BeanLinker.MAP_GUARD, callSiteType), Map.class, GuardedInvocationComponent.ValidationType.INSTANCE_OF);
        }
        return null;
    }
    
    private static void assertParameterCount(final CallSiteDescriptor descriptor, final int paramCount) {
        if (descriptor.getMethodType().parameterCount() != paramCount) {
            throw new BootstrapMethodError(descriptor.getName() + " must have exactly " + paramCount + " parameters.");
        }
    }
    
    static {
        BeanLinker.GET_LIST_ELEMENT = Lookup.PUBLIC.findVirtual(List.class, "get", MethodType.methodType(Object.class, Integer.TYPE));
        BeanLinker.GET_MAP_ELEMENT = Lookup.PUBLIC.findVirtual(Map.class, "get", MethodType.methodType(Object.class, Object.class));
        BeanLinker.LIST_GUARD = Guards.getInstanceOfGuard(List.class);
        BeanLinker.MAP_GUARD = Guards.getInstanceOfGuard(Map.class);
        BeanLinker.RANGE_CHECK_ARRAY = findRangeCheck(Object.class);
        BeanLinker.RANGE_CHECK_LIST = findRangeCheck(List.class);
        BeanLinker.CONTAINS_MAP = Lookup.PUBLIC.findVirtual(Map.class, "containsKey", MethodType.methodType(Boolean.TYPE, Object.class));
        BeanLinker.SET_LIST_ELEMENT = Lookup.PUBLIC.findVirtual(List.class, "set", MethodType.methodType(Object.class, Integer.TYPE, Object.class));
        BeanLinker.PUT_MAP_ELEMENT = Lookup.PUBLIC.findVirtual(Map.class, "put", MethodType.methodType(Object.class, Object.class, Object.class));
        BeanLinker.GET_ARRAY_LENGTH = Lookup.PUBLIC.findStatic(Array.class, "getLength", MethodType.methodType(Integer.TYPE, Object.class));
        BeanLinker.GET_COLLECTION_LENGTH = Lookup.PUBLIC.findVirtual(Collection.class, "size", MethodType.methodType(Integer.TYPE));
        BeanLinker.GET_MAP_LENGTH = Lookup.PUBLIC.findVirtual(Map.class, "size", MethodType.methodType(Integer.TYPE));
        BeanLinker.COLLECTION_GUARD = Guards.getInstanceOfGuard(Collection.class);
    }
    
    private enum CollectionType
    {
        ARRAY, 
        LIST, 
        MAP;
    }
    
    private static class Binder
    {
        private final LinkerServices linkerServices;
        private final MethodType methodType;
        private final Object fixedKey;
        
        Binder(final LinkerServices linkerServices, final MethodType methodType, final Object fixedKey) {
            this.linkerServices = linkerServices;
            this.methodType = ((fixedKey == null) ? methodType : methodType.insertParameterTypes(1, (Class<?>[])new Class[] { fixedKey.getClass() }));
            this.fixedKey = fixedKey;
        }
        
        MethodHandle bind(final MethodHandle handle) {
            return this.bindToFixedKey(this.linkerServices.asTypeLosslessReturn(handle, this.methodType));
        }
        
        MethodHandle bindTest(final MethodHandle handle) {
            return this.bindToFixedKey(Guards.asType(handle, this.methodType));
        }
        
        private MethodHandle bindToFixedKey(final MethodHandle handle) {
            return (this.fixedKey == null) ? handle : MethodHandles.insertArguments(handle, 1, this.fixedKey);
        }
    }
}
