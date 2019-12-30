// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.apache.log4j.Category;
import org.apache.log4j.Level;

public interface LoggerRepository
{
    void addHierarchyEventListener(final HierarchyEventListener p0);
    
    boolean isDisabled(final int p0);
    
    void setThreshold(final Level p0);
    
    void setThreshold(final String p0);
    
    void emitNoAppenderWarning(final Category p0);
    
    Level getThreshold();
    
    Logger getLogger(final String p0);
    
    Logger getLogger(final String p0, final LoggerFactory p1);
    
    Logger getRootLogger();
    
    Logger exists(final String p0);
    
    void shutdown();
    
    Enumeration getCurrentLoggers();
    
    Enumeration getCurrentCategories();
    
    void fireAddAppenderEvent(final Category p0, final Appender p1);
    
    void resetConfiguration();
}
