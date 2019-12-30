// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.RewriteException;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.options.Options;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.JoinPredecessor;
import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.internal.dynalink.support.NameCodec;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.objects.NativeArray;
import java.util.List;
import jdk.nashorn.internal.runtime.ArgumentSetter;
import jdk.nashorn.internal.runtime.Scope;
import java.util.EnumSet;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import java.util.Iterator;
import java.util.Collection;
import jdk.nashorn.internal.codegen.types.ArrayType;
import jdk.nashorn.internal.codegen.types.BitwiseType;
import jdk.nashorn.internal.codegen.types.NumericType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.logging.Loggable;
import java.io.PrintStream;
import java.util.IdentityHashMap;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.ir.Symbol;
import java.util.Map;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.internal.org.objectweb.asm.MethodVisitor;

public class MethodEmitter
{
    private final MethodVisitor method;
    private final ClassEmitter classEmitter;
    protected FunctionNode functionNode;
    private Label.Stack stack;
    private boolean preventUndefinedLoad;
    private final Map<Symbol, LocalVariableDef> localVariableDefs;
    private final Context context;
    static final int LARGE_STRING_THRESHOLD = 32768;
    private final DebugLogger log;
    private final boolean debug;
    private static final int DEBUG_TRACE_LINE;
    private static final Handle LINKERBOOTSTRAP;
    private static final Handle POPULATE_ARRAY_BOOTSTRAP;
    private final CompilerConstants.FieldAccess ERR_STREAM;
    private final CompilerConstants.Call PRINT;
    private final CompilerConstants.Call PRINTLN;
    private final CompilerConstants.Call PRINT_STACKTRACE;
    private static int linePrefix;
    static final /* synthetic */ boolean $assertionsDisabled;
    
    MethodEmitter(final ClassEmitter classEmitter, final MethodVisitor method) {
        this(classEmitter, method, null);
    }
    
    MethodEmitter(final ClassEmitter classEmitter, final MethodVisitor method, final FunctionNode functionNode) {
        this.localVariableDefs = new IdentityHashMap<Symbol, LocalVariableDef>();
        this.ERR_STREAM = CompilerConstants.staticField(System.class, "err", PrintStream.class);
        this.PRINT = CompilerConstants.virtualCallNoLookup(PrintStream.class, "print", Void.TYPE, Object.class);
        this.PRINTLN = CompilerConstants.virtualCallNoLookup(PrintStream.class, "println", Void.TYPE, Object.class);
        this.PRINT_STACKTRACE = CompilerConstants.virtualCallNoLookup(Throwable.class, "printStackTrace", Void.TYPE, (Class<?>[])new Class[0]);
        this.context = classEmitter.getContext();
        this.classEmitter = classEmitter;
        this.method = method;
        this.functionNode = functionNode;
        this.stack = null;
        this.log = this.context.getLogger(CodeGenerator.class);
        this.debug = this.log.isEnabled();
    }
    
    public void begin() {
        this.classEmitter.beginMethod(this);
        this.newStack();
        this.method.visitCode();
    }
    
    public void end() {
        this.method.visitMaxs(0, 0);
        this.method.visitEnd();
        this.classEmitter.endMethod(this);
    }
    
    boolean isReachable() {
        return this.stack != null;
    }
    
    private void doesNotContinueSequentially() {
        this.stack = null;
    }
    
    private void newStack() {
        this.stack = new Label.Stack();
    }
    
    @Override
    public String toString() {
        return "methodEmitter: " + ((this.functionNode == null) ? this.method : this.functionNode.getName()).toString() + ' ' + Debug.id(this);
    }
    
    void pushType(final Type type) {
        if (type != null) {
            this.stack.push(type);
        }
    }
    
    private Type popType(final Type expected) {
        final Type type = this.popType();
        assert type.isEquivalentTo(expected) : type + " is not compatible with " + expected;
        return type;
    }
    
    private Type popType() {
        return this.stack.pop();
    }
    
    private NumericType popNumeric() {
        final Type type = this.popType();
        if (type.isBoolean()) {
            return Type.INT;
        }
        assert type.isNumeric();
        return (NumericType)type;
    }
    
    private BitwiseType popBitwise() {
        final Type type = this.popType();
        if (type == Type.BOOLEAN) {
            return Type.INT;
        }
        return (BitwiseType)type;
    }
    
    private BitwiseType popInteger() {
        final Type type = this.popType();
        if (type == Type.BOOLEAN) {
            return Type.INT;
        }
        assert type == Type.INT;
        return (BitwiseType)type;
    }
    
    private ArrayType popArray() {
        final Type type = this.popType();
        assert type.isArray() : type;
        return (ArrayType)type;
    }
    
    final Type peekType(final int pos) {
        return this.stack.peek(pos);
    }
    
    final Type peekType() {
        return this.stack.peek();
    }
    
    MethodEmitter _new(final String classDescriptor, final Type type) {
        this.debug("new", classDescriptor);
        this.method.visitTypeInsn(187, classDescriptor);
        this.pushType(type);
        return this;
    }
    
    MethodEmitter _new(final Class<?> clazz) {
        return this._new(CompilerConstants.className(clazz), Type.typeFor(clazz));
    }
    
    MethodEmitter newInstance(final Class<?> clazz) {
        return this.invoke(CompilerConstants.constructorNoLookup(clazz));
    }
    
    MethodEmitter dup(final int depth) {
        if (this.peekType().dup(this.method, depth) == null) {
            return null;
        }
        this.debug("dup", depth);
        switch (depth) {
            case 0: {
                final int l0 = this.stack.getTopLocalLoad();
                this.pushType(this.peekType());
                this.stack.markLocalLoad(l0);
                break;
            }
            case 1: {
                final int l0 = this.stack.getTopLocalLoad();
                final Type p0 = this.popType();
                final int l2 = this.stack.getTopLocalLoad();
                final Type p2 = this.popType();
                this.pushType(p0);
                this.stack.markLocalLoad(l0);
                this.pushType(p2);
                this.stack.markLocalLoad(l2);
                this.pushType(p0);
                this.stack.markLocalLoad(l0);
                break;
            }
            case 2: {
                final int l0 = this.stack.getTopLocalLoad();
                final Type p0 = this.popType();
                final int l2 = this.stack.getTopLocalLoad();
                final Type p2 = this.popType();
                final int l3 = this.stack.getTopLocalLoad();
                final Type p3 = this.popType();
                this.pushType(p0);
                this.stack.markLocalLoad(l0);
                this.pushType(p3);
                this.stack.markLocalLoad(l3);
                this.pushType(p2);
                this.stack.markLocalLoad(l2);
                this.pushType(p0);
                this.stack.markLocalLoad(l0);
                break;
            }
            default: {
                assert false : "illegal dup depth = " + depth;
                return null;
            }
        }
        return this;
    }
    
