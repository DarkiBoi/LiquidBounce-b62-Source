// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen.types;

import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Undefined;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.CallSite;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import java.util.Collections;
import java.util.WeakHashMap;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import java.util.TreeMap;
import java.io.DataInput;
import java.io.IOException;
import java.util.Iterator;
import java.io.DataOutput;
import jdk.nashorn.internal.runtime.Context;
import java.util.concurrent.ConcurrentMap;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.nashorn.internal.codegen.CompilerConstants;
import java.util.Map;
import java.io.Serializable;

public abstract class Type implements Comparable<Type>, BytecodeOps, Serializable
{
    private static final long serialVersionUID = 1L;
    private final transient String name;
    private final transient String descriptor;
    private final transient int weight;
    private final transient int slots;
    private final Class<?> clazz;
    private static final Map<Class<?>, jdk.internal.org.objectweb.asm.Type> INTERNAL_TYPE_CACHE;
    private final transient jdk.internal.org.objectweb.asm.Type internalType;
    protected static final int MIN_WEIGHT = -1;
    protected static final int MAX_WEIGHT = 20;
    static final CompilerConstants.Call BOOTSTRAP;
    static final Handle MATHBOOTSTRAP;
    private static final ConcurrentMap<Class<?>, Type> cache;
    public static final Type BOOLEAN;
    public static final BitwiseType INT;
    public static final NumericType NUMBER;
    public static final Type LONG;
    public static final Type STRING;
    public static final Type CHARSEQUENCE;
    public static final Type OBJECT;
    public static final Type UNDEFINED;
    public static final Type SCRIPT_OBJECT;
    public static final ArrayType INT_ARRAY;
    public static final ArrayType LONG_ARRAY;
    public static final ArrayType NUMBER_ARRAY;
    public static final ArrayType OBJECT_ARRAY;
    public static final Type THIS;
    public static final Type SCOPE;
    public static final Type UNKNOWN;
    public static final Type SLOT_2;
    
