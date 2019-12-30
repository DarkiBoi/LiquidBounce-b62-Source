// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import java.util.Iterator;
import java.text.MessageFormat;
import java.util.Enumeration;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import java.util.Map;
import java.util.List;
import org.apache.log4j.spi.AppenderAttachable;

public class AsyncAppender extends AppenderSkeleton implements AppenderAttachable
{
    public static final int DEFAULT_BUFFER_SIZE = 128;
    private final List buffer;
    private final Map discardMap;
    private int bufferSize;
    AppenderAttachableImpl aai;
    private final AppenderAttachableImpl appenders;
    private final Thread dispatcher;
    private boolean locationInfo;
    private boolean blocking;
    
    public AsyncAppender() {
        this.buffer = new ArrayList();
        this.discardMap = new HashMap();
        this.bufferSize = 128;
        this.locationInfo = false;
        this.blocking = true;
        this.appenders = new AppenderAttachableImpl();
        this.aai = this.appenders;
        (this.dispatcher = new Thread(new Dispatcher(this, this.buffer, this.discardMap, this.appenders))).setDaemon(true);
        this.dispatcher.setName("AsyncAppender-Dispatcher-" + this.dispatcher.getName());
        this.dispatcher.start();
    }
    
    public void addAppender(final Appender newAppender) {
        synchronized (this.appenders) {
            this.appenders.addAppender(newAppender);
        }
    }
    
    public void append(final LoggingEvent event) {
        if (this.dispatcher == null || !this.dispatcher.isAlive() || this.bufferSize <= 0) {
            synchronized (this.appenders) {
                this.appenders.appendLoopOnAppenders(event);
            }
            return;
        }
        event.getNDC();
        event.getThreadName();
        event.getMDCCopy();
        if (this.locationInfo) {
            event.getLocationInformation();
        }
        event.getRenderedMessage();
        event.getThrowableStrRep();
        synchronized (this.buffer) {
            while (true) {
                final int previousSize = this.buffer.size();
                if (previousSize < this.bufferSize) {
                    this.buffer.add(event);
                    if (previousSize == 0) {
                        this.buffer.notifyAll();
                        break;
                    }
                    break;
                }
                else {
                    boolean discard = true;
                    if (this.blocking && !Thread.interrupted() && Thread.currentThread() != this.dispatcher) {
                        try {
                            this.buffer.wait();
                            discard = false;
                        }
                        catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    if (!discard) {
                        continue;
                    }
                    final String loggerName = event.getLoggerName();
                    DiscardSummary summary = this.discardMap.get(loggerName);
                    if (summary == null) {
                        summary = new DiscardSummary(event);
                        this.discardMap.put(loggerName, summary);
                        break;
                    }
                    summary.add(event);
                    break;
                }
            }
        }
    }
    
    public void close() {
        synchronized (this.buffer) {
            this.closed = true;
            this.buffer.notifyAll();
        }
        try {
            this.dispatcher.join();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogLog.error("Got an InterruptedException while waiting for the dispatcher to finish.", e);
        }
        synchronized (this.appenders) {
            final Enumeration iter = this.appenders.getAllAppenders();
            if (iter != null) {
                while (iter.hasMoreElements()) {
                    final Object next = iter.nextElement();
                    if (next instanceof Appender) {
                        ((Appender)next).close();
                    }
                }
            }
        }
    }
    
    public Enumeration getAllAppenders() {
        synchronized (this.appenders) {
            return this.appenders.getAllAppenders();
        }
    }
    
    public Appender getAppender(final String name) {
        synchronized (this.appenders) {
            return this.appenders.getAppender(name);
        }
    }
    
    public boolean getLocationInfo() {
        return this.locationInfo;
    }
    
    public boolean isAttached(final Appender appender) {
        synchronized (this.appenders) {
            return this.appenders.isAttached(appender);
        }
    }
    
    public boolean requiresLayout() {
        return false;
    }
    
    public void removeAllAppenders() {
        synchronized (this.appenders) {
            this.appenders.removeAllAppenders();
        }
    }
    
    public void removeAppender(final Appender appender) {
        synchronized (this.appenders) {
            this.appenders.removeAppender(appender);
        }
    }
    
    public void removeAppender(final String name) {
        synchronized (this.appenders) {
            this.appenders.removeAppender(name);
        }
    }
    
    public void setLocationInfo(final boolean flag) {
        this.locationInfo = flag;
    }
    
    public void setBufferSize(final int size) {
        if (size < 0) {
            throw new NegativeArraySizeException("size");
        }
        synchronized (this.buffer) {
            this.bufferSize = ((size < 1) ? 1 : size);
            this.buffer.notifyAll();
        }
    }
    
    public int getBufferSize() {
        return this.bufferSize;
    }
    
    public void setBlocking(final boolean value) {
        synchronized (this.buffer) {
            this.blocking = value;
            this.buffer.notifyAll();
        }
    }
    
    public boolean getBlocking() {
        return this.blocking;
    }
    
    private static final class DiscardSummary
    {
        private LoggingEvent maxEvent;
        private int count;
        
        public DiscardSummary(final LoggingEvent event) {
            this.maxEvent = event;
            this.count = 1;
        }
        
        public void add(final LoggingEvent event) {
            if (event.getLevel().toInt() > this.maxEvent.getLevel().toInt()) {
                this.maxEvent = event;
            }
            ++this.count;
        }
        
        public LoggingEvent createEvent() {
            final String msg = MessageFormat.format("Discarded {0} messages due to full event buffer including: {1}", new Integer(this.count), this.maxEvent.getMessage());
            return new LoggingEvent("org.apache.log4j.AsyncAppender.DONT_REPORT_LOCATION", Logger.getLogger(this.maxEvent.getLoggerName()), this.maxEvent.getLevel(), msg, null);
        }
    }
    
    private static class Dispatcher implements Runnable
    {
        private final AsyncAppender parent;
        private final List buffer;
        private final Map discardMap;
        private final AppenderAttachableImpl appenders;
        
        public Dispatcher(final AsyncAppender parent, final List buffer, final Map discardMap, final AppenderAttachableImpl appenders) {
            this.parent = parent;
            this.buffer = buffer;
            this.appenders = appenders;
            this.discardMap = discardMap;
        }
        
        public void run() {
            boolean isActive = true;
            try {
                while (isActive) {
                    LoggingEvent[] events = null;
                    synchronized (this.buffer) {
                        int bufferSize;
                        for (bufferSize = this.buffer.size(), isActive = !this.parent.closed; bufferSize == 0 && isActive; bufferSize = this.buffer.size(), isActive = !this.parent.closed) {
                            this.buffer.wait();
                        }
                        if (bufferSize > 0) {
                            events = new LoggingEvent[bufferSize + this.discardMap.size()];
                            this.buffer.toArray(events);
                            int index = bufferSize;
                            final Iterator iter = this.discardMap.values().iterator();
                            while (iter.hasNext()) {
                                events[index++] = iter.next().createEvent();
                            }
                            this.buffer.clear();
                            this.discardMap.clear();
                            this.buffer.notifyAll();
                        }
                    }
                    if (events != null) {
                        for (int i = 0; i < events.length; ++i) {
                            synchronized (this.appenders) {
                                this.appenders.appendLoopOnAppenders(events[i]);
                            }
                        }
                    }
                }
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
