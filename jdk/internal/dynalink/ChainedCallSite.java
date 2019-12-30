// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink;

import jdk.internal.dynalink.support.Lookup;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import jdk.internal.dynalink.linker.GuardedInvocation;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.support.AbstractRelinkableCallSite;

public class ChainedCallSite extends AbstractRelinkableCallSite
{
    private static final MethodHandle PRUNE_CATCHES;
    private static final MethodHandle PRUNE_SWITCHPOINTS;
    private final AtomicReference<LinkedList<GuardedInvocation>> invocations;
    
    public ChainedCallSite(final CallSiteDescriptor descriptor) {
        super(descriptor);
        this.invocations = new AtomicReference<LinkedList<GuardedInvocation>>();
    }
    
    protected int getMaxChainLength() {
        return 8;
    }
    
    @Override
    public void relink(final GuardedInvocation guardedInvocation, final MethodHandle fallback) {
        this.relinkInternal(guardedInvocation, fallback, false, false);
    }
    
    @Override
    public void resetAndRelink(final GuardedInvocation guardedInvocation, final MethodHandle fallback) {
        this.relinkInternal(guardedInvocation, fallback, true, false);
    }
    
    private MethodHandle relinkInternal(final GuardedInvocation invocation, final MethodHandle relink, final boolean reset, final boolean removeCatches) {
        final LinkedList<GuardedInvocation> currentInvocations = this.invocations.get();
        final LinkedList<GuardedInvocation> newInvocations = (LinkedList<GuardedInvocation>)((currentInvocations == null || reset) ? new LinkedList<GuardedInvocation>() : currentInvocations.clone());
        final Iterator<GuardedInvocation> it = newInvocations.iterator();
        while (it.hasNext()) {
            final GuardedInvocation inv = it.next();
            if (inv.hasBeenInvalidated() || (removeCatches && inv.getException() != null)) {
                it.remove();
            }
        }
        if (invocation != null) {
            if (newInvocations.size() == this.getMaxChainLength()) {
                newInvocations.removeFirst();
            }
            newInvocations.addLast(invocation);
        }
        final MethodHandle pruneAndInvokeSwitchPoints = this.makePruneAndInvokeMethod(relink, this.getPruneSwitchpoints());
        final MethodHandle pruneAndInvokeCatches = this.makePruneAndInvokeMethod(relink, this.getPruneCatches());
        MethodHandle target = relink;
        for (final GuardedInvocation inv2 : newInvocations) {
            target = inv2.compose(target, pruneAndInvokeSwitchPoints, pruneAndInvokeCatches);
        }
        if (this.invocations.compareAndSet(currentInvocations, newInvocations)) {
            this.setTarget(target);
        }
        return target;
    }
    
    protected MethodHandle getPruneSwitchpoints() {
        return ChainedCallSite.PRUNE_SWITCHPOINTS;
    }
    
    protected MethodHandle getPruneCatches() {
        return ChainedCallSite.PRUNE_CATCHES;
    }
    
    private MethodHandle makePruneAndInvokeMethod(final MethodHandle relink, final MethodHandle prune) {
        final MethodHandle boundPrune = MethodHandles.insertArguments(prune, 0, this, relink);
        final MethodHandle ignoreArgsPrune = MethodHandles.dropArguments(boundPrune, 0, this.type().parameterList());
        return MethodHandles.foldArguments(MethodHandles.exactInvoker(this.type()), ignoreArgsPrune);
    }
    
    private MethodHandle prune(final MethodHandle relink, final boolean catches) {
        return this.relinkInternal(null, relink, false, catches);
    }
    
    static {
        PRUNE_CATCHES = MethodHandles.insertArguments(Lookup.findOwnSpecial(MethodHandles.lookup(), "prune", MethodHandle.class, MethodHandle.class, Boolean.TYPE), 2, true);
        PRUNE_SWITCHPOINTS = MethodHandles.insertArguments(Lookup.findOwnSpecial(MethodHandles.lookup(), "prune", MethodHandle.class, MethodHandle.class, Boolean.TYPE), 2, false);
    }
}
