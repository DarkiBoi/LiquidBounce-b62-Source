// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.Iterator;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodHandles;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.codegen.types.Type;
import java.util.HashSet;
import java.util.Set;

public enum CompilerConstants
{
    __FILE__, 
    __DIR__, 
    __LINE__, 
    INIT("<init>"), 
    CLINIT("<clinit>"), 
    EVAL("eval"), 
    SOURCE("source", (Class<?>)Source.class), 
    CONSTANTS("constants", (Class<?>)Object[].class), 
    STRICT_MODE("strictMode", (Class<?>)Boolean.TYPE), 
    DEFAULT_SCRIPT_NAME("Script"), 
    ANON_FUNCTION_PREFIX("L:"), 
    NESTED_FUNCTION_SEPARATOR("#"), 
    ID_FUNCTION_SEPARATOR("-"), 
    PROGRAM(":program"), 
    CREATE_PROGRAM_FUNCTION(":createProgramFunction"), 
    THIS("this", (Class<?>)Object.class), 
    THIS_DEBUGGER(":this"), 
    SCOPE(":scope", (Class<?>)ScriptObject.class, 2), 
    RETURN(":return"), 
    CALLEE(":callee", (Class<?>)ScriptFunction.class), 
    VARARGS(":varargs", (Class<?>)Object[].class), 
    ARGUMENTS_VAR("arguments", (Class<?>)Object.class), 
    ARGUMENTS(":arguments", (Class<?>)ScriptObject.class), 
    EXPLODED_ARGUMENT_PREFIX(":xarg"), 
    ITERATOR_PREFIX(":i", (Class<?>)Iterator.class), 
    SWITCH_TAG_PREFIX(":s"), 
    EXCEPTION_PREFIX(":e", (Class<?>)Throwable.class), 
    QUICK_PREFIX(":q"), 
    TEMP_PREFIX(":t"), 
    LITERAL_PREFIX(":l"), 
    REGEX_PREFIX(":r"), 
    JAVA_THIS((String)null, 0), 
    INIT_MAP((String)null, 1), 
    INIT_SCOPE((String)null, 2), 
    INIT_ARGUMENTS((String)null, 3), 
    JS_OBJECT_DUAL_FIELD_PREFIX("JD"), 
    JS_OBJECT_SINGLE_FIELD_PREFIX("JO"), 
    ALLOCATE("allocate"), 
    SPLIT_PREFIX(":split"), 
    SPLIT_ARRAY_ARG(":split_array", 3), 
    GET_STRING(":getString"), 
    GET_MAP(":getMap"), 
    SET_MAP(":setMap"), 
    GET_ARRAY_PREFIX(":get"), 
    GET_ARRAY_SUFFIX("$array");
    
    private static Set<String> symbolNames;
    private static final String INTERNAL_METHOD_PREFIX = ":";
    private final String symbolName;
    private final Class<?> type;
    private final int slot;
    
    private CompilerConstants() {
        this.symbolName = this.name();
        this.type = null;
        this.slot = -1;
    }
    
    private CompilerConstants(final String symbolName) {
        this(symbolName, -1);
    }
    
    private CompilerConstants(final String symbolName, final int slot) {
        this(symbolName, null, slot);
    }
    
    private CompilerConstants(final String symbolName, final Class<?> type) {
        this(symbolName, type, -1);
    }
    
    private CompilerConstants(final String symbolName, final Class<?> type, final int slot) {
        this.symbolName = symbolName;
        this.type = type;
        this.slot = slot;
    }
    
    public static boolean isCompilerConstant(final String name) {
        ensureSymbolNames();
        return CompilerConstants.symbolNames.contains(name);
    }
    
    private static void ensureSymbolNames() {
        if (CompilerConstants.symbolNames == null) {
            CompilerConstants.symbolNames = new HashSet<String>();
            for (final CompilerConstants cc : values()) {
                CompilerConstants.symbolNames.add(cc.symbolName);
            }
        }
    }
    
    public final String symbolName() {
        return this.symbolName;
    }
    
    public final Class<?> type() {
        return this.type;
    }
    
