// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import java.util.Collection;
import java.util.Set;
import jdk.Exported;

@Exported
public interface JSObject
{
    Object call(final Object p0, final Object... p1);
    
    Object newObject(final Object... p0);
    
    Object eval(final String p0);
    
    Object getMember(final String p0);
    
    Object getSlot(final int p0);
    
    boolean hasMember(final String p0);
    
    boolean hasSlot(final int p0);
    
    void removeMember(final String p0);
    
    void setMember(final String p0, final Object p1);
    
    void setSlot(final int p0, final Object p1);
    
    Set<String> keySet();
    
    Collection<Object> values();
    
    boolean isInstance(final Object p0);
    
    boolean isInstanceOf(final Object p0);
    
    String getClassName();
    
    boolean isFunction();
    
    boolean isStrictFunction();
    
    boolean isArray();
    
    @Deprecated
    double toNumber();
}
