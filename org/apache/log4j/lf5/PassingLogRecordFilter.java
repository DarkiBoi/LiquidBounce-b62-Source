// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.lf5;

public class PassingLogRecordFilter implements LogRecordFilter
{
    public boolean passes(final LogRecord record) {
        return true;
    }
    
    public void reset() {
    }
}
