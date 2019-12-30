// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import java.util.Locale;
import java.lang.reflect.Method;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptFunction;
import java.util.Objects;
import javax.script.ScriptEngine;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Modifier;
import java.io.IOException;
import jdk.nashorn.internal.runtime.Source;
import javax.script.CompiledScript;
import javax.script.SimpleBindings;
import javax.script.ScriptException;
import javax.script.ScriptContext;
import java.io.Reader;
import javax.script.Bindings;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.security.AccessController;
import jdk.nashorn.internal.runtime.ErrorManager;
import java.security.PrivilegedAction;
import jdk.nashorn.internal.runtime.options.Options;
import java.util.MissingResourceException;
import java.text.MessageFormat;
import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Permission;
import java.security.Permissions;
import java.util.ResourceBundle;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import javax.script.ScriptEngineFactory;
import java.security.AccessControlContext;
import jdk.Exported;
import javax.script.Invocable;
import javax.script.Compilable;
import javax.script.AbstractScriptEngine;

@Exported
public final class NashornScriptEngine extends AbstractScriptEngine implements Compilable, Invocable
{
    public static final String NASHORN_GLOBAL = "nashorn.global";
    private static final AccessControlContext CREATE_CONTEXT_ACC_CTXT;
    private static final AccessControlContext CREATE_GLOBAL_ACC_CTXT;
    private final ScriptEngineFactory factory;
    private final Context nashornContext;
    private final boolean _global_per_engine;
    private final Global global;
    private static final String MESSAGES_RESOURCE = "jdk.nashorn.api.scripting.resources.Messages";
    private static final ResourceBundle MESSAGES_BUNDLE;
    
