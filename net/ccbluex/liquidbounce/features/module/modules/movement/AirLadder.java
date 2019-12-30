// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.BlockVine;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockLadder;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AirLadder", description = "Allows you to climb up ladders/vines without touching them.", category = ModuleCategory.MOVEMENT)
public class AirLadder extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if ((BlockUtils.getBlock(new BlockPos(AirLadder.mc.field_71439_g.field_70165_t, AirLadder.mc.field_71439_g.field_70163_u + 1.0, AirLadder.mc.field_71439_g.field_70161_v)) instanceof BlockLadder && AirLadder.mc.field_71439_g.field_70123_F) || BlockUtils.getBlock(new BlockPos(AirLadder.mc.field_71439_g.field_70165_t, AirLadder.mc.field_71439_g.field_70163_u, AirLadder.mc.field_71439_g.field_70161_v)) instanceof BlockVine || BlockUtils.getBlock(new BlockPos(AirLadder.mc.field_71439_g.field_70165_t, AirLadder.mc.field_71439_g.field_70163_u + 1.0, AirLadder.mc.field_71439_g.field_70161_v)) instanceof BlockVine) {
            AirLadder.mc.field_71439_g.field_70181_x = 0.15;
            AirLadder.mc.field_71439_g.field_70159_w = 0.0;
            AirLadder.mc.field_71439_g.field_70179_y = 0.0;
        }
    }
}
