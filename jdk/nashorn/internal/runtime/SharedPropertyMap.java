// 
// Decompiled by Procyon v0.5.36
// 

package jdk.nashorn.internal.runtime;

import java.lang.invoke.SwitchPoint;

public final class SharedPropertyMap extends PropertyMap
{
    private SwitchPoint switchPoint;
    private static final long serialVersionUID = 2166297719721778876L;
    
    public SharedPropertyMap(final PropertyMap map) {
        super(map);
        this.switchPoint = new SwitchPoint();
    }
    
    @Override
    public void propertyAdded(final Property property, final boolean isSelf) {
        if (isSelf) {
            this.invalidateSwitchPoint();
        }
        super.propertyAdded(property, isSelf);
    }
    
    @Override
    public void propertyDeleted(final Property property, final boolean isSelf) {
        if (isSelf) {
            this.invalidateSwitchPoint();
        }
        super.propertyDeleted(property, isSelf);
    }
    
    @Override
    public void propertyModified(final Property oldProperty, final Property newProperty, final boolean isSelf) {
        if (isSelf) {
            this.invalidateSwitchPoint();
        }
        super.propertyModified(oldProperty, newProperty, isSelf);
    }
    
    @Override
    synchronized boolean isValidSharedProtoMap() {
        return this.switchPoint != null;
    }
    
    @Override
    synchronized SwitchPoint getSharedProtoSwitchPoint() {
        return this.switchPoint;
    }
    
    synchronized void invalidateSwitchPoint() {
        if (this.switchPoint != null) {
            assert !this.switchPoint.hasBeenInvalidated();
            SwitchPoint.invalidateAll(new SwitchPoint[] { this.switchPoint });
            this.switchPoint = null;
        }
    }
}
