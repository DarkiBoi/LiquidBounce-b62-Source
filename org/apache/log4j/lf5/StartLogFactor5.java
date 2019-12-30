// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.lf5;

import org.apache.log4j.lf5.viewer.LogBrokerMonitor;

public class StartLogFactor5
{
    public static final void main(final String[] args) {
        final LogBrokerMonitor monitor = new LogBrokerMonitor(LogLevel.getLog4JLevels());
        monitor.setFrameSize(LF5Appender.getDefaultMonitorWidth(), LF5Appender.getDefaultMonitorHeight());
        monitor.setFontSize(12);
        monitor.show();
    }
}
