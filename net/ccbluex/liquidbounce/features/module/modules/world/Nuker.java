// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import java.util.Iterator;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Optional;
import net.minecraft.util.MovingObjectPosition;
import java.util.function.Function;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.minecraft.block.Block;
import java.util.Map;
import net.minecraft.util.Vec3;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.block.BlockAir;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import java.util.ArrayList;
import net.minecraft.util.BlockPos;
import java.util.List;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Nuker", description = "Breaks all blocks around you.", category = ModuleCategory.WORLD)
public class Nuker extends Module
{
    private final FloatValue radiusValue;
    private final BoolValue throughWallsValue;
    private final List<BlockPos> attackedBlocks;
    private BlockPos currentBlock;
    static float currentDamage;
    private int blockHitDelay;
    
    public Nuker() {
        this.radiusValue = new FloatValue("Radius", 5.2f, 1.0f, 6.0f);
        this.throughWallsValue = new BoolValue("ThroughWalls", false);
        this.attackedBlocks = new ArrayList<BlockPos>();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return;
        }
        this.attackedBlocks.clear();
        if (Nuker.mc.field_71442_b.func_78762_g()) {
            BlockPos blockPos2;
            Vec3 eyesPos;
            MovingObjectPosition movingObjectPosition;
            final Optional<Map.Entry<BlockPos, Block>> optionalEntry = BlockUtils.searchBlocks(this.radiusValue.asInteger() + 1).entrySet().stream().filter(entry -> !(entry.getValue() instanceof BlockAir) && BlockUtils.getCenterDistance(entry.getKey()) <= this.radiusValue.asFloat()).filter(entry -> {
                if (this.throughWallsValue.asBoolean()) {
                    return true;
                }
                else {
                    blockPos2 = entry.getKey();
                    eyesPos = RotationUtils.getEyesPos();
                    movingObjectPosition = Nuker.mc.field_71441_e.func_147447_a(eyesPos, new Vec3(blockPos2.func_177958_n() + 0.5, blockPos2.func_177956_o() + 0.5, blockPos2.func_177952_p() + 0.5), false, true, false);
                    return movingObjectPosition != null && movingObjectPosition.func_178782_a().equals((Object)blockPos2);
                }
            }).findAny();
            if (!optionalEntry.isPresent()) {
                return;
            }
            final Map.Entry<BlockPos, Block> entry2 = optionalEntry.get();
            final BlockPos blockPos3 = entry2.getKey();
            final Block block = entry2.getValue();
            if (!blockPos3.equals((Object)this.currentBlock)) {
                Nuker.currentDamage = 0.0f;
            }
            this.currentBlock = blockPos3;
            this.attackedBlocks.add(blockPos3);
            RotationUtils.faceBlockPacket(blockPos3);
            final AutoTool autoTool = (AutoTool)ModuleManager.getModule(AutoTool.class);
            if (autoTool != null && autoTool.getState()) {
                autoTool.switchSlot(blockPos3);
            }
            if (Nuker.currentDamage == 0.0f) {
                Nuker.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos3, EnumFacing.DOWN));
                if (Nuker.mc.field_71439_g.field_71075_bZ.field_75098_d || block.func_180647_a((EntityPlayer)Nuker.mc.field_71439_g, (World)Nuker.mc.field_71441_e, blockPos3) >= 1.0f) {
                    Nuker.currentDamage = 0.0f;
                    Nuker.mc.field_71439_g.func_71038_i();
                    Nuker.mc.field_71442_b.func_178888_a(blockPos3, EnumFacing.DOWN);
                    return;
                }
            }
            Nuker.mc.field_71439_g.func_71038_i();
            Nuker.currentDamage += block.func_180647_a((EntityPlayer)Nuker.mc.field_71439_g, (World)Nuker.mc.field_71441_e, blockPos3);
            Nuker.mc.field_71441_e.func_175715_c(Nuker.mc.field_71439_g.func_145782_y(), blockPos3, (int)(Nuker.currentDamage * 10.0f) - 1);
            if (Nuker.currentDamage >= 1.0f) {
                Nuker.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos3, EnumFacing.DOWN));
                Nuker.mc.field_71442_b.func_178888_a(blockPos3, EnumFacing.DOWN);
                this.blockHitDelay = 4;
                Nuker.currentDamage = 0.0f;
            }
        }
        else {
            Vec3 eyesPos2;
            MovingObjectPosition movingObjectPosition2;
            BlockUtils.searchBlocks(this.radiusValue.asInteger() + 1).entrySet().stream().filter(entry -> !(entry.getValue() instanceof BlockAir) && BlockUtils.getCenterDistance(entry.getKey()) <= this.radiusValue.asFloat()).map((Function<? super Object, ?>)Map.Entry::getKey).filter(blockPos -> {
                if (this.throughWallsValue.asBoolean()) {
                    return true;
                }
                else {
                    eyesPos2 = RotationUtils.getEyesPos();
                    movingObjectPosition2 = Nuker.mc.field_71441_e.func_147447_a(eyesPos2, new Vec3(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 0.5, blockPos.func_177952_p() + 0.5), false, true, false);
                    return movingObjectPosition2 != null && movingObjectPosition2.func_178782_a().equals((Object)blockPos);
                }
            }).forEach(blockPos -> {
                Nuker.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                Nuker.mc.field_71439_g.func_71038_i();
                Nuker.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                this.attackedBlocks.add(blockPos);
            });
        }
    }
    
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        for (final BlockPos blockPos : this.attackedBlocks) {
            RenderUtils.drawBlockBox(blockPos, Color.RED, true);
        }
    }
}