    public final int slot() {
        return this.slot;
    }
    
    public final String descriptor() {
        assert this.type != null : " asking for descriptor of typeless constant";
        return typeDescriptor(this.type);
    }
    
    public static String className(final Class<?> type) {
        return Type.getInternalName(type);
    }
    
    public static String methodDescriptor(final Class<?> rtype, final Class<?>... ptypes) {
        return Type.getMethodDescriptor(rtype, ptypes);
    }
    
    public static String typeDescriptor(final Class<?> clazz) {
        return Type.typeFor(clazz).getDescriptor();
    }
    
    public static Call constructorNoLookup(final Class<?> clazz) {
        return specialCallNoLookup(clazz, CompilerConstants.INIT.symbolName(), Void.TYPE, (Class<?>[])new Class[0]);
    }
    
    public static Call constructorNoLookup(final String className, final Class<?>... ptypes) {
        return specialCallNoLookup(className, CompilerConstants.INIT.symbolName(), methodDescriptor(Void.TYPE, ptypes));
    }
    
    public static Call constructorNoLookup(final Class<?> clazz, final Class<?>... ptypes) {
        return specialCallNoLookup(clazz, CompilerConstants.INIT.symbolName(), Void.TYPE, ptypes);
    }
    
    public static Call specialCallNoLookup(final String className, final String name, final String desc) {
        return new Call(null, className, name, desc) {
            @Override
            MethodEmitter invoke(final MethodEmitter method) {
                return method.invokespecial(this.className, this.name, this.descriptor);
            }
            
            @Override
            public void invoke(final MethodVisitor mv) {
                mv.visitMethodInsn(183, this.className, this.name, desc, false);
            }
        };
    }
    
    public static Call specialCallNoLookup(final Class<?> clazz, final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return specialCallNoLookup(className(clazz), name, methodDescriptor(rtype, ptypes));
    }
    
    public static Call staticCallNoLookup(final String className, final String name, final String desc) {
        return new Call(null, className, name, desc) {
            @Override
            MethodEmitter invoke(final MethodEmitter method) {
                return method.invokestatic(this.className, this.name, this.descriptor);
            }
            
            @Override
            public void invoke(final MethodVisitor mv) {
                mv.visitMethodInsn(184, this.className, this.name, desc, false);
            }
        };
    }
    
    public static Call staticCallNoLookup(final Class<?> clazz, final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return staticCallNoLookup(className(clazz), name, methodDescriptor(rtype, ptypes));
    }
    
    public static Call virtualCallNoLookup(final Class<?> clazz, final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return new Call(null, className(clazz), name, methodDescriptor(rtype, ptypes)) {
            @Override
            MethodEmitter invoke(final MethodEmitter method) {
                return method.invokevirtual(this.className, this.name, this.descriptor);
            }
            
            @Override
            public void invoke(final MethodVisitor mv) {
                mv.visitMethodInsn(182, this.className, this.name, this.descriptor, false);
            }
        };
    }
    
    public static Call interfaceCallNoLookup(final Class<?> clazz, final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return new Call(null, className(clazz), name, methodDescriptor(rtype, ptypes)) {
            @Override
            MethodEmitter invoke(final MethodEmitter method) {
                return method.invokeinterface(this.className, this.name, this.descriptor);
            }
            
            @Override
            public void invoke(final MethodVisitor mv) {
                mv.visitMethodInsn(185, this.className, this.name, this.descriptor, true);
            }
        };
    }
    
    public static FieldAccess virtualField(final String className, final String name, final String desc) {
        return new FieldAccess(className, name, desc) {
            public MethodEmitter get(final MethodEmitter method) {
                return method.getField(this.className, this.name, this.descriptor);
            }
            
            public void put(final MethodEmitter method) {
                method.putField(this.className, this.name, this.descriptor);
            }
        };
    }
    
    public static FieldAccess virtualField(final Class<?> clazz, final String name, final Class<?> type) {
        return virtualField(className(clazz), name, typeDescriptor(type));
    }
    
