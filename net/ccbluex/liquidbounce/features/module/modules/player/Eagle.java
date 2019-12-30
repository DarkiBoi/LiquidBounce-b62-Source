// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Eagle", description = "Makes you eagle (aka. FastBridge).", category = ModuleCategory.PLAYER)
public class Eagle extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        Eagle.mc.field_71474_y.field_74311_E.field_74513_e = (Eagle.mc.field_71441_e.func_180495_p(new BlockPos(Eagle.mc.field_71439_g.field_70165_t, Eagle.mc.field_71439_g.field_70163_u - 1.0, Eagle.mc.field_71439_g.field_70161_v)).func_177230_c() == Blocks.field_150350_a);
    }
    
    @Override
    public void onDisable() {
        if (Eagle.mc.field_71439_g == null) {
            return;
        }
        if (!GameSettings.func_100015_a(Eagle.mc.field_71474_y.field_74311_E)) {
            Eagle.mc.field_71474_y.field_74311_E.field_74513_e = false;
        }
    }
}
