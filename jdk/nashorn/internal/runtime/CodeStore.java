// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import jdk.nashorn.internal.codegen.OptimisticTypesPersistence;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import jdk.nashorn.internal.runtime.options.Options;
import java.io.File;
import jdk.nashorn.internal.codegen.types.Type;
import java.io.Serializable;
import java.util.Map;
import java.util.Iterator;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.ServiceLoader;
import java.security.Permission;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;

@Logger(name = "codestore")
public abstract class CodeStore implements Loggable
{
    public static final String NASHORN_PROVIDE_CODE_STORE = "nashorn.provideCodeStore";
    private DebugLogger log;
    
    protected CodeStore() {
    }
    
    @Override
    public DebugLogger initLogger(final Context context) {
        return this.log = context.getLogger(this.getClass());
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    public static CodeStore newCodeStore(final Context context) {
        final Class<CodeStore> baseClass = CodeStore.class;
        try {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new RuntimePermission("nashorn.provideCodeStore"));
            }
            final ServiceLoader<CodeStore> services = ServiceLoader.load(baseClass);
            final Iterator<CodeStore> iterator = services.iterator();
            if (iterator.hasNext()) {
                final CodeStore store = iterator.next();
                store.initLogger(context).info("using code store provider ", store.getClass().getCanonicalName());
                return store;
            }
        }
        catch (AccessControlException e) {
            context.getLogger(CodeStore.class).warning("failed to load code store provider ", e);
        }
        try {
            final CodeStore store2 = new DirectoryCodeStore(context);
            store2.initLogger(context);
            return store2;
        }
        catch (IOException e2) {
            context.getLogger(CodeStore.class).warning("failed to create cache directory ", e2);
            return null;
        }
    }
    
    public StoredScript store(final String functionKey, final Source source, final String mainClassName, final Map<String, byte[]> classBytes, final Map<Integer, FunctionInitializer> initializers, final Object[] constants, final int compilationId) {
        return this.store(functionKey, source, this.storedScriptFor(source, mainClassName, classBytes, initializers, constants, compilationId));
    }
    
    public abstract StoredScript store(final String p0, final Source p1, final StoredScript p2);
    
    public abstract StoredScript load(final Source p0, final String p1);
    
    public StoredScript storedScriptFor(final Source source, final String mainClassName, final Map<String, byte[]> classBytes, final Map<Integer, FunctionInitializer> initializers, final Object[] constants, final int compilationId) {
        for (final Object constant : constants) {
            if (!(constant instanceof Serializable)) {
                this.getLogger().warning("cannot store ", source, " non serializable constant ", constant);
                return null;
            }
        }
        return new StoredScript(compilationId, mainClassName, classBytes, initializers, constants);
    }
    
    public static String getCacheKey(final Object functionId, final Type[] paramTypes) {
        final StringBuilder b = new StringBuilder().append(functionId);
        if (paramTypes != null && paramTypes.length > 0) {
            b.append('-');
            for (final Type t : paramTypes) {
                b.append(Type.getShortSignatureDescriptor(t));
            }
        }
        return b.toString();
    }
    
    public static class DirectoryCodeStore extends CodeStore
    {
        private static final int DEFAULT_MIN_SIZE = 1000;
        private final File dir;
        private final boolean readOnly;
        private final int minSize;
        
        public DirectoryCodeStore(final Context context) throws IOException {
            this(context, Options.getStringProperty("nashorn.persistent.code.cache", "nashorn_code_cache"), false, 1000);
        }
        
        public DirectoryCodeStore(final Context context, final String path, final boolean readOnly, final int minSize) throws IOException {
            this.dir = checkDirectory(path, context.getEnv(), readOnly);
            this.readOnly = readOnly;
            this.minSize = minSize;
        }
        
        private static File checkDirectory(final String path, final ScriptEnvironment env, final boolean readOnly) throws IOException {
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<File>)new PrivilegedExceptionAction<File>() {
                    @Override
                    public File run() throws IOException {
                        final File dir = new File(path, getVersionDir(env)).getAbsoluteFile();
                        if (readOnly) {
                            if (!dir.exists() || !dir.isDirectory()) {
                                throw new IOException("Not a directory: " + dir.getPath());
                            }
                            if (!dir.canRead()) {
                                throw new IOException("Directory not readable: " + dir.getPath());
                            }
                        }
                        else {
                            if (!dir.exists() && !dir.mkdirs()) {
                                throw new IOException("Could not create directory: " + dir.getPath());
                            }
                            if (!dir.isDirectory()) {
                                throw new IOException("Not a directory: " + dir.getPath());
                            }
                            if (!dir.canRead() || !dir.canWrite()) {
                                throw new IOException("Directory not readable or writable: " + dir.getPath());
                            }
                        }
                        return dir;
                    }
                });
            }
            catch (PrivilegedActionException e) {
                throw (IOException)e.getException();
            }
        }
        
        private static String getVersionDir(final ScriptEnvironment env) throws IOException {
            try {
                final String versionDir = OptimisticTypesPersistence.getVersionDirName();
                return env._optimistic_types ? (versionDir + "_opt") : versionDir;
            }
            catch (Exception e) {
                throw new IOException(e);
            }
        }
        
        @Override
        public StoredScript load(final Source source, final String functionKey) {
            if (this.belowThreshold(source)) {
                return null;
            }
            final File file = this.getCacheFile(source, functionKey);
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<StoredScript>)new PrivilegedExceptionAction<StoredScript>() {
                    @Override
                    public StoredScript run() throws IOException, ClassNotFoundException {
                        if (!file.exists()) {
                            return null;
                        }
                        try (final ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                            final StoredScript storedScript = (StoredScript)in.readObject();
                            DirectoryCodeStore.this.getLogger().info("loaded ", source, "-", functionKey);
                            return storedScript;
                        }
                    }
                });
            }
            catch (PrivilegedActionException e) {
                this.getLogger().warning("failed to load ", source, "-", functionKey, ": ", e.getException());
                return null;
            }
        }
        
        @Override
        public StoredScript store(final String functionKey, final Source source, final StoredScript script) {
            if (this.readOnly || script == null || this.belowThreshold(source)) {
                return null;
            }
            final File file = this.getCacheFile(source, functionKey);
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<StoredScript>)new PrivilegedExceptionAction<StoredScript>() {
                    @Override
                    public StoredScript run() throws IOException {
                        try (final ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                            out.writeObject(script);
                        }
                        DirectoryCodeStore.this.getLogger().info("stored ", source, "-", functionKey);
                        return script;
                    }
                });
            }
            catch (PrivilegedActionException e) {
                this.getLogger().warning("failed to store ", script, "-", functionKey, ": ", e.getException());
                return null;
            }
        }
        
        private File getCacheFile(final Source source, final String functionKey) {
            return new File(this.dir, source.getDigest() + '-' + functionKey);
        }
        
        private boolean belowThreshold(final Source source) {
            if (source.getLength() < this.minSize) {
                this.getLogger().info("below size threshold ", source);
                return true;
            }
            return false;
        }
    }
}
