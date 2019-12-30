// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.Objects;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

final class ConstantData
{
    final List<Object> constants;
    final Map<String, Integer> stringMap;
    final Map<Object, Integer> objectMap;
    
    ConstantData() {
        this.constants = new ArrayList<Object>();
        this.stringMap = new HashMap<String, Integer>();
        this.objectMap = new HashMap<Object, Integer>();
    }
    
    public int add(final String string) {
        final Integer value = this.stringMap.get(string);
        if (value != null) {
            return value;
        }
        this.constants.add(string);
        final int index = this.constants.size() - 1;
        this.stringMap.put(string, index);
        return index;
    }
    
    public int add(final Object object) {
        assert object != null;
        Object entry;
        if (object.getClass().isArray()) {
            entry = new ArrayWrapper(object);
        }
        else if (object instanceof PropertyMap) {
            entry = new PropertyMapWrapper((PropertyMap)object);
        }
        else {
            entry = object;
        }
        final Integer value = this.objectMap.get(entry);
        if (value != null) {
            return value;
        }
        this.constants.add(object);
        final int index = this.constants.size() - 1;
        this.objectMap.put(entry, index);
        return index;
    }
    
    Object[] toArray() {
        return this.constants.toArray();
    }
    
    private static class ArrayWrapper
    {
        private final Object array;
        private final int hashCode;
        
        public ArrayWrapper(final Object array) {
            this.array = array;
            this.hashCode = this.calcHashCode();
        }
        
        private int calcHashCode() {
            final Class<?> cls = this.array.getClass();
            if (!cls.getComponentType().isPrimitive()) {
                return Arrays.hashCode((Object[])this.array);
            }
            if (cls == double[].class) {
                return Arrays.hashCode((double[])this.array);
            }
            if (cls == long[].class) {
                return Arrays.hashCode((long[])this.array);
            }
            if (cls == int[].class) {
                return Arrays.hashCode((int[])this.array);
            }
            throw new AssertionError((Object)("ConstantData doesn't support " + cls));
        }
        
        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof ArrayWrapper)) {
                return false;
            }
            final Object otherArray = ((ArrayWrapper)other).array;
            if (this.array == otherArray) {
                return true;
            }
            final Class<?> cls = this.array.getClass();
            if (cls == otherArray.getClass()) {
                if (!cls.getComponentType().isPrimitive()) {
                    return Arrays.equals((Object[])this.array, (Object[])otherArray);
                }
                if (cls == double[].class) {
                    return Arrays.equals((double[])this.array, (double[])otherArray);
                }
                if (cls == long[].class) {
                    return Arrays.equals((long[])this.array, (long[])otherArray);
                }
                if (cls == int[].class) {
                    return Arrays.equals((int[])this.array, (int[])otherArray);
                }
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }
    
    private static class PropertyMapWrapper
    {
        private final PropertyMap propertyMap;
        private final int hashCode;
        
        public PropertyMapWrapper(final PropertyMap map) {
            this.hashCode = Arrays.hashCode(map.getProperties()) + 31 * Objects.hashCode(map.getClassName());
            this.propertyMap = map;
        }
        
        @Override
        public int hashCode() {
            return this.hashCode;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof PropertyMapWrapper)) {
                return false;
            }
            final PropertyMap otherMap = ((PropertyMapWrapper)other).propertyMap;
            return this.propertyMap == otherMap || (Arrays.equals(this.propertyMap.getProperties(), otherMap.getProperties()) && Objects.equals(this.propertyMap.getClassName(), otherMap.getClassName()));
        }
    }
}
