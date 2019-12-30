// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.ref.SoftReference;
import java.lang.ref.ReferenceQueue;
import java.util.LinkedHashMap;
import java.security.PrivilegedActionException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import jdk.nashorn.internal.runtime.logging.Logger;
import java.util.function.Supplier;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import java.util.function.Consumer;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.nashorn.internal.ir.FunctionNode;
import java.security.CodeSigner;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.events.RuntimeEvent;
import java.util.logging.Level;
import jdk.nashorn.internal.codegen.Compiler;
import javax.script.ScriptEngine;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.internal.org.objectweb.asm.util.CheckClassAdapter;
import jdk.internal.org.objectweb.asm.ClassReader;
import java.lang.reflect.Modifier;
import java.util.Objects;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import java.net.MalformedURLException;
import java.io.File;
import java.security.AccessController;
import java.net.URL;
import java.io.IOException;
import java.security.PrivilegedAction;
import jdk.nashorn.internal.codegen.CompilerConstants;
import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import jdk.nashorn.internal.runtime.options.Options;
import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.Permission;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import java.security.AccessControlContext;
import jdk.nashorn.api.scripting.ClassFilter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import jdk.nashorn.internal.objects.Global;
import java.lang.invoke.SwitchPoint;
import java.util.Map;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;

public final class Context
{
    public static final String NASHORN_SET_CONFIG = "nashorn.setConfig";
    public static final String NASHORN_CREATE_CONTEXT = "nashorn.createContext";
    public static final String NASHORN_CREATE_GLOBAL = "nashorn.createGlobal";
    public static final String NASHORN_GET_CONTEXT = "nashorn.getContext";
    public static final String NASHORN_JAVA_REFLECTION = "nashorn.JavaReflection";
    public static final String NASHORN_DEBUG_MODE = "nashorn.debugMode";
    private static final String LOAD_CLASSPATH = "classpath:";
    private static final String LOAD_FX = "fx:";
    private static final String LOAD_NASHORN = "nashorn:";
    private static MethodHandles.Lookup LOOKUP;
    private static MethodType CREATE_PROGRAM_FUNCTION_TYPE;
    private final FieldMode fieldMode;
    private final Map<String, SwitchPoint> builtinSwitchPoints;
    public static final boolean DEBUG;
    private static final ThreadLocal<Global> currentGlobal;
    private ClassCache classCache;
    private CodeStore codeStore;
    private final AtomicReference<GlobalConstants> globalConstantsRef;
    private final ScriptEnvironment env;
    final boolean _strict;
    private final ClassLoader appLoader;
    private final ScriptLoader scriptLoader;
    private final ErrorManager errors;
    private final AtomicLong uniqueScriptId;
    private final ClassFilter classFilter;
    private static final ClassLoader myLoader;
    private static final StructureLoader theStructLoader;
    private static final AccessControlContext NO_PERMISSIONS_ACC_CTXT;
    private static final AccessControlContext CREATE_LOADER_ACC_CTXT;
    private static final AccessControlContext CREATE_GLOBAL_ACC_CTXT;
    private final Map<String, DebugLogger> loggers;
    
    public static Global getGlobal() {
        return Context.currentGlobal.get();
    }
    
    public static void setGlobal(final ScriptObject global) {
        if (global != null && !(global instanceof Global)) {
            throw new IllegalArgumentException("not a global!");
        }
        setGlobal((Global)global);
    }
    
    public static void setGlobal(final Global global) {
        assert getGlobal() != global;
        if (global != null) {
            final GlobalConstants globalConstants = getContext(global).getGlobalConstants();
            if (globalConstants != null) {
                globalConstants.invalidateAll();
            }
        }
        Context.currentGlobal.set(global);
    }
    
