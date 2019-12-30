// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.util.List;

public abstract class ObjectCreator<T> implements CodeGenerator.SplitLiteralCreator
{
    final List<MapTuple<T>> tuples;
    final CodeGenerator codegen;
    protected PropertyMap propertyMap;
    private final boolean isScope;
    private final boolean hasArguments;
    
    ObjectCreator(final CodeGenerator codegen, final List<MapTuple<T>> tuples, final boolean isScope, final boolean hasArguments) {
        this.codegen = codegen;
        this.tuples = tuples;
        this.isScope = isScope;
        this.hasArguments = hasArguments;
    }
    
    public void makeObject(final MethodEmitter method) {
        this.createObject(method);
        final int objectSlot = method.getUsedSlotsWithLiveTemporaries();
        final Type objectType = method.peekType();
        method.storeTemp(objectType, objectSlot);
        this.populateRange(method, objectType, objectSlot, 0, this.tuples.size());
    }
    
    protected abstract void createObject(final MethodEmitter p0);
    
    protected abstract PropertyMap makeMap();
    
    protected MapCreator<?> newMapCreator(final Class<? extends ScriptObject> clazz) {
        return new MapCreator<Object>(clazz, (List<MapTuple<?>>)this.tuples);
    }
    
    protected void loadScope(final MethodEmitter method) {
        method.loadCompilerConstant(CompilerConstants.SCOPE);
    }
    
    protected MethodEmitter loadMap(final MethodEmitter method) {
        this.codegen.loadConstant(this.propertyMap);
        return method;
    }
    
    PropertyMap getMap() {
        return this.propertyMap;
    }
    
    protected boolean isScope() {
        return this.isScope;
    }
    
    protected boolean hasArguments() {
        return this.hasArguments;
    }
    
    protected abstract Class<? extends ScriptObject> getAllocatorClass();
    
    protected abstract void loadValue(final T p0, final Type p1);
    
    MethodEmitter loadTuple(final MethodEmitter method, final MapTuple<T> tuple, final boolean pack) {
        this.loadValue(tuple.value, tuple.type);
        if (!this.codegen.useDualFields() || !tuple.isPrimitive()) {
            method.convert(Type.OBJECT);
        }
        else if (pack) {
            method.pack();
        }
        return method;
    }
    
    MethodEmitter loadIndex(final MethodEmitter method, final long index) {
        return JSType.isRepresentableAsInt(index) ? method.load((int)index) : method.load((double)index);
    }
}
