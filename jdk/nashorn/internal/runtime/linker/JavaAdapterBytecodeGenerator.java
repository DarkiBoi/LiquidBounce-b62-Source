// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import java.lang.annotation.Annotation;
import sun.reflect.CallerSensitive;
import java.security.AccessController;
import java.util.Arrays;
import java.security.PrivilegedAction;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.runtime.JSType;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.AccessibleObject;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.commons.InstructionAdapter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.security.AccessControlContext;
import jdk.internal.org.objectweb.asm.ClassWriter;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Collection;
import jdk.internal.org.objectweb.asm.Type;

final class JavaAdapterBytecodeGenerator
{
    private static final Type SCRIPTUTILS_TYPE;
    private static final Type OBJECT_TYPE;
    private static final Type CLASS_TYPE;
    static final String OBJECT_TYPE_NAME;
    static final String SCRIPTUTILS_TYPE_NAME;
    static final String INIT = "<init>";
    static final String GLOBAL_FIELD_NAME = "global";
    static final String GLOBAL_TYPE_DESCRIPTOR;
    static final String SET_GLOBAL_METHOD_DESCRIPTOR;
    static final String VOID_NOARG_METHOD_DESCRIPTOR;
    private static final Type SCRIPT_OBJECT_TYPE;
    private static final Type SCRIPT_FUNCTION_TYPE;
    private static final Type STRING_TYPE;
    private static final Type METHOD_TYPE_TYPE;
    private static final Type METHOD_HANDLE_TYPE;
    private static final String GET_HANDLE_OBJECT_DESCRIPTOR;
    private static final String GET_HANDLE_FUNCTION_DESCRIPTOR;
    private static final String GET_CLASS_INITIALIZER_DESCRIPTOR;
    private static final Type RUNTIME_EXCEPTION_TYPE;
    private static final Type THROWABLE_TYPE;
    private static final Type UNSUPPORTED_OPERATION_TYPE;
    private static final String SERVICES_CLASS_TYPE_NAME;
    private static final String RUNTIME_EXCEPTION_TYPE_NAME;
    private static final String ERROR_TYPE_NAME;
    private static final String THROWABLE_TYPE_NAME;
    private static final String UNSUPPORTED_OPERATION_TYPE_NAME;
    private static final String METHOD_HANDLE_TYPE_DESCRIPTOR;
    private static final String GET_GLOBAL_METHOD_DESCRIPTOR;
    private static final String GET_CLASS_METHOD_DESCRIPTOR;
    private static final String EXPORT_RETURN_VALUE_METHOD_DESCRIPTOR;
    private static final String UNWRAP_METHOD_DESCRIPTOR;
    private static final String GET_CONVERTER_METHOD_DESCRIPTOR;
    private static final String TO_CHAR_PRIMITIVE_METHOD_DESCRIPTOR;
    private static final String TO_STRING_METHOD_DESCRIPTOR;
    private static final String ADAPTER_PACKAGE_PREFIX = "jdk/nashorn/javaadapters/";
    private static final String ADAPTER_CLASS_NAME_SUFFIX = "$$NashornJavaAdapter";
    private static final String JAVA_PACKAGE_PREFIX = "java/";
    private static final int MAX_GENERATED_TYPE_NAME_LENGTH = 255;
    private static final String CLASS_INIT = "<clinit>";
    static final String SUPER_PREFIX = "super$";
    private static final Collection<MethodInfo> EXCLUDED;
    private final Class<?> superClass;
    private final List<Class<?>> interfaces;
    private final ClassLoader commonLoader;
    private final boolean classOverride;
    private final String superClassName;
    private final String generatedClassName;
    private final Set<String> usedFieldNames;
    private final Set<String> abstractMethodNames;
    private final String samName;
    private final Set<MethodInfo> finalMethods;
    private final Set<MethodInfo> methodInfos;
    private boolean autoConvertibleFromFunction;
    private boolean hasExplicitFinalizer;
    private final Map<Class<?>, String> converterFields;
    private final Set<Class<?>> samReturnTypes;
    private final ClassWriter cw;
    private static final AccessControlContext GET_DECLARED_MEMBERS_ACC_CTXT;
    
    JavaAdapterBytecodeGenerator(final Class<?> superClass, final List<Class<?>> interfaces, final ClassLoader commonLoader, final boolean classOverride) throws AdaptationException {
        this.usedFieldNames = new HashSet<String>();
        this.abstractMethodNames = new HashSet<String>();
        this.finalMethods = new HashSet<MethodInfo>(JavaAdapterBytecodeGenerator.EXCLUDED);
        this.methodInfos = new HashSet<MethodInfo>();
        this.autoConvertibleFromFunction = false;
        this.hasExplicitFinalizer = false;
        this.converterFields = new LinkedHashMap<Class<?>, String>();
        this.samReturnTypes = new HashSet<Class<?>>();
        assert superClass != null && !superClass.isInterface();
        assert interfaces != null;
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.classOverride = classOverride;
        this.commonLoader = commonLoader;
        this.cw = new ClassWriter(3) {
            @Override
            protected String getCommonSuperClass(final String type1, final String type2) {
                return JavaAdapterBytecodeGenerator.this.getCommonSuperClass(type1, type2);
            }
        };
        this.superClassName = Type.getInternalName(superClass);
        this.generatedClassName = getGeneratedClassName(superClass, interfaces);
        this.cw.visit(51, 33, this.generatedClassName, null, this.superClassName, getInternalTypeNames(interfaces));
        this.generateGlobalFields();
        this.gatherMethods(superClass);
        this.gatherMethods(interfaces);
        this.samName = ((this.abstractMethodNames.size() == 1) ? this.abstractMethodNames.iterator().next() : null);
        this.generateHandleFields();
        this.generateConverterFields();
        if (classOverride) {
            this.generateClassInit();
        }
        this.generateConstructors();
        this.generateMethods();
        this.generateSuperMethods();
        if (this.hasExplicitFinalizer) {
            this.generateFinalizerMethods();
        }
        this.cw.visitEnd();
    }
    
