// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.world;

import net.minecraft.client.settings.GameSettings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.init.Blocks;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoBreak", description = "Automatically breaks the block you are looking at.", category = ModuleCategory.WORLD)
public class AutoBreak extends Module
{
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (AutoBreak.mc.field_71476_x == null || AutoBreak.mc.field_71476_x.func_178782_a() == null) {
            return;
        }
        AutoBreak.mc.field_71474_y.field_74312_F.field_74513_e = (AutoBreak.mc.field_71441_e.func_180495_p(AutoBreak.mc.field_71476_x.func_178782_a()).func_177230_c() != Blocks.field_150350_a);
    }
    
    @Override
    public void onDisable() {
        if (!GameSettings.func_100015_a(AutoBreak.mc.field_71474_y.field_74312_F)) {
            AutoBreak.mc.field_71474_y.field_74312_F.field_74513_e = false;
        }
    }
}
