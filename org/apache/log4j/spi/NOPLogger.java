// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import java.util.ResourceBundle;
import org.apache.log4j.Priority;
import java.util.Vector;
import java.util.Enumeration;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public final class NOPLogger extends Logger
{
    public NOPLogger(final NOPLoggerRepository repo, final String name) {
        super(name);
        this.repository = repo;
        this.level = Level.OFF;
        this.parent = this;
    }
    
    public void addAppender(final Appender newAppender) {
    }
    
    public void assertLog(final boolean assertion, final String msg) {
    }
    
    public void callAppenders(final LoggingEvent event) {
    }
    
    void closeNestedAppenders() {
    }
    
    public void debug(final Object message) {
    }
    
    public void debug(final Object message, final Throwable t) {
    }
    
    public void error(final Object message) {
    }
    
    public void error(final Object message, final Throwable t) {
    }
    
    public void fatal(final Object message) {
    }
    
    public void fatal(final Object message, final Throwable t) {
    }
    
    public Enumeration getAllAppenders() {
        return new Vector().elements();
    }
    
    public Appender getAppender(final String name) {
        return null;
    }
    
    public Level getEffectiveLevel() {
        return Level.OFF;
    }
    
    public Priority getChainedPriority() {
        return this.getEffectiveLevel();
    }
    
    public ResourceBundle getResourceBundle() {
        return null;
    }
    
    public void info(final Object message) {
    }
    
    public void info(final Object message, final Throwable t) {
    }
    
    public boolean isAttached(final Appender appender) {
        return false;
    }
    
    public boolean isDebugEnabled() {
        return false;
    }
    
    public boolean isEnabledFor(final Priority level) {
        return false;
    }
    
    public boolean isInfoEnabled() {
        return false;
    }
    
    public void l7dlog(final Priority priority, final String key, final Throwable t) {
    }
    
    public void l7dlog(final Priority priority, final String key, final Object[] params, final Throwable t) {
    }
    
    public void log(final Priority priority, final Object message, final Throwable t) {
    }
    
    public void log(final Priority priority, final Object message) {
    }
    
    public void log(final String callerFQCN, final Priority level, final Object message, final Throwable t) {
    }
    
    public void removeAllAppenders() {
    }
    
    public void removeAppender(final Appender appender) {
    }
    
    public void removeAppender(final String name) {
    }
    
    public void setLevel(final Level level) {
    }
    
    public void setPriority(final Priority priority) {
    }
    
    public void setResourceBundle(final ResourceBundle bundle) {
    }
    
    public void warn(final Object message) {
    }
    
    public void warn(final Object message, final Throwable t) {
    }
    
    public void trace(final Object message) {
    }
    
    public void trace(final Object message, final Throwable t) {
    }
    
    public boolean isTraceEnabled() {
        return false;
    }
}
