// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.io.DataInput;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.DataOutput;
import java.io.ObjectOutputStream;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.codegen.FunctionSignature;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.codegen.types.Type;
import java.util.Map;
import java.lang.invoke.MethodType;
import java.io.Serializable;

public final class FunctionInitializer implements Serializable
{
    private final String className;
    private final MethodType methodType;
    private final int flags;
    private transient Map<Integer, Type> invalidatedProgramPoints;
    private transient Class<?> code;
    private static final long serialVersionUID = -5420835725902966692L;
    
    public FunctionInitializer(final FunctionNode functionNode) {
        this(functionNode, null);
    }
    
    public FunctionInitializer(final FunctionNode functionNode, final Map<Integer, Type> invalidatedProgramPoints) {
        this.className = functionNode.getCompileUnit().getUnitClassName();
        this.methodType = new FunctionSignature(functionNode).getMethodType();
        this.flags = functionNode.getFlags();
        this.invalidatedProgramPoints = invalidatedProgramPoints;
        final CompileUnit cu = functionNode.getCompileUnit();
        if (cu != null) {
            this.code = cu.getCode();
        }
        assert this.className != null;
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public MethodType getMethodType() {
        return this.methodType;
    }
    
    public int getFlags() {
        return this.flags;
    }
    
    public Class<?> getCode() {
        return this.code;
    }
    
    void setCode(final Class<?> code) {
        if (this.code != null) {
            throw new IllegalStateException("code already set");
        }
        assert this.className.equals(code.getTypeName().replace('.', '/')) : "unexpected class name";
        this.code = code;
    }
    
    public Map<Integer, Type> getInvalidatedProgramPoints() {
        return this.invalidatedProgramPoints;
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        Type.writeTypeMap(this.invalidatedProgramPoints, out);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.invalidatedProgramPoints = Type.readTypeMap(in);
    }
}
