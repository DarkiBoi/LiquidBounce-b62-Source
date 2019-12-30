// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink;

import jdk.internal.dynalink.support.Lookup;
import java.util.List;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import java.lang.invoke.MutableCallSite;
import java.util.Objects;
import jdk.internal.dynalink.support.RuntimeContextLinkRequestImpl;
import jdk.internal.dynalink.support.LinkRequestImpl;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.LinkerServices;

public class DynamicLinker
{
    private static final String CLASS_NAME;
    private static final String RELINK_METHOD_NAME = "relink";
    private static final String INITIAL_LINK_CLASS_NAME = "java.lang.invoke.MethodHandleNatives";
    private static final String INITIAL_LINK_METHOD_NAME = "linkCallSite";
    private final LinkerServices linkerServices;
    private final GuardedInvocationFilter prelinkFilter;
    private final int runtimeContextArgCount;
    private final boolean syncOnRelink;
    private final int unstableRelinkThreshold;
    private static final MethodHandle RELINK;
    
    DynamicLinker(final LinkerServices linkerServices, final GuardedInvocationFilter prelinkFilter, final int runtimeContextArgCount, final boolean syncOnRelink, final int unstableRelinkThreshold) {
        if (runtimeContextArgCount < 0) {
            throw new IllegalArgumentException("runtimeContextArgCount < 0");
        }
        if (unstableRelinkThreshold < 0) {
            throw new IllegalArgumentException("unstableRelinkThreshold < 0");
        }
        this.linkerServices = linkerServices;
        this.prelinkFilter = prelinkFilter;
        this.runtimeContextArgCount = runtimeContextArgCount;
        this.syncOnRelink = syncOnRelink;
        this.unstableRelinkThreshold = unstableRelinkThreshold;
    }
    
    public <T extends RelinkableCallSite> T link(final T callSite) {
        callSite.initialize(this.createRelinkAndInvokeMethod(callSite, 0));
        return callSite;
    }
    
    public LinkerServices getLinkerServices() {
        return this.linkerServices;
    }
    
    private MethodHandle createRelinkAndInvokeMethod(final RelinkableCallSite callSite, final int relinkCount) {
        final MethodHandle boundRelinker = MethodHandles.insertArguments(DynamicLinker.RELINK, 0, this, callSite, relinkCount);
        final MethodType type = callSite.getDescriptor().getMethodType();
        final MethodHandle collectingRelinker = boundRelinker.asCollector(Object[].class, type.parameterCount());
        return MethodHandles.foldArguments(MethodHandles.exactInvoker(type), collectingRelinker.asType(type.changeReturnType((Class<?>)MethodHandle.class)));
    }
    
    private MethodHandle relink(final RelinkableCallSite callSite, final int relinkCount, final Object... arguments) throws Exception {
        final CallSiteDescriptor callSiteDescriptor = callSite.getDescriptor();
        final boolean unstableDetectionEnabled = this.unstableRelinkThreshold > 0;
        final boolean callSiteUnstable = unstableDetectionEnabled && relinkCount >= this.unstableRelinkThreshold;
        final LinkRequest linkRequest = (this.runtimeContextArgCount == 0) ? new LinkRequestImpl(callSiteDescriptor, callSite, relinkCount, callSiteUnstable, arguments) : new RuntimeContextLinkRequestImpl(callSiteDescriptor, callSite, relinkCount, callSiteUnstable, arguments, this.runtimeContextArgCount);
        GuardedInvocation guardedInvocation = this.linkerServices.getGuardedInvocation(linkRequest);
        if (guardedInvocation == null) {
            throw new NoSuchDynamicMethodException(callSiteDescriptor.toString());
        }
        if (this.runtimeContextArgCount > 0) {
            final MethodType origType = callSiteDescriptor.getMethodType();
            final MethodHandle invocation = guardedInvocation.getInvocation();
            if (invocation.type().parameterCount() == origType.parameterCount() - this.runtimeContextArgCount) {
                final List<Class<?>> prefix = origType.parameterList().subList(1, this.runtimeContextArgCount + 1);
                final MethodHandle guard = guardedInvocation.getGuard();
                guardedInvocation = guardedInvocation.dropArguments(1, prefix);
            }
        }
        guardedInvocation = this.prelinkFilter.filter(guardedInvocation, linkRequest, this.linkerServices);
        Objects.requireNonNull(guardedInvocation);
        int newRelinkCount = relinkCount;
        if (unstableDetectionEnabled && newRelinkCount <= this.unstableRelinkThreshold && newRelinkCount++ == this.unstableRelinkThreshold) {
            callSite.resetAndRelink(guardedInvocation, this.createRelinkAndInvokeMethod(callSite, newRelinkCount));
        }
        else {
            callSite.relink(guardedInvocation, this.createRelinkAndInvokeMethod(callSite, newRelinkCount));
        }
        if (this.syncOnRelink) {
            MutableCallSite.syncAll(new MutableCallSite[] { (MutableCallSite)callSite });
        }
        return guardedInvocation.getInvocation();
    }
    
    public static StackTraceElement getLinkedCallSiteLocation() {
        final StackTraceElement[] trace = new Throwable().getStackTrace();
        for (int i = 0; i < trace.length - 1; ++i) {
            final StackTraceElement frame = trace[i];
            if (isRelinkFrame(frame) || isInitialLinkFrame(frame)) {
                return trace[i + 1];
            }
        }
        return null;
    }
    
    @Deprecated
    public static StackTraceElement getRelinkedCallSiteLocation() {
        return getLinkedCallSiteLocation();
    }
    
    private static boolean isInitialLinkFrame(final StackTraceElement frame) {
        return testFrame(frame, "linkCallSite", "java.lang.invoke.MethodHandleNatives");
    }
    
    private static boolean isRelinkFrame(final StackTraceElement frame) {
        return testFrame(frame, "relink", DynamicLinker.CLASS_NAME);
    }
    
    private static boolean testFrame(final StackTraceElement frame, final String methodName, final String className) {
        return methodName.equals(frame.getMethodName()) && className.equals(frame.getClassName());
    }
    
    static {
        CLASS_NAME = DynamicLinker.class.getName();
        RELINK = Lookup.findOwnSpecial(MethodHandles.lookup(), "relink", MethodHandle.class, RelinkableCallSite.class, Integer.TYPE, Object[].class);
    }
}
