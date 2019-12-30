// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.helpers;

import org.apache.log4j.spi.ErrorHandler;
import java.io.Writer;

public class SyslogQuietWriter extends QuietWriter
{
    int syslogFacility;
    int level;
    
    public SyslogQuietWriter(final Writer writer, final int syslogFacility, final ErrorHandler eh) {
        super(writer, eh);
        this.syslogFacility = syslogFacility;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setSyslogFacility(final int syslogFacility) {
        this.syslogFacility = syslogFacility;
    }
    
    public void write(final String string) {
        super.write("<" + (this.syslogFacility | this.level) + ">" + string);
    }
}
