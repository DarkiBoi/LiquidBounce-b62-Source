// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import jdk.internal.dynalink.support.Lookup;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.JSType;
import java.util.List;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodHandle;

public final class NativeFunction
{
    public static final MethodHandle TO_APPLY_ARGS;
    private static PropertyMap $nasgenmap$;
    
    private NativeFunction() {
        throw new UnsupportedOperationException();
    }
    
    public static String toString(final Object self) {
        if (!(self instanceof ScriptFunction)) {
            throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(self));
        }
        return ((ScriptFunction)self).toSource();
    }
    
    public static Object apply(final Object self, final Object thiz, final Object array) {
        checkCallable(self);
        final Object[] args = toApplyArgs(array);
        if (self instanceof ScriptFunction) {
            return ScriptRuntime.apply((ScriptFunction)self, thiz, args);
        }
        if (self instanceof JSObject) {
            return ((JSObject)self).call(thiz, args);
        }
        throw new AssertionError((Object)"Should not reach here");
    }
    
    public static Object[] toApplyArgs(final Object array) {
        if (array instanceof NativeArguments) {
            return ((NativeArguments)array).getArray().asObjectArray();
        }
        if (array instanceof ScriptObject) {
            final ScriptObject sobj = (ScriptObject)array;
            final int n = lengthToInt(sobj.getLength());
            final Object[] args = new Object[n];
            for (int i = 0; i < args.length; ++i) {
                args[i] = sobj.get(i);
            }
            return args;
        }
        if (array instanceof Object[]) {
            return (Object[])array;
        }
        if (array instanceof List) {
            final List<?> list = (List<?>)array;
            return list.toArray(new Object[list.size()]);
        }
        if (array == null || array == ScriptRuntime.UNDEFINED) {
            return ScriptRuntime.EMPTY_ARRAY;
        }
        if (array instanceof JSObject) {
            final JSObject jsObj = (JSObject)array;
            final Object len = jsObj.hasMember("length") ? jsObj.getMember("length") : Integer.valueOf(0);
            final int n2 = lengthToInt(len);
            final Object[] args2 = new Object[n2];
            for (int j = 0; j < args2.length; ++j) {
                args2[j] = (jsObj.hasSlot(j) ? jsObj.getSlot(j) : ScriptRuntime.UNDEFINED);
            }
            return args2;
        }
        throw ECMAErrors.typeError("function.apply.expects.array", new String[0]);
    }
    
    private static int lengthToInt(final Object len) {
        final long ln = JSType.toUint32(len);
        if (ln > 2147483647L) {
            throw ECMAErrors.rangeError("range.error.inappropriate.array.length", JSType.toString(len));
        }
        return (int)ln;
    }
    
    private static void checkCallable(final Object self) {
        if (!(self instanceof ScriptFunction) && (!(self instanceof JSObject) || !((JSObject)self).isFunction())) {
            throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(self));
        }
    }
    
    public static Object call(final Object self, final Object... args) {
        checkCallable(self);
        final Object thiz = (args.length == 0) ? ScriptRuntime.UNDEFINED : args[0];
        Object[] arguments;
        if (args.length > 1) {
            arguments = new Object[args.length - 1];
            System.arraycopy(args, 1, arguments, 0, arguments.length);
        }
        else {
            arguments = ScriptRuntime.EMPTY_ARRAY;
        }
        if (self instanceof ScriptFunction) {
            return ScriptRuntime.apply((ScriptFunction)self, thiz, arguments);
        }
        if (self instanceof JSObject) {
            return ((JSObject)self).call(thiz, arguments);
        }
        throw new AssertionError((Object)"should not reach here");
    }
    
    public static Object bind(final Object self, final Object... args) {
        final Object thiz = (args.length == 0) ? ScriptRuntime.UNDEFINED : args[0];
        Object[] arguments;
        if (args.length > 1) {
            arguments = new Object[args.length - 1];
            System.arraycopy(args, 1, arguments, 0, arguments.length);
        }
        else {
            arguments = ScriptRuntime.EMPTY_ARRAY;
        }
        return Bootstrap.bindCallable(self, thiz, arguments);
    }
    
    public static String toSource(final Object self) {
        if (!(self instanceof ScriptFunction)) {
            throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(self));
        }
        return ((ScriptFunction)self).toSource();
    }
    
    public static ScriptFunction function(final boolean newObj, final Object self, final Object... args) {
        final StringBuilder sb = new StringBuilder();
        sb.append("(function (");
        String funcBody;
        if (args.length > 0) {
            final StringBuilder paramListBuf = new StringBuilder();
            for (int i = 0; i < args.length - 1; ++i) {
                paramListBuf.append(JSType.toString(args[i]));
                if (i < args.length - 2) {
                    paramListBuf.append(",");
                }
            }
            funcBody = JSType.toString(args[args.length - 1]);
            final String paramList = paramListBuf.toString();
            if (!paramList.isEmpty()) {
                checkFunctionParameters(paramList);
                sb.append(paramList);
            }
        }
        else {
            funcBody = null;
        }
        sb.append(") {\n");
        if (args.length > 0) {
            checkFunctionBody(funcBody);
            sb.append(funcBody);
            sb.append('\n');
        }
        sb.append("})");
        final Global global = Global.instance();
        final Context context = global.getContext();
        return (ScriptFunction)context.eval(global, sb.toString(), global, "<function>");
    }
    
    private static void checkFunctionParameters(final String params) {
        final Parser parser = getParser(params);
        try {
            parser.parseFormalParameterList();
        }
        catch (ParserException pe) {
            pe.throwAsEcmaException();
        }
    }
    
    private static void checkFunctionBody(final String funcBody) {
        final Parser parser = getParser(funcBody);
        try {
            parser.parseFunctionBody();
        }
        catch (ParserException pe) {
            pe.throwAsEcmaException();
        }
    }
    
    private static Parser getParser(final String sourceText) {
        final ScriptEnvironment env = Global.getEnv();
        return new Parser(env, Source.sourceFor("<function>", sourceText), new Context.ThrowErrorManager(), env._strict, null);
    }
    
    static {
        TO_APPLY_ARGS = Lookup.findOwnStatic(MethodHandles.lookup(), "toApplyArgs", Object[].class, Object.class);
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeFunction.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
