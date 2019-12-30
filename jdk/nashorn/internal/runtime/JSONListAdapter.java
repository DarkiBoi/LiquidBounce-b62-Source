// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.api.scripting.AbstractJSObject;
import java.util.Collection;
import java.util.Set;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.api.scripting.JSObject;

public final class JSONListAdapter extends ListAdapter implements JSObject
{
    public JSONListAdapter(final JSObject obj, final Global global) {
        super(obj, global);
    }
    
    public Object unwrap(final Object homeGlobal) {
        final Object unwrapped = ScriptObjectMirror.unwrap(this.obj, homeGlobal);
        return (unwrapped != this.obj) ? unwrapped : this;
    }
    
    @Override
    public Object call(final Object thiz, final Object... args) {
        return this.obj.call(thiz, args);
    }
    
    @Override
    public Object newObject(final Object... args) {
        return this.obj.newObject(args);
    }
    
    @Override
    public Object eval(final String s) {
        return this.obj.eval(s);
    }
    
    @Override
    public Object getMember(final String name) {
        return this.obj.getMember(name);
    }
    
    @Override
    public Object getSlot(final int index) {
        return this.obj.getSlot(index);
    }
    
    @Override
    public boolean hasMember(final String name) {
        return this.obj.hasMember(name);
    }
    
    @Override
    public boolean hasSlot(final int slot) {
        return this.obj.hasSlot(slot);
    }
    
    @Override
    public void removeMember(final String name) {
        this.obj.removeMember(name);
    }
    
    @Override
    public void setMember(final String name, final Object value) {
        this.obj.setMember(name, value);
    }
    
    @Override
    public void setSlot(final int index, final Object value) {
        this.obj.setSlot(index, value);
    }
    
    @Override
    public Set<String> keySet() {
        return this.obj.keySet();
    }
    
    @Override
    public Collection<Object> values() {
        return this.obj.values();
    }
    
    @Override
    public boolean isInstance(final Object instance) {
        return this.obj.isInstance(instance);
    }
    
    @Override
    public boolean isInstanceOf(final Object clazz) {
        return this.obj.isInstanceOf(clazz);
    }
    
    @Override
    public String getClassName() {
        return this.obj.getClassName();
    }
    
    @Override
    public boolean isFunction() {
        return this.obj.isFunction();
    }
    
    @Override
    public boolean isStrictFunction() {
        return this.obj.isStrictFunction();
    }
    
    @Override
    public boolean isArray() {
        return this.obj.isArray();
    }
    
    @Deprecated
    @Override
    public double toNumber() {
        return this.obj.toNumber();
    }
    
    public Object getDefaultValue(final Class<?> hint) {
        return AbstractJSObject.getDefaultValue(this.obj, hint);
    }
}
