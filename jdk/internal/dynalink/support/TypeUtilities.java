// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.util.HashMap;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.Map;

public class TypeUtilities
{
    static final Class<Object> OBJECT_CLASS;
    private static final Map<Class<?>, Class<?>> WRAPPER_TYPES;
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPES;
    private static final Map<String, Class<?>> PRIMITIVE_TYPES_BY_NAME;
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPES;
    private static final Set<Class<?>> PRIMITIVE_WRAPPER_TYPES;
    
    private TypeUtilities() {
    }
    
    public static Class<?> getCommonLosslessConversionType(final Class<?> c1, final Class<?> c2) {
        if (c1 == c2) {
            return c1;
        }
        if (c1 == Void.TYPE || c2 == Void.TYPE) {
            return Object.class;
        }
        if (isConvertibleWithoutLoss(c2, c1)) {
            return c1;
        }
        if (isConvertibleWithoutLoss(c1, c2)) {
            return c2;
        }
        if (c1.isPrimitive() && c2.isPrimitive()) {
            if ((c1 == Byte.TYPE && c2 == Character.TYPE) || (c1 == Character.TYPE && c2 == Byte.TYPE)) {
                return Integer.TYPE;
            }
            if ((c1 == Short.TYPE && c2 == Character.TYPE) || (c1 == Character.TYPE && c2 == Short.TYPE)) {
                return Integer.TYPE;
            }
            if ((c1 == Integer.TYPE && c2 == Float.TYPE) || (c1 == Float.TYPE && c2 == Integer.TYPE)) {
                return Double.TYPE;
            }
        }
        return getMostSpecificCommonTypeUnequalNonprimitives(c1, c2);
    }
    
    private static Class<?> getMostSpecificCommonTypeUnequalNonprimitives(final Class<?> c1, final Class<?> c2) {
        final Class<?> npc1 = c1.isPrimitive() ? getWrapperType(c1) : c1;
        final Class<?> npc2 = c2.isPrimitive() ? getWrapperType(c2) : c2;
        final Set<Class<?>> a1 = getAssignables(npc1, npc2);
        final Set<Class<?>> a2 = getAssignables(npc2, npc1);
        a1.retainAll(a2);
        if (a1.isEmpty()) {
            return Object.class;
        }
        final List<Class<?>> max = new ArrayList<Class<?>>();
    Label_0087:
        for (final Class<?> clazz : a1) {
            final Iterator<Class<?>> maxiter = max.iterator();
            while (maxiter.hasNext()) {
                final Class<?> maxClazz = maxiter.next();
                if (isSubtype(maxClazz, clazz)) {
                    continue Label_0087;
                }
                if (!isSubtype(clazz, maxClazz)) {
                    continue;
                }
                maxiter.remove();
            }
            max.add(clazz);
        }
        if (max.size() > 1) {
            return Object.class;
        }
        return max.get(0);
    }
    
    private static Set<Class<?>> getAssignables(final Class<?> c1, final Class<?> c2) {
        final Set<Class<?>> s = new HashSet<Class<?>>();
        collectAssignables(c1, c2, s);
        return s;
    }
    
    private static void collectAssignables(final Class<?> c1, final Class<?> c2, final Set<Class<?>> s) {
        if (c1.isAssignableFrom(c2)) {
            s.add(c1);
        }
        final Class<?> sc = c1.getSuperclass();
        if (sc != null) {
            collectAssignables(sc, c2, s);
        }
        final Class<?>[] itf = c1.getInterfaces();
        for (int i = 0; i < itf.length; ++i) {
            collectAssignables(itf[i], c2, s);
        }
    }
    
