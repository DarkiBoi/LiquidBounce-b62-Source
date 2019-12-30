// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.BlockAir;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockSlab;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.BlockSlime;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "BufferSpeed", description = "Allows you to walk faster on slabs and stairs.", category = ModuleCategory.MOVEMENT)
public class BufferSpeed extends Module
{
    private final BoolValue speedLimitValue;
    private final FloatValue maxSpeedValue;
    private final BoolValue bufferValue;
    private final BoolValue stairsValue;
    private final FloatValue stairsBoostValue;
    private final ListValue stairsModeValue;
    private final BoolValue slabsValue;
    private final FloatValue slabsBoostValue;
    private final ListValue slabsModeValue;
    private final BoolValue iceValue;
    private final FloatValue iceBoostValue;
    private final BoolValue snowValue;
    private final FloatValue snowBoostValue;
    private final BoolValue snowPortValue;
    private final BoolValue wallValue;
    private final FloatValue wallBoostValue;
    private final ListValue wallModeValue;
    private final BoolValue headBlockValue;
    private final FloatValue headBlockBoostValue;
    private final BoolValue slimeValue;
    private final BoolValue airStrafeValue;
    private final BoolValue noHurtValue;
    private double speed;
    private boolean down;
    private boolean forceDown;
    private boolean fastHop;
    private boolean hadFastHop;
    private boolean legitHop;
    
