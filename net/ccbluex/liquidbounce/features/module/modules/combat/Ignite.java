// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemBucket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.block.BlockAir;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.minecraft.entity.Entity;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.minecraft.init.Items;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Ignite", description = "Automatically sets targets aroung you on fire.", category = ModuleCategory.COMBAT)
public class Ignite extends Module
{
    private final BoolValue lighterValue;
    private final BoolValue lavaBucketValue;
    private final MSTimer msTimer;
    
    public Ignite() {
        this.lighterValue = new BoolValue("Lighter", true);
        this.lavaBucketValue = new BoolValue("Lava", true);
        this.msTimer = new MSTimer();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!this.msTimer.hasTimePassed(500L)) {
            return;
        }
        final int lighterInHotbar = this.lighterValue.asBoolean() ? InventoryUtils.findItem(36, 45, Items.field_151033_d) : -1;
        final int lavaInHotbar = this.lavaBucketValue.asBoolean() ? InventoryUtils.findItem(26, 45, Items.field_151129_at) : -1;
        if (lighterInHotbar == -1 && lavaInHotbar == -1) {
            return;
        }
        final int fireInHotbar = (lighterInHotbar != -1) ? lighterInHotbar : lavaInHotbar;
        for (final Entity entity : Ignite.mc.field_71441_e.field_72996_f) {
            if (EntityUtils.isSelected(entity, true) && !entity.func_70027_ad()) {
                final BlockPos blockPos = entity.func_180425_c();
                if (Ignite.mc.field_71439_g.func_174818_b(blockPos) >= 22.3 || !BlockUtils.isReplaceable(blockPos)) {
                    continue;
                }
                if (!(BlockUtils.getBlock(blockPos) instanceof BlockAir)) {
                    continue;
                }
                RotationUtils.keepRotation = true;
                Ignite.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(fireInHotbar - 36));
                final ItemStack itemStack = Ignite.mc.field_71439_g.field_71069_bz.func_75139_a(fireInHotbar).func_75211_c();
                if (itemStack.func_77973_b() instanceof ItemBucket) {
                    final double diffX = blockPos.func_177958_n() + 0.5 - Ignite.mc.field_71439_g.field_70165_t;
                    final double diffY = blockPos.func_177956_o() + 0.5 - (Ignite.mc.field_71439_g.func_174813_aQ().field_72338_b + Ignite.mc.field_71439_g.func_70047_e());
                    final double diffZ = blockPos.func_177952_p() + 0.5 - Ignite.mc.field_71439_g.field_70161_v;
                    final double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
                    final float pitch = (float)(-(Math.atan2(diffY, sqrt) * 180.0 / 3.141592653589793));
                    Ignite.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(Ignite.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - Ignite.mc.field_71439_g.field_70177_z), Ignite.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - Ignite.mc.field_71439_g.field_70125_A), Ignite.mc.field_71439_g.field_70122_E));
                    Ignite.mc.field_71442_b.func_78769_a((EntityPlayer)Ignite.mc.field_71439_g, (World)Ignite.mc.field_71441_e, itemStack);
                }
                else {
                    for (final EnumFacing side : EnumFacing.values()) {
                        final BlockPos neighbor = blockPos.func_177972_a(side);
                        if (BlockUtils.canBeClicked(neighbor)) {
                            final double diffX2 = neighbor.func_177958_n() + 0.5 - Ignite.mc.field_71439_g.field_70165_t;
                            final double diffY2 = neighbor.func_177956_o() + 0.5 - (Ignite.mc.field_71439_g.func_174813_aQ().field_72338_b + Ignite.mc.field_71439_g.func_70047_e());
                            final double diffZ2 = neighbor.func_177952_p() + 0.5 - Ignite.mc.field_71439_g.field_70161_v;
                            final double sqrt2 = Math.sqrt(diffX2 * diffX2 + diffZ2 * diffZ2);
                            final float yaw2 = (float)(Math.atan2(diffZ2, diffX2) * 180.0 / 3.141592653589793) - 90.0f;
                            final float pitch2 = (float)(-(Math.atan2(diffY2, sqrt2) * 180.0 / 3.141592653589793));
                            Ignite.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(Ignite.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw2 - Ignite.mc.field_71439_g.field_70177_z), Ignite.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch2 - Ignite.mc.field_71439_g.field_70125_A), Ignite.mc.field_71439_g.field_70122_E));
                            if (Ignite.mc.field_71442_b.func_178890_a(Ignite.mc.field_71439_g, Ignite.mc.field_71441_e, itemStack, neighbor, side.func_176734_d(), new Vec3(side.func_176730_m()))) {
                                Ignite.mc.field_71439_g.func_71038_i();
                                break;
                            }
                        }
                    }
                }
                Ignite.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Ignite.mc.field_71439_g.field_71071_by.field_70461_c));
                RotationUtils.keepRotation = false;
                Ignite.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(Ignite.mc.field_71439_g.field_70177_z, Ignite.mc.field_71439_g.field_70125_A, Ignite.mc.field_71439_g.field_70122_E));
                this.msTimer.reset();
                break;
            }
        }
    }
}
