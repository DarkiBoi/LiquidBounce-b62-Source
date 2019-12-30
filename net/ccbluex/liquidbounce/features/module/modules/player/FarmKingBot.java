// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FarmKingBot", description = "Allows you to play FarmKing while being AFK.", category = ModuleCategory.PLAYER)
public class FarmKingBot extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        BlockPos targetBlockPos = null;
        for (final Entity entity : FarmKingBot.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItemFrame) {
                final EntityItemFrame entityItemFrame = (EntityItemFrame)entity;
                if (entityItemFrame.func_82335_i() == null || entityItemFrame.func_82335_i().func_77973_b() == null) {
                    continue;
                }
                final ItemStack itemStack = entityItemFrame.func_82335_i();
                final Item item = itemStack.func_77973_b();
                if (!item.func_77658_a().equals("tile.cloth") || itemStack.func_77952_i() != 5) {
                    continue;
                }
                targetBlockPos = entity.func_180425_c();
                if (FarmKingBot.mc.field_71439_g.func_70032_d(entity) > 21.399999618530273) {
                    continue;
                }
                FarmKingBot.mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity(entity, C02PacketUseEntity.Action.INTERACT));
            }
        }
        if (targetBlockPos == null) {
            for (int radius = 40, x = -radius; x < radius; ++x) {
                for (int y = radius; y > -radius; --y) {
                    int z = -radius;
                    while (z < radius) {
                        final int xPos = (int)FarmKingBot.mc.field_71439_g.field_70165_t + x;
                        final int yPos = (int)FarmKingBot.mc.field_71439_g.field_70163_u + y;
                        final int zPos = (int)FarmKingBot.mc.field_71439_g.field_70161_v + z;
                        final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                        final Block block = FarmKingBot.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
                        if (block instanceof BlockTallGrass) {
                            targetBlockPos = blockPos;
                            if (FarmKingBot.mc.field_71439_g.func_174818_b(blockPos) <= 21.399999618530273) {
                                FarmKingBot.mc.field_71439_g.func_71038_i();
                                FarmKingBot.mc.field_71442_b.func_180511_b(blockPos, EnumFacing.DOWN);
                                break;
                            }
                            break;
                        }
                        else {
                            ++z;
                        }
                    }
                    if (targetBlockPos != null) {
                        break;
                    }
                }
                if (targetBlockPos != null) {
                    break;
                }
            }
        }
        if (targetBlockPos != null) {
            final double diffX = targetBlockPos.func_177958_n() + 0.5 - FarmKingBot.mc.field_71439_g.field_70165_t;
            final double diffY = targetBlockPos.func_177956_o() + 0.5 - (FarmKingBot.mc.field_71439_g.func_174813_aQ().field_72338_b + FarmKingBot.mc.field_71439_g.func_70047_e());
            final double diffZ = targetBlockPos.func_177952_p() + 0.5 - FarmKingBot.mc.field_71439_g.field_70161_v;
            final double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
            final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
            final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
            FarmKingBot.mc.field_71439_g.field_70177_z += MathHelper.func_76142_g(yaw - FarmKingBot.mc.field_71439_g.field_70177_z);
            FarmKingBot.mc.field_71439_g.field_70125_A += MathHelper.func_76142_g(pitch - FarmKingBot.mc.field_71439_g.field_70125_A);
            if (FarmKingBot.mc.field_71439_g.field_70123_F && FarmKingBot.mc.field_71439_g.field_70122_E) {
                FarmKingBot.mc.field_71439_g.func_70664_aZ();
            }
            FarmKingBot.mc.field_71474_y.field_74351_w.field_74513_e = true;
        }
        else {
            FarmKingBot.mc.field_71474_y.field_74351_w.field_74513_e = false;
        }
    }
    
    @Override
    public void onDisable() {
        if (!Keyboard.isKeyDown(FarmKingBot.mc.field_71474_y.field_74351_w.func_151463_i())) {
            FarmKingBot.mc.field_71474_y.field_74351_w.field_74513_e = false;
        }
        super.onDisable();
    }
}
