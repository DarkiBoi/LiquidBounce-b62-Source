// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Zoot", description = "Removes all bad potion effects/fire.", category = ModuleCategory.PLAYER)
public class Zoot extends Module
{
    private final BoolValue badEffectsValue;
    private final BoolValue fireValue;
    private final BoolValue noAirValue;
    
    public Zoot() {
        this.badEffectsValue = new BoolValue("BadEffects", true);
        this.fireValue = new BoolValue("Fire", true);
        this.noAirValue = new BoolValue("NoAir", false);
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.noAirValue.asBoolean() && !Zoot.mc.field_71439_g.field_70122_E) {
            return;
        }
        if (this.badEffectsValue.asBoolean()) {
            for (final PotionEffect potion : Zoot.mc.field_71439_g.func_70651_bq()) {
                if (potion != null && (Zoot.mc.field_71439_g.func_70644_a(Potion.field_76438_s) || Zoot.mc.field_71439_g.func_70644_a(Potion.field_76421_d) || Zoot.mc.field_71439_g.func_70644_a(Potion.field_76419_f) || Zoot.mc.field_71439_g.func_70644_a(Potion.field_76433_i) || Zoot.mc.field_71439_g.func_70644_a(Potion.field_76431_k) || Zoot.mc.field_71439_g.func_70644_a(Potion.field_76440_q) || Zoot.mc.field_71439_g.func_70644_a(Potion.field_76437_t) || Zoot.mc.field_71439_g.func_70644_a(Potion.field_82731_v) || Zoot.mc.field_71439_g.func_70644_a(Potion.field_76436_u))) {
                    for (int i = 0; i < potion.func_76459_b() / 20; ++i) {
                        Zoot.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer());
                    }
                }
            }
        }
        if (this.fireValue.asBoolean() && !Zoot.mc.field_71439_g.field_71075_bZ.field_75098_d && Zoot.mc.field_71439_g.func_70027_ad()) {
            for (int j = 0; j < 10; ++j) {
                Zoot.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer());
            }
        }
    }
}
