// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.injection.forge.mixins.packets;

import org.spongepowered.asm.mixin.Overwrite;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.EnumConnectionState;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin({ C00Handshake.class })
public class MixinC00Handshake
{
    @Shadow
    private int field_149600_a;
    @Shadow
    private int field_149599_c;
    @Shadow
    private EnumConnectionState field_149597_d;
    @Shadow
    private String field_149598_b;
    
    @Overwrite
    public void func_148840_b(final PacketBuffer buf) {
        buf.func_150787_b(this.field_149600_a);
        buf.func_180714_a(this.field_149598_b + ((AntiForge.enabled && AntiForge.blockFML) ? "" : "\u0000FML\u0000"));
        buf.writeShort(this.field_149599_c);
        buf.func_150787_b(this.field_149597_d.func_150759_c());
    }
}
