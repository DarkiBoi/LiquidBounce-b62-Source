// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.events.MotionEvent;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoWeapon", description = "Automatically selects the best weapon in your hotbar.", category = ModuleCategory.COMBAT)
public class AutoWeapon extends Module
{
    private final BoolValue silentValue;
    private C02PacketUseEntity packetUseEntity;
    private boolean spoofedSlot;
    private boolean gotIt;
    private int tick;
    
    public AutoWeapon() {
        this.silentValue = new BoolValue("SpoofItem", false);
    }
    
    @EventTarget
    public void onAttack(final AttackEvent event) {
        this.gotIt = true;
    }
    
    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.getPacket()).func_149565_c() == C02PacketUseEntity.Action.ATTACK && this.gotIt) {
            this.gotIt = false;
            int slot = -1;
            double bestDamage = 0.0;
            for (int i = 0; i < 9; ++i) {
                final ItemStack itemStack = AutoWeapon.mc.field_71439_g.field_71071_by.func_70301_a(i);
                if (itemStack != null && (itemStack.func_77973_b() instanceof ItemSword || itemStack.func_77973_b() instanceof ItemTool)) {
                    for (final AttributeModifier attributeModifier : itemStack.func_111283_C().get((Object)"generic.attackDamage")) {
                        final double damage = attributeModifier.func_111164_d() + 1.25 * ItemUtils.getEnchantment(itemStack, Enchantment.field_180314_l);
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            slot = i;
                        }
                    }
                }
            }
            if (slot != -1 && slot != AutoWeapon.mc.field_71439_g.field_71071_by.field_70461_c) {
                if (this.silentValue.asBoolean()) {
                    AutoWeapon.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(slot));
                    this.spoofedSlot = true;
                }
                else {
                    AutoWeapon.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                    AutoWeapon.mc.field_71442_b.func_78765_e();
                }
                event.setCancelled(true);
                this.packetUseEntity = (C02PacketUseEntity)event.getPacket();
                this.tick = 0;
            }
        }
    }
    
    @EventTarget(ignoreCondition = true)
    public void onUpdate(final MotionEvent event) {
        if (this.tick < 1) {
            ++this.tick;
            return;
        }
        if (this.packetUseEntity != null) {
            AutoWeapon.mc.func_147114_u().func_147298_b().func_179290_a((Packet)this.packetUseEntity);
            if (this.spoofedSlot) {
                AutoWeapon.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoWeapon.mc.field_71439_g.field_71071_by.field_70461_c));
            }
            this.packetUseEntity = null;
        }
    }
}
