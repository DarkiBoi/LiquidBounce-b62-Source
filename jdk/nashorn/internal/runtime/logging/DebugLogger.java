// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.logging;

import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Permission;
import java.util.logging.LoggingPermission;
import java.security.Permissions;
import java.security.AccessControlContext;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.events.RuntimeEvent;
import jdk.nashorn.internal.runtime.Context;
import java.io.PrintWriter;
import java.security.AccessController;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Formatter;
import java.util.logging.ConsoleHandler;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DebugLogger
{
    public static final DebugLogger DISABLED_LOGGER;
    private final Logger logger;
    private final boolean isEnabled;
    private int indent;
    private static final int INDENT_SPACE = 4;
    private final boolean isQuiet;
    
    public DebugLogger(final String loggerName, final Level loggerLevel, final boolean isQuiet) {
        this.logger = instantiateLogger(loggerName, loggerLevel);
        this.isQuiet = isQuiet;
        assert this.logger != null;
        this.isEnabled = (this.getLevel() != Level.OFF);
    }
    
    private static Logger instantiateLogger(final String name, final Level level) {
        final Logger logger = Logger.getLogger(name);
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                for (final Handler h : logger.getHandlers()) {
                    logger.removeHandler(h);
                }
                logger.setLevel(level);
                logger.setUseParentHandlers(false);
                final Handler c = new ConsoleHandler();
                c.setFormatter(new Formatter() {
                    @Override
                    public String format(final LogRecord record) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append('[').append(record.getLoggerName()).append("] ").append(record.getMessage()).append('\n');
                        return sb.toString();
                    }
                });
                logger.addHandler(c);
                c.setLevel(level);
                return null;
            }
        }, createLoggerControlAccCtxt());
        return logger;
    }
    
    public Level getLevel() {
        return (this.logger.getLevel() == null) ? Level.OFF : this.logger.getLevel();
    }
    
    public PrintWriter getOutputStream() {
        return Context.getCurrentErr();
    }
    
    public static String quote(final String str) {
        if (str.isEmpty()) {
            return "''";
        }
        char startQuote = '\0';
        char endQuote = '\0';
        char quote = '\0';
        if (str.startsWith("\\") || str.startsWith("\"")) {
            startQuote = str.charAt(0);
        }
        if (str.endsWith("\\") || str.endsWith("\"")) {
            endQuote = str.charAt(str.length() - 1);
        }
        if (startQuote == '\0' || endQuote == '\0') {
            quote = ((startQuote == '\0') ? endQuote : startQuote);
        }
        if (quote == '\0') {
            quote = '\'';
        }
        return ((startQuote == '\0') ? quote : startQuote) + str + ((endQuote == '\0') ? quote : endQuote);
    }
    
    public boolean isEnabled() {
        return this.isEnabled;
    }
    
    public static boolean isEnabled(final DebugLogger logger) {
        return logger != null && logger.isEnabled();
    }
    
    public void indent(final int pos) {
        if (this.isEnabled) {
            this.indent += pos * 4;
        }
    }
    
    public void indent() {
        this.indent += 4;
    }
    
    public void unindent() {
        this.indent -= 4;
        if (this.indent < 0) {
            this.indent = 0;
        }
    }
    
    private static void logEvent(final RuntimeEvent<?> event) {
        if (event != null) {
            final Global global = Context.getGlobal();
            if (global.has("Debug")) {
                final ScriptObject debug = (ScriptObject)global.get("Debug");
                final ScriptFunction addRuntimeEvent = (ScriptFunction)debug.get("addRuntimeEvent");
                ScriptRuntime.apply(addRuntimeEvent, debug, event);
            }
        }
    }
    
    public boolean isLoggable(final Level level) {
        return this.logger.isLoggable(level);
    }
    
    public void finest(final String str) {
        this.log(Level.FINEST, str);
    }
    
    public void finest(final RuntimeEvent<?> event, final String str) {
        this.finest(str);
        logEvent(event);
    }
    
    public void finest(final Object... objs) {
        this.log(Level.FINEST, objs);
    }
    
    public void finest(final RuntimeEvent<?> event, final Object... objs) {
        this.finest(objs);
        logEvent(event);
    }
    
    public void finer(final String str) {
        this.log(Level.FINER, str);
    }
    
    public void finer(final RuntimeEvent<?> event, final String str) {
        this.finer(str);
        logEvent(event);
    }
    
    public void finer(final Object... objs) {
        this.log(Level.FINER, objs);
    }
    
    public void finer(final RuntimeEvent<?> event, final Object... objs) {
        this.finer(objs);
        logEvent(event);
    }
    
    public void fine(final String str) {
        this.log(Level.FINE, str);
    }
    
    public void fine(final RuntimeEvent<?> event, final String str) {
        this.fine(str);
        logEvent(event);
    }
    
    public void fine(final Object... objs) {
        this.log(Level.FINE, objs);
    }
    
    public void fine(final RuntimeEvent<?> event, final Object... objs) {
        this.fine(objs);
        logEvent(event);
    }
    
    public void config(final String str) {
        this.log(Level.CONFIG, str);
    }
    
    public void config(final RuntimeEvent<?> event, final String str) {
        this.config(str);
        logEvent(event);
    }
    
    public void config(final Object... objs) {
        this.log(Level.CONFIG, objs);
    }
    
    public void config(final RuntimeEvent<?> event, final Object... objs) {
        this.config(objs);
        logEvent(event);
    }
    
    public void info(final String str) {
        this.log(Level.INFO, str);
    }
    
    public void info(final RuntimeEvent<?> event, final String str) {
        this.info(str);
        logEvent(event);
    }
    
    public void info(final Object... objs) {
        this.log(Level.INFO, objs);
    }
    
    public void info(final RuntimeEvent<?> event, final Object... objs) {
        this.info(objs);
        logEvent(event);
    }
    
    public void warning(final String str) {
        this.log(Level.WARNING, str);
    }
    
    public void warning(final RuntimeEvent<?> event, final String str) {
        this.warning(str);
        logEvent(event);
    }
    
    public void warning(final Object... objs) {
        this.log(Level.WARNING, objs);
    }
    
    public void warning(final RuntimeEvent<?> event, final Object... objs) {
        this.warning(objs);
        logEvent(event);
    }
    
    public void severe(final String str) {
        this.log(Level.SEVERE, str);
    }
    
    public void severe(final RuntimeEvent<?> event, final String str) {
        this.severe(str);
        logEvent(event);
    }
    
    public void severe(final Object... objs) {
        this.log(Level.SEVERE, objs);
    }
    
    public void severe(final RuntimeEvent<?> event, final Object... objs) {
        this.severe(objs);
        logEvent(event);
    }
    
    public void log(final Level level, final String str) {
        if (this.isEnabled && !this.isQuiet && this.logger.isLoggable(level)) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.indent; ++i) {
                sb.append(' ');
            }
            sb.append(str);
            this.logger.log(level, sb.toString());
        }
    }
    
    public void log(final Level level, final Object... objs) {
        if (this.isEnabled && !this.isQuiet && this.logger.isLoggable(level)) {
            final StringBuilder sb = new StringBuilder();
            for (final Object obj : objs) {
                sb.append(obj);
            }
            this.log(level, sb.toString());
        }
    }
    
    private static AccessControlContext createLoggerControlAccCtxt() {
        final Permissions perms = new Permissions();
        perms.add(new LoggingPermission("control", null));
        return new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, perms) });
    }
    
    static {
        DISABLED_LOGGER = new DebugLogger("disabled", Level.OFF, false);
    }
}
