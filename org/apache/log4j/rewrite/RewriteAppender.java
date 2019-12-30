// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.rewrite;

import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.xml.DOMConfigurator;
import java.util.Properties;
import org.w3c.dom.Element;
import java.util.Enumeration;
import org.apache.log4j.Appender;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.xml.UnrecognizedElementHandler;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.AppenderSkeleton;

public class RewriteAppender extends AppenderSkeleton implements AppenderAttachable, UnrecognizedElementHandler
{
    private RewritePolicy policy;
    private final AppenderAttachableImpl appenders;
    
    public RewriteAppender() {
        this.appenders = new AppenderAttachableImpl();
    }
    
    protected void append(final LoggingEvent event) {
        LoggingEvent rewritten = event;
        if (this.policy != null) {
            rewritten = this.policy.rewrite(event);
        }
        if (rewritten != null) {
            synchronized (this.appenders) {
                this.appenders.appendLoopOnAppenders(rewritten);
            }
        }
    }
    
    public void addAppender(final Appender newAppender) {
        synchronized (this.appenders) {
            this.appenders.addAppender(newAppender);
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
    
    public void close() {
        this.closed = true;
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
    
    public void setRewritePolicy(final RewritePolicy rewritePolicy) {
        this.policy = rewritePolicy;
    }
    
    public boolean parseUnrecognizedElement(final Element element, final Properties props) throws Exception {
        final String nodeName = element.getNodeName();
        if ("rewritePolicy".equals(nodeName)) {
            final Object rewritePolicy = DOMConfigurator.parseElement(element, props, RewritePolicy.class);
            if (rewritePolicy != null) {
                if (rewritePolicy instanceof OptionHandler) {
                    ((OptionHandler)rewritePolicy).activateOptions();
                }
                this.setRewritePolicy((RewritePolicy)rewritePolicy);
            }
            return true;
        }
        return false;
    }
}