    Type(final String name, final Class<?> clazz, final int weight, final int slots) {
        this.name = name;
        this.clazz = clazz;
        this.descriptor = jdk.internal.org.objectweb.asm.Type.getDescriptor(clazz);
        this.weight = weight;
        assert weight >= -1 && weight <= 20 : "illegal type weight: " + weight;
        this.slots = slots;
        this.internalType = getInternalType(clazz);
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public Class<?> getTypeClass() {
        return this.clazz;
    }
    
    public Type nextWider() {
        return null;
    }
    
    public Class<?> getBoxedType() {
        assert !this.getTypeClass().isPrimitive();
        return null;
    }
    
    public abstract char getBytecodeStackType();
    
    public static String getMethodDescriptor(final Type returnType, final Type... types) {
        final jdk.internal.org.objectweb.asm.Type[] itypes = new jdk.internal.org.objectweb.asm.Type[types.length];
        for (int i = 0; i < types.length; ++i) {
            itypes[i] = types[i].getInternalType();
        }
        return jdk.internal.org.objectweb.asm.Type.getMethodDescriptor(returnType.getInternalType(), itypes);
    }
    
    public static String getMethodDescriptor(final Class<?> returnType, final Class<?>... types) {
        final jdk.internal.org.objectweb.asm.Type[] itypes = new jdk.internal.org.objectweb.asm.Type[types.length];
        for (int i = 0; i < types.length; ++i) {
            itypes[i] = getInternalType(types[i]);
        }
        return jdk.internal.org.objectweb.asm.Type.getMethodDescriptor(getInternalType(returnType), itypes);
    }
    
    public static char getShortSignatureDescriptor(final Type type) {
        if (type instanceof BooleanType) {
            return 'Z';
        }
        return type.getBytecodeStackType();
    }
    
    static Type typeFor(final jdk.internal.org.objectweb.asm.Type itype) {
        switch (itype.getSort()) {
            case 1: {
                return Type.BOOLEAN;
            }
            case 5: {
                return Type.INT;
            }
            case 7: {
                return Type.LONG;
            }
            case 8: {
                return Type.NUMBER;
            }
            case 10: {
                if (Context.isStructureClass(itype.getClassName())) {
                    return Type.SCRIPT_OBJECT;
                }
                try {
                    return typeFor(Class.forName(itype.getClassName()));
                }
                catch (ClassNotFoundException e) {
                    throw new AssertionError((Object)e);
                }
                return null;
            }
            case 0: {
                return null;
            }
            case 9: {
                switch (itype.getElementType().getSort()) {
                    case 8: {
                        return Type.NUMBER_ARRAY;
                    }
                    case 5: {
                        return Type.INT_ARRAY;
                    }
                    case 7: {
                        return Type.LONG_ARRAY;
                    }
                    default: {
                        assert false;
                        return Type.OBJECT_ARRAY;
                    }
                    case 10: {
                        return Type.OBJECT_ARRAY;
                    }
                }
                break;
            }
            default: {
                assert false : "Unknown itype : " + itype + " sort " + itype.getSort();
                return null;
            }
        }
    }
    
    public static Type getMethodReturnType(final String methodDescriptor) {
        return typeFor(jdk.internal.org.objectweb.asm.Type.getReturnType(methodDescriptor));
    }
    
    public static Type[] getMethodArguments(final String methodDescriptor) {
        final jdk.internal.org.objectweb.asm.Type[] itypes = jdk.internal.org.objectweb.asm.Type.getArgumentTypes(methodDescriptor);
        final Type[] types = new Type[itypes.length];
        for (int i = 0; i < itypes.length; ++i) {
            types[i] = typeFor(itypes[i]);
        }
        return types;
    }
    
    public static void writeTypeMap(final Map<Integer, Type> typeMap, final DataOutput output) throws IOException {
        if (typeMap == null) {
            output.writeInt(0);
        }
        else {
            output.writeInt(typeMap.size());
            for (final Map.Entry<Integer, Type> e : typeMap.entrySet()) {
                output.writeInt(e.getKey());
                final Type type = e.getValue();
                byte typeChar;
                if (type == Type.OBJECT) {
                    typeChar = 76;
                }
                else if (type == Type.NUMBER) {
                    typeChar = 68;
                }
                else {
                    if (type != Type.LONG) {
                        throw new AssertionError();
                    }
                    typeChar = 74;
                }
                output.writeByte(typeChar);
            }
        }
    }
    
    public static Map<Integer, Type> readTypeMap(final DataInput input) throws IOException {
        final int size = input.readInt();
        if (size <= 0) {
            return null;
        }
        final Map<Integer, Type> map = new TreeMap<Integer, Type>();
        for (int i = 0; i < size; ++i) {
            final int pp = input.readInt();
            final int typeChar = input.readByte();
            Type type = null;
            switch (typeChar) {
                case 76: {
                    type = Type.OBJECT;
                    break;
                }
                case 68: {
                    type = Type.NUMBER;
                    break;
                }
                case 74: {
                    type = Type.LONG;
                    break;
                }
                default: {
                    continue;
                }
            }
            map.put(pp, type);
        }
        return map;
    }
    
    static jdk.internal.org.objectweb.asm.Type getInternalType(final String className) {
        return jdk.internal.org.objectweb.asm.Type.getType(className);
    }
    
    private jdk.internal.org.objectweb.asm.Type getInternalType() {
        return this.internalType;
    }
    
    private static jdk.internal.org.objectweb.asm.Type lookupInternalType(final Class<?> type) {
        final Map<Class<?>, jdk.internal.org.objectweb.asm.Type> c = Type.INTERNAL_TYPE_CACHE;
        jdk.internal.org.objectweb.asm.Type itype = c.get(type);
        if (itype != null) {
            return itype;
        }
        itype = jdk.internal.org.objectweb.asm.Type.getType(type);
        c.put(type, itype);
        return itype;
    }
    
    private static jdk.internal.org.objectweb.asm.Type getInternalType(final Class<?> type) {
        return lookupInternalType(type);
    }
    
    static void invokestatic(final MethodVisitor method, final CompilerConstants.Call call) {
        method.visitMethodInsn(184, call.className(), call.name(), call.descriptor(), false);
    }
    
    public String getInternalName() {
        return jdk.internal.org.objectweb.asm.Type.getInternalName(this.getTypeClass());
    }
    
    public static String getInternalName(final Class<?> clazz) {
        return jdk.internal.org.objectweb.asm.Type.getInternalName(clazz);
    }
    
    public boolean isUnknown() {
        return this.equals(Type.UNKNOWN);
    }
    
    public boolean isJSPrimitive() {
        return !this.isObject() || this.isString();
    }
    
    public boolean isBoolean() {
        return this.equals(Type.BOOLEAN);
    }
    
    public boolean isInteger() {
        return this.equals(Type.INT);
    }
    
    public boolean isLong() {
        return this.equals(Type.LONG);
    }
    
    public boolean isNumber() {
        return this.equals(Type.NUMBER);
    }
    
    public boolean isNumeric() {
        return this instanceof NumericType;
    }
    
    public boolean isArray() {
        return this instanceof ArrayType;
    }
    
    public boolean isCategory2() {
        return this.getSlots() == 2;
    }
    
    public boolean isObject() {
        return this instanceof ObjectType;
    }
    
    public boolean isPrimitive() {
        return !this.isObject();
    }
    
    public boolean isString() {
        return this.equals(Type.STRING);
    }
    
    public boolean isCharSequence() {
        return this.equals(Type.CHARSEQUENCE);
    }
    
    public boolean isEquivalentTo(final Type type) {
        return this.weight() == type.weight() || (this.isObject() && type.isObject());
    }
    
    public static boolean isAssignableFrom(final Type type0, final Type type1) {
        if (type0.isObject() && type1.isObject()) {
            return type0.weight() >= type1.weight();
        }
        return type0.weight() == type1.weight();
    }
    
    public boolean isAssignableFrom(final Type type) {
        return isAssignableFrom(this, type);
    }
    
    public static boolean areEquivalent(final Type type0, final Type type1) {
        return type0.isEquivalentTo(type1);
    }
    
    public int getSlots() {
        return this.slots;
    }
    
    public static Type widest(final Type type0, final Type type1) {
        if (type0.isArray() && type1.isArray()) {
            return (((ArrayType)type0).getElementType() == ((ArrayType)type1).getElementType()) ? type0 : Type.OBJECT;
        }
        if (type0.isArray() != type1.isArray()) {
            return Type.OBJECT;
        }
        if (type0.isObject() && type1.isObject() && type0.getTypeClass() != type1.getTypeClass()) {
            return Type.OBJECT;
        }
        return (type0.weight() > type1.weight()) ? type0 : type1;
    }
    
    public static Class<?> widest(final Class<?> type0, final Class<?> type1) {
        return widest(typeFor(type0), typeFor(type1)).getTypeClass();
    }
    
    public static Type widestReturnType(final Type t1, final Type t2) {
        if (t1.isUnknown()) {
            return t2;
        }
        if (t2.isUnknown()) {
            return t1;
        }
        if (t1.isBoolean() != t2.isBoolean() || t1.isNumeric() != t2.isNumeric()) {
            return Type.OBJECT;
        }
        return widest(t1, t2);
    }
    
    public static Type generic(final Type type) {
        return type.isObject() ? Type.OBJECT : type;
    }
    
    public static Type narrowest(final Type type0, final Type type1) {
        return type0.narrowerThan(type1) ? type0 : type1;
    }
    
    public boolean narrowerThan(final Type type) {
        return this.weight() < type.weight();
    }
    
    public boolean widerThan(final Type type) {
        return this.weight() > type.weight();
    }
    
    public static Type widest(final Type type0, final Type type1, final Type limit) {
        final Type type2 = widest(type0, type1);
        if (type2.weight() > limit.weight()) {
            return limit;
        }
        return type2;
    }
    
    public static Type narrowest(final Type type0, final Type type1, final Type limit) {
        final Type type2 = (type0.weight() < type1.weight()) ? type0 : type1;
        if (type2.weight() < limit.weight()) {
            return limit;
        }
        return type2;
    }
    
    public Type narrowest(final Type other) {
        return narrowest(this, other);
    }
    
    public Type widest(final Type other) {
        return widest(this, other);
    }
    
    int weight() {
        return this.weight;
    }
    
    public String getDescriptor() {
        return this.descriptor;
    }
    
    public String getShortDescriptor() {
        return this.descriptor;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public static Type typeFor(final Class<?> clazz) {
        final Type type = Type.cache.get(clazz);
        if (type != null) {
            return type;
        }
        assert clazz == Void.TYPE;
        Type newType;
        if (clazz.isArray()) {
            newType = new ArrayType(clazz);
        }
        else {
            newType = new ObjectType(clazz);
        }
        final Type existingType = Type.cache.putIfAbsent(clazz, newType);
        return (existingType == null) ? newType : existingType;
    }
    
    @Override
    public int compareTo(final Type o) {
        return o.weight() - this.weight();
    }
    
    @Override
    public Type dup(final MethodVisitor method, final int depth) {
        return dup(method, this, depth);
    }
    
    @Override
    public Type swap(final MethodVisitor method, final Type other) {
        swap(method, this, other);
        return other;
    }
    
    @Override
    public Type pop(final MethodVisitor method) {
        pop(method, this);
        return this;
    }
    
    @Override
    public Type loadEmpty(final MethodVisitor method) {
        assert false : "unsupported operation";
        return null;
    }
    
    protected static void pop(final MethodVisitor method, final Type type) {
        method.visitInsn(type.isCategory2() ? 88 : 87);
    }
    
    private static Type dup(final MethodVisitor method, final Type type, final int depth) {
        final boolean cat2 = type.isCategory2();
        switch (depth) {
            case 0: {
                method.visitInsn(cat2 ? 92 : 89);
                break;
            }
            case 1: {
                method.visitInsn(cat2 ? 93 : 90);
                break;
            }
            case 2: {
                method.visitInsn(cat2 ? 94 : 91);
                break;
            }
            default: {
                return null;
            }
        }
        return type;
    }
    
    private static void swap(final MethodVisitor method, final Type above, final Type below) {
        if (below.isCategory2()) {
            if (above.isCategory2()) {
                method.visitInsn(94);
                method.visitInsn(88);
            }
            else {
                method.visitInsn(91);
                method.visitInsn(87);
            }
        }
        else if (above.isCategory2()) {
            method.visitInsn(93);
            method.visitInsn(88);
        }
        else {
            method.visitInsn(95);
        }
    }
    
    private static <T extends Type> T putInCache(final T type) {
        Type.cache.put(type.getTypeClass(), type);
        return type;
    }
    
    protected final Object readResolve() {
        return typeFor(this.clazz);
    }
    
    static {
        INTERNAL_TYPE_CACHE = Collections.synchronizedMap(new WeakHashMap<Class<?>, jdk.internal.org.objectweb.asm.Type>());
        BOOTSTRAP = CompilerConstants.staticCallNoLookup(Bootstrap.class, "mathBootstrap", CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, Integer.TYPE);
        MATHBOOTSTRAP = new Handle(6, Type.BOOTSTRAP.className(), "mathBootstrap", Type.BOOTSTRAP.descriptor());
        cache = new ConcurrentHashMap<Class<?>, Type>();
        BOOLEAN = putInCache(new BooleanType());
        INT = putInCache(new IntType());
        NUMBER = putInCache(new NumberType());
        LONG = putInCache(new LongType());
        STRING = putInCache(new ObjectType(String.class));
        CHARSEQUENCE = putInCache(new ObjectType(CharSequence.class));
        OBJECT = putInCache(new ObjectType());
        UNDEFINED = putInCache(new ObjectType(Undefined.class));
        SCRIPT_OBJECT = putInCache(new ObjectType(ScriptObject.class));
        INT_ARRAY = putInCache(new ArrayType((Class)int[].class) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void astore(final MethodVisitor method) {
                method.visitInsn(79);
            }
            
            @Override
            public Type aload(final MethodVisitor method) {
                method.visitInsn(46);
                return Type$1.INT;
            }
            
            @Override
            public Type newarray(final MethodVisitor method) {
                method.visitIntInsn(188, 10);
                return this;
            }
            
            @Override
            public Type getElementType() {
                return Type$1.INT;
            }
        });
        LONG_ARRAY = putInCache(new ArrayType((Class)long[].class) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void astore(final MethodVisitor method) {
                method.visitInsn(80);
            }
            
            @Override
            public Type aload(final MethodVisitor method) {
                method.visitInsn(47);
                return Type$2.LONG;
            }
            
            @Override
            public Type newarray(final MethodVisitor method) {
                method.visitIntInsn(188, 11);
                return this;
            }
            
            @Override
            public Type getElementType() {
                return Type$2.LONG;
            }
        });
        NUMBER_ARRAY = putInCache(new ArrayType((Class)double[].class) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void astore(final MethodVisitor method) {
                method.visitInsn(82);
            }
            
            @Override
            public Type aload(final MethodVisitor method) {
                method.visitInsn(49);
                return Type$3.NUMBER;
            }
            
            @Override
            public Type newarray(final MethodVisitor method) {
                method.visitIntInsn(188, 7);
                return this;
            }
            
            @Override
            public Type getElementType() {
                return Type$3.NUMBER;
            }
        });
        OBJECT_ARRAY = putInCache(new ArrayType(Object[].class));
        THIS = new ObjectType() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public String toString() {
                return "this";
            }
        };
        SCOPE = new ObjectType() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public String toString() {
                return "scope";
            }
        };
        UNKNOWN = new ValueLessType("<unknown>") {
            private static final long serialVersionUID = 1L;
            
            @Override
            public String getDescriptor() {
                return "<unknown>";
            }
            
            @Override
            public char getBytecodeStackType() {
                return 'U';
            }
        };
        SLOT_2 = new ValueLessType("<slot_2>") {
            private static final long serialVersionUID = 1L;
            
            @Override
            public String getDescriptor() {
                return "<slot_2>";
            }
            
            @Override
            public char getBytecodeStackType() {
                throw new UnsupportedOperationException("getBytecodeStackType");
            }
        };
    }
    
    private abstract static class ValueLessType extends Type
    {
        private static final long serialVersionUID = 1L;
        
        ValueLessType(final String name) {
            super(name, Unknown.class, -1, 1);
        }
        
        @Override
        public Type load(final MethodVisitor method, final int slot) {
            throw new UnsupportedOperationException("load " + slot);
        }
        
        @Override
        public void store(final MethodVisitor method, final int slot) {
            throw new UnsupportedOperationException("store " + slot);
        }
        
        @Override
        public Type ldc(final MethodVisitor method, final Object c) {
            throw new UnsupportedOperationException("ldc " + c);
        }
        
        @Override
        public Type loadUndefined(final MethodVisitor method) {
            throw new UnsupportedOperationException("load undefined");
        }
        
        @Override
        public Type loadForcedInitializer(final MethodVisitor method) {
            throw new UnsupportedOperationException("load forced initializer");
        }
        
        @Override
        public Type convert(final MethodVisitor method, final Type to) {
            throw new UnsupportedOperationException("convert => " + to);
        }
        
        @Override
        public void _return(final MethodVisitor method) {
            throw new UnsupportedOperationException("return");
        }
        
        @Override
        public Type add(final MethodVisitor method, final int programPoint) {
            throw new UnsupportedOperationException("add");
        }
    }
    
    private interface Unknown
    {
    }
}
