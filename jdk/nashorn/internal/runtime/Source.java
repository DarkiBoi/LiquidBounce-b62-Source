// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.util.Arrays;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.net.MalformedURLException;
import java.io.IOError;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import jdk.nashorn.internal.parser.Token;
import java.util.Objects;
import jdk.nashorn.api.scripting.URLReader;
import java.io.Reader;
import java.io.File;
import java.nio.charset.Charset;
import java.net.URL;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import java.io.IOException;
import java.util.Base64;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;

@Logger(name = "source")
public final class Source implements Loggable
{
    private static final int BUF_SIZE = 8192;
    private static final Cache CACHE;
    private static final Base64.Encoder BASE64;
    private final String name;
    private final String base;
    private final Data data;
    private int hash;
    private volatile byte[] digest;
    private String explicitURL;
    
    private Source(final String name, final String base, final Data data) {
        this.name = name;
        this.base = base;
        this.data = data;
    }
    
    private static synchronized Source sourceFor(final String name, final String base, final URLData data) throws IOException {
        try {
            final Source newSource = new Source(name, base, data);
            final Source existingSource = Source.CACHE.get(newSource);
            if (existingSource != null) {
                data.checkPermissionAndClose();
                return existingSource;
            }
            data.load();
            Source.CACHE.put(newSource, newSource);
            return newSource;
        }
        catch (RuntimeException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException)cause;
            }
            throw e;
        }
    }
    
    DebuggerSupport.SourceInfo getSourceInfo() {
        return new DebuggerSupport.SourceInfo(this.getName(), this.data.hashCode(), this.data.url(), this.data.array());
    }
    
    private static void debug(final Object... msg) {
        final DebugLogger logger = getLoggerStatic();
        if (logger != null) {
            logger.info(msg);
        }
    }
    
    private char[] data() {
        return this.data.array();
    }
    
    public static Source sourceFor(final String name, final char[] content, final boolean isEval) {
        return new Source(name, baseName(name), new RawData(content, isEval));
    }
    
    public static Source sourceFor(final String name, final char[] content) {
        return sourceFor(name, content, false);
    }
    
    public static Source sourceFor(final String name, final String content, final boolean isEval) {
        return new Source(name, baseName(name), new RawData(content, isEval));
    }
    
    public static Source sourceFor(final String name, final String content) {
        return sourceFor(name, content, false);
    }
    
    public static Source sourceFor(final String name, final URL url) throws IOException {
        return sourceFor(name, url, null);
    }
    
    public static Source sourceFor(final String name, final URL url, final Charset cs) throws IOException {
        return sourceFor(name, baseURL(url), new URLData(url, cs));
    }
    
    public static Source sourceFor(final String name, final File file) throws IOException {
        return sourceFor(name, file, null);
    }
    
    public static Source sourceFor(final String name, final File file, final Charset cs) throws IOException {
        final File absFile = file.getAbsoluteFile();
        return sourceFor(name, dirName(absFile, null), new FileData(file, cs));
    }
    
    public static Source sourceFor(final String name, final Reader reader) throws IOException {
        if (reader instanceof URLReader) {
            final URLReader urlReader = (URLReader)reader;
            return sourceFor(name, urlReader.getURL(), urlReader.getCharset());
        }
        return new Source(name, baseName(name), new RawData(reader));
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Source)) {
            return false;
        }
        final Source other = (Source)obj;
        return Objects.equals(this.name, other.name) && this.data.equals(other.data);
    }
    
    @Override
    public int hashCode() {
        int h = this.hash;
        if (h == 0) {
            final int hash = this.data.hashCode() ^ Objects.hashCode(this.name);
            this.hash = hash;
            h = hash;
        }
        return h;
    }
    
    public String getString() {
        return this.data.toString();
    }
    
    public String getName() {
        return this.name;
    }
    
    public long getLastModified() {
        return this.data.lastModified();
    }
    
    public String getBase() {
        return this.base;
    }
    
    public String getString(final int start, final int len) {
        return new String(this.data(), start, len);
    }
    
    public String getString(final long token) {
        final int start = Token.descPosition(token);
        final int len = Token.descLength(token);
        return new String(this.data(), start, len);
    }
    
    public URL getURL() {
        return this.data.url();
    }
    
    public String getExplicitURL() {
        return this.explicitURL;
    }
    
    public void setExplicitURL(final String explicitURL) {
        this.explicitURL = explicitURL;
    }
    
    public boolean isEvalCode() {
        return this.data.isEvalCode();
    }
    
    private int findBOLN(final int position) {
        final char[] d = this.data();
        for (int i = position - 1; i > 0; --i) {
            final char ch = d[i];
            if (ch == '\n' || ch == '\r') {
                return i + 1;
            }
        }
        return 0;
    }
    
    private int findEOLN(final int position) {
        final char[] d = this.data();
        final int length = d.length;
        for (int i = position; i < length; ++i) {
            final char ch = d[i];
            if (ch == '\n' || ch == '\r') {
                return i - 1;
            }
        }
        return length - 1;
    }
    
    public int getLine(final int position) {
        final char[] d = this.data();
        int line = 1;
        for (final char ch : d) {
            if (ch == '\n') {
                ++line;
            }
        }
        return line;
    }
    
    public int getColumn(final int position) {
        return position - this.findBOLN(position);
    }
    
    public String getSourceLine(final int position) {
        final int first = this.findBOLN(position);
        final int last = this.findEOLN(position);
        return new String(this.data(), first, last - first + 1);
    }
    
    public char[] getContent() {
        return this.data();
    }
    
    public int getLength() {
        return this.data.length();
    }
    
    public static char[] readFully(final Reader reader) throws IOException {
        final char[] arr = new char[8192];
        final StringBuilder sb = new StringBuilder();
        try {
            int numChars;
            while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
                sb.append(arr, 0, numChars);
            }
        }
        finally {
            reader.close();
        }
        return sb.toString().toCharArray();
    }
    
    public static char[] readFully(final File file) throws IOException {
        if (!file.isFile()) {
            throw new IOException(file + " is not a file");
        }
        return byteToCharArray(Files.readAllBytes(file.toPath()));
    }
    
    public static char[] readFully(final File file, final Charset cs) throws IOException {
        if (!file.isFile()) {
            throw new IOException(file + " is not a file");
        }
        final byte[] buf = Files.readAllBytes(file.toPath());
        return (cs != null) ? new String(buf, cs).toCharArray() : byteToCharArray(buf);
    }
    
    public static char[] readFully(final URL url) throws IOException {
        return readFully(url.openStream());
    }
    
    public static char[] readFully(final URL url, final Charset cs) throws IOException {
        return readFully(url.openStream(), cs);
    }
    
    public String getDigest() {
        return new String(this.getDigestBytes(), StandardCharsets.US_ASCII);
    }
    
    private byte[] getDigestBytes() {
        byte[] ldigest = this.digest;
        if (ldigest == null) {
            final char[] content = this.data();
            final byte[] bytes = new byte[content.length * 2];
            for (int i = 0; i < content.length; ++i) {
                bytes[i * 2] = (byte)(content[i] & '\u00ff');
                bytes[i * 2 + 1] = (byte)((content[i] & '\uff00') >> 8);
            }
            try {
                final MessageDigest md = MessageDigest.getInstance("SHA-1");
                if (this.name != null) {
                    md.update(this.name.getBytes(StandardCharsets.UTF_8));
                }
                if (this.base != null) {
                    md.update(this.base.getBytes(StandardCharsets.UTF_8));
                }
                if (this.getURL() != null) {
                    md.update(this.getURL().toString().getBytes(StandardCharsets.UTF_8));
                }
                ldigest = (this.digest = Source.BASE64.encode(md.digest(bytes)));
            }
            catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return ldigest;
    }
    
    public static String baseURL(final URL url) {
        if (url.getProtocol().equals("file")) {
            try {
                final Path path = Paths.get(url.toURI());
                final Path parent = path.getParent();
                return (parent != null) ? (parent + File.separator) : null;
            }
            catch (SecurityException | URISyntaxException | IOError ex) {
                final Throwable t;
                final Throwable e = t;
                return null;
            }
        }
        String path2 = url.getPath();
        if (path2.isEmpty()) {
            return null;
        }
        path2 = path2.substring(0, path2.lastIndexOf(47) + 1);
        final int port = url.getPort();
        try {
            return new URL(url.getProtocol(), url.getHost(), port, path2).toString();
        }
        catch (MalformedURLException e2) {
            return null;
        }
    }
    
    private static String dirName(final File file, final String DEFAULT_BASE_NAME) {
        final String res = file.getParent();
        return (res != null) ? (res + File.separator) : DEFAULT_BASE_NAME;
    }
    
    private static String baseName(final String name) {
        int idx = name.lastIndexOf(47);
        if (idx == -1) {
            idx = name.lastIndexOf(92);
        }
        return (idx != -1) ? name.substring(0, idx + 1) : null;
    }
    
    private static char[] readFully(final InputStream is, final Charset cs) throws IOException {
        return (cs != null) ? new String(readBytes(is), cs).toCharArray() : readFully(is);
    }
    
    private static char[] readFully(final InputStream is) throws IOException {
        return byteToCharArray(readBytes(is));
    }
    
    private static char[] byteToCharArray(final byte[] bytes) {
        Charset cs = StandardCharsets.UTF_8;
        int start = 0;
        if (bytes.length > 1 && bytes[0] == -2 && bytes[1] == -1) {
            start = 2;
            cs = StandardCharsets.UTF_16BE;
        }
        else if (bytes.length > 1 && bytes[0] == -1 && bytes[1] == -2) {
            if (bytes.length > 3 && bytes[2] == 0 && bytes[3] == 0) {
                start = 4;
                cs = Charset.forName("UTF-32LE");
            }
            else {
                start = 2;
                cs = StandardCharsets.UTF_16LE;
            }
        }
        else if (bytes.length > 2 && bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65) {
            start = 3;
            cs = StandardCharsets.UTF_8;
        }
        else if (bytes.length > 3 && bytes[0] == 0 && bytes[1] == 0 && bytes[2] == -2 && bytes[3] == -1) {
            start = 4;
            cs = Charset.forName("UTF-32BE");
        }
        return new String(bytes, start, bytes.length - start, cs).toCharArray();
    }
    
    static byte[] readBytes(final InputStream is) throws IOException {
        final byte[] arr = new byte[8192];
        try (final ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
            int numBytes;
            while ((numBytes = is.read(arr, 0, arr.length)) > 0) {
                buf.write(arr, 0, numBytes);
            }
            return buf.toByteArray();
        }
        finally {
            is.close();
        }
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
    private static URL getURLFromFile(final File file) {
        try {
            return file.toURI().toURL();
        }
        catch (SecurityException | MalformedURLException ex2) {
            final Exception ex;
            final Exception ignored = ex;
            return null;
        }
    }
    
    private static DebugLogger getLoggerStatic() {
        final Context context = Context.getContextTrustedOrNull();
        return (context == null) ? null : context.getLogger(Source.class);
    }
    
    @Override
    public DebugLogger initLogger(final Context context) {
        return context.getLogger(this.getClass());
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.initLogger(Context.getContextTrusted());
    }
    
    private File dumpFile(final File dirFile) {
        final URL u = this.getURL();
        final StringBuilder buf = new StringBuilder();
        buf.append(LocalDateTime.now().toString());
        buf.append('_');
        if (u != null) {
            buf.append(u.toString().replace('/', '_').replace('\\', '_'));
        }
        else {
            buf.append(this.getName());
        }
        return new File(dirFile, buf.toString());
    }
    
    void dump(final String dir) {
        final File dirFile = new File(dir);
        final File file = this.dumpFile(dirFile);
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            debug("Skipping source dump for " + this.name);
            return;
        }
        try (final FileOutputStream fos = new FileOutputStream(file)) {
            final PrintWriter pw = new PrintWriter(fos);
            pw.print(this.data.toString());
            pw.flush();
        }
        catch (IOException ioExp) {
            debug("Skipping source dump for " + this.name + ": " + ECMAErrors.getMessage("io.error.cant.write", dir.toString() + " : " + ioExp.toString()));
        }
    }
    
    static {
        CACHE = new Cache();
        BASE64 = Base64.getUrlEncoder().withoutPadding();
    }
    
    private static class Cache extends WeakHashMap<Source, WeakReference<Source>>
    {
        public Source get(final Source key) {
            final WeakReference<Source> ref = super.get(key);
            return (ref == null) ? null : ref.get();
        }
        
        public void put(final Source key, final Source value) {
            assert !(value.data instanceof RawData);
            this.put(key, new WeakReference<Source>(value));
        }
    }
    
    private static class RawData implements Data
    {
        private final char[] array;
        private final boolean evalCode;
        private int hash;
        
        private RawData(final char[] array, final boolean evalCode) {
            this.array = Objects.requireNonNull(array);
            this.evalCode = evalCode;
        }
        
        private RawData(final String source, final boolean evalCode) {
            this.array = Objects.requireNonNull(source).toCharArray();
            this.evalCode = evalCode;
        }
        
        private RawData(final Reader reader) throws IOException {
            this(Source.readFully(reader), false);
        }
        
        @Override
        public int hashCode() {
            int h = this.hash;
            if (h == 0) {
                final int hash = Arrays.hashCode(this.array) ^ (this.evalCode ? 1 : 0);
                this.hash = hash;
                h = hash;
            }
            return h;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof RawData) {
                final RawData other = (RawData)obj;
                return Arrays.equals(this.array, other.array) && this.evalCode == other.evalCode;
            }
            return false;
        }
        
        @Override
        public String toString() {
            return new String(this.array());
        }
        
        @Override
        public URL url() {
            return null;
        }
        
        @Override
        public int length() {
            return this.array.length;
        }
        
        @Override
        public long lastModified() {
            return 0L;
        }
        
        @Override
        public char[] array() {
            return this.array;
        }
        
        @Override
        public boolean isEvalCode() {
            return this.evalCode;
        }
    }
    
    private static class URLData implements Data
    {
        private final URL url;
        protected final Charset cs;
        private int hash;
        protected char[] array;
        protected int length;
        protected long lastModified;
        
        private URLData(final URL url, final Charset cs) {
            this.url = Objects.requireNonNull(url);
            this.cs = cs;
        }
        
        @Override
        public int hashCode() {
            int h = this.hash;
            if (h == 0) {
                final int hashCode = this.url.hashCode();
                this.hash = hashCode;
                h = hashCode;
            }
            return h;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof URLData)) {
                return false;
            }
            final URLData otherData = (URLData)other;
            if (this.url.equals(otherData.url)) {
                try {
                    if (this.isDeferred()) {
                        assert !otherData.isDeferred();
                        this.loadMeta();
                    }
                    else if (otherData.isDeferred()) {
                        otherData.loadMeta();
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return this.length == otherData.length && this.lastModified == otherData.lastModified;
            }
            return false;
        }
        
        @Override
        public String toString() {
            return new String(this.array());
        }
        
        @Override
        public URL url() {
            return this.url;
        }
        
        @Override
        public int length() {
            return this.length;
        }
        
        @Override
        public long lastModified() {
            return this.lastModified;
        }
        
        @Override
        public char[] array() {
            assert !this.isDeferred();
            return this.array;
        }
        
        @Override
        public boolean isEvalCode() {
            return false;
        }
        
        boolean isDeferred() {
            return this.array == null;
        }
        
        protected void checkPermissionAndClose() throws IOException {
            final InputStream in = this.url.openStream();
            final Throwable t = null;
            if (in != null) {
                if (t != null) {
                    try {
                        in.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                else {
                    in.close();
                }
            }
            debug(new Object[] { "permission checked for ", this.url });
        }
        
        protected void load() throws IOException {
            if (this.array == null) {
                final URLConnection c = this.url.openConnection();
                try (final InputStream in = c.getInputStream()) {
                    this.array = ((this.cs == null) ? readFully(in) : readFully(in, this.cs));
                    this.length = this.array.length;
                    this.lastModified = c.getLastModified();
                    debug(new Object[] { "loaded content for ", this.url });
                }
            }
        }
        
        protected void loadMeta() throws IOException {
            if (this.length == 0 && this.lastModified == 0L) {
                final URLConnection c = this.url.openConnection();
                this.length = c.getContentLength();
                this.lastModified = c.getLastModified();
                debug(new Object[] { "loaded metadata for ", this.url });
            }
        }
    }
    
    private static class FileData extends URLData
    {
        private final File file;
        
        private FileData(final File file, final Charset cs) {
            super(getURLFromFile(file), cs);
            this.file = file;
        }
        
        @Override
        protected void checkPermissionAndClose() throws IOException {
            if (!this.file.canRead()) {
                throw new FileNotFoundException(this.file + " (Permission Denied)");
            }
            debug(new Object[] { "permission checked for ", this.file });
        }
        
        @Override
        protected void loadMeta() {
            if (this.length == 0 && this.lastModified == 0L) {
                this.length = (int)this.file.length();
                this.lastModified = this.file.lastModified();
                debug(new Object[] { "loaded metadata for ", this.file });
            }
        }
        
        @Override
        protected void load() throws IOException {
            if (this.array == null) {
                this.array = ((this.cs == null) ? Source.readFully(this.file) : Source.readFully(this.file, this.cs));
                this.length = this.array.length;
                this.lastModified = this.file.lastModified();
                debug(new Object[] { "loaded content for ", this.file });
            }
        }
    }
    
    private interface Data
    {
        URL url();
        
        int length();
        
        long lastModified();
        
        char[] array();
        
        boolean isEvalCode();
    }
}
