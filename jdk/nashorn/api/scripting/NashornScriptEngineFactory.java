// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import java.util.Arrays;
import java.security.Permission;
import java.util.Objects;
import jdk.nashorn.internal.runtime.Context;
import javax.script.ScriptEngine;
import jdk.nashorn.internal.runtime.Version;
import java.util.Collections;
import java.util.List;
import jdk.Exported;
import javax.script.ScriptEngineFactory;

@Exported
public final class NashornScriptEngineFactory implements ScriptEngineFactory
{
    private static final String[] DEFAULT_OPTIONS;
    private static final List<String> names;
    private static final List<String> mimeTypes;
    private static final List<String> extensions;
    
    @Override
    public String getEngineName() {
        return (String)this.getParameter("javax.script.engine");
    }
    
    @Override
    public String getEngineVersion() {
        return (String)this.getParameter("javax.script.engine_version");
    }
    
    @Override
    public List<String> getExtensions() {
        return Collections.unmodifiableList((List<? extends String>)NashornScriptEngineFactory.extensions);
    }
    
    @Override
    public String getLanguageName() {
        return (String)this.getParameter("javax.script.language");
    }
    
    @Override
    public String getLanguageVersion() {
        return (String)this.getParameter("javax.script.language_version");
    }
    
    @Override
    public String getMethodCallSyntax(final String obj, final String method, final String... args) {
        final StringBuilder sb = new StringBuilder().append(obj).append('.').append(method).append('(');
        final int len = args.length;
        if (len > 0) {
            sb.append(args[0]);
        }
        for (int i = 1; i < len; ++i) {
            sb.append(',').append(args[i]);
        }
        sb.append(')');
        return sb.toString();
    }
    
    @Override
    public List<String> getMimeTypes() {
        return Collections.unmodifiableList((List<? extends String>)NashornScriptEngineFactory.mimeTypes);
    }
    
    @Override
    public List<String> getNames() {
        return Collections.unmodifiableList((List<? extends String>)NashornScriptEngineFactory.names);
    }
    
    @Override
    public String getOutputStatement(final String toDisplay) {
        return "print(" + toDisplay + ")";
    }
    
    @Override
    public Object getParameter(final String key) {
        switch (key) {
            case "javax.script.name": {
                return "javascript";
            }
            case "javax.script.engine": {
                return "Oracle Nashorn";
            }
            case "javax.script.engine_version": {
                return Version.version();
            }
            case "javax.script.language": {
                return "ECMAScript";
            }
            case "javax.script.language_version": {
                return "ECMA - 262 Edition 5.1";
            }
            case "THREADING": {
                return null;
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public String getProgram(final String... statements) {
        final StringBuilder sb = new StringBuilder();
        for (final String statement : statements) {
            sb.append(statement).append(';');
        }
        return sb.toString();
    }
    
    @Override
    public ScriptEngine getScriptEngine() {
        try {
            return new NashornScriptEngine(this, NashornScriptEngineFactory.DEFAULT_OPTIONS, getAppClassLoader(), null);
        }
        catch (RuntimeException e) {
            if (Context.DEBUG) {
                e.printStackTrace();
            }
            throw e;
        }
    }
    
    public ScriptEngine getScriptEngine(final ClassLoader appLoader) {
        return this.newEngine(NashornScriptEngineFactory.DEFAULT_OPTIONS, appLoader, null);
    }
    
    public ScriptEngine getScriptEngine(final ClassFilter classFilter) {
        return this.newEngine(NashornScriptEngineFactory.DEFAULT_OPTIONS, getAppClassLoader(), Objects.requireNonNull(classFilter));
    }
    
    public ScriptEngine getScriptEngine(final String... args) {
        return this.newEngine(Objects.requireNonNull(args), getAppClassLoader(), null);
    }
    
    public ScriptEngine getScriptEngine(final String[] args, final ClassLoader appLoader) {
        return this.newEngine(Objects.requireNonNull(args), appLoader, null);
    }
    
    public ScriptEngine getScriptEngine(final String[] args, final ClassLoader appLoader, final ClassFilter classFilter) {
        return this.newEngine(Objects.requireNonNull(args), appLoader, Objects.requireNonNull(classFilter));
    }
    
    private ScriptEngine newEngine(final String[] args, final ClassLoader appLoader, final ClassFilter classFilter) {
        checkConfigPermission();
        try {
            return new NashornScriptEngine(this, args, appLoader, classFilter);
        }
        catch (RuntimeException e) {
            if (Context.DEBUG) {
                e.printStackTrace();
            }
            throw e;
        }
    }
    
    private static void checkConfigPermission() {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("nashorn.setConfig"));
        }
    }
    
    private static List<String> immutableList(final String... elements) {
        return Collections.unmodifiableList((List<? extends String>)Arrays.asList((T[])elements));
    }
    
    private static ClassLoader getAppClassLoader() {
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        return (ccl == null) ? NashornScriptEngineFactory.class.getClassLoader() : ccl;
    }
    
    static {
        DEFAULT_OPTIONS = new String[] { "-doe" };
        names = immutableList("nashorn", "Nashorn", "js", "JS", "JavaScript", "javascript", "ECMAScript", "ecmascript");
        mimeTypes = immutableList("application/javascript", "application/ecmascript", "text/javascript", "text/ecmascript");
        extensions = immutableList("js");
    }
}
