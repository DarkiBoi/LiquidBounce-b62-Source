// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandles;

public class Lookup
{
    private final MethodHandles.Lookup lookup;
    public static final Lookup PUBLIC;
    
    public Lookup(final MethodHandles.Lookup lookup) {
        this.lookup = lookup;
    }
    
    public MethodHandle unreflect(final Method m) {
        return unreflect(this.lookup, m);
    }
    
    public static MethodHandle unreflect(final MethodHandles.Lookup lookup, final Method m) {
        try {
            return lookup.unreflect(m);
        }
        catch (IllegalAccessException e) {
            final IllegalAccessError ee = new IllegalAccessError("Failed to unreflect method " + m);
            ee.initCause(e);
            throw ee;
        }
    }
    
    public MethodHandle unreflectGetter(final Field f) {
        try {
            return this.lookup.unreflectGetter(f);
        }
        catch (IllegalAccessException e) {
            final IllegalAccessError ee = new IllegalAccessError("Failed to unreflect getter for field " + f);
            ee.initCause(e);
            throw ee;
        }
    }
    
    public MethodHandle findGetter(final Class<?> refc, final String name, final Class<?> type) {
        try {
            return this.lookup.findGetter(refc, name, type);
        }
        catch (IllegalAccessException e) {
            final IllegalAccessError ee = new IllegalAccessError("Failed to access getter for field " + refc.getName() + "." + name + " of type " + type.getName());
            ee.initCause(e);
            throw ee;
        }
        catch (NoSuchFieldException e2) {
            final NoSuchFieldError ee2 = new NoSuchFieldError("Failed to find getter for field " + refc.getName() + "." + name + " of type " + type.getName());
            ee2.initCause(e2);
            throw ee2;
        }
    }
    
    public MethodHandle unreflectSetter(final Field f) {
        try {
            return this.lookup.unreflectSetter(f);
        }
        catch (IllegalAccessException e) {
            final IllegalAccessError ee = new IllegalAccessError("Failed to unreflect setter for field " + f);
            ee.initCause(e);
            throw ee;
        }
    }
    
    public MethodHandle unreflectConstructor(final Constructor<?> c) {
        return unreflectConstructor(this.lookup, c);
    }
    
    public static MethodHandle unreflectConstructor(final MethodHandles.Lookup lookup, final Constructor<?> c) {
        try {
            return lookup.unreflectConstructor(c);
        }
        catch (IllegalAccessException e) {
            final IllegalAccessError ee = new IllegalAccessError("Failed to unreflect constructor " + c);
            ee.initCause(e);
            throw ee;
        }
    }
    
    public MethodHandle findSpecial(final Class<?> declaringClass, final String name, final MethodType type) {
        try {
            return this.lookup.findSpecial(declaringClass, name, type, declaringClass);
        }
        catch (IllegalAccessException e) {
            final IllegalAccessError ee = new IllegalAccessError("Failed to access special method " + methodDescription(declaringClass, name, type));
            ee.initCause(e);
            throw ee;
        }
        catch (NoSuchMethodException e2) {
            final NoSuchMethodError ee2 = new NoSuchMethodError("Failed to find special method " + methodDescription(declaringClass, name, type));
            ee2.initCause(e2);
            throw ee2;
        }
    }
    
    private static String methodDescription(final Class<?> declaringClass, final String name, final MethodType type) {
        return declaringClass.getName() + "#" + name + type;
    }
    
    public MethodHandle findStatic(final Class<?> declaringClass, final String name, final MethodType type) {
        try {
            return this.lookup.findStatic(declaringClass, name, type);
        }
        catch (IllegalAccessException e) {
            final IllegalAccessError ee = new IllegalAccessError("Failed to access static method " + methodDescription(declaringClass, name, type));
            ee.initCause(e);
            throw ee;
        }
        catch (NoSuchMethodException e2) {
            final NoSuchMethodError ee2 = new NoSuchMethodError("Failed to find static method " + methodDescription(declaringClass, name, type));
            ee2.initCause(e2);
            throw ee2;
        }
    }
    
    public MethodHandle findVirtual(final Class<?> declaringClass, final String name, final MethodType type) {
        try {
            return this.lookup.findVirtual(declaringClass, name, type);
        }
        catch (IllegalAccessException e) {
            final IllegalAccessError ee = new IllegalAccessError("Failed to access virtual method " + methodDescription(declaringClass, name, type));
            ee.initCause(e);
            throw ee;
        }
        catch (NoSuchMethodException e2) {
            final NoSuchMethodError ee2 = new NoSuchMethodError("Failed to find virtual method " + methodDescription(declaringClass, name, type));
            ee2.initCause(e2);
            throw ee2;
        }
    }
    
    public static MethodHandle findOwnSpecial(final MethodHandles.Lookup lookup, final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return new Lookup(lookup).findOwnSpecial(name, rtype, ptypes);
    }
    
    public MethodHandle findOwnSpecial(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return this.findSpecial(this.lookup.lookupClass(), name, MethodType.methodType(rtype, ptypes));
    }
    
    public static MethodHandle findOwnStatic(final MethodHandles.Lookup lookup, final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return new Lookup(lookup).findOwnStatic(name, rtype, ptypes);
    }
    
    public MethodHandle findOwnStatic(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        return this.findStatic(this.lookup.lookupClass(), name, MethodType.methodType(rtype, ptypes));
    }
    
    static {
        PUBLIC = new Lookup(MethodHandles.publicLookup());
    }
}
