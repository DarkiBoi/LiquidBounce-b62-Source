// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KDeclarationContainer;

public class PropertyReference0Impl extends PropertyReference0
{
    private final KDeclarationContainer owner;
    private final String name;
    private final String signature;
    
    public PropertyReference0Impl(final KDeclarationContainer owner, final String name, final String signature) {
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
    public Object get() {
        return this.getGetter().call(new Object[0]);
    }
}
