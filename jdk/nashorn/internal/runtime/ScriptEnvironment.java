// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.List;
import jdk.nashorn.internal.runtime.options.Option;
import java.util.logging.Level;
import java.util.HashMap;
import jdk.nashorn.internal.runtime.options.KeyValueOption;
import java.util.StringTokenizer;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import java.util.Map;
import java.util.Locale;
import java.util.TimeZone;
import jdk.nashorn.internal.runtime.options.Options;
import jdk.nashorn.internal.codegen.Namespace;
import java.io.PrintWriter;

public final class ScriptEnvironment
{
    private static final boolean ALLOW_EAGER_COMPILATION_SILENT_OVERRIDE;
    private final PrintWriter out;
    private final PrintWriter err;
    private final Namespace namespace;
    private final Options options;
    public final int _class_cache_size;
    public final boolean _compile_only;
    public final boolean _const_as_var;
    public final int _callsite_flags;
    public final boolean _debug_lines;
    public final String _dest_dir;
    public final boolean _dump_on_error;
    public final boolean _early_lvalue_error;
    public final boolean _empty_statements;
    public final boolean _fullversion;
    public final boolean _fx;
    public final boolean _global_per_engine;
    public final boolean _es6;
    public static final String COMPILE_ONLY_OPTIMISTIC_ARG = "optimistic";
    public final FunctionStatementBehavior _function_statement;
    public final boolean _lazy_compilation;
    public final boolean _optimistic_types;
    public final boolean _loader_per_compile;
    public final boolean _no_java;
    public final boolean _no_syntax_extensions;
    public final boolean _no_typed_arrays;
    public final boolean _parse_only;
    public final boolean _persistent_cache;
    public final boolean _print_ast;
    public final boolean _print_lower_ast;
    public final boolean _print_code;
    public final String _print_code_dir;
    public final String _print_code_func;
    public final boolean _print_mem_usage;
    public final boolean _print_no_newline;
    public final boolean _print_parse;
    public final boolean _print_lower_parse;
    public final boolean _print_symbols;
    public final boolean _scripting;
    public final boolean _strict;
    public final boolean _version;
    public final boolean _verify_code;
    public final TimeZone _timezone;
    public final Locale _locale;
    public final Map<String, LoggingOption.LoggerInfo> _loggers;
    public final Timing _timing;
    
