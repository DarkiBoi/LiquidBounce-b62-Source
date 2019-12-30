// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "SuperKnockback", description = "Increases knockback dealt to other entities.", category = ModuleCategory.COMBAT)
public class SuperKnockback extends Module
{
    @EventTarget
    public void onAttack(final AttackEvent event) {
        if (SuperKnockback.mc.field_71439_g.func_70051_ag()) {
            SuperKnockback.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
        }
        SuperKnockback.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
        SuperKnockback.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
        SuperKnockback.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
        SuperKnockback.mc.field_71439_g.func_70031_b(true);
        SuperKnockback.mc.field_71439_g.field_175171_bO = true;
    }
}