    MethodEmitter dup2() {
        this.debug("dup2");
        if (this.peekType().isCategory2()) {
            final int l0 = this.stack.getTopLocalLoad();
            this.pushType(this.peekType());
            this.stack.markLocalLoad(l0);
        }
        else {
            final int l0 = this.stack.getTopLocalLoad();
            final Type p0 = this.popType();
            final int l2 = this.stack.getTopLocalLoad();
            final Type p2 = this.popType();
            this.pushType(p0);
            this.stack.markLocalLoad(l0);
            this.pushType(p2);
            this.stack.markLocalLoad(l2);
            this.pushType(p0);
            this.stack.markLocalLoad(l0);
            this.pushType(p2);
            this.stack.markLocalLoad(l2);
        }
        this.method.visitInsn(92);
        return this;
    }
    
    MethodEmitter dup() {
        return this.dup(0);
    }
    
    MethodEmitter pop() {
        this.debug("pop", this.peekType());
        this.popType().pop(this.method);
        return this;
    }
    
    MethodEmitter pop2() {
        if (this.peekType().isCategory2()) {
            this.popType();
        }
        else {
            this.get2n();
        }
        return this;
    }
    
    MethodEmitter swap() {
        this.debug("swap");
        final int l0 = this.stack.getTopLocalLoad();
        final Type p0 = this.popType();
        final int l2 = this.stack.getTopLocalLoad();
        final Type p2 = this.popType();
        p0.swap(this.method, p2);
        this.pushType(p0);
        this.stack.markLocalLoad(l0);
        this.pushType(p2);
        this.stack.markLocalLoad(l2);
        return this;
    }
    
    void pack() {
        final Type type = this.peekType();
        if (type.isInteger()) {
            this.convert(ObjectClassGenerator.PRIMITIVE_FIELD_TYPE);
        }
        else if (!type.isLong()) {
            if (type.isNumber()) {
                this.invokestatic("java/lang/Double", "doubleToRawLongBits", "(D)J");
            }
            else {
                assert false : type + " cannot be packed!";
            }
        }
    }
    
    void initializeMethodParameter(final Symbol symbol, final Type type, final Label start) {
        assert symbol.isBytecodeLocal();
        this.localVariableDefs.put(symbol, new LocalVariableDef(start.getLabel(), type));
    }
    
    MethodEmitter newStringBuilder() {
        return this.invoke(CompilerConstants.constructorNoLookup(StringBuilder.class)).dup();
    }
    
    MethodEmitter stringBuilderAppend() {
        this.convert(Type.STRING);
        return this.invoke(CompilerConstants.virtualCallNoLookup(StringBuilder.class, "append", StringBuilder.class, String.class));
    }
    
    MethodEmitter and() {
        this.debug("and");
        this.pushType(this.get2i().and(this.method));
        return this;
    }
    
    MethodEmitter or() {
        this.debug("or");
        this.pushType(this.get2i().or(this.method));
        return this;
    }
    
    MethodEmitter xor() {
        this.debug("xor");
        this.pushType(this.get2i().xor(this.method));
        return this;
    }
    
    MethodEmitter shr() {
        this.debug("shr");
        this.popInteger();
        this.pushType(this.popBitwise().shr(this.method));
        return this;
    }
    
    MethodEmitter shl() {
        this.debug("shl");
        this.popInteger();
        this.pushType(this.popBitwise().shl(this.method));
        return this;
    }
    
    MethodEmitter sar() {
        this.debug("sar");
        this.popInteger();
        this.pushType(this.popBitwise().sar(this.method));
        return this;
    }
    
    MethodEmitter neg(final int programPoint) {
        this.debug("neg");
        this.pushType(this.popNumeric().neg(this.method, programPoint));
        return this;
    }
    
    void _catch(final Label recovery) {
        assert this.stack == null;
        recovery.onCatch();
        this.label(recovery);
        this.beginCatchBlock();
    }
    
    void _catch(final Collection<Label> recoveries) {
        assert this.stack == null;
        for (final Label l : recoveries) {
            this.label(l);
        }
        this.beginCatchBlock();
    }
    
    private void beginCatchBlock() {
        if (!this.isReachable()) {
            this.newStack();
        }
        this.pushType(Type.typeFor(Throwable.class));
    }
    
    private void _try(final Label entry, final Label exit, final Label recovery, final String typeDescriptor, final boolean isOptimismHandler) {
        recovery.joinFromTry(entry.getStack(), isOptimismHandler);
        this.method.visitTryCatchBlock(entry.getLabel(), exit.getLabel(), recovery.getLabel(), typeDescriptor);
    }
    
    void _try(final Label entry, final Label exit, final Label recovery, final Class<?> clazz) {
        this._try(entry, exit, recovery, CompilerConstants.className(clazz), clazz == UnwarrantedOptimismException.class);
    }
    
    void _try(final Label entry, final Label exit, final Label recovery) {
        this._try(entry, exit, recovery, null, false);
    }
    
    void markLabelAsOptimisticCatchHandler(final Label label, final int liveLocalCount) {
        label.markAsOptimisticCatchHandler(this.stack, liveLocalCount);
    }
    
    MethodEmitter loadConstants() {
        this.getStatic(this.classEmitter.getUnitClassName(), CompilerConstants.CONSTANTS.symbolName(), CompilerConstants.CONSTANTS.descriptor());
        assert this.peekType().isArray() : this.peekType();
        return this;
    }
    
    MethodEmitter loadUndefined(final Type type) {
        this.debug("load undefined ", type);
        this.pushType(type.loadUndefined(this.method));
        return this;
    }
    
    MethodEmitter loadForcedInitializer(final Type type) {
        this.debug("load forced initializer ", type);
        this.pushType(type.loadForcedInitializer(this.method));
        return this;
    }
    
    MethodEmitter loadEmpty(final Type type) {
        this.debug("load empty ", type);
        this.pushType(type.loadEmpty(this.method));
        return this;
    }
    
    MethodEmitter loadNull() {
        this.debug("aconst_null");
        this.pushType(Type.OBJECT.ldc(this.method, null));
        return this;
    }
    
    MethodEmitter loadType(final String className) {
        this.debug("load type", className);
        this.method.visitLdcInsn(jdk.internal.org.objectweb.asm.Type.getObjectType(className));
        this.pushType(Type.OBJECT);
        return this;
    }
    
    MethodEmitter load(final boolean b) {
        this.debug("load boolean", b);
        this.pushType(Type.BOOLEAN.ldc(this.method, b));
        return this;
    }
    
    MethodEmitter load(final int i) {
        this.debug("load int", i);
        this.pushType(Type.INT.ldc(this.method, i));
        return this;
    }
    
    MethodEmitter load(final double d) {
        this.debug("load double", d);
        this.pushType(Type.NUMBER.ldc(this.method, d));
        return this;
    }
    
    MethodEmitter load(final long l) {
        this.debug("load long", l);
        this.pushType(Type.LONG.ldc(this.method, l));
        return this;
    }
    
    MethodEmitter arraylength() {
        this.debug("arraylength");
        this.popType(Type.OBJECT);
        this.pushType(Type.OBJECT_ARRAY.arraylength(this.method));
        return this;
    }
    
