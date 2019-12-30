// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import java.util.Collection;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Method;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import java.util.List;
import java.util.Map;

public final class PatternParser
{
    private static final char ESCAPE_CHAR = '%';
    private static final int LITERAL_STATE = 0;
    private static final int CONVERTER_STATE = 1;
    private static final int DOT_STATE = 3;
    private static final int MIN_STATE = 4;
    private static final int MAX_STATE = 5;
    private static final Map PATTERN_LAYOUT_RULES;
    private static final Map FILENAME_PATTERN_RULES;
    
    private PatternParser() {
    }
    
    public static Map getPatternLayoutRules() {
        return PatternParser.PATTERN_LAYOUT_RULES;
    }
    
    public static Map getFileNamePatternRules() {
        return PatternParser.FILENAME_PATTERN_RULES;
    }
    
    private static int extractConverter(final char lastChar, final String pattern, int i, final StringBuffer convBuf, final StringBuffer currentLiteral) {
        convBuf.setLength(0);
        if (!Character.isUnicodeIdentifierStart(lastChar)) {
            return i;
        }
        convBuf.append(lastChar);
        while (i < pattern.length() && Character.isUnicodeIdentifierPart(pattern.charAt(i))) {
            convBuf.append(pattern.charAt(i));
            currentLiteral.append(pattern.charAt(i));
            ++i;
        }
        return i;
    }
    
    private static int extractOptions(final String pattern, int i, final List options) {
        while (i < pattern.length() && pattern.charAt(i) == '{') {
            final int end = pattern.indexOf(125, i);
            if (end == -1) {
                break;
            }
            final String r = pattern.substring(i + 1, end);
            options.add(r);
            i = end + 1;
        }
        return i;
    }
    
