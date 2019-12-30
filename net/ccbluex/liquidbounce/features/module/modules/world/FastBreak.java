// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "FastBreak", description = "Allows you to break blocks faster.", category = ModuleCategory.WORLD)
public class FastBreak extends Module
{
    private final FloatValue breakDamage;
    
    public FastBreak() {
        this.breakDamage = new FloatValue("BreakDamage", 0.8f, 0.1f, 1.0f);
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        FastBreak.mc.field_71442_b.field_78781_i = 0;
        if (FastBreak.mc.field_71442_b.field_78770_f > this.breakDamage.asFloat()) {
            FastBreak.mc.field_71428_T.field_74278_d = 1.1f;
            FastBreak.mc.field_71442_b.field_78770_f = 1.0f;
            FastBreak.mc.field_71428_T.field_74278_d = 1.0f;
        }
        if (Fucker.currentDamage > this.breakDamage.asFloat()) {
            FastBreak.mc.field_71428_T.field_74278_d = 1.1f;
            Fucker.currentDamage = 1.0f;
            FastBreak.mc.field_71428_T.field_74278_d = 1.0f;
        }
        if (Nuker.currentDamage > this.breakDamage.asFloat()) {
            FastBreak.mc.field_71428_T.field_74278_d = 1.1f;
            Nuker.currentDamage = 1.0f;
            FastBreak.mc.field_71428_T.field_74278_d = 1.0f;
        }
    }
}
