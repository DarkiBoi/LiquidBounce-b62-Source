// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.item;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import net.ccbluex.liquidbounce.injection.implementations.IItemStack;

@Mixin({ ItemStack.class })
public class MixinItemStack implements IItemStack
{
    private long itemDelay;
    
    @Inject(method = { "<init>(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)V" }, at = { @At("RETURN") })
    private void init(final CallbackInfo callbackInfo) {
        this.itemDelay = System.currentTimeMillis();
    }
    
    @Override
    public long getItemDelay() {
        return this.itemDelay;
    }
}
