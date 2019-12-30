// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Teams", description = "Prevents Killaura from attacking team mates.", category = ModuleCategory.MISC)
public class Teams extends Module
{
    private final BoolValue scoreboardValue;
    private final BoolValue colorValue;
    
    public Teams() {
        this.scoreboardValue = new BoolValue("ScoreboardTeam", true);
        this.colorValue = new BoolValue("Color", true);
    }
    
    public boolean isInYourTeam(final EntityLivingBase entity) {
        if (Teams.mc.field_71439_g != null && entity != null) {
            if (this.scoreboardValue.asBoolean() && Teams.mc.field_71439_g.func_96124_cp() != null && entity.func_96124_cp() != null && Teams.mc.field_71439_g.func_96124_cp().func_142054_a(entity.func_96124_cp())) {
                return true;
            }
            if (this.colorValue.asBoolean() && Teams.mc.field_71439_g.func_145748_c_() != null && entity.func_145748_c_() != null) {
                final String targetName = entity.func_145748_c_().func_150254_d().replace("§r", "");
                final String clientName = Teams.mc.field_71439_g.func_145748_c_().func_150254_d().replace("§r", "");
                return targetName.startsWith("§" + clientName.charAt(1));
            }
        }
        return false;
    }
}