    MethodEmitter load(final String s) {
        this.debug("load string", s);
        if (s == null) {
            this.loadNull();
            return this;
        }
        final int length = s.length();
        if (length > 32768) {
            this._new(StringBuilder.class);
            this.dup();
            this.load(length);
            this.invoke(CompilerConstants.constructorNoLookup(StringBuilder.class, Integer.TYPE));
            for (int n = 0; n < length; n += 32768) {
                final String part = s.substring(n, Math.min(n + 32768, length));
                this.load(part);
                this.stringBuilderAppend();
            }
            this.invoke(CompilerConstants.virtualCallNoLookup(StringBuilder.class, "toString", String.class, (Class<?>[])new Class[0]));
            return this;
        }
        this.pushType(Type.OBJECT.ldc(this.method, s));
        return this;
    }
    
    MethodEmitter load(final IdentNode ident) {
        return this.load(ident.getSymbol(), ident.getType());
    }
    
    MethodEmitter load(final Symbol symbol, final Type type) {
        assert symbol != null;
        if (symbol.hasSlot()) {
            final int slot = symbol.getSlot(type);
            this.debug("load symbol", symbol.getName(), " slot=", slot, "type=", type);
            this.load(type, slot);
        }
        else if (symbol.isParam()) {
            assert this.functionNode.isVarArg() : "Non-vararg functions have slotted parameters";
            final int index = symbol.getFieldIndex();
            if (this.functionNode.needsArguments()) {
                this.debug("load symbol", symbol.getName(), " arguments index=", index);
                this.loadCompilerConstant(CompilerConstants.ARGUMENTS);
                this.load(index);
                ScriptObject.GET_ARGUMENT.invoke(this);
            }
            else {
                this.debug("load symbol", symbol.getName(), " array index=", index);
                this.loadCompilerConstant(CompilerConstants.VARARGS);
                this.load(symbol.getFieldIndex());
                this.arrayload();
            }
        }
        return this;
    }
    
    MethodEmitter load(final Type type, final int slot) {
        this.debug("explicit load", type, slot);
        final Type loadType = type.load(this.method, slot);
        assert loadType != null;
        this.pushType((loadType == Type.OBJECT && this.isThisSlot(slot)) ? Type.THIS : loadType);
        assert slot < this.stack.localVariableTypes.size() && this.stack.localVariableTypes.get(slot) != Type.UNKNOWN : "Attempted load of uninitialized slot " + slot + " (as type " + type + ")";
        this.stack.markLocalLoad(slot);
        return this;
    }
    
    private boolean isThisSlot(final int slot) {
        if (this.functionNode == null) {
            return slot == CompilerConstants.JAVA_THIS.slot();
        }
        final int thisSlot = this.getCompilerConstantSymbol(CompilerConstants.THIS).getSlot(Type.OBJECT);
        assert thisSlot == 1;
        if (!MethodEmitter.$assertionsDisabled && !this.functionNode.needsCallee() && thisSlot != 0) {
            throw new AssertionError();
        }
        return slot == thisSlot;
    }
    
    MethodEmitter loadHandle(final String className, final String methodName, final String descName, final EnumSet<ClassEmitter.Flag> flags) {
        this.debug("load handle ");
        this.pushType(Type.OBJECT.ldc(this.method, new Handle(ClassEmitter.Flag.getValue(flags), className, methodName, descName)));
        return this;
    }
    
    private Symbol getCompilerConstantSymbol(final CompilerConstants cc) {
        return this.functionNode.getBody().getExistingSymbol(cc.symbolName());
    }
    
    boolean hasScope() {
        return this.getCompilerConstantSymbol(CompilerConstants.SCOPE).hasSlot();
    }
    
    MethodEmitter loadCompilerConstant(final CompilerConstants cc) {
        return this.loadCompilerConstant(cc, null);
    }
    
    MethodEmitter loadCompilerConstant(final CompilerConstants cc, final Type type) {
        if (cc == CompilerConstants.SCOPE && this.peekType() == Type.SCOPE) {
            this.dup();
            return this;
        }
        return this.load(this.getCompilerConstantSymbol(cc), (type != null) ? type : getCompilerConstantType(cc));
    }
    
    MethodEmitter loadScope() {
        return this.loadCompilerConstant(CompilerConstants.SCOPE).checkcast(Scope.class);
    }
    
    MethodEmitter setSplitState(final int state) {
        return this.loadScope().load(state).invoke(Scope.SET_SPLIT_STATE);
    }
    
    void storeCompilerConstant(final CompilerConstants cc) {
        this.storeCompilerConstant(cc, null);
    }
    
    void storeCompilerConstant(final CompilerConstants cc, final Type type) {
        final Symbol symbol = this.getCompilerConstantSymbol(cc);
        if (!symbol.hasSlot()) {
            return;
        }
        this.debug("store compiler constant ", symbol);
        this.store(symbol, (type != null) ? type : getCompilerConstantType(cc));
    }
    
    private static Type getCompilerConstantType(final CompilerConstants cc) {
        final Class<?> constantType = cc.type();
        assert constantType != null;
        return Type.typeFor(constantType);
    }
    
    MethodEmitter arrayload() {
        this.debug("Xaload");
        this.popType(Type.INT);
        this.pushType(this.popArray().aload(this.method));
        return this;
    }
    
    void arraystore() {
        this.debug("Xastore");
        final Type value = this.popType();
        final Type index = this.popType(Type.INT);
        assert index.isInteger() : "array index is not integer, but " + index;
        final ArrayType array = this.popArray();
        assert value.isEquivalentTo(array.getElementType()) : "Storing " + value + " into " + array;
        assert array.isObject();
        array.astore(this.method);
    }
    
    void store(final IdentNode ident) {
        final Type type = ident.getType();
        final Symbol symbol = ident.getSymbol();
        if (type == Type.UNDEFINED) {
            assert this.peekType() == Type.UNDEFINED;
            this.store(symbol, Type.OBJECT);
        }
        else {
            this.store(symbol, type);
        }
    }
    
    void closeLocalVariable(final Symbol symbol, final Label label) {
        final LocalVariableDef def = this.localVariableDefs.get(symbol);
        if (def != null) {
            this.endLocalValueDef(symbol, def, label.getLabel());
        }
        if (this.isReachable()) {
            this.markDeadLocalVariable(symbol);
        }
    }
    
    void markDeadLocalVariable(final Symbol symbol) {
        if (!symbol.isDead()) {
            this.markDeadSlots(symbol.getFirstSlot(), symbol.slotCount());
        }
    }
    
    void markDeadSlots(final int firstSlot, final int slotCount) {
        this.stack.markDeadLocalVariables(firstSlot, slotCount);
    }
    
