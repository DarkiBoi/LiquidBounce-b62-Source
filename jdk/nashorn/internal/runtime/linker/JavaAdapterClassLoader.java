// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.codegen.DumpBytecode;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.runtime.Context;
import java.security.SecureClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import jdk.internal.dynalink.beans.StaticClass;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.security.AccessControlContext;

final class JavaAdapterClassLoader
{
    private static final AccessControlContext CREATE_LOADER_ACC_CTXT;
    private static final AccessControlContext GET_CONTEXT_ACC_CTXT;
    private static final Collection<String> VISIBLE_INTERNAL_CLASS_NAMES;
    private final String className;
    private final byte[] classBytes;
    
    JavaAdapterClassLoader(final String className, final byte[] classBytes) {
        this.className = className.replace('/', '.');
        this.classBytes = classBytes;
    }
    
    StaticClass generateClass(final ClassLoader parentLoader, final ProtectionDomain protectionDomain) {
        assert protectionDomain != null;
        return AccessController.doPrivileged((PrivilegedAction<StaticClass>)new PrivilegedAction<StaticClass>() {
            @Override
            public StaticClass run() {
                try {
                    return StaticClass.forClass(Class.forName(JavaAdapterClassLoader.this.className, true, JavaAdapterClassLoader.this.createClassLoader(parentLoader, protectionDomain)));
                }
                catch (ClassNotFoundException e) {
                    throw new AssertionError((Object)e);
                }
            }
        }, JavaAdapterClassLoader.CREATE_LOADER_ACC_CTXT);
    }
    
    private ClassLoader createClassLoader(final ClassLoader parentLoader, final ProtectionDomain protectionDomain) {
        return new SecureClassLoader(parentLoader) {
            private final ClassLoader myLoader = this.getClass().getClassLoader();
            
            public Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
                try {
                    Context.checkPackageAccess(name);
                    return super.loadClass(name, resolve);
                }
                catch (SecurityException se) {
                    if (JavaAdapterClassLoader.VISIBLE_INTERNAL_CLASS_NAMES.contains(name)) {
                        return this.myLoader.loadClass(name);
                    }
                    throw se;
                }
            }
            
            @Override
            protected Class<?> findClass(final String name) throws ClassNotFoundException {
                if (!name.equals(JavaAdapterClassLoader.this.className)) {
                    throw new ClassNotFoundException(name);
                }
                assert JavaAdapterClassLoader.this.classBytes != null : "what? already cleared .class bytes!!";
                final Context ctx = AccessController.doPrivileged((PrivilegedAction<Context>)new PrivilegedAction<Context>() {
                    @Override
                    public Context run() {
                        return Context.getContext();
                    }
                }, JavaAdapterClassLoader.GET_CONTEXT_ACC_CTXT);
                DumpBytecode.dumpBytecode(ctx.getEnv(), ctx.getLogger(Compiler.class), JavaAdapterClassLoader.this.classBytes, name);
                return this.defineClass(name, JavaAdapterClassLoader.this.classBytes, 0, JavaAdapterClassLoader.this.classBytes.length, protectionDomain);
            }
        };
    }
    
    static {
        CREATE_LOADER_ACC_CTXT = ClassAndLoader.createPermAccCtxt("createClassLoader");
        GET_CONTEXT_ACC_CTXT = ClassAndLoader.createPermAccCtxt("nashorn.getContext");
        VISIBLE_INTERNAL_CLASS_NAMES = Collections.unmodifiableCollection((Collection<? extends String>)new HashSet<String>(Arrays.asList(JavaAdapterServices.class.getName(), ScriptObject.class.getName(), ScriptFunction.class.getName(), JSType.class.getName())));
    }
}
