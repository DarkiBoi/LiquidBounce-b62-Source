// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.xml;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import org.apache.log4j.helpers.LogLog;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;

public class Log4jEntityResolver implements EntityResolver
{
    private static final String PUBLIC_ID = "-//APACHE//DTD LOG4J 1.2//EN";
    
    public InputSource resolveEntity(final String publicId, final String systemId) {
        if (systemId.endsWith("log4j.dtd") || "-//APACHE//DTD LOG4J 1.2//EN".equals(publicId)) {
            final Class clazz = this.getClass();
            InputStream in = clazz.getResourceAsStream("/org/apache/log4j/xml/log4j.dtd");
            if (in == null) {
                LogLog.warn("Could not find [log4j.dtd] using [" + clazz.getClassLoader() + "] class loader, parsed without DTD.");
                in = new ByteArrayInputStream(new byte[0]);
            }
            return new InputSource(in);
        }
        return null;
    }
}
