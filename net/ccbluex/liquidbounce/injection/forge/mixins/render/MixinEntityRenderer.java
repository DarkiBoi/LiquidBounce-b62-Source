// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.Vec3;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.ccbluex.liquidbounce.features.module.modules.player.Reach;
import org.lwjgl.opengl.GL11;
import net.ccbluex.liquidbounce.features.module.modules.render.Tracers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraft.world.World;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.features.module.modules.render.CameraClip;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.NoHurtCam;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.LiquidBounce;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ EntityRenderer.class })
public abstract class MixinEntityRenderer
{
    @Shadow
    private Entity field_78528_u;
    @Shadow
    private Minecraft field_78531_r;
    @Shadow
    private float field_78491_C;
    @Shadow
    private float field_78490_B;
    @Shadow
    private boolean field_78500_U;
    
    @Shadow
    public abstract void func_175069_a(final ResourceLocation p0);
    
    @Shadow
    protected abstract void func_78479_a(final float p0, final int p1);
    
    @Inject(method = { "renderWorldPass" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE) })
    private void renderWorldPass(final int pass, final float partialTicks, final long finishTimeNano, final CallbackInfo callbackInfo) {
        LiquidBounce.CLIENT.eventManager.callEvent(new Render3DEvent(partialTicks));
    }
    
    @Inject(method = { "hurtCameraEffect" }, at = { @At("HEAD") }, cancellable = true)
    private void injectHurtCameraEffect(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(NoHurtCam.class).getState()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "orientCamera" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D") }, cancellable = true)
    private void cameraClip(final float partialTicks, final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(CameraClip.class).getState()) {
            callbackInfo.cancel();
            final Entity entity = this.field_78531_r.func_175606_aa();
            float f = entity.func_70047_e();
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).func_70608_bn()) {
                ++f;
                GlStateManager.func_179109_b(0.0f, 0.3f, 0.0f);
                if (!this.field_78531_r.field_71474_y.field_74325_U) {
                    final BlockPos blockpos = new BlockPos(entity);
                    final IBlockState iblockstate = this.field_78531_r.field_71441_e.func_180495_p(blockpos);
                    ForgeHooksClient.orientBedCamera((IBlockAccess)this.field_78531_r.field_71441_e, blockpos, iblockstate, entity);
                    GlStateManager.func_179114_b(entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                    GlStateManager.func_179114_b(entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks, -1.0f, 0.0f, 0.0f);
                }
            }
            else if (this.field_78531_r.field_71474_y.field_74320_O > 0) {
                final double d3 = this.field_78491_C + (this.field_78490_B - this.field_78491_C) * partialTicks;
                if (this.field_78531_r.field_71474_y.field_74325_U) {
                    GlStateManager.func_179109_b(0.0f, 0.0f, (float)(-d3));
                }
                else {
                    final float f2 = entity.field_70177_z;
                    float f3 = entity.field_70125_A;
                    if (this.field_78531_r.field_71474_y.field_74320_O == 2) {
                        f3 += 180.0f;
                    }
                    if (this.field_78531_r.field_71474_y.field_74320_O == 2) {
                        GlStateManager.func_179114_b(180.0f, 0.0f, 1.0f, 0.0f);
                    }
                    GlStateManager.func_179114_b(entity.field_70125_A - f3, 1.0f, 0.0f, 0.0f);
                    GlStateManager.func_179114_b(entity.field_70177_z - f2, 0.0f, 1.0f, 0.0f);
                    GlStateManager.func_179109_b(0.0f, 0.0f, (float)(-d3));
                    GlStateManager.func_179114_b(f2 - entity.field_70177_z, 0.0f, 1.0f, 0.0f);
                    GlStateManager.func_179114_b(f3 - entity.field_70125_A, 1.0f, 0.0f, 0.0f);
                }
            }
            else {
                GlStateManager.func_179109_b(0.0f, 0.0f, -0.1f);
            }
            if (!this.field_78531_r.field_71474_y.field_74325_U) {
                float yaw = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * partialTicks + 180.0f;
                final float pitch = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks;
                final float roll = 0.0f;
                if (entity instanceof EntityAnimal) {
                    final EntityAnimal entityanimal = (EntityAnimal)entity;
                    yaw = entityanimal.field_70758_at + (entityanimal.field_70759_as - entityanimal.field_70758_at) * partialTicks + 180.0f;
                }
                final Block block = ActiveRenderInfo.func_180786_a((World)this.field_78531_r.field_71441_e, entity, partialTicks);
                final EntityViewRenderEvent.CameraSetup event = new EntityViewRenderEvent.CameraSetup((EntityRenderer)this, entity, block, (double)partialTicks, yaw, pitch, roll);
                MinecraftForge.EVENT_BUS.post((net.minecraftforge.fml.common.eventhandler.Event)event);
                GlStateManager.func_179114_b(event.roll, 0.0f, 0.0f, 1.0f);
                GlStateManager.func_179114_b(event.pitch, 1.0f, 0.0f, 0.0f);
                GlStateManager.func_179114_b(event.yaw, 0.0f, 1.0f, 0.0f);
            }
            GlStateManager.func_179109_b(0.0f, -f, 0.0f);
            final double d4 = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * partialTicks;
            final double d5 = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * partialTicks + f;
            final double d6 = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * partialTicks;
            this.field_78500_U = this.field_78531_r.field_71438_f.func_72721_a(d4, d5, d6, partialTicks);
        }
    }
    
    @Inject(method = { "setupCameraTransform" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;setupViewBobbing(F)V", shift = At.Shift.BEFORE) })
    private void setupCameraViewBobbingBefore(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(Tracers.class).getState()) {
            GL11.glPushMatrix();
        }
    }
    
    @Inject(method = { "setupCameraTransform" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;setupViewBobbing(F)V", shift = At.Shift.AFTER) })
    private void setupCameraViewBobbingAfter(final CallbackInfo callbackInfo) {
        if (ModuleManager.getModule(Tracers.class).getState()) {
            GL11.glPopMatrix();
        }
    }
    
    @Overwrite
    public void func_78473_a(final float p_getMouseOver_1_) {
        final Entity entity = this.field_78531_r.func_175606_aa();
        if (entity != null && this.field_78531_r.field_71441_e != null) {
            this.field_78531_r.field_71424_I.func_76320_a("pick");
            this.field_78531_r.field_147125_j = null;
            double d0 = this.field_78531_r.field_71442_b.func_78757_d();
            this.field_78531_r.field_71476_x = entity.func_174822_a(d0, p_getMouseOver_1_);
            double d2 = d0;
            final Vec3 vec3 = entity.func_174824_e(p_getMouseOver_1_);
            boolean flag = false;
            if (this.field_78531_r.field_71442_b.func_78749_i()) {
                d0 = 6.0;
                d2 = 6.0;
            }
            else if (d0 > 3.0 && !ModuleManager.getModule(Reach.class).getState()) {
                flag = true;
            }
            if (this.field_78531_r.field_71476_x != null) {
                d2 = this.field_78531_r.field_71476_x.field_72307_f.func_72438_d(vec3);
            }
            final Vec3 vec4 = entity.func_70676_i(p_getMouseOver_1_);
            final Vec3 vec5 = vec3.func_72441_c(vec4.field_72450_a * d0, vec4.field_72448_b * d0, vec4.field_72449_c * d0);
            this.field_78528_u = null;
            Vec3 vec6 = null;
            final float f = 1.0f;
            final List<Entity> list = (List<Entity>)this.field_78531_r.field_71441_e.func_175674_a(entity, entity.func_174813_aQ().func_72321_a(vec4.field_72450_a * d0, vec4.field_72448_b * d0, vec4.field_72449_c * d0).func_72314_b((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.field_180132_d, p_apply_1_ -> p_apply_1_.func_70067_L()));
            double d3 = d2;
            for (int j = 0; j < list.size(); ++j) {
                final Entity entity2 = list.get(j);
                final float f2 = entity2.func_70111_Y();
                final AxisAlignedBB axisalignedbb = entity2.func_174813_aQ().func_72314_b((double)f2, (double)f2, (double)f2);
                final MovingObjectPosition movingobjectposition = axisalignedbb.func_72327_a(vec3, vec5);
                if (axisalignedbb.func_72318_a(vec3)) {
                    if (d3 >= 0.0) {
                        this.field_78528_u = entity2;
                        vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.field_72307_f);
                        d3 = 0.0;
                    }
                }
                else if (movingobjectposition != null) {
                    final double d4 = vec3.func_72438_d(movingobjectposition.field_72307_f);
                    if (d4 < d3 || d3 == 0.0) {
                        if (entity2 == entity.field_70154_o && !entity.canRiderInteract()) {
                            if (d3 == 0.0) {
                                this.field_78528_u = entity2;
                                vec6 = movingobjectposition.field_72307_f;
                            }
                        }
                        else {
                            this.field_78528_u = entity2;
                            vec6 = movingobjectposition.field_72307_f;
                            d3 = d4;
                        }
                    }
                }
            }
            if (this.field_78528_u != null && flag && vec3.func_72438_d(vec6) > 3.0) {
                this.field_78528_u = null;
                this.field_78531_r.field_71476_x = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, (EnumFacing)null, new BlockPos(vec6));
            }
            if (this.field_78528_u != null && (d3 < d2 || this.field_78531_r.field_71476_x == null)) {
                this.field_78531_r.field_71476_x = new MovingObjectPosition(this.field_78528_u, vec6);
                if (this.field_78528_u instanceof EntityLivingBase || this.field_78528_u instanceof EntityItemFrame) {
                    this.field_78531_r.field_147125_j = this.field_78528_u;
                }
            }
            this.field_78531_r.field_71424_I.func_76319_b();
        }
    }
}