    public static Context getContext() {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("nashorn.getContext"));
        }
        return getContextTrusted();
    }
    
    public static PrintWriter getCurrentErr() {
        final ScriptObject global = getGlobal();
        return (global != null) ? global.getContext().getErr() : new PrintWriter(System.err);
    }
    
    public static void err(final String str) {
        err(str, true);
    }
    
    public static void err(final String str, final boolean crlf) {
        final PrintWriter err = getCurrentErr();
        if (err != null) {
            if (crlf) {
                err.println(str);
            }
            else {
                err.print(str);
            }
        }
    }
    
    ClassLoader getAppLoader() {
        return this.appLoader;
    }
    
    ClassLoader getStructLoader() {
        return Context.theStructLoader;
    }
    
    private static AccessControlContext createNoPermAccCtxt() {
        return new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, new Permissions()) });
    }
    
    private static AccessControlContext createPermAccCtxt(final String permName) {
        final Permissions perms = new Permissions();
        perms.add(new RuntimePermission(permName));
        return new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, perms) });
    }
    
    public Context(final Options options, final ErrorManager errors, final ClassLoader appLoader) {
        this(options, errors, appLoader, null);
    }
    
    public Context(final Options options, final ErrorManager errors, final ClassLoader appLoader, final ClassFilter classFilter) {
        this(options, errors, new PrintWriter(System.out, true), new PrintWriter(System.err, true), appLoader, classFilter);
    }
    
    public Context(final Options options, final ErrorManager errors, final PrintWriter out, final PrintWriter err, final ClassLoader appLoader) {
        this(options, errors, out, err, appLoader, null);
    }
    
    public Context(final Options options, final ErrorManager errors, final PrintWriter out, final PrintWriter err, final ClassLoader appLoader, final ClassFilter classFilter) {
        this.builtinSwitchPoints = new HashMap<String, SwitchPoint>();
        this.globalConstantsRef = new AtomicReference<GlobalConstants>();
        this.loggers = new HashMap<String, DebugLogger>();
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("nashorn.createContext"));
        }
        this.classFilter = classFilter;
        this.env = new ScriptEnvironment(options, out, err);
        this._strict = this.env._strict;
        if (this.env._loader_per_compile) {
            this.scriptLoader = null;
            this.uniqueScriptId = null;
        }
        else {
            this.scriptLoader = this.createNewLoader();
            this.uniqueScriptId = new AtomicLong();
        }
        this.errors = errors;
        final String classPath = options.getString("classpath");
        if (!this.env._compile_only && classPath != null && !classPath.isEmpty()) {
            if (sm != null) {
                sm.checkCreateClassLoader();
            }
            this.appLoader = NashornLoader.createClassLoader(classPath, appLoader);
        }
        else {
            this.appLoader = appLoader;
        }
        final int cacheSize = this.env._class_cache_size;
        if (cacheSize > 0) {
            this.classCache = new ClassCache(cacheSize);
        }
        if (this.env._persistent_cache) {
            this.codeStore = CodeStore.newCodeStore(this);
        }
        if (this.env._version) {
            this.getErr().println("nashorn " + Version.version());
        }
        if (this.env._fullversion) {
            this.getErr().println("nashorn full version " + Version.fullVersion());
        }
        if (Options.getBooleanProperty("nashorn.fields.dual")) {
            this.fieldMode = FieldMode.DUAL;
        }
        else if (Options.getBooleanProperty("nashorn.fields.objects")) {
            this.fieldMode = FieldMode.OBJECTS;
        }
        else {
            this.fieldMode = FieldMode.AUTO;
        }
        this.initLoggers();
    }
    
    public ClassFilter getClassFilter() {
        return this.classFilter;
    }
    
    GlobalConstants getGlobalConstants() {
        return this.globalConstantsRef.get();
    }
    
    public ErrorManager getErrorManager() {
        return this.errors;
    }
    
    public ScriptEnvironment getEnv() {
        return this.env;
    }
    
    public PrintWriter getOut() {
        return this.env.getOut();
    }
    
    public PrintWriter getErr() {
        return this.env.getErr();
    }
    
    public boolean useDualFields() {
        return this.fieldMode == FieldMode.DUAL || (this.fieldMode == FieldMode.AUTO && this.env._optimistic_types);
    }
    
    public static PropertyMap getGlobalMap() {
        return getGlobal().getMap();
    }
    
    public ScriptFunction compileScript(final Source source, final ScriptObject scope) {
        return this.compileScript(source, scope, this.errors);
    }
    
    public MultiGlobalCompiledScript compileScript(final Source source) {
        final Class<?> clazz = this.compile(source, this.errors, this._strict);
        final MethodHandle createProgramFunctionHandle = getCreateProgramFunctionHandle(clazz);
        return new MultiGlobalCompiledScript() {
            @Override
            public ScriptFunction getFunction(final Global newGlobal) {
                return invokeCreateProgramFunctionHandle(createProgramFunctionHandle, newGlobal);
            }
        };
    }
    
    public Object eval(final ScriptObject initialScope, final String string, final Object callThis, final Object location) {
        return this.eval(initialScope, string, callThis, location, false, false);
    }
    
    public Object eval(final ScriptObject initialScope, final String string, final Object callThis, final Object location, final boolean strict, final boolean evalCall) {
        final String file = (location == ScriptRuntime.UNDEFINED || location == null) ? "<eval>" : location.toString();
        final Source source = Source.sourceFor(file, string, evalCall);
        final boolean directEval = evalCall && location != ScriptRuntime.UNDEFINED;
        final Global global = getGlobal();
        ScriptObject scope = initialScope;
        boolean strictFlag = strict || this._strict;
        Class<?> clazz = null;
        try {
            clazz = this.compile(source, new ThrowErrorManager(), strictFlag);
        }
        catch (ParserException e) {
            e.throwAsEcmaException(global);
            return null;
        }
        if (!strictFlag) {
            try {
                strictFlag = clazz.getField(CompilerConstants.STRICT_MODE.symbolName()).getBoolean(null);
            }
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex2) {
                final Exception ex;
                final Exception e2 = ex;
                strictFlag = false;
            }
        }
        if (strictFlag) {
            scope = newScope(scope);
        }
        final ScriptFunction func = getProgramFunction(clazz, scope);
        Object evalThis;
        if (directEval) {
            evalThis = (((callThis != ScriptRuntime.UNDEFINED && callThis != null) || strictFlag) ? callThis : global);
        }
        else {
            evalThis = callThis;
        }
        return ScriptRuntime.apply(func, evalThis, new Object[0]);
    }
    
    private static ScriptObject newScope(final ScriptObject callerScope) {
        return new Scope(callerScope, PropertyMap.newMap(Scope.class));
    }
    
    private static Source loadInternal(final String srcStr, final String prefix, final String resourcePath) {
        if (srcStr.startsWith(prefix)) {
            final String resource = resourcePath + srcStr.substring(prefix.length());
            return AccessController.doPrivileged((PrivilegedAction<Source>)new PrivilegedAction<Source>() {
                @Override
                public Source run() {
                    try {
                        final URL resURL = Context.class.getResource(resource);
                        return (resURL != null) ? Source.sourceFor(srcStr, resURL) : null;
                    }
                    catch (IOException exp) {
                        return null;
                    }
                }
            });
        }
        return null;
    }
    
    public Object load(final Object scope, final Object from) throws IOException {
        final Object src = (from instanceof ConsString) ? from.toString() : from;
        Source source = null;
        if (src instanceof String) {
            final String srcStr = (String)src;
            if (srcStr.startsWith("classpath:")) {
                final URL url = this.getResourceURL(srcStr.substring("classpath:".length()));
                source = ((url != null) ? Source.sourceFor(url.toString(), url) : null);
            }
            else {
                final File file = new File(srcStr);
                if (srcStr.indexOf(58) != -1) {
                    if ((source = loadInternal(srcStr, "nashorn:", "resources/")) == null && (source = loadInternal(srcStr, "fx:", "resources/fx/")) == null) {
                        URL url2;
                        try {
                            url2 = new URL(srcStr);
                        }
                        catch (MalformedURLException e) {
                            url2 = file.toURI().toURL();
                        }
                        source = Source.sourceFor(url2.toString(), url2);
                    }
                }
                else if (file.isFile()) {
                    source = Source.sourceFor(srcStr, file);
                }
            }
        }
        else if (src instanceof File && ((File)src).isFile()) {
            final File file2 = (File)src;
            source = Source.sourceFor(file2.getName(), file2);
        }
        else if (src instanceof URL) {
            final URL url3 = (URL)src;
            source = Source.sourceFor(url3.toString(), url3);
        }
        else if (src instanceof ScriptObject) {
            final ScriptObject sobj = (ScriptObject)src;
            if (sobj.has("script") && sobj.has("name")) {
                final String script = JSType.toString(sobj.get("script"));
                final String name = JSType.toString(sobj.get("name"));
                source = Source.sourceFor(name, script);
            }
        }
        else if (src instanceof Map) {
            final Map<?, ?> map = (Map<?, ?>)src;
            if (map.containsKey("script") && map.containsKey("name")) {
                final String script = JSType.toString(map.get("script"));
                final String name = JSType.toString(map.get("name"));
                source = Source.sourceFor(name, script);
            }
        }
        if (source == null) {
            throw ECMAErrors.typeError("cant.load.script", ScriptRuntime.safeToString(from));
        }
        if (scope instanceof ScriptObject && ((ScriptObject)scope).isScope()) {
            final ScriptObject sobj = (ScriptObject)scope;
            assert sobj.isGlobal() : "non-Global scope object!!";
            return this.evaluateSource(source, sobj, sobj);
        }
        else {
            if (scope == null || scope == ScriptRuntime.UNDEFINED) {
                final Global global = getGlobal();
                return this.evaluateSource(source, global, global);
            }
            final Global global = getGlobal();
            final ScriptObject evalScope = newScope(global);
            final ScriptObject withObj = ScriptRuntime.openWith(evalScope, scope);
            return this.evaluateSource(source, withObj, global);
        }
    }
    
    public Object loadWithNewGlobal(final Object from, final Object... args) throws IOException {
        final Global oldGlobal = getGlobal();
        final Global newGlobal = AccessController.doPrivileged((PrivilegedAction<Global>)new PrivilegedAction<Global>() {
            @Override
            public Global run() {
                try {
                    return Context.this.newGlobal();
                }
                catch (RuntimeException e) {
                    if (Context.DEBUG) {
                        e.printStackTrace();
                    }
                    throw e;
                }
            }
        }, Context.CREATE_GLOBAL_ACC_CTXT);
        this.initGlobal(newGlobal);
        setGlobal(newGlobal);
        final Object[] wrapped = (args == null) ? ScriptRuntime.EMPTY_ARRAY : ScriptObjectMirror.wrapArray(args, oldGlobal);
        newGlobal.put("arguments", newGlobal.wrapAsObject(wrapped), this.env._strict);
        try {
            return ScriptObjectMirror.unwrap(ScriptObjectMirror.wrap(this.load(newGlobal, from), newGlobal), oldGlobal);
        }
        finally {
            setGlobal(oldGlobal);
        }
    }
    
    public static Class<? extends ScriptObject> forStructureClass(final String fullName) throws ClassNotFoundException {
        if (System.getSecurityManager() != null && !StructureLoader.isStructureClass(fullName)) {
            throw new ClassNotFoundException(fullName);
        }
        return (Class<? extends ScriptObject>)Class.forName(fullName, true, Context.theStructLoader);
    }
    
    public static void checkPackageAccess(final Class<?> clazz) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            Class<?> bottomClazz;
            for (bottomClazz = clazz; bottomClazz.isArray(); bottomClazz = bottomClazz.getComponentType()) {}
            checkPackageAccess(sm, bottomClazz.getName());
        }
    }
    
    public static void checkPackageAccess(final String pkgName) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            checkPackageAccess(sm, pkgName.endsWith(".") ? pkgName : (pkgName + "."));
        }
    }
    
    private static void checkPackageAccess(final SecurityManager sm, final String fullName) {
        Objects.requireNonNull(sm);
        final int index = fullName.lastIndexOf(46);
        if (index != -1) {
            final String pkgName = fullName.substring(0, index);
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Void>() {
                @Override
                public Void run() {
                    sm.checkPackageAccess(pkgName);
                    return null;
                }
            }, Context.NO_PERMISSIONS_ACC_CTXT);
        }
    }
    
    public static boolean isStructureClass(final String className) {
        return StructureLoader.isStructureClass(className);
    }
    
    private static boolean isAccessiblePackage(final Class<?> clazz) {
        try {
            checkPackageAccess(clazz);
            return true;
        }
        catch (SecurityException se) {
            return false;
        }
    }
    
    public static boolean isAccessibleClass(final Class<?> clazz) {
        return Modifier.isPublic(clazz.getModifiers()) && isAccessiblePackage(clazz);
    }
    
    public Class<?> findClass(final String fullName) throws ClassNotFoundException {
        if (fullName.indexOf(91) != -1 || fullName.indexOf(47) != -1) {
            throw new ClassNotFoundException(fullName);
        }
        if (this.classFilter != null && !this.classFilter.exposeToScripts(fullName)) {
            throw new ClassNotFoundException(fullName);
        }
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            checkPackageAccess(sm, fullName);
        }
        if (this.appLoader != null) {
            return Class.forName(fullName, true, this.appLoader);
        }
        final Class<?> cl = Class.forName(fullName);
        if (cl.getClassLoader() == null) {
            return cl;
        }
        throw new ClassNotFoundException(fullName);
    }
    
    public static void printStackTrace(final Throwable t) {
        if (Context.DEBUG) {
            t.printStackTrace(getCurrentErr());
        }
    }
    
    public void verify(final byte[] bytecode) {
        if (this.env._verify_code && System.getSecurityManager() == null) {
            CheckClassAdapter.verify(new ClassReader(bytecode), Context.theStructLoader, false, new PrintWriter(System.err, true));
        }
    }
    
    public Global createGlobal() {
        return this.initGlobal(this.newGlobal());
    }
    
    public Global newGlobal() {
        this.createOrInvalidateGlobalConstants();
        return new Global(this);
    }
    
    private void createOrInvalidateGlobalConstants() {
        while (true) {
            final GlobalConstants currentGlobalConstants = this.getGlobalConstants();
            if (currentGlobalConstants != null) {
                currentGlobalConstants.invalidateForever();
                return;
            }
            final GlobalConstants newGlobalConstants = new GlobalConstants(this.getLogger(GlobalConstants.class));
            if (this.globalConstantsRef.compareAndSet(null, newGlobalConstants)) {
                return;
            }
        }
    }
    
    public Global initGlobal(final Global global, final ScriptEngine engine) {
        if (!this.env._compile_only) {
            final Global oldGlobal = getGlobal();
            try {
                setGlobal(global);
                global.initBuiltinObjects(engine);
            }
            finally {
                setGlobal(oldGlobal);
            }
        }
        return global;
    }
    
    public Global initGlobal(final Global global) {
        return this.initGlobal(global, null);
    }
    
    static Context getContextTrusted() {
        return getContext(getGlobal());
    }
    
    static Context getContextTrustedOrNull() {
        final Global global = getGlobal();
        return (global == null) ? null : getContext(global);
    }
    
    private static Context getContext(final Global global) {
        return global.getContext();
    }
    
    static Context fromClass(final Class<?> clazz) {
        final ClassLoader loader = clazz.getClassLoader();
        if (loader instanceof ScriptLoader) {
            return ((ScriptLoader)loader).getContext();
        }
        return getContextTrusted();
    }
    
    private URL getResourceURL(final String resName) {
        if (this.appLoader != null) {
            return this.appLoader.getResource(resName);
        }
        return ClassLoader.getSystemResource(resName);
    }
    
    private Object evaluateSource(final Source source, final ScriptObject scope, final ScriptObject thiz) {
        ScriptFunction script = null;
        try {
            script = this.compileScript(source, scope, new ThrowErrorManager());
        }
        catch (ParserException e) {
            e.throwAsEcmaException();
        }
        return ScriptRuntime.apply(script, thiz, new Object[0]);
    }
    
    private static ScriptFunction getProgramFunction(final Class<?> script, final ScriptObject scope) {
        if (script == null) {
            return null;
        }
        return invokeCreateProgramFunctionHandle(getCreateProgramFunctionHandle(script), scope);
    }
    
    private static MethodHandle getCreateProgramFunctionHandle(final Class<?> script) {
        try {
            return Context.LOOKUP.findStatic(script, CompilerConstants.CREATE_PROGRAM_FUNCTION.symbolName(), Context.CREATE_PROGRAM_FUNCTION_TYPE);
        }
        catch (NoSuchMethodException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            throw new AssertionError("Failed to retrieve a handle for the program function for " + script.getName(), e);
        }
    }
    
    private static ScriptFunction invokeCreateProgramFunctionHandle(final MethodHandle createProgramFunctionHandle, final ScriptObject scope) {
        try {
            return createProgramFunctionHandle.invokeExact(scope);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable e = t2;
            throw e;
        }
        catch (Throwable t) {
            throw new AssertionError("Failed to create a program function", t);
        }
    }
    
    private ScriptFunction compileScript(final Source source, final ScriptObject scope, final ErrorManager errMan) {
        return getProgramFunction(this.compile(source, errMan, this._strict), scope);
    }
    
    private synchronized Class<?> compile(final Source source, final ErrorManager errMan, final boolean strict) {
        errMan.reset();
        Class<?> script = this.findCachedClass(source);
        if (script != null) {
            final DebugLogger log = this.getLogger(Compiler.class);
            if (log.isEnabled()) {
                log.fine(new RuntimeEvent<Object>(Level.INFO, source), "Code cache hit for ", source, " avoiding recompile.");
            }
            return script;
        }
        StoredScript storedScript = null;
        FunctionNode functionNode = null;
        final boolean useCodeStore = this.codeStore != null && !this.env._parse_only && (!this.env._optimistic_types || this.env._lazy_compilation);
        final String cacheKey = useCodeStore ? CodeStore.getCacheKey("script", null) : null;
        if (useCodeStore) {
            storedScript = this.codeStore.load(source, cacheKey);
        }
        if (storedScript == null) {
            if (this.env._dest_dir != null) {
                source.dump(this.env._dest_dir);
            }
            functionNode = new Parser(this.env, source, errMan, strict, this.getLogger(Parser.class)).parse();
            if (errMan.hasErrors()) {
                return null;
            }
            if (this.env._print_ast || functionNode.getFlag(524288)) {
                this.getErr().println(new ASTWriter(functionNode));
            }
            if (this.env._print_parse || functionNode.getFlag(131072)) {
                this.getErr().println(new PrintVisitor(functionNode, true, false));
            }
        }
        if (this.env._parse_only) {
            return null;
        }
        final URL url = source.getURL();
        final ScriptLoader loader = this.env._loader_per_compile ? this.createNewLoader() : this.scriptLoader;
        final CodeSource cs = new CodeSource(url, (CodeSigner[])null);
        final CodeInstaller installer = new ContextCodeInstaller(this, loader, cs);
        if (storedScript == null) {
            final Compiler.CompilationPhases phases = Compiler.CompilationPhases.COMPILE_ALL;
            final Compiler compiler = Compiler.forInitialCompilation(installer, source, errMan, strict | functionNode.isStrict());
            final FunctionNode compiledFunction = compiler.compile(functionNode, phases);
            if (errMan.hasErrors()) {
                return null;
            }
            script = compiledFunction.getRootClass();
            compiler.persistClassInfo(cacheKey, compiledFunction);
        }
        else {
            Compiler.updateCompilationId(storedScript.getCompilationId());
            script = storedScript.installScript(source, installer);
        }
        this.cacheClass(source, script);
        return script;
    }
    
    private ScriptLoader createNewLoader() {
        return AccessController.doPrivileged((PrivilegedAction<ScriptLoader>)new PrivilegedAction<ScriptLoader>() {
            @Override
            public ScriptLoader run() {
                return new ScriptLoader(Context.this);
            }
        }, Context.CREATE_LOADER_ACC_CTXT);
    }
    
    private long getUniqueScriptId() {
        return this.uniqueScriptId.getAndIncrement();
    }
    
    private Class<?> findCachedClass(final Source source) {
        final ClassReference ref = (this.classCache == null) ? null : this.classCache.get(source);
        return (ref != null) ? ref.get() : null;
    }
    
    private void cacheClass(final Source source, final Class<?> clazz) {
        if (this.classCache != null) {
            this.classCache.cache(source, clazz);
        }
    }
    
    private void initLoggers() {
        ((Loggable)MethodHandleFactory.getFunctionality()).initLogger(this);
    }
    
    public DebugLogger getLogger(final Class<? extends Loggable> clazz) {
        return this.getLogger(clazz, null);
    }
    
    public DebugLogger getLogger(final Class<? extends Loggable> clazz, final Consumer<DebugLogger> initHook) {
        final String name = getLoggerName(clazz);
        DebugLogger logger = this.loggers.get(name);
        if (logger == null) {
            if (!this.env.hasLogger(name)) {
                return DebugLogger.DISABLED_LOGGER;
            }
            final LoggingOption.LoggerInfo info = this.env._loggers.get(name);
            logger = new DebugLogger(name, info.getLevel(), info.isQuiet());
            if (initHook != null) {
                initHook.accept(logger);
            }
            this.loggers.put(name, logger);
        }
        return logger;
    }
    
    public MethodHandle addLoggingToHandle(final Class<? extends Loggable> clazz, final MethodHandle mh, final Supplier<String> text) {
        return this.addLoggingToHandle(clazz, Level.INFO, mh, Integer.MAX_VALUE, false, text);
    }
    
    public MethodHandle addLoggingToHandle(final Class<? extends Loggable> clazz, final Level level, final MethodHandle mh, final int paramStart, final boolean printReturnValue, final Supplier<String> text) {
        final DebugLogger log = this.getLogger(clazz);
        if (log.isEnabled()) {
            return MethodHandleFactory.addDebugPrintout(log, level, mh, paramStart, printReturnValue, text.get());
        }
        return mh;
    }
    
    private static String getLoggerName(final Class<?> clazz) {
        Class<?> current = clazz;
        while (current != null) {
            final Logger log = current.getAnnotation(Logger.class);
            if (log != null) {
                assert !"".equals(log.name());
                return log.name();
            }
            else {
                current = current.getSuperclass();
            }
        }
        assert false;
        return null;
    }
    
    public SwitchPoint newBuiltinSwitchPoint(final String name) {
        assert this.builtinSwitchPoints.get(name) == null;
        final SwitchPoint sp = new BuiltinSwitchPoint();
        this.builtinSwitchPoints.put(name, sp);
        return sp;
    }
    
    public SwitchPoint getBuiltinSwitchPoint(final String name) {
        return this.builtinSwitchPoints.get(name);
    }
    
    static {
        Context.LOOKUP = MethodHandles.lookup();
        Context.CREATE_PROGRAM_FUNCTION_TYPE = MethodType.methodType(ScriptFunction.class, ScriptObject.class);
        DebuggerSupport.FORCELOAD = true;
        DEBUG = Options.getBooleanProperty("nashorn.debug");
        currentGlobal = new ThreadLocal<Global>();
        myLoader = Context.class.getClassLoader();
        NO_PERMISSIONS_ACC_CTXT = createNoPermAccCtxt();
        CREATE_LOADER_ACC_CTXT = createPermAccCtxt("createClassLoader");
        CREATE_GLOBAL_ACC_CTXT = createPermAccCtxt("nashorn.createGlobal");
        theStructLoader = AccessController.doPrivileged((PrivilegedAction<StructureLoader>)new PrivilegedAction<StructureLoader>() {
            @Override
            public StructureLoader run() {
                return new StructureLoader(Context.myLoader);
            }
        }, Context.CREATE_LOADER_ACC_CTXT);
    }
    
    private enum FieldMode
    {
        AUTO, 
        OBJECTS, 
        DUAL;
    }
    
    public static class ContextCodeInstaller implements CodeInstaller
    {
        private final Context context;
        private final ScriptLoader loader;
        private final CodeSource codeSource;
        private int usageCount;
        private int bytesDefined;
        private static final int MAX_USAGES = 10;
        private static final int MAX_BYTES_DEFINED = 200000;
        
        private ContextCodeInstaller(final Context context, final ScriptLoader loader, final CodeSource codeSource) {
            this.usageCount = 0;
            this.bytesDefined = 0;
            this.context = context;
            this.loader = loader;
            this.codeSource = codeSource;
        }
        
        @Override
        public Context getContext() {
            return this.context;
        }
        
        @Override
        public Class<?> install(final String className, final byte[] bytecode) {
            ++this.usageCount;
            this.bytesDefined += bytecode.length;
            final String binaryName = Compiler.binaryName(className);
            return this.loader.installClass(binaryName, bytecode, this.codeSource);
        }
        
        @Override
        public void initialize(final Collection<Class<?>> classes, final Source source, final Object[] constants) {
            try {
                AccessController.doPrivileged((PrivilegedExceptionAction<Object>)new PrivilegedExceptionAction<Void>() {
                    @Override
                    public Void run() throws Exception {
                        for (final Class<?> clazz : classes) {
                            final Field sourceField = clazz.getDeclaredField(CompilerConstants.SOURCE.symbolName());
                            sourceField.setAccessible(true);
                            sourceField.set(null, source);
                            final Field constantsField = clazz.getDeclaredField(CompilerConstants.CONSTANTS.symbolName());
                            constantsField.setAccessible(true);
                            constantsField.set(null, constants);
                        }
                        return null;
                    }
                });
            }
            catch (PrivilegedActionException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public void verify(final byte[] code) {
            this.context.verify(code);
        }
        
        @Override
        public long getUniqueScriptId() {
            return this.context.getUniqueScriptId();
        }
        
        @Override
        public void storeScript(final String cacheKey, final Source source, final String mainClassName, final Map<String, byte[]> classBytes, final Map<Integer, FunctionInitializer> initializers, final Object[] constants, final int compilationId) {
            if (this.context.codeStore != null) {
                this.context.codeStore.store(cacheKey, source, mainClassName, classBytes, initializers, constants, compilationId);
            }
        }
        
        @Override
        public StoredScript loadScript(final Source source, final String functionKey) {
            if (this.context.codeStore != null) {
                return this.context.codeStore.load(source, functionKey);
            }
            return null;
        }
        
        @Override
        public CodeInstaller withNewLoader() {
            if (this.usageCount < 10 && this.bytesDefined < 200000) {
                return this;
            }
            return new ContextCodeInstaller(this.context, this.context.createNewLoader(), this.codeSource);
        }
        
        @Override
        public boolean isCompatibleWith(final CodeInstaller other) {
            if (other instanceof ContextCodeInstaller) {
                final ContextCodeInstaller cci = (ContextCodeInstaller)other;
                return cci.context == this.context && cci.codeSource == this.codeSource;
            }
            return false;
        }
    }
    
    public static class ThrowErrorManager extends ErrorManager
    {
        @Override
        public void error(final String message) {
            throw new ParserException(message);
        }
        
        @Override
        public void error(final ParserException e) {
            throw e;
        }
    }
    
    private static class ClassCache extends LinkedHashMap<Source, ClassReference>
    {
        private final int size;
        private final ReferenceQueue<Class<?>> queue;
        
        ClassCache(final int size) {
            super(size, 0.75f, true);
            this.size = size;
            this.queue = new ReferenceQueue<Class<?>>();
        }
        
        void cache(final Source source, final Class<?> clazz) {
            this.put(source, new ClassReference(clazz, this.queue, source));
        }
        
        @Override
        protected boolean removeEldestEntry(final Map.Entry<Source, ClassReference> eldest) {
            return this.size() > this.size;
        }
        
        @Override
        public ClassReference get(final Object key) {
            ClassReference ref;
            while ((ref = (ClassReference)this.queue.poll()) != null) {
                this.remove(ref.source);
            }
            return super.get(key);
        }
    }
    
    private static class ClassReference extends SoftReference<Class<?>>
    {
        private final Source source;
        
        ClassReference(final Class<?> clazz, final ReferenceQueue<Class<?>> queue, final Source source) {
            super(clazz, queue);
            this.source = source;
        }
    }
    
    public static final class BuiltinSwitchPoint extends SwitchPoint
    {
    }
    
    public interface MultiGlobalCompiledScript
    {
        ScriptFunction getFunction(final Global p0);
    }
}
