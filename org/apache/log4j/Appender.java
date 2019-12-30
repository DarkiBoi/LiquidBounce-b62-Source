// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.Filter;

public interface Appender
{
    void addFilter(final Filter p0);
    
    Filter getFilter();
    
    void clearFilters();
    
    void close();
    
    void doAppend(final LoggingEvent p0);
    
    String getName();
    
    void setErrorHandler(final ErrorHandler p0);
    
    ErrorHandler getErrorHandler();
    
    void setLayout(final Layout p0);
    
    Layout getLayout();
    
    void setName(final String p0);
    
    boolean requiresLayout();
}
