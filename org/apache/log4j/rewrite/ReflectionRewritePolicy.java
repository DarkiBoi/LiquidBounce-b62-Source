// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.rewrite;

import java.beans.PropertyDescriptor;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;
import java.beans.Introspector;
import java.util.HashMap;
import org.apache.log4j.spi.LoggingEvent;

public class ReflectionRewritePolicy implements RewritePolicy
{
    public LoggingEvent rewrite(final LoggingEvent source) {
        final Object msg = source.getMessage();
        if (!(msg instanceof String)) {
            Object newMsg = msg;
            final Map rewriteProps = new HashMap(source.getProperties());
            try {
                final PropertyDescriptor[] props = Introspector.getBeanInfo(msg.getClass(), Object.class).getPropertyDescriptors();
                if (props.length > 0) {
                    for (int i = 0; i < props.length; ++i) {
                        try {
                            final Object propertyValue = props[i].getReadMethod().invoke(msg, (Object[])null);
                            if ("message".equalsIgnoreCase(props[i].getName())) {
                                newMsg = propertyValue;
                            }
                            else {
                                rewriteProps.put(props[i].getName(), propertyValue);
                            }
                        }
                        catch (Exception e) {
                            LogLog.warn("Unable to evaluate property " + props[i].getName(), e);
                        }
                    }
                    return new LoggingEvent(source.getFQNOfLoggerClass(), (source.getLogger() != null) ? source.getLogger() : Logger.getLogger(source.getLoggerName()), source.getTimeStamp(), source.getLevel(), newMsg, source.getThreadName(), source.getThrowableInformation(), source.getNDC(), source.getLocationInformation(), rewriteProps);
                }
            }
            catch (Exception e2) {
                LogLog.warn("Unable to get property descriptors", e2);
            }
        }
        return source;
    }
}
