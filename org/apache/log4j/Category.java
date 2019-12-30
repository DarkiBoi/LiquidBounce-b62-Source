// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import java.util.Vector;
import org.apache.log4j.spi.HierarchyEventListener;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import org.apache.log4j.helpers.NullEnumeration;
import java.util.Enumeration;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.LoggerRepository;
import java.util.ResourceBundle;
import org.apache.log4j.spi.AppenderAttachable;

public class Category implements AppenderAttachable
{
    protected String name;
    protected volatile Level level;
    protected volatile Category parent;
    private static final String FQCN;
    protected ResourceBundle resourceBundle;
    protected LoggerRepository repository;
    AppenderAttachableImpl aai;
    protected boolean additive;
    
    protected Category(final String name) {
        this.additive = true;
        this.name = name;
    }
    
    public synchronized void addAppender(final Appender newAppender) {
        if (this.aai == null) {
            this.aai = new AppenderAttachableImpl();
        }
        this.aai.addAppender(newAppender);
        this.repository.fireAddAppenderEvent(this, newAppender);
    }
    
    public void assertLog(final boolean assertion, final String msg) {
        if (!assertion) {
            this.error(msg);
        }
    }
    
    public void callAppenders(final LoggingEvent event) {
        int writes = 0;
        for (Category c = this; c != null; c = c.parent) {
            synchronized (c) {
                if (c.aai != null) {
                    writes += c.aai.appendLoopOnAppenders(event);
                }
                if (!c.additive) {
                    break;
                }
            }
        }
        if (writes == 0) {
            this.repository.emitNoAppenderWarning(this);
        }
    }
    
