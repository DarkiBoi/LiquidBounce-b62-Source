// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import javax.naming.NameNotFoundException;
import org.apache.log4j.spi.LoggingEvent;
import javax.jms.ObjectMessage;
import javax.jms.Message;
import javax.jms.TopicSubscriber;
import javax.jms.TopicSession;
import javax.jms.TopicConnection;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.Logger;
import javax.jms.MessageListener;

public class JMSSink implements MessageListener
{
    static Logger logger;
    
    public static void main(final String[] args) throws Exception {
        if (args.length != 5) {
            usage("Wrong number of arguments.");
        }
        final String tcfBindingName = args[0];
        final String topicBindingName = args[1];
        final String username = args[2];
        final String password = args[3];
        final String configFile = args[4];
        if (configFile.endsWith(".xml")) {
            DOMConfigurator.configure(configFile);
        }
        else {
            PropertyConfigurator.configure(configFile);
        }
        new JMSSink(tcfBindingName, topicBindingName, username, password);
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type \"exit\" to quit JMSSink.");
        String s;
        do {
            s = stdin.readLine();
        } while (!s.equalsIgnoreCase("exit"));
        System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
    }
    
    public JMSSink(final String tcfBindingName, final String topicBindingName, final String username, final String password) {
        try {
            final Context ctx = new InitialContext();
            final TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(ctx, tcfBindingName);
            final TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);
            topicConnection.start();
            final TopicSession topicSession = topicConnection.createTopicSession(false, 1);
            final Topic topic = (Topic)ctx.lookup(topicBindingName);
            final TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
            topicSubscriber.setMessageListener((MessageListener)this);
        }
        catch (JMSException e) {
            JMSSink.logger.error("Could not read JMS message.", (Throwable)e);
        }
        catch (NamingException e2) {
            JMSSink.logger.error("Could not read JMS message.", e2);
        }
        catch (RuntimeException e3) {
            JMSSink.logger.error("Could not read JMS message.", e3);
        }
    }
    
    public void onMessage(final Message message) {
        try {
            if (message instanceof ObjectMessage) {
                final ObjectMessage objectMessage = (ObjectMessage)message;
                final LoggingEvent event = (LoggingEvent)objectMessage.getObject();
                final Logger remoteLogger = Logger.getLogger(event.getLoggerName());
                remoteLogger.callAppenders(event);
            }
            else {
                JMSSink.logger.warn("Received message is of type " + message.getJMSType() + ", was expecting ObjectMessage.");
            }
        }
        catch (JMSException jmse) {
            JMSSink.logger.error("Exception thrown while processing incoming message.", (Throwable)jmse);
        }
    }
    
    protected static Object lookup(final Context ctx, final String name) throws NamingException {
        try {
            return ctx.lookup(name);
        }
        catch (NameNotFoundException e) {
            JMSSink.logger.error("Could not find name [" + name + "].");
            throw e;
        }
    }
    
    static void usage(final String msg) {
        System.err.println(msg);
        System.err.println("Usage: java " + JMSSink.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password configFile");
        System.exit(1);
    }
    
    static {
        JMSSink.logger = Logger.getLogger(JMSSink.class);
    }
}
