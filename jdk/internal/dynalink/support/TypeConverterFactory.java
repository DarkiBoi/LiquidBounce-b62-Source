// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardedTypeConversion;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.WrongMethodTypeException;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.lang.invoke.MethodType;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.MethodTypeConversionStrategy;
import jdk.internal.dynalink.linker.ConversionComparator;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;

public class TypeConverterFactory
{
    private final GuardingTypeConverterFactory[] factories;
    private final ConversionComparator[] comparators;
    private final MethodTypeConversionStrategy autoConversionStrategy;
    private final ClassValue<ClassMap<MethodHandle>> converterMap;
    private final ClassValue<ClassMap<MethodHandle>> converterIdentityMap;
    private final ClassValue<ClassMap<Boolean>> canConvert;
    static final MethodHandle IDENTITY_CONVERSION;
    
    private static final ClassLoader getClassLoader(final Class<?> clazz) {
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        }, ClassLoaderGetterContextProvider.GET_CLASS_LOADER_CONTEXT);
    }
    
    public TypeConverterFactory(final Iterable<? extends GuardingTypeConverterFactory> factories, final MethodTypeConversionStrategy autoConversionStrategy) {
        this.converterMap = new ClassValue<ClassMap<MethodHandle>>() {
            @Override
            protected ClassMap<MethodHandle> computeValue(final Class<?> sourceType) {
                return new ClassMap<MethodHandle>(getClassLoader(sourceType)) {
                    @Override
                    protected MethodHandle computeValue(final Class<?> targetType) {
                        try {
                            return TypeConverterFactory.this.createConverter(sourceType, targetType);
                        }
                        catch (RuntimeException e) {
                            throw e;
                        }
                        catch (Exception e2) {
                            throw new RuntimeException(e2);
                        }
                    }
                };
            }
        };
        this.converterIdentityMap = new ClassValue<ClassMap<MethodHandle>>() {
            @Override
            protected ClassMap<MethodHandle> computeValue(final Class<?> sourceType) {
                return new ClassMap<MethodHandle>(getClassLoader(sourceType)) {
                    @Override
                    protected MethodHandle computeValue(final Class<?> targetType) {
                        if (!TypeConverterFactory.canAutoConvert(sourceType, targetType)) {
                            final MethodHandle converter = TypeConverterFactory.this.getCacheableTypeConverter(sourceType, targetType);
                            if (converter != TypeConverterFactory.IDENTITY_CONVERSION) {
                                return converter;
                            }
                        }
                        return TypeConverterFactory.IDENTITY_CONVERSION.asType(MethodType.methodType(targetType, sourceType));
                    }
                };
            }
        };
        this.canConvert = new ClassValue<ClassMap<Boolean>>() {
            @Override
            protected ClassMap<Boolean> computeValue(final Class<?> sourceType) {
                return new ClassMap<Boolean>(getClassLoader(sourceType)) {
                    @Override
                    protected Boolean computeValue(final Class<?> targetType) {
                        try {
                            return TypeConverterFactory.this.getTypeConverterNull(sourceType, targetType) != null;
                        }
                        catch (RuntimeException e) {
                            throw e;
                        }
                        catch (Exception e2) {
                            throw new RuntimeException(e2);
                        }
                    }
                };
            }
        };
        final List<GuardingTypeConverterFactory> l = new LinkedList<GuardingTypeConverterFactory>();
        final List<ConversionComparator> c = new LinkedList<ConversionComparator>();
        for (final GuardingTypeConverterFactory factory : factories) {
            l.add(factory);
            if (factory instanceof ConversionComparator) {
                c.add((ConversionComparator)factory);
            }
        }
        this.factories = l.toArray(new GuardingTypeConverterFactory[l.size()]);
        this.comparators = c.toArray(new ConversionComparator[c.size()]);
        this.autoConversionStrategy = autoConversionStrategy;
    }
    
    public MethodHandle asType(final MethodHandle handle, final MethodType fromType) {
        MethodHandle newHandle = handle;
        final MethodType toType = newHandle.type();
        final int l = toType.parameterCount();
        if (l != fromType.parameterCount()) {
            throw new WrongMethodTypeException("Parameter counts differ: " + handle.type() + " vs. " + fromType);
        }
        int pos = 0;
        final List<MethodHandle> converters = new LinkedList<MethodHandle>();
        for (int i = 0; i < l; ++i) {
            final Class<?> fromParamType = fromType.parameterType(i);
            final Class<?> toParamType = toType.parameterType(i);
            if (canAutoConvert(fromParamType, toParamType)) {
                newHandle = applyConverters(newHandle, pos, converters);
            }
            else {
                final MethodHandle converter = this.getTypeConverterNull(fromParamType, toParamType);
                if (converter != null) {
                    if (converters.isEmpty()) {
                        pos = i;
                    }
                    converters.add(converter);
                }
                else {
                    newHandle = applyConverters(newHandle, pos, converters);
                }
            }
        }
        newHandle = applyConverters(newHandle, pos, converters);
        final Class<?> fromRetType = fromType.returnType();
        final Class<?> toRetType = toType.returnType();
        if (fromRetType != Void.TYPE && toRetType != Void.TYPE && !canAutoConvert(toRetType, fromRetType)) {
            final MethodHandle converter2 = this.getTypeConverterNull(toRetType, fromRetType);
            if (converter2 != null) {
                newHandle = MethodHandles.filterReturnValue(newHandle, converter2);
            }
        }
        final MethodHandle autoConvertedHandle = (this.autoConversionStrategy != null) ? this.autoConversionStrategy.asType(newHandle, fromType) : newHandle;
        return autoConvertedHandle.asType(fromType);
    }
    
    private static MethodHandle applyConverters(final MethodHandle handle, final int pos, final List<MethodHandle> converters) {
        if (converters.isEmpty()) {
            return handle;
        }
        final MethodHandle newHandle = MethodHandles.filterArguments(handle, pos, (MethodHandle[])converters.toArray(new MethodHandle[converters.size()]));
        converters.clear();
        return newHandle;
    }
    
    public boolean canConvert(final Class<?> from, final Class<?> to) {
        return canAutoConvert(from, to) || this.canConvert.get(from).get(to);
    }
    
    public ConversionComparator.Comparison compareConversion(final Class<?> sourceType, final Class<?> targetType1, final Class<?> targetType2) {
        for (final ConversionComparator comparator : this.comparators) {
            final ConversionComparator.Comparison result = comparator.compareConversion(sourceType, targetType1, targetType2);
            if (result != ConversionComparator.Comparison.INDETERMINATE) {
                return result;
            }
        }
        if (TypeUtilities.isMethodInvocationConvertible(sourceType, targetType1)) {
            if (!TypeUtilities.isMethodInvocationConvertible(sourceType, targetType2)) {
                return ConversionComparator.Comparison.TYPE_1_BETTER;
            }
        }
        else if (TypeUtilities.isMethodInvocationConvertible(sourceType, targetType2)) {
            return ConversionComparator.Comparison.TYPE_2_BETTER;
        }
        return ConversionComparator.Comparison.INDETERMINATE;
    }
    
    static boolean canAutoConvert(final Class<?> fromType, final Class<?> toType) {
        return TypeUtilities.isMethodInvocationConvertible(fromType, toType);
    }
    
    MethodHandle getCacheableTypeConverterNull(final Class<?> sourceType, final Class<?> targetType) {
        final MethodHandle converter = this.getCacheableTypeConverter(sourceType, targetType);
        return (converter == TypeConverterFactory.IDENTITY_CONVERSION) ? null : converter;
    }
    
    MethodHandle getTypeConverterNull(final Class<?> sourceType, final Class<?> targetType) {
        try {
            return this.getCacheableTypeConverterNull(sourceType, targetType);
        }
        catch (NotCacheableConverter e) {
            return e.converter;
        }
    }
    
    MethodHandle getCacheableTypeConverter(final Class<?> sourceType, final Class<?> targetType) {
        return this.converterMap.get(sourceType).get(targetType);
    }
    
    public MethodHandle getTypeConverter(final Class<?> sourceType, final Class<?> targetType) {
        try {
            return this.converterIdentityMap.get(sourceType).get(targetType);
        }
        catch (NotCacheableConverter e) {
            return e.converter;
        }
    }
    
    MethodHandle createConverter(final Class<?> sourceType, final Class<?> targetType) throws Exception {
        final MethodType type = MethodType.methodType(targetType, sourceType);
        MethodHandle last;
        final MethodHandle identity = last = TypeConverterFactory.IDENTITY_CONVERSION.asType(type);
        boolean cacheable = true;
        int i = this.factories.length;
        while (i-- > 0) {
            final GuardedTypeConversion next = this.factories[i].convertToType(sourceType, targetType);
            if (next != null) {
                cacheable = (cacheable && next.isCacheable());
                final GuardedInvocation conversionInvocation = next.getConversionInvocation();
                conversionInvocation.assertType(type);
                last = conversionInvocation.compose(last);
            }
        }
        if (last == identity) {
            return TypeConverterFactory.IDENTITY_CONVERSION;
        }
        if (cacheable) {
            return last;
        }
        throw new NotCacheableConverter(last);
    }
    
    static {
        IDENTITY_CONVERSION = MethodHandles.identity(Object.class);
    }
    
    private static class NotCacheableConverter extends RuntimeException
    {
        final MethodHandle converter;
        
        NotCacheableConverter(final MethodHandle converter) {
            super("", null, false, false);
            this.converter = converter;
        }
    }
}
