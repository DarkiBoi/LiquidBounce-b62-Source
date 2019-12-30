// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.events.MotionEvent;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Strafe", description = "Allows you to freely move in mid air.", category = ModuleCategory.MOVEMENT)
public class Strafe extends Module
{
    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (event.getEventState() == EventState.POST) {
            return;
        }
        MovementUtils.strafe();
    }
}
