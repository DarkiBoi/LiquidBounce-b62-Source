// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ServiceLoader;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import java.util.List;

public class AutoDiscovery
{
    private AutoDiscovery() {
    }
    
    public static List<GuardingDynamicLinker> loadLinkers() {
        return getLinkers((ServiceLoader<GuardingDynamicLinker>)ServiceLoader.load((Class<T>)GuardingDynamicLinker.class));
    }
    
    public static List<GuardingDynamicLinker> loadLinkers(final ClassLoader cl) {
        return getLinkers((ServiceLoader<GuardingDynamicLinker>)ServiceLoader.load((Class<T>)GuardingDynamicLinker.class, cl));
    }
    
    private static <T> List<T> getLinkers(final ServiceLoader<T> loader) {
        final List<T> list = new LinkedList<T>();
        for (final T linker : loader) {
            list.add(linker);
        }
        return list;
    }
}
