// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.GuiSlot;

@Mixin({ ServerSelectionList.class })
public abstract class MixinServerSelectionList extends GuiSlot
{
    public MixinServerSelectionList(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(mcIn, width, height, topIn, bottomIn, slotHeightIn);
    }
    
    @Overwrite
    protected int func_148137_d() {
        return this.field_148155_a - 5;
    }
}
