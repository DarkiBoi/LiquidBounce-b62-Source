// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.linker;

import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;

public interface MethodTypeConversionStrategy
{
    MethodHandle asType(final MethodHandle p0, final MethodType p1);
}
