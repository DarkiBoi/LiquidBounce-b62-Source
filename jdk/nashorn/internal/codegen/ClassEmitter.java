// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.RewriteException;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;
import jdk.nashorn.internal.ir.debug.NashornTextifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import jdk.nashorn.internal.ir.debug.NashornClassReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.scripts.JS;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.Collections;
import java.util.Set;
import jdk.nashorn.internal.runtime.Context;
import jdk.internal.org.objectweb.asm.ClassWriter;
import java.util.HashSet;
import java.util.EnumSet;

public class ClassEmitter
{
    private static final EnumSet<Flag> DEFAULT_METHOD_FLAGS;
    private boolean classStarted;
    private boolean classEnded;
    private final HashSet<MethodEmitter> methodsStarted;
    protected final ClassWriter cw;
    protected final Context context;
    private String unitClassName;
    private Set<Class<?>> constantMethodNeeded;
    private int methodCount;
    private int initCount;
    private int clinitCount;
    private int fieldCount;
    private final Set<String> methodNames;
    
    private ClassEmitter(final Context context, final ClassWriter cw) {
        this.context = context;
        this.cw = cw;
        this.methodsStarted = new HashSet<MethodEmitter>();
        this.methodNames = new HashSet<String>();
    }
    
    public Set<String> getMethodNames() {
        return Collections.unmodifiableSet((Set<? extends String>)this.methodNames);
    }
    
    ClassEmitter(final Context context, final String className, final String superClassName, final String... interfaceNames) {
        this(context, new ClassWriter(3));
        this.cw.visit(51, 33, className, null, superClassName, interfaceNames);
    }
    
    ClassEmitter(final Context context, final String sourceName, final String unitClassName, final boolean strictMode) {
        this(context, new ClassWriter(3) {
            private static final String OBJECT_CLASS = "java/lang/Object";
            
            @Override
            protected String getCommonSuperClass(final String type1, final String type2) {
                try {
                    return super.getCommonSuperClass(type1, type2);
                }
                catch (RuntimeException e) {
                    if (isScriptObject("jdk/nashorn/internal/scripts", type1) && isScriptObject("jdk/nashorn/internal/scripts", type2)) {
                        return CompilerConstants.className(ScriptObject.class);
                    }
                    return "java/lang/Object";
                }
            }
        });
        this.unitClassName = unitClassName;
        this.constantMethodNeeded = new HashSet<Class<?>>();
        this.cw.visit(51, 33, unitClassName, null, pathName(JS.class.getName()), null);
        this.cw.visitSource(sourceName, null);
        this.defineCommonStatics(strictMode);
    }
    
    Context getContext() {
        return this.context;
    }
    
    String getUnitClassName() {
        return this.unitClassName;
    }
    
    public int getMethodCount() {
        return this.methodCount;
    }
    
    public int getClinitCount() {
        return this.clinitCount;
    }
    
    public int getInitCount() {
        return this.initCount;
    }
    
    public int getFieldCount() {
        return this.fieldCount;
    }
    
    private static String pathName(final String name) {
        return name.replace('.', '/');
    }
    
    private void defineCommonStatics(final boolean strictMode) {
        this.field(EnumSet.of(Flag.PRIVATE, Flag.STATIC), CompilerConstants.SOURCE.symbolName(), Source.class);
        this.field(EnumSet.of(Flag.PRIVATE, Flag.STATIC), CompilerConstants.CONSTANTS.symbolName(), Object[].class);
        this.field(EnumSet.of(Flag.PUBLIC, Flag.STATIC, Flag.FINAL), CompilerConstants.STRICT_MODE.symbolName(), Boolean.TYPE, strictMode);
    }
    
