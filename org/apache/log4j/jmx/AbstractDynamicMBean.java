// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.jmx;

import java.util.Enumeration;
import javax.management.InstanceNotFoundException;
import javax.management.NotCompliantMBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.ObjectName;
import org.apache.log4j.Logger;
import java.util.Iterator;
import javax.management.JMException;
import javax.management.Attribute;
import javax.management.RuntimeOperationsException;
import javax.management.AttributeList;
import org.apache.log4j.Appender;
import java.util.Vector;
import javax.management.MBeanServer;
import javax.management.MBeanRegistration;
import javax.management.DynamicMBean;

public abstract class AbstractDynamicMBean implements DynamicMBean, MBeanRegistration
{
    String dClassName;
    MBeanServer server;
    private final Vector mbeanList;
    
    public AbstractDynamicMBean() {
        this.mbeanList = new Vector();
    }
    
    protected static String getAppenderName(final Appender appender) {
        String name = appender.getName();
        if (name == null || name.trim().length() == 0) {
            name = appender.toString();
        }
        return name;
    }
    
    public AttributeList getAttributes(final String[] attributeNames) {
        if (attributeNames == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"), "Cannot invoke a getter of " + this.dClassName);
        }
        final AttributeList resultList = new AttributeList();
        if (attributeNames.length == 0) {
            return resultList;
        }
        for (int i = 0; i < attributeNames.length; ++i) {
            try {
                final Object value = this.getAttribute(attributeNames[i]);
                resultList.add(new Attribute(attributeNames[i], value));
            }
            catch (JMException e) {
                e.printStackTrace();
            }
            catch (RuntimeException e2) {
                e2.printStackTrace();
            }
        }
        return resultList;
    }
    
    public AttributeList setAttributes(final AttributeList attributes) {
        if (attributes == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList attributes cannot be null"), "Cannot invoke a setter of " + this.dClassName);
        }
        final AttributeList resultList = new AttributeList();
        if (attributes.isEmpty()) {
            return resultList;
        }
        for (final Attribute attr : attributes) {
            try {
                this.setAttribute(attr);
                final String name = attr.getName();
                final Object value = this.getAttribute(name);
                resultList.add(new Attribute(name, value));
            }
            catch (JMException e) {
                e.printStackTrace();
            }
            catch (RuntimeException e2) {
                e2.printStackTrace();
            }
        }
        return resultList;
    }
    
    protected abstract Logger getLogger();
    
    public void postDeregister() {
        this.getLogger().debug("postDeregister is called.");
    }
    
    public void postRegister(final Boolean registrationDone) {
    }
    
    public ObjectName preRegister(final MBeanServer server, final ObjectName name) {
        this.getLogger().debug("preRegister called. Server=" + server + ", name=" + name);
        this.server = server;
        return name;
    }
    
    protected void registerMBean(final Object mbean, final ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        this.server.registerMBean(mbean, objectName);
        this.mbeanList.add(objectName);
    }
    
    public void preDeregister() {
        this.getLogger().debug("preDeregister called.");
        final Enumeration iterator = this.mbeanList.elements();
        while (iterator.hasMoreElements()) {
            final ObjectName name = iterator.nextElement();
            try {
                this.server.unregisterMBean(name);
            }
            catch (InstanceNotFoundException e) {
                this.getLogger().warn("Missing MBean " + name.getCanonicalName());
            }
            catch (MBeanRegistrationException e2) {
                this.getLogger().warn("Failed unregistering " + name.getCanonicalName());
            }
        }
    }
}
