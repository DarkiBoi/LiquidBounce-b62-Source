// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

public class DefaultRepositorySelector implements RepositorySelector
{
    final LoggerRepository repository;
    
    public DefaultRepositorySelector(final LoggerRepository repository) {
        this.repository = repository;
    }
    
    public LoggerRepository getLoggerRepository() {
        return this.repository;
    }
}
