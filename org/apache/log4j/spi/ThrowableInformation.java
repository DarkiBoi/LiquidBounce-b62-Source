// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.spi;

import org.apache.log4j.DefaultThrowableRenderer;
import org.apache.log4j.Category;
import java.io.Serializable;

public class ThrowableInformation implements Serializable
{
    static final long serialVersionUID = -4748765566864322735L;
    private transient Throwable throwable;
    private transient Category category;
    private String[] rep;
    
    public ThrowableInformation(final Throwable throwable) {
        this.throwable = throwable;
    }
    
    public ThrowableInformation(final Throwable throwable, final Category category) {
        this.throwable = throwable;
        this.category = category;
    }
    
    public ThrowableInformation(final String[] r) {
        if (r != null) {
            this.rep = r.clone();
        }
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    public synchronized String[] getThrowableStrRep() {
        if (this.rep == null) {
            ThrowableRenderer renderer = null;
            if (this.category != null) {
                final LoggerRepository repo = this.category.getLoggerRepository();
                if (repo instanceof ThrowableRendererSupport) {
                    renderer = ((ThrowableRendererSupport)repo).getThrowableRenderer();
                }
            }
            if (renderer == null) {
                this.rep = DefaultThrowableRenderer.render(this.throwable);
            }
            else {
                this.rep = renderer.doRender(this.throwable);
            }
        }
        return this.rep.clone();
    }
}
