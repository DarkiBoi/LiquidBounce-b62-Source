// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ConsString;
import jdk.nashorn.internal.runtime.JSONListAdapter;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.security.AccessController;
import java.security.PrivilegedAction;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import java.util.concurrent.Callable;
import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Permission;
import java.security.Permissions;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.security.AccessControlContext;
import jdk.Exported;
import javax.script.Bindings;

@Exported
public final class ScriptObjectMirror extends AbstractJSObject implements Bindings
{
    private static final AccessControlContext GET_CONTEXT_ACC_CTXT;
    private final ScriptObject sobj;
    private final Global global;
    private final boolean strict;
    private final boolean jsonCompatible;
    
    private static AccessControlContext getContextAccCtxt() {
        final Permissions perms = new Permissions();
        perms.add(new RuntimePermission("nashorn.getContext"));
        return new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, perms) });
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ScriptObjectMirror && this.sobj.equals(((ScriptObjectMirror)other).sobj);
    }
    
    @Override
    public int hashCode() {
        return this.sobj.hashCode();
    }
    
    @Override
    public String toString() {
        return this.inGlobal((Callable<String>)new Callable<String>() {
            @Override
            public String call() {
                return ScriptRuntime.safeToString(ScriptObjectMirror.this.sobj);
            }
        });
    }
    
    @Override
    public Object call(final Object thiz, final Object... args) {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != this.global;
        try {
            if (globalChanged) {
                Context.setGlobal(this.global);
            }
            if (this.sobj instanceof ScriptFunction) {
                final Object[] modArgs = globalChanged ? this.wrapArrayLikeMe(args, oldGlobal) : args;
                final Object self = globalChanged ? this.wrapLikeMe(thiz, oldGlobal) : thiz;
                return this.wrapLikeMe(ScriptRuntime.apply((ScriptFunction)this.sobj, unwrap(self, this.global), unwrapArray(modArgs, this.global)));
            }
            throw new RuntimeException("not a function: " + this.toString());
        }
        catch (NashornException ne) {
            throw ne.initEcmaError(this.global);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }
    
    @Override
    public Object newObject(final Object... args) {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != this.global;
        try {
            if (globalChanged) {
                Context.setGlobal(this.global);
            }
            if (this.sobj instanceof ScriptFunction) {
                final Object[] modArgs = globalChanged ? this.wrapArrayLikeMe(args, oldGlobal) : args;
                return this.wrapLikeMe(ScriptRuntime.construct((ScriptFunction)this.sobj, unwrapArray(modArgs, this.global)));
            }
            throw new RuntimeException("not a constructor: " + this.toString());
        }
        catch (NashornException ne) {
            throw ne.initEcmaError(this.global);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }
    
    @Override
    public Object eval(final String s) {
        return this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                final Context context = AccessController.doPrivileged((PrivilegedAction<Context>)new PrivilegedAction<Context>() {
                    @Override
                    public Context run() {
                        return Context.getContext();
                    }
                }, ScriptObjectMirror.GET_CONTEXT_ACC_CTXT);
                return ScriptObjectMirror.this.wrapLikeMe(context.eval(ScriptObjectMirror.this.global, s, ScriptObjectMirror.this.sobj, null));
            }
        });
    }
    
    public Object callMember(final String functionName, final Object... args) {
        Objects.requireNonNull(functionName);
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != this.global;
        try {
            if (globalChanged) {
                Context.setGlobal(this.global);
            }
            final Object val = this.sobj.get(functionName);
            if (val instanceof ScriptFunction) {
                final Object[] modArgs = globalChanged ? this.wrapArrayLikeMe(args, oldGlobal) : args;
                return this.wrapLikeMe(ScriptRuntime.apply((ScriptFunction)val, this.sobj, unwrapArray(modArgs, this.global)));
            }
            if (val instanceof JSObject && ((JSObject)val).isFunction()) {
                return ((JSObject)val).call(this.sobj, args);
            }
            throw new NoSuchMethodException("No such function " + functionName);
        }
        catch (NashornException ne) {
            throw ne.initEcmaError(this.global);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }
    
    @Override
    public Object getMember(final String name) {
        Objects.requireNonNull(name);
        return this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                return ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.get(name));
            }
        });
    }
    
    @Override
    public Object getSlot(final int index) {
        return this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                return ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.get(index));
            }
        });
    }
    
    @Override
    public boolean hasMember(final String name) {
        Objects.requireNonNull(name);
        return this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.has(name);
            }
        });
    }
    
    @Override
    public boolean hasSlot(final int slot) {
        return this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.has(slot);
            }
        });
    }
    
    @Override
    public void removeMember(final String name) {
        this.remove(Objects.requireNonNull(name));
    }
    
    @Override
    public void setMember(final String name, final Object value) {
        this.put(Objects.requireNonNull(name), value);
    }
    
    @Override
    public void setSlot(final int index, final Object value) {
        this.inGlobal((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() {
                ScriptObjectMirror.this.sobj.set(index, ScriptObjectMirror.unwrap(value, ScriptObjectMirror.this.global), ScriptObjectMirror.this.getCallSiteFlags());
                return null;
            }
        });
    }
    
    public void setIndexedPropertiesToExternalArrayData(final ByteBuffer buf) {
        this.inGlobal((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() {
                ScriptObjectMirror.this.sobj.setArray(ArrayData.allocate(buf));
                return null;
            }
        });
    }
    
    @Override
    public boolean isInstance(final Object instance) {
        if (!(instance instanceof ScriptObjectMirror)) {
            return false;
        }
        final ScriptObjectMirror mirror = (ScriptObjectMirror)instance;
        return this.global == mirror.global && this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isInstance(mirror.sobj);
            }
        });
    }
    
    @Override
    public String getClassName() {
        return this.sobj.getClassName();
    }
    
    @Override
    public boolean isFunction() {
        return this.sobj instanceof ScriptFunction;
    }
    
    @Override
    public boolean isStrictFunction() {
        return this.isFunction() && ((ScriptFunction)this.sobj).isStrict();
    }
    
    @Override
    public boolean isArray() {
        return this.sobj.isArray();
    }
    
    @Override
    public void clear() {
        this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                ScriptObjectMirror.this.sobj.clear(ScriptObjectMirror.this.strict);
                return null;
            }
        });
    }
    
    @Override
    public boolean containsKey(final Object key) {
        checkKey(key);
        return this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.containsKey(key);
            }
        });
    }
    
    @Override
    public boolean containsValue(final Object value) {
        return this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.containsValue(ScriptObjectMirror.unwrap(value, ScriptObjectMirror.this.global));
            }
        });
    }
    
    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.inGlobal((Callable<Set<Map.Entry<String, Object>>>)new Callable<Set<Map.Entry<String, Object>>>() {
            @Override
            public Set<Map.Entry<String, Object>> call() {
                final Iterator<String> iter = ScriptObjectMirror.this.sobj.propertyIterator();
                final Set<Map.Entry<String, Object>> entries = new LinkedHashSet<Map.Entry<String, Object>>();
                while (iter.hasNext()) {
                    final String key = iter.next();
                    final Object value = ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.get(key)));
                    entries.add(new AbstractMap.SimpleImmutableEntry<String, Object>(key, value));
                }
                return Collections.unmodifiableSet((Set<? extends Map.Entry<String, Object>>)entries);
            }
        });
    }
    
    @Override
    public Object get(final Object key) {
        checkKey(key);
        return this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                return ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.get(key)));
            }
        });
    }
    
    @Override
    public boolean isEmpty() {
        return this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isEmpty();
            }
        });
    }
    
    @Override
    public Set<String> keySet() {
        return this.inGlobal((Callable<Set<String>>)new Callable<Set<String>>() {
            @Override
            public Set<String> call() {
                final Iterator<String> iter = ScriptObjectMirror.this.sobj.propertyIterator();
                final Set<String> keySet = new LinkedHashSet<String>();
                while (iter.hasNext()) {
                    keySet.add(iter.next());
                }
                return Collections.unmodifiableSet((Set<? extends String>)keySet);
            }
        });
    }
    
    @Override
    public Object put(final String key, final Object value) {
        checkKey(key);
        final ScriptObject oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != this.global;
        return this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                final Object modValue = globalChanged ? ScriptObjectMirror.this.wrapLikeMe(value, oldGlobal) : value;
                return ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.put(key, ScriptObjectMirror.unwrap(modValue, ScriptObjectMirror.this.global), ScriptObjectMirror.this.strict)));
            }
        });
    }
    
    @Override
    public void putAll(final Map<? extends String, ?> map) {
        Objects.requireNonNull(map);
        final ScriptObject oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != this.global;
        this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                for (final Map.Entry<? extends String, ?> entry : map.entrySet()) {
                    final Object value = entry.getValue();
                    final Object modValue = globalChanged ? ScriptObjectMirror.this.wrapLikeMe(value, oldGlobal) : value;
                    final String key = (String)entry.getKey();
                    checkKey(key);
                    ScriptObjectMirror.this.sobj.set(key, ScriptObjectMirror.unwrap(modValue, ScriptObjectMirror.this.global), ScriptObjectMirror.this.getCallSiteFlags());
                }
                return null;
            }
        });
    }
    
    @Override
    public Object remove(final Object key) {
        checkKey(key);
        return this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                return ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.remove(key, ScriptObjectMirror.this.strict)));
            }
        });
    }
    
    public boolean delete(final Object key) {
        return this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.delete(ScriptObjectMirror.unwrap(key, ScriptObjectMirror.this.global), ScriptObjectMirror.this.strict);
            }
        });
    }
    
    @Override
    public int size() {
        return this.inGlobal((Callable<Integer>)new Callable<Integer>() {
            @Override
            public Integer call() {
                return ScriptObjectMirror.this.sobj.size();
            }
        });
    }
    
    @Override
    public Collection<Object> values() {
        return this.inGlobal((Callable<Collection<Object>>)new Callable<Collection<Object>>() {
            @Override
            public Collection<Object> call() {
                final List<Object> values = new ArrayList<Object>(ScriptObjectMirror.this.size());
                final Iterator<Object> iter = ScriptObjectMirror.this.sobj.valueIterator();
                while (iter.hasNext()) {
                    values.add(ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(iter.next())));
                }
                return Collections.unmodifiableList((List<?>)values);
            }
        });
    }
    
    public Object getProto() {
        return this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                return ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.getProto());
            }
        });
    }
    
    public void setProto(final Object proto) {
        this.inGlobal((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() {
                ScriptObjectMirror.this.sobj.setPrototypeOf(ScriptObjectMirror.unwrap(proto, ScriptObjectMirror.this.global));
                return null;
            }
        });
    }
    
    public Object getOwnPropertyDescriptor(final String key) {
        return this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                return ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.getOwnPropertyDescriptor(key));
            }
        });
    }
    
    public String[] getOwnKeys(final boolean all) {
        return this.inGlobal((Callable<String[]>)new Callable<String[]>() {
            @Override
            public String[] call() {
                return ScriptObjectMirror.this.sobj.getOwnKeys(all);
            }
        });
    }
    
    public ScriptObjectMirror preventExtensions() {
        return this.inGlobal((Callable<ScriptObjectMirror>)new Callable<ScriptObjectMirror>() {
            @Override
            public ScriptObjectMirror call() {
                ScriptObjectMirror.this.sobj.preventExtensions();
                return ScriptObjectMirror.this;
            }
        });
    }
    
    public boolean isExtensible() {
        return this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isExtensible();
            }
        });
    }
    
    public ScriptObjectMirror seal() {
        return this.inGlobal((Callable<ScriptObjectMirror>)new Callable<ScriptObjectMirror>() {
            @Override
            public ScriptObjectMirror call() {
                ScriptObjectMirror.this.sobj.seal();
                return ScriptObjectMirror.this;
            }
        });
    }
    
    public boolean isSealed() {
        return this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isSealed();
            }
        });
    }
    
    public ScriptObjectMirror freeze() {
        return this.inGlobal((Callable<ScriptObjectMirror>)new Callable<ScriptObjectMirror>() {
            @Override
            public ScriptObjectMirror call() {
                ScriptObjectMirror.this.sobj.freeze();
                return ScriptObjectMirror.this;
            }
        });
    }
    
    public boolean isFrozen() {
        return this.inGlobal((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isFrozen();
            }
        });
    }
    
    public static boolean isUndefined(final Object obj) {
        return obj == ScriptRuntime.UNDEFINED;
    }
    
    public <T> T to(final Class<T> type) {
        return this.inGlobal((Callable<T>)new Callable<T>() {
            @Override
            public T call() {
                return type.cast(ScriptUtils.convert(ScriptObjectMirror.this.sobj, type));
            }
        });
    }
    
    public static Object wrap(final Object obj, final Object homeGlobal) {
        return wrap(obj, homeGlobal, false);
    }
    
    public static Object wrapAsJSONCompatible(final Object obj, final Object homeGlobal) {
        return wrap(obj, homeGlobal, true);
    }
    
    private static Object wrap(final Object obj, final Object homeGlobal, final boolean jsonCompatible) {
        if (obj instanceof ScriptObject) {
            if (!(homeGlobal instanceof Global)) {
                return obj;
            }
            final ScriptObject sobj = (ScriptObject)obj;
            final Global global = (Global)homeGlobal;
            final ScriptObjectMirror mirror = new ScriptObjectMirror(sobj, global, jsonCompatible);
            if (jsonCompatible && sobj.isArray()) {
                return new JSONListAdapter(mirror, global);
            }
            return mirror;
        }
        else {
            if (obj instanceof ConsString) {
                return obj.toString();
            }
            if (jsonCompatible && obj instanceof ScriptObjectMirror) {
                return ((ScriptObjectMirror)obj).asJSONCompatible();
            }
            return obj;
        }
    }
    
    private Object wrapLikeMe(final Object obj, final Object homeGlobal) {
        return wrap(obj, homeGlobal, this.jsonCompatible);
    }
    
    private Object wrapLikeMe(final Object obj) {
        return this.wrapLikeMe(obj, this.global);
    }
    
    public static Object unwrap(final Object obj, final Object homeGlobal) {
        if (obj instanceof ScriptObjectMirror) {
            final ScriptObjectMirror mirror = (ScriptObjectMirror)obj;
            return (mirror.global == homeGlobal) ? mirror.sobj : obj;
        }
        if (obj instanceof JSONListAdapter) {
            return ((JSONListAdapter)obj).unwrap(homeGlobal);
        }
        return obj;
    }
    
    public static Object[] wrapArray(final Object[] args, final Object homeGlobal) {
        return wrapArray(args, homeGlobal, false);
    }
    
    private static Object[] wrapArray(final Object[] args, final Object homeGlobal, final boolean jsonCompatible) {
        if (args == null || args.length == 0) {
            return args;
        }
        final Object[] newArgs = new Object[args.length];
        int index = 0;
        for (final Object obj : args) {
            newArgs[index] = wrap(obj, homeGlobal, jsonCompatible);
            ++index;
        }
        return newArgs;
    }
    
    private Object[] wrapArrayLikeMe(final Object[] args, final Object homeGlobal) {
        return wrapArray(args, homeGlobal, this.jsonCompatible);
    }
    
    public static Object[] unwrapArray(final Object[] args, final Object homeGlobal) {
        if (args == null || args.length == 0) {
            return args;
        }
        final Object[] newArgs = new Object[args.length];
        int index = 0;
        for (final Object obj : args) {
            newArgs[index] = unwrap(obj, homeGlobal);
            ++index;
        }
        return newArgs;
    }
    
    public static boolean identical(final Object obj1, final Object obj2) {
        final Object o1 = (obj1 instanceof ScriptObjectMirror) ? ((ScriptObjectMirror)obj1).sobj : obj1;
        final Object o2 = (obj2 instanceof ScriptObjectMirror) ? ((ScriptObjectMirror)obj2).sobj : obj2;
        return o1 == o2;
    }
    
    ScriptObjectMirror(final ScriptObject sobj, final Global global) {
        this(sobj, global, false);
    }
    
    private ScriptObjectMirror(final ScriptObject sobj, final Global global, final boolean jsonCompatible) {
        assert sobj != null : "ScriptObjectMirror on null!";
        assert global != null : "home Global is null";
        this.sobj = sobj;
        this.global = global;
        this.strict = global.isStrictContext();
        this.jsonCompatible = jsonCompatible;
    }
    
    ScriptObject getScriptObject() {
        return this.sobj;
    }
    
    Global getHomeGlobal() {
        return this.global;
    }
    
    static Object translateUndefined(final Object obj) {
        return (obj == ScriptRuntime.UNDEFINED) ? null : obj;
    }
    
    private int getCallSiteFlags() {
        return this.strict ? 2 : 0;
    }
    
    private <V> V inGlobal(final Callable<V> callable) {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != this.global;
        Label_0029: {
            if (!globalChanged) {
                break Label_0029;
            }
            Context.setGlobal(this.global);
            try {
                return callable.call();
            }
            catch (NashornException ne) {
                throw ne.initEcmaError(this.global);
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e2) {
                throw new AssertionError("Cannot happen", e2);
            }
            finally {
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
            }
        }
    }
    
    private static void checkKey(final Object key) {
        Objects.requireNonNull(key, "key can not be null");
        if (!(key instanceof String)) {
            throw new ClassCastException("key should be a String. It is " + key.getClass().getName() + " instead.");
        }
        if (((String)key).length() == 0) {
            throw new IllegalArgumentException("key can not be empty");
        }
    }
    
    @Deprecated
    @Override
    public double toNumber() {
        return this.inGlobal((Callable<Double>)new Callable<Double>() {
            @Override
            public Double call() {
                return JSType.toNumber(ScriptObjectMirror.this.sobj);
            }
        });
    }
    
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return this.inGlobal((Callable<Object>)new Callable<Object>() {
            @Override
            public Object call() {
                try {
                    return ScriptObjectMirror.this.sobj.getDefaultValue(hint);
                }
                catch (ECMAException e) {
                    throw new UnsupportedOperationException(e.getMessage(), e);
                }
            }
        });
    }
    
    private ScriptObjectMirror asJSONCompatible() {
        if (this.jsonCompatible) {
            return this;
        }
        return new ScriptObjectMirror(this.sobj, this.global, true);
    }
    
    static {
        GET_CONTEXT_ACC_CTXT = getContextAccCtxt();
    }
}
