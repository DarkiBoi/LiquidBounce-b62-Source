// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ConsString;

public final class ArrayIndex
{
    private static final int INVALID_ARRAY_INDEX = -1;
    private static final long MAX_ARRAY_INDEX = 4294967294L;
    
    private ArrayIndex() {
    }
    
    private static long fromString(final String key) {
        long value = 0L;
        final int length = key.length();
        if (length == 0 || (length > 1 && key.charAt(0) == '0')) {
            return -1L;
        }
        for (int i = 0; i < length; ++i) {
            final char digit = key.charAt(i);
            if (digit < '0' || digit > '9') {
                return -1L;
            }
            value = value * 10L + digit - 48L;
            if (value > 4294967294L) {
                return -1L;
            }
        }
        return value;
    }
    
    public static int getArrayIndex(final Object key) {
        if (key instanceof Integer) {
            return getArrayIndex((int)key);
        }
        if (key instanceof Double) {
            return getArrayIndex((double)key);
        }
        if (key instanceof String) {
            return (int)fromString((String)key);
        }
        if (key instanceof Long) {
            return getArrayIndex((long)key);
        }
        if (key instanceof ConsString) {
            return (int)fromString(key.toString());
        }
        assert !(key instanceof ScriptObject);
        return -1;
    }
    
    public static int getArrayIndex(final int key) {
        return (key >= 0) ? key : -1;
    }
    
    public static int getArrayIndex(final long key) {
        if (key >= 0L && key <= 4294967294L) {
            return (int)key;
        }
        return -1;
    }
    
    public static int getArrayIndex(final double key) {
        if (JSType.isRepresentableAsInt(key)) {
            return getArrayIndex((int)key);
        }
        if (JSType.isRepresentableAsLong(key)) {
            return getArrayIndex((long)key);
        }
        return -1;
    }
    
    public static int getArrayIndex(final String key) {
        return (int)fromString(key);
    }
    
    public static boolean isValidArrayIndex(final int index) {
        return index != -1;
    }
    
    public static long toLongIndex(final int index) {
        return JSType.toUint32(index);
    }
    
    public static String toKey(final int index) {
        return Long.toString(JSType.toUint32(index));
    }
}
