// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.minecraft.util.FoodStats;
import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerCapabilities;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer extends MixinEntityLivingBase
{
    @Shadow
    protected int field_71101_bC;
    @Shadow
    public PlayerCapabilities field_71075_bZ;
    
    @Shadow
    @Override
    public abstract ItemStack func_70694_bm();
    
    @Shadow
    public abstract GameProfile func_146103_bH();
    
    @Shadow
    protected abstract boolean func_70041_e_();
    
    @Shadow
    protected abstract String func_145776_H();
    
    @Shadow
    public abstract FoodStats func_71024_bL();
    
    @Shadow
    public abstract int func_71057_bx();
    
    @Shadow
    public abstract ItemStack func_71011_bu();
    
    @Shadow
    public abstract boolean func_71039_bw();
}
