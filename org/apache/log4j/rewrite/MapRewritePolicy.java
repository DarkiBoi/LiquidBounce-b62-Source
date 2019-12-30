// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.rewrite;

import java.util.Iterator;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.spi.LoggingEvent;

public class MapRewritePolicy implements RewritePolicy
{
    public LoggingEvent rewrite(final LoggingEvent source) {
        final Object msg = source.getMessage();
        if (msg instanceof Map) {
            final Map props = new HashMap(source.getProperties());
            final Map eventProps = (Map)msg;
            Object newMsg = eventProps.get("message");
            if (newMsg == null) {
                newMsg = msg;
            }
            for (final Map.Entry entry : eventProps.entrySet()) {
                if (!"message".equals(entry.getKey())) {
                    props.put(entry.getKey(), entry.getValue());
                }
            }
            return new LoggingEvent(source.getFQNOfLoggerClass(), (source.getLogger() != null) ? source.getLogger() : Logger.getLogger(source.getLoggerName()), source.getTimeStamp(), source.getLevel(), newMsg, source.getThreadName(), source.getThrowableInformation(), source.getNDC(), source.getLocationInformation(), props);
        }
        return source;
    }
}
