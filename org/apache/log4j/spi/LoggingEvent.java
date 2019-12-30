// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.helpers.LogLog;
import java.io.InterruptedIOException;
import org.apache.log4j.helpers.Loader;
import java.lang.reflect.Method;
import java.io.ObjectInputStream;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import java.util.Map;
import org.apache.log4j.Level;
import java.util.Hashtable;
import org.apache.log4j.Priority;
import org.apache.log4j.Category;
import java.io.Serializable;

public class LoggingEvent implements Serializable
{
    private static long startTime;
    public final transient String fqnOfCategoryClass;
    private transient Category logger;
    public final String categoryName;
    public transient Priority level;
    private String ndc;
    private Hashtable mdcCopy;
    private boolean ndcLookupRequired;
    private boolean mdcCopyLookupRequired;
    private transient Object message;
    private String renderedMessage;
    private String threadName;
    private ThrowableInformation throwableInfo;
    public final long timeStamp;
    private LocationInfo locationInfo;
    static final long serialVersionUID = -868428216207166145L;
    static final Integer[] PARAM_ARRAY;
    static final String TO_LEVEL = "toLevel";
    static final Class[] TO_LEVEL_PARAMS;
    static final Hashtable methodCache;
    
    public LoggingEvent(final String fqnOfCategoryClass, final Category logger, final Priority level, final Object message, final Throwable throwable) {
        this.ndcLookupRequired = true;
        this.mdcCopyLookupRequired = true;
        this.fqnOfCategoryClass = fqnOfCategoryClass;
        this.logger = logger;
        this.categoryName = logger.getName();
        this.level = level;
        this.message = message;
        if (throwable != null) {
            this.throwableInfo = new ThrowableInformation(throwable, logger);
        }
        this.timeStamp = System.currentTimeMillis();
    }
    
    public LoggingEvent(final String fqnOfCategoryClass, final Category logger, final long timeStamp, final Priority level, final Object message, final Throwable throwable) {
        this.ndcLookupRequired = true;
        this.mdcCopyLookupRequired = true;
        this.fqnOfCategoryClass = fqnOfCategoryClass;
        this.logger = logger;
        this.categoryName = logger.getName();
        this.level = level;
        this.message = message;
        if (throwable != null) {
            this.throwableInfo = new ThrowableInformation(throwable, logger);
        }
        this.timeStamp = timeStamp;
    }
    
    public LoggingEvent(final String fqnOfCategoryClass, final Category logger, final long timeStamp, final Level level, final Object message, final String threadName, final ThrowableInformation throwable, final String ndc, final LocationInfo info, final Map properties) {
        this.ndcLookupRequired = true;
        this.mdcCopyLookupRequired = true;
        this.fqnOfCategoryClass = fqnOfCategoryClass;
        this.logger = logger;
        if (logger != null) {
            this.categoryName = logger.getName();
        }
        else {
            this.categoryName = null;
        }
        this.level = level;
        this.message = message;
        if (throwable != null) {
            this.throwableInfo = throwable;
        }
        this.timeStamp = timeStamp;
        this.threadName = threadName;
        this.ndcLookupRequired = false;
        this.ndc = ndc;
        this.locationInfo = info;
        this.mdcCopyLookupRequired = false;
        if (properties != null) {
            this.mdcCopy = new Hashtable(properties);
        }
    }
    
    public LocationInfo getLocationInformation() {
        if (this.locationInfo == null) {
            this.locationInfo = new LocationInfo(new Throwable(), this.fqnOfCategoryClass);
        }
        return this.locationInfo;
    }
    
    public Level getLevel() {
        return (Level)this.level;
    }
    
    public String getLoggerName() {
        return this.categoryName;
    }
    
    public Category getLogger() {
        return this.logger;
    }
    
    public Object getMessage() {
        if (this.message != null) {
            return this.message;
        }
        return this.getRenderedMessage();
    }
    
    public String getNDC() {
        if (this.ndcLookupRequired) {
            this.ndcLookupRequired = false;
            this.ndc = NDC.get();
        }
        return this.ndc;
    }
    
    public Object getMDC(final String key) {
        if (this.mdcCopy != null) {
            final Object r = this.mdcCopy.get(key);
            if (r != null) {
                return r;
            }
        }
        return MDC.get(key);
    }
    
    public void getMDCCopy() {
        if (this.mdcCopyLookupRequired) {
            this.mdcCopyLookupRequired = false;
            final Hashtable t = MDC.getContext();
            if (t != null) {
                this.mdcCopy = (Hashtable)t.clone();
            }
        }
    }
    
