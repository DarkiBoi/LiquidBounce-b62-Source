// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.support.TypeUtilities;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.support.Guards;
import java.lang.invoke.MethodType;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import jdk.internal.dynalink.support.Lookup;
import java.util.Map;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;

abstract class AbstractJavaLinker implements GuardingDynamicLinker
{
    final Class<?> clazz;
    private final MethodHandle classGuard;
    private final MethodHandle assignableGuard;
    private final Map<String, AnnotatedDynamicMethod> propertyGetters;
    private final Map<String, DynamicMethod> propertySetters;
    private final Map<String, DynamicMethod> methods;
    private static final MethodHandle IS_METHOD_HANDLE_NOT_NULL;
    private static final MethodHandle CONSTANT_NULL_DROP_METHOD_HANDLE;
    private static final Lookup privateLookup;
    private static final MethodHandle IS_ANNOTATED_METHOD_NOT_NULL;
    private static final MethodHandle CONSTANT_NULL_DROP_ANNOTATED_METHOD;
    private static final MethodHandle GET_ANNOTATED_METHOD;
    private static final MethodHandle GETTER_INVOKER;
    private static final MethodHandle IS_DYNAMIC_METHOD;
    private static final MethodHandle OBJECT_IDENTITY;
    private static MethodHandle GET_PROPERTY_GETTER_HANDLE;
    private final MethodHandle getPropertyGetterHandle;
    private static final MethodHandle GET_PROPERTY_SETTER_HANDLE;
    private final MethodHandle getPropertySetterHandle;
    private static MethodHandle GET_DYNAMIC_METHOD;
    private final MethodHandle getDynamicMethod;
    
    AbstractJavaLinker(final Class<?> clazz, final MethodHandle classGuard) {
        this(clazz, classGuard, classGuard);
    }
    
    AbstractJavaLinker(final Class<?> clazz, final MethodHandle classGuard, final MethodHandle assignableGuard) {
        this.propertyGetters = new HashMap<String, AnnotatedDynamicMethod>();
        this.propertySetters = new HashMap<String, DynamicMethod>();
        this.methods = new HashMap<String, DynamicMethod>();
        this.getPropertyGetterHandle = AbstractJavaLinker.GET_PROPERTY_GETTER_HANDLE.bindTo(this);
        this.getPropertySetterHandle = AbstractJavaLinker.GET_PROPERTY_SETTER_HANDLE.bindTo(this);
        this.getDynamicMethod = AbstractJavaLinker.GET_DYNAMIC_METHOD.bindTo(this);
        this.clazz = clazz;
        this.classGuard = classGuard;
        this.assignableGuard = assignableGuard;
        final FacetIntrospector introspector = this.createFacetIntrospector();
        for (final Method method : introspector.getMethods()) {
            final String name = method.getName();
            this.addMember(name, method, this.methods);
            if (name.startsWith("get") && name.length() > 3 && method.getParameterTypes().length == 0) {
                this.setPropertyGetter(method, 3);
            }
            else if (name.startsWith("is") && name.length() > 2 && method.getParameterTypes().length == 0 && method.getReturnType() == Boolean.TYPE) {
                this.setPropertyGetter(method, 2);
            }
            else {
                if (!name.startsWith("set") || name.length() <= 3 || method.getParameterTypes().length != 1) {
                    continue;
                }
                this.addMember(decapitalize(name.substring(3)), method, this.propertySetters);
            }
        }
        for (final Field field : introspector.getFields()) {
            final String name = field.getName();
            if (!this.propertyGetters.containsKey(name)) {
                this.setPropertyGetter(name, introspector.unreflectGetter(field), GuardedInvocationComponent.ValidationType.EXACT_CLASS);
            }
            if (!Modifier.isFinal(field.getModifiers()) && !this.propertySetters.containsKey(name)) {
                this.addMember(name, new SimpleDynamicMethod(introspector.unreflectSetter(field), clazz, name), this.propertySetters);
            }
        }
        for (final Map.Entry<String, MethodHandle> innerClassSpec : introspector.getInnerClassGetters().entrySet()) {
            final String name = innerClassSpec.getKey();
            if (!this.propertyGetters.containsKey(name)) {
                this.setPropertyGetter(name, innerClassSpec.getValue(), GuardedInvocationComponent.ValidationType.EXACT_CLASS);
            }
        }
    }
    
