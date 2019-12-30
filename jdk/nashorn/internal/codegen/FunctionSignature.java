// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.Iterator;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.lookup.Lookup;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.ir.Expression;
import java.util.List;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.codegen.types.Type;

public final class FunctionSignature
{
    private final Type[] paramTypes;
    private final Type returnType;
    private final String descriptor;
    private final MethodType methodType;
    
    public FunctionSignature(final boolean hasSelf, final boolean hasCallee, final Type retType, final List<? extends Expression> args) {
        this(hasSelf, hasCallee, retType, typeArray(args));
    }
    
    public FunctionSignature(final boolean hasSelf, final boolean hasCallee, final Type retType, final int nArgs) {
        this(hasSelf, hasCallee, retType, objectArgs(nArgs));
    }
    
    private FunctionSignature(final boolean hasSelf, final boolean hasCallee, final Type retType, final Type... argTypes) {
        int count = 1;
        boolean isVarArg;
        if (argTypes == null) {
            isVarArg = true;
        }
        else {
            isVarArg = (argTypes.length > 250);
            count = (isVarArg ? 1 : argTypes.length);
        }
        if (hasCallee) {
            ++count;
        }
        if (hasSelf) {
            ++count;
        }
        this.paramTypes = new Type[count];
        int next = 0;
        if (hasCallee) {
            this.paramTypes[next++] = Type.typeFor(ScriptFunction.class);
        }
        if (hasSelf) {
            this.paramTypes[next++] = Type.OBJECT;
        }
        if (isVarArg) {
            this.paramTypes[next] = Type.OBJECT_ARRAY;
        }
        else if (argTypes != null) {
            int j = 0;
            while (next < count) {
                final Type type = argTypes[j++];
                this.paramTypes[next++] = (type.isObject() ? Type.OBJECT : type);
            }
        }
        else {
            assert false : "isVarArgs cannot be false when argTypes are null";
        }
        this.returnType = retType;
        this.descriptor = Type.getMethodDescriptor(this.returnType, this.paramTypes);
        final List<Class<?>> paramTypeList = new ArrayList<Class<?>>();
        for (final Type paramType : this.paramTypes) {
            paramTypeList.add(paramType.getTypeClass());
        }
        this.methodType = Lookup.MH.type(this.returnType.getTypeClass(), (Class<?>[])paramTypeList.toArray(new Class[this.paramTypes.length]));
    }
    
    public FunctionSignature(final FunctionNode functionNode) {
        this(true, functionNode.needsCallee(), functionNode.getReturnType(), (functionNode.isVarArg() && !functionNode.isProgram()) ? null : functionNode.getParameters());
    }
    
    private static Type[] typeArray(final List<? extends Expression> args) {
        if (args == null) {
            return null;
        }
        final Type[] typeArray = new Type[args.size()];
        int pos = 0;
        for (final Expression arg : args) {
            typeArray[pos++] = arg.getType();
        }
        return typeArray;
    }
    
    @Override
    public String toString() {
        return this.descriptor;
    }
    
    public int size() {
        return this.paramTypes.length;
    }
    
    public Type[] getParamTypes() {
        return this.paramTypes.clone();
    }
    
    public MethodType getMethodType() {
        return this.methodType;
    }
    
    public Type getReturnType() {
        return this.returnType;
    }
    
    private static Type[] objectArgs(final int nArgs) {
        final Type[] array = new Type[nArgs];
        for (int i = 0; i < nArgs; ++i) {
            array[i] = Type.OBJECT;
        }
        return array;
    }
}
