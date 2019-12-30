// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirJump;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.MathHelper;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.JumpEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityLivingBase.class })
public abstract class MixinEntityLivingBase extends MixinEntity
{
    @Shadow
    private int field_70773_bE;
    @Shadow
    protected boolean field_70703_bu;
    
    @Shadow
    protected abstract float func_175134_bD();
    
    @Shadow
    public abstract PotionEffect func_70660_b(final Potion p0);
    
    @Shadow
    public abstract boolean func_70644_a(final Potion p0);
    
    @Shadow
    public void func_70636_d() {
    }
    
    @Shadow
    protected abstract void func_180433_a(final double p0, final boolean p1, final Block p2, final BlockPos p3);
    
    @Shadow
    public abstract float func_110143_aJ();
    
    @Shadow
    public abstract ItemStack func_70694_bm();
    
    @Overwrite
    protected void func_70664_aZ() {
        final JumpEvent jumpEvent = new JumpEvent(this.func_175134_bD());
        LiquidBounce.CLIENT.eventManager.callEvent(jumpEvent);
        if (jumpEvent.isCancelled()) {
            return;
        }
        this.field_70181_x = jumpEvent.getMotion();
        if (this.func_70644_a(Potion.field_76430_j)) {
            this.field_70181_x += (this.func_70660_b(Potion.field_76430_j).func_76458_c() + 1) * 0.1f;
        }
        if (this.func_70051_ag()) {
            final float f = this.field_70177_z * 0.017453292f;
            this.field_70159_w -= MathHelper.func_76126_a(f) * 0.2f;
            this.field_70179_y += MathHelper.func_76134_b(f) * 0.2f;
        }
        this.field_70160_al = true;
    }
    
    @Inject(method = { "onLivingUpdate" }, at = { @At("HEAD") })
    private void headLiving(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(NoJumpDelay.class).getState()) {
            this.field_70773_bE = 0;
        }
    }
    
    @Inject(method = { "onLivingUpdate" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;isJumping:Z", ordinal = 1) })
    private void airJumpInjection(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(AirJump.class).getState() && this.field_70703_bu && this.field_70773_bE == 0) {
            this.func_70664_aZ();
            this.field_70773_bE = 10;
        }
    }
    
    @Inject(method = { "getLook" }, at = { @At("HEAD") }, cancellable = true)
    private void getLook(final CallbackInfoReturnable<Vec3> callbackInfoReturnable) {
        if (((EntityLivingBase)this) instanceof EntityPlayerSP) {
            callbackInfoReturnable.setReturnValue(this.func_174806_f(this.field_70125_A, this.field_70177_z));
        }
    }
    
    @Inject(method = { "isPotionActive(Lnet/minecraft/potion/Potion;)Z" }, at = { @At("HEAD") }, cancellable = true)
    private void isPotionActive(final Potion p_isPotionActive_1_, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final AntiBlind antiBlind = (AntiBlind)ModuleManager.getModule(AntiBlind.class);
        if ((p_isPotionActive_1_ == Potion.field_76431_k || p_isPotionActive_1_ == Potion.field_76440_q) && antiBlind.getState() && antiBlind.getConfusionEffect().asBoolean()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}
