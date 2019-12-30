// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoWalk", description = "Automatically makes you walk.", category = ModuleCategory.MOVEMENT)
public class AutoWalk extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        AutoWalk.mc.field_71474_y.field_74351_w.field_74513_e = true;
    }
    
    @Override
    public void onDisable() {
        if (!GameSettings.func_100015_a(AutoWalk.mc.field_71474_y.field_74351_w)) {
            AutoWalk.mc.field_71474_y.field_74351_w.field_74513_e = false;
        }
    }
}
