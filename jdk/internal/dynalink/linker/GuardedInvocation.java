// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.linker;

import java.lang.invoke.WrongMethodTypeException;
import jdk.nashorn.internal.lookup.Lookup;
import java.util.List;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.Guards;
import java.lang.invoke.MethodType;
import java.util.Objects;
import java.lang.invoke.SwitchPoint;
import java.lang.invoke.MethodHandle;

public class GuardedInvocation
{
    private final MethodHandle invocation;
    private final MethodHandle guard;
    private final Class<? extends Throwable> exception;
    private final SwitchPoint[] switchPoints;
    
    public GuardedInvocation(final MethodHandle invocation) {
        this(invocation, null, (SwitchPoint)null, null);
    }
    
    public GuardedInvocation(final MethodHandle invocation, final MethodHandle guard) {
        this(invocation, guard, (SwitchPoint)null, null);
    }
    
    public GuardedInvocation(final MethodHandle invocation, final SwitchPoint switchPoint) {
        this(invocation, null, switchPoint, null);
    }
    
    public GuardedInvocation(final MethodHandle invocation, final MethodHandle guard, final SwitchPoint switchPoint) {
        this(invocation, guard, switchPoint, null);
    }
    
    public GuardedInvocation(final MethodHandle invocation, final MethodHandle guard, final SwitchPoint switchPoint, final Class<? extends Throwable> exception) {
        this.invocation = Objects.requireNonNull(invocation);
        this.guard = guard;
        this.switchPoints = (SwitchPoint[])((switchPoint == null) ? null : new SwitchPoint[] { switchPoint });
        this.exception = exception;
    }
    
    public GuardedInvocation(final MethodHandle invocation, final MethodHandle guard, final SwitchPoint[] switchPoints, final Class<? extends Throwable> exception) {
        this.invocation = Objects.requireNonNull(invocation);
        this.guard = guard;
        this.switchPoints = (SwitchPoint[])((switchPoints == null) ? null : ((SwitchPoint[])switchPoints.clone()));
        this.exception = exception;
    }
    
    public MethodHandle getInvocation() {
        return this.invocation;
    }
    
    public MethodHandle getGuard() {
        return this.guard;
    }
    
    public SwitchPoint[] getSwitchPoints() {
        return (SwitchPoint[])((this.switchPoints == null) ? null : ((SwitchPoint[])this.switchPoints.clone()));
    }
    
    public Class<? extends Throwable> getException() {
        return this.exception;
    }
    
    public boolean hasBeenInvalidated() {
        if (this.switchPoints == null) {
            return false;
        }
        for (final SwitchPoint sp : this.switchPoints) {
            if (sp.hasBeenInvalidated()) {
                return true;
            }
        }
        return false;
    }
    
    public void assertType(final MethodType type) {
        assertType(this.invocation, type);
        if (this.guard != null) {
            assertType(this.guard, type.changeReturnType((Class<?>)Boolean.TYPE));
        }
    }
    
    public GuardedInvocation replaceMethods(final MethodHandle newInvocation, final MethodHandle newGuard) {
        return new GuardedInvocation(newInvocation, newGuard, this.switchPoints, this.exception);
    }
    
    public GuardedInvocation addSwitchPoint(final SwitchPoint newSwitchPoint) {
        if (newSwitchPoint == null) {
            return this;
        }
        SwitchPoint[] newSwitchPoints;
        if (this.switchPoints != null) {
            newSwitchPoints = new SwitchPoint[this.switchPoints.length + 1];
            System.arraycopy(this.switchPoints, 0, newSwitchPoints, 0, this.switchPoints.length);
            newSwitchPoints[this.switchPoints.length] = newSwitchPoint;
        }
        else {
            newSwitchPoints = new SwitchPoint[] { newSwitchPoint };
        }
        return new GuardedInvocation(this.invocation, this.guard, newSwitchPoints, this.exception);
    }
    
    private GuardedInvocation replaceMethodsOrThis(final MethodHandle newInvocation, final MethodHandle newGuard) {
        if (newInvocation == this.invocation && newGuard == this.guard) {
            return this;
        }
        return this.replaceMethods(newInvocation, newGuard);
    }
    
    public GuardedInvocation asType(final MethodType newType) {
        return this.replaceMethodsOrThis(this.invocation.asType(newType), (this.guard == null) ? null : Guards.asType(this.guard, newType));
    }
    
    public GuardedInvocation asType(final LinkerServices linkerServices, final MethodType newType) {
        return this.replaceMethodsOrThis(linkerServices.asType(this.invocation, newType), (this.guard == null) ? null : Guards.asType(linkerServices, this.guard, newType));
    }
    
    public GuardedInvocation asTypeSafeReturn(final LinkerServices linkerServices, final MethodType newType) {
        return this.replaceMethodsOrThis(linkerServices.asTypeLosslessReturn(this.invocation, newType), (this.guard == null) ? null : Guards.asType(linkerServices, this.guard, newType));
    }
    
    public GuardedInvocation asType(final CallSiteDescriptor desc) {
        return this.asType(desc.getMethodType());
    }
    
    public GuardedInvocation filterArguments(final int pos, final MethodHandle... filters) {
        return this.replaceMethods(MethodHandles.filterArguments(this.invocation, pos, filters), (this.guard == null) ? null : MethodHandles.filterArguments(this.guard, pos, filters));
    }
    
    public GuardedInvocation dropArguments(final int pos, final List<Class<?>> valueTypes) {
        return this.replaceMethods(MethodHandles.dropArguments(this.invocation, pos, valueTypes), (this.guard == null) ? null : MethodHandles.dropArguments(this.guard, pos, valueTypes));
    }
    
    public GuardedInvocation dropArguments(final int pos, final Class<?>... valueTypes) {
        return this.replaceMethods(MethodHandles.dropArguments(this.invocation, pos, valueTypes), (this.guard == null) ? null : MethodHandles.dropArguments(this.guard, pos, valueTypes));
    }
    
    public MethodHandle compose(final MethodHandle fallback) {
        return this.compose(fallback, fallback, fallback);
    }
    
    public MethodHandle compose(final MethodHandle guardFallback, final MethodHandle switchpointFallback, final MethodHandle catchFallback) {
        final MethodHandle guarded = (this.guard == null) ? this.invocation : MethodHandles.guardWithTest(this.guard, this.invocation, guardFallback);
        final MethodHandle catchGuarded = (this.exception == null) ? guarded : Lookup.MH.catchException(guarded, this.exception, MethodHandles.dropArguments(catchFallback, 0, this.exception));
        if (this.switchPoints == null) {
            return catchGuarded;
        }
        MethodHandle spGuarded = catchGuarded;
        for (final SwitchPoint sp : this.switchPoints) {
            spGuarded = sp.guardWithTest(spGuarded, switchpointFallback);
        }
        return spGuarded;
    }
    
    private static void assertType(final MethodHandle mh, final MethodType type) {
        if (!mh.type().equals((Object)type)) {
            throw new WrongMethodTypeException("Expected type: " + type + " actual type: " + mh.type());
        }
    }
}
