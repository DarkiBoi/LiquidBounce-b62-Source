// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.util.IdentityHashMap;
import java.util.Map;
import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import java.util.List;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.api.scripting.JSObject;
import java.util.Iterator;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ConsString;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.JSONFunctions;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import java.lang.invoke.MethodHandle;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.runtime.linker.InvokeByName;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeJSON extends ScriptObject
{
    private static final Object TO_JSON;
    private static final Object JSOBJECT_INVOKER;
    private static final Object REPLACER_INVOKER;
    private static PropertyMap $nasgenmap$;
    
    private static InvokeByName getTO_JSON() {
        return Global.instance().getInvokeByName(NativeJSON.TO_JSON, new Callable<InvokeByName>() {
            @Override
            public InvokeByName call() {
                return new InvokeByName("toJSON", ScriptObject.class, Object.class, (Class<?>[])new Class[] { Object.class });
            }
        });
    }
    
    private static MethodHandle getJSOBJECT_INVOKER() {
        return Global.instance().getDynamicInvoker(NativeJSON.JSOBJECT_INVOKER, new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", Object.class, Object.class, Object.class);
            }
        });
    }
    
    private static MethodHandle getREPLACER_INVOKER() {
        return Global.instance().getDynamicInvoker(NativeJSON.REPLACER_INVOKER, new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", Object.class, Object.class, Object.class, Object.class, Object.class);
            }
        });
    }
    
    private NativeJSON() {
        throw new UnsupportedOperationException();
    }
    
    public static Object parse(final Object self, final Object text, final Object reviver) {
        return JSONFunctions.parse(text, reviver);
    }
    
    public static Object stringify(final Object self, final Object value, final Object replacer, final Object space) {
        final StringifyState state = new StringifyState();
        if (Bootstrap.isCallable(replacer)) {
            state.replacerFunction = replacer;
        }
        else if (ScriptObject.isArray(replacer) || isJSObjectArray(replacer) || replacer instanceof Iterable || (replacer != null && replacer.getClass().isArray())) {
            state.propertyList = new ArrayList<String>();
            final Iterator<Object> iter = ArrayLikeIterator.arrayLikeIterator(replacer);
            while (iter.hasNext()) {
                String item = null;
                final Object v = iter.next();
                if (v instanceof String) {
                    item = (String)v;
                }
                else if (v instanceof ConsString) {
                    item = v.toString();
                }
                else if (v instanceof Number || v instanceof NativeNumber || v instanceof NativeString) {
                    item = JSType.toString(v);
                }
                if (item != null) {
                    state.propertyList.add(item);
                }
            }
        }
        Object modSpace = space;
        if (modSpace instanceof NativeNumber) {
            modSpace = JSType.toNumber(JSType.toPrimitive(modSpace, Number.class));
        }
        else if (modSpace instanceof NativeString) {
            modSpace = JSType.toString(JSType.toPrimitive(modSpace, String.class));
        }
        String gap;
        if (modSpace instanceof Number) {
            final int indent = Math.min(10, JSType.toInteger(modSpace));
            if (indent < 1) {
                gap = "";
            }
            else {
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < indent; ++i) {
                    sb.append(' ');
                }
                gap = sb.toString();
            }
        }
        else if (JSType.isString(modSpace)) {
            final String str = modSpace.toString();
            gap = str.substring(0, Math.min(10, str.length()));
        }
        else {
            gap = "";
        }
        state.gap = gap;
        final ScriptObject wrapper = Global.newEmptyInstance();
        wrapper.set("", value, 0);
        return str("", wrapper, state);
    }
    
    private static Object str(final Object key, final Object holder, final StringifyState state) {
        assert holder instanceof ScriptObject || holder instanceof JSObject;
        Object value = getProperty(holder, key);
        try {
            if (value instanceof ScriptObject) {
                final InvokeByName toJSONInvoker = getTO_JSON();
                final ScriptObject svalue = (ScriptObject)value;
                final Object toJSON = toJSONInvoker.getGetter().invokeExact(svalue);
                if (Bootstrap.isCallable(toJSON)) {
                    value = toJSONInvoker.getInvoker().invokeExact(toJSON, svalue, key);
                }
            }
            else if (value instanceof JSObject) {
                final JSObject jsObj = (JSObject)value;
                final Object toJSON2 = jsObj.getMember("toJSON");
                if (Bootstrap.isCallable(toJSON2)) {
                    value = getJSOBJECT_INVOKER().invokeExact(toJSON2, value);
                }
            }
            if (state.replacerFunction != null) {
                value = getREPLACER_INVOKER().invokeExact(state.replacerFunction, holder, key, value);
            }
        }
        catch (Error | RuntimeException error) {
            final Throwable t2;
            final Throwable t = t2;
            throw t;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        final boolean isObj = value instanceof ScriptObject;
        if (isObj) {
            if (value instanceof NativeNumber) {
                value = JSType.toNumber(value);
            }
            else if (value instanceof NativeString) {
                value = JSType.toString(value);
            }
            else if (value instanceof NativeBoolean) {
                value = ((NativeBoolean)value).booleanValue();
            }
        }
        if (value == null) {
            return "null";
        }
        if (Boolean.TRUE.equals(value)) {
            return "true";
        }
        if (Boolean.FALSE.equals(value)) {
            return "false";
        }
        if (value instanceof String) {
            return JSONFunctions.quote((String)value);
        }
        if (value instanceof ConsString) {
            return JSONFunctions.quote(value.toString());
        }
        if (value instanceof Number) {
            return JSType.isFinite(((Number)value).doubleValue()) ? JSType.toString(value) : "null";
        }
        final JSType type = JSType.of(value);
        if (type == JSType.OBJECT) {
            if (ScriptObject.isArray(value) || isJSObjectArray(value)) {
                return JA(value, state);
            }
            if (value instanceof ScriptObject || value instanceof JSObject) {
                return JO(value, state);
            }
        }
        return ScriptRuntime.UNDEFINED;
    }
    
    private static String JO(final Object value, final StringifyState state) {
        assert value instanceof ScriptObject || value instanceof JSObject;
        if (state.stack.containsKey(value)) {
            throw ECMAErrors.typeError("JSON.stringify.cyclic", new String[0]);
        }
        state.stack.put(value, value);
        final StringBuilder stepback = new StringBuilder(state.indent.toString());
        state.indent.append(state.gap);
        final StringBuilder finalStr = new StringBuilder();
        final List<Object> partial = new ArrayList<Object>();
        final List<String> k = (state.propertyList == null) ? Arrays.asList(getOwnKeys(value)) : state.propertyList;
        for (final Object p : k) {
            final Object strP = str(p, value, state);
            if (strP != ScriptRuntime.UNDEFINED) {
                final StringBuilder member = new StringBuilder();
                member.append(JSONFunctions.quote(p.toString())).append(':');
                if (!state.gap.isEmpty()) {
                    member.append(' ');
                }
                member.append(strP);
                partial.add(member);
            }
        }
        if (partial.isEmpty()) {
            finalStr.append("{}");
        }
        else if (state.gap.isEmpty()) {
            final int size = partial.size();
            int index = 0;
            finalStr.append('{');
            for (final Object str : partial) {
                finalStr.append(str);
                if (index < size - 1) {
                    finalStr.append(',');
                }
                ++index;
            }
            finalStr.append('}');
        }
        else {
            final int size = partial.size();
            int index = 0;
            finalStr.append("{\n");
            finalStr.append((CharSequence)state.indent);
            for (final Object str : partial) {
                finalStr.append(str);
                if (index < size - 1) {
                    finalStr.append(",\n");
                    finalStr.append((CharSequence)state.indent);
                }
                ++index;
            }
            finalStr.append('\n');
            finalStr.append((CharSequence)stepback);
            finalStr.append('}');
        }
        state.stack.remove(value);
        state.indent = stepback;
        return finalStr.toString();
    }
    
    private static Object JA(final Object value, final StringifyState state) {
        assert value instanceof ScriptObject || value instanceof JSObject;
        if (state.stack.containsKey(value)) {
            throw ECMAErrors.typeError("JSON.stringify.cyclic", new String[0]);
        }
        state.stack.put(value, value);
        final StringBuilder stepback = new StringBuilder(state.indent.toString());
        state.indent.append(state.gap);
        final List<Object> partial = new ArrayList<Object>();
        for (int length = JSType.toInteger(getLength(value)), index = 0; index < length; ++index) {
            Object strP = str(index, value, state);
            if (strP == ScriptRuntime.UNDEFINED) {
                strP = "null";
            }
            partial.add(strP);
        }
        final StringBuilder finalStr = new StringBuilder();
        if (partial.isEmpty()) {
            finalStr.append("[]");
        }
        else if (state.gap.isEmpty()) {
            final int size = partial.size();
            int index = 0;
            finalStr.append('[');
            for (final Object str : partial) {
                finalStr.append(str);
                if (index < size - 1) {
                    finalStr.append(',');
                }
                ++index;
            }
            finalStr.append(']');
        }
        else {
            final int size = partial.size();
            int index = 0;
            finalStr.append("[\n");
            finalStr.append((CharSequence)state.indent);
            for (final Object str : partial) {
                finalStr.append(str);
                if (index < size - 1) {
                    finalStr.append(",\n");
                    finalStr.append((CharSequence)state.indent);
                }
                ++index;
            }
            finalStr.append('\n');
            finalStr.append((CharSequence)stepback);
            finalStr.append(']');
        }
        state.stack.remove(value);
        state.indent = stepback;
        return finalStr.toString();
    }
    
    private static String[] getOwnKeys(final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).getOwnKeys(false);
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).getOwnKeys(false);
        }
        if (obj instanceof JSObject) {
            return ((JSObject)obj).keySet().toArray(new String[0]);
        }
        throw new AssertionError((Object)"should not reach here");
    }
    
    private static Object getLength(final Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).getLength();
        }
        if (obj instanceof JSObject) {
            return ((JSObject)obj).getMember("length");
        }
        throw new AssertionError((Object)"should not reach here");
    }
    
    private static boolean isJSObjectArray(final Object obj) {
        return obj instanceof JSObject && ((JSObject)obj).isArray();
    }
    
    private static Object getProperty(final Object holder, final Object key) {
        if (holder instanceof ScriptObject) {
            return ((ScriptObject)holder).get(key);
        }
        if (!(holder instanceof JSObject)) {
            return new AssertionError((Object)"should not reach here");
        }
        final JSObject jsObj = (JSObject)holder;
        if (key instanceof Integer) {
            return jsObj.getSlot((int)key);
        }
        return jsObj.getMember(Objects.toString(key));
    }
    
    static {
        TO_JSON = new Object();
        JSOBJECT_INVOKER = new Object();
        REPLACER_INVOKER = new Object();
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeJSON.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
    
    private static class StringifyState
    {
        final Map<Object, Object> stack;
        StringBuilder indent;
        String gap;
        List<String> propertyList;
        Object replacerFunction;
        
        private StringifyState() {
            this.stack = new IdentityHashMap<Object, Object>();
            this.indent = new StringBuilder();
            this.gap = "";
            this.propertyList = null;
            this.replacerFunction = null;
        }
    }
}