    private static AccessControlContext createPermAccCtxt(final String permName) {
        final Permissions perms = new Permissions();
        perms.add(new RuntimePermission(permName));
        return new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, perms) });
    }
    
    private static String getMessage(final String msgId, final String... args) {
        try {
            return new MessageFormat(NashornScriptEngine.MESSAGES_BUNDLE.getString(msgId)).format(args);
        }
        catch (MissingResourceException e) {
            throw new RuntimeException("no message resource found for message id: " + msgId);
        }
    }
    
    NashornScriptEngine(final NashornScriptEngineFactory factory, final String[] args, final ClassLoader appLoader, final ClassFilter classFilter) {
        assert args != null : "null argument array";
        this.factory = factory;
        final Options options = new Options("nashorn");
        options.process(args);
        final ErrorManager errMgr = new Context.ThrowErrorManager();
        this.nashornContext = AccessController.doPrivileged((PrivilegedAction<Context>)new PrivilegedAction<Context>() {
            @Override
            public Context run() {
                try {
                    return new Context(options, errMgr, appLoader, classFilter);
                }
                catch (RuntimeException e) {
                    if (Context.DEBUG) {
                        e.printStackTrace();
                    }
                    throw e;
                }
            }
        }, NashornScriptEngine.CREATE_CONTEXT_ACC_CTXT);
        this._global_per_engine = this.nashornContext.getEnv()._global_per_engine;
        this.global = this.createNashornGlobal();
        this.context.setBindings(new ScriptObjectMirror(this.global, this.global), 100);
    }
    
    @Override
    public Object eval(final Reader reader, final ScriptContext ctxt) throws ScriptException {
        return this.evalImpl(makeSource(reader, ctxt), ctxt);
    }
    
    @Override
    public Object eval(final String script, final ScriptContext ctxt) throws ScriptException {
        return this.evalImpl(makeSource(script, ctxt), ctxt);
    }
    
    @Override
    public ScriptEngineFactory getFactory() {
        return this.factory;
    }
    
    @Override
    public Bindings createBindings() {
        if (this._global_per_engine) {
            return new SimpleBindings();
        }
        return this.createGlobalMirror();
    }
    
    @Override
    public CompiledScript compile(final Reader reader) throws ScriptException {
        return this.asCompiledScript(makeSource(reader, this.context));
    }
    
    @Override
    public CompiledScript compile(final String str) throws ScriptException {
        return this.asCompiledScript(makeSource(str, this.context));
    }
    
    @Override
    public Object invokeFunction(final String name, final Object... args) throws ScriptException, NoSuchMethodException {
        return this.invokeImpl(null, name, args);
    }
    
    @Override
    public Object invokeMethod(final Object thiz, final String name, final Object... args) throws ScriptException, NoSuchMethodException {
        if (thiz == null) {
            throw new IllegalArgumentException(getMessage("thiz.cannot.be.null", new String[0]));
        }
        return this.invokeImpl(thiz, name, args);
    }
    
    @Override
    public <T> T getInterface(final Class<T> clazz) {
        return this.getInterfaceInner(null, clazz);
    }
    
    @Override
    public <T> T getInterface(final Object thiz, final Class<T> clazz) {
        if (thiz == null) {
            throw new IllegalArgumentException(getMessage("thiz.cannot.be.null", new String[0]));
        }
        return (T)this.getInterfaceInner(thiz, (Class<Object>)clazz);
    }
    
    private static Source makeSource(final Reader reader, final ScriptContext ctxt) throws ScriptException {
        try {
            return Source.sourceFor(getScriptName(ctxt), reader);
        }
        catch (IOException e) {
            throw new ScriptException(e);
        }
    }
    
    private static Source makeSource(final String src, final ScriptContext ctxt) {
        return Source.sourceFor(getScriptName(ctxt), src);
    }
    
    private static String getScriptName(final ScriptContext ctxt) {
        final Object val = ctxt.getAttribute("javax.script.filename");
        return (val != null) ? val.toString() : "<eval>";
    }
    
    private <T> T getInterfaceInner(final Object thiz, final Class<T> clazz) {
        assert !(thiz instanceof ScriptObject) : "raw ScriptObject not expected here";
        if (clazz == null || !clazz.isInterface()) {
            throw new IllegalArgumentException(getMessage("interface.class.expected", new String[0]));
        }
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            if (!Modifier.isPublic(clazz.getModifiers())) {
                throw new SecurityException(getMessage("implementing.non.public.interface", clazz.getName()));
            }
            Context.checkPackageAccess(clazz);
        }
        ScriptObject realSelf = null;
        Global realGlobal = null;
        if (thiz == null) {
            realGlobal = (Global)(realSelf = this.getNashornGlobalFrom(this.context));
        }
        else if (thiz instanceof ScriptObjectMirror) {
            final ScriptObjectMirror mirror = (ScriptObjectMirror)thiz;
            realSelf = mirror.getScriptObject();
            realGlobal = mirror.getHomeGlobal();
            if (!isOfContext(realGlobal, this.nashornContext)) {
                throw new IllegalArgumentException(getMessage("script.object.from.another.engine", new String[0]));
            }
        }
        if (realSelf == null) {
            throw new IllegalArgumentException(getMessage("interface.on.non.script.object", new String[0]));
        }
        try {
            final Global oldGlobal = Context.getGlobal();
            final boolean globalChanged = oldGlobal != realGlobal;
            try {
                if (globalChanged) {
                    Context.setGlobal(realGlobal);
                }
                if (!isInterfaceImplemented(clazz, realSelf)) {
                    return null;
                }
                return clazz.cast(JavaAdapterFactory.getConstructor(realSelf.getClass(), clazz, MethodHandles.publicLookup()).invoke(realSelf));
            }
            finally {
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
            }
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
    
    private Global getNashornGlobalFrom(final ScriptContext ctxt) {
        if (this._global_per_engine) {
            return this.global;
        }
        final Bindings bindings = ctxt.getBindings(100);
        if (bindings instanceof ScriptObjectMirror) {
            final Global glob = this.globalFromMirror((ScriptObjectMirror)bindings);
            if (glob != null) {
                return glob;
            }
        }
        final Object scope = bindings.get("nashorn.global");
        if (scope instanceof ScriptObjectMirror) {
            final Global glob2 = this.globalFromMirror((ScriptObjectMirror)scope);
            if (glob2 != null) {
                return glob2;
            }
        }
        final ScriptObjectMirror mirror = this.createGlobalMirror();
        bindings.put("nashorn.global", (Object)mirror);
        mirror.getHomeGlobal().setInitScriptContext(ctxt);
        return mirror.getHomeGlobal();
    }
    
    private Global globalFromMirror(final ScriptObjectMirror mirror) {
        final ScriptObject sobj = mirror.getScriptObject();
        if (sobj instanceof Global && isOfContext((Global)sobj, this.nashornContext)) {
            return (Global)sobj;
        }
        return null;
    }
    
    private ScriptObjectMirror createGlobalMirror() {
        final Global newGlobal = this.createNashornGlobal();
        return new ScriptObjectMirror(newGlobal, newGlobal);
    }
    
    private Global createNashornGlobal() {
        final Global newGlobal = AccessController.doPrivileged((PrivilegedAction<Global>)new PrivilegedAction<Global>() {
            @Override
            public Global run() {
                try {
                    return NashornScriptEngine.this.nashornContext.newGlobal();
                }
                catch (RuntimeException e) {
                    if (Context.DEBUG) {
                        e.printStackTrace();
                    }
                    throw e;
                }
            }
        }, NashornScriptEngine.CREATE_GLOBAL_ACC_CTXT);
        this.nashornContext.initGlobal(newGlobal, this);
        return newGlobal;
    }
    
    private Object invokeImpl(final Object selfObject, final String name, final Object... args) throws ScriptException, NoSuchMethodException {
        Objects.requireNonNull(name);
        assert !(selfObject instanceof ScriptObject) : "raw ScriptObject not expected here";
        Global invokeGlobal = null;
        ScriptObjectMirror selfMirror = null;
        if (selfObject instanceof ScriptObjectMirror) {
            selfMirror = (ScriptObjectMirror)selfObject;
            if (!isOfContext(selfMirror.getHomeGlobal(), this.nashornContext)) {
                throw new IllegalArgumentException(getMessage("script.object.from.another.engine", new String[0]));
            }
            invokeGlobal = selfMirror.getHomeGlobal();
        }
        else if (selfObject == null) {
            final Global ctxtGlobal = invokeGlobal = this.getNashornGlobalFrom(this.context);
            selfMirror = (ScriptObjectMirror)ScriptObjectMirror.wrap(ctxtGlobal, ctxtGlobal);
        }
        if (selfMirror != null) {
            try {
                return ScriptObjectMirror.translateUndefined(selfMirror.callMember(name, args));
            }
            catch (Exception e) {
                final Throwable cause = e.getCause();
                if (cause instanceof NoSuchMethodException) {
                    throw (NoSuchMethodException)cause;
                }
                throwAsScriptException(e, invokeGlobal);
                throw new AssertionError((Object)"should not reach here");
            }
        }
        throw new IllegalArgumentException(getMessage("interface.on.non.script.object", new String[0]));
    }
    
    private Object evalImpl(final Source src, final ScriptContext ctxt) throws ScriptException {
        return this.evalImpl(this.compileImpl(src, ctxt), ctxt);
    }
    
    private Object evalImpl(final ScriptFunction script, final ScriptContext ctxt) throws ScriptException {
        return this.evalImpl(script, ctxt, this.getNashornGlobalFrom(ctxt));
    }
    
    private Object evalImpl(final Context.MultiGlobalCompiledScript mgcs, final ScriptContext ctxt, final Global ctxtGlobal) throws ScriptException {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != ctxtGlobal;
        try {
            if (globalChanged) {
                Context.setGlobal(ctxtGlobal);
            }
            final ScriptFunction script = mgcs.getFunction(ctxtGlobal);
            final ScriptContext oldCtxt = ctxtGlobal.getScriptContext();
            ctxtGlobal.setScriptContext(ctxt);
            try {
                return ScriptObjectMirror.translateUndefined(ScriptObjectMirror.wrap(ScriptRuntime.apply(script, ctxtGlobal, new Object[0]), ctxtGlobal));
            }
            finally {
                ctxtGlobal.setScriptContext(oldCtxt);
            }
        }
        catch (Exception e) {
            throwAsScriptException(e, ctxtGlobal);
            throw new AssertionError((Object)"should not reach here");
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }
    
    private Object evalImpl(final ScriptFunction script, final ScriptContext ctxt, final Global ctxtGlobal) throws ScriptException {
        if (script == null) {
            return null;
        }
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != ctxtGlobal;
        try {
            if (globalChanged) {
                Context.setGlobal(ctxtGlobal);
            }
            final ScriptContext oldCtxt = ctxtGlobal.getScriptContext();
            ctxtGlobal.setScriptContext(ctxt);
            try {
                return ScriptObjectMirror.translateUndefined(ScriptObjectMirror.wrap(ScriptRuntime.apply(script, ctxtGlobal, new Object[0]), ctxtGlobal));
            }
            finally {
                ctxtGlobal.setScriptContext(oldCtxt);
            }
        }
        catch (Exception e) {
            throwAsScriptException(e, ctxtGlobal);
            throw new AssertionError((Object)"should not reach here");
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }
    
    private static void throwAsScriptException(final Exception e, final Global global) throws ScriptException {
        if (e instanceof ScriptException) {
            throw (ScriptException)e;
        }
        if (e instanceof NashornException) {
            final NashornException ne = (NashornException)e;
            final ScriptException se = new ScriptException(ne.getMessage(), ne.getFileName(), ne.getLineNumber(), ne.getColumnNumber());
            ne.initEcmaError(global);
            se.initCause(e);
            throw se;
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException)e;
        }
        throw new ScriptException(e);
    }
    
    private CompiledScript asCompiledScript(final Source source) throws ScriptException {
        final Global oldGlobal = Context.getGlobal();
        final Global newGlobal = this.getNashornGlobalFrom(this.context);
        final boolean globalChanged = oldGlobal != newGlobal;
        Context.MultiGlobalCompiledScript mgcs;
        ScriptFunction func;
        try {
            if (globalChanged) {
                Context.setGlobal(newGlobal);
            }
            mgcs = this.nashornContext.compileScript(source);
            func = mgcs.getFunction(newGlobal);
        }
        catch (Exception e) {
            throwAsScriptException(e, newGlobal);
            throw new AssertionError((Object)"should not reach here");
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return new CompiledScript() {
            @Override
            public Object eval(final ScriptContext ctxt) throws ScriptException {
                final Global globalObject = NashornScriptEngine.this.getNashornGlobalFrom(ctxt);
                if (func.getScope() == globalObject) {
                    return NashornScriptEngine.this.evalImpl(func, ctxt, globalObject);
                }
                return NashornScriptEngine.this.evalImpl(mgcs, ctxt, globalObject);
            }
            
            @Override
            public ScriptEngine getEngine() {
                return NashornScriptEngine.this;
            }
        };
    }
    
    private ScriptFunction compileImpl(final Source source, final ScriptContext ctxt) throws ScriptException {
        return this.compileImpl(source, this.getNashornGlobalFrom(ctxt));
    }
    
    private ScriptFunction compileImpl(final Source source, final Global newGlobal) throws ScriptException {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != newGlobal;
        try {
            if (globalChanged) {
                Context.setGlobal(newGlobal);
            }
            return this.nashornContext.compileScript(source, newGlobal);
        }
        catch (Exception e) {
            throwAsScriptException(e, newGlobal);
            throw new AssertionError((Object)"should not reach here");
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }
    
    private static boolean isInterfaceImplemented(final Class<?> iface, final ScriptObject sobj) {
        for (final Method method : iface.getMethods()) {
            if (method.getDeclaringClass() != Object.class) {
                if (Modifier.isAbstract(method.getModifiers())) {
                    final Object obj = sobj.get(method.getName());
                    if (!(obj instanceof ScriptFunction)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private static boolean isOfContext(final Global global, final Context context) {
        return global.isOfContext(context);
    }
    
    static {
        CREATE_CONTEXT_ACC_CTXT = createPermAccCtxt("nashorn.createContext");
        CREATE_GLOBAL_ACC_CTXT = createPermAccCtxt("nashorn.createGlobal");
        MESSAGES_BUNDLE = ResourceBundle.getBundle("jdk.nashorn.api.scripting.resources.Messages", Locale.getDefault());
    }
}
