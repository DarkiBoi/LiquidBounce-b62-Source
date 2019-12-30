// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import org.apache.log4j.spi.LoggerFactory;

public class Logger extends Category
{
    private static final String FQCN;
    
    protected Logger(final String name) {
        super(name);
    }
    
    public static Logger getLogger(final String name) {
        return LogManager.getLogger(name);
    }
    
    public static Logger getLogger(final Class clazz) {
        return LogManager.getLogger(clazz.getName());
    }
    
    public static Logger getRootLogger() {
        return LogManager.getRootLogger();
    }
    
    public static Logger getLogger(final String name, final LoggerFactory factory) {
        return LogManager.getLogger(name, factory);
    }
    
    public void trace(final Object message) {
        if (this.repository.isDisabled(5000)) {
            return;
        }
        if (Level.TRACE.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Logger.FQCN, Level.TRACE, message, null);
        }
    }
    
    public void trace(final Object message, final Throwable t) {
        if (this.repository.isDisabled(5000)) {
            return;
        }
        if (Level.TRACE.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Logger.FQCN, Level.TRACE, message, t);
        }
    }
    
    public boolean isTraceEnabled() {
        return !this.repository.isDisabled(5000) && Level.TRACE.isGreaterOrEqual(this.getEffectiveLevel());
    }
    
    static {
        FQCN = Logger.class.getName();
    }
}