    public BufferSpeed() {
        this.speedLimitValue = new BoolValue("SpeedLimit", true);
        this.maxSpeedValue = new FloatValue("MaxSpeed", 2.0f, 1.0f, 5.0f);
        this.bufferValue = new BoolValue("Buffer", true);
        this.stairsValue = new BoolValue("Stairs", true);
        this.stairsBoostValue = new FloatValue("StairsBoost", 1.87f, 1.0f, 2.0f);
        this.stairsModeValue = new ListValue("StairsMode", new String[] { "Old", "New" }, "New");
        this.slabsValue = new BoolValue("Slabs", true);
        this.slabsBoostValue = new FloatValue("SlabsBoost", 1.87f, 1.0f, 2.0f);
        this.slabsModeValue = new ListValue("SlabsMode", new String[] { "Old", "New" }, "New");
        this.iceValue = new BoolValue("Ice", false);
        this.iceBoostValue = new FloatValue("IceBoost", 1.342f, 1.0f, 2.0f);
        this.snowValue = new BoolValue("Snow", true);
        this.snowBoostValue = new FloatValue("SnowBoost", 1.87f, 1.0f, 2.0f);
        this.snowPortValue = new BoolValue("SnowPort", true);
        this.wallValue = new BoolValue("Wall", true);
        this.wallBoostValue = new FloatValue("WallBoost", 1.87f, 1.0f, 2.0f);
        this.wallModeValue = new ListValue("WallMode", new String[] { "Old", "New" }, "New");
        this.headBlockValue = new BoolValue("HeadBlock", true);
        this.headBlockBoostValue = new FloatValue("HeadBlockBoost", 1.87f, 1.0f, 2.0f);
        this.slimeValue = new BoolValue("Slime", true);
        this.airStrafeValue = new BoolValue("AirStrafe", false);
        this.noHurtValue = new BoolValue("NoHurt", true);
        this.speed = 0.0;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (ModuleManager.getModule(Speed.class).getState() || (this.noHurtValue.asBoolean() && BufferSpeed.mc.field_71439_g.field_70737_aN > 0)) {
            this.reset();
            return;
        }
        final BlockPos blockPos = new BlockPos(BufferSpeed.mc.field_71439_g.field_70165_t, BufferSpeed.mc.field_71439_g.func_174813_aQ().field_72338_b, BufferSpeed.mc.field_71439_g.field_70161_v);
        if (this.forceDown || (this.down && BufferSpeed.mc.field_71439_g.field_70181_x == 0.0)) {
            BufferSpeed.mc.field_71439_g.field_70181_x = -1.0;
            this.down = false;
            this.forceDown = false;
        }
        if (this.fastHop) {
            BufferSpeed.mc.field_71439_g.field_71102_ce = 0.0211f;
            this.hadFastHop = true;
        }
        else if (this.hadFastHop) {
            BufferSpeed.mc.field_71439_g.field_71102_ce = 0.02f;
            this.hadFastHop = false;
        }
        if (!MovementUtils.isMoving() || BufferSpeed.mc.field_71439_g.func_70093_af() || BufferSpeed.mc.field_71439_g.func_70090_H()) {
            this.reset();
            return;
        }
        if (BufferSpeed.mc.field_71439_g.field_70122_E) {
            this.fastHop = false;
            if (this.slimeValue.asBoolean() && (BlockUtils.getBlock(blockPos.func_177977_b()) instanceof BlockSlime || BlockUtils.getBlock(blockPos) instanceof BlockSlime)) {
                BufferSpeed.mc.field_71439_g.func_70664_aZ();
                BufferSpeed.mc.field_71439_g.field_70181_x = 0.08;
                final EntityPlayerSP field_71439_g = BufferSpeed.mc.field_71439_g;
                field_71439_g.field_70159_w *= 1.132;
                final EntityPlayerSP field_71439_g2 = BufferSpeed.mc.field_71439_g;
                field_71439_g2.field_70179_y *= 1.132;
                this.down = true;
                return;
            }
            if (this.slabsValue.asBoolean() && BlockUtils.getBlock(blockPos) instanceof BlockSlab) {
                final String lowerCase = this.slabsModeValue.asString().toLowerCase();
                switch (lowerCase) {
                    case "old": {
                        this.boost(this.slabsBoostValue.asFloat());
                        return;
                    }
                    case "new": {
                        this.fastHop = true;
                        if (this.legitHop) {
                            BufferSpeed.mc.field_71439_g.func_70664_aZ();
                            BufferSpeed.mc.field_71439_g.field_70122_E = false;
                            this.legitHop = false;
                            return;
                        }
                        BufferSpeed.mc.field_71439_g.field_70122_E = false;
                        MovementUtils.strafe(0.375f);
                        BufferSpeed.mc.field_71439_g.func_70664_aZ();
                        BufferSpeed.mc.field_71439_g.field_70181_x = 0.41;
                        return;
                    }
                }
            }
            if (this.stairsValue.asBoolean() && (BlockUtils.getBlock(blockPos.func_177977_b()) instanceof BlockStairs || BlockUtils.getBlock(blockPos) instanceof BlockStairs)) {
                final String lowerCase2 = this.stairsModeValue.asString().toLowerCase();
                switch (lowerCase2) {
                    case "old": {
                        this.boost(this.stairsBoostValue.asFloat());
                        return;
                    }
                    case "new": {
                        this.fastHop = true;
                        if (this.legitHop) {
                            BufferSpeed.mc.field_71439_g.func_70664_aZ();
                            BufferSpeed.mc.field_71439_g.field_70122_E = false;
                            this.legitHop = false;
                            return;
                        }
                        BufferSpeed.mc.field_71439_g.field_70122_E = false;
                        MovementUtils.strafe(0.375f);
                        BufferSpeed.mc.field_71439_g.func_70664_aZ();
                        BufferSpeed.mc.field_71439_g.field_70181_x = 0.41;
                        return;
                    }
                }
            }
            this.legitHop = true;
            if (this.headBlockValue.asBoolean() && BlockUtils.getBlock(blockPos.func_177981_b(2)) != Blocks.field_150350_a) {
                this.boost(this.headBlockBoostValue.asFloat());
                return;
            }
            if (this.iceValue.asBoolean() && (BlockUtils.getBlock(blockPos.func_177977_b()) == Blocks.field_150432_aD || BlockUtils.getBlock(blockPos.func_177977_b()) == Blocks.field_150403_cj)) {
                this.boost(this.iceBoostValue.asFloat());
                return;
            }
            if (this.snowValue.asBoolean() && BlockUtils.getBlock(blockPos) == Blocks.field_150431_aC && (this.snowPortValue.asBoolean() || BufferSpeed.mc.field_71439_g.field_70163_u - (int)BufferSpeed.mc.field_71439_g.field_70163_u >= 0.125)) {
                if (BufferSpeed.mc.field_71439_g.field_70163_u - (int)BufferSpeed.mc.field_71439_g.field_70163_u >= 0.125) {
                    this.boost(this.snowBoostValue.asFloat());
                }
                else {
                    BufferSpeed.mc.field_71439_g.func_70664_aZ();
                    this.forceDown = true;
                }
                return;
            }
            if (this.wallValue.asBoolean()) {
                final String lowerCase3 = this.wallModeValue.asString().toLowerCase();
                switch (lowerCase3) {
                    case "old": {
                        if ((BufferSpeed.mc.field_71439_g.field_70123_F && EntityUtils.isNearBlock()) || !(BlockUtils.getBlock(new BlockPos(BufferSpeed.mc.field_71439_g.field_70165_t, BufferSpeed.mc.field_71439_g.field_70163_u + 2.0, BufferSpeed.mc.field_71439_g.field_70161_v)) instanceof BlockAir)) {
                            this.boost(this.wallBoostValue.asFloat());
                            return;
                        }
                        break;
                    }
                    case "new": {
                        if (EntityUtils.isNearBlock() && !BufferSpeed.mc.field_71439_g.field_71158_b.field_78901_c) {
                            BufferSpeed.mc.field_71439_g.func_70664_aZ();
                            BufferSpeed.mc.field_71439_g.field_70181_x = 0.08;
                            final EntityPlayerSP field_71439_g3 = BufferSpeed.mc.field_71439_g;
                            field_71439_g3.field_70159_w *= 0.99;
                            final EntityPlayerSP field_71439_g4 = BufferSpeed.mc.field_71439_g;
                            field_71439_g4.field_70179_y *= 0.99;
                            this.down = true;
                            return;
                        }
                        break;
                    }
                }
            }
            final float currentSpeed = MovementUtils.getSpeed();
            if (this.speed < currentSpeed) {
                this.speed = currentSpeed;
            }
            if (this.bufferValue.asBoolean() && this.speed > 0.20000000298023224) {
                this.speed /= 1.0199999809265137;
                MovementUtils.strafe((float)this.speed);
            }
        }
        else {
            this.speed = 0.0;
            if (this.airStrafeValue.asBoolean()) {
                MovementUtils.strafe();
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.reset();
    }
    
    @Override
    public void onDisable() {
        this.reset();
    }
    
    private void reset() {
        if (BufferSpeed.mc.field_71439_g == null) {
            return;
        }
        this.legitHop = true;
        this.speed = 0.0;
        if (this.hadFastHop) {
            BufferSpeed.mc.field_71439_g.field_71102_ce = 0.02f;
            this.hadFastHop = false;
        }
    }
    
    private void boost(final float boost) {
        final EntityPlayerSP field_71439_g = BufferSpeed.mc.field_71439_g;
        field_71439_g.field_70159_w *= boost;
        final EntityPlayerSP field_71439_g2 = BufferSpeed.mc.field_71439_g;
        field_71439_g2.field_70179_y *= boost;
        this.speed = MovementUtils.getSpeed();
        if (this.speedLimitValue.asBoolean() && this.speed > this.maxSpeedValue.asFloat()) {
            this.speed = this.maxSpeedValue.asFloat();
        }
    }
}
