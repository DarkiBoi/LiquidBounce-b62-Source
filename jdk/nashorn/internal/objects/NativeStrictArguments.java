// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.objects;

import java.util.Collection;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import java.util.ArrayList;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ScriptFunction;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.PropertyMap;
import java.lang.invoke.MethodHandle;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeStrictArguments extends ScriptObject
{
    private static final MethodHandle G$LENGTH;
    private static final MethodHandle S$LENGTH;
    private static final PropertyMap map$;
    private Object length;
    private final Object[] namedArgs;
    
    static PropertyMap getInitialMap() {
        return NativeStrictArguments.map$;
    }
    
    NativeStrictArguments(final Object[] values, final int numParams, final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        this.setIsArguments();
        final ScriptFunction func = Global.instance().getTypeErrorThrower();
        final int flags = 6;
        this.initUserAccessors("caller", 6, func, func);
        this.initUserAccessors("callee", 6, func, func);
        this.setArray(ArrayData.allocate(values));
        this.length = values.length;
        this.namedArgs = new Object[numParams];
        if (numParams > values.length) {
            Arrays.fill(this.namedArgs, ScriptRuntime.UNDEFINED);
        }
        System.arraycopy(values, 0, this.namedArgs, 0, Math.min(this.namedArgs.length, values.length));
    }
    
    @Override
    public String getClassName() {
        return "Arguments";
    }
    
    @Override
    public Object getArgument(final int key) {
        return (key >= 0 && key < this.namedArgs.length) ? this.namedArgs[key] : ScriptRuntime.UNDEFINED;
    }
    
    @Override
    public void setArgument(final int key, final Object value) {
        if (key >= 0 && key < this.namedArgs.length) {
            this.namedArgs[key] = value;
        }
    }
    
    public static Object G$length(final Object self) {
        if (self instanceof NativeStrictArguments) {
            return ((NativeStrictArguments)self).getArgumentsLength();
        }
        return 0;
    }
    
    public static void S$length(final Object self, final Object value) {
        if (self instanceof NativeStrictArguments) {
            ((NativeStrictArguments)self).setArgumentsLength(value);
        }
    }
    
    private Object getArgumentsLength() {
        return this.length;
    }
    
    private void setArgumentsLength(final Object length) {
        this.length = length;
    }
    
    private static MethodHandle findOwnMH(final String name, final Class<?> rtype, final Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeStrictArguments.class, name, Lookup.MH.type(rtype, types));
    }
    
    static {
        G$LENGTH = findOwnMH("G$length", Object.class, Object.class);
        S$LENGTH = findOwnMH("S$length", Void.TYPE, Object.class, Object.class);
        final ArrayList<Property> properties = new ArrayList<Property>(1);
        properties.add(AccessorProperty.create("length", 2, NativeStrictArguments.G$LENGTH, NativeStrictArguments.S$LENGTH));
        PropertyMap map = PropertyMap.newMap(properties);
        final int flags = 6;
        map = map.addPropertyNoHistory(map.newUserAccessors("caller", 6));
        map = (map$ = map.addPropertyNoHistory(map.newUserAccessors("callee", 6)));
    }
}
