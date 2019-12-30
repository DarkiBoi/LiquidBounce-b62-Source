// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.net.URL;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Field;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.scripts.JS;
import java.lang.invoke.MethodHandle;

final class DebuggerSupport
{
    static boolean FORCELOAD;
    
    static void notifyInvoke(final MethodHandle mh) {
    }
    
    static SourceInfo getSourceInfo(final Class<?> clazz) {
        if (JS.class.isAssignableFrom(clazz)) {
            try {
                final Field sourceField = clazz.getDeclaredField(CompilerConstants.SOURCE.symbolName());
                sourceField.setAccessible(true);
                final Source src = (Source)sourceField.get(null);
                return src.getSourceInfo();
            }
            catch (IllegalAccessException | NoSuchFieldException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException ignored = ex;
                return null;
            }
        }
        return null;
    }
    
    static Object getGlobal() {
        return Context.getGlobal();
    }
    
    static Object eval(final ScriptObject scope, final Object self, final String string, final boolean returnException) {
        final ScriptObject global = Context.getGlobal();
        final ScriptObject initialScope = (scope != null) ? scope : global;
        final Object callThis = (self != null) ? self : global;
        final Context context = global.getContext();
        try {
            return context.eval(initialScope, string, callThis, ScriptRuntime.UNDEFINED);
        }
        catch (Throwable ex) {
            return returnException ? ex : null;
        }
    }
    
    static DebuggerValueDesc[] valueInfos(final Object object, final boolean all) {
        assert object instanceof ScriptObject;
        return getDebuggerValueDescs((ScriptObject)object, all, new HashSet<Object>());
    }
    
    static DebuggerValueDesc valueInfo(final String name, final Object value, final boolean all) {
        return valueInfo(name, value, all, new HashSet<Object>());
    }
    
    private static DebuggerValueDesc valueInfo(final String name, final Object value, final boolean all, final Set<Object> duplicates) {
        if (value instanceof ScriptObject && !(value instanceof ScriptFunction)) {
            final ScriptObject object = (ScriptObject)value;
            return new DebuggerValueDesc(name, !object.isEmpty(), value, objectAsString(object, all, duplicates));
        }
        return new DebuggerValueDesc(name, false, value, valueAsString(value));
    }
    
    private static DebuggerValueDesc[] getDebuggerValueDescs(final ScriptObject object, final boolean all, final Set<Object> duplicates) {
        if (duplicates.contains(object)) {
            return null;
        }
        duplicates.add(object);
        final String[] keys = object.getOwnKeys(all);
        final DebuggerValueDesc[] descs = new DebuggerValueDesc[keys.length];
        for (int i = 0; i < keys.length; ++i) {
            final String key = keys[i];
            descs[i] = valueInfo(key, object.get(key), all, duplicates);
        }
        duplicates.remove(object);
        return descs;
    }
    
    private static String objectAsString(final ScriptObject object, final boolean all, final Set<Object> duplicates) {
        final StringBuilder sb = new StringBuilder();
        if (ScriptObject.isArray(object)) {
            sb.append('[');
            for (long length = (long)object.getDouble("length", -1), i = 0L; i < length; ++i) {
                if (object.has((double)i)) {
                    final Object valueAsObject = object.get((double)i);
                    final boolean isUndefined = valueAsObject == ScriptRuntime.UNDEFINED;
                    if (isUndefined) {
                        if (i != 0L) {
                            sb.append(",");
                        }
                    }
                    else {
                        if (i != 0L) {
                            sb.append(", ");
                        }
                        if (valueAsObject instanceof ScriptObject && !(valueAsObject instanceof ScriptFunction)) {
                            final String objectString = objectAsString((ScriptObject)valueAsObject, all, duplicates);
                            sb.append((objectString != null) ? objectString : "{...}");
                        }
                        else {
                            sb.append(valueAsString(valueAsObject));
                        }
                    }
                }
                else if (i != 0L) {
                    sb.append(',');
                }
            }
            sb.append(']');
        }
        else {
            sb.append('{');
            final DebuggerValueDesc[] descs = getDebuggerValueDescs(object, all, duplicates);
            if (descs != null) {
                for (int j = 0; j < descs.length; ++j) {
                    if (j != 0) {
                        sb.append(", ");
                    }
                    final String valueAsString = descs[j].valueAsString;
                    sb.append(descs[j].key);
                    sb.append(": ");
                    sb.append(valueAsString);
                }
            }
            sb.append('}');
        }
        return sb.toString();
    }
    
    static String valueAsString(final Object value) {
        final JSType type = JSType.of(value);
        switch (type) {
            case BOOLEAN: {
                return value.toString();
            }
            case STRING: {
                return escape(value.toString());
            }
            case NUMBER: {
                return JSType.toString(((Number)value).doubleValue());
            }
            case NULL: {
                return "null";
            }
            case UNDEFINED: {
                return "undefined";
            }
            case OBJECT: {
                return ScriptRuntime.safeToString(value);
            }
            case FUNCTION: {
                if (value instanceof ScriptFunction) {
                    return ((ScriptFunction)value).toSource();
                }
                return value.toString();
            }
            default: {
                return value.toString();
            }
        }
    }
    
    private static String escape(final String value) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (final char ch : value.toCharArray()) {
            switch (ch) {
                case '\\': {
                    sb.append("\\\\");
                    break;
                }
                case '\"': {
                    sb.append("\\\"");
                    break;
                }
                case '\'': {
                    sb.append("\\'");
                    break;
                }
                case '\b': {
                    sb.append("\\b");
                    break;
                }
                case '\f': {
                    sb.append("\\f");
                    break;
                }
                case '\n': {
                    sb.append("\\n");
                    break;
                }
                case '\r': {
                    sb.append("\\r");
                    break;
                }
                case '\t': {
                    sb.append("\\t");
                    break;
                }
                default: {
                    if (ch < ' ' || ch >= '\u00ff') {
                        sb.append("\\u");
                        final String hex = Integer.toHexString(ch);
                        for (int i = hex.length(); i < 4; ++i) {
                            sb.append('0');
                        }
                        sb.append(hex);
                        break;
                    }
                    sb.append(ch);
                    break;
                }
            }
        }
        sb.append("\"");
        return sb.toString();
    }
    
    static {
        DebuggerSupport.FORCELOAD = true;
        final DebuggerValueDesc forceLoad = new DebuggerValueDesc(null, false, null, null);
        final SourceInfo sourceInfo = new SourceInfo(null, 0, null, null);
    }
    
    static class DebuggerValueDesc
    {
        final String key;
        final boolean expandable;
        final Object valueAsObject;
        final String valueAsString;
        
        DebuggerValueDesc(final String key, final boolean expandable, final Object valueAsObject, final String valueAsString) {
            this.key = key;
            this.expandable = expandable;
            this.valueAsObject = valueAsObject;
            this.valueAsString = valueAsString;
        }
    }
    
    static class SourceInfo
    {
        final String name;
        final URL url;
        final int hash;
        final char[] content;
        
        SourceInfo(final String name, final int hash, final URL url, final char[] content) {
            this.name = name;
            this.hash = hash;
            this.url = url;
            this.content = content;
        }
    }
}