    public String getRenderedMessage() {
        if (this.renderedMessage == null && this.message != null) {
            if (this.message instanceof String) {
                this.renderedMessage = (String)this.message;
            }
            else {
                final LoggerRepository repository = this.logger.getLoggerRepository();
                if (repository instanceof RendererSupport) {
                    final RendererSupport rs = (RendererSupport)repository;
                    this.renderedMessage = rs.getRendererMap().findAndRender(this.message);
                }
                else {
                    this.renderedMessage = this.message.toString();
                }
            }
        }
        return this.renderedMessage;
    }
    
    public static long getStartTime() {
        return LoggingEvent.startTime;
    }
    
    public String getThreadName() {
        if (this.threadName == null) {
            this.threadName = Thread.currentThread().getName();
        }
        return this.threadName;
    }
    
    public ThrowableInformation getThrowableInformation() {
        return this.throwableInfo;
    }
    
    public String[] getThrowableStrRep() {
        if (this.throwableInfo == null) {
            return null;
        }
        return this.throwableInfo.getThrowableStrRep();
    }
    
    private void readLevel(final ObjectInputStream ois) throws IOException, ClassNotFoundException {
        final int p = ois.readInt();
        try {
            final String className = (String)ois.readObject();
            if (className == null) {
                this.level = Level.toLevel(p);
            }
            else {
                Method m = LoggingEvent.methodCache.get(className);
                if (m == null) {
                    final Class clazz = Loader.loadClass(className);
                    m = clazz.getDeclaredMethod("toLevel", (Class[])LoggingEvent.TO_LEVEL_PARAMS);
                    LoggingEvent.methodCache.put(className, m);
                }
                this.level = (Level)m.invoke(null, new Integer(p));
            }
        }
        catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof InterruptedException || e.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.warn("Level deserialization failed, reverting to default.", e);
            this.level = Level.toLevel(p);
        }
        catch (NoSuchMethodException e2) {
            LogLog.warn("Level deserialization failed, reverting to default.", e2);
            this.level = Level.toLevel(p);
        }
        catch (IllegalAccessException e3) {
            LogLog.warn("Level deserialization failed, reverting to default.", e3);
            this.level = Level.toLevel(p);
        }
        catch (RuntimeException e4) {
            LogLog.warn("Level deserialization failed, reverting to default.", e4);
            this.level = Level.toLevel(p);
        }
    }
    
    private void readObject(final ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.readLevel(ois);
        if (this.locationInfo == null) {
            this.locationInfo = new LocationInfo(null, null);
        }
    }
    
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        this.getThreadName();
        this.getRenderedMessage();
        this.getNDC();
        this.getMDCCopy();
        this.getThrowableStrRep();
        oos.defaultWriteObject();
        this.writeLevel(oos);
    }
    
    private void writeLevel(final ObjectOutputStream oos) throws IOException {
        oos.writeInt(this.level.toInt());
        final Class clazz = this.level.getClass();
        if (clazz == Level.class) {
            oos.writeObject(null);
        }
        else {
            oos.writeObject(clazz.getName());
        }
    }
    
    public final void setProperty(final String propName, final String propValue) {
        if (this.mdcCopy == null) {
            this.getMDCCopy();
        }
        if (this.mdcCopy == null) {
            this.mdcCopy = new Hashtable();
        }
        this.mdcCopy.put(propName, propValue);
    }
    
    public final String getProperty(final String key) {
        final Object value = this.getMDC(key);
        String retval = null;
        if (value != null) {
            retval = value.toString();
        }
        return retval;
    }
    
    public final boolean locationInformationExists() {
        return this.locationInfo != null;
    }
    
    public final long getTimeStamp() {
        return this.timeStamp;
    }
    
    public Set getPropertyKeySet() {
        return this.getProperties().keySet();
    }
    
    public Map getProperties() {
        this.getMDCCopy();
        Map properties;
        if (this.mdcCopy == null) {
            properties = new HashMap();
        }
        else {
            properties = this.mdcCopy;
        }
        return Collections.unmodifiableMap((Map<?, ?>)properties);
    }
    
    public String getFQNOfLoggerClass() {
        return this.fqnOfCategoryClass;
    }
    
    public Object removeProperty(final String propName) {
        if (this.mdcCopy == null) {
            this.getMDCCopy();
        }
        if (this.mdcCopy == null) {
            this.mdcCopy = new Hashtable();
        }
        return this.mdcCopy.remove(propName);
    }
    
    static {
        LoggingEvent.startTime = System.currentTimeMillis();
        PARAM_ARRAY = new Integer[1];
        TO_LEVEL_PARAMS = new Class[] { Integer.TYPE };
        methodCache = new Hashtable(3);
    }
}
