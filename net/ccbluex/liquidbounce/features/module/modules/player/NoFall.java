// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.minecraft.block.Block;
import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockLiquid;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoFall", description = "Prevents you from taking fall damage.", category = ModuleCategory.PLAYER)
public class NoFall extends Module
{
    private final ListValue modeValue;
    private int state;
    private boolean jumped;
    private final TickTimer spartanTimer;
    
    public NoFall() {
        this.modeValue = new ListValue("Mode", new String[] { "SpoofGround", "NoGround", "Packet", "AAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "Spartan", "CubeCraft" }, "SpoofGround");
        this.spartanTimer = new TickTimer();
    }
    
    @EventTarget(ignoreCondition = true)
    public void onUpdate(final UpdateEvent event) {
        if (NoFall.mc.field_71439_g.field_70122_E) {
            this.jumped = false;
        }
        if (NoFall.mc.field_71439_g.field_70181_x > 0.0) {
            this.jumped = true;
        }
        if (!this.getState() || ModuleManager.getModule(FreeCam.class).getState()) {
            return;
        }
        if (BlockUtils.collideBlock(NoFall.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(NoFall.mc.field_71439_g.func_174813_aQ().field_72336_d, NoFall.mc.field_71439_g.func_174813_aQ().field_72337_e, NoFall.mc.field_71439_g.func_174813_aQ().field_72334_f, NoFall.mc.field_71439_g.func_174813_aQ().field_72340_a, NoFall.mc.field_71439_g.func_174813_aQ().field_72338_b - 0.01, NoFall.mc.field_71439_g.func_174813_aQ().field_72339_c), block -> block instanceof BlockLiquid)) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "packet": {
                if (NoFall.mc.field_71439_g.field_70143_R > 2.0f) {
                    NoFall.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    break;
                }
                break;
            }
            case "cubecraft": {
                if (NoFall.mc.field_71439_g.field_70143_R > 2.0f) {
                    NoFall.mc.field_71439_g.field_70122_E = false;
                    NoFall.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer(true));
                    break;
                }
                break;
            }
            case "aac": {
                if (NoFall.mc.field_71439_g.field_70143_R > 2.0f) {
                    NoFall.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    this.state = 2;
                }
                else if (this.state == 2 && NoFall.mc.field_71439_g.field_70143_R < 2.0f) {
                    NoFall.mc.field_71439_g.field_70181_x = 0.1;
                    this.state = 3;
                    return;
                }
                switch (this.state) {
                    case 3: {
                        NoFall.mc.field_71439_g.field_70181_x = 0.1;
                        this.state = 4;
                        break;
                    }
                    case 4: {
                        NoFall.mc.field_71439_g.field_70181_x = 0.1;
                        this.state = 5;
                        break;
                    }
                    case 5: {
                        NoFall.mc.field_71439_g.field_70181_x = 0.1;
                        this.state = 1;
                        break;
                    }
                }
                break;
            }
            case "laac": {
                if (!this.jumped && NoFall.mc.field_71439_g.field_70122_E && !NoFall.mc.field_71439_g.func_70617_f_() && !NoFall.mc.field_71439_g.func_70090_H() && !NoFall.mc.field_71439_g.field_70134_J) {
                    NoFall.mc.field_71439_g.field_70181_x = -6.0;
                    break;
                }
                break;
            }
            case "aac3.3.11": {
                if (NoFall.mc.field_71439_g.field_70143_R > 2.0f) {
                    final EntityPlayerSP field_71439_g = NoFall.mc.field_71439_g;
                    final EntityPlayerSP field_71439_g2 = NoFall.mc.field_71439_g;
                    final double n2 = 0.0;
                    field_71439_g2.field_70179_y = n2;
                    field_71439_g.field_70159_w = n2;
                    NoFall.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.field_71439_g.field_70165_t, NoFall.mc.field_71439_g.field_70163_u - 0.001, NoFall.mc.field_71439_g.field_70161_v, NoFall.mc.field_71439_g.field_70122_E));
                    NoFall.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    break;
                }
                break;
            }
            case "aac3.3.15": {
                if (NoFall.mc.field_71439_g.field_70143_R > 2.0f) {
                    NoFall.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.field_71439_g.field_70165_t, Double.NaN, NoFall.mc.field_71439_g.field_70161_v, false));
                    NoFall.mc.field_71439_g.field_70143_R = -9999.0f;
                    break;
                }
                break;
            }
            case "spartan": {
                this.spartanTimer.update();
                if (NoFall.mc.field_71439_g.field_70143_R > 1.5 && this.spartanTimer.hasTimePassed(10)) {
                    NoFall.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.field_71439_g.field_70165_t, NoFall.mc.field_71439_g.field_70163_u + 10.0, NoFall.mc.field_71439_g.field_70161_v, true));
                    NoFall.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.field_71439_g.field_70165_t, NoFall.mc.field_71439_g.field_70163_u - 10.0, NoFall.mc.field_71439_g.field_70161_v, true));
                    this.spartanTimer.reset();
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();
        final String mode = this.modeValue.asString();
        if (packet instanceof C03PacketPlayer && mode.equalsIgnoreCase("SpoofGround")) {
            ((C03PacketPlayer)packet).field_149474_g = true;
        }
        if (packet instanceof C03PacketPlayer && mode.equalsIgnoreCase("NoGround")) {
            ((C03PacketPlayer)packet).field_149474_g = false;
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (BlockUtils.collideBlock(NoFall.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(NoFall.mc.field_71439_g.func_174813_aQ().field_72336_d, NoFall.mc.field_71439_g.func_174813_aQ().field_72337_e, NoFall.mc.field_71439_g.func_174813_aQ().field_72334_f, NoFall.mc.field_71439_g.func_174813_aQ().field_72340_a, NoFall.mc.field_71439_g.func_174813_aQ().field_72338_b - 0.01, NoFall.mc.field_71439_g.func_174813_aQ().field_72339_c), block -> block instanceof BlockLiquid)) {
            return;
        }
        final String lowerCase = this.modeValue.asString().toLowerCase();
        switch (lowerCase) {
            case "laac": {
                if (!this.jumped && !NoFall.mc.field_71439_g.field_70122_E && !NoFall.mc.field_71439_g.func_70617_f_() && !NoFall.mc.field_71439_g.func_70090_H() && !NoFall.mc.field_71439_g.field_70134_J && NoFall.mc.field_71439_g.field_70181_x < 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget(ignoreCondition = true)
    public void onJump(final JumpEvent event) {
        this.jumped = true;
    }
    
    @Override
    public String getTag() {
        return this.modeValue.asString();
    }
}
