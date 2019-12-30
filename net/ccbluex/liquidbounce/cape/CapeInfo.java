// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.cape;

import net.minecraft.util.ResourceLocation;

public class CapeInfo
{
    private final ResourceLocation resourceLocation;
    private boolean capeAvailable;
    
    public CapeInfo(final ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }
    
    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }
    
    public boolean isCapeAvailable() {
        return this.capeAvailable;
    }
    
    public void setCapeAvailable(final boolean capeAvailable) {
        this.capeAvailable = capeAvailable;
    }
}
