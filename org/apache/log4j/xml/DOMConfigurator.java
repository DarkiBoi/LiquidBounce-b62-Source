// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.xml;

import org.apache.log4j.spi.ThrowableRendererSupport;
import org.xml.sax.EntityResolver;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.Reader;
import javax.xml.parsers.FactoryConfigurationError;
import java.io.InputStream;
import java.net.URLConnection;
import org.xml.sax.InputSource;
import java.net.URL;
import java.io.IOException;
import org.xml.sax.SAXException;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.spi.ThrowableRenderer;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.Layout;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.Loader;
import java.io.InterruptedIOException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.log4j.helpers.LogLog;
import org.w3c.dom.Element;
import org.apache.log4j.Appender;
import org.w3c.dom.Document;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import java.util.Properties;
import java.util.Hashtable;
import org.apache.log4j.spi.Configurator;

public class DOMConfigurator implements Configurator
{
    static final String CONFIGURATION_TAG = "log4j:configuration";
    static final String OLD_CONFIGURATION_TAG = "configuration";
    static final String RENDERER_TAG = "renderer";
    private static final String THROWABLE_RENDERER_TAG = "throwableRenderer";
    static final String APPENDER_TAG = "appender";
    static final String APPENDER_REF_TAG = "appender-ref";
    static final String PARAM_TAG = "param";
    static final String LAYOUT_TAG = "layout";
    static final String CATEGORY = "category";
    static final String LOGGER = "logger";
    static final String LOGGER_REF = "logger-ref";
    static final String CATEGORY_FACTORY_TAG = "categoryFactory";
    static final String LOGGER_FACTORY_TAG = "loggerFactory";
    static final String NAME_ATTR = "name";
    static final String CLASS_ATTR = "class";
    static final String VALUE_ATTR = "value";
    static final String ROOT_TAG = "root";
    static final String ROOT_REF = "root-ref";
    static final String LEVEL_TAG = "level";
    static final String PRIORITY_TAG = "priority";
    static final String FILTER_TAG = "filter";
    static final String ERROR_HANDLER_TAG = "errorHandler";
    static final String REF_ATTR = "ref";
    static final String ADDITIVITY_ATTR = "additivity";
    static final String THRESHOLD_ATTR = "threshold";
    static final String CONFIG_DEBUG_ATTR = "configDebug";
    static final String INTERNAL_DEBUG_ATTR = "debug";
    private static final String RESET_ATTR = "reset";
    static final String RENDERING_CLASS_ATTR = "renderingClass";
    static final String RENDERED_CLASS_ATTR = "renderedClass";
    static final String EMPTY_STR = "";
    static final Class[] ONE_STRING_PARAM;
    static final String dbfKey = "javax.xml.parsers.DocumentBuilderFactory";
    Hashtable appenderBag;
    Properties props;
    LoggerRepository repository;
    protected LoggerFactory catFactory;
    
    public DOMConfigurator() {
        this.catFactory = null;
        this.appenderBag = new Hashtable();
    }
    
    protected Appender findAppenderByName(final Document doc, final String appenderName) {
        Appender appender = this.appenderBag.get(appenderName);
        if (appender != null) {
            return appender;
        }
        Element element = null;
        final NodeList list = doc.getElementsByTagName("appender");
        for (int t = 0; t < list.getLength(); ++t) {
            final Node node = list.item(t);
            final NamedNodeMap map = node.getAttributes();
            final Node attrNode = map.getNamedItem("name");
            if (appenderName.equals(attrNode.getNodeValue())) {
                element = (Element)node;
                break;
            }
        }
        if (element == null) {
            LogLog.error("No appender named [" + appenderName + "] could be found.");
            return null;
        }
        appender = this.parseAppender(element);
        if (appender != null) {
            this.appenderBag.put(appenderName, appender);
        }
        return appender;
    }
    
