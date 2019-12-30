// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.support.Lookup;
import jdk.internal.dynalink.support.TypeUtilities;
import java.util.Iterator;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import jdk.internal.dynalink.linker.LinkerServices;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;
import java.util.Map;

class OverloadedMethod
{
    private final Map<ClassString, MethodHandle> argTypesToMethods;
    private final OverloadedDynamicMethod parent;
    private final MethodType callSiteType;
    private final MethodHandle invoker;
    private final LinkerServices linkerServices;
    private final ArrayList<MethodHandle> fixArgMethods;
    private final ArrayList<MethodHandle> varArgMethods;
    private static final MethodHandle SELECT_METHOD;
    private static final MethodHandle THROW_NO_SUCH_METHOD;
    private static final MethodHandle THROW_AMBIGUOUS_METHOD;
    
    OverloadedMethod(final List<MethodHandle> methodHandles, final OverloadedDynamicMethod parent, final MethodType callSiteType, final LinkerServices linkerServices) {
        this.argTypesToMethods = new ConcurrentHashMap<ClassString, MethodHandle>();
        this.parent = parent;
        final Class<?> commonRetType = getCommonReturnType(methodHandles);
        this.callSiteType = callSiteType.changeReturnType(commonRetType);
        this.linkerServices = linkerServices;
        this.fixArgMethods = new ArrayList<MethodHandle>(methodHandles.size());
        this.varArgMethods = new ArrayList<MethodHandle>(methodHandles.size());
        final int argNum = callSiteType.parameterCount();
        for (final MethodHandle mh : methodHandles) {
            if (mh.isVarargsCollector()) {
                final MethodHandle asFixed = mh.asFixedArity();
                if (argNum == asFixed.type().parameterCount()) {
                    this.fixArgMethods.add(asFixed);
                }
                this.varArgMethods.add(mh);
            }
            else {
                this.fixArgMethods.add(mh);
            }
        }
        this.fixArgMethods.trimToSize();
        this.varArgMethods.trimToSize();
        final MethodHandle bound = OverloadedMethod.SELECT_METHOD.bindTo(this);
        final MethodHandle collecting = SingleDynamicMethod.collectArguments(bound, argNum).asType(callSiteType.changeReturnType((Class<?>)MethodHandle.class));
        this.invoker = linkerServices.asTypeLosslessReturn(MethodHandles.foldArguments(MethodHandles.exactInvoker(this.callSiteType), collecting), callSiteType);
    }
    
    MethodHandle getInvoker() {
        return this.invoker;
    }
    
    private MethodHandle selectMethod(final Object[] args) throws NoSuchMethodException {
        final Class<?>[] argTypes = (Class<?>[])new Class[args.length];
        for (int i = 0; i < argTypes.length; ++i) {
            final Object arg = args[i];
            argTypes[i] = ((arg == null) ? ClassString.NULL_CLASS : arg.getClass());
        }
        final ClassString classString = new ClassString(argTypes);
        MethodHandle method = this.argTypesToMethods.get(classString);
        if (method == null) {
            List<MethodHandle> methods = classString.getMaximallySpecifics(this.fixArgMethods, this.linkerServices, false);
            if (methods.isEmpty()) {
                methods = classString.getMaximallySpecifics(this.varArgMethods, this.linkerServices, true);
            }
            switch (methods.size()) {
                case 0: {
                    method = this.getNoSuchMethodThrower(argTypes);
                    break;
                }
                case 1: {
                    method = SingleDynamicMethod.getInvocation(methods.get(0), this.callSiteType, this.linkerServices);
                    break;
                }
                default: {
                    method = this.getAmbiguousMethodThrower(argTypes, methods);
                    break;
                }
            }
            if (classString.isVisibleFrom(this.parent.getClassLoader())) {
                this.argTypesToMethods.put(classString, method);
            }
        }
        return method;
    }
    
    private MethodHandle getNoSuchMethodThrower(final Class<?>[] argTypes) {
        return this.adaptThrower(MethodHandles.insertArguments(OverloadedMethod.THROW_NO_SUCH_METHOD, 0, this, argTypes));
    }
    
