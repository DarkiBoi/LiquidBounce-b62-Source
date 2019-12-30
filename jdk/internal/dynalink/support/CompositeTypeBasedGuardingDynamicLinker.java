// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import java.util.Collections;
import java.util.Collection;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

public class CompositeTypeBasedGuardingDynamicLinker implements TypeBasedGuardingDynamicLinker, Serializable
{
    private static final long serialVersionUID = 1L;
    private final ClassValue<List<TypeBasedGuardingDynamicLinker>> classToLinker;
    
    public CompositeTypeBasedGuardingDynamicLinker(final Iterable<? extends TypeBasedGuardingDynamicLinker> linkers) {
        final List<TypeBasedGuardingDynamicLinker> l = new LinkedList<TypeBasedGuardingDynamicLinker>();
        for (final TypeBasedGuardingDynamicLinker linker : linkers) {
            l.add(linker);
        }
        this.classToLinker = new ClassToLinker(l.toArray(new TypeBasedGuardingDynamicLinker[l.size()]));
    }
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return !this.classToLinker.get(type).isEmpty();
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices) throws Exception {
        final Object obj = linkRequest.getReceiver();
        if (obj == null) {
            return null;
        }
        for (final TypeBasedGuardingDynamicLinker linker : this.classToLinker.get(obj.getClass())) {
            final GuardedInvocation invocation = linker.getGuardedInvocation(linkRequest, linkerServices);
            if (invocation != null) {
                return invocation;
            }
        }
        return null;
    }
    
    public static List<GuardingDynamicLinker> optimize(final Iterable<? extends GuardingDynamicLinker> linkers) {
        final List<GuardingDynamicLinker> llinkers = new LinkedList<GuardingDynamicLinker>();
        final List<TypeBasedGuardingDynamicLinker> tblinkers = new LinkedList<TypeBasedGuardingDynamicLinker>();
        for (final GuardingDynamicLinker linker : linkers) {
            if (linker instanceof TypeBasedGuardingDynamicLinker) {
                tblinkers.add((TypeBasedGuardingDynamicLinker)linker);
            }
            else {
                addTypeBased(llinkers, tblinkers);
                llinkers.add(linker);
            }
        }
        addTypeBased(llinkers, tblinkers);
        return llinkers;
    }
    
    private static void addTypeBased(final List<GuardingDynamicLinker> llinkers, final List<TypeBasedGuardingDynamicLinker> tblinkers) {
        switch (tblinkers.size()) {
            case 0: {
                break;
            }
            case 1: {
                llinkers.addAll(tblinkers);
                tblinkers.clear();
                break;
            }
            default: {
                llinkers.add(new CompositeTypeBasedGuardingDynamicLinker(tblinkers));
                tblinkers.clear();
                break;
            }
        }
    }
    
    private static class ClassToLinker extends ClassValue<List<TypeBasedGuardingDynamicLinker>>
    {
        private static final List<TypeBasedGuardingDynamicLinker> NO_LINKER;
        private final TypeBasedGuardingDynamicLinker[] linkers;
        private final List<TypeBasedGuardingDynamicLinker>[] singletonLinkers;
        
        ClassToLinker(final TypeBasedGuardingDynamicLinker[] linkers) {
            this.linkers = linkers;
            this.singletonLinkers = (List<TypeBasedGuardingDynamicLinker>[])new List[linkers.length];
            for (int i = 0; i < linkers.length; ++i) {
                this.singletonLinkers[i] = Collections.singletonList(linkers[i]);
            }
        }
        
        @Override
        protected List<TypeBasedGuardingDynamicLinker> computeValue(final Class<?> clazz) {
            List<TypeBasedGuardingDynamicLinker> list = ClassToLinker.NO_LINKER;
            for (int i = 0; i < this.linkers.length; ++i) {
                final TypeBasedGuardingDynamicLinker linker = this.linkers[i];
                if (linker.canLinkType(clazz)) {
                    switch (list.size()) {
                        case 0: {
                            list = this.singletonLinkers[i];
                            continue;
                        }
                        case 1: {
                            list = new LinkedList<TypeBasedGuardingDynamicLinker>(list);
                            break;
                        }
                    }
                    list.add(linker);
                }
            }
            return list;
        }
        
        static {
            NO_LINKER = Collections.emptyList();
        }
    }
}
