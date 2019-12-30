// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import java.util.UUID;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ LayerHeldItem.class })
public class MixinLayerHeldItem
{
    @Shadow
    @Final
    private RendererLivingEntity<?> field_177206_a;
    
    @Overwrite
    public void func_177141_a(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        ItemStack itemstack = entitylivingbaseIn.func_70694_bm();
        if (itemstack != null) {
            GlStateManager.func_179094_E();
            if (this.field_177206_a.func_177087_b().field_78091_s) {
                final float f = 0.5f;
                GlStateManager.func_179109_b(0.0f, 0.625f, 0.0f);
                GlStateManager.func_179114_b(-20.0f, -1.0f, 0.0f, 0.0f);
                GlStateManager.func_179152_a(f, f, f);
            }
            final UUID uuid = entitylivingbaseIn.func_110124_au();
            final EntityPlayer entityplayer = Minecraft.func_71410_x().field_71441_e.func_152378_a(uuid);
            if (entityplayer != null && entityplayer.func_70632_aY()) {
                if (entitylivingbaseIn.func_70093_af()) {
                    ((ModelBiped)this.field_177206_a.func_177087_b()).func_178718_a(0.0325f);
                    GlStateManager.func_179109_b(-0.58f, 0.3f, -0.2f);
                    GlStateManager.func_179114_b(-24390.0f, 137290.0f, -2009900.0f, -2054900.0f);
                }
                else {
                    ((ModelBiped)this.field_177206_a.func_177087_b()).func_178718_a(0.0325f);
                    GlStateManager.func_179109_b(-0.48f, 0.2f, -0.2f);
                    GlStateManager.func_179114_b(-24390.0f, 137290.0f, -2009900.0f, -2054900.0f);
                }
            }
            else {
                ((ModelBiped)this.field_177206_a.func_177087_b()).func_178718_a(0.0625f);
            }
            GlStateManager.func_179109_b(-0.0625f, 0.4375f, 0.0625f);
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer)entitylivingbaseIn).field_71104_cf != null) {
                itemstack = new ItemStack((Item)Items.field_151112_aM, 0);
            }
            final Item item = itemstack.func_77973_b();
            final Minecraft minecraft = Minecraft.func_71410_x();
            if (item instanceof ItemBlock && Block.func_149634_a(item).func_149645_b() == 2) {
                GlStateManager.func_179109_b(0.0f, 0.1875f, -0.3125f);
                GlStateManager.func_179114_b(20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.func_179114_b(45.0f, 0.0f, 1.0f, 0.0f);
                final float f2 = 0.375f;
                GlStateManager.func_179152_a(-f2, -f2, f2);
            }
            if (entitylivingbaseIn.func_70093_af()) {
                GlStateManager.func_179109_b(0.0f, 0.203125f, 0.0f);
            }
            minecraft.func_175597_ag().func_178099_a(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.func_179121_F();
        }
    }
}
