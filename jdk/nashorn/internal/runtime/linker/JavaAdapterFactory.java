// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime.linker;

import jdk.nashorn.internal.runtime.ScriptFunction;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.security.PermissionCollection;
import java.net.URL;
import java.security.CodeSource;
import java.security.CodeSigner;
import java.security.Permission;
import java.security.Permissions;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.LinkRequestImpl;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.lookup.Lookup;
import java.lang.invoke.MethodHandle;
import java.security.AccessController;
import java.security.PrivilegedAction;
import jdk.nashorn.internal.runtime.Context;
import jdk.internal.dynalink.beans.StaticClass;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.runtime.ScriptObject;
import java.util.List;
import java.util.Map;
import java.security.AccessControlContext;
import java.security.ProtectionDomain;

public final class JavaAdapterFactory
{
    private static final ProtectionDomain MINIMAL_PERMISSION_DOMAIN;
    private static final AccessControlContext CREATE_ADAPTER_INFO_ACC_CTXT;
    private static final ClassValue<Map<List<Class<?>>, AdapterInfo>> ADAPTER_INFO_MAPS;
    
    public static StaticClass getAdapterClassFor(final Class<?>[] types, final ScriptObject classOverrides, final MethodHandles.Lookup lookup) {
        return getAdapterClassFor(types, classOverrides, getProtectionDomain(lookup));
    }
    
