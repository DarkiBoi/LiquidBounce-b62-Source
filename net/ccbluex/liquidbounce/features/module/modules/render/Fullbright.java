// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.PotionEffect;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.minecraft.potion.Potion;
import net.ccbluex.liquidbounce.valuesystem.types.ListValue;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "Fullbright", description = "Brightens up the world around you.", category = ModuleCategory.RENDER)
public class Fullbright extends Module
{
    private final ListValue modeValue;
    private float prevGamma;
    
    public Fullbright() {
        this.modeValue = new ListValue("Mode", new String[] { "Gamma", "NightVision" }, "Gamma");
        this.prevGamma = -1.0f;
    }
    
    @Override
    public void onEnable() {
        this.prevGamma = Fullbright.mc.field_71474_y.field_74333_Y;
    }
    
    @Override
    public void onDisable() {
        if (this.prevGamma == -1.0f) {
            return;
        }
        Fullbright.mc.field_71474_y.field_74333_Y = this.prevGamma;
        this.prevGamma = -1.0f;
        Fullbright.mc.field_71439_g.func_70618_n(Potion.field_76439_r.field_76415_H);
    }
    
    @EventTarget(ignoreCondition = true)
    public void onUpdate(final UpdateEvent event) {
        if (this.getState() || ModuleManager.getModule(XRay.class).getState()) {
            final String lowerCase = this.modeValue.asString().toLowerCase();
            switch (lowerCase) {
                case "gamma": {
                    if (Fullbright.mc.field_71474_y.field_74333_Y <= 100.0f) {
                        final GameSettings field_71474_y = Fullbright.mc.field_71474_y;
                        ++field_71474_y.field_74333_Y;
                        break;
                    }
                    break;
                }
                case "nightvision": {
                    Fullbright.mc.field_71439_g.func_70690_d(new PotionEffect(Potion.field_76439_r.field_76415_H, 1337, 1));
                    break;
                }
            }
        }
        else if (this.prevGamma != -1.0f) {
            Fullbright.mc.field_71474_y.field_74333_Y = this.prevGamma;
            this.prevGamma = -1.0f;
        }
    }
}
