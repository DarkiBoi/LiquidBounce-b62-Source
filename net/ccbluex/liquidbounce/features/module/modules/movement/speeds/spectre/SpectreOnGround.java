// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class SpectreOnGround extends SpeedMode
{
    private int speedUp;
    
    public SpectreOnGround() {
        super("SpectreOnGround");
    }
    
    @Override
    public void onMotion() {
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
        if (!MovementUtils.isMoving() || SpectreOnGround.mc.field_71439_g.field_71158_b.field_78901_c) {
            return;
        }
        if (this.speedUp >= 10) {
            if (SpectreOnGround.mc.field_71439_g.field_70122_E) {
                SpectreOnGround.mc.field_71439_g.field_70159_w = 0.0;
                SpectreOnGround.mc.field_71439_g.field_70179_y = 0.0;
                this.speedUp = 0;
            }
            return;
        }
        if (SpectreOnGround.mc.field_71439_g.field_70122_E && SpectreOnGround.mc.field_71474_y.field_74351_w.func_151470_d()) {
            final float f = SpectreOnGround.mc.field_71439_g.field_70177_z * 0.017453292f;
            final EntityPlayerSP field_71439_g = SpectreOnGround.mc.field_71439_g;
            field_71439_g.field_70159_w -= MathHelper.func_76126_a(f) * 0.145f;
            final EntityPlayerSP field_71439_g2 = SpectreOnGround.mc.field_71439_g;
            field_71439_g2.field_70179_y += MathHelper.func_76134_b(f) * 0.145f;
            event.setX(SpectreOnGround.mc.field_71439_g.field_70159_w);
            event.setY(0.005);
            event.setZ(SpectreOnGround.mc.field_71439_g.field_70179_y);
            ++this.speedUp;
        }
    }
}
