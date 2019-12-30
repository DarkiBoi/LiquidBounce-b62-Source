// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.regexp;

import jdk.nashorn.internal.runtime.options.Options;
import java.util.Collections;
import java.util.WeakHashMap;
import jdk.nashorn.internal.runtime.ParserException;
import java.util.Map;

public class RegExpFactory
{
    private static final RegExpFactory instance;
    private static final String JDK = "jdk";
    private static final String JONI = "joni";
    private static final Map<String, RegExp> REGEXP_CACHE;
    
    public RegExp compile(final String pattern, final String flags) throws ParserException {
        return new JdkRegExp(pattern, flags);
    }
    
    public static RegExp create(final String pattern, final String flags) {
        final String key = pattern + "/" + flags;
        RegExp regexp = RegExpFactory.REGEXP_CACHE.get(key);
        if (regexp == null) {
            regexp = RegExpFactory.instance.compile(pattern, flags);
            RegExpFactory.REGEXP_CACHE.put(key, regexp);
        }
        return regexp;
    }
    
    public static void validate(final String pattern, final String flags) throws ParserException {
        create(pattern, flags);
    }
    
    public static boolean usesJavaUtilRegex() {
        return RegExpFactory.instance != null && RegExpFactory.instance.getClass() == RegExpFactory.class;
    }
    
    static {
        REGEXP_CACHE = Collections.synchronizedMap(new WeakHashMap<String, RegExp>());
        final String stringProperty;
        final String impl = stringProperty = Options.getStringProperty("nashorn.regexp.impl", "joni");
        switch (stringProperty) {
            case "joni": {
                instance = new JoniRegExp.Factory();
                break;
            }
            case "jdk": {
                instance = new RegExpFactory();
                break;
            }
            default: {
                instance = null;
                throw new InternalError("Unsupported RegExp factory: " + impl);
            }
        }
    }
}