    public ScriptEnvironment(final Options options, final PrintWriter out, final PrintWriter err) {
        this.out = out;
        this.err = err;
        this.namespace = new Namespace();
        this.options = options;
        this._class_cache_size = options.getInteger("class.cache.size");
        this._compile_only = options.getBoolean("compile.only");
        this._const_as_var = options.getBoolean("const.as.var");
        this._debug_lines = options.getBoolean("debug.lines");
        this._dest_dir = options.getString("d");
        this._dump_on_error = options.getBoolean("doe");
        this._early_lvalue_error = options.getBoolean("early.lvalue.error");
        this._empty_statements = options.getBoolean("empty.statements");
        this._fullversion = options.getBoolean("fullversion");
        if (options.getBoolean("function.statement.error")) {
            this._function_statement = FunctionStatementBehavior.ERROR;
        }
        else if (options.getBoolean("function.statement.warning")) {
            this._function_statement = FunctionStatementBehavior.WARNING;
        }
        else {
            this._function_statement = FunctionStatementBehavior.ACCEPT;
        }
        this._fx = options.getBoolean("fx");
        this._global_per_engine = options.getBoolean("global.per.engine");
        this._optimistic_types = options.getBoolean("optimistic.types");
        final boolean lazy_compilation = options.getBoolean("lazy.compilation");
        if (!lazy_compilation && this._optimistic_types) {
            if (!ScriptEnvironment.ALLOW_EAGER_COMPILATION_SILENT_OVERRIDE) {
                throw new IllegalStateException(ECMAErrors.getMessage("config.error.eagerCompilationConflictsWithOptimisticTypes", options.getOptionTemplateByKey("lazy.compilation").getName(), options.getOptionTemplateByKey("optimistic.types").getName()));
            }
            this._lazy_compilation = true;
        }
        else {
            this._lazy_compilation = lazy_compilation;
        }
        this._loader_per_compile = options.getBoolean("loader.per.compile");
        this._no_java = options.getBoolean("no.java");
        this._no_syntax_extensions = options.getBoolean("no.syntax.extensions");
        this._no_typed_arrays = options.getBoolean("no.typed.arrays");
        this._parse_only = options.getBoolean("parse.only");
        this._persistent_cache = options.getBoolean("persistent.code.cache");
        this._print_ast = options.getBoolean("print.ast");
        this._print_lower_ast = options.getBoolean("print.lower.ast");
        this._print_code = (options.getString("print.code") != null);
        this._print_mem_usage = options.getBoolean("print.mem.usage");
        this._print_no_newline = options.getBoolean("print.no.newline");
        this._print_parse = options.getBoolean("print.parse");
        this._print_lower_parse = options.getBoolean("print.lower.parse");
        this._print_symbols = options.getBoolean("print.symbols");
        this._scripting = options.getBoolean("scripting");
        this._strict = options.getBoolean("strict");
        this._version = options.getBoolean("version");
        this._verify_code = options.getBoolean("verify.code");
        final String language = options.getString("language");
        if (language == null || language.equals("es5")) {
            this._es6 = false;
        }
        else {
            if (!language.equals("es6")) {
                throw new RuntimeException("Unsupported language: " + language);
            }
            this._es6 = true;
        }
        String dir = null;
        String func = null;
        final String pc = options.getString("print.code");
        if (pc != null) {
            final StringTokenizer st = new StringTokenizer(pc, ",");
            while (st.hasMoreTokens()) {
                final StringTokenizer st2 = new StringTokenizer(st.nextToken(), ":");
                while (st2.hasMoreTokens()) {
                    final String cmd = st2.nextToken();
                    if ("dir".equals(cmd)) {
                        dir = st2.nextToken();
                    }
                    else {
                        if (!"function".equals(cmd)) {
                            continue;
                        }
                        func = st2.nextToken();
                    }
                }
            }
        }
        this._print_code_dir = dir;
        this._print_code_func = func;
        int callSiteFlags = 0;
        if (options.getBoolean("profile.callsites")) {
            callSiteFlags |= 0x40;
        }
        if (options.get("trace.callsites") instanceof KeyValueOption) {
            callSiteFlags |= 0x80;
            final KeyValueOption kv = (KeyValueOption)options.get("trace.callsites");
            if (kv.hasValue("miss")) {
                callSiteFlags |= 0x100;
            }
            if (kv.hasValue("enterexit") || (callSiteFlags & 0x100) == 0x0) {
                callSiteFlags |= 0x200;
            }
            if (kv.hasValue("objects")) {
                callSiteFlags |= 0x400;
            }
        }
        this._callsite_flags = callSiteFlags;
        final Option<?> timezoneOption = options.get("timezone");
        if (timezoneOption != null) {
            this._timezone = (TimeZone)timezoneOption.getValue();
        }
        else {
            this._timezone = TimeZone.getDefault();
        }
        final Option<?> localeOption = options.get("locale");
        if (localeOption != null) {
            this._locale = (Locale)localeOption.getValue();
        }
        else {
            this._locale = Locale.getDefault();
        }
        final LoggingOption loggingOption = (LoggingOption)options.get("log");
        this._loggers = ((loggingOption == null) ? new HashMap<String, LoggingOption.LoggerInfo>() : loggingOption.getLoggers());
        final LoggingOption.LoggerInfo timeLoggerInfo = this._loggers.get(Timing.getLoggerName());
        this._timing = new Timing(timeLoggerInfo != null && timeLoggerInfo.getLevel() != Level.OFF);
    }
    
    public PrintWriter getOut() {
        return this.out;
    }
    
    public PrintWriter getErr() {
        return this.err;
    }
    
    public Namespace getNamespace() {
        return this.namespace;
    }
    
    public List<String> getFiles() {
        return this.options.getFiles();
    }
    
    public List<String> getArguments() {
        return this.options.getArguments();
    }
    
    public boolean hasLogger(final String name) {
        return this._loggers.get(name) != null;
    }
    
    public boolean isTimingEnabled() {
        return this._timing != null && this._timing.isEnabled();
    }
    
    static {
        ALLOW_EAGER_COMPILATION_SILENT_OVERRIDE = Options.getBooleanProperty("nashorn.options.allowEagerCompilationSilentOverride", false);
    }
    
    public enum FunctionStatementBehavior
    {
        ACCEPT, 
        WARNING, 
        ERROR;
    }
}
