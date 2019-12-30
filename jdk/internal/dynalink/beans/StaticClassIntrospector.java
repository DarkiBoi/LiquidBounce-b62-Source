// 
// Decompiled by Procyon v0.5.36
// 

package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.lang.invoke.MethodHandle;
import java.util.Map;

class StaticClassIntrospector extends FacetIntrospector
{
    StaticClassIntrospector(final Class<?> clazz) {
        super(clazz, false);
    }
    
    @Override
    Map<String, MethodHandle> getInnerClassGetters() {
        final Map<String, MethodHandle> map = new HashMap<String, MethodHandle>();
        for (final Class<?> innerClass : this.membersLookup.getInnerClasses()) {
            map.put(innerClass.getSimpleName(), this.editMethodHandle(MethodHandles.constant(StaticClass.class, StaticClass.forClass(innerClass))));
        }
        return map;
    }
    
    @Override
    MethodHandle editMethodHandle(final MethodHandle mh) {
        return editStaticMethodHandle(mh);
    }
    
    static MethodHandle editStaticMethodHandle(final MethodHandle mh) {
        return dropReceiver(mh, Object.class);
    }
    
    static MethodHandle editConstructorMethodHandle(final MethodHandle cmh) {
        return dropReceiver(cmh, StaticClass.class);
    }
    
    private static MethodHandle dropReceiver(final MethodHandle mh, final Class<?> receiverClass) {
        MethodHandle newHandle = MethodHandles.dropArguments(mh, 0, receiverClass);
        if (mh.isVarargsCollector() && !newHandle.isVarargsCollector()) {
            final MethodType type = mh.type();
            newHandle = newHandle.asVarargsCollector(type.parameterType(type.parameterCount() - 1));
        }
        return newHandle;
    }
}
