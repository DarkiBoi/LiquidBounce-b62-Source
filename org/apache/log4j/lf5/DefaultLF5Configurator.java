// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.lf5;

import org.apache.log4j.spi.LoggerRepository;
import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Configurator;

public class DefaultLF5Configurator implements Configurator
{
    private DefaultLF5Configurator() {
    }
    
    public static void configure() throws IOException {
        final String resource = "/org/apache/log4j/lf5/config/defaultconfig.properties";
        final URL configFileResource = DefaultLF5Configurator.class.getResource(resource);
        if (configFileResource != null) {
            PropertyConfigurator.configure(configFileResource);
            return;
        }
        throw new IOException("Error: Unable to open the resource" + resource);
    }
    
    public void doConfigure(final InputStream inputStream, final LoggerRepository repository) {
        throw new IllegalStateException("This class should NOT be instantiated!");
    }
    
    public void doConfigure(final URL configURL, final LoggerRepository repository) {
        throw new IllegalStateException("This class should NOT be instantiated!");
    }
}
