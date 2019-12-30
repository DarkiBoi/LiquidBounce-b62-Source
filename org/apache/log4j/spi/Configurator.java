// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import java.net.URL;
import java.io.InputStream;

public interface Configurator
{
    public static final String INHERITED = "inherited";
    public static final String NULL = "null";
    
    void doConfigure(final InputStream p0, final LoggerRepository p1);
    
    void doConfigure(final URL p0, final LoggerRepository p1);
}
