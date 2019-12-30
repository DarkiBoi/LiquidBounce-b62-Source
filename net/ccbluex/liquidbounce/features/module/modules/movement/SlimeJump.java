// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockSlime;
import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "SlimeJump", description = "Allows you to to jump higher on slime blocks.", category = ModuleCategory.MOVEMENT)
public class SlimeJump extends Module
{
    private final FloatValue motionValue;
    private final ListValue modeValue;
    
    public SlimeJump() {
        this.motionValue = new FloatValue("Motion", 0.42f, 0.2f, 1.0f);
        this.modeValue = new ListValue("Mode", new String[] { "Set", "Add" }, "Add");
    }
    
    @EventTarget
    public void onJump(final JumpEvent event) {
        if (SlimeJump.mc.field_71439_g != null && SlimeJump.mc.field_71441_e != null && BlockUtils.getBlock(SlimeJump.mc.field_71439_g.func_180425_c().func_177977_b()) instanceof BlockSlime) {
            event.setCancelled(true);
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "set": {
                    SlimeJump.mc.field_71439_g.field_70181_x = this.motionValue.asFloat();
                    break;
                }
                case "add": {
                    final EntityPlayerSP field_71439_g = SlimeJump.mc.field_71439_g;
                    field_71439_g.field_70181_x += this.motionValue.asFloat();
                    break;
                }
            }
        }
    }
}