    public static void parse(final String pattern, final List patternConverters, final List formattingInfos, final Map converterRegistry, final Map rules) {
        if (pattern == null) {
            throw new NullPointerException("pattern");
        }
        final StringBuffer currentLiteral = new StringBuffer(32);
        final int patternLength = pattern.length();
        int state = 0;
        int i = 0;
        FormattingInfo formattingInfo = FormattingInfo.getDefault();
        while (i < patternLength) {
            final char c = pattern.charAt(i++);
            switch (state) {
                case 0: {
                    if (i == patternLength) {
                        currentLiteral.append(c);
                        continue;
                    }
                    if (c != '%') {
                        currentLiteral.append(c);
                        continue;
                    }
                    switch (pattern.charAt(i)) {
                        case '%': {
                            currentLiteral.append(c);
                            ++i;
                            continue;
                        }
                        default: {
                            if (currentLiteral.length() != 0) {
                                patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
                                formattingInfos.add(FormattingInfo.getDefault());
                            }
                            currentLiteral.setLength(0);
                            currentLiteral.append(c);
                            state = 1;
                            formattingInfo = FormattingInfo.getDefault();
                            continue;
                        }
                    }
                    break;
                }
                case 1: {
                    currentLiteral.append(c);
                    switch (c) {
                        case '-': {
                            formattingInfo = new FormattingInfo(true, formattingInfo.getMinLength(), formattingInfo.getMaxLength());
                            continue;
                        }
                        case '.': {
                            state = 3;
                            continue;
                        }
                        default: {
                            if (c >= '0' && c <= '9') {
                                formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), c - '0', formattingInfo.getMaxLength());
                                state = 4;
                                continue;
                            }
                            i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, converterRegistry, rules, patternConverters, formattingInfos);
                            state = 0;
                            formattingInfo = FormattingInfo.getDefault();
                            currentLiteral.setLength(0);
                            continue;
                        }
                    }
                    break;
                }
                case 4: {
                    currentLiteral.append(c);
                    if (c >= '0' && c <= '9') {
                        formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength() * 10 + (c - '0'), formattingInfo.getMaxLength());
                        continue;
                    }
                    if (c == '.') {
                        state = 3;
                        continue;
                    }
                    i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, converterRegistry, rules, patternConverters, formattingInfos);
                    state = 0;
                    formattingInfo = FormattingInfo.getDefault();
                    currentLiteral.setLength(0);
                    continue;
                }
                case 3: {
                    currentLiteral.append(c);
                    if (c >= '0' && c <= '9') {
                        formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), c - '0');
                        state = 5;
                        continue;
                    }
                    LogLog.error("Error occured in position " + i + ".\n Was expecting digit, instead got char \"" + c + "\".");
                    state = 0;
                    continue;
                }
                case 5: {
                    currentLiteral.append(c);
                    if (c >= '0' && c <= '9') {
                        formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength() * 10 + (c - '0'));
                        continue;
                    }
                    i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, converterRegistry, rules, patternConverters, formattingInfos);
                    state = 0;
                    formattingInfo = FormattingInfo.getDefault();
                    currentLiteral.setLength(0);
                    continue;
                }
            }
        }
        if (currentLiteral.length() != 0) {
            patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
            formattingInfos.add(FormattingInfo.getDefault());
        }
    }
    
    private static PatternConverter createConverter(final String converterId, final StringBuffer currentLiteral, final Map converterRegistry, final Map rules, final List options) {
        String converterName = converterId;
        Object converterObj = null;
        for (int i = converterId.length(); i > 0 && converterObj == null; --i) {
            converterName = converterName.substring(0, i);
            if (converterRegistry != null) {
                converterObj = converterRegistry.get(converterName);
            }
            if (converterObj == null && rules != null) {
                converterObj = rules.get(converterName);
            }
        }
        if (converterObj == null) {
            LogLog.error("Unrecognized format specifier [" + converterId + "]");
            return null;
        }
        Class converterClass = null;
        Label_0211: {
            if (!(converterObj instanceof Class)) {
                if (converterObj instanceof String) {
                    try {
                        converterClass = Loader.loadClass((String)converterObj);
                        break Label_0211;
                    }
                    catch (ClassNotFoundException ex) {
                        LogLog.warn("Class for conversion pattern %" + converterName + " not found", ex);
                        return null;
                    }
                }
                LogLog.warn("Bad map entry for conversion pattern %" + converterName + ".");
                return null;
            }
            converterClass = (Class)converterObj;
            try {
                final Method factory = converterClass.getMethod("newInstance", Class.forName("[Ljava.lang.String;"));
                String[] optionsArray = new String[options.size()];
                optionsArray = options.toArray(optionsArray);
                final Object newObj = factory.invoke(null, optionsArray);
                if (newObj instanceof PatternConverter) {
                    currentLiteral.delete(0, currentLiteral.length() - (converterId.length() - converterName.length()));
                    return (PatternConverter)newObj;
                }
                LogLog.warn("Class " + converterClass.getName() + " does not extend PatternConverter.");
            }
            catch (Exception ex2) {
                LogLog.error("Error creating converter for " + converterId, ex2);
                try {
                    final PatternConverter pc = converterClass.newInstance();
                    currentLiteral.delete(0, currentLiteral.length() - (converterId.length() - converterName.length()));
                    return pc;
                }
                catch (Exception ex3) {
                    LogLog.error("Error creating converter for " + converterId, ex3);
                }
            }
        }
        return null;
    }
    
    private static int finalizeConverter(final char c, final String pattern, int i, final StringBuffer currentLiteral, final FormattingInfo formattingInfo, final Map converterRegistry, final Map rules, final List patternConverters, final List formattingInfos) {
        final StringBuffer convBuf = new StringBuffer();
        i = extractConverter(c, pattern, i, convBuf, currentLiteral);
        final String converterId = convBuf.toString();
        final List options = new ArrayList();
        i = extractOptions(pattern, i, options);
        final PatternConverter pc = createConverter(converterId, currentLiteral, converterRegistry, rules, options);
        if (pc == null) {
            StringBuffer msg;
            if (converterId == null || converterId.length() == 0) {
                msg = new StringBuffer("Empty conversion specifier starting at position ");
            }
            else {
                msg = new StringBuffer("Unrecognized conversion specifier [");
                msg.append(converterId);
                msg.append("] starting at position ");
            }
            msg.append(Integer.toString(i));
            msg.append(" in conversion pattern.");
            LogLog.error(msg.toString());
            patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
            formattingInfos.add(FormattingInfo.getDefault());
        }
        else {
            patternConverters.add(pc);
            formattingInfos.add(formattingInfo);
            if (currentLiteral.length() > 0) {
                patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
                formattingInfos.add(FormattingInfo.getDefault());
            }
        }
        currentLiteral.setLength(0);
        return i;
    }
    
    static {
        final Map rules = new HashMap(17);
        rules.put("c", LoggerPatternConverter.class);
        rules.put("logger", LoggerPatternConverter.class);
        rules.put("C", ClassNamePatternConverter.class);
        rules.put("class", ClassNamePatternConverter.class);
        rules.put("d", DatePatternConverter.class);
        rules.put("date", DatePatternConverter.class);
        rules.put("F", FileLocationPatternConverter.class);
        rules.put("file", FileLocationPatternConverter.class);
        rules.put("l", FullLocationPatternConverter.class);
        rules.put("L", LineLocationPatternConverter.class);
        rules.put("line", LineLocationPatternConverter.class);
        rules.put("m", MessagePatternConverter.class);
        rules.put("message", MessagePatternConverter.class);
        rules.put("n", LineSeparatorPatternConverter.class);
        rules.put("M", MethodLocationPatternConverter.class);
        rules.put("method", MethodLocationPatternConverter.class);
        rules.put("p", LevelPatternConverter.class);
        rules.put("level", LevelPatternConverter.class);
        rules.put("r", RelativeTimePatternConverter.class);
        rules.put("relative", RelativeTimePatternConverter.class);
        rules.put("t", ThreadPatternConverter.class);
        rules.put("thread", ThreadPatternConverter.class);
        rules.put("x", NDCPatternConverter.class);
        rules.put("ndc", NDCPatternConverter.class);
        rules.put("X", PropertiesPatternConverter.class);
        rules.put("properties", PropertiesPatternConverter.class);
        rules.put("sn", SequenceNumberPatternConverter.class);
        rules.put("sequenceNumber", SequenceNumberPatternConverter.class);
        rules.put("throwable", ThrowableInformationPatternConverter.class);
        PATTERN_LAYOUT_RULES = new ReadOnlyMap(rules);
        final Map fnameRules = new HashMap(4);
        fnameRules.put("d", FileDatePatternConverter.class);
        fnameRules.put("date", FileDatePatternConverter.class);
        fnameRules.put("i", IntegerPatternConverter.class);
        fnameRules.put("index", IntegerPatternConverter.class);
        FILENAME_PATTERN_RULES = new ReadOnlyMap(fnameRules);
    }
    
    private static class ReadOnlyMap implements Map
    {
        private final Map map;
        
        public ReadOnlyMap(final Map src) {
            this.map = src;
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final Object key) {
            return this.map.containsKey(key);
        }
        
        public boolean containsValue(final Object value) {
            return this.map.containsValue(value);
        }
        
        public Set entrySet() {
            return this.map.entrySet();
        }
        
        public Object get(final Object key) {
            return this.map.get(key);
        }
        
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        public Set keySet() {
            return this.map.keySet();
        }
        
        public Object put(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        public void putAll(final Map t) {
            throw new UnsupportedOperationException();
        }
        
        public Object remove(final Object key) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return this.map.size();
        }
        
        public Collection values() {
            return this.map.values();
        }
    }
}
