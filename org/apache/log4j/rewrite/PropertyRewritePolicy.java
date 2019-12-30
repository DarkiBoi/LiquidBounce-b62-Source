// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.rewrite;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

public class PropertyRewritePolicy implements RewritePolicy
{
    private Map properties;
    
    public PropertyRewritePolicy() {
        this.properties = Collections.EMPTY_MAP;
    }
    
    public void setProperties(final String props) {
        final Map hashTable = new HashMap();
        final StringTokenizer pairs = new StringTokenizer(props, ",");
        while (pairs.hasMoreTokens()) {
            final StringTokenizer entry = new StringTokenizer(pairs.nextToken(), "=");
            hashTable.put(entry.nextElement().toString().trim(), entry.nextElement().toString().trim());
        }
        synchronized (this) {
            this.properties = hashTable;
        }
    }
    
    public LoggingEvent rewrite(final LoggingEvent source) {
        if (!this.properties.isEmpty()) {
            final Map rewriteProps = new HashMap(source.getProperties());
            for (final Map.Entry entry : this.properties.entrySet()) {
                if (!rewriteProps.containsKey(entry.getKey())) {
                    rewriteProps.put(entry.getKey(), entry.getValue());
                }
            }
            return new LoggingEvent(source.getFQNOfLoggerClass(), (source.getLogger() != null) ? source.getLogger() : Logger.getLogger(source.getLoggerName()), source.getTimeStamp(), source.getLevel(), source.getMessage(), source.getThreadName(), source.getThrowableInformation(), source.getNDC(), source.getLocationInformation(), rewriteProps);
        }
        return source;
    }
}
