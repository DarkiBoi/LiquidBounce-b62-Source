// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp.joni.encoding;

public final class ObjPtr<T>
{
    public T p;
    
    public ObjPtr() {
        this(null);
    }
    
    public ObjPtr(final T p) {
        this.p = p;
    }
}
