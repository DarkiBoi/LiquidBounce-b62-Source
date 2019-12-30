// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.util.Arrays;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.Set;
import java.lang.reflect.Method;
import java.util.Map;

class AccessibleMembersLookup
{
    private final Map<MethodSignature, Method> methods;
    private final Set<Class<?>> innerClasses;
    private final boolean instance;
    
    AccessibleMembersLookup(final Class<?> clazz, final boolean instance) {
        this.methods = new HashMap<MethodSignature, Method>();
        this.innerClasses = new LinkedHashSet<Class<?>>();
        this.instance = instance;
        this.lookupAccessibleMembers(clazz);
    }
    
    Method getAccessibleMethod(final Method m) {
        return (m == null) ? null : this.methods.get(new MethodSignature(m));
    }
    
    Collection<Method> getMethods() {
        return this.methods.values();
    }
    
    Class<?>[] getInnerClasses() {
        return this.innerClasses.toArray(new Class[this.innerClasses.size()]);
    }
    
    private void lookupAccessibleMembers(final Class<?> clazz) {
        boolean searchSuperTypes;
        if (!CheckRestrictedPackage.isRestrictedClass(clazz)) {
            searchSuperTypes = false;
            for (final Method method : clazz.getMethods()) {
                final boolean isStatic = Modifier.isStatic(method.getModifiers());
                if (this.instance != isStatic) {
                    final MethodSignature sig = new MethodSignature(method);
                    if (!this.methods.containsKey(sig)) {
                        final Class<?> declaringClass = method.getDeclaringClass();
                        if (declaringClass != clazz && CheckRestrictedPackage.isRestrictedClass(declaringClass)) {
                            searchSuperTypes = true;
                        }
                        else if (!isStatic || clazz == declaringClass) {
                            this.methods.put(sig, method);
                        }
                    }
                }
            }
            for (final Class<?> innerClass : clazz.getClasses()) {
                this.innerClasses.add(innerClass);
            }
        }
        else {
            searchSuperTypes = true;
        }
        if (this.instance && searchSuperTypes) {
            final Class<?>[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; ++i) {
                this.lookupAccessibleMembers(interfaces[i]);
            }
            final Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                this.lookupAccessibleMembers(superclass);
            }
        }
    }
    
    static final class MethodSignature
    {
        private final String name;
        private final Class<?>[] args;
        
        MethodSignature(final String name, final Class<?>[] args) {
            this.name = name;
            this.args = args;
        }
        
        MethodSignature(final Method method) {
            this(method.getName(), method.getParameterTypes());
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof MethodSignature) {
                final MethodSignature ms = (MethodSignature)o;
                return ms.name.equals(this.name) && Arrays.equals(this.args, ms.args);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.name.hashCode() ^ Arrays.hashCode(this.args);
        }
        
        @Override
        public String toString() {
            final StringBuilder b = new StringBuilder();
            b.append("[MethodSignature ").append(this.name).append('(');
            if (this.args.length > 0) {
                b.append(this.args[0].getCanonicalName());
                for (int i = 1; i < this.args.length; ++i) {
                    b.append(", ").append(this.args[i].getCanonicalName());
                }
            }
            return b.append(")]").toString();
        }
    }
}
