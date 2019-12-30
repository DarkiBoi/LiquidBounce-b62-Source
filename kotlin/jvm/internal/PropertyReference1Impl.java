// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.reflect.KDeclarationContainer;

public class PropertyReference1Impl extends PropertyReference1
{
    private final KDeclarationContainer owner;
    private final String name;
    private final String signature;
    
    public PropertyReference1Impl(final KDeclarationContainer owner, final String name, final String signature) {
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
    public Object get(final Object receiver) {
        return this.getGetter().call(receiver);
    }
}
