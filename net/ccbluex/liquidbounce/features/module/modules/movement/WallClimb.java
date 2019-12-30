// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.event.events.BlockBBEvent;
import net.minecraft.network.Packet;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockAir;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "WallClimb", description = "Allows you to climb up walls like a spider.", category = ModuleCategory.MOVEMENT)
public class WallClimb extends Module
{
    private final ListValue modeValue;
    private final ListValue clipMode;
    private final FloatValue checkerClimbMotionValue;
    private boolean glitch;
    private int waited;
    
    public WallClimb() {
        this.modeValue = new ListValue("Mode", new String[] { "Simple", "CheckerClimb", "Clip", "AAC3.3.12" }, "Simple");
        this.clipMode = new ListValue("ClipMode", new String[] { "Jump", "Fast" }, "Fast");
        this.checkerClimbMotionValue = new FloatValue("CheckerClimbMotion", 0.0f, 0.0f, 1.0f);
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (!WallClimb.mc.field_71439_g.field_70123_F || WallClimb.mc.field_71439_g.func_70617_f_() || WallClimb.mc.field_71439_g.func_70090_H() || WallClimb.mc.field_71439_g.func_180799_ab()) {
            return;
        }
        if ("simple".equalsIgnoreCase(this.modeValue.asString())) {
            event.setY(0.2);
            WallClimb.mc.field_71439_g.field_70181_x = 0.0;
        }
    }
    
    @EventTarget
    public void onUpdate(final MotionEvent event) {
        if (event.getEventState() != EventState.POST) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "clip": {
                if (WallClimb.mc.field_71439_g.field_70181_x < 0.0) {
                    this.glitch = true;
                }
                if (WallClimb.mc.field_71439_g.field_70123_F) {
                    final String lowerCase2 = this.clipMode.asString().toLowerCase();
                    switch (lowerCase2) {
                        case "jump": {
                            if (WallClimb.mc.field_71439_g.field_70122_E) {
                                WallClimb.mc.field_71439_g.func_70664_aZ();
                                break;
                            }
                            break;
                        }
                        case "fast": {
                            if (WallClimb.mc.field_71439_g.field_70122_E) {
                                WallClimb.mc.field_71439_g.field_70181_x = 0.42;
                                break;
                            }
                            if (WallClimb.mc.field_71439_g.field_70181_x < 0.0) {
                                WallClimb.mc.field_71439_g.field_70181_x = -0.3;
                                break;
                            }
                            break;
                        }
                    }
                    break;
                }
                break;
            }
            case "checkerclimb": {
                final boolean isInsideBlock = BlockUtils.collideBlockIntersects(WallClimb.mc.field_71439_g.func_174813_aQ(), block -> !(block instanceof BlockAir));
                final float motion = this.checkerClimbMotionValue.asFloat();
                if (isInsideBlock && motion != 0.0f) {
                    WallClimb.mc.field_71439_g.field_70181_x = motion;
                    break;
                }
                break;
            }
            case "aac3.3.12": {
                if (WallClimb.mc.field_71439_g.field_70123_F && !WallClimb.mc.field_71439_g.func_70617_f_()) {
                    ++this.waited;
                    if (this.waited == 1) {
                        WallClimb.mc.field_71439_g.field_70181_x = 0.43;
                    }
                    if (this.waited == 12) {
                        WallClimb.mc.field_71439_g.field_70181_x = 0.43;
                    }
                    if (this.waited == 23) {
                        WallClimb.mc.field_71439_g.field_70181_x = 0.43;
                    }
                    if (this.waited == 29) {
                        WallClimb.mc.field_71439_g.func_70107_b(WallClimb.mc.field_71439_g.field_70165_t, WallClimb.mc.field_71439_g.field_70163_u + 0.5, WallClimb.mc.field_71439_g.field_70161_v);
                    }
                    if (this.waited >= 30) {
                        this.waited = 0;
                        break;
                    }
                    break;
                }
                else {
                    if (WallClimb.mc.field_71439_g.field_70122_E) {
                        this.waited = 0;
                        break;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            if (this.glitch) {
                final float yaw = (float)MovementUtils.getDirection();
                packetPlayer.field_149479_a -= MathHelper.func_76126_a(yaw) * 1.0E-8;
                packetPlayer.field_149478_c += MathHelper.func_76134_b(yaw) * 1.0E-8;
                this.glitch = false;
            }
        }
    }
    
    @EventTarget
    public void onBlockBB(final BlockBBEvent event) {
        if (WallClimb.mc.field_71439_g == null) {
            return;
        }
        final String mode = this.modeValue.asString();
        final String lowerCase = mode.toLowerCase();
        switch (lowerCase) {
            case "checkerclimb": {
                if (event.getY() > WallClimb.mc.field_71439_g.field_70163_u) {
                    event.setBoundingBox(null);
                    break;
                }
                break;
            }
            case "clip": {
                if (event.getBlock() != null && WallClimb.mc.field_71439_g != null && event.getBlock() instanceof BlockAir && event.getY() < WallClimb.mc.field_71439_g.field_70163_u && WallClimb.mc.field_71439_g.field_70123_F && !WallClimb.mc.field_71439_g.func_70617_f_() && !WallClimb.mc.field_71439_g.func_70090_H() && !WallClimb.mc.field_71439_g.func_180799_ab()) {
                    event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).func_72317_d(WallClimb.mc.field_71439_g.field_70165_t, (double)((int)WallClimb.mc.field_71439_g.field_70163_u - 1), WallClimb.mc.field_71439_g.field_70161_v));
                    break;
                }
                break;
            }
        }
    }
}
