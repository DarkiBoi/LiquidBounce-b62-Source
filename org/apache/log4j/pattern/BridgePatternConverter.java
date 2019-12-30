// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.helpers.PatternConverter;

public final class BridgePatternConverter extends PatternConverter
{
    private LoggingEventPatternConverter[] patternConverters;
    private FormattingInfo[] patternFields;
    private boolean handlesExceptions;
    
    public BridgePatternConverter(final String pattern) {
        this.next = null;
        this.handlesExceptions = false;
        final List converters = new ArrayList();
        final List fields = new ArrayList();
        final Map converterRegistry = null;
        PatternParser.parse(pattern, converters, fields, converterRegistry, PatternParser.getPatternLayoutRules());
        this.patternConverters = new LoggingEventPatternConverter[converters.size()];
        this.patternFields = new FormattingInfo[converters.size()];
        int i = 0;
        final Iterator converterIter = converters.iterator();
        final Iterator fieldIter = fields.iterator();
        while (converterIter.hasNext()) {
            final Object converter = converterIter.next();
            if (converter instanceof LoggingEventPatternConverter) {
                this.patternConverters[i] = (LoggingEventPatternConverter)converter;
                this.handlesExceptions |= this.patternConverters[i].handlesThrowable();
            }
            else {
                this.patternConverters[i] = new LiteralPatternConverter("");
            }
            if (fieldIter.hasNext()) {
                this.patternFields[i] = fieldIter.next();
            }
            else {
                this.patternFields[i] = FormattingInfo.getDefault();
            }
            ++i;
        }
    }
    
    protected String convert(final LoggingEvent event) {
        final StringBuffer sbuf = new StringBuffer();
        this.format(sbuf, event);
        return sbuf.toString();
    }
    
    public void format(final StringBuffer sbuf, final LoggingEvent e) {
        for (int i = 0; i < this.patternConverters.length; ++i) {
            final int startField = sbuf.length();
            this.patternConverters[i].format(e, sbuf);
            this.patternFields[i].format(startField, sbuf);
        }
    }
    
    public boolean ignoresThrowable() {
        return !this.handlesExceptions;
    }
}
