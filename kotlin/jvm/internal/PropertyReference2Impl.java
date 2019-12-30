// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KDeclarationContainer;

public class PropertyReference2Impl extends PropertyReference2
{
    private final KDeclarationContainer owner;
    private final String name;
    private final String signature;
    
    public PropertyReference2Impl(final KDeclarationContainer owner, final String name, final String signature) {
        this.owner = owner;
        this.name = name;
        this.signature = signature;
    }
    
    @Override
    public KDeclarationContainer getOwner() {
        return this.owner;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getSignature() {
        return this.signature;
    }
    
    @Override
    public Object get(final Object receiver1, final Object receiver2) {
        return this.getGetter().call(receiver1, receiver2);
    }
}
