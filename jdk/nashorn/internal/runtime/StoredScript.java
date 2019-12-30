// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.util.Arrays;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public final class StoredScript implements Serializable
{
    private final int compilationId;
    private final String mainClassName;
    private final Map<String, byte[]> classBytes;
    private final Object[] constants;
    private final Map<Integer, FunctionInitializer> initializers;
    private static final long serialVersionUID = 2958227232195298340L;
    
    public StoredScript(final int compilationId, final String mainClassName, final Map<String, byte[]> classBytes, final Map<Integer, FunctionInitializer> initializers, final Object[] constants) {
        this.compilationId = compilationId;
        this.mainClassName = mainClassName;
        this.classBytes = classBytes;
        this.constants = constants;
        this.initializers = initializers;
    }
    
    public int getCompilationId() {
        return this.compilationId;
    }
    
    private Map<String, Class<?>> installClasses(final Source source, final CodeInstaller installer) {
        final Map<String, Class<?>> installedClasses = new HashMap<String, Class<?>>();
        final byte[] mainClassBytes = this.classBytes.get(this.mainClassName);
        final Class<?> mainClass = installer.install(this.mainClassName, mainClassBytes);
        installedClasses.put(this.mainClassName, mainClass);
        for (final Map.Entry<String, byte[]> entry : this.classBytes.entrySet()) {
            final String className = entry.getKey();
            if (!className.equals(this.mainClassName)) {
                installedClasses.put(className, installer.install(className, entry.getValue()));
            }
        }
        installer.initialize(installedClasses.values(), source, this.constants);
        return installedClasses;
    }
    
    FunctionInitializer installFunction(final RecompilableScriptFunctionData data, final CodeInstaller installer) {
        final Map<String, Class<?>> installedClasses = this.installClasses(data.getSource(), installer);
        assert this.initializers != null;
        assert this.initializers.size() == 1;
        final FunctionInitializer initializer = this.initializers.values().iterator().next();
        for (int i = 0; i < this.constants.length; ++i) {
            if (this.constants[i] instanceof RecompilableScriptFunctionData) {
                final RecompilableScriptFunctionData newData = data.getScriptFunctionData(((RecompilableScriptFunctionData)this.constants[i]).getFunctionNodeId());
                assert newData != null;
                newData.initTransients(data.getSource(), installer);
                this.constants[i] = newData;
            }
        }
        initializer.setCode(installedClasses.get(initializer.getClassName()));
        return initializer;
    }
    
    Class<?> installScript(final Source source, final CodeInstaller installer) {
        final Map<String, Class<?>> installedClasses = this.installClasses(source, installer);
        for (final Object constant : this.constants) {
            if (constant instanceof RecompilableScriptFunctionData) {
                final RecompilableScriptFunctionData data = (RecompilableScriptFunctionData)constant;
                data.initTransients(source, installer);
                final FunctionInitializer initializer = this.initializers.get(data.getFunctionNodeId());
                if (initializer != null) {
                    initializer.setCode(installedClasses.get(initializer.getClassName()));
                    data.initializeCode(initializer);
                }
            }
        }
        return installedClasses.get(this.mainClassName);
    }
    
    @Override
    public int hashCode() {
        int hash = this.mainClassName.hashCode();
        hash = 31 * hash + this.classBytes.hashCode();
        hash = 31 * hash + Arrays.hashCode(this.constants);
        return hash;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StoredScript)) {
            return false;
        }
        final StoredScript cs = (StoredScript)obj;
        return this.mainClassName.equals(cs.mainClassName) && this.classBytes.equals(cs.classBytes) && Arrays.equals(this.constants, cs.constants);
    }
}
