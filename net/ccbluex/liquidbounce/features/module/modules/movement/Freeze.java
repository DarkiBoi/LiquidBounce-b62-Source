// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Freeze", description = "Allows you to stay stuck in mid air.", category = ModuleCategory.MOVEMENT)
public class Freeze extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        Freeze.mc.field_71439_g.field_70128_L = true;
        Freeze.mc.field_71439_g.field_70177_z = Freeze.mc.field_71439_g.field_71109_bG;
        Freeze.mc.field_71439_g.field_70125_A = Freeze.mc.field_71439_g.field_70726_aT;
    }
    
    @Override
    public void onDisable() {
        Freeze.mc.field_71439_g.field_70128_L = false;
    }
}
