// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.Property;
import java.util.Collection;
import java.util.Collections;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.NativeJavaPackage;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeJavaImporter extends ScriptObject
{
    private final Object[] args;
    private static PropertyMap $nasgenmap$;
    
    private NativeJavaImporter(final Object[] args, final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        this.args = args;
    }
    
    private NativeJavaImporter(final Object[] args, final Global global) {
        this(args, global.getJavaImporterPrototype(), NativeJavaImporter.$nasgenmap$);
    }
    
    private NativeJavaImporter(final Object[] args) {
        this(args, Global.instance());
    }
    
    @Override
    public String getClassName() {
        return "JavaImporter";
    }
    
    public static NativeJavaImporter constructor(final boolean isNew, final Object self, final Object... args) {
        return new NativeJavaImporter(args);
    }
    
    public static Object __noSuchProperty__(final Object self, final Object name) {
        if (!(self instanceof NativeJavaImporter)) {
            throw ECMAErrors.typeError("not.a.java.importer", ScriptRuntime.safeToString(self));
        }
        return ((NativeJavaImporter)self).createProperty(JSType.toString(name));
    }
    
    public static Object __noSuchMethod__(final Object self, final Object... args) {
        throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(args[0]));
    }
    
    @Override
    public GuardedInvocation noSuchProperty(final CallSiteDescriptor desc, final LinkRequest request) {
        return this.createAndSetProperty(desc) ? super.lookup(desc, request) : super.noSuchProperty(desc, request);
    }
    
    @Override
    public GuardedInvocation noSuchMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        return this.createAndSetProperty(desc) ? super.lookup(desc, request) : super.noSuchMethod(desc, request);
    }
    
    @Override
    protected Object invokeNoSuchProperty(final String name, final boolean isScope, final int programPoint) {
        final Object retval = this.createProperty(name);
        if (UnwarrantedOptimismException.isValid(programPoint)) {
            throw new UnwarrantedOptimismException(retval, programPoint);
        }
        return retval;
    }
    
    private boolean createAndSetProperty(final CallSiteDescriptor desc) {
        final String name = desc.getNameToken(2);
        final Object value = this.createProperty(name);
        if (value != null) {
            this.set(name, value, 0);
            return true;
        }
        return false;
    }
    
    private Object createProperty(final String name) {
        final int len = this.args.length;
        for (int i = len - 1; i > -1; --i) {
            final Object obj = this.args[i];
            if (obj instanceof StaticClass) {
                if (((StaticClass)obj).getRepresentedClass().getSimpleName().equals(name)) {
                    return obj;
                }
            }
            else if (obj instanceof NativeJavaPackage) {
                final String pkgName = ((NativeJavaPackage)obj).getName();
                final String fullName = pkgName.isEmpty() ? name : (pkgName + "." + name);
                final Context context = Global.instance().getContext();
                try {
                    return StaticClass.forClass(context.findClass(fullName));
                }
                catch (ClassNotFoundException ex) {}
            }
        }
        return null;
    }
    
    static {
        $clinit$();
    }
    
    public static void $clinit$() {
        NativeJavaImporter.$nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}