    private static String decapitalize(final String str) {
        assert str != null;
        if (str.isEmpty()) {
            return str;
        }
        final char c0 = str.charAt(0);
        if (Character.isLowerCase(c0)) {
            return str;
        }
        if (str.length() > 1 && Character.isUpperCase(str.charAt(1))) {
            return str;
        }
        final char[] c2 = str.toCharArray();
        c2[0] = Character.toLowerCase(c0);
        return new String(c2);
    }
    
    abstract FacetIntrospector createFacetIntrospector();
    
    Collection<String> getReadablePropertyNames() {
        return getUnmodifiableKeys(this.propertyGetters);
    }
    
    Collection<String> getWritablePropertyNames() {
        return getUnmodifiableKeys(this.propertySetters);
    }
    
    Collection<String> getMethodNames() {
        return getUnmodifiableKeys(this.methods);
    }
    
    private static Collection<String> getUnmodifiableKeys(final Map<String, ?> m) {
        return Collections.unmodifiableCollection((Collection<? extends String>)m.keySet());
    }
    
    private void setPropertyGetter(final String name, final SingleDynamicMethod handle, final GuardedInvocationComponent.ValidationType validationType) {
        this.propertyGetters.put(name, new AnnotatedDynamicMethod(handle, validationType));
    }
    
    private void setPropertyGetter(final Method getter, final int prefixLen) {
        this.setPropertyGetter(decapitalize(getter.getName().substring(prefixLen)), createDynamicMethod(getMostGenericGetter(getter)), GuardedInvocationComponent.ValidationType.INSTANCE_OF);
    }
    
    void setPropertyGetter(final String name, final MethodHandle handle, final GuardedInvocationComponent.ValidationType validationType) {
        this.setPropertyGetter(name, new SimpleDynamicMethod(handle, this.clazz, name), validationType);
    }
    
    private void addMember(final String name, final AccessibleObject ao, final Map<String, DynamicMethod> methodMap) {
        this.addMember(name, createDynamicMethod(ao), methodMap);
    }
    
    private void addMember(final String name, final SingleDynamicMethod method, final Map<String, DynamicMethod> methodMap) {
        final DynamicMethod existingMethod = methodMap.get(name);
        final DynamicMethod newMethod = mergeMethods(method, existingMethod, this.clazz, name);
        if (newMethod != existingMethod) {
            methodMap.put(name, newMethod);
        }
    }
    
    static DynamicMethod createDynamicMethod(final Iterable<? extends AccessibleObject> members, final Class<?> clazz, final String name) {
        DynamicMethod dynMethod = null;
        for (final AccessibleObject method : members) {
            dynMethod = mergeMethods(createDynamicMethod(method), dynMethod, clazz, name);
        }
        return dynMethod;
    }
    
    private static SingleDynamicMethod createDynamicMethod(final AccessibleObject m) {
        if (CallerSensitiveDetector.isCallerSensitive(m)) {
            return new CallerSensitiveDynamicMethod(m);
        }
        MethodHandle mh;
        try {
            mh = unreflectSafely(m);
        }
        catch (IllegalAccessError e) {
            return new CallerSensitiveDynamicMethod(m);
        }
        final Member member = (Member)m;
        return new SimpleDynamicMethod(mh, member.getDeclaringClass(), member.getName(), m instanceof Constructor);
    }
    
    private static MethodHandle unreflectSafely(final AccessibleObject m) {
        if (!(m instanceof Method)) {
            return StaticClassIntrospector.editConstructorMethodHandle(Lookup.PUBLIC.unreflectConstructor((Constructor<?>)m));
        }
        final Method reflMethod = (Method)m;
        final MethodHandle handle = Lookup.PUBLIC.unreflect(reflMethod);
        if (Modifier.isStatic(reflMethod.getModifiers())) {
            return StaticClassIntrospector.editStaticMethodHandle(handle);
        }
        return handle;
    }
    
