// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AntiAFK", description = "Prevents you from getting kicked for being AFK.", category = ModuleCategory.PLAYER)
public class AntiAFK extends Module
{
    private final MSTimer timer;
    
    public AntiAFK() {
        this.timer = new MSTimer();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        AntiAFK.mc.field_71474_y.field_74351_w.field_74513_e = true;
        if (this.timer.hasTimePassed(500L)) {
            final EntityPlayerSP field_71439_g = AntiAFK.mc.field_71439_g;
            field_71439_g.field_70177_z += 180.0f;
            this.timer.reset();
        }
    }
    
    @Override
    public void onDisable() {
        if (!GameSettings.func_100015_a(AntiAFK.mc.field_71474_y.field_74351_w)) {
            AntiAFK.mc.field_71474_y.field_74351_w.field_74513_e = false;
        }
    }
}
