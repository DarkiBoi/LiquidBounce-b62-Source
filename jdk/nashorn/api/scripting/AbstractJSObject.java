// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import jdk.nashorn.internal.runtime.JSONListAdapter;
import jdk.nashorn.internal.runtime.JSType;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Objects;
import jdk.Exported;

@Exported
public abstract class AbstractJSObject implements JSObject
{
    @Override
    public Object call(final Object thiz, final Object... args) {
        throw new UnsupportedOperationException("call");
    }
    
    @Override
    public Object newObject(final Object... args) {
        throw new UnsupportedOperationException("newObject");
    }
    
    @Override
    public Object eval(final String s) {
        throw new UnsupportedOperationException("eval");
    }
    
    @Override
    public Object getMember(final String name) {
        Objects.requireNonNull(name);
        return null;
    }
    
    @Override
    public Object getSlot(final int index) {
        return null;
    }
    
    @Override
    public boolean hasMember(final String name) {
        Objects.requireNonNull(name);
        return false;
    }
    
    @Override
    public boolean hasSlot(final int slot) {
        return false;
    }
    
    @Override
    public void removeMember(final String name) {
        Objects.requireNonNull(name);
    }
    
    @Override
    public void setMember(final String name, final Object value) {
        Objects.requireNonNull(name);
    }
    
    @Override
    public void setSlot(final int index, final Object value) {
    }
    
    @Override
    public Set<String> keySet() {
        return Collections.emptySet();
    }
    
    @Override
    public Collection<Object> values() {
        return Collections.emptySet();
    }
    
    @Override
    public boolean isInstance(final Object instance) {
        return false;
    }
    
    @Override
    public boolean isInstanceOf(final Object clazz) {
        return clazz instanceof JSObject && ((JSObject)clazz).isInstance(this);
    }
    
    @Override
    public String getClassName() {
        return this.getClass().getName();
    }
    
    @Override
    public boolean isFunction() {
        return false;
    }
    
    @Override
    public boolean isStrictFunction() {
        return false;
    }
    
    @Override
    public boolean isArray() {
        return false;
    }
    
    @Deprecated
    @Override
    public double toNumber() {
        return JSType.toNumber(JSType.toPrimitive(this, Number.class));
    }
    
    public Object getDefaultValue(final Class<?> hint) {
        return DefaultValueImpl.getDefaultValue(this, hint);
    }
    
    public static Object getDefaultValue(final JSObject jsobj, final Class<?> hint) {
        if (jsobj instanceof AbstractJSObject) {
            return ((AbstractJSObject)jsobj).getDefaultValue(hint);
        }
        if (jsobj instanceof JSONListAdapter) {
            return ((JSONListAdapter)jsobj).getDefaultValue(hint);
        }
        return DefaultValueImpl.getDefaultValue(jsobj, hint);
    }
}
