// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.beans.StaticClass;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.objects.annotations.Function;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;

public final class NativeJavaPackage extends ScriptObject
{
    private static final MethodHandleFunctionality MH;
    private static final MethodHandle CLASS_NOT_FOUND;
    private static final MethodHandle TYPE_GUARD;
    private final String name;
    
    public NativeJavaPackage(final String name, final ScriptObject proto) {
        super(proto, null);
        Context.checkPackageAccess(name);
        this.name = name;
    }
    
    @Override
    public String getClassName() {
        return "JavaPackage";
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof NativeJavaPackage && this.name.equals(((NativeJavaPackage)other).name);
    }
    
    @Override
    public int hashCode() {
        return (this.name == null) ? 0 : this.name.hashCode();
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String safeToString() {
        return this.toString();
    }
    
    @Override
    public String toString() {
        return "[JavaPackage " + this.name + "]";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (hint == String.class) {
            return this.toString();
        }
        return super.getDefaultValue(hint);
    }
    
    @Override
    protected GuardedInvocation findNewMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        return createClassNotFoundInvocation(desc);
    }
    
    @Override
    protected GuardedInvocation findCallMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        return createClassNotFoundInvocation(desc);
    }
    
    private static GuardedInvocation createClassNotFoundInvocation(final CallSiteDescriptor desc) {
        final MethodType type = desc.getMethodType();
        return new GuardedInvocation(NativeJavaPackage.MH.dropArguments(NativeJavaPackage.CLASS_NOT_FOUND, 1, type.parameterList().subList(1, type.parameterCount())), (type.parameterType(0) == NativeJavaPackage.class) ? null : NativeJavaPackage.TYPE_GUARD);
    }
    
    private static void classNotFound(final NativeJavaPackage pkg) throws ClassNotFoundException {
        throw new ClassNotFoundException(pkg.name);
    }
    
    @Function(attributes = 2)
    public static Object __noSuchProperty__(final Object self, final Object name) {
        throw new AssertionError((Object)"__noSuchProperty__ placeholder called");
    }
    
    @Function(attributes = 2)
    public static Object __noSuchMethod__(final Object self, final Object... args) {
        throw new AssertionError((Object)"__noSuchMethod__ placeholder called");
    }
    
    @Override
    public GuardedInvocation noSuchProperty(final CallSiteDescriptor desc, final LinkRequest request) {
        final String propertyName = desc.getNameToken(2);
        this.createProperty(propertyName);
        return super.lookup(desc, request);
    }
    
    @Override
    protected Object invokeNoSuchProperty(final String key, final boolean isScope, final int programPoint) {
        final Object retval = this.createProperty(key);
        if (UnwarrantedOptimismException.isValid(programPoint)) {
            throw new UnwarrantedOptimismException(retval, programPoint);
        }
        return retval;
    }
    
    @Override
    public GuardedInvocation noSuchMethod(final CallSiteDescriptor desc, final LinkRequest request) {
        return this.noSuchProperty(desc, request);
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return NativeJavaPackage.MH.findStatic(MethodHandles.lookup(), NativeJavaPackage.class, name, NativeJavaPackage.MH.type(rtype, types));
    }
    
    private Object createProperty(final String propertyName) {
        final String fullName = this.name.isEmpty() ? propertyName : (this.name + "." + propertyName);
        final Context context = Context.getContextTrusted();
        Class<?> javaClass = null;
        try {
            javaClass = context.findClass(fullName);
        }
        catch (NoClassDefFoundError noClassDefFoundError) {}
        catch (ClassNotFoundException ex) {}
        final int openBrace = propertyName.indexOf(40);
        final int closeBrace = propertyName.lastIndexOf(41);
        if (openBrace == -1 && closeBrace == -1) {
            Object propertyValue;
            if (javaClass == null) {
                propertyValue = new NativeJavaPackage(fullName, this.getProto());
            }
            else {
                propertyValue = StaticClass.forClass(javaClass);
            }
            this.set(propertyName, propertyValue, 0);
            return propertyValue;
        }
        final int lastChar = propertyName.length() - 1;
        if (openBrace == -1 || closeBrace != lastChar) {
            throw ECMAErrors.typeError("improper.constructor.signature", propertyName);
        }
        final String className = this.name + "." + propertyName.substring(0, openBrace);
        try {
            javaClass = context.findClass(className);
        }
        catch (NoClassDefFoundError | ClassNotFoundException noClassDefFoundError2) {
            final Throwable t;
            final Throwable e = t;
            throw ECMAErrors.typeError(e, "no.such.java.class", className);
        }
        final Object constructor = BeansLinker.getConstructorMethod(javaClass, propertyName.substring(openBrace + 1, lastChar));
        if (constructor != null) {
            this.set(propertyName, constructor, 0);
            return constructor;
        }
        throw ECMAErrors.typeError("no.such.java.constructor", propertyName);
    }
    
    static {
        MH = MethodHandleFactory.getFunctionality();
        CLASS_NOT_FOUND = findOwnMH("classNotFound", Void.TYPE, NativeJavaPackage.class);
        TYPE_GUARD = Guards.getClassGuard(NativeJavaPackage.class);
    }
}