    private void generateGlobalFields() {
        this.cw.visitField(0x12 | (this.classOverride ? 8 : 0), "global", JavaAdapterBytecodeGenerator.GLOBAL_TYPE_DESCRIPTOR, null, null).visitEnd();
        this.usedFieldNames.add("global");
    }
    
    JavaAdapterClassLoader createAdapterClassLoader() {
        return new JavaAdapterClassLoader(this.generatedClassName, this.cw.toByteArray());
    }
    
    boolean isAutoConvertibleFromFunction() {
        return this.autoConvertibleFromFunction;
    }
    
    private static String getGeneratedClassName(final Class<?> superType, final List<Class<?>> interfaces) {
        final Class<?> namingType = (superType == Object.class) ? (interfaces.isEmpty() ? Object.class : interfaces.get(0)) : superType;
        final Package pkg = namingType.getPackage();
        final String namingTypeName = Type.getInternalName(namingType);
        final StringBuilder buf = new StringBuilder();
        if (namingTypeName.startsWith("java/") || pkg == null || pkg.isSealed()) {
            buf.append("jdk/nashorn/javaadapters/").append(namingTypeName);
        }
        else {
            buf.append(namingTypeName).append("$$NashornJavaAdapter");
        }
        final Iterator<Class<?>> it = interfaces.iterator();
        if (superType == Object.class && it.hasNext()) {
            it.next();
        }
        while (it.hasNext()) {
            buf.append("$$").append(it.next().getSimpleName());
        }
        return buf.toString().substring(0, Math.min(255, buf.length()));
    }
    
    private static String[] getInternalTypeNames(final List<Class<?>> classes) {
        final int interfaceCount = classes.size();
        final String[] interfaceNames = new String[interfaceCount];
        for (int i = 0; i < interfaceCount; ++i) {
            interfaceNames[i] = Type.getInternalName(classes.get(i));
        }
        return interfaceNames;
    }
    
    private void generateHandleFields() {
        final int flags = 0x12 | (this.classOverride ? 8 : 0);
        for (final MethodInfo mi : this.methodInfos) {
            this.cw.visitField(flags, mi.methodHandleFieldName, JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR, null, null).visitEnd();
        }
    }
    
    private void generateConverterFields() {
        final int flags = 0x12 | (this.classOverride ? 8 : 0);
        for (final MethodInfo mi : this.methodInfos) {
            final Class<?> returnType = mi.type.returnType();
            if (!returnType.isPrimitive() && returnType != Object.class && returnType != String.class && !this.converterFields.containsKey(returnType)) {
                final String name = this.nextName("convert");
                this.converterFields.put(returnType, name);
                if (mi.getName().equals(this.samName)) {
                    this.samReturnTypes.add(returnType);
                }
                this.cw.visitField(flags, name, JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR, null, null).visitEnd();
            }
        }
    }
    
