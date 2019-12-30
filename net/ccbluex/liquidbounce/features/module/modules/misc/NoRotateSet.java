// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoRotateSet", description = "Prevents the server from rotating your head.", category = ModuleCategory.MISC)
public class NoRotateSet extends Module
{
    private final BoolValue confirmValue;
    private final BoolValue illegalRotationValue;
    private final BoolValue noZeroValue;
    
    public NoRotateSet() {
        this.confirmValue = new BoolValue("Confirm", true);
        this.illegalRotationValue = new BoolValue("ConfirmIllegalRotation", false);
        this.noZeroValue = new BoolValue("NoZero", false);
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (NoRotateSet.mc.field_71439_g == null) {
            return;
        }
        final Packet packet = event.getPacket();
        if (packet instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook sPacketPlayerPosLook = (S08PacketPlayerPosLook)packet;
            if (this.noZeroValue.asBoolean() && sPacketPlayerPosLook.func_148931_f() == 0.0f && sPacketPlayerPosLook.func_148930_g() == 0.0f) {
                return;
            }
            if ((this.illegalRotationValue.asBoolean() || (sPacketPlayerPosLook.func_148930_g() <= 90.0f && sPacketPlayerPosLook.func_148930_g() >= -90.0f && RotationUtils.lastLook != null && sPacketPlayerPosLook.func_148931_f() != RotationUtils.lastLook[0] && sPacketPlayerPosLook.func_148930_g() != RotationUtils.lastLook[1])) && this.confirmValue.asBoolean()) {
                NoRotateSet.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(sPacketPlayerPosLook.func_148931_f(), sPacketPlayerPosLook.func_148930_g(), NoRotateSet.mc.field_71439_g.field_70122_E));
            }
            sPacketPlayerPosLook.field_148936_d = NoRotateSet.mc.field_71439_g.field_70177_z;
            sPacketPlayerPosLook.field_148937_e = NoRotateSet.mc.field_71439_g.field_70125_A;
        }
    }
}
