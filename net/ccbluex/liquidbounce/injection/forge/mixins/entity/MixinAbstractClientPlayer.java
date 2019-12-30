// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.minecraft.init.Items;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.cape.CapeAPI;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.ccbluex.liquidbounce.cape.CapeInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ AbstractClientPlayer.class })
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer
{
    private CapeInfo capeInfo;
    
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void initCape(final World worldIn, final GameProfile playerProfile, final CallbackInfo callbackInfo) {
        if (!CapeAPI.getInstance().hasCapeService()) {
            return;
        }
        this.capeInfo = CapeAPI.getInstance().loadCape(playerProfile.getId());
    }
    
    @Inject(method = { "getLocationCape" }, at = { @At("HEAD") }, cancellable = true)
    private void getCape(final CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (this.capeInfo != null && this.capeInfo.getResourceLocation() != null && this.capeInfo.isCapeAvailable()) {
            callbackInfoReturnable.setReturnValue(this.capeInfo.getResourceLocation());
        }
    }
    
    @Inject(method = { "getFovModifier" }, at = { @At("HEAD") }, cancellable = true)
    private void getFovModifier(final CallbackInfoReturnable<Float> callbackInfoReturnable) {
        final NoFOV fovModule = (NoFOV)ModuleManager.getModule(NoFOV.class);
        if (fovModule.getState()) {
            float newFOV = fovModule.fovValue.asFloat();
            if (!this.func_71039_bw()) {
                callbackInfoReturnable.setReturnValue(newFOV);
                return;
            }
            if (this.func_71011_bu().func_77973_b() != Items.field_151031_f) {
                callbackInfoReturnable.setReturnValue(newFOV);
                return;
            }
            final int i = this.func_71057_bx();
            float f1 = i / 20.0f;
            f1 = ((f1 > 1.0f) ? 1.0f : (f1 * f1));
            newFOV *= 1.0f - f1 * 0.15f;
            callbackInfoReturnable.setReturnValue(newFOV);
        }
    }
    
    @Inject(method = { "getLocationSkin()Lnet/minecraft/util/ResourceLocation;" }, at = { @At("HEAD") }, cancellable = true)
    private void getSkin(final CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        final NameProtect nameProtect = (NameProtect)ModuleManager.getModule(NameProtect.class);
        if (nameProtect.getState() && nameProtect.skinProtectValue.asBoolean()) {
            if (!nameProtect.allPlayersValue.asBoolean() && !this.func_146103_bH().getName().equals(Minecraft.func_71410_x().field_71439_g.func_146103_bH().getName())) {
                return;
            }
            callbackInfoReturnable.setReturnValue(DefaultPlayerSkin.func_177334_a(this.func_110124_au()));
        }
    }
}
