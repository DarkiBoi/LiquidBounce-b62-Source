// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.linker;

public interface ConversionComparator
{
    Comparison compareConversion(final Class<?> p0, final Class<?> p1, final Class<?> p2);
    
    public enum Comparison
    {
        INDETERMINATE, 
        TYPE_1_BETTER, 
        TYPE_2_BETTER;
    }
}
