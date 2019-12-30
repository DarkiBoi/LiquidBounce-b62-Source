// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import java.util.concurrent.Callable;
import java.lang.invoke.MethodHandle;

public final class JSONFunctions
{
    private static final Object REVIVER_INVOKER;
    
    private JSONFunctions() {
    }
    
    private static MethodHandle getREVIVER_INVOKER() {
        return Context.getGlobal().getDynamicInvoker(JSONFunctions.REVIVER_INVOKER, new Callable<MethodHandle>() {
            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", Object.class, Object.class, Object.class, String.class, Object.class);
            }
        });
    }
    
    public static String quote(final String str) {
        return JSONParser.quote(str);
    }
    
    public static Object parse(final Object text, final Object reviver) {
        final String str = JSType.toString(text);
        final Global global = Context.getGlobal();
        final boolean dualFields = global.useDualFields();
        final JSONParser parser = new JSONParser(str, global, dualFields);
        Object value;
        try {
            value = parser.parse();
        }
        catch (ParserException e) {
            throw ECMAErrors.syntaxError(e, "invalid.json", e.getMessage());
        }
        return applyReviver(global, value, reviver);
    }
    
    private static Object applyReviver(final Global global, final Object unfiltered, final Object reviver) {
        if (Bootstrap.isCallable(reviver)) {
            final ScriptObject root = global.newObject();
            root.addOwnProperty("", 0, unfiltered);
            return walk(root, "", reviver);
        }
        return unfiltered;
    }
    
    private static Object walk(final ScriptObject holder, final Object name, final Object reviver) {
        final Object val = holder.get(name);
        if (val instanceof ScriptObject) {
            final ScriptObject valueObj = (ScriptObject)val;
            if (valueObj.isArray()) {
                for (int length = JSType.toInteger(valueObj.getLength()), i = 0; i < length; ++i) {
                    final String key = Integer.toString(i);
                    final Object newElement = walk(valueObj, key, reviver);
                    if (newElement == ScriptRuntime.UNDEFINED) {
                        valueObj.delete(i, false);
                    }
                    else {
                        setPropertyValue(valueObj, key, newElement);
                    }
                }
            }
            else {
                final String[] ownKeys;
                final String[] keys = ownKeys = valueObj.getOwnKeys(false);
                for (final String key2 : ownKeys) {
                    final Object newElement2 = walk(valueObj, key2, reviver);
                    if (newElement2 == ScriptRuntime.UNDEFINED) {
                        valueObj.delete(key2, false);
                    }
                    else {
                        setPropertyValue(valueObj, key2, newElement2);
                    }
                }
            }
        }
        try {
            return getREVIVER_INVOKER().invokeExact(reviver, (Object)holder, JSType.toString(name), val);
        }
        catch (Error | RuntimeException error) {
            final Throwable t2;
            final Throwable t = t2;
            throw t;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    private static void setPropertyValue(final ScriptObject sobj, final String name, final Object value) {
        final int index = ArrayIndex.getArrayIndex(name);
        if (ArrayIndex.isValidArrayIndex(index)) {
            sobj.defineOwnProperty(index, value);
        }
        else if (sobj.getMap().findProperty(name) != null) {
            sobj.set(name, value, 0);
        }
        else {
            sobj.addOwnProperty(name, 0, value);
        }
    }
    
    static {
        REVIVER_INVOKER = new Object();
    }
}
