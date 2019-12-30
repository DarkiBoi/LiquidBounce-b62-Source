// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.scripts.JO;
import jdk.nashorn.internal.scripts.JD;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.Property;
import java.util.Iterator;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.List;
import jdk.nashorn.internal.ir.Expression;

public final class SpillObjectCreator extends ObjectCreator<Expression>
{
    SpillObjectCreator(final CodeGenerator codegen, final List<MapTuple<Expression>> tuples) {
        super(codegen, tuples, false, false);
        this.makeMap();
    }
    
    public void createObject(final MethodEmitter method) {
        assert !this.isScope() : "spill scope objects are not currently supported";
        final int length = this.tuples.size();
        final boolean dualFields = this.codegen.useDualFields();
        final int spillLength = ScriptObject.spillAllocationLength(length);
        final long[] jpresetValues = (long[])(dualFields ? new long[spillLength] : null);
        final Object[] opresetValues = new Object[spillLength];
        final Class<?> objectClass = this.getAllocatorClass();
        ArrayData arrayData = ArrayData.allocate(ScriptRuntime.EMPTY_ARRAY);
        int pos = 0;
        for (final MapTuple<Expression> tuple : this.tuples) {
            final String key = tuple.key;
            final Expression value = tuple.value;
            method.invalidateSpecialName(tuple.key);
            if (value != null) {
                final Object constantValue = LiteralNode.objectAsConstant(value);
                if (constantValue != LiteralNode.POSTSET_MARKER) {
                    final Property property = this.propertyMap.findProperty(key);
                    if (property != null) {
                        property.setType(dualFields ? JSType.unboxedFieldType(constantValue) : Object.class);
                        final int slot = property.getSlot();
                        if (dualFields && constantValue instanceof Number) {
                            jpresetValues[slot] = ObjectClassGenerator.pack((Number)constantValue);
                        }
                        else {
                            opresetValues[slot] = constantValue;
                        }
                    }
                    else {
                        final long oldLength = arrayData.length();
                        final int index = ArrayIndex.getArrayIndex(key);
                        final long longIndex = ArrayIndex.toLongIndex(index);
                        assert ArrayIndex.isValidArrayIndex(index);
                        if (longIndex >= oldLength) {
                            arrayData = arrayData.ensure(longIndex);
                        }
                        if (constantValue instanceof Integer) {
                            arrayData = arrayData.set(index, (int)constantValue, false);
                        }
                        else if (constantValue instanceof Double) {
                            arrayData = arrayData.set(index, (double)constantValue, false);
                        }
                        else {
                            arrayData = arrayData.set(index, constantValue, false);
                        }
                        if (longIndex > oldLength) {
                            arrayData = arrayData.delete(oldLength, longIndex - 1L);
                        }
                    }
                }
            }
            ++pos;
        }
        method._new(objectClass).dup();
        this.codegen.loadConstant(this.propertyMap);
        if (dualFields) {
            this.codegen.loadConstant(jpresetValues);
        }
        else {
            method.loadNull();
        }
        this.codegen.loadConstant(opresetValues);
        method.invoke(CompilerConstants.constructorNoLookup(objectClass, PropertyMap.class, long[].class, Object[].class));
        if (arrayData.length() > 0L) {
            method.dup();
            this.codegen.loadConstant(arrayData);
            method.invoke(CompilerConstants.virtualCallNoLookup(ScriptObject.class, "setArray", Void.TYPE, ArrayData.class));
        }
    }
    
    @Override
    public void populateRange(final MethodEmitter method, final Type objectType, final int objectSlot, final int start, final int end) {
        final int callSiteFlags = this.codegen.getCallSiteFlags();
        method.load(objectType, objectSlot);
        for (int i = start; i < end; ++i) {
            final MapTuple<Expression> tuple = (MapTuple<Expression>)this.tuples.get(i);
            if (!LiteralNode.isConstant(tuple.value)) {
                final Property property = this.propertyMap.findProperty(tuple.key);
                if (property == null) {
                    final int index = ArrayIndex.getArrayIndex(tuple.key);
                    assert ArrayIndex.isValidArrayIndex(index);
                    method.dup();
                    this.loadIndex(method, ArrayIndex.toLongIndex(index));
                    this.loadTuple(method, tuple, false);
                    method.dynamicSetIndex(callSiteFlags);
                }
                else {
                    method.dup();
                    this.loadTuple(method, tuple, false);
                    method.dynamicSet(property.getKey(), this.codegen.getCallSiteFlags(), false);
                }
            }
        }
    }
    
    @Override
    protected PropertyMap makeMap() {
        assert this.propertyMap == null : "property map already initialized";
        final Class<? extends ScriptObject> clazz = this.getAllocatorClass();
        return this.propertyMap = new MapCreator(clazz, (List<MapTuple<Object>>)this.tuples).makeSpillMap(false, this.codegen.useDualFields());
    }
    
    @Override
    protected void loadValue(final Expression expr, final Type type) {
        this.codegen.loadExpressionAsType(expr, Type.generic(type));
    }
    
    @Override
    protected Class<? extends ScriptObject> getAllocatorClass() {
        return (Class<? extends ScriptObject>)(this.codegen.useDualFields() ? JD.class : JO.class);
    }
}