    private void endLocalValueDef(final Symbol symbol, final LocalVariableDef def, final jdk.internal.org.objectweb.asm.Label label) {
        String name = symbol.getName();
        if (name.equals(CompilerConstants.THIS.symbolName())) {
            name = CompilerConstants.THIS_DEBUGGER.symbolName();
        }
        this.method.visitLocalVariable(name, def.type.getDescriptor(), null, def.label, label, symbol.getSlot(def.type));
    }
    
    void store(final Symbol symbol, final Type type) {
        this.store(symbol, type, true);
    }
    
    void store(final Symbol symbol, final Type type, final boolean onlySymbolLiveValue) {
        assert symbol != null : "No symbol to store";
        if (symbol.hasSlot()) {
            final boolean isLiveType = symbol.hasSlotFor(type);
            final LocalVariableDef existingDef = this.localVariableDefs.get(symbol);
            if (existingDef == null || existingDef.type != type) {
                final jdk.internal.org.objectweb.asm.Label here = new jdk.internal.org.objectweb.asm.Label();
                if (isLiveType) {
                    final LocalVariableDef newDef = new LocalVariableDef(here, type);
                    this.localVariableDefs.put(symbol, newDef);
                }
                this.method.visitLabel(here);
                if (existingDef != null) {
                    this.endLocalValueDef(symbol, existingDef, here);
                }
            }
            if (isLiveType) {
                final int slot = symbol.getSlot(type);
                this.debug("store symbol", symbol.getName(), " type=", type, " slot=", slot);
                this.storeHidden(type, slot, onlySymbolLiveValue);
            }
            else {
                if (onlySymbolLiveValue) {
                    this.markDeadLocalVariable(symbol);
                }
                this.debug("dead store symbol ", symbol.getName(), " type=", type);
                this.pop();
            }
        }
        else if (symbol.isParam()) {
            assert !symbol.isScope();
            assert this.functionNode.isVarArg() : "Non-vararg functions have slotted parameters";
            final int index = symbol.getFieldIndex();
            if (this.functionNode.needsArguments()) {
                this.convert(Type.OBJECT);
                this.debug("store symbol", symbol.getName(), " arguments index=", index);
                this.loadCompilerConstant(CompilerConstants.ARGUMENTS);
                this.load(index);
                ArgumentSetter.SET_ARGUMENT.invoke(this);
            }
            else {
                this.convert(Type.OBJECT);
                this.debug("store symbol", symbol.getName(), " array index=", index);
                this.loadCompilerConstant(CompilerConstants.VARARGS);
                this.load(index);
                ArgumentSetter.SET_ARRAY_ELEMENT.invoke(this);
            }
        }
        else {
            this.debug("dead store symbol ", symbol.getName(), " type=", type);
            this.pop();
        }
    }
    
    void storeHidden(final Type type, final int slot) {
        this.storeHidden(type, slot, true);
    }
    
    void storeHidden(final Type type, final int slot, final boolean onlyLiveSymbolValue) {
        this.explicitStore(type, slot);
        this.stack.onLocalStore(type, slot, onlyLiveSymbolValue);
    }
    
    void storeTemp(final Type type, final int slot) {
        this.explicitStore(type, slot);
        this.defineTemporaryLocalVariable(slot, slot + type.getSlots());
        this.onLocalStore(type, slot);
    }
    
    void onLocalStore(final Type type, final int slot) {
        this.stack.onLocalStore(type, slot, true);
    }
    
    private void explicitStore(final Type type, final int slot) {
        assert slot != -1;
        this.debug("explicit store", type, slot);
        this.popType(type);
        type.store(this.method, slot);
    }
    
    void defineBlockLocalVariable(final int fromSlot, final int toSlot) {
        this.stack.defineBlockLocalVariable(fromSlot, toSlot);
    }
    
    void defineTemporaryLocalVariable(final int fromSlot, final int toSlot) {
        this.stack.defineTemporaryLocalVariable(fromSlot, toSlot);
    }
    
    int defineTemporaryLocalVariable(final int width) {
        return this.stack.defineTemporaryLocalVariable(width);
    }
    
    void undefineLocalVariables(final int fromSlot, final boolean canTruncateSymbol) {
        if (this.isReachable()) {
            this.stack.undefineLocalVariables(fromSlot, canTruncateSymbol);
        }
    }
    
    List<Type> getLocalVariableTypes() {
        return this.stack.localVariableTypes;
    }
    
    List<Type> getWidestLiveLocals(final List<Type> localTypes) {
        return this.stack.getWidestLiveLocals(localTypes);
    }
    
    String markSymbolBoundariesInLvarTypesDescriptor(final String lvarDescriptor) {
        return this.stack.markSymbolBoundariesInLvarTypesDescriptor(lvarDescriptor);
    }
    
    void iinc(final int slot, final int increment) {
        this.debug("iinc");
        this.method.visitIincInsn(slot, increment);
    }
    
    public void athrow() {
        this.debug("athrow");
        final Type receiver = this.popType(Type.OBJECT);
        assert Throwable.class.isAssignableFrom(receiver.getTypeClass()) : receiver.getTypeClass();
        this.method.visitInsn(191);
        this.doesNotContinueSequentially();
    }
    
    MethodEmitter _instanceof(final String classDescriptor) {
        this.debug("instanceof", classDescriptor);
        this.popType(Type.OBJECT);
        this.method.visitTypeInsn(193, classDescriptor);
        this.pushType(Type.INT);
        return this;
    }
    
    MethodEmitter _instanceof(final Class<?> clazz) {
        return this._instanceof(CompilerConstants.className(clazz));
    }
    
    MethodEmitter checkcast(final String classDescriptor) {
        this.debug("checkcast", classDescriptor);
        assert this.peekType().isObject();
        this.method.visitTypeInsn(192, classDescriptor);
        return this;
    }
    
    MethodEmitter checkcast(final Class<?> clazz) {
        return this.checkcast(CompilerConstants.className(clazz));
    }
    
    MethodEmitter newarray(final ArrayType arrayType) {
        this.debug("newarray ", "arrayType=", arrayType);
        this.popType(Type.INT);
        this.pushType(arrayType.newarray(this.method));
        return this;
    }
    
    MethodEmitter multinewarray(final ArrayType arrayType, final int dims) {
        this.debug("multianewarray ", arrayType, dims);
        for (int i = 0; i < dims; ++i) {
            this.popType(Type.INT);
        }
        this.pushType(arrayType.newarray(this.method, dims));
        return this;
    }
    
    private Type fixParamStack(final String signature) {
        final Type[] params = Type.getMethodArguments(signature);
        for (int i = params.length - 1; i >= 0; --i) {
            this.popType(params[i]);
        }
        final Type returnType = Type.getMethodReturnType(signature);
        return returnType;
    }
    
    MethodEmitter invoke(final CompilerConstants.Call call) {
        return call.invoke(this);
    }
    
    private MethodEmitter invoke(final int opcode, final String className, final String methodName, final String methodDescriptor, final boolean hasReceiver) {
        final Type returnType = this.fixParamStack(methodDescriptor);
        if (hasReceiver) {
            this.popType(Type.OBJECT);
        }
        this.method.visitMethodInsn(opcode, className, methodName, methodDescriptor, opcode == 185);
        if (returnType != null) {
            this.pushType(returnType);
        }
        return this;
    }
    
