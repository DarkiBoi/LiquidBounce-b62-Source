// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FastPlace", description = "Allows you to place blocks faster.", category = ModuleCategory.WORLD)
public class FastPlace extends Module
{
    public final IntegerValue speedValue;
    
    public FastPlace() {
        this.speedValue = new IntegerValue("Speed", 0, 0, 4);
    }
}
