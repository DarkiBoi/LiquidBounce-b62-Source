// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.lookup;

import java.util.Arrays;
import java.lang.invoke.SwitchPoint;
import java.lang.reflect.Method;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.options.Options;
import java.lang.invoke.MethodType;
import java.util.List;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.ArrayList;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.Context;
import java.lang.invoke.MethodHandle;
import java.util.logging.Level;
import java.lang.invoke.MethodHandles;

public final class MethodHandleFactory
{
    private static final MethodHandles.Lookup PUBLIC_LOOKUP;
    private static final MethodHandles.Lookup LOOKUP;
    private static final Level TRACE_LEVEL;
    private static final MethodHandleFunctionality FUNC;
    private static final boolean PRINT_STACKTRACE;
    private static final MethodHandle TRACE;
    private static final MethodHandle TRACE_RETURN;
    private static final MethodHandle TRACE_RETURN_VOID;
    private static final String VOID_TAG = "[VOID]";
    
    private MethodHandleFactory() {
    }
    
    public static String stripName(final Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Class) {
            return ((Class)obj).getSimpleName();
        }
        return obj.toString();
    }
    
    public static MethodHandleFunctionality getFunctionality() {
        return MethodHandleFactory.FUNC;
    }
    
    private static void err(final String str) {
        Context.getContext().getErr().println(str);
    }
    
    static Object traceReturn(final DebugLogger logger, final Object value) {
        final String str = "    return" + ("[VOID]".equals(value) ? ";" : (" " + stripName(value) + "; // [type=" + ((value == null) ? "null]" : (stripName(value.getClass()) + ']'))));
        if (logger == null) {
            err(str);
        }
        else if (logger.isEnabled()) {
            logger.log(MethodHandleFactory.TRACE_LEVEL, str);
        }
        return value;
    }
    
    static void traceReturnVoid(final DebugLogger logger) {
        traceReturn(logger, "[VOID]");
    }
    
    static void traceArgs(final DebugLogger logger, final String tag, final int paramStart, final Object... args) {
        final StringBuilder sb = new StringBuilder();
        sb.append(tag);
        for (int i = paramStart; i < args.length; ++i) {
            if (i == paramStart) {
                sb.append(" => args: ");
            }
            sb.append('\'').append(stripName(argString(args[i]))).append('\'').append(' ').append('[').append("type=").append((args[i] == null) ? "null" : stripName(args[i].getClass())).append(']');
            if (i + 1 < args.length) {
                sb.append(", ");
            }
        }
        if (logger == null) {
            err(sb.toString());
        }
        else {
            logger.log(MethodHandleFactory.TRACE_LEVEL, sb);
        }
        stacktrace(logger);
    }
    
    private static void stacktrace(final DebugLogger logger) {
        if (!MethodHandleFactory.PRINT_STACKTRACE) {
            return;
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(baos);
        new Throwable().printStackTrace(ps);
        final String st = baos.toString();
        if (logger == null) {
            err(st);
        }
        else {
            logger.log(MethodHandleFactory.TRACE_LEVEL, st);
        }
    }
    
    private static String argString(final Object arg) {
        if (arg == null) {
            return "null";
        }
        if (arg.getClass().isArray()) {
            final List<Object> list = new ArrayList<Object>();
            for (final Object elem : (Object[])arg) {
                list.add('\'' + argString(elem) + '\'');
            }
            return list.toString();
        }
        if (arg instanceof ScriptObject) {
            return arg.toString() + " (map=" + Debug.id(((ScriptObject)arg).getMap()) + ')';
        }
        return arg.toString();
    }
    
    public static MethodHandle addDebugPrintout(final MethodHandle mh, final Object tag) {
        return addDebugPrintout(null, Level.OFF, mh, 0, true, tag);
    }
    
    public static MethodHandle addDebugPrintout(final DebugLogger logger, final Level level, final MethodHandle mh, final Object tag) {
        return addDebugPrintout(logger, level, mh, 0, true, tag);
    }
    
    public static MethodHandle addDebugPrintout(final MethodHandle mh, final int paramStart, final boolean printReturnValue, final Object tag) {
        return addDebugPrintout(null, Level.OFF, mh, paramStart, printReturnValue, tag);
    }
    
    public static MethodHandle addDebugPrintout(final DebugLogger logger, final Level level, final MethodHandle mh, final int paramStart, final boolean printReturnValue, final Object tag) {
        final MethodType type = mh.type();
        if (logger == null || !logger.isLoggable(level)) {
            return mh;
        }
        assert MethodHandleFactory.TRACE != null;
        MethodHandle trace = MethodHandles.insertArguments(MethodHandleFactory.TRACE, 0, logger, tag, paramStart);
        trace = MethodHandles.foldArguments(mh, trace.asCollector(Object[].class, type.parameterCount()).asType(type.changeReturnType((Class<?>)Void.TYPE)));
        final Class<?> retType = type.returnType();
        if (printReturnValue) {
            if (retType != Void.TYPE) {
                final MethodHandle traceReturn = MethodHandles.insertArguments(MethodHandleFactory.TRACE_RETURN, 0, logger);
                trace = MethodHandles.filterReturnValue(trace, traceReturn.asType(traceReturn.type().changeParameterType(0, retType).changeReturnType(retType)));
            }
            else {
                trace = MethodHandles.filterReturnValue(trace, MethodHandles.insertArguments(MethodHandleFactory.TRACE_RETURN_VOID, 0, logger));
            }
        }
        return trace;
    }
    
    static {
        PUBLIC_LOOKUP = MethodHandles.publicLookup();
        LOOKUP = MethodHandles.lookup();
        TRACE_LEVEL = Level.INFO;
        FUNC = new StandardMethodHandleFunctionality();
        PRINT_STACKTRACE = Options.getBooleanProperty("nashorn.methodhandles.debug.stacktrace");
        TRACE = MethodHandleFactory.FUNC.findStatic(MethodHandleFactory.LOOKUP, MethodHandleFactory.class, "traceArgs", MethodType.methodType(Void.TYPE, DebugLogger.class, String.class, Integer.TYPE, Object[].class));
        TRACE_RETURN = MethodHandleFactory.FUNC.findStatic(MethodHandleFactory.LOOKUP, MethodHandleFactory.class, "traceReturn", MethodType.methodType(Object.class, DebugLogger.class, Object.class));
        TRACE_RETURN_VOID = MethodHandleFactory.FUNC.findStatic(MethodHandleFactory.LOOKUP, MethodHandleFactory.class, "traceReturnVoid", MethodType.methodType(Void.TYPE, DebugLogger.class));
    }
    
    public static class LookupException extends RuntimeException
    {
        public LookupException(final Exception e) {
            super(e);
        }
    }
    
    @Logger(name = "methodhandles")
    private static class StandardMethodHandleFunctionality implements MethodHandleFunctionality, Loggable
    {
        private DebugLogger log;
        
        public StandardMethodHandleFunctionality() {
            this.log = DebugLogger.DISABLED_LOGGER;
        }
        
        @Override
        public DebugLogger initLogger(final Context context) {
            return this.log = context.getLogger(this.getClass());
        }
        
        @Override
        public DebugLogger getLogger() {
            return this.log;
        }
        
        protected static String describe(final Object... data) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; ++i) {
                final Object d = data[i];
                if (d == null) {
                    sb.append("<null> ");
                }
                else if (JSType.isString(d)) {
                    sb.append(d.toString());
                    sb.append(' ');
                }
                else if (d.getClass().isArray()) {
                    sb.append("[ ");
                    for (final Object da : (Object[])d) {
                        sb.append(describe(da)).append(' ');
                    }
                    sb.append("] ");
                }
                else {
                    sb.append(d).append('{').append(Integer.toHexString(System.identityHashCode(d))).append('}');
                }
                if (i + 1 < data.length) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        
        public MethodHandle debug(final MethodHandle master, final String str, final Object... args) {
            if (this.log.isEnabled()) {
                if (MethodHandleFactory.PRINT_STACKTRACE) {
                    stacktrace(this.log);
                }
                return MethodHandleFactory.addDebugPrintout(this.log, Level.INFO, master, Integer.MAX_VALUE, false, str + ' ' + describe(args));
            }
            return master;
        }
        
        @Override
        public MethodHandle filterArguments(final MethodHandle target, final int pos, final MethodHandle... filters) {
            final MethodHandle mh = MethodHandles.filterArguments(target, pos, filters);
            return this.debug(mh, "filterArguments", target, pos, filters);
        }
        
        @Override
        public MethodHandle filterReturnValue(final MethodHandle target, final MethodHandle filter) {
            final MethodHandle mh = MethodHandles.filterReturnValue(target, filter);
            return this.debug(mh, "filterReturnValue", target, filter);
        }
        
        @Override
        public MethodHandle guardWithTest(final MethodHandle test, final MethodHandle target, final MethodHandle fallback) {
            final MethodHandle mh = MethodHandles.guardWithTest(test, target, fallback);
            return this.debug(mh, "guardWithTest", test, target, fallback);
        }
        
        @Override
        public MethodHandle insertArguments(final MethodHandle target, final int pos, final Object... values) {
            final MethodHandle mh = MethodHandles.insertArguments(target, pos, values);
            return this.debug(mh, "insertArguments", target, pos, values);
        }
        
        @Override
        public MethodHandle dropArguments(final MethodHandle target, final int pos, final Class<?>... values) {
            final MethodHandle mh = MethodHandles.dropArguments(target, pos, values);
            return this.debug(mh, "dropArguments", target, pos, values);
        }
        
        @Override
        public MethodHandle dropArguments(final MethodHandle target, final int pos, final List<Class<?>> values) {
            final MethodHandle mh = MethodHandles.dropArguments(target, pos, values);
            return this.debug(mh, "dropArguments", target, pos, values);
        }
        
        @Override
        public MethodHandle asType(final MethodHandle handle, final MethodType type) {
            final MethodHandle mh = handle.asType(type);
            return this.debug(mh, "asType", handle, type);
        }
        
        @Override
        public MethodHandle bindTo(final MethodHandle handle, final Object x) {
            final MethodHandle mh = handle.bindTo(x);
            return this.debug(mh, "bindTo", handle, x);
        }
        
        @Override
        public MethodHandle foldArguments(final MethodHandle target, final MethodHandle combiner) {
            final MethodHandle mh = MethodHandles.foldArguments(target, combiner);
            return this.debug(mh, "foldArguments", target, combiner);
        }
        
        @Override
        public MethodHandle explicitCastArguments(final MethodHandle target, final MethodType type) {
            final MethodHandle mh = MethodHandles.explicitCastArguments(target, type);
            return this.debug(mh, "explicitCastArguments", target, type);
        }
        
        @Override
        public MethodHandle arrayElementGetter(final Class<?> type) {
            final MethodHandle mh = MethodHandles.arrayElementGetter(type);
            return this.debug(mh, "arrayElementGetter", type);
        }
        
        @Override
        public MethodHandle arrayElementSetter(final Class<?> type) {
            final MethodHandle mh = MethodHandles.arrayElementSetter(type);
            return this.debug(mh, "arrayElementSetter", type);
        }
        
        @Override
        public MethodHandle throwException(final Class<?> returnType, final Class<? extends Throwable> exType) {
            final MethodHandle mh = MethodHandles.throwException(returnType, exType);
            return this.debug(mh, "throwException", returnType, exType);
        }
        
        @Override
        public MethodHandle catchException(final MethodHandle target, final Class<? extends Throwable> exType, final MethodHandle handler) {
            final MethodHandle mh = MethodHandles.catchException(target, exType, handler);
            return this.debug(mh, "catchException", exType);
        }
        
        @Override
        public MethodHandle constant(final Class<?> type, final Object value) {
            final MethodHandle mh = MethodHandles.constant(type, value);
            return this.debug(mh, "constant", type, value);
        }
        
        @Override
        public MethodHandle identity(final Class<?> type) {
            final MethodHandle mh = MethodHandles.identity(type);
            return this.debug(mh, "identity", type);
        }
        
        @Override
        public MethodHandle asCollector(final MethodHandle handle, final Class<?> arrayType, final int arrayLength) {
            final MethodHandle mh = handle.asCollector(arrayType, arrayLength);
            return this.debug(mh, "asCollector", handle, arrayType, arrayLength);
        }
        
        @Override
        public MethodHandle asSpreader(final MethodHandle handle, final Class<?> arrayType, final int arrayLength) {
            final MethodHandle mh = handle.asSpreader(arrayType, arrayLength);
            return this.debug(mh, "asSpreader", handle, arrayType, arrayLength);
        }
        
        @Override
        public MethodHandle getter(final MethodHandles.Lookup explicitLookup, final Class<?> clazz, final String name, final Class<?> type) {
            try {
                final MethodHandle mh = explicitLookup.findGetter(clazz, name, type);
                return this.debug(mh, "getter", explicitLookup, clazz, name, type);
            }
            catch (NoSuchFieldException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new LookupException(e);
            }
        }
        
        @Override
        public MethodHandle staticGetter(final MethodHandles.Lookup explicitLookup, final Class<?> clazz, final String name, final Class<?> type) {
            try {
                final MethodHandle mh = explicitLookup.findStaticGetter(clazz, name, type);
                return this.debug(mh, "static getter", explicitLookup, clazz, name, type);
            }
            catch (NoSuchFieldException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new LookupException(e);
            }
        }
        
        @Override
        public MethodHandle setter(final MethodHandles.Lookup explicitLookup, final Class<?> clazz, final String name, final Class<?> type) {
            try {
                final MethodHandle mh = explicitLookup.findSetter(clazz, name, type);
                return this.debug(mh, "setter", explicitLookup, clazz, name, type);
            }
            catch (NoSuchFieldException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new LookupException(e);
            }
        }
        
        @Override
        public MethodHandle staticSetter(final MethodHandles.Lookup explicitLookup, final Class<?> clazz, final String name, final Class<?> type) {
            try {
                final MethodHandle mh = explicitLookup.findStaticSetter(clazz, name, type);
                return this.debug(mh, "static setter", explicitLookup, clazz, name, type);
            }
            catch (NoSuchFieldException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new LookupException(e);
            }
        }
        
        @Override
        public MethodHandle find(final Method method) {
            try {
                final MethodHandle mh = MethodHandleFactory.PUBLIC_LOOKUP.unreflect(method);
                return this.debug(mh, "find", method);
            }
            catch (IllegalAccessException e) {
                throw new LookupException(e);
            }
        }
        
        @Override
        public MethodHandle findStatic(final MethodHandles.Lookup explicitLookup, final Class<?> clazz, final String name, final MethodType type) {
            try {
                final MethodHandle mh = explicitLookup.findStatic(clazz, name, type);
                return this.debug(mh, "findStatic", explicitLookup, clazz, name, type);
            }
            catch (NoSuchMethodException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new LookupException(e);
            }
        }
        
        @Override
        public MethodHandle findSpecial(final MethodHandles.Lookup explicitLookup, final Class<?> clazz, final String name, final MethodType type, final Class<?> thisClass) {
            try {
                final MethodHandle mh = explicitLookup.findSpecial(clazz, name, type, thisClass);
                return this.debug(mh, "findSpecial", explicitLookup, clazz, name, type);
            }
            catch (NoSuchMethodException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new LookupException(e);
            }
        }
        
        @Override
        public MethodHandle findVirtual(final MethodHandles.Lookup explicitLookup, final Class<?> clazz, final String name, final MethodType type) {
            try {
                final MethodHandle mh = explicitLookup.findVirtual(clazz, name, type);
                return this.debug(mh, "findVirtual", explicitLookup, clazz, name, type);
            }
            catch (NoSuchMethodException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new LookupException(e);
            }
        }
        
        @Override
        public SwitchPoint createSwitchPoint() {
            final SwitchPoint sp = new SwitchPoint();
            this.log.log(MethodHandleFactory.TRACE_LEVEL, "createSwitchPoint ", sp);
            return sp;
        }
        
        @Override
        public MethodHandle guardWithTest(final SwitchPoint sp, final MethodHandle before, final MethodHandle after) {
            final MethodHandle mh = sp.guardWithTest(before, after);
            return this.debug(mh, "guardWithTest", sp, before, after);
        }
        
        @Override
        public MethodType type(final Class<?> returnType, final Class<?>... paramTypes) {
            final MethodType mt = MethodType.methodType(returnType, paramTypes);
            this.log.log(MethodHandleFactory.TRACE_LEVEL, "methodType ", returnType, " ", Arrays.toString(paramTypes), " ", mt);
            return mt;
        }
    }
}
