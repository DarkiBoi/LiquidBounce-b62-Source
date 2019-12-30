// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.block.BlockSoulSand;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ BlockSoulSand.class })
public class MixinBlockSoulSand
{
    @Inject(method = { "onEntityCollidedWithBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void onEntityCollidedWithBlock(final CallbackInfo callbackInfo) {
        final NoSlow noSlow = (NoSlow)ModuleManager.getModule(NoSlow.class);
        if (noSlow.getState() && noSlow.soulsandValue.asBoolean()) {
            callbackInfo.cancel();
        }
    }
}
