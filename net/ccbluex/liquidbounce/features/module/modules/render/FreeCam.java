// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FreeCam", description = "Allows you to move out of your body.", category = ModuleCategory.RENDER)
public class FreeCam extends Module
{
    private final FloatValue speedValue;
    private final BoolValue flyValue;
    private final BoolValue noClipValue;
    private EntityOtherPlayerMP fakePlayer;
    private double oldX;
    private double oldY;
    private double oldZ;
    
    public FreeCam() {
        this.speedValue = new FloatValue("Speed", 0.8f, 0.1f, 2.0f);
        this.flyValue = new BoolValue("Fly", true);
        this.noClipValue = new BoolValue("NoClip", true);
        this.fakePlayer = null;
    }
    
    @Override
    public void onEnable() {
        if (FreeCam.mc.field_71439_g == null) {
            return;
        }
        this.oldX = FreeCam.mc.field_71439_g.field_70165_t;
        this.oldY = FreeCam.mc.field_71439_g.field_70163_u;
        this.oldZ = FreeCam.mc.field_71439_g.field_70161_v;
        (this.fakePlayer = new EntityOtherPlayerMP((World)FreeCam.mc.field_71441_e, FreeCam.mc.field_71439_g.func_146103_bH())).func_71049_a((EntityPlayer)FreeCam.mc.field_71439_g, true);
        this.fakePlayer.field_70759_as = FreeCam.mc.field_71439_g.field_70759_as;
        this.fakePlayer.func_82149_j((Entity)FreeCam.mc.field_71439_g);
        FreeCam.mc.field_71441_e.func_73027_a(-1000, (Entity)this.fakePlayer);
        if (this.noClipValue.asBoolean()) {
            FreeCam.mc.field_71439_g.field_70145_X = true;
        }
    }
    
    @Override
    public void onDisable() {
        if (FreeCam.mc.field_71439_g == null || this.fakePlayer == null) {
            return;
        }
        FreeCam.mc.field_71439_g.func_70080_a(this.oldX, this.oldY, this.oldZ, FreeCam.mc.field_71439_g.field_70177_z, FreeCam.mc.field_71439_g.field_70125_A);
        FreeCam.mc.field_71441_e.func_73028_b(this.fakePlayer.func_145782_y());
        this.fakePlayer = null;
        FreeCam.mc.field_71439_g.field_70159_w = 0.0;
        FreeCam.mc.field_71439_g.field_70181_x = 0.0;
        FreeCam.mc.field_71439_g.field_70179_y = 0.0;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.noClipValue.asBoolean()) {
            FreeCam.mc.field_71439_g.field_70145_X = true;
        }
        FreeCam.mc.field_71439_g.field_70143_R = 0.0f;
        if (this.flyValue.asBoolean()) {
            final float value = this.speedValue.asFloat();
            FreeCam.mc.field_71439_g.field_70181_x = 0.0;
            FreeCam.mc.field_71439_g.field_70159_w = 0.0;
            FreeCam.mc.field_71439_g.field_70179_y = 0.0;
            if (FreeCam.mc.field_71474_y.field_74314_A.func_151470_d()) {
                final EntityPlayerSP field_71439_g = FreeCam.mc.field_71439_g;
                field_71439_g.field_70181_x += value;
            }
            if (FreeCam.mc.field_71474_y.field_74311_E.func_151470_d()) {
                final EntityPlayerSP field_71439_g2 = FreeCam.mc.field_71439_g;
                field_71439_g2.field_70181_x -= value;
            }
            MovementUtils.strafe(value);
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction) {
            event.setCancelled(true);
        }
    }
}
