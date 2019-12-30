// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.Arrays;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.ir.FunctionNode;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.codegen.types.Type;

public final class TypeMap
{
    private final int functionNodeId;
    private final Type[] paramTypes;
    private final Type returnType;
    private final boolean needsCallee;
    
    public TypeMap(final int functionNodeId, final MethodType type, final boolean needsCallee) {
        final Type[] types = new Type[type.parameterCount()];
        int pos = 0;
        for (final Class<?> p : type.parameterArray()) {
            types[pos++] = Type.typeFor(p);
        }
        this.functionNodeId = functionNodeId;
        this.paramTypes = types;
        this.returnType = Type.typeFor(type.returnType());
        this.needsCallee = needsCallee;
    }
    
    public Type[] getParameterTypes(final int functionNodeId) {
        assert this.functionNodeId == functionNodeId;
        return this.paramTypes.clone();
    }
    
    MethodType getCallSiteType(final FunctionNode functionNode) {
        assert this.functionNodeId == functionNode.getId();
        final Type[] types = this.paramTypes;
        MethodType mt = MethodType.methodType(this.returnType.getTypeClass());
        if (this.needsCallee) {
            mt = mt.appendParameterTypes(ScriptFunction.class);
        }
        mt = mt.appendParameterTypes(Object.class);
        for (final Type type : types) {
            if (type == null) {
                return null;
            }
            mt = mt.appendParameterTypes(type.getTypeClass());
        }
        return mt;
    }
    
    public boolean needsCallee() {
        return this.needsCallee;
    }
    
    Type get(final FunctionNode functionNode, final int pos) {
        assert this.functionNodeId == functionNode.getId();
        final Type[] types = this.paramTypes;
        assert pos < types.length : "fn = " + functionNode.getId() + " types=" + Arrays.toString(types) + " || pos=" + pos + " >= length=" + types.length + " in " + this;
        if (types != null && pos < types.length) {
            return types[pos];
        }
        return null;
    }
    
    @Override
    public String toString() {
        return this.toString("");
    }
    
    String toString(final String prefix) {
        final StringBuilder sb = new StringBuilder();
        final int id = this.functionNodeId;
        sb.append(prefix).append('\t');
        sb.append("function ").append(id).append('\n');
        sb.append(prefix).append("\t\tparamTypes=");
        sb.append(Arrays.toString(this.paramTypes));
        sb.append('\n');
        sb.append(prefix).append("\t\treturnType=");
        final Type ret = this.returnType;
        sb.append((ret == null) ? "N/A" : ret);
        sb.append('\n');
        return sb.toString();
    }
}
