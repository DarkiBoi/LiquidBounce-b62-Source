// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.block.Block;

import java.util.Iterator;
import java.util.List;

import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.event.events.StepConfirmEvent;
import net.ccbluex.liquidbounce.event.events.StepEvent;
import net.minecraft.util.AxisAlignedBB;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.minecraft.potion.Potion;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.event.events.PushOutEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.features.module.modules.fun.Derp;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AntiHunger;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.MovementInput;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP extends MixinAbstractClientPlayer {
    @Shadow
    public int field_71157_e;
    @Shadow
    public float field_71086_bY;
    @Shadow
    public float field_71080_cy;
    @Shadow
    public MovementInput field_71158_b;
    @Shadow
    @Final
    public NetHandlerPlayClient field_71174_a;
    @Shadow
    protected int field_71156_d;
    @Shadow
    protected Minecraft field_71159_c;
    @Shadow
    private boolean field_175171_bO;
    @Shadow
    private float field_110321_bQ;
    @Shadow
    private int field_110320_a;
    @Shadow
    private boolean field_175170_bN;
    @Shadow
    private double field_175172_bI;
    @Shadow
    private int field_175168_bP;
    @Shadow
    private double field_175166_bJ;
    @Shadow
    private double field_175167_bK;
    @Shadow
    private float field_175164_bL;
    @Shadow
    private float field_175165_bM;

    @Shadow
    public abstract void func_85030_a(final String p0, final float p1, final float p2);

    @Shadow
    public abstract void func_70031_b(final boolean p0);

    @Shadow
    protected abstract boolean func_145771_j(final double p0, final double p1, final double p2);

    @Shadow
    public abstract void func_71016_p();

    @Shadow
    protected abstract void func_110318_g();

    @Shadow
    public abstract boolean func_110317_t();

    @Shadow
    public abstract boolean func_70093_af();

    @Shadow
    protected abstract boolean func_175160_A();

    @Overwrite
    public void func_175161_p() {
        try {
            LiquidBounce.CLIENT.eventManager.callEvent(new MotionEvent(EventState.PRE));
            final InventoryMove inventoryMove = (InventoryMove) ModuleManager.getModule(InventoryMove.class);
            final Sneak sneak = (Sneak) ModuleManager.getModule(Sneak.class);
            final boolean fakeSprint = (inventoryMove.getState() && inventoryMove.aacAdditionProValue.asBoolean()) || ModuleManager.getModule(AntiHunger.class).getState() || (sneak.getState() && (!MovementUtils.isMoving() || !sneak.stopMoveValue.asBoolean()) && sneak.modeValue.asString().equalsIgnoreCase("MineSecure"));
            final boolean sprinting = this.func_70051_ag() && !fakeSprint;
            if (sprinting != this.field_175171_bO) {
                if (sprinting) {
                    this.field_71174_a.func_147297_a((Packet) new C0BPacketEntityAction((Entity) this, C0BPacketEntityAction.Action.START_SPRINTING));
                } else {
                    this.field_71174_a.func_147297_a((Packet) new C0BPacketEntityAction((Entity) this, C0BPacketEntityAction.Action.STOP_SPRINTING));
                }
                this.field_175171_bO = sprinting;
            }
            final boolean sneaking = this.func_70093_af();
            if (sneaking != this.field_175170_bN && !sneak.getState() && !sneak.modeValue.asString().equalsIgnoreCase("Legit")) {
                if (sneaking) {
                    this.field_71174_a.func_147297_a((Packet) new C0BPacketEntityAction((Entity) this, C0BPacketEntityAction.Action.START_SNEAKING));
                } else {
                    this.field_71174_a.func_147297_a((Packet) new C0BPacketEntityAction((Entity) this, C0BPacketEntityAction.Action.STOP_SNEAKING));
                }
                this.field_175170_bN = sneaking;
            }
            if (this.func_175160_A()) {
                float yaw = this.field_70177_z;
                float pitch = this.field_70125_A;
                final float lastReportedYaw = RotationUtils.lastLook[0];
                final float lastReportedPitch = RotationUtils.lastLook[1];
                final Derp derp = (Derp) ModuleManager.getModule(Derp.class);
                if (derp.getState()) {
                    final float[] rot = derp.getRotation();
                    yaw = rot[0];
                    pitch = rot[1];
                }
                if (RotationUtils.lookChanged) {
                    yaw = RotationUtils.targetYaw;
                    pitch = RotationUtils.targetPitch;
                }
                final double xDiff = this.field_70165_t - this.field_175172_bI;
                final double yDiff = this.func_174813_aQ().field_72338_b - this.field_175166_bJ;
                final double zDiff = this.field_70161_v - this.field_175167_bK;
                final double yawDiff = yaw - lastReportedYaw;
                final double pitchDiff = pitch - lastReportedPitch;
                boolean moved = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff > 9.0E-4 || this.field_175168_bP >= 20;
                final boolean rotated = yawDiff != 0.0 || pitchDiff != 0.0;
                if (this.field_70154_o == null) {
                    if (moved && rotated) {
                        this.field_71174_a.func_147297_a((Packet) new C03PacketPlayer.C06PacketPlayerPosLook(this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v, yaw, pitch, this.field_70122_E));
                    } else if (moved) {
                        this.field_71174_a.func_147297_a((Packet) new C03PacketPlayer.C04PacketPlayerPosition(this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v, this.field_70122_E));
                    } else if (rotated) {
                        this.field_71174_a.func_147297_a((Packet) new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, this.field_70122_E));
                    } else {
                        this.field_71174_a.func_147297_a((Packet) new C03PacketPlayer(this.field_70122_E));
                    }
                } else {
                    this.field_71174_a.func_147297_a((Packet) new C03PacketPlayer.C06PacketPlayerPosLook(this.field_70159_w, -999.0, this.field_70179_y, yaw, pitch, this.field_70122_E));
                    moved = false;
                }
                ++this.field_175168_bP;
                if (moved) {
                    this.field_175172_bI = this.field_70165_t;
                    this.field_175166_bJ = this.func_174813_aQ().field_72338_b;
                    this.field_175167_bK = this.field_70161_v;
                    this.field_175168_bP = 0;
                }
                if (rotated) {
                    this.field_175164_bL = this.field_70177_z;
                    this.field_175165_bM = this.field_70125_A;
                }
            }
            LiquidBounce.CLIENT.eventManager.callEvent(new MotionEvent(EventState.POST));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = {"swingItem"}, at = {@At("HEAD")}, cancellable = true)
    private void swingItem(final CallbackInfo callbackInfo) {
        final NoSwing noSwing = (NoSwing) ModuleManager.getModule(NoSwing.class);
        if (noSwing.getState()) {
            callbackInfo.cancel();
            if (!noSwing.serverSideValue.asBoolean()) {
                this.field_71174_a.func_147297_a((Packet) new C0APacketAnimation());
            }
        }
    }

    @Inject(method = {"pushOutOfBlocks"}, at = {@At("HEAD")}, cancellable = true)
    private void onPushOutOfBlocks(final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final PushOutEvent event = new PushOutEvent();
        event.setCancelled(this.field_70145_X);
        LiquidBounce.CLIENT.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Overwrite
    @Override
    public void func_70636_d() {
        LiquidBounce.CLIENT.eventManager.callEvent(new UpdateEvent());
        if (this.field_71157_e > 0) {
            --this.field_71157_e;
            if (this.field_71157_e == 0) {
                this.func_70031_b(false);
            }
        }
        if (this.field_71156_d > 0) {
            --this.field_71156_d;
        }
        this.field_71080_cy = this.field_71086_bY;
        if (this.field_71087_bX) {
            if (this.field_71159_c.field_71462_r != null && !this.field_71159_c.field_71462_r.func_73868_f()) {
                this.field_71159_c.func_147108_a((GuiScreen) null);
            }
            if (this.field_71086_bY == 0.0f) {
                this.field_71159_c.func_147118_V().func_147682_a((ISound) PositionedSoundRecord.func_147674_a(new ResourceLocation("portal.trigger"), this.field_70146_Z.nextFloat() * 0.4f + 0.8f));
            }
            this.field_71086_bY += 0.0125f;
            if (this.field_71086_bY >= 1.0f) {
                this.field_71086_bY = 1.0f;
            }
            this.field_71087_bX = false;
        } else if (this.func_70644_a(Potion.field_76431_k) && this.func_70660_b(Potion.field_76431_k).func_76459_b() > 60) {
            this.field_71086_bY += 0.006666667f;
            if (this.field_71086_bY > 1.0f) {
                this.field_71086_bY = 1.0f;
            }
        } else {
            if (this.field_71086_bY > 0.0f) {
                this.field_71086_bY -= 0.05f;
            }
            if (this.field_71086_bY < 0.0f) {
                this.field_71086_bY = 0.0f;
            }
        }
        if (this.field_71088_bW > 0) {
            --this.field_71088_bW;
        }
        final boolean flag = this.field_71158_b.field_78901_c;
        final boolean flag2 = this.field_71158_b.field_78899_d;
        final float f = 0.8f;
        final boolean flag3 = this.field_71158_b.field_78900_b >= f;
        this.field_71158_b.func_78898_a();
        final NoSlow noSlow = (NoSlow) ModuleManager.getModule(NoSlow.class);
        final KillAura killAura = (KillAura) ModuleManager.getModule(KillAura.class);
        if (this.func_70694_bm() != null && (this.func_71039_bw() || (this.func_70694_bm().func_77973_b() instanceof ItemSword && killAura.blocking)) && !this.func_70115_ae()) {
            final String mode = noSlow.getMode(this.func_70694_bm().func_77973_b()).toLowerCase();
            if (noSlow.getState() && !mode.equalsIgnoreCase("off")) {
                final String lowerCase = mode.toLowerCase();
                switch (lowerCase) {
                    case "aac": {
                        final MovementInput field_71158_b = this.field_71158_b;
                        field_71158_b.field_78902_a *= 0.6f;
                        final MovementInput field_71158_b2 = this.field_71158_b;
                        field_71158_b2.field_78900_b *= 0.6f;
                        break;
                    }
                    case "spartan": {
                        final MovementInput field_71158_b3 = this.field_71158_b;
                        field_71158_b3.field_78902_a *= 0.6f;
                        final MovementInput field_71158_b4 = this.field_71158_b;
                        field_71158_b4.field_78900_b *= 0.6f;
                        break;
                    }
                    case "killswitch": {
                        final MovementInput field_71158_b5 = this.field_71158_b;
                        field_71158_b5.field_78902_a *= 0.7f;
                        final MovementInput field_71158_b6 = this.field_71158_b;
                        field_71158_b6.field_78900_b *= 0.7f;
                        break;
                    }
                    case "antiaura": {
                        final MovementInput field_71158_b7 = this.field_71158_b;
                        field_71158_b7.field_78902_a *= 0.7f;
                        final MovementInput field_71158_b8 = this.field_71158_b;
                        field_71158_b8.field_78900_b *= 0.7f;
                        break;
                    }
                    case "nnc": {
                        if (this.field_71159_c.field_71439_g.func_70694_bm() != null && this.field_71159_c.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemSword) {
                            break;
                        }
                        if (this.field_71159_c.field_71439_g.func_70694_bm() != null && this.field_71159_c.field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBow && this.field_71159_c.field_71439_g.func_71057_bx() >= 25) {
                            break;
                        }
                        final MovementInput field_71158_b9 = this.field_71158_b;
                        field_71158_b9.field_78902_a *= 0.2f;
                        final MovementInput field_71158_b10 = this.field_71158_b;
                        field_71158_b10.field_78900_b *= 0.2f;
                        this.field_71156_d = 0;
                        break;
                    }
                    case "custom": {
                        final float v = 1.0f - noSlow.customReducementValue.asFloat();
                        final MovementInput field_71158_b11 = this.field_71158_b;
                        field_71158_b11.field_78902_a *= v;
                        final MovementInput field_71158_b12 = this.field_71158_b;
                        field_71158_b12.field_78900_b *= v;
                        break;
                    }
                }
            } else {
                final MovementInput field_71158_b13 = this.field_71158_b;
                field_71158_b13.field_78902_a *= 0.2f;
                final MovementInput field_71158_b14 = this.field_71158_b;
                field_71158_b14.field_78900_b *= 0.2f;
                this.field_71156_d = 0;
            }
        }
        this.func_145771_j(this.field_70165_t - this.field_70130_N * 0.35, this.func_174813_aQ().field_72338_b + 0.5, this.field_70161_v + this.field_70130_N * 0.35);
        this.func_145771_j(this.field_70165_t - this.field_70130_N * 0.35, this.func_174813_aQ().field_72338_b + 0.5, this.field_70161_v - this.field_70130_N * 0.35);
        this.func_145771_j(this.field_70165_t + this.field_70130_N * 0.35, this.func_174813_aQ().field_72338_b + 0.5, this.field_70161_v - this.field_70130_N * 0.35);
        this.func_145771_j(this.field_70165_t + this.field_70130_N * 0.35, this.func_174813_aQ().field_72338_b + 0.5, this.field_70161_v + this.field_70130_N * 0.35);
        final Sprint sprint = (Sprint) ModuleManager.getModule(Sprint.class);
        final boolean flag4 = !sprint.foodValue.asBoolean() || this.func_71024_bL().func_75116_a() > 6.0f || this.field_71075_bZ.field_75101_c;
        if (this.field_70122_E && !flag2 && !flag3 && this.field_71158_b.field_78900_b >= f && !this.func_70051_ag() && flag4 && !this.func_71039_bw() && !this.func_70644_a(Potion.field_76440_q)) {
            if (this.field_71156_d <= 0 && !this.field_71159_c.field_71474_y.field_151444_V.func_151470_d()) {
                this.field_71156_d = 7;
            } else {
                this.func_70031_b(true);
            }
        }
        if (!this.func_70051_ag() && this.field_71158_b.field_78900_b >= f && flag4 && (noSlow.getState() || !this.func_71039_bw()) && !this.func_70644_a(Potion.field_76440_q) && this.field_71159_c.field_71474_y.field_151444_V.func_151470_d()) {
            this.func_70031_b(true);
        }
        final Scaffold scaffold = (Scaffold) ModuleManager.getModule(Scaffold.class);
        if ((scaffold.getState() && !scaffold.sprintValue.asBoolean()) || (sprint.getState() && sprint.checkServerSide.asBoolean() && !sprint.allDirectionsValue.asBoolean() && RotationUtils.lookChanged && RotationUtils.getRotationDifference(this.field_71159_c.field_71439_g.field_70177_z, this.field_71159_c.field_71439_g.field_70125_A) > 30.0)) {
            this.func_70031_b(false);
        }
        if (this.func_70051_ag() && ((sprint.getState() && sprint.allDirectionsValue.asBoolean() && this.field_71158_b.field_78900_b < f) || this.field_70123_F || !flag4)) {
            this.func_70031_b(false);
        }
        if (this.field_71075_bZ.field_75101_c) {
            if (this.field_71159_c.field_71442_b.func_178887_k()) {
                if (!this.field_71075_bZ.field_75100_b) {
                    this.field_71075_bZ.field_75100_b = true;
                    this.func_71016_p();
                }
            } else if (!flag && this.field_71158_b.field_78901_c) {
                if (this.field_71101_bC == 0) {
                    this.field_71101_bC = 7;
                } else {
                    this.field_71075_bZ.field_75100_b = !this.field_71075_bZ.field_75100_b;
                    this.func_71016_p();
                    this.field_71101_bC = 0;
                }
            }
        }
        if (this.field_71075_bZ.field_75100_b && this.func_175160_A()) {
            if (this.field_71158_b.field_78899_d) {
                this.field_70181_x -= this.field_71075_bZ.func_75093_a() * 3.0f;
            }
            if (this.field_71158_b.field_78901_c) {
                this.field_70181_x += this.field_71075_bZ.func_75093_a() * 3.0f;
            }
        }
        if (this.func_110317_t()) {
            if (this.field_110320_a < 0) {
                ++this.field_110320_a;
                if (this.field_110320_a == 0) {
                    this.field_110321_bQ = 0.0f;
                }
            }
            if (flag && !this.field_71158_b.field_78901_c) {
                this.field_110320_a = -10;
                this.func_110318_g();
            } else if (!flag && this.field_71158_b.field_78901_c) {
                this.field_110320_a = 0;
                this.field_110321_bQ = 0.0f;
            } else if (flag) {
                ++this.field_110320_a;
                if (this.field_110320_a < 10) {
                    this.field_110321_bQ = this.field_110320_a * 0.1f;
                } else {
                    this.field_110321_bQ = 0.8f + 2.0f / (this.field_110320_a - 9) * 0.1f;
                }
            }
        } else {
            this.field_110321_bQ = 0.0f;
        }
        super.func_70636_d();
        if (this.field_70122_E && this.field_71075_bZ.field_75100_b && !this.field_71159_c.field_71442_b.func_178887_k()) {
            this.field_71075_bZ.field_75100_b = false;
            this.func_71016_p();
        }
    }

    @Override
    public void func_70091_d(double x, double y, double z) {
        final MoveEvent moveEvent = new MoveEvent(x, y, z);
        LiquidBounce.CLIENT.eventManager.callEvent(moveEvent);
        if (moveEvent.isCancelled()) {
            return;
        }
        x = moveEvent.getX();
        y = moveEvent.getY();
        z = moveEvent.getZ();
        if (this.field_70145_X) {
            this.func_174826_a(this.func_174813_aQ().func_72317_d(x, y, z));
            this.field_70165_t = (this.func_174813_aQ().field_72340_a + this.func_174813_aQ().field_72336_d) / 2.0;
            this.field_70163_u = this.func_174813_aQ().field_72338_b;
            this.field_70161_v = (this.func_174813_aQ().field_72339_c + this.func_174813_aQ().field_72334_f) / 2.0;
        } else {
            this.field_70170_p.field_72984_F.func_76320_a("move");
            final double d0 = this.field_70165_t;
            final double d2 = this.field_70163_u;
            final double d3 = this.field_70161_v;
            if (this.field_70134_J) {
                this.field_70134_J = false;
                x *= 0.25;
                y *= 0.05000000074505806;
                z *= 0.25;
                this.field_70159_w = 0.0;
                this.field_70181_x = 0.0;
                this.field_70179_y = 0.0;
            }
            double d4 = x;
            final double d5 = y;
            double d6 = z;
            final boolean flag = this.field_70122_E && this.func_70093_af();
            if (flag || moveEvent.safeWalk) {
                final double d7 = 0.05;
                while (x != 0.0 && this.field_70170_p.func_72945_a((Entity) this, this.func_174813_aQ().func_72317_d(x, -1.0, 0.0)).isEmpty()) {
                    if (x < d7 && x >= -d7) {
                        x = 0.0;
                    } else if (x > 0.0) {
                        x -= d7;
                    } else {
                        x += d7;
                    }
                    d4 = x;
                }
                while (z != 0.0 && this.field_70170_p.func_72945_a((Entity) this, this.func_174813_aQ().func_72317_d(0.0, -1.0, z)).isEmpty()) {
                    if (z < d7 && z >= -d7) {
                        z = 0.0;
                    } else if (z > 0.0) {
                        z -= d7;
                    } else {
                        z += d7;
                    }
                    d6 = z;
                }
                while (x != 0.0 && z != 0.0 && this.field_70170_p.func_72945_a((Entity) this, this.func_174813_aQ().func_72317_d(x, -1.0, z)).isEmpty()) {
                    if (x < d7 && x >= -d7) {
                        x = 0.0;
                    } else if (x > 0.0) {
                        x -= d7;
                    } else {
                        x += d7;
                    }
                    d4 = x;
                    if (z < d7 && z >= -d7) {
                        z = 0.0;
                    } else if (z > 0.0) {
                        z -= d7;
                    } else {
                        z += d7;
                    }
                    d6 = z;
                }
            }
            final List<AxisAlignedBB> list1 = (List<AxisAlignedBB>) this.field_70170_p.func_72945_a((Entity) this, this.func_174813_aQ().func_72321_a(x, y, z));
            final AxisAlignedBB axisalignedbb = this.func_174813_aQ();
            for (final AxisAlignedBB axisalignedbb2 : list1) {
                y = axisalignedbb2.func_72323_b(this.func_174813_aQ(), y);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
            final boolean flag2 = this.field_70122_E || (d5 != y && d5 < 0.0);
            for (final AxisAlignedBB axisalignedbb3 : list1) {
                x = axisalignedbb3.func_72316_a(this.func_174813_aQ(), x);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(x, 0.0, 0.0));
            for (final AxisAlignedBB axisalignedbb4 : list1) {
                z = axisalignedbb4.func_72322_c(this.func_174813_aQ(), z);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, 0.0, z));
            if (this.field_70138_W > 0.0f && flag2 && (d4 != x || d6 != z)) {
                final StepEvent stepEvent = new StepEvent(this.field_70138_W);
                LiquidBounce.CLIENT.eventManager.callEvent(stepEvent);
                final double d8 = x;
                final double d9 = y;
                final double d10 = z;
                final AxisAlignedBB axisalignedbb5 = this.func_174813_aQ();
                this.func_174826_a(axisalignedbb);
                y = stepEvent.getStepHeight();
                final List<AxisAlignedBB> list2 = (List<AxisAlignedBB>) this.field_70170_p.func_72945_a((Entity) this, this.func_174813_aQ().func_72321_a(d4, y, d6));
                AxisAlignedBB axisalignedbb6 = this.func_174813_aQ();
                final AxisAlignedBB axisalignedbb7 = axisalignedbb6.func_72321_a(d4, 0.0, d6);
                double d11 = y;
                for (final AxisAlignedBB axisalignedbb8 : list2) {
                    d11 = axisalignedbb8.func_72323_b(axisalignedbb7, d11);
                }
                axisalignedbb6 = axisalignedbb6.func_72317_d(0.0, d11, 0.0);
                double d12 = d4;
                for (final AxisAlignedBB axisalignedbb9 : list2) {
                    d12 = axisalignedbb9.func_72316_a(axisalignedbb6, d12);
                }
                axisalignedbb6 = axisalignedbb6.func_72317_d(d12, 0.0, 0.0);
                double d13 = d6;
                for (final AxisAlignedBB axisalignedbb10 : list2) {
                    d13 = axisalignedbb10.func_72322_c(axisalignedbb6, d13);
                }
                axisalignedbb6 = axisalignedbb6.func_72317_d(0.0, 0.0, d13);
                AxisAlignedBB axisalignedbb11 = this.func_174813_aQ();
                double d14 = y;
                for (final AxisAlignedBB axisalignedbb12 : list2) {
                    d14 = axisalignedbb12.func_72323_b(axisalignedbb11, d14);
                }
                axisalignedbb11 = axisalignedbb11.func_72317_d(0.0, d14, 0.0);
                double d15 = d4;
                for (final AxisAlignedBB axisalignedbb13 : list2) {
                    d15 = axisalignedbb13.func_72316_a(axisalignedbb11, d15);
                }
                axisalignedbb11 = axisalignedbb11.func_72317_d(d15, 0.0, 0.0);
                double d16 = d6;
                for (final AxisAlignedBB axisalignedbb14 : list2) {
                    d16 = axisalignedbb14.func_72322_c(axisalignedbb11, d16);
                }
                axisalignedbb11 = axisalignedbb11.func_72317_d(0.0, 0.0, d16);
                final double d17 = d12 * d12 + d13 * d13;
                final double d18 = d15 * d15 + d16 * d16;
                if (d17 > d18) {
                    x = d12;
                    z = d13;
                    y = -d11;
                    this.func_174826_a(axisalignedbb6);
                } else {
                    x = d15;
                    z = d16;
                    y = -d14;
                    this.func_174826_a(axisalignedbb11);
                }
                for (final AxisAlignedBB axisalignedbb15 : list2) {
                    y = axisalignedbb15.func_72323_b(this.func_174813_aQ(), y);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
                if (d8 * d8 + d10 * d10 >= x * x + z * z) {
                    x = d8;
                    y = d9;
                    z = d10;
                    this.func_174826_a(axisalignedbb5);
                } else {
                    LiquidBounce.CLIENT.eventManager.callEvent(new StepConfirmEvent());
                }
            }
            this.field_70170_p.field_72984_F.func_76319_b();
            this.field_70170_p.field_72984_F.func_76320_a("rest");
            this.field_70165_t = (this.func_174813_aQ().field_72340_a + this.func_174813_aQ().field_72336_d) / 2.0;
            this.field_70163_u = this.func_174813_aQ().field_72338_b;
            this.field_70161_v = (this.func_174813_aQ().field_72339_c + this.func_174813_aQ().field_72334_f) / 2.0;
            this.field_70123_F = (d4 != x || d6 != z);
            this.field_70124_G = (d5 != y);
            this.field_70122_E = (this.field_70124_G && d5 < 0.0);
            this.field_70132_H = (this.field_70123_F || this.field_70124_G);
            final int i = MathHelper.func_76128_c(this.field_70165_t);
            final int j = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224);
            final int k = MathHelper.func_76128_c(this.field_70161_v);
            BlockPos blockpos = new BlockPos(i, j, k);
            Block block1 = this.field_70170_p.func_180495_p(blockpos).func_177230_c();
            if (block1.func_149688_o() == Material.field_151579_a) {
                final Block block2 = this.field_70170_p.func_180495_p(blockpos.func_177977_b()).func_177230_c();
                if (block2 instanceof BlockFence || block2 instanceof BlockWall || block2 instanceof BlockFenceGate) {
                    block1 = block2;
                    blockpos = blockpos.func_177977_b();
                }
            }
            this.func_180433_a(y, this.field_70122_E, block1, blockpos);
            if (d4 != x) {
                this.field_70159_w = 0.0;
            }
            if (d6 != z) {
                this.field_70179_y = 0.0;
            }
            if (d5 != y) {
                block1.func_176216_a(this.field_70170_p, (Entity) this);
            }
            if (this.func_70041_e_() && !flag && this.field_70154_o == null) {
                final double d19 = this.field_70165_t - d0;
                double d20 = this.field_70163_u - d2;
                final double d21 = this.field_70161_v - d3;
                if (block1 != Blocks.field_150468_ap) {
                    d20 = 0.0;
                }
                if (block1 != null && this.field_70122_E) {
                    block1.func_176199_a(this.field_70170_p, blockpos, (Entity) this);
                }
                this.field_70140_Q += (float) (MathHelper.func_76133_a(d19 * d19 + d21 * d21) * 0.6);
                this.field_82151_R += (float) (MathHelper.func_76133_a(d19 * d19 + d20 * d20 + d21 * d21) * 0.6);
                if (this.field_82151_R > this.getNextStepDistance() && block1.func_149688_o() != Material.field_151579_a) {
                    this.setNextStepDistance((int) this.field_82151_R + 1);
                    if (this.func_70090_H()) {
                        float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w * 0.20000000298023224 + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y * 0.20000000298023224) * 0.35f;
                        if (f > 1.0f) {
                            f = 1.0f;
                        }
                        this.func_85030_a(this.func_145776_H(), f, 1.0f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                    }
                    this.func_180429_a(blockpos, block1);
                }
            }
            try {
                this.func_145775_I();
            } catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.func_85055_a(throwable, "Checking entity block collision");
                final CrashReportCategory crashreportcategory = crashreport.func_85058_a("Entity being checked for collision");
                this.func_85029_a(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            final boolean flag3 = this.func_70026_G();
            if (this.field_70170_p.func_147470_e(this.func_174813_aQ().func_72331_e(0.001, 0.001, 0.001))) {
                this.func_70081_e(1);
                if (!flag3) {
                    this.func_70015_d(this.getFire() + 1);
                    if (this.getFire() == 0) {
                        this.func_70015_d(8);
                    }
                }
            } else if (this.getFire() <= 0) {
                this.func_70015_d(-this.field_70174_ab);
            }
            if (flag3 && this.getFire() > 0) {
                this.func_85030_a("random.fizz", 0.7f, 1.6f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                this.func_70015_d(-this.field_70174_ab);
            }
            this.field_70170_p.field_72984_F.func_76319_b();
        }
    }
}
