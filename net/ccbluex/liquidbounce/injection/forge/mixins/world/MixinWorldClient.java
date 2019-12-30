// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.world;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ WorldClient.class })
public class MixinWorldClient
{
    @ModifyVariable(method = { "doVoidFogParticles" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;randomDisplayTick(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V", shift = At.Shift.AFTER), ordinal = 0)
    private boolean handleBarriers(final boolean flag) {
        final TrueSight trueSight = (TrueSight)ModuleManager.getModule(TrueSight.class);
        return flag || (trueSight.getState() && trueSight.barriesValue.asBoolean());
    }
}
