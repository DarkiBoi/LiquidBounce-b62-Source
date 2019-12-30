// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.fun;

import net.ccbluex.liquidbounce.valuesystem.types.FloatValue;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Derp", description = "Makes it look like you were derping around.", category = ModuleCategory.FUN)
public class Derp extends Module
{
    private final BoolValue headlessValue;
    private final BoolValue spinnyValue;
    private final FloatValue incrementValue;
    private float r;
    
    public Derp() {
        this.headlessValue = new BoolValue("Headless", false);
        this.spinnyValue = new BoolValue("Spinny", false);
        this.incrementValue = new FloatValue("Increment", 1.0f, 0.0f, 50.0f);
    }
    
    public float[] getRotation() {
        final float[] returnRotation = { Derp.mc.field_71439_g.field_70177_z + (float)(Math.random() * 360.0 - 180.0), (float)(Math.random() * 180.0 - 90.0) };
        if (this.headlessValue.asBoolean()) {
            returnRotation[1] = 180.0f;
        }
        if (this.spinnyValue.asBoolean()) {
            returnRotation[0] = this.r + this.incrementValue.asFloat();
            this.r = returnRotation[0];
        }
        return returnRotation;
    }
}
