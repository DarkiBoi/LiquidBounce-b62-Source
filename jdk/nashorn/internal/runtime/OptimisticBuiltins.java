// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.objects.annotations.SpecializedFunction;

public interface OptimisticBuiltins
{
    SpecializedFunction.LinkLogic getLinkLogic(final Class<? extends SpecializedFunction.LinkLogic> p0);
    
    boolean hasPerInstanceAssumptions();
}
