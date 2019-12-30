// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.support.TypeUtilities;
import jdk.internal.dynalink.linker.ConversionComparator;
import java.lang.invoke.MethodType;
import java.util.Iterator;
import java.util.LinkedList;
import jdk.internal.dynalink.linker.LinkerServices;
import java.util.List;
import java.lang.invoke.MethodHandle;

class MaximallySpecific
{
    private static final MethodTypeGetter<MethodHandle> METHOD_HANDLE_TYPE_GETTER;
    private static final MethodTypeGetter<SingleDynamicMethod> DYNAMIC_METHOD_TYPE_GETTER;
    static final /* synthetic */ boolean $assertionsDisabled;
    
    static List<SingleDynamicMethod> getMaximallySpecificMethods(final List<SingleDynamicMethod> methods, final boolean varArgs) {
        return getMaximallySpecificSingleDynamicMethods(methods, varArgs, null, null);
    }
    
    static List<MethodHandle> getMaximallySpecificMethodHandles(final List<MethodHandle> methods, final boolean varArgs, final Class<?>[] argTypes, final LinkerServices ls) {
        return getMaximallySpecificMethods(methods, varArgs, argTypes, ls, MaximallySpecific.METHOD_HANDLE_TYPE_GETTER);
    }
    
    static List<SingleDynamicMethod> getMaximallySpecificSingleDynamicMethods(final List<SingleDynamicMethod> methods, final boolean varArgs, final Class<?>[] argTypes, final LinkerServices ls) {
        return getMaximallySpecificMethods(methods, varArgs, argTypes, ls, MaximallySpecific.DYNAMIC_METHOD_TYPE_GETTER);
    }
    
    private static <T> List<T> getMaximallySpecificMethods(final List<T> methods, final boolean varArgs, final Class<?>[] argTypes, final LinkerServices ls, final MethodTypeGetter<T> methodTypeGetter) {
        if (methods.size() < 2) {
            return methods;
        }
        final LinkedList<T> maximals = new LinkedList<T>();
        for (final T m : methods) {
            final MethodType methodType = methodTypeGetter.getMethodType(m);
            boolean lessSpecific = false;
            final Iterator<T> maximal = maximals.iterator();
            while (maximal.hasNext()) {
                final T max = maximal.next();
                switch (isMoreSpecific(methodType, methodTypeGetter.getMethodType(max), varArgs, argTypes, ls)) {
                    case TYPE_1_BETTER: {
                        maximal.remove();
                        continue;
                    }
                    case TYPE_2_BETTER: {
                        lessSpecific = true;
                        continue;
                    }
                    case INDETERMINATE: {
                        continue;
                    }
                    default: {
                        throw new AssertionError();
                    }
                }
            }
            if (!lessSpecific) {
                maximals.addLast(m);
            }
        }
        return maximals;
    }
    
    private static ConversionComparator.Comparison isMoreSpecific(final MethodType t1, final MethodType t2, final boolean varArgs, final Class<?>[] argTypes, final LinkerServices ls) {
        final int pc1 = t1.parameterCount();
        final int pc2 = t2.parameterCount();
        if (!MaximallySpecific.$assertionsDisabled && !varArgs && (pc1 != pc2 || (argTypes != null && argTypes.length != pc1))) {
            throw new AssertionError();
        }
        assert argTypes == null == (ls == null);
        final int maxPc = Math.max(Math.max(pc1, pc2), (argTypes == null) ? 0 : argTypes.length);
        boolean t1MoreSpecific = false;
        boolean t2MoreSpecific = false;
        for (int i = 1; i < maxPc; ++i) {
            final Class<?> c1 = getParameterClass(t1, pc1, i, varArgs);
            final Class<?> c2 = getParameterClass(t2, pc2, i, varArgs);
            if (c1 != c2) {
                final ConversionComparator.Comparison cmp = compare(c1, c2, argTypes, i, ls);
                if (cmp == ConversionComparator.Comparison.TYPE_1_BETTER && !t1MoreSpecific) {
                    t1MoreSpecific = true;
                    if (t2MoreSpecific) {
                        return ConversionComparator.Comparison.INDETERMINATE;
                    }
                }
                if (cmp == ConversionComparator.Comparison.TYPE_2_BETTER && !t2MoreSpecific) {
                    t2MoreSpecific = true;
                    if (t1MoreSpecific) {
                        return ConversionComparator.Comparison.INDETERMINATE;
                    }
                }
            }
        }
        if (t1MoreSpecific) {
            return ConversionComparator.Comparison.TYPE_1_BETTER;
        }
        if (t2MoreSpecific) {
            return ConversionComparator.Comparison.TYPE_2_BETTER;
        }
        return ConversionComparator.Comparison.INDETERMINATE;
    }
    
    private static ConversionComparator.Comparison compare(final Class<?> c1, final Class<?> c2, final Class<?>[] argTypes, final int i, final LinkerServices cmp) {
        if (cmp != null) {
            final ConversionComparator.Comparison c3 = cmp.compareConversion(argTypes[i], c1, c2);
            if (c3 != ConversionComparator.Comparison.INDETERMINATE) {
                return c3;
            }
        }
        if (TypeUtilities.isSubtype(c1, c2)) {
            return ConversionComparator.Comparison.TYPE_1_BETTER;
        }
        if (TypeUtilities.isSubtype(c2, c1)) {
            return ConversionComparator.Comparison.TYPE_2_BETTER;
        }
        return ConversionComparator.Comparison.INDETERMINATE;
    }
    
    private static Class<?> getParameterClass(final MethodType t, final int l, final int i, final boolean varArgs) {
        return (varArgs && i >= l - 1) ? t.parameterType(l - 1).getComponentType() : t.parameterType(i);
    }
    
    static {
        METHOD_HANDLE_TYPE_GETTER = new MethodTypeGetter<MethodHandle>() {
            @Override
            MethodType getMethodType(final MethodHandle t) {
                return t.type();
            }
        };
        DYNAMIC_METHOD_TYPE_GETTER = new MethodTypeGetter<SingleDynamicMethod>() {
            @Override
            MethodType getMethodType(final SingleDynamicMethod t) {
                return t.getMethodType();
            }
        };
    }
    
    private abstract static class MethodTypeGetter<T>
    {
        abstract MethodType getMethodType(final T p0);
    }
}
