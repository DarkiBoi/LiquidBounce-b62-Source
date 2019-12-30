// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import java.util.stream.Stream;
import java.util.function.IntFunction;
import java.util.function.Function;
import java.nio.file.LinkOption;
import java.util.function.Predicate;
import java.nio.file.FileVisitOption;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.TimerTask;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import java.nio.file.Files;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.security.MessageDigest;
import java.net.URL;
import jdk.nashorn.internal.runtime.options.Options;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.AccessController;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.security.PrivilegedAction;
import java.util.Map;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.Source;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.File;

public final class OptimisticTypesPersistence
{
    private static final int DEFAULT_MAX_FILES = 0;
    private static final int UNLIMITED_FILES = -1;
    private static final int MAX_FILES;
    private static final int DEFAULT_CLEANUP_DELAY = 20;
    private static final int CLEANUP_DELAY;
    private static final String DEFAULT_CACHE_SUBDIR_NAME = "com.oracle.java.NashornTypeInfo";
    private static final File baseCacheDir;
    private static final File cacheDir;
    private static final Object[] locks;
    private static final long ERROR_REPORT_THRESHOLD = 60000L;
    private static volatile long lastReportedError;
    private static final AtomicBoolean scheduledCleanup;
    private static final Timer cleanupTimer;
    
    public static Object getLocationDescriptor(final Source source, final int functionId, final Type[] paramTypes) {
        if (OptimisticTypesPersistence.cacheDir == null) {
            return null;
        }
        final StringBuilder b = new StringBuilder(48);
        b.append(source.getDigest()).append('-').append(functionId);
        if (paramTypes != null && paramTypes.length > 0) {
            b.append('-');
            for (final Type t : paramTypes) {
                b.append(Type.getShortSignatureDescriptor(t));
            }
        }
        return new LocationDescriptor(new File(OptimisticTypesPersistence.cacheDir, b.toString()));
    }
    
