// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;
import java.util.ResourceBundle;

public final class LogSF extends LogXF
{
    private static final String FQCN;
    
    private LogSF() {
    }
    
    private static String format(final String pattern, final Object[] arguments) {
        if (pattern != null) {
            String retval = "";
            int count = 0;
            int prev = 0;
            for (int pos = pattern.indexOf("{"); pos >= 0; pos = pattern.indexOf("{", prev)) {
                if (pos == 0 || pattern.charAt(pos - 1) != '\\') {
                    retval += pattern.substring(prev, pos);
                    if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '}') {
                        if (arguments != null && count < arguments.length) {
                            retval += arguments[count++];
                        }
                        else {
                            retval += "{}";
                        }
                        prev = pos + 2;
                    }
                    else {
                        retval += "{";
                        prev = pos + 1;
                    }
                }
                else {
                    retval = retval + pattern.substring(prev, pos - 1) + "{";
                    prev = pos + 1;
                }
            }
            return retval + pattern.substring(prev);
        }
        return null;
    }
    
    private static String format(final String pattern, final Object arg0) {
        if (pattern != null) {
            if (pattern.indexOf("\\{") >= 0) {
                return format(pattern, new Object[] { arg0 });
            }
            final int pos = pattern.indexOf("{}");
            if (pos >= 0) {
                return pattern.substring(0, pos) + arg0 + pattern.substring(pos + 2);
            }
        }
        return pattern;
    }
    
    private static String format(final String resourceBundleName, final String key, final Object[] arguments) {
        String pattern;
        if (resourceBundleName != null) {
            try {
                final ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName);
                pattern = bundle.getString(key);
            }
            catch (Exception ex) {
                pattern = key;
            }
        }
        else {
            pattern = key;
        }
        return format(pattern, arguments);
    }
    
    private static void forcedLog(final Logger logger, final Level level, final String msg) {
        logger.callAppenders(new LoggingEvent(LogSF.FQCN, logger, level, msg, null));
    }
    
    private static void forcedLog(final Logger logger, final Level level, final String msg, final Throwable t) {
        logger.callAppenders(new LoggingEvent(LogSF.FQCN, logger, level, msg, t));
    }
    
    public static void trace(final Logger logger, final String pattern, final Object[] arguments) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, arguments));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final Object[] arguments) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, arguments));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final Object[] arguments) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, arguments));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final Object[] arguments) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, arguments));
        }
    }
    
    public static void error(final Logger logger, final String pattern, final Object[] arguments) {
        if (logger.isEnabledFor(Level.ERROR)) {
            forcedLog(logger, Level.ERROR, format(pattern, arguments));
        }
    }
    
    public static void fatal(final Logger logger, final String pattern, final Object[] arguments) {
        if (logger.isEnabledFor(Level.FATAL)) {
            forcedLog(logger, Level.FATAL, format(pattern, arguments));
        }
    }
    
    public static void trace(final Logger logger, final Throwable t, final String pattern, final Object[] arguments) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, arguments), t);
        }
    }
    
    public static void debug(final Logger logger, final Throwable t, final String pattern, final Object[] arguments) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, arguments), t);
        }
    }
    
    public static void info(final Logger logger, final Throwable t, final String pattern, final Object[] arguments) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, arguments), t);
        }
    }
    
    public static void warn(final Logger logger, final Throwable t, final String pattern, final Object[] arguments) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, arguments), t);
        }
    }
    
    public static void error(final Logger logger, final Throwable t, final String pattern, final Object[] arguments) {
        if (logger.isEnabledFor(Level.ERROR)) {
            forcedLog(logger, Level.ERROR, format(pattern, arguments), t);
        }
    }
    
    public static void fatal(final Logger logger, final Throwable t, final String pattern, final Object[] arguments) {
        if (logger.isEnabledFor(Level.FATAL)) {
            forcedLog(logger, Level.FATAL, format(pattern, arguments), t);
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final boolean argument) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final char argument) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final byte argument) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final short argument) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final int argument) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final long argument) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final float argument) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final double argument) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final Object argument) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, argument));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final Object arg0, final Object arg1) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.toArray(arg0, arg1)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final Object arg0, final Object arg1, final Object arg2) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.toArray(arg0, arg1, arg2)));
        }
    }
    
    public static void trace(final Logger logger, final String pattern, final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
        if (logger.isEnabledFor(LogSF.TRACE)) {
            forcedLog(logger, LogSF.TRACE, format(pattern, LogXF.toArray(arg0, arg1, arg2, arg3)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final boolean argument) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final char argument) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final byte argument) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final short argument) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final int argument) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final long argument) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final float argument) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final double argument) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final Object argument) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, argument));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final Object arg0, final Object arg1) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.toArray(arg0, arg1)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final Object arg0, final Object arg1, final Object arg2) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.toArray(arg0, arg1, arg2)));
        }
    }
    
    public static void debug(final Logger logger, final String pattern, final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
        if (logger.isDebugEnabled()) {
            forcedLog(logger, Level.DEBUG, format(pattern, LogXF.toArray(arg0, arg1, arg2, arg3)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final boolean argument) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final char argument) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final byte argument) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final short argument) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final int argument) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final long argument) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final float argument) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final double argument) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final Object argument) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, argument));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final Object arg0, final Object arg1) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.toArray(arg0, arg1)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final Object arg0, final Object arg1, final Object arg2) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.toArray(arg0, arg1, arg2)));
        }
    }
    
    public static void info(final Logger logger, final String pattern, final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
        if (logger.isInfoEnabled()) {
            forcedLog(logger, Level.INFO, format(pattern, LogXF.toArray(arg0, arg1, arg2, arg3)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final boolean argument) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final char argument) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final byte argument) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final short argument) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final int argument) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final long argument) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final float argument) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final double argument) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.valueOf(argument)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final Object argument) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, argument));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final Object arg0, final Object arg1) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.toArray(arg0, arg1)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final Object arg0, final Object arg1, final Object arg2) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.toArray(arg0, arg1, arg2)));
        }
    }
    
    public static void warn(final Logger logger, final String pattern, final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
        if (logger.isEnabledFor(Level.WARN)) {
            forcedLog(logger, Level.WARN, format(pattern, LogXF.toArray(arg0, arg1, arg2, arg3)));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final Object[] parameters) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, parameters));
        }
    }
    
    public static void log(final Logger logger, final Level level, final Throwable t, final String pattern, final Object[] parameters) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, parameters), t);
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final Object param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(param1)));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final boolean param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final byte param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final char param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final short param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final int param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final long param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final float param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final double param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final Object arg0, final Object arg1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(arg0, arg1)));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final Object arg0, final Object arg1, final Object arg2) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(arg0, arg1, arg2)));
        }
    }
    
    public static void log(final Logger logger, final Level level, final String pattern, final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(pattern, LogXF.toArray(arg0, arg1, arg2, arg3)));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final Object[] parameters) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, parameters));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final Throwable t, final String bundleName, final String key, final Object[] parameters) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, parameters), t);
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final Object param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(param1)));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final boolean param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final char param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final byte param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final short param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final int param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final long param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final float param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final double param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(LogXF.valueOf(param1))));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final Object param0, final Object param1) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(param0, param1)));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final Object param0, final Object param1, final Object param2) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(param0, param1, param2)));
        }
    }
    
    public static void logrb(final Logger logger, final Level level, final String bundleName, final String key, final Object param0, final Object param1, final Object param2, final Object param3) {
        if (logger.isEnabledFor(level)) {
            forcedLog(logger, level, format(bundleName, key, LogXF.toArray(param0, param1, param2, param3)));
        }
    }
    
    static {
        FQCN = LogSF.class.getName();
    }
}