    private void defineCommonUtilities() {
        assert this.unitClassName != null;
        if (this.constantMethodNeeded.contains(String.class)) {
            final MethodEmitter getStringMethod = this.method(EnumSet.of(Flag.PRIVATE, Flag.STATIC), CompilerConstants.GET_STRING.symbolName(), String.class, Integer.TYPE);
            getStringMethod.begin();
            getStringMethod.getStatic(this.unitClassName, CompilerConstants.CONSTANTS.symbolName(), CompilerConstants.CONSTANTS.descriptor()).load(Type.INT, 0).arrayload().checkcast(String.class)._return();
            getStringMethod.end();
        }
        if (this.constantMethodNeeded.contains(PropertyMap.class)) {
            final MethodEmitter getMapMethod = this.method(EnumSet.of(Flag.PUBLIC, Flag.STATIC), CompilerConstants.GET_MAP.symbolName(), PropertyMap.class, Integer.TYPE);
            getMapMethod.begin();
            getMapMethod.loadConstants().load(Type.INT, 0).arrayload().checkcast(PropertyMap.class)._return();
            getMapMethod.end();
            final MethodEmitter setMapMethod = this.method(EnumSet.of(Flag.PUBLIC, Flag.STATIC), CompilerConstants.SET_MAP.symbolName(), Void.TYPE, Integer.TYPE, PropertyMap.class);
            setMapMethod.begin();
            setMapMethod.loadConstants().load(Type.INT, 0).load(Type.OBJECT, 1).arraystore();
            setMapMethod.returnVoid();
            setMapMethod.end();
        }
        for (final Class<?> clazz : this.constantMethodNeeded) {
            if (clazz.isArray()) {
                this.defineGetArrayMethod(clazz);
            }
        }
    }
    
    private void defineGetArrayMethod(final Class<?> clazz) {
        assert this.unitClassName != null;
        final String methodName = getArrayMethodName(clazz);
        final MethodEmitter getArrayMethod = this.method(EnumSet.of(Flag.PRIVATE, Flag.STATIC), methodName, clazz, Integer.TYPE);
        getArrayMethod.begin();
        getArrayMethod.getStatic(this.unitClassName, CompilerConstants.CONSTANTS.symbolName(), CompilerConstants.CONSTANTS.descriptor()).load(Type.INT, 0).arrayload().checkcast(clazz).invoke(CompilerConstants.virtualCallNoLookup(clazz, "clone", Object.class, (Class<?>[])new Class[0])).checkcast(clazz)._return();
        getArrayMethod.end();
    }
    
    static String getArrayMethodName(final Class<?> clazz) {
        assert clazz.isArray();
        return CompilerConstants.GET_ARRAY_PREFIX.symbolName() + clazz.getComponentType().getSimpleName() + CompilerConstants.GET_ARRAY_SUFFIX.symbolName();
    }
    
    void needGetConstantMethod(final Class<?> clazz) {
        this.constantMethodNeeded.add(clazz);
    }
    
    private static boolean isScriptObject(final String scriptPrefix, final String type) {
        return type.startsWith(scriptPrefix) || type.equals(CompilerConstants.className(ScriptObject.class)) || type.startsWith("jdk/nashorn/internal/objects");
    }
    
    public void begin() {
        this.classStarted = true;
    }
    
    public void end() {
        assert this.classStarted : "class not started for " + this.unitClassName;
        if (this.unitClassName != null) {
            final MethodEmitter initMethod = this.init(EnumSet.of(Flag.PRIVATE), (Class<?>[])new Class[0]);
            initMethod.begin();
            initMethod.load(Type.OBJECT, 0);
            initMethod.newInstance(JS.class);
            initMethod.returnVoid();
            initMethod.end();
            this.defineCommonUtilities();
        }
        this.cw.visitEnd();
        this.classStarted = false;
        this.classEnded = true;
        assert this.methodsStarted.isEmpty() : "methodsStarted not empty " + this.methodsStarted;
    }
    
