// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.helpers;

import org.apache.log4j.pattern.LogEvent;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Set;
import org.apache.log4j.spi.LoggingEvent;
import java.lang.reflect.Method;

public final class MDCKeySetExtractor
{
    private final Method getKeySetMethod;
    public static final MDCKeySetExtractor INSTANCE;
    
    private MDCKeySetExtractor() {
        Method getMethod = null;
        try {
            getMethod = LoggingEvent.class.getMethod("getPropertyKeySet", (Class[])null);
        }
        catch (Exception ex) {
            getMethod = null;
        }
        this.getKeySetMethod = getMethod;
    }
    
    public Set getPropertyKeySet(final LoggingEvent event) throws Exception {
        Set keySet = null;
        if (this.getKeySetMethod != null) {
            keySet = (Set)this.getKeySetMethod.invoke(event, (Object[])null);
        }
        else {
            final ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            final ObjectOutputStream os = new ObjectOutputStream(outBytes);
            os.writeObject(event);
            os.close();
            final byte[] raw = outBytes.toByteArray();
            final String subClassName = LogEvent.class.getName();
            if (raw[6] == 0 || raw[7] == subClassName.length()) {
                for (int i = 0; i < subClassName.length(); ++i) {
                    raw[8 + i] = (byte)subClassName.charAt(i);
                }
                final ByteArrayInputStream inBytes = new ByteArrayInputStream(raw);
                final ObjectInputStream is = new ObjectInputStream(inBytes);
                final Object cracked = is.readObject();
                if (cracked instanceof LogEvent) {
                    keySet = ((LogEvent)cracked).getPropertyKeySet();
                }
                is.close();
            }
        }
        return keySet;
    }
    
    static {
        INSTANCE = new MDCKeySetExtractor();
    }
}
