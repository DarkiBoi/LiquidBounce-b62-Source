// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "IceSpeed", description = "Allows you to walk faster on ice.", category = ModuleCategory.MOVEMENT)
public class IceSpeed extends Module
{
    private final ListValue modeValue;
    
    public IceSpeed() {
        this.modeValue = new ListValue("Mode", new String[] { "NCP", "AAC", "Spartan" }, "NCP");
    }
    
    @Override
    public void onEnable() {
        if (this.modeValue.asString().equalsIgnoreCase("NCP")) {
            Blocks.field_150432_aD.field_149765_K = 0.39f;
            Blocks.field_150403_cj.field_149765_K = 0.39f;
        }
        super.onEnable();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        final String mode = this.modeValue.asString();
        if (mode.equalsIgnoreCase("NCP")) {
            Blocks.field_150432_aD.field_149765_K = 0.39f;
            Blocks.field_150403_cj.field_149765_K = 0.39f;
        }
        else {
            Blocks.field_150432_aD.field_149765_K = 0.98f;
            Blocks.field_150403_cj.field_149765_K = 0.98f;
        }
        if (IceSpeed.mc.field_71439_g.field_70122_E && !IceSpeed.mc.field_71439_g.func_70617_f_() && !IceSpeed.mc.field_71439_g.func_70093_af() && IceSpeed.mc.field_71439_g.func_70051_ag() && IceSpeed.mc.field_71439_g.field_71158_b.field_78900_b > 0.0) {
            if (mode.equalsIgnoreCase("AAC")) {
                final Material material = BlockUtils.getMaterial(IceSpeed.mc.field_71439_g.func_180425_c().func_177977_b());
                if (material == Material.field_151588_w || material == Material.field_151598_x) {
                    final EntityPlayerSP field_71439_g = IceSpeed.mc.field_71439_g;
                    field_71439_g.field_70159_w *= 1.342;
                    final EntityPlayerSP field_71439_g2 = IceSpeed.mc.field_71439_g;
                    field_71439_g2.field_70179_y *= 1.342;
                    Blocks.field_150432_aD.field_149765_K = 0.6f;
                    Blocks.field_150403_cj.field_149765_K = 0.6f;
                }
            }
            if (mode.equalsIgnoreCase("Spartan")) {
                final Material material = BlockUtils.getMaterial(IceSpeed.mc.field_71439_g.func_180425_c().func_177977_b());
                if (material == Material.field_151588_w || material == Material.field_151598_x) {
                    final Block upBlock = BlockUtils.getBlock(new BlockPos(IceSpeed.mc.field_71439_g.field_70165_t, IceSpeed.mc.field_71439_g.field_70163_u + 2.0, IceSpeed.mc.field_71439_g.field_70161_v));
                    if (!(upBlock instanceof BlockAir)) {
                        final EntityPlayerSP field_71439_g3 = IceSpeed.mc.field_71439_g;
                        field_71439_g3.field_70159_w *= 1.342;
                        final EntityPlayerSP field_71439_g4 = IceSpeed.mc.field_71439_g;
                        field_71439_g4.field_70179_y *= 1.342;
                    }
                    else {
                        final EntityPlayerSP field_71439_g5 = IceSpeed.mc.field_71439_g;
                        field_71439_g5.field_70159_w *= 1.18;
                        final EntityPlayerSP field_71439_g6 = IceSpeed.mc.field_71439_g;
                        field_71439_g6.field_70179_y *= 1.18;
                    }
                    Blocks.field_150432_aD.field_149765_K = 0.6f;
                    Blocks.field_150403_cj.field_149765_K = 0.6f;
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        Blocks.field_150432_aD.field_149765_K = 0.98f;
        Blocks.field_150403_cj.field_149765_K = 0.98f;
        super.onDisable();
    }
}
