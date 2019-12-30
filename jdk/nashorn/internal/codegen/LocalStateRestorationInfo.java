// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.codegen.types.Type;

public class LocalStateRestorationInfo
{
    private final Type[] localVariableTypes;
    private final int[] stackLoads;
    
    LocalStateRestorationInfo(final Type[] localVariableTypes, final int[] stackLoads) {
        this.localVariableTypes = localVariableTypes;
        this.stackLoads = stackLoads;
    }
    
    public Type[] getLocalVariableTypes() {
        return this.localVariableTypes.clone();
    }
    
    public int[] getStackLoads() {
        return this.stackLoads.clone();
    }
}
