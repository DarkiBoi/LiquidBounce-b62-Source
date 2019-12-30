// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.io.ObjectInputStream;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import jdk.nashorn.internal.codegen.types.Type;

public final class UnwarrantedOptimismException extends RuntimeException
{
    public static final int INVALID_PROGRAM_POINT = -1;
    public static final int FIRST_PROGRAM_POINT = 1;
    private Object returnValue;
    private final int programPoint;
    private final Type returnType;
    
    public UnwarrantedOptimismException(final Object returnValue, final int programPoint) {
        this(returnValue, programPoint, getReturnType(returnValue));
    }
    
    public static boolean isValid(final int programPoint) {
        assert programPoint >= -1;
        return programPoint != -1;
    }
    
    private static Type getReturnType(final Object v) {
        if (v instanceof Double) {
            return Type.NUMBER;
        }
        assert !(v instanceof Integer) : v + " is an int";
        return Type.OBJECT;
    }
    
    public UnwarrantedOptimismException(final Object returnValue, final int programPoint, final Type returnType) {
        super("", null, false, Context.DEBUG);
        assert !Type.typeFor(returnValue.getClass()).isNumeric();
        assert returnType != Type.INT;
        this.returnValue = returnValue;
        this.programPoint = programPoint;
        this.returnType = returnType;
    }
    
    public static UnwarrantedOptimismException createNarrowest(final Object returnValue, final int programPoint) {
        if (returnValue instanceof Float || (returnValue instanceof Long && JSType.isRepresentableAsDouble((long)returnValue))) {
            return new UnwarrantedOptimismException(((Number)returnValue).doubleValue(), programPoint, Type.NUMBER);
        }
        return new UnwarrantedOptimismException(returnValue, programPoint);
    }
    
    public Object getReturnValueDestructive() {
        final Object retval = this.returnValue;
        this.returnValue = null;
        return retval;
    }
    
    Object getReturnValueNonDestructive() {
        return this.returnValue;
    }
    
    public Type getReturnType() {
        return this.returnType;
    }
    
    public boolean hasInvalidProgramPoint() {
        return this.programPoint == -1;
    }
    
    public int getProgramPoint() {
        return this.programPoint;
    }
    
    public boolean hasPrimitiveReturnValue() {
        return this.returnValue instanceof Number || this.returnValue instanceof Boolean;
    }
    
    @Override
    public String getMessage() {
        return "UNWARRANTED OPTIMISM: [returnValue=" + this.returnValue + " (class=" + ((this.returnValue == null) ? "null" : this.returnValue.getClass().getSimpleName()) + (this.hasInvalidProgramPoint() ? " <invalid program point>" : (" @ program point #" + this.programPoint)) + ")]";
    }
    
    private void writeObject(final ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException(this.getClass().getName());
    }
    
    private void readObject(final ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException(this.getClass().getName());
    }
}
