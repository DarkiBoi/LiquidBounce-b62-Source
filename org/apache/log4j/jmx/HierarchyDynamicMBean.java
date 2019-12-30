// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.jmx;

import javax.management.InvalidAttributeValueException;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.OptionConverter;
import javax.management.Attribute;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanInfo;
import javax.management.JMException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.NotificationFilterSupport;
import javax.management.ObjectName;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanAttributeInfo;
import java.lang.reflect.Constructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import javax.management.NotificationBroadcasterSupport;
import java.util.Vector;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.NotificationBroadcaster;
import org.apache.log4j.spi.HierarchyEventListener;

public class HierarchyDynamicMBean extends AbstractDynamicMBean implements HierarchyEventListener, NotificationBroadcaster
{
    static final String ADD_APPENDER = "addAppender.";
    static final String THRESHOLD = "threshold";
    private MBeanConstructorInfo[] dConstructors;
    private MBeanOperationInfo[] dOperations;
    private Vector vAttributes;
    private String dClassName;
    private String dDescription;
    private NotificationBroadcasterSupport nbs;
    private LoggerRepository hierarchy;
    private static Logger log;
    
    public HierarchyDynamicMBean() {
        this.dConstructors = new MBeanConstructorInfo[1];
        this.dOperations = new MBeanOperationInfo[1];
        this.vAttributes = new Vector();
        this.dClassName = this.getClass().getName();
        this.dDescription = "This MBean acts as a management facade for org.apache.log4j.Hierarchy.";
        this.nbs = new NotificationBroadcasterSupport();
        this.hierarchy = LogManager.getLoggerRepository();
        this.buildDynamicMBeanInfo();
    }
    
    private void buildDynamicMBeanInfo() {
        final Constructor[] constructors = this.getClass().getConstructors();
        this.dConstructors[0] = new MBeanConstructorInfo("HierarchyDynamicMBean(): Constructs a HierarchyDynamicMBean instance", constructors[0]);
        this.vAttributes.add(new MBeanAttributeInfo("threshold", "java.lang.String", "The \"threshold\" state of the hiearchy.", true, true, false));
        final MBeanParameterInfo[] params = { new MBeanParameterInfo("name", "java.lang.String", "Create a logger MBean") };
        this.dOperations[0] = new MBeanOperationInfo("addLoggerMBean", "addLoggerMBean(): add a loggerMBean", params, "javax.management.ObjectName", 1);
    }
    
    public ObjectName addLoggerMBean(final String name) {
        final Logger cat = LogManager.exists(name);
        if (cat != null) {
            return this.addLoggerMBean(cat);
        }
        return null;
    }
    
    ObjectName addLoggerMBean(final Logger logger) {
        final String name = logger.getName();
        ObjectName objectName = null;
        try {
            final LoggerDynamicMBean loggerMBean = new LoggerDynamicMBean(logger);
            objectName = new ObjectName("log4j", "logger", name);
            if (!this.server.isRegistered(objectName)) {
                this.registerMBean(loggerMBean, objectName);
                final NotificationFilterSupport nfs = new NotificationFilterSupport();
                nfs.enableType("addAppender." + logger.getName());
                HierarchyDynamicMBean.log.debug("---Adding logger [" + name + "] as listener.");
                this.nbs.addNotificationListener(loggerMBean, nfs, null);
                this.vAttributes.add(new MBeanAttributeInfo("logger=" + name, "javax.management.ObjectName", "The " + name + " logger.", true, true, false));
            }
        }
        catch (JMException e) {
            HierarchyDynamicMBean.log.error("Could not add loggerMBean for [" + name + "].", e);
        }
        catch (RuntimeException e2) {
            HierarchyDynamicMBean.log.error("Could not add loggerMBean for [" + name + "].", e2);
        }
        return objectName;
    }
    
    public void addNotificationListener(final NotificationListener listener, final NotificationFilter filter, final Object handback) {
        this.nbs.addNotificationListener(listener, filter, handback);
    }
    
    protected Logger getLogger() {
        return HierarchyDynamicMBean.log;
    }
    
    public MBeanInfo getMBeanInfo() {
        final MBeanAttributeInfo[] attribs = new MBeanAttributeInfo[this.vAttributes.size()];
        this.vAttributes.toArray(attribs);
        return new MBeanInfo(this.dClassName, this.dDescription, attribs, this.dConstructors, this.dOperations, new MBeanNotificationInfo[0]);
    }
    
    public MBeanNotificationInfo[] getNotificationInfo() {
        return this.nbs.getNotificationInfo();
    }
    
    public Object invoke(final String operationName, final Object[] params, final String[] signature) throws MBeanException, ReflectionException {
        if (operationName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"), "Cannot invoke a null operation in " + this.dClassName);
        }
        if (operationName.equals("addLoggerMBean")) {
            return this.addLoggerMBean((String)params[0]);
        }
        throw new ReflectionException(new NoSuchMethodException(operationName), "Cannot find the operation " + operationName + " in " + this.dClassName);
    }
    
    public Object getAttribute(final String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attributeName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke a getter of " + this.dClassName + " with null attribute name");
        }
        HierarchyDynamicMBean.log.debug("Called getAttribute with [" + attributeName + "].");
        if (attributeName.equals("threshold")) {
            return this.hierarchy.getThreshold();
        }
        if (attributeName.startsWith("logger")) {
            final int k = attributeName.indexOf("%3D");
            String val = attributeName;
            if (k > 0) {
                val = attributeName.substring(0, k) + '=' + attributeName.substring(k + 3);
            }
            try {
                return new ObjectName("log4j:" + val);
            }
            catch (JMException e) {
                HierarchyDynamicMBean.log.error("Could not create ObjectName" + val);
            }
            catch (RuntimeException e2) {
                HierarchyDynamicMBean.log.error("Could not create ObjectName" + val);
            }
        }
        throw new AttributeNotFoundException("Cannot find " + attributeName + " attribute in " + this.dClassName);
    }
    
    public void addAppenderEvent(final Category logger, final Appender appender) {
        HierarchyDynamicMBean.log.debug("addAppenderEvent called: logger=" + logger.getName() + ", appender=" + appender.getName());
        final Notification n = new Notification("addAppender." + logger.getName(), this, 0L);
        n.setUserData(appender);
        HierarchyDynamicMBean.log.debug("sending notification.");
        this.nbs.sendNotification(n);
    }
    
    public void removeAppenderEvent(final Category cat, final Appender appender) {
        HierarchyDynamicMBean.log.debug("removeAppenderCalled: logger=" + cat.getName() + ", appender=" + appender.getName());
    }
    
    public void postRegister(final Boolean registrationDone) {
        HierarchyDynamicMBean.log.debug("postRegister is called.");
        this.hierarchy.addHierarchyEventListener(this);
        final Logger root = this.hierarchy.getRootLogger();
        this.addLoggerMBean(root);
    }
    
    public void removeNotificationListener(final NotificationListener listener) throws ListenerNotFoundException {
        this.nbs.removeNotificationListener(listener);
    }
    
    public void setAttribute(final Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Cannot invoke a setter of " + this.dClassName + " with null attribute");
        }
        final String name = attribute.getName();
        final Object value = attribute.getValue();
        if (name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke the setter of " + this.dClassName + " with null attribute name");
        }
        if (name.equals("threshold")) {
            final Level l = OptionConverter.toLevel((String)value, this.hierarchy.getThreshold());
            this.hierarchy.setThreshold(l);
        }
    }
    
    static {
        HierarchyDynamicMBean.log = Logger.getLogger(HierarchyDynamicMBean.class);
    }
}