    MethodEmitter invokespecial(final String className, final String methodName, final String methodDescriptor) {
        this.debug("invokespecial", className, ".", methodName, methodDescriptor);
        return this.invoke(183, className, methodName, methodDescriptor, true);
    }
    
    MethodEmitter invokevirtual(final String className, final String methodName, final String methodDescriptor) {
        this.debug("invokevirtual", className, ".", methodName, methodDescriptor, " ", this.stack);
        return this.invoke(182, className, methodName, methodDescriptor, true);
    }
    
    MethodEmitter invokestatic(final String className, final String methodName, final String methodDescriptor) {
        this.debug("invokestatic", className, ".", methodName, methodDescriptor);
        this.invoke(184, className, methodName, methodDescriptor, false);
        return this;
    }
    
    MethodEmitter invokestatic(final String className, final String methodName, final String methodDescriptor, final Type returnType) {
        this.invokestatic(className, methodName, methodDescriptor);
        this.popType();
        this.pushType(returnType);
        return this;
    }
    
    MethodEmitter invokeinterface(final String className, final String methodName, final String methodDescriptor) {
        this.debug("invokeinterface", className, ".", methodName, methodDescriptor);
        return this.invoke(185, className, methodName, methodDescriptor, true);
    }
    
    static jdk.internal.org.objectweb.asm.Label[] getLabels(final Label... table) {
        final jdk.internal.org.objectweb.asm.Label[] internalLabels = new jdk.internal.org.objectweb.asm.Label[table.length];
        for (int i = 0; i < table.length; ++i) {
            internalLabels[i] = table[i].getLabel();
        }
        return internalLabels;
    }
    
    void lookupswitch(final Label defaultLabel, final int[] values, final Label... table) {
        this.debug("lookupswitch", this.peekType());
        this.adjustStackForSwitch(defaultLabel, table);
        this.method.visitLookupSwitchInsn(defaultLabel.getLabel(), values, getLabels(table));
        this.doesNotContinueSequentially();
    }
    
    void tableswitch(final int lo, final int hi, final Label defaultLabel, final Label... table) {
        this.debug("tableswitch", this.peekType());
        this.adjustStackForSwitch(defaultLabel, table);
        this.method.visitTableSwitchInsn(lo, hi, defaultLabel.getLabel(), getLabels(table));
        this.doesNotContinueSequentially();
    }
    
    private void adjustStackForSwitch(final Label defaultLabel, final Label... table) {
        this.popType(Type.INT);
        this.joinTo(defaultLabel);
        for (final Label label : table) {
            this.joinTo(label);
        }
    }
    
    void conditionalJump(final Condition cond, final Label trueLabel) {
        this.conditionalJump(cond, cond != Condition.GT && cond != Condition.GE, trueLabel);
    }
    
    void conditionalJump(final Condition cond, final boolean isCmpG, final Label trueLabel) {
        if (this.peekType().isCategory2()) {
            this.debug("[ld]cmp isCmpG=", isCmpG);
            this.pushType(this.get2n().cmp(this.method, isCmpG));
            this.jump(Condition.toUnary(cond), trueLabel, 1);
        }
        else {
            this.debug("if", cond);
            this.jump(Condition.toBinary(cond, this.peekType().isObject()), trueLabel, 2);
        }
    }
    
    void _return(final Type type) {
        this.debug("return", type);
        assert this.stack.size() == 1 : "Only return value on stack allowed at return point - depth=" + this.stack.size() + " stack = " + this.stack;
        final Type stackType = this.peekType();
        if (!Type.areEquivalent(type, stackType)) {
            this.convert(type);
        }
        this.popType(type)._return(this.method);
        this.doesNotContinueSequentially();
    }
    
    void _return() {
        this._return(this.peekType());
    }
    
    void returnVoid() {
        this.debug("return [void]");
        assert this.stack.isEmpty() : this.stack;
        this.method.visitInsn(177);
        this.doesNotContinueSequentially();
    }
    
    MethodEmitter cmp(final boolean isCmpG) {
        this.pushType(this.get2n().cmp(this.method, isCmpG));
        return this;
    }
    
    private void jump(final int opcode, final Label label, final int n) {
        for (int i = 0; i < n; ++i) {
            assert this.peekType().isInteger() || this.peekType().isObject() : "expecting integer type or object for jump, but found " + this.peekType();
            this.popType();
        }
        this.joinTo(label);
        this.method.visitJumpInsn(opcode, label.getLabel());
    }
    
    void if_acmpeq(final Label label) {
        this.debug("if_acmpeq", label);
        this.jump(165, label, 2);
    }
    
    void if_acmpne(final Label label) {
        this.debug("if_acmpne", label);
        this.jump(166, label, 2);
    }
    
    void ifnull(final Label label) {
        this.debug("ifnull", label);
        this.jump(198, label, 1);
    }
    
    void ifnonnull(final Label label) {
        this.debug("ifnonnull", label);
        this.jump(199, label, 1);
    }
    
    void ifeq(final Label label) {
        this.debug("ifeq ", label);
        this.jump(153, label, 1);
    }
    
    void if_icmpeq(final Label label) {
        this.debug("if_icmpeq", label);
        this.jump(159, label, 2);
    }
    
    void ifne(final Label label) {
        this.debug("ifne", label);
        this.jump(154, label, 1);
    }
    
    void if_icmpne(final Label label) {
        this.debug("if_icmpne", label);
        this.jump(160, label, 2);
    }
    
    void iflt(final Label label) {
        this.debug("iflt", label);
        this.jump(155, label, 1);
    }
    
    void if_icmplt(final Label label) {
        this.debug("if_icmplt", label);
        this.jump(161, label, 2);
    }
    
    void ifle(final Label label) {
        this.debug("ifle", label);
        this.jump(158, label, 1);
    }
    
    void if_icmple(final Label label) {
        this.debug("if_icmple", label);
        this.jump(164, label, 2);
    }
    
    void ifgt(final Label label) {
        this.debug("ifgt", label);
        this.jump(157, label, 1);
    }
    
    void if_icmpgt(final Label label) {
        this.debug("if_icmpgt", label);
        this.jump(163, label, 2);
    }
    
    void ifge(final Label label) {
        this.debug("ifge", label);
        this.jump(156, label, 1);
    }
    
    void if_icmpge(final Label label) {
        this.debug("if_icmpge", label);
        this.jump(162, label, 2);
    }
    
    void _goto(final Label label) {
        this.debug("goto", label);
        this.jump(167, label, 0);
        this.doesNotContinueSequentially();
    }
    
    void gotoLoopStart(final Label loopStart) {
        this.debug("goto (loop)", loopStart);
        this.jump(167, loopStart, 0);
    }
    
    void uncheckedGoto(final Label target) {
        this.method.visitJumpInsn(167, target.getLabel());
    }
    
