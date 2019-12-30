// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Ghost;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AutoRespawn", description = "Automatically respawns you after dying.", category = ModuleCategory.PLAYER)
public class AutoRespawn extends Module
{
    private final BoolValue instantValue;
    
    public AutoRespawn() {
        this.instantValue = new BoolValue("Instant", true);
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (ModuleManager.getModule(Ghost.class).getState()) {
            return;
        }
        if (this.instantValue.asBoolean()) {
            if (AutoRespawn.mc.field_71439_g.func_110143_aJ() != 0.0f) {
                if (!AutoRespawn.mc.field_71439_g.field_70128_L) {
                    return;
                }
            }
        }
        else if (!(AutoRespawn.mc.field_71462_r instanceof GuiGameOver) || ((GuiGameOver)AutoRespawn.mc.field_71462_r).field_146347_a < 20) {
            return;
        }
        AutoRespawn.mc.field_71439_g.func_71004_bE();
        AutoRespawn.mc.func_147108_a((GuiScreen)null);
    }
}
