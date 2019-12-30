// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.arrays;

import java.util.List;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.Iterator;

public abstract class ArrayLikeIterator<T> implements Iterator<T>
{
    protected long index;
    protected final boolean includeUndefined;
    
    ArrayLikeIterator(final boolean includeUndefined) {
        this.includeUndefined = includeUndefined;
        this.index = 0L;
    }
    
    public boolean isReverse() {
        return false;
    }
    
    protected long bumpIndex() {
        return this.index++;
    }
    
    public long nextIndex() {
        return this.index;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
    
    public abstract long getLength();
    
    public static ArrayLikeIterator<Object> arrayLikeIterator(final Object object) {
        return arrayLikeIterator(object, false);
    }
    
    public static ArrayLikeIterator<Object> reverseArrayLikeIterator(final Object object) {
        return reverseArrayLikeIterator(object, false);
    }
    
    public static ArrayLikeIterator<Object> arrayLikeIterator(final Object object, final boolean includeUndefined) {
        Object obj = object;
        if (ScriptObject.isArray(obj)) {
            return new ScriptArrayIterator((ScriptObject)obj, includeUndefined);
        }
        obj = JSType.toScriptObject(obj);
        if (obj instanceof ScriptObject) {
            return new ScriptObjectIterator((ScriptObject)obj, includeUndefined);
        }
        if (obj instanceof JSObject) {
            return new JSObjectIterator((JSObject)obj, includeUndefined);
        }
        if (obj instanceof List) {
            return new JavaListIterator((List<?>)obj, includeUndefined);
        }
        if (obj != null && obj.getClass().isArray()) {
            return new JavaArrayIterator(obj, includeUndefined);
        }
        return new EmptyArrayLikeIterator();
    }
    
    public static ArrayLikeIterator<Object> reverseArrayLikeIterator(final Object object, final boolean includeUndefined) {
        Object obj = object;
        if (ScriptObject.isArray(obj)) {
            return new ReverseScriptArrayIterator((ScriptObject)obj, includeUndefined);
        }
        obj = JSType.toScriptObject(obj);
        if (obj instanceof ScriptObject) {
            return new ReverseScriptObjectIterator((ScriptObject)obj, includeUndefined);
        }
        if (obj instanceof JSObject) {
            return new ReverseJSObjectIterator((JSObject)obj, includeUndefined);
        }
        if (obj instanceof List) {
            return new ReverseJavaListIterator((List<?>)obj, includeUndefined);
        }
        if (obj != null && obj.getClass().isArray()) {
            return new ReverseJavaArrayIterator(obj, includeUndefined);
        }
        return new EmptyArrayLikeIterator();
    }
}
