// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;

public abstract class LogXF
{
    protected static final Level TRACE;
    private static final String FQCN;
    
    protected LogXF() {
    }
    
    protected static Boolean valueOf(final boolean b) {
        if (b) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    protected static Character valueOf(final char c) {
        return new Character(c);
    }
    
    protected static Byte valueOf(final byte b) {
        return new Byte(b);
    }
    
    protected static Short valueOf(final short b) {
        return new Short(b);
    }
    
    protected static Integer valueOf(final int b) {
        return new Integer(b);
    }
    
    protected static Long valueOf(final long b) {
        return new Long(b);
    }
    
    protected static Float valueOf(final float b) {
        return new Float(b);
    }
    
    protected static Double valueOf(final double b) {
        return new Double(b);
    }
    
    protected static Object[] toArray(final Object param1) {
        return new Object[] { param1 };
    }
    
    protected static Object[] toArray(final Object param1, final Object param2) {
        return new Object[] { param1, param2 };
    }
    
    protected static Object[] toArray(final Object param1, final Object param2, final Object param3) {
        return new Object[] { param1, param2, param3 };
    }
    
    protected static Object[] toArray(final Object param1, final Object param2, final Object param3, final Object param4) {
        return new Object[] { param1, param2, param3, param4 };
    }
    
    public static void entering(final Logger logger, final String sourceClass, final String sourceMethod) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(LogXF.FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " ENTRY", null));
        }
    }
    
    public static void entering(final Logger logger, final String sourceClass, final String sourceMethod, final String param) {
        if (logger.isDebugEnabled()) {
            final String msg = sourceClass + "." + sourceMethod + " ENTRY " + param;
            logger.callAppenders(new LoggingEvent(LogXF.FQCN, logger, Level.DEBUG, msg, null));
        }
    }
    
    public static void entering(final Logger logger, final String sourceClass, final String sourceMethod, final Object param) {
        if (logger.isDebugEnabled()) {
            String msg = sourceClass + "." + sourceMethod + " ENTRY ";
            if (param == null) {
                msg += "null";
            }
            else {
                try {
                    msg += param;
                }
                catch (Throwable ex) {
                    msg += "?";
                }
            }
            logger.callAppenders(new LoggingEvent(LogXF.FQCN, logger, Level.DEBUG, msg, null));
        }
    }
    
    public static void entering(final Logger logger, final String sourceClass, final String sourceMethod, final Object[] params) {
        if (logger.isDebugEnabled()) {
            String msg = sourceClass + "." + sourceMethod + " ENTRY ";
            if (params != null && params.length > 0) {
                String delim = "{";
                for (int i = 0; i < params.length; ++i) {
                    try {
                        msg = msg + delim + params[i];
                    }
                    catch (Throwable ex) {
                        msg = msg + delim + "?";
                    }
                    delim = ",";
                }
                msg += "}";
            }
            else {
                msg += "{}";
            }
            logger.callAppenders(new LoggingEvent(LogXF.FQCN, logger, Level.DEBUG, msg, null));
        }
    }
    
    public static void exiting(final Logger logger, final String sourceClass, final String sourceMethod) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(LogXF.FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " RETURN", null));
        }
    }
    
    public static void exiting(final Logger logger, final String sourceClass, final String sourceMethod, final String result) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(LogXF.FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " RETURN " + result, null));
        }
    }
    
    public static void exiting(final Logger logger, final String sourceClass, final String sourceMethod, final Object result) {
        if (logger.isDebugEnabled()) {
            String msg = sourceClass + "." + sourceMethod + " RETURN ";
            if (result == null) {
                msg += "null";
            }
            else {
                try {
                    msg += result;
                }
                catch (Throwable ex) {
                    msg += "?";
                }
            }
            logger.callAppenders(new LoggingEvent(LogXF.FQCN, logger, Level.DEBUG, msg, null));
        }
    }
    
    public static void throwing(final Logger logger, final String sourceClass, final String sourceMethod, final Throwable thrown) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(LogXF.FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " THROW", thrown));
        }
    }
    
    static {
        TRACE = new Level(5000, "TRACE", 7);
        FQCN = LogXF.class.getName();
    }
}
