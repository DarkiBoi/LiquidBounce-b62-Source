// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import jdk.internal.dynalink.support.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.Field;
import java.util.Collection;
import java.lang.invoke.MethodHandle;
import java.util.Map;

abstract class FacetIntrospector
{
    private final Class<?> clazz;
    private final boolean instance;
    private final boolean isRestricted;
    protected final AccessibleMembersLookup membersLookup;
    
    FacetIntrospector(final Class<?> clazz, final boolean instance) {
        this.clazz = clazz;
        this.instance = instance;
        this.isRestricted = CheckRestrictedPackage.isRestrictedClass(clazz);
        this.membersLookup = new AccessibleMembersLookup(clazz, instance);
    }
    
    abstract Map<String, MethodHandle> getInnerClassGetters();
    
    Collection<Field> getFields() {
        if (this.isRestricted) {
            return (Collection<Field>)Collections.emptySet();
        }
        final Field[] fields = this.clazz.getFields();
        final Collection<Field> cfields = new ArrayList<Field>(fields.length);
        for (final Field field : fields) {
            final boolean isStatic = Modifier.isStatic(field.getModifiers());
            if (!isStatic || this.clazz == field.getDeclaringClass()) {
                if (this.instance != isStatic && this.isAccessible(field)) {
                    cfields.add(field);
                }
            }
        }
        return cfields;
    }
    
    boolean isAccessible(final Member m) {
        final Class<?> declaring = m.getDeclaringClass();
        return declaring == this.clazz || !CheckRestrictedPackage.isRestrictedClass(declaring);
    }
    
    Collection<Method> getMethods() {
        return this.membersLookup.getMethods();
    }
    
    MethodHandle unreflectGetter(final Field field) {
        return this.editMethodHandle(Lookup.PUBLIC.unreflectGetter(field));
    }
    
    MethodHandle unreflectSetter(final Field field) {
        return this.editMethodHandle(Lookup.PUBLIC.unreflectSetter(field));
    }
    
    abstract MethodHandle editMethodHandle(final MethodHandle p0);
}
