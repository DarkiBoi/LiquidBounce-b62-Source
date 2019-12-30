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

@ModuleInfo(name = "NoClip", description = "Allows you to freely move through walls (A sandblock has to fall on your head).", category = ModuleCategory.MOVEMENT)
public class NoClip extends Module
{
    @Override
    public void onDisable() {
        if (NoClip.mc.field_71439_g == null) {
            return;
        }
        NoClip.mc.field_71439_g.field_70145_X = false;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        NoClip.mc.field_71439_g.field_70145_X = true;
        NoClip.mc.field_71439_g.field_70143_R = 0.0f;
        NoClip.mc.field_71439_g.field_70122_E = false;
        NoClip.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
        NoClip.mc.field_71439_g.field_70159_w = 0.0;
        NoClip.mc.field_71439_g.field_70181_x = 0.0;
        NoClip.mc.field_71439_g.field_70179_y = 0.0;
        final float speed = 0.32f;
        NoClip.mc.field_71439_g.field_70747_aH = speed;
        if (NoClip.mc.field_71474_y.field_74314_A.func_151470_d()) {
            final EntityPlayerSP field_71439_g = NoClip.mc.field_71439_g;
            field_71439_g.field_70181_x += speed;
        }
        if (NoClip.mc.field_71474_y.field_74311_E.func_151470_d()) {
            final EntityPlayerSP field_71439_g2 = NoClip.mc.field_71439_g;
            field_71439_g2.field_70181_x -= speed;
        }
    }
}
