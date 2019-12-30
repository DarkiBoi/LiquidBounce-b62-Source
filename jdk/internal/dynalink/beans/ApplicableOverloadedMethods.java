// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.support.TypeUtilities;
import java.util.Iterator;
import java.util.LinkedList;
import java.lang.invoke.MethodType;
import java.util.List;

class ApplicableOverloadedMethods
{
    private final List<SingleDynamicMethod> methods;
    private final boolean varArgs;
    static final ApplicabilityTest APPLICABLE_BY_SUBTYPING;
    static final ApplicabilityTest APPLICABLE_BY_METHOD_INVOCATION_CONVERSION;
    static final ApplicabilityTest APPLICABLE_BY_VARIABLE_ARITY;
    
    ApplicableOverloadedMethods(final List<SingleDynamicMethod> methods, final MethodType callSiteType, final ApplicabilityTest test) {
        this.methods = new LinkedList<SingleDynamicMethod>();
        for (final SingleDynamicMethod m : methods) {
            if (test.isApplicable(callSiteType, m)) {
                this.methods.add(m);
            }
        }
        this.varArgs = (test == ApplicableOverloadedMethods.APPLICABLE_BY_VARIABLE_ARITY);
    }
    
    List<SingleDynamicMethod> getMethods() {
        return this.methods;
    }
    
    List<SingleDynamicMethod> findMaximallySpecificMethods() {
        return MaximallySpecific.getMaximallySpecificMethods(this.methods, this.varArgs);
    }
    
    static {
        APPLICABLE_BY_SUBTYPING = new ApplicabilityTest() {
            @Override
            boolean isApplicable(final MethodType callSiteType, final SingleDynamicMethod method) {
                final MethodType methodType = method.getMethodType();
                final int methodArity = methodType.parameterCount();
                if (methodArity != callSiteType.parameterCount()) {
                    return false;
                }
                for (int i = 1; i < methodArity; ++i) {
                    if (!TypeUtilities.isSubtype(callSiteType.parameterType(i), methodType.parameterType(i))) {
                        return false;
                    }
                }
                return true;
            }
        };
        APPLICABLE_BY_METHOD_INVOCATION_CONVERSION = new ApplicabilityTest() {
            @Override
            boolean isApplicable(final MethodType callSiteType, final SingleDynamicMethod method) {
                final MethodType methodType = method.getMethodType();
                final int methodArity = methodType.parameterCount();
                if (methodArity != callSiteType.parameterCount()) {
                    return false;
                }
                for (int i = 1; i < methodArity; ++i) {
                    if (!TypeUtilities.isMethodInvocationConvertible(callSiteType.parameterType(i), methodType.parameterType(i))) {
                        return false;
                    }
                }
                return true;
            }
        };
        APPLICABLE_BY_VARIABLE_ARITY = new ApplicabilityTest() {
            @Override
            boolean isApplicable(final MethodType callSiteType, final SingleDynamicMethod method) {
                if (!method.isVarArgs()) {
                    return false;
                }
                final MethodType methodType = method.getMethodType();
                final int methodArity = methodType.parameterCount();
                final int fixArity = methodArity - 1;
                final int callSiteArity = callSiteType.parameterCount();
                if (fixArity > callSiteArity) {
                    return false;
                }
                for (int i = 1; i < fixArity; ++i) {
                    if (!TypeUtilities.isMethodInvocationConvertible(callSiteType.parameterType(i), methodType.parameterType(i))) {
                        return false;
                    }
                }
                final Class<?> varArgType = methodType.parameterType(fixArity).getComponentType();
                for (int j = fixArity; j < callSiteArity; ++j) {
                    if (!TypeUtilities.isMethodInvocationConvertible(callSiteType.parameterType(j), varArgType)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }
    
    abstract static class ApplicabilityTest
    {
        abstract boolean isApplicable(final MethodType p0, final SingleDynamicMethod p1);
    }
}