    void canThrow(final Label catchLabel) {
        catchLabel.joinFromTry(this.stack, false);
    }
    
    private void joinTo(final Label label) {
        assert this.isReachable();
        label.joinFrom(this.stack);
    }
    
    void label(final Label label) {
        this.breakLabel(label, -1);
    }
    
    void breakLabel(final Label label, final int liveLocals) {
        if (!this.isReachable()) {
            assert label.getStack() == null != label.isReachable();
        }
        else {
            this.joinTo(label);
        }
        final Label.Stack labelStack = label.getStack();
        this.stack = ((labelStack == null) ? null : labelStack.clone());
        if (this.stack != null && label.isBreakTarget() && liveLocals != -1) {
            assert this.stack.firstTemp >= liveLocals;
            this.stack.firstTemp = liveLocals;
        }
        this.debug_label(label);
        this.method.visitLabel(label.getLabel());
    }
    
    MethodEmitter convert(final Type to) {
        final Type from = this.peekType();
        final Type type = from.convert(this.method, to);
        if (type != null) {
            if (!from.isEquivalentTo(to)) {
                this.debug("convert", from, "->", to);
            }
            if (type != from) {
                final int l0 = this.stack.getTopLocalLoad();
                this.popType();
                this.pushType(type);
                if (!from.isObject()) {
                    this.stack.markLocalLoad(l0);
                }
            }
        }
        return this;
    }
    
    private Type get2() {
        final Type p0 = this.popType();
        final Type p2 = this.popType();
        assert p0.isEquivalentTo(p2) : "expecting equivalent types on stack but got " + p0 + " and " + p2;
        return p0;
    }
    
    private BitwiseType get2i() {
        final BitwiseType p0 = this.popBitwise();
        final BitwiseType p2 = this.popBitwise();
        assert p0.isEquivalentTo(p2) : "expecting equivalent types on stack but got " + p0 + " and " + p2;
        return p0;
    }
    
    private NumericType get2n() {
        final NumericType p0 = this.popNumeric();
        final NumericType p2 = this.popNumeric();
        assert p0.isEquivalentTo(p2) : "expecting equivalent types on stack but got " + p0 + " and " + p2;
        return p0;
    }
    
    MethodEmitter add(final int programPoint) {
        this.debug("add");
        this.pushType(this.get2().add(this.method, programPoint));
        return this;
    }
    
    MethodEmitter sub(final int programPoint) {
        this.debug("sub");
        this.pushType(this.get2n().sub(this.method, programPoint));
        return this;
    }
    
    MethodEmitter mul(final int programPoint) {
        this.debug("mul ");
        this.pushType(this.get2n().mul(this.method, programPoint));
        return this;
    }
    
    MethodEmitter div(final int programPoint) {
        this.debug("div");
        this.pushType(this.get2n().div(this.method, programPoint));
        return this;
    }
    
    MethodEmitter rem(final int programPoint) {
        this.debug("rem");
        this.pushType(this.get2n().rem(this.method, programPoint));
        return this;
    }
    
    protected Type[] getTypesFromStack(final int count) {
        return this.stack.getTopTypes(count);
    }
    
    int[] getLocalLoadsOnStack(final int from, final int to) {
        return this.stack.getLocalLoads(from, to);
    }
    
    int getStackSize() {
        return this.stack.size();
    }
    
    int getFirstTemp() {
        return this.stack.firstTemp;
    }
    
    int getUsedSlotsWithLiveTemporaries() {
        return this.stack.getUsedSlotsWithLiveTemporaries();
    }
    
    private String getDynamicSignature(final Type returnType, final int argCount) {
        final Type[] paramTypes = new Type[argCount];
        int pos = 0;
        for (int i = argCount - 1; i >= 0; --i) {
            Type pt = this.stack.peek(pos++);
            if (ScriptObject.class.isAssignableFrom(pt.getTypeClass()) && !NativeArray.class.isAssignableFrom(pt.getTypeClass())) {
                pt = Type.SCRIPT_OBJECT;
            }
            paramTypes[i] = pt;
        }
        final String descriptor = Type.getMethodDescriptor(returnType, paramTypes);
        for (int j = 0; j < argCount; ++j) {
            this.popType(paramTypes[argCount - j - 1]);
        }
        return descriptor;
    }
    
    MethodEmitter invalidateSpecialName(final String name) {
        switch (name) {
            case "apply":
            case "call": {
                this.debug("invalidate_name", "name=", name);
                this.load("Function");
                this.invoke(ScriptRuntime.INVALIDATE_RESERVED_BUILTIN_NAME);
                break;
            }
        }
        return this;
    }
    
    MethodEmitter dynamicNew(final int argCount, final int flags) {
        return this.dynamicNew(argCount, flags, null);
    }
    
    MethodEmitter dynamicNew(final int argCount, final int flags, final String msg) {
        assert !isOptimistic(flags);
        this.debug("dynamic_new", "argcount=", argCount);
        final String signature = this.getDynamicSignature(Type.OBJECT, argCount);
        this.method.visitInvokeDynamicInsn((msg != null && msg.length() < 32768) ? ("dyn:new:" + NameCodec.encode(msg)) : "dyn:new", signature, MethodEmitter.LINKERBOOTSTRAP, flags);
        this.pushType(Type.OBJECT);
        return this;
    }
    
    MethodEmitter dynamicCall(final Type returnType, final int argCount, final int flags) {
        return this.dynamicCall(returnType, argCount, flags, null);
    }
    
    MethodEmitter dynamicCall(final Type returnType, final int argCount, final int flags, final String msg) {
        this.debug("dynamic_call", "args=", argCount, "returnType=", returnType);
        final String signature = this.getDynamicSignature(returnType, argCount);
        this.debug("   signature", signature);
        this.method.visitInvokeDynamicInsn((msg != null && msg.length() < 32768) ? ("dyn:call:" + NameCodec.encode(msg)) : "dyn:call", signature, MethodEmitter.LINKERBOOTSTRAP, flags);
        this.pushType(returnType);
        return this;
    }
    
    MethodEmitter dynamicArrayPopulatorCall(final int argCount, final int startIndex) {
        this.debug("populate_array", "args=", argCount, "startIndex=", startIndex);
        final String signature = this.getDynamicSignature(Type.OBJECT_ARRAY, argCount);
        this.method.visitInvokeDynamicInsn("populateArray", signature, MethodEmitter.POPULATE_ARRAY_BOOTSTRAP, startIndex);
        this.pushType(Type.OBJECT_ARRAY);
        return this;
    }
    
    MethodEmitter dynamicGet(final Type valueType, final String name, final int flags, final boolean isMethod, final boolean isIndex) {
        if (name.length() > 32768) {
            return this.load(name).dynamicGetIndex(valueType, flags, isMethod);
        }
        this.debug("dynamic_get", name, valueType, getProgramPoint(flags));
        Type type = valueType;
        if (type.isObject() || type.isBoolean()) {
            type = Type.OBJECT;
        }
        this.popType(Type.SCOPE);
        this.method.visitInvokeDynamicInsn(dynGetOperation(isMethod, isIndex) + ':' + NameCodec.encode(name), Type.getMethodDescriptor(type, Type.OBJECT), MethodEmitter.LINKERBOOTSTRAP, flags);
        this.pushType(type);
        this.convert(valueType);
        return this;
    }
    
