// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.fun;

import net.ccbluex.liquidbounce.event.EventTarget;
import java.util.Random;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import net.ccbluex.liquidbounce.valuesystem.types.IntegerValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "SkinDerp", description = "Makes your skin blink (Requires multi-layer skin).", category = ModuleCategory.FUN)
public class SkinDerp extends Module
{
    private final IntegerValue delayValue;
    private final BoolValue hatValue;
    private final BoolValue jacketValue;
    private final BoolValue leftPantsValue;
    private final BoolValue rightPantsValue;
    private final BoolValue leftSleeveValue;
    private final BoolValue rightSleeveValue;
    private final MSTimer timer;
    
    public SkinDerp() {
        this.delayValue = new IntegerValue("Delay", 0, 0, 1000);
        this.hatValue = new BoolValue("Hat", true);
        this.jacketValue = new BoolValue("Jacket", true);
        this.leftPantsValue = new BoolValue("LeftPants", true);
        this.rightPantsValue = new BoolValue("RightPants", true);
        this.leftSleeveValue = new BoolValue("LeftSleeve", true);
        this.rightSleeveValue = new BoolValue("RightSleeve", true);
        this.timer = new MSTimer();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.timer.hasTimePassed(this.delayValue.asInteger())) {
            final Random random = RandomUtils.getRandom();
            if (this.hatValue.asBoolean()) {
                SkinDerp.mc.field_71474_y.func_178878_a(EnumPlayerModelParts.HAT, random.nextBoolean());
            }
            if (this.jacketValue.asBoolean()) {
                SkinDerp.mc.field_71474_y.func_178878_a(EnumPlayerModelParts.JACKET, random.nextBoolean());
            }
            if (this.leftPantsValue.asBoolean()) {
                SkinDerp.mc.field_71474_y.func_178878_a(EnumPlayerModelParts.LEFT_PANTS_LEG, random.nextBoolean());
            }
            if (this.rightPantsValue.asBoolean()) {
                SkinDerp.mc.field_71474_y.func_178878_a(EnumPlayerModelParts.RIGHT_PANTS_LEG, random.nextBoolean());
            }
            if (this.leftSleeveValue.asBoolean()) {
                SkinDerp.mc.field_71474_y.func_178878_a(EnumPlayerModelParts.LEFT_SLEEVE, random.nextBoolean());
            }
            if (this.rightSleeveValue.asBoolean()) {
                SkinDerp.mc.field_71474_y.func_178878_a(EnumPlayerModelParts.RIGHT_SLEEVE, random.nextBoolean());
            }
            this.timer.reset();
        }
    }
}