    private void throwNoSuchMethod(final Class<?>[] argTypes) throws NoSuchMethodException {
        if (this.varArgMethods.isEmpty()) {
            throw new NoSuchMethodException("None of the fixed arity signatures " + getSignatureList(this.fixArgMethods) + " of method " + this.parent.getName() + " match the argument types " + argTypesString(argTypes));
        }
        throw new NoSuchMethodException("None of the fixed arity signatures " + getSignatureList(this.fixArgMethods) + " or the variable arity signatures " + getSignatureList(this.varArgMethods) + " of the method " + this.parent.getName() + " match the argument types " + argTypesString(argTypes));
    }
    
    private MethodHandle getAmbiguousMethodThrower(final Class<?>[] argTypes, final List<MethodHandle> methods) {
        return this.adaptThrower(MethodHandles.insertArguments(OverloadedMethod.THROW_AMBIGUOUS_METHOD, 0, this, argTypes, methods));
    }
    
    private MethodHandle adaptThrower(final MethodHandle rawThrower) {
        return MethodHandles.dropArguments(rawThrower, 0, this.callSiteType.parameterList()).asType(this.callSiteType);
    }
    
    private void throwAmbiguousMethod(final Class<?>[] argTypes, final List<MethodHandle> methods) throws NoSuchMethodException {
        final String arity = methods.get(0).isVarargsCollector() ? "variable" : "fixed";
        throw new NoSuchMethodException("Can't unambiguously select between " + arity + " arity signatures " + getSignatureList(methods) + " of the method " + this.parent.getName() + " for argument types " + argTypesString(argTypes));
    }
    
    private static String argTypesString(final Class<?>[] classes) {
        final StringBuilder b = new StringBuilder().append('[');
        appendTypes(b, classes, false);
        return b.append(']').toString();
    }
    
    private static String getSignatureList(final List<MethodHandle> methods) {
        final StringBuilder b = new StringBuilder().append('[');
        final Iterator<MethodHandle> it = methods.iterator();
        if (it.hasNext()) {
            appendSig(b, it.next());
            while (it.hasNext()) {
                appendSig(b.append(", "), it.next());
            }
        }
        return b.append(']').toString();
    }
    
    private static void appendSig(final StringBuilder b, final MethodHandle m) {
        b.append('(');
        appendTypes(b, m.type().parameterArray(), m.isVarargsCollector());
        b.append(')');
    }
    
    private static void appendTypes(final StringBuilder b, final Class<?>[] classes, final boolean varArg) {
        final int l = classes.length;
        if (!varArg) {
            if (l > 1) {
                b.append(classes[1].getCanonicalName());
                for (int i = 2; i < l; ++i) {
                    b.append(", ").append(classes[i].getCanonicalName());
                }
            }
        }
        else {
            for (int i = 1; i < l - 1; ++i) {
                b.append(classes[i].getCanonicalName()).append(", ");
            }
            b.append(classes[l - 1].getComponentType().getCanonicalName()).append("...");
        }
    }
    
    private static Class<?> getCommonReturnType(final List<MethodHandle> methodHandles) {
        final Iterator<MethodHandle> it = methodHandles.iterator();
        Class<?> retType = it.next().type().returnType();
        while (it.hasNext()) {
            retType = TypeUtilities.getCommonLosslessConversionType(retType, it.next().type().returnType());
        }
        return retType;
    }
    
    static {
        SELECT_METHOD = Lookup.findOwnSpecial(MethodHandles.lookup(), "selectMethod", MethodHandle.class, Object[].class);
        THROW_NO_SUCH_METHOD = Lookup.findOwnSpecial(MethodHandles.lookup(), "throwNoSuchMethod", Void.TYPE, Class[].class);
        THROW_AMBIGUOUS_METHOD = Lookup.findOwnSpecial(MethodHandles.lookup(), "throwAmbiguousMethod", Void.TYPE, Class[].class, List.class);
    }
}
