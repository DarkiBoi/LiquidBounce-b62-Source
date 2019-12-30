// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import java.util.Enumeration;
import org.apache.log4j.Appender;

public interface AppenderAttachable
{
    void addAppender(final Appender p0);
    
    Enumeration getAllAppenders();
    
    Appender getAppender(final String p0);
    
    boolean isAttached(final Appender p0);
    
    void removeAllAppenders();
    
    void removeAppender(final Appender p0);
    
    void removeAppender(final String p0);
}
