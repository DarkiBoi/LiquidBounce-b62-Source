// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.scripts;

import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public class JD extends ScriptObject
{
    private static final PropertyMap map$;
    
    public static PropertyMap getInitialMap() {
        return JD.map$;
    }
    
    public JD(final PropertyMap map) {
        super(map);
    }
    
    public JD(final ScriptObject proto) {
        super(proto, getInitialMap());
    }
    
    public JD(final PropertyMap map, final long[] primitiveSpill, final Object[] objectSpill) {
        super(map, primitiveSpill, objectSpill);
    }
    
    public static ScriptObject allocate(final PropertyMap map) {
        return new JD(map);
    }
    
    static {
        map$ = PropertyMap.newMap(JD.class);
    }
}
