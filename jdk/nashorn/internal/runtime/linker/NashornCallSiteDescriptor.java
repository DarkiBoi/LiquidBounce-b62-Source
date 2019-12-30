// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import java.util.concurrent.ConcurrentHashMap;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ConcurrentMap;
import jdk.internal.dynalink.support.AbstractCallSiteDescriptor;

public final class NashornCallSiteDescriptor extends AbstractCallSiteDescriptor
{
    public static final int CALLSITE_SCOPE = 1;
    public static final int CALLSITE_STRICT = 2;
    public static final int CALLSITE_FAST_SCOPE = 4;
    public static final int CALLSITE_OPTIMISTIC = 8;
    public static final int CALLSITE_APPLY_TO_CALL = 16;
    public static final int CALLSITE_DECLARE = 32;
    public static final int CALLSITE_PROFILE = 64;
    public static final int CALLSITE_TRACE = 128;
    public static final int CALLSITE_TRACE_MISSES = 256;
    public static final int CALLSITE_TRACE_ENTEREXIT = 512;
    public static final int CALLSITE_TRACE_VALUES = 1024;
    public static final int CALLSITE_PROGRAM_POINT_SHIFT = 11;
    public static final int MAX_PROGRAM_POINT_VALUE = 2097151;
    public static final int FLAGS_MASK = 2047;
    private static final ClassValue<ConcurrentMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor>> canonicals;
    private final MethodHandles.Lookup lookup;
    private final String operator;
    private final String operand;
    private final MethodType methodType;
    private final int flags;
    
    public static String toString(final int flags) {
        final StringBuilder sb = new StringBuilder();
        if ((flags & 0x1) != 0x0) {
            if ((flags & 0x4) != 0x0) {
                sb.append("fastscope ");
            }
            else {
                assert (flags & 0x4) == 0x0 : "can't be fastscope without scope";
                sb.append("scope ");
            }
            if ((flags & 0x20) != 0x0) {
                sb.append("declare ");
            }
        }
        if ((flags & 0x10) != 0x0) {
            sb.append("apply2call ");
        }
        if ((flags & 0x2) != 0x0) {
            sb.append("strict ");
        }
        return (sb.length() == 0) ? "" : (" " + sb.toString().trim());
    }
    
    public static NashornCallSiteDescriptor get(final MethodHandles.Lookup lookup, final String name, final MethodType methodType, final int flags) {
        final String[] tokenizedName = CallSiteDescriptorFactory.tokenizeName(name);
        assert tokenizedName.length >= 2;
        assert "dyn".equals(tokenizedName[0]);
        assert tokenizedName[1] != null;
        return get(lookup, tokenizedName[1], (tokenizedName.length == 3) ? tokenizedName[2].intern() : null, methodType, flags);
    }
    
    private static NashornCallSiteDescriptor get(final MethodHandles.Lookup lookup, final String operator, final String operand, final MethodType methodType, final int flags) {
        final NashornCallSiteDescriptor csd = new NashornCallSiteDescriptor(lookup, operator, operand, methodType, flags);
        final ConcurrentMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor> classCanonicals = NashornCallSiteDescriptor.canonicals.get(lookup.lookupClass());
        final NashornCallSiteDescriptor canonical = classCanonicals.putIfAbsent(csd, csd);
        return (canonical != null) ? canonical : csd;
    }
    
    private NashornCallSiteDescriptor(final MethodHandles.Lookup lookup, final String operator, final String operand, final MethodType methodType, final int flags) {
        this.lookup = lookup;
        this.operator = operator;
        this.operand = operand;
        this.methodType = methodType;
        this.flags = flags;
    }
    
    @Override
    public int getNameTokenCount() {
        return (this.operand == null) ? 2 : 3;
    }
    
    @Override
    public String getNameToken(final int i) {
        switch (i) {
            case 0: {
                return "dyn";
            }
            case 1: {
                return this.operator;
            }
            case 2: {
                if (this.operand != null) {
                    return this.operand;
                }
                break;
            }
        }
        throw new IndexOutOfBoundsException(String.valueOf(i));
    }
    
