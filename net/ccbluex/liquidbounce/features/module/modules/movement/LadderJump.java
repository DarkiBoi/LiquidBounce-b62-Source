// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "LadderJump", description = "Boosts you up when touching a ladder.", category = ModuleCategory.MOVEMENT)
public class LadderJump extends Module
{
    static boolean jumped;
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (LadderJump.mc.field_71439_g.field_70122_E) {
            if (LadderJump.mc.field_71439_g.func_70617_f_()) {
                LadderJump.mc.field_71439_g.field_70181_x = 1.5;
                LadderJump.jumped = true;
            }
            else {
                LadderJump.jumped = false;
            }
        }
        else if (!LadderJump.mc.field_71439_g.func_70617_f_() && LadderJump.jumped) {
            final EntityPlayerSP field_71439_g = LadderJump.mc.field_71439_g;
            field_71439_g.field_70181_x += 0.059;
        }
    }
}
