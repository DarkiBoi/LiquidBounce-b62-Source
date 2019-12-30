// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.block.material.Material;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.event.events.ClickBlockEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "CivBreak", description = "Allows you to break blocks instantly.", category = ModuleCategory.WORLD)
public class CivBreak extends Module
{
    private BlockPos blockPos;
    private EnumFacing enumFacing;
    private final BoolValue airResetValue;
    private final BoolValue rangeResetValue;
    private final BoolValue rotationsValue;
    
    public CivBreak() {
        this.airResetValue = new BoolValue("Air-Reset", true);
        this.rangeResetValue = new BoolValue("Range-Reset", true);
        this.rotationsValue = new BoolValue("Rotations", true);
    }
    
    @EventTarget
    public void onBlockClick(final ClickBlockEvent event) {
        if (BlockUtils.getBlock(event.getClickedBlock()) == Blocks.field_150357_h) {
            return;
        }
        this.blockPos = event.getClickedBlock();
        this.enumFacing = event.getEnumFacing();
        CivBreak.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, this.enumFacing));
        CivBreak.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.enumFacing));
    }
    
    @EventTarget
    public void onUpdate(final MotionEvent event) {
        if (event.getEventState() == EventState.POST && this.blockPos != null && CivBreak.mc.field_71441_e.func_180495_p(this.blockPos).func_177230_c().func_149688_o() != Material.field_151579_a && CivBreak.mc.field_71439_g.func_174818_b(this.blockPos) < 22.399999618530273) {
            CivBreak.mc.field_71439_g.func_71038_i();
            if (this.rotationsValue.asBoolean()) {
                RotationUtils.faceBlockPacket(this.blockPos);
            }
            CivBreak.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, this.enumFacing));
            CivBreak.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.enumFacing));
            CivBreak.mc.field_71442_b.func_180511_b(this.blockPos, this.enumFacing);
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (this.blockPos != null) {
            if ((this.airResetValue.asBoolean() && CivBreak.mc.field_71441_e.func_180495_p(this.blockPos).func_177230_c().func_149688_o() == Material.field_151579_a) || (this.rangeResetValue.asBoolean() && CivBreak.mc.field_71439_g.func_174818_b(this.blockPos) >= 22.399999618530273)) {
                this.blockPos = null;
                return;
            }
            RenderUtils.drawBlockBox(this.blockPos, Color.RED, true);
        }
    }
}