    synchronized void closeNestedAppenders() {
        final Enumeration enumeration = this.getAllAppenders();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                final Appender a = enumeration.nextElement();
                if (a instanceof AppenderAttachable) {
                    a.close();
                }
            }
        }
    }
    
    public void debug(final Object message) {
        if (this.repository.isDisabled(10000)) {
            return;
        }
        if (Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.DEBUG, message, null);
        }
    }
    
    public void debug(final Object message, final Throwable t) {
        if (this.repository.isDisabled(10000)) {
            return;
        }
        if (Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.DEBUG, message, t);
        }
    }
    
    public void error(final Object message) {
        if (this.repository.isDisabled(40000)) {
            return;
        }
        if (Level.ERROR.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.ERROR, message, null);
        }
    }
    
    public void error(final Object message, final Throwable t) {
        if (this.repository.isDisabled(40000)) {
            return;
        }
        if (Level.ERROR.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.ERROR, message, t);
        }
    }
    
    public static Logger exists(final String name) {
        return LogManager.exists(name);
    }
    
    public void fatal(final Object message) {
        if (this.repository.isDisabled(50000)) {
            return;
        }
        if (Level.FATAL.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.FATAL, message, null);
        }
    }
    
    public void fatal(final Object message, final Throwable t) {
        if (this.repository.isDisabled(50000)) {
            return;
        }
        if (Level.FATAL.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.FATAL, message, t);
        }
    }
    
    protected void forcedLog(final String fqcn, final Priority level, final Object message, final Throwable t) {
        this.callAppenders(new LoggingEvent(fqcn, this, level, message, t));
    }
    
    public boolean getAdditivity() {
        return this.additive;
    }
    
    public synchronized Enumeration getAllAppenders() {
        if (this.aai == null) {
            return NullEnumeration.getInstance();
        }
        return this.aai.getAllAppenders();
    }
    
    public synchronized Appender getAppender(final String name) {
        if (this.aai == null || name == null) {
            return null;
        }
        return this.aai.getAppender(name);
    }
    
    public Level getEffectiveLevel() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.level != null) {
                return c.level;
            }
        }
        return null;
    }
    
    public Priority getChainedPriority() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.level != null) {
                return c.level;
            }
        }
        return null;
    }
    
    public static Enumeration getCurrentCategories() {
        return LogManager.getCurrentLoggers();
    }
    
    public static LoggerRepository getDefaultHierarchy() {
        return LogManager.getLoggerRepository();
    }
    
    public LoggerRepository getHierarchy() {
        return this.repository;
    }
    
    public LoggerRepository getLoggerRepository() {
        return this.repository;
    }
    
    public static Category getInstance(final String name) {
        return LogManager.getLogger(name);
    }
    
    public static Category getInstance(final Class clazz) {
        return LogManager.getLogger(clazz);
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final Category getParent() {
        return this.parent;
    }
    
    public final Level getLevel() {
        return this.level;
    }
    
    public final Level getPriority() {
        return this.level;
    }
    
    public static final Category getRoot() {
        return LogManager.getRootLogger();
    }
    
    public ResourceBundle getResourceBundle() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.resourceBundle != null) {
                return c.resourceBundle;
            }
        }
        return null;
    }
    
    protected String getResourceBundleString(final String key) {
        final ResourceBundle rb = this.getResourceBundle();
        if (rb == null) {
            return null;
        }
        try {
            return rb.getString(key);
        }
        catch (MissingResourceException mre) {
            this.error("No resource is associated with key \"" + key + "\".");
            return null;
        }
    }
    
    public void info(final Object message) {
        if (this.repository.isDisabled(20000)) {
            return;
        }
        if (Level.INFO.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.INFO, message, null);
        }
    }
    
    public void info(final Object message, final Throwable t) {
        if (this.repository.isDisabled(20000)) {
            return;
        }
        if (Level.INFO.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.INFO, message, t);
        }
    }
    
    public boolean isAttached(final Appender appender) {
        return appender != null && this.aai != null && this.aai.isAttached(appender);
    }
    
    public boolean isDebugEnabled() {
        return !this.repository.isDisabled(10000) && Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel());
    }
    
    public boolean isEnabledFor(final Priority level) {
        return !this.repository.isDisabled(level.level) && level.isGreaterOrEqual(this.getEffectiveLevel());
    }
    
    public boolean isInfoEnabled() {
        return !this.repository.isDisabled(20000) && Level.INFO.isGreaterOrEqual(this.getEffectiveLevel());
    }
    
    public void l7dlog(final Priority priority, final String key, final Throwable t) {
        if (this.repository.isDisabled(priority.level)) {
            return;
        }
        if (priority.isGreaterOrEqual(this.getEffectiveLevel())) {
            String msg = this.getResourceBundleString(key);
            if (msg == null) {
                msg = key;
            }
            this.forcedLog(Category.FQCN, priority, msg, t);
        }
    }
    
    public void l7dlog(final Priority priority, final String key, final Object[] params, final Throwable t) {
        if (this.repository.isDisabled(priority.level)) {
            return;
        }
        if (priority.isGreaterOrEqual(this.getEffectiveLevel())) {
            final String pattern = this.getResourceBundleString(key);
            String msg;
            if (pattern == null) {
                msg = key;
            }
            else {
                msg = MessageFormat.format(pattern, params);
            }
            this.forcedLog(Category.FQCN, priority, msg, t);
        }
    }
    
    public void log(final Priority priority, final Object message, final Throwable t) {
        if (this.repository.isDisabled(priority.level)) {
            return;
        }
        if (priority.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, priority, message, t);
        }
    }
    
    public void log(final Priority priority, final Object message) {
        if (this.repository.isDisabled(priority.level)) {
            return;
        }
        if (priority.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, priority, message, null);
        }
    }
    
    public void log(final String callerFQCN, final Priority level, final Object message, final Throwable t) {
        if (this.repository.isDisabled(level.level)) {
            return;
        }
        if (level.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(callerFQCN, level, message, t);
        }
    }
    
    private void fireRemoveAppenderEvent(final Appender appender) {
        if (appender != null) {
            if (this.repository instanceof Hierarchy) {
                ((Hierarchy)this.repository).fireRemoveAppenderEvent(this, appender);
            }
            else if (this.repository instanceof HierarchyEventListener) {
                ((HierarchyEventListener)this.repository).removeAppenderEvent(this, appender);
            }
        }
    }
    
    public synchronized void removeAllAppenders() {
        if (this.aai != null) {
            final Vector appenders = new Vector();
            Enumeration iter = this.aai.getAllAppenders();
            while (iter != null && iter.hasMoreElements()) {
                appenders.add(iter.nextElement());
            }
            this.aai.removeAllAppenders();
            iter = appenders.elements();
            while (iter.hasMoreElements()) {
                this.fireRemoveAppenderEvent(iter.nextElement());
            }
            this.aai = null;
        }
    }
    
    public synchronized void removeAppender(final Appender appender) {
        if (appender == null || this.aai == null) {
            return;
        }
        final boolean wasAttached = this.aai.isAttached(appender);
        this.aai.removeAppender(appender);
        if (wasAttached) {
            this.fireRemoveAppenderEvent(appender);
        }
    }
    
    public synchronized void removeAppender(final String name) {
        if (name == null || this.aai == null) {
            return;
        }
        final Appender appender = this.aai.getAppender(name);
        this.aai.removeAppender(name);
        if (appender != null) {
            this.fireRemoveAppenderEvent(appender);
        }
    }
    
    public void setAdditivity(final boolean additive) {
        this.additive = additive;
    }
    
    final void setHierarchy(final LoggerRepository repository) {
        this.repository = repository;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    public void setPriority(final Priority priority) {
        this.level = (Level)priority;
    }
    
    public void setResourceBundle(final ResourceBundle bundle) {
        this.resourceBundle = bundle;
    }
    
    public static void shutdown() {
        LogManager.shutdown();
    }
    
    public void warn(final Object message) {
        if (this.repository.isDisabled(30000)) {
            return;
        }
        if (Level.WARN.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.WARN, message, null);
        }
    }
    
    public void warn(final Object message, final Throwable t) {
        if (this.repository.isDisabled(30000)) {
            return;
        }
        if (Level.WARN.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(Category.FQCN, Level.WARN, message, t);
        }
    }
    
    static {
        FQCN = Category.class.getName();
    }
}
