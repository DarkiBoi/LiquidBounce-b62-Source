// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import java.io.Writer;

class NullWriter extends Writer
{
    public void close() {
    }
    
    public void flush() {
    }
    
    public void write(final char[] cbuf, final int off, final int len) {
    }
}
