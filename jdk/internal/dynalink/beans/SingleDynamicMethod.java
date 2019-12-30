// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.support.Lookup;
import java.util.StringTokenizer;
import jdk.internal.dynalink.support.Guards;
import java.lang.reflect.Array;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;

abstract class SingleDynamicMethod extends DynamicMethod
{
    private static final MethodHandle CAN_CONVERT_TO;
    
    SingleDynamicMethod(final String name) {
        super(name);
    }
    
    abstract boolean isVarArgs();
    
    abstract MethodType getMethodType();
    
    abstract MethodHandle getTarget(final MethodHandles.Lookup p0);
    
    @Override
    MethodHandle getInvocation(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices) {
        return getInvocation(this.getTarget(callSiteDescriptor.getLookup()), callSiteDescriptor.getMethodType(), linkerServices);
    }
    
    @Override
    SingleDynamicMethod getMethodForExactParamTypes(final String paramTypes) {
        return typeMatchesDescription(paramTypes, this.getMethodType()) ? this : null;
    }
    
    @Override
    boolean contains(final SingleDynamicMethod method) {
        return this.getMethodType().parameterList().equals(method.getMethodType().parameterList());
    }
    
    static String getMethodNameWithSignature(final MethodType type, final String methodName, final boolean withReturnType) {
        final String typeStr = type.toString();
        final int retTypeIndex = typeStr.lastIndexOf(41) + 1;
        int secondParamIndex = typeStr.indexOf(44) + 1;
        if (secondParamIndex == 0) {
            secondParamIndex = retTypeIndex - 1;
        }
        final StringBuilder b = new StringBuilder();
        if (withReturnType) {
            b.append(typeStr, retTypeIndex, typeStr.length()).append(' ');
        }
        return b.append(methodName).append('(').append(typeStr, secondParamIndex, retTypeIndex).toString();
    }
    
    static MethodHandle getInvocation(final MethodHandle target, final MethodType callSiteType, final LinkerServices linkerServices) {
        final MethodHandle filteredTarget = linkerServices.filterInternalObjects(target);
        final MethodType methodType = filteredTarget.type();
        final int paramsLen = methodType.parameterCount();
        final boolean varArgs = target.isVarargsCollector();
        final MethodHandle fixTarget = varArgs ? filteredTarget.asFixedArity() : filteredTarget;
        final int fixParamsLen = varArgs ? (paramsLen - 1) : paramsLen;
        final int argsLen = callSiteType.parameterCount();
        if (argsLen < fixParamsLen) {
            return null;
        }
        if (argsLen == fixParamsLen) {
            MethodHandle matchedMethod;
            if (varArgs) {
                matchedMethod = MethodHandles.insertArguments(fixTarget, fixParamsLen, Array.newInstance(methodType.parameterType(fixParamsLen).getComponentType(), 0));
            }
            else {
                matchedMethod = fixTarget;
            }
            return createConvertingInvocation(matchedMethod, linkerServices, callSiteType);
        }
        if (!varArgs) {
            return null;
        }
        final Class<?> varArgType = methodType.parameterType(fixParamsLen);
        if (argsLen != paramsLen) {
            return createConvertingInvocation(collectArguments(fixTarget, argsLen), linkerServices, callSiteType);
        }
        final Class<?> callSiteLastArgType = callSiteType.parameterType(fixParamsLen);
        if (varArgType.isAssignableFrom(callSiteLastArgType)) {
            return createConvertingInvocation(filteredTarget, linkerServices, callSiteType).asVarargsCollector(callSiteLastArgType);
        }
        final MethodHandle varArgCollectingInvocation = createConvertingInvocation(collectArguments(fixTarget, argsLen), linkerServices, callSiteType);
        final boolean isAssignableFromArray = callSiteLastArgType.isAssignableFrom(varArgType);
        final boolean isCustomConvertible = linkerServices.canConvert(callSiteLastArgType, varArgType);
        if (!isAssignableFromArray && !isCustomConvertible) {
            return varArgCollectingInvocation;
        }
        final MethodHandle arrayConvertingInvocation = createConvertingInvocation(MethodHandles.filterArguments(fixTarget, fixParamsLen, linkerServices.getTypeConverter(callSiteLastArgType, varArgType)), linkerServices, callSiteType);
        final MethodHandle canConvertArgToArray = MethodHandles.insertArguments(SingleDynamicMethod.CAN_CONVERT_TO, 0, linkerServices, varArgType);
        final MethodHandle canConvertLastArgToArray = MethodHandles.dropArguments(canConvertArgToArray, 0, MethodType.genericMethodType(fixParamsLen).parameterList()).asType(callSiteType.changeReturnType((Class<?>)Boolean.TYPE));
        final MethodHandle convertToArrayWhenPossible = MethodHandles.guardWithTest(canConvertLastArgToArray, arrayConvertingInvocation, varArgCollectingInvocation);
        if (isAssignableFromArray) {
            return MethodHandles.guardWithTest(Guards.isInstance(varArgType, fixParamsLen, callSiteType), createConvertingInvocation(fixTarget, linkerServices, callSiteType), isCustomConvertible ? convertToArrayWhenPossible : varArgCollectingInvocation);
        }
        assert isCustomConvertible;
        return convertToArrayWhenPossible;
    }
    
    private static boolean canConvertTo(final LinkerServices linkerServices, final Class<?> to, final Object obj) {
        return obj != null && linkerServices.canConvert(obj.getClass(), to);
    }
    
    static MethodHandle collectArguments(final MethodHandle target, final int parameterCount) {
        final MethodType methodType = target.type();
        final int fixParamsLen = methodType.parameterCount() - 1;
        final Class<?> arrayType = methodType.parameterType(fixParamsLen);
        return target.asCollector(arrayType, parameterCount - fixParamsLen);
    }
    
    private static MethodHandle createConvertingInvocation(final MethodHandle sizedMethod, final LinkerServices linkerServices, final MethodType callSiteType) {
        return linkerServices.asTypeLosslessReturn(sizedMethod, callSiteType);
    }
    
    private static boolean typeMatchesDescription(final String paramTypes, final MethodType type) {
        final StringTokenizer tok = new StringTokenizer(paramTypes, ", ");
        for (int i = 1; i < type.parameterCount(); ++i) {
            if (!tok.hasMoreTokens() || !typeNameMatches(tok.nextToken(), type.parameterType(i))) {
                return false;
            }
        }
        return !tok.hasMoreTokens();
    }
    
    private static boolean typeNameMatches(final String typeName, final Class<?> type) {
        return typeName.equals((typeName.indexOf(46) == -1) ? type.getSimpleName() : type.getCanonicalName());
    }
    
    static {
        CAN_CONVERT_TO = Lookup.findOwnStatic(MethodHandles.lookup(), "canConvertTo", Boolean.TYPE, LinkerServices.class, Class.class, Object.class);
    }
}
