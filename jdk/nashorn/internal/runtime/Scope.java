// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.codegen.CompilerConstants;
import java.util.concurrent.atomic.LongAdder;

public class Scope extends ScriptObject
{
    private int splitState;
    private static final LongAdder count;
    public static final CompilerConstants.Call GET_SPLIT_STATE;
    public static final CompilerConstants.Call SET_SPLIT_STATE;
    
    public Scope(final PropertyMap map) {
        super(map);
        this.splitState = -1;
        incrementCount();
    }
    
    public Scope(final ScriptObject proto, final PropertyMap map) {
        super(proto, map);
        this.splitState = -1;
        incrementCount();
    }
    
    public Scope(final PropertyMap map, final long[] primitiveSpill, final Object[] objectSpill) {
        super(map, primitiveSpill, objectSpill);
        this.splitState = -1;
        incrementCount();
    }
    
    @Override
    public boolean isScope() {
        return true;
    }
    
    @Override
    boolean hasWithScope() {
        for (ScriptObject obj = this; obj != null; obj = obj.getProto()) {
            if (obj instanceof WithObject) {
                return true;
            }
        }
        return false;
    }
    
    public int getSplitState() {
        return this.splitState;
    }
    
    public void setSplitState(final int state) {
        this.splitState = state;
    }
    
    public static long getScopeCount() {
        return (Scope.count != null) ? Scope.count.sum() : 0L;
    }
    
    private static void incrementCount() {
        if (Context.DEBUG) {
            Scope.count.increment();
        }
    }
    
    static {
        count = (Context.DEBUG ? new LongAdder() : null);
        GET_SPLIT_STATE = CompilerConstants.virtualCallNoLookup(Scope.class, "getSplitState", Integer.TYPE, (Class<?>[])new Class[0]);
        SET_SPLIT_STATE = CompilerConstants.virtualCallNoLookup(Scope.class, "setSplitState", Void.TYPE, Integer.TYPE);
    }
}