    protected Appender findAppenderByReference(final Element appenderRef) {
        final String appenderName = this.subst(appenderRef.getAttribute("ref"));
        final Document doc = appenderRef.getOwnerDocument();
        return this.findAppenderByName(doc, appenderName);
    }
    
    private static void parseUnrecognizedElement(final Object instance, final Element element, final Properties props) throws Exception {
        boolean recognized = false;
        if (instance instanceof UnrecognizedElementHandler) {
            recognized = ((UnrecognizedElementHandler)instance).parseUnrecognizedElement(element, props);
        }
        if (!recognized) {
            LogLog.warn("Unrecognized element " + element.getNodeName());
        }
    }
    
    private static void quietParseUnrecognizedElement(final Object instance, final Element element, final Properties props) {
        try {
            parseUnrecognizedElement(instance, element, props);
        }
        catch (Exception ex) {
            if (ex instanceof InterruptedException || ex instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Error in extension content: ", ex);
        }
    }
    
    protected Appender parseAppender(final Element appenderElement) {
        final String className = this.subst(appenderElement.getAttribute("class"));
        LogLog.debug("Class name: [" + className + ']');
        try {
            final Object instance = Loader.loadClass(className).newInstance();
            final Appender appender = (Appender)instance;
            final PropertySetter propSetter = new PropertySetter(appender);
            appender.setName(this.subst(appenderElement.getAttribute("name")));
            final NodeList children = appenderElement.getChildNodes();
            for (int length = children.getLength(), loop = 0; loop < length; ++loop) {
                final Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == 1) {
                    final Element currentElement = (Element)currentNode;
                    if (currentElement.getTagName().equals("param")) {
                        this.setParameter(currentElement, propSetter);
                    }
                    else if (currentElement.getTagName().equals("layout")) {
                        appender.setLayout(this.parseLayout(currentElement));
                    }
                    else if (currentElement.getTagName().equals("filter")) {
                        this.parseFilters(currentElement, appender);
                    }
                    else if (currentElement.getTagName().equals("errorHandler")) {
                        this.parseErrorHandler(currentElement, appender);
                    }
                    else if (currentElement.getTagName().equals("appender-ref")) {
                        final String refName = this.subst(currentElement.getAttribute("ref"));
                        if (appender instanceof AppenderAttachable) {
                            final AppenderAttachable aa = (AppenderAttachable)appender;
                            LogLog.debug("Attaching appender named [" + refName + "] to appender named [" + appender.getName() + "].");
                            aa.addAppender(this.findAppenderByReference(currentElement));
                        }
                        else {
                            LogLog.error("Requesting attachment of appender named [" + refName + "] to appender named [" + appender.getName() + "] which does not implement org.apache.log4j.spi.AppenderAttachable.");
                        }
                    }
                    else {
                        parseUnrecognizedElement(instance, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            return appender;
        }
        catch (Exception oops) {
            if (oops instanceof InterruptedException || oops instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not create an Appender. Reported error follows.", oops);
            return null;
        }
    }
    
    protected void parseErrorHandler(final Element element, final Appender appender) {
        final ErrorHandler eh = (ErrorHandler)OptionConverter.instantiateByClassName(this.subst(element.getAttribute("class")), ErrorHandler.class, null);
        if (eh != null) {
            eh.setAppender(appender);
            final PropertySetter propSetter = new PropertySetter(eh);
            final NodeList children = element.getChildNodes();
            for (int length = children.getLength(), loop = 0; loop < length; ++loop) {
                final Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == 1) {
                    final Element currentElement = (Element)currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals("param")) {
                        this.setParameter(currentElement, propSetter);
                    }
                    else if (tagName.equals("appender-ref")) {
                        eh.setBackupAppender(this.findAppenderByReference(currentElement));
                    }
                    else if (tagName.equals("logger-ref")) {
                        final String loggerName = currentElement.getAttribute("ref");
                        final Logger logger = (this.catFactory == null) ? this.repository.getLogger(loggerName) : this.repository.getLogger(loggerName, this.catFactory);
                        eh.setLogger(logger);
                    }
                    else if (tagName.equals("root-ref")) {
                        final Logger root = this.repository.getRootLogger();
                        eh.setLogger(root);
                    }
                    else {
                        quietParseUnrecognizedElement(eh, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            appender.setErrorHandler(eh);
        }
    }
    
    protected void parseFilters(final Element element, final Appender appender) {
        final String clazz = this.subst(element.getAttribute("class"));
        final Filter filter = (Filter)OptionConverter.instantiateByClassName(clazz, Filter.class, null);
        if (filter != null) {
            final PropertySetter propSetter = new PropertySetter(filter);
            final NodeList children = element.getChildNodes();
            for (int length = children.getLength(), loop = 0; loop < length; ++loop) {
                final Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == 1) {
                    final Element currentElement = (Element)currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals("param")) {
                        this.setParameter(currentElement, propSetter);
                    }
                    else {
                        quietParseUnrecognizedElement(filter, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            LogLog.debug("Adding filter of type [" + filter.getClass() + "] to appender named [" + appender.getName() + "].");
            appender.addFilter(filter);
        }
    }
    
    protected void parseCategory(final Element loggerElement) {
        final String catName = this.subst(loggerElement.getAttribute("name"));
        final String className = this.subst(loggerElement.getAttribute("class"));
        Logger cat;
        if ("".equals(className)) {
            LogLog.debug("Retreiving an instance of org.apache.log4j.Logger.");
            cat = ((this.catFactory == null) ? this.repository.getLogger(catName) : this.repository.getLogger(catName, this.catFactory));
        }
        else {
            LogLog.debug("Desired logger sub-class: [" + className + ']');
            try {
                final Class clazz = Loader.loadClass(className);
                final Method getInstanceMethod = clazz.getMethod("getLogger", (Class[])DOMConfigurator.ONE_STRING_PARAM);
                cat = (Logger)getInstanceMethod.invoke(null, catName);
            }
            catch (InvocationTargetException oops) {
                if (oops.getTargetException() instanceof InterruptedException || oops.getTargetException() instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("Could not retrieve category [" + catName + "]. Reported error follows.", oops);
                return;
            }
            catch (Exception oops2) {
                LogLog.error("Could not retrieve category [" + catName + "]. Reported error follows.", oops2);
                return;
            }
        }
        synchronized (cat) {
            final boolean additivity = OptionConverter.toBoolean(this.subst(loggerElement.getAttribute("additivity")), true);
            LogLog.debug("Setting [" + cat.getName() + "] additivity to [" + additivity + "].");
            cat.setAdditivity(additivity);
            this.parseChildrenOfLoggerElement(loggerElement, cat, false);
        }
    }
    
    protected void parseCategoryFactory(final Element factoryElement) {
        final String className = this.subst(factoryElement.getAttribute("class"));
        if ("".equals(className)) {
            LogLog.error("Category Factory tag class attribute not found.");
            LogLog.debug("No Category Factory configured.");
        }
        else {
            LogLog.debug("Desired category factory: [" + className + ']');
            final Object factory = OptionConverter.instantiateByClassName(className, LoggerFactory.class, null);
            if (factory instanceof LoggerFactory) {
                this.catFactory = (LoggerFactory)factory;
            }
            else {
                LogLog.error("Category Factory class " + className + " does not implement org.apache.log4j.LoggerFactory");
            }
            final PropertySetter propSetter = new PropertySetter(factory);
            Element currentElement = null;
            Node currentNode = null;
            final NodeList children = factoryElement.getChildNodes();
            for (int length = children.getLength(), loop = 0; loop < length; ++loop) {
                currentNode = children.item(loop);
                if (currentNode.getNodeType() == 1) {
                    currentElement = (Element)currentNode;
                    if (currentElement.getTagName().equals("param")) {
                        this.setParameter(currentElement, propSetter);
                    }
                    else {
                        quietParseUnrecognizedElement(factory, currentElement, this.props);
                    }
                }
            }
        }
    }
    
    protected void parseRoot(final Element rootElement) {
        final Logger root = this.repository.getRootLogger();
        synchronized (root) {
            this.parseChildrenOfLoggerElement(rootElement, root, true);
        }
    }
    
    protected void parseChildrenOfLoggerElement(final Element catElement, final Logger cat, final boolean isRoot) {
        final PropertySetter propSetter = new PropertySetter(cat);
        cat.removeAllAppenders();
        final NodeList children = catElement.getChildNodes();
        for (int length = children.getLength(), loop = 0; loop < length; ++loop) {
            final Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == 1) {
                final Element currentElement = (Element)currentNode;
                final String tagName = currentElement.getTagName();
                if (tagName.equals("appender-ref")) {
                    final Element appenderRef = (Element)currentNode;
                    final Appender appender = this.findAppenderByReference(appenderRef);
                    final String refName = this.subst(appenderRef.getAttribute("ref"));
                    if (appender != null) {
                        LogLog.debug("Adding appender named [" + refName + "] to category [" + cat.getName() + "].");
                    }
                    else {
                        LogLog.debug("Appender named [" + refName + "] not found.");
                    }
                    cat.addAppender(appender);
                }
                else if (tagName.equals("level")) {
                    this.parseLevel(currentElement, cat, isRoot);
                }
                else if (tagName.equals("priority")) {
                    this.parseLevel(currentElement, cat, isRoot);
                }
                else if (tagName.equals("param")) {
                    this.setParameter(currentElement, propSetter);
                }
                else {
                    quietParseUnrecognizedElement(cat, currentElement, this.props);
                }
            }
        }
        propSetter.activate();
    }
    
    protected Layout parseLayout(final Element layout_element) {
        final String className = this.subst(layout_element.getAttribute("class"));
        LogLog.debug("Parsing layout of class: \"" + className + "\"");
        try {
            final Object instance = Loader.loadClass(className).newInstance();
            final Layout layout = (Layout)instance;
            final PropertySetter propSetter = new PropertySetter(layout);
            final NodeList params = layout_element.getChildNodes();
            for (int length = params.getLength(), loop = 0; loop < length; ++loop) {
                final Node currentNode = params.item(loop);
                if (currentNode.getNodeType() == 1) {
                    final Element currentElement = (Element)currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals("param")) {
                        this.setParameter(currentElement, propSetter);
                    }
                    else {
                        parseUnrecognizedElement(instance, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            return layout;
        }
        catch (Exception oops) {
            if (oops instanceof InterruptedException || oops instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not create the Layout. Reported error follows.", oops);
            return null;
        }
    }
    
    protected void parseRenderer(final Element element) {
        final String renderingClass = this.subst(element.getAttribute("renderingClass"));
        final String renderedClass = this.subst(element.getAttribute("renderedClass"));
        if (this.repository instanceof RendererSupport) {
            RendererMap.addRenderer((RendererSupport)this.repository, renderedClass, renderingClass);
        }
    }
    
    protected ThrowableRenderer parseThrowableRenderer(final Element element) {
        final String className = this.subst(element.getAttribute("class"));
        LogLog.debug("Parsing throwableRenderer of class: \"" + className + "\"");
        try {
            final Object instance = Loader.loadClass(className).newInstance();
            final ThrowableRenderer tr = (ThrowableRenderer)instance;
            final PropertySetter propSetter = new PropertySetter(tr);
            final NodeList params = element.getChildNodes();
            for (int length = params.getLength(), loop = 0; loop < length; ++loop) {
                final Node currentNode = params.item(loop);
                if (currentNode.getNodeType() == 1) {
                    final Element currentElement = (Element)currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals("param")) {
                        this.setParameter(currentElement, propSetter);
                    }
                    else {
                        parseUnrecognizedElement(instance, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            return tr;
        }
        catch (Exception oops) {
            if (oops instanceof InterruptedException || oops instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not create the ThrowableRenderer. Reported error follows.", oops);
            return null;
        }
    }
    
    protected void parseLevel(final Element element, final Logger logger, final boolean isRoot) {
        String catName = logger.getName();
        if (isRoot) {
            catName = "root";
        }
        final String priStr = this.subst(element.getAttribute("value"));
        LogLog.debug("Level value for " + catName + " is  [" + priStr + "].");
        if ("inherited".equalsIgnoreCase(priStr) || "null".equalsIgnoreCase(priStr)) {
            if (isRoot) {
                LogLog.error("Root level cannot be inherited. Ignoring directive.");
            }
            else {
                logger.setLevel(null);
            }
        }
        else {
            final String className = this.subst(element.getAttribute("class"));
            if ("".equals(className)) {
                logger.setLevel(OptionConverter.toLevel(priStr, Level.DEBUG));
            }
            else {
                LogLog.debug("Desired Level sub-class: [" + className + ']');
                try {
                    final Class clazz = Loader.loadClass(className);
                    final Method toLevelMethod = clazz.getMethod("toLevel", (Class[])DOMConfigurator.ONE_STRING_PARAM);
                    final Level pri = (Level)toLevelMethod.invoke(null, priStr);
                    logger.setLevel(pri);
                }
                catch (Exception oops) {
                    if (oops instanceof InterruptedException || oops instanceof InterruptedIOException) {
                        Thread.currentThread().interrupt();
                    }
                    LogLog.error("Could not create level [" + priStr + "]. Reported error follows.", oops);
                    return;
                }
            }
        }
        LogLog.debug(catName + " level set to " + logger.getLevel());
    }
    
    protected void setParameter(final Element elem, final PropertySetter propSetter) {
        final String name = this.subst(elem.getAttribute("name"));
        String value = elem.getAttribute("value");
        value = this.subst(OptionConverter.convertSpecialChars(value));
        propSetter.setProperty(name, value);
    }
    
    public static void configure(final Element element) {
        final DOMConfigurator configurator = new DOMConfigurator();
        configurator.doConfigure(element, LogManager.getLoggerRepository());
    }
    
    public static void configureAndWatch(final String configFilename) {
        configureAndWatch(configFilename, 60000L);
    }
    
    public static void configureAndWatch(final String configFilename, final long delay) {
        final XMLWatchdog xdog = new XMLWatchdog(configFilename);
        xdog.setDelay(delay);
        xdog.start();
    }
    
    public void doConfigure(final String filename, final LoggerRepository repository) {
        final ParseAction action = new ParseAction() {
            public Document parse(final DocumentBuilder parser) throws SAXException, IOException {
                return parser.parse(new File(filename));
            }
            
            public String toString() {
                return "file [" + filename + "]";
            }
        };
        this.doConfigure(action, repository);
    }
    
    public void doConfigure(final URL url, final LoggerRepository repository) {
        final ParseAction action = new ParseAction() {
            public Document parse(final DocumentBuilder parser) throws SAXException, IOException {
                final URLConnection uConn = url.openConnection();
                uConn.setUseCaches(false);
                final InputStream stream = uConn.getInputStream();
                try {
                    final InputSource src = new InputSource(stream);
                    src.setSystemId(url.toString());
                    return parser.parse(src);
                }
                finally {
                    stream.close();
                }
            }
            
            public String toString() {
                return "url [" + url.toString() + "]";
            }
        };
        this.doConfigure(action, repository);
    }
    
    public void doConfigure(final InputStream inputStream, final LoggerRepository repository) throws FactoryConfigurationError {
        final ParseAction action = new ParseAction() {
            public Document parse(final DocumentBuilder parser) throws SAXException, IOException {
                final InputSource inputSource = new InputSource(inputStream);
                inputSource.setSystemId("dummy://log4j.dtd");
                return parser.parse(inputSource);
            }
            
            public String toString() {
                return "input stream [" + inputStream.toString() + "]";
            }
        };
        this.doConfigure(action, repository);
    }
    
    public void doConfigure(final Reader reader, final LoggerRepository repository) throws FactoryConfigurationError {
        final ParseAction action = new ParseAction() {
            public Document parse(final DocumentBuilder parser) throws SAXException, IOException {
                final InputSource inputSource = new InputSource(reader);
                inputSource.setSystemId("dummy://log4j.dtd");
                return parser.parse(inputSource);
            }
            
            public String toString() {
                return "reader [" + reader.toString() + "]";
            }
        };
        this.doConfigure(action, repository);
    }
    
    protected void doConfigure(final InputSource inputSource, final LoggerRepository repository) throws FactoryConfigurationError {
        if (inputSource.getSystemId() == null) {
            inputSource.setSystemId("dummy://log4j.dtd");
        }
        final ParseAction action = new ParseAction() {
            public Document parse(final DocumentBuilder parser) throws SAXException, IOException {
                return parser.parse(inputSource);
            }
            
            public String toString() {
                return "input source [" + inputSource.toString() + "]";
            }
        };
        this.doConfigure(action, repository);
    }
    
    private final void doConfigure(final ParseAction action, final LoggerRepository repository) throws FactoryConfigurationError {
        DocumentBuilderFactory dbf = null;
        this.repository = repository;
        try {
            LogLog.debug("System property is :" + OptionConverter.getSystemProperty("javax.xml.parsers.DocumentBuilderFactory", null));
            dbf = DocumentBuilderFactory.newInstance();
            LogLog.debug("Standard DocumentBuilderFactory search succeded.");
            LogLog.debug("DocumentBuilderFactory is: " + dbf.getClass().getName());
        }
        catch (FactoryConfigurationError fce) {
            final Exception e = fce.getException();
            LogLog.debug("Could not instantiate a DocumentBuilderFactory.", e);
            throw fce;
        }
        try {
            dbf.setValidating(true);
            final DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            docBuilder.setErrorHandler(new SAXErrorHandler());
            docBuilder.setEntityResolver(new Log4jEntityResolver());
            final Document doc = action.parse(docBuilder);
            this.parse(doc.getDocumentElement());
        }
        catch (Exception e2) {
            if (e2 instanceof InterruptedException || e2 instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not parse " + action.toString() + ".", e2);
        }
    }
    
    public void doConfigure(final Element element, final LoggerRepository repository) {
        this.repository = repository;
        this.parse(element);
    }
    
    public static void configure(final String filename) throws FactoryConfigurationError {
        new DOMConfigurator().doConfigure(filename, LogManager.getLoggerRepository());
    }
    
    public static void configure(final URL url) throws FactoryConfigurationError {
        new DOMConfigurator().doConfigure(url, LogManager.getLoggerRepository());
    }
    
    protected void parse(final Element element) {
        final String rootElementName = element.getTagName();
        if (!rootElementName.equals("log4j:configuration")) {
            if (!rootElementName.equals("configuration")) {
                LogLog.error("DOM element is - not a <log4j:configuration> element.");
                return;
            }
            LogLog.warn("The <configuration> element has been deprecated.");
            LogLog.warn("Use the <log4j:configuration> element instead.");
        }
        final String debugAttrib = this.subst(element.getAttribute("debug"));
        LogLog.debug("debug attribute= \"" + debugAttrib + "\".");
        if (!debugAttrib.equals("") && !debugAttrib.equals("null")) {
            LogLog.setInternalDebugging(OptionConverter.toBoolean(debugAttrib, true));
        }
        else {
            LogLog.debug("Ignoring debug attribute.");
        }
        final String resetAttrib = this.subst(element.getAttribute("reset"));
        LogLog.debug("reset attribute= \"" + resetAttrib + "\".");
        if (!"".equals(resetAttrib) && OptionConverter.toBoolean(resetAttrib, false)) {
            this.repository.resetConfiguration();
        }
        final String confDebug = this.subst(element.getAttribute("configDebug"));
        if (!confDebug.equals("") && !confDebug.equals("null")) {
            LogLog.warn("The \"configDebug\" attribute is deprecated.");
            LogLog.warn("Use the \"debug\" attribute instead.");
            LogLog.setInternalDebugging(OptionConverter.toBoolean(confDebug, true));
        }
        final String thresholdStr = this.subst(element.getAttribute("threshold"));
        LogLog.debug("Threshold =\"" + thresholdStr + "\".");
        if (!"".equals(thresholdStr) && !"null".equals(thresholdStr)) {
            this.repository.setThreshold(thresholdStr);
        }
        String tagName = null;
        Element currentElement = null;
        Node currentNode = null;
        final NodeList children = element.getChildNodes();
        final int length = children.getLength();
        for (int loop = 0; loop < length; ++loop) {
            currentNode = children.item(loop);
            if (currentNode.getNodeType() == 1) {
                currentElement = (Element)currentNode;
                tagName = currentElement.getTagName();
                if (tagName.equals("categoryFactory") || tagName.equals("loggerFactory")) {
                    this.parseCategoryFactory(currentElement);
                }
            }
        }
        for (int loop = 0; loop < length; ++loop) {
            currentNode = children.item(loop);
            if (currentNode.getNodeType() == 1) {
                currentElement = (Element)currentNode;
                tagName = currentElement.getTagName();
                if (tagName.equals("category") || tagName.equals("logger")) {
                    this.parseCategory(currentElement);
                }
                else if (tagName.equals("root")) {
                    this.parseRoot(currentElement);
                }
                else if (tagName.equals("renderer")) {
                    this.parseRenderer(currentElement);
                }
                else if (tagName.equals("throwableRenderer")) {
                    if (this.repository instanceof ThrowableRendererSupport) {
                        final ThrowableRenderer tr = this.parseThrowableRenderer(currentElement);
                        if (tr != null) {
                            ((ThrowableRendererSupport)this.repository).setThrowableRenderer(tr);
                        }
                    }
                }
                else if (!tagName.equals("appender") && !tagName.equals("categoryFactory") && !tagName.equals("loggerFactory")) {
                    quietParseUnrecognizedElement(this.repository, currentElement, this.props);
                }
            }
        }
    }
    
    protected String subst(final String value) {
        return subst(value, this.props);
    }
    
    public static String subst(final String value, final Properties props) {
        try {
            return OptionConverter.substVars(value, props);
        }
        catch (IllegalArgumentException e) {
            LogLog.warn("Could not perform variable substitution.", e);
            return value;
        }
    }
    
    public static void setParameter(final Element elem, final PropertySetter propSetter, final Properties props) {
        final String name = subst(elem.getAttribute("name"), props);
        String value = elem.getAttribute("value");
        value = subst(OptionConverter.convertSpecialChars(value), props);
        propSetter.setProperty(name, value);
    }
    
    public static Object parseElement(final Element element, final Properties props, final Class expectedClass) throws Exception {
        final String clazz = subst(element.getAttribute("class"), props);
        final Object instance = OptionConverter.instantiateByClassName(clazz, expectedClass, null);
        if (instance != null) {
            final PropertySetter propSetter = new PropertySetter(instance);
            final NodeList children = element.getChildNodes();
            for (int length = children.getLength(), loop = 0; loop < length; ++loop) {
                final Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == 1) {
                    final Element currentElement = (Element)currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals("param")) {
                        setParameter(currentElement, propSetter, props);
                    }
                    else {
                        parseUnrecognizedElement(instance, currentElement, props);
                    }
                }
            }
            return instance;
        }
        return null;
    }
    
    static {
        ONE_STRING_PARAM = new Class[] { String.class };
    }
    
    private interface ParseAction
    {
        Document parse(final DocumentBuilder p0) throws SAXException, IOException;
    }
}
