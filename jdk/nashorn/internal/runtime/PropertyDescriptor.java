// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

public interface PropertyDescriptor
{
    public static final int GENERIC = 0;
    public static final int DATA = 1;
    public static final int ACCESSOR = 2;
    public static final String CONFIGURABLE = "configurable";
    public static final String ENUMERABLE = "enumerable";
    public static final String WRITABLE = "writable";
    public static final String VALUE = "value";
    public static final String GET = "get";
    public static final String SET = "set";
    
    boolean isConfigurable();
    
    boolean isEnumerable();
    
    boolean isWritable();
    
    Object getValue();
    
    ScriptFunction getGetter();
    
    ScriptFunction getSetter();
    
    void setConfigurable(final boolean p0);
    
    void setEnumerable(final boolean p0);
    
    void setWritable(final boolean p0);
    
    void setValue(final Object p0);
    
    void setGetter(final Object p0);
    
    void setSetter(final Object p0);
    
    PropertyDescriptor fillFrom(final ScriptObject p0);
    
    int type();
    
    boolean has(final Object p0);
    
    boolean hasAndEquals(final PropertyDescriptor p0);
}
