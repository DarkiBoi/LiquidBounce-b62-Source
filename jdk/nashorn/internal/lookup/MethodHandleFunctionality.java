// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.lookup;

import java.lang.invoke.SwitchPoint;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.lang.invoke.MethodHandle;

public interface MethodHandleFunctionality
{
    MethodHandle filterArguments(final MethodHandle p0, final int p1, final MethodHandle... p2);
    
    MethodHandle filterReturnValue(final MethodHandle p0, final MethodHandle p1);
    
    MethodHandle guardWithTest(final MethodHandle p0, final MethodHandle p1, final MethodHandle p2);
    
    MethodHandle insertArguments(final MethodHandle p0, final int p1, final Object... p2);
    
    MethodHandle dropArguments(final MethodHandle p0, final int p1, final Class<?>... p2);
    
    MethodHandle dropArguments(final MethodHandle p0, final int p1, final List<Class<?>> p2);
    
    MethodHandle foldArguments(final MethodHandle p0, final MethodHandle p1);
    
    MethodHandle explicitCastArguments(final MethodHandle p0, final MethodType p1);
    
    MethodHandle arrayElementGetter(final Class<?> p0);
    
    MethodHandle arrayElementSetter(final Class<?> p0);
    
    MethodHandle throwException(final Class<?> p0, final Class<? extends Throwable> p1);
    
    MethodHandle catchException(final MethodHandle p0, final Class<? extends Throwable> p1, final MethodHandle p2);
    
    MethodHandle constant(final Class<?> p0, final Object p1);
    
    MethodHandle identity(final Class<?> p0);
    
    MethodHandle asType(final MethodHandle p0, final MethodType p1);
    
    MethodHandle asCollector(final MethodHandle p0, final Class<?> p1, final int p2);
    
    MethodHandle asSpreader(final MethodHandle p0, final Class<?> p1, final int p2);
    
    MethodHandle bindTo(final MethodHandle p0, final Object p1);
    
    MethodHandle getter(final MethodHandles.Lookup p0, final Class<?> p1, final String p2, final Class<?> p3);
    
    MethodHandle staticGetter(final MethodHandles.Lookup p0, final Class<?> p1, final String p2, final Class<?> p3);
    
    MethodHandle setter(final MethodHandles.Lookup p0, final Class<?> p1, final String p2, final Class<?> p3);
    
    MethodHandle staticSetter(final MethodHandles.Lookup p0, final Class<?> p1, final String p2, final Class<?> p3);
    
    MethodHandle find(final Method p0);
    
    MethodHandle findStatic(final MethodHandles.Lookup p0, final Class<?> p1, final String p2, final MethodType p3);
    
    MethodHandle findVirtual(final MethodHandles.Lookup p0, final Class<?> p1, final String p2, final MethodType p3);
    
    MethodHandle findSpecial(final MethodHandles.Lookup p0, final Class<?> p1, final String p2, final MethodType p3, final Class<?> p4);
    
    SwitchPoint createSwitchPoint();
    
    MethodHandle guardWithTest(final SwitchPoint p0, final MethodHandle p1, final MethodHandle p2);
    
    MethodType type(final Class<?> p0, final Class<?>... p1);
}