    public static FieldAccess staticField(final String className, final String name, final String desc) {
        return new FieldAccess(className, name, desc) {
            public MethodEmitter get(final MethodEmitter method) {
                return method.getStatic(this.className, this.name, this.descriptor);
            }
            
            public void put(final MethodEmitter method) {
                method.putStatic(this.className, this.name, this.descriptor);
            }
        };
    }
    
    public static FieldAccess staticField(final Class<?> clazz, final String name, final Class<?> type) {
        return staticField(className(clazz), name, typeDescriptor(type));
    }
    
    public static Call staticCall(final MethodHandles.Lookup lookup, final Class<?> clazz, final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return new Call(Lookup.MH.findStatic(lookup, clazz, name, Lookup.MH.type(rtype, ptypes)), className(clazz), name, methodDescriptor(rtype, ptypes)) {
            @Override
            MethodEmitter invoke(final MethodEmitter method) {
                return method.invokestatic(this.className, this.name, this.descriptor);
            }
            
            @Override
            public void invoke(final MethodVisitor mv) {
                mv.visitMethodInsn(184, this.className, this.name, this.descriptor, false);
            }
        };
    }
    
    public static Call virtualCall(final MethodHandles.Lookup lookup, final Class<?> clazz, final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return new Call(Lookup.MH.findVirtual(lookup, clazz, name, Lookup.MH.type(rtype, ptypes)), className(clazz), name, methodDescriptor(rtype, ptypes)) {
            @Override
            MethodEmitter invoke(final MethodEmitter method) {
                return method.invokevirtual(this.className, this.name, this.descriptor);
            }
            
            @Override
            public void invoke(final MethodVisitor mv) {
                mv.visitMethodInsn(182, this.className, this.name, this.descriptor, false);
            }
        };
    }
    
    public static Call specialCall(final MethodHandles.Lookup lookup, final Class<?> clazz, final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return new Call(Lookup.MH.findSpecial(lookup, clazz, name, Lookup.MH.type(rtype, ptypes), clazz), className(clazz), name, methodDescriptor(rtype, ptypes)) {
            @Override
            MethodEmitter invoke(final MethodEmitter method) {
                return method.invokespecial(this.className, this.name, this.descriptor);
            }
            
            @Override
            public void invoke(final MethodVisitor mv) {
                mv.visitMethodInsn(183, this.className, this.name, this.descriptor, false);
            }
        };
    }
    
    public static boolean isInternalMethodName(final String methodName) {
        return methodName.startsWith(":") && !methodName.equals(CompilerConstants.PROGRAM.symbolName);
    }
    
    static {
        for (final CompilerConstants c : values()) {
            final String symbolName = c.symbolName();
            if (symbolName != null) {
                symbolName.intern();
            }
        }
    }
    
    private abstract static class Access
    {
        protected final MethodHandle methodHandle;
        protected final String className;
        protected final String name;
        protected final String descriptor;
        
        protected Access(final MethodHandle methodHandle, final String className, final String name, final String descriptor) {
            this.methodHandle = methodHandle;
            this.className = className;
            this.name = name;
            this.descriptor = descriptor;
        }
        
        public MethodHandle methodHandle() {
            return this.methodHandle;
        }
        
        public String className() {
            return this.className;
        }
        
        public String name() {
            return this.name;
        }
        
        public String descriptor() {
            return this.descriptor;
        }
    }
    
    public abstract static class FieldAccess extends Access
    {
        protected FieldAccess(final String className, final String name, final String descriptor) {
            super(null, className, name, descriptor);
        }
        
        protected abstract MethodEmitter get(final MethodEmitter p0);
        
        protected abstract void put(final MethodEmitter p0);
    }
    
    public abstract static class Call extends Access
    {
        protected Call(final String className, final String name, final String descriptor) {
            super(null, className, name, descriptor);
        }
        
        protected Call(final MethodHandle methodHandle, final String className, final String name, final String descriptor) {
            super(methodHandle, className, name, descriptor);
        }
        
        abstract MethodEmitter invoke(final MethodEmitter p0);
        
        public abstract void invoke(final MethodVisitor p0);
    }
}
