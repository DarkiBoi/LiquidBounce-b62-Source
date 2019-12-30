// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.api.scripting;

import java.io.CharArrayReader;
import jdk.nashorn.internal.runtime.Source;
import java.io.IOException;
import java.util.Objects;
import java.nio.charset.Charset;
import java.net.URL;
import jdk.Exported;
import java.io.Reader;

@Exported
public final class URLReader extends Reader
{
    private final URL url;
    private final Charset cs;
    private Reader reader;
    
    public URLReader(final URL url) {
        this(url, (Charset)null);
    }
    
    public URLReader(final URL url, final String charsetName) {
        this(url, Charset.forName(charsetName));
    }
    
    public URLReader(final URL url, final Charset cs) {
        this.url = Objects.requireNonNull(url);
        this.cs = cs;
    }
    
    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        return this.getReader().read(cbuf, off, len);
    }
    
    @Override
    public void close() throws IOException {
        this.getReader().close();
    }
    
    public URL getURL() {
        return this.url;
    }
    
    public Charset getCharset() {
        return this.cs;
    }
    
    private Reader getReader() throws IOException {
        synchronized (this.lock) {
            if (this.reader == null) {
                this.reader = new CharArrayReader(Source.readFully(this.url, this.cs));
            }
        }
        return this.reader;
    }
}
