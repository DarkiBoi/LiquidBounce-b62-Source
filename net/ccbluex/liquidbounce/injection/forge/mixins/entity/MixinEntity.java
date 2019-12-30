// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.ccbluex.liquidbounce.features.module.modules.exploit.NoPitchLimit;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.HitBox;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.UUID;
import net.minecraft.util.Vec3;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.AxisAlignedBB;
import java.util.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ Entity.class })
public abstract class MixinEntity
{
    @Shadow
    public double field_70165_t;
    @Shadow
    public double field_70163_u;
    @Shadow
    public double field_70161_v;
    @Shadow
    public float field_70125_A;
    @Shadow
    public float field_70177_z;
    @Shadow
    public Entity field_70154_o;
    @Shadow
    public double field_70159_w;
    @Shadow
    public double field_70181_x;
    @Shadow
    public double field_70179_y;
    @Shadow
    public boolean field_70122_E;
    @Shadow
    public boolean field_70160_al;
    @Shadow
    public boolean field_70145_X;
    @Shadow
    public World field_70170_p;
    @Shadow
    protected boolean field_70134_J;
    @Shadow
    public float field_70138_W;
    @Shadow
    public boolean field_70123_F;
    @Shadow
    public boolean field_70124_G;
    @Shadow
    public boolean field_70132_H;
    @Shadow
    public float field_70140_Q;
    @Shadow
    public float field_82151_R;
    @Shadow
    protected Random field_70146_Z;
    @Shadow
    public int field_70174_ab;
    @Shadow
    protected boolean field_71087_bX;
    @Shadow
    public int field_71088_bW;
    @Shadow
    public float field_70130_N;
    @Shadow
    private int field_70150_b;
    @Shadow
    private int field_70151_c;
    @Shadow
    public float field_70127_C;
    @Shadow
    public float field_70126_B;
    
    @Shadow
    public abstract boolean func_70051_ag();
    
    @Shadow
    public abstract AxisAlignedBB func_174813_aQ();
    
    @Shadow
    public void func_70091_d(final double x, final double y, final double z) {
    }
    
    @Shadow
    public abstract boolean func_70090_H();
    
    @Shadow
    public abstract boolean func_70115_ae();
    
    @Shadow
    public abstract void func_70015_d(final int p0);
    
    @Shadow
    protected abstract void func_70081_e(final int p0);
    
    @Shadow
    public abstract boolean func_70026_G();
    
    @Shadow
    public abstract void func_85029_a(final CrashReportCategory p0);
    
    @Shadow
    protected abstract void func_145775_I();
    
    @Shadow
    protected abstract void func_180429_a(final BlockPos p0, final Block p1);
    
    @Shadow
    public abstract void func_174826_a(final AxisAlignedBB p0);
    
    @Shadow
    protected abstract Vec3 func_174806_f(final float p0, final float p1);
    
    @Shadow
    public abstract UUID func_110124_au();
    
    public int getNextStepDistance() {
        return this.field_70150_b;
    }
    
    public void setNextStepDistance(final int nextStepDistance) {
        this.field_70150_b = nextStepDistance;
    }
    
    public int getFire() {
        return this.field_70151_c;
    }
    
    @Inject(method = { "getCollisionBorderSize" }, at = { @At("HEAD") }, cancellable = true)
    private void getCollisionBorderSize(final CallbackInfoReturnable<Float> callbackInfoReturnable) {
        final HitBox hitBox = (HitBox)ModuleManager.getModule(HitBox.class);
        if (hitBox.getState()) {
            callbackInfoReturnable.setReturnValue(0.1f + hitBox.sizeValue.asFloat());
        }
    }
    
    @Inject(method = { "setAngles" }, at = { @At("HEAD") }, cancellable = true)
    private void setAngles(final float yaw, final float pitch, final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(NoPitchLimit.class).getState()) {
            callbackInfo.cancel();
            final float f = this.field_70125_A;
            final float f2 = this.field_70177_z;
            this.field_70177_z += (float)(yaw * 0.15);
            this.field_70125_A -= (float)(pitch * 0.15);
            this.field_70127_C += this.field_70125_A - f;
            this.field_70126_B += this.field_70177_z - f2;
        }
    }
}