    private static DynamicMethod mergeMethods(final SingleDynamicMethod method, final DynamicMethod existing, final Class<?> clazz, final String name) {
        if (existing == null) {
            return method;
        }
        if (existing.contains(method)) {
            return existing;
        }
        if (existing instanceof SingleDynamicMethod) {
            final OverloadedDynamicMethod odm = new OverloadedDynamicMethod(clazz, name);
            odm.addMethod((SingleDynamicMethod)existing);
            odm.addMethod(method);
            return odm;
        }
        if (existing instanceof OverloadedDynamicMethod) {
            ((OverloadedDynamicMethod)existing).addMethod(method);
            return existing;
        }
        throw new AssertionError();
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest request, final LinkerServices linkerServices) throws Exception {
        final LinkRequest ncrequest = request.withoutRuntimeContext();
        final CallSiteDescriptor callSiteDescriptor = ncrequest.getCallSiteDescriptor();
        final String op = callSiteDescriptor.getNameToken(1);
        if ("callMethod" == op) {
            return this.getCallPropWithThis(callSiteDescriptor, linkerServices);
        }
        for (List<String> operations = CallSiteDescriptorFactory.tokenizeOperators(callSiteDescriptor); !operations.isEmpty(); operations = pop(operations)) {
            final GuardedInvocationComponent gic = this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, operations);
            if (gic != null) {
                return gic.getGuardedInvocation();
            }
        }
        return null;
    }
    
    protected GuardedInvocationComponent getGuardedInvocationComponent(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices, final List<String> operations) throws Exception {
        if (operations.isEmpty()) {
            return null;
        }
        final String op = operations.get(0);
        if ("getProp".equals(op)) {
            return this.getPropertyGetter(callSiteDescriptor, linkerServices, pop(operations));
        }
        if ("setProp".equals(op)) {
            return this.getPropertySetter(callSiteDescriptor, linkerServices, pop(operations));
        }
        if ("getMethod".equals(op)) {
            return this.getMethodGetter(callSiteDescriptor, linkerServices, pop(operations));
        }
        return null;
    }
    
    static final <T> List<T> pop(final List<T> l) {
        return l.subList(1, l.size());
    }
    
    MethodHandle getClassGuard(final CallSiteDescriptor desc) {
        return this.getClassGuard(desc.getMethodType());
    }
    
    MethodHandle getClassGuard(final MethodType type) {
        return Guards.asType(this.classGuard, type);
    }
    
    GuardedInvocationComponent getClassGuardedInvocationComponent(final MethodHandle invocation, final MethodType type) {
        return new GuardedInvocationComponent(invocation, this.getClassGuard(type), this.clazz, GuardedInvocationComponent.ValidationType.EXACT_CLASS);
    }
    
    SingleDynamicMethod getConstructorMethod(final String signature) {
        return null;
    }
    
    private MethodHandle getAssignableGuard(final MethodType type) {
        return Guards.asType(this.assignableGuard, type);
    }
    
    private GuardedInvocation getCallPropWithThis(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices) {
        switch (callSiteDescriptor.getNameTokenCount()) {
            case 3: {
                return this.createGuardedDynamicMethodInvocation(callSiteDescriptor, linkerServices, callSiteDescriptor.getNameToken(2), this.methods);
            }
            default: {
                return null;
            }
        }
    }
    
    private GuardedInvocation createGuardedDynamicMethodInvocation(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices, final String methodName, final Map<String, DynamicMethod> methodMap) {
        final MethodHandle inv = this.getDynamicMethodInvocation(callSiteDescriptor, linkerServices, methodName, methodMap);
        return (inv == null) ? null : new GuardedInvocation(inv, this.getClassGuard(callSiteDescriptor.getMethodType()));
    }
    
    private MethodHandle getDynamicMethodInvocation(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices, final String methodName, final Map<String, DynamicMethod> methodMap) {
        final DynamicMethod dynaMethod = this.getDynamicMethod(methodName, methodMap);
        return (dynaMethod != null) ? dynaMethod.getInvocation(callSiteDescriptor, linkerServices) : null;
    }
    
    private DynamicMethod getDynamicMethod(final String methodName, final Map<String, DynamicMethod> methodMap) {
        final DynamicMethod dynaMethod = methodMap.get(methodName);
        return (dynaMethod != null) ? dynaMethod : this.getExplicitSignatureDynamicMethod(methodName, methodMap);
    }
    
    private SingleDynamicMethod getExplicitSignatureDynamicMethod(final String fullName, final Map<String, DynamicMethod> methodsMap) {
        final int lastChar = fullName.length() - 1;
        if (fullName.charAt(lastChar) != ')') {
            return null;
        }
        final int openBrace = fullName.indexOf(40);
        if (openBrace == -1) {
            return null;
        }
        final String name = fullName.substring(0, openBrace);
        final String signature = fullName.substring(openBrace + 1, lastChar);
        final DynamicMethod simpleNamedMethod = methodsMap.get(name);
        if (simpleNamedMethod != null) {
            return simpleNamedMethod.getMethodForExactParamTypes(signature);
        }
        if (name.isEmpty()) {
            return this.getConstructorMethod(signature);
        }
        return null;
    }
    
    private GuardedInvocationComponent getPropertySetter(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices, final List<String> operations) throws Exception {
        switch (callSiteDescriptor.getNameTokenCount()) {
            case 2: {
                assertParameterCount(callSiteDescriptor, 3);
                final MethodType origType = callSiteDescriptor.getMethodType();
                final MethodType type = (origType.returnType() == Void.TYPE) ? origType : origType.changeReturnType((Class<?>)Object.class);
                final MethodType setterType = type.dropParameterTypes(1, 2);
                final MethodHandle boundGetter = MethodHandles.insertArguments(this.getPropertySetterHandle, 0, callSiteDescriptor.changeMethodType(setterType), linkerServices);
                final MethodHandle typedGetter = linkerServices.asType(boundGetter, type.changeReturnType((Class<?>)MethodHandle.class));
                final MethodHandle invokeHandle = MethodHandles.exactInvoker(setterType);
                final MethodHandle invokeHandleFolded = MethodHandles.dropArguments(invokeHandle, 2, type.parameterType(1));
                final GuardedInvocationComponent nextComponent = this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, operations);
                MethodHandle fallbackFolded;
                if (nextComponent == null) {
                    fallbackFolded = MethodHandles.dropArguments(AbstractJavaLinker.CONSTANT_NULL_DROP_METHOD_HANDLE, 1, type.parameterList()).asType(type.insertParameterTypes(0, (Class<?>[])new Class[] { MethodHandle.class }));
                }
                else {
                    fallbackFolded = MethodHandles.dropArguments(nextComponent.getGuardedInvocation().getInvocation(), 0, MethodHandle.class);
                }
                final MethodHandle compositeSetter = MethodHandles.foldArguments(MethodHandles.guardWithTest(AbstractJavaLinker.IS_METHOD_HANDLE_NOT_NULL, invokeHandleFolded, fallbackFolded), typedGetter);
                if (nextComponent == null) {
                    return this.getClassGuardedInvocationComponent(compositeSetter, type);
                }
                return nextComponent.compose(compositeSetter, this.getClassGuard(type), this.clazz, GuardedInvocationComponent.ValidationType.EXACT_CLASS);
            }
            case 3: {
                assertParameterCount(callSiteDescriptor, 2);
                final GuardedInvocation gi = this.createGuardedDynamicMethodInvocation(callSiteDescriptor, linkerServices, callSiteDescriptor.getNameToken(2), this.propertySetters);
                if (gi != null) {
                    return new GuardedInvocationComponent(gi, this.clazz, GuardedInvocationComponent.ValidationType.EXACT_CLASS);
                }
                return this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, operations);
            }
            default: {
                return null;
            }
        }
    }
    
    private GuardedInvocationComponent getPropertyGetter(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices, final List<String> ops) throws Exception {
        switch (callSiteDescriptor.getNameTokenCount()) {
            case 2: {
                final MethodType type = callSiteDescriptor.getMethodType().changeReturnType((Class<?>)Object.class);
                assertParameterCount(callSiteDescriptor, 2);
                final MethodHandle typedGetter = linkerServices.asType(this.getPropertyGetterHandle, type.changeReturnType((Class<?>)AnnotatedDynamicMethod.class));
                final MethodHandle callSiteBoundMethodGetter = MethodHandles.insertArguments(AbstractJavaLinker.GET_ANNOTATED_METHOD, 1, callSiteDescriptor.getLookup(), linkerServices);
                final MethodHandle callSiteBoundInvoker = MethodHandles.filterArguments(AbstractJavaLinker.GETTER_INVOKER, 0, callSiteBoundMethodGetter);
                final MethodHandle invokeHandleTyped = linkerServices.asType(callSiteBoundInvoker, MethodType.methodType(type.returnType(), AnnotatedDynamicMethod.class, type.parameterType(0)));
                final MethodHandle invokeHandleFolded = MethodHandles.dropArguments(invokeHandleTyped, 2, type.parameterType(1));
                final GuardedInvocationComponent nextComponent = this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, ops);
                MethodHandle fallbackFolded;
                if (nextComponent == null) {
                    fallbackFolded = MethodHandles.dropArguments(AbstractJavaLinker.CONSTANT_NULL_DROP_ANNOTATED_METHOD, 1, type.parameterList()).asType(type.insertParameterTypes(0, (Class<?>[])new Class[] { AnnotatedDynamicMethod.class }));
                }
                else {
                    final MethodHandle nextInvocation = nextComponent.getGuardedInvocation().getInvocation();
                    final MethodType nextType = nextInvocation.type();
                    fallbackFolded = MethodHandles.dropArguments(nextInvocation.asType(nextType.changeReturnType((Class<?>)Object.class)), 0, AnnotatedDynamicMethod.class);
                }
                final MethodHandle compositeGetter = MethodHandles.foldArguments(MethodHandles.guardWithTest(AbstractJavaLinker.IS_ANNOTATED_METHOD_NOT_NULL, invokeHandleFolded, fallbackFolded), typedGetter);
                if (nextComponent == null) {
                    return this.getClassGuardedInvocationComponent(compositeGetter, type);
                }
                return nextComponent.compose(compositeGetter, this.getClassGuard(type), this.clazz, GuardedInvocationComponent.ValidationType.EXACT_CLASS);
            }
            case 3: {
                assertParameterCount(callSiteDescriptor, 1);
                final AnnotatedDynamicMethod annGetter = this.propertyGetters.get(callSiteDescriptor.getNameToken(2));
                if (annGetter == null) {
                    return this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, ops);
                }
                final MethodHandle getter = annGetter.getInvocation(callSiteDescriptor, linkerServices);
                final GuardedInvocationComponent.ValidationType validationType = annGetter.validationType;
                return new GuardedInvocationComponent(getter, this.getGuard(validationType, callSiteDescriptor.getMethodType()), this.clazz, validationType);
            }
            default: {
                return null;
            }
        }
    }
    
    private MethodHandle getGuard(final GuardedInvocationComponent.ValidationType validationType, final MethodType methodType) {
        switch (validationType) {
            case EXACT_CLASS: {
                return this.getClassGuard(methodType);
            }
            case INSTANCE_OF: {
                return this.getAssignableGuard(methodType);
            }
            case IS_ARRAY: {
                return Guards.isArray(0, methodType);
            }
            case NONE: {
                return null;
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    private GuardedInvocationComponent getMethodGetter(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices, final List<String> ops) throws Exception {
        final MethodType type = callSiteDescriptor.getMethodType().changeReturnType((Class<?>)Object.class);
        switch (callSiteDescriptor.getNameTokenCount()) {
            case 2: {
                assertParameterCount(callSiteDescriptor, 2);
                final GuardedInvocationComponent nextComponent = this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, ops);
                if (nextComponent == null || !TypeUtilities.areAssignable(DynamicMethod.class, nextComponent.getGuardedInvocation().getInvocation().type().returnType())) {
                    return this.getClassGuardedInvocationComponent(linkerServices.asType(this.getDynamicMethod, type), type);
                }
                final MethodHandle typedGetter = linkerServices.asType(this.getDynamicMethod, type);
                final MethodHandle returnMethodHandle = linkerServices.asType(MethodHandles.dropArguments(AbstractJavaLinker.OBJECT_IDENTITY, 1, type.parameterList()), type.insertParameterTypes(0, (Class<?>[])new Class[] { Object.class }));
                final MethodHandle nextComponentInvocation = nextComponent.getGuardedInvocation().getInvocation();
                assert nextComponentInvocation.type().changeReturnType(type.returnType()).equals((Object)type);
                final MethodHandle nextCombinedInvocation = MethodHandles.dropArguments(nextComponentInvocation, 0, Object.class);
                final MethodHandle compositeGetter = MethodHandles.foldArguments(MethodHandles.guardWithTest(AbstractJavaLinker.IS_DYNAMIC_METHOD, returnMethodHandle, nextCombinedInvocation), typedGetter);
                return nextComponent.compose(compositeGetter, this.getClassGuard(type), this.clazz, GuardedInvocationComponent.ValidationType.EXACT_CLASS);
            }
            case 3: {
                assertParameterCount(callSiteDescriptor, 1);
                final DynamicMethod method = this.getDynamicMethod(callSiteDescriptor.getNameToken(2));
                if (method == null) {
                    return this.getGuardedInvocationComponent(callSiteDescriptor, linkerServices, ops);
                }
                return this.getClassGuardedInvocationComponent(linkerServices.asType(MethodHandles.dropArguments(MethodHandles.constant(Object.class, method), 0, type.parameterType(0)), type), type);
            }
            default: {
                return null;
            }
        }
    }
    
    static MethodPair matchReturnTypes(final MethodHandle m1, final MethodHandle m2) {
        final MethodType type1 = m1.type();
        final MethodType type2 = m2.type();
        final Class<?> commonRetType = TypeUtilities.getCommonLosslessConversionType(type1.returnType(), type2.returnType());
        return new MethodPair(m1.asType(type1.changeReturnType(commonRetType)), m2.asType(type2.changeReturnType(commonRetType)));
    }
    
    private static void assertParameterCount(final CallSiteDescriptor descriptor, final int paramCount) {
        if (descriptor.getMethodType().parameterCount() != paramCount) {
            throw new BootstrapMethodError(descriptor.getName() + " must have exactly " + paramCount + " parameters.");
        }
    }
    
    private Object getPropertyGetterHandle(final Object id) {
        return this.propertyGetters.get(String.valueOf(id));
    }
    
    private MethodHandle getPropertySetterHandle(final CallSiteDescriptor setterDescriptor, final LinkerServices linkerServices, final Object id) {
        return this.getDynamicMethodInvocation(setterDescriptor, linkerServices, String.valueOf(id), this.propertySetters);
    }
    
    private Object getDynamicMethod(final Object name) {
        return this.getDynamicMethod(String.valueOf(name), this.methods);
    }
    
    DynamicMethod getDynamicMethod(final String name) {
        return this.getDynamicMethod(name, this.methods);
    }
    
    private static Method getMostGenericGetter(final Method getter) {
        return getMostGenericGetter(getter.getName(), getter.getReturnType(), getter.getDeclaringClass());
    }
    
    private static Method getMostGenericGetter(final String name, final Class<?> returnType, final Class<?> declaringClass) {
        if (declaringClass == null) {
            return null;
        }
        for (final Class<?> itf : declaringClass.getInterfaces()) {
            final Method itfGetter = getMostGenericGetter(name, returnType, itf);
            if (itfGetter != null) {
                return itfGetter;
            }
        }
        final Method superGetter = getMostGenericGetter(name, returnType, declaringClass.getSuperclass());
        if (superGetter != null) {
            return superGetter;
        }
        if (!CheckRestrictedPackage.isRestrictedClass(declaringClass)) {
            try {
                return declaringClass.getMethod(name, (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException ex) {}
        }
        return null;
    }
    
    static {
        IS_METHOD_HANDLE_NOT_NULL = Guards.isNotNull().asType(MethodType.methodType(Boolean.TYPE, MethodHandle.class));
        CONSTANT_NULL_DROP_METHOD_HANDLE = MethodHandles.dropArguments(MethodHandles.constant(Object.class, null), 0, MethodHandle.class);
        privateLookup = new Lookup(MethodHandles.lookup());
        IS_ANNOTATED_METHOD_NOT_NULL = Guards.isNotNull().asType(MethodType.methodType(Boolean.TYPE, AnnotatedDynamicMethod.class));
        CONSTANT_NULL_DROP_ANNOTATED_METHOD = MethodHandles.dropArguments(MethodHandles.constant(Object.class, null), 0, AnnotatedDynamicMethod.class);
        GET_ANNOTATED_METHOD = AbstractJavaLinker.privateLookup.findVirtual(AnnotatedDynamicMethod.class, "getTarget", MethodType.methodType(MethodHandle.class, MethodHandles.Lookup.class, LinkerServices.class));
        GETTER_INVOKER = MethodHandles.invoker(MethodType.methodType(Object.class, Object.class));
        IS_DYNAMIC_METHOD = Guards.isInstance(DynamicMethod.class, MethodType.methodType(Boolean.TYPE, Object.class));
        OBJECT_IDENTITY = MethodHandles.identity(Object.class);
        AbstractJavaLinker.GET_PROPERTY_GETTER_HANDLE = MethodHandles.dropArguments(AbstractJavaLinker.privateLookup.findOwnSpecial("getPropertyGetterHandle", Object.class, Object.class), 1, Object.class);
        GET_PROPERTY_SETTER_HANDLE = MethodHandles.dropArguments(MethodHandles.dropArguments(AbstractJavaLinker.privateLookup.findOwnSpecial("getPropertySetterHandle", MethodHandle.class, CallSiteDescriptor.class, LinkerServices.class, Object.class), 3, Object.class), 5, Object.class);
        AbstractJavaLinker.GET_DYNAMIC_METHOD = MethodHandles.dropArguments(AbstractJavaLinker.privateLookup.findOwnSpecial("getDynamicMethod", Object.class, Object.class), 1, Object.class);
    }
    
    static class MethodPair
    {
        final MethodHandle method1;
        final MethodHandle method2;
        
        MethodPair(final MethodHandle method1, final MethodHandle method2) {
            this.method1 = method1;
            this.method2 = method2;
        }
        
        MethodHandle guardWithTest(final MethodHandle test) {
            return MethodHandles.guardWithTest(test, this.method1, this.method2);
        }
    }
    
    private static final class AnnotatedDynamicMethod
    {
        private final SingleDynamicMethod method;
        final GuardedInvocationComponent.ValidationType validationType;
        
        AnnotatedDynamicMethod(final SingleDynamicMethod method, final GuardedInvocationComponent.ValidationType validationType) {
            this.method = method;
            this.validationType = validationType;
        }
        
        MethodHandle getInvocation(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices) {
            return this.method.getInvocation(callSiteDescriptor, linkerServices);
        }
        
        MethodHandle getTarget(final MethodHandles.Lookup lookup, final LinkerServices linkerServices) {
            final MethodHandle inv = linkerServices.filterInternalObjects(this.method.getTarget(lookup));
            assert inv != null;
            return inv;
        }
    }
}
