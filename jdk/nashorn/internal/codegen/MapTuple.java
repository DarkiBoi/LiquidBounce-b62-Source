// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Symbol;

class MapTuple<T>
{
    final String key;
    final Symbol symbol;
    final Type type;
    final T value;
    
    MapTuple(final String key, final Symbol symbol, final Type type) {
        this(key, symbol, type, null);
    }
    
    MapTuple(final String key, final Symbol symbol, final Type type, final T value) {
        this.key = key;
        this.symbol = symbol;
        this.type = type;
        this.value = value;
    }
    
    public Class<?> getValueType() {
        return null;
    }
    
    boolean isPrimitive() {
        return this.getValueType() != null && this.getValueType().isPrimitive() && this.getValueType() != Boolean.TYPE;
    }
    
    @Override
    public String toString() {
        return "[key=" + this.key + ", symbol=" + this.symbol + ", value=" + this.value + " (" + ((this.value == null) ? "null" : this.value.getClass().getSimpleName()) + ")]";
    }
}
