// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.RootLogger;
import java.util.Enumeration;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.NOPLoggerRepository;
import org.apache.log4j.spi.LoggerRepository;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.spi.RepositorySelector;

public class LogManager
{
    public static final String DEFAULT_CONFIGURATION_FILE = "log4j.properties";
    static final String DEFAULT_XML_CONFIGURATION_FILE = "log4j.xml";
    public static final String DEFAULT_CONFIGURATION_KEY = "log4j.configuration";
    public static final String CONFIGURATOR_CLASS_KEY = "log4j.configuratorClass";
    public static final String DEFAULT_INIT_OVERRIDE_KEY = "log4j.defaultInitOverride";
    private static Object guard;
    private static RepositorySelector repositorySelector;
    
    public static void setRepositorySelector(final RepositorySelector selector, final Object guard) throws IllegalArgumentException {
        if (LogManager.guard != null && LogManager.guard != guard) {
            throw new IllegalArgumentException("Attempted to reset the LoggerFactory without possessing the guard.");
        }
        if (selector == null) {
            throw new IllegalArgumentException("RepositorySelector must be non-null.");
        }
        LogManager.guard = guard;
        LogManager.repositorySelector = selector;
    }
    
    private static boolean isLikelySafeScenario(final Exception ex) {
        final StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        final String msg = stringWriter.toString();
        return msg.indexOf("org.apache.catalina.loader.WebappClassLoader.stop") != -1;
    }
    
    public static LoggerRepository getLoggerRepository() {
        if (LogManager.repositorySelector == null) {
            LogManager.repositorySelector = new DefaultRepositorySelector(new NOPLoggerRepository());
            LogManager.guard = null;
            final Exception ex = new IllegalStateException("Class invariant violation");
            final String msg = "log4j called after unloading, see http://logging.apache.org/log4j/1.2/faq.html#unload.";
            if (isLikelySafeScenario(ex)) {
                LogLog.debug(msg, ex);
            }
            else {
                LogLog.error(msg, ex);
            }
        }
        return LogManager.repositorySelector.getLoggerRepository();
    }
    
    public static Logger getRootLogger() {
        return getLoggerRepository().getRootLogger();
    }
    
    public static Logger getLogger(final String name) {
        return getLoggerRepository().getLogger(name);
    }
    
    public static Logger getLogger(final Class clazz) {
        return getLoggerRepository().getLogger(clazz.getName());
    }
    
    public static Logger getLogger(final String name, final LoggerFactory factory) {
        return getLoggerRepository().getLogger(name, factory);
    }
    
    public static Logger exists(final String name) {
        return getLoggerRepository().exists(name);
    }
    
    public static Enumeration getCurrentLoggers() {
        return getLoggerRepository().getCurrentLoggers();
    }
    
    public static void shutdown() {
        getLoggerRepository().shutdown();
    }
    
    public static void resetConfiguration() {
        getLoggerRepository().resetConfiguration();
    }
    
    static {
        LogManager.guard = null;
        final Hierarchy h = new Hierarchy(new RootLogger(Level.DEBUG));
        LogManager.repositorySelector = new DefaultRepositorySelector(h);
        final String override = OptionConverter.getSystemProperty("log4j.defaultInitOverride", null);
        if (override == null || "false".equalsIgnoreCase(override)) {
            final String configurationOptionStr = OptionConverter.getSystemProperty("log4j.configuration", null);
            final String configuratorClassName = OptionConverter.getSystemProperty("log4j.configuratorClass", null);
            URL url = null;
            if (configurationOptionStr == null) {
                url = Loader.getResource("log4j.xml");
                if (url == null) {
                    url = Loader.getResource("log4j.properties");
                }
            }
            else {
                try {
                    url = new URL(configurationOptionStr);
                }
                catch (MalformedURLException ex) {
                    url = Loader.getResource(configurationOptionStr);
                }
            }
            if (url != null) {
                LogLog.debug("Using URL [" + url + "] for automatic log4j configuration.");
                try {
                    OptionConverter.selectAndConfigure(url, configuratorClassName, getLoggerRepository());
                }
                catch (NoClassDefFoundError e) {
                    LogLog.warn("Error during default initialization", e);
                }
            }
            else {
                LogLog.debug("Could not find resource: [" + configurationOptionStr + "].");
            }
        }
        else {
            LogLog.debug("Default initialization of overridden by log4j.defaultInitOverrideproperty.");
        }
    }
}
