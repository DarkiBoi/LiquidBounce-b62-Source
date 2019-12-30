// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import org.apache.log4j.helpers.LogLog;

public class ConsoleAppender extends WriterAppender
{
    public static final String SYSTEM_OUT = "System.out";
    public static final String SYSTEM_ERR = "System.err";
    protected String target;
    private boolean follow;
    
    public ConsoleAppender() {
        this.target = "System.out";
        this.follow = false;
    }
    
    public ConsoleAppender(final Layout layout) {
        this(layout, "System.out");
    }
    
    public ConsoleAppender(final Layout layout, final String target) {
        this.target = "System.out";
        this.follow = false;
        this.setLayout(layout);
        this.setTarget(target);
        this.activateOptions();
    }
    
    public void setTarget(final String value) {
        final String v = value.trim();
        if ("System.out".equalsIgnoreCase(v)) {
            this.target = "System.out";
        }
        else if ("System.err".equalsIgnoreCase(v)) {
            this.target = "System.err";
        }
        else {
            this.targetWarn(value);
        }
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public final void setFollow(final boolean newValue) {
        this.follow = newValue;
    }
    
    public final boolean getFollow() {
        return this.follow;
    }
    
    void targetWarn(final String val) {
        LogLog.warn("[" + val + "] should be System.out or System.err.");
        LogLog.warn("Using previously set target, System.out by default.");
    }
    
    public void activateOptions() {
        if (this.follow) {
            if (this.target.equals("System.err")) {
                this.setWriter(this.createWriter(new SystemErrStream()));
            }
            else {
                this.setWriter(this.createWriter(new SystemOutStream()));
            }
        }
        else if (this.target.equals("System.err")) {
            this.setWriter(this.createWriter(System.err));
        }
        else {
            this.setWriter(this.createWriter(System.out));
        }
        super.activateOptions();
    }
    
    protected final void closeWriter() {
        if (this.follow) {
            super.closeWriter();
        }
    }
    
    private static class SystemErrStream extends OutputStream
    {
        public SystemErrStream() {
        }
        
        public void close() {
        }
        
        public void flush() {
            System.err.flush();
        }
        
        public void write(final byte[] b) throws IOException {
            System.err.write(b);
        }
        
        public void write(final byte[] b, final int off, final int len) throws IOException {
            System.err.write(b, off, len);
        }
        
        public void write(final int b) throws IOException {
            System.err.write(b);
        }
    }
    
    private static class SystemOutStream extends OutputStream
    {
        public SystemOutStream() {
        }
        
        public void close() {
        }
        
        public void flush() {
            System.out.flush();
        }
        
        public void write(final byte[] b) throws IOException {
            System.out.write(b);
        }
        
        public void write(final byte[] b, final int off, final int len) throws IOException {
            System.out.write(b, off, len);
        }
        
        public void write(final int b) throws IOException {
            System.out.write(b);
        }
    }
}