    private static StaticClass getAdapterClassFor(final Class<?>[] types, final ScriptObject classOverrides, final ProtectionDomain protectionDomain) {
        assert types != null && types.length > 0;
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            for (final Class<?> type : types) {
                Context.checkPackageAccess(type);
                ReflectionCheckLinker.checkReflectionAccess(type, true);
            }
        }
        return getAdapterInfo(types).getAdapterClass(classOverrides, protectionDomain);
    }
    
    private static ProtectionDomain getProtectionDomain(final MethodHandles.Lookup lookup) {
        if ((lookup.lookupModes() & 0x2) == 0x0) {
            return JavaAdapterFactory.MINIMAL_PERMISSION_DOMAIN;
        }
        return getProtectionDomain(lookup.lookupClass());
    }
    
    private static ProtectionDomain getProtectionDomain(final Class<?> clazz) {
        return AccessController.doPrivileged((PrivilegedAction<ProtectionDomain>)new PrivilegedAction<ProtectionDomain>() {
            @Override
            public ProtectionDomain run() {
                return clazz.getProtectionDomain();
            }
        });
    }
    
    public static MethodHandle getConstructor(final Class<?> sourceType, final Class<?> targetType, final MethodHandles.Lookup lookup) throws Exception {
        final StaticClass adapterClass = getAdapterClassFor(new Class[] { targetType }, null, lookup);
        return Lookup.MH.bindTo(Bootstrap.getLinkerServices().getGuardedInvocation(new LinkRequestImpl(NashornCallSiteDescriptor.get(lookup, "dyn:new", MethodType.methodType(targetType, StaticClass.class, sourceType), 0), null, 0, false, new Object[] { adapterClass, null })).getInvocation(), adapterClass);
    }
    
    static boolean isAutoConvertibleFromFunction(final Class<?> clazz) {
        return getAdapterInfo(new Class[] { clazz }).autoConvertibleFromFunction;
    }
    
    private static AdapterInfo getAdapterInfo(final Class<?>[] types) {
        final ClassAndLoader definingClassAndLoader = ClassAndLoader.getDefiningClassAndLoader(types);
        final Map<List<Class<?>>, AdapterInfo> adapterInfoMap = JavaAdapterFactory.ADAPTER_INFO_MAPS.get(definingClassAndLoader.getRepresentativeClass());
        final List<Class<?>> typeList = (types.length == 1) ? Collections.singletonList(types[0]) : Arrays.asList((Class<?>[])types.clone());
        AdapterInfo adapterInfo;
        synchronized (adapterInfoMap) {
            adapterInfo = adapterInfoMap.get(typeList);
            if (adapterInfo == null) {
                adapterInfo = createAdapterInfo(types, definingClassAndLoader);
                adapterInfoMap.put(typeList, adapterInfo);
            }
        }
        return adapterInfo;
    }
    
    private static AdapterInfo createAdapterInfo(final Class<?>[] types, final ClassAndLoader definingClassAndLoader) {
        Class<?> superClass = null;
        final List<Class<?>> interfaces = new ArrayList<Class<?>>(types.length);
        for (final Class<?> t : types) {
            final int mod = t.getModifiers();
            if (!t.isInterface()) {
                if (superClass != null) {
                    return new AdapterInfo(AdaptationResult.Outcome.ERROR_MULTIPLE_SUPERCLASSES, t.getCanonicalName() + " and " + superClass.getCanonicalName());
                }
                if (Modifier.isFinal(mod)) {
                    return new AdapterInfo(AdaptationResult.Outcome.ERROR_FINAL_CLASS, t.getCanonicalName());
                }
                superClass = t;
            }
            else {
                if (interfaces.size() > 65535) {
                    throw new IllegalArgumentException("interface limit exceeded");
                }
                interfaces.add(t);
            }
            if (!Modifier.isPublic(mod)) {
                return new AdapterInfo(AdaptationResult.Outcome.ERROR_NON_PUBLIC_CLASS, t.getCanonicalName());
            }
        }
        final Class<?> effectiveSuperClass = (superClass == null) ? Object.class : superClass;
        return AccessController.doPrivileged((PrivilegedAction<AdapterInfo>)new PrivilegedAction<AdapterInfo>() {
            @Override
            public AdapterInfo run() {
                try {
                    return new AdapterInfo(effectiveSuperClass, interfaces, definingClassAndLoader);
                }
                catch (AdaptationException e) {
                    return new AdapterInfo(e.getAdaptationResult());
                }
                catch (RuntimeException e2) {
                    return new AdapterInfo(new AdaptationResult(AdaptationResult.Outcome.ERROR_OTHER, new String[] { Arrays.toString(types), e2.toString() }));
                }
            }
        }, JavaAdapterFactory.CREATE_ADAPTER_INFO_ACC_CTXT);
    }
    
    private static ProtectionDomain createMinimalPermissionDomain() {
        final Permissions permissions = new Permissions();
        permissions.add(new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.objects"));
        permissions.add(new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime"));
        permissions.add(new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime.linker"));
        return new ProtectionDomain(new CodeSource(null, (CodeSigner[])null), permissions);
    }
    
    static {
        MINIMAL_PERMISSION_DOMAIN = createMinimalPermissionDomain();
        CREATE_ADAPTER_INFO_ACC_CTXT = ClassAndLoader.createPermAccCtxt("createClassLoader", "getClassLoader", "accessDeclaredMembers", "accessClassInPackage.jdk.nashorn.internal.runtime");
        ADAPTER_INFO_MAPS = new ClassValue<Map<List<Class<?>>, AdapterInfo>>() {
            @Override
            protected Map<List<Class<?>>, AdapterInfo> computeValue(final Class<?> type) {
                return new HashMap<List<Class<?>>, AdapterInfo>();
            }
        };
    }
    
    private static class AdapterInfo
    {
        private static final ClassAndLoader SCRIPT_OBJECT_LOADER;
        private final ClassLoader commonLoader;
        private final JavaAdapterClassLoader classAdapterGenerator;
        private final JavaAdapterClassLoader instanceAdapterGenerator;
        private final Map<CodeSource, StaticClass> instanceAdapters;
        final boolean autoConvertibleFromFunction;
        final AdaptationResult adaptationResult;
        
        AdapterInfo(final Class<?> superClass, final List<Class<?>> interfaces, final ClassAndLoader definingLoader) throws AdaptationException {
            this.instanceAdapters = new ConcurrentHashMap<CodeSource, StaticClass>();
            this.commonLoader = findCommonLoader(definingLoader);
            final JavaAdapterBytecodeGenerator gen = new JavaAdapterBytecodeGenerator(superClass, interfaces, this.commonLoader, false);
            this.autoConvertibleFromFunction = gen.isAutoConvertibleFromFunction();
            this.instanceAdapterGenerator = gen.createAdapterClassLoader();
            this.classAdapterGenerator = new JavaAdapterBytecodeGenerator(superClass, interfaces, this.commonLoader, true).createAdapterClassLoader();
            this.adaptationResult = AdaptationResult.SUCCESSFUL_RESULT;
        }
        
        AdapterInfo(final AdaptationResult.Outcome outcome, final String classList) {
            this(new AdaptationResult(outcome, new String[] { classList }));
        }
        
        AdapterInfo(final AdaptationResult adaptationResult) {
            this.instanceAdapters = new ConcurrentHashMap<CodeSource, StaticClass>();
            this.commonLoader = null;
            this.classAdapterGenerator = null;
            this.instanceAdapterGenerator = null;
            this.autoConvertibleFromFunction = false;
            this.adaptationResult = adaptationResult;
        }
        
        StaticClass getAdapterClass(final ScriptObject classOverrides, final ProtectionDomain protectionDomain) {
            if (this.adaptationResult.getOutcome() != AdaptationResult.Outcome.SUCCESS) {
                throw this.adaptationResult.typeError();
            }
            return (classOverrides == null) ? this.getInstanceAdapterClass(protectionDomain) : this.getClassAdapterClass(classOverrides, protectionDomain);
        }
        
        private StaticClass getInstanceAdapterClass(final ProtectionDomain protectionDomain) {
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource == null) {
                codeSource = JavaAdapterFactory.MINIMAL_PERMISSION_DOMAIN.getCodeSource();
            }
            StaticClass instanceAdapterClass = this.instanceAdapters.get(codeSource);
            if (instanceAdapterClass != null) {
                return instanceAdapterClass;
            }
            final ProtectionDomain effectiveDomain = codeSource.equals(JavaAdapterFactory.MINIMAL_PERMISSION_DOMAIN.getCodeSource()) ? JavaAdapterFactory.MINIMAL_PERMISSION_DOMAIN : protectionDomain;
            instanceAdapterClass = this.instanceAdapterGenerator.generateClass(this.commonLoader, effectiveDomain);
            final StaticClass existing = this.instanceAdapters.putIfAbsent(codeSource, instanceAdapterClass);
            return (existing == null) ? instanceAdapterClass : existing;
        }
        
        private StaticClass getClassAdapterClass(final ScriptObject classOverrides, final ProtectionDomain protectionDomain) {
            JavaAdapterServices.setClassOverrides(classOverrides);
            try {
                return this.classAdapterGenerator.generateClass(this.commonLoader, protectionDomain);
            }
            finally {
                JavaAdapterServices.setClassOverrides(null);
            }
        }
        
        private static ClassLoader findCommonLoader(final ClassAndLoader classAndLoader) throws AdaptationException {
            if (classAndLoader.canSee(AdapterInfo.SCRIPT_OBJECT_LOADER)) {
                return classAndLoader.getLoader();
            }
            if (AdapterInfo.SCRIPT_OBJECT_LOADER.canSee(classAndLoader)) {
                return AdapterInfo.SCRIPT_OBJECT_LOADER.getLoader();
            }
            throw new AdaptationException(AdaptationResult.Outcome.ERROR_NO_COMMON_LOADER, classAndLoader.getRepresentativeClass().getCanonicalName());
        }
        
        static {
            SCRIPT_OBJECT_LOADER = new ClassAndLoader(ScriptFunction.class, true);
        }
    }
}