    @Override
    public MethodHandles.Lookup getLookup() {
        return this.lookup;
    }
    
    @Override
    public boolean equals(final CallSiteDescriptor csd) {
        return super.equals(csd) && this.flags == getFlags(csd);
    }
    
    @Override
    public MethodType getMethodType() {
        return this.methodType;
    }
    
    public String getOperator() {
        return this.operator;
    }
    
    public String getFirstOperator() {
        final int delim = this.operator.indexOf("|");
        return (delim == -1) ? this.operator : this.operator.substring(0, delim);
    }
    
    public String getOperand() {
        return this.operand;
    }
    
    public String getFunctionDescription() {
        assert this.getFirstOperator().equals("call") || this.getFirstOperator().equals("new");
        return (this.getNameTokenCount() > 2) ? this.getNameToken(2) : null;
    }
    
    public static String getFunctionDescription(final CallSiteDescriptor desc) {
        return (desc instanceof NashornCallSiteDescriptor) ? ((NashornCallSiteDescriptor)desc).getFunctionDescription() : null;
    }
    
    public String getFunctionErrorMessage(final Object obj) {
        final String funcDesc = this.getFunctionDescription();
        return (funcDesc != null) ? funcDesc : ScriptRuntime.safeToString(obj);
    }
    
    public static String getFunctionErrorMessage(final CallSiteDescriptor desc, final Object obj) {
        return (desc instanceof NashornCallSiteDescriptor) ? ((NashornCallSiteDescriptor)desc).getFunctionErrorMessage(obj) : ScriptRuntime.safeToString(obj);
    }
    
    public static int getFlags(final CallSiteDescriptor desc) {
        return (desc instanceof NashornCallSiteDescriptor) ? ((NashornCallSiteDescriptor)desc).flags : 0;
    }
    
    private boolean isFlag(final int flag) {
        return (this.flags & flag) != 0x0;
    }
    
    private static boolean isFlag(final CallSiteDescriptor desc, final int flag) {
        return (getFlags(desc) & flag) != 0x0;
    }
    
    public static boolean isScope(final CallSiteDescriptor desc) {
        return isFlag(desc, 1);
    }
    
    public static boolean isFastScope(final CallSiteDescriptor desc) {
        return isFlag(desc, 4);
    }
    
    public static boolean isStrict(final CallSiteDescriptor desc) {
        return isFlag(desc, 2);
    }
    
    public static boolean isApplyToCall(final CallSiteDescriptor desc) {
        return isFlag(desc, 16);
    }
    
    public static boolean isOptimistic(final CallSiteDescriptor desc) {
        return isFlag(desc, 8);
    }
    
    public static boolean isDeclaration(final CallSiteDescriptor desc) {
        return isFlag(desc, 32);
    }
    
    public static boolean isStrictFlag(final int flags) {
        return (flags & 0x2) != 0x0;
    }
    
    public static boolean isScopeFlag(final int flags) {
        return (flags & 0x1) != 0x0;
    }
    
    public static int getProgramPoint(final CallSiteDescriptor desc) {
        assert isOptimistic(desc) : "program point requested from non-optimistic descriptor " + desc;
        return getFlags(desc) >> 11;
    }
    
    boolean isProfile() {
        return this.isFlag(64);
    }
    
    boolean isTrace() {
        return this.isFlag(128);
    }
    
    boolean isTraceMisses() {
        return this.isFlag(256);
    }
    
    boolean isTraceEnterExit() {
        return this.isFlag(512);
    }
    
    boolean isTraceObjects() {
        return this.isFlag(1024);
    }
    
    boolean isOptimistic() {
        return this.isFlag(8);
    }
    
    @Override
    public CallSiteDescriptor changeMethodType(final MethodType newMethodType) {
        return get(this.getLookup(), this.operator, this.operand, newMethodType, this.flags);
    }
    
    static {
        canonicals = new ClassValue<ConcurrentMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor>>() {
            @Override
            protected ConcurrentMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor> computeValue(final Class<?> type) {
                return new ConcurrentHashMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor>();
            }
        };
    }
}
