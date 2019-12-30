// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.item.ItemFishingRod;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoFish", description = "Automatically catches fish when using a rod.", category = ModuleCategory.PLAYER)
public class AutoFish extends Module
{
    private final MSTimer rodOutTimer;
    
    public AutoFish() {
        this.rodOutTimer = new MSTimer();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (AutoFish.mc.field_71439_g.func_70694_bm() == null || !(AutoFish.mc.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemFishingRod)) {
            return;
        }
        if ((this.rodOutTimer.hasTimePassed(500L) && AutoFish.mc.field_71439_g.field_71104_cf == null) || (AutoFish.mc.field_71439_g.field_71104_cf != null && AutoFish.mc.field_71439_g.field_71104_cf.field_70159_w == 0.0 && AutoFish.mc.field_71439_g.field_71104_cf.field_70179_y == 0.0 && AutoFish.mc.field_71439_g.field_71104_cf.field_70181_x != 0.0)) {
            AutoFish.mc.func_147121_ag();
            this.rodOutTimer.reset();
        }
    }
}
