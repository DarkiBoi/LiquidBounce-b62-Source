// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Chams", description = "Allows you to see targets through blocks.", category = ModuleCategory.RENDER)
public class Chams extends Module
{
    public final BoolValue targetsValue;
    public final BoolValue chestsValue;
    public final BoolValue itemsValue;
    
    public Chams() {
        this.targetsValue = new BoolValue("Targets", true);
        this.chestsValue = new BoolValue("Chests", true);
        this.itemsValue = new BoolValue("Items", true);
    }
}