    void dynamicSet(final String name, final int flags, final boolean isIndex) {
        if (name.length() > 32768) {
            this.load(name).swap().dynamicSetIndex(flags);
            return;
        }
        assert !isOptimistic(flags);
        this.debug("dynamic_set", name, this.peekType());
        Type type = this.peekType();
        if (type.isObject() || type.isBoolean()) {
            type = Type.OBJECT;
            this.convert(Type.OBJECT);
        }
        this.popType(type);
        this.popType(Type.SCOPE);
        this.method.visitInvokeDynamicInsn(dynSetOperation(isIndex) + ':' + NameCodec.encode(name), CompilerConstants.methodDescriptor(Void.TYPE, Object.class, type.getTypeClass()), MethodEmitter.LINKERBOOTSTRAP, flags);
    }
    
    MethodEmitter dynamicGetIndex(final Type result, final int flags, final boolean isMethod) {
        if (!MethodEmitter.$assertionsDisabled && !result.getTypeClass().isPrimitive() && result.getTypeClass() != Object.class) {
            throw new AssertionError();
        }
        this.debug("dynamic_get_index", this.peekType(1), "[", this.peekType(), "]", getProgramPoint(flags));
        Type resultType = result;
        if (result.isBoolean()) {
            resultType = Type.OBJECT;
        }
        Type index = this.peekType();
        if (index.isObject() || index.isBoolean()) {
            index = Type.OBJECT;
            this.convert(Type.OBJECT);
        }
        this.popType();
        this.popType(Type.OBJECT);
        final String signature = Type.getMethodDescriptor(resultType, Type.OBJECT, index);
        this.method.visitInvokeDynamicInsn(dynGetOperation(isMethod, true), signature, MethodEmitter.LINKERBOOTSTRAP, flags);
        this.pushType(resultType);
        if (result.isBoolean()) {
            this.convert(Type.BOOLEAN);
        }
        return this;
    }
    
    private static String getProgramPoint(final int flags) {
        if ((flags & 0x8) == 0x0) {
            return "";
        }
        return "pp=" + String.valueOf((flags & 0xFFFFF800) >> 11);
    }
    
    void dynamicSetIndex(final int flags) {
        assert !isOptimistic(flags);
        this.debug("dynamic_set_index", this.peekType(2), "[", this.peekType(1), "] =", this.peekType());
        Type value = this.peekType();
        if (value.isObject() || value.isBoolean()) {
            value = Type.OBJECT;
            this.convert(Type.OBJECT);
        }
        this.popType();
        Type index = this.peekType();
        if (index.isObject() || index.isBoolean()) {
            index = Type.OBJECT;
            this.convert(Type.OBJECT);
        }
        this.popType(index);
        final Type receiver = this.popType(Type.OBJECT);
        assert receiver.isObject();
        this.method.visitInvokeDynamicInsn("dyn:setElem|setProp", CompilerConstants.methodDescriptor(Void.TYPE, receiver.getTypeClass(), index.getTypeClass(), value.getTypeClass()), MethodEmitter.LINKERBOOTSTRAP, flags);
    }
    
    MethodEmitter loadKey(final Object key) {
        if (key instanceof IdentNode) {
            this.method.visitLdcInsn(((IdentNode)key).getName());
        }
        else if (key instanceof LiteralNode) {
            this.method.visitLdcInsn(((LiteralNode)key).getString());
        }
        else {
            this.method.visitLdcInsn(JSType.toString(key));
        }
        this.pushType(Type.OBJECT);
        return this;
    }
    
    private static Type fieldType(final String desc) {
        switch (desc) {
            case "Z":
            case "B":
            case "C":
            case "S":
            case "I": {
                return Type.INT;
            }
            case "F": {
                assert false;
                return Type.NUMBER;
            }
            case "D": {
                return Type.NUMBER;
            }
            case "J": {
                return Type.LONG;
            }
            default: {
                assert desc.startsWith("[") || desc.startsWith("L") : desc + " is not an object type";
                switch (desc.charAt(0)) {
                    case 'L': {
                        return Type.OBJECT;
                    }
                    case '[': {
                        return Type.typeFor(Array.newInstance(fieldType(desc.substring(1)).getTypeClass(), 0).getClass());
                    }
                    default: {
                        assert false;
                        return Type.OBJECT;
                    }
                }
                break;
            }
        }
    }
    
    MethodEmitter getField(final CompilerConstants.FieldAccess fa) {
        return fa.get(this);
    }
    
    void putField(final CompilerConstants.FieldAccess fa) {
        fa.put(this);
    }
    
    MethodEmitter getField(final String className, final String fieldName, final String fieldDescriptor) {
        this.debug("getfield", "receiver=", this.peekType(), className, ".", fieldName, fieldDescriptor);
        final Type receiver = this.popType();
        assert receiver.isObject();
        this.method.visitFieldInsn(180, className, fieldName, fieldDescriptor);
        this.pushType(fieldType(fieldDescriptor));
        return this;
    }
    
    MethodEmitter getStatic(final String className, final String fieldName, final String fieldDescriptor) {
        this.debug("getstatic", className, ".", fieldName, ".", fieldDescriptor);
        this.method.visitFieldInsn(178, className, fieldName, fieldDescriptor);
        this.pushType(fieldType(fieldDescriptor));
        return this;
    }
    
    void putField(final String className, final String fieldName, final String fieldDescriptor) {
        this.debug("putfield", "receiver=", this.peekType(1), "value=", this.peekType());
        this.popType(fieldType(fieldDescriptor));
        this.popType(Type.OBJECT);
        this.method.visitFieldInsn(181, className, fieldName, fieldDescriptor);
    }
    
    void putStatic(final String className, final String fieldName, final String fieldDescriptor) {
        this.debug("putfield", "value=", this.peekType());
        this.popType(fieldType(fieldDescriptor));
        this.method.visitFieldInsn(179, className, fieldName, fieldDescriptor);
    }
    
    void lineNumber(final int line) {
        if (this.context.getEnv()._debug_lines) {
            this.debug_label("[LINE]", line);
            final jdk.internal.org.objectweb.asm.Label l = new jdk.internal.org.objectweb.asm.Label();
            this.method.visitLabel(l);
            this.method.visitLineNumber(line, l);
        }
    }
    
    void beforeJoinPoint(final JoinPredecessor joinPredecessor) {
        for (LocalVariableConversion next = joinPredecessor.getLocalVariableConversion(); next != null; next = next.getNext()) {
            final Symbol symbol = next.getSymbol();
            if (next.isLive()) {
                this.emitLocalVariableConversion(next, true);
            }
            else {
                this.markDeadLocalVariable(symbol);
            }
        }
    }
    
