// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.options;

import jdk.nashorn.internal.runtime.QuotedStringTokenizer;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.Map;
import java.util.Locale;
import java.util.TimeZone;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.text.MessageFormat;
import java.util.Collections;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Objects;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.OutputStream;
import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Permission;
import java.util.PropertyPermission;
import java.security.Permissions;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.List;
import java.io.PrintWriter;
import java.security.AccessControlContext;

public final class Options
{
    private static final AccessControlContext READ_PROPERTY_ACC_CTXT;
    private final String resource;
    private final PrintWriter err;
    private final List<String> files;
    private final List<String> arguments;
    private final TreeMap<String, Option<?>> options;
    private static final String NASHORN_ARGS_PREPEND_PROPERTY = "nashorn.args.prepend";
    private static final String NASHORN_ARGS_PROPERTY = "nashorn.args";
    private static final String MESSAGES_RESOURCE = "jdk.nashorn.internal.runtime.resources.Options";
    private static ResourceBundle bundle;
    private static HashMap<Object, Object> usage;
    private static Collection<OptionTemplate> validOptions;
    private static OptionTemplate helpOptionTemplate;
    private static OptionTemplate definePropTemplate;
    private static String definePropPrefix;
    
    private static AccessControlContext createPropertyReadAccCtxt() {
        final Permissions perms = new Permissions();
        perms.add(new PropertyPermission("nashorn.*", "read"));
        return new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, perms) });
    }
    
    public Options(final String resource) {
        this(resource, new PrintWriter(System.err, true));
    }
    
    public Options(final String resource, final PrintWriter err) {
        this.resource = resource;
        this.err = err;
        this.files = new ArrayList<String>();
        this.arguments = new ArrayList<String>();
        this.options = new TreeMap<String, Option<?>>();
        for (final OptionTemplate t : Options.validOptions) {
            if (t.getDefaultValue() != null) {
                final String v = getStringProperty(t.getKey(), null);
                if (v != null) {
                    this.set(t.getKey(), createOption(t, v));
                }
                else {
                    if (t.getDefaultValue() == null) {
                        continue;
                    }
                    this.set(t.getKey(), createOption(t, t.getDefaultValue()));
                }
            }
        }
    }
    
    public String getResource() {
        return this.resource;
    }
    
    @Override
    public String toString() {
        return this.options.toString();
    }
    
    private static void checkPropertyName(final String name) {
        if (!Objects.requireNonNull(name).startsWith("nashorn.")) {
            throw new IllegalArgumentException(name);
        }
    }
    
    public static boolean getBooleanProperty(final String name, final Boolean defValue) {
        checkPropertyName(name);
        return AccessController.doPrivileged((PrivilegedAction<Boolean>)new PrivilegedAction<Boolean>() {
            @Override
            public Boolean run() {
                try {
                    final String property = System.getProperty(name);
                    if (property == null && defValue != null) {
                        return defValue;
                    }
                    return property != null && !"false".equalsIgnoreCase(property);
                }
                catch (SecurityException e) {
                    return false;
                }
            }
        }, Options.READ_PROPERTY_ACC_CTXT);
    }
    
    public static boolean getBooleanProperty(final String name) {
        return getBooleanProperty(name, null);
    }
    
    public static String getStringProperty(final String name, final String defValue) {
        checkPropertyName(name);
        return AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction<String>() {
            @Override
            public String run() {
                try {
                    return System.getProperty(name, defValue);
                }
                catch (SecurityException e) {
                    return defValue;
                }
            }
        }, Options.READ_PROPERTY_ACC_CTXT);
    }
    
    public static int getIntProperty(final String name, final int defValue) {
        checkPropertyName(name);
        return AccessController.doPrivileged((PrivilegedAction<Integer>)new PrivilegedAction<Integer>() {
            @Override
            public Integer run() {
                try {
                    return Integer.getInteger(name, defValue);
                }
                catch (SecurityException e) {
                    return defValue;
                }
            }
        }, Options.READ_PROPERTY_ACC_CTXT);
    }
    
    public Option<?> get(final String key) {
        return this.options.get(this.key(key));
    }
    
    public boolean getBoolean(final String key) {
        final Option<?> option = this.get(key);
        return option != null && (boolean)option.getValue();
    }
    
    public int getInteger(final String key) {
        final Option<?> option = this.get(key);
        return (int)((option != null) ? option.getValue() : 0);
    }
    
    public String getString(final String key) {
        final Option<?> option = this.get(key);
        if (option != null) {
            final String value = (String)option.getValue();
            if (value != null) {
                return value.intern();
            }
        }
        return null;
    }
    
    public void set(final String key, final Option<?> option) {
        this.options.put(this.key(key), option);
    }
    
    public void set(final String key, final boolean option) {
        this.set(key, new Option<Object>((Object)option));
    }
    
    public void set(final String key, final String option) {
        this.set(key, new Option<Object>(option));
    }
    
    public List<String> getArguments() {
        return Collections.unmodifiableList((List<? extends String>)this.arguments);
    }
    
    public List<String> getFiles() {
        return Collections.unmodifiableList((List<? extends String>)this.files);
    }
    
    public static Collection<OptionTemplate> getValidOptions() {
        return Collections.unmodifiableCollection((Collection<? extends OptionTemplate>)Options.validOptions);
    }
    
    private String key(final String shortKey) {
        String key;
        for (key = shortKey; key.startsWith("-"); key = key.substring(1, key.length())) {}
        key = key.replace("-", ".");
        final String keyPrefix = this.resource + ".option.";
        if (key.startsWith(keyPrefix)) {
            return key;
        }
        return keyPrefix + key;
    }
    
    static String getMsg(final String msgId, final String... args) {
        try {
            final String msg = Options.bundle.getString(msgId);
            if (args.length == 0) {
                return msg;
            }
            return new MessageFormat(msg).format(args);
        }
        catch (MissingResourceException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public void displayHelp(final IllegalArgumentException e) {
        if (e instanceof IllegalOptionException) {
            final OptionTemplate template = ((IllegalOptionException)e).getTemplate();
            if (template.isXHelp()) {
                this.displayHelp(true);
            }
            else {
                this.err.println(((IllegalOptionException)e).getTemplate());
            }
            return;
        }
        if (e != null && e.getMessage() != null) {
            this.err.println(getMsg("option.error.invalid.option", e.getMessage(), Options.helpOptionTemplate.getShortName(), Options.helpOptionTemplate.getName()));
            this.err.println();
            return;
        }
        this.displayHelp(false);
    }
    
    public void displayHelp(final boolean extended) {
        for (final OptionTemplate t : Options.validOptions) {
            if ((extended || !t.isUndocumented()) && t.getResource().equals(this.resource)) {
                this.err.println(t);
                this.err.println();
            }
        }
    }
    
    public void process(final String[] args) {
        final LinkedList<String> argList = new LinkedList<String>();
        addSystemProperties("nashorn.args.prepend", argList);
        this.processArgList(argList);
        assert argList.isEmpty();
        Collections.addAll(argList, args);
        this.processArgList(argList);
        assert argList.isEmpty();
        addSystemProperties("nashorn.args", argList);
        this.processArgList(argList);
        assert argList.isEmpty();
    }
    
    private void processArgList(final LinkedList<String> argList) {
        while (!argList.isEmpty()) {
            final String arg = argList.remove(0);
            if (arg.isEmpty()) {
                continue;
            }
            if ("--".equals(arg)) {
                this.arguments.addAll(argList);
                argList.clear();
            }
            else if (!arg.startsWith("-") || arg.length() == 1) {
                this.files.add(arg);
            }
            else if (arg.startsWith(Options.definePropPrefix)) {
                final String value = arg.substring(Options.definePropPrefix.length());
                final int eq = value.indexOf(61);
                if (eq != -1) {
                    System.setProperty(value.substring(0, eq), value.substring(eq + 1));
                }
                else {
                    if (value.isEmpty()) {
                        throw new IllegalOptionException(Options.definePropTemplate);
                    }
                    System.setProperty(value, "");
                }
            }
            else {
                final ParsedArg parg = new ParsedArg(arg);
                if (parg.template.isValueNextArg()) {
                    if (argList.isEmpty()) {
                        throw new IllegalOptionException(parg.template);
                    }
                    parg.value = argList.remove(0);
                }
                if (parg.template.isHelp()) {
                    if (!argList.isEmpty()) {
                        try {
                            final OptionTemplate t = new ParsedArg(argList.get(0)).template;
                            throw new IllegalOptionException(t);
                        }
                        catch (IllegalArgumentException e) {
                            throw e;
                        }
                    }
                    throw new IllegalArgumentException();
                }
                if (parg.template.isXHelp()) {
                    throw new IllegalOptionException(parg.template);
                }
                this.set(parg.template.getKey(), createOption(parg.template, parg.value));
                if (parg.template.getDependency() == null) {
                    continue;
                }
                argList.addFirst(parg.template.getDependency());
            }
        }
    }
    
    private static void addSystemProperties(final String sysPropName, final List<String> argList) {
        final String sysArgs = getStringProperty(sysPropName, null);
        if (sysArgs != null) {
            final StringTokenizer st = new StringTokenizer(sysArgs);
            while (st.hasMoreTokens()) {
                argList.add(st.nextToken());
            }
        }
    }
    
    public OptionTemplate getOptionTemplateByKey(final String shortKey) {
        final String fullKey = this.key(shortKey);
        for (final OptionTemplate t : Options.validOptions) {
            if (t.getKey().equals(fullKey)) {
                return t;
            }
        }
        throw new IllegalArgumentException(shortKey);
    }
    
    private static OptionTemplate getOptionTemplateByName(final String name) {
        for (final OptionTemplate t : Options.validOptions) {
            if (t.nameMatches(name)) {
                return t;
            }
        }
        return null;
    }
    
    private static Option<?> createOption(final OptionTemplate t, final String value) {
        final String type = t.getType();
        switch (type) {
            case "string": {
                return new Option<Object>(value);
            }
            case "timezone": {
                return new Option<Object>(TimeZone.getTimeZone(value));
            }
            case "locale": {
                return new Option<Object>(Locale.forLanguageTag(value));
            }
            case "keyvalues": {
                return new KeyValueOption(value);
            }
            case "log": {
                return new LoggingOption(value);
            }
            case "boolean": {
                return new Option<Object>((Object)(value != null && Boolean.parseBoolean(value)));
            }
            case "integer": {
                try {
                    return new Option<Object>((Object)((value == null) ? 0 : Integer.parseInt(value)));
                }
                catch (NumberFormatException nfe) {
                    throw new IllegalOptionException(t);
                }
            }
            case "properties": {
                initProps(new KeyValueOption(value));
                return null;
            }
            default: {
                throw new IllegalArgumentException(value);
            }
        }
    }
    
    private static void initProps(final KeyValueOption kv) {
        for (final Map.Entry<String, String> entry : kv.getValues().entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
    }
    
    static {
        READ_PROPERTY_ACC_CTXT = createPropertyReadAccCtxt();
        Options.bundle = ResourceBundle.getBundle("jdk.nashorn.internal.runtime.resources.Options", Locale.getDefault());
        Options.validOptions = new TreeSet<OptionTemplate>();
        Options.usage = new HashMap<Object, Object>();
        final Enumeration<String> keys = Options.bundle.getKeys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            final StringTokenizer st = new StringTokenizer(key, ".");
            String resource = null;
            String type = null;
            if (st.countTokens() > 0) {
                resource = st.nextToken();
            }
            if (st.countTokens() > 0) {
                type = st.nextToken();
            }
            if ("option".equals(type)) {
                String helpKey = null;
                String xhelpKey = null;
                String definePropKey = null;
                try {
                    helpKey = Options.bundle.getString(resource + ".options.help.key");
                    xhelpKey = Options.bundle.getString(resource + ".options.xhelp.key");
                    definePropKey = Options.bundle.getString(resource + ".options.D.key");
                }
                catch (MissingResourceException ex) {}
                final boolean isHelp = key.equals(helpKey);
                final boolean isXHelp = key.equals(xhelpKey);
                final OptionTemplate t = new OptionTemplate(resource, key, Options.bundle.getString(key), isHelp, isXHelp);
                Options.validOptions.add(t);
                if (isHelp) {
                    Options.helpOptionTemplate = t;
                }
                if (!key.equals(definePropKey)) {
                    continue;
                }
                Options.definePropPrefix = t.getName();
                Options.definePropTemplate = t;
            }
            else {
                if (resource == null || !"options".equals(type)) {
                    continue;
                }
                Options.usage.put(resource, Options.bundle.getObject(key));
            }
        }
    }
    
    private static class IllegalOptionException extends IllegalArgumentException
    {
        private final OptionTemplate template;
        
        IllegalOptionException(final OptionTemplate t) {
            this.template = t;
        }
        
        OptionTemplate getTemplate() {
            return this.template;
        }
    }
    
    private static class ParsedArg
    {
        OptionTemplate template;
        String value;
        
        ParsedArg(final String argument) {
            final QuotedStringTokenizer st = new QuotedStringTokenizer(argument, "=");
            if (!st.hasMoreTokens()) {
                throw new IllegalArgumentException();
            }
            final String token = st.nextToken();
            this.template = getOptionTemplateByName(token);
            if (this.template == null) {
                throw new IllegalArgumentException(argument);
            }
            this.value = "";
            if (st.hasMoreTokens()) {
                while (st.hasMoreTokens()) {
                    this.value += st.nextToken();
                    if (st.hasMoreTokens()) {
                        this.value += ':';
                    }
                }
            }
            else if ("boolean".equals(this.template.getType())) {
                this.value = "true";
            }
        }
    }
}
