// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.utils;

import java.util.HashMap;
import java.util.Map;

public final class ClassUtils
{
    private static final Map<String, Boolean> CLASSES_MAP;
    
    public static boolean hasClass(final String className) {
        if (ClassUtils.CLASSES_MAP.containsKey(className)) {
            return ClassUtils.CLASSES_MAP.get(className);
        }
        try {
            Class.forName(className);
            ClassUtils.CLASSES_MAP.put(className, true);
            return true;
        }
        catch (ClassNotFoundException e) {
            ClassUtils.CLASSES_MAP.put(className, false);
            return false;
        }
    }
    
    static {
        CLASSES_MAP = new HashMap<String, Boolean>();
    }
}