    static String disassemble(final byte[] bytecode) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final PrintWriter pw = new PrintWriter(baos)) {
            final NashornClassReader cr = new NashornClassReader(bytecode);
            final Context ctx = AccessController.doPrivileged((PrivilegedAction<Context>)new PrivilegedAction<Context>() {
                @Override
                public Context run() {
                    return Context.getContext();
                }
            });
            final TraceClassVisitor tcv = new TraceClassVisitor(null, new NashornTextifier(ctx.getEnv(), cr), pw);
            cr.accept(tcv, 0);
        }
        final String str = new String(baos.toByteArray());
        return str;
    }
    
    void beginMethod(final MethodEmitter method) {
        assert !this.methodsStarted.contains(method);
        this.methodsStarted.add(method);
    }
    
    void endMethod(final MethodEmitter method) {
        assert this.methodsStarted.contains(method);
        this.methodsStarted.remove(method);
    }
    
    MethodEmitter method(final String methodName, final Class<?> rtype, final Class<?>... ptypes) {
        return this.method(ClassEmitter.DEFAULT_METHOD_FLAGS, methodName, rtype, ptypes);
    }
    
    MethodEmitter method(final EnumSet<Flag> methodFlags, final String methodName, final Class<?> rtype, final Class<?>... ptypes) {
        ++this.methodCount;
        this.methodNames.add(methodName);
        return new MethodEmitter(this, this.methodVisitor(methodFlags, methodName, rtype, ptypes));
    }
    
    MethodEmitter method(final String methodName, final String descriptor) {
        return this.method(ClassEmitter.DEFAULT_METHOD_FLAGS, methodName, descriptor);
    }
    
    MethodEmitter method(final EnumSet<Flag> methodFlags, final String methodName, final String descriptor) {
        ++this.methodCount;
        this.methodNames.add(methodName);
        return new MethodEmitter(this, this.cw.visitMethod(Flag.getValue(methodFlags), methodName, descriptor, null, null));
    }
    
    MethodEmitter method(final FunctionNode functionNode) {
        ++this.methodCount;
        this.methodNames.add(functionNode.getName());
        final FunctionSignature signature = new FunctionSignature(functionNode);
        final MethodVisitor mv = this.cw.visitMethod(0x9 | (functionNode.isVarArg() ? 128 : 0), functionNode.getName(), signature.toString(), null, null);
        return new MethodEmitter(this, mv, functionNode);
    }
    
    MethodEmitter restOfMethod(final FunctionNode functionNode) {
        ++this.methodCount;
        this.methodNames.add(functionNode.getName());
        final MethodVisitor mv = this.cw.visitMethod(9, functionNode.getName(), Type.getMethodDescriptor(functionNode.getReturnType().getTypeClass(), RewriteException.class), null, null);
        return new MethodEmitter(this, mv, functionNode);
    }
    
    MethodEmitter clinit() {
        ++this.clinitCount;
        return this.method(EnumSet.of(Flag.STATIC), CompilerConstants.CLINIT.symbolName(), Void.TYPE, (Class<?>[])new Class[0]);
    }
    
    MethodEmitter init() {
        ++this.initCount;
        return this.method(CompilerConstants.INIT.symbolName(), Void.TYPE, (Class<?>[])new Class[0]);
    }
    
    MethodEmitter init(final Class<?>... ptypes) {
        ++this.initCount;
        return this.method(CompilerConstants.INIT.symbolName(), Void.TYPE, ptypes);
    }
    
    MethodEmitter init(final EnumSet<Flag> flags, final Class<?>... ptypes) {
        ++this.initCount;
        return this.method(flags, CompilerConstants.INIT.symbolName(), Void.TYPE, ptypes);
    }
    
    final void field(final EnumSet<Flag> fieldFlags, final String fieldName, final Class<?> fieldType, final Object value) {
        ++this.fieldCount;
        this.cw.visitField(Flag.getValue(fieldFlags), fieldName, CompilerConstants.typeDescriptor(fieldType), null, value).visitEnd();
    }
    
    final void field(final EnumSet<Flag> fieldFlags, final String fieldName, final Class<?> fieldType) {
        this.field(fieldFlags, fieldName, fieldType, null);
    }
    
    final void field(final String fieldName, final Class<?> fieldType) {
        this.field(EnumSet.of(Flag.PUBLIC), fieldName, fieldType, null);
    }
    
    byte[] toByteArray() {
        assert this.classEnded;
        if (!this.classEnded) {
            return null;
        }
        return this.cw.toByteArray();
    }
    
    private MethodVisitor methodVisitor(final EnumSet<Flag> flags, final String methodName, final Class<?> rtype, final Class<?>... ptypes) {
        return this.cw.visitMethod(Flag.getValue(flags), methodName, CompilerConstants.methodDescriptor(rtype, ptypes), null, null);
    }
    
    static {
        DEFAULT_METHOD_FLAGS = EnumSet.of(Flag.PUBLIC);
    }
    
    enum Flag
    {
        HANDLE_STATIC(6), 
        HANDLE_NEWSPECIAL(8), 
        HANDLE_SPECIAL(7), 
        HANDLE_VIRTUAL(5), 
        HANDLE_INTERFACE(9), 
        FINAL(16), 
        STATIC(8), 
        PUBLIC(1), 
        PRIVATE(2);
        
        private int value;
        
        private Flag(final int value) {
            this.value = value;
        }
        
        int getValue() {
            return this.value;
        }
        
        static int getValue(final EnumSet<Flag> flags) {
            int v = 0;
            for (final Flag flag : flags) {
                v |= flag.getValue();
            }
            return v;
        }
    }
}