    private void generateClassInit() {
        final InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(8, "<clinit>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), null, null));
        mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "getClassOverrides", JavaAdapterBytecodeGenerator.GET_CLASS_INITIALIZER_DESCRIPTOR, false);
        Label initGlobal;
        if (this.samName != null) {
            final Label notAFunction = new Label();
            mv.dup();
            mv.instanceOf(JavaAdapterBytecodeGenerator.SCRIPT_FUNCTION_TYPE);
            mv.ifeq(notAFunction);
            mv.checkcast(JavaAdapterBytecodeGenerator.SCRIPT_FUNCTION_TYPE);
            for (final MethodInfo mi : this.methodInfos) {
                if (mi.getName().equals(this.samName)) {
                    mv.dup();
                    loadMethodTypeAndGetHandle(mv, mi, JavaAdapterBytecodeGenerator.GET_HANDLE_FUNCTION_DESCRIPTOR);
                }
                else {
                    mv.visitInsn(1);
                }
                mv.putstatic(this.generatedClassName, mi.methodHandleFieldName, JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR);
            }
            initGlobal = new Label();
            mv.goTo(initGlobal);
            mv.visitLabel(notAFunction);
        }
        else {
            initGlobal = null;
        }
        for (final MethodInfo mi2 : this.methodInfos) {
            mv.dup();
            mv.aconst(mi2.getName());
            loadMethodTypeAndGetHandle(mv, mi2, JavaAdapterBytecodeGenerator.GET_HANDLE_OBJECT_DESCRIPTOR);
            mv.putstatic(this.generatedClassName, mi2.methodHandleFieldName, JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        if (initGlobal != null) {
            mv.visitLabel(initGlobal);
        }
        invokeGetGlobalWithNullCheck(mv);
        mv.putstatic(this.generatedClassName, "global", JavaAdapterBytecodeGenerator.GLOBAL_TYPE_DESCRIPTOR);
        this.generateConverterInit(mv, false);
        endInitMethod(mv);
    }
    
    private void generateConverterInit(final InstructionAdapter mv, final boolean samOnly) {
        assert !this.classOverride;
        for (final Map.Entry<Class<?>, String> converterField : this.converterFields.entrySet()) {
            final Class<?> returnType = converterField.getKey();
            if (!this.classOverride) {
                mv.visitVarInsn(25, 0);
            }
            if (samOnly && !this.samReturnTypes.contains(returnType)) {
                mv.visitInsn(1);
            }
            else {
                mv.aconst(Type.getType(converterField.getKey()));
                mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "getObjectConverter", JavaAdapterBytecodeGenerator.GET_CONVERTER_METHOD_DESCRIPTOR, false);
            }
            if (this.classOverride) {
                mv.putstatic(this.generatedClassName, converterField.getValue(), JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR);
            }
            else {
                mv.putfield(this.generatedClassName, converterField.getValue(), JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR);
            }
        }
    }
    
    private static void loadMethodTypeAndGetHandle(final InstructionAdapter mv, final MethodInfo mi, final String getHandleDescriptor) {
        mv.aconst(Type.getMethodType(mi.type.generic().toMethodDescriptorString()));
        mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "getHandle", getHandleDescriptor, false);
    }
    
    private static void invokeGetGlobalWithNullCheck(final InstructionAdapter mv) {
        invokeGetGlobal(mv);
        mv.dup();
        mv.invokevirtual(JavaAdapterBytecodeGenerator.OBJECT_TYPE_NAME, "getClass", JavaAdapterBytecodeGenerator.GET_CLASS_METHOD_DESCRIPTOR, false);
        mv.pop();
    }
    
    private void generateConstructors() throws AdaptationException {
        boolean gotCtor = false;
        for (final Constructor<?> ctor : this.superClass.getDeclaredConstructors()) {
            final int modifier = ctor.getModifiers();
            if ((modifier & 0x5) != 0x0 && !isCallerSensitive(ctor)) {
                this.generateConstructors(ctor);
                gotCtor = true;
            }
        }
        if (!gotCtor) {
            throw new AdaptationException(AdaptationResult.Outcome.ERROR_NO_ACCESSIBLE_CONSTRUCTOR, this.superClass.getCanonicalName());
        }
    }
    
    private void generateConstructors(final Constructor<?> ctor) {
        if (this.classOverride) {
            this.generateDelegatingConstructor(ctor);
        }
        else {
            this.generateOverridingConstructor(ctor, false);
            if (this.samName != null) {
                if (!this.autoConvertibleFromFunction && ctor.getParameterTypes().length == 0) {
                    this.autoConvertibleFromFunction = true;
                }
                this.generateOverridingConstructor(ctor, true);
            }
        }
    }
    
