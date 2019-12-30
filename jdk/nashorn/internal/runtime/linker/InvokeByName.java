// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;

public final class InvokeByName
{
    private final String name;
    private final MethodHandle getter;
    private final MethodHandle invoker;
    
    public InvokeByName(final String name, final Class<?> targetClass) {
        this(name, targetClass, Object.class, (Class<?>[])new Class[0]);
    }
    
    public InvokeByName(final String name, final Class<?> targetClass, final Class<?> rtype, final Class<?>... ptypes) {
        this.name = name;
        this.getter = Bootstrap.createDynamicInvoker("dyn:getMethod|getProp|getElem:" + name, Object.class, targetClass);
        final int plength = ptypes.length;
        Class<?>[] finalPtypes;
        if (plength == 0) {
            finalPtypes = (Class<?>[])new Class[] { Object.class, targetClass };
        }
        else {
            finalPtypes = (Class<?>[])new Class[plength + 2];
            finalPtypes[0] = Object.class;
            finalPtypes[1] = targetClass;
            System.arraycopy(ptypes, 0, finalPtypes, 2, plength);
        }
        this.invoker = Bootstrap.createDynamicInvoker("dyn:call", rtype, finalPtypes);
    }
    
    public String getName() {
        return this.name;
    }
    
    public MethodHandle getGetter() {
        return this.getter;
    }
    
    public MethodHandle getInvoker() {
        return this.invoker;
    }
}
