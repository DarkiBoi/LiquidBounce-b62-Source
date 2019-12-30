// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.util.Collections;
import java.lang.invoke.MethodHandle;
import java.util.Map;

class BeanIntrospector extends FacetIntrospector
{
    BeanIntrospector(final Class<?> clazz) {
        super(clazz, true);
    }
    
    @Override
    Map<String, MethodHandle> getInnerClassGetters() {
        return Collections.emptyMap();
    }
    
    @Override
    MethodHandle editMethodHandle(final MethodHandle mh) {
        return mh;
    }
}