    private static Map<Class<?>, Class<?>> createWrapperTypes() {
        final Map<Class<?>, Class<?>> wrapperTypes = new IdentityHashMap<Class<?>, Class<?>>(8);
        wrapperTypes.put(Boolean.TYPE, Boolean.class);
        wrapperTypes.put(Byte.TYPE, Byte.class);
        wrapperTypes.put(Character.TYPE, Character.class);
        wrapperTypes.put(Short.TYPE, Short.class);
        wrapperTypes.put(Integer.TYPE, Integer.class);
        wrapperTypes.put(Long.TYPE, Long.class);
        wrapperTypes.put(Float.TYPE, Float.class);
        wrapperTypes.put(Double.TYPE, Double.class);
        return Collections.unmodifiableMap((Map<? extends Class<?>, ? extends Class<?>>)wrapperTypes);
    }
    
    private static Map<String, Class<?>> createClassNameMapping(final Collection<Class<?>> classes) {
        final Map<String, Class<?>> map = new HashMap<String, Class<?>>();
        for (final Class<?> clazz : classes) {
            map.put(clazz.getName(), clazz);
        }
        return map;
    }
    
    private static <K, V> Map<V, K> invertMap(final Map<K, V> map) {
        final Map<V, K> inverted = new IdentityHashMap<V, K>(map.size());
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            inverted.put(entry.getValue(), entry.getKey());
        }
        return Collections.unmodifiableMap((Map<? extends V, ? extends K>)inverted);
    }
    
    public static boolean isMethodInvocationConvertible(final Class<?> sourceType, final Class<?> targetType) {
        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }
        if (sourceType.isPrimitive()) {
            if (targetType.isPrimitive()) {
                return isProperPrimitiveSubtype(sourceType, targetType);
            }
            assert TypeUtilities.WRAPPER_TYPES.get(sourceType) != null : sourceType.getName();
            return targetType.isAssignableFrom(TypeUtilities.WRAPPER_TYPES.get(sourceType));
        }
        else {
            if (targetType.isPrimitive()) {
                final Class<?> unboxedCallSiteType = TypeUtilities.PRIMITIVE_TYPES.get(sourceType);
                return unboxedCallSiteType != null && (unboxedCallSiteType == targetType || isProperPrimitiveSubtype(unboxedCallSiteType, targetType));
            }
            return false;
        }
    }
    
    public static boolean isConvertibleWithoutLoss(final Class<?> sourceType, final Class<?> targetType) {
        if (targetType.isAssignableFrom(sourceType) || targetType == Void.TYPE) {
            return true;
        }
        if (!sourceType.isPrimitive()) {
            return false;
        }
        if (sourceType == Void.TYPE) {
            return targetType == Object.class;
        }
        if (targetType.isPrimitive()) {
            return isProperPrimitiveLosslessSubtype(sourceType, targetType);
        }
        assert TypeUtilities.WRAPPER_TYPES.get(sourceType) != null : sourceType.getName();
        return targetType.isAssignableFrom(TypeUtilities.WRAPPER_TYPES.get(sourceType));
    }
    
    public static boolean isPotentiallyConvertible(final Class<?> callSiteType, final Class<?> methodType) {
        if (areAssignable(callSiteType, methodType)) {
            return true;
        }
        if (callSiteType.isPrimitive()) {
            return methodType.isPrimitive() || isAssignableFromBoxedPrimitive(methodType);
        }
        return methodType.isPrimitive() && isAssignableFromBoxedPrimitive(callSiteType);
    }
    
    public static boolean areAssignable(final Class<?> c1, final Class<?> c2) {
        return c1.isAssignableFrom(c2) || c2.isAssignableFrom(c1);
    }
    
    public static boolean isSubtype(final Class<?> subType, final Class<?> superType) {
        return superType.isAssignableFrom(subType) || (superType.isPrimitive() && subType.isPrimitive() && isProperPrimitiveSubtype(subType, superType));
    }
    
    private static boolean isProperPrimitiveSubtype(final Class<?> subType, final Class<?> superType) {
        if (superType == Boolean.TYPE || subType == Boolean.TYPE) {
            return false;
        }
        if (subType == Byte.TYPE) {
            return superType != Character.TYPE;
        }
        if (subType == Character.TYPE) {
            return superType != Short.TYPE && superType != Byte.TYPE;
        }
        if (subType == Short.TYPE) {
            return superType != Character.TYPE && superType != Byte.TYPE;
        }
        if (subType == Integer.TYPE) {
            return superType == Long.TYPE || superType == Float.TYPE || superType == Double.TYPE;
        }
        if (subType == Long.TYPE) {
            return superType == Float.TYPE || superType == Double.TYPE;
        }
        return subType == Float.TYPE && superType == Double.TYPE;
    }
    
    private static boolean isProperPrimitiveLosslessSubtype(final Class<?> subType, final Class<?> superType) {
        if (superType == Boolean.TYPE || subType == Boolean.TYPE) {
            return false;
        }
        if (superType == Character.TYPE || subType == Character.TYPE) {
            return false;
        }
        if (subType == Byte.TYPE) {
            return true;
        }
        if (subType == Short.TYPE) {
            return superType != Byte.TYPE;
        }
        if (subType == Integer.TYPE) {
            return superType == Long.TYPE || superType == Double.TYPE;
        }
        return subType == Float.TYPE && superType == Double.TYPE;
    }
    
    private static Map<Class<?>, Class<?>> createWrapperToPrimitiveTypes() {
        final Map<Class<?>, Class<?>> classes = new IdentityHashMap<Class<?>, Class<?>>();
        classes.put(Void.class, Void.TYPE);
        classes.put(Boolean.class, Boolean.TYPE);
        classes.put(Byte.class, Byte.TYPE);
        classes.put(Character.class, Character.TYPE);
        classes.put(Short.class, Short.TYPE);
        classes.put(Integer.class, Integer.TYPE);
        classes.put(Long.class, Long.TYPE);
        classes.put(Float.class, Float.TYPE);
        classes.put(Double.class, Double.TYPE);
        return classes;
    }
    
    private static Set<Class<?>> createPrimitiveWrapperTypes() {
        final Map<Class<?>, Class<?>> classes = new IdentityHashMap<Class<?>, Class<?>>();
        addClassHierarchy(classes, Boolean.class);
        addClassHierarchy(classes, Byte.class);
        addClassHierarchy(classes, Character.class);
        addClassHierarchy(classes, Short.class);
        addClassHierarchy(classes, Integer.class);
        addClassHierarchy(classes, Long.class);
        addClassHierarchy(classes, Float.class);
        addClassHierarchy(classes, Double.class);
        return classes.keySet();
    }
    
    private static void addClassHierarchy(final Map<Class<?>, Class<?>> map, final Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        map.put(clazz, clazz);
        addClassHierarchy(map, clazz.getSuperclass());
        for (final Class<?> itf : clazz.getInterfaces()) {
            addClassHierarchy(map, itf);
        }
    }
    
    private static boolean isAssignableFromBoxedPrimitive(final Class<?> clazz) {
        return TypeUtilities.PRIMITIVE_WRAPPER_TYPES.contains(clazz);
    }
    
    public static Class<?> getPrimitiveTypeByName(final String name) {
        return TypeUtilities.PRIMITIVE_TYPES_BY_NAME.get(name);
    }
    
    public static Class<?> getPrimitiveType(final Class<?> wrapperType) {
        return TypeUtilities.WRAPPER_TO_PRIMITIVE_TYPES.get(wrapperType);
    }
    
    public static Class<?> getWrapperType(final Class<?> primitiveType) {
        return TypeUtilities.WRAPPER_TYPES.get(primitiveType);
    }
    
    public static boolean isWrapperType(final Class<?> type) {
        return TypeUtilities.PRIMITIVE_TYPES.containsKey(type);
    }
    
    static {
        OBJECT_CLASS = Object.class;
        WRAPPER_TYPES = createWrapperTypes();
        PRIMITIVE_TYPES = invertMap(TypeUtilities.WRAPPER_TYPES);
        PRIMITIVE_TYPES_BY_NAME = createClassNameMapping(TypeUtilities.WRAPPER_TYPES.keySet());
        WRAPPER_TO_PRIMITIVE_TYPES = createWrapperToPrimitiveTypes();
        PRIMITIVE_WRAPPER_TYPES = createPrimitiveWrapperTypes();
    }
}
