// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collection;
import jdk.nashorn.internal.runtime.ECMAErrors;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Permission;
import java.security.Permissions;
import java.security.AccessControlContext;

final class ClassAndLoader
{
    private static final AccessControlContext GET_LOADER_ACC_CTXT;
    private final Class<?> representativeClass;
    private ClassLoader loader;
    private boolean loaderRetrieved;
    
    static AccessControlContext createPermAccCtxt(final String... permNames) {
        final Permissions perms = new Permissions();
        for (final String permName : permNames) {
            perms.add(new RuntimePermission(permName));
        }
        return new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, perms) });
    }
    
    ClassAndLoader(final Class<?> representativeClass, final boolean retrieveLoader) {
        this.representativeClass = representativeClass;
        if (retrieveLoader) {
            this.retrieveLoader();
        }
    }
    
    Class<?> getRepresentativeClass() {
        return this.representativeClass;
    }
    
    boolean canSee(final ClassAndLoader other) {
        try {
            final Class<?> otherClass = other.getRepresentativeClass();
            return Class.forName(otherClass.getName(), false, this.getLoader()) == otherClass;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    ClassLoader getLoader() {
        if (!this.loaderRetrieved) {
            this.retrieveLoader();
        }
        return this.getRetrievedLoader();
    }
    
    ClassLoader getRetrievedLoader() {
        assert this.loaderRetrieved;
        return this.loader;
    }
    
    private void retrieveLoader() {
        this.loader = this.representativeClass.getClassLoader();
        this.loaderRetrieved = true;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ClassAndLoader && ((ClassAndLoader)obj).getRetrievedLoader() == this.getRetrievedLoader();
    }
    
    @Override
    public int hashCode() {
        return System.identityHashCode(this.getRetrievedLoader());
    }
    
    static ClassAndLoader getDefiningClassAndLoader(final Class<?>[] types) {
        if (types.length == 1) {
            return new ClassAndLoader(types[0], false);
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassAndLoader>)new PrivilegedAction<ClassAndLoader>() {
            @Override
            public ClassAndLoader run() {
                return ClassAndLoader.getDefiningClassAndLoaderPrivileged(types);
            }
        }, ClassAndLoader.GET_LOADER_ACC_CTXT);
    }
    
    static ClassAndLoader getDefiningClassAndLoaderPrivileged(final Class<?>[] types) {
        final Collection<ClassAndLoader> maximumVisibilityLoaders = getMaximumVisibilityLoaders(types);
        final Iterator<ClassAndLoader> it = maximumVisibilityLoaders.iterator();
        if (maximumVisibilityLoaders.size() == 1) {
            return it.next();
        }
        assert maximumVisibilityLoaders.size() > 1;
        final StringBuilder b = new StringBuilder();
        b.append(it.next().getRepresentativeClass().getCanonicalName());
        while (it.hasNext()) {
            b.append(", ").append(it.next().getRepresentativeClass().getCanonicalName());
        }
        throw ECMAErrors.typeError("extend.ambiguous.defining.class", b.toString());
    }
    
    private static Collection<ClassAndLoader> getMaximumVisibilityLoaders(final Class<?>[] types) {
        final List<ClassAndLoader> maximumVisibilityLoaders = new LinkedList<ClassAndLoader>();
    Label_0018:
        for (final ClassAndLoader maxCandidate : getClassLoadersForTypes(types)) {
            final Iterator<ClassAndLoader> it = maximumVisibilityLoaders.iterator();
            while (it.hasNext()) {
                final ClassAndLoader existingMax = it.next();
                final boolean candidateSeesExisting = maxCandidate.canSee(existingMax);
                final boolean exitingSeesCandidate = existingMax.canSee(maxCandidate);
                if (candidateSeesExisting) {
                    if (exitingSeesCandidate) {
                        continue;
                    }
                    it.remove();
                }
                else {
                    if (exitingSeesCandidate) {
                        continue Label_0018;
                    }
                    continue;
                }
            }
            maximumVisibilityLoaders.add(maxCandidate);
        }
        return maximumVisibilityLoaders;
    }
    
    private static Collection<ClassAndLoader> getClassLoadersForTypes(final Class<?>[] types) {
        final Map<ClassAndLoader, ClassAndLoader> classesAndLoaders = new LinkedHashMap<ClassAndLoader, ClassAndLoader>();
        for (final Class<?> c : types) {
            final ClassAndLoader cl = new ClassAndLoader(c, true);
            if (!classesAndLoaders.containsKey(cl)) {
                classesAndLoaders.put(cl, cl);
            }
        }
        return classesAndLoaders.keySet();
    }
    
    static {
        GET_LOADER_ACC_CTXT = createPermAccCtxt("getClassLoader");
    }
}