    private void generateDelegatingConstructor(final Constructor<?> ctor) {
        final Type originalCtorType = Type.getType(ctor);
        final Type[] argTypes = originalCtorType.getArgumentTypes();
        final InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(0x1 | (ctor.isVarArgs() ? 128 : 0), "<init>", Type.getMethodDescriptor(originalCtorType.getReturnType(), argTypes), null, null));
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        int offset = 1;
        for (final Type argType : argTypes) {
            mv.load(offset, argType);
            offset += argType.getSize();
        }
        mv.invokespecial(this.superClassName, "<init>", originalCtorType.getDescriptor(), false);
        endInitMethod(mv);
    }
    
    private void generateOverridingConstructor(final Constructor<?> ctor, final boolean fromFunction) {
        final Type originalCtorType = Type.getType(ctor);
        final Type[] originalArgTypes = originalCtorType.getArgumentTypes();
        final int argLen = originalArgTypes.length;
        final Type[] newArgTypes = new Type[argLen + 1];
        final Type extraArgumentType = fromFunction ? JavaAdapterBytecodeGenerator.SCRIPT_FUNCTION_TYPE : JavaAdapterBytecodeGenerator.SCRIPT_OBJECT_TYPE;
        newArgTypes[argLen] = extraArgumentType;
        System.arraycopy(originalArgTypes, 0, newArgTypes, 0, argLen);
        final InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(1, "<init>", Type.getMethodDescriptor(originalCtorType.getReturnType(), newArgTypes), null, null));
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        final Class<?>[] argTypes = ctor.getParameterTypes();
        int offset = 1;
        for (int i = 0; i < argLen; ++i) {
            final Type argType = Type.getType(argTypes[i]);
            mv.load(offset, argType);
            offset += argType.getSize();
        }
        mv.invokespecial(this.superClassName, "<init>", originalCtorType.getDescriptor(), false);
        final String getHandleDescriptor = fromFunction ? JavaAdapterBytecodeGenerator.GET_HANDLE_FUNCTION_DESCRIPTOR : JavaAdapterBytecodeGenerator.GET_HANDLE_OBJECT_DESCRIPTOR;
        for (final MethodInfo mi : this.methodInfos) {
            mv.visitVarInsn(25, 0);
            if (fromFunction && !mi.getName().equals(this.samName)) {
                mv.visitInsn(1);
            }
            else {
                mv.visitVarInsn(25, offset);
                if (!fromFunction) {
                    mv.aconst(mi.getName());
                }
                loadMethodTypeAndGetHandle(mv, mi, getHandleDescriptor);
            }
            mv.putfield(this.generatedClassName, mi.methodHandleFieldName, JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        mv.visitVarInsn(25, 0);
        invokeGetGlobalWithNullCheck(mv);
        mv.putfield(this.generatedClassName, "global", JavaAdapterBytecodeGenerator.GLOBAL_TYPE_DESCRIPTOR);
        this.generateConverterInit(mv, fromFunction);
        endInitMethod(mv);
        if (!fromFunction) {
            newArgTypes[argLen] = JavaAdapterBytecodeGenerator.OBJECT_TYPE;
            final InstructionAdapter mv2 = new InstructionAdapter(this.cw.visitMethod(1, "<init>", Type.getMethodDescriptor(originalCtorType.getReturnType(), newArgTypes), null, null));
            this.generateOverridingConstructorWithObjectParam(mv2, ctor, originalCtorType.getDescriptor());
        }
    }
    
    private void generateOverridingConstructorWithObjectParam(final InstructionAdapter mv, final Constructor<?> ctor, final String ctorDescriptor) {
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        final Class<?>[] argTypes = ctor.getParameterTypes();
        int offset = 1;
        for (int i = 0; i < argTypes.length; ++i) {
            final Type argType = Type.getType(argTypes[i]);
            mv.load(offset, argType);
            offset += argType.getSize();
        }
        mv.invokespecial(this.superClassName, "<init>", ctorDescriptor, false);
        mv.visitVarInsn(25, offset);
        mv.visitInsn(1);
        mv.visitInsn(1);
        mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "getHandle", JavaAdapterBytecodeGenerator.GET_HANDLE_OBJECT_DESCRIPTOR, false);
        endInitMethod(mv);
    }
    
    private static void endInitMethod(final InstructionAdapter mv) {
        mv.visitInsn(177);
        endMethod(mv);
    }
    
    private static void endMethod(final InstructionAdapter mv) {
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }
    
    private static void invokeGetGlobal(final InstructionAdapter mv) {
        mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "getGlobal", JavaAdapterBytecodeGenerator.GET_GLOBAL_METHOD_DESCRIPTOR, false);
    }
    
    private static void invokeSetGlobal(final InstructionAdapter mv) {
        mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "setGlobal", JavaAdapterBytecodeGenerator.SET_GLOBAL_METHOD_DESCRIPTOR, false);
    }
    
    private String nextName(final String name) {
        int i = 0;
        String nextName;
        String ordinal;
        int maxNameLen;
        for (nextName = name; !this.usedFieldNames.add(nextName); nextName = ((name.length() <= maxNameLen) ? name : name.substring(0, maxNameLen)).concat(ordinal)) {
            ordinal = String.valueOf(i++);
            maxNameLen = 255 - ordinal.length();
        }
        return nextName;
    }
    
    private void generateMethods() {
        for (final MethodInfo mi : this.methodInfos) {
            this.generateMethod(mi);
        }
    }
    
    private void generateMethod(final MethodInfo mi) {
        final Method method = mi.method;
        final Class<?>[] exceptions = method.getExceptionTypes();
        final String[] exceptionNames = getExceptionNames(exceptions);
        final MethodType type = mi.type;
        final String methodDesc = type.toMethodDescriptorString();
        final String name = mi.getName();
        final Type asmType = Type.getMethodType(methodDesc);
        final Type[] asmArgTypes = asmType.getArgumentTypes();
        final InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(getAccessModifiers(method), name, methodDesc, null, exceptionNames));
        mv.visitCode();
        final Label handleDefined = new Label();
        final Class<?> returnType = type.returnType();
        final Type asmReturnType = Type.getType(returnType);
        if (this.classOverride) {
            mv.getstatic(this.generatedClassName, mi.methodHandleFieldName, JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        else {
            mv.visitVarInsn(25, 0);
            mv.getfield(this.generatedClassName, mi.methodHandleFieldName, JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        mv.visitInsn(89);
        mv.visitJumpInsn(199, handleDefined);
        if (Modifier.isAbstract(method.getModifiers())) {
            mv.anew(JavaAdapterBytecodeGenerator.UNSUPPORTED_OPERATION_TYPE);
            mv.dup();
            mv.invokespecial(JavaAdapterBytecodeGenerator.UNSUPPORTED_OPERATION_TYPE_NAME, "<init>", JavaAdapterBytecodeGenerator.VOID_NOARG_METHOD_DESCRIPTOR, false);
            mv.athrow();
        }
        else {
            mv.visitInsn(87);
            this.emitSuperCall(mv, method.getDeclaringClass(), name, methodDesc);
        }
        mv.visitLabel(handleDefined);
        if (this.classOverride) {
            mv.getstatic(this.generatedClassName, "global", JavaAdapterBytecodeGenerator.GLOBAL_TYPE_DESCRIPTOR);
        }
        else {
            mv.visitVarInsn(25, 0);
            mv.getfield(this.generatedClassName, "global", JavaAdapterBytecodeGenerator.GLOBAL_TYPE_DESCRIPTOR);
        }
        final Label setupGlobal = new Label();
        mv.visitLabel(setupGlobal);
        int nextLocalVar = 1;
        for (final Type t : asmArgTypes) {
            nextLocalVar += t.getSize();
        }
        final int currentGlobalVar = nextLocalVar++;
        final int globalsDifferVar = nextLocalVar++;
        mv.dup();
        invokeGetGlobal(mv);
        mv.dup();
        mv.visitVarInsn(58, currentGlobalVar);
        final Label globalsDiffer = new Label();
        mv.ifacmpne(globalsDiffer);
        mv.pop();
        mv.iconst(0);
        final Label invokeHandle = new Label();
        mv.goTo(invokeHandle);
        mv.visitLabel(globalsDiffer);
        invokeSetGlobal(mv);
        mv.iconst(1);
        mv.visitLabel(invokeHandle);
        mv.visitVarInsn(54, globalsDifferVar);
        int varOffset = 1;
        for (final Type t2 : asmArgTypes) {
            mv.load(varOffset, t2);
            boxStackTop(mv, t2);
            varOffset += t2.getSize();
        }
        final Label tryBlockStart = new Label();
        mv.visitLabel(tryBlockStart);
        emitInvokeExact(mv, type.generic());
        this.convertReturnValue(mv, returnType, asmReturnType);
        final Label tryBlockEnd = new Label();
        mv.visitLabel(tryBlockEnd);
        emitFinally(mv, currentGlobalVar, globalsDifferVar);
        mv.areturn(asmReturnType);
        final boolean throwableDeclared = isThrowableDeclared(exceptions);
        Label throwableHandler;
        if (!throwableDeclared) {
            throwableHandler = new Label();
            mv.visitLabel(throwableHandler);
            mv.anew(JavaAdapterBytecodeGenerator.RUNTIME_EXCEPTION_TYPE);
            mv.dupX1();
            mv.swap();
            mv.invokespecial(JavaAdapterBytecodeGenerator.RUNTIME_EXCEPTION_TYPE_NAME, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, JavaAdapterBytecodeGenerator.THROWABLE_TYPE), false);
        }
        else {
            throwableHandler = null;
        }
        final Label rethrowHandler = new Label();
        mv.visitLabel(rethrowHandler);
        emitFinally(mv, currentGlobalVar, globalsDifferVar);
        mv.athrow();
        final Label methodEnd = new Label();
        mv.visitLabel(methodEnd);
        mv.visitLocalVariable("currentGlobal", JavaAdapterBytecodeGenerator.GLOBAL_TYPE_DESCRIPTOR, null, setupGlobal, methodEnd, currentGlobalVar);
        mv.visitLocalVariable("globalsDiffer", Type.BOOLEAN_TYPE.getDescriptor(), null, setupGlobal, methodEnd, globalsDifferVar);
        if (throwableDeclared) {
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, JavaAdapterBytecodeGenerator.THROWABLE_TYPE_NAME);
            assert throwableHandler == null;
        }
        else {
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, JavaAdapterBytecodeGenerator.RUNTIME_EXCEPTION_TYPE_NAME);
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, JavaAdapterBytecodeGenerator.ERROR_TYPE_NAME);
            for (final String excName : exceptionNames) {
                mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, excName);
            }
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, throwableHandler, JavaAdapterBytecodeGenerator.THROWABLE_TYPE_NAME);
        }
        endMethod(mv);
    }
    
    private void convertReturnValue(final InstructionAdapter mv, final Class<?> returnType, final Type asmReturnType) {
        switch (asmReturnType.getSort()) {
            case 0: {
                mv.pop();
                break;
            }
            case 1: {
                JSType.TO_BOOLEAN.invoke(mv);
                break;
            }
            case 3: {
                JSType.TO_INT32.invoke(mv);
                mv.visitInsn(145);
                break;
            }
            case 4: {
                JSType.TO_INT32.invoke(mv);
                mv.visitInsn(147);
                break;
            }
            case 2: {
                mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "toCharPrimitive", JavaAdapterBytecodeGenerator.TO_CHAR_PRIMITIVE_METHOD_DESCRIPTOR, false);
                break;
            }
            case 5: {
                JSType.TO_INT32.invoke(mv);
                break;
            }
            case 7: {
                JSType.TO_LONG.invoke(mv);
                break;
            }
            case 6: {
                JSType.TO_NUMBER.invoke(mv);
                mv.visitInsn(144);
                break;
            }
            case 8: {
                JSType.TO_NUMBER.invoke(mv);
                break;
            }
            default: {
                if (asmReturnType.equals(JavaAdapterBytecodeGenerator.OBJECT_TYPE)) {
                    mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "exportReturnValue", JavaAdapterBytecodeGenerator.EXPORT_RETURN_VALUE_METHOD_DESCRIPTOR, false);
                    break;
                }
                if (asmReturnType.equals(JavaAdapterBytecodeGenerator.STRING_TYPE)) {
                    mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "toString", JavaAdapterBytecodeGenerator.TO_STRING_METHOD_DESCRIPTOR, false);
                    break;
                }
                if (this.classOverride) {
                    mv.getstatic(this.generatedClassName, this.converterFields.get(returnType), JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR);
                }
                else {
                    mv.visitVarInsn(25, 0);
                    mv.getfield(this.generatedClassName, this.converterFields.get(returnType), JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE_DESCRIPTOR);
                }
                mv.swap();
                emitInvokeExact(mv, MethodType.methodType(returnType, Object.class));
                break;
            }
        }
    }
    
    private static void emitInvokeExact(final InstructionAdapter mv, final MethodType type) {
        mv.invokevirtual(JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE.getInternalName(), "invokeExact", type.toMethodDescriptorString(), false);
    }
    
    private static void boxStackTop(final InstructionAdapter mv, final Type t) {
        switch (t.getSort()) {
            case 1: {
                invokeValueOf(mv, "Boolean", 'Z');
                break;
            }
            case 3:
            case 4:
            case 5: {
                invokeValueOf(mv, "Integer", 'I');
                break;
            }
            case 2: {
                invokeValueOf(mv, "Character", 'C');
                break;
            }
            case 6: {
                mv.visitInsn(141);
                invokeValueOf(mv, "Double", 'D');
                break;
            }
            case 7: {
                invokeValueOf(mv, "Long", 'J');
                break;
            }
            case 8: {
                invokeValueOf(mv, "Double", 'D');
                break;
            }
            case 9:
            case 11: {
                break;
            }
            case 10: {
                if (t.equals(JavaAdapterBytecodeGenerator.OBJECT_TYPE)) {
                    mv.invokestatic(JavaAdapterBytecodeGenerator.SCRIPTUTILS_TYPE_NAME, "unwrap", JavaAdapterBytecodeGenerator.UNWRAP_METHOD_DESCRIPTOR, false);
                    break;
                }
                break;
            }
            default: {
                assert false;
                break;
            }
        }
    }
    
    private static void invokeValueOf(final InstructionAdapter mv, final String boxedType, final char unboxedType) {
        mv.invokestatic("java/lang/" + boxedType, "valueOf", "(" + unboxedType + ")Ljava/lang/" + boxedType + ";", false);
    }
    
    private static void emitFinally(final InstructionAdapter mv, final int currentGlobalVar, final int globalsDifferVar) {
        mv.visitVarInsn(21, globalsDifferVar);
        final Label skip = new Label();
        mv.ifeq(skip);
        mv.visitVarInsn(25, currentGlobalVar);
        invokeSetGlobal(mv);
        mv.visitLabel(skip);
    }
    
    private static boolean isThrowableDeclared(final Class<?>[] exceptions) {
        for (final Class<?> exception : exceptions) {
            if (exception == Throwable.class) {
                return true;
            }
        }
        return false;
    }
    
    private void generateSuperMethods() {
        for (final MethodInfo mi : this.methodInfos) {
            if (!Modifier.isAbstract(mi.method.getModifiers())) {
                this.generateSuperMethod(mi);
            }
        }
    }
    
    private void generateSuperMethod(final MethodInfo mi) {
        final Method method = mi.method;
        final String methodDesc = mi.type.toMethodDescriptorString();
        final String name = mi.getName();
        final InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(getAccessModifiers(method), "super$" + name, methodDesc, null, getExceptionNames(method.getExceptionTypes())));
        mv.visitCode();
        this.emitSuperCall(mv, method.getDeclaringClass(), name, methodDesc);
        endMethod(mv);
    }
    
    private Class<?> findInvokespecialOwnerFor(final Class<?> cl) {
        assert Modifier.isInterface(cl.getModifiers()) : cl + " is not an interface";
        if (cl.isAssignableFrom(this.superClass)) {
            return this.superClass;
        }
        for (final Class<?> iface : this.interfaces) {
            if (cl.isAssignableFrom(iface)) {
                return iface;
            }
        }
        throw new AssertionError((Object)("can't find the class/interface that extends " + cl));
    }
    
    private void emitSuperCall(final InstructionAdapter mv, final Class<?> owner, final String name, final String methodDesc) {
        mv.visitVarInsn(25, 0);
        int nextParam = 1;
        final Type methodType = Type.getMethodType(methodDesc);
        for (final Type t : methodType.getArgumentTypes()) {
            mv.load(nextParam, t);
            nextParam += t.getSize();
        }
        if (Modifier.isInterface(owner.getModifiers())) {
            mv.invokespecial(Type.getInternalName(this.findInvokespecialOwnerFor(owner)), name, methodDesc, false);
        }
        else {
            mv.invokespecial(this.superClassName, name, methodDesc, false);
        }
        mv.areturn(methodType.getReturnType());
    }
    
    private void generateFinalizerMethods() {
        final String finalizerDelegateName = this.nextName("access$");
        this.generateFinalizerDelegate(finalizerDelegateName);
        this.generateFinalizerOverride(finalizerDelegateName);
    }
    
    private void generateFinalizerDelegate(final String finalizerDelegateName) {
        final InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(10, finalizerDelegateName, Type.getMethodDescriptor(Type.VOID_TYPE, JavaAdapterBytecodeGenerator.OBJECT_TYPE), null, null));
        mv.visitVarInsn(25, 0);
        mv.checkcast(Type.getType(this.generatedClassName));
        mv.invokespecial(this.superClassName, "finalize", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), false);
        mv.visitInsn(177);
        endMethod(mv);
    }
    
    private void generateFinalizerOverride(final String finalizerDelegateName) {
        final InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(1, "finalize", JavaAdapterBytecodeGenerator.VOID_NOARG_METHOD_DESCRIPTOR, null, null));
        mv.aconst(new Handle(6, this.generatedClassName, finalizerDelegateName, Type.getMethodDescriptor(Type.VOID_TYPE, JavaAdapterBytecodeGenerator.OBJECT_TYPE)));
        mv.visitVarInsn(25, 0);
        mv.invokestatic(JavaAdapterBytecodeGenerator.SERVICES_CLASS_TYPE_NAME, "invokeNoPermissions", Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE, JavaAdapterBytecodeGenerator.OBJECT_TYPE), false);
        mv.visitInsn(177);
        endMethod(mv);
    }
    
    private static String[] getExceptionNames(final Class<?>[] exceptions) {
        final String[] exceptionNames = new String[exceptions.length];
        for (int i = 0; i < exceptions.length; ++i) {
            exceptionNames[i] = Type.getInternalName(exceptions[i]);
        }
        return exceptionNames;
    }
    
    private static int getAccessModifiers(final Method method) {
        return 0x1 | (method.isVarArgs() ? 128 : 0);
    }
    
    private void gatherMethods(final Class<?> type) throws AdaptationException {
        if (Modifier.isPublic(type.getModifiers())) {
            final Method[] array;
            final Method[] typeMethods = array = (type.isInterface() ? type.getMethods() : type.getDeclaredMethods());
            for (final Method typeMethod : array) {
                final String name = typeMethod.getName();
                if (!name.startsWith("super$")) {
                    final int m = typeMethod.getModifiers();
                    if (!Modifier.isStatic(m)) {
                        if (Modifier.isPublic(m) || Modifier.isProtected(m)) {
                            if (name.equals("finalize") && typeMethod.getParameterCount() == 0) {
                                if (type != Object.class) {
                                    this.hasExplicitFinalizer = true;
                                    if (Modifier.isFinal(m)) {
                                        throw new AdaptationException(AdaptationResult.Outcome.ERROR_FINAL_FINALIZER, type.getCanonicalName());
                                    }
                                }
                            }
                            else {
                                final MethodInfo mi = new MethodInfo(typeMethod);
                                if (Modifier.isFinal(m) || isCallerSensitive(typeMethod)) {
                                    this.finalMethods.add(mi);
                                }
                                else if (!this.finalMethods.contains(mi) && this.methodInfos.add(mi)) {
                                    if (Modifier.isAbstract(m)) {
                                        this.abstractMethodNames.add(mi.getName());
                                    }
                                    mi.setIsCanonical(this);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!type.isInterface()) {
            final Class<?> superType = type.getSuperclass();
            if (superType != null) {
                this.gatherMethods(superType);
            }
            for (final Class<?> itf : type.getInterfaces()) {
                this.gatherMethods(itf);
            }
        }
    }
    
    private void gatherMethods(final List<Class<?>> classes) throws AdaptationException {
        for (final Class<?> c : classes) {
            this.gatherMethods(c);
        }
    }
    
    private static Collection<MethodInfo> getExcludedMethods() {
        return AccessController.doPrivileged((PrivilegedAction<Collection<MethodInfo>>)new PrivilegedAction<Collection<MethodInfo>>() {
            @Override
            public Collection<MethodInfo> run() {
                try {
                    return Arrays.asList(new MethodInfo((Class)Object.class, "finalize", new Class[0]), new MethodInfo((Class)Object.class, "clone", new Class[0]));
                }
                catch (NoSuchMethodException e) {
                    throw new AssertionError((Object)e);
                }
            }
        }, JavaAdapterBytecodeGenerator.GET_DECLARED_MEMBERS_ACC_CTXT);
    }
    
    private String getCommonSuperClass(final String type1, final String type2) {
        try {
            final Class<?> c1 = Class.forName(type1.replace('/', '.'), false, this.commonLoader);
            final Class<?> c2 = Class.forName(type2.replace('/', '.'), false, this.commonLoader);
            if (c1.isAssignableFrom(c2)) {
                return type1;
            }
            if (c2.isAssignableFrom(c1)) {
                return type2;
            }
            if (c1.isInterface() || c2.isInterface()) {
                return JavaAdapterBytecodeGenerator.OBJECT_TYPE_NAME;
            }
            return assignableSuperClass(c1, c2).getName().replace('.', '/');
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static Class<?> assignableSuperClass(final Class<?> c1, final Class<?> c2) {
        final Class<?> superClass = c1.getSuperclass();
        return superClass.isAssignableFrom(c2) ? superClass : assignableSuperClass(superClass, c2);
    }
    
    private static boolean isCallerSensitive(final AccessibleObject e) {
        return e.isAnnotationPresent((Class<? extends Annotation>)CallerSensitive.class);
    }
    
    static {
        SCRIPTUTILS_TYPE = Type.getType(ScriptUtils.class);
        OBJECT_TYPE = Type.getType(Object.class);
        CLASS_TYPE = Type.getType(Class.class);
        OBJECT_TYPE_NAME = JavaAdapterBytecodeGenerator.OBJECT_TYPE.getInternalName();
        SCRIPTUTILS_TYPE_NAME = JavaAdapterBytecodeGenerator.SCRIPTUTILS_TYPE.getInternalName();
        GLOBAL_TYPE_DESCRIPTOR = JavaAdapterBytecodeGenerator.OBJECT_TYPE.getDescriptor();
        SET_GLOBAL_METHOD_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE, JavaAdapterBytecodeGenerator.OBJECT_TYPE);
        VOID_NOARG_METHOD_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]);
        SCRIPT_OBJECT_TYPE = Type.getType(ScriptObject.class);
        SCRIPT_FUNCTION_TYPE = Type.getType(ScriptFunction.class);
        STRING_TYPE = Type.getType(String.class);
        METHOD_TYPE_TYPE = Type.getType(MethodType.class);
        METHOD_HANDLE_TYPE = Type.getType(MethodHandle.class);
        GET_HANDLE_OBJECT_DESCRIPTOR = Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE, JavaAdapterBytecodeGenerator.OBJECT_TYPE, JavaAdapterBytecodeGenerator.STRING_TYPE, JavaAdapterBytecodeGenerator.METHOD_TYPE_TYPE);
        GET_HANDLE_FUNCTION_DESCRIPTOR = Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE, JavaAdapterBytecodeGenerator.SCRIPT_FUNCTION_TYPE, JavaAdapterBytecodeGenerator.METHOD_TYPE_TYPE);
        GET_CLASS_INITIALIZER_DESCRIPTOR = Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.OBJECT_TYPE, new Type[0]);
        RUNTIME_EXCEPTION_TYPE = Type.getType(RuntimeException.class);
        THROWABLE_TYPE = Type.getType(Throwable.class);
        UNSUPPORTED_OPERATION_TYPE = Type.getType(UnsupportedOperationException.class);
        SERVICES_CLASS_TYPE_NAME = Type.getInternalName(JavaAdapterServices.class);
        RUNTIME_EXCEPTION_TYPE_NAME = JavaAdapterBytecodeGenerator.RUNTIME_EXCEPTION_TYPE.getInternalName();
        ERROR_TYPE_NAME = Type.getInternalName(Error.class);
        THROWABLE_TYPE_NAME = JavaAdapterBytecodeGenerator.THROWABLE_TYPE.getInternalName();
        UNSUPPORTED_OPERATION_TYPE_NAME = JavaAdapterBytecodeGenerator.UNSUPPORTED_OPERATION_TYPE.getInternalName();
        METHOD_HANDLE_TYPE_DESCRIPTOR = JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE.getDescriptor();
        GET_GLOBAL_METHOD_DESCRIPTOR = Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.OBJECT_TYPE, new Type[0]);
        GET_CLASS_METHOD_DESCRIPTOR = Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.CLASS_TYPE, new Type[0]);
        EXPORT_RETURN_VALUE_METHOD_DESCRIPTOR = Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.OBJECT_TYPE, JavaAdapterBytecodeGenerator.OBJECT_TYPE);
        UNWRAP_METHOD_DESCRIPTOR = Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.OBJECT_TYPE, JavaAdapterBytecodeGenerator.OBJECT_TYPE);
        GET_CONVERTER_METHOD_DESCRIPTOR = Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.METHOD_HANDLE_TYPE, JavaAdapterBytecodeGenerator.CLASS_TYPE);
        TO_CHAR_PRIMITIVE_METHOD_DESCRIPTOR = Type.getMethodDescriptor(Type.CHAR_TYPE, JavaAdapterBytecodeGenerator.OBJECT_TYPE);
        TO_STRING_METHOD_DESCRIPTOR = Type.getMethodDescriptor(JavaAdapterBytecodeGenerator.STRING_TYPE, JavaAdapterBytecodeGenerator.OBJECT_TYPE);
        EXCLUDED = getExcludedMethods();
        GET_DECLARED_MEMBERS_ACC_CTXT = ClassAndLoader.createPermAccCtxt("accessDeclaredMembers");
    }
    
    private static class MethodInfo
    {
        private final Method method;
        private final MethodType type;
        private String methodHandleFieldName;
        
        private MethodInfo(final Class<?> clazz, final String name, final Class<?>... argTypes) throws NoSuchMethodException {
            this(clazz.getDeclaredMethod(name, argTypes));
        }
        
        private MethodInfo(final Method method) {
            this.method = method;
            this.type = Lookup.MH.type(method.getReturnType(), method.getParameterTypes());
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof MethodInfo && this.equals((MethodInfo)obj);
        }
        
        private boolean equals(final MethodInfo other) {
            return this.getName().equals(other.getName()) && this.type.equals((Object)other.type);
        }
        
        String getName() {
            return this.method.getName();
        }
        
        @Override
        public int hashCode() {
            return this.getName().hashCode() ^ this.type.hashCode();
        }
        
        void setIsCanonical(final JavaAdapterBytecodeGenerator self) {
            this.methodHandleFieldName = self.nextName(this.getName());
        }
    }
}
