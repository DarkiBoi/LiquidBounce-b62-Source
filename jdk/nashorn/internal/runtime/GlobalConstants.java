// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import java.util.logging.Level;
import java.util.Arrays;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.internal.dynalink.DynamicLinker;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import java.lang.invoke.SwitchPoint;
import java.util.Iterator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.logging.Loggable;

@Logger(name = "const")
public final class GlobalConstants implements Loggable
{
    public static final boolean GLOBAL_ONLY = true;
    private static final MethodHandles.Lookup LOOKUP;
    private static final MethodHandle INVALIDATE_SP;
    private static final MethodHandle RECEIVER_GUARD;
    private final DebugLogger log;
    private final Map<String, Access> map;
    private final AtomicBoolean invalidatedForever;
    
    public GlobalConstants(final DebugLogger log) {
        this.map = new HashMap<String, Access>();
        this.invalidatedForever = new AtomicBoolean(false);
        this.log = ((log == null) ? DebugLogger.DISABLED_LOGGER : log);
    }
    
    @Override
    public DebugLogger getLogger() {
        return this.log;
    }
    
    @Override
    public DebugLogger initLogger(final Context context) {
        return DebugLogger.DISABLED_LOGGER;
    }
    
    public void invalidateAll() {
        if (!this.invalidatedForever.get()) {
            this.log.info("New global created - invalidating all constant callsites without increasing invocation count.");
            synchronized (this) {
                for (final Access acc : this.map.values()) {
                    acc.invalidateUncounted();
                }
            }
        }
    }
    
    public void invalidateForever() {
        if (this.invalidatedForever.compareAndSet(false, true)) {
            this.log.info("New global created - invalidating all constant callsites.");
            synchronized (this) {
                for (final Access acc : this.map.values()) {
                    acc.invalidateForever();
                }
                this.map.clear();
            }
        }
    }
    
    private synchronized Object invalidateSwitchPoint(final Object obj, final Access acc) {
        if (this.log.isEnabled()) {
            this.log.info("*** Invalidating switchpoint " + acc.getSwitchPoint() + " for receiver=" + obj + " access=" + acc);
        }
        acc.invalidateOnce();
        if (acc.mayRetry()) {
            if (this.log.isEnabled()) {
                this.log.info("Retry is allowed for " + acc + "... Creating a new switchpoint.");
            }
            acc.newSwitchPoint();
        }
        else if (this.log.isEnabled()) {
            this.log.info("This was the last time I allowed " + DebugLogger.quote(acc.getName()) + " to relink as constant.");
        }
        return obj;
    }
    
    private Access getOrCreateSwitchPoint(final String name) {
        Access acc = this.map.get(name);
        if (acc != null) {
            return acc;
        }
        final SwitchPoint sp = new SwitchPoint();
        this.map.put(name, acc = new Access(name, sp));
        return acc;
    }
    
    void delete(final String name) {
        if (!this.invalidatedForever.get()) {
            synchronized (this) {
                final Access acc = this.map.get(name);
                if (acc != null) {
                    acc.invalidateForever();
                }
            }
        }
    }
    
    private static boolean receiverGuard(final Access acc, final Object boundReceiver, final Object receiver) {
        final boolean id = receiver == boundReceiver;
        if (!id) {
            acc.failGuard();
        }
        return id;
    }
    
    private static boolean isGlobalSetter(final ScriptObject receiver, final FindProperty find) {
        if (find == null) {
            return receiver.isScope();
        }
        return find.getOwner().isGlobal();
    }
    
    GuardedInvocation findSetMethod(final FindProperty find, final ScriptObject receiver, final GuardedInvocation inv, final CallSiteDescriptor desc, final LinkRequest request) {
        if (this.invalidatedForever.get() || !isGlobalSetter(receiver, find)) {
            return null;
        }
        final String name = desc.getNameToken(2);
        synchronized (this) {
            final Access acc = this.getOrCreateSwitchPoint(name);
            if (this.log.isEnabled()) {
                this.log.fine("Trying to link constant SETTER ", acc);
            }
            if (!acc.mayRetry() || this.invalidatedForever.get()) {
                if (this.log.isEnabled()) {
                    this.log.fine("*** SET: Giving up on " + DebugLogger.quote(name) + " - retry count has exceeded " + DynamicLinker.getLinkedCallSiteLocation());
                }
                return null;
            }
            if (acc.hasBeenInvalidated()) {
                this.log.info("New chance for " + acc);
                acc.newSwitchPoint();
            }
            assert !acc.hasBeenInvalidated();
            final MethodHandle target = inv.getInvocation();
            final Class<?> receiverType = target.type().parameterType(0);
            final MethodHandle boundInvalidator = Lookup.MH.bindTo(GlobalConstants.INVALIDATE_SP, this);
            final MethodHandle invalidator = Lookup.MH.asType(boundInvalidator, boundInvalidator.type().changeParameterType(0, receiverType).changeReturnType(receiverType));
            final MethodHandle mh = Lookup.MH.filterArguments(inv.getInvocation(), 0, Lookup.MH.insertArguments(invalidator, 1, acc));
            assert inv.getSwitchPoints() == null : Arrays.asList(inv.getSwitchPoints());
            this.log.info("Linked setter " + DebugLogger.quote(name) + " " + acc.getSwitchPoint());
            return new GuardedInvocation(mh, inv.getGuard(), acc.getSwitchPoint(), inv.getException());
        }
    }
    
