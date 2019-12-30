// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.support;

import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

public class BottomGuardingDynamicLinker implements TypeBasedGuardingDynamicLinker
{
    public static final BottomGuardingDynamicLinker INSTANCE;
    
    private BottomGuardingDynamicLinker() {
    }
    
    @Override
    public boolean canLinkType(final Class<?> type) {
        return false;
    }
    
    @Override
    public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices) {
        return null;
    }
    
    static {
        INSTANCE = new BottomGuardingDynamicLinker();
    }
}
