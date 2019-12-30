// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.runtime.SpillProperty;
import jdk.nashorn.internal.ir.Symbol;
import java.util.Iterator;
import java.util.Collection;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.runtime.Property;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.List;

public class MapCreator<T>
{
    private final Class<?> structure;
    private final List<MapTuple<T>> tuples;
    
    MapCreator(final Class<? extends ScriptObject> structure, final List<MapTuple<T>> tuples) {
        this.structure = structure;
        this.tuples = tuples;
    }
    
    PropertyMap makeFieldMap(final boolean hasArguments, final boolean dualFields, final int fieldCount, final int fieldMaximum, final boolean evalCode) {
        final List<Property> properties = new ArrayList<Property>();
        assert this.tuples != null;
        for (final MapTuple<T> tuple : this.tuples) {
            final String key = tuple.key;
            final Symbol symbol = tuple.symbol;
            final Class<?> initialType = dualFields ? tuple.getValueType() : Object.class;
            if (symbol != null && !ArrayIndex.isValidArrayIndex(ArrayIndex.getArrayIndex(key))) {
                final int flags = getPropertyFlags(symbol, hasArguments, evalCode, dualFields);
                final Property property = new AccessorProperty(key, flags, this.structure, symbol.getFieldIndex(), initialType);
                properties.add(property);
            }
        }
        return PropertyMap.newMap(properties, this.structure.getName(), fieldCount, fieldMaximum, 0);
    }
    
    PropertyMap makeSpillMap(final boolean hasArguments, final boolean dualFields) {
        final List<Property> properties = new ArrayList<Property>();
        int spillIndex = 0;
        assert this.tuples != null;
        for (final MapTuple<T> tuple : this.tuples) {
            final String key = tuple.key;
            final Symbol symbol = tuple.symbol;
            final Class<?> initialType = dualFields ? tuple.getValueType() : Object.class;
            if (symbol != null && !ArrayIndex.isValidArrayIndex(ArrayIndex.getArrayIndex(key))) {
                final int flags = getPropertyFlags(symbol, hasArguments, false, dualFields);
                properties.add(new SpillProperty(key, flags, spillIndex++, initialType));
            }
        }
        return PropertyMap.newMap(properties, this.structure.getName(), 0, 0, spillIndex);
    }
    
    static int getPropertyFlags(final Symbol symbol, final boolean hasArguments, final boolean evalCode, final boolean dualFields) {
        int flags = 0;
        if (symbol.isParam()) {
            flags |= 0x8;
        }
        if (hasArguments) {
            flags |= 0x10;
        }
        if (symbol.isScope() && !evalCode) {
            flags |= 0x4;
        }
        if (symbol.isFunctionDeclaration()) {
            flags |= 0x20;
        }
        if (symbol.isConst()) {
            flags |= 0x1;
        }
        if (symbol.isBlockScoped()) {
            flags |= 0x400;
        }
        if (symbol.isBlockScoped() && symbol.isScope()) {
            flags |= 0x200;
        }
        if (dualFields) {
            flags |= 0x800;
        }
        return flags;
    }
}
