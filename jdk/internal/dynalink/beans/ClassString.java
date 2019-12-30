// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.support.TypeUtilities;
import java.util.Iterator;
import java.util.LinkedList;
import jdk.internal.dynalink.linker.LinkerServices;
import java.lang.invoke.MethodHandle;
import java.util.List;
import jdk.internal.dynalink.support.Guards;
import java.lang.invoke.MethodType;

final class ClassString
{
    static final Class<?> NULL_CLASS;
    private final Class<?>[] classes;
    private int hashCode;
    
    ClassString(final Class<?>[] classes) {
        this.classes = classes;
    }
    
    ClassString(final MethodType type) {
        this(type.parameterArray());
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ClassString)) {
            return false;
        }
        final Class<?>[] otherClasses = ((ClassString)other).classes;
        if (otherClasses.length != this.classes.length) {
            return false;
        }
        for (int i = 0; i < otherClasses.length; ++i) {
            if (otherClasses[i] != this.classes[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            int h = 0;
            for (int i = 0; i < this.classes.length; ++i) {
                h ^= this.classes[i].hashCode();
            }
            this.hashCode = h;
        }
        return this.hashCode;
    }
    
    boolean isVisibleFrom(final ClassLoader classLoader) {
        for (int i = 0; i < this.classes.length; ++i) {
            if (!Guards.canReferenceDirectly(classLoader, this.classes[i].getClassLoader())) {
                return false;
            }
        }
        return true;
    }
    
    List<MethodHandle> getMaximallySpecifics(final List<MethodHandle> methods, final LinkerServices linkerServices, final boolean varArg) {
        return MaximallySpecific.getMaximallySpecificMethodHandles(this.getApplicables(methods, linkerServices, varArg), varArg, this.classes, linkerServices);
    }
    
    LinkedList<MethodHandle> getApplicables(final List<MethodHandle> methods, final LinkerServices linkerServices, final boolean varArg) {
        final LinkedList<MethodHandle> list = new LinkedList<MethodHandle>();
        for (final MethodHandle member : methods) {
            if (this.isApplicable(member, linkerServices, varArg)) {
                list.add(member);
            }
        }
        return list;
    }
    
    private boolean isApplicable(final MethodHandle method, final LinkerServices linkerServices, final boolean varArg) {
        final Class<?>[] formalTypes = method.type().parameterArray();
        final int cl = this.classes.length;
        final int fl = formalTypes.length - (varArg ? 1 : 0);
        if (varArg) {
            if (cl < fl) {
                return false;
            }
        }
        else if (cl != fl) {
            return false;
        }
        for (int i = 1; i < fl; ++i) {
            if (!canConvert(linkerServices, this.classes[i], formalTypes[i])) {
                return false;
            }
        }
        if (varArg) {
            final Class<?> varArgType = formalTypes[fl].getComponentType();
            for (int j = fl; j < cl; ++j) {
                if (!canConvert(linkerServices, this.classes[j], varArgType)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean canConvert(final LinkerServices ls, final Class<?> from, final Class<?> to) {
        if (from == ClassString.NULL_CLASS) {
            return !to.isPrimitive();
        }
        return (ls == null) ? TypeUtilities.isMethodInvocationConvertible(from, to) : ls.canConvert(from, to);
    }
    
    static {
        NULL_CLASS = new Object() {}.getClass();
    }
}
