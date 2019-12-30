// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.Exported;

@Exported
public final class ScriptUtils
{
    private ScriptUtils() {
    }
    
    public static String parse(final String code, final String name, final boolean includeLoc) {
        return ScriptRuntime.parse(code, name, includeLoc);
    }
    
    public static String format(final String format, final Object[] args) {
        return Formatter.format(format, args);
    }
    
    public static Object makeSynchronizedFunction(final Object func, final Object sync) {
        final Object unwrapped = unwrap(func);
        if (unwrapped instanceof ScriptFunction) {
            return ((ScriptFunction)unwrapped).createSynchronized(unwrap(sync));
        }
        throw new IllegalArgumentException();
    }
    
    public static ScriptObjectMirror wrap(final Object obj) {
        if (obj instanceof ScriptObjectMirror) {
            return (ScriptObjectMirror)obj;
        }
        if (obj instanceof ScriptObject) {
            final ScriptObject sobj = (ScriptObject)obj;
            return (ScriptObjectMirror)ScriptObjectMirror.wrap(sobj, Context.getGlobal());
        }
        throw new IllegalArgumentException();
    }
    
    public static Object unwrap(final Object obj) {
        if (obj instanceof ScriptObjectMirror) {
            return ScriptObjectMirror.unwrap(obj, Context.getGlobal());
        }
        return obj;
    }
    
    public static Object[] wrapArray(final Object[] args) {
        if (args == null || args.length == 0) {
            return args;
        }
        return ScriptObjectMirror.wrapArray(args, Context.getGlobal());
    }
    
    public static Object[] unwrapArray(final Object[] args) {
        if (args == null || args.length == 0) {
            return args;
        }
        return ScriptObjectMirror.unwrapArray(args, Context.getGlobal());
    }
    
    public static Object convert(final Object obj, final Object type) {
        if (obj == null) {
            return null;
        }
        Class<?> clazz;
        if (type instanceof Class) {
            clazz = (Class<?>)type;
        }
        else {
            if (!(type instanceof StaticClass)) {
                throw new IllegalArgumentException("type expected");
            }
            clazz = ((StaticClass)type).getRepresentedClass();
        }
        final LinkerServices linker = Bootstrap.getLinkerServices();
        final Object objToConvert = unwrap(obj);
        final MethodHandle converter = linker.getTypeConverter(objToConvert.getClass(), clazz);
        if (converter == null) {
            throw new UnsupportedOperationException("conversion not supported");
        }
        try {
            return converter.invoke(objToConvert);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
