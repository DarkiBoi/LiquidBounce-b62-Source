// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import java.lang.invoke.MethodHandle;

public final class Specialization
{
    private final MethodHandle mh;
    private final Class<? extends SpecializedFunction.LinkLogic> linkLogicClass;
    private final boolean isOptimistic;
    
    public Specialization(final MethodHandle mh) {
        this(mh, false);
    }
    
    public Specialization(final MethodHandle mh, final boolean isOptimistic) {
        this(mh, null, isOptimistic);
    }
    
    public Specialization(final MethodHandle mh, final Class<? extends SpecializedFunction.LinkLogic> linkLogicClass, final boolean isOptimistic) {
        this.mh = mh;
        this.isOptimistic = isOptimistic;
        if (linkLogicClass != null) {
            this.linkLogicClass = (SpecializedFunction.LinkLogic.isEmpty(linkLogicClass) ? null : linkLogicClass);
        }
        else {
            this.linkLogicClass = null;
        }
    }
    
    public MethodHandle getMethodHandle() {
        return this.mh;
    }
    
    public Class<? extends SpecializedFunction.LinkLogic> getLinkLogicClass() {
        return this.linkLogicClass;
    }
    
    public boolean isOptimistic() {
        return this.isOptimistic;
    }
}