    void beforeTry(final TryNode tryNode, final Label recovery) {
        for (LocalVariableConversion next = tryNode.getLocalVariableConversion(); next != null; next = next.getNext()) {
            if (next.isLive()) {
                final Type to = this.emitLocalVariableConversion(next, false);
                recovery.getStack().onLocalStore(to, next.getSymbol().getSlot(to), true);
            }
        }
    }
    
    private static String dynGetOperation(final boolean isMethod, final boolean isIndex) {
        if (isMethod) {
            return isIndex ? "dyn:getMethod|getElem|getProp" : "dyn:getMethod|getProp|getElem";
        }
        return isIndex ? "dyn:getElem|getProp|getMethod" : "dyn:getProp|getElem|getMethod";
    }
    
    private static String dynSetOperation(final boolean isIndex) {
        return isIndex ? "dyn:setElem|setProp" : "dyn:setProp|setElem";
    }
    
    private Type emitLocalVariableConversion(final LocalVariableConversion conversion, final boolean onlySymbolLiveValue) {
        final Type from = conversion.getFrom();
        final Type to = conversion.getTo();
        final Symbol symbol = conversion.getSymbol();
        assert symbol.isBytecodeLocal();
        if (from == Type.UNDEFINED) {
            this.loadUndefined(to);
        }
        else {
            this.load(symbol, from).convert(to);
        }
        this.store(symbol, to, onlySymbolLiveValue);
        return to;
    }
    
    void print() {
        this.getField(this.ERR_STREAM);
        this.swap();
        this.convert(Type.OBJECT);
        this.invoke(this.PRINT);
    }
    
    void println() {
        this.getField(this.ERR_STREAM);
        this.swap();
        this.convert(Type.OBJECT);
        this.invoke(this.PRINTLN);
    }
    
    void print(final String string) {
        this.getField(this.ERR_STREAM);
        this.load(string);
        this.invoke(this.PRINT);
    }
    
    void println(final String string) {
        this.getField(this.ERR_STREAM);
        this.load(string);
        this.invoke(this.PRINTLN);
    }
    
    void stacktrace() {
        this._new(Throwable.class);
        this.dup();
        this.invoke(CompilerConstants.constructorNoLookup(Throwable.class));
        this.invoke(this.PRINT_STACKTRACE);
    }
    
    private void debug(final Object... args) {
        if (this.debug) {
            this.debug(30, args);
        }
    }
    
    private void debug(final String arg) {
        if (this.debug) {
            this.debug((Object)30, arg);
        }
    }
    
    private void debug(final Object arg0, final Object arg1) {
        if (this.debug) {
            this.debug(30, arg0, arg1);
        }
    }
    
    private void debug(final Object arg0, final Object arg1, final Object arg2) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2);
        }
    }
    
    private void debug(final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2, arg3);
        }
    }
    
    private void debug(final Object arg0, final Object arg1, final Object arg2, final Object arg3, final Object arg4) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2, arg3, arg4);
        }
    }
    
    private void debug(final Object arg0, final Object arg1, final Object arg2, final Object arg3, final Object arg4, final Object arg5) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2, arg3, arg4, arg5);
        }
    }
    
    private void debug(final Object arg0, final Object arg1, final Object arg2, final Object arg3, final Object arg4, final Object arg5, final Object arg6) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
    }
    
    private void debug_label(final Object... args) {
        if (this.debug) {
            this.debug(22, args);
        }
    }
    
    private void debug(final int padConstant, final Object... args) {
        if (this.debug) {
            final StringBuilder sb = new StringBuilder();
            sb.append('#');
            sb.append(++MethodEmitter.linePrefix);
            for (int pad = 5 - sb.length(); pad > 0; --pad) {
                sb.append(' ');
            }
            if (this.isReachable() && !this.stack.isEmpty()) {
                sb.append("{");
                sb.append(this.stack.size());
                sb.append(":");
                for (int pos = 0; pos < this.stack.size(); ++pos) {
                    final Type t = this.stack.peek(pos);
                    if (t == Type.SCOPE) {
                        sb.append("scope");
                    }
                    else if (t == Type.THIS) {
                        sb.append("this");
                    }
                    else if (t.isObject()) {
                        String desc;
                        int i;
                        for (desc = t.getDescriptor(), i = 0; desc.charAt(i) == '[' && i < desc.length(); ++i) {
                            sb.append('[');
                        }
                        desc = desc.substring(i);
                        final int slash = desc.lastIndexOf(47);
                        if (slash != -1) {
                            desc = desc.substring(slash + 1, desc.length() - 1);
                        }
                        if ("Object".equals(desc)) {
                            sb.append('O');
                        }
                        else {
                            sb.append(desc);
                        }
                    }
                    else {
                        sb.append(t.getDescriptor());
                    }
                    final int loadIndex = this.stack.localLoads[this.stack.sp - 1 - pos];
                    if (loadIndex != -1) {
                        sb.append('(').append(loadIndex).append(')');
                    }
                    if (pos + 1 < this.stack.size()) {
                        sb.append(' ');
                    }
                }
                sb.append('}');
                sb.append(' ');
            }
            for (int pad = padConstant - sb.length(); pad > 0; --pad) {
                sb.append(' ');
            }
            for (final Object arg : args) {
                sb.append(arg);
                sb.append(' ');
            }
            if (this.context.getEnv() != null) {
                this.log.info(sb);
                if (MethodEmitter.DEBUG_TRACE_LINE == MethodEmitter.linePrefix) {
                    new Throwable().printStackTrace(this.log.getOutputStream());
                }
            }
        }
    }
    
    void setFunctionNode(final FunctionNode functionNode) {
        this.functionNode = functionNode;
    }
    
    void setPreventUndefinedLoad() {
        this.preventUndefinedLoad = true;
    }
    
    private static boolean isOptimistic(final int flags) {
        return (flags & 0x8) != 0x0;
    }
    
    static {
        final String tl = Options.getStringProperty("nashorn.codegen.debug.trace", "-1");
        int line = -1;
        try {
            line = Integer.parseInt(tl);
        }
        catch (NumberFormatException ex) {}
        DEBUG_TRACE_LINE = line;
        LINKERBOOTSTRAP = new Handle(6, Bootstrap.BOOTSTRAP.className(), Bootstrap.BOOTSTRAP.name(), Bootstrap.BOOTSTRAP.descriptor());
        POPULATE_ARRAY_BOOTSTRAP = new Handle(6, RewriteException.BOOTSTRAP.className(), RewriteException.BOOTSTRAP.name(), RewriteException.BOOTSTRAP.descriptor());
        MethodEmitter.linePrefix = 0;
    }
    
    private static class LocalVariableDef
    {
        private final jdk.internal.org.objectweb.asm.Label label;
        private final Type type;
        
        LocalVariableDef(final jdk.internal.org.objectweb.asm.Label label, final Type type) {
            this.label = label;
            this.type = type;
        }
    }
}