    public static MethodHandle staticConstantGetter(final Object c) {
        return Lookup.MH.dropArguments(JSType.unboxConstant(c), 0, Object.class);
    }
    
    private MethodHandle constantGetter(final Object c) {
        final MethodHandle mh = staticConstantGetter(c);
        if (this.log.isEnabled()) {
            return MethodHandleFactory.addDebugPrintout(this.log, Level.FINEST, mh, "getting as constant");
        }
        return mh;
    }
    
    GuardedInvocation findGetMethod(final FindProperty find, final ScriptObject receiver, final CallSiteDescriptor desc) {
        if (this.invalidatedForever.get() || !NashornCallSiteDescriptor.isFastScope(desc) || !find.getOwner().isGlobal() || find.getProperty() instanceof UserAccessorProperty) {
            return null;
        }
        final boolean isOptimistic = NashornCallSiteDescriptor.isOptimistic(desc);
        final int programPoint = isOptimistic ? NashornCallSiteDescriptor.getProgramPoint(desc) : -1;
        final Class<?> retType = desc.getMethodType().returnType();
        final String name = desc.getNameToken(2);
        synchronized (this) {
            final Access acc = this.getOrCreateSwitchPoint(name);
            this.log.fine("Starting to look up object value " + name);
            final Object c = find.getObjectValue();
            if (this.log.isEnabled()) {
                this.log.fine("Trying to link constant GETTER " + acc + " value = " + c);
            }
            if (acc.hasBeenInvalidated() || acc.guardFailed() || this.invalidatedForever.get()) {
                if (this.log.isEnabled()) {
                    this.log.info("*** GET: Giving up on " + DebugLogger.quote(name) + " - retry count has exceeded " + DynamicLinker.getLinkedCallSiteLocation());
                }
                return null;
            }
            final MethodHandle cmh = this.constantGetter(c);
            MethodHandle mh;
            if (isOptimistic) {
                if (JSType.getAccessorTypeIndex(cmh.type().returnType()) <= JSType.getAccessorTypeIndex(retType)) {
                    mh = Lookup.MH.asType(cmh, cmh.type().changeReturnType(retType));
                }
                else {
                    mh = Lookup.MH.dropArguments(Lookup.MH.insertArguments(JSType.THROW_UNWARRANTED.methodHandle(), 0, c, programPoint), 0, Object.class);
                }
            }
            else {
                mh = Lookup.filterReturnType(cmh, retType);
            }
            MethodHandle guard;
            if (find.getOwner().isGlobal()) {
                guard = null;
            }
            else {
                guard = Lookup.MH.insertArguments(GlobalConstants.RECEIVER_GUARD, 0, acc, receiver);
            }
            if (this.log.isEnabled()) {
                this.log.info("Linked getter " + DebugLogger.quote(name) + " as MethodHandle.constant() -> " + c + " " + acc.getSwitchPoint());
                mh = MethodHandleFactory.addDebugPrintout(this.log, Level.FINE, mh, "get const " + acc);
            }
            return new GuardedInvocation(mh, guard, acc.getSwitchPoint(), null);
        }
    }
    
    static {
        LOOKUP = MethodHandles.lookup();
        INVALIDATE_SP = CompilerConstants.virtualCall(GlobalConstants.LOOKUP, GlobalConstants.class, "invalidateSwitchPoint", Object.class, Object.class, Access.class).methodHandle();
        RECEIVER_GUARD = CompilerConstants.staticCall(GlobalConstants.LOOKUP, GlobalConstants.class, "receiverGuard", Boolean.TYPE, Access.class, Object.class, Object.class).methodHandle();
    }
    
    private static class Access
    {
        private final String name;
        private SwitchPoint sp;
        private int invalidations;
        private boolean guardFailed;
        private static final int MAX_RETRIES = 2;
        
        private Access(final String name, final SwitchPoint sp) {
            this.name = name;
            this.sp = sp;
        }
        
        private boolean hasBeenInvalidated() {
            return this.sp.hasBeenInvalidated();
        }
        
        private boolean guardFailed() {
            return this.guardFailed;
        }
        
        private void failGuard() {
            this.invalidateOnce();
            this.guardFailed = true;
        }
        
        private void newSwitchPoint() {
            assert this.hasBeenInvalidated();
            this.sp = new SwitchPoint();
        }
        
        private void invalidate(final int count) {
            if (!this.sp.hasBeenInvalidated()) {
                SwitchPoint.invalidateAll(new SwitchPoint[] { this.sp });
                this.invalidations += count;
            }
        }
        
        private void invalidateUncounted() {
            this.invalidate(0);
        }
        
        private void invalidateOnce() {
            this.invalidate(1);
        }
        
        private void invalidateForever() {
            this.invalidate(2);
        }
        
        private boolean mayRetry() {
            return this.invalidations < 2;
        }
        
        @Override
        public String toString() {
            return "[" + DebugLogger.quote(this.name) + " <id=" + Debug.id(this) + "> inv#=" + this.invalidations + '/' + 2 + " sp_inv=" + this.sp.hasBeenInvalidated() + ']';
        }
        
        String getName() {
            return this.name;
        }
        
        SwitchPoint getSwitchPoint() {
            return this.sp;
        }
    }
}
