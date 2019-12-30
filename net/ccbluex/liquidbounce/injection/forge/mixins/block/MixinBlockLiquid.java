// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.world.Liquids;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.block.BlockLiquid;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ BlockLiquid.class })
public class MixinBlockLiquid
{
    @Inject(method = { "canCollideCheck" }, at = { @At("HEAD") }, cancellable = true)
    private void onCollideCheck(final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ModuleManager.getModule(Liquids.class).getState()) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}
