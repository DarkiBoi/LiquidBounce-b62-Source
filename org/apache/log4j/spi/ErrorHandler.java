// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

public interface ErrorHandler extends OptionHandler
{
    void setLogger(final Logger p0);
    
    void error(final String p0, final Exception p1, final int p2);
    
    void error(final String p0);
    
    void error(final String p0, final Exception p1, final int p2, final LoggingEvent p3);
    
    void setAppender(final Appender p0);
    
    void setBackupAppender(final Appender p0);
}
