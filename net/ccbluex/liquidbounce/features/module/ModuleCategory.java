// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module;

public enum ModuleCategory
{
    COMBAT("Combat"), 
    PLAYER("Player"), 
    MOVEMENT("Movement"), 
    RENDER("Render"), 
    WORLD("World"), 
    MISC("Misc"), 
    EXPLOIT("Exploit"), 
    FUN("Fun");
    
    private final String displayName;
    
    private ModuleCategory(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
}
