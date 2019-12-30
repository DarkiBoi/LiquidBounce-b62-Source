// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySkull;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ TileEntityItemStackRenderer.class })
public class MixinTileEntityItemStackRenderer
{
    @Shadow
    private TileEntityBanner field_179024_e;
    @Shadow
    private TileEntityEnderChest field_147716_d;
    @Shadow
    private TileEntityChest field_147718_c;
    @Shadow
    private TileEntityChest field_147717_b;
    
    @Overwrite
    public void func_179022_a(final ItemStack itemStackIn) {
        if (itemStackIn.func_77973_b() == Items.field_179564_cE) {
            this.field_179024_e.func_175112_a(itemStackIn);
            TileEntityRendererDispatcher.field_147556_a.func_147549_a((TileEntity)this.field_179024_e, 0.0, 0.0, 0.0, 0.0f);
        }
        else if (itemStackIn.func_77973_b() == Items.field_151144_bL) {
            GameProfile gameprofile = null;
            if (itemStackIn.func_77942_o()) {
                final NBTTagCompound nbttagcompound = itemStackIn.func_77978_p();
                try {
                    if (nbttagcompound.func_150297_b("SkullOwner", 10)) {
                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.func_74775_l("SkullOwner"));
                    }
                    else if (nbttagcompound.func_150297_b("SkullOwner", 8) && nbttagcompound.func_74779_i("SkullOwner").length() > 0) {
                        final GameProfile lvt_2_2_ = new GameProfile((UUID)null, nbttagcompound.func_74779_i("SkullOwner"));
                        gameprofile = TileEntitySkull.func_174884_b(lvt_2_2_);
                        nbttagcompound.func_82580_o("SkullOwner");
                        nbttagcompound.func_74782_a("SkullOwner", (NBTBase)NBTUtil.func_180708_a(new NBTTagCompound(), gameprofile));
                    }
                }
                catch (Exception ex) {}
            }
            if (TileEntitySkullRenderer.field_147536_b != null) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b(-0.5f, 0.0f, -0.5f);
                GlStateManager.func_179152_a(2.0f, 2.0f, 2.0f);
                GlStateManager.func_179129_p();
                TileEntitySkullRenderer.field_147536_b.func_180543_a(0.0f, 0.0f, 0.0f, EnumFacing.UP, 0.0f, itemStackIn.func_77960_j(), gameprofile, -1);
                GlStateManager.func_179089_o();
                GlStateManager.func_179121_F();
            }
        }
        else {
            final Block block = Block.func_149634_a(itemStackIn.func_77973_b());
            if (block == Blocks.field_150477_bB) {
                TileEntityRendererDispatcher.field_147556_a.func_147549_a((TileEntity)this.field_147716_d, 0.0, 0.0, 0.0, 0.0f);
            }
            else if (block == Blocks.field_150447_bR) {
                TileEntityRendererDispatcher.field_147556_a.func_147549_a((TileEntity)this.field_147718_c, 0.0, 0.0, 0.0, 0.0f);
            }
            else if (block != Blocks.field_150486_ae) {
                ForgeHooksClient.renderTileItem(itemStackIn.func_77973_b(), itemStackIn.func_77960_j());
            }
            else {
                TileEntityRendererDispatcher.field_147556_a.func_147549_a((TileEntity)this.field_147717_b, 0.0, 0.0, 0.0, 0.0f);
            }
        }
    }
}
