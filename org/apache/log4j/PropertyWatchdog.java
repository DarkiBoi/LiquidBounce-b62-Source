// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import org.apache.log4j.helpers.FileWatchdog;

class PropertyWatchdog extends FileWatchdog
{
    PropertyWatchdog(final String filename) {
        super(filename);
    }
    
    public void doOnChange() {
        new PropertyConfigurator().doConfigure(this.filename, LogManager.getLoggerRepository());
    }
}
