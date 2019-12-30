// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.xml;

import java.util.Properties;
import org.w3c.dom.Element;

public interface UnrecognizedElementHandler
{
    boolean parseUnrecognizedElement(final Element p0, final Properties p1) throws Exception;
}
