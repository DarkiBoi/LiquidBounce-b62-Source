// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.scripts;

import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public class JO extends ScriptObject
{
    private static final PropertyMap map$;
    
    public static PropertyMap getInitialMap() {
        return JO.map$;
    }
    
    public JO(final PropertyMap map) {
        super(map);
    }
    
    public JO(final ScriptObject proto) {
        super(proto, getInitialMap());
    }
    
    public JO(final PropertyMap map, final long[] primitiveSpill, final Object[] objectSpill) {
        super(map, primitiveSpill, objectSpill);
    }
    
    public static ScriptObject allocate(final PropertyMap map) {
        return new JO(map);
    }
    
    static {
        map$ = PropertyMap.newMap(JO.class);
    }
}
