// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Parkour", description = "Automatically jumps when reaching the edge of a block.", category = ModuleCategory.MOVEMENT)
public class Parkour extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (MovementUtils.isMoving() && Parkour.mc.field_71439_g.field_70122_E && !Parkour.mc.field_71439_g.func_70093_af() && !Parkour.mc.field_71474_y.field_74311_E.func_151470_d() && !Parkour.mc.field_71474_y.field_74314_A.func_151470_d() && Parkour.mc.field_71441_e.func_72945_a((Entity)Parkour.mc.field_71439_g, Parkour.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -0.5, 0.0).func_72314_b(-0.001, 0.0, -0.001)).isEmpty()) {
            Parkour.mc.field_71439_g.func_70664_aZ();
        }
    }
}
