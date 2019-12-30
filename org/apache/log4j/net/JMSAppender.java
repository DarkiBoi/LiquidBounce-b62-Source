// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import javax.jms.ObjectMessage;
import javax.jms.Message;
import java.io.Serializable;
import org.apache.log4j.spi.LoggingEvent;
import javax.naming.NameNotFoundException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import java.util.Hashtable;
import javax.naming.InitialContext;
import java.util.Properties;
import org.apache.log4j.helpers.LogLog;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicConnection;
import org.apache.log4j.AppenderSkeleton;

public class JMSAppender extends AppenderSkeleton
{
    String securityPrincipalName;
    String securityCredentials;
    String initialContextFactoryName;
    String urlPkgPrefixes;
    String providerURL;
    String topicBindingName;
    String tcfBindingName;
    String userName;
    String password;
    boolean locationInfo;
    TopicConnection topicConnection;
    TopicSession topicSession;
    TopicPublisher topicPublisher;
    
    public void setTopicConnectionFactoryBindingName(final String tcfBindingName) {
        this.tcfBindingName = tcfBindingName;
    }
    
    public String getTopicConnectionFactoryBindingName() {
        return this.tcfBindingName;
    }
    
    public void setTopicBindingName(final String topicBindingName) {
        this.topicBindingName = topicBindingName;
    }
    
    public String getTopicBindingName() {
        return this.topicBindingName;
    }
    
    public boolean getLocationInfo() {
        return this.locationInfo;
    }
    
    public void activateOptions() {
        try {
            LogLog.debug("Getting initial context.");
            Context jndi;
            if (this.initialContextFactoryName != null) {
                final Properties env = new Properties();
                env.put("java.naming.factory.initial", this.initialContextFactoryName);
                if (this.providerURL != null) {
                    env.put("java.naming.provider.url", this.providerURL);
                }
                else {
                    LogLog.warn("You have set InitialContextFactoryName option but not the ProviderURL. This is likely to cause problems.");
                }
                if (this.urlPkgPrefixes != null) {
                    env.put("java.naming.factory.url.pkgs", this.urlPkgPrefixes);
                }
                if (this.securityPrincipalName != null) {
                    env.put("java.naming.security.principal", this.securityPrincipalName);
                    if (this.securityCredentials != null) {
                        env.put("java.naming.security.credentials", this.securityCredentials);
                    }
                    else {
                        LogLog.warn("You have set SecurityPrincipalName option but not the SecurityCredentials. This is likely to cause problems.");
                    }
                }
                jndi = new InitialContext(env);
            }
            else {
                jndi = new InitialContext();
            }
            LogLog.debug("Looking up [" + this.tcfBindingName + "]");
            final TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)this.lookup(jndi, this.tcfBindingName);
            LogLog.debug("About to create TopicConnection.");
            if (this.userName != null) {
                this.topicConnection = topicConnectionFactory.createTopicConnection(this.userName, this.password);
            }
            else {
                this.topicConnection = topicConnectionFactory.createTopicConnection();
            }
            LogLog.debug("Creating TopicSession, non-transactional, in AUTO_ACKNOWLEDGE mode.");
            this.topicSession = this.topicConnection.createTopicSession(false, 1);
            LogLog.debug("Looking up topic name [" + this.topicBindingName + "].");
            final Topic topic = (Topic)this.lookup(jndi, this.topicBindingName);
            LogLog.debug("Creating TopicPublisher.");
            this.topicPublisher = this.topicSession.createPublisher(topic);
            LogLog.debug("Starting TopicConnection.");
            this.topicConnection.start();
            jndi.close();
        }
        catch (JMSException e) {
            this.errorHandler.error("Error while activating options for appender named [" + this.name + "].", (Exception)e, 0);
        }
        catch (NamingException e2) {
            this.errorHandler.error("Error while activating options for appender named [" + this.name + "].", e2, 0);
        }
        catch (RuntimeException e3) {
            this.errorHandler.error("Error while activating options for appender named [" + this.name + "].", e3, 0);
        }
    }
    
    protected Object lookup(final Context ctx, final String name) throws NamingException {
        try {
            return ctx.lookup(name);
        }
        catch (NameNotFoundException e) {
            LogLog.error("Could not find name [" + name + "].");
            throw e;
        }
    }
    
    protected boolean checkEntryConditions() {
        String fail = null;
        if (this.topicConnection == null) {
            fail = "No TopicConnection";
        }
        else if (this.topicSession == null) {
            fail = "No TopicSession";
        }
        else if (this.topicPublisher == null) {
            fail = "No TopicPublisher";
        }
        if (fail != null) {
            this.errorHandler.error(fail + " for JMSAppender named [" + this.name + "].");
            return false;
        }
        return true;
    }
    
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        LogLog.debug("Closing appender [" + this.name + "].");
        this.closed = true;
        try {
            if (this.topicSession != null) {
                this.topicSession.close();
            }
            if (this.topicConnection != null) {
                this.topicConnection.close();
            }
        }
        catch (JMSException e) {
            LogLog.error("Error while closing JMSAppender [" + this.name + "].", (Throwable)e);
        }
        catch (RuntimeException e2) {
            LogLog.error("Error while closing JMSAppender [" + this.name + "].", e2);
        }
        this.topicPublisher = null;
        this.topicSession = null;
        this.topicConnection = null;
    }
    
    public void append(final LoggingEvent event) {
        if (!this.checkEntryConditions()) {
            return;
        }
        try {
            final ObjectMessage msg = this.topicSession.createObjectMessage();
            if (this.locationInfo) {
                event.getLocationInformation();
            }
            msg.setObject((Serializable)event);
            this.topicPublisher.publish((Message)msg);
        }
        catch (JMSException e) {
            this.errorHandler.error("Could not publish message in JMSAppender [" + this.name + "].", (Exception)e, 0);
        }
        catch (RuntimeException e2) {
            this.errorHandler.error("Could not publish message in JMSAppender [" + this.name + "].", e2, 0);
        }
    }
    
    public String getInitialContextFactoryName() {
        return this.initialContextFactoryName;
    }
    
    public void setInitialContextFactoryName(final String initialContextFactoryName) {
        this.initialContextFactoryName = initialContextFactoryName;
    }
    
    public String getProviderURL() {
        return this.providerURL;
    }
    
    public void setProviderURL(final String providerURL) {
        this.providerURL = providerURL;
    }
    
    String getURLPkgPrefixes() {
        return this.urlPkgPrefixes;
    }
    
    public void setURLPkgPrefixes(final String urlPkgPrefixes) {
        this.urlPkgPrefixes = urlPkgPrefixes;
    }
    
    public String getSecurityCredentials() {
        return this.securityCredentials;
    }
    
    public void setSecurityCredentials(final String securityCredentials) {
        this.securityCredentials = securityCredentials;
    }
    
    public String getSecurityPrincipalName() {
        return this.securityPrincipalName;
    }
    
    public void setSecurityPrincipalName(final String securityPrincipalName) {
        this.securityPrincipalName = securityPrincipalName;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public void setLocationInfo(final boolean locationInfo) {
        this.locationInfo = locationInfo;
    }
    
    protected TopicConnection getTopicConnection() {
        return this.topicConnection;
    }
    
    protected TopicSession getTopicSession() {
        return this.topicSession;
    }
    
    protected TopicPublisher getTopicPublisher() {
        return this.topicPublisher;
    }
    
    public boolean requiresLayout() {
        return false;
    }
}
