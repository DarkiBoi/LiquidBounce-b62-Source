// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LoggingEvent;

public interface RewritePolicy
{
    LoggingEvent rewrite(final LoggingEvent p0);
}
