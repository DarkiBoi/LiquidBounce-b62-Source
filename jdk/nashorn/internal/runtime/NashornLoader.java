// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.net.MalformedURLException;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Permissions;
import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.Permission;
import java.security.SecureClassLoader;

abstract class NashornLoader extends SecureClassLoader
{
    private static final String OBJECTS_PKG = "jdk.nashorn.internal.objects";
    private static final String RUNTIME_PKG = "jdk.nashorn.internal.runtime";
    private static final String RUNTIME_ARRAYS_PKG = "jdk.nashorn.internal.runtime.arrays";
    private static final String RUNTIME_LINKER_PKG = "jdk.nashorn.internal.runtime.linker";
    private static final String SCRIPTS_PKG = "jdk.nashorn.internal.scripts";
    private static final Permission[] SCRIPT_PERMISSIONS;
    
    NashornLoader(final ClassLoader parent) {
        super(parent);
    }
    
    protected static void checkPackageAccess(final String name) {
        final int i = name.lastIndexOf(46);
        if (i != -1) {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                final String substring;
                final String pkgName = substring = name.substring(0, i);
                switch (substring) {
                    case "jdk.nashorn.internal.runtime":
                    case "jdk.nashorn.internal.runtime.arrays":
                    case "jdk.nashorn.internal.runtime.linker":
                    case "jdk.nashorn.internal.objects":
                    case "jdk.nashorn.internal.scripts": {
                        break;
                    }
                    default: {
                        sm.checkPackageAccess(pkgName);
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    protected PermissionCollection getPermissions(final CodeSource codesource) {
        final Permissions permCollection = new Permissions();
        for (final Permission perm : NashornLoader.SCRIPT_PERMISSIONS) {
            permCollection.add(perm);
        }
        return permCollection;
    }
    
    static ClassLoader createClassLoader(final String classPath, final ClassLoader parent) {
        final URL[] urls = pathToURLs(classPath);
        return URLClassLoader.newInstance(urls, parent);
    }
    
    private static URL[] pathToURLs(final String path) {
        String[] components;
        URL[] urls;
        int count;
        URL url;
        for (components = path.split(File.pathSeparator), urls = new URL[components.length], count = 0; count < components.length; urls[count++] = url) {
            url = fileToURL(new File(components[count]));
            if (url != null) {}
        }
        if (urls.length != count) {
            final URL[] tmp = new URL[count];
            System.arraycopy(urls, 0, tmp, 0, count);
            urls = tmp;
        }
        return urls;
    }
    
    private static URL fileToURL(final File file) {
        String name;
        try {
            name = file.getCanonicalPath();
        }
        catch (IOException e) {
            name = file.getAbsolutePath();
        }
        name = name.replace(File.separatorChar, '/');
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        if (!file.isFile()) {
            name += "/";
        }
        try {
            return new URL("file", "", name);
        }
        catch (MalformedURLException e2) {
            throw new IllegalArgumentException("file");
        }
    }
    
    static {
        SCRIPT_PERMISSIONS = new Permission[] { new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime.linker"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.objects"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.scripts"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime.arrays") };
    }
}