    public static void store(final Object locationDescriptor, final Map<Integer, Type> optimisticTypes) {
        if (locationDescriptor == null || optimisticTypes.isEmpty()) {
            return;
        }
        final File file = ((LocationDescriptor)locationDescriptor).file;
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                synchronized (getFileLock(file)) {
                    if (!file.exists()) {
                        scheduleCleanup();
                    }
                    try (final FileOutputStream out = new FileOutputStream(file)) {
                        out.getChannel().lock();
                        final DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(out));
                        Type.writeTypeMap(optimisticTypes, dout);
                        dout.flush();
                    }
                    catch (Exception e) {
                        reportError("write", file, e);
                    }
                }
                return null;
            }
        });
    }
    
    public static Map<Integer, Type> load(final Object locationDescriptor) {
        if (locationDescriptor == null) {
            return null;
        }
        final File file = ((LocationDescriptor)locationDescriptor).file;
        return AccessController.doPrivileged((PrivilegedAction<Map<Integer, Type>>)new PrivilegedAction<Map<Integer, Type>>() {
            @Override
            public Map<Integer, Type> run() {
                try {
                    if (!file.isFile()) {
                        return null;
                    }
                    synchronized (getFileLock(file)) {
                        try (final FileInputStream in = new FileInputStream(file)) {
                            in.getChannel().lock(0L, Long.MAX_VALUE, true);
                            final DataInputStream din = new DataInputStream(new BufferedInputStream(in));
                            return Type.readTypeMap(din);
                        }
                    }
                }
                catch (Exception e) {
                    reportError("read", file, e);
                    return null;
                }
            }
        });
    }
    
    private static void reportError(final String msg, final File file, final Exception e) {
        final long now = System.currentTimeMillis();
        if (now - OptimisticTypesPersistence.lastReportedError > 60000L) {
            reportError(String.format("Failed to %s %s", msg, file), e);
            OptimisticTypesPersistence.lastReportedError = now;
        }
    }
    
    private static void reportError(final String msg, final Exception e) {
        getLogger().warning(msg, "\n", exceptionToString(e));
    }
    
    private static String exceptionToString(final Exception e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, false);
        e.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
    
    private static File createBaseCacheDir() {
        if (OptimisticTypesPersistence.MAX_FILES == 0 || Options.getBooleanProperty("nashorn.typeInfo.disabled")) {
            return null;
        }
        try {
            return createBaseCacheDirPrivileged();
        }
        catch (Exception e) {
            reportError("Failed to create cache dir", e);
            return null;
        }
    }
    
    private static File createBaseCacheDirPrivileged() {
        return AccessController.doPrivileged((PrivilegedAction<File>)new PrivilegedAction<File>() {
            @Override
            public File run() {
                final String explicitDir = System.getProperty("nashorn.typeInfo.cacheDir");
                File dir;
                if (explicitDir != null) {
                    dir = new File(explicitDir);
                }
                else {
                    final File systemCacheDir = getSystemCacheDir();
                    dir = new File(systemCacheDir, "com.oracle.java.NashornTypeInfo");
                    if (isSymbolicLink(dir)) {
                        return null;
                    }
                }
                return dir;
            }
        });
    }
    
    private static File createCacheDir(final File baseDir) {
        if (baseDir == null) {
            return null;
        }
        try {
            return createCacheDirPrivileged(baseDir);
        }
        catch (Exception e) {
            reportError("Failed to create cache dir", e);
            return null;
        }
    }
    
    private static File createCacheDirPrivileged(final File baseDir) {
        return AccessController.doPrivileged((PrivilegedAction<File>)new PrivilegedAction<File>() {
            @Override
            public File run() {
                String versionDirName;
                try {
                    versionDirName = OptimisticTypesPersistence.getVersionDirName();
                }
                catch (Exception e) {
                    reportError("Failed to calculate version dir name", e);
                    return null;
                }
                final File versionDir = new File(baseDir, versionDirName);
                if (isSymbolicLink(versionDir)) {
                    return null;
                }
                versionDir.mkdirs();
                if (versionDir.isDirectory()) {
                    getLogger().info("Optimistic type persistence directory is " + versionDir);
                    return versionDir;
                }
                getLogger().warning("Could not create optimistic type persistence directory " + versionDir);
                return null;
            }
        });
    }
    
    private static File getSystemCacheDir() {
        final String os = System.getProperty("os.name", "generic");
        if ("Mac OS X".equals(os)) {
            return new File(new File(System.getProperty("user.home"), "Library"), "Caches");
        }
        if (os.startsWith("Windows")) {
            return new File(System.getProperty("java.io.tmpdir"));
        }
        return new File(System.getProperty("user.home"), ".cache");
    }
    
    public static String getVersionDirName() throws Exception {
        final URL url = OptimisticTypesPersistence.class.getResource("anchor.properties");
        final String protocol = url.getProtocol();
        if (protocol.equals("jar")) {
            final String jarUrlFile = url.getFile();
            final String filePath = jarUrlFile.substring(0, jarUrlFile.indexOf(33));
            final URL file = new URL(filePath);
            try (final InputStream in = file.openStream()) {
                final byte[] buf = new byte[131072];
                final MessageDigest digest = MessageDigest.getInstance("SHA-1");
                while (true) {
                    final int l = in.read(buf);
                    if (l == -1) {
                        break;
                    }
                    digest.update(buf, 0, l);
                }
                return Base64.getUrlEncoder().withoutPadding().encodeToString(digest.digest());
            }
        }
        if (protocol.equals("file")) {
            final String fileStr = url.getFile();
            final String className = OptimisticTypesPersistence.class.getName();
            final int packageNameLen = className.lastIndexOf(46);
            final String dirStr = fileStr.substring(0, fileStr.length() - packageNameLen - 1);
            final File dir = new File(dirStr);
            return "dev-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(getLastModifiedClassFile(dir, 0L)));
        }
        throw new AssertionError();
    }
    
    private static long getLastModifiedClassFile(final File dir, final long max) {
        long currentMax = max;
        for (final File f : dir.listFiles()) {
            if (f.getName().endsWith(".class")) {
                final long lastModified = f.lastModified();
                if (lastModified > currentMax) {
                    currentMax = lastModified;
                }
            }
            else if (f.isDirectory()) {
                final long lastModified = getLastModifiedClassFile(f, currentMax);
                if (lastModified > currentMax) {
                    currentMax = lastModified;
                }
            }
        }
        return currentMax;
    }
    
    private static boolean isSymbolicLink(final File file) {
        if (Files.isSymbolicLink(file.toPath())) {
            getLogger().warning("Directory " + file + " is a symlink");
            return true;
        }
        return false;
    }
    
    private static Object[] createLockArray() {
        final Object[] lockArray = new Object[Runtime.getRuntime().availableProcessors() * 2];
        for (int i = 0; i < lockArray.length; ++i) {
            lockArray[i] = new Object();
        }
        return lockArray;
    }
    
    private static Object getFileLock(final File file) {
        return OptimisticTypesPersistence.locks[(file.hashCode() & Integer.MAX_VALUE) % OptimisticTypesPersistence.locks.length];
    }
    
    private static DebugLogger getLogger() {
        try {
            return Context.getContext().getLogger(RecompilableScriptFunctionData.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return DebugLogger.DISABLED_LOGGER;
        }
    }
    
    private static void scheduleCleanup() {
        if (OptimisticTypesPersistence.MAX_FILES != -1 && OptimisticTypesPersistence.scheduledCleanup.compareAndSet(false, true)) {
            OptimisticTypesPersistence.cleanupTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    OptimisticTypesPersistence.scheduledCleanup.set(false);
                    try {
                        doCleanup();
                    }
                    catch (IOException ex) {}
                }
            }, TimeUnit.SECONDS.toMillis(OptimisticTypesPersistence.CLEANUP_DELAY));
        }
    }
    
    private static void doCleanup() throws IOException {
        final Path[] files = getAllRegularFilesInLastModifiedOrder();
        for (int nFiles = files.length, filesToDelete = Math.max(0, nFiles - OptimisticTypesPersistence.MAX_FILES), filesDeleted = 0, i = 0; i < nFiles && filesDeleted < filesToDelete; ++i) {
            try {
                Files.deleteIfExists(files[i]);
                ++filesDeleted;
            }
            catch (Exception ex) {}
            files[i] = null;
        }
    }
    
    private static Path[] getAllRegularFilesInLastModifiedOrder() throws IOException {
        try (final Stream<Path> filesStream = Files.walk(OptimisticTypesPersistence.baseCacheDir.toPath(), new FileVisitOption[0])) {
            return filesStream.filter(new Predicate<Path>() {
                @Override
                public boolean test(final Path path) {
                    return !Files.isDirectory(path, new LinkOption[0]);
                }
            }).map((Function<? super Path, ?>)new Function<Path, PathAndTime>() {
                @Override
                public PathAndTime apply(final Path path) {
                    return new PathAndTime(path);
                }
            }).sorted().map((Function<? super Object, ?>)new Function<PathAndTime, Path>() {
                @Override
                public Path apply(final PathAndTime pathAndTime) {
                    return pathAndTime.path;
                }
            }).toArray((IntFunction<Path[]>)new IntFunction<Path[]>() {
                @Override
                public Path[] apply(final int length) {
                    return new Path[length];
                }
            });
        }
    }
    
    private static int getMaxFiles() {
        final String str = Options.getStringProperty("nashorn.typeInfo.maxFiles", null);
        if (str == null) {
            return 0;
        }
        if ("unlimited".equals(str)) {
            return -1;
        }
        return Math.max(0, Integer.parseInt(str));
    }
    
    static {
        MAX_FILES = getMaxFiles();
        CLEANUP_DELAY = Math.max(0, Options.getIntProperty("nashorn.typeInfo.cleanupDelaySeconds", 20));
        baseCacheDir = createBaseCacheDir();
        cacheDir = createCacheDir(OptimisticTypesPersistence.baseCacheDir);
        locks = (Object[])((OptimisticTypesPersistence.cacheDir == null) ? null : createLockArray());
        if (OptimisticTypesPersistence.baseCacheDir == null || OptimisticTypesPersistence.MAX_FILES == -1) {
            scheduledCleanup = null;
            cleanupTimer = null;
        }
        else {
            scheduledCleanup = new AtomicBoolean();
            cleanupTimer = new Timer(true);
        }
    }
    
    private static final class LocationDescriptor
    {
        private final File file;
        
        LocationDescriptor(final File file) {
            this.file = file;
        }
    }
    
    private static class PathAndTime implements Comparable<PathAndTime>
    {
        private final Path path;
        private final long time;
        
        PathAndTime(final Path path) {
            this.path = path;
            this.time = getTime(path);
        }
        
        @Override
        public int compareTo(final PathAndTime other) {
            return Long.compare(this.time, other.time);
        }
        
        private static long getTime(final Path path) {
            try {
                return Files.getLastModifiedTime(path, new LinkOption[0]).toMillis();
            }
            catch (IOException e) {
                return -1L;
            }
        }
    }
}
