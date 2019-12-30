// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class NCPYPort extends SpeedMode
{
    private int jumps;
    
    public NCPYPort() {
        super("NCPYPort");
    }
    
    @Override
    public void onMotion() {
        if (NCPYPort.mc.field_71439_g.func_70617_f_() || NCPYPort.mc.field_71439_g.func_70090_H() || NCPYPort.mc.field_71439_g.func_180799_ab() || NCPYPort.mc.field_71439_g.field_70134_J || !MovementUtils.isMoving() || NCPYPort.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (this.jumps >= 4 && NCPYPort.mc.field_71439_g.field_70122_E) {
            this.jumps = 0;
        }
        if (NCPYPort.mc.field_71439_g.field_70122_E) {
            NCPYPort.mc.field_71439_g.field_70181_x = ((this.jumps <= 1) ? 0.41999998688697815 : 0.4000000059604645);
            final float f = NCPYPort.mc.field_71439_g.field_70177_z * 0.017453292f;
            final EntityPlayerSP field_71439_g = NCPYPort.mc.field_71439_g;
            field_71439_g.field_70159_w -= MathHelper.func_76126_a(f) * 0.2f;
            final EntityPlayerSP field_71439_g2 = NCPYPort.mc.field_71439_g;
            field_71439_g2.field_70179_y += MathHelper.func_76134_b(f) * 0.2f;
            ++this.jumps;
        }
        else if (this.jumps <= 1) {
            NCPYPort.mc.field_71439_g.field_70181_x = -5.0;
        }
        MovementUtils.strafe();
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
    }
}
