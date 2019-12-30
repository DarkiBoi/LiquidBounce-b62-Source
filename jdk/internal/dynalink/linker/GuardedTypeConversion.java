// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.linker;

public class GuardedTypeConversion
{
    private final GuardedInvocation conversionInvocation;
    private final boolean cacheable;
    
    public GuardedTypeConversion(final GuardedInvocation conversionInvocation, final boolean cacheable) {
        this.conversionInvocation = conversionInvocation;
        this.cacheable = cacheable;
    }
    
    public GuardedInvocation getConversionInvocation() {
        return this.conversionInvocation;
    }
    
    public boolean isCacheable() {
        return this.cacheable;
    }
}
