// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "WaterFly", description = "Allows you to move through water faster.", category = ModuleCategory.MOVEMENT)
public class WaterFly extends Module
{
    private final FloatValue motionValue;
    
    public WaterFly() {
        this.motionValue = new FloatValue("Motion", 0.5f, 0.1f, 1.0f);
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (WaterFly.mc.field_71439_g.func_70090_H()) {
            event.setY(this.motionValue.asFloat());
            WaterFly.mc.field_71439_g.field_70181_x = this.motionValue.asFloat();
        }
    }
}
