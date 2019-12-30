// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink;

import java.security.AccessController;
import jdk.internal.dynalink.support.ClassLoaderGetterContextProvider;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Set;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.LinkerServicesImpl;
import jdk.internal.dynalink.support.TypeConverterFactory;
import jdk.internal.dynalink.support.DefaultPrelinkFilter;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;
import java.util.LinkedList;
import jdk.internal.dynalink.support.CompositeGuardingDynamicLinker;
import jdk.internal.dynalink.support.BottomGuardingDynamicLinker;
import jdk.internal.dynalink.support.CompositeTypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.AutoDiscovery;
import java.util.HashSet;
import jdk.internal.dynalink.beans.BeansLinker;
import java.util.Collections;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import jdk.internal.dynalink.linker.MethodHandleTransformer;
import jdk.internal.dynalink.linker.MethodTypeConversionStrategy;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import java.util.List;

public class DynamicLinkerFactory
{
    public static final int DEFAULT_UNSTABLE_RELINK_THRESHOLD = 8;
    private boolean classLoaderExplicitlySet;
    private ClassLoader classLoader;
    private List<? extends GuardingDynamicLinker> prioritizedLinkers;
    private List<? extends GuardingDynamicLinker> fallbackLinkers;
    private int runtimeContextArgCount;
    private boolean syncOnRelink;
    private int unstableRelinkThreshold;
    private GuardedInvocationFilter prelinkFilter;
    private MethodTypeConversionStrategy autoConversionStrategy;
    private MethodHandleTransformer internalObjectsFilter;
    
    public DynamicLinkerFactory() {
        this.classLoaderExplicitlySet = false;
        this.runtimeContextArgCount = 0;
        this.syncOnRelink = false;
        this.unstableRelinkThreshold = 8;
    }
    
    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.classLoaderExplicitlySet = true;
    }
    
    public void setPrioritizedLinkers(final List<? extends GuardingDynamicLinker> prioritizedLinkers) {
        this.prioritizedLinkers = ((prioritizedLinkers == null) ? null : new ArrayList<GuardingDynamicLinker>(prioritizedLinkers));
    }
    
    public void setPrioritizedLinkers(final GuardingDynamicLinker... prioritizedLinkers) {
        this.setPrioritizedLinkers(Arrays.asList(prioritizedLinkers));
    }
    
    public void setPrioritizedLinker(final GuardingDynamicLinker prioritizedLinker) {
        if (prioritizedLinker == null) {
            throw new IllegalArgumentException("prioritizedLinker == null");
        }
        this.prioritizedLinkers = Collections.singletonList(prioritizedLinker);
    }
    
    public void setFallbackLinkers(final List<? extends GuardingDynamicLinker> fallbackLinkers) {
        this.fallbackLinkers = ((fallbackLinkers == null) ? null : new ArrayList<GuardingDynamicLinker>(fallbackLinkers));
    }
    
    public void setFallbackLinkers(final GuardingDynamicLinker... fallbackLinkers) {
        this.setFallbackLinkers(Arrays.asList(fallbackLinkers));
    }
    
    public void setRuntimeContextArgCount(final int runtimeContextArgCount) {
        if (runtimeContextArgCount < 0) {
            throw new IllegalArgumentException("runtimeContextArgCount < 0");
        }
        this.runtimeContextArgCount = runtimeContextArgCount;
    }
    
    public void setSyncOnRelink(final boolean syncOnRelink) {
        this.syncOnRelink = syncOnRelink;
    }
    
    public void setUnstableRelinkThreshold(final int unstableRelinkThreshold) {
        if (unstableRelinkThreshold < 0) {
            throw new IllegalArgumentException("unstableRelinkThreshold < 0");
        }
        this.unstableRelinkThreshold = unstableRelinkThreshold;
    }
    
    public void setPrelinkFilter(final GuardedInvocationFilter prelinkFilter) {
        this.prelinkFilter = prelinkFilter;
    }
    
    public void setAutoConversionStrategy(final MethodTypeConversionStrategy autoConversionStrategy) {
        this.autoConversionStrategy = autoConversionStrategy;
    }
    
    public void setInternalObjectsFilter(final MethodHandleTransformer internalObjectsFilter) {
        this.internalObjectsFilter = internalObjectsFilter;
    }
    
    public DynamicLinker createLinker() {
        if (this.prioritizedLinkers == null) {
            this.prioritizedLinkers = Collections.emptyList();
        }
        if (this.fallbackLinkers == null) {
            this.fallbackLinkers = Collections.singletonList((GuardingDynamicLinker)new BeansLinker());
        }
        final Set<Class<? extends GuardingDynamicLinker>> knownLinkerClasses = new HashSet<Class<? extends GuardingDynamicLinker>>();
        addClasses(knownLinkerClasses, this.prioritizedLinkers);
        addClasses(knownLinkerClasses, this.fallbackLinkers);
        final ClassLoader effectiveClassLoader = this.classLoaderExplicitlySet ? this.classLoader : getThreadContextClassLoader();
        final List<GuardingDynamicLinker> discovered = AutoDiscovery.loadLinkers(effectiveClassLoader);
        final List<GuardingDynamicLinker> linkers = new ArrayList<GuardingDynamicLinker>(this.prioritizedLinkers.size() + discovered.size() + this.fallbackLinkers.size());
        linkers.addAll(this.prioritizedLinkers);
        for (final GuardingDynamicLinker linker : discovered) {
            if (!knownLinkerClasses.contains(linker.getClass())) {
                linkers.add(linker);
            }
        }
        linkers.addAll(this.fallbackLinkers);
        final List<GuardingDynamicLinker> optimized = CompositeTypeBasedGuardingDynamicLinker.optimize(linkers);
        GuardingDynamicLinker composite = null;
        switch (linkers.size()) {
            case 0: {
                composite = BottomGuardingDynamicLinker.INSTANCE;
                break;
            }
            case 1: {
                composite = optimized.get(0);
                break;
            }
            default: {
                composite = new CompositeGuardingDynamicLinker(optimized);
                break;
            }
        }
        final List<GuardingTypeConverterFactory> typeConverters = new LinkedList<GuardingTypeConverterFactory>();
        for (final GuardingDynamicLinker linker2 : linkers) {
            if (linker2 instanceof GuardingTypeConverterFactory) {
                typeConverters.add((GuardingTypeConverterFactory)linker2);
            }
        }
        if (this.prelinkFilter == null) {
            this.prelinkFilter = new DefaultPrelinkFilter();
        }
        return new DynamicLinker(new LinkerServicesImpl(new TypeConverterFactory(typeConverters, this.autoConversionStrategy), composite, this.internalObjectsFilter), this.prelinkFilter, this.runtimeContextArgCount, this.syncOnRelink, this.unstableRelinkThreshold);
    }
    
    private static ClassLoader getThreadContextClassLoader() {
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }, ClassLoaderGetterContextProvider.GET_CLASS_LOADER_CONTEXT);
    }
    
    private static void addClasses(final Set<Class<? extends GuardingDynamicLinker>> knownLinkerClasses, final List<? extends GuardingDynamicLinker> linkers) {
        for (final GuardingDynamicLinker linker : linkers) {
            knownLinkerClasses.add(linker.getClass());
        }
    }
}
