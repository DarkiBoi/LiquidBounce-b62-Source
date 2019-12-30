// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.ir.Symbol;
import java.util.Iterator;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.util.List;
import jdk.nashorn.internal.runtime.ScriptObject;

public abstract class FieldObjectCreator<T> extends ObjectCreator<T>
{
    private String fieldObjectClassName;
    private Class<? extends ScriptObject> fieldObjectClass;
    private int fieldCount;
    private int paddedFieldCount;
    private int paramCount;
    private final int callSiteFlags;
    private final boolean evalCode;
    
    FieldObjectCreator(final CodeGenerator codegen, final List<MapTuple<T>> tuples) {
        this(codegen, tuples, false, false);
    }
    
    FieldObjectCreator(final CodeGenerator codegen, final List<MapTuple<T>> tuples, final boolean isScope, final boolean hasArguments) {
        super(codegen, tuples, isScope, hasArguments);
        this.callSiteFlags = codegen.getCallSiteFlags();
        this.evalCode = codegen.isEvalCode();
        this.countFields();
        this.findClass();
    }
    
    public void createObject(final MethodEmitter method) {
        this.makeMap();
        final String className = this.getClassName();
        assert this.fieldObjectClass != null;
        method._new(this.fieldObjectClass).dup();
        this.loadMap(method);
        if (this.isScope()) {
            this.loadScope(method);
            if (this.hasArguments()) {
                method.loadCompilerConstant(CompilerConstants.ARGUMENTS);
                method.invoke(CompilerConstants.constructorNoLookup(className, PropertyMap.class, ScriptObject.class, CompilerConstants.ARGUMENTS.type()));
            }
            else {
                method.invoke(CompilerConstants.constructorNoLookup(className, PropertyMap.class, ScriptObject.class));
            }
        }
        else {
            method.invoke(CompilerConstants.constructorNoLookup(className, PropertyMap.class));
        }
    }
    
    @Override
    public void populateRange(final MethodEmitter method, final Type objectType, final int objectSlot, final int start, final int end) {
        method.load(objectType, objectSlot);
        for (int i = start; i < end; ++i) {
            final MapTuple<T> tuple = this.tuples.get(i);
            if (tuple.symbol != null && tuple.value != null) {
                final int index = ArrayIndex.getArrayIndex(tuple.key);
                method.dup();
                if (!ArrayIndex.isValidArrayIndex(index)) {
                    this.putField(method, tuple.key, tuple.symbol.getFieldIndex(), tuple);
                }
                else {
                    this.putSlot(method, ArrayIndex.toLongIndex(index), tuple);
                }
                method.invalidateSpecialName(tuple.key);
            }
        }
    }
    
    @Override
    protected PropertyMap makeMap() {
        assert this.propertyMap == null : "property map already initialized";
        return this.propertyMap = this.newMapCreator(this.fieldObjectClass).makeFieldMap(this.hasArguments(), this.codegen.useDualFields(), this.fieldCount, this.paddedFieldCount, this.evalCode);
    }
    
    private void putField(final MethodEmitter method, final String key, final int fieldIndex, final MapTuple<T> tuple) {
        final Type fieldType = (this.codegen.useDualFields() && tuple.isPrimitive()) ? ObjectClassGenerator.PRIMITIVE_FIELD_TYPE : Type.OBJECT;
        final String fieldClass = this.getClassName();
        final String fieldName = ObjectClassGenerator.getFieldName(fieldIndex, fieldType);
        final String fieldDesc = CompilerConstants.typeDescriptor(fieldType.getTypeClass());
        assert fieldName.equals(ObjectClassGenerator.getFieldName(fieldIndex, ObjectClassGenerator.PRIMITIVE_FIELD_TYPE)) || fieldType.isObject() : key + " object keys must store to L*-fields";
        assert fieldName.equals(ObjectClassGenerator.getFieldName(fieldIndex, Type.OBJECT)) || fieldType.isPrimitive() : key + " primitive keys must store to J*-fields";
        this.loadTuple(method, tuple, true);
        method.putField(fieldClass, fieldName, fieldDesc);
    }
    
    private void putSlot(final MethodEmitter method, final long index, final MapTuple<T> tuple) {
        this.loadIndex(method, index);
        this.loadTuple(method, tuple, false);
        method.dynamicSetIndex(this.callSiteFlags);
    }
    
    private void findClass() {
        this.fieldObjectClassName = (this.isScope() ? ObjectClassGenerator.getClassName(this.fieldCount, this.paramCount, this.codegen.useDualFields()) : ObjectClassGenerator.getClassName(this.paddedFieldCount, this.codegen.useDualFields()));
        try {
            this.fieldObjectClass = Context.forStructureClass(Compiler.binaryName(this.fieldObjectClassName));
        }
        catch (ClassNotFoundException e) {
            throw new AssertionError((Object)"Nashorn has encountered an internal error.  Structure can not be created.");
        }
    }
    
    @Override
    protected Class<? extends ScriptObject> getAllocatorClass() {
        return this.fieldObjectClass;
    }
    
    String getClassName() {
        return this.fieldObjectClassName;
    }
    
    private void countFields() {
        for (final MapTuple<T> tuple : this.tuples) {
            final Symbol symbol = tuple.symbol;
            if (symbol != null) {
                if (this.hasArguments() && symbol.isParam()) {
                    symbol.setFieldIndex(this.paramCount++);
                }
                else {
                    if (ArrayIndex.isValidArrayIndex(ArrayIndex.getArrayIndex(tuple.key))) {
                        continue;
                    }
                    symbol.setFieldIndex(this.fieldCount++);
                }
            }
        }
        this.paddedFieldCount = ObjectClassGenerator.getPaddedFieldCount(this.fieldCount);
    }
}
