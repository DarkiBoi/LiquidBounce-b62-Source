// 
// Decompiled by Procyon v0.5.36
// 

package net.ccbluex.liquidbounce.features.module.modules.render;

import org.jetbrains.annotations.NotNull;
import net.ccbluex.liquidbounce.valuesystem.types.BoolValue;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.Module;

@ModuleInfo(name = "AntiBlind", description = "Cancels blindness effects.", category = ModuleCategory.RENDER)
@Metadata(mv = { 1, 1, 13 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006¨\u0006\u000b" }, d2 = { "Lnet/ccbluex/liquidbounce/features/module/modules/render/AntiBlind;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "confusionEffect", "Lnet/ccbluex/liquidbounce/valuesystem/types/BoolValue;", "getConfusionEffect", "()Lnet/ccbluex/liquidbounce/valuesystem/types/BoolValue;", "fireEffect", "getFireEffect", "pumpkinEffect", "getPumpkinEffect", "LiquidBounce" })
public final class AntiBlind extends Module
{
    @NotNull
    private final BoolValue confusionEffect;
    @NotNull
    private final BoolValue pumpkinEffect;
    @NotNull
    private final BoolValue fireEffect;
    
    @NotNull
    public final BoolValue getConfusionEffect() {
        return this.confusionEffect;
    }
    
    @NotNull
    public final BoolValue getPumpkinEffect() {
        return this.pumpkinEffect;
    }
    
    @NotNull
    public final BoolValue getFireEffect() {
        return this.fireEffect;
    }
    
    public AntiBlind() {
        this.confusionEffect = new BoolValue("Confusion", true);
        this.pumpkinEffect = new BoolValue("Pumpkin", true);
        this.fireEffect = new BoolValue("Fire", false);
    }
}
