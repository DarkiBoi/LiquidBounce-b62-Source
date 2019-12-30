// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "TrueSight", description = "Allows you to see invisible entities and barriers.", category = ModuleCategory.RENDER)
public class TrueSight extends Module
{
    public final BoolValue barriesValue;
    public final BoolValue entitiesValue;
    
    public TrueSight() {
        this.barriesValue = new BoolValue("Barriers", true);
        this.entitiesValue = new BoolValue("Entities", true);
    }
}
