// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.AllocationStrategy;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import java.util.EnumSet;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.util.LinkedList;
import jdk.nashorn.internal.runtime.FunctionScope;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.Iterator;
import java.util.List;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;

@Logger(name = "fields")
public final class ObjectClassGenerator implements Loggable
{
    private static final MethodHandle IS_TYPE_GUARD;
    private static final String SCOPE_MARKER = "P";
    static final int FIELD_PADDING = 4;
    private final DebugLogger log;
    private static final Type[] FIELD_TYPES_OBJECT;
    private static final Type[] FIELD_TYPES_DUAL;
    public static final Type PRIMITIVE_FIELD_TYPE;
    private static final MethodHandle GET_DIFFERENT;
    private static final MethodHandle GET_DIFFERENT_UNDEFINED;
    private static boolean initialized;
    private final Context context;
    private final boolean dualFields;
    public static final MethodHandle PACK_DOUBLE;
    public static final MethodHandle UNPACK_DOUBLE;
    
    public ObjectClassGenerator(final Context context, final boolean dualFields) {
        this.context = context;
        this.dualFields = dualFields;
        assert context != null;
        this.log = this.initLogger(context);
        if (!ObjectClassGenerator.initialized) {
            ObjectClassGenerator.initialized = true;
            if (!dualFields) {
                this.log.warning("Running with object fields only - this is a deprecated configuration.");
            }
        }
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    @Override
    public DebugLogger initLogger(final Context ctxt) {
        return ctxt.getLogger(this.getClass());
    }
    
    public static long pack(final Number n) {
        if (n instanceof Integer) {
            return n.intValue();
        }
        if (n instanceof Long) {
            return n.longValue();
        }
        if (n instanceof Double) {
            return Double.doubleToRawLongBits(n.doubleValue());
        }
        throw new AssertionError((Object)("cannot pack" + n));
    }
    
    private static String getPrefixName(final boolean dualFields) {
        return dualFields ? CompilerConstants.JS_OBJECT_DUAL_FIELD_PREFIX.symbolName() : CompilerConstants.JS_OBJECT_SINGLE_FIELD_PREFIX.symbolName();
    }
    
    private static String getPrefixName(final String className) {
        if (className.startsWith(CompilerConstants.JS_OBJECT_DUAL_FIELD_PREFIX.symbolName())) {
            return getPrefixName(true);
        }
        if (className.startsWith(CompilerConstants.JS_OBJECT_SINGLE_FIELD_PREFIX.symbolName())) {
            return getPrefixName(false);
        }
        throw new AssertionError((Object)("Not a structure class: " + className));
    }
    
    public static String getClassName(final int fieldCount, final boolean dualFields) {
        final String prefix = getPrefixName(dualFields);
        return (fieldCount != 0) ? ("jdk/nashorn/internal/scripts/" + prefix + fieldCount) : ("jdk/nashorn/internal/scripts/" + prefix);
    }
    
    public static String getClassName(final int fieldCount, final int paramCount, final boolean dualFields) {
        return "jdk/nashorn/internal/scripts/" + getPrefixName(dualFields) + fieldCount + "P" + paramCount;
    }
    
    public static int getFieldCount(final Class<?> clazz) {
        final String name = clazz.getSimpleName();
        final String prefix = getPrefixName(name);
        if (prefix.equals(name)) {
            return 0;
        }
        final int scopeMarker = name.indexOf("P");
        return Integer.parseInt((scopeMarker == -1) ? name.substring(prefix.length()) : name.substring(prefix.length(), scopeMarker));
    }
    
    public static String getFieldName(final int fieldIndex, final Type type) {
        return type.getDescriptor().substring(0, 1) + fieldIndex;
    }
    
    private void initializeToUndefined(final MethodEmitter init, final String className, final List<String> fieldNames) {
        if (this.dualFields) {
            return;
        }
        if (fieldNames.isEmpty()) {
            return;
        }
        init.load(Type.OBJECT, CompilerConstants.JAVA_THIS.slot());
        init.loadUndefined(Type.OBJECT);
        final Iterator<String> iter = fieldNames.iterator();
        while (iter.hasNext()) {
            final String fieldName = iter.next();
            if (iter.hasNext()) {
                init.dup2();
            }
            init.putField(className, fieldName, Type.OBJECT.getDescriptor());
        }
    }
    
    public byte[] generate(final String descriptor) {
        final String[] counts = descriptor.split("P");
        final int fieldCount = Integer.valueOf(counts[0]);
        if (counts.length == 1) {
            return this.generate(fieldCount);
        }
        final int paramCount = Integer.valueOf(counts[1]);
        return this.generate(fieldCount, paramCount);
    }
    
    public byte[] generate(final int fieldCount) {
        final String className = getClassName(fieldCount, this.dualFields);
        final String superName = CompilerConstants.className(ScriptObject.class);
        final ClassEmitter classEmitter = this.newClassEmitter(className, superName);
        this.addFields(classEmitter, fieldCount);
        final MethodEmitter init = newInitMethod(classEmitter);
        init.returnVoid();
        init.end();
        final MethodEmitter initWithSpillArrays = newInitWithSpillArraysMethod(classEmitter, ScriptObject.class);
        initWithSpillArrays.returnVoid();
        initWithSpillArrays.end();
        newEmptyInit(className, classEmitter);
        newAllocate(className, classEmitter);
        return this.toByteArray(className, classEmitter);
    }
    
    public byte[] generate(final int fieldCount, final int paramCount) {
        final String className = getClassName(fieldCount, paramCount, this.dualFields);
        final String superName = CompilerConstants.className(FunctionScope.class);
        final ClassEmitter classEmitter = this.newClassEmitter(className, superName);
        final List<String> initFields = this.addFields(classEmitter, fieldCount);
        final MethodEmitter init = newInitScopeMethod(classEmitter);
        this.initializeToUndefined(init, className, initFields);
        init.returnVoid();
        init.end();
        final MethodEmitter initWithSpillArrays = newInitWithSpillArraysMethod(classEmitter, FunctionScope.class);
        this.initializeToUndefined(initWithSpillArrays, className, initFields);
        initWithSpillArrays.returnVoid();
        initWithSpillArrays.end();
        final MethodEmitter initWithArguments = newInitScopeWithArgumentsMethod(classEmitter);
        this.initializeToUndefined(initWithArguments, className, initFields);
        initWithArguments.returnVoid();
        initWithArguments.end();
        return this.toByteArray(className, classEmitter);
    }
    
    private List<String> addFields(final ClassEmitter classEmitter, final int fieldCount) {
        final List<String> initFields = new LinkedList<String>();
        final Type[] fieldTypes = this.dualFields ? ObjectClassGenerator.FIELD_TYPES_DUAL : ObjectClassGenerator.FIELD_TYPES_OBJECT;
        for (int i = 0; i < fieldCount; ++i) {
            for (final Type type : fieldTypes) {
                final String fieldName = getFieldName(i, type);
                classEmitter.field(fieldName, type.getTypeClass());
                if (type == Type.OBJECT) {
                    initFields.add(fieldName);
                }
            }
        }
        return initFields;
    }
    
    private ClassEmitter newClassEmitter(final String className, final String superName) {
        final ClassEmitter classEmitter = new ClassEmitter(this.context, className, superName, new String[0]);
        classEmitter.begin();
        return classEmitter;
    }
    
    private static MethodEmitter newInitMethod(final ClassEmitter classEmitter) {
        final MethodEmitter init = classEmitter.init(PropertyMap.class);
        init.begin();
        init.load(Type.OBJECT, CompilerConstants.JAVA_THIS.slot());
        init.load(Type.OBJECT, CompilerConstants.INIT_MAP.slot());
        init.invoke(CompilerConstants.constructorNoLookup(ScriptObject.class, PropertyMap.class));
        return init;
    }
    
    private static MethodEmitter newInitWithSpillArraysMethod(final ClassEmitter classEmitter, final Class<?> superClass) {
        final MethodEmitter init = classEmitter.init(PropertyMap.class, long[].class, Object[].class);
        init.begin();
        init.load(Type.OBJECT, CompilerConstants.JAVA_THIS.slot());
        init.load(Type.OBJECT, CompilerConstants.INIT_MAP.slot());
        init.load(Type.LONG_ARRAY, 2);
        init.load(Type.OBJECT_ARRAY, 3);
        init.invoke(CompilerConstants.constructorNoLookup(superClass, PropertyMap.class, long[].class, Object[].class));
        return init;
    }
    
    private static MethodEmitter newInitScopeMethod(final ClassEmitter classEmitter) {
        final MethodEmitter init = classEmitter.init(PropertyMap.class, ScriptObject.class);
        init.begin();
        init.load(Type.OBJECT, CompilerConstants.JAVA_THIS.slot());
        init.load(Type.OBJECT, CompilerConstants.INIT_MAP.slot());
        init.load(Type.OBJECT, CompilerConstants.INIT_SCOPE.slot());
        init.invoke(CompilerConstants.constructorNoLookup(FunctionScope.class, PropertyMap.class, ScriptObject.class));
        return init;
    }
    
    private static MethodEmitter newInitScopeWithArgumentsMethod(final ClassEmitter classEmitter) {
        final MethodEmitter init = classEmitter.init(PropertyMap.class, ScriptObject.class, ScriptObject.class);
        init.begin();
        init.load(Type.OBJECT, CompilerConstants.JAVA_THIS.slot());
        init.load(Type.OBJECT, CompilerConstants.INIT_MAP.slot());
        init.load(Type.OBJECT, CompilerConstants.INIT_SCOPE.slot());
        init.load(Type.OBJECT, CompilerConstants.INIT_ARGUMENTS.slot());
        init.invoke(CompilerConstants.constructorNoLookup(FunctionScope.class, PropertyMap.class, ScriptObject.class, ScriptObject.class));
        return init;
    }
    
    private static void newEmptyInit(final String className, final ClassEmitter classEmitter) {
        final MethodEmitter emptyInit = classEmitter.init();
        emptyInit.begin();
        emptyInit.load(Type.OBJECT, CompilerConstants.JAVA_THIS.slot());
        emptyInit.loadNull();
        emptyInit.invoke(CompilerConstants.constructorNoLookup(className, PropertyMap.class));
        emptyInit.returnVoid();
        emptyInit.end();
    }
    
    private static void newAllocate(final String className, final ClassEmitter classEmitter) {
        final MethodEmitter allocate = classEmitter.method(EnumSet.of(ClassEmitter.Flag.PUBLIC, ClassEmitter.Flag.STATIC), CompilerConstants.ALLOCATE.symbolName(), ScriptObject.class, PropertyMap.class);
        allocate.begin();
        allocate._new(className, Type.typeFor(ScriptObject.class));
        allocate.dup();
        allocate.load(Type.typeFor(PropertyMap.class), 0);
        allocate.invoke(CompilerConstants.constructorNoLookup(className, PropertyMap.class));
        allocate._return();
        allocate.end();
    }
    
    private byte[] toByteArray(final String className, final ClassEmitter classEmitter) {
        classEmitter.end();
        final byte[] code = classEmitter.toByteArray();
        final ScriptEnvironment env = this.context.getEnv();
        DumpBytecode.dumpBytecode(env, this.log, code, className);
        if (env._verify_code) {
            this.context.verify(code);
        }
        return code;
    }
    
    private static Object getDifferent(final Object receiver, final Class<?> forType, final MethodHandle primitiveGetter, final MethodHandle objectGetter, final int programPoint) {
        final MethodHandle sameTypeGetter = getterForType(forType, primitiveGetter, objectGetter);
        final MethodHandle mh = Lookup.MH.asType(sameTypeGetter, sameTypeGetter.type().changeReturnType((Class<?>)Object.class));
        try {
            final Object value = mh.invokeExact(receiver);
            throw new UnwarrantedOptimismException(value, programPoint);
        }
        catch (Error | RuntimeException error) {
            final Throwable t;
            final Throwable e = t;
            throw e;
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    
    private static Object getDifferentUndefined(final int programPoint) {
        throw new UnwarrantedOptimismException(Undefined.getUndefined(), programPoint);
    }
    
    private static MethodHandle getterForType(final Class<?> forType, final MethodHandle primitiveGetter, final MethodHandle objectGetter) {
        switch (JSType.getAccessorTypeIndex(forType)) {
            case 0: {
                return Lookup.MH.explicitCastArguments(primitiveGetter, primitiveGetter.type().changeReturnType((Class<?>)Integer.TYPE));
            }
            case 1: {
                return Lookup.MH.filterReturnValue(primitiveGetter, ObjectClassGenerator.UNPACK_DOUBLE);
            }
            case 2: {
                return objectGetter;
            }
            default: {
                throw new AssertionError(forType);
            }
        }
    }
    
    private static MethodHandle createGetterInner(final Class<?> forType, final Class<?> type, final MethodHandle primitiveGetter, final MethodHandle objectGetter, final List<MethodHandle> converters, final int programPoint) {
        final int fti = (forType == null) ? -1 : JSType.getAccessorTypeIndex(forType);
        final int ti = JSType.getAccessorTypeIndex(type);
        final boolean isOptimistic = converters == JSType.CONVERT_OBJECT_OPTIMISTIC;
        final boolean isPrimitiveStorage = forType != null && forType.isPrimitive();
        final MethodHandle getter = (primitiveGetter == null) ? objectGetter : (isPrimitiveStorage ? primitiveGetter : objectGetter);
        if (forType == null) {
            if (!isOptimistic) {
                return Lookup.MH.dropArguments(JSType.GET_UNDEFINED.get(ti), 0, Object.class);
            }
            if (ti == 2) {
                return Lookup.MH.dropArguments(JSType.GET_UNDEFINED.get(2), 0, Object.class);
            }
            return Lookup.MH.asType(Lookup.MH.dropArguments(Lookup.MH.insertArguments(ObjectClassGenerator.GET_DIFFERENT_UNDEFINED, 0, programPoint), 0, Object.class), getter.type().changeReturnType(type));
        }
        else {
            assert forType == Object.class : forType;
            if (isOptimistic) {
                if (fti < ti) {
                    assert fti != -1;
                    final MethodHandle tgetter = getterForType(forType, primitiveGetter, objectGetter);
                    return Lookup.MH.asType(tgetter, tgetter.type().changeReturnType(type));
                }
                else {
                    if (fti == ti) {
                        return getterForType(forType, primitiveGetter, objectGetter);
                    }
                    assert fti > ti;
                    if (fti == 2) {
                        return Lookup.MH.filterReturnValue(objectGetter, Lookup.MH.insertArguments(converters.get(ti), 1, programPoint));
                    }
                    return Lookup.MH.asType(Lookup.MH.filterArguments(objectGetter, 0, Lookup.MH.insertArguments(ObjectClassGenerator.GET_DIFFERENT, 1, forType, primitiveGetter, objectGetter, programPoint)), objectGetter.type().changeReturnType(type));
                }
            }
            else {
                assert !isOptimistic;
                final MethodHandle tgetter = getterForType(forType, primitiveGetter, objectGetter);
                if (fti == 2) {
                    if (fti != ti) {
                        return Lookup.MH.filterReturnValue(tgetter, JSType.CONVERT_OBJECT.get(ti));
                    }
                    return tgetter;
                }
                else {
                    assert primitiveGetter != null;
                    final MethodType tgetterType = tgetter.type();
                    switch (fti) {
                        case 0: {
                            return Lookup.MH.asType(tgetter, tgetterType.changeReturnType(type));
                        }
                        case 1: {
                            switch (ti) {
                                case 0: {
                                    return Lookup.MH.filterReturnValue(tgetter, JSType.TO_INT32_D.methodHandle);
                                }
                                case 1: {
                                    assert tgetterType.returnType() == Double.TYPE;
                                    return tgetter;
                                }
                                default: {
                                    return Lookup.MH.asType(tgetter, tgetterType.changeReturnType((Class<?>)Object.class));
                                }
                            }
                            break;
                        }
                        default: {
                            throw new UnsupportedOperationException(forType + "=>" + type);
                        }
                    }
                }
            }
        }
    }
    
    public static MethodHandle createGetter(final Class<?> forType, final Class<?> type, final MethodHandle primitiveGetter, final MethodHandle objectGetter, final int programPoint) {
        return createGetterInner(forType, type, primitiveGetter, objectGetter, UnwarrantedOptimismException.isValid(programPoint) ? JSType.CONVERT_OBJECT_OPTIMISTIC : JSType.CONVERT_OBJECT, programPoint);
    }
    
    public static MethodHandle createSetter(final Class<?> forType, final Class<?> type, final MethodHandle primitiveSetter, final MethodHandle objectSetter) {
        assert forType != null;
        final int fti = JSType.getAccessorTypeIndex(forType);
        final int ti = JSType.getAccessorTypeIndex(type);
        if (fti == 2 || primitiveSetter == null) {
            if (ti == 2) {
                return objectSetter;
            }
            return Lookup.MH.asType(objectSetter, objectSetter.type().changeParameterType(1, type));
        }
        else {
            final MethodType pmt = primitiveSetter.type();
            switch (fti) {
                case 0: {
                    switch (ti) {
                        case 0: {
                            return Lookup.MH.asType(primitiveSetter, pmt.changeParameterType(1, (Class<?>)Integer.TYPE));
                        }
                        case 1: {
                            return Lookup.MH.filterArguments(primitiveSetter, 1, ObjectClassGenerator.PACK_DOUBLE);
                        }
                        default: {
                            return objectSetter;
                        }
                    }
                    break;
                }
                case 1: {
                    if (ti == 2) {
                        return objectSetter;
                    }
                    return Lookup.MH.asType(Lookup.MH.filterArguments(primitiveSetter, 1, ObjectClassGenerator.PACK_DOUBLE), pmt.changeParameterType(1, type));
                }
                default: {
                    throw new UnsupportedOperationException(forType + "=>" + type);
                }
            }
        }
    }
    
    private static boolean isType(final Class<?> boxedForType, final Object x) {
        return x != null && x.getClass() == boxedForType;
    }
    
    private static Class<? extends Number> getBoxedType(final Class<?> forType) {
        if (forType == Integer.TYPE) {
            return Integer.class;
        }
        if (forType == Long.TYPE) {
            return Long.class;
        }
        if (forType == Double.TYPE) {
            return Double.class;
        }
        assert false;
        return null;
    }
    
    public static MethodHandle createGuardBoxedPrimitiveSetter(final Class<?> forType, final MethodHandle primitiveSetter, final MethodHandle objectSetter) {
        final Class<? extends Number> boxedForType = getBoxedType(forType);
        return Lookup.MH.guardWithTest(Lookup.MH.insertArguments(Lookup.MH.dropArguments(ObjectClassGenerator.IS_TYPE_GUARD, 1, Object.class), 0, boxedForType), Lookup.MH.asType(primitiveSetter, objectSetter.type()), objectSetter);
    }
    
    static int getPaddedFieldCount(final int count) {
        return count / 4 * 4 + 4;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ObjectClassGenerator.class, name, Lookup.MH.type(rtype, types));
    }
    
    static AllocationStrategy createAllocationStrategy(final int thisProperties, final boolean dualFields) {
        final int paddedFieldCount = getPaddedFieldCount(thisProperties);
        return new AllocationStrategy(paddedFieldCount, dualFields);
    }
    
    static {
        IS_TYPE_GUARD = findOwnMH("isType", Boolean.TYPE, Class.class, Object.class);
        FIELD_TYPES_OBJECT = new Type[] { Type.OBJECT };
        FIELD_TYPES_DUAL = new Type[] { Type.LONG, Type.OBJECT };
        PRIMITIVE_FIELD_TYPE = Type.LONG;
        GET_DIFFERENT = findOwnMH("getDifferent", Object.class, Object.class, Class.class, MethodHandle.class, MethodHandle.class, Integer.TYPE);
        GET_DIFFERENT_UNDEFINED = findOwnMH("getDifferentUndefined", Object.class, Integer.TYPE);
        ObjectClassGenerator.initialized = false;
        PACK_DOUBLE = Lookup.MH.explicitCastArguments(Lookup.MH.findStatic(MethodHandles.publicLookup(), Double.class, "doubleToRawLongBits", Lookup.MH.type(Long.TYPE, Double.TYPE)), Lookup.MH.type(Long.TYPE, Double.TYPE));
        UNPACK_DOUBLE = Lookup.MH.findStatic(MethodHandles.publicLookup(), Double.class, "longBitsToDouble", Lookup.MH.type(Double.TYPE, Long.TYPE));
    }
}
