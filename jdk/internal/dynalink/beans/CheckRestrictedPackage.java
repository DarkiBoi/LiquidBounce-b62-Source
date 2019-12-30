// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;

class CheckRestrictedPackage
{
    private static final AccessControlContext NO_PERMISSIONS_CONTEXT;
    
    static boolean isRestrictedClass(final Class<?> clazz) {
        if (!Modifier.isPublic(clazz.getModifiers())) {
            return true;
        }
        final SecurityManager sm = System.getSecurityManager();
        if (sm == null) {
            return false;
        }
        final String name = clazz.getName();
        final int i = name.lastIndexOf(46);
        if (i == -1) {
            return false;
        }
        try {
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Void>() {
                @Override
                public Void run() {
                    sm.checkPackageAccess(name.substring(0, i));
                    return null;
                }
            }, CheckRestrictedPackage.NO_PERMISSIONS_CONTEXT);
        }
        catch (SecurityException e) {
            return true;
        }
        return false;
    }
    
    private static AccessControlContext createNoPermissionsContext() {
        return new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, new Permissions()) });
    }
    
    static {
        NO_PERMISSIONS_CONTEXT = createNoPermissionsContext();
    }
}
