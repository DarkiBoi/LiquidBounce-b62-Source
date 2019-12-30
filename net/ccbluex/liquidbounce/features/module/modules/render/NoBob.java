// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "NoBob", description = "Disables the view bobbing effect.", category = ModuleCategory.RENDER)
public class NoBob extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        NoBob.mc.field_71439_g.field_70140_Q = 0.0f;
    }
}
