// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.support.TypeUtilities;
import java.util.Comparator;
import java.util.Collections;
import java.text.Collator;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.CallSiteDescriptor;
import java.util.Iterator;
import java.util.LinkedList;

class OverloadedDynamicMethod extends DynamicMethod
{
    private final LinkedList<SingleDynamicMethod> methods;
    private final ClassLoader classLoader;
    
    OverloadedDynamicMethod(final Class<?> clazz, final String name) {
        this(new LinkedList<SingleDynamicMethod>(), clazz.getClassLoader(), DynamicMethod.getClassAndMethodName(clazz, name));
    }
    
    private OverloadedDynamicMethod(final LinkedList<SingleDynamicMethod> methods, final ClassLoader classLoader, final String name) {
        super(name);
        this.methods = methods;
        this.classLoader = classLoader;
    }
    
    @Override
    SingleDynamicMethod getMethodForExactParamTypes(final String paramTypes) {
        final LinkedList<SingleDynamicMethod> matchingMethods = new LinkedList<SingleDynamicMethod>();
        for (final SingleDynamicMethod method : this.methods) {
            final SingleDynamicMethod matchingMethod = method.getMethodForExactParamTypes(paramTypes);
            if (matchingMethod != null) {
                matchingMethods.add(matchingMethod);
            }
        }
        switch (matchingMethods.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return matchingMethods.getFirst();
            }
            default: {
                throw new BootstrapMethodError("Can't choose among " + matchingMethods + " for argument types " + paramTypes + " for method " + this.getName());
            }
        }
    }
    
    public MethodHandle getInvocation(final CallSiteDescriptor callSiteDescriptor, final LinkerServices linkerServices) {
        final MethodType callSiteType = callSiteDescriptor.getMethodType();
        final ApplicableOverloadedMethods subtypingApplicables = this.getApplicables(callSiteType, ApplicableOverloadedMethods.APPLICABLE_BY_SUBTYPING);
        final ApplicableOverloadedMethods methodInvocationApplicables = this.getApplicables(callSiteType, ApplicableOverloadedMethods.APPLICABLE_BY_METHOD_INVOCATION_CONVERSION);
        final ApplicableOverloadedMethods variableArityApplicables = this.getApplicables(callSiteType, ApplicableOverloadedMethods.APPLICABLE_BY_VARIABLE_ARITY);
        List<SingleDynamicMethod> maximallySpecifics = subtypingApplicables.findMaximallySpecificMethods();
        if (maximallySpecifics.isEmpty()) {
            maximallySpecifics = methodInvocationApplicables.findMaximallySpecificMethods();
            if (maximallySpecifics.isEmpty()) {
                maximallySpecifics = variableArityApplicables.findMaximallySpecificMethods();
            }
        }
        final List<SingleDynamicMethod> invokables = (List<SingleDynamicMethod>)this.methods.clone();
        invokables.removeAll(subtypingApplicables.getMethods());
        invokables.removeAll(methodInvocationApplicables.getMethods());
        invokables.removeAll(variableArityApplicables.getMethods());
        final Iterator<SingleDynamicMethod> it = invokables.iterator();
        while (it.hasNext()) {
            final SingleDynamicMethod m = it.next();
            if (!isApplicableDynamically(linkerServices, callSiteType, m)) {
                it.remove();
            }
        }
        if (invokables.isEmpty() && maximallySpecifics.size() > 1) {
            throw new BootstrapMethodError("Can't choose among " + maximallySpecifics + " for argument types " + callSiteType);
        }
        invokables.addAll(maximallySpecifics);
        switch (invokables.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return invokables.iterator().next().getInvocation(callSiteDescriptor, linkerServices);
            }
            default: {
                final List<MethodHandle> methodHandles = new ArrayList<MethodHandle>(invokables.size());
                final MethodHandles.Lookup lookup = callSiteDescriptor.getLookup();
                for (final SingleDynamicMethod method : invokables) {
                    methodHandles.add(method.getTarget(lookup));
                }
                return new OverloadedMethod(methodHandles, this, callSiteType, linkerServices).getInvoker();
            }
        }
    }
    
    public boolean contains(final SingleDynamicMethod m) {
        for (final SingleDynamicMethod method : this.methods) {
            if (method.contains(m)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isConstructor() {
        assert !this.methods.isEmpty();
        return this.methods.getFirst().isConstructor();
    }
    
    @Override
    public String toString() {
        final List<String> names = new ArrayList<String>(this.methods.size());
        int len = 0;
        for (final SingleDynamicMethod m : this.methods) {
            final String name = m.getName();
            len += name.length();
            names.add(name);
        }
        final Collator collator = Collator.getInstance();
        collator.setStrength(1);
        Collections.sort(names, collator);
        final String className = this.getClass().getName();
        final int totalLength = className.length() + len + 2 * names.size() + 3;
        final StringBuilder b = new StringBuilder(totalLength);
        b.append('[').append(className).append('\n');
        for (final String name2 : names) {
            b.append(' ').append(name2).append('\n');
        }
        b.append(']');
        assert b.length() == totalLength;
        return b.toString();
    }
    
    ClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    private static boolean isApplicableDynamically(final LinkerServices linkerServices, final MethodType callSiteType, final SingleDynamicMethod m) {
        final MethodType methodType = m.getMethodType();
        final boolean varArgs = m.isVarArgs();
        final int fixedArgLen = methodType.parameterCount() - (varArgs ? 1 : 0);
        final int callSiteArgLen = callSiteType.parameterCount();
        if (varArgs) {
            if (callSiteArgLen < fixedArgLen) {
                return false;
            }
        }
        else if (callSiteArgLen != fixedArgLen) {
            return false;
        }
        for (int i = 1; i < fixedArgLen; ++i) {
            if (!isApplicableDynamically(linkerServices, callSiteType.parameterType(i), methodType.parameterType(i))) {
                return false;
            }
        }
        if (!varArgs) {
            return true;
        }
        final Class<?> varArgArrayType = methodType.parameterType(fixedArgLen);
        final Class<?> varArgType = varArgArrayType.getComponentType();
        if (fixedArgLen == callSiteArgLen - 1) {
            final Class<?> callSiteArgType = callSiteType.parameterType(fixedArgLen);
            return isApplicableDynamically(linkerServices, callSiteArgType, varArgArrayType) || isApplicableDynamically(linkerServices, callSiteArgType, varArgType);
        }
        for (int j = fixedArgLen; j < callSiteArgLen; ++j) {
            if (!isApplicableDynamically(linkerServices, callSiteType.parameterType(j), varArgType)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isApplicableDynamically(final LinkerServices linkerServices, final Class<?> callSiteType, final Class<?> methodType) {
        return TypeUtilities.isPotentiallyConvertible(callSiteType, methodType) || linkerServices.canConvert(callSiteType, methodType);
    }
    
    private ApplicableOverloadedMethods getApplicables(final MethodType callSiteType, final ApplicableOverloadedMethods.ApplicabilityTest test) {
        return new ApplicableOverloadedMethods(this.methods, callSiteType, test);
    }
    
    public void addMethod(final SingleDynamicMethod method) {
        assert this.constructorFlagConsistent(method);
        this.methods.add(method);
    }
    
    private boolean constructorFlagConsistent(final SingleDynamicMethod method) {
        return this.methods.isEmpty() || this.methods.getFirst().isConstructor() == method.isConstructor();
    }
}
