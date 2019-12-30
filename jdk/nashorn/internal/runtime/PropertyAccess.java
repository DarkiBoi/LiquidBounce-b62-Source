// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

public interface PropertyAccess
{
    int getInt(final Object p0, final int p1);
    
    int getInt(final double p0, final int p1);
    
    int getInt(final int p0, final int p1);
    
    double getDouble(final Object p0, final int p1);
    
    double getDouble(final double p0, final int p1);
    
    double getDouble(final int p0, final int p1);
    
    Object get(final Object p0);
    
    Object get(final double p0);
    
    Object get(final int p0);
    
    void set(final Object p0, final int p1, final int p2);
    
    void set(final Object p0, final double p1, final int p2);
    
    void set(final Object p0, final Object p1, final int p2);
    
    void set(final double p0, final int p1, final int p2);
    
    void set(final double p0, final double p1, final int p2);
    
    void set(final double p0, final Object p1, final int p2);
    
    void set(final int p0, final int p1, final int p2);
    
    void set(final int p0, final double p1, final int p2);
    
    void set(final int p0, final Object p1, final int p2);
    
    boolean has(final Object p0);
    
    boolean has(final int p0);
    
    boolean has(final double p0);
    
    boolean hasOwnProperty(final Object p0);
    
    boolean hasOwnProperty(final int p0);
    
    boolean hasOwnProperty(final double p0);
    
    boolean delete(final int p0, final boolean p1);
    
    boolean delete(final double p0, final boolean p1);
    
    boolean delete(final Object p0, final boolean p1);
}
