// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

public class FunctionScope extends Scope
{
    public final ScriptObject arguments;
    
    public FunctionScope(final PropertyMap map, final ScriptObject callerScope, final ScriptObject arguments) {
        super(callerScope, map);
        this.arguments = arguments;
    }
    
    public FunctionScope(final PropertyMap map, final ScriptObject callerScope) {
        super(callerScope, map);
        this.arguments = null;
    }
    
    public FunctionScope(final PropertyMap map, final long[] primitiveSpill, final Object[] objectSpill) {
        super(map, primitiveSpill, objectSpill);
        this.arguments = null;
    }
}
