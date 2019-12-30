// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.Permission;
import java.security.Permissions;
import java.security.AccessControlContext;

public class ClassLoaderGetterContextProvider
{
    public static final AccessControlContext GET_CLASS_LOADER_CONTEXT;
    
    static {
        final Permissions perms = new Permissions();
        perms.add(new RuntimePermission("getClassLoader"));
        GET_CLASS_LOADER_CONTEXT = new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, perms) });
    }
}
