// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.potion.Potion;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;

public class YPort extends SpeedMode
{
    private double moveSpeed;
    private int level;
    private double lastDist;
    private int timerDelay;
    private boolean safeJump;
    
    public YPort() {
        super("YPort");
        this.moveSpeed = 0.2873;
        this.level = 1;
    }
    
    @Override
    public void onMotion() {
        if (!this.safeJump && !YPort.mc.field_71474_y.field_74314_A.func_151470_d() && !YPort.mc.field_71439_g.func_70617_f_() && !YPort.mc.field_71439_g.func_70055_a(Material.field_151586_h) && !YPort.mc.field_71439_g.func_70055_a(Material.field_151587_i) && !YPort.mc.field_71439_g.func_70090_H() && ((!(this.getBlock(-1.1) instanceof BlockAir) && !(this.getBlock(-1.1) instanceof BlockAir)) || (!(this.getBlock(-0.1) instanceof BlockAir) && YPort.mc.field_71439_g.field_70159_w != 0.0 && YPort.mc.field_71439_g.field_70179_y != 0.0 && !YPort.mc.field_71439_g.field_70122_E && YPort.mc.field_71439_g.field_70143_R < 3.0f && YPort.mc.field_71439_g.field_70143_R > 0.05)) && this.level == 3) {
            YPort.mc.field_71439_g.field_70181_x = -0.3994;
        }
        final double xDist = YPort.mc.field_71439_g.field_70165_t - YPort.mc.field_71439_g.field_70169_q;
        final double zDist = YPort.mc.field_71439_g.field_70161_v - YPort.mc.field_71439_g.field_70166_s;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        if (!MovementUtils.isMoving()) {
            this.safeJump = true;
        }
        else if (YPort.mc.field_71439_g.field_70122_E) {
            this.safeJump = false;
        }
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onMove(final MoveEvent event) {
        ++this.timerDelay;
        this.timerDelay %= 5;
        if (this.timerDelay != 0) {
            YPort.mc.field_71428_T.field_74278_d = 1.0f;
        }
        else {
            if (MovementUtils.hasMotion()) {
                YPort.mc.field_71428_T.field_74278_d = 32767.0f;
            }
            if (MovementUtils.hasMotion()) {
                YPort.mc.field_71428_T.field_74278_d = 1.3f;
                final EntityPlayerSP field_71439_g = YPort.mc.field_71439_g;
                field_71439_g.field_70159_w *= 1.0199999809265137;
                final EntityPlayerSP field_71439_g2 = YPort.mc.field_71439_g;
                field_71439_g2.field_70179_y *= 1.0199999809265137;
            }
        }
        if (YPort.mc.field_71439_g.field_70122_E && MovementUtils.hasMotion()) {
            this.level = 2;
        }
        if (this.round(YPort.mc.field_71439_g.field_70163_u - (int)YPort.mc.field_71439_g.field_70163_u) == this.round(0.138)) {
            final EntityPlayerSP field_71439_g3 = YPort.mc.field_71439_g;
            field_71439_g3.field_70181_x -= 0.08;
            event.y -= 0.09316090325960147;
            final EntityPlayerSP field_71439_g4 = YPort.mc.field_71439_g;
            field_71439_g4.field_70163_u -= 0.09316090325960147;
        }
        if (this.level == 1 && (YPort.mc.field_71439_g.field_70701_bs != 0.0f || YPort.mc.field_71439_g.field_70702_br != 0.0f)) {
            this.level = 2;
            this.moveSpeed = 1.38 * this.getBaseMoveSpeed() - 0.01;
        }
        else if (this.level == 2) {
            this.level = 3;
            YPort.mc.field_71439_g.field_70181_x = 0.399399995803833;
            event.y = 0.399399995803833;
            this.moveSpeed *= 2.149;
        }
        else if (this.level == 3) {
            this.level = 4;
            final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
            this.moveSpeed = this.lastDist - difference;
        }
        else {
            if (YPort.mc.field_71441_e.func_72945_a((Entity)YPort.mc.field_71439_g, YPort.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, YPort.mc.field_71439_g.field_70181_x, 0.0)).size() > 0 || YPort.mc.field_71439_g.field_70124_G) {
                this.level = 1;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        float forward = YPort.mc.field_71439_g.field_71158_b.field_78900_b;
        float strafe = YPort.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = YPort.mc.field_71439_g.field_70177_z;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0.0;
            event.z = 0.0;
        }
        else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        event.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
        event.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
        YPort.mc.field_71439_g.field_70138_W = 0.6f;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0.0;
            event.z = 0.0;
        }
    }
    
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (YPort.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            final int amplifier = YPort.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    private Block getBlock(final AxisAlignedBB axisAlignedBB) {
        for (int x = MathHelper.func_76128_c(axisAlignedBB.field_72340_a); x < MathHelper.func_76128_c(axisAlignedBB.field_72336_d) + 1; ++x) {
            for (int z = MathHelper.func_76128_c(axisAlignedBB.field_72339_c); z < MathHelper.func_76128_c(axisAlignedBB.field_72334_f) + 1; ++z) {
                final Block block = YPort.mc.field_71441_e.func_180495_p(new BlockPos(x, (int)axisAlignedBB.field_72338_b, z)).func_177230_c();
                if (block != null) {
                    return block;
                }
            }
        }
        return null;
    }
    
    private Block getBlock(final double offset) {
        return this.getBlock(YPort.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, offset, 0.0));
    }
    
    private double round(final double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
