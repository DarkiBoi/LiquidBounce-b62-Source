// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.lf5;

import org.apache.log4j.lf5.viewer.LogBrokerMonitor;

public class AppenderFinalizer
{
    protected LogBrokerMonitor _defaultMonitor;
    
    public AppenderFinalizer(final LogBrokerMonitor defaultMonitor) {
        this._defaultMonitor = null;
        this._defaultMonitor = defaultMonitor;
    }
    
    protected void finalize() throws Throwable {
        System.out.println("Disposing of the default LogBrokerMonitor instance");
        this._defaultMonitor.dispose();
    }
}
